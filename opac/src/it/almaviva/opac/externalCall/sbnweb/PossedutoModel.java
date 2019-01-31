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
package it.almaviva.opac.externalCall.sbnweb;

import it.almaviva.pssql.bean.Polo;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

public interface PossedutoModel {
	public SbnwebType fascicoli(Polo polo, String codBiblioteca, String idDoc, String inventario);
	public SbnwebType disponibilita(Polo polo, String bid, String codBiblioteca);
}
