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
package it.almaviva.opac.images.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;


import it.almaviva.opac.images.ImagesStored;
import it.almaviva.utils.opac.Util;

public class ImagesManagerDao implements ImagesStored {
	private static Logger log = Logger.getLogger(ImagesManagerDao.class);

	
	//legge le immagini dalle cartelle del filesystem
	@Override
	public String getImg(String path_to_directory, String file) {
		path_to_directory = Util.cleanPath(path_to_directory);
		String found = "";
		file = file + ".png";
		try {
			File[] listOfFiles = new File(path_to_directory).listFiles();

			found = "";
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					if (listOfFiles[i].getName().indexOf(file) > -1) {
						found = path_to_directory + File.separator + listOfFiles[i].getName();
						log.debug("Found file " + listOfFiles[i].getName());
						break;
					}

				} else if (listOfFiles[i].isDirectory()) {
					log.info("Directory " + listOfFiles[i].getName());
				}
			}
		} catch (Exception e) {
			log.info("Error getting file " + path_to_directory + File.separator + file, e);
	//e.printStackTrace();
			found = "";
		}

		return found;
	}

	@Override
	//metodo per copiare le immagini  nelle cartelle del filesystem
	public Boolean storeImg(String path, List<MultipartFile> files, String filename) {
		Boolean isUploaded = false;
		path = Util.cleanPath(path);

		log.info("Coping " + filename + " TO " + path);
		String pathFile = path + File.separator + filename + ".png";
		if (!files.isEmpty()) {
			try {
				for (MultipartFile file : files) {
					
					
					InputStream in = new ByteArrayInputStream(file.getBytes());
					pathFile = Util.cleanPath(pathFile);
					BufferedImage bfimg = ImageIO.read(in);
					// resize dell immagine
					//bfimg = Scalr.resize(bfimg, 180);
					File destinationFile = new File(pathFile);

					// copio l immagine, attenzione sovrascrive il file
					ImageIO.write(bfimg, "png", destinationFile);
				}
				isUploaded = true;
				log.info("Copied");
			} catch (Exception e) {
				log.info("Error uploading file " + pathFile, e);
				//e.printStackTrace();
			}

		} else {
			log.info("No file to Upload");
		}
		return isUploaded;
	}

	@Override
	public Boolean rename(String path, String originalName, String newName) {
		path = Util.cleanPath(path);
		log.info("Renaming " + originalName + " TO " + newName);
		File oldFile = new File( path + File.separator + originalName + ".png");
		File newFile = new File( path + File.separator + newName + ".png");
		if(newFile.exists())
			newFile.delete();
	
		Boolean renamed = oldFile.renameTo(newFile);
		return renamed;
	}
	@Override
	public Boolean delete(String path, String name) {
		path = Util.cleanPath(path);
		log.info("Renaming " + name + " TO " + name);
		File oldFile = new File( path + File.separator + name + ".png");
		if(oldFile.exists())
			oldFile.delete();
	
		return true;
	}
}
