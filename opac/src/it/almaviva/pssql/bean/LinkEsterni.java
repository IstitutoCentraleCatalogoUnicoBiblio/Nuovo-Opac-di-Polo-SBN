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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
@Cacheable(false)
@Table(name = "link_esterni")
@Entity(name = "link_esterni")
public class LinkEsterni {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "url")
	private String url;

	@Column(name = "testo_it")
	private String testo_it;
	@Column(name = "testo_en")
	private String testo_en;
	@Column(name = "cod_polo")
	private String cod_polo;
	
	private String tipo_link;

	@ManyToOne
	@PrimaryKeyJoinColumn(name = "cod_polo", referencedColumnName = "cod_polo")
	private Polo polo;

	public LinkEsterni() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LinkEsterni(String url, String testo_it, String testo_en, String cod_polo, String tipo_link) {
		super();
		this.url = url;
		this.testo_it = testo_it;
		this.testo_en = testo_en;
		this.cod_polo = cod_polo;
		this.tipo_link = tipo_link;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTesto_it() {
		return testo_it;
	}

	public void setTesto_it(String testo_it) {
		this.testo_it = testo_it;
	}

	public String getTesto_en() {
		return testo_en;
	}

	public void setTesto_en(String testo_en) {
		this.testo_en = testo_en;
	}

	public void setPolo(Polo polo) {
		this.polo = polo;
	}
	public Polo polo() {
		return polo;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}

	public String getTipo_link() {
		return tipo_link;
	}

	public void setTipo_link(String tipo_link) {
		this.tipo_link = tipo_link;
	}

}
