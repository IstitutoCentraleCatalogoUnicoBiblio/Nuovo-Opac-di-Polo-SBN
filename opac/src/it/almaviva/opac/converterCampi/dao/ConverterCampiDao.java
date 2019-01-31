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
package it.almaviva.opac.converterCampi.dao;

import it.almaviva.opac.converterCampi.ConverterCampi;

//converte i campi di ricerca nominati in quelli di solr, valido solo per quelli qui presenti negli switch
public class ConverterCampiDao implements ConverterCampi {

	@Override
	public String converToPublic(String field) {
		return fromSearchFieldToPublicField(field);
	}

	@Override
	public String converToSearch(String field) {
		return fromPublicToSearch(field, false);
	}

	@Override
	public String convertToSearchFacet(String field) {
		return fromPublicToFacet(field);
	}

	public static String fromPublicToSearch(String field, Boolean isAuth) {
		switch (field) {
		case "nome":
			field = (isAuth) ? "nome" : "nome_tot";
			break;
		case "nome_ente":
			field = (isAuth) ? "nome_ente" : "nome_tot";
			break;
		case "ente":
			field = (isAuth) ? "nome_ente" : "nome_tot";
			break;
		case "note":
			field = (isAuth) ? "nota" : "note";
			break;
		case "titolo":
			field = "titolo_all";
			break;
		case "soggetto":
			field = "soggetto_tot";
			break;
		case "titolo_uniforme":
			field = "titolo_uniforme";
			break;
		case "collezione":
			field = "collezione_id";
			break;
		case "collection":
			field = "collezione_id";
			break;
		case "titolo_raccolta":
			field = "titolo_raccolta";
			break;
		case "luogo":
			field = "luogo";
			break;
		case "dewey_code":
			field = "dewey_code";
			break;
		case "dewey_des":
			field = "dewey_des";
			break;
		case "keywords":
			field = "keywords";
			break;
		case "any":
			field = "any";
			break;
		case "level":
			field = "level";
			break;
		case "tiporec":
			field = "tiporec";
			break;
		case "library":
			field = "tag977";
			break;
		case "id":
			field = "id";
			break;
		case "bid":
			field = "id";
			break;
		case "vid":
			field = "id";
		case "lingua":
			field = "lingua";
			break;
		case "paese":
			field = "paese";
			break;
		case "bni":
			field = "bni";
			break;
		case "issn":
			field = "issn";
			break;
		case "isbn":
			field = "isbn";
			break;
		case "marca":
			field = "marca";
			break;
		case "impronta":
			field = "impronta";
			break;
		case "possessore":
			field = "possessore";
			break;
		case "abstract":
			field = "abstract";
			break;
		case "colltit_tip_461_new_contenuti":
			field = "colltit_tip_461_new";
		default:
			field = field.trim();
		}
		return field;
	}

	private String fromSearchFieldToPublicField(String field) {

		switch (field) {
		case "nome_tot":
			field = "nome";
			break;
		case "nomescan":
			field = "nome";
			break;
		case "titoloscan":
			field = "titolo";
			break;
		case "nome_ente":
			field = "ente";
			break;
		case "titolo_all":
			field = "titolo";
			break;
		case "soggettof":
			field = "soggetto";
			break;
		case "titolo_uniformef":
			field = "titolo_uniforme";
			break;
		case "collezionef":
			field = "collezione";
			break;
		case "titolo_raccoltaf":
			field = "titolo_raccolta";
			break;
		case "luogof":
			field = "luogo";
			break;
		case "dewey_code":
			field = "dewey_code";
			break;
		case "dewey_desf":
			field = "dewey_des";
			break;
		case "keywords":
			field = "keywords";
			break;
		case "level":
			field = "level";
			break;
		case "tiporec":
			field = "tiporec";
			break;
		case "tag977":
			field = "library";
			break;
		case "id":
			field = "id";
			break;
		case "bid":
			field = "bid";
			break;
		case "lingua":
			field = "lingua";
			break;
		case "paese":
			field = "paese";
			break;
		case "bni":
			field = "bni";
			break;
		case "issn":
			field = "issn";
			break;
		case "isbn":
			field = "isbn";
			break;
		case "marcaf":
			field = "marca";
			break;
		case "improntaf":
			field = "impronta";
			break;
		case "possessoref":
			field = "possessore";
			break;
		case "abstract":
			field = "abstract";
			break;
		case "luogo":
			field = "luogo";
			break;
		case "marca":
			field = "marca";
			break;
		case "impronta":
			field = "impronta";
			break;
		case "possessore":
			field = "possessore";
			break;
		default:

			field = field.trim();
		}

		return field;
	}

	public static String fromPublicToFacet(String field) {
		switch (field) {
		case "dataf":
			field = "dataf";
			break;
			
		case "data":
			field = "dataf";
			break;
			
		case "nome":
			field = "nomescan";
			break;
			
		case "titolo":
			field = "titoloscan";
			break;
			
		case "soggetto":
			field = "soggettof";
			break;
			
		case "editore":
			field = "editoref";
			break;
			
		case "titolo_uniforme":
			field = "titolo_uniformef";
			break;
			
		case "collezione":
			field = "collezionef";
			break;
			
		case "collection":
			field = "collezione_id";
			break;
			
		case "titolo_raccolta":
			field = "titolo_raccolta";
			break;
			
		case "luogo":
			field = "luogof";
			break;
			
		case "dewey_code":
			field = "dewey_code";
			break;
			
		case "dewey_des":
			field = "dewey_desf";
			break;
			
		case "keywords":
			field = "keywords";
			break;
			
		case "level":
			field = "level";
			break;
			
		case "tiporec":
			field = "tiporec";
			break;
			
		case "library":
			field = "tag977";
			break;
			
		case "tag977":
			field = "tag977";
			break;
			
		case "id":
			field = "id";
			break;
			
		case "bid":
			field = "bid";
			break;
			
		case "lingua":
			field = "lingua";
			break;
			
		case "paese":
			field = "paese";
			break;
			
		case "bni":
			field = "bni";
			break;
			
		case "issn":
			field = "issn";
			break;
			
		case "isbn":
			field = "isbn";
			break;
			
		case "marca":
			field = "marcaf";
			break;
			
		case "forma":
			field = "formaf";
			break;
			
		case "impronta":
			field = "improntaf";
			break;
			
		case "possessore":
			field = "possessoref";
			break;
			
		case "improntaf":
			field = "improntaf";
			break;
			
		case "possessoref":
			field = "possessoref";
			break;
			
		case "abstract":
			field = "abstract";
			break;
		case "classi_PGI_686_tot":
			field = "classi_PGI_686_totf";
			break;
		default:
			field = field.trim();
		}
		return field;
	}

	@Override
	public String convertToSearchAuth(String field) {
		return fromPublicToSearch(field, true);
	}

	@Override
	public String covertToSearchTerm(String field) {
		return convertToSearchFacet(field);
	}

}
