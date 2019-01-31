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
package it.almaviva.opac.externalCall.sbnweb;

public class PossedutoRequestJson {
	private String polo;
	private String biblioteca;
	private String bid;
	private String inventario;
	public String getPolo() {
		return polo;
	}
	public void setPolo(String polo) {
		this.polo = polo;
	}
	public String getBiblioteca() {
		return biblioteca;
	}
	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getInventario() {
		return inventario;
	}
	public void setInventario(String inventario) {
		this.inventario = inventario;
	}
	public PossedutoRequestJson(String polo, String biblioteca, String bid, String inventario) {
		super();
		this.polo = polo;
		this.biblioteca = biblioteca;
		this.bid = bid;
		this.inventario = inventario;
	}
	
}
