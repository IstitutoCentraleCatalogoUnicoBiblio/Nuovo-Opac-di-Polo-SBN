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
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Cacheable(false)
@Entity(name = "biblio")
@Table(name = "biblio")
@NamedQueries(value = {
		@NamedQuery(name = "Biblio.findAll", query = "SELECT b FROM biblio b where b.polo.code = :cod_polo"),
		@NamedQuery(name = "Biblio.findNonDeleted", query = "SELECT b FROM biblio b where b.polo.code = :cod_polo and b.fl_canc <> 'S'"),
		@NamedQuery(name = "Biblio.findSingle", query = "SELECT b FROM biblio b WHERE b.polo.code = :cod_polo AND b.cod_bib = :cod_bib")
})
public class Biblio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "cod_bib")
	private String cod_bib;

	@Column(name = "link_servizi")
	private String link_servizi;

	@Column(name = "fl_canc")
	private String fl_canc;
	@Column(name = "cod_anagrafe")
	private String isil;

	@Column(name = "kardex")
	private String kardex;

	@Column(name = "posseduto")
	private String sbnweb;
	@Column(name = "cod_appl_servizi")
	private String cod_appl_servizi;

	@Column(name = "flag_logo")
	private String flag_logo;
	@ManyToOne
	@JoinColumn(name = "cod_polo", referencedColumnName = "cod_polo")
	private Polo polo;

	@OneToMany(mappedBy = "biblioteca")
	private List<BiblioContatto> contatti;

	@OneToOne(mappedBy = "bibReference")
	private BiblioDettagli dettaglio;

	@ManyToMany // prima la join che referenzia a te e poi agli altri
	@JoinTable(name = "rel_bibliogruppi", joinColumns = @JoinColumn(name = "id_biblio"), inverseJoinColumns = @JoinColumn(name = "id_gruppi"))
	private List<Gruppi> gruppi;
	
	public List<Gruppi> getGruppi() {
		List<Gruppi> grupsNoBib = new ArrayList<>();
		for (Gruppi gruppo : gruppi) {
			Gruppi gruToadd = new Gruppi(gruppo.getId(), gruppo.getName(), gruppo.getCod_polo());
			grupsNoBib.add(gruToadd);
		}
		return grupsNoBib;
	}

	public Integer getNGruppiAppartenenza() {
		return gruppi.size();
	}

	public void setGruppi(List<Gruppi> gruppi) {
		this.gruppi = gruppi;
	}

	public BiblioDettagli getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(BiblioDettagli dettaglio) {
		this.dettaglio = dettaglio;
	}

	public List<BiblioContatto> getContatti() {
		return contatti;
	}

	public void setContatti(List<BiblioContatto> contatti) {
		this.contatti = contatti;
	}

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
		return isil;
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
		return (link_servizi.equals("1") ? true : false);
	}

	public Boolean getKardex() {
		return (kardex.equals("1") ? true : false);
	}

	public Boolean getSbnweb() {
		return (sbnweb.equals("1") ? true : false);
	}

	public String polo() {
		return polo.getCode();
	}
	public String email_ResponsabilePolo() {
		return polo.getEmail_referente();
	}
	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}
	public String getFl_canc() {
		return this.fl_canc;
	}
	public Boolean isDeleted() {
		return fl_canc.toUpperCase().equals("S");
	}

	public void setPolo(Polo polo) {
		this.polo = polo;
	}

	public Boolean getFlag_logo() {
		return (flag_logo.equals("1") ? true : false);
	}

	public void setFlag_logo(String flag_logo) {
		this.flag_logo = flag_logo;
	}

}
