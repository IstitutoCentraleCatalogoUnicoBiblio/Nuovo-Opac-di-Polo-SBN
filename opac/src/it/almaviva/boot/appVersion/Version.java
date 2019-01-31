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
package it.almaviva.boot.appVersion;

public class Version {
	private String version = "";
	private String last_release = "";
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLast_release() {
		return last_release;
	}
	public void setLast_release(String last_release) {
		this.last_release = last_release;
	}
	@Override
	public String toString() {
		return "Version [version=" + version + ", last_release=" + last_release + "]";
	}
	public String toJson() {
		return "{'version':'" + version + "', 'last_release':'" + last_release + "'};";

	}
	
}
