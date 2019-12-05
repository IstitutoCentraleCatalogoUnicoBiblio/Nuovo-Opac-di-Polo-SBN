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
package it.almaviva.pssql.bean;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
@Cacheable(false)
@Entity(name="applicativo_servizi")
@Table(name = "applicativo_servizi")
public class ApplicativoServizi {

	@Column(name = "cod_polo")
	private String polo;
	
	@Column(name = "descr")
	private String descrizione;
	
	@Column(name = "link_fisso")
	private String url;
	@Id
	@Column(name="id")
	private Integer id;

	@Column(name = "cod_appl")
	private String cod_appl;
	
	@ManyToOne
	@PrimaryKeyJoinColumn (name="cod_polo", referencedColumnName="cod_polo")
	private Polo poloreference;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCod_appl() {
		return cod_appl.trim();
	}

	public void setCod_appl(String cod_appl) {
		this.cod_appl = cod_appl;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}


	
}
