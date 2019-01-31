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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.opac.bean.amministrazione.BiblioModel;
import it.almaviva.opac.bean.amministrazione.GruppoModel;
import it.almaviva.opac.bean.amministrazione.LinkEsterniModel;
import it.almaviva.opac.bean.amministrazione.PoloModel;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.pssql.bean.User_opac;
import it.almaviva.pssql.dbManager.AmministrazioneOpacDBInt;
import it.almaviva.pssql.dbManager.dao.AmministrazioneOpacDBDao;

@Service
public class AmministrazioneOpacDBServices implements AmministrazioneOpacDBInt {

	@Autowired
	AmministrazioneOpacDBInt ammDao = new AmministrazioneOpacDBDao();

	@Override
	public Boolean isValidUser(User_opac usr) {
		return ammDao.isValidUser(usr);
	}

	@Override
	public Boolean updatePsw(User_opac usr, String newPsw) {
		return ammDao.updatePsw(usr, newPsw);
	}

	@Override
	public User_opac isValidUserModel(User_opac usr) {
		return ammDao.isValidUserModel(usr);
	}

	@Override
	public Polo updatePolo(PoloModel polotoUpdate) {
		return ammDao.updatePolo(polotoUpdate);
	}

	@Override
	public Polo insertGruppo(GruppoModel g) {

		return ammDao.insertGruppo(g);
	}

	@Override
	public Polo deleteGruppo(GruppoModel g) {
		return ammDao.deleteGruppo(g);
	}

	@Override
	public Polo updateGruppo(GruppoModel g) {
		return ammDao.updateGruppo(g);
	}

	@Override
	public Polo insertBiblio(BiblioModel b) {
		return ammDao.insertBiblio(b);
	}

	@Override
	public Polo deleteBiblio(BiblioModel b) {
		return ammDao.deleteBiblio(b);
	}

	@Override
	public Polo updateBiblio(BiblioModel b) {
		return ammDao.updateBiblio(b);
	}

	@Override
	public Polo insertLink(LinkEsterniModel l) {
		return ammDao.insertLink(l);
	}

	@Override
	public Polo updateLink(LinkEsterniModel l) {
		return ammDao.updateLink(l);
	}

	@Override
	public Polo deleteLink(LinkEsterniModel l) {
		return ammDao.deleteLink(l);
	}

}
