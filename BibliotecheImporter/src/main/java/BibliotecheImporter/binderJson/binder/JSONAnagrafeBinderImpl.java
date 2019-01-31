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
package BibliotecheImporter.binderJson.binder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import BibliotecheImporter.binderJson.beans.Biblioteche;

public class JSONAnagrafeBinderImpl implements JSONAnagrafeBinder {
	private Gson gson = new Gson();
	private static Logger log = Logger.getLogger(JSONAnagrafeBinderImpl.class);

	private Reader createReader() {
		try {
			log.info("Opening anagrafe json file");
			return new FileReader(
					System.getProperty("user.home") + "/bibliotecheImporter/"+"biblioteche.json");
		} catch (FileNotFoundException e) {
			log.info("Json anagrafe non trovato", e);
			return null;
		}
	}

	@Override
	public Biblioteche bind() {
		return gson.fromJson(createReader(), Biblioteche.class);
	}

}
