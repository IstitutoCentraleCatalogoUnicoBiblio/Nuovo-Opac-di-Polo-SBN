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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.opac.classificazioniDewey.ClassificazioneType;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.opac.services.SolrQueryExecuteServices;
import it.almaviva.pssql.bean.ClassificazioniDewey;

@RestController
public class APIClassificazioniController {
	@Autowired
	SolrQueryExecuteServices queryMaker;
	GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();
	@Autowired
	InterrogazioneOpacDBServices dbServices;
	
	@GetMapping("api/{polo}/get/classificazioni")
	public List<ClassificazioniDewey> getClassificazioni( @PathVariable String polo){
		
		return dbServices.getClassificazioniDewey();
	}

	@GetMapping("api/{polo}/get/classificazioni/{posizione}/{start}")
	public List<ClassificazioniDewey> getClassificazioni(@PathVariable String posizione, @PathVariable String start,  @PathVariable String polo){
		
		List<ClassificazioniDewey> deweys =  dbServices.getClassificazioniDewey(cast_pos(posizione), start);
		return queryMaker.classificazioniDewey(deweys, polo, ClassificazioneType.normalize(cast_pos(posizione)), start);
	}
	@GetMapping("api/{polo}/get/classificazioni/{posizione}")
	public List<ClassificazioniDewey> getClassificazioni(@PathVariable String posizione,  @PathVariable String polo){
		
		List<ClassificazioniDewey> deweys =  dbServices.getClassificazioniDewey(cast_pos(posizione), null);
		return queryMaker.classificazioniDewey(deweys, polo, ClassificazioneType.normalize(cast_pos(posizione)));	
	}
	private Integer cast_pos(String posizione) {
		try {
			return Integer.parseInt(posizione);
		} catch (Exception e) {
			return null;
		}
	}
}
