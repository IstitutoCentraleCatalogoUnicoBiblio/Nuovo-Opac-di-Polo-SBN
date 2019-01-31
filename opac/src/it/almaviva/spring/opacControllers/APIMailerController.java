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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.mailer.MailBean;
import it.almaviva.opac.services.MailSenderServices;
import it.almaviva.utils.opac.ServerStatusBean;

/**
 * 
 *         <h2>Controller Mailer</h2> Classe che gestisce gli url per spedire
 *         via mail
 * @see Spring Framework
 */
@Controller
public class APIMailerController {
	static Logger log = Logger.getLogger(APIMailerController.class);

	@CrossOrigin(origins = "*")
	// ritorna in output i dati del singolo polo

	@RequestMapping(value = "/api/{polo}/mail", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ModelAndView mail(@RequestBody MailBean recipient, @PathVariable String polo) {
		log.info("Mailer Controller!");

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("response");

		// invio email
		MailSenderServices mailServ = new MailSenderServices();
		ServerStatusBean server = mailServ.sendMail(recipient);

		// Creo il responseJSON
		log.info("Returning JSON");

		mv.addObject("response", generateJson(recipient, server));

		return mv;
	}

	@RequestMapping(value = "/api/mail/test/{mail:.+}")
	public ModelAndView mailTest(@PathVariable String mail) {
		log.info("Mailer Controller!");

		// Creazione della view di response.jsp
		ModelAndView mv = new ModelAndView("response");

		MailBean recipient = new MailBean();

		recipient.setText("Ciao Utente, l'opac invia anche le mail!");
		recipient.setTo(mail);
		recipient.setHtml("<h1>Bla bla bla questo è un testo </i>html</i></h1>");

		MailSenderServices mailer = new MailSenderServices();

		// Creo il responseJSON
		log.info("Returning JSON");

		mv.addObject("response", generateJson(recipient, mailer.sendMail(recipient)));

		return mv;
	}

	private JSONObject generateJson(MailBean recipient, ServerStatusBean serverStatus) {
		JSONObject resp = new JSONObject();
		JSONObject req = new JSONObject();
		JSONObject serv = new JSONObject();
		serv.put("code", serverStatus.getCode());
		serv.put("message", serverStatus.getMessage());
		serv.put("error", serverStatus.getError());

		req.put("to", recipient.getTo());
		req.put("from", "noreply-sbnweb@almaviva.it");
		req.put("text", recipient.getText());
		req.put("subject", recipient.getSubject());

		resp.put("request", req);
		resp.put("serverStatus", serv);
		return resp;
	}
}
