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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.pssql.bean.Biblio;
import it.almaviva.pssql.bean.ClassificazioniDewey;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.pssql.dbManager.InterrogazioneOpacDBInt;
import it.almaviva.pssql.dbManager.dao.InterrogazioneOpacDBDao;

@Service
public class InterrogazioneOpacDBServices implements InterrogazioneOpacDBInt{
	
	@Autowired
	InterrogazioneOpacDBDao dao = new InterrogazioneOpacDBDao();
	
	@Override
	public Polo getSinglePolo(String codPolo) {
		return dao.getSinglePolo(codPolo);
	}

	@Override
	public Boolean isPolo(String codPolo) {
		return dao.isPolo(codPolo);
	}

	@Override
	public List<Polo> getPoloList() {
		return dao.getPoloList()
				;
	}

	@Override
	public Biblio getBiblio(String cod_polo, String cod_bib) {
		return dao.getBiblio(cod_polo, cod_bib);
	}

	@Override
	public List<ClassificazioniDewey> getClassificazioniDewey() {
		
		return dao.getClassificazioniDewey();
	}

	@Override
	public List<ClassificazioniDewey> getClassificazioniDewey(Integer posizione, String start) {
		if(start == null)
			start = "";
		return dao.getClassificazioniDewey(posizione, start);
	}

}
