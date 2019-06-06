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
package it.almaviva.utils.opac;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.TermsResponse.Term;

import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.pssql.bean.Polo;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

public class GenerateServerStatusDao implements GenerateServerStatus {
	static Logger log = Logger.getLogger(GenerateServerStatusDao.class);

	// genera e ritorna lo stato del server solr
	public ServerStatusBean generateServerStatus(SolrResponseBean solrResponse) {
		ServerStatusBean server = new ServerStatusBean();
		server = (solrResponse == null) ? buildServerStatus(503) : typeSearch(solrResponse);
		return server;
	}

	// Possibilità di creare un server status custom
	public ServerStatusBean createCustomServerStatus(Integer codice, String messaggio, String descrErrore) {
		ServerStatusBean s = new ServerStatusBean();
		s.setCode(codice);
		s.setError(descrErrore);
		s.setMessage(messaggio);
		return s;
	}

	// costruisce il tipo di messaggio del server in base al tipo di codice
	private ServerStatusBean buildServerStatus(Integer codice) {
		ServerStatusBean s = null;

		// in base al codice passato viene creato un testo di debug
		switch (codice) {
		case 401:
			s = new ServerStatusBean(401, "User or Psw errati", "Authentication Error");
			break;
		case 404:
			s = new ServerStatusBean(404, "La ricerca non ha prodoto risulati", "Not Found");
			break;
		case 420:
			s = new ServerStatusBean(420, "Timeout di connessione", "Timeout");
			break;

		case 200:
			s = new ServerStatusBean(200, "OK", null);
			break;

		case 500:
			s = new ServerStatusBean(500, "Solr o Postgres non sembrano rispondere :(", "Internal Server Error");
			break;
		case 502:
			s = new ServerStatusBean(502, "Postgres non sembra rispondere :(",
					"Il Database non sembra funzionare, contattare l'assistenza");
			break;
		case 503:
			s = new ServerStatusBean(503, "Solr non sembra rispondere :(",
					"Il motore di ricerca non sembra funzionare, contattare l'assistenza");
			break;
		case 504:
			s = new ServerStatusBean(504, "SbnWeb non sembra rispondere :(",
					"Il link di posseduto non ha funzionato correttamente");
			break;
		case 510:
			s = new ServerStatusBean(510, "Testo oppure oggetto mancanti", "Text or subject missing");
			break;
		case 511:
			s = new ServerStatusBean(511, "Indirizzo email non corretto", "Bad email");
			break;
		case 512:
			s = new ServerStatusBean(511, "Indirizzo email non corretto", "Bad email");
			break;
		default:
			s = new ServerStatusBean(codice, "E' successo qualcosa", "Codice non presente!");

		}

		return s;
	}

	// genera e ritorna lo stato del server
	public ServerStatusBean generateServerStatus(List<Polo> polo) {
		ServerStatusBean server = new ServerStatusBean();
		if ((polo == null)) {
			// db è tornato nullo quindi non risponde e si è verificato un
			// errore interno
			server = buildServerStatus(502);
		} else {
			server = buildServerStatus(200);

		}

		return server;
	}

	public ServerStatusBean generateServerStatus(Polo polo) {
		ServerStatusBean server = new ServerStatusBean();
		if ((polo == null)) {
			// db è tornato nullo quindi non risponde e si è verificato un
			// errore interno
			server = buildServerStatus(502);
		} else {
			server = buildServerStatus(200);

		}

		return server;
	}

	
	@Override
	public ServerStatusBean generateTermsServerStatus(List<Term> terms) {
		ServerStatusBean server = new ServerStatusBean();
		if ((terms == null)) {
			// db è tornato nullo quindi non risponde e si è verificato un
			// errore interno
			server = buildServerStatus(503);
		} else {
			server = buildServerStatus(200);

		}
		return server;
	}

	private ServerStatusBean typeSearch(SolrResponseBean solrResponse) {
		ServerStatusBean server = new ServerStatusBean();
		switch (solrResponse.getType()) {
		case biblio_SYNTETIC:
			server = (solrResponse.getSintetica().size() == 0) ? buildServerStatus(404) : buildServerStatus(200);
			break;
		case biblio_DETAIL:
			server = (solrResponse.getDocs().size() == 0) ? buildServerStatus(404) : buildServerStatus(200);
			break;
		case author_SYNTETIC:
			server = (solrResponse.getAutori().size() == 0) ? buildServerStatus(404) : buildServerStatus(200);

			break;
		case author_DETAIL:
			server = (solrResponse.getAutori().size() == 0) ? buildServerStatus(404) : buildServerStatus(200);

			break;
		}
		return server;
	}

	@Override
	public ServerStatusBean serverStatusFromCode(Integer code) {
		// TODO Auto-generated method stub
		return buildServerStatus(code);
	}

	@Override
	public ServerStatusBean generateServerStatusForPosseduto(SbnwebType sbn) {
		if (sbn == null) {
			return buildServerStatus(500);
		} else {
			return buildServerStatus(200);
		}
	}

	@Override
	public ServerStatusBean generateServerStatusForPosseduto(Integer posseduto) {
		if (posseduto == null) {
			return buildServerStatus(500);
		} else {
			return buildServerStatus(200);
		}
	}

	@Override
	public ServerStatusBean generateLogin(Boolean canLogin) {
		//se puoi loggare quindi credenziali esatte, 200 altrimenti non sei autorizzatos
		return (canLogin) ? buildServerStatus(200) : buildServerStatus(401);
	}

	@Override
	public ServerStatusBean isDone(Boolean isUpdated) {
		
		return (isUpdated) ? buildServerStatus(200) : buildServerStatus(512);
	}

}
