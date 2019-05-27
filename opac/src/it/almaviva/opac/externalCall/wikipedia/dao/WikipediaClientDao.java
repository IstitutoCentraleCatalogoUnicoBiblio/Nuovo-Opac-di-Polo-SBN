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

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.apache.log4j.Logger;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.almaviva.opac.externalCall.wikipedia.WikipediaClientModelInt;
import it.almaviva.opac.services.PropertiesLoader;
import it.almaviva.utils.Costanti;
import it.almaviva.utils.opac.Util;

//Dao di chiamata a wikipedia
public class WikipediaClientDao implements WikipediaClientModelInt {
	private static String URL = "https://it.wikipedia.org/w/api.php?action=query&list=search&srsearch={search}&srwhat=text&srprop=timestamp&format=json";
	public static String use_proxy_url = null;
	public static Integer use_proxy_port = null;

	static Logger log = Logger.getLogger(WikipediaClientDao.class);
	 public WikipediaClientDao() {
			loadProxyConf();
	}
	public static void loadProxyConf() {
		PropertiesLoader properties = new PropertiesLoader();
		String proxy_url = properties.getProps("PROXY_CONNECTION_URL");
		String proxy_port = properties.getProps("PROXY_CONNECTION_PORT");
		use_proxy_url = Util.isFilled(proxy_url) ? proxy_url : null;
		try {
			use_proxy_port = Util.isFilled(proxy_port) ? Integer.parseInt(proxy_port) : 8080;
		} catch (NumberFormatException e) {
			log.error("use_proxy_port impostato a 8080 per evitare errori", e);
			use_proxy_port = 8080;
		}
		if(Util.isFilled(use_proxy_url)) 
			log.info("Imposto il proxy per wikipedia: " + use_proxy_url + ":" + use_proxy_port);
	}
	@Override
	public Object callWikipedia(String keywordQuery) {
		try {
			log.info("Calling wikipedia server...");
			RestTemplate rest = new RestTemplate();
			SimpleClientHttpRequestFactory rf =
				    (SimpleClientHttpRequestFactory) rest.getRequestFactory();
				rf.setReadTimeout(Costanti.some_second_timeout);
				rf.setConnectTimeout(Costanti.some_second_timeout);
				if(Util.isFilled(use_proxy_url)) {
					log.debug("Imposto il proxy per wikipedia: " + use_proxy_url + ":" + use_proxy_port);
					Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(use_proxy_url, use_proxy_port));
					rf.setProxy(proxy);
				}
				
			Object o = rest.getForObject(URL, Object.class, keywordQuery);
			//log.info(o);
			log.info("Wikipedia success!");
			return o;
		} catch ( Exception e) {
			log.info("Wikipedia è andato male", e);
			//log.error(e);
			return null;
		}
	}
}
