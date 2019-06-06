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
package it.almaviva.opac.services;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.testng.log4testng.Logger;

import it.almaviva.opac.images.ImageType;
import it.almaviva.opac.images.dao.ImagesManagerDao;
import it.almaviva.utils.Costanti;
import it.almaviva.utils.opac.Util;

public class ImageServices {

	static Logger log = Logger.getLogger(ImageServices.class);
	private ImagesManagerDao imgDao = new ImagesManagerDao();

	private String getPath(String codPolo) {

		String path = null;
		String path_loghi_properties = PropertiesLoader.getProperty(Costanti.path_loghi_properties);
		if (!Util.isFilled(path_loghi_properties))
			return null;
		path = System.getProperty(Costanti.user_home) + File.separator + path_loghi_properties;
		path = path + "/" + codPolo;
		path = Util.cleanPath(path);

		log.info("Read path img: " + path);
		return path;

	}

	private String getFileNameBib(String codPolo, String codBib, ImageType imgType, Boolean bibUseLogo) {
		return ImageType.createFileName(imgType, codPolo, codBib, bibUseLogo);
	}

	private String getFileNamePolo(String codPolo, ImageType imgType) {
		return ImageType.createFileName(imgType, codPolo, null, false);

	}

	// Get LOGO
	public String getPoloLogo(String codPolo) {
		return imgDao.getImg(this.getPath(codPolo), this.getFileNamePolo(codPolo, ImageType.LOGO));
	}

	public String getPoloBibLogo(String codPolo, String codBib, Boolean bibUseLogo) {
		return imgDao.getImg(this.getPath(codPolo), this.getFileNameBib(codPolo, codBib, ImageType.LOGO, bibUseLogo));

	}

	// Get Link1
	public String getPoloLink1(String codPolo) {
		return imgDao.getImg(this.getPath(codPolo), this.getFileNamePolo(codPolo, ImageType.LINK1));
	}

	public String getPoloBibLink1(String codPolo, String codBib, Boolean bibUseLogo) {
		return imgDao.getImg(this.getPath(codPolo), this.getFileNameBib(codPolo, codBib, ImageType.LINK1, bibUseLogo));

	}

	public Boolean uploadImg(String codPolo, String codBib, ImageType imgType, List<MultipartFile> files) {
		String path = this.getPath(codPolo);
		String file = this.getFileNameBib(codPolo, codBib, imgType, true);
		return imgDao.storeImg(path, files, file);
	}

	public Boolean rename(String codPolo, ImageType tipoImg, ImageType newName) {
		return imgDao.rename(this.getPath(codPolo), this.getFileNamePolo(codPolo, tipoImg),
				this.getFileNamePolo(codPolo, tipoImg).replace(tipoImg.toString().toLowerCase(),
						newName.toString().toLowerCase()));
	}

	public Boolean delete(String codPolo, ImageType tipoImg) {
		return imgDao.delete(this.getPath(codPolo), this.getFileNamePolo(codPolo, tipoImg));
	}

	public String getPoloLink2(String codPolo) {
		return imgDao.getImg(this.getPath(codPolo), this.getFileNamePolo(codPolo, ImageType.LINK2));

	}

	public String getPoloBibLink2(String codPolo, String codBib, Boolean bibUseLogo) {
		return imgDao.getImg(this.getPath(codPolo), this.getFileNameBib(codPolo, codBib, ImageType.LINK2, bibUseLogo));

	}

	public String getScrittaPolo(String codPolo) {
		return imgDao.getImg(this.getPath(codPolo), this.getFileNamePolo(codPolo, ImageType.LOGO_POLO_SCRITTA));

	}

	public String getScrittaBiblio(String codPolo, String codBib, Boolean bibUseLogo) {
		return imgDao.getImg(this.getPath(codPolo),
				this.getFileNameBib(codPolo, codBib, ImageType.LOGO_POLO_SCRITTA, bibUseLogo));

	}
}
