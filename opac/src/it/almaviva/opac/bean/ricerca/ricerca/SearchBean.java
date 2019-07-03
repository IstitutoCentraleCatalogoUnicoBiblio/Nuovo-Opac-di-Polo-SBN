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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.almaviva.opac.bean.ricerca.result.MatchType;
import it.almaviva.opac.bean.ricerca.result.OperatorType;
import it.almaviva.opac.bean.ricerca.result.Order;
//bean di ricerca per documenti, la struttura di esempio descritta dal file txt
public class SearchBean {
	//numero di start della ricerca
	private Integer start;
	//numero massimo di righe da ottenere
	private Integer maxRows;
	//tipo do sort. si puo scegliere un campo per cui sortare che sia presente all'interno dello switch in SolrQueryExecute 
	private String sort;
	//Gruppo di filtri
	private GroupFilters filters;
	//ordinamento asc o desc
	private String order = Order.ASC;
	//flag di controllo se il risultato da fornire deve essere un dettaglio di un documento
	private Boolean isDetail = false ;
	public SearchBean(Integer start, Integer maxRows, String sort, GroupFilters filters, String order,
			Boolean isDetail) {
		super();
		this.start = start;
		this.maxRows = maxRows;
		this.sort = sort;
		this.filters = filters;
		this.order = order;
		this.isDetail = isDetail;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getMaxRows() {
		return maxRows;
	}
	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public GroupFilters getFilters() {
		return filters;
	}
	public void setFilters(GroupFilters filters) {
		this.filters = filters;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Boolean getIsDetail() {
		return isDetail;
	}
	public void setIsDetail(Boolean isDetail) {
		this.isDetail = isDetail;
	}
	public SearchBean() {
		super();
	}
	public static SearchBean getNovita(String dateFrom) {
		return build("tagI950", dateFrom);
	}
	public static SearchBean build(String field, String value) {
		SearchBean searchList = new SearchBean();
		Filters lit = new Filters();
		List<Filter> lfilt = new ArrayList<Filter>();
		lfilt.add(new Filter(field, value, OperatorType.AND, MatchType.andWord));
		lit.setFilters(lfilt);
		List<Filters> lfilters = new ArrayList<Filters>();
		lfilters.add(lit);
		
		searchList.setIsDetail(false);
		searchList.setMaxRows(10);
		searchList.setSort("score");
		searchList.setStart(0);
		searchList.setFilters(new GroupFilters(lfilters, OperatorType.AND));
		return searchList;
	
	}
	public static SearchBean buildSearchBeanByMap(Map<String, String> filters){
		SearchBean searchList = new SearchBean();
		Filters lit = new Filters();
		List<Filter> lfilt = new ArrayList<Filter>();
		for (Map.Entry<String, String> entry : filters.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
			lfilt.add(new Filter(key, value, OperatorType.AND, MatchType.andWord));		    
		}
		lit.setFilters(lfilt);
		List<Filters> lfilters = new ArrayList<Filters>();
		lfilters.add(lit);
		
		searchList.setIsDetail(false);
		searchList.setMaxRows(10);
		searchList.setSort("score");
		searchList.setStart(0);
		searchList.setFilters(new GroupFilters(lfilters, OperatorType.AND));
		return searchList;
	
	}
	
}
