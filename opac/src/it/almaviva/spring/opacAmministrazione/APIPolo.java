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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.bean.amministrazione.PoloModel;
import it.almaviva.opac.services.AmministrazioneOpacDBServices;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.PoloCachedManager;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.utils.opac.ServerStatusBean;

@Controller
public class APIPolo {
	static Logger log = Logger.getLogger(APIPolo.class);
	private static GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();

	@Autowired
	PoloCachedManager cachedPoli;
	
	@Autowired
	AmministrazioneOpacDBServices ammDbManager;
	
	@RequestMapping(value = "/api/{codPolo}/admin/polo/update", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	
	public ModelAndView polo(@PathVariable String codPolo, @RequestBody PoloModel polotoUpdate) {
		ModelAndView model = new ModelAndView("response");
		
		Polo polo = ammDbManager.updatePolo(polotoUpdate);
	
		model.addObject("response", generateAdminResponse(polo));
		cachedPoli.update();
		return model;
	}
	protected static JSONObject generateAdminResponse(Polo polo) {
	
		JSONObject resp = new JSONObject();
		JSONObject poloJson = new JSONObject();
		ServerStatusBean serverStatus = serverStatusMaker.generateServerStatus(polo);
		// generate richiesta
		if (!(polo == null)) {
			try {
				try {
					poloJson.put("libraries", polo.allLibraries());
					poloJson.put("name", polo.getName());
					poloJson.put("code", polo.getCode());
					poloJson.put("flag_mlol", polo.getFlag_mlol());
					poloJson.put("flag_wiki", polo.getFlag_wiki());
					poloJson.put("flag_maps", polo.getFlag_maps());
					poloJson.put("flag_chiedi", polo.getAuthority_flags() != null ? polo.getAuthority_flags().getFlag_chiedi() : "ERRORE FLAG NON IMPOSTATO/I");

					poloJson.put("maps_api_key", polo.getMaps_api_key());
					poloJson.put("linkApplicativi", polo.getLinks());
					poloJson.put("auth_autori", polo.getAuthority_flags() != null ? polo.getAuthority_flags().getFlag_autori() : "ERRORE FLAG NON IMPOSTATO/I");
					poloJson.put("auth_soggetti", polo.getAuthority_flags() != null ? polo.getAuthority_flags().getFlag_soggetti() : "ERRORE FLAG NON IMPOSTATO/I");
					poloJson.put("materiale_inv", polo.materialeInv());
					poloJson.put("codici_frui", polo.fruizioni());
					//poloJson.put("n_docs", polo.getNrdocs());
				//	poloJson.put("n_docs_aut", polo.getN_doc_auth());
					poloJson.put("auth_classificazioni", polo.getAuthority_flags() != null ? polo.getAuthority_flags().getFlag_classificazioni() : "ERRORE FLAG NON IMPOSTATO/I" );
					poloJson.put("useAuthority", polo.getAuthority_flags() != null ? polo.getAuthority_flags().useButtonAuthority() : "ERRORE FLAG NON IMPOSTATO/I");
					poloJson.put("data_scarico", polo.getDatascarico());
					poloJson.put("nome_referente", polo.getNome_referente());
					poloJson.put("email_referente", polo.getEmail_referente());
					poloJson.put("email_referente", polo.getEmail_referente());
					poloJson.put("gruppi_bib", polo.getGruppiBib());
					poloJson.put("flag_logo", polo.useLogoImg());
					poloJson.put("maps_api_key", polo.getMaps_api_key());
					
					poloJson.put("mlol_portal_id", polo.mlolCredential() != null ? polo.mlolCredential().getPortal_id() : "");
					poloJson.put("mlol_api_key", polo.mlolCredential() != null ? polo.mlolCredential().getApi_key(): "" );
					poloJson.put("link_esterni", polo.getLink_esterni());
					poloJson.put("email_segnalazioni", polo.getEmail_segnalazioni());
					poloJson.put("numero_referente", polo.getNumero_referente());

			
				} catch (JSONException | NullPointerException e) {
					e.printStackTrace();
					poloJson = null;
					log.info("Attenzione, il database non contiene tutte le informazioni necessarie.", e);
				}
				
				
			} catch (JSONException | NullPointerException e) {
				e.printStackTrace();
				log.info("Attenzione, il database non contiene tutte le informazioni necessarie.", e);
			}

		}
		resp.put("polo", poloJson);
		resp.put("serverStatus", ServerStatusBean.toJson(serverStatus));
		log.info("Returning JSON: " + polo.getCode());
		return resp;
	}
}
