/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.almaviva.spring.opacControllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import it.almaviva.boot.appVersion.Version;
import it.almaviva.boot.appVersion.VersioningReader;
import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.opac.services.MLOLClientServices;
import it.almaviva.opac.services.PoloCachedManager;
import it.almaviva.opac.services.SolrQueryExecuteServices;
import it.almaviva.opac.services.WikipediaClientServices;
import it.almaviva.pssql.bean.Biblio;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.utils.opac.ServerStatusBean;
import it.medialibrary.api.RicercaResponseRicercaResultMedia;

/**
 *         <h2>Controller del Polo</h2> Classe che gestisce gli url per ottenere
 *         una lista di poli o il dettaglio di polo singolo
 * @see Spring Framework
 */
public class OpacViewController {
	static Logger log = Logger.getLogger(OpacViewController.class);
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
	// costruisce il json di ritorno
	private JSONObject generateJSONResponse(Polo polo) {
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

	private JSONObject generateJSONPolos() throws SQLException {
		List<Polo> poli = cachedPoli.getPoli();
		if (poli.size() == 1)
			return generateJSONResponse(poli.get(0));
		
	
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
	private Polo checkPolo(String poloStr) {
		try {
			return cachedPoli.getPolo(poloStr);
		} catch (Exception e) {
			log.error("Error checking polo", e);
			return null;

		}

	}

	/**
	 * @param polo
	 * @return ModelAndview
	 * 
	 *         Il metodo ritorna il dettaglio del singolo polo selezionato
	 *         all'interno di un json costruito all'interno di una ModelAndView</br>
	 *         RequestMapping: "/api/getPolo/{polo}"
	 */
	@CrossOrigin(origins = "*")
	// ritorna in output i dati del singolo polo
	public ModelAndView getSinglePolo(@PathVariable String polo) {
		if(polo == null)
			polo = "";
		polo = polo.toUpperCase();
		// Controllo del controller e dei dati ricevuti in post
		log.info("GetPolo Impostazioni id: " + polo);

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("opac");

		Polo poloObj = checkPolo(polo);
		// Creo il responseJSON
		log.info("Returning JSON");

		//mv.addObject("response", generateJSONResponse(poloObj));
		mv.addObject("response", generateJSONResponse(poloObj));
		mv.addObject("searchResult", dummyJsonObj);
		mv.addObject("opac_version", version.toJson());

		return mv;
	}

	//@RequestMapping(value = "/{polo:[3]*[A-Z|0-9]+}/{cod_bib:[2]*[A-Z|0-9]+}")
	public ModelAndView getSingleBib(@PathVariable String polo, @PathVariable String cod_bib) {

		// Controllo del controller e dei dati ricevuti in post
		log.info("GetPolo Impostazioni id: " + polo + cod_bib);

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("opac");
		Polo poloObj = checkPolo(polo);
		
		// Creo il responseJSON
		log.info("Returning JSON");
		Biblio b = poloObj.allLibraries().stream().filter(biblioteca -> biblioteca.getCod_bib().trim().equals(cod_bib.trim())).findFirst().orElse(null);
		List<Biblio> bibs = new ArrayList<Biblio>();
		bibs.add(b);
		poloObj.setLibraries(bibs);
		
		mv.addObject("response", generateJSONResponse(poloObj));
		mv.addObject("searchResult", dummyJsonObj);
		mv.addObject("opac_version", version.toJson());

		return mv;
	}

	/**
	 * @return ModelAndView Il metodo ritorna la lista dei poli all'interno di un
	 *         json costruito all'interno di una ModelAndView</br>
	 *         RequestMapping: "/api/getPolo/{polo}"
	 */
	@CrossOrigin(origins = "*")

	// ritorna in output i dati del singolo polo
	@RequestMapping(value = "/")
	public ModelAndView getListPolo() {

		// Controllo del controller e dei dati ricevuti in post
		log.info("GetPolo List");

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("opac");

		// Creo il responseJSON
		try {
			log.info("Returning JSON");
			// return generateJSONPolos();
			JSONObject generateJSONPolos = generateJSONPolos();
			mv.addObject("response", generateJSONPolos);
			mv.addObject("searchResult", dummyObj);
			mv.addObject("opac_version", version.toJson());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
			e.printStackTrace();
			return null;
		}

		return mv;
	}
	//Dettaglio del documento
	@GetMapping("/{polo}/dettaglio/documento/{bid}")
	public ModelAndView detailDocument(@PathVariable String polo) {
		return getSinglePolo(polo);
	}

	@GetMapping("/{polo}/dettaglio/autore/{bid}")
	public ModelAndView detailAuthor(@PathVariable String polo) {
		return getSinglePolo(polo);
	}
	
	
	
	
	private JSONObject search(Map<String, String> filters, String cod_polo) {
	
		SearchBean searchBean = SearchBean.buildSearchBeanByMap(filters);
		
		//eseguo la ricerca 
		SolrResponseBean solr = queryMaker.searchRun(searchBean, cod_polo);
		ServerStatusBean server = serverStatusMaker.generateServerStatus(solr);
		RicercaResponseRicercaResultMedia mlol = mlolSearch.ricerca(cod_polo, searchBean);
		Object wikipediaApi = wikiManager.callWikipediaApi(cod_polo, searchBean);
		return searchController.generateJSONResponse(searchBean, solr, mlol, wikipediaApi, server);
	}
	
	
	
	@RequestMapping(value = "/{polo:[3]*[A-Z|0-9]+}")
	public ModelAndView ricerca(@RequestParam(value = "search", defaultValue = "") String searchText, @PathVariable String polo) {
		ModelAndView view = getSinglePolo(polo);
		if(searchText == null || "".equals(searchText)) {
			return view;

		}
		Map<String, String> filters = new HashMap<>();
			filters.put("any", searchText);
		JSONObject searchResult = search(filters, polo);
		
		view.addObject("searchResult", searchResult);

		log.info("Tentativo di ricerca: " + searchText);
		return view;
	}
	@RequestMapping(value = "/{polo:[3]*[A-Z|0-9]+}/{cod_bib:[2]*[A-Z|0-9]+}")
	public ModelAndView ricercaFiltroBib(@RequestParam(value = "search", defaultValue = "") String searchText, @PathVariable String polo, @PathVariable String cod_bib) {
		ModelAndView view = getSingleBib(polo, cod_bib);
		if(searchText == null || "".equals(searchText)) {
			return view;
		}
		Map<String, String> filters = new HashMap<>();
			filters.put("any", searchText);
			filters.put("library", cod_bib);
		JSONObject searchResult = search(filters, polo);
		
		view.addObject("searchResult", searchResult);

		log.info("Tentativo di ricerca: " + searchText);
		return view;
	}

}
