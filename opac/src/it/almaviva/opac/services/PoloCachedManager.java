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
package it.almaviva.opac.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import it.almaviva.pssql.bean.Polo;

//Gestisce i dati del polo in cache
@Repository
public class PoloCachedManager {
	static Logger log = Logger.getLogger(PoloCachedManager.class);

	@Autowired
	private InterrogazioneOpacDBServices dbPostgresManager;
	
	private  List<JSONObject> poliJson  = new ArrayList<>();
	private List<Polo> poli = new ArrayList<>();
	
	public PoloCachedManager() {
	}
    @EventListener(ContextRefreshedEvent.class)
	public void init() {
    	poliJson.clear();
    	poli.clear();
    	poli = dbPostgresManager.getPoloList();
		toJsonList(poli);
	}
	public void toJsonList(List<Polo> poli) {
		poli.forEach(polo -> {
			poliJson.add(poloToJson(polo));
		});
		 
	}
	public List<JSONObject> getJsonPoli() {
		if(poliJson.isEmpty()) {
			log.info("Istanziando cache dei poli");
			init();
		}
		return poliJson;
	}
	public List<Polo> getPoli() {
		if(poli.isEmpty()) {
			log.info("Istanziando cache dei poli");
			init();
		}
		return poli;
	}
	public void update() {
		log.info("Aggiornando cache dei poli");
		init();
	}
	public Polo getPolo(String cod_polo) {
		if(poli.isEmpty()) 
			getPoli();
		
		Polo filteredCodPolo = poli.stream().filter(polo -> polo.getCode().toUpperCase().equals(cod_polo)).findFirst().orElse(null);
		
		try {
			return (filteredCodPolo != null) ? filteredCodPolo.clone() : null;
		} catch (CloneNotSupportedException e) {
			return filteredCodPolo;
		}
	}
	public JSONObject poloToJson(Polo polo) {
		JSONObject poloJson = new JSONObject();
		poloJson.put("libraries", polo.nonDeletedLibraries());
		poloJson.put("name", polo.getName());
		poloJson.put("code", polo.getCode());
		poloJson.put("flag_mlol", polo.getFlag_mlol());
		poloJson.put("flag_wiki", polo.getFlag_wiki());
		poloJson.put("flag_maps", polo.getFlag_maps());
		poloJson.put("maps_api_key", polo.getMaps_api_key());
		poloJson.put("linkApplicativi", polo.getLinks());
		poloJson.put("auth_autori", polo.getAuthority_flags().getFlag_autori());
		poloJson.put("auth_soggetti", polo.getAuthority_flags().getFlag_soggetti());
		poloJson.put("auth_classificazioni", polo.getAuthority_flags().getFlag_classificazioni());
		poloJson.put("useAuthority", polo.getAuthority_flags().useButtonAuthority());
		poloJson.put("materiale_inv", polo.materialeInv());
		poloJson.put("codici_frui", polo.fruizioni());
		poloJson.put("n_docs", polo.getNrdocs());
		poloJson.put("n_docs_aut", polo.getN_doc_auth());
		poloJson.put("data_scarico", polo.getDatascarico());
		poloJson.put("nome_referente", polo.getNome_referente());
		poloJson.put("email_referente", polo.getEmail_referente());
		poloJson.put("flag_logo", polo.useLogoImg());
		poloJson.put("gruppi_bib", polo.getGruppiBib());
		poloJson.put("link_esterni", polo.getLink_esterni());
		poloJson.put("email_segnalazioni", polo.getEmail_segnalazioni());
		poloJson.put("numero_referente", polo.getNumero_referente());
		poloJson.put("flag_chiedi", polo.getAuthority_flags().getFlag_chiedi());
		poloJson.put("elementi_464", polo.getLunghezza_464());

		poloJson.put("hasLinkLogo", polo.hasLinkLogo());
		return poloJson;
	}
	
}
