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

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.opac.images.ImageType;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.ImageServices;
import it.almaviva.utils.opac.ServerStatusBean;

@Controller
public class APIUploadImage {
	private static Logger log = Logger.getLogger(APIUploadImage.class);
	private GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();
	private ImageServices gis = new ImageServices();
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/api/{codPolo}/upload/{tipoImmagine}", method = RequestMethod.POST)
	public ModelAndView uploadPoloLogo(@RequestParam("file") List<MultipartFile> files, @PathVariable String codPolo, @PathVariable String tipoImmagine) {
		Boolean uploaded = gis.uploadImg(codPolo, null, ImageType.getTypeFromString(tipoImmagine), files);
		ServerStatusBean ss = serverStatusMaker.isDone(uploaded);
		log.info("Uploading logo polo");
		return prepareModelAndView(uploaded, ss);
	}

	@RequestMapping(value = "/api/{codPolo}/{codBib}/upload/{tipoImmagine}", method = RequestMethod.POST)
	public ModelAndView uploadBibLogo(@RequestParam("file") List<MultipartFile> files, @PathVariable String codPolo,
			@PathVariable String codBib, @PathVariable String tipoImmagine) {
		Boolean uploaded = gis.uploadImg(codPolo, codBib, ImageType.getTypeFromString(tipoImmagine), files);
		ServerStatusBean ss = serverStatusMaker.isDone(uploaded);
		log.info("Uploading logo biblioteca");
		return prepareModelAndView(uploaded, ss);

	}

	private ModelAndView prepareModelAndView(Boolean isUploaded, ServerStatusBean ss) {
		ModelAndView mv = new ModelAndView("response");
		JSONObject response = new JSONObject();
		response.put("uploaded", isUploaded);
		response.put("serverStatus", ServerStatusBean.toJson(ss));
		mv.addObject("response", response);
		return mv;
	}

}
