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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "flag_authority")
@Table(name = "flag_authority")
public class Authority_flags {
	@Id
	private Integer id;

	@Column(name = "flag_autori")
	private String flag_autori;

	@Column(name = "flag_soggetti")
	private String flag_soggetti;

	@Column(name = "flag_classi")
	private String flag_classificazioni;
	@Column(name = "flag_chiedi")
	private String flag_chiedi;

	@OneToOne
	@JoinColumn(name = "cod_polo", referencedColumnName = "cod_polo")
	private Polo polo_auth;

	public Boolean getFlag_autori() {
		return (flag_autori.equals("1") ? true : false);
	}

	public void setFlag_autori(String flag_autori) {
		this.flag_autori = flag_autori;
	}

	public Boolean getFlag_soggetti() {
		return (flag_soggetti.equals("1") ? true : false);
	}

	public void setFlag_soggetti(String flag_soggetti) {
		this.flag_soggetti = flag_soggetti;
	}

	public Boolean getFlag_classificazioni() {
		return (flag_classificazioni.equals("1") ? true : false);
	}

	public Boolean getFlag_chiedi(){
		return (flag_chiedi.equals("1") ? true : false);
	}

	public void setFlag_chiedi(String flag_chiedi) {
		this.flag_chiedi = flag_chiedi;
	}

	public void setFlag_classificazioni(String flag_classificazioni) {
		this.flag_classificazioni = flag_classificazioni;
	}

	public Boolean useButtonAuthority() {
		return (getFlag_classificazioni() || getFlag_autori());
	}

}
