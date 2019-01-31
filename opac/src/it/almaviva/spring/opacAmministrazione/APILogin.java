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

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.testng.log4testng.Logger;

import it.almaviva.opac.bean.amministrazione.LoginBean;
import it.almaviva.opac.services.AmministrazioneOpacDBServices;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.pssql.bean.User_opac;
import it.almaviva.utils.opac.ServerStatusBean;

@Controller
public class APILogin {
	 static Logger log = Logger.getLogger(APILogin.class);
	private GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();
	@Autowired
	InterrogazioneOpacDBServices dbPostgresManager;
	@Autowired
	AmministrazioneOpacDBServices ammDbManager;

	
	@RequestMapping(value = "/api/{codPolo}/admin/login", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView login(@PathVariable String codPolo, @RequestBody LoginBean user) {
		ModelAndView model = new ModelAndView("response");
		log.info("Loggiging " + user.toString(codPolo));
		User_opac usr = User_opac.toUserModel(user, codPolo);
		
		User_opac inDB = ammDbManager.isValidUserModel(usr);
		
		usr = (inDB != null) ? inDB : usr ;
		Boolean logged = (inDB != null);

		Polo polo = (logged) ? dbPostgresManager.getSinglePolo(codPolo.toUpperCase()) : null;
		if (logged)
			log.info("Login success for " + user.toString(codPolo) );
		else
			log.info("Login Error for " + user.toString(codPolo) );
		model.addObject("response",
				loginResponse(logged, usr, serverStatusMaker.generateLogin(logged), polo));
		return model;

	}

	private JSONObject loginResponse(Boolean loggedin, User_opac user, ServerStatusBean sstatus, Polo polo) {
		JSONObject userLogin = new JSONObject();
		
		JSONObject response = APIPolo.generateAdminResponse(polo);

		userLogin.put("username", user.getUsername());
		userLogin.put("login", loggedin);
		
		userLogin.put("superuser", (loggedin) ? user.getSuperuser(): null);
		response.put("user", userLogin);
		
		return response;
	}
}
