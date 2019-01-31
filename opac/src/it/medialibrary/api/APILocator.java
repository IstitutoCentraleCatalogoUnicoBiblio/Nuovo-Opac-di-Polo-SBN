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
 * APILocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class APILocator extends org.apache.axis.client.Service implements it.medialibrary.api.API {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
 * <h3>API
 * 			MediaLibraryOnLine (MLOL)</h3>
 */

    public APILocator() {
    }


    public APILocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public APILocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for APISoap
    private java.lang.String APISoap_address = "https://api.medialibrary.it/api.asmx";

    public java.lang.String getAPISoapAddress() {
        return APISoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String APISoapWSDDServiceName = "APISoap";

    public java.lang.String getAPISoapWSDDServiceName() {
        return APISoapWSDDServiceName;
    }

    public void setAPISoapWSDDServiceName(java.lang.String name) {
        APISoapWSDDServiceName = name;
    }

    public it.medialibrary.api.APISoap getAPISoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(APISoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAPISoap(endpoint);
    }

    public it.medialibrary.api.APISoap getAPISoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.medialibrary.api.APISoapStub _stub = new it.medialibrary.api.APISoapStub(portAddress, this);
            _stub.setPortName(getAPISoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAPISoapEndpointAddress(java.lang.String address) {
        APISoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.medialibrary.api.APISoap.class.isAssignableFrom(serviceEndpointInterface)) {
                it.medialibrary.api.APISoapStub _stub = new it.medialibrary.api.APISoapStub(new java.net.URL(APISoap_address), this);
                _stub.setPortName(getAPISoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("APISoap".equals(inputPortName)) {
            return getAPISoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://api.medialibrary.it/", "API");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://api.medialibrary.it/", "APISoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("APISoap".equals(portName)) {
            setAPISoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
