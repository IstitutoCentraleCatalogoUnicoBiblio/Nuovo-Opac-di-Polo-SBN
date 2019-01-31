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
 * TopListResponseTopListResultMedia_listMedia.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public class TopListResponseTopListResultMedia_listMedia  implements java.io.Serializable {
    private byte pos;

    private int id;

    private java.lang.String dc_title;

    private java.lang.String[] dc_creator;

    private java.lang.String url_cover;

    public TopListResponseTopListResultMedia_listMedia() {
    }

    public TopListResponseTopListResultMedia_listMedia(
           byte pos,
           int id,
           java.lang.String dc_title,
           java.lang.String[] dc_creator,
           java.lang.String url_cover) {
           this.pos = pos;
           this.id = id;
           this.dc_title = dc_title;
           this.dc_creator = dc_creator;
           this.url_cover = url_cover;
    }


    /**
     * Gets the pos value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @return pos
     */
    public byte getPos() {
        return pos;
    }


    /**
     * Sets the pos value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @param pos
     */
    public void setPos(byte pos) {
        this.pos = pos;
    }


    /**
     * Gets the id value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the dc_title value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @return dc_title
     */
    public java.lang.String getDc_title() {
        return dc_title;
    }


    /**
     * Sets the dc_title value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @param dc_title
     */
    public void setDc_title(java.lang.String dc_title) {
        this.dc_title = dc_title;
    }


    /**
     * Gets the dc_creator value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @return dc_creator
     */
    public java.lang.String[] getDc_creator() {
        return dc_creator;
    }


    /**
     * Sets the dc_creator value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @param dc_creator
     */
    public void setDc_creator(java.lang.String[] dc_creator) {
        this.dc_creator = dc_creator;
    }

    public java.lang.String getDc_creator(int i) {
        return this.dc_creator[i];
    }

    public void setDc_creator(int i, java.lang.String _value) {
        this.dc_creator[i] = _value;
    }


    /**
     * Gets the url_cover value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @return url_cover
     */
    public java.lang.String getUrl_cover() {
        return url_cover;
    }


    /**
     * Sets the url_cover value for this TopListResponseTopListResultMedia_listMedia.
     * 
     * @param url_cover
     */
    public void setUrl_cover(java.lang.String url_cover) {
        this.url_cover = url_cover;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TopListResponseTopListResultMedia_listMedia)) return false;
        TopListResponseTopListResultMedia_listMedia other = (TopListResponseTopListResultMedia_listMedia) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.pos == other.getPos() &&
            this.id == other.getId() &&
            ((this.dc_title==null && other.getDc_title()==null) || 
             (this.dc_title!=null &&
              this.dc_title.equals(other.getDc_title()))) &&
            ((this.dc_creator==null && other.getDc_creator()==null) || 
             (this.dc_creator!=null &&
              java.util.Arrays.equals(this.dc_creator, other.getDc_creator()))) &&
            ((this.url_cover==null && other.getUrl_cover()==null) || 
             (this.url_cover!=null &&
              this.url_cover.equals(other.getUrl_cover())));
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
        _hashCode += getPos();
        _hashCode += getId();
        if (getDc_title() != null) {
            _hashCode += getDc_title().hashCode();
        }
        if (getDc_creator() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDc_creator());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDc_creator(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUrl_cover() != null) {
            _hashCode += getUrl_cover().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TopListResponseTopListResultMedia_listMedia.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://api.medialibrary.it/", ">>>>TopListResponse>TopListResult>media_list>media"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "pos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "byte"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dc_title");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "dc_title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dc_creator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "dc_creator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url_cover");
        elemField.setXmlName(new javax.xml.namespace.QName("http://api.medialibrary.it/", "url_cover"));
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
