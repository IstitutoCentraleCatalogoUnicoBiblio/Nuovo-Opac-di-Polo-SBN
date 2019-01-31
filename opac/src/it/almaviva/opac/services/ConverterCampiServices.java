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

import it.almaviva.opac.converterCampi.ConverterCampi;
import it.almaviva.opac.converterCampi.dao.ConverterCampiDao;

public class ConverterCampiServices implements ConverterCampi{
	private ConverterCampiDao converter = new ConverterCampiDao();
	@Override
	public String converToPublic(String field) {
		return converter.converToPublic(field);
	}

	@Override
	public String converToSearch(String field) {
		return converter.converToSearch(field);
	}

	@Override
	public String convertToSearchFacet(String field) {
		return converter.convertToSearchFacet(field);
	}

	@Override
	public String convertToSearchAuth(String field) {
		return converter.convertToSearchAuth(field);
	}

	@Override
	public String covertToSearchTerm(String field) {
		return converter.covertToSearchTerm(field);
	}
}
