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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import BibliotecheImporter.BibliotecheImporter.App;
import BibliotecheImporter.Statistiche.Statistiche;
import BibliotecheImporter.binderJson.beans.Biblioteca;
import BibliotecheImporter.binderJson.beans.Contatto;
import dbManager.connectionManager.DBConnManager;

public class DatabaseQueryDao {
	private DBConnManager db = new DBConnManager();
	private Logger log = Logger.getLogger(App.class);
	private Statistiche statistiche = new Statistiche();

	public Statistiche getStatistiche() {
		return statistiche;
	}

	private String getGoogleApiKey(String cod_polo) {
		String api_key = new String("");
		String querySearch = "SELECT flag_maps, maps_api_key FROM tb_polo WHERE cod_polo ='" + cod_polo + "';";
		// String querySearch = "select * from biblio where cod_bib = ' TR'";
		Connection conn = db.openConnection();
		try {
			// log.info("Ottengo la lista delle biblioteche nel DB OPAC..");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(querySearch);
			// log.info(rs);

			while (rs.next()) {
				
				api_key = rs.getString("maps_api_key");
				
			}
		} catch (SQLException e) {
			log.info("Exception lista bib db opac: " + e.getMessage());
		}
		db.closeConnection();
		return (api_key.equals("")) ? null : api_key;
	}

	public List<BibliotecheInDBBean> getLibraries() {
		List<BibliotecheInDBBean> bibliotecheOpac = new ArrayList<BibliotecheInDBBean>();
		String querySearch = "SELECT * FROM biblio";
		// String querySearch = "select * from biblio where cod_bib = ' TR'";
		Connection conn = db.openConnection();
		try {
			log.info("Ottengo la lista delle biblioteche nel DB OPAC..");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(querySearch);
			// log.info(rs);
			while (rs.next()) {
				bibliotecheOpac.add(new BibliotecheInDBBean(rs.getString("cod_polo"), rs.getString("cod_bib").trim()));
				statistiche.newPolo(rs.getString("cod_polo"));
			}
			statistiche.setTotaleBibInDbOpac(bibliotecheOpac.size());
		} catch (SQLException e) {
			log.info("Exception lista bib db opac: " + e.getMessage());
		}
		db.closeConnection();
		return (bibliotecheOpac.size() == 0) ? null : bibliotecheOpac;
	}

	public void delete(String cod_polo, String cod_bib) {
		// aggiunto spazio perchè la tabella lo richiede
		cod_bib = " " + cod_bib.trim();
		// connessione
		Connection conn = db.openConnection();
		String queryDeleteDettagli = "DELETE FROM biblio_dettagli WHERE cod_bib = '" + cod_bib + "' AND cod_polo = '"
				+ cod_polo + "';";
		String queryDeleteContatti = "DELETE FROM biblio_contatti WHERE cod_bib = '" + cod_bib + "' AND cod_polo = '"
				+ cod_polo + "';";
		try {
			log.trace("Eliminando dettagli per:" + cod_polo + cod_bib);
			Statement st = conn.createStatement();
			int n = st.executeUpdate(queryDeleteDettagli);
			log.trace("Success!");
		} catch (SQLException e) {
			// eccezione la query non ritorna risultati
			log.info("Exception lista bib: " + e.getMessage());
		}
		try {
			log.trace("Eliminando contatti per: " + cod_polo + cod_bib);
			Statement st = conn.createStatement();
			int n = st.executeUpdate(queryDeleteContatti);
			log.trace("Success!");
		} catch (SQLException e) {
			// eccezione la query non ritorna risultati
			log.info("Exception lista bib: " + e.getMessage());
		}
		db.closeConnection();

	}

