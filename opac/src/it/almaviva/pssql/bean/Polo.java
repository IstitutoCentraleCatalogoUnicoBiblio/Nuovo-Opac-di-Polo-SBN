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
import java.util.Comparator;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import it.almaviva.opac.services.PropertiesLoader;
import it.almaviva.utils.opac.Util;
@Cacheable(false)
@Entity(name = "tb_polo")
@Table(name = "tb_polo")
@NamedQueries(value = {
		@NamedQuery(name = "Polo.findSingle", query = "SELECT b FROM tb_polo b where b.code = :cod_polo order by b.code"),
		@NamedQuery(name = "Polo.findAll", query = "SELECT b FROM tb_polo b order by b.code"),
		@NamedQuery(name = "Polo.findNonDeleted", query = "SELECT b FROM tb_polo b where upper(b.fl_canc) <> 'S' order by b.code"), })
public class Polo implements Cloneable {
	@Column(name = "name")
	private String name;

	@Column
	private Integer nrdocs;
	@Id
	@Column(name = "cod_polo")
	private String code;

	@OneToMany(mappedBy = "polo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	//@OrderBy("fl_canc ASC, UPPER(name) ASC")
	private List<Biblio> libraries;

	@OneToMany(mappedBy = "poloreference", fetch = FetchType.LAZY)
	private List<ApplicativoServizi> linkApplicativi;

	@OneToOne(mappedBy = "polo_auth", fetch = FetchType.LAZY)
	private Authority_flags authority_flags;

	@OneToMany(mappedBy = "polo", fetch = FetchType.LAZY)
	private List<CatFruizione> fruizioni;
	@OneToMany(mappedBy = "polo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@OrderBy("tipo_link ASC")
	private List<LinkEsterni> link_esterni;

	@OneToMany(mappedBy = "polo", fetch = FetchType.LAZY)
	private List<MaterialeInventariale> materialeinv;

	@OneToMany(mappedBy = "polo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Gruppi> gruppiBib;

	@Column(name = "username_ws")
	private String username;

	@Column(name = "userpwd_ws")
	private String password;

	@Column(name = "linkrichiesta")
	private String link;

	@Column(name = "link_ws")
	private String linkWebservice;

	@Column(name = "port_solr")
	private String port_solr;

	@Column(name = "host_solr")
	private String host_solr;

	@OneToOne(mappedBy = "poloreference")
	private AccessoMlol accessoMlol;

	@Column(name = "flag_mlol")
	private String flag_mlol;

	@Column(name = "flag_wiki")
	private String flag_wiki;

	@Column(name = "datascarico")
	private String datascarico;

	@Column(name = "nrauth_au")
	private Integer n_doc_auth;

	@Column(name = "email_riferimento")
	private String email_referente;

	@Column(name = "nome_referente")
	private String nome_referente;

	@Column(name = "maps_api_key")
	private String maps_api_key;

	@Column(name = "flag_maps")
	private String flag_maps;

	@Column(name = "fl_canc")
	private String fl_canc;

	@Column(name = "flag_logo")
	private String flag_logo;

	@Column(name = "email_segnalazioni")
	private String email_segnalazioni;

	@Column(name = "numero_referente")
	private String numero_referente;

	public Boolean useLogoImg() {
		return flag_logo.equals("1");
	}

	public void setFlag_logo(String flag_logo) {
		this.flag_logo = flag_logo;
	}

	public List<Gruppi> getGruppiBib() {
		return gruppiBib;
	}

	public void setGruppiBib(List<Gruppi> gruppiBib) {
		this.gruppiBib = gruppiBib;
	}

	public Boolean getFl_canc() {
		return fl_canc.equals("S");
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public String getEmail_referente() {
		return email_referente;
	}

	public void setEmail_referente(String email_referente) {
		this.email_referente = email_referente;
	}

	public String getNome_referente() {
		return nome_referente;
	}

	public void setNome_referente(String nome_referente) {
		this.nome_referente = nome_referente;
	}

	public Integer getN_doc_auth() {
		return n_doc_auth;
	}

	public void setN_doc_auth(Integer n_doc_auth) {
		this.n_doc_auth = n_doc_auth;
	}

	public String getDatascarico() {
		return (datascarico != null) ? datascarico : "";
	}

	public void setDatascarico(String datascarico) {
		this.datascarico = datascarico;
	}

	// cambiamento per evitare di tirare giu tutto il DB nella lista dei poli
	public List<CatFruizione> fruizioni() {
		return fruizioni;
	}

	public void setFruizioni(List<CatFruizione> fruizioni) {
		this.fruizioni = fruizioni;
	}

	// cambiamento per evitare di tirare giu tutto il DB nella lista dei poli
	public List<MaterialeInventariale> materialeInv() {
		return materialeinv;
	}

	public void setMaterialeinv(List<MaterialeInventariale> materialeinv) {
		this.materialeinv = materialeinv;
	}

	public Authority_flags getAuthority_flags() {
		return authority_flags;
	}

	public void setAuthority_flags(Authority_flags authority_flags) {
		this.authority_flags = authority_flags;
	}

	public List<ApplicativoServizi> getLinks() {
		return linkApplicativi;
	}

	public void setLinks(List<ApplicativoServizi> linkApplicativi) {
		this.linkApplicativi = linkApplicativi;
	}

	public String webServiceUrl() {
		return linkWebservice;
	}

	public void setLinkWebservice(String linkWebservice) {
		this.linkWebservice = linkWebservice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNrdocs() {
		return nrdocs;
	}

	public void setNrdocs(Integer nrdocs) {
		this.nrdocs = nrdocs;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// cambiamento per evitare di tirare giu tutto il DB nella lista dei poli
	public List<Biblio> allLibraries() {
		// riorna anche le biblioteche eliminate
		return libraries;
	}

	public List<Biblio> nonDeletedLibraries() {
		// ritorna solo le biblioteche non cancellate in modo logico
		List<Biblio> biblioteche = new ArrayList<Biblio>();
		for (Biblio b : libraries) {
			if (!b.isDeleted())
				biblioteche.add(b);
		}
		biblioteche.sort(Comparator.comparing(Biblio::getFl_canc, String.CASE_INSENSITIVE_ORDER)
							.thenComparing(Biblio::getName, String.CASE_INSENSITIVE_ORDER));
		
		return biblioteche;
	}

	public void setLibraries(List<Biblio> libraries) {
		this.libraries = libraries;
	}

	public String username() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String password() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String url() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public String port_solr() {
		return port_solr;
	}

	public void setPort_solr(String port_solr) {
		this.port_solr = port_solr;
	}

	public String host_solr() {
		return host_solr;
	}

	public void setHost_solr(String host_solr) {
		this.host_solr = host_solr;
	}

	public AccessoMlol mlolCredential() {
		return accessoMlol;
	}

	public void setAccessoMlol(AccessoMlol accessoMlol) {
		this.accessoMlol = accessoMlol;
	}

	public Boolean getFlag_mlol() {
		return flag_mlol.equals("1");
	}

	public void setFlag_mlol(String flag_mlol) {
		this.flag_mlol = flag_mlol;
	}

	public Boolean getFlag_wiki() {
		return flag_wiki.equals("1");
	}

	public void setFlag_wiki(String flag_wiki) {
		this.flag_wiki = flag_wiki;
	}

	public String getMaps_api_key() {
		return ((maps_api_key.equals("") || maps_api_key == null) ? null : maps_api_key);
	}

	public void setMaps_api_key(String maps_api_key) {
		this.maps_api_key = maps_api_key;
	}

	public Boolean getFlag_maps() {
		return flag_maps.equals("1");
	}

	public void setFlag_maps(String flag_maps) {
		this.flag_maps = flag_maps;
	}

	public List<LinkEsterni> getLink_esterni() {
		return link_esterni;
	}

	public void setLink_esterni(List<LinkEsterni> link_esterni) {
		this.link_esterni = link_esterni;
	}

	public String getEmail_segnalazioni() {
		return email_segnalazioni;
	}

	public void setEmail_segnalazioni(String email_segnalazioni) {
		this.email_segnalazioni = email_segnalazioni;
	}

	// Controllo se il polo utilizza un link all interno ddell img di logo
	public Boolean hasLinkLogo() {
		Boolean hasLinkonLogo = false;
		for (LinkEsterni link : link_esterni) {
			if (link.getTipo_link().equals("logo")) {
				hasLinkonLogo = true;
				break;
			}

		}
		return hasLinkonLogo;
	}

	public String getNumero_referente() {
		return numero_referente;
	}

	public void setNumero_referente(String numero_referente) {
		this.numero_referente = numero_referente;
	}
	@Override
	public Polo clone() throws CloneNotSupportedException {
	   try
	   {
		   Polo clonedMyClass = (Polo) super.clone();
	       // if you have custom object, then you need create a new one in here
	       return clonedMyClass ;
	   } catch (CloneNotSupportedException e) {
	       e.printStackTrace();
	       return new Polo();
	   }

	  }
	//Almaviva3 15/05/2019 Manutenzione, parametrizzato numero elementi nella 464
	public String getLunghezza_464() {
		String lunghezza_464_default = "12";
		if(!Util.isFilled(this.code))
			return lunghezza_464_default;
		
		String lungezza464 = PropertiesLoader.getProperty("ELEMENTI_464_" + this.code.toUpperCase());
		return Util.isFilled(lungezza464) ? lungezza464 : lunghezza_464_default;
	}
}
