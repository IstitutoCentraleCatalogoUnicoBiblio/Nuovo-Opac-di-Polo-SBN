package it.almaviva.aggiornaDBPolo.postgreSQL.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "accesso_mlol")
@Table(name = "accesso_mlol")
public class AccessoMlol {
	@Id
	private String id;
	@Column
	private String api_key;
	@Column
	private String portal_id;

	@OneToOne
	@PrimaryKeyJoinColumn(name = "cod_polo", referencedColumnName = "cod_polo")
	private PoloBean poloreference;

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	public String getPortal_id() {
		return portal_id;
	}

	public void setPortal_id(String portal_id) {
		this.portal_id = portal_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
