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
package it.almaviva.spring.opacControllers.wsBibliotecario;

public class BibliotecaModel {
	enum Status {
		OK, NOT_FOUND;
	}
	private String cod_bib;
	private String nome;
	private String cod_polo;
	private Status status;

	public String getCod_bib() {
		return cod_bib;
	}

	public void setCod_bib(String cod_bib) {
		this.cod_bib = cod_bib;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
