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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.externalCall.wikipedia.dao.WikipediaClientDao;
import it.almaviva.opac.services.MailSenderServices;

/**
 * <h2>Controller di mapping</h2> Classe che gestisce gli url per il
 * re-indirizzamento in base al codice del polo o biblioteca selezionati
 * 
 * @see SpringFramework
 */

@Controller
public class MappingSinglePolo extends OpacViewController {
	private static Logger log = Logger.getLogger(MappingSinglePolo.class);

	/**
	 * Metodo che conttrolla il polo e genera il redirect
	 * 
	 * @param codice
	 * @return redirect
	 */
	@CrossOrigin(origins = "*")

	/**
	 * Metodo che conttrolla il polo in ricerca semplice e genera la pagina come l'index
	 * 
	 * @param codice
	 * @return redirect "/#!/polo/poloInput
	 */
	@RequestMapping(value = {"/{codice}/ricercaSemplice",
			"/{codice}/contatti",
			"/{codice}/ricercaAvanzata",
			"/{codice}/authority",
			"/{codice}/info/polo",
			"/{codice}/info/biblioteche",
			})
	public ModelAndView processPageCanSeeImmediatly(@PathVariable String codice) {
		log.info("Caricamento pagina diretta polo: " + codice);
		
		return getSinglePolo(codice);
	}
	@RequestMapping(value = {"/{codice}/result",
							 "/{codice}/preferiti",
							 "/{codice}/modifica"})
	public String processResult(@PathVariable String codice) {
		log.info("Redirecting polo " + codice);
		return "redirect:" + checkPolo(codice.toUpperCase());
	}
	@RequestMapping(value = {"/reload","/restart"})
	public ModelAndView reloadCache() {
		cachedPoli.update();
		MailSenderServices.create();
		WikipediaClientDao.loadProxyConf();
		return getListPolo();
	}
	/**
	 * Metodo che controlla l'esistenza effettiva di un polo o una biblioteca
	 * 
	 * @param cod
	 * @return urlRedirectString
	 */
	private String checkPolo(String cod) {
		String redirectUrl = new String();
		if (cod.trim().toLowerCase().length() == 3)

			redirectUrl = "/" + cod.toUpperCase();

		return redirectUrl;
	}

}
