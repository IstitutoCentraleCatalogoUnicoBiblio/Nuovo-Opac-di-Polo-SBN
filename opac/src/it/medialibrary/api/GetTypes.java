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
 * GetTypes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class GetTypes  implements java.io.Serializable {
    private java.lang.String api_key;

    private java.lang.String portal_id;

    public GetTypes() {
    }

    public GetTypes(
           java.lang.String api_key,
           java.lang.String portal_id) {
           this.api_key = api_key;
           this.portal_id = portal_id;
    }


    /**
     * Gets the api_key value for this GetTypes.
     * 
     * @return api_key
     */
    public java.lang.String getApi_key() {
        return api_key;
    }


    /**
     * Sets the api_key value for this GetTypes.
     * 
     * @param api_key
     */
    public void setApi_key(java.lang.String api_key) {
        this.api_key = api_key;
    }


    /**
     * Gets the portal_id value for this GetTypes.
     * 
     * @return portal_id
     */
    public java.lang.String getPortal_id() {
        return portal_id;
    }


    /**
     * Sets the portal_id value for this GetTypes.
     * 
     * @param portal_id
     */
    public void setPortal_id(java.lang.String portal_id) {
        this.portal_id = portal_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetTypes)) return false;
        GetTypes other = (GetTypes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.api_key==null && other.getApi_key()==null) || 
             (this.api_key!=null &&
              this.api_key.equals(other.getApi_key()))) &&
            ((this.portal_id==null && other.getPortal_id()==null) || 
             (this.portal_id!=null &&
              this.portal_id.equals(other.getPortal_id())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getApi_key() != null) {
            _hashCode += getApi_key().hashCode();
        }
        if (getPortal_id() != null) {
            _hashCode += getPortal_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetTypes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">GetTypes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("api_key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "api_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("portal_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "portal_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
