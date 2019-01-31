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
 * NewListResponseNewListResultMedia_list.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class NewListResponseNewListResultMedia_list  implements java.io.Serializable {
    private java.lang.String status;

    private it.medialibrary.api.NewListResponseNewListResultMedia_listMedia[] media;

    public NewListResponseNewListResultMedia_list() {
    }

    public NewListResponseNewListResultMedia_list(
           java.lang.String status,
           it.medialibrary.api.NewListResponseNewListResultMedia_listMedia[] media) {
           this.status = status;
           this.media = media;
    }


    /**
     * Gets the status value for this NewListResponseNewListResultMedia_list.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this NewListResponseNewListResultMedia_list.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the media value for this NewListResponseNewListResultMedia_list.
     * 
     * @return media
     */
    public it.medialibrary.api.NewListResponseNewListResultMedia_listMedia[] getMedia() {
        return media;
    }


    /**
     * Sets the media value for this NewListResponseNewListResultMedia_list.
     * 
     * @param media
     */
    public void setMedia(it.medialibrary.api.NewListResponseNewListResultMedia_listMedia[] media) {
        this.media = media;
    }

    public it.medialibrary.api.NewListResponseNewListResultMedia_listMedia getMedia(int i) {
        return this.media[i];
    }

    public void setMedia(int i, it.medialibrary.api.NewListResponseNewListResultMedia_listMedia _value) {
        this.media[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NewListResponseNewListResultMedia_list)) return false;
        NewListResponseNewListResultMedia_list other = (NewListResponseNewListResultMedia_list) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.media==null && other.getMedia()==null) || 
             (this.media!=null &&
              java.util.Arrays.equals(this.media, other.getMedia())));
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getMedia() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMedia());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMedia(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NewListResponseNewListResultMedia_list.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>>NewListResponse>NewListResult>media_list"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("media");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "media"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>>>NewListResponse>NewListResult>media_list>media"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
