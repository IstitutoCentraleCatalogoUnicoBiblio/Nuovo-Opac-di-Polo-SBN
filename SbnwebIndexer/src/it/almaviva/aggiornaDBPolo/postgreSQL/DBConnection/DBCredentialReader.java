package it.almaviva.aggiornaDBPolo.postgreSQL.DBConnection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.almaviva.sbnweb.util.Constants;

//legge el credenziali in un file situato nella home  utente!
public class DBCredentialReader {
	private static Logger log = Logger.getLogger(DBCredentialReader.class);
	private static Properties props = null;
	//Legge la property richiesta vedere la classe Constants
	private static String getCredential(String property ) {
		
		try {
			initializeProperties();
			
			log.info("Reading property: " + property);
			//System.out.println("URL properties: " + url);
			
			String propData = props.getProperty(property);
			return propData;
		} catch (FileNotFoundException e) {
			log.info("Failed Read property: " + property, e);
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			log.info("Failead Read property: " + property, e);
			e.printStackTrace();
			return "";
		}
	
	}
	private static void initializeProperties() throws IOException, FileNotFoundException {
		if(props == null) {
			log.info("Initializing....");
			props = new Properties();
			String url = System.getProperty("user.home") + "/"+Constants.credentialDBfileName +".properties";
			props.load(new FileInputStream(url));

		}
	} 
	//ritrona la mappa con le credenziali per l'accesso al database
	public static Map<String, String> getCredential () {
		Map<String, String> credential = new HashMap<String, String>();
		credential.put("javax.persistence.jdbc.url", getCredential(Constants.db_url));
		credential.put("javax.persistence.jdbc.user", getCredential(Constants.db_user));
		credential.put("javax.persistence.jdbc.password", getCredential(Constants.db_password));
		credential.put("javax.persistence.jdbc.schema", getCredential(Constants.db_schema));
		log.info("Properties readed. Connecting to DB... waiting.");
		return credential;
	}
}
