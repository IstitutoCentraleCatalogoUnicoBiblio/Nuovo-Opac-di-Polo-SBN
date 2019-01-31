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
package BibliotecheImporter.BibliotecheImporter;

import org.apache.log4j.Logger;

import BibliotecheImporter.binderJson.beans.Biblioteca;
import BibliotecheImporter.binderJson.beans.Biblioteche;
import BibliotecheImporter.binderJson.binder.JSONAnagrafeBinderImpl;
import dbManager.query.DataImport;

public class App {
	private static JSONAnagrafeBinderImpl binder = new JSONAnagrafeBinderImpl();
	private static Logger log = Logger.getLogger(App.class);
	private static DataImport dataImport = new DataImport();
	private static Boolean importAll = true;

	public static void main(String[] args) {
		log.info("--------------BIBLIOTECHE IMPORTER STARTED-------------");
		Biblioteche bibsJson = binder.bind();
		log.info("Biblioteche lette dal json: " + bibsJson.getBiblioteche().size());

		String codPolo = null;
		try {
			codPolo = args[0].toUpperCase().substring(0, 3);
			importAll = false;
			log.info("Attendi qualche minuto, sto importanto i dati del polo: " + codPolo);
		} catch (Exception e) {
			log.info("Attendi qualche minuto, sto importanto i dati di tutti i poli");
		}
		if (!importAll)
			for (Biblioteca bibToImp : bibsJson.getBiblioteche()) {
				// log.info("------------------------");
				if (codPolo != null && codPolo.equals(bibToImp.getCodici_dentificativi().getCodPolo()))
					dataImport.importData(bibToImp);
			}
		else
			for (Biblioteca bibToImp : bibsJson.getBiblioteche()) {
				dataImport.importData(bibToImp);
			}

		log.info("--------------STATS-------------");
		log.info("Statistiche: " + dataImport.cleanedStats());
		log.info("--------------FINISHED-------------");
	}

}
