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
/**
 * API.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public interface API extends javax.xml.rpc.Service {

/**
 * <h3>API
 * 			MediaLibraryOnLine (MLOL)</h3>
 */
    public java.lang.String getAPISoapAddress();

    public it.medialibrary.api.APISoap getAPISoap() throws javax.xml.rpc.ServiceException;

    public it.medialibrary.api.APISoap getAPISoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
