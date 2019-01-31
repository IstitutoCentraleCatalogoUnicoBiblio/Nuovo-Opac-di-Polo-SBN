package it.almaviva.sbnweb.auth.cleanDatazione;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  * @version 1.7
 * </br><b>DESCRIZIONE</b></br>
 * Classe usata per la pulizia di stringe dei campi di datazione di per core di Authority nel DB Solr.</br>
 *
 * Per la creazione di un nuovo oggetto usare il metodo costruttore passandogli la stringa sporca</br>
 *
 *	<b>DATAZIONI AMMESSE </b></br>
 *	</ br>
 *	&#60;1840-1890> date di nascita e morte</br>
 *	&#60;1924- > autore probabilmente vivente</br>
* 	&#60;n. 1870> anno di morte sconosciuto</br>
*	&#60;m. 1982> anno di nascita sconosciuto</br>
*	&#60;fl. 1550> unico anno di attivit� conosciuto</br>
*	&#60;fl. 1760-1825> anni di attivit� conosciuti</br>
*	&#60;sec. 19. 1. met� > vissuto nella prima met� del secolo</br>
*	&#60;sec. 19. 2. met� > vissuto nella seconda met� del secolo</br>
*	&#60;sec. 19.> conosciuto il secolo</br>
*	&#60;sec. 19.-20.> vissuto tra i due secoli</br>
*	&#60;1845?-1905> anno di nascita probabile</br>
*	&#60;1890-ca. 1960> anno di morte approssimativo</br>
*	&#60;n. 1889?> anno di nascita probabile</br>
*	&#60;ca. 1890-1960> anno di nascita approssimativo</br>
*	&#60;ca. 1896-ca. 1967> anno di nascita e morte approssimativi</br>
*	&#60;ca. n. 32 a.c.> anno di nascita e morte approssimativi avanti Cristo</br>
*	&#60;ca. n. 32 a.c.> anno di nascita e morte approssimativi dopo Cristo</br>
 *
 */
public class DatazioneCleaner {
	
	private String datazioneSporca;
	
	//Costanti di servizio
	private final String  SECOLO = "sec";
	private final String  PUNCT = "..";
	private final String  MORTE = "m";
	private final String  NASCITA = "n";
	//Pattern dove deve cercare il testo, nel nostro caso all'interno dei maggiore minore
	private final Pattern pattern = Pattern.compile("<(.+?)>");

	//costruttore viene impostata la data stringa come � schifosa
	
	/**
	 * @param datazioneSporca
	 * </br>
	 * Per la creazione di un nuovo oggetto usare il metodo costruttore passandogli la stringa sporca
	 * 
	 */
	public DatazioneCleaner(String datazioneSporca) {
		super();
		datazioneSporca = "<" + datazioneSporca +">";
		try {
			Matcher matcher = pattern.matcher(datazioneSporca);
			matcher.find();
			this.datazioneSporca = matcher.group(1).trim();
		}catch (IllegalStateException e) {
			this.datazioneSporca = "";
		}
		
	}
	
	//metodo pubblico per il ritorno di una lista di date identificate nella stringa
	
	/**
	 * Ritorna una lista di date identificate nella stringa della datazione</br>
	 * 
	 * @return List: String
	 * 
	 */
	public List<String> cleanedList() {
		return this.clean();
	}
	
	
	/**
	 *  Ritorna una stringa di date concatenate</br>
	 * 
	 *
	 * @return String
	 *
	 * Es: '1596_YOUR_SEPARATOR_HERE_1650'
	 */
	public String cleanedString(String separator){
		String toReturn = new String();
	List<String> strings= this.clean();
	Iterator<String> list = strings.iterator();
		for (String s: strings){
			list.next();
			String sep = (separator == null) ? "-" : separator;
			if (list.hasNext()) {
				toReturn += s + sep;
			} else {
				toReturn += s ;
			}
		}
		return toReturn;
	//	return String.join((separator == null) ? "-" : separator, this.clean()).trim();
		
	}
	/**
	 * Ritorna una stringa di date concatenate</br>
	 * 
	 * @return String
	 *  Es: '1596-1650'
	 */
	public String cleanedString(){
		return this.cleanedString(null);
	}
	//Ritorna una stringa per permettere un log Pulito
	
