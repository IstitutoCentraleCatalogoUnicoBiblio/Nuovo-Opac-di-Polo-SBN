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
package it.almaviva.opac.solr.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.almaviva.opac.bean.ricerca.result.AuthorityBean;
import it.almaviva.opac.bean.ricerca.result.CampiRicercaDetailBean;
import it.almaviva.opac.bean.ricerca.result.CampiRicercaSinteticaBean;
import it.almaviva.opac.bean.ricerca.result.CoreType;
import it.almaviva.opac.bean.ricerca.result.FaccettaBean;
import it.almaviva.opac.bean.ricerca.result.Order;
import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.result.SolrReturnType;
import it.almaviva.opac.bean.ricerca.result.SottoFaccettaBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchTerm;
import it.almaviva.opac.classificazioniDewey.ClassificazioneType;
import it.almaviva.opac.services.ConverterCampiServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.opac.solr.SolrQueryBuilder;
import it.almaviva.opac.solr.SolrQueryExecuteInterface;
import it.almaviva.opac.solr.SolrUrlBuilder;
import it.almaviva.pssql.bean.ClassificazioniDewey;

//Interrogazione di solr
@Repository
public class SolrQueryExecuteDao implements SolrQueryExecuteInterface {

	static Logger log = Logger.getLogger(SolrQueryExecuteDao.class);

	// classi solr
	private SolrClient solrClient;
	private SolrQuery query = new SolrQuery();
	private ConverterCampiServices conv = new ConverterCampiServices();

	// classi custom
	private SolrResponseBean model = new SolrResponseBean();

