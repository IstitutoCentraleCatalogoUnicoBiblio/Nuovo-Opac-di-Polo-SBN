package it.almaviva.aggiornaDBPolo.Solr;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import it.almaviva.aggiornaDBPolo.postgreSQL.DBConnection.DBManager;
import it.almaviva.aggiornaDBPolo.postgreSQL.bean.PoloBean;

//Questa classe genera l'url di connessione a solr prendendo i dati dal DB 
public class SolrUrlBuilder {
	// costanti di url
	private final String protocol = "http";
	private final String solr = "solr";
	private final String biblio = "biblio";
	private final String auth = "authority";
	private final String sogg = "soggetti";
	private Logger log = LoggerFactory.getLogger(SolrUrlBuilder.class);
	private String urlSolr = new String();

	// DB Manager
	DBManager dbManager = new DBManager();
	private String biblioCoreBuilder(PoloBean polo) {

		return protocol + "://" + polo.host_solr().trim() + ":" + polo.port_solr().trim() + "/" + solr + "/" + biblio
				+ "_" + polo.getCode().trim() ;
	}

	private String authCoreBuilder(PoloBean polo) {
		return protocol + "://" + polo.host_solr().trim() + ":" + polo.port_solr().trim() + "/" + solr + "/" + auth
				+ "_" + polo.getCode().trim() ;
	}

	private String soggCoreBuilder(PoloBean polo) {
		return protocol + "://" + polo.host_solr().trim() + ":" + polo.port_solr().trim() + "/" + solr + "/" + sogg
				+ "_" + polo.getCode().trim() ;
	}

	// rende accessibile dall'esterno l'url costruito
	public String getSolrUrl(PoloBean polo, CoreType coreType) {
		// ottengo i dati dal DB per il polo
	//	PoloBean poloObj = dbManager.getPolo(polo);
		
		// pulisco la cache e genero un nuovo url in base al tipo di core
		switch (coreType) {
		case BIBLIO:
			urlSolr = biblioCoreBuilder(polo);
			break;
		case AUTHOR:
			urlSolr = authCoreBuilder(polo);
			break;
		case SOGGETTI:
			urlSolr = soggCoreBuilder(polo);
			break;
		default:
			//in caso di default cerco su biblio "nel dubbio nega"
			urlSolr = biblioCoreBuilder(polo);
		}

		log.info("Solr core: " + coreType.toString() + " URL: " + urlSolr);
		return urlSolr;
	}
}
