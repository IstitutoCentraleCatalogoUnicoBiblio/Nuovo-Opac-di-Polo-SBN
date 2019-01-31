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
 * RicercaAv.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class RicercaAv  implements java.io.Serializable {
    private java.lang.String api_key;

    private java.lang.String titolo;

    private java.lang.String autore;

    private java.lang.String editore;

    private java.lang.String lingua;

    private java.lang.String idTipologia;

    private java.lang.String idPortale;

    public RicercaAv() {
    }

    public RicercaAv(
           java.lang.String api_key,
           java.lang.String titolo,
           java.lang.String autore,
           java.lang.String editore,
           java.lang.String lingua,
           java.lang.String idTipologia,
           java.lang.String idPortale) {
           this.api_key = api_key;
           this.titolo = titolo;
           this.autore = autore;
           this.editore = editore;
           this.lingua = lingua;
           this.idTipologia = idTipologia;
           this.idPortale = idPortale;
    }


    /**
     * Gets the api_key value for this RicercaAv.
     * 
     * @return api_key
     */
    public java.lang.String getApi_key() {
        return api_key;
    }


    /**
     * Sets the api_key value for this RicercaAv.
     * 
     * @param api_key
     */
    public void setApi_key(java.lang.String api_key) {
        this.api_key = api_key;
    }


    /**
     * Gets the titolo value for this RicercaAv.
     * 
     * @return titolo
     */
    public java.lang.String getTitolo() {
        return titolo;
    }


    /**
     * Sets the titolo value for this RicercaAv.
     * 
     * @param titolo
     */
    public void setTitolo(java.lang.String titolo) {
        this.titolo = titolo;
    }


    /**
     * Gets the autore value for this RicercaAv.
     * 
     * @return autore
     */
    public java.lang.String getAutore() {
        return autore;
    }


    /**
     * Sets the autore value for this RicercaAv.
     * 
     * @param autore
     */
    public void setAutore(java.lang.String autore) {
        this.autore = autore;
    }


    /**
     * Gets the editore value for this RicercaAv.
     * 
     * @return editore
     */
    public java.lang.String getEditore() {
        return editore;
    }


    /**
     * Sets the editore value for this RicercaAv.
     * 
     * @param editore
     */
    public void setEditore(java.lang.String editore) {
        this.editore = editore;
    }


    /**
     * Gets the lingua value for this RicercaAv.
     * 
     * @return lingua
     */
    public java.lang.String getLingua() {
        return lingua;
    }


    /**
     * Sets the lingua value for this RicercaAv.
     * 
     * @param lingua
     */
    public void setLingua(java.lang.String lingua) {
        this.lingua = lingua;
    }


    /**
     * Gets the idTipologia value for this RicercaAv.
     * 
     * @return idTipologia
     */
    public java.lang.String getIdTipologia() {
        return idTipologia;
    }


    /**
     * Sets the idTipologia value for this RicercaAv.
     * 
     * @param idTipologia
     */
    public void setIdTipologia(java.lang.String idTipologia) {
        this.idTipologia = idTipologia;
    }


    /**
     * Gets the idPortale value for this RicercaAv.
     * 
     * @return idPortale
     */
    public java.lang.String getIdPortale() {
        return idPortale;
    }


    /**
     * Sets the idPortale value for this RicercaAv.
     * 
     * @param idPortale
     */
    public void setIdPortale(java.lang.String idPortale) {
        this.idPortale = idPortale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RicercaAv)) return false;
        RicercaAv other = (RicercaAv) obj;
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
            ((this.titolo==null && other.getTitolo()==null) || 
             (this.titolo!=null &&
              this.titolo.equals(other.getTitolo()))) &&
            ((this.autore==null && other.getAutore()==null) || 
             (this.autore!=null &&
              this.autore.equals(other.getAutore()))) &&
            ((this.editore==null && other.getEditore()==null) || 
             (this.editore!=null &&
              this.editore.equals(other.getEditore()))) &&
            ((this.lingua==null && other.getLingua()==null) || 
             (this.lingua!=null &&
              this.lingua.equals(other.getLingua()))) &&
            ((this.idTipologia==null && other.getIdTipologia()==null) || 
             (this.idTipologia!=null &&
              this.idTipologia.equals(other.getIdTipologia()))) &&
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
        if (getTitolo() != null) {
            _hashCode += getTitolo().hashCode();
        }
        if (getAutore() != null) {
            _hashCode += getAutore().hashCode();
        }
        if (getEditore() != null) {
            _hashCode += getEditore().hashCode();
        }
        if (getLingua() != null) {
            _hashCode += getLingua().hashCode();
        }
        if (getIdTipologia() != null) {
            _hashCode += getIdTipologia().hashCode();
        }
        if (getIdPortale() != null) {
            _hashCode += getIdPortale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RicercaAv.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">RicercaAv"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("api_key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "api_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titolo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "titolo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "autore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("editore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "editore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lingua");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "lingua"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipologia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "idTipologia"));
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