	private SolrQueryBuilder queryBuilder = new SolrQueryBuilder();
	@Autowired
	private SolrUrlBuilder urlBuilder = new SolrUrlBuilder();
	@Autowired
	private InterrogazioneOpacDBServices dbManager = new InterrogazioneOpacDBServices();
	// faccette
	private final Integer MAX_FACET = 50;
	private final Integer MIN_FACET_COUNT = 1;
	private final List<String> facets = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("formato_eletf");
			add("level");
			add("tiporec");
			add("nomef");
			add("relator_codef");
			//add("nomef_noposs");
			//add("relator_codef_noposs");		
			add("possessoref");
			add("soggettof");
			add("dewey_code");
			add("luogof");
			add("editoref");
			add("dataf");
			add("collezionef");
			add("lingua");
			add("titolo_uniformef");
			add("formaf");
			add("tag977");

		}
	};

	private SolrQuery addFacet(SolrQuery query) {
		facets.forEach(facet -> {
			query.addFacetField(facet.toLowerCase());
		});

		query.setFacetLimit(MAX_FACET);
		query.setFacetMinCount(MIN_FACET_COUNT);
		log.info("Added " + facets.size() + " facets to query");
		return query;
	}

	@Override
	public SolrResponseBean searchRun(SearchBean search, String codPolo) {
		// richiamo metodo di esecuzione della chiamata a solr
		return executeQuery(queryBuilder.queryBuilder(search.getFilters(), CoreType.BIBLIO), search, codPolo,
				CoreType.BIBLIO);
	}

	@Override
	public SolrResponseBean searchAuth(SearchBean search, String codPolo) {
		return executeQueryAuth(queryBuilder.queryBuilder(search.getFilters(), CoreType.AUTHOR), search, codPolo,
				CoreType.AUTHOR);
	}

	@Override
	public List<Term> searchTerms(SearchTerm search, String codPolo, CoreType coreType) {
		return searchTermsRun(queryBuilder.queryFrase(search.getField(), search.getValue()), search, codPolo, coreType);
	}

	private SolrQuery sort(SolrQuery query, String fieldToSort, String orderType) {
		log.info("Adding sort " + fieldToSort + " " + orderType);

		switch (fieldToSort.toLowerCase()) {
		case "autore":
			query.addSort("autore", Order.order(orderType));
			query.addSort("titolo_sint", Order.order(orderType));
			query.addSort("dataa", Order.order(orderType));

			break;
		case "syntetic_title":
			query.addSort("titolo_sint", Order.order(orderType));
			query.addSort("autore", Order.order(orderType));
			query.addSort("dataa", Order.order(orderType));
			break;
		case "dataa":
			query.addSort("dataFine", Order.order(orderType));
			query.addSort("titolo_sint", Order.order(orderType));
			query.addSort("autore", Order.order(orderType));
			break;
		case "datada":
			query.addSort("dataInizio", Order.order(orderType));
			query.addSort("titolo_sint", Order.order(orderType));
			query.addSort("autore", Order.order(orderType));
			break;
		case "score":
			query.addSort("score", Order.order(Order.DESC));
			query.addSort("titolo_sint", Order.order(orderType));
			query.addSort("autore", Order.order(orderType));
			break;
		case "rilevanza":
			query.addSort("score", Order.order(Order.DESC));
			query.addSort("titolo_sint", Order.order(orderType));
			query.addSort("autore", Order.order(orderType));
			break;
		case "nascita":
			query.addSort("score", Order.order(orderType));

			break;
		case "nomeauth":
			query.addSort("nomesort", Order.order(orderType));
			break;
		case "data_nascita_asc":
			query.addSort("data_nascita1", ORDER.asc);
			query.addSort("nomesort", ORDER.asc);
			break;
		case "data_nascita_desc":
			query.addSort("data_nascita1", ORDER.desc);
			query.addSort("nomesort", ORDER.asc);
			break;
		default:
			if (fieldToSort.toUpperCase().indexOf("_ASC") > -1) {
				query.addSort(fieldToSort.substring(0, fieldToSort.toUpperCase().indexOf("_ASC")).toString(),
						ORDER.asc);

			} else if (fieldToSort.toUpperCase().indexOf("_DESC") > -1) {
				query.addSort(fieldToSort.substring(0, fieldToSort.toUpperCase().indexOf("_DESC")).toString(),
						ORDER.desc);

			} else {
				query.addSort(fieldToSort, ORDER.asc);

			}

		}
		return query;
	}

	// esecuzione della chiamata solr
	private SolrResponseBean executeQueryAuth(String queryStr, SearchBean searchList, String codPolo,
			CoreType coreType) {

		model = new SolrResponseBean();
		log.info("Searching on Solr Auth Docs for " + codPolo);
		if (!dbManager.getSinglePolo(codPolo).getAuthority_flags().getFlag_autori()) {
			log.info("Polo " + codPolo + " non abilitato allla ricerca di autori");
			return null;
		}

		try {
			// apertura client http
			solrClient = new HttpSolrClient(urlBuilder.getSolrUrl(codPolo, coreType));

			// creazione della query
			query = new SolrQuery(queryStr);

			// aggiunta delle impostazioni di query: sort righe e inizio

			query.setRows(searchList.getMaxRows());
			query.setStart(searchList.getStart());
			// query = sort(query, searchList.getSort(), searchList.getOrder());
			query = sort(query, searchList.getSort(), searchList.getOrder());

			// Esecutione Query
			QueryResponse response = solrClient.query(query);

			List<AuthorityBean> autori = new ArrayList<AuthorityBean>();
			
			log.info("Trovati autori:" + response.getResults().getNumFound());
			
			// richiesto dettaglio
			autori = response.getBeans(AuthorityBean.class);
			solrClient.close();
			autori.forEach(autore -> {
				solrClient = new HttpSolrClient(urlBuilder.getSolrUrl(codPolo, CoreType.BIBLIO));
				query = new SolrQuery("nome_tot:" + autore.getIdAuth());
				try {
					QueryResponse localResponse = solrClient.query(query);
					autore.setAviableTitles(localResponse.getResults().getNumFound());
				} catch (RemoteSolrException | SolrServerException | IOException e) {
					log.info("Exception on gettting titles of author");
					// e.printStackTrace();
				}

				// autore.setAviableTitles(123);
			});
			// costruzione dei risultati di ritorno
			model.setAutori(autori);
			model.setNumFound(response.getResults().getNumFound());
			model.setSintetica(null);
			model.setType(SolrReturnType.author_SYNTETIC);
			// model.setFacc(lFacc);
			model.setFacc(null);
			model.setDocs(null);
			model.setQuery(queryBuilder.normalizeQuery(queryStr));

			// eccezioni varie
		} catch (RemoteSolrException | SolrServerException | IOException | IndexOutOfBoundsException
				| NullPointerException | BindingException e) {

			log.info("ECCEZIONE ricerca Autori", e);

			model = null;

		}

		return model;
	}

	// esecuzione della chiamata solr
	private SolrResponseBean executeQuery(String queryStr, SearchBean searchList, String codPolo, CoreType coreType) {
		model = new SolrResponseBean();
		log.info("Searching on Solr Document Biblio for " + codPolo);
		try {

			// apertura client http
			solrClient = new HttpSolrClient(urlBuilder.getSolrUrl(codPolo, coreType));
			// creazione della query
			query = new SolrQuery(queryStr);

			// aggiunta delle impostazioni di query: sort righe e inizio

			query.setRows(searchList.getMaxRows());
			query.setStart(searchList.getStart());
			query = sort(query, searchList.getSort(), searchList.getOrder());

			// FACCETTE
			if (!searchList.getIsDetail())
				query = addFacet(query);
			List<FaccettaBean> lFacc = new ArrayList<FaccettaBean>();
			// Esecutione Query
			//Almaviva3 17/06/2019 URI-TOO Long passata a chiamata post per modifica spostamento forma
			  QueryRequest queryRequest=new QueryRequest(query);
	            queryRequest.setMethod(METHOD.POST);
	          //  NamedList nl=solrClient.request(queryRequest);
	            NamedList<Object> nl=solrClient.request(queryRequest);
	            QueryResponse response =new QueryResponse(nl, solrClient);
			// faccette
			if (!searchList.getIsDetail()) {

				for (FacetField facet : response.getFacetFields()) {
					List<SottoFaccettaBean> lSottFacc = new ArrayList<SottoFaccettaBean>();

					for (int c = 0; c < facet.getValues().size(); c++) {
						SottoFaccettaBean sApp = new SottoFaccettaBean(facet.getValues().get(c).getName(),
								facet.getValues().get(c).getCount());
						lSottFacc.add(sApp);
					}
					FaccettaBean app = new FaccettaBean(facet.getName(), facet.getValueCount(), lSottFacc);
					lFacc.add(app);
				}
			}

			// documenti trovati
			List<CampiRicercaDetailBean> documenti = new ArrayList<CampiRicercaDetailBean>();
			List<CampiRicercaSinteticaBean> sintetica = new ArrayList<CampiRicercaSinteticaBean>();

			if (searchList.getIsDetail() == false) {
				// richiesta sintetica
				sintetica = response.getBeans(CampiRicercaSinteticaBean.class);

				// costruzione dei risultati di ritorno
				model.setDocs(null);
				model.setNumFound(response.getResults().getNumFound());
				model.setSintetica(sintetica);
				model.setFacc(lFacc);
				model.setType(SolrReturnType.biblio_SYNTETIC);
				// model.setQuery(query.getQuery());
				model.setQuery(queryStr);
			} else {
				// richiesto dettaglio
				documenti = response.getBeans(CampiRicercaDetailBean.class);
				solrClient.close();
				if (dbManager.getSinglePolo(codPolo).getAuthority_flags().getFlag_autori()) {
					documenti.forEach(documento -> {
						solrClient = new HttpSolrClient(urlBuilder.getSolrUrl(codPolo, CoreType.AUTHOR));
						try {
							documento.getNome_tot().forEach(name -> {
								try {
									String vid = name.substring(name.indexOf("|") + 1, name.indexOf("|") + 11);
									SolrQuery q = new SolrQuery("id:" + vid);
									log.info("Autore: " + vid);
									QueryResponse localResponse = solrClient.query(q);

									Boolean aviable = (localResponse.getResults().getNumFound() > 0) ? true : false;
									documento.pushAviable(aviable);
								} catch (RemoteSolrException | SolrServerException | IOException
										| IndexOutOfBoundsException e) {
									log.info("Exception on gettting titles of author: " + e.getMessage());

								}
							});
						} catch (NullPointerException e) {
							log.info("Nomi non presenti");

						}
						// query = new SolrQuery("nome_tot:"
						// +autore.getIdAuth());

					});
				} else {
					log.info("Polo non abilitato alla ricerca su authority");
				}
				// costruzione dei risultati di ritorno
				model.setDocs(documenti);
				model.setNumFound(response.getResults().getNumFound());
				model.setSintetica(null);
				model.setType(SolrReturnType.biblio_DETAIL);
				// model.setFacc(lFacc);
				model.setQuery(queryStr);
			}

			// eccezioni varie
		} catch (RemoteSolrException | SolrServerException | IOException | IndexOutOfBoundsException
				| NullPointerException | BindingException e) {

			log.error("ECCEZIONE ricerca Documenti: ", e);
			model = null;

		}

		return model;
	}

	private List<Term> searchTermsRun(String queryStr, SearchTerm request, String codPolo, CoreType coreType) {
		List<Term> term = null;
		log.info("Searching on Solr Term for " + codPolo);
		try {

			// apertura client http
			solrClient = new HttpSolrClient(urlBuilder.getSolrUrl(codPolo, coreType));
			query = new SolrQuery();
			query.setTerms(true);
			query.setRequestHandler("/terms");
			// query.ter
			// query.addSort("index", ORDER.asc);
			query.setTermsSortString("index");

			String field = conv.covertToSearchTerm(request.getField());
			// field = "editorew";
			query.addTermsField(field);
			// query.setTermsPrefix(request.getValue());

			query.setTermsLower(request.getValue());
			query.setTermsLimit(100);
			query.setTermsLowerInclusive(true);
			query.setTermsMinCount(1);

			QueryResponse response = solrClient.query(query);
			TermsResponse terms = response.getTermsResponse();
			term = terms.getTerms(field);

			log.info("Numero terms" + request.toString() + " trovati: " + term.size());

		} catch (RemoteSolrException | SolrServerException | IOException | IndexOutOfBoundsException
				| NullPointerException | BindingException e) {

			log.error("ECCEZIONE ricerca Termini", e);
			// e.printStackTrace();
		}
		return term;
	}

	// Ricerca per novita
	@Override
	public SolrResponseBean novita(String codPolo, String dateFrom) {
		return executeQuery("tagI950:([" + dateFrom + " TO *])", SearchBean.getNovita(dateFrom), codPolo,
				CoreType.BIBLIO);
	}

	// evo Classificazioni Dewey authority 15/03/2018
	public List<ClassificazioniDewey> classificazioniDewey(List<ClassificazioniDewey> deweys, String cod_polo,
			ClassificazioneType tipoClasse, String start) {
		log.info("Searching on Solr Document Biblio for " + "Classificazioni Dewey");
		// if(tipoClasse == ClassificazioneType.CLASSE) return deweys;
		try {

			// apertura client http
			solrClient = new HttpSolrClient(urlBuilder.getSolrUrl(cod_polo, CoreType.BIBLIO));
			String queryValue = (start.equals("")) ? "*" : start + "*";
			// creazione della query
			query = new SolrQuery("dewey_code:(" + queryValue + ")");
			// aggiunta della faccetta NullPointerException
			
			query.addFacetField(ClassificazioneType.getFacet(tipoClasse));
			query.setFacetMinCount(MIN_FACET_COUNT);
			if (tipoClasse == ClassificazioneType.TOTF)
				query.setFacetPrefix(start);

			QueryResponse response = solrClient.query(query);
			for (FacetField facet : response.getFacetFields()) {
				if (tipoClasse != ClassificazioneType.TOTF)
					for (Count contof : facet.getValues()) {

						ClassificazioniDewey.setNumNotizie(deweys, contof);
					}
				if (tipoClasse == ClassificazioneType.TOTF)
					deweys = ClassificazioniDewey.newList(facet.getValues());
			}
			return deweys;
		} catch (RemoteSolrException | SolrServerException | IOException | IndexOutOfBoundsException
				| NullPointerException | BindingException e) {

			log.error("ECCEZIONE ricerca Classficazioni dewey", e);
			// e.printStackTrace();
			return deweys;
		}
	}

	@Override
	public List<ClassificazioniDewey> classificazioniDewey(List<ClassificazioniDewey> deweys, String cod_polo,
			ClassificazioneType tipoClasse) {
		return null;
	}
}
