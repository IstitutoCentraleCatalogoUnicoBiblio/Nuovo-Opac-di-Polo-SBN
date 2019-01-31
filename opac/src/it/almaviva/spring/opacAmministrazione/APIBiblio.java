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

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

import it.almaviva.opac.bean.amministrazione.BiblioModel;
import it.almaviva.opac.services.AmministrazioneOpacDBServices;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.opac.services.PoloCachedManager;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.utils.Costanti;
import it.almaviva.utils.opac.ServerStatusBean;
import it.almaviva.utils.opac.Util;

@Controller
public class APIBiblio {
	static Logger log = Logger.getLogger(APIBiblio.class);
	@Autowired
	PoloCachedManager cachedPoli;
	
	@Autowired
	InterrogazioneOpacDBServices dbPostgresManager;
	@Autowired
	AmministrazioneOpacDBServices ammDbManager;

	@RequestMapping(value = "/api/{codPolo}/admin/biblio/insert", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView insertGruppo(@PathVariable String codPolo, @RequestBody BiblioModel toInsert) {
		log.info("inserendo biblio " + toInsert.getName() + " per " + codPolo);
		ModelAndView model = new ModelAndView("response");
		Polo polo = ammDbManager.insertBiblio(toInsert);
		model.addObject("response", APIPolo.generateAdminResponse(polo));
		cachedPoli.update();
		return model;
	}

	@RequestMapping(value = "/api/{codPolo}/admin/biblio/update", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView update(@PathVariable String codPolo, @RequestBody BiblioModel toUpdate) {
		log.info("agiornando biblio " + toUpdate.getName() + " per " + codPolo);
		ModelAndView model = new ModelAndView("response");
		Polo polo = ammDbManager.updateBiblio(toUpdate);
		model.addObject("response", APIPolo.generateAdminResponse(polo));
		cachedPoli.update();
		return model;
	}

	@RequestMapping(value = "/api/{codPolo}/admin/biblio/delete", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ModelAndView delete(@PathVariable String codPolo, @RequestBody BiblioModel toDelete) {
		log.info("inserendo biblio " + toDelete.getName() + " per " + codPolo);
		ModelAndView model = new ModelAndView("response");
		Polo polo = ammDbManager.deleteBiblio(toDelete);
		model.addObject("response", APIPolo.generateAdminResponse(polo));
		cachedPoli.update();
		return model;
	}

	@RequestMapping(value = "/api/{codPolo}/admin/biblio/import")
	public ModelAndView getDataFromAnagrafe(@PathVariable String codPolo) {
		log.info("Running biblioteche importer");
		ModelAndView model = new ModelAndView("response");
		GenerateServerStatusServices ss = new GenerateServerStatusServices();
		JSONObject resp = new JSONObject();
		String target = new String(Costanti.user_home + "/bibliotecheImporter/bibliotecheImporter.sh");
		log.info("launching: " + target);
		target = Util.cleanPath(target);
		try {
		
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(target);
			proc.waitFor();
			StringBuffer output = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
			//System.out.println("### " + output);
			log.info("--------BiblioteceImporter--------");
			log.info(output.toString());
			log.info("----------------------------------");
			resp.put("serverStatus", ServerStatusBean.toJson(ss.serverStatusFromCode(200)));
		} catch (Throwable t) {
			log.info("Error running sh bibliotecheImporter");
			resp.put("serverStatus", ServerStatusBean.toJson(ss.serverStatusFromCode(400)));
		}

		
	//	resp.put("response", null);
		model.addObject("response", resp);
		return model;
	}

}
