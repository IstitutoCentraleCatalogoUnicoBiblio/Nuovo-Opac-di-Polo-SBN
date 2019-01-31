package it.almaviva.aggiornaDBPolo.postgreSQL.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "materialeinventariale")
@Table ( name = "materialeinventariale")
public class MaterialeInventariale {
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "cod_polo")
	private String code;
	
	@Column(name = "cod_matinv")
	private String cod;
	
	@Column(name = "desc_it")
	private String descr_it;
	
	@Column(name = "desc_en")
	private String descr_en;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="cod_polo", referencedColumnName="cod_polo")
	private PoloBean polo;

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDescr_it() {
		return descr_it;
	}

	public void setDescr_it(String descr_it) {
		this.descr_it = descr_it;
	}

	public String getDescr_en() {
		return descr_en;
	}

	public void setDescr_en(String descr_en) {
		this.descr_en = descr_en;
	}
	
	
}
