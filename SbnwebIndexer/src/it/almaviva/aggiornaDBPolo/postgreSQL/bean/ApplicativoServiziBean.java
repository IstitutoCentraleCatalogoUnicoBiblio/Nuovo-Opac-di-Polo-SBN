package it.almaviva.aggiornaDBPolo.postgreSQL.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name="applicativo_servizi")
@Table(name = "applicativo_servizi")
public class ApplicativoServiziBean {

	@Column(name = "cod_polo")
	private String polo;
	
	@Column(name = "descr")
	private String descrizione;
	
	@Column(name = "link_fisso")
	private String url;
	@Id
	@Column(name="id")
	private Integer id;

	@Column(name = "cod_appl")
	private String cod_appl;
	
	@ManyToOne
	@PrimaryKeyJoinColumn (name="cod_polo", referencedColumnName="cod_polo")
	private PoloBean poloreference;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCod_appl() {
		return cod_appl;
	}

	public void setCod_appl(String cod_appl) {
		this.cod_appl = cod_appl;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}


	
}
