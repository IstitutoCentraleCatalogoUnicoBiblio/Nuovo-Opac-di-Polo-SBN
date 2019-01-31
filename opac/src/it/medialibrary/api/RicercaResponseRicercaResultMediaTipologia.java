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
 * RicercaResponseRicercaResultMediaTipologia.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class RicercaResponseRicercaResultMediaTipologia  implements java.io.Serializable {
    private int id;

    private java.lang.String nome;

    private int risultati;

    private java.lang.String icona;

    private java.lang.String url;

    private java.lang.String url_mlol;

    public RicercaResponseRicercaResultMediaTipologia() {
    }

    public RicercaResponseRicercaResultMediaTipologia(
           int id,
           java.lang.String nome,
           int risultati,
           java.lang.String icona,
           java.lang.String url,
           java.lang.String url_mlol) {
           this.id = id;
           this.nome = nome;
           this.risultati = risultati;
           this.icona = icona;
           this.url = url;
           this.url_mlol = url_mlol;
    }


    /**
     * Gets the id value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the nome value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the risultati value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @return risultati
     */
    public int getRisultati() {
        return risultati;
    }


    /**
     * Sets the risultati value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @param risultati
     */
    public void setRisultati(int risultati) {
        this.risultati = risultati;
    }


    /**
     * Gets the icona value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @return icona
     */
    public java.lang.String getIcona() {
        return icona;
    }


    /**
     * Sets the icona value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @param icona
     */
    public void setIcona(java.lang.String icona) {
        this.icona = icona;
    }


    /**
     * Gets the url value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }


    /**
     * Gets the url_mlol value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @return url_mlol
     */
    public java.lang.String getUrl_mlol() {
        return url_mlol;
    }


    /**
     * Sets the url_mlol value for this RicercaResponseRicercaResultMediaTipologia.
     * 
     * @param url_mlol
     */
    public void setUrl_mlol(java.lang.String url_mlol) {
        this.url_mlol = url_mlol;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RicercaResponseRicercaResultMediaTipologia)) return false;
        RicercaResponseRicercaResultMediaTipologia other = (RicercaResponseRicercaResultMediaTipologia) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            this.risultati == other.getRisultati() &&
            ((this.icona==null && other.getIcona()==null) || 
             (this.icona!=null &&
              this.icona.equals(other.getIcona()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl()))) &&
            ((this.url_mlol==null && other.getUrl_mlol()==null) || 
             (this.url_mlol!=null &&
              this.url_mlol.equals(other.getUrl_mlol())));
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
        _hashCode += getId();
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        _hashCode += getRisultati();
        if (getIcona() != null) {
            _hashCode += getIcona().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getUrl_mlol() != null) {
            _hashCode += getUrl_mlol().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RicercaResponseRicercaResultMediaTipologia.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>>>RicercaResponse>RicercaResult>media>tipologia"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("risultati");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "risultati"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("icona");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "icona"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url_mlol");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "url_mlol"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
