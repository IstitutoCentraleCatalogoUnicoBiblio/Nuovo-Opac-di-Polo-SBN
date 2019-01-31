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

import org.apache.solr.client.solrj.response.TermsResponse.Term;

import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.pssql.bean.Polo;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

public interface GenerateServerStatus {
	public ServerStatusBean generateServerStatus(SolrResponseBean solrResponse);
	public ServerStatusBean createCustomServerStatus(Integer codice, String messaggio, String descrErrore);
	public ServerStatusBean generateServerStatus(List<Polo> polo);
	public ServerStatusBean generateServerStatus(Polo polo);
	public ServerStatusBean generateTermsServerStatus(List<Term> terms);
	public ServerStatusBean serverStatusFromCode(Integer code);
	public ServerStatusBean generateServerStatusForPosseduto(SbnwebType sbn);
	public ServerStatusBean generateServerStatusForPosseduto(Integer  posseduto);
	public ServerStatusBean generateLogin(Boolean canLogin);
	public ServerStatusBean isDone(Boolean isUpdated);
}
