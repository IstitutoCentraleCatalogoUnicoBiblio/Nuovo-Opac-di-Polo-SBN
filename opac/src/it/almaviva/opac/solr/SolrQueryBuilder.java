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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import it.almaviva.opac.bean.ricerca.result.CoreType;
import it.almaviva.opac.bean.ricerca.result.OperatorType;
import it.almaviva.opac.bean.ricerca.ricerca.Filter;
import it.almaviva.opac.bean.ricerca.ricerca.Filters;
import it.almaviva.opac.bean.ricerca.ricerca.GroupFilters;
import it.almaviva.opac.services.ConverterCampiServices;

//questa classe viene usata per la creazione della stringa di query per Solr
public class SolrQueryBuilder {
	static Logger log = Logger.getLogger(SolrQueryBuilder.class);
	private ConverterCampiServices converter = new ConverterCampiServices();

	// creazione query per frase con un solo campo: usato spesso con Any
	public String queryFrase(String field, String value) {

		return genericQueryPhrase(field, value);
	}

	// una ricerca per id non necessita gli apici
	public String queryByID(String bid) {

		return genericQueryPhrase("id", bid);
	}

	// creazione di query con parole in And
	public String paroleInAnd(String campo, String testo) {

		return genericQuery(campo, testo, OperatorType.AND);

	}

	// creazione di query con parole in And
	public String paroleInOR(String field, String value) {

		return genericQuery(field, value, OperatorType.OR);
	}

	public String createQuery(String field, String value, String operator) {
		return genericQuery(field, value, operator);
	}

	// creazione di query generico per una query per parole in AND/OR/NOT
	private String genericQuery(String field, String value, String operator) {
		String query = new String();

		List<String> list = new ArrayList<String>();
		list = Arrays.asList(value.split(" "));
		ListIterator<String> iterator = list.listIterator();

		for (String str : list) {
			iterator.next();
			if (iterator.hasNext()) {
				query += field + ":(" + str + ") " + operator + " ";

			} else {
				query += field + ":(" + str + ") ";
			}

		}
		logQueryBuilded(query);
		return query;
	}

	// generico query per frase
	private String genericQueryPhrase(String field, String value) {
		String query = new String();

		query = field + ":(" + value + ") ";

		logQueryBuilded(query);
		return query;
	}

	// metodo per il log della query creata
	private void logQueryBuilded(String queryToPrint) {
		log.info("Created query: " + queryToPrint);
	}

	// metodi per la creazione delle query con gruppi di query

	public String queryBuilder(GroupFilters filters, CoreType core) {
		log.info("Groupfilters" + filters.toString());
		String queryToBuild = new String();
		List<Filters> ff = filters.getFilters();
		ListIterator<Filters> filterIterator = ff.listIterator();
		for (Filters filter : filters.getFilters()) {
			filterIterator.next();
			if (filterIterator.hasNext()) {
				queryToBuild += " " + queryBuilder(filter.getFilters(), core) + " " + filter.getOperator() + " ";

			} else {
				queryToBuild += " " + queryBuilder(filter.getFilters(), core) + " ";
			}

		}

		logQueryBuilded(queryToBuild);
		return queryToBuild;
	}

