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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.bean.amministrazione.LoginBean;
import it.almaviva.opac.services.AmministrazioneOpacDBServices;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.pssql.bean.User_opac;
import it.almaviva.utils.opac.ServerStatusBean;

@Controller
public class APICambioPsw {
	static Logger log = Logger.getLogger(APICambioPsw.class);
	private GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();

	@Autowired
	InterrogazioneOpacDBServices dbPostgresManager;
	@Autowired
	AmministrazioneOpacDBServices ammDbManager ;
	
	@RequestMapping(value = "/api/{codPolo}/admin/user/update", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView login(@PathVariable String codPolo, @RequestBody LoginBean user) {
		ModelAndView model = new ModelAndView("response");
		log.info("Changing psw per " + user.getPassword());
		Boolean isUpdated = ammDbManager.updatePsw(User_opac.toUserModel(user, codPolo), user.getNewPsw());
		ServerStatusBean ss = serverStatusMaker.isDone(isUpdated);
				
		model.addObject("response", toJson(user, isUpdated, ss));	
		return model;
	}
	
	private JSONObject toJson(LoginBean user, Boolean isUpdated, ServerStatusBean sstatus) {
		JSONObject response = new JSONObject();
		JSONObject userLogin = new JSONObject();
		userLogin.put("username", user.getUsername());
		userLogin.put("updated", isUpdated);
		
		
		response.put("user", userLogin);
		response.put("serverStatus", ServerStatusBean.toJson(sstatus));
		return response;
		
	}
}
