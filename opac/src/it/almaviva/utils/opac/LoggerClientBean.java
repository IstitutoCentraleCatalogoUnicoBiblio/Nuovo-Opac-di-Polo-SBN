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


public class LoggerClientBean {
	private String fileJs;
	private String developer;
	private String timestamp ;
	private String riga;
	private String polo;
	private String text;

	public String getFileJs() {
		return fileJs;
	}

	public void setFileJs(String fileJs) {
		this.fileJs = fileJs;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRiga() {
		return riga;
	}

	public void setRiga(String riga) {
		this.riga = riga;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}
	
}
