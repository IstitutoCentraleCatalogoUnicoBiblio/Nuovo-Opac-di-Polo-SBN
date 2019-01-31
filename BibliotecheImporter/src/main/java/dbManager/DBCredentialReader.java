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
package dbManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import utils.Constants;


//legge el credenziali in un file situato nella home  utente!
public class DBCredentialReader {
	private static Logger log = Logger.getLogger(DBCredentialReader.class);

	//Legge la property richiesta vedere la classe Constants
	private static String getCredential(String property ) {
		Properties p = new Properties();
		String url = System.getProperty("user.home") + "/"+Constants.credentialDBfileName +".properties";
		try {
			log.trace("Reading property: " + property);
			
			//System.out.println("URL properties: " + url);
			p.load(new FileInputStream(url));
			String propData = p.getProperty(property);
			//System.out.println(property + " data: " + propData);
			return propData;
		} catch (FileNotFoundException e) {
			log.trace("Failed Read property: " + property, e);
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			log.trace("Failead Read property: " + property, e);
			e.printStackTrace();
			return "";
		}
	
	} 
	//ritrona la mappa con le credenziali per l'accesso al database
	public static Map<String, String> getCredential () {
		Map<String, String> credential = new HashMap<String, String>();
		credential.put(Constants.db_url, getCredential(Constants.db_url));
		credential.put(Constants.db_user, getCredential(Constants.db_user));
		credential.put(Constants.db_password, getCredential(Constants.db_password));
		return credential;
	}
}