	/**
	 * Ritorna una string per permettere un log di test chiaro e pulito</br>
	 * Es: 'DatazioneCleaner: 1500-1577 ---> 1500-1577'</br>
	 * 
	 * @return String
	 * 
	 */
	public String toLogClean() {
		return "DatazioneCleaner: '" + datazioneSporca + "' ---> '" + this.cleanedString(null) +"'";
	}
	//avvio della pulizia della stringa
	private List<String> clean() {
		//crea una nuova stringa
		String cleaned = new String(datazioneSporca);
		
		//clean iniziale e imposta a minuscolo
		cleaned = cleaned.toLowerCase().trim();
		
		//clean tramite regex ottenendo solo numeri
		cleaned = cleaned.replaceAll("({4}[^0-9])", " ");
		//trim
		cleaned = cleaned.trim();
		//pulizia spazi interni e trasformati in un solo pipe
		cleaned = cleaned.replaceAll("( +)", "|");
	
		//controllo del secolo
		if( isSecolo()) {
			//controllo della presenza 2.meta nella stringa trimmata
			if(datazioneSporca.toLowerCase().replaceAll("( +)", "").trim().replace("met�", "meta").indexOf("2.meta") > -1)
				cleaned = number(cleaned.substring(0,cleaned.indexOf("|")), true).toString() + "51|" + number(cleaned.substring(0,cleaned.indexOf("|")), false).toString() +"00" + "|sec" ;
		
			//controllo della presenza 1.meta nella stringa trimmata
			else if (datazioneSporca.toLowerCase().replaceAll("( +)", "").trim().replace("met�", "meta").indexOf("1.meta") > -1)
				cleaned = number(cleaned.substring(0,cleaned.indexOf("|")), true).toString() + "01|" + number(cleaned.substring(0,cleaned.indexOf("|")), true).toString() +"50" + "|sec" ;
			
			//altrimenti � un sec normale
			else 
				if (cleaned.indexOf("|") > 0)
					cleaned = number(cleaned.substring(0,cleaned.indexOf("|")), true).toString()+ "01|" + number(cleaned.substring(cleaned.indexOf("|") + 1), true).toString()+"00" + "|sec";
				else 
					
					cleaned = number(cleaned.substring(0), true).toString()+ "01|" + number(cleaned.substring(cleaned.indexOf("|") + 1), false).toString()+"00" + "|sec";

		} else {
		//	if (datazioneSporca.replaceAll("(\\p{Punct}+)", "").indexOf(SECOLO) > -1) {
				//cleaned = number(cleaned, true).toString() +"00"+ cleaned.substring(cleaned.indexOf("|"));
		//	}
			cleaned = formatter(cleaned);
			
			
			//cleaned += (datazioneSporca.replaceAll("(\\p{Punct}+)", "").indexOf(SECOLO) > -1) ? number(cleaned.substring(0, 2), true).toString() ;
			cleaned =  (datazioneSporca.indexOf(PUNCT) > -1 ) ? number(cleaned, false).toString() +"00" +"|"+number(cleaned, false).toString() +"99": cleaned;

		}
		cleaned = (datazioneSporca.toLowerCase().indexOf("fl") > -1 ) ? "" : cleaned;
		cleaned = isAC() ? "" : cleaned;

		try {
			cleaned = (cleaned.substring(0,1).equals("-")) ? "": cleaned;
		} catch(StringIndexOutOfBoundsException e ) {
			cleaned = "";
		}
		//controllo la presenza della parola "sec" all'interno della data iniziale

		return Arrays.asList(cleaned.split("\\|"));
	}
	//controllo se contiene "sec"
	private Boolean isSecolo() {
		return (datazioneSporca.replaceAll("(\\p{Punct}+)", "").toLowerCase().indexOf(SECOLO.toLowerCase()) > -1) ? true : false;
	}
	
	//controllo di una data avanti Cristo 
	private Boolean isAC() {
		return (datazioneSporca.replaceAll("(\\p{Punct}+)", "").toLowerCase().indexOf("ac") > -1) ? true : false;

	}
	//metodo che formatta il ritorno
	private String formatter(String str) {
		//se contiene solo la data di nascita
		if( datazioneSporca.toLowerCase().indexOf(NASCITA) > -1) {
			str = (str.length() == 2 && isSecolo()) ? str+"00":str;
			str +="|"+"XXXX";
		//se contiene la data di morte
		} else if( datazioneSporca.toLowerCase().indexOf(MORTE) > -1){		
			str = "XXXX" + "|"  + ((str.length() == 2 && isSecolo()) ? str+"99":str);
		}
		Integer xx = datazioneSporca.toLowerCase().trim().indexOf(str);
		if( xx == 1)  {
			str = str+"|"+"XXXX";
		} else if (xx > 1 && !isSecolo()){
			str = "XXXX" + "|" + str;

		} /*else if (isSecolo() && xx > 1 ) {
			str = number(str, true).toString()+ "00|" + number(str , true).toString()+"99";
		} */
		
		return str;
	}
	//il flag serve per identificare se stai cercando la scritta del secolo
	private Integer number(String input, Boolean flagSecolo) {
		  try {
			  Integer a = (flagSecolo) ? Integer.parseInt(input) - 1 : Integer.parseInt(input);
		    return a;
		  }
		  catch (NumberFormatException e) {
		    // is not numeric
			 // System.out.println("SOME WAS WRONG IN DATE" + datazioneSporca);
		    return -1;
		  }
		}
}
