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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "materialeinventariale")
@Table ( name = "materialeinventariale")
public class MaterialeInventariale {
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "cod_polo")
	private String code;
	
	@Column(name = "cod_matinv")
	private String cod;
	
	@Column(name = "desc_it")
	private String descr_it;
	
	@Column(name = "desc_en")
	private String descr_en;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="cod_polo", referencedColumnName="cod_polo")
	private Polo polo;

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDescr_it() {
		return descr_it;
	}

	public void setDescr_it(String descr_it) {
		this.descr_it = descr_it;
	}

	public String getDescr_en() {
		return descr_en;
	}

	public void setDescr_en(String descr_en) {
		this.descr_en = descr_en;
	}
	
	
}
