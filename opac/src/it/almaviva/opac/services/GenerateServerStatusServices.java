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
package it.almaviva.opac.services;

import java.util.List;

import org.apache.solr.client.solrj.response.TermsResponse.Term;

import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.utils.opac.GenerateServerStatus;
import it.almaviva.utils.opac.GenerateServerStatusDao;
import it.almaviva.utils.opac.ServerStatusBean;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

public class GenerateServerStatusServices implements GenerateServerStatus {
	private GenerateServerStatusDao serverStatus = new GenerateServerStatusDao();

	@Override
	public ServerStatusBean generateServerStatus(SolrResponseBean solrResponse) {
		return serverStatus.generateServerStatus(solrResponse);
	}

	@Override
	public ServerStatusBean createCustomServerStatus(Integer codice, String messaggio, String descrErrore) {
		return serverStatus.createCustomServerStatus(codice, messaggio, descrErrore);
	}

	@Override
	public ServerStatusBean generateServerStatus(List<Polo> polo) {
		return serverStatus.generateServerStatus(polo);
	}

	@Override
	public ServerStatusBean generateServerStatus(Polo polo) {
		return serverStatus.generateServerStatus(polo);
	}

	@Override
	public ServerStatusBean generateTermsServerStatus(List<Term> terms) {
		return serverStatus.generateTermsServerStatus(terms);
	}

	@Override
	public ServerStatusBean serverStatusFromCode(Integer code) {
		return serverStatus.serverStatusFromCode(code);
	}

	@Override
	public ServerStatusBean generateServerStatusForPosseduto(SbnwebType sbn) {
		return serverStatus.generateServerStatusForPosseduto(sbn);
	}

	@Override
	public ServerStatusBean generateServerStatusForPosseduto(Integer posseduto) {
		return serverStatus.generateServerStatusForPosseduto(posseduto);
	}

	@Override
	public ServerStatusBean generateLogin(Boolean canLogin) {
		return serverStatus.generateLogin(canLogin);
	}

	@Override
	public ServerStatusBean isDone(Boolean isUpdated) {
		return serverStatus.isDone(isUpdated);
	}

}
