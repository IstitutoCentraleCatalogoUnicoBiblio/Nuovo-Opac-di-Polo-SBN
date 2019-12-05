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
package it.almaviva.boot.appVersion;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class VersioningReader {
	static Logger log = Logger.getLogger(VersioningReader.class);
	//Almaviva3 23/07/2019 manutenzione, aggiunto versioning GIT per la lettura della versione. 
	//non oiu manuale 
	private final static String VERSION = "git.build.version"; //"VERSION";
	private final static String LAST_RELEASE = "git.build.time";//"LAST_RELEASE";
	private final static String GIT_VERSION = "git.commit.id.abbrev";
	public static Version loadedVersion = null;

	private static void read() {
		CodeConverter propsReader = load_props("/version.properties");//load_props("/appVersion.properties");
		String version = propsReader.get(VERSION);
		String last_release = propsReader.get(LAST_RELEASE);
			String git_version = propsReader.get(GIT_VERSION);
		
		
		loadedVersion = new Version();
		loadedVersion.setLast_release(last_release);
		loadedVersion.setVersion(version + "-" + git_version);
	}
	
	private static CodeConverter load_props(String props_file) {
		final Properties p = new Properties();
		try {
			p.load(VersioningReader.class.getResourceAsStream(props_file) );
			
			return new CodeConverter() {
				@Override
				public String get(String code) {
					String value = p.getProperty(code);
					return value != null ? value : code;
				}
				public String getNull(String code) {
					String value = p.getProperty(code);
					return value;	
				}

			};
			
		} catch (IOException e) {
			log.error("", e);
		}
		
		return null;
	}
	public static Version getVersion () {
		if(loadedVersion == null)
			read();
		
		return loadedVersion;
	}
}
