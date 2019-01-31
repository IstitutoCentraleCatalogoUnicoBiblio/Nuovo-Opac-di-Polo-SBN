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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import it.almaviva.opac.bean.ricerca.ricerca.Filter;
import it.almaviva.opac.bean.ricerca.ricerca.Filters;
import it.almaviva.opac.bean.ricerca.ricerca.GroupFilters;

//Classe di creazione query per Medialibrary, usata anche per wikipedia
public class MLOLQueryBuilder {
	static Logger log = Logger.getLogger(MLOLQueryBuilder.class);
	private String query = "";

	private String queryBuilder(GroupFilters filters) {
		log.info("Groupfilters" + filters.toString());
		String queryToBuild = new String();
		List<Filters> ff = filters.getFilters();
		ListIterator<Filters> filterIterator = ff.listIterator();
		for (Filters filter : filters.getFilters()) {
			filterIterator.next();

			queryBuilder(filter.getFilters());

		}

		return queryToBuild;
	}

	private String queryBuilder(List<Filter> filters) {
		String queryToBuild = new String();

		// ListIterator<Filter> filterIterator = filters.listIterator();
		for (Filter filter : filters) {
			if (filter.isGroup()) {
				// ricorsivo
				queryBuilder(filter.getOtherFiltersGroup());
			} else {
				addQuery(filter);
			}

		}
		return queryToBuild;
	}

	public String getMLOLQuery(GroupFilters filters) {
		// building popolating query str
		this.queryBuilder(filters);
		// returning query
		String toReturnQuery = new String(query.replaceAll("( +)", " +").trim());
		query = "";
		log.info("Builded external query: '" + toReturnQuery + "'");
		return toReturnQuery;
	}

	private void addQuery(Filter filter) {
		if (isMLOLSearchable(filter.getField()))
			query += "  " + filter.getValue();
	}

	private Boolean isMLOLSearchable(String field) {
		List<String> campidata = new ArrayList<String>();

		// campi validi per una ricerca su MLOL
		campidata.add("keywords");
		campidata.add("any");
		campidata.add("titolo");
		campidata.add("nome");
		campidata.add("editore");
		campidata.add("nomef");
		campidata.add("editoref");
		campidata.add("lingua");
		campidata.add("autore");
		return (campidata.indexOf(field.toLowerCase()) > -1);
	}
}
