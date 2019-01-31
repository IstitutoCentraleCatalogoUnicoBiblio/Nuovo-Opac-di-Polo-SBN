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
package it.almaviva.opac.bean.ricerca.result;

import java.util.List;

import it.almaviva.opac.services.ConverterCampiServices;
//bean di un a faccetta
public class FaccettaBean {
	
	
	private String nome;
	private int value;
	private List<SottoFaccettaBean> sottof;
	
	private ConverterCampiServices converter = new ConverterCampiServices();
	
	public FaccettaBean(String nome, int value, List<SottoFaccettaBean> sottof) {
		super();
		this.nome = nome;
		this.value = value;
		this.sottof = sottof;
	}
	public String getNome() {
		return converter.converToPublic(nome);
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public List<SottoFaccettaBean> getSottof() {
		return sottof;
	}
	public void setSottof(List<SottoFaccettaBean> sottof) {
		this.sottof = sottof;
	}
	

}
