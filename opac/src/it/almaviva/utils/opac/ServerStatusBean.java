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

import org.json.JSONObject;

public class ServerStatusBean {
	
	private Integer code;
	private String message;
	private String error;
	
	public ServerStatusBean() {
	}

	public ServerStatusBean(Integer code, String message, String error) {
		super();
		this.code = code;
		this.message = message;
		this.error = error;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public static JSONObject toJson(ServerStatusBean serverStatus) {
		JSONObject serv = new JSONObject();
		serv.put("code", serverStatus.getCode());
		serv.put("message", serverStatus.getMessage());
		serv.put("error", serverStatus.getError());
		return serv;

	}
	

}
