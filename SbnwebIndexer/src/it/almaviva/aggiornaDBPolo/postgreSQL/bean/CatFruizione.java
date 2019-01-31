package it.almaviva.aggiornaDBPolo.postgreSQL.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "cat_fruizione950")
@Table( name = "cat_fruizione950")
public class CatFruizione {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "cod_polo")
	private String code;
	
	@Column(name = "cod_cat")
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cod == null) ? 0 : cod.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatFruizione other = (CatFruizione) obj;
		if (cod == null) {
			if (other.cod != null)
				return false;
		} else if (!cod.equals(other.cod))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	} 
	

	
}
