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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
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

import it.almaviva.opac.bean.ricerca.result.CoreType;
import it.almaviva.opac.bean.ricerca.ricerca.SearchTerm;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.SolrQueryExecuteServices;
import it.almaviva.utils.opac.ServerStatusBean;

/**
 * <h2>Controller di ricerca Termini</h2> Classe che gestisce gli url per la
 * ricerca dei termini
 * 
 * @see SpringFramework
 * 
 * 
 */
@Controller
public class APISearchTermsController {
	static Logger log = Logger.getLogger(APISearchTermsController.class);
	@Autowired
	SolrQueryExecuteServices sqedi;
	GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();

	/**
	 * Il metodo ritorna una lista di termini di una ricerca all'interno di un
	 * polo selezionato all'interno di un json costruito all'interno di una
	 * ModelAndView</br>
	 * RequestMapping: "/api/{polo}/search/terms"
	 * 
	 * @param SearchTerm
	 * @see SearchTerm
	 * @param polo
	 * 
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/{polo}/search/terms", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView processSubmit(@RequestBody SearchTerm search, @PathVariable String polo,
			HttpServletRequest request) {

		// Controllo del controller e dei dati ricevuti in post
		log.info("Controller: " + search.toString());
		List<Term> termini = sqedi.searchTerms(search, polo, CoreType.BIBLIO);
		ServerStatusBean server = serverStatusMaker.generateTermsServerStatus(termini);
		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("response");

		// Creo il responseJSON
		log.info("Returning JSON");
		mv.addObject("response", generateJSONResponse(search, termini, server));
		return mv;
	}

	private JSONObject generateJSONResponse(SearchTerm request, List<Term> solrResponse,
			ServerStatusBean serverStatus) {

		JSONObject resp = new JSONObject();
		JSONObject solrDocs = new JSONObject();
		JSONObject serv = new JSONObject();
		JSONObject req = new JSONObject();

		req.put("field", request.getField());
		req.put("value", request.getValue());
		serv.put("code", serverStatus.getCode());
		serv.put("message", serverStatus.getMessage());
		serv.put("error", serverStatus.getError());
		if (!(solrResponse == null)) {
			solrDocs.put("documenti", solrResponse);

			solrDocs.put("numFound", solrResponse.size());

		} else {
			solrDocs = null;
		}
		resp.put("serverStatus", serv);
		resp.put("request", req);
		resp.put("solrDocs", solrDocs);

		return resp;
	}
}
