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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

//field da ritornare dopo una ricerca
public class CampiRicercaDetailBean {
	private List<Boolean> authorAviables = new ArrayList<Boolean>();
	@Field
	private String id;
	@Field("autore")
	private String author;

	@Field("titolo_sint")
	private String syntetic_title;

	@Field
	private String unimarc;

	@Field("soggetto_tot")
	private List<String> soggetto;

	@Field
	private String publish;

	@Field
	private List<String> tag977;

	@Field
	private List<String> tag950;
	@Field("faparte_tit")
	private List<String> isPartOf;
	@Field("materia")
	private List<String> materia;
	@Field
	private List<String> nome_tot;
	@Field
	private List<String> nome_view;
	@Field
	private List<String> dewey_code;

	@Field
	private List<String> dewey_des;

	@Field("dewey_*")
	private Map<String, String> dewey;

	@Field("paese")
	private List<String> country;
	@Field("lingua")
	private List<String> language;
	@Field("dataf")
	private List<String> date;

	@Field("level")
	private String level;

	@Field("tiporec")
	private String recordType;

	@Field("pre_titolo")
	private String pre_title;

	@Field
	private List<String> descr;
	@Field
	private List<String> edition;
	// New Campi
	@Field("av_*")
	private Map<String, String> av;

	@Field("disegno_*")
	private Map<String, String> draw;
	@Field("formato_elet_*")
	private Map<String, String> formato_elet;
	@Field("grafica_*")
	private Map<String, String> graphics;
	@Field("organico_*")
	private Map<String, String> organics;
	@Field("rappre*")
	private Map<String, String> rappresentation;

	@Field("colltit_tip_*_new")
	private Map<String, String> colltit_tip;

	@Field("nome")
	private List<String> name;
	@Field("tipo_elab")
	private List<String> tipo_elab;
	@Field("incipit")
	private List<String> incipit;
	@Field("data_comp")
	private List<String> data_comp;

	@Field("titolo_*")
	private Map<String, String> titles;

	@Field("*")
	private Map<String, String> stored;
	@Field("abstract")
	private List<String> abstract_;

	@Field("nota*")
	private Map<String, String> note;

	@Field("isbn")
	private List<String> isbn;
	@Field("issn")
	private List<String> issn;
	@Field("ismn")
	private List<String> ismn;
	@Field("bni")
	private List<String> bni;
	@Field("numeri_tip_*")
	private Map<String, String> numbers_tip;
	@Field("altritit_tip_517")
	private List<String> otherTitles;
	@Field("forma")
	private List<String> form;
	@Field("tonalita")
	private List<String> tonality;
	@Field("pres")
	private List<String> presentation;
	@Field("impronta")
	private List<String> imprint;
	@Field("marca")
	private List<String> mark;
	@Field("numerazione")
	private String numeration;
	@Field("collezione_id")
	private List<String> collections;
	// cartografia
	@Field("cart_*")
	private Map<String, String> cartografia;

	@Field("mat_inv_950")
	private List<String> mat_inv_950;

	@Field("spogli967")
	private Long spogli967;

	@Field("monografie967")
	private Long monografie967;

	@Field("classi_PGI_686_tot")
	private List<String> classi_PGI_686_tot;

	//almaviva3 MAD export unimarc v10.3.2 per gestione del SET verso SUBSET
	public String getLeader() {
		String leader = unimarc.substring(7,(7+24));
		return leader;
	}
	
	public List<String> getClassi_PGI_686_tot() {
		return classi_PGI_686_tot;
	}

	public void setClassi_PGI_686_tot(List<String> classi_PGI_686_tot) {
		this.classi_PGI_686_tot = classi_PGI_686_tot;
	}

	public Long getSpogli967() {
		return spogli967;
	}

	public void setSpogli967(Long spogli967) {
		this.spogli967 = spogli967;
	}

	public Long getMonografie967() {
		return monografie967;
	}

	public void setMonografie967(Long monografie967) {
		this.monografie967 = monografie967;
	}

	public List<String> getMat_inv_950() {
		return mat_inv_950;
	}

	public void setMat_inv_950(List<String> mat_inv_950) {
		this.mat_inv_950 = mat_inv_950;
	}

	public Map<String, String> getCartografia() {
		return cartografia;
	}

	public void setCartografia(Map<String, String> cartografia) {
		this.cartografia = cartografia;
	}

	public List<String> getMateria() {
		return materia;
	}

	public void setMateria(List<String> materia) {
		this.materia = materia;
	}

	public Map<String, String> getFormato_elet() {
		return formato_elet;
	}

	public void setFormato_elet(Map<String, String> formato_elet) {
		this.formato_elet = formato_elet;
	}

	public void pushAviable(Boolean aviable) {
		authorAviables.add(aviable);
	}

	public List<Boolean> getAuthorAviables() {
		return authorAviables;
	}

