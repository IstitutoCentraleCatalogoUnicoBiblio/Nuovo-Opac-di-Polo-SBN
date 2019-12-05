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
package it.almaviva.spring.opacControllers;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.almaviva.opac.bean.ricerca.result.AuthorityBean;
import it.almaviva.opac.bean.ricerca.result.CampiRicercaDetailBean;
import it.almaviva.opac.bean.ricerca.result.CampiRicercaSinteticaBean;
import it.almaviva.opac.bean.ricerca.result.SolrResponseBean;
import it.almaviva.opac.bean.ricerca.ricerca.SearchBean;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.opac.services.SolrQueryExecuteServices;

/**
 * <h2>Controller di export dei documenti</h2> Classe che gestisce gli url per
 * la ricerca degli autori
 * ICCU IL 7/5/2017 HA RICHIESTO DI NON ESPORTARE DATA E QUERY  DI RICERCA
 * @see SpringFramework
 * 
 * 
 */
@Controller
public class APISearchExportController {

	private static Logger log = Logger.getLogger(APISearchExportController.class);
	@Autowired
	SolrQueryExecuteServices queryMaker;

	GenerateServerStatusServices serverStatusMaker = new GenerateServerStatusServices();

	/**
	 * Riceve una ricerca in input e ritorna un file .txt con l'export del
	 * documento richiesto</br>
	 * L'export puo essere per ID oppure unimarc <b>La richiesta <i>DEVE</i>
	 * essere una sintetica</b>
	 * 
	 * @param search
	 * @param polo
	 * @param exportType
	 * @param response
	 * @throws IOException
	 */
	@CrossOrigin(origins = "*")

	@RequestMapping(value = "/api/{polo}/search/documents/export/{exportType}", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public void startDownload(@RequestBody SearchBean search, @PathVariable String polo,
			@PathVariable String exportType, HttpServletResponse response) throws IOException {
		search.setMaxRows(5000);
		search.setStart(0);
		// Controllo del controller e dei dati ricevuti in post
		log.info("Controller: " + search.toString() + "Polo" + polo);
		SolrResponseBean solr = queryMaker.searchRun(search, polo);
		String timeStamp = new SimpleDateFormat("HH:mm_dd-MM-yyyy").format(new Date(System.currentTimeMillis()));

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition",
				"attachment;filename=Export_" + exportType.toLowerCase() + "_" + timeStamp + ".txt");
		response.setCharacterEncoding("UTF-8");

		ServletOutputStream out = response.getOutputStream();
		//out.println("Exported on " + timeStamp);

		//out.println(solr.getQuery());
		//out.println("\n\n");
		if (solr.getDocs() != null) {
			List<CampiRicercaDetailBean> arr = solr.getDocs();
		//	out.println("N.doc: " + arr.size());
			for (CampiRicercaDetailBean str : arr) {

				if (exportType.toUpperCase().equals("ID")) {
					out.println(str.getId());
					log.info("Returning id");

				} else if (exportType.toUpperCase().equals("UNIMARC")) {
					out.println(str.getUnimarc()+ "\n");
					log.info("Returning unimarc ");

				} else {
					out.println("Cannot export requested format: " + exportType);
					out.println("Please use ID or UNIMARC");
				}

			}

		} else {
			List<CampiRicercaSinteticaBean> arr = solr.getSintetica();
			//out.println("N.doc: " + arr.size());
			for (CampiRicercaSinteticaBean str : arr) {

				if (exportType.toUpperCase().equals("ID")) {
					out.println(str.getId()); //+ "\n"
					log.info("Returning Exported data");

				} else if (exportType.toUpperCase().equals("UNIMARC")) {
					String unimarc = new String(str.getUnimarc().getBytes(), "ISO-8859-1");
					out.println(unimarc + "\n");
				} else {
					log.info("Cannon Export");

					out.println("Cannot export requested format: " + exportType);
					out.println("Please use ID or UNIMARC");
				}

			}

		}
		log.info("Closing request");

		out.flush();
		out.close();

	}

	/**
	 * Riceve una ricerca in input e ritorna un file .txt con l'export del
	 * documento autore richiesto</br>
	 * L'export puo essere per ID oppure unimarc
	 * 
	 * @param search
	 * @param polo
	 * @param exportType
	 * @param response
	 * @throws IOException
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/{polo}/search/authority/export/{exportType}", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public void startDownloads(@RequestBody SearchBean search, @PathVariable String polo,
			@PathVariable String exportType, HttpServletResponse response) throws IOException {
		search.setMaxRows(5000);
		search.setStart(0);
		// Controllo del controller e dei dati ricevuti in post
		log.info("Controller: " + search.toString() + "Polo" + polo);
		SolrResponseBean solr = queryMaker.searchAuth(search, polo);
		String timeStamp = new SimpleDateFormat("HH:mm_dd-MM-yyyy").format(new Date(System.currentTimeMillis()));

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition",
				"attachment;filename=Export_" + exportType.toLowerCase() + "_" + timeStamp + ".txt");
		response.setCharacterEncoding("UTF-8");

		ServletOutputStream out = response.getOutputStream();
		//out.println("Exported on " + timeStamp);

		//out.println(solr.getQuery());
		//out.println("\n\n");
		if (solr.getDocs() != null) {
			List<AuthorityBean> arr = solr.getAutori();
			//out.println("N.doc: " + arr.size());
			for (AuthorityBean str : arr) {

				if (exportType.toUpperCase().equals("ID")) {
					out.println(str.getIdAuth());
					log.info("Returning id");

				} else if (exportType.toUpperCase().equals("UNIMARC")) {
					out.println(str.getUnimarc()+ "\n");
					log.info("Returning Unimarc");

				} else {
					out.println("Cannot export requested format: " + exportType);
					out.println("Please use ID or UNIMARC");
				}

			}

		} else {
			List<CampiRicercaSinteticaBean> arr = solr.getSintetica();
			out.println("N.doc: " + arr.size());
			for (CampiRicercaSinteticaBean str : arr) {

				if (exportType.toUpperCase().equals("ID")) {
					out.println(str.getId()+ "\n");

				} else if (exportType.toUpperCase().equals("UNIMARC")) {
					String unimarc = new String(str.getUnimarc().getBytes(), "ISO-8859-1");
					out.println(unimarc+ "\n");
				} else {
					out.println("Cannot export requested format: " + exportType);
					out.println("Please use ID or UNIMARC");
				}

			}

		}
		log.info("Closing request JSON");

		out.flush();
		out.close();

	}
}
