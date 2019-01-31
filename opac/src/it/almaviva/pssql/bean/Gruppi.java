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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "gruppi")
@Table(name = "gruppi")
public class Gruppi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	
//	@Column(name = "cod_polo")
	private String cod_polo;
	
	@ManyToOne
	@JoinColumn(name = "cod_polo", referencedColumnName = "cod_polo", insertable = false, updatable = false)
	private Polo polo;
	@ManyToMany
	@JoinTable(name="rel_bibliogruppi", joinColumns = @JoinColumn(name = "id_gruppi"), inverseJoinColumns =  @JoinColumn( name ="id_biblio"))
	private List<Biblio> biblioteche;
	

	public List<Biblio> getBiblioteche() {
		//ritorna le biblioteche non eliminate
		List<Biblio> bib = new ArrayList<Biblio>();
		for (Biblio b: biblioteche){
			if(!b.isDeleted())
				bib.add(b);
		}
		return bib;
	}

	public void setBiblioteche(List<Biblio> biblioteche) {
		this.biblioteche = biblioteche;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}
	public Gruppi(){
	}
	public Gruppi(Integer id, String name, String cod_polo) {
		super();
		this.id = id;
		this.name = name;
		this.cod_polo = cod_polo;
	
	}

	
	
}
