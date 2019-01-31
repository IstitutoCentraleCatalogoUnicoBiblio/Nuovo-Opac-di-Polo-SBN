package it.iccu.sbn.opac2.mlol.integration.client.util;

import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

public class Utils {
	
	public static final Properties argsToProperties(String... args) {
		Properties p = new Properties();
		for (String arg : args) {
			String[] values = arg.split("\\=");
			if (values.length == 2)
				p.put(values[0].toLowerCase(), values[1]);
		}
		
		return p;
	}
	
	public static InputStream getStream(String resource) {
		return Utils.class.getClassLoader().getResourceAsStream(resource);
	}
	
	public static final boolean isFilled(Collection<?> value) {
		return (value != null && value.size() > 0 );
	}
	
	public static final boolean isFilled(Object[] value) {
		return (value != null && !(value.length < 1) );
	}

	public static final boolean isFilled(String value) {
		return (value != null && !"".equals(value.trim()) );
	}
	
	public static final int length(String value) {
		if (value != null)
			return value.length();
		
		return 0;
	}
	
	public static final String safeSubstringAndTrim(final String str, int start, int end) {
		int len = length(str);
		if (len < 1)
			return null;
		
		if (start > end)
			return null;
		
		if ( (start + 1) > len)
			return null;

		if ( (end + 1) > len)
			end = len;
		
		return str.substring(start, end).trim();
	}
	
	public static final String safeSubstringAndTrim(final String str, int start) {
		return Utils.safeSubstringAndTrim(str, start, length(str) );
	}

}
