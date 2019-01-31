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
package it.medialibrary.api;

public class APISoapProxy implements it.medialibrary.api.APISoap {
  private String _endpoint = null;
  private it.medialibrary.api.APISoap aPISoap = null;
  
  public APISoapProxy() {
    _initAPISoapProxy();
  }
  
  public APISoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initAPISoapProxy();
  }
  
  private void _initAPISoapProxy() {
    try {
      aPISoap = (new it.medialibrary.api.APILocator()).getAPISoap();
      if (aPISoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)aPISoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)aPISoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (aPISoap != null)
      ((javax.xml.rpc.Stub)aPISoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.medialibrary.api.APISoap getAPISoap() {
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap;
  }
  
  public it.medialibrary.api.GetRecordsResponseGetRecordsResult getRecords(java.lang.String api_key, java.lang.String portal_id, java.lang.String format, boolean full_export) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.getRecords(api_key, portal_id, format, full_export);
  }
  
  public it.medialibrary.api.OpenMediaResponseOpenMediaResult openMedia(java.lang.String api_key, java.lang.String portal_id, java.lang.String username, java.lang.String media_id) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.openMedia(api_key, portal_id, username, media_id);
  }
  
  public it.medialibrary.api.RicercaResponseRicercaResult ricerca(java.lang.String api_key, java.lang.String keywords, java.lang.String idPortale) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.ricerca(api_key, keywords, idPortale);
  }
  
  public it.medialibrary.api.RicercaAvResponseRicercaAvResult ricercaAv(java.lang.String api_key, java.lang.String titolo, java.lang.String autore, java.lang.String editore, java.lang.String lingua, java.lang.String idTipologia, java.lang.String idPortale) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.ricercaAv(api_key, titolo, autore, editore, lingua, idTipologia, idPortale);
  }
  
  public it.medialibrary.api.TopListResponseTopListResult topList(java.lang.String api_key, java.lang.String portal_id, java.lang.String type_id) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.topList(api_key, portal_id, type_id);
  }
  
  public it.medialibrary.api.NewListResponseNewListResult newList(java.lang.String api_key, java.lang.String portal_id, java.lang.String type_id) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.newList(api_key, portal_id, type_id);
  }
  
  public it.medialibrary.api.UserLoansResponseUserLoansResult userLoans(java.lang.String api_key, java.lang.String portal_id, java.lang.String username) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.userLoans(api_key, portal_id, username);
  }
  
  public it.medialibrary.api.GetTypesResponseGetTypesResult getTypes(java.lang.String api_key, java.lang.String portal_id) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.getTypes(api_key, portal_id);
  }
  
  public it.medialibrary.api.GetAuthURLResponseGetAuthURLResult getAuthURL(java.lang.String api_key, java.lang.String portal_id, java.lang.String username) throws java.rmi.RemoteException{
    if (aPISoap == null)
      _initAPISoapProxy();
    return aPISoap.getAuthURL(api_key, portal_id, username);
  }
  
  
}
