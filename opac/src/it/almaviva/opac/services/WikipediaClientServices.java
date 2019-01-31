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
import org.springframework.stereotype.Repository;

import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.externalCall.wikipedia.WikipediaClientInt;
import it.almaviva.opac.externalCall.wikipedia.WikipediaQueryBuilder;
import it.almaviva.opac.externalCall.wikipedia.dao.WikipediaClientDao;

@Repository
public class WikipediaClientServices implements WikipediaClientInt {
	@Autowired
	InterrogazioneOpacDBServices dbManager = new InterrogazioneOpacDBServices();

	WikipediaClientDao wikipedia = new WikipediaClientDao();

	@Override
	public Object callWikipediaApi(String codPolo, SearchBean search) {
		if (dbManager.getSinglePolo(codPolo).getFlag_wiki()) {
			// costruisco la query per la ricerca su wikipedia, la query è
			// simile
			return wikipedia.callWikipedia(new WikipediaQueryBuilder().getWikipediaQuery(search.getFilters()));
		} else {
			return null;
		}

	}

}
