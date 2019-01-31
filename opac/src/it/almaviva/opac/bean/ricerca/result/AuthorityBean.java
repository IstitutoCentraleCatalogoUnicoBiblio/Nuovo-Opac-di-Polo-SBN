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
package it.almaviva.opac.bean.ricerca.result;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

public class AuthorityBean {
	
	private Long aviableTitles;
	@Field("id")
	private String idAuth;
	@Field
	private String id_internaz;
	@Field
	private String datazione;
	@Field("unimarc_text")
	private String unimarc;
	@Field("status_voce")
	private String status;
	
	@Field("nome*")
	private Map<String, String> authors;
	
	@Field("regole")
	private List<String> regole;
	
	@Field("lingua")
	private List<String> language;
	@Field("paese")
	private List<String> country;
	@Field("formevar*")
	private Map<String, String> forms;
	@Field("vedianche*")
	private Map<String, String> vedianche;
	@Field("fonti")
	private List<String> fonti;
	@Field("altre_fonti")
	private List<String> altre_fonti;
	
	@Field("nota")
	private List<String> note;
	
	@Field
	private String datazione_ente;
	
	@Field
	private String data_agg;
	

	public String getDatazione_ente() {
		return datazione_ente;
	}

	public void setDatazione_ente(String datazione_ente) {
		this.datazione_ente = datazione_ente;
	}

	public String getData_agg() {
		return data_agg;
	}

	public void setData_agg(String data_agg) {
		this.data_agg = data_agg;
	}

	public String getIdAuth() {
		return idAuth;
	}

	public void setIdAuth(String idAuth) {
		this.idAuth = idAuth;
	}

	public String getId_internaz() {
		return id_internaz;
	}

	public void setId_internaz(String id_internaz) {
		this.id_internaz = id_internaz;
	}

	public String getDatazione() {
		return datazione;
	}

	public void setDatazione(String datazione) {
		this.datazione = datazione;
	}

	public String getUnimarc() {
		return unimarc;
	}

	public void setUnimarc(String unimarc) {
		this.unimarc = unimarc;
	}

	public Long getAviableTitles() {
		return aviableTitles;
	}

	public void setAviableTitles(Long aviableTitles) {
		this.aviableTitles = aviableTitles;
	}

	public List<String> getFonti() {
		return fonti;
	}

	public void setFonti(List<String> fonti) {
		this.fonti = fonti;
	}

	public List<String> getAltre_fonti() {
		return altre_fonti;
	}

	public void setAltre_fonti(List<String> altre_fonti) {
		this.altre_fonti = altre_fonti;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getAuthors() {
		return authors;
	}

	public void setAuthors(Map<String, String> authors) {
		this.authors = authors;
	}

	public List<String> getRegole() {
		return regole;
	}

	public void setRegole(List<String> regole) {
		this.regole = regole;
	}

	public List<String> getLanguage() {
		return language;
	}

	public void setLanguage(List<String> language) {
		this.language = language;
	}

	public List<String> getCountry() {
		return country;
	}

	public void setCountry(List<String> country) {
		this.country = country;
	}

	public Map<String, String> getForms() {
		return forms;
	}

	public void setForms(Map<String, String> forms) {
		this.forms = forms;
	}

	public Map<String, String> getVedianche() {
		return vedianche;
	}

	public void setVedianche(Map<String, String> vedianche) {
		this.vedianche = vedianche;
	}

	public List<String> getFonts() {
		return fonti;
	}

	public void setFonts(List<String> fonts) {
		this.fonti = fonts;
	}

	public List<String> getOther_fonts() {
		return altre_fonti;
	}

	public void setOther_fonts(List<String> other_fonts) {
		this.altre_fonti = other_fonts;
	}

	public List<String> getNote() {
		return note;
	}

	public void setNote(List<String> note) {
		this.note = note;
	}

	
	
}
