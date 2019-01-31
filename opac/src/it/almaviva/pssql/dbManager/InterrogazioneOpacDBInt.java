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
package it.almaviva.pssql.dbManager;

import java.util.List;

import it.almaviva.pssql.bean.Biblio;
import it.almaviva.pssql.bean.ClassificazioniDewey;
import it.almaviva.pssql.bean.Polo;

public interface InterrogazioneOpacDBInt {

	Polo getSinglePolo(String codPolo);

	Boolean isPolo(String codPolo);

	List<Polo> getPoloList();
	Biblio  getBiblio (String cod_polo, String cod_bib);
	List<ClassificazioniDewey> getClassificazioniDewey();
	List<ClassificazioniDewey> getClassificazioniDewey(Integer posizione, String start);
}
