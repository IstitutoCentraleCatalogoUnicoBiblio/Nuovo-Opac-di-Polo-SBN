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

@Entity(name = "cat_fruizione950")
@Table( name = "cat_fruizione950")
public class CatFruizione {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "cod_polo")
	private String code;
	
	@Column(name = "cod_cat")
	private String cod;
	
	@Column(name = "desc_it")
	private String descr_it;
	
	@Column(name = "desc_en")
	private String descr_en;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="cod_polo", referencedColumnName="cod_polo")
	private Polo polo;

	public String getCod() {
		return cod.trim();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cod == null) ? 0 : cod.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatFruizione other = (CatFruizione) obj;
		if (cod == null) {
			if (other.cod != null)
				return false;
		} else if (!cod.equals(other.cod))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	} 
	

	
}
