package it.almaviva.opac.services;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.almaviva.utils.Costanti;

public class PropertiesLoader {
		private Map<String, String> props = new HashMap<>();
		private final Properties p = new Properties();
		static Logger log = Logger.getLogger(PropertiesLoader.class);
		private  String props_path_file =System.getProperty("user.home") + Costanti.file_properties_opac_installazione;
		
		public PropertiesLoader() {
				load_props();
		}

		public String getProps(String key) {
			key = key.toUpperCase().trim();
			return props.get(key);
		}

		private void load_props() {
			try {
				p.load(new FileInputStream(props_path_file));
				log.debug("------> Reading properties <----");
				Enumeration<?> e = p.propertyNames();
			
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String value = p.getProperty(key);
					log.debug("Key : " + key + ", Value : " + value);
					props.put(key, value);
				}
				log.debug("------> Finish properties <----");
			} catch (Exception e) {
				//NullPointer in seguito
				props = null;
				log.error("Error load props", e);
			}
		}
	}

