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
 * RicercaAvResponseRicercaAvResultMedia.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class RicercaAvResponseRicercaAvResultMedia  implements java.io.Serializable {
    private java.lang.String status;

    private java.lang.String stato;

    private int totale;

    private it.medialibrary.api.RicercaAvResponseRicercaAvResultMediaTipologia[] tipologia;

    public RicercaAvResponseRicercaAvResultMedia() {
    }

    public RicercaAvResponseRicercaAvResultMedia(
           java.lang.String status,
           java.lang.String stato,
           int totale,
           it.medialibrary.api.RicercaAvResponseRicercaAvResultMediaTipologia[] tipologia) {
           this.status = status;
           this.stato = stato;
           this.totale = totale;
           this.tipologia = tipologia;
    }


    /**
     * Gets the status value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the stato value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the totale value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @return totale
     */
    public int getTotale() {
        return totale;
    }


    /**
     * Sets the totale value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @param totale
     */
    public void setTotale(int totale) {
        this.totale = totale;
    }


    /**
     * Gets the tipologia value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @return tipologia
     */
    public it.medialibrary.api.RicercaAvResponseRicercaAvResultMediaTipologia[] getTipologia() {
        return tipologia;
    }


    /**
     * Sets the tipologia value for this RicercaAvResponseRicercaAvResultMedia.
     * 
     * @param tipologia
     */
    public void setTipologia(it.medialibrary.api.RicercaAvResponseRicercaAvResultMediaTipologia[] tipologia) {
        this.tipologia = tipologia;
    }

    public it.medialibrary.api.RicercaAvResponseRicercaAvResultMediaTipologia getTipologia(int i) {
        return this.tipologia[i];
    }

    public void setTipologia(int i, it.medialibrary.api.RicercaAvResponseRicercaAvResultMediaTipologia _value) {
        this.tipologia[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RicercaAvResponseRicercaAvResultMedia)) return false;
        RicercaAvResponseRicercaAvResultMedia other = (RicercaAvResponseRicercaAvResultMedia) obj;
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
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            this.totale == other.getTotale() &&
            ((this.tipologia==null && other.getTipologia()==null) || 
             (this.tipologia!=null &&
              java.util.Arrays.equals(this.tipologia, other.getTipologia())));
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
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        _hashCode += getTotale();
        if (getTipologia() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTipologia());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTipologia(), i);
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
        new org.apache.axis.description.TypeDesc(RicercaAvResponseRicercaAvResultMedia.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>>RicercaAvResponse>RicercaAvResult>media"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totale");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "totale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "tipologia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>>>RicercaAvResponse>RicercaAvResult>media>tipologia"));
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
