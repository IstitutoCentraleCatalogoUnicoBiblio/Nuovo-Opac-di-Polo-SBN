package it.almaviva.aggiornaDBPolo.postgreSQL.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "tb_polo")
@Table(name = "tb_polo")
public class PoloBean {
	@Column(name = "name")
	private String name;

	@Column
	private Integer nrdocs;
	@Id
	@Column(name = "cod_polo")
	private String code;

	@OneToMany(mappedBy = "polo")
	private List<LibraryBean> libraries;

	@OneToMany(mappedBy = "poloreference")
	private List<ApplicativoServiziBean> linkApplicativi;

	@OneToOne(mappedBy = "polo_auth")
	private Authority_flags authority_flags;

	@OneToMany(mappedBy = "polo")
	private List<CatFruizione> fruizioni;

	@OneToMany(mappedBy = "polo")
	private List<MaterialeInventariale> materialeinv;

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
	
	@Column(name = "maps_api_key")
	private String maps_api_key;
	
	@Column(name = "flag_maps")
	private String flag_maps;

	
	public Integer getN_doc_auth() {
		return n_doc_auth;
	}

	public void setN_doc_auth(Integer n_doc_auth) {
		this.n_doc_auth = n_doc_auth;
	}

	public String getDatascarico() {
		return datascarico;
	}

	public void setDatascarico(String datascarico) {
		this.datascarico = datascarico;
	}

	public List<CatFruizione> getFruizioni() {
		return fruizioni;
	}

	public void setFruizioni(List<CatFruizione> fruizioni) {
		this.fruizioni = fruizioni;
	}

	public List<MaterialeInventariale> getMaterialeinv() {
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

	public List<ApplicativoServiziBean> getLinks() {
		return linkApplicativi;
	}

	public void setLinks(List<ApplicativoServiziBean> linkApplicativi) {
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

	public List<LibraryBean> getLibraries() {
		return libraries;
	}

	public void setLibraries(List<LibraryBean> libraries) {
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

}
