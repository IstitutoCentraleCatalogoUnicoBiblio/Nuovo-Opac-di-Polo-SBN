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
package it.almaviva.opac.solr;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.almaviva.opac.bean.ricerca.result.CoreType;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.pssql.bean.Polo;

@Repository
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
	@Autowired
	InterrogazioneOpacDBServices dbManager = new InterrogazioneOpacDBServices();

	private String biblioCoreBuilder(Polo polo) {

		return protocol + "://" + polo.host_solr().trim() + ":" + polo.port_solr().trim() + "/" + solr + "/" + biblio
				+ "_" + polo.getCode().trim() + "/";
	}

	private String authCoreBuilder(Polo polo) {
		return protocol + "://" + polo.host_solr().trim() + ":" + polo.port_solr().trim() + "/" + solr + "/" + auth
				+ "_" + polo.getCode().trim() + "/";
	}

	private String soggCoreBuilder(Polo polo) {
		return protocol + "://" + polo.host_solr().trim() + ":" + polo.port_solr().trim() + "/" + solr + "/" + sogg
				+ "_" + polo.getCode().trim() + "/";
	}

	// rende accessibile dall'esterno l'url costruito
	public String getSolrUrl(String polo, CoreType coreType) {
		// ottengo i dati dal DB per il polo
		Polo poloObj = dbManager.getSinglePolo(polo);
		
		// pulisco la cache e genero un nuovo url in base al tipo di core
		switch (coreType) {
		case BIBLIO:
			urlSolr = biblioCoreBuilder(poloObj);
			break;
		case AUTHOR:
			urlSolr = authCoreBuilder(poloObj);
			break;
		case SOGGETTI:
			urlSolr = soggCoreBuilder(poloObj);
			break;
		default:
			//in caso di default cerco su biblio "nel dubbio nega"
			urlSolr = biblioCoreBuilder(poloObj);
		}

		log.info("Solr core: " + coreType.toString() + " URL: " + urlSolr);
		return urlSolr;
	}
}
