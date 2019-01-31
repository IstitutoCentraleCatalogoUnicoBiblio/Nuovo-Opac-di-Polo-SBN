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
package it.almaviva.spring.opacAmministrazione;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.bean.amministrazione.GruppoModel;
import it.almaviva.opac.services.AmministrazioneOpacDBServices;
import it.almaviva.opac.services.PoloCachedManager;
import it.almaviva.pssql.bean.Polo;

@Controller
public class APIGruppi {
	static Logger log = Logger.getLogger(APIGruppi.class);
	@Autowired
	PoloCachedManager cachedPoli;
		
	@Autowired
	AmministrazioneOpacDBServices ammDbManager ;

	@RequestMapping(value = "/api/{codPolo}/admin/gruppi/insert", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView insertGruppo(@PathVariable String codPolo, @RequestBody GruppoModel toInsert) {
		log.info("inserendo gruppo " + toInsert.getName() + " per " + codPolo);
		ModelAndView model = new ModelAndView("response");
		Polo polo = ammDbManager.insertGruppo(toInsert);
		model.addObject("response", APIPolo.generateAdminResponse(polo));
		cachedPoli.update();
		return model;
	}
	
	@RequestMapping(value = "/api/{codPolo}/admin/gruppi/update", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView update(@PathVariable String codPolo, @RequestBody GruppoModel toUpdate) {
		log.info("agiornando gruppo " + toUpdate.getName() + " per " + codPolo);
		ModelAndView model = new ModelAndView("response");
		Polo polo = ammDbManager.updateGruppo(toUpdate);
		model.addObject("response", APIPolo.generateAdminResponse(polo));
		cachedPoli.update();
		return model;
	}
	@RequestMapping(value = "/api/{codPolo}/admin/gruppi/delete", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView delete(@PathVariable String codPolo, @RequestBody GruppoModel toDelete) {
		log.info("inserendo gruppo " + toDelete.getName() + " per " + codPolo);
		ModelAndView model = new ModelAndView("response");
		Polo polo = ammDbManager.deleteGruppo(toDelete);
		model.addObject("response", APIPolo.generateAdminResponse(polo));
		cachedPoli.update();
		return model;
	}

}
