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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.DateNovitaType;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.utils.opac.ServerStatusBean;
import it.almaviva.utils.opac.Util;

@Controller
public class APIDocumentGetNovita extends APISearchDocumentController {
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/{polo}/search/novita/{date}", method = RequestMethod.GET)
	public ModelAndView runRequest(@PathVariable String polo, @PathVariable String date) {
		
		return processNovitaRequest(polo, date);
	}
	@RequestMapping(value = "/api/{polo}/search/novita", method = RequestMethod.GET)
	public ModelAndView runRequestNoDate(@PathVariable String polo, @PathVariable String date) {
		return processNovitaRequest(polo, DateNovitaType.LAST_MONTH.toString());
	}
	
	private ModelAndView processNovitaRequest(String polo, String date) {
		ModelAndView mv = new ModelAndView("response");
		date = Util.checkNovitaDate(date);
		SolrResponseBean solr = queryMaker.novita(polo, date);
		ServerStatusBean server = serverStatusMaker.generateServerStatus(solr);
		mv.addObject("response", super.generateJSONResponse(SearchBean.getNovita(date), solr, null, null, server));
		return mv;
	}
	
}
