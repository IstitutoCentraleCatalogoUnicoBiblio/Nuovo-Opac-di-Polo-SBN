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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "biblio_dettagli")
@Table(name = "biblio_dettagli")
public class BiblioDettagli {
	@Id
	private int id;

	@Column(name = "cod_bib", insertable = false, updatable = false)
	private String cod_bib;
	@Column(name = "cod_polo", insertable = false, updatable = false)
	private String cod_polo;

	@Column(name = "indirizzo")
	private String indirizzo;

	@Column(name = "cap")
	private String cap;

	@Column(name = "cod_anagrafe")
	private String isil;

	@Column(name = "citta")
	private String citta;

	@Column(name = "provincia")
	private String provincia;

	@Column(name = "latitudine")
	private String latitudine;

	@Column(name = "longitudine")
	private String longitudine;
	@OneToOne
	@JoinColumns({ @JoinColumn(name = "cod_bib", referencedColumnName = "cod_bib"),
			@JoinColumn(name = "cod_polo", referencedColumnName = "cod_polo") })
	private Biblio bibReference;
	
	public void setBibReference(Biblio bibReference) {
		this.bibReference = bibReference;
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

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}


	public String getIsil() {
		return isil;
	}


	public void setIsil(String isil) {
		this.isil = isil;
	}


	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	public String getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

}