	public void insert(Biblioteca bibIns) {
	//LATITUDINE E LONGITUDINE SONO INVERTITI SUL DB
		Connection conn = db.openConnection();
		String insertDettagli = "";
		try {
			if (bibIns.getLocation().getLong().equals("") || bibIns.getLocation().getLat().equals(""))
				addCoordFromGoogle(bibIns);

			insertDettagli = "INSERT INTO biblio_dettagli" + " VALUES " + "(nextval('biblio_dettagli_id_seq')," + " ' "
					+ bibIns.getCodici_dentificativi().getCodBib() + "', '"
					+ bibIns.getCodici_dentificativi().getIsil().replaceAll("IT-", "") + "', '"
					+ bibIns.getCodici_dentificativi().getCodPolo() + "', '"
					+ bibIns.getLocation().getIndirizzo().replaceAll("'", "''") + "', '" + bibIns.getLocation().getCap()
					+ "', '" + bibIns.getLocation().getComune().getNome().replaceAll("'", "''") + "', '"
					+ bibIns.getLocation().getProvincia().getNome().replaceAll("'", "''") + "', '"
					+ bibIns.getLocation().getLong() + "', '" + bibIns.getLocation().getLat() + "', now())";
		} catch (ArrayIndexOutOfBoundsException e1) {
			// TODO Auto-generated catch block
			// statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getSbn()).incrementErrors();

			e1.printStackTrace();
		}
		try {
			log.trace("Inserendo biblio_dettagli per: " + bibIns.getCodici_dentificativi().getSbn());
			Statement st = conn.createStatement();
			if (bibIns.getLocation().getLong().equals("") || bibIns.getLocation().getLat().equals(""))
				statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getCodPolo()).incrementErrors();

			int n = st.executeUpdate(insertDettagli);

			statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getCodPolo()).incrementDettagli();
			log.trace("Success!" + n);
			// incremento errore

		} catch (SQLException e) {
			// eccezione la query non ritorna risultati
			log.info("La query non ha restituito alcun risultato ");
		} catch (ArrayIndexOutOfBoundsException e) {
			log.info("ArrayIndexOutOfBoundsException: " + insertDettagli);
			statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getSbn()).incrementErrors();

			// e.printStackTrace();
		}
		log.trace("Inserendo biblio_contatti per: " + bibIns.getCodici_dentificativi().getSbn());
		for (Contatto contatto : bibIns.getContatti()) {
			try {
				log.trace("Inserendo contatto...");
				String insertContatto = "INSERT INTO biblio_contatti VALUES (nextval('biblio_contatti_id_seq'), ' "
						+ bibIns.getCodici_dentificativi().getCodBib() + "', '"
						+ bibIns.getCodici_dentificativi().getCodPolo() + "','" + contatto.getTipo() + "', '"
						+ contatto.getValore() + "', now());";

				Statement st = conn.createStatement();
				int n = st.executeUpdate(insertContatto);
				statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getCodPolo()).incrementContatti();
				log.trace("Success!");
			} catch (SQLException e) {
				// eccezione la query non ritorna risultati
				log.info("Exception contatto: " + e.getMessage());
				statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getCodPolo()).incrementErrors();

			}
		}
		log.trace("Success! Contatti inseriti: ");
		db.closeConnection();

	}

	private void addCoordFromGoogle(Biblioteca bibIns) {
		log.info("Ottengo coordinate da servizi Google Maps per " + bibIns.getCodici_dentificativi().getSbn());
		String api_key = getGoogleApiKey(bibIns.getCodici_dentificativi().getCodPolo());
		if(api_key == null) {
			return ;
		}
			
		GeoApiContext context = new GeoApiContext()
				.setApiKey(api_key);
		GeocodingResult[] results;
		try {
			results = GeocodingApi
					.geocode(context, bibIns.getLocation().getIndirizzo() + ", " + bibIns.getLocation().getComune().getNome())
					.await();
			// Gson gson = new GsonBuilder().setPrettyPrinting().create();
			// LAT PER PRIMA
			bibIns.getLocation().getCoordinate().add(results[0].geometry.location.lat);
			// long per seconda
			bibIns.getLocation().getCoordinate().add(results[0].geometry.location.lng);
			log.info("Coordinate ottenute da google per " + bibIns.getCodici_dentificativi().getSbn());
			statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getCodPolo()).incrementFromGoogleMaps();
			// System.out.println(gson.toJson(results[0].geometry.location));
		} catch (Exception e) {
			statistiche.getStatsPolo(bibIns.getCodici_dentificativi().getCodPolo()).incrementErrorGoogleMaps();
			log.info("Errore richiesta Maps, impossibile ottenere le mappe per "
					+ bibIns.getCodici_dentificativi().getSbn(), e);
		}

	}
}
