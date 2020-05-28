package it.almaviva.aggiornaDBPolo.main;
//it.almaviva.aggiornaDBPolo.main.AggiornaDatabasePoloMain
import org.apache.log4j.Logger;

import it.almaviva.aggiornaDBPolo.Solr.CoreType;
import it.almaviva.aggiornaDBPolo.Solr.SolrDBQuery;
import it.almaviva.aggiornaDBPolo.postgreSQL.DBConnection.DBManager;
import it.almaviva.aggiornaDBPolo.postgreSQL.bean.PoloBean;

//Classe main che aggiorna il polo, il try{}catch() � stato fatto solo per lavorare in debug, 
//prende in input da riga di comando il codice del polo, sotto il comando con cui richiamarlo
public class AggiornaDatabasePoloMain {
	// java -jar SbnwebIndexer.jar SBW biblio data aggiornamento
	private static DBManager dbManager = new DBManager();
	private static SolrDBQuery solrConnection = new SolrDBQuery();
	private static Logger log = Logger.getLogger(AggiornaDatabasePoloMain.class);

	public static void main(String[] args) {
		try {
			log.info("Connected to Database....");
			log.info("Selected Polo: " + args[0] +" starting update ");
			
			
			//1.ottiene l'oggetto polo da postgres
			//2.interroga solr e calcola il numero dei documenti
			//3. aggiorna il db
			PoloBean polo = dbManager.getPolo(args[0]);
			CoreType core = CoreType.getCoreType(args[1]);
			
			dbManager.updateDB(solrConnection.calculateDocuments(polo, args[2] , core), core);	
			
			log.info("Database updated successfully!!");
		} catch (ArrayIndexOutOfBoundsException e) {
			log.info("Ops, some was wrog no polo selected!!");
			PoloBean polo = dbManager.getPolo("SBW");
			CoreType core = CoreType.getCoreType("biblio");
			
			dbManager.updateDB(solrConnection.calculateDocuments(polo,"domani", core), core);

			e.printStackTrace();
		}
	}

}
