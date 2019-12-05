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

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.bean.ricerca.result.CoreType;
import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.GroupFilters;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.MLOLClientServices;
import it.almaviva.opac.services.SolrQueryExecuteServices;
import it.almaviva.opac.services.WikipediaClientServices;
import it.almaviva.opac.solr.SolrQueryBuilder;
import it.almaviva.utils.opac.ServerStatusBean;
import it.almaviva.utils.opac.Util;
import it.medialibrary.api.RicercaResponseRicercaResultMedia;

/**
 * <h2>Controller di ricerca</h2> Classe che gestisce gli url per la ricerca dei
 * documenti
 * 
 * @see SpringFramework
 * 
 * 
 */
//@Controller
public class APISearchDocumentController {

	private static Logger log = Logger.getLogger(APISearchDocumentController.class);
	@Autowired
	SolrQueryExecuteServices queryMaker = new SolrQueryExecuteServices();
	GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();

	@Autowired
	WikipediaClientServices wikiManager = new WikipediaClientServices();

	@Autowired
	MLOLClientServices mlolSearch = new MLOLClientServices();


	@CrossOrigin(origins = "*")
	

	/**
	 * Il metodo ritorna il risultato di una ricerca all'interno di un polo
	 * selezionato all'interno di un json costruito all'interno di una
	 * ModelAndView</br>
	 * RequestMapping: "/api/{polo}/search/documents"
	 * 
	 * @param search
	 * @see Search
	 * @param polo
	 * @return ModelAndview
	 * 
	 */
	@RequestMapping(value = "/api/{polo}/search/documents", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView runRequest(@RequestBody SearchBean search, @PathVariable String polo) {

		// Controllo del controller e dei dati ricevuti in post
		log.info("--> inizio ricerca x Polo " + polo);
		SolrResponseBean solr = queryMaker.searchRun(search, polo);
		RicercaResponseRicercaResultMedia mlol = new RicercaResponseRicercaResultMedia();
		Object wikipediaResult = new Object();
		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("response");

		ServerStatusBean server = serverStatusMaker.generateServerStatus(solr);
		// MLOL obj
		if (!search.getIsDetail()) {
			mlol = mlolSearch.ricerca(polo, search);
			wikipediaResult = wikiManager.callWikipediaApi(polo, search);
		} else {
			mlol.setStatus("detail_document");
			wikipediaResult = "";
		}

		// Creo il responseJSON
		log.info("--> fine ricerca Returning JSON");

		mv.addObject("response", generateJSONResponse(search, solr, mlol, wikipediaResult, server));

		return mv;
	}

	protected JSONObject generateJSONResponse(SearchBean request, SolrResponseBean solrResponse,
			RicercaResponseRicercaResultMedia mlol, Object wikipediaResult, ServerStatusBean serverStatus) {
		// creo un oggetto JSON
		JSONObject resp = new JSONObject();
		JSONObject solrDocs = new JSONObject();
		JSONObject serv = new JSONObject();
		JSONObject ric = new JSONObject();
		JSONObject mlolObjson = new JSONObject();
		JSONObject mlolStatus = new JSONObject();
		JSONObject mlolObj = new JSONObject();
		JSONObject groupfilter = new JSONObject();
	//	JSONObject wikipedia = new JSONObject();
		ric.put("maxRows", request.getMaxRows());
		ric.put("start", request.getStart());
		ric.put("filters", groupfilter);
		ric.put("isDetail", request.getIsDetail());
		ric.put("order", request.getOrder());

		ric.put("sort", Util.normalizeStort(request.getSort()));
		try {
			GroupFilters group = new GroupFilters();
			group = request.getFilters();
			group = Util.andwordFilters(group);
			groupfilter.put("filters", group.getFilters());

			SolrQueryBuilder q = new SolrQueryBuilder();
			log.debug("QUERY REBUILDING FOR RETURN JSON test");
			q.queryBuilder(group, CoreType.BIBLIO);

			// groupfilter.put("operator", group.getOperator());
			// generate richiesta
			
		} catch (NullPointerException e1) {
			log.error(e1);
		}

		// generate server status
		serv.put("code", serverStatus.getCode());
		serv.put("message", serverStatus.getMessage());
		serv.put("error", serverStatus.getError());
		// setto le propriet�

		if (!(solrResponse == null)) {
			if (request.getIsDetail() == false) {
				solrDocs.put("documenti", solrResponse.getSintetica());
			} else {
				solrDocs.put("documenti", solrResponse.getDocs());
			}

			solrDocs.put("numFound", solrResponse.getNumFound());
			solrDocs.put("faccette", solrResponse.getFacc());
			solrDocs.put("query", solrResponse.getQuery());
		} else {
			solrDocs = null;
		}
		try {
			// MLOL Structure
			if (mlol != null) {
				mlolObjson.put("tipologie", mlol.getTipologia());
				mlolObjson.put("ndocs", mlol.getTotale());
				mlolStatus.put("mlolStatus", mlol.getStatus());
				mlolStatus.put("mlolStato", mlol.getStato());
				mlolObj.put("documents", mlolObjson);
				mlolObj.put("serverStatus", mlolStatus);
			}
			
		} catch (NullPointerException e) {
			mlolObj.put("error", "is_detail_error_null_object");
			log.error("NullPointerController mlol returned null");
			log.error(e);
		}

		// ritorno dei miei oggetti
		if (wikipediaResult == null) {
			JSONObject wikipediaErrorNull = new JSONObject();
			wikipediaErrorNull.put("error", "is_detail_error_null_object");
			resp.put("wikiDocs", wikipediaErrorNull);
		} else {
			resp.put("wikiDocs", wikipediaResult);
		}
		// resp.put("wikiDocs", wikipediaResult);
		resp.put("solrDocs", solrDocs);
		resp.put("mlolDocs", mlolObj);
		resp.put("serverStatus", serv);
		resp.put("request", ric);
		// log.info("Response: "+ resp.toString());
		return resp;
	}

}
