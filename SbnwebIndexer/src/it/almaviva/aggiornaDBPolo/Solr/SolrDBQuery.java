package it.almaviva.aggiornaDBPolo.Solr;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

import it.almaviva.aggiornaDBPolo.postgreSQL.bean.PoloBean;
import it.almaviva.sbnweb.util.Constants;

//Classe che serve per interrogare solr, al momento � valida solo per il core BIBLIO
public class SolrDBQuery {

	private SolrQuery query = new SolrQuery();
	private QueryResponse response = new QueryResponse();
	private SolrUrlBuilder urlBuilder = new SolrUrlBuilder();
	private static Logger log = Logger.getLogger(SolrDBQuery.class);

	public PoloBean calculateDocuments(PoloBean polo, String datascarico, CoreType core) {

		try {
			log.info("Counting Solr documents");
			// apertura client http
			SolrServer server = new CommonsHttpSolrServer(urlBuilder.getSolrUrl(polo, core));
			//SolrServer server = new CommonsHttpSolrServer("http://193.206.221.27:8983/solr/biblio_SBW");
			// HttpSolrClient solrClient = new
			// HttpSolrClient(urlBuilder.getSolrUrl(polo, CoreType.BIBLIO));

			// creazione della query *:* in modo da selezionare tutto, potrebbe
			// metterci un po
			query = new SolrQuery("id:*");

			// Esecutione Query
			response = server.query(query);
			// server.
			log.info("Found: " + response.getResults().getNumFound() + " docs");
			if (core == CoreType.BIBLIO)
				polo.setNrdocs((int) response.getResults().getNumFound());
			else if (core == CoreType.AUTHOR)
				polo.setN_doc_auth((int) response.getResults().getNumFound());
			// Fix per scarico parziale
			if (!datascarico.toUpperCase().equals(Constants.no_update_data_scarico_parziale))
				polo.setDatascarico(datascarico);
			
			return polo;
		} catch (Exception e) {
			log.error("Exception solr", e);
			e.printStackTrace();
			return null;
		}

	}
}
