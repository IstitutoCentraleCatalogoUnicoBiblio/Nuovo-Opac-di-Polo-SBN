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

import it.almaviva.opac.bean.amministrazione.BiblioModel;
import it.almaviva.opac.bean.amministrazione.GruppoModel;
import it.almaviva.opac.bean.amministrazione.LinkEsterniModel;
import it.almaviva.opac.bean.amministrazione.PoloModel;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.pssql.bean.User_opac;

public interface AmministrazioneOpacDBInt {
	
	public Boolean isValidUser(User_opac usr);
	public User_opac isValidUserModel(User_opac usr);
	public Boolean updatePsw (User_opac usr, String newPsw);
	public Polo updatePolo(PoloModel polotoUpdate);
	
	public Polo insertGruppo(GruppoModel g);
	public Polo deleteGruppo(GruppoModel g);
	public Polo updateGruppo(GruppoModel g);
	
	public Polo insertBiblio(BiblioModel b);
	public Polo deleteBiblio(BiblioModel b);
	public Polo updateBiblio(BiblioModel b);
	
	public Polo insertLink(LinkEsterniModel l);
	public Polo updateLink(LinkEsterniModel l);
	public Polo deleteLink(LinkEsterniModel l);
	
	
}
