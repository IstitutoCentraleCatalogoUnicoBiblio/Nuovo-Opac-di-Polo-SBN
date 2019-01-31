package it.inera.opacsbn.solrmarc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeConversion {
	
	public static final String DATETIME_PATTERN = "yyyyMMdd";
	private static final String SOLR_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	/**
	 *	Trasforma DDMMYYYY in YYYY-MM-01Thh:mm:ssZ 
	 *	2008-10-01T04:00:00Z 
	 * @throws Exception 
	 * 	
	 */
	public static String getSolrDatetime(String input) throws Exception  {
		return getSolrDatetime(input, DATETIME_PATTERN);
	}
	
	
	public static String getSolrDatetime(String input, String pattern) throws Exception  {
		DateFormat formatterInput = new SimpleDateFormat(pattern);
		Date date = formatterInput.parse(input);
		DateFormat formatterOutput = new SimpleDateFormat(SOLR_DATETIME_PATTERN);
		String output = formatterOutput.format(date);
		return output;
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(getSolrDatetime("20100615"));
	}
	
	
}

