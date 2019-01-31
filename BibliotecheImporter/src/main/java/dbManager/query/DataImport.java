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
package dbManager.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import BibliotecheImporter.Statistiche.Statistiche;
import BibliotecheImporter.binderJson.beans.Biblioteca;
import BibliotecheImporter.binderJson.beans.CodiciIdentificativi;
import BibliotecheImporter.binderJson.beans.Contatto;
import BibliotecheImporter.binderJson.beans.Denominazioni;
import BibliotecheImporter.binderJson.beans.Indirizzo;
import BibliotecheImporter.binderJson.beans.ProvComune;

public class DataImport {
	private DatabaseQueryDao dbq = new DatabaseQueryDao();
	private List<BibliotecheInDBBean> bibliotecheOpac;
	
	// private Integer nBibUpdated = 0;
	private Logger log = Logger.getLogger(DataImport.class);

	public DataImport() {
		bibliotecheOpac = dbq.getLibraries();

	}

	public void importData(Biblioteca bibToImport) {
		// decisione di importare o meno
		 //bibToImport = createFakeBib();
		if (bibToImport.getCodici_dentificativi().getSbn() == null) {
			// log.info("SBN: null");
			return;
		}

		try {
			String cod_polo = bibToImport.getCodici_dentificativi().getCodPolo();
			String cod_bib = bibToImport.getCodici_dentificativi().getCodBib();
			if (isToImport(cod_polo, cod_bib)) {
				log.info("Importing: " + cod_polo + " " + cod_bib);
				dbq.delete(cod_polo, cod_bib);
				dbq.insert(bibToImport);
				// Metodo che gestisce le statististiche

			} else {
				// log.info("Is NOT to import & update ");

			}
		} catch (NullPointerException e) {
			log.info("NullPointerException", e);
		}

	}

	private Boolean isToImport(String cod_polo, String cod_bib) {
		boolean flag_polo = false;
		boolean flag_bib = false;
		// BibliotecheInDBBean bib = new BibliotecheInDBBean(cod_polo, cod_bib);
		for (BibliotecheInDBBean bib : bibliotecheOpac) {
			String polo = bib.getCod_polo().trim().toUpperCase();
			if (polo.equals(cod_polo.trim().toUpperCase())) {
				flag_polo = true;
				break;
			}
		}
		for (BibliotecheInDBBean bib : bibliotecheOpac) {
			if (bib.getCod_bib().trim().toLowerCase().equals(cod_bib.trim().toLowerCase())) {
				flag_bib = true;
				break;
			}
		}

		return (flag_bib && flag_polo);
	}
	public String cleanedStats() {
		Statistiche stats = dbq.getStatistiche();
		StringBuilder stBuild = new StringBuilder();
		stBuild.append("Totale biblioteche in DB opac:" + stats.getTotaleBibInDbOpac());
		stats.getStatistichePolo().entrySet().forEach(statPolo -> {
			stBuild.append("\n" + statPolo.getKey() + ": " + statPolo.getValue().toString());
		});
		return stBuild.toString();
	}
	// biblioteca finta per prove singole
	private Biblioteca createFakeBib() {
		Contatto contattoFake = new Contatto();
		contattoFake.setTipo("E-mail");
		contattoFake.setValore("mail@mail.it");
		Contatto contattoFake2 = new Contatto();
		contattoFake2.setTipo("Telefono");
		contattoFake2.setValore("333333333");
		Denominazioni fDen = new Denominazioni();
		fDen.setUfficiale("Biblioteca di Test. ProvaImporter");
		CodiciIdentificativi ci = new CodiciIdentificativi();
		ci.setSbn("SBWFI");
		ci.setIsil("RM9999");

		ProvComune c = new ProvComune();
		c.setNome("Roma");
		ProvComune p = new ProvComune();
		p.setNome("RM");
		List<Double> coords = new ArrayList<Double>();
		coords.add(12.5555);
		coords.add(41.1222);

		Indirizzo ind = new Indirizzo();
		ind.setCap("00139");
		ind.setComune(c);
		ind.setProvincia(p);
		ind.setIndirizzo("Via di casa mia");
		ind.setRegione("Lazio");
		ind.setCoordinate(coords);

		Biblioteca fake = new Biblioteca();
		fake.setCodici_dentificativi(ci);
		fake.setContatti(new ArrayList<Contatto>());
		fake.getContatti().add(contattoFake);
		fake.getContatti().add(contattoFake2);
		fake.setDenominazioni(fDen);
		fake.setLocation(ind);

		return fake;

	}
}
