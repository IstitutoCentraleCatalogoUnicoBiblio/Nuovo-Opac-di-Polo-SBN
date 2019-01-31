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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.testng.log4testng.Logger;

import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.pssql.bean.Polo;

@Controller
public class AmministrazioneIndex {
	private Logger log = Logger.getLogger(AmministrazioneIndex.class);
	
	@Autowired
	InterrogazioneOpacDBServices dbPostgresManager = new InterrogazioneOpacDBServices();

	@RequestMapping("/{codPolo}/admin/login")
	public ModelAndView amministrazioneIndex (@PathVariable String codPolo) {
		ModelAndView view = new ModelAndView("opac_amministrazione");
		log.info("Loading Home amministrazione per polo " + codPolo);
		view.addObject("codPolo", codPolo);
		try {
			Polo singlePolo = dbPostgresManager.getSinglePolo(codPolo);
			view.addObject("polo", singlePolo);
			//view.addObject("logged", ammDbManager.isValidUser(usr));
		} catch (Exception e) {
			log.info("Impossibile connettersi al db", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			Polo p = new Polo();
			p.setCode(codPolo);
			view.addObject("polo", p);
		}
		
		return view;
	}
	
	@RequestMapping("/{codPolo}/admin")
	public ModelAndView redirectLogin(@PathVariable String codPolo) {
	 return amministrazioneIndex(codPolo);
	}
	@RequestMapping("/{codPolo}/amministrazione")
	public ModelAndView redirect(@PathVariable String codPolo) {
	 return amministrazioneIndex(codPolo);
	}

	@RequestMapping("/{codPolo}/login")
	public ModelAndView redirectLoginNoAdmin(@PathVariable String codPolo) {
	 return amministrazioneIndex(codPolo);
	}

}