	public void setAuthorAviables(List<Boolean> authorAviables) {
		this.authorAviables = authorAviables;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSyntetic_title() {
		return syntetic_title;
	}

	public void setSyntetic_title(String syntetic_title) {
		this.syntetic_title = syntetic_title;
	}

	public String getUnimarc() {
		return unimarc;
	}

	public void setUnimarc(String unimarc) {
		this.unimarc = unimarc;
	}

	public List<String> getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(List<String> soggetto) {
		this.soggetto = soggetto;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public List<String> getTag977() {
		return tag977;
	}

	public void setTag977(List<String> tag977) {
		this.tag977 = tag977;
	}

	public List<String> getTag950() {
		return tag950;
	}

	public void setTag950(List<String> tag950) {
		this.tag950 = tag950;
	}

	public List<String> getIsPartOf() {
		return isPartOf;
	}

	public void setIsPartOf(List<String> isPartOf) {
		this.isPartOf = isPartOf;
	}

	public List<String> getNome_tot() {
		return nome_tot;
	}

	public void setNome_tot(List<String> nome_tot) {
		this.nome_tot = nome_tot;
	}

	public List<String> getDewey_code() {
		return dewey_code;
	}

	public void setDewey_code(List<String> dewey_code) {
		this.dewey_code = dewey_code;
	}

	public List<String> getDewey_des() {
		return dewey_des;
	}

	public void setDewey_des(List<String> dewey_des) {
		this.dewey_des = dewey_des;
	}

	public Map<String, String> getDewey() {
		return dewey;
	}

	public void setDewey(Map<String, String> dewey) {
		this.dewey = dewey;
	}

	public List<String> getCountry() {
		return country;
	}

	public void setCountry(List<String> country) {
		this.country = country;
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

	public String getPre_title() {
		return pre_title;
	}

	public void setPre_title(String pre_title) {
		this.pre_title = pre_title;
	}

	public List<String> getDescr() {
		return descr;
	}

	public void setDescr(List<String> descr) {
		this.descr = descr;
	}

	public List<String> getEdition() {
		return edition;
	}

	public void setEdition(List<String> edition) {
		this.edition = edition;
	}

	public Map<String, String> getAv() {
		return av;
	}

	public void setAv(Map<String, String> av) {
		this.av = av;
	}

	public Map<String, String> getDraw() {
		return draw;
	}

	public void setDraw(Map<String, String> draw) {
		this.draw = draw;
	}

	public Map<String, String> getGraphics() {
		return graphics;
	}

	public void setGraphics(Map<String, String> graphics) {
		this.graphics = graphics;
	}

	public Map<String, String> getOrganics() {
		return organics;
	}

	public void setOrganics(Map<String, String> organics) {
		this.organics = organics;
	}

	public Map<String, String> getRappresentation() {
		return rappresentation;
	}

	public void setRappresentation(Map<String, String> rappresentation) {
		this.rappresentation = rappresentation;
	}

	public Map<String, String> getColltit_tip() {
		return colltit_tip;
	}

	public void setColltit_tip(Map<String, String> colltit_tip) {
		this.colltit_tip = colltit_tip;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	public List<String> getTipo_elab() {
		return tipo_elab;
	}

	public void setTipo_elab(List<String> tipo_elab) {
		this.tipo_elab = tipo_elab;
	}

	public List<String> getIncipit() {
		return incipit;
	}

	public void setIncipit(List<String> incipit) {
		this.incipit = incipit;
	}

	public List<String> getData_comp() {
		return data_comp;
	}

	public void setData_comp(List<String> data_comp) {
		this.data_comp = data_comp;
	}

	public Map<String, String> getTitles() {
		return titles;
	}

	public void setTitles(Map<String, String> titles) {
		this.titles = titles;
	}

	public Map<String, String> getStored() {
		return null;
		// return stored;
	}

	public void setStored(Map<String, String> stored) {
		this.stored = stored;
	}

	public List<String> getAbstract_() {
		return abstract_;
	}

	public void setAbstract_(List<String> abstract_) {
		this.abstract_ = abstract_;
	}

	public Map<String, String> getNote() {
		return note;
	}

	public void setNote(Map<String, String> note) {
		this.note = note;
	}

	public List<String> getIsbn() {
		return isbn;
	}

	public void setIsbn(List<String> isbn) {
		this.isbn = isbn;
	}

	public List<String> getIssn() {
		return issn;
	}

	public void setIssn(List<String> issn) {
		this.issn = issn;
	}

	public List<String> getIsmn() {
		return ismn;
	}

	public void setIsmn(List<String> ismn) {
		this.ismn = ismn;
	}

	public List<String> getBni() {
		return bni;
	}

	public void setBni(List<String> bni) {
		this.bni = bni;
	}

	public Map<String, String> getNumbers_tip() {
		return numbers_tip;
	}

	public void setNumbers_tip(Map<String, String> numbers_tip) {
		this.numbers_tip = numbers_tip;
	}

	public List<String> getOtherTitles() {
		return otherTitles;
	}

	public void setOtherTitles(List<String> otherTitles) {
		this.otherTitles = otherTitles;
	}

	public List<String> getForm() {
		return form;
	}

	public void setForm(List<String> form) {
		this.form = form;
	}

	public List<String> getTonality() {
		return tonality;
	}

	public void setTonality(List<String> tonality) {
		this.tonality = tonality;
	}

	public List<String> getPresentation() {
		return presentation;
	}

	public void setPresentation(List<String> presentation) {
		this.presentation = presentation;
	}

	public List<String> getImprint() {
		return imprint;
	}

	public void setImprint(List<String> imprint) {
		this.imprint = imprint;
	}

	public List<String> getMark() {
		return mark;
	}

	public void setMark(List<String> mark) {
		this.mark = mark;
	}

	public String getNumeration() {
		return numeration;
	}

	public void setNumeration(String numeration) {
		this.numeration = numeration;
	}

	public List<String> getCollections() {
		return collections;
	}

	public void setCollections(List<String> collections) {
		this.collections = collections;
	}

	public List<String> getNome_view() {
		return nome_view;
	}

	public void setNome_view(List<String> nome_view) {
		this.nome_view = nome_view;
	}

}
