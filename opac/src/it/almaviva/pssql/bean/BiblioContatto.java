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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "biblio_contatti")
@Table(name = "biblio_contatti")
public class BiblioContatto {
	@Id

	private int id;

	@Column(name = "cod_bib", insertable = false, updatable = false)
	private String cod_bib;

	@Column(name = "cod_polo", insertable = false, updatable = false)
	private String cod_polo;

	@Column(name = "tipo")
	private String tipo;
	@Column(name = "valore")
	private String valore;
	// @JoinColumn(name = "cod_bib", referencedColumnName = "cod_bib")
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "cod_bib", referencedColumnName = "cod_bib"),
			@JoinColumn(name = "cod_polo", referencedColumnName = "cod_polo") })
	private Biblio biblioteca;

	public void setBiblioteca(Biblio biblioteca) {
		this.biblioteca = biblioteca;
	}
	public void setId(int id) {
		this.id = id;
	}

	public void setCod_bib(String cod_bib) {
		this.cod_bib = cod_bib;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValore() {
		return valore.trim();
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

}
