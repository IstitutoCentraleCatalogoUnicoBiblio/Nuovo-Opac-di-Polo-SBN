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
package it.almaviva.opac.bean.amministrazione;

import java.util.List;

import it.almaviva.pssql.bean.Biblio;

public class GruppoModel {
	private Integer id;
	private String cod_polo;
	private String name;
	private List<Biblio> biblioteche;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Biblio> getBiblioteche() {
		return biblioteche;
	}
	public void setBiblioteche(List<Biblio> biblioteche) {
		this.biblioteche = biblioteche;
	}
	public String getCod_polo() {
		return cod_polo;
	}
	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
