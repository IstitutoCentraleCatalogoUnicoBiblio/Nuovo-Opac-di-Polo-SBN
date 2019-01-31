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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.almaviva.opac.externalCall.sbnweb.PossedutoJson;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.SbnWebServiceServices;
import it.almaviva.utils.opac.ServerStatusBean;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

@Controller
public class APISbnWebController {

	static Logger log = Logger.getLogger(APISbnWebController.class);

	@Autowired
	SbnWebServiceServices dispServices;

	GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();

	@CrossOrigin(origins = "*")
	// @RequestMapping(value="/api/services/kardex/{polo:[3]*[A-Z|0-9]+}/{biblioteca:[2]*[A-Z|0-9]+}/{id:[10]*[A-Z|0-9]+}")
	@RequestMapping(value = "/api/services/kardex/{polo:[3]*[A-Z|0-9]+}/{biblioteca:[2]*[A-Z|0-9]+}/{id:[10]*[A-Z|0-9]+}/{inventario}")
	public ModelAndView load(@PathVariable String polo, @PathVariable String biblioteca, @PathVariable String id,
			@PathVariable String inventario) {
		log.info("Controller dei posseduti ");
		SbnwebType sbn = dispServices.fascicoli(polo, biblioteca, id, inventario);
		ServerStatusBean server = serverStatusMaker.generateServerStatusForPosseduto(sbn);
		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("response");
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(new PossedutoJson(sbn, server, polo, biblioteca, id, inventario));
		// Creo il responseJSON
		log.info("Returning JSON");

		mv.addObject("response", json);
		return mv;

	}

	@RequestMapping(value = "/api/services/posseduto/{polo:[3]*[A-Z|0-9]+}/{biblioteca:[2]*[A-Z|0-9]+}/{id:[10]*[A-Z|0-9]+}")
	public ModelAndView loadcomplete(@PathVariable String polo, @PathVariable String biblioteca,
			@PathVariable String id) {
		log.info("Controller dei posseduti ");
		SbnwebType sbn = dispServices.disponibilita(polo, id, biblioteca);
		// log.info(sbn);

		ServerStatusBean server = serverStatusMaker.generateServerStatusForPosseduto(sbn);
		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("response");

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(new PossedutoJson(sbn, server, polo, biblioteca, id, null));
		// Creo il responseJSON
		log.info("Returning JSON");

		mv.addObject("response", json);
		return mv;

	}

}
