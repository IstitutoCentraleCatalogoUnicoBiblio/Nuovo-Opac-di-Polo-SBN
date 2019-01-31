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
package it.almaviva.utils.amministrazione;

import java.util.List;

import it.almaviva.pssql.bean.Biblio;
import it.almaviva.pssql.bean.Gruppi;
import it.almaviva.pssql.bean.LinkEsterni;

public class UtilsAmministrazione {
	public static Integer getIndexInGruppiBib(List<Gruppi> gruppi_bib, String cod_polo, Integer id) {
		// Integer idx = -1;

		cod_polo = cod_polo.trim().toUpperCase();
		for (int i = 0; i < gruppi_bib.size(); i++) {
			if (gruppi_bib.get(i).getId() == id && gruppi_bib.get(i).getCod_polo().equals(cod_polo))
				return i;
		}

		return -1;
	}
	public static Integer getIndexInBib(List<Biblio> bib_list, String cod_polo, Integer id) {
		// Integer idx = -1;

		cod_polo = cod_polo.trim().toUpperCase();
		for (int i = 0; i < bib_list.size(); i++) {
			if (bib_list.get(i).getId() == id && bib_list.get(i).polo().equals(cod_polo))
				return i;
		}
		return -1;
	}
	public static Integer getIndexInLink(List<LinkEsterni> links, String cod_polo, Integer id) {
		// Integer idx = -1;

		cod_polo = cod_polo.trim().toUpperCase();
		for (int i = 0; i < links.size(); i++) {
			if (links.get(i).getId().equals(id) && links.get(i).getCod_polo().equals(cod_polo))
				return i;
		}
		return -1;
	}
}
