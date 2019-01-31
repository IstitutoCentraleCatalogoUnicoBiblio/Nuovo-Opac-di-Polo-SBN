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
package it.almaviva.spring.imagesControllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.almaviva.opac.images.ImageType;
import it.almaviva.opac.services.ImageServices;
import it.almaviva.opac.services.InterrogazioneOpacDBServices;

@Controller
public class APIGetImage {
	private ImageServices gis = new ImageServices();
	static Logger log = Logger.getLogger(APIGetImage.class);
	@Autowired
	InterrogazioneOpacDBServices dbManager = new InterrogazioneOpacDBServices();

	private ResponseEntity<byte[]> searchImage(String filePath) {
		File file = new File(filePath);
		try {
			HttpHeaders headers = new HttpHeaders();
			String mimeType = Files.probeContentType(file.toPath());
			MediaType media = MediaType.parseMediaType(mimeType);
			headers.setContentType(media);

			return new ResponseEntity<byte[]>(Files.readAllBytes(file.toPath()), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Downloading logo " + filePath);
			log.info("Downloading logo " + filePath);

			return null;
		}
	}
	
	@GetMapping(value = "/img/get/{tipoImmagine}/{codPolo}")
	public ResponseEntity<byte[]> get(@PathVariable String tipoImmagine, @PathVariable String codPolo) throws IOException {
		String filePath = null;
		 switch (ImageType.getTypeFromString(tipoImmagine)) {
		case LOGO:
			 filePath = gis.getPoloLogo(codPolo.substring(0, 3).toUpperCase());
			break;
		case LINK1: 
			filePath = gis.getPoloLink1(codPolo.substring(0, 3).toUpperCase());
			break;
		case LINK2:
			filePath = gis.getPoloLink2(codPolo.substring(0, 3).toUpperCase());
			break;
		case LOGO_POLO_SCRITTA:
			filePath = gis.getScrittaPolo(codPolo.substring(0, 3).toUpperCase());
			break;
		default:
			return null;
		}
		
		return searchImage(filePath);
	}

	@GetMapping(value = "/img/get/{tipoImmagine}/{codPolo:[3]*[A-Z|0-9]+}/{codBib:[2]*[A-Z|0-9]+}")
	public ResponseEntity<byte[]> getLogo(@PathVariable String tipoImmagine, @PathVariable String codPolo, @PathVariable String codBib) {

		String filePath = null;
		 switch (ImageType.getTypeFromString(tipoImmagine)) {
		case LOGO:
			filePath = gis.getPoloBibLogo(codPolo.substring(0, 3).toUpperCase(), codBib.substring(0, 2),
					dbManager.getBiblio(codPolo, codBib).getFlag_logo());			
			break;
		case LINK1: 
			filePath = gis.getPoloBibLink1(codPolo.substring(0, 3).toUpperCase(), codBib.substring(0, 2), true);
			break;
		case LINK2:
			filePath = gis.getPoloBibLink2(codPolo.substring(0, 3).toUpperCase(), codBib.substring(0, 2), true);
			break;
		case LOGO_POLO_SCRITTA:
			filePath = gis.getScrittaBiblio(codPolo.substring(0, 3).toUpperCase(), codBib.substring(0, 2), true);
			break;
	
		default:
			return null;
		}
		
		return searchImage(filePath);
	}
	
}
