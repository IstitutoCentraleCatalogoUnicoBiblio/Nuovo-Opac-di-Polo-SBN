package it.almaviva.opac.services;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.almaviva.utils.Costanti;

public class PropertiesLoader {
	private static Map<String, String> props = new HashMap<>();

	static Logger log = Logger.getLogger(PropertiesLoader.class);
	
	private static String props_path_file = System.getProperty("user.home")
			+ Costanti.file_properties_opac_installazione;

	public PropertiesLoader() {
		if (props.isEmpty())
			load_props();
	}

	public String getProps(String key) {
		key = key.toUpperCase().trim();
		return props.get(key);
	}

	public static String getProperty(String key) {
		if (props.isEmpty())
			load_props();
		key = key.toUpperCase().trim();
		return props.get(key);
	}
	public static void reload() {
		props.clear();
		load_props();
	}

	private static void load_props() {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(props_path_file));
			log.info("------> Reading properties <----");
			log.info("Reading props:" + props_path_file);
			Enumeration<?> e = p.propertyNames();

			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = p.getProperty(key);
				log.debug("Key : " + key + ", Value : " + value);
				props.put(key, value);
			}
			log.info("------> Finish properties <----");
		} catch (Exception e) {
			// NullPointer in seguito
			props = null;
			log.error("Error load props", e);
		}
	}
}
