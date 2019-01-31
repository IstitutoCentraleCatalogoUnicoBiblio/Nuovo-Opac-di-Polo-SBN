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

import it.almaviva.opac.bean.ricerca.result.OperatorType;
//Bean di gruppi di filtri
public class GroupFilters {
	//Lista di gruppi di filtri
	private List<Filters> filters;
	//operatore 
	private String operator = OperatorType.AND;
	
	public List<Filters> getFilters() {
		return filters;
	}
	public void setFilters(List<Filters> filters) {
		this.filters = filters;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public GroupFilters() {
		super();
	}
	public GroupFilters(List<Filters> filters, String operator) {
		super();
		this.filters = filters;
		this.operator = operator;
	}
	
	
}
