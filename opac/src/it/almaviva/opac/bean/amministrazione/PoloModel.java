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
package it.almaviva.opac.bean.amministrazione;

public class PoloModel {
	String code, name, nome_referente, email_referente, maps_api_key, mlol_portal_id, mlol_api_key, email_segnalazioni,
			url_logo, numero_referente;
	Boolean flag_mlol, flag_wiki, flag_maps, flag_autori, flag_logo, flag_classificazioni, flag_chiedi;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNome_referente() {
		return nome_referente;
	}

	public void setNome_referente(String nome_referente) {
		this.nome_referente = nome_referente;
	}

	public String getEmail_referente() {
		return email_referente;
	}

	public void setEmail_referente(String email_referente) {
		this.email_referente = email_referente;
	}

	public String getFlag_mlol() {
		return (flag_mlol) ? "1" : "0";
	}

	public void setFlag_mlol(Boolean flag_mlol) {
		this.flag_mlol = flag_mlol;
	}

	public String getFlag_wiki() {
		return (flag_wiki) ? "1" : "0";
	}

	public void setFlag_wiki(Boolean flag_wiki) {
		this.flag_wiki = flag_wiki;
	}

	public String getFlag_maps() {
		return (flag_maps) ? "1" : "0";
	}

	public void setFlag_maps(Boolean flag_maps) {
		this.flag_maps = flag_maps;
	}

	public String getFlag_autori() {
		return (flag_autori) ? "1" : "0";
	}

	public String getFlag_classificazioni() {
		return (flag_classificazioni) ? "1" : "0";
	}

	public void setFlag_classificazioni(Boolean flag_classificazioni) {
		this.flag_classificazioni = flag_classificazioni;
	}

	public void setFlag_autori(Boolean flag_autori) {
		this.flag_autori = flag_autori;
	}

	public String getFlag_logo() {
		return (flag_logo) ? "1" : "0";

	}

	public void setFlag_logo(Boolean flag_logo) {
		this.flag_logo = flag_logo;
	}

	public String getMaps_api_key() {
		return maps_api_key;
	}

	public void setMaps_api_key(String maps_api_key) {
		this.maps_api_key = maps_api_key;
	}

	public String getMlol_portal_id() {
		return mlol_portal_id;
	}

	public void setMlol_portal_id(String mlol_portal_id) {
		this.mlol_portal_id = mlol_portal_id;
	}

	public String getMlol_api_key() {
		return mlol_api_key;
	}

	public void setMlol_api_key(String mlol_api_key) {
		this.mlol_api_key = mlol_api_key;
	}

	public String getEmail_segnalazioni() {
		return email_segnalazioni;
	}

	public void setEmail_segnalazione(String email_segnalazioni) {
		this.email_segnalazioni = email_segnalazioni;
	}

	public String getUrl_logo() {
		return url_logo;
	}

	public void setUrl_logo(String url_logo) {
		this.url_logo = url_logo;
	}

	public String getNumero_referente() {
		return numero_referente;
	}

	public void setNumero_referente(String numero_referente) {
		this.numero_referente = numero_referente;
	}

	public String getFlag_chiedi() {
		return (flag_chiedi) ? "1" : "0";
	}

	public void setFlag_chiedi(Boolean flag_chiedi) {
		this.flag_chiedi = flag_chiedi;
	}

}
