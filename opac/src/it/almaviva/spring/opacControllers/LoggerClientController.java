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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.utils.opac.LoggerClientBean;

/**
 * <h2>Controller di Log</h2> Classe che gestisce il log su file del lato client
 * 
 * @see SpringFramework
 * 
 * 
 */
//@Controller
//Non usata, era un test per loggare su server quello che avveniva su client
public class LoggerClientController {
	private static Logger log = Logger.getLogger(LoggerClientController.class);
	private final String SPACE = " ";

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/log/client/", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView processSubmit(@RequestBody LoggerClientBean toLog, HttpServletRequest request) {

		// Controllo del controller e dei dati ricevuti in post
		log.info("LoggerClientController");

		// Creo il responseJSON
		log.info(logStrBuild(toLog));
		ModelAndView mv = new ModelAndView("response");
		mv.addObject("response", null);
		return mv;
	}

	private String logStrBuild(LoggerClientBean toLog) {

		StringBuilder strBuilder = new StringBuilder(30);

		strBuilder.append("Logginngig client: ")
			.append(toLog.getTimestamp())
			.append(SPACE)
			.append(toLog.getPolo())
			.append(SPACE)
			.append(toLog.getFileJs())
			.append(SPACE + ":" + SPACE)
			.append(toLog.getRiga())
			.append("\n")
			.append("Testo: ")
			.append(toLog.getText());
		
		return strBuilder.toString();
	}
}
