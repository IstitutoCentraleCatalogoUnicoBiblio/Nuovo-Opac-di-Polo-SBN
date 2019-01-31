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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.almaviva.opac.bean.ricerca.result.CoreType;
import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchTerm;
import it.almaviva.opac.classificazioniDewey.ClassificazioneType;
import it.almaviva.opac.solr.SolrQueryExecuteInterface;
import it.almaviva.opac.solr.dao.SolrQueryExecuteDao;
import it.almaviva.pssql.bean.ClassificazioniDewey;

@Repository
public class SolrQueryExecuteServices implements SolrQueryExecuteInterface {

	@Autowired
	SolrQueryExecuteDao queryRunner = new SolrQueryExecuteDao();

	@Override
	public SolrResponseBean searchRun(SearchBean search, String codPolo) {

		return queryRunner.searchRun(search, codPolo);
	}

	@Override
	public List<Term> searchTerms(SearchTerm search, String codPolo, CoreType coreType) {

		return queryRunner.searchTerms(search, codPolo, coreType);
	}

	@Override
	public SolrResponseBean searchAuth(SearchBean search, String codPolo) {

		return queryRunner.searchAuth(search, codPolo);
	}

	@Override
	public SolrResponseBean novita(String codPolo, String dateFrom) {

		return queryRunner.novita(codPolo, dateFrom);
	}

	@Override
	public List<ClassificazioniDewey> classificazioniDewey(List<ClassificazioniDewey> deweys, String cod_polo,
			ClassificazioneType tipoClasse) {
		return queryRunner.classificazioniDewey(deweys, cod_polo, tipoClasse, "");
	}

	@Override
	public List<ClassificazioniDewey> classificazioniDewey(List<ClassificazioniDewey> deweys, String cod_polo,
			ClassificazioneType tipoClasse, String start) {
		return queryRunner.classificazioniDewey(deweys, cod_polo, tipoClasse, start);
	}

}
