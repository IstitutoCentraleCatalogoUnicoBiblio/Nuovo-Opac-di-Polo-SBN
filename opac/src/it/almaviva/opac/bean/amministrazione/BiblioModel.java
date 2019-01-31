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

import it.almaviva.pssql.bean.Gruppi;

public class BiblioModel {

	private Integer id;

	private String cod_bib, cod_polo, name, cod_appl_servizi, isil;

	private Boolean kardex, sbnweb, link_servizi, deleted, flag_logo;

	private List<Gruppi> gruppi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCod_bib() {
		return cod_bib;
	}

	public void setCod_bib(String cod_bib) {
		this.cod_bib = " " + cod_bib.trim().toUpperCase();
	}

	public String getLink_servizi() {
		return (link_servizi) ? "1" : "0";
	}

	public void setLink_servizi(Boolean link_servizi) {
		this.link_servizi = link_servizi;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKardex(Boolean kardex) {
		this.kardex = kardex;
	}

	public String getSbnweb() {
		return (sbnweb) ? "1" : "0";
	}

	public void setSbnweb(Boolean sbnweb) {
		this.sbnweb = sbnweb;
	}

	public List<Gruppi> getGruppi() {
		return gruppi;
	}

	public void setGruppi(List<Gruppi> gruppi) {
		this.gruppi = gruppi;
	}

	public String getCod_appl_servizi() {
		return cod_appl_servizi;
	}

	public void setCod_appl_servizi(String cod_appl_servizi) {
		this.cod_appl_servizi = cod_appl_servizi;
	}

	public String getKardex() {
		return (kardex) ? "1" : "0";
	}

	public String getDeleted() {
		return (deleted)? "S" :"N";
	}

	public void setIsNewBib(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getIsil() {
		return isil;
	}

	public void setIsil(String isil) {
		this.isil = isil;
	}

	public String getFlag_logo() {
		return (flag_logo)? "1" :"0";
	}

	public void setFlag_logo(Boolean flag_logo) {
		this.flag_logo = flag_logo;
	}

}
