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

import java.util.List;

import org.apache.solr.client.solrj.response.TermsResponse.Term;

import it.almaviva.opac.bean.ricerca.result.CoreType;
import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchTerm;
import it.almaviva.opac.classificazioniDewey.ClassificazioneType;
import it.almaviva.pssql.bean.ClassificazioniDewey;

public interface SolrQueryExecuteInterface {

	SolrResponseBean searchRun(SearchBean search, String codPolo);
	SolrResponseBean searchAuth(SearchBean search, String codPolo);
	List<Term> searchTerms(SearchTerm search, String codPolo, CoreType coreType);
	
	SolrResponseBean novita(String codPolo, String dateFrom);
	List<ClassificazioniDewey> classificazioniDewey(List<ClassificazioniDewey> deweys, String cod_polo, ClassificazioneType tipoClasse);
	List<ClassificazioniDewey> classificazioniDewey(List<ClassificazioniDewey> deweys, String cod_polo, ClassificazioneType tipoClasse, String start);


}
