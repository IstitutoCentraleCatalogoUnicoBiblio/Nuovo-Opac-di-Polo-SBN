package it.almaviva.sbnweb.util;

public class CleanNoSort {
	//Caratteri no sort unimarc
	private static String START_PRETITOLO_NAP = "\uC298";
	private static String END_PRETITOLO_NAP = "\uC29C";
	private static String START_PRETITOLO_GENERICO = "\u0088";
	private static String END_PRETITOLO_GENERICO = "\u0089";
	
	//private static String TO_REPLACE_START = "<<";
	//private static String TO_REPLACE_END = ">>";
	private static String TO_REPLACE_START = "";
	private static String TO_REPLACE_END = "";
	
	public static String clean(String str) {
		
		return str.replaceAll(START_PRETITOLO_GENERICO, TO_REPLACE_START)
				.replaceAll(END_PRETITOLO_GENERICO, TO_REPLACE_END)
				.replaceAll(START_PRETITOLO_NAP, TO_REPLACE_START)
				.replaceAll(END_PRETITOLO_NAP, TO_REPLACE_END);
		
	}
}
