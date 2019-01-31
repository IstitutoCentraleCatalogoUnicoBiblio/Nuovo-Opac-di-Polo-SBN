package it.almaviva.aggiornaDBPolo.postgreSQL.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

import it.almaviva.aggiornaDBPolo.Solr.CoreType;
import it.almaviva.aggiornaDBPolo.postgreSQL.bean.PoloBean;
//Gestisce JPA per le chiamate al db
public class DBManager {
	private EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ConverterPersistence",
			DBCredentialReader.getCredential());
	private EntityManager entitymanager = emfactory.createEntityManager();

	private static Logger log = Logger.getLogger(DBManager.class);

	public PoloBean getPolo(String cod_polo) {
		//ottiene il polo in base al codice
		if (!entitymanager.getTransaction().isActive())
				entitymanager.getTransaction().begin();
		
		PoloBean polo = entitymanager.find(PoloBean.class, cod_polo);
	//	entitymanager.getTransaction().c
		return polo;
	}

	public Boolean updateDB(PoloBean polo, CoreType core) {
		//Aggiorna il polo al segito del conteggio di solr
		PoloBean p;
		try {
			System.out.println("POLO: " +polo.getCode());
			p = entitymanager.find(PoloBean.class, polo.getCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	//	p.setNrdocs(polo.getNrdocs());
		p = polo;
		entitymanager.merge(p);
		entitymanager.flush();
		entitymanager.getTransaction().commit();
		log.info("Executing update:" + p.getCode());
		return true;
	}

}
