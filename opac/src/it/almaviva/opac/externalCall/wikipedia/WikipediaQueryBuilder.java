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
package it.almaviva.opac.externalCall.wikipedia;

import org.apache.log4j.Logger;

import it.almaviva.opac.bean.ricerca.ricerca.GroupFilters;
import it.almaviva.opac.externalCall.medialibrary.MLOLQueryBuilder;

//Costruisce una query di parole per wikipedia rimuovendo il + usato come operatore da MediaLibrary
public class WikipediaQueryBuilder extends MLOLQueryBuilder {
	static Logger log = Logger.getLogger(WikipediaQueryBuilder.class);

	public String getWikipediaQuery(GroupFilters filters) {
		log.info("Building Wikipedia query");
		return super.getMLOLQuery(filters).replace("+", "");
	}
}