	// generico query per [ * TO *] [ val TO *] [* TO val*]
	private String genericQueryWithTO(String field, String value, CoreType core) {
		String query = new String();
		value = value.toLowerCase().replaceAll("[a-z]+", "");
		field = field.toLowerCase();
		if (value.equals("")) {
			logQueryBuilded("ERRORE query per data vuota");
			return null;
		} else {
			if (core == CoreType.BIBLIO) {
				// data a MODIFICA DEL 14 LUGLIO DA MARY
				if (field.equals("dataa") || field.equals("dataa2")) {
					//field = "dataFine";
					field = "dataInizio";
					query = field + ":([ * TO " + value + "]) ";
					// data da
				} else if (field.equals("datada") || field.equals("datada2")) {
					//field = "dataInizio";
					field = "dataFine";
					query = field + ":([ " + value + " TO *]) ";
					// cartografia costruzione query
					// rapporiz da a
				} else if (field.equals("tagi950")) {
					query = "tagI950:([ " + value + " TO *]) ";
				} else if (field.replace("cart_", "").equals("rapp_oriza")) {
					query = "cart_rapp_oriz:([ * TO " + value + "]) ";

				} else if (field.replace("cart_", "").equals("rapp_orizda")) {
					query = "cart_rapp_oriz:([ " + value + " TO *]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("rapp_vertda")) {
					query = "cart_rapp_vert:([ " + value + " TO *]) ";

				} else if (field.replace("cart_","").toLowerCase().equals("rapp_verta")) {
					query = "cart_rapp_vert:([ * TO " + value + "]) ";

					// latitudine

				} else if (field.replace("cart_", "").toLowerCase().equals("latitudineda_SUD".toLowerCase())) {
					query = "cart_latitudines:([ -" + value + " TO *]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("latitudinea_SUD".toLowerCase())) {
					query = "cart_latitudinen:([ * TO -" + value + "]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("latitudineda_NORD".toLowerCase())) {
					query = "cart_latitudines:([" + value + " TO *]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("latitudineda_EQUATORE".toLowerCase())) {
					query = "cart_latitudines:([" + value + " TO *]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("latitudinea_NORD".toLowerCase())) {
					query = "cart_latitudinen:([ * TO " + value + "]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("latitudinea_EQUATORE".toLowerCase())) {
					query = "cart_latitudinen:([ * TO " + value + "]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("longitudineda_OVEST".toLowerCase())) {
					query = "cart_longitudinee:([ -" + value + " TO *]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("longitudinea_OVEST".toLowerCase())) {
					query = "cart_longitudinew:([ * TO -" + value + "]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("longitudineda_EST".toLowerCase())) {
					query = "cart_longitudinee:([" + value + " TO *]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("longitudinea_EST".toLowerCase())) {
					query = "cart_longitudinew:([ * TO " + value + "]) ";
				} else if (field.replace("cart_", "").toLowerCase().equals("longitudineda_MERIDIANO".toLowerCase())) {
					query = "cart_longitudinee:([" + value + " TO *]) ";

				} else if (field.replace("cart_", "").toLowerCase().equals("longitudinea_MERIDIANO".toLowerCase())) {
					query = "cart_longitudinew:([ * TO " + value + "]) ";
				} else {
					log.error("campo special non trovato");
					return null;
				}
				// longitudine

			} else if (core == CoreType.AUTHOR) {
				if (field.equals("data_nascita1")) {
					field = "data_nascita1";
					query = field + ":([ " + value + " TO *]) ";
				}
				if (field.equals("data_morte1")) {
					field = "data_morte1";
					query = field + ":([ " + value + " TO *]) ";
				}
				if (field.equals("data_nascita2")) {
					field = "data_nascita2";
					query = field + ":([ * TO " + value + "]) ";
				}
				if (field.equals("data_morte2")) {
					field = "data_morte2";
					query = field + ":([ * TO " + value + "]) ";
				}
			}

			logQueryBuilded(query);
			return query;
		}

	}

	private String queryBuilder(List<Filter> filters, CoreType core) {
		String queryToBuild = new String();
		List<String> campidata = fromTo();

		queryToBuild = "(";
		ListIterator<Filter> filterIterator = filters.listIterator();
		for (Filter filter : filters) {
			filterIterator.next();
			if (filterIterator.hasNext()) {
				if (filter.isGroup()) {
					try {
						queryToBuild += queryBuilder(filter.getOtherFiltersGroup(), core) + " "
								+ filter.getOperator() + " ";

					} catch (IndexOutOfBoundsException e) {
						log.info(e);
						queryToBuild += queryBuilder(filter.getOtherFiltersGroup(), core) + " " + filter.getOperator()
								+ " ";

					}
					// filters.get(filterIterator.previousIndex()).getOperator()
					// queryToBuild +=
					// queryBuilder(filter.getOtherFiltersGroup(), core ) + " "
					// +filters.get(filterIterator.previousIndex()).getOperator()+
					// " ";
				} else {
					Boolean isSpecial = campidata.contains(filter.getField());

					if (isSpecial) {
						log.info("TROVATA UNA SPECIAL: " + filter.getField());
						if (filterIterator.hasNext()) {
							queryToBuild += genericQueryWithTO(filter.getField(), filter.getValue(), core)
									+ filter.getOperator() + " ";
						} else {
							queryToBuild += genericQueryWithTO(filter.getField(), filter.getValue(), core);
						}

					} else {
						queryToBuild += checkCampi(filter.getField(), filter.getValue(), filter.getMatch(), core)
								+ filter.getOperator() + " ";
					}
				}
			} else {
				if (filter.isGroup()) {
					queryToBuild += queryBuilder(filter.getOtherFiltersGroup(), core);
				} else {
					Boolean isSpecial = campidata.contains(filter.getField());
					
					if (isSpecial) {
						log.info("TROVATA UNA SPECIAL: " + filter.getField());
						if (filterIterator.hasNext()) {
							queryToBuild += genericQueryWithTO(filter.getField(), filter.getValue(), core)
									+ filter.getOperator() + " ";
						} else {
							queryToBuild += genericQueryWithTO(filter.getField(), filter.getValue(), core);
						}

					} else {
						queryToBuild += checkCampi(filter.getField(), filter.getValue(), filter.getMatch(), core) + " ";

					}
				}
			}

		}
		queryToBuild += ")";
		return queryToBuild;
	}

