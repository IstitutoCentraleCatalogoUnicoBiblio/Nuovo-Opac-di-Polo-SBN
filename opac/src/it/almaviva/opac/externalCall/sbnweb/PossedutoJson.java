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

import it.almaviva.utils.opac.ServerStatusBean;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

public class PossedutoJson {
	private SbnwebType sbnweb;
	private ServerStatusBean serverStatus;
	private PossedutoRequestJson request;
	public PossedutoRequestJson getRequest() {
		return request;
	}
	public void setRequest(PossedutoRequestJson request) {
		this.request = request;
	}

	public SbnwebType getSbnweb() {
		return sbnweb;
	}
	public void setSbnweb(SbnwebType sbnweb) {
		this.sbnweb = sbnweb;
	}
	
	public ServerStatusBean getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(ServerStatusBean serverStatus) {
		this.serverStatus = serverStatus;
	}
	public PossedutoJson(SbnwebType sbnweb, ServerStatusBean server, String polo, String biblioteca, String bid, String inventario) {
		super();
		this.sbnweb = sbnweb;
		this.request = new PossedutoRequestJson(polo,biblioteca,bid, inventario);
		this.serverStatus = server;
	}
	
}
