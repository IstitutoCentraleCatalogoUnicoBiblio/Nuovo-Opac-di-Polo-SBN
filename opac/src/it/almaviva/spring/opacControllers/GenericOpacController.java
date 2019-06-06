package it.almaviva.spring.opacControllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import it.almaviva.boot.appVersion.Version;
import it.almaviva.boot.appVersion.VersioningReader;
import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.externalCall.wikipedia.dao.WikipediaClientDao;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.opac.services.MLOLClientServices;
import it.almaviva.opac.services.MailSenderServices;
import it.almaviva.opac.services.PoloCachedManager;
import it.almaviva.opac.services.PropertiesLoader;
import it.almaviva.opac.services.SolrQueryExecuteServices;
import it.almaviva.opac.services.WikipediaClientServices;
import it.almaviva.pssql.bean.Biblio;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.utils.opac.ServerStatusBean;
import it.medialibrary.api.RicercaResponseRicercaResultMedia;

public class GenericOpacController {
	static Logger log = Logger.getLogger(GenericOpacController.class);
	GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();
	Version version = VersioningReader.getVersion();
	@Autowired
	PoloCachedManager cachedPoli;
	@Autowired
	InterrogazioneOpacDBServices dbPostgresManager;
	@Autowired
	SolrQueryExecuteServices queryMaker = new SolrQueryExecuteServices();

	@Autowired
	WikipediaClientServices wikiManager = new WikipediaClientServices();

	@Autowired
	MLOLClientServices mlolSearch = new MLOLClientServices();
	APISearchDocumentController searchController = new APISearchDocumentController();
	private String dummyObj = "undefined";
	private String dummyJsonObj = new GsonBuilder().create().toJson(dummyObj);
	
	public ModelAndView reloadCache() {
		cachedPoli.update();
		MailSenderServices.create();
		WikipediaClientDao.loadProxyConf();
		PropertiesLoader.reload();
		return getListPolo();
	}
	
	private JSONObject poloJSON(Polo polo) {
		JSONObject resp = new JSONObject();
		ServerStatusBean serverStatus = serverStatusMaker.generateServerStatus(polo);
		// generate richiesta
		if (!(polo == null)) {
			try {
				JSONObject poloJson = cachedPoli.poloToJson(polo);
				resp.put("polo", poloJson);
			} catch (JSONException | NullPointerException e) {
				e.printStackTrace();
				log.info("Attenzione, il database non contiene tutte le informazioni necessarie.", e);
			}
		}
		resp.put("serverStatus", ServerStatusBean.toJson(serverStatus));

		return resp;
	}

	private JSONObject pololistJSON() throws SQLException {
		List<Polo> poli = cachedPoli.getPoli();
		if (poli.size() == 1)
			return poloJSON(poli.get(0));

		JSONObject resp = new JSONObject();
		JSONObject serv = new JSONObject();
		ServerStatusBean serverStatus = serverStatusMaker.generateServerStatus(poli);

		serv.put("code", serverStatus.getCode());
		serv.put("message", serverStatus.getMessage());
		serv.put("error", serverStatus.getError());
		// generate richiesta
		resp.put("poli", cachedPoli.getJsonPoli());
		resp.put("serverStatus", serv);

		return resp;
	}

	// seleziona il polo di ritorno deve essere sostituito con la chiamata al DB
	public Polo checkPolo(String poloStr) {
		try {
			return cachedPoli.getPolo(poloStr);
		} catch (Exception e) {
			log.error("Error checking polo", e);
			return null;

		}

	}
	public String checkCodPolo(String codPolo) {
		String redirectUrl = new String();
		if (codPolo.trim().toLowerCase().length() == 3)

			redirectUrl = "/" + codPolo.toUpperCase();

		return redirectUrl;
	}
	public String checkCodBib(String codPolo, String codBib) {
		String redirectUrl = new String();
			redirectUrl = "/" + codPolo.trim().toUpperCase() + "/" + codBib.trim().toUpperCase() ;

		return redirectUrl;
	}
	public ModelAndView getOpacPolo(String codPolo) {
		if(codPolo == null)
			codPolo = "";
		codPolo = codPolo.toUpperCase();
		// Controllo del controller e dei dati ricevuti in post
		log.info("getOpacPolo: " + codPolo);

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("opac");

		Polo poloObj = checkPolo(codPolo);
		// Creo il responseJSON
		//log.info("Returning JSON");

		mv.addObject("response", poloJSON(poloObj));
		mv.addObject("searchResult", dummyJsonObj);
		mv.addObject("opac_version", version.toJson());
		mv.addObject("isOCNSearch", false);

		return mv;
	}
	public ModelAndView getOpacBiblioteca(String codPolo,  String codBib) {

		// Controllo del controller e dei dati ricevuti in post
		log.info("getOpacBiblioteca: " + codPolo + codBib);

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("opac");
		Polo poloObj = checkPolo(codPolo);
		
		// Creo il responseJSON
		//log.info("Returning JSON");
		Biblio b = poloObj.allLibraries().stream().filter(biblioteca -> biblioteca.getCod_bib().trim().equals(codBib.trim())).findFirst().orElse(null);
		List<Biblio> bibs = new ArrayList<Biblio>();
		bibs.add(b);
		poloObj.setLibraries(bibs);
		
		mv.addObject("response", poloJSON(poloObj));
		mv.addObject("searchResult", dummyJsonObj);
		mv.addObject("opac_version", version.toJson());
		mv.addObject("isOCNSearch", false);

		return mv;
	}

	public Map<String, String> buildSearchFilterMap(String searchText, String field) {
		Map<String, String> filters = new HashMap<>();
		filters.put(field, searchText);
		return filters;
	}
	//avvia la ricerca diretta
	public JSONObject search(Map<String, String> filters, String cod_polo) {
		
		SearchBean searchBean = SearchBean.buildSearchBeanByMap(filters);
		
		//eseguo la ricerca 
		SolrResponseBean solr = queryMaker.searchRun(searchBean, cod_polo);
		ServerStatusBean server = serverStatusMaker.generateServerStatus(solr);
		RicercaResponseRicercaResultMedia mlol = mlolSearch.ricerca(cod_polo, searchBean);
		Object wikipediaApi = wikiManager.callWikipediaApi(cod_polo, searchBean);
		return searchController.generateJSONResponse(searchBean, solr, mlol, wikipediaApi, server);
	}
	public ModelAndView getListPolo() {

		// Controllo del controller e dei dati ricevuti in post
		log.info("GetPolo List");

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("opac");

		// Creo il responseJSON
		try {
			//log.info("Returning JSON");
			JSONObject generateJSONPolos = pololistJSON();
			mv.addObject("response", generateJSONPolos);
			mv.addObject("searchResult", dummyObj);
			mv.addObject("opac_version", version.toJson());
			mv.addObject("isOCNSearch", false);

		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			return null;
		}

		return mv;
	}

}
