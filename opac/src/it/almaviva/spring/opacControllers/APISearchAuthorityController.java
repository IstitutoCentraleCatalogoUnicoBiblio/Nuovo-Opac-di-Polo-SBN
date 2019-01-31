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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.GroupFilters;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.services.ConverterCampiServices;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.SolrQueryExecuteServices;
import it.almaviva.utils.opac.ServerStatusBean;
import it.almaviva.utils.opac.Util;

/**
 * <h2>Controller di ricerca autori</h2> Classe che gestisce gli url per la
 * ricerca degli autori
 * 
 * @see SpringFramework
 * 
 * 
 */
@Controller
public class APISearchAuthorityController {

	static Logger log = Logger.getLogger(APISearchAuthorityController.class);
	@Autowired
	SolrQueryExecuteServices queryMaker;
	ConverterCampiServices converter = new ConverterCampiServices();
	GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();

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
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/{polo}/search/authority", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView runRequest(@RequestBody SearchBean search, @PathVariable String polo) {

		// Controllo del controller e dei dati ricevuti in post
		log.info("Controller AUTH: " + search.toString() + "Polo" + polo);
		SolrResponseBean solr = queryMaker.searchAuth(search, polo);
		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("response");
		ServerStatusBean server = serverStatusMaker.generateServerStatus(solr);

		// Creo il responseJSON
		log.info("Returning JSON");
		mv.addObject("response", generateJSONResponse(search, solr, server));

		return mv;
	}

	private JSONObject generateJSONResponse(SearchBean request, SolrResponseBean solrResponse,
			ServerStatusBean serverStatus) {
		// creo un oggetto JSON
		JSONObject resp = new JSONObject();
		JSONObject solrDocs = new JSONObject();
		JSONObject serv = new JSONObject();
		JSONObject ric = new JSONObject();
		JSONObject groupfilter = new JSONObject();
		GroupFilters group = new GroupFilters();
		group = request.getFilters();
		group = Util.andwordFilters(group);
		groupfilter.put("filters", group.getFilters());
		groupfilter.put("operator", group.getOperator());
		// generate richiesta
		ric.put("maxRows", request.getMaxRows());
		ric.put("start", request.getStart());
		ric.put("filters", groupfilter);
		ric.put("isDetail", request.getIsDetail());
		ric.put("order", request.getOrder());

		ric.put("sort", Util.normalizeStort(request.getSort()));

		// generate server status
		serv.put("code", serverStatus.getCode());
		serv.put("message", serverStatus.getMessage());
		serv.put("error", serverStatus.getError());
		// setto le proprietà

		if (!(solrResponse == null)) {

			solrDocs.put("documenti", solrResponse.getAutori());

			solrDocs.put("numFound", solrResponse.getNumFound());
			solrDocs.put("faccette", solrResponse.getFacc());
			solrDocs.put("query", solrResponse.getQuery());
		} else {
			solrDocs = null;
		}

		// ritorno dei miei oggetti
		resp.put("solrDocs", solrDocs);
		resp.put("serverStatus", serv);
		resp.put("request", ric);
		// log.info("Response: "+ resp.toString());
		return resp;
	}

}
