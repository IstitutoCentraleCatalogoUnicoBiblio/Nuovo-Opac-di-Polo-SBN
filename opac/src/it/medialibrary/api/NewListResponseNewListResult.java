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
 * NewListResponseNewListResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class NewListResponseNewListResult  implements java.io.Serializable {
    private it.medialibrary.api.NewListResponseNewListResultMedia_list media_list;

    public NewListResponseNewListResult() {
    }

    public NewListResponseNewListResult(
           it.medialibrary.api.NewListResponseNewListResultMedia_list media_list) {
           this.media_list = media_list;
    }


    /**
     * Gets the media_list value for this NewListResponseNewListResult.
     * 
     * @return media_list
     */
    public it.medialibrary.api.NewListResponseNewListResultMedia_list getMedia_list() {
        return media_list;
    }


    /**
     * Sets the media_list value for this NewListResponseNewListResult.
     * 
     * @param media_list
     */
    public void setMedia_list(it.medialibrary.api.NewListResponseNewListResultMedia_list media_list) {
        this.media_list = media_list;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NewListResponseNewListResult)) return false;
        NewListResponseNewListResult other = (NewListResponseNewListResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.media_list==null && other.getMedia_list()==null) || 
             (this.media_list!=null &&
              this.media_list.equals(other.getMedia_list())));
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
        if (getMedia_list() != null) {
            _hashCode += getMedia_list().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NewListResponseNewListResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>NewListResponse>NewListResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("media_list");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "media_list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>>NewListResponse>NewListResult>media_list"));
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
