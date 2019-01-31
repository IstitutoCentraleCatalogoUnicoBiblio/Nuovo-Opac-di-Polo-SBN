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
package it.almaviva.opac.bean.ricerca.result;

import java.util.List;

public class SolrResponseBean {
	//Elenco di documenti dettagliati	
	private List<CampiRicercaDetailBean> documenti;
	private List<CampiRicercaSinteticaBean> sintetica;
	private List<AuthorityBean> autori;
	private SolrReturnType type;
	//numero TOTALE di documenti trovati
	private Long numFound;
	
	//faccetta
	private List<FaccettaBean> facc;
	
	//query
	private String query;
	
	public SolrReturnType getType() {
		return type;
	}
	public void setType(SolrReturnType type) {
		this.type = type;
	}
	public List<AuthorityBean> getAutori() {
		return autori;
	}
	public void setAutori(List<AuthorityBean> autori) {
		this.autori = autori;
	}
	public List<CampiRicercaSinteticaBean> getSintetica() {
		return sintetica;
	}
	public void setSintetica(List<CampiRicercaSinteticaBean> sintetica) {
		this.sintetica = sintetica;
	}
	public List<CampiRicercaDetailBean> getDocumenti() {
		return documenti;
	}
	public void setDocumenti(List<CampiRicercaDetailBean> documenti) {
		this.documenti = documenti;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<FaccettaBean> getFacc() {
		return facc;
	}
	public void setFacc(List<FaccettaBean> facc) {
		this.facc = facc;
	}
	public List<CampiRicercaDetailBean> getDocs() {
		return documenti;
	}
	public void setDocs(List<CampiRicercaDetailBean> documenti) {
		this.documenti =  documenti;
	}
	public Long getNumFound() {
		return numFound;
	}
	public void setNumFound(Long numFound) {
		this.numFound =  numFound;
	}
}
