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
package dbManager.connectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import dbManager.DBCredentialReader;
import utils.Constants;

public class DBConnManager {
	private static Logger log = Logger.getLogger(DBConnManager.class);

	  private Connection conn = null;
	  
	//apre connessione
	public  Connection openConnection() {
		Map<String, String> credential  = DBCredentialReader.getCredential();
		try {
			log.trace("Connecting to database...");
			Class.forName(Constants.pg_driver);	 
			conn = DriverManager.getConnection(credential.get(Constants.db_url), credential.get(Constants.db_user), credential.get(Constants.db_password));
			log.trace("Database connection Opened");

		} catch(ClassNotFoundException e) {
			//classe driver mysql non trovata
			log.trace("404 Driver NOT found " + e.getMessage());
		} catch (SQLException e) {
			//problema di connessione DB
			log.trace("SQL: " + e.getMessage());
		}
		
		return conn;		
	}
	//Chiusura DB
		public void closeConnection() {
			try {
				if(!conn.isClosed()) {

					conn.close();
					log.trace("Database connection Closed");

				} 
			} catch (SQLException e) {
				log.trace("SQL: " + e.getMessage());
			}
		}
}
