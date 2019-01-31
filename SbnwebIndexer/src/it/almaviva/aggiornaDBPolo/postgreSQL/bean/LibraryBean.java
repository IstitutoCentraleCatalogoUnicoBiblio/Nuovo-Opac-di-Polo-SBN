package it.almaviva.aggiornaDBPolo.postgreSQL.bean;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="biblio")
@Table(name = "biblio")
public class LibraryBean {
	@Id
	private int id;
	
	@Column(name = "name")
	private String name;
	
	
	@Column(name = "cod_bib")
	private String cod_bib;
	
	@Column(name ="link_servizi")
	private String link_servizi;
	
	@Column(name ="cod_anagrafe")
	private String isil;
	@Column(name ="kardex")
	private String kardex;
	@Column(name ="sbnweb")
	private String sbnweb;
	@Column(name ="cod_appl_servizi")
	private String cod_appl_servizi;
	
	@ManyToOne
	@JoinColumn(name="cod_polo", referencedColumnName="cod_polo")
	private PoloBean polo;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCod_bib() {
		return cod_bib.trim();
	}
	public void setCod_bib(String cod_bib) {
		this.cod_bib = cod_bib;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public void setLink_servizi(String link_servizi) {
		this.link_servizi = link_servizi;
	}
	public String getIsil() {
		return isil.toUpperCase();
	}
	public void setIsil(String isil) {
		this.isil = isil;
	}

	public void setKardex(String kardex) {
		this.kardex = kardex;
	}
	public void setSbnweb(String sbnweb) {
		this.sbnweb = sbnweb;
	}
	public String getCod_appl_servizi() {
		return cod_appl_servizi.toUpperCase();
	}
	public void setCod_appl_servizi(String cod_appl_servizi) {
		this.cod_appl_servizi = cod_appl_servizi;
	}
	public Boolean getLink_servizi() {
		return (link_servizi.equals("1") ? true: false);
	}
	public Boolean getKardex() {
		return (kardex.equals("1") ? true: false);
	}
	public Boolean getSbnweb() {
		return (sbnweb.equals("1" ) ? true: false);
	}

	
}