	private String checkCampi(String field, String value, String matchType, CoreType core) {
		String q = new String();
		//FIX cod_dewey 08/03/2018
		if(field.toLowerCase().equals("dewey_code"))
			matchType =  "phrase";
		switch (matchType) {
		case "phrase":
			value = value.replaceAll("\\s+", " ");
			field = (core == CoreType.BIBLIO) ? converter.converToSearch(field) : converter.convertToSearchAuth(field);

			q += (field.toLowerCase().equals("incipit") || field.toLowerCase().equals("dewey_code")) ? queryFrase(field,  value ) :  queryFrase(field, "\"" + value + "\"");
			break;
		case "completePhrase":
			value = value.replace("\"", "\\\"");
			field = converter.convertToSearchFacet(field);

			q += queryFrase(field, "\"" + value + "\"");

			break;
		case "andWord":
			value = value.replaceAll("[\\p{Punct} && [^*]]+", " ");
			value = value.replaceAll("\\s+", " ");
			value = value.trim();
			field = (core == CoreType.BIBLIO) ? converter.converToSearch(field) : converter.convertToSearchAuth(field);
			q += paroleInAnd(field, value);
			break;
		default:
			// d.setCampo("any");
		}
		return q.replace("( +)", " ").trim();
	}

	public String normalizeQuery(String query) {
		query = query.replace("\"", "");
		query = query.replace("(", "");
		query = query.replace(")", "");
		query = query.trim();

		return query.trim().replace("( +)", " ");
	}

	// Sono una serie di campi per cui la query su solr è campo:([value TO *])
	// (o viceversa)
	private List<String> fromTo() {
		List<String> campidata = new ArrayList<String>();
		//campo i950
		campidata.add("tagI950");
		
		// date di pubblicazione
		campidata.add("dataa");
		campidata.add("datada");
		campidata.add("dataa2");
		campidata.add("datada2");
		campidata.add("dateFrom");
		campidata.add("dateTo");
		campidata.add("dataInizio");
		campidata.add("dataFine");

		// date nascita auth autori
		campidata.add("data_nascita1");
		campidata.add("data_nascita2");
		campidata.add("data_morte1");
		campidata.add("data_morte2");

		// cartografia biblio
		campidata.add("longitudineda_EST");
		campidata.add("longitudinea_OVEST");
		campidata.add("longitudinea_EST");
		campidata.add("longitudineda_OVEST");
		campidata.add("longitudinea_MERIDIANO");
		campidata.add("longitudineda_MERIDIANO");
		
		campidata.add("latitudinea_NORD");
		campidata.add("latitudineda_SUD");
		campidata.add("latitudinea_SUD");
		campidata.add("latitudineda_NORD");
		campidata.add("latitudinea_EQUATORE");
		campidata.add("latitudineda_EQUATORE");

		campidata.add("rapp_orizda");
		campidata.add("rapp_vertda");
		campidata.add("rapp_oriza");
		campidata.add("rapp_verta");
		campidata.add("cart_rapp_orizda");
		campidata.add("cart_rapp_vertda");
		campidata.add("cart_rapp_oriza");
		campidata.add("cart_rapp_verta");
		return campidata;
	}
}
