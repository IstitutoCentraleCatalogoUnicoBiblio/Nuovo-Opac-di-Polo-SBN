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
package it.almaviva.opac.bean.ricerca.ricerca;

public enum DateNovitaType {
	IERI, OGGI, LAST_WEEK, LAST_MONTH, OTHER;
	
	
	public static DateNovitaType checkDate(String date) {
		switch (date.toUpperCase().trim()) {
		case "IERI":
			return IERI;
		case "OGGI":
			return OGGI;
		case "LAST_WEEK":
			return LAST_WEEK;
		case "LAST_MONTH":
			return LAST_MONTH;
		default:
			return OTHER;
		}
	}

}
