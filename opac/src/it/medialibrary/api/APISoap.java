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
 * APISoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.medialibrary.api;

public interface APISoap extends java.rmi.Remote {

    /**
     * Restituisce i
     * 				record da ingestire. L'update è incrementale rispetto all'ultima
     * 				chiamata, a meno che <i>full_export</i> non sia
     * 				impostato a <i>true</i>. <i>format</i> può
     * 				assumere i valori <i>xmldc</i> (default) e
     * 				<i>xmlmarc</i>.
     */
    public it.medialibrary.api.GetRecordsResponseGetRecordsResult getRecords(java.lang.String api_key, java.lang.String portal_id, java.lang.String format, boolean full_export) throws java.rmi.RemoteException;

    /**
     * Avvia la
     * 				consultazione di una risorsa su MLOL. Restituisce l'URL da chiamare
     * 				per la consultazione e i parametri da passare in POST.
     */
    public it.medialibrary.api.OpenMediaResponseOpenMediaResult openMedia(java.lang.String api_key, java.lang.String portal_id, java.lang.String username, java.lang.String media_id) throws java.rmi.RemoteException;

    /**
     * Ricerca una o più
     * 				parole chiave in tutti i campi. Restituisce il numero di item
     * 				suddivisi per tipologia.
     */
    public it.medialibrary.api.RicercaResponseRicercaResult ricerca(java.lang.String api_key, java.lang.String keywords, java.lang.String idPortale) throws java.rmi.RemoteException;

    /**
     * Ricerca una o più
     * 				parole chiave in uno o più campi specifici. Restituisce il numero
     * di
     * 				item suddivisi per tipologia.
     */
    public it.medialibrary.api.RicercaAvResponseRicercaAvResult ricercaAv(java.lang.String api_key, java.lang.String titolo, java.lang.String autore, java.lang.String editore, java.lang.String lingua, java.lang.String idTipologia, java.lang.String idPortale) throws java.rmi.RemoteException;

    /**
     * Resituisce la lista
     * 				dei titoli più consultati per una tipologia ed un ente/portale
     * 				specifico.
     */
    public it.medialibrary.api.TopListResponseTopListResult topList(java.lang.String api_key, java.lang.String portal_id, java.lang.String type_id) throws java.rmi.RemoteException;

    /**
     * Resituisce la lista
     * 				dei titoli aggiunti di recente per una tipologia ed un ente/portale
     * 				specifico.
     */
    public it.medialibrary.api.NewListResponseNewListResult newList(java.lang.String api_key, java.lang.String portal_id, java.lang.String type_id) throws java.rmi.RemoteException;

    /**
     * Resituisce la
     * 				restituisce la lista dei prestiti effettuati dall'utente, inclusi
     * 				quelli in corso.
     */
    public it.medialibrary.api.UserLoansResponseUserLoansResult userLoans(java.lang.String api_key, java.lang.String portal_id, java.lang.String username) throws java.rmi.RemoteException;

    /**
     * Resituisce la lista
     * 				delle tipologie di media abilitate per il portale cliente, con
     * i
     * 				relativi codici e icone.
     */
    public it.medialibrary.api.GetTypesResponseGetTypesResult getTypes(java.lang.String api_key, java.lang.String portal_id) throws java.rmi.RemoteException;

    /**
     * Crea un URL che
     * 				consente di redirezionare l'utente nel portale cliente, facendolo
     * 				trovare già autenticato.
     */
    public it.medialibrary.api.GetAuthURLResponseGetAuthURLResult getAuthURL(java.lang.String api_key, java.lang.String portal_id, java.lang.String username) throws java.rmi.RemoteException;
}
