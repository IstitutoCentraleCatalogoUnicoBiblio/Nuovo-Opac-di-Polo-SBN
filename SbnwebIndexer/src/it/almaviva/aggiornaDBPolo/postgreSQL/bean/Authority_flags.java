package it.almaviva.aggiornaDBPolo.postgreSQL.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "flag_authority")
@Table(name = "flag_authority")
public class Authority_flags {
	@Id
	private String id;
	
	@Column(name = "flag_autori")
	private String flag_autori;
	
	@Column(name = "flag_soggetti")
	private String flag_soggetti;

	@OneToOne
	@JoinColumn(name = "cod_polo", referencedColumnName = "cod_polo" )
	private PoloBean polo_auth;

	public Boolean getFlag_autori() {
		return (flag_autori.equals("1") ? true: false);
	}

	public void setFlag_autori(String flag_autori) {
		this.flag_autori = flag_autori;
	}

	public Boolean getFlag_soggetti() {
		 return (flag_soggetti.equals("1") ? true: false);
	}

	public void setFlag_soggetti(String flag_soggetti) {
		this.flag_soggetti = flag_soggetti;
	}


}
