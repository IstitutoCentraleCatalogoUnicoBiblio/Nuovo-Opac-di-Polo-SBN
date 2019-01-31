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
 * Ricerca.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class Ricerca  implements java.io.Serializable {
    private java.lang.String api_key;

    private java.lang.String keywords;

    private java.lang.String idPortale;

    public Ricerca() {
    }

    public Ricerca(
           java.lang.String api_key,
           java.lang.String keywords,
           java.lang.String idPortale) {
           this.api_key = api_key;
           this.keywords = keywords;
           this.idPortale = idPortale;
    }


    /**
     * Gets the api_key value for this Ricerca.
     * 
     * @return api_key
     */
    public java.lang.String getApi_key() {
        return api_key;
    }


    /**
     * Sets the api_key value for this Ricerca.
     * 
     * @param api_key
     */
    public void setApi_key(java.lang.String api_key) {
        this.api_key = api_key;
    }


    /**
     * Gets the keywords value for this Ricerca.
     * 
     * @return keywords
     */
    public java.lang.String getKeywords() {
        return keywords;
    }


    /**
     * Sets the keywords value for this Ricerca.
     * 
     * @param keywords
     */
    public void setKeywords(java.lang.String keywords) {
        this.keywords = keywords;
    }


    /**
     * Gets the idPortale value for this Ricerca.
     * 
     * @return idPortale
     */
    public java.lang.String getIdPortale() {
        return idPortale;
    }


    /**
     * Sets the idPortale value for this Ricerca.
     * 
     * @param idPortale
     */
    public void setIdPortale(java.lang.String idPortale) {
        this.idPortale = idPortale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ricerca)) return false;
        Ricerca other = (Ricerca) obj;
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
            ((this.keywords==null && other.getKeywords()==null) || 
             (this.keywords!=null &&
              this.keywords.equals(other.getKeywords()))) &&
            ((this.idPortale==null && other.getIdPortale()==null) || 
             (this.idPortale!=null &&
              this.idPortale.equals(other.getIdPortale())));
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
        if (getKeywords() != null) {
            _hashCode += getKeywords().hashCode();
        }
        if (getIdPortale() != null) {
            _hashCode += getIdPortale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Ricerca.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">Ricerca"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("api_key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "api_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keywords");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "keywords"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPortale");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "idPortale"));
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
