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
package it.almaviva.opac.externalCall.medialibrary.dao;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import it.almaviva.opac.externalCall.medialibrary.MLOLClientIntModel;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.utils.Costanti;
import it.medialibrary.api.APILocator;
import it.medialibrary.api.APISoap;
import it.medialibrary.api.APISoapStub;
import it.medialibrary.api.NewListResponseNewListResult;
import it.medialibrary.api.NewListResponseNewListResultMedia_list;
import it.medialibrary.api.RicercaAvResponseRicercaAvResult;
import it.medialibrary.api.RicercaAvResponseRicercaAvResultMedia;
import it.medialibrary.api.RicercaResponseRicercaResult;
import it.medialibrary.api.RicercaResponseRicercaResultMedia;
import it.medialibrary.api.TopListResponseTopListResult;
import it.medialibrary.api.TopListResponseTopListResultMedia_list;
//Classe di chiamata al webservice MediaLibrary
public class MLOLClientDao implements MLOLClientIntModel {
	// Chiavi di prova polo bari
	static Logger log = Logger.getLogger(MLOLClientDao.class);

	@Override
	public RicercaResponseRicercaResultMedia ricerca(Polo polo, String search) {
	
		RicercaResponseRicercaResult ricerca = new RicercaResponseRicercaResult();
		RicercaResponseRicercaResultMedia mediaResult = null;

		if (polo.getFlag_mlol()) {

			try {
				log.info("Calling MLOL for: "+ search);
				ricerca = getMlolApi().ricerca(polo.mlolCredential().getApi_key(), search,
						polo.mlolCredential().getPortal_id());
				mediaResult = ricerca.getMedia();
				log.info("OK, connesso e interrogato a MLOL");

			} catch (ServiceException | RemoteException e) {
				log.error("ERRORE: impossibile connettersi a MLOL", e);
				
			}
		}
		log.info("Returning MLOL data for " + polo.getCode());
		return mediaResult;
	}

	@Override
	public RicercaAvResponseRicercaAvResultMedia ricercaAv(Polo polo, String titolo, String autore, String editore,
			String lingua, String idTipologia) {
		log.info("Calling MLOL ricercaAv...");
		RicercaAvResponseRicercaAvResult ricerca = new RicercaAvResponseRicercaAvResult();
		RicercaAvResponseRicercaAvResultMedia mediaResult = null;
		titolo = (titolo == null) ? "" : titolo;
		autore = (autore == null) ? "" : autore;
		editore = (editore == null) ? "" : editore;
		lingua = (lingua == null) ? "" : lingua;
		idTipologia = (idTipologia == null) ? "" : idTipologia;

		if (polo.getFlag_mlol()) {
			try {

				ricerca = getMlolApi().ricercaAv(polo.mlolCredential().getApi_key(), titolo, autore, editore, lingua,
						idTipologia, polo.mlolCredential().getPortal_id());
				mediaResult = ricerca.getMedia();
				log.info("OK, connesso e interrogato a MLOL");

			} catch (ServiceException | RemoteException e) {
				log.info("ERRORE: impossibile connettersi a MLOL", e);
				
			}
		}
		log.info("Returning MLOL data for " + polo.getCode());

		return mediaResult;
	}

	@Override
	public TopListResponseTopListResultMedia_list topList(Polo polo, String idTipologia) {
		log.info("Calling MLOL topList...");
		TopListResponseTopListResult ricerca = new TopListResponseTopListResult();
		TopListResponseTopListResultMedia_list mediaResult = null;
		if (polo.getFlag_mlol()) {
			try {

				ricerca = getMlolApi().topList(polo.mlolCredential().getApi_key(), polo.mlolCredential().getPortal_id(),
						idTipologia);
				mediaResult = ricerca.getMedia_list();
				log.info("OK, connesso e interrogato a MLOL");

			} catch (ServiceException | RemoteException e) {
				log.info("ERRORE: impossibile connettersi a MLOL", e);
			
			}
		}
		log.info("Returning MLOL data for " + polo.getCode());

		return mediaResult;
	}

	@Override
	public NewListResponseNewListResultMedia_list newList(Polo polo, String idTipologia) {
		log.info("Calling MLOL newList...");
		NewListResponseNewListResult ricerca = new NewListResponseNewListResult();
		NewListResponseNewListResultMedia_list mediaResult = null;
		if (polo.getFlag_mlol()) {
			try {

				ricerca = getMlolApi().newList(polo.mlolCredential().getApi_key(), polo.mlolCredential().getPortal_id(),
						idTipologia);
				mediaResult = ricerca.getMedia_list();
				log.info("OK, connesso e interrogato a MLOL");

			} catch (ServiceException | RemoteException e) {
				log.info("ERRORE: impossibile connettersi a MLOL", e);
			
			}
		}
		log.info("Returning MLOL data for " + polo.getCode());
		return mediaResult;
	}

	private APISoap getMlolApi() throws ServiceException {
		APILocator apiLocator = new APILocator();
		
		APISoapStub apiSoap = (APISoapStub) apiLocator.getAPISoap();
		org.apache.axis.client.Stub s = apiSoap;
		
		s.setTimeout(Costanti.some_second_timeout);
		return apiSoap;
	}
}
