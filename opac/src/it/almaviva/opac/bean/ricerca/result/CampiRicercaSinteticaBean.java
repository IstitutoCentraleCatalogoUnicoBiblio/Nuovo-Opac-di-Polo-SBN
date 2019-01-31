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

import org.apache.solr.client.solrj.beans.Field;

public class CampiRicercaSinteticaBean {

	@Field("autore")
	private String author;
	@Field("coautore")
	private List<String> coautore;

	@Field("faparte_tit") //colltit_tip_461_new 
	private List<String> faparte_monografia;
	@Field("colltit_tip_463_new") //colltit_tip_463_new 
	private List<String> faparte_spoglio;
	@Field("pre_titolo")
	private String pre_title;
	@Field("titolo_sint")
	private String syntetic_title;

	@Field("publish")
	private String publish;
	@Field
	private String level;
	@Field("tiporec")
	private String recordType;
	@Field("lingua")
	private List<String> language;
	@Field("dataf")
	private List<String> date;

	@Field("id")
	private String id;

	@Field
	private String unimarc;

	public List<String> getCoautore() {
		return coautore;
	}

	public void setCoautore(List<String> coautore) {
		this.coautore = coautore;
	}

	public String getUnimarc() {
		return unimarc;
	}

	public void setUnimarc(String unimarc) {
		this.unimarc = unimarc;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	public List<String> getFaparte_monografia() {
		return faparte_monografia;
	}

	public void setFaparte_monografia(List<String> faparte_monografia) {
		this.faparte_monografia = faparte_monografia;
	}

	public List<String> getFaparte_spoglio() {
		return faparte_spoglio;
	}

	public void setFaparte_spoglio(List<String> faparte_spoglio) {
		this.faparte_spoglio = faparte_spoglio;
	}

	public String getPre_title() {
		return pre_title;
	}

	public void setPre_title(String pre_title) {
		this.pre_title = pre_title;
	}

	public String getSyntetic_title() {
		return syntetic_title;
	}

	public void setSyntetic_title(String syntetic_title) {
		this.syntetic_title = syntetic_title;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public List<String> getLanguage() {
		return language;
	}

	public void setLanguage(List<String> language) {
		this.language = language;
	}

	public List<String> getDate() {
		return date;
	}

	public void setDate(List<String> date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
