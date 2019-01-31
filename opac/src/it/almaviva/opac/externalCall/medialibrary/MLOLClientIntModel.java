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
package it.almaviva.opac.externalCall.medialibrary;

import it.almaviva.pssql.bean.Polo;
import it.medialibrary.api.NewListResponseNewListResultMedia_list;
import it.medialibrary.api.RicercaAvResponseRicercaAvResultMedia;
import it.medialibrary.api.RicercaResponseRicercaResultMedia;
import it.medialibrary.api.TopListResponseTopListResultMedia_list;

public interface MLOLClientIntModel  {
	public RicercaResponseRicercaResultMedia ricerca(Polo polo,  String search );
	public RicercaAvResponseRicercaAvResultMedia ricercaAv(Polo polo,  String titolo, String autore, String editore, String lingua, String idTipologia);
	public TopListResponseTopListResultMedia_list topList(Polo polo, String idTipologia);
	public NewListResponseNewListResultMedia_list newList(Polo polo, String idTipologia);

}
