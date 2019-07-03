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
package it.almaviva.opac.externalCall.sbnweb.dao;

import java.util.List;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;

import org.apache.log4j.Logger;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import it.almaviva.opac.externalCall.sbnweb.PossedutoModel;
import it.almaviva.opac.externalCall.sbnweb.SbnWebServiceType;
import it.almaviva.pssql.bean.Polo;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;

//Classe di gestione chiamate Servizi SBNWeb 
public class SbnWebServiceDao implements PossedutoModel {

	static Logger log = Logger.getLogger(SbnWebServiceDao.class);
	private Boolean disp = true;
	private String pathKardex = "/sbn/api/1.0/periodici/kardex/{codBiblioteca}/{bid}/inventario?id={inventario}"
			+ "&disp={disp}";

	private String pathPosseduto = "/sbn/api/1.0/docfisico/posseduto/{codBiblioteca}/{bid}" + "?disp={disp}";
	// bid esempiocfi0358933

	private SbnwebType callSBNWebService(Polo polo, String bid, String codBiblioteca, String inventario,
			SbnWebServiceType serviceType) throws ResourceAccessException {

		log.info("Detecting posseduto...");

		SbnwebType sbnweb = new SbnwebType();

		RestTemplate rest = new RestTemplate();
		log.info("Login SBNWeb username: " + polo.username());
		rest.getInterceptors().add(new BasicAuthorizationInterceptor(polo.username(), polo.password()));

		UrlDetector url = new UrlDetector(polo.webServiceUrl(), UrlDetectorOptions.Default);
		List<Url> found = url.detect();
		if (serviceType == SbnWebServiceType.Posseduto) {
			String serviceUrl = found.get(0).getScheme() + "://" + found.get(0).getHost()+":"+found.get(0).getPort() + pathPosseduto;
			log.info("Connecting to posseduto: " + serviceUrl);

			sbnweb = rest.getForObject(serviceUrl, SbnwebType.class, codBiblioteca, bid, disp);

		} else {
			String serviceUrl = found.get(0).getScheme() + "://" + found.get(0).getHost()+":"+found.get(0).getPort() + pathKardex;
			log.info("Connecting to kardex: " + serviceUrl);

			sbnweb = rest.getForObject(serviceUrl, SbnwebType.class, codBiblioteca, bid, inventario, disp);

		}

		log.info("Returning SBNWebData for" + polo.getCode());
		log.info(sbnweb.toString());

		return sbnweb;

	}

	@Override
	public SbnwebType disponibilita(Polo polo, String bid, String codBiblioteca) {
		try {
			log.info("Calling SBNWEB Service");
			SbnwebType SBNWebResponse = callSBNWebService(polo, bid, codBiblioteca, null, SbnWebServiceType.Posseduto);
			log.info("Success SBNWEB Service");

			return SBNWebResponse;
		} catch (ResourceAccessException e) {
			log.info("ResourceAccessException disponibilita", e);

			return null;
		} catch (Exception e) {
			log.info("Errore WS disponibilita", e);

			return null;
		}
	}

	@Override
	public SbnwebType fascicoli(Polo polo, String codBiblioteca, String idDoc, String inventario) {
		try {
			log.info("SBNWEB Service");
			SbnwebType SBNWebResponse = callSBNWebService(polo, idDoc, codBiblioteca, inventario,
					SbnWebServiceType.Kardex);
			return SBNWebResponse;
		} catch (ResourceAccessException e) {
			log.info("ResourceAccessException disponibilita", e);

			return null;
		} catch (Exception e) {
			log.info("Errore WS disponibilita", e);

			return null;
		}
	}
}
