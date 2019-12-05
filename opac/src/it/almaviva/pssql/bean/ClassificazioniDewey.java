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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.solr.client.solrj.response.FacetField.Count;
@Cacheable(false)

@Entity(name = "classificazioni")
@Table(name = "classificazioni")
@NamedQueries(value = {
		@NamedQuery(name = "Classificazioni.findAll", query = "SELECT cd FROM classificazioni cd ORDER BY cd.classe"),
		@NamedQuery(name = "Classificazioni.findFromPosizione", query = "SELECT cd FROM classificazioni cd WHERE cd.posizione = :posizione ORDER BY cd.classe"),
		@NamedQuery(name = "Classificazioni.findFromPosizioneAndStart", query = "SELECT cd FROM classificazioni cd WHERE cd.posizione = :posizione AND cd.classe LIKE CONCAT( :start, '%') ORDER BY cd.classe")

})
public class ClassificazioniDewey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "classe")
	private String classe;

	// TODO: traduzione

	@Column(name = "descr")
	private String descrizione;

	@Column(name = "posizione")
	private Integer posizione;
	@Transient
	private Long notizie;

	public String getClasse() {
		return classe;
	}

	public ClassificazioniDewey() {
		super();
	}

	public ClassificazioniDewey(String classe, String descrizione, Long notizie, Integer posizione) {
		super();
		this.classe = classe;
		this.descrizione = descrizione;
		this.notizie = notizie;
		this.posizione = posizione;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getPosizione() {
		return posizione;
	}

	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}

	public int getId() {
		return id;
	}

	public String getTraslationKey() {
		return "dew_" + this.getClasse();
	}

	public Long getNotizie() {
		return notizie;
	}

	public void setNotizie(Long notizie) {
		this.notizie = notizie;
	}

	public static void setNumNotizie(List<ClassificazioniDewey> deweys, Count facet_count) {
		deweys.forEach(dewey -> {
			if (dewey.getClasse().equals(facet_count.getName()))
				dewey.setNotizie(facet_count.getCount());
		});
	}

	public static List<ClassificazioniDewey> newList(List<Count> list) {
		List<ClassificazioniDewey> deweys = new ArrayList<>();
		list.forEach(facet -> {
			String dewey_descr = descrizioneBuild(facet.getName());
			String dewey_code = deweyCodeBuild(facet.getName());
			Integer alreadyFound = ClassificazioniDewey.getIndexFromClasse(deweys, dewey_code);
			if (alreadyFound > -1) {
				// TODO Controllo edizione
				if (ClassificazioniDewey.checkEdition(deweys.get(alreadyFound).getDescrizione(), dewey_descr))
					deweys.get(alreadyFound).setDescrizione(dewey_descr);

				deweys.get(alreadyFound).setNotizie(deweys.get(alreadyFound).getNotizie() + facet.getCount());
			} else {
				deweys.add(new ClassificazioniDewey(dewey_code, dewey_descr, facet.getCount(), 4));
			}
		});
		return deweys;
	};

	private static String descrizioneBuild(String dewey_tot) {
		return dewey_tot.substring((dewey_tot.indexOf("|"))).replace("|", "");
	}

	private static String deweyCodeBuild(String dewey_tot) {
		return dewey_tot.substring(0, (dewey_tot.indexOf("|") + 1)).replace("|", "");

	}

	private static Boolean checkEdition(String foundDescription, String currentDescription) {
		
		foundDescription = foundDescription + " ";
		currentDescription = currentDescription + " ";
		String foundEdition = foundDescription.substring(0, foundDescription.indexOf(" ")).replace("ed.", "").trim();
		String currentEdition = currentDescription.substring(0, currentDescription.indexOf(" ")).replace("ed.", "")
				.trim();
		int compareTo = currentEdition.compareTo(foundEdition);
		return compareTo > -1;
	}

	private static Integer getIndexFromClasse(List<ClassificazioniDewey> list, String classeToFind) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getClasse().equals(classeToFind))
				return i;
		}
		return -1;
	}
}
