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
package it.almaviva.pssql.dbManager.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;
import org.springframework.stereotype.Repository;

import it.almaviva.pssql.bean.Biblio;
import it.almaviva.pssql.bean.ClassificazioniDewey;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.pssql.dbManager.InterrogazioneOpacDBInt;

//Classe che usa JPA per la connessione e l'interrogazione del database Postgres Opac
@Repository
public class InterrogazioneOpacDBDao implements InterrogazioneOpacDBInt {
	private static Logger log = Logger.getLogger(InterrogazioneOpacDBDao.class);

	@PersistenceContext
	private EntityManager em;

	/*
	 * @see
	 * it.almaviva.opac.PostgreSQL.dbManager.DatabaseOpac#getSinglePolo(java.
	 * lang.String)
	 */
	@Override
	public Polo getSinglePolo(String codPolo) {
		// fix per la cache

		log.info("Getting polo " + codPolo);
		// Svuoto la cache jpa
		em.getEntityManagerFactory().getCache().evictAll();
		TypedQuery<Polo> q = em.createNamedQuery("Polo.findSingle", Polo.class).setParameter("cod_polo", codPolo);
		q.setHint("javax.persistence.cache.storeMode", "REFRESH");
		// refresh della cache di JPA
		// q.setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache);

		return q.getSingleResult();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.almaviva.opac.PostgreSQL.dbManager.DatabaseOpac#isPolo(java.lang.
	 * String)
	 */
	@Override
	@SuppressWarnings("finally")
	public Boolean isPolo(String codPolo) {
		TypedQuery<Polo> q = em.createQuery("select p from tb_polo p where p.code = '" + codPolo + "'", Polo.class);
		List<Polo> poli = q.getResultList();
		log.info("Checking isPolo");
		Polo poloBean = null;
		try {
			poloBean = poli.get(0);
		} catch (IndexOutOfBoundsException e) {
			log.info("IndexOutOfBoundsException");
			e.printStackTrace();
		} finally {
			if (poloBean != null) {
				log.info("Yes, is Polo: " + poloBean.getCode());
				return true;
			} else {
				log.info("Nope, is not in DB polo: " + codPolo);
				return false;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.almaviva.opac.PostgreSQL.dbManager.DatabaseOpac#getPoloList()
	 */
	@Override
	public List<Polo> getPoloList() {
		TypedQuery<Polo> q = em.createNamedQuery("Polo.findNonDeleted", Polo.class);
		q.setHint("javax.persistence.cache.storeMode", "REFRESH");
		q.setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache);


		List<Polo> poli = q.getResultList();
		log.info("Returning Polo list n: " + poli.size());
		poli.forEach(polo -> {
			log.info(polo.getCode());
		});

		return poli;
	}

	@Override
	public Biblio getBiblio(String cod_polo, String cod_bib) {
		TypedQuery<Biblio> q = em.createNamedQuery("Biblio.findSingle", Biblio.class)
				.setParameter("cod_polo", cod_polo.toUpperCase())
				.setParameter("cod_bib", " " + cod_bib.trim().toUpperCase());

		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<ClassificazioniDewey> getClassificazioniDewey() {
		TypedQuery<ClassificazioniDewey> q = em.createNamedQuery("Classificazioni.findAll", ClassificazioniDewey.class);
		return q.getResultList();
	}

	@Override
	public List<ClassificazioniDewey> getClassificazioniDewey(Integer posizione, String start) {
		
		if (start.equals("")) {
			
			TypedQuery<ClassificazioniDewey> q = em
					.createNamedQuery("Classificazioni.findFromPosizione", ClassificazioniDewey.class)
					.setParameter("posizione", posizione);
			return q.getResultList();

		} else {
			TypedQuery<ClassificazioniDewey> q = em
					.createNamedQuery("Classificazioni.findFromPosizioneAndStart", ClassificazioniDewey.class)
					.setParameter("posizione", posizione).setParameter("start", start);
			return q.getResultList();

		}
	}
}
