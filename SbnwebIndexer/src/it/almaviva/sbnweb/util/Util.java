package it.almaviva.sbnweb.util;

import java.util.List;
import java.util.Vector;

public class Util {
	public static boolean isNumeric(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			// s is not numeric
			return false;
		}
	}

	public static String cleanStr(String str) {

		if (str != null) {
			str = str.replace(" ", "");
			if (str.equals("")) {

				return null;
			} else {
				return str;
			}
		} else {
			return null;
		}
	}
	
	// Fix per $b in 899 all'interno di tesi di laurea non presente
	public static String isFilled899(Vector<String> list, Integer i) {
		try {
			return list.get(i);
		} catch (ArrayIndexOutOfBoundsException e) {
			return " ";
		}
	}
	// Fix per $b in 899 all'interno di tesi di laurea non presente
		public static String isFilled899(List<String> list, Integer i) {
			try {
				return list.get(i);
			} catch (ArrayIndexOutOfBoundsException e) {
				return " ";
			}
			catch (IndexOutOfBoundsException e) {
				return " ";
			}
		}
}
