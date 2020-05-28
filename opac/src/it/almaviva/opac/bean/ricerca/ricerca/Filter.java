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

import java.util.List;

import it.almaviva.opac.bean.ricerca.result.MatchType;
import it.almaviva.opac.bean.ricerca.result.OperatorType;
import it.almaviva.utils.opac.RandomIdGenerator;

//Bean di un singolo filtro vero e proprio
public class Filter {
	public Filter() {

	}

	public Filter(String field, String value, String operator, String match) {
		super();
		this.field = field;
		this.value = value;
		this.operator = operator;
		this.match = match;
		// this.otherFiltersGroup = otherFiltersGroup;
	}

	// campo
	private String field;
	// valore
	private String value;
	// operatore valido per il successivo filtro se presente AND di default
	private String operator = OperatorType.AND;
	// tipo di match se per parole in and o frase o frase esatta.
	private String match = MatchType.andWord;
	// ulteriore gruppo di filtri necessario per i gruppi di gruppi
	private List<Filter> otherFiltersGroup;

	private String uuid = RandomIdGenerator.getId();
	public String getUuid() {
		return uuid;
	}
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public List<Filter> getOtherFiltersGroup() {
		try {
			if (otherFiltersGroup.size() > 0 && field.toUpperCase().equals("GROUP") || otherFiltersGroup == null) {
				return otherFiltersGroup;
			} else {
				return null;
			}
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void setOtherFiltersGroup(List<Filter> otherFiltersGroup) {
		this.otherFiltersGroup = otherFiltersGroup;
	}

	public Boolean isGroup() {
		try {
			if (otherFiltersGroup.size() > 0 && field.toUpperCase().equals("GROUP") || otherFiltersGroup == null) {
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

}
