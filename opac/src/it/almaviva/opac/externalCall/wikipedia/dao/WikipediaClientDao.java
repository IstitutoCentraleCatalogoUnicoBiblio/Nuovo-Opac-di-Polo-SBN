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
package it.almaviva.opac.externalCall.wikipedia.dao;

import org.apache.log4j.Logger;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.almaviva.opac.externalCall.wikipedia.WikipediaClientModelInt;
import it.almaviva.utils.Costanti;

//Dao di chiamata a wikipedia
public class WikipediaClientDao implements WikipediaClientModelInt {
	private static String URL = "https://it.wikipedia.org/w/api.php?action=query&list=search&srsearch={search}&srwhat=text&srprop=timestamp&format=json";

	static Logger log = Logger.getLogger(WikipediaClientDao.class);

	@Override
	public Object callWikipedia(String keywordQuery) {
		try {
			log.info("Calling wikipedia server...");
			RestTemplate rest = new RestTemplate();
			SimpleClientHttpRequestFactory rf =
				    (SimpleClientHttpRequestFactory) rest.getRequestFactory();
				rf.setReadTimeout(Costanti.some_second_timeout);
				rf.setConnectTimeout(Costanti.some_second_timeout);
			Object o = rest.getForObject(URL, Object.class, keywordQuery);
			log.info("Wikipedia success!");
			return o;
		} catch ( Exception e) {
			log.info("Wikipedia è andato male", e);
			//log.error(e);
			return null;
		}
	}
}
