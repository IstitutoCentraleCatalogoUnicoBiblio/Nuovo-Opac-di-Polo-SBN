package it.inera.opacsbn.solrmarc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.marc4j.MarcStreamWriter;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.solrmarc.index.SolrIndexer;

import it.almaviva.sbnweb.util.CleanNoSort;
import it.inera.opacsbn.solrmarc.numeri.Numeri_tip;
import it.inera.opacsbn.solrmarc.rappres.Rappres_tip;


/**
 * 
 * @author Renato Eschini r.eschini@inera.it
 *
 */
public class OpacSbnIndexer extends SolrIndexer {

	/** Carattere di start del pretitolo **/
	public static String START_PRETITOLO = "\u0088";
	/** Carattere di end del pretitolo **/
	public static String END_PRETITOLO = "\u0089";

	public static String project = "sbn";

	/**
	 * File di properties con mapping:
	 */
	public static Properties props_bib = null;
	public static Properties props_prov = null;
	public static Properties props_raccolte = null;

	public final static String START_PRETITOLO_PROPS = "opac.start_pretitolo";
	public final static String END_PRETITOLO_PROPS = "opac.end_pretitolo";
	public final static String PROJECT_PROPS = "opac.project";

	/**
	 * File xml con i dati opacinfo da Opencms, per librarsi. Se nullo perchè file inesistente, non viene valutato nei metodi che lo usano
	 */
	//public static InfoRecordsXml infoRecordsXml = null;

	public OpacSbnIndexer(String indexingPropsFile, String[] propertyDirs) {
		super(indexingPropsFile, propertyDirs);
		}

	/**
	 * 
	 * @param record
	 * @return
	 */
	public String getAbstract(final Record record) {
		// controllo se ho un file opacinfo.xml instanziato
		/*if (infoRecordsXml == null) return null;
		String bid = ((ControlField) record.getVariableField("001")).getData();
		try {
			return infoRecordsXml.getAbstract(bid);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} */
		return null;
	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	public String getIndice(final Record record) {
		// controllo se ho un file opacinfo.xml instanziato
	/*	if (infoRecordsXml == null) return null;
		String bid = ((ControlField) record.getVariableField("001")).getData();
		try {
			return infoRecordsXml.getIndice(bid);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}*/
		return null;
	}

	/**
	 * 700:710 tutto separato da spazio tranne $3 e $4 
	 */
	public String getAutore(final Record record) {

		StringBuilder buffer = new StringBuilder("");

		List<VariableField> marcFieldList = record.getVariableFields("700");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				if (subfield.getCode() != '3' && subfield.getCode() != '4') {

					//String value = cleanPretitolo(subfield.getData());


					/* if (buffer.length() > 0 && !value.startsWith(",")) {
						buffer.append(" ");
					} */
					buffer.append(CleanNoSort.clean(subfield.getData()));

				}
			}

		}

		marcFieldList = record.getVariableFields("710");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				if (subfield.getCode() != '3' && subfield.getCode() != '4') {

					String value = cleanPretitolo(subfield.getData());
					/* if (buffer.length() > 0 && !value.startsWith(","))
						buffer.append(" "); */

					buffer.append(value);

				}
			}
		}

		if (buffer.length() > 0) {
			return buffer.toString();
		}
		return null;
	}

	/**
	 * 701:711 tutto separato da spazio tranne $3 e $4 
	 */
	public String getCoAutore(final Record record) {

		StringBuilder buffer = new StringBuilder("");

		List<VariableField> marcFieldList = record.getVariableFields("701");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				if (subfield.getCode() != '3' && subfield.getCode() != '4') {

					//String value = cleanPretitolo(subfield.getData());


					/* if (buffer.length() > 0 && !value.startsWith(",")) {
						buffer.append(" ");
					} */
					buffer.append(CleanNoSort.clean(subfield.getData()));

				}
			}

		}

		marcFieldList = record.getVariableFields("711");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				if (subfield.getCode() != '3' && subfield.getCode() != '4') {

					String value = cleanPretitolo(subfield.getData());
					/* if (buffer.length() > 0 && !value.startsWith(","))
						buffer.append(" "); */

					buffer.append(value);

				}
			}
		}

		if (buffer.length() > 0) {
			return buffer.toString();
		}
		return null;
	}


	/**
	 * 701:711 tutto separato da spazio tranne $3 e $4 
	 */
	public Set<String> getCoAutoreM(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		StringBuilder buffer = new StringBuilder("");

		List<VariableField> marcFieldList = record.getVariableFields("701");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				if (subfield.getCode() != '3' && subfield.getCode() != '4') {

					//String value = cleanPretitolo(subfield.getData());


					/* if (buffer.length() > 0 && !value.startsWith(",")) {
						buffer.append(" ");
					} */
					buffer.append(CleanNoSort.clean(subfield.getData()));

				}
			}
			if (buffer.length() > 0) {
				result.add (buffer.toString());
			        buffer = new StringBuilder("");
			}
		}

		marcFieldList = record.getVariableFields("711");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				if (subfield.getCode() != '3' && subfield.getCode() != '4') {

					String value = cleanPretitolo(subfield.getData());
					/* if (buffer.length() > 0 && !value.startsWith(","))
						buffer.append(" "); */

					buffer.append(value);

				}
			}
			if (buffer.length() > 0) {
				result.add (buffer.toString());
			        buffer = new StringBuilder("");
			}
   
		}

		return result;
	}

	/* 
		si prende tutto il 210 
		(primo)$a
		$a $a ecc
		 $b
		:$c
		,$d
		(primo)$e(
		;$e;$e ecc $f $f $f ecc :$g:$g ecc ,$h)
	 */
	public String getPublish(final Record record) {
		StringBuilder buffer = new StringBuilder("");

		boolean parentesi = false;
		boolean first = true;

		List<VariableField> marcFieldList = record.getVariableFields("210");
		for (VariableField vf : marcFieldList) {

			// $a $a $a $a ecc...
			DataField marcField = (DataField) vf;

			List<Subfield> subfields =  marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();

				switch (code) {
				case 'a':
					if (buffer.length() > 0) buffer.append(" ; ");
					buffer.append(cleanPretitolo(subfield.getData()));
					break;
				case 'b':
					if (buffer.length() > 0) buffer.append(" ");
					buffer.append(subfield.getData());
					break;
				case 'c':
					buffer.append(" : ").append(subfield.getData());
					break;
				case 'd':
					buffer.append(", ").append(subfield.getData());
					break;
				case 'e':
					if (first) {
						buffer.append(" (").append(subfield.getData());
						first = false;
						parentesi = true;
					} else {
						buffer.append(" ; ").append(subfield.getData());
					}
					break;
				case 'f':
					buffer.append(" ").append(subfield.getData());
					break;
				case 'g':
					buffer.append(" : ").append(subfield.getData());
					break;
				case 'h':
					buffer.append(", ").append(subfield.getData());
					break;
				default:
					break;
				}

			}

		}
		if (parentesi) {
			buffer.append(")");
		}

		return buffer.toString();
	}

	public String getAllFieldsConcat(final Record record,String tagstr, String separator,String nosub) {
		StringBuilder buffer = new StringBuilder("");

		if (separator == null)
			separator = " - ";
		List<VariableField> marcFieldList = record.getVariableFields(tagstr);
		for (VariableField vf : marcFieldList) {

			// $a $a $a $a ecc...
			DataField marcField = (DataField) vf;

			List<Subfield> subfields =  marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();
				if (nosub != null && nosub.contains(code+""))
					continue;
				if (buffer.length() > 0) buffer.append(separator);
				buffer.append(subfield.getData());

			}

		}

		return buffer.toString();
	}
	
	public Set<String> getCollocazione(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		
		StringBuilder buffer = new StringBuilder("");
		List<VariableField> marcFieldList = record.getVariableFields("950");
		for (VariableField vf : marcFieldList) {

			DataField marcField = (DataField) vf;
			boolean firstE = true;
			
			List<Subfield> subfields =  marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();
				
				if (code == 'd') {
					firstE = true;
					if (buffer.length() > 0) {
						result.add (buffer.toString());
						buffer = new StringBuilder("");
					}
					buffer.append(subfield.getData().substring(3));
	
				}
				if (code == 'e') {
					if (firstE) {
						if (subfield.getData().length() > 24) {
						String seq = subfield.getData().substring(24);
						if (seq.length() > 20)
							   seq = seq.substring(0,20);
						if (seq != null)
							seq = seq.trim();
						if (!(seq.isEmpty()))
						  buffer.append(" ").append(seq);
						}
						firstE = false;
					}
				}
			}
		}
		if (buffer.length() > 0)
			result.add (buffer.toString());
		return result;
		
		
	}

	public String getSubString(final Record record,String tagstr, String from) {
		StringBuilder buffer = new StringBuilder("");
		int fromI = 0;
		try {
			 fromI = Integer.parseInt(from);
		} catch (NumberFormatException nfe) { 
		   fromI = 0;
		}
		
		Set<String> field = getFieldList(record, tagstr);
		for (Iterator<String> iterator = field.iterator(); iterator.hasNext();) {
		   
			//String data= iterator.next();
			// logger.error("getSubString: data " +  data);
			buffer.append(iterator.next().substring(fromI));
			
		}	

		return buffer.toString();
	}

	/*
	 isbd = 200
		(primo)$a
		;$a;$a ecc
		(primo)[$b $b $b ecc] 
		.$c.$c ecc
		=$d=$d ecc
		:$e:$e ecc
		/$f/$f ecc
		;$g;$g ecc
		;$v;$v ecc
	 */
	public String getIsbd(final Record record) {
		StringBuilder buffer = new StringBuilder("");

		List<VariableField> marcFieldList = record.getVariableFields("200");
		for (VariableField vf : marcFieldList) {

			// (primo)$a;$a;$a ecc
			DataField marcField = (DataField) vf;


			boolean firstA = true;

			boolean parentesiB = false;
			int idxLastB = 0;
			boolean firstB = true;

			List<Subfield> subfields =  marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();


				switch (code) {

				case 'a':
					if (firstA) {
						buffer.append(cleanPretitolo(subfield.getData()));
						firstA = false;
					} else {
						buffer.append(" ; ").append(cleanPretitolo(subfield.getData()));	
					}
					break;

				case 'b':
					if (firstB) {
						buffer.append(" [").append(subfield.getData());
						firstB = false;
						parentesiB = true;
						idxLastB = buffer.length();
					} else {
						buffer.append(" ").append(subfield.getData());
						idxLastB = buffer.length();
					}
					break;

				case 'c':
					buffer.append(" . ").append(subfield.getData());
					break;

				case 'd':
					buffer.append(" = ").append(subfield.getData());
					break;

				case 'e':
					buffer.append(" : ").append(subfield.getData());
					break;

				case 'f':
					buffer.append(" / ").append(subfield.getData());
					break;

				case 'g':
					buffer.append(" ; ").append(subfield.getData());
					break;

				case 'v':
					buffer.append(" ; ").append(subfield.getData());
					break;

				default:
					break;
				}	
			}

			// inserisco la parentesi dopo l'ultima b inserita
			if (parentesiB) {
				buffer.insert(idxLastB, "] ");
			}

		}

		return buffer.toString();
	}


	/*
	 isbd = 200
		(primo)$a
		;$a;$a ecc
		(primo)[$b $b $b ecc] 
		.$c.$c ecc
		=$d=$d ecc
		:$e:$e ecc
		/$f/$f ecc
		;$g;$g ecc
		;$v;$v ecc
	 */
	public String getIsbdSimili(final Record record) {
		StringBuilder buffer = new StringBuilder("");

		List<VariableField> marcFieldList = record.getVariableFields("200");
		for (VariableField vf : marcFieldList) {

			// (primo)$a;$a;$a ecc
			DataField marcField = (DataField) vf;


			boolean firstA = true;

			boolean parentesiB = false;
			int idxLastB = 0;
			boolean firstB = true;

			List<Subfield> subfields =  marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();


				switch (code) {

				case 'a':
					if (firstA) {
						buffer.append(cleanPretitolo(subfield.getData()));
						firstA = false;
					} else {
						buffer.append(" ; ").append(cleanPretitolo(subfield.getData()));	
					}
					break;

				case 'b':
					if (firstB) {
						buffer.append(" [").append(subfield.getData());
						firstB = false;
						parentesiB = true;
						idxLastB = buffer.length();
					} else {
						buffer.append(" ").append(subfield.getData());
						idxLastB = buffer.length();
					}
					break;

				case 'c':
					buffer.append(" . ").append(subfield.getData());
					break;

				case 'd':
					buffer.append(" = ").append(subfield.getData());
					break;

				default:
					break;
				}	
			}

			// inserisco la parentesi dopo l'ultima b inserita
			if (parentesiB) {
				buffer.insert(idxLastB, "] ");
			}

		}

		return buffer.toString();
	}


	/**
	 * prima   parte: 700-750 a,b,c,d,e,f,g (separati da spazio)  
	 * 
	 * 
	 * seconda parte: 790-791 b <c> : d  : g f  → z 
	 * 
	 * 
	 * terza   parte: 927 c  
	 * 
	 * 
	 * quarta  parte: 411-499  prefix 1700, 1710  a,b,c,d,e,f,g (separati da spazio)  
	 * 
	 * 
	 * quinta  parte: 500 $9
	 * 
	 * 
	 * 
	 * tipo archivio = header pos. 19      se vale I o A === 790-791 b <c> :d  :g f ALTRIMENTI 790-791 b c :d  :g f
	 * 
	 * @param record
	 * @return
	 */
	public Set<String> getNome(final Record record) {
		Set<String> result = getNomeGenerico(record, null, false, false);       
		return result;
	}

	/**
	 * come getNome ma filtrata per $4
	 */
	public Set<String> getNomeRel(final Record record, String $4) {
		Set<String> result = getNomeGenerico(record, $4, false, false);
		return result;
	}

	/**
	 * come getNome ma senza 790-791
	 */
	public Set<String> getNomef(final Record record) {
		Set<String> result = getNomeGenerico(record, null, true, false);
		return result;
	}


	/**
	 * come getNomeVid
	 */
	public Set<String> getNomeVid(final Record record) {
		Set<String> result = getNomeGenerico(record, null, false, true);  
		return result;
	}


	/**
	 * Se $4 == null return true;
	 * Se $4 != null && marcField.getSubfields('4') == $4 return true
	 * else return false;
	 */
	public boolean isNome(DataField marcField, String $4) {
		boolean test = true;
		if ($4 != null) {
			test = false;
			List<Subfield> checkSubfields = marcField.getSubfields('4');
			for (Subfield checkSubfield : checkSubfields) {
				if ($4.equalsIgnoreCase(checkSubfield.getData())) test = true; 
			}
		}
		return test;
	}
//  getNomef   getNomeGenerico(record, null, true, false);
// getNome --> getNomeGenerico(record, null, false, false);  
	public Set<String> getNomeGenerico(final Record record, String $4, boolean nomef, boolean nomeVid) {
		Set<String> result = new LinkedHashSet<String>();
		String separator = "";
		if ("sbnweb".equals(project))
			separator = " ";

		/**
		 *  prima parte: 700-750 a,b,c,d,e,f,g (separati da spazio)
		 */
		int startTag = 700;
		int endTag = 750;


		String subfldTags = "[a|b|c|d|e|f|g]"; 
		if (nomeVid) { // $3<---- vid
			subfldTags = "[3]";
		}

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;

					if (isNome(marcField, $4)) {
						StringBuilder buffer = new StringBuilder("");
						List<Subfield> subfields = marcField.getSubfields();
						for (Subfield subfield : subfields) {
							Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
							if (matcher.matches()) {
								if (buffer.length() > 0 && !subfield.getData().startsWith(","))
									buffer.append(separator != null ? separator : "");
								buffer.append(subfield.getData());
							}
						}
						if (buffer.length() > 0) {
							if (tag > 709 && tag < 720)
								result.add(cleanPretitolo(buffer.toString()));
							else
								result.add(buffer.toString());
						}
					}

				}

			}
		}

		// se è nomef non passo da qui...
		if (!nomef) {
			/**
			 *  seconda parte: 790-791 b <c> : d  : g f  → z
			 */
			startTag = 790;
			endTag = 791;
			subfldTags = "[a|b|c|d|e|g|f|z|y]";

			if (nomeVid) {  // $3<---- vid
			    subfldTags = "[a|b|c|d|e|g|f|3|y]";
			}

			// itero sui tag
			for (int tag = startTag; tag <= endTag; tag++) {
				List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
				if (!marcFieldList.isEmpty()) {
					Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
					for (VariableField vf : marcFieldList) {
						DataField marcField = (DataField) vf;

						if (isNome(marcField, $4)) {
							StringBuilder buffer = new StringBuilder("");
							String vid = "";
							List<Subfield> subfields = marcField.getSubfields();
							for (Subfield subfield : subfields) {
								Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
								if (matcher.matches()) {

									if (buffer.length() > 0 && !subfield.getData().startsWith(","))
										buffer.append(separator != null ? separator : " ");

									String data = subfield.getData();

									/*// se il subfield è c controllo < >
									if ('c' == subfield.getCode()) {
										if (!data.trim().startsWith("<")) data = "<".concat(data);
										if (!data.trim().endsWith(">")) data = data.concat(">");
									}

									// se il subfield è d o g metto i due punti
									if ('d' == subfield.getCode() || 'g' == subfield.getCode()) {
										data = " : ".concat(data);
									}*/
									// se il subfield è z metto →
									if ('z' == subfield.getCode()) {
										data = " -> ".concat(data);
									}
									if ('3' == subfield.getCode()) {
									   vid = data;
									}
									else if ('y' == subfield.getCode() && !buffer.toString().trim().equals(data.trim())) {
										buffer.delete(0,buffer.length());

										if (!nomeVid) {
										 buffer.append(data);
										}
										else
										  vid = "";
									}
									else
									// b e f entrano come arrivano...
									buffer.append(data);
								}
							}
							if (nomeVid && !vid.isEmpty()) {
								result.add(vid);

							}
							else if (buffer.length() > 0) {
								if (tag == 791)
									result.add(cleanPretitolo(buffer.toString()));
								else
									result.add(buffer.toString());

							}
						}
					}
				}
			}
		}
		if (!"sbnweb".equals(project)) {
			/**
			 * terza parte 927c  // $3<----
			 */
			char subField = 'c';

			if (nomeVid) {  // $3<---- vid
				subField = '3';
			}
			if ($4 == null) { // solo se $4 == null, sono in nome e non in nome_rel_$4
				List<VariableField> marcField927List = record.getVariableFields("927");
				for (VariableField vf : marcField927List) {
					DataField marcField927 = (DataField) vf;
					List<Subfield> subfield927cList = marcField927.getSubfields(subField);
					for (Subfield subfield : subfield927cList) {
						if (subfield.getData() != null && !"".equals(subfield.getData())) {
							result.add(subfield.getData());
						}
					}
				}
			}
			/**
			 * quarta  parte: 411-499  prefix 1 700, 1 710  a,b,c,d,e,f,g (separati da spazio)  
			 */
			if ($4 == null) { // solo se $4 == null, sono in nome e non in nome_rel_$4

				startTag = 410;
				endTag = 499;
				subfldTags = "[a|b|c|d|e|f|g]";
				if (nomeVid) {  // $3<---- vid
					subfldTags = "[3]";
				}

				// itero sui tag
				for (int tag = startTag; tag <= endTag; tag++) {
					List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
					if (!marcFieldList.isEmpty()) {
						Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
						for (VariableField vf : marcFieldList) {
							DataField marcField = (DataField) vf;
							boolean good = false;
							StringBuilder buffer = new StringBuilder("");
							List<Subfield> subfields = marcField.getSubfields();
							for (Subfield subfield : subfields) {
								String subfieldString = subfield.toString();
								if (good && buffer.length() > 0 && subfieldString.startsWith("$17") ){
									result.add(buffer.toString());
									buffer = new StringBuilder("");
									// buffer.delete(0, buffer.length());
								}
								if (subfieldString.startsWith("$17") ) good = true;
								if (subfieldString.startsWith("$1200")) good = false;
								if (tag == 463 && (subfieldString.startsWith("$1702") || subfieldString.startsWith("$1712"))) good = false;
								if (good) {
									/* if (new_field && buffer.length() > 0) {
									    result.add(buffer.toString());
									    // buffer.delete(0, buffer.length());
									    buffer = new StringBuilder("");
								} */
									Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
									if (matcher.matches()) {
										if (buffer.length() > 0 && !subfield.getData().startsWith(",")) buffer.append(separator != null ? separator : "");
										if (subfieldString.startsWith("$171"))
											buffer.append(cleanPretitolo(subfield.getData()));
										else
											buffer.append(subfield.getData());	
									}
								}
							}
							if (buffer.length() > 0) result.add(buffer.toString()); 
						}
					}
				}



			}

			/**
			 * quinta  parte: 500 $9  
			 */
			subField = '9';

			if (nomeVid) {  //  vid per il momento non presente : quindi inserito else e subField = '0'
				subField = '0';
			}

			else if ($4 == null) { // solo se $4 == null, sono in nome e non in nome_rel_$4
				List<VariableField> marcField500List = record.getVariableFields("500");
				for (VariableField vf : marcField500List) {
					DataField marcField500 = (DataField) vf;
					List<Subfield> subfield500cList = marcField500.getSubfields(subField);
					for (Subfield subfield : subfield500cList) {
						if (subfield.getData() != null && !"".equals(subfield.getData())) {
							result.add(subfield.getData());
						}
					}
				}
			}
		}
		result = getFold(result); 

		return result;
	}

	/**
	 * 600-675 a,  - x ,b, c, d
	 * @param record
	 * @return
	 */
	public Set<String> getSoggettoCBL(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		String separator = " ";

		/**
		 *  600-675 a,  - x ,b, c, d (separati da spazio) e non 620
		 */
		int startTag = 600;
		int endTag = 617;
		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			if (tag == 609 || (tag >= 611 && tag <= 614))
				continue;
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;
					StringBuilder buffer = new StringBuilder("");
					List<Subfield> subfields = marcField.getSubfields();
					for (Subfield subfield : subfields) {
						if ('3' != subfield.getCode() && subfield.getCode() != '2') {

							if (buffer.length() > 0)
								buffer.append(separator != null ? separator : " ");

							String data = subfield.getData();

							// se il subfield è x metto - 
							if ('x' == subfield.getCode()) {
								data = "- ".concat(data);
							}


							// a b c d entrano come arrivano...
							buffer.append(data);
						}
					}
					if (buffer.length() > 0)
						result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
				}

			}
		}

		return result;
	}


	/**
	 * 600-675 a,  - x ,b, c, d
	 * @param record
	 * @return
	 */
	public Set<String> getSoggetto(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		String separator = " ";

		/**
		 *  600-675 a,  - x ,b, c, d (separati da spazio) e non 620
		 */
		int startTag = 600;
		int endTag = 675;
		String subfldTags = "[a|x|b|c|d|2]"; /* "[a|x|b|c|d|2] non  necessario ticket */

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			if (tag == 620)
				continue;
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;
					StringBuilder buffer = new StringBuilder("");
					List<Subfield> subfields = marcField.getSubfields();
					for (Subfield subfield : subfields) {
						Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
						if (matcher.matches()) {

							if (buffer.length() > 0)
								buffer.append(separator != null ? separator : " ");

							String data = subfield.getData();

							// se il subfield è x metto - 
							if ('x' == subfield.getCode()) {
								data = "- ".concat(data);
							}

							if ('2' == subfield.getCode()) {
								data = ". ".concat(data);
							}

							// a b c d entrano come arrivano...
							buffer.append(data);
						}
					}
					if (buffer.length() > 0)
						result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
				}

			}
		}

		return result;
	}


	/**
	 * 600-675 a,  - x ,b, c, d
	 * @param record
	 * @return
	 */
	public Set<String> getSoggettoF(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		String separator = " ";

		/**
		 *  600-675 a,  - x ,b, c, d (separati da spazio) e non 620
		 */
		int startTag = 600;
		int endTag = 675;
		String subfldTags = "[a|x|b|c|d]"; /* "[a|x|b|c|d] non  necessario ticket */

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			if (tag == 620)
				continue;
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;
					StringBuilder buffer = new StringBuilder("");
					List<Subfield> subfields = marcField.getSubfields();
					for (Subfield subfield : subfields) {
						Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
						if (matcher.matches()) {

							if (buffer.length() > 0)
								buffer.append(separator != null ? separator : " ");

							String data = subfield.getData();

							// se il subfield è x metto - 
							if ('x' == subfield.getCode()) {
								data = "- ".concat(data);
							}

							// a b c d entrano come arrivano...
							buffer.append(data);
						}
					}
					if (buffer.length() > 0)
						result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
				}

			}
		}

		return result;
	}

	/**
	 * 200 $a,$e
	 * 454 $a,$e (454 è essenziale)
	 * 461 $a,$e
	 * 462 $a,$e (per sicurezza; anche se non so immaginare la situazione)
	 * @param record
	 * @return
	 */
	public Set<String> getTitoloNav(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		String separator = " ";

		String subfldTags = "[a|e]"; 

		List<VariableField> marcFieldList = record.getVariableFields("200");
		if (!marcFieldList.isEmpty()) {
			Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
			for (VariableField vf : marcFieldList) {
				DataField marcField = (DataField) vf;
				StringBuilder buffer = new StringBuilder("");
				List<Subfield> subfields = marcField.getSubfields();
				boolean initial_a = true;
				for (Subfield subfield : subfields) {
					Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());

					if (matcher.matches()) {

						if (buffer.length() > 0)
							buffer.append(separator != null ? separator : " ");

						String data = subfield.getData();

						// se il subfield è e metto : 
						if ('e' == subfield.getCode()) {
							data = ":".concat(data);
						}
						// successive a metto ;
						else if (!initial_a){
							data = ";". concat(data);
						}
						else {
							data = cleanPretitolo(data);
							initial_a = false;
						}

						buffer.append(data);
					}
				}
				if (buffer.length() > 0) {
					String cleanedBuff = buffer.toString().replaceAll("\\s*\\.?\\s*-?\\s*$", "");
					result.add(String.valueOf(foldToASCII(cleanedBuff.toCharArray(), cleanedBuff.length())).trim());
				}
			}

		}	   
		result.addAll(getTitNav_$(record,"454"));
		result.addAll(getTitNav_$(record,"461"));
		result.addAll(getTitNav_$(record,"462"));
		return result;
	}


	/**
	 * 
	 * @param record
	 * @param lowerBoundStr
	 * @param upperBoundStr
	 * @param subField
	 * @return
	 */
	public Set<String> getAllSearchableFields(final Record record, String lowerBoundStr, String upperBoundStr, String subField, String fold) {

		Set<String> result = new LinkedHashSet<String>();

		int lowerBound = localParseInt(lowerBoundStr, 100);
		int upperBound = localParseInt(upperBoundStr, 900);

		List<DataField> fields = record.getDataFields();
		for (DataField field : fields) {
			int tag = localParseInt(field.getTag(), -1);
			if ((tag >= lowerBound) && (tag < upperBound)) {
				Pattern subfieldPattern = Pattern.compile(subField.length() == 0 ? "." : subField);
				List<Subfield> subfields = field.getSubfields();
				for (Subfield subfield : subfields) {
					Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
					if (matcher.matches()) {
						String data = subfield.getData();
						result.add(data);
					}
				}    			
			}
		}

		result = cleanPretitolo(result,fold); // clean del pretitolo

		return result;
	}

	public Set<String> getNumeriTipY(final Record record) {
		return Numeri_tip.getResult(record, "071", "30", null, null, "a");
	}
	/**
	 * solo per i numeri con TIP E,L,O,X,Y,V,W e A,F
	 */
	public Set<String> getNumeri_tip_$(final Record record, String tip) {
		if ("A".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "071", "0", null, null, "az");
		}
		if ("F".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "071", "1", null, null, "az");
		}
		if ("H".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "071", "4", null, null, "az");
		}
		// da capire che numero è E ? quale indicatore 3 o 4 ?
		if ("E".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "071", "3", null, null, "az");
		}
		if ("L".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "071", "2", null, null, "az");
		}
		if ("O1".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "071", "6", null, null, "az");
		}
		if ("O".equalsIgnoreCase(tip)) {
			Set<String> result = getFieldList(record, "929b");
			result.addAll(getOperaNum(result));
			result.addAll(Numeri_tip.getResult(record, "017", "01", null, null, "a"));
			return result;
		}
		if ("X".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "017", "70", "2", "X", "a");
		}
		if ("Y".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "017", "70", "2", "Y", "a");
		}
		if ("U".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "017", "70", "2", "U", "a");
		}
		if ("P1".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "017", "70", "2", "P", "a");
		}
		// SIAE non Segnatura
		if ("W1".equalsIgnoreCase(tip)) {
			return Numeri_tip.getResult(record, "017", "70", "2", "W", "a");
		}

		//V:   929 c    17 ind 70 a $2 = V
		if ("V".equalsIgnoreCase(tip)) {
			Set<String> result = new LinkedHashSet<String>();

			Set<String> tag929c = getFieldList(record, "929c");
			result.addAll(tag929c);

			List<VariableField> field = (List<VariableField>) record.getVariableFields("017");
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				DataField dataField = (DataField) iterator.next();
				char ind1 = dataField.getIndicator1();
				char ind2 = dataField.getIndicator2();
				if (ind1 == '7' || ind2 == '0') {

					// cerco il $2
					boolean found = false;
					List<Subfield> subflds = dataField.getSubfields('2');
					for (Iterator<Subfield> iteratorSubflds = subflds.iterator(); iteratorSubflds.hasNext();) {
						Subfield subfield = (Subfield) iteratorSubflds.next();
						if (tip.equalsIgnoreCase(subfield.getData())) {
							found = true;
							break;
						}
					}
					// prendo il $a e lo aggiungo ai risultati
					if (found) {
						subflds = dataField.getSubfields('a');
						for (Iterator<Subfield> iteratorSubflds = subflds.iterator(); iteratorSubflds.hasNext();) {
							Subfield subfield = (Subfield) iteratorSubflds.next();
							result.add(subfield.getData());
						}
					}
				}
			}
			return result;
		}

		//W:            Caricare il valore per la ricerca dal tag 899 subfield $1 , $3 e $c separati da spazio. 
		if ("W".equalsIgnoreCase(tip)) {
			Set<String> result = new LinkedHashSet<String>();

			List<VariableField> field = (List<VariableField>) record.getVariableFields("899");
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				DataField dataField = (DataField) iterator.next();
				Subfield subfld$1 = dataField.getSubfield('1');
				Subfield subfld$3 = dataField.getSubfield('3');
				Subfield subfld$c = dataField.getSubfield('c');
				StringBuilder buffer = new StringBuilder();
				if (((subfld$3 == null || subfld$3.getData() == null ||  "".equalsIgnoreCase(subfld$3.getData().trim()))) &&
						(subfld$c == null || subfld$c.getData() == null ||  "".equalsIgnoreCase(subfld$c.getData().trim()))	) 
					continue;

				if (subfld$1 != null && subfld$1.getData() != null &&  !"".equalsIgnoreCase(subfld$1.getData().trim())) {
					buffer.append(subfld$1.getData());
					buffer.append(" ");
				}
				if (subfld$3 != null && subfld$3.getData() != null &&  !"".equalsIgnoreCase(subfld$3.getData().trim())) {
					buffer.append(subfld$3.getData());
					buffer.append(" ");
				}
				if (subfld$c != null && subfld$c.getData() != null &&  !"".equalsIgnoreCase(subfld$c.getData().trim())) {
					buffer.append(subfld$c.getData());
				}
				if (buffer.length() > 0) {
					result.add(buffer.toString().trim());	
				}
			}
			return result;
		}
		return null;
	}

	public Set<String> getFieldRange(final Record record, String tagStr, String from, String to, String length) {
		Set<String> result = new LinkedHashSet<String>();

		int start = localParseInt(from, 1);
		int end = localParseInt(to, 1);
		int numchars = localParseInt(length, 1);
		for (int i=start; i<=end ;i=i+numchars) {
			int fin = i+numchars;
			result.addAll(getFieldList(record, tagStr +"[" + i + "-" + fin + "]"));
		}
		return result;
	}
	public Set<String> getPresentazione(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> marcFieldList = record.getVariableFields("125");
		for (VariableField vf : marcFieldList) {

			DataField marcField = (DataField) vf;
			List<Subfield> subfields =  marcField.getSubfields();
			boolean addC = false;
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();
				if (code == 'a'  && (!"m".equalsIgnoreCase(subfield.getData().toCharArray()[0] + "")))
					result.add(subfield.getData());
				else if (code == 'a')
					addC = true;
				else if (code == 'c' && addC) {
					result.addAll(getFieldRange(record, "125", "0", subfield.getData().length() + "", "1"));
				}

			}
		}

		return result;
	}


	/**
	 * organico_tip_3 e organico_tip_4
	 */
	public Set<String> getOrganico_tip(final Record record, String tagStr) {
		Set<String> result = new LinkedHashSet<String>();
		Set<String> field = getFieldList(record, tagStr);
		for (Iterator<String> iterator = field.iterator(); iterator != null  && iterator.hasNext();) {
			String data = iterator.next();
			if (data.contains("(")) {
				String[] elements = data.split("\\(");
				Boolean first = true;
				String before = "";

				for (int i=0; i<elements.length; i++) {
					String[] subels = elements[i].split("\\)",2);
					if (subels.length > 1)
						elements [i] = subels[1].replaceAll("\\$", "");
					if ( elements[i].replaceAll(" ", "").length() > 0) {
						if (elements[i].startsWith(","))
							elements[i] = elements[i].substring(1).replaceAll(" ", "");

					}	
					String[] words = elements[i].split("[,/%]%*");
					if (first) {
						first = false;
						before = subels[0].replaceAll(" ", "");
					}
					else {
						result.add(before.concat("(").concat(subels[0].concat(")")));
					}
					if (words != null && words.length > 0) {
						before = words[words.length -1];
						if (before != null && before.length() > 0)
							before = before.replaceAll(" ", "");	
						for	 (int k=0; k< words.length -1; k++) {
							String tmp_wd = words[k]; 
							if (tmp_wd != null && tmp_wd.length() > 0)
								result.add(tmp_wd .replaceAll(" ", ""));
						}
					}
				}
				if (before.length()>0)
					result.add(before);
			}
			else {
				String[] elements = data.replaceAll("\\$", "").split("[,/%]%*");
				for (int i=0; i<elements.length; i++) {
					if (elements[i] != null && elements[i].replaceAll(" ", "").length() > 0)
						result.add(elements[i].replaceAll(" ", ""));
				}
			}
		}
		return result;
	}

	/**
	 * 
	 */
	public Set<String> getRappres_tip(final Record record, String n) {
		if ("2".equalsIgnoreCase(n)) {
			// 922 p,q,r,s,t,u con $a = 2
			return Rappres_tip.getResult(record, "922", "[p|q|r|s|t|u]", "a", "2");
		}
		if ("4".equalsIgnoreCase(n)) {
			// 922 p,q,r,s,t,u con $a = 4
			return Rappres_tip.getResult(record, "922",  "[p|q|r|s|t|u]", "a", "4");
		}
		if ("5".equalsIgnoreCase(n)) {
			// 922 p,q,r,s,t,u con $a = 5
			return Rappres_tip.getResult(record, "922",  "[p|q|r|s|t|u]", "a", "5");
		}
		return null;
	}


	/**
	 * 921 b c (separati da – e racchisi da parentesi)
	 * @param record
	 * @return
	 */
	public  Set<String> getMarca(final Record record) {

		/* 
		 * 921 $a identificativo della marca (da mascherare)
		 * 921 $b descrizione della marca
		 * 921 $e motto (preceduto da "punto spazio)
		 * 921 $d collocazione della marca nella pubblicazione (es. "sul front."), preceduto da "virgola spazio"
		 * 921 $c (ripetibile) citazione (codici eventualmente separati da "virgola spazio", e racchiusi tra parentesi tonde) 
		 */

		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> marcFieldList = record.getVariableFields("921");
		for (VariableField vf : marcFieldList) {

			DataField marcField = (DataField) vf;
			StringBuilder buffer = new StringBuilder("");

			boolean parentesiC = false;
			boolean firstC = true;
			int idxLastC = 0;

			List<Subfield> subfields =  marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();


				switch (code) {
				case 'b':
					buffer.append(subfield.getData());
					break;
				case 'e':
					buffer.append(". ").append(subfield.getData());
					break;
				case 'c':
					if (firstC) {
						buffer.append(" (").append(subfield.getData());
						firstC = false;
						parentesiC = true;
						idxLastC = buffer.length();
					} else {
						buffer.append(", ").append(subfield.getData());
						idxLastC = buffer.length();
					}
					break;


				default:
					break;
				}	
			}

			// inserisco la parentesi dopo l'ultima b inserita
			if (parentesiC) {
				buffer.insert(idxLastC, ")");
			}
			result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
		}

		return result;
	}

	/*
	guida posizione 6 (partendo da 0) 
		se vale e oppure f va messo a C
		se vale i oppure j oppure k va messo a S
		altrimenti si legge l'id del record e 
			se a pos. 11 c'e' E allora si mette A
			se contiene MUS oppure MSM si mette M
		in tutti gli altri casi si mette I

	 */
	public String getBaseProv(final Record record) {
		String result = new String();
		String leader = record.getLeader().toString();
		char baseProv = leader.charAt(6);

		String id = ((ControlField) record.getVariableField("001")).getData();
		if (baseProv == 'e' || baseProv == 'f') {
			result = "C";
		} else if (baseProv == 'i' || baseProv == 'j' || baseProv == 'k') {
			result = "S";
		} else if (id != null && id.length() > 11 && id.charAt(11) == 'E') {
			result = "A";
		} else if (id != null && id.indexOf("MUS") != -1 || id.indexOf("MSM") != -1) {
			result = "M";
		} else {
			result = "I";
		}

		return result;
	}

	public String getLevel(final Record record) {
		String result = new String();
		String leader = record.getLeader().toString();
		char level = leader.charAt(7);
		if (level == 's') {
			Set<String> field = getFieldList(record, "110a");
			
			if (field != null && field.size() > 0  && ((String)field.toArray()[0]).substring(0,1).equalsIgnoreCase("b")) 
				level = 'c';
		} 
		result = level + "";
		return result;
	}

	public Set<String> getTipoMatIccu(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		String leader = record.getLeader().toString();
		char baseProv = leader.charAt(6);
		String id = ((ControlField) record.getVariableField("001")).getData();
		Set<String> vals92x = getFieldList(record, "928a:928b:928c:928d:929a:929e:929c");

		boolean preslibr = false;
		Set<String> vals140 = getFieldList(record, "140a");
		if (vals140 != null) {
		for (Iterator<String> iterator = vals140.iterator(); iterator.hasNext();) {
			String data= (String)iterator.next();
			// logger.warn("Ciao a:x" + data.substring(17,19));
			if ((data != null)  && (data.substring(17,19)).equals("da")) {
				preslibr = true;
				break;
			}
		}
		}
		if (!preslibr) {
			Set<String> vals105 = getFieldList(record, "105a");
			if (vals105 != null) {
			for (Iterator<String> iterator = vals105.iterator(); iterator.hasNext();) {
				String data= (String)iterator.next();
				// logger.warn("Ciao m:" + data.substring(11));
				if (data != null && data.substring(11,12).equals("i")) {
					preslibr = true;
					break;
				}
			}
		  }
		}
		//logger.warn("Ciao pres :" + preslibr);

		 if (baseProv == 'c' || baseProv == 'd' || ((baseProv == 'b' || baseProv == 'a') && preslibr) || baseProv == 'j' || id.indexOf("MUS") != -1 ||
			id.indexOf("MSM") != -1  || (baseProv == 'g' && vals92x.size() > 0) )
	        result.add("M");
	    if (id.length() > 12 && id.charAt(11) == 'E')
		result.add("A");
	    else if (id.indexOf("MUS") == -1 && id.indexOf("MSM") == -1)
		result.add("N");

		return result;
	}


	public Set<String> getTipoMat(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		String leader = record.getLeader().toString();
		char baseProv = leader.charAt(6);
		String id = ((ControlField) record.getVariableField("001")).getData();
		Set<String> vals92x = getFieldList(record, "928a:928b:928c:928d:929a:929e:929c");
		Set<String> vals125 = getFieldList(record, "125b");
		boolean pres125 = false;
		for (Iterator<String> iterator = vals125.iterator(); iterator.hasNext();) {
			String data= (String)iterator.next();
			if (data.contains("b")) {
				pres125 = true;
				break;
			}
		}

		if (baseProv == 'c' || baseProv == 'd' || ((baseProv == 'b' || baseProv == 'a') && pres125) || baseProv == 'j' || id.indexOf("MUS") != -1 || 
				id.indexOf("MSM") != -1  || (baseProv == 'g' && vals92x.size() > 0) ) 
			result.add("M");
		if (id.length() > 12 && id.charAt(11) == 'E')
			result.add("A");
		else if (id.indexOf("MUS") == -1 && id.indexOf("MSM") == -1)
			result.add("N");

		return result;
	}

	public String getFormatoElettronico(final Record record, String tagStr) {
		Set<String> result = getFieldList(record, tagStr);
		String val = "Y";
		if (result.size() > 0)
			return val;
		else {
			Set<String> result2 = getFieldList(record, "899e");
			if (result2.contains("S"))
				return val;
		}
		return "";
	}

	public String getPresenza(final Record record, String tagStr) {
		Set<String> result = getFieldList(record, tagStr);
		String val = "Y";
		if (result.size() > 0)
			return val;
		return "";
	}

	public String getAntico(final Record record, String tagStr) {
		Set<String> result = getFieldList(record, tagStr);
		String val = "Y";
		if (result.size() > 0)
			return val;
		String bid = ((ControlField) record.getVariableField("001")).getData();
		if (bid.substring(3,4).equalsIgnoreCase("E"))
			return val;
		result = getFieldList(record, "100a[9-12]:100a[13:16]");
		Iterator<String> iterator = result.iterator(); 
		String datada = "";
		String dataa = "";
		if (iterator.hasNext()) 
			datada = ((String) iterator.next()).replaceAll(" ", "");
		if (iterator.hasNext()) 
			dataa = ((String) iterator.next()).replaceAll(" ", "");
		if (dataa.length() > 0 && validDate(dataa) && Integer.parseInt(dataa) < 1830)
			return val;
		if (datada.length() > 0 && validDate(datada) && Integer.parseInt(datada) < 1830)
			return val;
		return "";
	}

	public Set<String> getGraficaDisegno(final Record record, String tagStr) {
		Set<String> result = new LinkedHashSet<String>();
		Set<String> field4 = getFieldList(record, tagStr + "[4-5]");
		Set<String> field10 = getFieldList(record, tagStr + "[10-11]");
		Iterator<String> iterator2 = field10.iterator();
		for (Iterator<String> iterator = field4.iterator() ; iterator.hasNext();) {
			String data = (String)iterator.next();
			String data2 = "";
			if (iterator2.hasNext())
				data2 = (String)iterator2.next();
			if ("xx".equalsIgnoreCase(data.trim()) && !"xx".equalsIgnoreCase(data2.trim()))
				result.add("D");
			if ("xx".equalsIgnoreCase(data2.trim()) && !"xx".equalsIgnoreCase(data.trim()))
				result.add("S");
		}
		return result;

	}
	public String getCartTradFoto(final Record record) {

		Set<String> field121 = getFieldList(record, "121a");
		Set<String> field120 = getFieldList(record, "120a");
		if (field121.size() > 0)
			return "F";
		else if (field120.size() > 0)
			return "C";
		return "";

	}
	public Set<String> getFormatoDist(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		Set<String> field115 = getFieldList(record, "115a");
		for (Iterator<String> iterator = field115.iterator() ; iterator.hasNext();) {
		   String value = (String)iterator.next();
		   result.add(value.substring(0,1) + value.substring(6,7) + value.substring(8,9) + value.substring(15,17));
		}
		return result;

	}

	public Set<String> getTipoScala(final Record record, String tagStr) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> fieldList =  record.getVariableFields(tagStr);
		for (VariableField vf : fieldList) {
			DataField data = (DataField) vf;
			result.add(data.getIndicator1() + "");
		}
		return result;



	}
	/**
	 * 
	 * 210c , 700-750 a,b,c,d,f,g (separati da spazio) con $4 650 o 750
	 * @deprecated Configurato solo il 210c nel file di configurazione
	 */
	public Set<String> getEditore(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		String separator = "";

		/**
		 *  prima parte: 210c se valorizzato
		 */
		List<VariableField> marcField210List = record.getVariableFields("210");
		for (VariableField vf : marcField210List) {
			DataField marcField210 = (DataField) vf;
			List<Subfield> subfield210cList = marcField210.getSubfields('c');
			for (Subfield subfield : subfield210cList) {
				if (subfield.getData() != null && !"".equals(subfield.getData())) {
					result.add(subfield.getData());
				}
			}
		}

		/**
		 *  seconda parte: 700-750 a,b,c,d,e,f,g (separati da spazio) con $4 650 o 750
		 */
		int startTag = 700;
		int endTag = 750;
		String subfldTags = "[a|b|c|d|e|f|g]";
		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;
					Subfield checkSubfield = marcField.getSubfield('4');
					if (checkSubfield != null && ("650".equalsIgnoreCase(checkSubfield.getData()) || "750".equalsIgnoreCase(checkSubfield.getData()))) {
						StringBuilder buffer = new StringBuilder("");
						List<Subfield> subfields = marcField.getSubfields();
						for (Subfield subfield : subfields) {
							Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
							if (matcher.matches()) {
								if (buffer.length() > 0)
									buffer.append(separator != null ? separator : "");
								buffer.append(subfield.getData());
							}
						}
						if (buffer.length() > 0)
							result.add(buffer.toString());
					}
				}
			}
		}

		return result;
	}

	/** 
	 * tag 410 $1 -> se contenuto inizia per 
	 * 		200 -> prendo $a, $e e $f
	 * 		0 200 -> prendo $a, $e e $f
	 * 
	 */
	public Set<String> getCollezione(final Record record) {
	  return getCollezionePre(record, true);
	}
	public Set<String> getCollezione2(final Record record) {
	  return getCollezionePre(record, false);
	}
	public Set<String> getCollezionePre(final Record record, boolean after_pre) {
		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> field = (List<VariableField>) record.getVariableFields("410");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();

			List<Subfield> subflds = dataField.getSubfields('1');
			for (Iterator<Subfield> iteratorSubflds = subflds.iterator(); iteratorSubflds.hasNext();) {
				Subfield subfield = (Subfield) iteratorSubflds.next();

				// se inizia per 200 o per 0 200 prendo tutti i $a
				if (subfield.getData().startsWith("200") || subfield.getData().startsWith("0 200")) {

					List<Subfield> subflds$a = dataField.getSubfields('a');
					for (Iterator<Subfield> iterator2 = subflds$a.iterator(); iterator2.hasNext();) {
						Subfield temp = (Subfield) iterator2.next();
						String data = temp.getData();
						if (!after_pre) {
						  data = CleanNoSort.clean(data);
                          data = String.valueOf(foldToASCII(data.toCharArray(), data.length())).trim();
						}
						result.add(data);
					}

					List<Subfield> subflds$e = dataField.getSubfields('e');
					for (Iterator<Subfield> iterator2 = subflds$e.iterator(); iterator2.hasNext();) {
						Subfield temp = (Subfield) iterator2.next();
						result.add(temp.getData());	
					}

					List<Subfield> subflds$f = dataField.getSubfields('f');
					for (Iterator<Subfield> iterator2 = subflds$f.iterator(); iterator2.hasNext();) {
						Subfield temp = (Subfield) iterator2.next();
						result.add(temp.getData());	
					}
				}
			}
		}
		if (after_pre)
		    result = cleanPretitolo(result,"1"); // clean del pretitolo

		return result;
	}


	public Set<String> getCollBid(final Record record) {
		Set<String> result = new LinkedHashSet<String>();


		int startTag = 461;
		int endTag = 463;


		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {

			List<VariableField> field = (List<VariableField>) record.getVariableFields(String.valueOf(tag));
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				DataField dataField = (DataField) iterator.next();
				Subfield subfield = dataField.getSubfield('1');
				String data = subfield.getData();
				if (data.startsWith("001")) {
					String bid = data.substring(3);
					result.add(bid);
				}
			}
		}

		return result;
	}

	public Set<String> getCollBidLevel(final Record record,String tag, String level) {
		Set<String> result = new LinkedHashSet<String>();
		String leader = record.getLeader().toString();
		char levelrec = leader.charAt(7);
		if (levelrec == level.charAt(0)) {
			if (tag.contains(":")) {
				String[] tags = tag.split(":");
				for (int i=0;i< tags.length;i++)
					result.addAll(getCollBid_$(record, tags[i]));
			}
			else
				return getCollBid_$(record,tag);
		}

		return result;
	}
	public Set<String> getCollBid_$(final Record record, String tag) {
		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> field = (List<VariableField>) record.getVariableFields(String.valueOf(tag));
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();
			Subfield subfield = dataField.getSubfield('1');
			if (subfield == null)
				continue;
			String data = subfield.getData();
			if (data.startsWith("001")) {
				String bid = data.substring(3);
				result.add(bid);
			}
		}

		return result;
	}
	/*
	 * 411-499 1200 a=COLLTIT
	 */
	public Set<String> getCollTit(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		int startTag = 411;
		int endTag = 499;
		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			result.addAll(getCollTit_$(record, String.valueOf(tag)));
		}

		result = cleanPretitolo(result,"1"); // clean del pretitolo

		return result;
	}

	public Set<String> getCollTit_$(final Record record, String tag) {

		return getCollTit_$(record, tag, null,true);

	}
	public String getCollOrd(final Record record) {

		String result = getCollOrd_$(record, "461");
		if (result == null || result.length() == 0)
		  result = getCollOrd_$(record, "462");
		return result;
	}
	public String getCollOrd_$(final Record record, String tag) {
		String result =null;
		List<VariableField> marcFieldList = record.getVariableFields(tag);
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			boolean good = false;


				List<Subfield> subfields =  marcField.getSubfields();
				StringBuilder buffer = new StringBuilder("");
				for (Subfield subfield : subfields) {
					char code = subfield.getCode();
					String subfieldString = subfield.toString();
					if (subfieldString.startsWith("$1200"))
						good = true;
					if (good) {
						switch (code) {
						case 'v':
							buffer.append(subfield.getData());
							break;
						default:
							break;
						}
					}
				}
				if (buffer.length() > 0)
				   result = buffer.toString().trim();
			}
	//	logger.warn("Record" +   record.getVariableField("001").toString() + result);

		try {
		    if (result != null) {
			 if (result.indexOf("v.") > 0)
				   result = result.replaceAll("v.", "");
			 if (result.indexOf("-") > 0)
				   result = result.replaceAll("-", ".");
			 if (result.indexOf("(") > 0)
					result = result.substring(0, result.indexOf("("));
			 if (result.indexOf("/") > 0)
					result = result.replaceAll("/", ".");
		    }
		} catch (NumberFormatException nfe) {
			logger.warn("Record" +   record.getVariableField("001").toString() + " non ordinabile nella madre : $v = " + result);
			result = null;
		}

		return result;
	}
	public Set<String> getCollTit_$(final Record record, String tag, String ind1, Boolean clean_pretit) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> marcFieldList = record.getVariableFields(tag);
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;

			boolean firstA = true;

			boolean parentesiB = false;
			int idxLastB = 0;
			boolean firstB = true;
			boolean good = false;


			List<Subfield> subfields =  marcField.getSubfields();
			StringBuilder buffer = new StringBuilder("");
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();
				String subfieldString = subfield.toString();
				if (ind1 == null && subfieldString.startsWith("$1200")) 
					good = true;
				else if (ind1 != null && subfieldString.startsWith("$1200"+ ind1))
					good = true;
				if (subfieldString.startsWith("$17")) good = false;

				if (good) {
					switch (code) {

					case 'a':
						if (firstA) {
							if (clean_pretit)
								buffer.append(cleanPretitolo(subfield.getData()));
							else
								buffer.append(subfield.getData().replaceAll("\u001bH", "<<").replaceAll("\u001bI", ">>"));
							firstA = false;
						} else {
							if (clean_pretit)
								buffer.append(" ; ").append(cleanPretitolo(subfield.getData()));
							else
								buffer.append(" ; ").append(subfield.getData().replaceAll("\u001bH", "<<").replaceAll("\u001bI", ">>"));
						}
						break;

					case 'b':
						if (firstB) {
							buffer.append(" [").append(subfield.getData());
							firstB = false;
							parentesiB = true;
						} else {
							buffer.append(" ").append(subfield.getData());
							idxLastB = buffer.length();
						}
						break;

					case 'c':
						buffer.append(" . ").append(subfield.getData());
						break;

					case 'd':
						buffer.append(" = ").append(subfield.getData());
						break;

					case 'e':
						buffer.append(" : ").append(subfield.getData());
						break;

					case 'f':
						buffer.append(" / ").append(subfield.getData());
						break;

					case 'g':
						buffer.append(" ; ").append(subfield.getData());
						break;

					default:
						break;
					}	
				}

				// inserisco la parentesi dopo l'ultima b inserita
				if (parentesiB) {
					buffer.insert(idxLastB, "] ");
				}
			}
			if (buffer.length() > 0) result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
		}
		//		if (clean_pretit)
		//		   result = cleanPretitolo(result); // clean del pretitolo : già fatto per ogni $a
		return result;
	}

	public Set<String> getTitNav_$(final Record record, String tag) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> marcFieldList = record.getVariableFields(tag);
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;

			boolean firstA = true;

			boolean good = false;


			List<Subfield> subfields =  marcField.getSubfields();
			StringBuilder buffer = new StringBuilder("");
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();
				String subfieldString = subfield.toString();
				if ( subfieldString.startsWith("$1200")) 
					good = true;
				if (subfieldString.startsWith("$17")) good = false;

				if (good) {
					switch (code) {

					case 'a':
						if (firstA) {

							buffer.append(cleanPretitolo(subfield.getData()));
							firstA = false;
						} else {
							buffer.append(" ; ").append(cleanPretitolo(subfield.getData()));
						}
						break;


					case 'e':
						buffer.append(" : ").append(subfield.getData());
						break;


					default:
						break;
					}	
				}

			}
			if (buffer.length() > 0) {
				String cleanedBuff = buffer.toString().replaceAll("\\s*\\.?\\s*-?\\s*$", "");
				result.add(String.valueOf(foldToASCII(cleanedBuff.toCharArray(), cleanedBuff.length())).trim());
			}
		}
		return result;
	}
	/*
	 	altritit_tip_A = 517$a quando $e = "APP"
		altritit_tip_I = 517$a quando $e = "INC"
		altritit_tip_L = 517$a quando $e = "ALT" 
		altritit_tip_D = 517$a quando $e = "OLE"
		altritit_tip_O = 517$a quando $e = "OPA"
		altritit_tip_8 = 517$a quando $e = "P"
		altritit_tip_V = 517$a quando $e = ""
	 */
	public Set<String> getAltritit_Tip_$(final Record record, String $tip) {
		Set<String> result = new LinkedHashSet<String>();

		DataField dataField = (DataField) record.getVariableField("517");
		if (dataField == null) {
			return null;
		}

		Subfield subfield = dataField.getSubfield('e');
		if (subfield == null) {
			return null;
		}

		String data = subfield.getData();

		Subfield value = dataField.getSubfield('a');

		if ("APP".equalsIgnoreCase(data) && "A".equalsIgnoreCase($tip)) {
			result.add(value.getData());
		}
		if ("INC".equalsIgnoreCase(data) && "I".equalsIgnoreCase($tip)) {
			result.add(value.getData());
		}
		if ("ALT".equalsIgnoreCase(data) && "L".equalsIgnoreCase($tip)) {
			result.add(value.getData());
		}
		if ("OLE".equalsIgnoreCase(data) && "D".equalsIgnoreCase($tip)) {
			result.add(value.getData());
		}
		if ("OPA".equalsIgnoreCase(data) && "O".equalsIgnoreCase($tip)) {
			result.add(value.getData());
		}
		if ("P".equalsIgnoreCase(data) && "8".equalsIgnoreCase($tip)) {
			result.add(value.getData());
		}
		if ("".equalsIgnoreCase(data) && "V".equalsIgnoreCase($tip)) {
			result.add(value.getData());
		}

		result = cleanPretitolo(result,"0"); // clean del pretitolo

		return result;
	}

	/*
	 * 463 a con header pos 7 = a
	 */
	public Set<String> getHosti(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		String leader =record.getLeader().toString();
		char test = leader.charAt(7);
		if (test == 'a') {
			List<VariableField> marcFieldList = record.getVariableFields("463");
			for (Iterator<VariableField> iterator = marcFieldList.iterator(); iterator.hasNext();) {
				DataField dataField = (DataField) iterator.next();
				List<Subfield> subfields = dataField.getSubfields('a');
				for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
					Subfield subfield = (Subfield) iterator2.next();
					result.add(subfield.getData());
				}
			}
			marcFieldList = record.getVariableFields("461");
			for (Iterator<VariableField> iterator = marcFieldList.iterator(); iterator.hasNext();) {
				DataField dataField = (DataField) iterator.next();
				List<Subfield> subfields = dataField.getSubfields('a');
				for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
					Subfield subfield = (Subfield) iterator2.next();
					result.add(subfield.getData());
				}
			}
		}
		return result;
	}

	/*
	 * 215 a a a : c : c : c ; d ; d ; d ; d + e + e + e + e
	 */
	public Set<String> getDescr(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> marcFieldList = record.getVariableFields("215");
		for (Iterator<VariableField> iterator = marcFieldList.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();

			StringBuilder buffer = new StringBuilder();

			List<Subfield> subfields =  dataField.getSubfields();
			for (Subfield subfield : subfields) {

				char code = subfield.getCode();
				switch (code) {
				case 'a':
					if (buffer.length() > 0) buffer.append(" ");
					buffer.append(subfield.getData());
					break;
				case 'c':
					if (buffer.length() > 0) buffer.append(" : ");
					buffer.append(subfield.getData());
					break;
				case 'd':
					if (buffer.length() > 0) buffer.append(" ; ");
					buffer.append(subfield.getData());
					break;	
				case 'e':
					if (buffer.length() > 0) buffer.append(" + ");
					buffer.append(subfield.getData());
					break;
				default:
					break;
				}

			}

			result.add(buffer.toString());
		}

		return result;
	}


	/*
	 * header pos= 7  se pos 9 = '  ' altrimenti pos 9 (se natura pos 7 = 'm' e 200 pos 0 = '0' allora natura  = 'w')
	 */
	public String getNatura(final Record record) {
		String natura = null;

		String leader = record.getLeader().toString();
		if (leader.charAt(9) != ' ') {
			natura = String.valueOf(leader.charAt(9));
		} else {
			DataField dataField = (DataField) record.getVariableField("200");
			if (leader.charAt(7) == 'm' && dataField != null && dataField.getIndicator1() == '0') {
				natura = "w";
			} else {
				natura = String.valueOf(leader.charAt(7));	
			}
		} 
		return natura;
	}

	/*
	 * 710-729 a,b,c,d,f,g (separati da spazio)  con indicatore 1 = 0 ente
	 * 
	 * 710-729 a,b,c,d,f,g (separati da spazio)  con indicatore 1 = 1 congresso
	 */

	public Set<String> getEnteOrCongresso(final Record record, String pInd1) {
		Set<String> result = new LinkedHashSet<String>();
		char ind1 = pInd1.charAt(0);
		Pattern subfieldPattern = Pattern.compile("[a|b|c|d||e|f|g]");
		int startTag = 710;
		int endTag = 729;
		// itero sui tag
		StringBuilder buffer = null;
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> field = (List<VariableField>) record.getVariableFields(String.valueOf(tag));
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				buffer = new StringBuilder();

				DataField dataField = (DataField) iterator.next();
				if (dataField.getIndicator1() == ind1) {
					List<Subfield> subfields = dataField.getSubfields();
					for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
						Subfield subfield = (Subfield) iterator2.next();
						Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
						if (matcher.matches()) {
							if (buffer.length() > 0) buffer.append("");
							buffer.append(cleanPretitolo(subfield.getData()));
						}
					}
				}
				if (buffer.length() > 0)
					result.add(buffer.toString());

			}
		}
		buffer = new StringBuilder();
		List<VariableField> field = (List<VariableField>) record.getVariableFields("791");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			buffer = new StringBuilder();
			DataField dataField = (DataField) iterator.next();
			if (dataField.getIndicator1() == ind1) {
				List<Subfield> subfields = dataField.getSubfields();
				for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
					Subfield subfield = (Subfield) iterator2.next();
					Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
					if (matcher.matches()) {
						if (buffer.length() > 0) buffer.append("");
						buffer.append(cleanPretitolo(subfield.getData()));
					}
				}
			}
			if (buffer.length() > 0)
				result.add(buffer.toString());

		}
		return result;
	}




	/*
	 * 70x-71x-72x-73x-74x con x = 3|2|1  → *                a,b,c,d,e,f,g (separati da spazio)
	 */
	public Set<String> getNome_liv_$(final Record record, String x) {
		Set<String> result = new LinkedHashSet<String>();
		Pattern subfieldPattern = Pattern.compile("[a|b|c|d|e|f|g]");
		String startTagString = "70".concat(x);
		String endTagString = "74".concat(x);
		int startTag = Integer.valueOf(startTagString);
		int endTag = Integer.valueOf(endTagString);
		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			if (!String.valueOf(tag).endsWith(x) )	continue;
			List<VariableField> field = (List<VariableField>) record.getVariableFields(String.valueOf(tag));
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				StringBuilder buffer = new StringBuilder();
				DataField dataField = (DataField) iterator.next();
				List<Subfield> subfields = dataField.getSubfields();
				for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
					Subfield subfield = (Subfield) iterator2.next();
					Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
					if (matcher.matches()) {
						buffer.append(cleanPretitolo(subfield.getData()));
					}

				}
				if (buffer.length() > 0)
					result.add(buffer.toString());
			}
		}
		return result;
	}


	/*
	 * 	
	 */

	public String getPretitolo(final Record record) {
		List<VariableField> field = (List<VariableField>) record.getVariableFields("200");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();
			List<Subfield> subfields = dataField.getSubfields('a');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				String subValue = subfield.getData();

				return getPretitolo(subValue);
			}
		}
		return null;
	}

	private String getPretitolo(String value) {
		if (value != null && value.indexOf(START_PRETITOLO) != -1 && value.indexOf(END_PRETITOLO) != -1) {
			int beginIndex = value.indexOf(START_PRETITOLO) + START_PRETITOLO.length();
			int endIndex = value.indexOf(END_PRETITOLO);
			return value.substring(beginIndex, endIndex);
		}
		return null;
	}
	public String getPreautore(final Record record) {
		List<VariableField> marcFieldList = record.getVariableFields("710");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				if (subfield.getCode() != '3' && subfield.getCode() != '4') {
					String subValue = getPretitolo(subfield.getData());
					if (subValue != null  && subValue.trim().length() > 0)
						return subValue;
				}
			}
		}

		return null;
	}
	public String getUnimarcBase64(final Record record) {
		record.getLeader().setCharCodingScheme(' ');  // annulla il charset schema perchè altrimenti non è standard
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 

		MarcStreamWriter marcStreamWriter = new MarcStreamWriter(baos, "UTF-8");
		marcStreamWriter.write(record);
		marcStreamWriter.close();

		try {
			baos.flush();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		String unimarcBase64 = new String(Base64.encodeBase64(baos.toByteArray()));

		return unimarcBase64;
	}
	 /* public String getTitolo(final Record record, String tagStr, String separator) {
		String result = getFieldVals(record, tagStr, separator);
		result= cleanPretitolo(result);
		result = String.valueOf(foldToASCII(result.toCharArray(), result.length())).trim();
		return result;
	} */ 
	 public String getTitolo(final Record record, String tagStr, String separator) {
         return getTitolo2(record,tagStr,separator,"1");
     }
     public String getTitolo2(final Record record, String tagStr, String separator, String pre) {
         String result = getFieldVals(record, tagStr, separator);
         if (pre == null || pre.length() == 0 || "1".equals(pre))
            result= cleanPretitolo(result);
         else
        //   result = result.replaceAll(START_PRETITOLO,"").replaceAll(END_PRETITOLO, "");
        	 result = CleanNoSort.clean(result);
         result = String.valueOf(foldToASCII(result.toCharArray(), result.length())).trim();
         return result;
    }
	public Set<String> getDataPubF(final Record record, String tagStr, String separator) {
		Set<String> values = getFieldList(record, tagStr);
		Set<String> result = new LinkedHashSet<String>();
		Iterator<String> iterator = values.iterator(); 
		String type = "";
		String datada = "";
		String dataa = "";
		if (iterator.hasNext()) 
			type = (String) iterator.next();
		if (iterator.hasNext()) 
			datada = ((String) iterator.next()).replaceAll(" ", "");
		if (iterator.hasNext()) 
			dataa = ((String) iterator.next()).replaceAll(" ", "");
		if (!validDate(datada))
			datada = "";
		if (!validDate(dataa))
			dataa = "";
		if (type.equalsIgnoreCase("a")  && datada.length() == 4) {
			result.add(datada.concat("-"));
		}
		else if (type.equalsIgnoreCase("b") || type.equalsIgnoreCase("g")  || 
				type.equalsIgnoreCase("d") || type.equalsIgnoreCase("f") ) {
			if (dataa.length() == 4)
				result.add(datada.concat("-").concat(dataa));
			else
				result.add(datada);
		}
		else if (type.equalsIgnoreCase("e") || type.equalsIgnoreCase("r")) {
			if (datada.length() == 4)
				result.add(datada);
			if (dataa.length() == 4)
				result.add(dataa);	
		}
		return result;
	}
	public String getDataDa(final Record record, String tagStr, String separator) {
		Set<String> values = getFieldList(record, tagStr);
		Iterator<String> iterator = values.iterator(); 
		String datada = "";
		if (iterator.hasNext()) {
		}
		if (iterator.hasNext()) 
			datada = ((String) iterator.next()).replaceAll(" ", "");

		datada= replaceDate(datada, '0');
		if (validDate(datada))
			return datada;
		return null;
	}
	public Set<String> getIntVal(final Record record, String tagStr) {
		Set<String> result = new LinkedHashSet<String>();

		Set<String> values = getFieldList(record, tagStr);
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {

			String val = (String) iterator.next();
			val = val.replaceAll("[\\,|\\.]", "");
			try {
				int valI = Integer.parseInt(val);
				result.add(valI +"");
			} catch (NumberFormatException nfe) { 
				continue;
			}
		}
		return result;
	}
	public Set<String> getCordinata(final Record record, String tagStr) {
		Set<String> result = new LinkedHashSet<String>();

		Set<String> values = getFieldList(record, tagStr);
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {

			String val = (String) iterator.next();
			if (val == null || (val.length() != 8 && val.length() != 7))
				continue;

			if (val.toLowerCase().startsWith("s") || val.toLowerCase().startsWith("o"))
				val = val.toLowerCase().replace("s", "-").replace("o", "-");
			else if (val.startsWith(" "))
				val = "0";
			else
				val = val.substring(1);
			try {
				int valI = Integer.parseInt(val);
				result.add(valI +"");
			} catch (NumberFormatException nfe) { 
				continue;
			}
		}
		return result;
	}
	public String getDataA(final Record record, String tagStr, String separator) {
		String leader = record.getLeader().toString();
		char level = leader.charAt(7);
		Set<String> values = getFieldList(record, tagStr);
		String result = null;
		Iterator<String> iterator = values.iterator(); 
		String type = "";
		String datada = "";
		String dataa = "";
		if (iterator.hasNext()) 
			type = (String) iterator.next();
		if (iterator.hasNext()) 
			datada = ((String) iterator.next()).replaceAll(" ", "");
		if (iterator.hasNext()) 
			dataa = ((String) iterator.next()).substring(4).replaceAll(" ", "");
		if (type.equalsIgnoreCase("b") || type.equalsIgnoreCase("g"))
		   dataa = replaceDate(dataa, '9');
		if (type.equalsIgnoreCase("e") || type.equalsIgnoreCase("r"))
			datada = replaceDate(datada, '9');
		else if (!(type.equalsIgnoreCase("f")))
			datada = replaceDate(datada, '0');
		if (!validDate(datada))
			datada = "";
		if (!validDate(dataa))
			dataa = "";
		if ((type.equalsIgnoreCase("b") || type.equalsIgnoreCase("g")  ||  type.equalsIgnoreCase("f")) && dataa.length() == 4) {
			result = dataa;
		}
		else if ((type.equalsIgnoreCase("d") || type.equalsIgnoreCase("e") || type.equalsIgnoreCase("r") || (type.equalsIgnoreCase("f") && level == 'm')) && datada.length() == 4  ) {
			result = datada;
		}
		else if ((type.equalsIgnoreCase("a") || type.equalsIgnoreCase("b") || type.equalsIgnoreCase("c") ||
				type.equalsIgnoreCase("g")  ||  (type.equalsIgnoreCase("f") && level != 'm')) && datada.length() == 4) {
			result = "9999";
		}
		return result;
	}
	public String getDataA2(final Record record, String tagStr, String separator) {
		Set<String> values = getFieldList(record, tagStr);
		String result = null;
		Iterator<String> iterator = values.iterator(); 
		String type = "";
		String datada = "";
		String dataa = "";
		if (iterator.hasNext()) 
			type = (String) iterator.next();
		if (iterator.hasNext()) 
			datada = ((String) iterator.next()).replaceAll(" ", "");
		if (iterator.hasNext()) 
			dataa = ((String) iterator.next()).replaceAll(" ", "");
		if (type.equalsIgnoreCase("e"))
			dataa = replaceDate(dataa, '9');

		if (!validDate(datada))
			datada = "";
		if (!validDate(dataa))
			dataa = "";

		if ((type.equalsIgnoreCase("e") || type.equalsIgnoreCase("r")) && dataa.length() == 4  ) {
			result = dataa;
		}
		return result;
	}
	public String getDataDa2(final Record record, String tagStr, String separator) {
		Set<String> values = getFieldList(record, tagStr);
		String result = null;
		Iterator<String> iterator = values.iterator();
		String type = "";
		String datada = "";
		String dataa = "";
		if (iterator.hasNext())
			type = (String) iterator.next();
		if (iterator.hasNext())
			datada = ((String) iterator.next()).replaceAll(" ", "");
		if (iterator.hasNext())
			dataa = ((String) iterator.next()).replaceAll(" ", "");
		if (type.equalsIgnoreCase("e"))
			dataa = replaceDate(dataa, '0');

	//	logger.error("getDataDa2 datada " +  datada);
	//	logger.error("getDataDa2 dataa " +  dataa);

		if (!validDate(datada))
			datada = "";
		if (!validDate(dataa))
			dataa = "";

		if ((type.equalsIgnoreCase("e") || type.equalsIgnoreCase("r")) && dataa.length() == 4  ) {
			result = dataa;
	    }
	//	logger.error("getDataDa2 result " +  result);
		return result;
	}

	// 700abcdfg:701abcdfg:702abcdfg:703abcdfg:704abcdfg:705abcdfg:706abcdfg:707abcdfg:708abcdfg:709abcdfg, " "
	public Set<String> getPersona(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		Pattern subfieldPattern = Pattern.compile("[a|b|c|d|e|f|g]");
		int startTag = 700;
		int endTag = 709;
		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> field = (List<VariableField>) record.getVariableFields(String.valueOf(tag));
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				StringBuilder buffer = new StringBuilder();
				DataField dataField = (DataField) iterator.next();
				List<Subfield> subfields = dataField.getSubfields();
				for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
					Subfield subfield = (Subfield) iterator2.next();
					Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
					if (matcher.matches()) {
						// if (buffer.length() > 0) buffer.append(" ");
						buffer.append(cleanPretitolo(subfield.getData()));
					}
				}
				result.add(buffer.toString());
			}
		}
		List<VariableField> field = (List<VariableField>) record.getVariableFields("790");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			StringBuilder buffer = new StringBuilder();
			DataField dataField = (DataField) iterator.next();
			List<Subfield> subfields = dataField.getSubfields();
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
				if (matcher.matches()) {
					// if (buffer.length() > 0) buffer.append(" ");
					buffer.append(cleanPretitolo(subfield.getData()));
				}
			}
			result.add(buffer.toString());
		}

		return result;
	}
	public Set<String> getBiblioteca(final Record record) {

		Set<String> result = new LinkedHashSet<String>();


		String bid = ((ControlField) record.getVariableField("001")).getData();
		if (bid.startsWith("SBFL") || bid.startsWith("000")) {	
			Set<String> biblios = getFieldList(record, "950a");
			for (Iterator<String> iterator = biblios.iterator() ; iterator.hasNext();) {
				String val = iterator.next();
				if (val.contains("Biblioteca Civica di Finale Ligure"))
					result.add("IT-SV0021");
				else if (val.contains("Biblioteca Civica di Calice Ligure"))
					result.add("IT-SV0097");
				else if (val.contains("Biblioteca civica di Magliolo"))
					result.add("GB00000555");
				else if (val.contains("Biblioteca San Lorenzo Varigotti"))
					result.add("GB00000531");
				else if (val.contains("Biblioteca Civica \"Camillo Sbarbaro\""))
					result.add("IT-SV0045");


			}
			return result;
		}

		if (props_bib != null && props_bib.size() > 0) {
			if (props_bib.getProperty(bid) != null) {
				String[] elements = props_bib.getProperty(bid).split(" ");
				for (int i=0; i<elements.length; i++) {
					if (elements[i] != null && elements[i].length() > 0)
						result.add(elements[i]);
				}
			}
		}
		return result;
	}

	public Set<String> getProvincia(final Record record, String tagStr) {

		Set<String> result = new LinkedHashSet<String>();
		if (props_prov != null && props_prov.size() > 0) {
			Set<String> values = getFieldList(record, tagStr);
			for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
				String val = (String)iterator.next();
				if (val != null && val.trim().length()>0 && props_prov.getProperty(val.trim()) != null && props_prov.getProperty(val.trim()).length() > 0)
					result.add(props_prov.getProperty(val.trim()));
			}
		}
		if (props_bib != null && props_bib.size() > 0 && props_prov != null && props_prov.size() > 0) {
			String bid = ((ControlField) record.getVariableField("001")).getData();
			if (props_bib.getProperty(bid) != null) {
				String[] elements = props_bib.getProperty(bid).split(" ");

				for (int i=0; i<elements.length; i++) {
					if (elements[i] != null && elements[i].length() > 0 && props_prov.getProperty(elements[i]) != null && props_prov.getProperty(elements[i]).length() > 0)
						result.add(props_prov.getProperty(elements[i]));
				}
			}
		}
		return result;
	}
	public Set<String> getDataBib(final Record record, String bib) {

		Set<String> result = new LinkedHashSet<String>();
		Set<String> values = getFieldList(record, "850a");
		Set<String> dates = getFieldList(record, "950i");
		if (values.size() != dates.size())
			return null;
		Iterator<String> dateIteraror = dates.iterator();
		for (Iterator<String> iterator = values.iterator() ; iterator.hasNext();) {
			String val = (String)iterator.next();
			String date = (String) dateIteraror.next();
			if (bib.equalsIgnoreCase(val))
				result.add(date);
		}
		return result;
	}

	public Set<String> getFieldListPretitCleaned(final Record record, String tagStr, String fold) {
		Set<String> result = getFieldList(record, tagStr);
		result = cleanPretitolo(result,fold);
		return result;
	}

	public Set<String> getLuogoCleaned(final Record record, String tagStr) {
		Set<String> result = getFieldList(record, tagStr);
		result = cleanLuogo(result);
		return result;
	}

	public Set<String> getEditoreCleaned(final Record record, String tagStr) {
		Set<String> result = getFieldList(record, tagStr);
		result = cleanEditore(result);
		return result;
	}

	// 100[0-7]
	public Set<String> getDatadb(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> field = (List<VariableField>) record.getVariableFields("100");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();
			String stringDatafield = dataField.toString();
			if (stringDatafield != null && stringDatafield.length() > 0) {
				result.add(stringDatafield.substring(0, 7));
			}
		}
		return result;
	}


	// content = 105[0]:105[1]:105[2]
	public Set<String> getContent(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		if( "E".equalsIgnoreCase(((ControlField) record.getVariableField("001")).getData().substring(8, 9))) {
			List<VariableField> field = (List<VariableField>) record.getVariableFields("114");
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				DataField dataField = (DataField) iterator.next();
				List<Subfield> subfields = dataField.getSubfields('a');
				for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
					Subfield subfield = (Subfield) iterator2.next();
					String stringDatafield = subfield.toString();
					if (stringDatafield != null && stringDatafield.length() > 8) {
						result.add(stringDatafield.substring(11,13));
						result.add(stringDatafield.substring(13,15));
						result.add(stringDatafield.substring(15,17));
						result.add(stringDatafield.substring(17,19));
					}
				}

			}
		} else {
			List<VariableField> field = (List<VariableField>) record.getVariableFields("105");
			for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
				DataField dataField = (DataField) iterator.next();
				List<Subfield> subfields = dataField.getSubfields('a');
				for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
					Subfield subfield = (Subfield) iterator2.next();
					String stringDatafield = subfield.toString();
					if (stringDatafield != null && stringDatafield.length() > 3) {
						result.add(stringDatafield.substring(6,7));
						result.add(stringDatafield.substring(7,8));
						result.add(stringDatafield.substring(8,9));
						result.add(stringDatafield.substring(9,10));
						if ("1".equals(stringDatafield.substring(10,11)))
							result.add(stringDatafield.substring(10,11));
					}
				}

			}
		}
		return result;
	}

	public Set<String> getCollTitFaparte_$(final Record record, String tag, String ind1, Boolean clean_pretit) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> marcFieldList = record.getVariableFields(tag);
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;

			boolean firstA = true;

			boolean parentesiB = false;
			int idxLastB = 0;
			boolean firstB = true;
			boolean good = false;


				List<Subfield> subfields =  marcField.getSubfields();
				StringBuilder buffer = new StringBuilder("");
				for (Subfield subfield : subfields) {
					char code = subfield.getCode();
					String subfieldString = subfield.toString();
					if (ind1 == null && subfieldString.startsWith("$1200"))
						good = true;
					else if (ind1 != null && subfieldString.startsWith("$1200"+ ind1))
						good = true;
					if (subfieldString.startsWith("$17")) good = false;

					if (good) {
						switch (code) {

						case 'a':
							if (firstA) {
								if (clean_pretit)
								buffer.append(cleanPretitolo(subfield.getData()));
								else
									buffer.append(subfield.getData().replaceAll(START_PRETITOLO, "<<").replaceAll(END_PRETITOLO, ">>"));
								firstA = false;
							} else {
								if (clean_pretit)
									buffer.append(" ; ").append(cleanPretitolo(subfield.getData()));
								else
									buffer.append(" ; ").append(subfield.getData().replaceAll(START_PRETITOLO, "<<").replaceAll(END_PRETITOLO, ">>"));
							}
							break;

						case 'b':
							if (firstB) {
								buffer.append(" [").append(subfield.getData());
								firstB = false;
								parentesiB = true;
							} else {
								buffer.append(" ").append(subfield.getData());
								idxLastB = buffer.length();
							}
							break;

						case 'c':
							buffer.append(" . ").append(subfield.getData());
							break;

						case 'd':
							buffer.append(" = ").append(subfield.getData());
							break;

						case 'e':
							buffer.append(" : ").append(subfield.getData());
							break;

						default:
							break;
						}
					}

					// inserisco la parentesi dopo l'ultima b inserita
					if (parentesiB) {
						buffer.insert(idxLastB, "] ");
					}
				}
				if (buffer.length() > 0) result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
			}
//		if (clean_pretit)
//		   result = cleanPretitolo(result); // clean del pretitolo : già fatto per ogni $a
		return result;
	}
	public Set<String> getFaPartediTit(final Record record) {

		Set<String> result = new LinkedHashSet<String>();
		// itero sui tag
		String leader = record.getLeader().toString();
		char ger = leader.charAt(8);
		char level = leader.charAt(7);

		if ('2' != ger)
			return null;
		int startTag = 461;
		int endTag = 463;
		if ('a' == level || 'm' == level);
		else
			startTag = 464;
		for (int tag = startTag; tag <= endTag; tag++) {
			if ('a' == level && tag == 462)
				continue;
			result.addAll(getCollTitFaparte_$(record, String.valueOf(tag),"1", false));
		}
		return result;
	}

	public Set<String> getFaPartedi(final Record record) {

		Set<String> result = new LinkedHashSet<String>();
		// itero sui tag
		String leader = record.getLeader().toString();
		char ger = leader.charAt(8);
		char level = leader.charAt(7);

		if ('2' != ger)
			return null;
		int startTag = 461;
		int endTag = 463;
		if ('a' == level || 'm' == level);
		else
			startTag = 464;
		for (int tag = startTag; tag <= endTag; tag++) {
			if ('a' == level && tag == 462)
				continue;
			result.addAll(getCollTit_$(record, String.valueOf(tag),"1", false));
		}
		//		if (many > 1)
		//			logger.debug("Record" +   record.getVariableField("001").toString() + " con più fa parte di ");
		return result;
	}

	public Set<String> getIncipit(final Record record, String tagStr, String soloLett) {
		Set<String> values = getFieldList(record, tagStr);
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = (String) iterator.next();
			if (value.length() > 0 && "0".equals(soloLett))
				result.add (String.valueOf(foldToASCII(value.toCharArray(), value.length())).trim());
			char[] input = value.toCharArray();
			char []output = new char[input.length];
			int j =0;
			for (int pos = 0 ; pos < input.length ; ++pos) {
				final char c = input[pos];
				if (c >= 'A' && c <= 'Z')
					output[j++] = c;
			}
			result.add(String.valueOf(output).trim());
		}
		return result;
	}
	public String getDataUpdate(final Record record, String tagStr, String pattern) {

		Set<String> values = getFieldList(record, tagStr);

		Iterator<String> iterator = values.iterator(); 
		while (iterator.hasNext()) {
			String date = (String) iterator.next();
			try {
				return DatetimeConversion.getSolrDatetime(date, pattern);
			} catch (Exception e) {
				// se va in exception non aggiungo nulla
				// e.printStackTrace();
			}
		}

		return null;
	}

	public Set<String> getCleanData(final Record record, String tagStr) {
		Set<String> result = new LinkedHashSet<String>();

		Set<String> values = getFieldList(record, tagStr);

		Iterator<String> iterator = values.iterator(); 
		while (iterator.hasNext()) {
			String data = (String) iterator.next();
			try {
				Integer.valueOf(data);
				result.add(data);
			} catch (Throwable ex) {

			}
		}

		return result;
	}
	public Set<String> getRaccolte(final Record record) {

		//		logger.warn("getRaccolte " );
		if (props_raccolte == null) return null;
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<Object> iter = props_raccolte.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			//		 logger.warn("Raccolte key " +   key  + " val " +  props_raccolte.getProperty(key));
			String[] elements = props_raccolte.getProperty(key).split(" ");
			if (key.indexOf("NOKEY")> 1 )
				key = key.substring(0, key.indexOf("NOKEY"));
			Set<String> values = null;
			if (elements.length == 3) {
				values = getFieldList(record, elements[0]);
				for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
					String valmarc = (String)iterator.next();
					if (check_value_raccolte(valmarc,elements[1], elements[2]))
						result.add(key);
				}
			}
			else {
				int num = elements.length / 3;
				values = getFieldList(record, elements[0]);
				//		 logger.warn("Raccolte multivalue " +   key  + " element " + elements[0]);

				if(values.isEmpty())
					continue;

				for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
					boolean ok = true;
					String valmarc = (String)iterator.next();
					//		 logger.warn("Raccolte valmarc " +   key  + " single value " + valmarc);
					for (int j =0; j<num ;j++) {
						if (!check_value_raccolte(valmarc,elements[3*j+1], elements[3*j+2])) {
							ok = false;
							break;
						}
						if (!ok) break;
					}
					if (ok) {
						//		 logger.warn("Raccolte insert " +   key  + " value " + valmarc);
						result.add(key);
						break;
					}
				}
			}
		}
		return result;
	}

	/*
	 * UTILITY
	 */
	@SuppressWarnings("unused")
	private String get_value_raccolte(String str, String tag) {
		// logger.warn("Raccolte val " +   str  + " tag " + tag);

		int begin = 0;
		int end = 0;
		if (tag.indexOf("[") > -1 ) {
			if (tag.indexOf("-") > -1 ) {
				begin = localParseInt(tag.substring(tag.indexOf('[')+1,tag.indexOf('-')), 0);
				end = localParseInt(tag.substring(tag.indexOf('-')+1,tag.indexOf(']')), 0);
			}
			else {
				begin = localParseInt(tag.substring(tag.indexOf('[')+1,tag.indexOf(']')), 0);
				end = begin +1;
			}
			// logger.warn("begin , end" + begin + "," + end);
			if (str != null && str.length() < end)
				return "";
			str = str.substring(begin, end);
		}
		// logger.warn("Raccolte val end" +   str  );

		return str;
	}
	private boolean check_value_raccolte(String str, String op, String values) {
		//			logger.warn("Raccolte check " +   str  + " op " + op + " values " + values);
		if (str== null || str.trim().replaceAll("|","").isEmpty() )
			return false;
		String[] elements = values.split(",");

		for (int i=0; i<elements.length; i++) {
			//			logger.warn("Raccolte check values " +   str  + " op " + op + " val" + elements[i]);
			if (op.equalsIgnoreCase("eq") && str.trim().equalsIgnoreCase(elements[i]))
				return true;
			else if (op.equalsIgnoreCase("eq"))
				continue;
			if (op.equalsIgnoreCase("ct") && str.toLowerCase().trim().indexOf(elements[i].replaceAll("_"," ").toLowerCase()) != -1)
			{	
				//			logger.warn("Raccolte check contains " +   str.toLowerCase().trim()  +  " val " + elements[i].replaceAll("_"," ").toLowerCase());
				return true;
			}
			else if (op.equalsIgnoreCase("ct"))
				continue;
			int esito = str.trim().compareTo(elements[i]);
			if ((op.equalsIgnoreCase("eq") || op.equalsIgnoreCase("ge")  || op.equalsIgnoreCase("le")) && esito == 0)
				return true;
			if ((op.equalsIgnoreCase("lt") || op.equalsIgnoreCase("le")) && esito < 0 )
				return true;
			if ((op.equalsIgnoreCase("gt")  || op.equalsIgnoreCase("ge")) && esito > 0 )
				return true;
		}
		return false;
	}
	private int localParseInt(String str, int defValue) {
		int value = defValue;
		try {
			value = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
		}
		return (value);
	}
	public static String cleanPretitolo(String value) {
		if (value != null && value.indexOf(START_PRETITOLO) != -1 && value.indexOf(END_PRETITOLO) != -1) {
			int beginIndex = value.indexOf(START_PRETITOLO);
			int endIndex = value.indexOf(END_PRETITOLO);
			StringBuilder buffer = new StringBuilder();
			buffer.append(value.substring(0, beginIndex));
			buffer.append(value.substring(endIndex+END_PRETITOLO.length()));
			return buffer.toString();
		}
		return value;
	}

	public Set<String> getFieldFold(final Record record, String tagStr) {
		Set<String> values = getFieldList(record, tagStr);
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = (String) iterator.next();
			if (value.length() > 0)
				result.add (String.valueOf(foldToASCII(value.toCharArray(), value.length())).trim());
		}
		return result;
	}

	public static Set<String> getOperaNum(Set<String> values) {
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = iterator.next();
			if (value != null && value.length() > 0 && value.trim().indexOf(" ") > 0)
				result.add(value.substring(0, value.trim().indexOf(" ")));		
		}
		return result;
	}

	public static Set<String> cleanPretitolo(Set<String> values, String fold) {
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = iterator.next();
			if (value != null && value.indexOf(START_PRETITOLO) != -1 && value.indexOf(END_PRETITOLO) != -1) {
				int beginIndex = value.indexOf(START_PRETITOLO);
				int endIndex = value.indexOf(END_PRETITOLO);
				StringBuilder buffer = new StringBuilder();
				buffer.append(value.substring(0, beginIndex));
				buffer.append(value.substring(endIndex+END_PRETITOLO.length()));
				value = buffer.toString();
			}
			if (fold != null && fold.equals("1"))
				value = String.valueOf(foldToASCII(value.toCharArray(), value.length())).trim();

			result.add(value);

		}
		return result;
	}
	public static Set<String> getFold(Set<String> values) {
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = iterator.next();
			value = value.replaceAll(START_PRETITOLO, "").replaceAll(END_PRETITOLO, "");
			value = String.valueOf(foldToASCII(value.toCharArray(), value.length())).trim();
			result.add(value);
		}
		return result;
	}
	public static Set<String> cleanLuogo(Set<String> values) {
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = ((String) iterator.next()).trim();
			String first = "";
			StringBuilder buffer = new StringBuilder();
			if (value.matches("^[ \\\\\\?\\!\\[\t\n\f\r]*[sS]*\\.([ \t\n\f\r]*)[lL]\\.[\t \\]\n\f\r\\?!\\\\]*[\t \n\f\r]*$"))
				result.add("[s.l.]");
			else {
				// remove [poi] all'inizio
				if (value.startsWith("[poi] "))
					value = value.substring("[poi] ".length());
				// [, \, <, ?, @, (, &, 'parentesi_graffa_aperta', 'centesimo (intendo la piccola c tagliata verticalmente)', 'grado';
				if (value.startsWith("?") || value.startsWith("!") || 
						value.startsWith("[") || value.startsWith("\\") ||
						value.startsWith("<") || value.startsWith("@") ||
						value.startsWith("(") || value.startsWith("&") ||
						value.startsWith("{") || value.startsWith("°") || value.startsWith("ç")) {
					first = value.substring(0,1);
					value = value.substring(1);
				}
				// ], !, >, ? (solo se è simmetrico a '?' in posizione iniziale), ), @, &, 'parentesi_graffa_chiusa'.

				if ((value.endsWith("?") && first.equals("?")) || value.endsWith("!") || 
						value.endsWith("]") || value.endsWith(">") || value.endsWith("@") ||
						value.endsWith(")") || value.endsWith("&") ||
						value.endsWith("}")) {
					buffer.append(value.substring(0, value.length()-1));
					result.add(buffer.toString());
				} else {
					result.add(String.valueOf(foldToASCII(value.toCharArray(),value.length())).trim());
				}
			}
		}
		return result;
	}
	public static Set<String> cleanEditore(Set<String> values) {
		Set<String> result = new LinkedHashSet<String>();
		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = iterator.next().trim();
			if (value.matches("^[ \\\\\\?!\\[\\(\t\n\f\r]*[sS]*\\.([ \t\n\f\r]*)[nN]\\.[!\\?\t \\]\n\f\r\\)\\\\]*[\t \n\f\r]*$"))				
				result.add("[s.n.]");
			else {
				// remove [poi] all'inizio
				if (value.startsWith("[poi] "))
					value = value.substring("[poi] ".length());
				result.add(String.valueOf(foldToASCII(value.toCharArray(),value.length())).trim());
			}
		}
		return result;
	}

	public static boolean validDate (String date) {
		if (date.length() != 4  || date.equals("9999") || date.equals("0000"))
			return false;
		try {
		} catch (NumberFormatException nfe) { 
			return false;
		}
		return true;
	}

        public static String replaceDate (String date, char conv) {
		if (date != null && date.length() > 2) {
		int len = date.length() - 2;

		String newdate = date.substring(0,2);
		for (int i=len; i<date.length();i++) {
			if (date.charAt(i) == '.')
			   newdate = newdate + conv;
			else
				newdate = newdate +  date.charAt(i);
		}
		 return newdate;
	}
	else
		return date;
	}

	/**
	 * Converts characters above ASCII to their ASCII equivalents.  For example,
	 * accents are removed from accented characters.
	 * @param input The string to fold
	 * @param length The number of characters in the input string
	 */
	public static char[] foldToASCII(char[] input, int length) {

		char[] output = new char[512];
		// Worst-case length required:
		final int maxSizeNeeded = 4 * length;
		if (output.length < maxSizeNeeded) {
			output = new char[getNextSize(maxSizeNeeded)];
		}

		int outputPos = 0;

		for (int pos = 0 ; pos < length ; ++pos) {
			final char c = input[pos];

			// Quick test: if it's not in range then just keep current character
			if (c < '\u0080') {
				output[outputPos++] = c;
			} else {
				switch (c) {
				case '\u00C0': // À  [LATIN CAPITAL LETTER A WITH GRAVE]
				case '\u00C1': // Á  [LATIN CAPITAL LETTER A WITH ACUTE]
				case '\u00C2': // Â  [LATIN CAPITAL LETTER A WITH CIRCUMFLEX]
				case '\u00C3': // Ã  [LATIN CAPITAL LETTER A WITH TILDE]
				case '\u00C4': // Ä  [LATIN CAPITAL LETTER A WITH DIAERESIS]
				case '\u00C5': // Å  [LATIN CAPITAL LETTER A WITH RING ABOVE]
				case '\u0100': // Ā  [LATIN CAPITAL LETTER A WITH MACRON]
				case '\u0102': // Ă  [LATIN CAPITAL LETTER A WITH BREVE]
				case '\u0104': // Ą  [LATIN CAPITAL LETTER A WITH OGONEK]
				case '\u018F': // Ə  http://en.wikipedia.org/wiki/Schwa  [LATIN CAPITAL LETTER SCHWA]
				case '\u01CD': // Ǎ  [LATIN CAPITAL LETTER A WITH CARON]
				case '\u01DE': // Ǟ  [LATIN CAPITAL LETTER A WITH DIAERESIS AND MACRON]
				case '\u01E0': // Ǡ  [LATIN CAPITAL LETTER A WITH DOT ABOVE AND MACRON]
				case '\u01FA': // Ǻ  [LATIN CAPITAL LETTER A WITH RING ABOVE AND ACUTE]
				case '\u0200': // Ȁ  [LATIN CAPITAL LETTER A WITH DOUBLE GRAVE]
				case '\u0202': // Ȃ  [LATIN CAPITAL LETTER A WITH INVERTED BREVE]
				case '\u0226': // Ȧ  [LATIN CAPITAL LETTER A WITH DOT ABOVE]
				case '\u023A': // Ⱥ  [LATIN CAPITAL LETTER A WITH STROKE]
				case '\u1D00': // ᴀ  [LATIN LETTER SMALL CAPITAL A]
				case '\u1E00': // Ḁ  [LATIN CAPITAL LETTER A WITH RING BELOW]
				case '\u1EA0': // Ạ  [LATIN CAPITAL LETTER A WITH DOT BELOW]
				case '\u1EA2': // Ả  [LATIN CAPITAL LETTER A WITH HOOK ABOVE]
				case '\u1EA4': // Ấ  [LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND ACUTE]
				case '\u1EA6': // Ầ  [LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND GRAVE]
				case '\u1EA8': // Ẩ  [LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND HOOK ABOVE]
				case '\u1EAA': // Ẫ  [LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND TILDE]
				case '\u1EAC': // Ậ  [LATIN CAPITAL LETTER A WITH CIRCUMFLEX AND DOT BELOW]
				case '\u1EAE': // Ắ  [LATIN CAPITAL LETTER A WITH BREVE AND ACUTE]
				case '\u1EB0': // Ằ  [LATIN CAPITAL LETTER A WITH BREVE AND GRAVE]
				case '\u1EB2': // Ẳ  [LATIN CAPITAL LETTER A WITH BREVE AND HOOK ABOVE]
				case '\u1EB4': // Ẵ  [LATIN CAPITAL LETTER A WITH BREVE AND TILDE]
				case '\u1EB6': // Ặ  [LATIN CAPITAL LETTER A WITH BREVE AND DOT BELOW]
				case '\u24B6': // Ⓐ  [CIRCLED LATIN CAPITAL LETTER A]
				case '\uFF21': // Ａ  [FULLWIDTH LATIN CAPITAL LETTER A]
					output[outputPos++] = 'A';
					break;
				case '\u00E0': // à  [LATIN SMALL LETTER A WITH GRAVE]
				case '\u00E1': // á  [LATIN SMALL LETTER A WITH ACUTE]
				case '\u00E2': // â  [LATIN SMALL LETTER A WITH CIRCUMFLEX]
				case '\u00E3': // ã  [LATIN SMALL LETTER A WITH TILDE]
				case '\u00E4': // ä  [LATIN SMALL LETTER A WITH DIAERESIS]
				case '\u00E5': // å  [LATIN SMALL LETTER A WITH RING ABOVE]
				case '\u0101': // ā  [LATIN SMALL LETTER A WITH MACRON]
				case '\u0103': // ă  [LATIN SMALL LETTER A WITH BREVE]
				case '\u0105': // ą  [LATIN SMALL LETTER A WITH OGONEK]
				case '\u01CE': // ǎ  [LATIN SMALL LETTER A WITH CARON]
				case '\u01DF': // ǟ  [LATIN SMALL LETTER A WITH DIAERESIS AND MACRON]
				case '\u01E1': // ǡ  [LATIN SMALL LETTER A WITH DOT ABOVE AND MACRON]
				case '\u01FB': // ǻ  [LATIN SMALL LETTER A WITH RING ABOVE AND ACUTE]
				case '\u0201': // ȁ  [LATIN SMALL LETTER A WITH DOUBLE GRAVE]
				case '\u0203': // ȃ  [LATIN SMALL LETTER A WITH INVERTED BREVE]
				case '\u0227': // ȧ  [LATIN SMALL LETTER A WITH DOT ABOVE]
				case '\u0250': // ɐ  [LATIN SMALL LETTER TURNED A]
				case '\u0259': // ə  [LATIN SMALL LETTER SCHWA]
				case '\u025A': // ɚ  [LATIN SMALL LETTER SCHWA WITH HOOK]
				case '\u1D8F': // ᶏ  [LATIN SMALL LETTER A WITH RETROFLEX HOOK]
				case '\u1D95': // ᶕ  [LATIN SMALL LETTER SCHWA WITH RETROFLEX HOOK]
				case '\u1E01': // ạ  [LATIN SMALL LETTER A WITH RING BELOW]
				case '\u1E9A': // ả  [LATIN SMALL LETTER A WITH RIGHT HALF RING]
				case '\u1EA1': // ạ  [LATIN SMALL LETTER A WITH DOT BELOW]
				case '\u1EA3': // ả  [LATIN SMALL LETTER A WITH HOOK ABOVE]
				case '\u1EA5': // ấ  [LATIN SMALL LETTER A WITH CIRCUMFLEX AND ACUTE]
				case '\u1EA7': // ầ  [LATIN SMALL LETTER A WITH CIRCUMFLEX AND GRAVE]
				case '\u1EA9': // ẩ  [LATIN SMALL LETTER A WITH CIRCUMFLEX AND HOOK ABOVE]
				case '\u1EAB': // ẫ  [LATIN SMALL LETTER A WITH CIRCUMFLEX AND TILDE]
				case '\u1EAD': // ậ  [LATIN SMALL LETTER A WITH CIRCUMFLEX AND DOT BELOW]
				case '\u1EAF': // ắ  [LATIN SMALL LETTER A WITH BREVE AND ACUTE]
				case '\u1EB1': // ằ  [LATIN SMALL LETTER A WITH BREVE AND GRAVE]
				case '\u1EB3': // ẳ  [LATIN SMALL LETTER A WITH BREVE AND HOOK ABOVE]
				case '\u1EB5': // ẵ  [LATIN SMALL LETTER A WITH BREVE AND TILDE]
				case '\u1EB7': // ặ  [LATIN SMALL LETTER A WITH BREVE AND DOT BELOW]
				case '\u2090': // ₐ  [LATIN SUBSCRIPT SMALL LETTER A]
				case '\u2094': // ₔ  [LATIN SUBSCRIPT SMALL LETTER SCHWA]
				case '\u24D0': // ⓐ  [CIRCLED LATIN SMALL LETTER A]
				case '\u2C65': // ⱥ  [LATIN SMALL LETTER A WITH STROKE]
				case '\u2C6F': // Ɐ  [LATIN CAPITAL LETTER TURNED A]
				case '\uFF41': // ａ  [FULLWIDTH LATIN SMALL LETTER A]
					output[outputPos++] = 'a';
					break;
				case '\uA732': // Ꜳ  [LATIN CAPITAL LETTER AA]
					output[outputPos++] = 'A';
					output[outputPos++] = 'A';
					break;
				case '\u00C6': // Æ  [LATIN CAPITAL LETTER AE]
				case '\u01E2': // Ǣ  [LATIN CAPITAL LETTER AE WITH MACRON]
				case '\u01FC': // Ǽ  [LATIN CAPITAL LETTER AE WITH ACUTE]
				case '\u1D01': // ᴁ  [LATIN LETTER SMALL CAPITAL AE]
					output[outputPos++] = 'A';
					output[outputPos++] = 'E';
					break;
				case '\uA734': // Ꜵ  [LATIN CAPITAL LETTER AO]
					output[outputPos++] = 'A';                    
					output[outputPos++] = 'O';
					break;
				case '\uA736': // Ꜷ  [LATIN CAPITAL LETTER AU]
					output[outputPos++] = 'A';
					output[outputPos++] = 'U';
					break;
				case '\uA738': // Ꜹ  [LATIN CAPITAL LETTER AV]
				case '\uA73A': // Ꜻ  [LATIN CAPITAL LETTER AV WITH HORIZONTAL BAR]
					output[outputPos++] = 'A';
					output[outputPos++] = 'V';
					break;
				case '\uA73C': // Ꜽ  [LATIN CAPITAL LETTER AY]
					output[outputPos++] = 'A';
					output[outputPos++] = 'Y';
					break;
				case '\u249C': // ⒜  [PARENTHESIZED LATIN SMALL LETTER A]
					output[outputPos++] = '(';
					output[outputPos++] = 'a';
					output[outputPos++] = ')';
					break;
				case '\uA733': // ꜳ  [LATIN SMALL LETTER AA]
					output[outputPos++] = 'a';
					output[outputPos++] = 'a';
					break;
				case '\u00E6': // æ  [LATIN SMALL LETTER AE]
				case '\u01E3': // ǣ  [LATIN SMALL LETTER AE WITH MACRON]
				case '\u01FD': // ǽ  [LATIN SMALL LETTER AE WITH ACUTE]
				case '\u1D02': // ᴂ  [LATIN SMALL LETTER TURNED AE]
					output[outputPos++] = 'a';
					output[outputPos++] = 'e';
					break;
				case '\uA735': // ꜵ  [LATIN SMALL LETTER AO]
					output[outputPos++] = 'a';
					output[outputPos++] = 'o';
					break;
				case '\uA737': // ꜷ  [LATIN SMALL LETTER AU]
					output[outputPos++] = 'a';
					output[outputPos++] = 'u';
					break;
				case '\uA739': // ꜹ  [LATIN SMALL LETTER AV]
				case '\uA73B': // ꜻ  [LATIN SMALL LETTER AV WITH HORIZONTAL BAR]
					output[outputPos++] = 'a';
					output[outputPos++] = 'v';
					break;
				case '\uA73D': // ꜽ  [LATIN SMALL LETTER AY]
					output[outputPos++] = 'a';
					output[outputPos++] = 'y';
					break;
				case '\u0181': // Ɓ  [LATIN CAPITAL LETTER B WITH HOOK]
				case '\u0182': // Ƃ  [LATIN CAPITAL LETTER B WITH TOPBAR]
				case '\u0243': // Ƀ  [LATIN CAPITAL LETTER B WITH STROKE]
				case '\u0299': // ʙ  [LATIN LETTER SMALL CAPITAL B]
				case '\u1D03': // ᴃ  [LATIN LETTER SMALL CAPITAL BARRED B]
				case '\u1E02': // Ḃ  [LATIN CAPITAL LETTER B WITH DOT ABOVE]
				case '\u1E04': // Ḅ  [LATIN CAPITAL LETTER B WITH DOT BELOW]
				case '\u1E06': // Ḇ  [LATIN CAPITAL LETTER B WITH LINE BELOW]
				case '\u24B7': // Ⓑ  [CIRCLED LATIN CAPITAL LETTER B]
				case '\uFF22': // Ｂ  [FULLWIDTH LATIN CAPITAL LETTER B]
					output[outputPos++] = 'B';
					break;
				case '\u0180': // ƀ  [LATIN SMALL LETTER B WITH STROKE]
				case '\u0183': // ƃ  [LATIN SMALL LETTER B WITH TOPBAR]
				case '\u0253': // ɓ  [LATIN SMALL LETTER B WITH HOOK]
				case '\u1D6C': // ᵬ  [LATIN SMALL LETTER B WITH MIDDLE TILDE]
				case '\u1D80': // ᶀ  [LATIN SMALL LETTER B WITH PALATAL HOOK]
				case '\u1E03': // ḃ  [LATIN SMALL LETTER B WITH DOT ABOVE]
				case '\u1E05': // ḅ  [LATIN SMALL LETTER B WITH DOT BELOW]
				case '\u1E07': // ḇ  [LATIN SMALL LETTER B WITH LINE BELOW]
				case '\u24D1': // ⓑ  [CIRCLED LATIN SMALL LETTER B]
				case '\uFF42': // ｂ  [FULLWIDTH LATIN SMALL LETTER B]
					output[outputPos++] = 'b';
					break;
				case '\u249D': // ⒝  [PARENTHESIZED LATIN SMALL LETTER B]
					output[outputPos++] = '(';                    
					output[outputPos++] = 'b';
					output[outputPos++] = ')';
					break;
				case '\u00C7': // Ç  [LATIN CAPITAL LETTER C WITH CEDILLA]
				case '\u0106': // Ć  [LATIN CAPITAL LETTER C WITH ACUTE]
				case '\u0108': // Ĉ  [LATIN CAPITAL LETTER C WITH CIRCUMFLEX]
				case '\u010A': // Ċ  [LATIN CAPITAL LETTER C WITH DOT ABOVE]
				case '\u010C': // Č  [LATIN CAPITAL LETTER C WITH CARON]
				case '\u0187': // Ƈ  [LATIN CAPITAL LETTER C WITH HOOK]
				case '\u023B': // Ȼ  [LATIN CAPITAL LETTER C WITH STROKE]
				case '\u0297': // ʗ  [LATIN LETTER STRETCHED C]
				case '\u1D04': // ᴄ  [LATIN LETTER SMALL CAPITAL C]
				case '\u1E08': // Ḉ  [LATIN CAPITAL LETTER C WITH CEDILLA AND ACUTE]
				case '\u24B8': // Ⓒ  [CIRCLED LATIN CAPITAL LETTER C]
				case '\uFF23': // Ｃ  [FULLWIDTH LATIN CAPITAL LETTER C]
					output[outputPos++] = 'C';
					break;
				case '\u00E7': // ç  [LATIN SMALL LETTER C WITH CEDILLA]
				case '\u0107': // ć  [LATIN SMALL LETTER C WITH ACUTE]
				case '\u0109': // ĉ  [LATIN SMALL LETTER C WITH CIRCUMFLEX]
				case '\u010B': // ċ  [LATIN SMALL LETTER C WITH DOT ABOVE]
				case '\u010D': // č  [LATIN SMALL LETTER C WITH CARON]
				case '\u0188': // ƈ  [LATIN SMALL LETTER C WITH HOOK]
				case '\u023C': // ȼ  [LATIN SMALL LETTER C WITH STROKE]
				case '\u0255': // ɕ  [LATIN SMALL LETTER C WITH CURL]
				case '\u1E09': // ḉ  [LATIN SMALL LETTER C WITH CEDILLA AND ACUTE]
				case '\u2184': // ↄ  [LATIN SMALL LETTER REVERSED C]
				case '\u24D2': // ⓒ  [CIRCLED LATIN SMALL LETTER C]
				case '\uA73E': // Ꜿ  [LATIN CAPITAL LETTER REVERSED C WITH DOT]
				case '\uA73F': // ꜿ  [LATIN SMALL LETTER REVERSED C WITH DOT]
				case '\uFF43': // ｃ  [FULLWIDTH LATIN SMALL LETTER C]
					output[outputPos++] = 'c';
					break;
				case '\u249E': // ⒞  [PARENTHESIZED LATIN SMALL LETTER C]
					output[outputPos++] = '(';
					output[outputPos++] = 'c';
					output[outputPos++] = ')';
					break;
				case '\u00D0': // Ð  [LATIN CAPITAL LETTER ETH]
				case '\u010E': // Ď  [LATIN CAPITAL LETTER D WITH CARON]
				case '\u0110': // Đ  [LATIN CAPITAL LETTER D WITH STROKE]
				case '\u0189': // Ɖ  [LATIN CAPITAL LETTER AFRICAN D]
				case '\u018A': // Ɗ  [LATIN CAPITAL LETTER D WITH HOOK]
				case '\u018B': // Ƌ  [LATIN CAPITAL LETTER D WITH TOPBAR]
				case '\u1D05': // ᴅ  [LATIN LETTER SMALL CAPITAL D]
				case '\u1D06': // ᴆ  [LATIN LETTER SMALL CAPITAL ETH]
				case '\u1E0A': // Ḋ  [LATIN CAPITAL LETTER D WITH DOT ABOVE]
				case '\u1E0C': // Ḍ  [LATIN CAPITAL LETTER D WITH DOT BELOW]
				case '\u1E0E': // Ḏ  [LATIN CAPITAL LETTER D WITH LINE BELOW]
				case '\u1E10': // Ḑ  [LATIN CAPITAL LETTER D WITH CEDILLA]
				case '\u1E12': // Ḓ  [LATIN CAPITAL LETTER D WITH CIRCUMFLEX BELOW]
				case '\u24B9': // Ⓓ  [CIRCLED LATIN CAPITAL LETTER D]
				case '\uA779': // Ꝺ  [LATIN CAPITAL LETTER INSULAR D]
				case '\uFF24': // Ｄ  [FULLWIDTH LATIN CAPITAL LETTER D]
					output[outputPos++] = 'D';
					break;
				case '\u00F0': // ð  [LATIN SMALL LETTER ETH]
				case '\u010F': // ď  [LATIN SMALL LETTER D WITH CARON]
				case '\u0111': // đ  [LATIN SMALL LETTER D WITH STROKE]
				case '\u018C': // ƌ  [LATIN SMALL LETTER D WITH TOPBAR]
				case '\u0221': // ȡ  [LATIN SMALL LETTER D WITH CURL]
				case '\u0256': // ɖ  [LATIN SMALL LETTER D WITH TAIL]
				case '\u0257': // ɗ  [LATIN SMALL LETTER D WITH HOOK]
				case '\u1D6D': // ᵭ  [LATIN SMALL LETTER D WITH MIDDLE TILDE]
				case '\u1D81': // ᶁ  [LATIN SMALL LETTER D WITH PALATAL HOOK]
				case '\u1D91': // ᶑ  [LATIN SMALL LETTER D WITH HOOK AND TAIL]
				case '\u1E0B': // ḋ  [LATIN SMALL LETTER D WITH DOT ABOVE]
				case '\u1E0D': // ḍ  [LATIN SMALL LETTER D WITH DOT BELOW]
				case '\u1E0F': // ḏ  [LATIN SMALL LETTER D WITH LINE BELOW]
				case '\u1E11': // ḑ  [LATIN SMALL LETTER D WITH CEDILLA]
				case '\u1E13': // ḓ  [LATIN SMALL LETTER D WITH CIRCUMFLEX BELOW]
				case '\u24D3': // ⓓ  [CIRCLED LATIN SMALL LETTER D]
				case '\uA77A': // ꝺ  [LATIN SMALL LETTER INSULAR D]
				case '\uFF44': // ｄ  [FULLWIDTH LATIN SMALL LETTER D]
					output[outputPos++] = 'd';
					break;
				case '\u01C4': // Ǆ  [LATIN CAPITAL LETTER DZ WITH CARON]
				case '\u01F1': // Ǳ  [LATIN CAPITAL LETTER DZ]
					output[outputPos++] = 'D';
					output[outputPos++] = 'Z';
					break;
				case '\u01C5': // ǅ  [LATIN CAPITAL LETTER D WITH SMALL LETTER Z WITH CARON]
				case '\u01F2': // ǲ  [LATIN CAPITAL LETTER D WITH SMALL LETTER Z]
					output[outputPos++] = 'D';
					output[outputPos++] = 'z';
					break;
				case '\u249F': // ⒟  [PARENTHESIZED LATIN SMALL LETTER D]
					output[outputPos++] = '(';
					output[outputPos++] = 'd';
					output[outputPos++] = ')';
					break;
				case '\u0238': // ȸ  [LATIN SMALL LETTER DB DIGRAPH]
					output[outputPos++] = 'd';
					output[outputPos++] = 'b';
					break;
				case '\u01C6': // ǆ  [LATIN SMALL LETTER DZ WITH CARON]
				case '\u01F3': // ǳ  [LATIN SMALL LETTER DZ]
				case '\u02A3': // ʣ  [LATIN SMALL LETTER DZ DIGRAPH]
				case '\u02A5': // ʥ  [LATIN SMALL LETTER DZ DIGRAPH WITH CURL]
					output[outputPos++] = 'd';
					output[outputPos++] = 'z';
					break;
				case '\u00C8': // È  [LATIN CAPITAL LETTER E WITH GRAVE]
				case '\u00C9': // É  [LATIN CAPITAL LETTER E WITH ACUTE]
				case '\u00CA': // Ê  [LATIN CAPITAL LETTER E WITH CIRCUMFLEX]
				case '\u00CB': // Ë  [LATIN CAPITAL LETTER E WITH DIAERESIS]
				case '\u0112': // Ē  [LATIN CAPITAL LETTER E WITH MACRON]
				case '\u0114': // Ĕ  [LATIN CAPITAL LETTER E WITH BREVE]
				case '\u0116': // Ė  [LATIN CAPITAL LETTER E WITH DOT ABOVE]
				case '\u0118': // Ę  [LATIN CAPITAL LETTER E WITH OGONEK]
				case '\u011A': // Ě  [LATIN CAPITAL LETTER E WITH CARON]
				case '\u018E': // Ǝ  [LATIN CAPITAL LETTER REVERSED E]
				case '\u0190': // Ɛ  [LATIN CAPITAL LETTER OPEN E]
				case '\u0204': // Ȅ  [LATIN CAPITAL LETTER E WITH DOUBLE GRAVE]
				case '\u0206': // Ȇ  [LATIN CAPITAL LETTER E WITH INVERTED BREVE]
				case '\u0228': // Ȩ  [LATIN CAPITAL LETTER E WITH CEDILLA]
				case '\u0246': // Ɇ  [LATIN CAPITAL LETTER E WITH STROKE]
				case '\u1D07': // ᴇ  [LATIN LETTER SMALL CAPITAL E]
				case '\u1E14': // Ḕ  [LATIN CAPITAL LETTER E WITH MACRON AND GRAVE]
				case '\u1E16': // Ḗ  [LATIN CAPITAL LETTER E WITH MACRON AND ACUTE]
				case '\u1E18': // Ḙ  [LATIN CAPITAL LETTER E WITH CIRCUMFLEX BELOW]
				case '\u1E1A': // Ḛ  [LATIN CAPITAL LETTER E WITH TILDE BELOW]
				case '\u1E1C': // Ḝ  [LATIN CAPITAL LETTER E WITH CEDILLA AND BREVE]
				case '\u1EB8': // Ẹ  [LATIN CAPITAL LETTER E WITH DOT BELOW]
				case '\u1EBA': // Ẻ  [LATIN CAPITAL LETTER E WITH HOOK ABOVE]
				case '\u1EBC': // Ẽ  [LATIN CAPITAL LETTER E WITH TILDE]
				case '\u1EBE': // Ế  [LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND ACUTE]
				case '\u1EC0': // Ề  [LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND GRAVE]
				case '\u1EC2': // Ể  [LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND HOOK ABOVE]
				case '\u1EC4': // Ễ  [LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND TILDE]
				case '\u1EC6': // Ệ  [LATIN CAPITAL LETTER E WITH CIRCUMFLEX AND DOT BELOW]
				case '\u24BA': // Ⓔ  [CIRCLED LATIN CAPITAL LETTER E]
				case '\u2C7B': // ⱻ  [LATIN LETTER SMALL CAPITAL TURNED E]
				case '\uFF25': // Ｅ  [FULLWIDTH LATIN CAPITAL LETTER E]
					output[outputPos++] = 'E';
					break;
				case '\u00E8': // è  [LATIN SMALL LETTER E WITH GRAVE]
				case '\u00E9': // é  [LATIN SMALL LETTER E WITH ACUTE]
				case '\u00EA': // ê  [LATIN SMALL LETTER E WITH CIRCUMFLEX]
				case '\u00EB': // ë  [LATIN SMALL LETTER E WITH DIAERESIS]
				case '\u0113': // ē  [LATIN SMALL LETTER E WITH MACRON]
				case '\u0115': // ĕ  [LATIN SMALL LETTER E WITH BREVE]
				case '\u0117': // ė  [LATIN SMALL LETTER E WITH DOT ABOVE]
				case '\u0119': // ę  [LATIN SMALL LETTER E WITH OGONEK]
				case '\u011B': // ě  [LATIN SMALL LETTER E WITH CARON]
				case '\u01DD': // ǝ  [LATIN SMALL LETTER TURNED E]
				case '\u0205': // ȅ  [LATIN SMALL LETTER E WITH DOUBLE GRAVE]
				case '\u0207': // ȇ  [LATIN SMALL LETTER E WITH INVERTED BREVE]
				case '\u0229': // ȩ  [LATIN SMALL LETTER E WITH CEDILLA]
				case '\u0247': // ɇ  [LATIN SMALL LETTER E WITH STROKE]
				case '\u0258': // ɘ  [LATIN SMALL LETTER REVERSED E]
				case '\u025B': // ɛ  [LATIN SMALL LETTER OPEN E]
				case '\u025C': // ɜ  [LATIN SMALL LETTER REVERSED OPEN E]
				case '\u025D': // ɝ  [LATIN SMALL LETTER REVERSED OPEN E WITH HOOK]
				case '\u025E': // ɞ  [LATIN SMALL LETTER CLOSED REVERSED OPEN E]
				case '\u029A': // ʚ  [LATIN SMALL LETTER CLOSED OPEN E]
				case '\u1D08': // ᴈ  [LATIN SMALL LETTER TURNED OPEN E]
				case '\u1D92': // ᶒ  [LATIN SMALL LETTER E WITH RETROFLEX HOOK]
				case '\u1D93': // ᶓ  [LATIN SMALL LETTER OPEN E WITH RETROFLEX HOOK]
				case '\u1D94': // ᶔ  [LATIN SMALL LETTER REVERSED OPEN E WITH RETROFLEX HOOK]
				case '\u1E15': // ḕ  [LATIN SMALL LETTER E WITH MACRON AND GRAVE]
				case '\u1E17': // ḗ  [LATIN SMALL LETTER E WITH MACRON AND ACUTE]
				case '\u1E19': // ḙ  [LATIN SMALL LETTER E WITH CIRCUMFLEX BELOW]
				case '\u1E1B': // ḛ  [LATIN SMALL LETTER E WITH TILDE BELOW]
				case '\u1E1D': // ḝ  [LATIN SMALL LETTER E WITH CEDILLA AND BREVE]
				case '\u1EB9': // ẹ  [LATIN SMALL LETTER E WITH DOT BELOW]
				case '\u1EBB': // ẻ  [LATIN SMALL LETTER E WITH HOOK ABOVE]
				case '\u1EBD': // ẽ  [LATIN SMALL LETTER E WITH TILDE]
				case '\u1EBF': // ế  [LATIN SMALL LETTER E WITH CIRCUMFLEX AND ACUTE]
				case '\u1EC1': // ề  [LATIN SMALL LETTER E WITH CIRCUMFLEX AND GRAVE]
				case '\u1EC3': // ể  [LATIN SMALL LETTER E WITH CIRCUMFLEX AND HOOK ABOVE]
				case '\u1EC5': // ễ  [LATIN SMALL LETTER E WITH CIRCUMFLEX AND TILDE]
				case '\u1EC7': // ệ  [LATIN SMALL LETTER E WITH CIRCUMFLEX AND DOT BELOW]
				case '\u2091': // ₑ  [LATIN SUBSCRIPT SMALL LETTER E]
				case '\u24D4': // ⓔ  [CIRCLED LATIN SMALL LETTER E]
				case '\u2C78': // ⱸ  [LATIN SMALL LETTER E WITH NOTCH]
				case '\uFF45': // ｅ  [FULLWIDTH LATIN SMALL LETTER E]
					output[outputPos++] = 'e';
					break;
				case '\u24A0': // ⒠  [PARENTHESIZED LATIN SMALL LETTER E]
					output[outputPos++] = '(';
					output[outputPos++] = 'e';
					output[outputPos++] = ')';
					break;
				case '\u0191': // Ƒ  [LATIN CAPITAL LETTER F WITH HOOK]
				case '\u1E1E': // Ḟ  [LATIN CAPITAL LETTER F WITH DOT ABOVE]
				case '\u24BB': // Ⓕ  [CIRCLED LATIN CAPITAL LETTER F]
				case '\uA730': // ꜰ  [LATIN LETTER SMALL CAPITAL F]
				case '\uA77B': // Ꝼ  [LATIN CAPITAL LETTER INSULAR F]
				case '\uA7FB': // ꟻ  [LATIN EPIGRAPHIC LETTER REVERSED F]
				case '\uFF26': // Ｆ  [FULLWIDTH LATIN CAPITAL LETTER F]
					output[outputPos++] = 'F';
					break;
				case '\u0192': // ƒ  [LATIN SMALL LETTER F WITH HOOK]
				case '\u1D6E': // ᵮ  [LATIN SMALL LETTER F WITH MIDDLE TILDE]
				case '\u1D82': // ᶂ  [LATIN SMALL LETTER F WITH PALATAL HOOK]
				case '\u1E1F': // ḟ  [LATIN SMALL LETTER F WITH DOT ABOVE]
				case '\u1E9B': // ẛ  [LATIN SMALL LETTER LONG S WITH DOT ABOVE]
				case '\u24D5': // ⓕ  [CIRCLED LATIN SMALL LETTER F]
				case '\uA77C': // ꝼ  [LATIN SMALL LETTER INSULAR F]
				case '\uFF46': // ｆ  [FULLWIDTH LATIN SMALL LETTER F]
					output[outputPos++] = 'f';
					break;
				case '\u24A1': // ⒡  [PARENTHESIZED LATIN SMALL LETTER F]
					output[outputPos++] = '(';
					output[outputPos++] = 'f';
					output[outputPos++] = ')';
					break;
				case '\uFB00': // ﬀ  [LATIN SMALL LIGATURE FF]
					output[outputPos++] = 'f';
					output[outputPos++] = 'f';
					break;
				case '\uFB03': // ﬃ  [LATIN SMALL LIGATURE FFI]
					output[outputPos++] = 'f';
					output[outputPos++] = 'f';
					output[outputPos++] = 'i';
					break;
				case '\uFB04': // ﬄ  [LATIN SMALL LIGATURE FFL]
					output[outputPos++] = 'f';
					output[outputPos++] = 'f';
					output[outputPos++] = 'l';
					break;
				case '\uFB01': // ﬁ  [LATIN SMALL LIGATURE FI]
					output[outputPos++] = 'f';
					output[outputPos++] = 'i';
					break;
				case '\uFB02': // ﬂ  [LATIN SMALL LIGATURE FL]
					output[outputPos++] = 'f';
					output[outputPos++] = 'l';
					break;
				case '\u011C': // Ĝ  [LATIN CAPITAL LETTER G WITH CIRCUMFLEX]
				case '\u011E': // Ğ  [LATIN CAPITAL LETTER G WITH BREVE]
				case '\u0120': // Ġ  [LATIN CAPITAL LETTER G WITH DOT ABOVE]
				case '\u0122': // Ģ  [LATIN CAPITAL LETTER G WITH CEDILLA]
				case '\u0193': // Ɠ  [LATIN CAPITAL LETTER G WITH HOOK]
				case '\u01E4': // Ǥ  [LATIN CAPITAL LETTER G WITH STROKE]
				case '\u01E5': // ǥ  [LATIN SMALL LETTER G WITH STROKE]
				case '\u01E6': // Ǧ  [LATIN CAPITAL LETTER G WITH CARON]
				case '\u01E7': // ǧ  [LATIN SMALL LETTER G WITH CARON]
				case '\u01F4': // Ǵ  [LATIN CAPITAL LETTER G WITH ACUTE]
				case '\u0262': // ɢ  [LATIN LETTER SMALL CAPITAL G]
				case '\u029B': // ʛ  [LATIN LETTER SMALL CAPITAL G WITH HOOK]
				case '\u1E20': // Ḡ  [LATIN CAPITAL LETTER G WITH MACRON]
				case '\u24BC': // Ⓖ  [CIRCLED LATIN CAPITAL LETTER G]
				case '\uA77D': // Ᵹ  [LATIN CAPITAL LETTER INSULAR G]
				case '\uA77E': // Ꝿ  [LATIN CAPITAL LETTER TURNED INSULAR G]
				case '\uFF27': // Ｇ  [FULLWIDTH LATIN CAPITAL LETTER G]
					output[outputPos++] = 'G';
					break;
				case '\u011D': // ĝ  [LATIN SMALL LETTER G WITH CIRCUMFLEX]
				case '\u011F': // ğ  [LATIN SMALL LETTER G WITH BREVE]
				case '\u0121': // ġ  [LATIN SMALL LETTER G WITH DOT ABOVE]
				case '\u0123': // ģ  [LATIN SMALL LETTER G WITH CEDILLA]
				case '\u01F5': // ǵ  [LATIN SMALL LETTER G WITH ACUTE]
				case '\u0260': // ɠ  [LATIN SMALL LETTER G WITH HOOK]
				case '\u0261': // ɡ  [LATIN SMALL LETTER SCRIPT G]
				case '\u1D77': // ᵷ  [LATIN SMALL LETTER TURNED G]
				case '\u1D79': // ᵹ  [LATIN SMALL LETTER INSULAR G]
				case '\u1D83': // ᶃ  [LATIN SMALL LETTER G WITH PALATAL HOOK]
				case '\u1E21': // ḡ  [LATIN SMALL LETTER G WITH MACRON]
				case '\u24D6': // ⓖ  [CIRCLED LATIN SMALL LETTER G]
				case '\uA77F': // ꝿ  [LATIN SMALL LETTER TURNED INSULAR G]
				case '\uFF47': // ｇ  [FULLWIDTH LATIN SMALL LETTER G]
					output[outputPos++] = 'g';
					break;
				case '\u24A2': // ⒢  [PARENTHESIZED LATIN SMALL LETTER G]
					output[outputPos++] = '(';
					output[outputPos++] = 'g';
					output[outputPos++] = ')';
					break;
				case '\u0124': // Ĥ  [LATIN CAPITAL LETTER H WITH CIRCUMFLEX]
				case '\u0126': // Ħ  [LATIN CAPITAL LETTER H WITH STROKE]
				case '\u021E': // Ȟ  [LATIN CAPITAL LETTER H WITH CARON]
				case '\u029C': // ʜ  [LATIN LETTER SMALL CAPITAL H]
				case '\u1E22': // Ḣ  [LATIN CAPITAL LETTER H WITH DOT ABOVE]
				case '\u1E24': // Ḥ  [LATIN CAPITAL LETTER H WITH DOT BELOW]
				case '\u1E26': // Ḧ  [LATIN CAPITAL LETTER H WITH DIAERESIS]
				case '\u1E28': // Ḩ  [LATIN CAPITAL LETTER H WITH CEDILLA]
				case '\u1E2A': // Ḫ  [LATIN CAPITAL LETTER H WITH BREVE BELOW]
				case '\u24BD': // Ⓗ  [CIRCLED LATIN CAPITAL LETTER H]
				case '\u2C67': // Ⱨ  [LATIN CAPITAL LETTER H WITH DESCENDER]
				case '\u2C75': // Ⱶ  [LATIN CAPITAL LETTER HALF H]
				case '\uFF28': // Ｈ  [FULLWIDTH LATIN CAPITAL LETTER H]
					output[outputPos++] = 'H';
					break;
				case '\u0125': // ĥ  [LATIN SMALL LETTER H WITH CIRCUMFLEX]
				case '\u0127': // ħ  [LATIN SMALL LETTER H WITH STROKE]
				case '\u021F': // ȟ  [LATIN SMALL LETTER H WITH CARON]
				case '\u0265': // ɥ  [LATIN SMALL LETTER TURNED H]
				case '\u0266': // ɦ  [LATIN SMALL LETTER H WITH HOOK]
				case '\u02AE': // ʮ  [LATIN SMALL LETTER TURNED H WITH FISHHOOK]
				case '\u02AF': // ʯ  [LATIN SMALL LETTER TURNED H WITH FISHHOOK AND TAIL]
				case '\u1E23': // ḣ  [LATIN SMALL LETTER H WITH DOT ABOVE]
				case '\u1E25': // ḥ  [LATIN SMALL LETTER H WITH DOT BELOW]
				case '\u1E27': // ḧ  [LATIN SMALL LETTER H WITH DIAERESIS]
				case '\u1E29': // ḩ  [LATIN SMALL LETTER H WITH CEDILLA]
				case '\u1E2B': // ḫ  [LATIN SMALL LETTER H WITH BREVE BELOW]
				case '\u1E96': // ẖ  [LATIN SMALL LETTER H WITH LINE BELOW]
				case '\u24D7': // ⓗ  [CIRCLED LATIN SMALL LETTER H]
				case '\u2C68': // ⱨ  [LATIN SMALL LETTER H WITH DESCENDER]
				case '\u2C76': // ⱶ  [LATIN SMALL LETTER HALF H]
				case '\uFF48': // ｈ  [FULLWIDTH LATIN SMALL LETTER H]
					output[outputPos++] = 'h';
					break;
				case '\u01F6': // Ƕ  http://en.wikipedia.org/wiki/Hwair  [LATIN CAPITAL LETTER HWAIR]
					output[outputPos++] = 'H';
					output[outputPos++] = 'V';
					break;
				case '\u24A3': // ⒣  [PARENTHESIZED LATIN SMALL LETTER H]
					output[outputPos++] = '(';
					output[outputPos++] = 'h';
					output[outputPos++] = ')';
					break;
				case '\u0195': // ƕ  [LATIN SMALL LETTER HV]
					output[outputPos++] = 'h';
					output[outputPos++] = 'v';
					break;
				case '\u00CC': // Ì  [LATIN CAPITAL LETTER I WITH GRAVE]
				case '\u00CD': // Í  [LATIN CAPITAL LETTER I WITH ACUTE]
				case '\u00CE': // Î  [LATIN CAPITAL LETTER I WITH CIRCUMFLEX]
				case '\u00CF': // Ï  [LATIN CAPITAL LETTER I WITH DIAERESIS]
				case '\u0128': // Ĩ  [LATIN CAPITAL LETTER I WITH TILDE]
				case '\u012A': // Ī  [LATIN CAPITAL LETTER I WITH MACRON]
				case '\u012C': // Ĭ  [LATIN CAPITAL LETTER I WITH BREVE]
				case '\u012E': // Į  [LATIN CAPITAL LETTER I WITH OGONEK]
				case '\u0130': // İ  [LATIN CAPITAL LETTER I WITH DOT ABOVE]
				case '\u0196': // Ɩ  [LATIN CAPITAL LETTER IOTA]
				case '\u0197': // Ɨ  [LATIN CAPITAL LETTER I WITH STROKE]
				case '\u01CF': // Ǐ  [LATIN CAPITAL LETTER I WITH CARON]
				case '\u0208': // Ȉ  [LATIN CAPITAL LETTER I WITH DOUBLE GRAVE]
				case '\u020A': // Ȋ  [LATIN CAPITAL LETTER I WITH INVERTED BREVE]
				case '\u026A': // ɪ  [LATIN LETTER SMALL CAPITAL I]
				case '\u1D7B': // ᵻ  [LATIN SMALL CAPITAL LETTER I WITH STROKE]
				case '\u1E2C': // Ḭ  [LATIN CAPITAL LETTER I WITH TILDE BELOW]
				case '\u1E2E': // Ḯ  [LATIN CAPITAL LETTER I WITH DIAERESIS AND ACUTE]
				case '\u1EC8': // Ỉ  [LATIN CAPITAL LETTER I WITH HOOK ABOVE]
				case '\u1ECA': // Ị  [LATIN CAPITAL LETTER I WITH DOT BELOW]
				case '\u24BE': // Ⓘ  [CIRCLED LATIN CAPITAL LETTER I]
				case '\uA7FE': // ꟾ  [LATIN EPIGRAPHIC LETTER I LONGA]
				case '\uFF29': // Ｉ  [FULLWIDTH LATIN CAPITAL LETTER I]
					output[outputPos++] = 'I';
					break;
				case '\u00EC': // ì  [LATIN SMALL LETTER I WITH GRAVE]
				case '\u00ED': // í  [LATIN SMALL LETTER I WITH ACUTE]
				case '\u00EE': // î  [LATIN SMALL LETTER I WITH CIRCUMFLEX]
				case '\u00EF': // ï  [LATIN SMALL LETTER I WITH DIAERESIS]
				case '\u0129': // ĩ  [LATIN SMALL LETTER I WITH TILDE]
				case '\u012B': // ī  [LATIN SMALL LETTER I WITH MACRON]
				case '\u012D': // ĭ  [LATIN SMALL LETTER I WITH BREVE]
				case '\u012F': // į  [LATIN SMALL LETTER I WITH OGONEK]
				case '\u0131': // ı  [LATIN SMALL LETTER DOTLESS I]
				case '\u01D0': // ǐ  [LATIN SMALL LETTER I WITH CARON]
				case '\u0209': // ȉ  [LATIN SMALL LETTER I WITH DOUBLE GRAVE]
				case '\u020B': // ȋ  [LATIN SMALL LETTER I WITH INVERTED BREVE]
				case '\u0268': // ɨ  [LATIN SMALL LETTER I WITH STROKE]
				case '\u1D09': // ᴉ  [LATIN SMALL LETTER TURNED I]
				case '\u1D62': // ᵢ  [LATIN SUBSCRIPT SMALL LETTER I]
				case '\u1D7C': // ᵼ  [LATIN SMALL LETTER IOTA WITH STROKE]
				case '\u1D96': // ᶖ  [LATIN SMALL LETTER I WITH RETROFLEX HOOK]
				case '\u1E2D': // ḭ  [LATIN SMALL LETTER I WITH TILDE BELOW]
				case '\u1E2F': // ḯ  [LATIN SMALL LETTER I WITH DIAERESIS AND ACUTE]
				case '\u1EC9': // ỉ  [LATIN SMALL LETTER I WITH HOOK ABOVE]
				case '\u1ECB': // ị  [LATIN SMALL LETTER I WITH DOT BELOW]
				case '\u2071': // ⁱ  [SUPERSCRIPT LATIN SMALL LETTER I]
				case '\u24D8': // ⓘ  [CIRCLED LATIN SMALL LETTER I]
				case '\uFF49': // ｉ  [FULLWIDTH LATIN SMALL LETTER I]
					output[outputPos++] = 'i';
					break;
				case '\u0132': // Ĳ  [LATIN CAPITAL LIGATURE IJ]
					output[outputPos++] = 'I';
					output[outputPos++] = 'J';
					break;
				case '\u24A4': // ⒤  [PARENTHESIZED LATIN SMALL LETTER I]
					output[outputPos++] = '(';
					output[outputPos++] = 'i';
					output[outputPos++] = ')';
					break;
				case '\u0133': // ĳ  [LATIN SMALL LIGATURE IJ]
					output[outputPos++] = 'i';
					output[outputPos++] = 'j';
					break;
				case '\u0134': // Ĵ  [LATIN CAPITAL LETTER J WITH CIRCUMFLEX]
				case '\u0248': // Ɉ  [LATIN CAPITAL LETTER J WITH STROKE]
				case '\u1D0A': // ᴊ  [LATIN LETTER SMALL CAPITAL J]
				case '\u24BF': // Ⓙ  [CIRCLED LATIN CAPITAL LETTER J]
				case '\uFF2A': // Ｊ  [FULLWIDTH LATIN CAPITAL LETTER J]
					output[outputPos++] = 'J';
					break;
				case '\u0135': // ĵ  [LATIN SMALL LETTER J WITH CIRCUMFLEX]
				case '\u01F0': // ǰ  [LATIN SMALL LETTER J WITH CARON]
				case '\u0237': // ȷ  [LATIN SMALL LETTER DOTLESS J]
				case '\u0249': // ɉ  [LATIN SMALL LETTER J WITH STROKE]
				case '\u025F': // ɟ  [LATIN SMALL LETTER DOTLESS J WITH STROKE]
				case '\u0284': // ʄ  [LATIN SMALL LETTER DOTLESS J WITH STROKE AND HOOK]
				case '\u029D': // ʝ  [LATIN SMALL LETTER J WITH CROSSED-TAIL]
				case '\u24D9': // ⓙ  [CIRCLED LATIN SMALL LETTER J]
				case '\u2C7C': // ⱼ  [LATIN SUBSCRIPT SMALL LETTER J]
				case '\uFF4A': // ｊ  [FULLWIDTH LATIN SMALL LETTER J]
					output[outputPos++] = 'j';
					break;
				case '\u24A5': // ⒥  [PARENTHESIZED LATIN SMALL LETTER J]
					output[outputPos++] = '(';
					output[outputPos++] = 'j';
					output[outputPos++] = ')';
					break;
				case '\u0136': // Ķ  [LATIN CAPITAL LETTER K WITH CEDILLA]
				case '\u0198': // Ƙ  [LATIN CAPITAL LETTER K WITH HOOK]
				case '\u01E8': // Ǩ  [LATIN CAPITAL LETTER K WITH CARON]
				case '\u1D0B': // ᴋ  [LATIN LETTER SMALL CAPITAL K]
				case '\u1E30': // Ḱ  [LATIN CAPITAL LETTER K WITH ACUTE]
				case '\u1E32': // Ḳ  [LATIN CAPITAL LETTER K WITH DOT BELOW]
				case '\u1E34': // Ḵ  [LATIN CAPITAL LETTER K WITH LINE BELOW]
				case '\u24C0': // Ⓚ  [CIRCLED LATIN CAPITAL LETTER K]
				case '\u2C69': // Ⱪ  [LATIN CAPITAL LETTER K WITH DESCENDER]
				case '\uA740': // Ꝁ  [LATIN CAPITAL LETTER K WITH STROKE]
				case '\uA742': // Ꝃ  [LATIN CAPITAL LETTER K WITH DIAGONAL STROKE]
				case '\uA744': // Ꝅ  [LATIN CAPITAL LETTER K WITH STROKE AND DIAGONAL STROKE]
				case '\uFF2B': // Ｋ  [FULLWIDTH LATIN CAPITAL LETTER K]
					output[outputPos++] = 'K';
					break;
				case '\u0137': // ķ  [LATIN SMALL LETTER K WITH CEDILLA]
				case '\u0199': // ƙ  [LATIN SMALL LETTER K WITH HOOK]
				case '\u01E9': // ǩ  [LATIN SMALL LETTER K WITH CARON]
				case '\u029E': // ʞ  [LATIN SMALL LETTER TURNED K]
				case '\u1D84': // ᶄ  [LATIN SMALL LETTER K WITH PALATAL HOOK]
				case '\u1E31': // ḱ  [LATIN SMALL LETTER K WITH ACUTE]
				case '\u1E33': // ḳ  [LATIN SMALL LETTER K WITH DOT BELOW]
				case '\u1E35': // ḵ  [LATIN SMALL LETTER K WITH LINE BELOW]
				case '\u24DA': // ⓚ  [CIRCLED LATIN SMALL LETTER K]
				case '\u2C6A': // ⱪ  [LATIN SMALL LETTER K WITH DESCENDER]
				case '\uA741': // ꝁ  [LATIN SMALL LETTER K WITH STROKE]
				case '\uA743': // ꝃ  [LATIN SMALL LETTER K WITH DIAGONAL STROKE]
				case '\uA745': // ꝅ  [LATIN SMALL LETTER K WITH STROKE AND DIAGONAL STROKE]
				case '\uFF4B': // ｋ  [FULLWIDTH LATIN SMALL LETTER K]
					output[outputPos++] = 'k';
					break;
				case '\u24A6': // ⒦  [PARENTHESIZED LATIN SMALL LETTER K]
					output[outputPos++] = '(';
					output[outputPos++] = 'k';
					output[outputPos++] = ')';
					break;
				case '\u0139': // Ĺ  [LATIN CAPITAL LETTER L WITH ACUTE]
				case '\u013B': // Ļ  [LATIN CAPITAL LETTER L WITH CEDILLA]
				case '\u013D': // Ľ  [LATIN CAPITAL LETTER L WITH CARON]
				case '\u013F': // Ŀ  [LATIN CAPITAL LETTER L WITH MIDDLE DOT]
				case '\u0141': // Ł  [LATIN CAPITAL LETTER L WITH STROKE]
				case '\u023D': // Ƚ  [LATIN CAPITAL LETTER L WITH BAR]
				case '\u029F': // ʟ  [LATIN LETTER SMALL CAPITAL L]
				case '\u1D0C': // ᴌ  [LATIN LETTER SMALL CAPITAL L WITH STROKE]
				case '\u1E36': // Ḷ  [LATIN CAPITAL LETTER L WITH DOT BELOW]
				case '\u1E38': // Ḹ  [LATIN CAPITAL LETTER L WITH DOT BELOW AND MACRON]
				case '\u1E3A': // Ḻ  [LATIN CAPITAL LETTER L WITH LINE BELOW]
				case '\u1E3C': // Ḽ  [LATIN CAPITAL LETTER L WITH CIRCUMFLEX BELOW]
				case '\u24C1': // Ⓛ  [CIRCLED LATIN CAPITAL LETTER L]
				case '\u2C60': // Ⱡ  [LATIN CAPITAL LETTER L WITH DOUBLE BAR]
				case '\u2C62': // Ɫ  [LATIN CAPITAL LETTER L WITH MIDDLE TILDE]
				case '\uA746': // Ꝇ  [LATIN CAPITAL LETTER BROKEN L]
				case '\uA748': // Ꝉ  [LATIN CAPITAL LETTER L WITH HIGH STROKE]
				case '\uA780': // Ꞁ  [LATIN CAPITAL LETTER TURNED L]
				case '\uFF2C': // Ｌ  [FULLWIDTH LATIN CAPITAL LETTER L]
					output[outputPos++] = 'L';
					break;
				case '\u013A': // ĺ  [LATIN SMALL LETTER L WITH ACUTE]
				case '\u013C': // ļ  [LATIN SMALL LETTER L WITH CEDILLA]
				case '\u013E': // ľ  [LATIN SMALL LETTER L WITH CARON]
				case '\u0140': // ŀ  [LATIN SMALL LETTER L WITH MIDDLE DOT]
				case '\u0142': // ł  [LATIN SMALL LETTER L WITH STROKE]
				case '\u019A': // ƚ  [LATIN SMALL LETTER L WITH BAR]
				case '\u0234': // ȴ  [LATIN SMALL LETTER L WITH CURL]
				case '\u026B': // ɫ  [LATIN SMALL LETTER L WITH MIDDLE TILDE]
				case '\u026C': // ɬ  [LATIN SMALL LETTER L WITH BELT]
				case '\u026D': // ɭ  [LATIN SMALL LETTER L WITH RETROFLEX HOOK]
				case '\u1D85': // ᶅ  [LATIN SMALL LETTER L WITH PALATAL HOOK]
				case '\u1E37': // ḷ  [LATIN SMALL LETTER L WITH DOT BELOW]
				case '\u1E39': // ḹ  [LATIN SMALL LETTER L WITH DOT BELOW AND MACRON]
				case '\u1E3B': // ḻ  [LATIN SMALL LETTER L WITH LINE BELOW]
				case '\u1E3D': // ḽ  [LATIN SMALL LETTER L WITH CIRCUMFLEX BELOW]
				case '\u24DB': // ⓛ  [CIRCLED LATIN SMALL LETTER L]
				case '\u2C61': // ⱡ  [LATIN SMALL LETTER L WITH DOUBLE BAR]
				case '\uA747': // ꝇ  [LATIN SMALL LETTER BROKEN L]
				case '\uA749': // ꝉ  [LATIN SMALL LETTER L WITH HIGH STROKE]
				case '\uA781': // ꞁ  [LATIN SMALL LETTER TURNED L]
				case '\uFF4C': // ｌ  [FULLWIDTH LATIN SMALL LETTER L]
					output[outputPos++] = 'l';
					break;
				case '\u01C7': // Ǉ  [LATIN CAPITAL LETTER LJ]
					output[outputPos++] = 'L';
					output[outputPos++] = 'J';
					break;
				case '\u1EFA': // Ỻ  [LATIN CAPITAL LETTER MIDDLE-WELSH LL]
					output[outputPos++] = 'L';
					output[outputPos++] = 'L';
					break;
				case '\u01C8': // ǈ  [LATIN CAPITAL LETTER L WITH SMALL LETTER J]
					output[outputPos++] = 'L';
					output[outputPos++] = 'j';
					break;
				case '\u24A7': // ⒧  [PARENTHESIZED LATIN SMALL LETTER L]
					output[outputPos++] = '(';
					output[outputPos++] = 'l';
					output[outputPos++] = ')';
					break;
				case '\u01C9': // ǉ  [LATIN SMALL LETTER LJ]
					output[outputPos++] = 'l';
					output[outputPos++] = 'j';
					break;
				case '\u1EFB': // ỻ  [LATIN SMALL LETTER MIDDLE-WELSH LL]
					output[outputPos++] = 'l';
					output[outputPos++] = 'l';
					break;
				case '\u02AA': // ʪ  [LATIN SMALL LETTER LS DIGRAPH]
					output[outputPos++] = 'l';
					output[outputPos++] = 's';
					break;
				case '\u02AB': // ʫ  [LATIN SMALL LETTER LZ DIGRAPH]
					output[outputPos++] = 'l';
					output[outputPos++] = 'z';
					break;
				case '\u019C': // Ɯ  [LATIN CAPITAL LETTER TURNED M]
				case '\u1D0D': // ᴍ  [LATIN LETTER SMALL CAPITAL M]
				case '\u1E3E': // Ḿ  [LATIN CAPITAL LETTER M WITH ACUTE]
				case '\u1E40': // Ṁ  [LATIN CAPITAL LETTER M WITH DOT ABOVE]
				case '\u1E42': // Ṃ  [LATIN CAPITAL LETTER M WITH DOT BELOW]
				case '\u24C2': // Ⓜ  [CIRCLED LATIN CAPITAL LETTER M]
				case '\u2C6E': // Ɱ  [LATIN CAPITAL LETTER M WITH HOOK]
				case '\uA7FD': // ꟽ  [LATIN EPIGRAPHIC LETTER INVERTED M]
				case '\uA7FF': // ꟿ  [LATIN EPIGRAPHIC LETTER ARCHAIC M]
				case '\uFF2D': // Ｍ  [FULLWIDTH LATIN CAPITAL LETTER M]
					output[outputPos++] = 'M';
					break;
				case '\u026F': // ɯ  [LATIN SMALL LETTER TURNED M]
				case '\u0270': // ɰ  [LATIN SMALL LETTER TURNED M WITH LONG LEG]
				case '\u0271': // ɱ  [LATIN SMALL LETTER M WITH HOOK]
				case '\u1D6F': // ᵯ  [LATIN SMALL LETTER M WITH MIDDLE TILDE]
				case '\u1D86': // ᶆ  [LATIN SMALL LETTER M WITH PALATAL HOOK]
				case '\u1E3F': // ḿ  [LATIN SMALL LETTER M WITH ACUTE]
				case '\u1E41': // ṁ  [LATIN SMALL LETTER M WITH DOT ABOVE]
				case '\u1E43': // ṃ  [LATIN SMALL LETTER M WITH DOT BELOW]
				case '\u24DC': // ⓜ  [CIRCLED LATIN SMALL LETTER M]
				case '\uFF4D': // ｍ  [FULLWIDTH LATIN SMALL LETTER M]
					output[outputPos++] = 'm';
					break;
				case '\u24A8': // ⒨  [PARENTHESIZED LATIN SMALL LETTER M]
					output[outputPos++] = '(';
					output[outputPos++] = 'm';
					output[outputPos++] = ')';
					break;
				case '\u00D1': // Ñ  [LATIN CAPITAL LETTER N WITH TILDE]
				case '\u0143': // Ń  [LATIN CAPITAL LETTER N WITH ACUTE]
				case '\u0145': // Ņ  [LATIN CAPITAL LETTER N WITH CEDILLA]
				case '\u0147': // Ň  [LATIN CAPITAL LETTER N WITH CARON]
				case '\u014A': // Ŋ  http://en.wikipedia.org/wiki/Eng_(letter)  [LATIN CAPITAL LETTER ENG]
				case '\u019D': // Ɲ  [LATIN CAPITAL LETTER N WITH LEFT HOOK]
				case '\u01F8': // Ǹ  [LATIN CAPITAL LETTER N WITH GRAVE]
				case '\u0220': // Ƞ  [LATIN CAPITAL LETTER N WITH LONG RIGHT LEG]
				case '\u0274': // ɴ  [LATIN LETTER SMALL CAPITAL N]
				case '\u1D0E': // ᴎ  [LATIN LETTER SMALL CAPITAL REVERSED N]
				case '\u1E44': // Ṅ  [LATIN CAPITAL LETTER N WITH DOT ABOVE]
				case '\u1E46': // Ṇ  [LATIN CAPITAL LETTER N WITH DOT BELOW]
				case '\u1E48': // Ṉ  [LATIN CAPITAL LETTER N WITH LINE BELOW]
				case '\u1E4A': // Ṋ  [LATIN CAPITAL LETTER N WITH CIRCUMFLEX BELOW]
				case '\u24C3': // Ⓝ  [CIRCLED LATIN CAPITAL LETTER N]
				case '\uFF2E': // Ｎ  [FULLWIDTH LATIN CAPITAL LETTER N]
					output[outputPos++] = 'N';
					break;
				case '\u00F1': // ñ  [LATIN SMALL LETTER N WITH TILDE]
				case '\u0144': // ń  [LATIN SMALL LETTER N WITH ACUTE]
				case '\u0146': // ņ  [LATIN SMALL LETTER N WITH CEDILLA]
				case '\u0148': // ň  [LATIN SMALL LETTER N WITH CARON]
				case '\u0149': // ŉ  [LATIN SMALL LETTER N PRECEDED BY APOSTROPHE]
				case '\u014B': // ŋ  http://en.wikipedia.org/wiki/Eng_(letter)  [LATIN SMALL LETTER ENG]
				case '\u019E': // ƞ  [LATIN SMALL LETTER N WITH LONG RIGHT LEG]
				case '\u01F9': // ǹ  [LATIN SMALL LETTER N WITH GRAVE]
				case '\u0235': // ȵ  [LATIN SMALL LETTER N WITH CURL]
				case '\u0272': // ɲ  [LATIN SMALL LETTER N WITH LEFT HOOK]
				case '\u0273': // ɳ  [LATIN SMALL LETTER N WITH RETROFLEX HOOK]
				case '\u1D70': // ᵰ  [LATIN SMALL LETTER N WITH MIDDLE TILDE]
				case '\u1D87': // ᶇ  [LATIN SMALL LETTER N WITH PALATAL HOOK]
				case '\u1E45': // ṅ  [LATIN SMALL LETTER N WITH DOT ABOVE]
				case '\u1E47': // ṇ  [LATIN SMALL LETTER N WITH DOT BELOW]
				case '\u1E49': // ṉ  [LATIN SMALL LETTER N WITH LINE BELOW]
				case '\u1E4B': // ṋ  [LATIN SMALL LETTER N WITH CIRCUMFLEX BELOW]
				case '\u207F': // ⁿ  [SUPERSCRIPT LATIN SMALL LETTER N]
				case '\u24DD': // ⓝ  [CIRCLED LATIN SMALL LETTER N]
				case '\uFF4E': // ｎ  [FULLWIDTH LATIN SMALL LETTER N]
					output[outputPos++] = 'n';
					break;
				case '\u01CA': // Ǌ  [LATIN CAPITAL LETTER NJ]
					output[outputPos++] = 'N';
					output[outputPos++] = 'J';
					break;
				case '\u01CB': // ǋ  [LATIN CAPITAL LETTER N WITH SMALL LETTER J]
					output[outputPos++] = 'N';
					output[outputPos++] = 'j';
					break;
				case '\u24A9': // ⒩  [PARENTHESIZED LATIN SMALL LETTER N]
					output[outputPos++] = '(';
					output[outputPos++] = 'n';
					output[outputPos++] = ')';
					break;
				case '\u01CC': // ǌ  [LATIN SMALL LETTER NJ]
					output[outputPos++] = 'n';
					output[outputPos++] = 'j';
					break;
				case '\u00D2': // Ò  [LATIN CAPITAL LETTER O WITH GRAVE]
				case '\u00D3': // Ó  [LATIN CAPITAL LETTER O WITH ACUTE]
				case '\u00D4': // Ô  [LATIN CAPITAL LETTER O WITH CIRCUMFLEX]
				case '\u00D5': // Õ  [LATIN CAPITAL LETTER O WITH TILDE]
				case '\u00D6': // Ö  [LATIN CAPITAL LETTER O WITH DIAERESIS]
				case '\u00D8': // Ø  [LATIN CAPITAL LETTER O WITH STROKE]
				case '\u014C': // Ō  [LATIN CAPITAL LETTER O WITH MACRON]
				case '\u014E': // Ŏ  [LATIN CAPITAL LETTER O WITH BREVE]
				case '\u0150': // Ő  [LATIN CAPITAL LETTER O WITH DOUBLE ACUTE]
				case '\u0186': // Ɔ  [LATIN CAPITAL LETTER OPEN O]
				case '\u019F': // Ɵ  [LATIN CAPITAL LETTER O WITH MIDDLE TILDE]
				case '\u01A0': // Ơ  [LATIN CAPITAL LETTER O WITH HORN]
				case '\u01D1': // Ǒ  [LATIN CAPITAL LETTER O WITH CARON]
				case '\u01EA': // Ǫ  [LATIN CAPITAL LETTER O WITH OGONEK]
				case '\u01EC': // Ǭ  [LATIN CAPITAL LETTER O WITH OGONEK AND MACRON]
				case '\u01FE': // Ǿ  [LATIN CAPITAL LETTER O WITH STROKE AND ACUTE]
				case '\u020C': // Ȍ  [LATIN CAPITAL LETTER O WITH DOUBLE GRAVE]
				case '\u020E': // Ȏ  [LATIN CAPITAL LETTER O WITH INVERTED BREVE]
				case '\u022A': // Ȫ  [LATIN CAPITAL LETTER O WITH DIAERESIS AND MACRON]
				case '\u022C': // Ȭ  [LATIN CAPITAL LETTER O WITH TILDE AND MACRON]
				case '\u022E': // Ȯ  [LATIN CAPITAL LETTER O WITH DOT ABOVE]
				case '\u0230': // Ȱ  [LATIN CAPITAL LETTER O WITH DOT ABOVE AND MACRON]
				case '\u1D0F': // ᴏ  [LATIN LETTER SMALL CAPITAL O]
				case '\u1D10': // ᴐ  [LATIN LETTER SMALL CAPITAL OPEN O]
				case '\u1E4C': // Ṍ  [LATIN CAPITAL LETTER O WITH TILDE AND ACUTE]
				case '\u1E4E': // Ṏ  [LATIN CAPITAL LETTER O WITH TILDE AND DIAERESIS]
				case '\u1E50': // Ṑ  [LATIN CAPITAL LETTER O WITH MACRON AND GRAVE]
				case '\u1E52': // Ṓ  [LATIN CAPITAL LETTER O WITH MACRON AND ACUTE]
				case '\u1ECC': // Ọ  [LATIN CAPITAL LETTER O WITH DOT BELOW]
				case '\u1ECE': // Ỏ  [LATIN CAPITAL LETTER O WITH HOOK ABOVE]
				case '\u1ED0': // Ố  [LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND ACUTE]
				case '\u1ED2': // Ồ  [LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND GRAVE]
				case '\u1ED4': // Ổ  [LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND HOOK ABOVE]
				case '\u1ED6': // Ỗ  [LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND TILDE]
				case '\u1ED8': // Ộ  [LATIN CAPITAL LETTER O WITH CIRCUMFLEX AND DOT BELOW]
				case '\u1EDA': // Ớ  [LATIN CAPITAL LETTER O WITH HORN AND ACUTE]
				case '\u1EDC': // Ờ  [LATIN CAPITAL LETTER O WITH HORN AND GRAVE]
				case '\u1EDE': // Ở  [LATIN CAPITAL LETTER O WITH HORN AND HOOK ABOVE]
				case '\u1EE0': // Ỡ  [LATIN CAPITAL LETTER O WITH HORN AND TILDE]
				case '\u1EE2': // Ợ  [LATIN CAPITAL LETTER O WITH HORN AND DOT BELOW]
				case '\u24C4': // Ⓞ  [CIRCLED LATIN CAPITAL LETTER O]
				case '\uA74A': // Ꝋ  [LATIN CAPITAL LETTER O WITH LONG STROKE OVERLAY]
				case '\uA74C': // Ꝍ  [LATIN CAPITAL LETTER O WITH LOOP]
				case '\uFF2F': // Ｏ  [FULLWIDTH LATIN CAPITAL LETTER O]
					output[outputPos++] = 'O';
					break;
				case '\u00F2': // ò  [LATIN SMALL LETTER O WITH GRAVE]
				case '\u00F3': // ó  [LATIN SMALL LETTER O WITH ACUTE]
				case '\u00F4': // ô  [LATIN SMALL LETTER O WITH CIRCUMFLEX]
				case '\u00F5': // õ  [LATIN SMALL LETTER O WITH TILDE]
				case '\u00F6': // ö  [LATIN SMALL LETTER O WITH DIAERESIS]
				case '\u00F8': // ø  [LATIN SMALL LETTER O WITH STROKE]
				case '\u014D': // ō  [LATIN SMALL LETTER O WITH MACRON]
				case '\u014F': // ŏ  [LATIN SMALL LETTER O WITH BREVE]
				case '\u0151': // ő  [LATIN SMALL LETTER O WITH DOUBLE ACUTE]
				case '\u01A1': // ơ  [LATIN SMALL LETTER O WITH HORN]
				case '\u01D2': // ǒ  [LATIN SMALL LETTER O WITH CARON]
				case '\u01EB': // ǫ  [LATIN SMALL LETTER O WITH OGONEK]
				case '\u01ED': // ǭ  [LATIN SMALL LETTER O WITH OGONEK AND MACRON]
				case '\u01FF': // ǿ  [LATIN SMALL LETTER O WITH STROKE AND ACUTE]
				case '\u020D': // ȍ  [LATIN SMALL LETTER O WITH DOUBLE GRAVE]
				case '\u020F': // ȏ  [LATIN SMALL LETTER O WITH INVERTED BREVE]
				case '\u022B': // ȫ  [LATIN SMALL LETTER O WITH DIAERESIS AND MACRON]
				case '\u022D': // ȭ  [LATIN SMALL LETTER O WITH TILDE AND MACRON]
				case '\u022F': // ȯ  [LATIN SMALL LETTER O WITH DOT ABOVE]
				case '\u0231': // ȱ  [LATIN SMALL LETTER O WITH DOT ABOVE AND MACRON]
				case '\u0254': // ɔ  [LATIN SMALL LETTER OPEN O]
				case '\u0275': // ɵ  [LATIN SMALL LETTER BARRED O]
				case '\u1D16': // ᴖ  [LATIN SMALL LETTER TOP HALF O]
				case '\u1D17': // ᴗ  [LATIN SMALL LETTER BOTTOM HALF O]
				case '\u1D97': // ᶗ  [LATIN SMALL LETTER OPEN O WITH RETROFLEX HOOK]
				case '\u1E4D': // ṍ  [LATIN SMALL LETTER O WITH TILDE AND ACUTE]
				case '\u1E4F': // ṏ  [LATIN SMALL LETTER O WITH TILDE AND DIAERESIS]
				case '\u1E51': // ṑ  [LATIN SMALL LETTER O WITH MACRON AND GRAVE]
				case '\u1E53': // ṓ  [LATIN SMALL LETTER O WITH MACRON AND ACUTE]
				case '\u1ECD': // ọ  [LATIN SMALL LETTER O WITH DOT BELOW]
				case '\u1ECF': // ỏ  [LATIN SMALL LETTER O WITH HOOK ABOVE]
				case '\u1ED1': // ố  [LATIN SMALL LETTER O WITH CIRCUMFLEX AND ACUTE]
				case '\u1ED3': // ồ  [LATIN SMALL LETTER O WITH CIRCUMFLEX AND GRAVE]
				case '\u1ED5': // ổ  [LATIN SMALL LETTER O WITH CIRCUMFLEX AND HOOK ABOVE]
				case '\u1ED7': // ỗ  [LATIN SMALL LETTER O WITH CIRCUMFLEX AND TILDE]
				case '\u1ED9': // ộ  [LATIN SMALL LETTER O WITH CIRCUMFLEX AND DOT BELOW]
				case '\u1EDB': // ớ  [LATIN SMALL LETTER O WITH HORN AND ACUTE]
				case '\u1EDD': // ờ  [LATIN SMALL LETTER O WITH HORN AND GRAVE]
				case '\u1EDF': // ở  [LATIN SMALL LETTER O WITH HORN AND HOOK ABOVE]
				case '\u1EE1': // ỡ  [LATIN SMALL LETTER O WITH HORN AND TILDE]
				case '\u1EE3': // ợ  [LATIN SMALL LETTER O WITH HORN AND DOT BELOW]
				case '\u2092': // ₒ  [LATIN SUBSCRIPT SMALL LETTER O]
				case '\u24DE': // ⓞ  [CIRCLED LATIN SMALL LETTER O]
				case '\u2C7A': // ⱺ  [LATIN SMALL LETTER O WITH LOW RING INSIDE]
				case '\uA74B': // ꝋ  [LATIN SMALL LETTER O WITH LONG STROKE OVERLAY]
				case '\uA74D': // ꝍ  [LATIN SMALL LETTER O WITH LOOP]
				case '\uFF4F': // ｏ  [FULLWIDTH LATIN SMALL LETTER O]
					output[outputPos++] = 'o';
					break;
				case '\u0152': // Œ  [LATIN CAPITAL LIGATURE OE]
				case '\u0276': // ɶ  [LATIN LETTER SMALL CAPITAL OE]
					output[outputPos++] = 'O';
					output[outputPos++] = 'E';
					break;
				case '\uA74E': // Ꝏ  [LATIN CAPITAL LETTER OO]
					output[outputPos++] = 'O';
					output[outputPos++] = 'O';
					break;
				case '\u0222': // Ȣ  http://en.wikipedia.org/wiki/OU  [LATIN CAPITAL LETTER OU]
				case '\u1D15': // ᴕ  [LATIN LETTER SMALL CAPITAL OU]
					output[outputPos++] = 'O';
					output[outputPos++] = 'U';
					break;
				case '\u24AA': // ⒪  [PARENTHESIZED LATIN SMALL LETTER O]
					output[outputPos++] = '(';
					output[outputPos++] = 'o';
					output[outputPos++] = ')';
					break;
				case '\u0153': // œ  [LATIN SMALL LIGATURE OE]
				case '\u1D14': // ᴔ  [LATIN SMALL LETTER TURNED OE]
					output[outputPos++] = 'o';
					output[outputPos++] = 'e';
					break;
				case '\uA74F': // ꝏ  [LATIN SMALL LETTER OO]
					output[outputPos++] = 'o';
					output[outputPos++] = 'o';
					break;
				case '\u0223': // ȣ  http://en.wikipedia.org/wiki/OU  [LATIN SMALL LETTER OU]
					output[outputPos++] = 'o';
					output[outputPos++] = 'u';
					break;
				case '\u01A4': // Ƥ  [LATIN CAPITAL LETTER P WITH HOOK]
				case '\u1D18': // ᴘ  [LATIN LETTER SMALL CAPITAL P]
				case '\u1E54': // Ṕ  [LATIN CAPITAL LETTER P WITH ACUTE]
				case '\u1E56': // Ṗ  [LATIN CAPITAL LETTER P WITH DOT ABOVE]
				case '\u24C5': // Ⓟ  [CIRCLED LATIN CAPITAL LETTER P]
				case '\u2C63': // Ᵽ  [LATIN CAPITAL LETTER P WITH STROKE]
				case '\uA750': // Ꝑ  [LATIN CAPITAL LETTER P WITH STROKE THROUGH DESCENDER]
				case '\uA752': // Ꝓ  [LATIN CAPITAL LETTER P WITH FLOURISH]
				case '\uA754': // Ꝕ  [LATIN CAPITAL LETTER P WITH SQUIRREL TAIL]
				case '\uFF30': // Ｐ  [FULLWIDTH LATIN CAPITAL LETTER P]
					output[outputPos++] = 'P';
					break;
				case '\u01A5': // ƥ  [LATIN SMALL LETTER P WITH HOOK]
				case '\u1D71': // ᵱ  [LATIN SMALL LETTER P WITH MIDDLE TILDE]
				case '\u1D7D': // ᵽ  [LATIN SMALL LETTER P WITH STROKE]
				case '\u1D88': // ᶈ  [LATIN SMALL LETTER P WITH PALATAL HOOK]
				case '\u1E55': // ṕ  [LATIN SMALL LETTER P WITH ACUTE]
				case '\u1E57': // ṗ  [LATIN SMALL LETTER P WITH DOT ABOVE]
				case '\u24DF': // ⓟ  [CIRCLED LATIN SMALL LETTER P]
				case '\uA751': // ꝑ  [LATIN SMALL LETTER P WITH STROKE THROUGH DESCENDER]
				case '\uA753': // ꝓ  [LATIN SMALL LETTER P WITH FLOURISH]
				case '\uA755': // ꝕ  [LATIN SMALL LETTER P WITH SQUIRREL TAIL]
				case '\uA7FC': // ꟼ  [LATIN EPIGRAPHIC LETTER REVERSED P]
				case '\uFF50': // ｐ  [FULLWIDTH LATIN SMALL LETTER P]
					output[outputPos++] = 'p';
					break;
				case '\u24AB': // ⒫  [PARENTHESIZED LATIN SMALL LETTER P]
					output[outputPos++] = '(';
					output[outputPos++] = 'p';
					output[outputPos++] = ')';
					break;
				case '\u024A': // Ɋ  [LATIN CAPITAL LETTER SMALL Q WITH HOOK TAIL]
				case '\u24C6': // Ⓠ  [CIRCLED LATIN CAPITAL LETTER Q]
				case '\uA756': // Ꝗ  [LATIN CAPITAL LETTER Q WITH STROKE THROUGH DESCENDER]
				case '\uA758': // Ꝙ  [LATIN CAPITAL LETTER Q WITH DIAGONAL STROKE]
				case '\uFF31': // Ｑ  [FULLWIDTH LATIN CAPITAL LETTER Q]
					output[outputPos++] = 'Q';
					break;
				case '\u0138': // ĸ  http://en.wikipedia.org/wiki/Kra_(letter)  [LATIN SMALL LETTER KRA]
				case '\u024B': // ɋ  [LATIN SMALL LETTER Q WITH HOOK TAIL]
				case '\u02A0': // ʠ  [LATIN SMALL LETTER Q WITH HOOK]
				case '\u24E0': // ⓠ  [CIRCLED LATIN SMALL LETTER Q]
				case '\uA757': // ꝗ  [LATIN SMALL LETTER Q WITH STROKE THROUGH DESCENDER]
				case '\uA759': // ꝙ  [LATIN SMALL LETTER Q WITH DIAGONAL STROKE]
				case '\uFF51': // ｑ  [FULLWIDTH LATIN SMALL LETTER Q]
					output[outputPos++] = 'q';
					break;
				case '\u24AC': // ⒬  [PARENTHESIZED LATIN SMALL LETTER Q]
					output[outputPos++] = '(';
					output[outputPos++] = 'q';
					output[outputPos++] = ')';
					break;
				case '\u0239': // ȹ  [LATIN SMALL LETTER QP DIGRAPH]
					output[outputPos++] = 'q';
					output[outputPos++] = 'p';
					break;
				case '\u0154': // Ŕ  [LATIN CAPITAL LETTER R WITH ACUTE]
				case '\u0156': // Ŗ  [LATIN CAPITAL LETTER R WITH CEDILLA]
				case '\u0158': // Ř  [LATIN CAPITAL LETTER R WITH CARON]
				case '\u0210': // Ȓ  [LATIN CAPITAL LETTER R WITH DOUBLE GRAVE]
				case '\u0212': // Ȓ  [LATIN CAPITAL LETTER R WITH INVERTED BREVE]
				case '\u024C': // Ɍ  [LATIN CAPITAL LETTER R WITH STROKE]
				case '\u0280': // ʀ  [LATIN LETTER SMALL CAPITAL R]
				case '\u0281': // ʁ  [LATIN LETTER SMALL CAPITAL INVERTED R]
				case '\u1D19': // ᴙ  [LATIN LETTER SMALL CAPITAL REVERSED R]
				case '\u1D1A': // ᴚ  [LATIN LETTER SMALL CAPITAL TURNED R]
				case '\u1E58': // Ṙ  [LATIN CAPITAL LETTER R WITH DOT ABOVE]
				case '\u1E5A': // Ṛ  [LATIN CAPITAL LETTER R WITH DOT BELOW]
				case '\u1E5C': // Ṝ  [LATIN CAPITAL LETTER R WITH DOT BELOW AND MACRON]
				case '\u1E5E': // Ṟ  [LATIN CAPITAL LETTER R WITH LINE BELOW]
				case '\u24C7': // Ⓡ  [CIRCLED LATIN CAPITAL LETTER R]
				case '\u2C64': // Ɽ  [LATIN CAPITAL LETTER R WITH TAIL]
				case '\uA75A': // Ꝛ  [LATIN CAPITAL LETTER R ROTUNDA]
				case '\uA782': // Ꞃ  [LATIN CAPITAL LETTER INSULAR R]
				case '\uFF32': // Ｒ  [FULLWIDTH LATIN CAPITAL LETTER R]
					output[outputPos++] = 'R';
					break;
				case '\u0155': // ŕ  [LATIN SMALL LETTER R WITH ACUTE]
				case '\u0157': // ŗ  [LATIN SMALL LETTER R WITH CEDILLA]
				case '\u0159': // ř  [LATIN SMALL LETTER R WITH CARON]
				case '\u0211': // ȑ  [LATIN SMALL LETTER R WITH DOUBLE GRAVE]
				case '\u0213': // ȓ  [LATIN SMALL LETTER R WITH INVERTED BREVE]
				case '\u024D': // ɍ  [LATIN SMALL LETTER R WITH STROKE]
				case '\u027C': // ɼ  [LATIN SMALL LETTER R WITH LONG LEG]
				case '\u027D': // ɽ  [LATIN SMALL LETTER R WITH TAIL]
				case '\u027E': // ɾ  [LATIN SMALL LETTER R WITH FISHHOOK]
				case '\u027F': // ɿ  [LATIN SMALL LETTER REVERSED R WITH FISHHOOK]
				case '\u1D63': // ᵣ  [LATIN SUBSCRIPT SMALL LETTER R]
				case '\u1D72': // ᵲ  [LATIN SMALL LETTER R WITH MIDDLE TILDE]
				case '\u1D73': // ᵳ  [LATIN SMALL LETTER R WITH FISHHOOK AND MIDDLE TILDE]
				case '\u1D89': // ᶉ  [LATIN SMALL LETTER R WITH PALATAL HOOK]
				case '\u1E59': // ṙ  [LATIN SMALL LETTER R WITH DOT ABOVE]
				case '\u1E5B': // ṛ  [LATIN SMALL LETTER R WITH DOT BELOW]
				case '\u1E5D': // ṝ  [LATIN SMALL LETTER R WITH DOT BELOW AND MACRON]
				case '\u1E5F': // ṟ  [LATIN SMALL LETTER R WITH LINE BELOW]
				case '\u24E1': // ⓡ  [CIRCLED LATIN SMALL LETTER R]
				case '\uA75B': // ꝛ  [LATIN SMALL LETTER R ROTUNDA]
				case '\uA783': // ꞃ  [LATIN SMALL LETTER INSULAR R]
				case '\uFF52': // ｒ  [FULLWIDTH LATIN SMALL LETTER R]
					output[outputPos++] = 'r';
					break;
				case '\u24AD': // ⒭  [PARENTHESIZED LATIN SMALL LETTER R]
					output[outputPos++] = '(';
					output[outputPos++] = 'r';
					output[outputPos++] = ')';
					break;
				case '\u015A': // Ś  [LATIN CAPITAL LETTER S WITH ACUTE]
				case '\u015C': // Ŝ  [LATIN CAPITAL LETTER S WITH CIRCUMFLEX]
				case '\u015E': // Ş  [LATIN CAPITAL LETTER S WITH CEDILLA]
				case '\u0160': // Š  [LATIN CAPITAL LETTER S WITH CARON]
				case '\u0218': // Ș  [LATIN CAPITAL LETTER S WITH COMMA BELOW]
				case '\u1E60': // Ṡ  [LATIN CAPITAL LETTER S WITH DOT ABOVE]
				case '\u1E62': // Ṣ  [LATIN CAPITAL LETTER S WITH DOT BELOW]
				case '\u1E64': // Ṥ  [LATIN CAPITAL LETTER S WITH ACUTE AND DOT ABOVE]
				case '\u1E66': // Ṧ  [LATIN CAPITAL LETTER S WITH CARON AND DOT ABOVE]
				case '\u1E68': // Ṩ  [LATIN CAPITAL LETTER S WITH DOT BELOW AND DOT ABOVE]
				case '\u24C8': // Ⓢ  [CIRCLED LATIN CAPITAL LETTER S]
				case '\uA731': // ꜱ  [LATIN LETTER SMALL CAPITAL S]
				case '\uA785': // ꞅ  [LATIN SMALL LETTER INSULAR S]
				case '\uFF33': // Ｓ  [FULLWIDTH LATIN CAPITAL LETTER S]
					output[outputPos++] = 'S';
					break;
				case '\u015B': // ś  [LATIN SMALL LETTER S WITH ACUTE]
				case '\u015D': // ŝ  [LATIN SMALL LETTER S WITH CIRCUMFLEX]
				case '\u015F': // ş  [LATIN SMALL LETTER S WITH CEDILLA]
				case '\u0161': // š  [LATIN SMALL LETTER S WITH CARON]
				case '\u017F': // ſ  http://en.wikipedia.org/wiki/Long_S  [LATIN SMALL LETTER LONG S]
				case '\u0219': // ș  [LATIN SMALL LETTER S WITH COMMA BELOW]
				case '\u023F': // ȿ  [LATIN SMALL LETTER S WITH SWASH TAIL]
				case '\u0282': // ʂ  [LATIN SMALL LETTER S WITH HOOK]
				case '\u1D74': // ᵴ  [LATIN SMALL LETTER S WITH MIDDLE TILDE]
				case '\u1D8A': // ᶊ  [LATIN SMALL LETTER S WITH PALATAL HOOK]
				case '\u1E61': // ṡ  [LATIN SMALL LETTER S WITH DOT ABOVE]
				case '\u1E63': // ṣ  [LATIN SMALL LETTER S WITH DOT BELOW]
				case '\u1E65': // ṥ  [LATIN SMALL LETTER S WITH ACUTE AND DOT ABOVE]
				case '\u1E67': // ṧ  [LATIN SMALL LETTER S WITH CARON AND DOT ABOVE]
				case '\u1E69': // ṩ  [LATIN SMALL LETTER S WITH DOT BELOW AND DOT ABOVE]
				case '\u1E9C': // ẜ  [LATIN SMALL LETTER LONG S WITH DIAGONAL STROKE]
				case '\u1E9D': // ẝ  [LATIN SMALL LETTER LONG S WITH HIGH STROKE]
				case '\u24E2': // ⓢ  [CIRCLED LATIN SMALL LETTER S]
				case '\uA784': // Ꞅ  [LATIN CAPITAL LETTER INSULAR S]
				case '\uFF53': // ｓ  [FULLWIDTH LATIN SMALL LETTER S]
					output[outputPos++] = 's';
					break;
				case '\u1E9E': // ẞ  [LATIN CAPITAL LETTER SHARP S]
					output[outputPos++] = 'S';
					output[outputPos++] = 'S';
					break;
				case '\u24AE': // ⒮  [PARENTHESIZED LATIN SMALL LETTER S]
					output[outputPos++] = '(';
					output[outputPos++] = 's';
					output[outputPos++] = ')';
					break;
				case '\u00DF': // ß  [LATIN SMALL LETTER SHARP S]
					output[outputPos++] = 's';
					output[outputPos++] = 's';
					break;
				case '\uFB06': // ﬆ  [LATIN SMALL LIGATURE ST]
					output[outputPos++] = 's';
					output[outputPos++] = 't';
					break;
				case '\u0162': // Ţ  [LATIN CAPITAL LETTER T WITH CEDILLA]
				case '\u0164': // Ť  [LATIN CAPITAL LETTER T WITH CARON]
				case '\u0166': // Ŧ  [LATIN CAPITAL LETTER T WITH STROKE]
				case '\u01AC': // Ƭ  [LATIN CAPITAL LETTER T WITH HOOK]
				case '\u01AE': // Ʈ  [LATIN CAPITAL LETTER T WITH RETROFLEX HOOK]
				case '\u021A': // Ț  [LATIN CAPITAL LETTER T WITH COMMA BELOW]
				case '\u023E': // Ⱦ  [LATIN CAPITAL LETTER T WITH DIAGONAL STROKE]
				case '\u1D1B': // ᴛ  [LATIN LETTER SMALL CAPITAL T]
				case '\u1E6A': // Ṫ  [LATIN CAPITAL LETTER T WITH DOT ABOVE]
				case '\u1E6C': // Ṭ  [LATIN CAPITAL LETTER T WITH DOT BELOW]
				case '\u1E6E': // Ṯ  [LATIN CAPITAL LETTER T WITH LINE BELOW]
				case '\u1E70': // Ṱ  [LATIN CAPITAL LETTER T WITH CIRCUMFLEX BELOW]
				case '\u24C9': // Ⓣ  [CIRCLED LATIN CAPITAL LETTER T]
				case '\uA786': // Ꞇ  [LATIN CAPITAL LETTER INSULAR T]
				case '\uFF34': // Ｔ  [FULLWIDTH LATIN CAPITAL LETTER T]
					output[outputPos++] = 'T';
					break;
				case '\u0163': // ţ  [LATIN SMALL LETTER T WITH CEDILLA]
				case '\u0165': // ť  [LATIN SMALL LETTER T WITH CARON]
				case '\u0167': // ŧ  [LATIN SMALL LETTER T WITH STROKE]
				case '\u01AB': // ƫ  [LATIN SMALL LETTER T WITH PALATAL HOOK]
				case '\u01AD': // ƭ  [LATIN SMALL LETTER T WITH HOOK]
				case '\u021B': // ț  [LATIN SMALL LETTER T WITH COMMA BELOW]
				case '\u0236': // ȶ  [LATIN SMALL LETTER T WITH CURL]
				case '\u0287': // ʇ  [LATIN SMALL LETTER TURNED T]
				case '\u0288': // ʈ  [LATIN SMALL LETTER T WITH RETROFLEX HOOK]
				case '\u1D75': // ᵵ  [LATIN SMALL LETTER T WITH MIDDLE TILDE]
				case '\u1E6B': // ṫ  [LATIN SMALL LETTER T WITH DOT ABOVE]
				case '\u1E6D': // ṭ  [LATIN SMALL LETTER T WITH DOT BELOW]
				case '\u1E6F': // ṯ  [LATIN SMALL LETTER T WITH LINE BELOW]
				case '\u1E71': // ṱ  [LATIN SMALL LETTER T WITH CIRCUMFLEX BELOW]
				case '\u1E97': // ẗ  [LATIN SMALL LETTER T WITH DIAERESIS]
				case '\u24E3': // ⓣ  [CIRCLED LATIN SMALL LETTER T]
				case '\u2C66': // ⱦ  [LATIN SMALL LETTER T WITH DIAGONAL STROKE]
				case '\uFF54': // ｔ  [FULLWIDTH LATIN SMALL LETTER T]
					output[outputPos++] = 't';
					break;
				case '\u00DE': // Þ  [LATIN CAPITAL LETTER THORN]
				case '\uA766': // Ꝧ  [LATIN CAPITAL LETTER THORN WITH STROKE THROUGH DESCENDER]
					output[outputPos++] = 'T';
					output[outputPos++] = 'H';
					break;
				case '\uA728': // Ꜩ  [LATIN CAPITAL LETTER TZ]
					output[outputPos++] = 'T';
					output[outputPos++] = 'Z';
					break;
				case '\u24AF': // ⒯  [PARENTHESIZED LATIN SMALL LETTER T]
					output[outputPos++] = '(';
					output[outputPos++] = 't';
					output[outputPos++] = ')';
					break;
				case '\u02A8': // ʨ  [LATIN SMALL LETTER TC DIGRAPH WITH CURL]
					output[outputPos++] = 't';
					output[outputPos++] = 'c';
					break;
				case '\u00FE': // þ  [LATIN SMALL LETTER THORN]
				case '\u1D7A': // ᵺ  [LATIN SMALL LETTER TH WITH STRIKETHROUGH]
				case '\uA767': // ꝧ  [LATIN SMALL LETTER THORN WITH STROKE THROUGH DESCENDER]
					output[outputPos++] = 't';
					output[outputPos++] = 'h';
					break;
				case '\u02A6': // ʦ  [LATIN SMALL LETTER TS DIGRAPH]
					output[outputPos++] = 't';
					output[outputPos++] = 's';
					break;
				case '\uA729': // ꜩ  [LATIN SMALL LETTER TZ]
					output[outputPos++] = 't';
					output[outputPos++] = 'z';
					break;
				case '\u00D9': // Ù  [LATIN CAPITAL LETTER U WITH GRAVE]
				case '\u00DA': // Ú  [LATIN CAPITAL LETTER U WITH ACUTE]
				case '\u00DB': // Û  [LATIN CAPITAL LETTER U WITH CIRCUMFLEX]
				case '\u00DC': // Ü  [LATIN CAPITAL LETTER U WITH DIAERESIS]
				case '\u0168': // Ũ  [LATIN CAPITAL LETTER U WITH TILDE]
				case '\u016A': // Ū  [LATIN CAPITAL LETTER U WITH MACRON]
				case '\u016C': // Ŭ  [LATIN CAPITAL LETTER U WITH BREVE]
				case '\u016E': // Ů  [LATIN CAPITAL LETTER U WITH RING ABOVE]
				case '\u0170': // Ű  [LATIN CAPITAL LETTER U WITH DOUBLE ACUTE]
				case '\u0172': // Ų  [LATIN CAPITAL LETTER U WITH OGONEK]
				case '\u01AF': // Ư  [LATIN CAPITAL LETTER U WITH HORN]
				case '\u01D3': // Ǔ  [LATIN CAPITAL LETTER U WITH CARON]
				case '\u01D5': // Ǖ  [LATIN CAPITAL LETTER U WITH DIAERESIS AND MACRON]
				case '\u01D7': // Ǘ  [LATIN CAPITAL LETTER U WITH DIAERESIS AND ACUTE]
				case '\u01D9': // Ǚ  [LATIN CAPITAL LETTER U WITH DIAERESIS AND CARON]
				case '\u01DB': // Ǜ  [LATIN CAPITAL LETTER U WITH DIAERESIS AND GRAVE]
				case '\u0214': // Ȕ  [LATIN CAPITAL LETTER U WITH DOUBLE GRAVE]
				case '\u0216': // Ȗ  [LATIN CAPITAL LETTER U WITH INVERTED BREVE]
				case '\u0244': // Ʉ  [LATIN CAPITAL LETTER U BAR]
				case '\u1D1C': // ᴜ  [LATIN LETTER SMALL CAPITAL U]
				case '\u1D7E': // ᵾ  [LATIN SMALL CAPITAL LETTER U WITH STROKE]
				case '\u1E72': // Ṳ  [LATIN CAPITAL LETTER U WITH DIAERESIS BELOW]
				case '\u1E74': // Ṵ  [LATIN CAPITAL LETTER U WITH TILDE BELOW]
				case '\u1E76': // Ṷ  [LATIN CAPITAL LETTER U WITH CIRCUMFLEX BELOW]
				case '\u1E78': // Ṹ  [LATIN CAPITAL LETTER U WITH TILDE AND ACUTE]
				case '\u1E7A': // Ṻ  [LATIN CAPITAL LETTER U WITH MACRON AND DIAERESIS]
				case '\u1EE4': // Ụ  [LATIN CAPITAL LETTER U WITH DOT BELOW]
				case '\u1EE6': // Ủ  [LATIN CAPITAL LETTER U WITH HOOK ABOVE]
				case '\u1EE8': // Ứ  [LATIN CAPITAL LETTER U WITH HORN AND ACUTE]
				case '\u1EEA': // Ừ  [LATIN CAPITAL LETTER U WITH HORN AND GRAVE]
				case '\u1EEC': // Ử  [LATIN CAPITAL LETTER U WITH HORN AND HOOK ABOVE]
				case '\u1EEE': // Ữ  [LATIN CAPITAL LETTER U WITH HORN AND TILDE]
				case '\u1EF0': // Ự  [LATIN CAPITAL LETTER U WITH HORN AND DOT BELOW]
				case '\u24CA': // Ⓤ  [CIRCLED LATIN CAPITAL LETTER U]
				case '\uFF35': // Ｕ  [FULLWIDTH LATIN CAPITAL LETTER U]
					output[outputPos++] = 'U';
					break;
				case '\u00F9': // ù  [LATIN SMALL LETTER U WITH GRAVE]
				case '\u00FA': // ú  [LATIN SMALL LETTER U WITH ACUTE]
				case '\u00FB': // û  [LATIN SMALL LETTER U WITH CIRCUMFLEX]
				case '\u00FC': // ü  [LATIN SMALL LETTER U WITH DIAERESIS]
				case '\u0169': // ũ  [LATIN SMALL LETTER U WITH TILDE]
				case '\u016B': // ū  [LATIN SMALL LETTER U WITH MACRON]
				case '\u016D': // ŭ  [LATIN SMALL LETTER U WITH BREVE]
				case '\u016F': // ů  [LATIN SMALL LETTER U WITH RING ABOVE]
				case '\u0171': // ű  [LATIN SMALL LETTER U WITH DOUBLE ACUTE]
				case '\u0173': // ų  [LATIN SMALL LETTER U WITH OGONEK]
				case '\u01B0': // ư  [LATIN SMALL LETTER U WITH HORN]
				case '\u01D4': // ǔ  [LATIN SMALL LETTER U WITH CARON]
				case '\u01D6': // ǖ  [LATIN SMALL LETTER U WITH DIAERESIS AND MACRON]
				case '\u01D8': // ǘ  [LATIN SMALL LETTER U WITH DIAERESIS AND ACUTE]
				case '\u01DA': // ǚ  [LATIN SMALL LETTER U WITH DIAERESIS AND CARON]
				case '\u01DC': // ǜ  [LATIN SMALL LETTER U WITH DIAERESIS AND GRAVE]
				case '\u0215': // ȕ  [LATIN SMALL LETTER U WITH DOUBLE GRAVE]
				case '\u0217': // ȗ  [LATIN SMALL LETTER U WITH INVERTED BREVE]
				case '\u0289': // ʉ  [LATIN SMALL LETTER U BAR]
				case '\u1D64': // ᵤ  [LATIN SUBSCRIPT SMALL LETTER U]
				case '\u1D99': // ᶙ  [LATIN SMALL LETTER U WITH RETROFLEX HOOK]
				case '\u1E73': // ṳ  [LATIN SMALL LETTER U WITH DIAERESIS BELOW]
				case '\u1E75': // ṵ  [LATIN SMALL LETTER U WITH TILDE BELOW]
				case '\u1E77': // ṷ  [LATIN SMALL LETTER U WITH CIRCUMFLEX BELOW]
				case '\u1E79': // ṹ  [LATIN SMALL LETTER U WITH TILDE AND ACUTE]
				case '\u1E7B': // ṻ  [LATIN SMALL LETTER U WITH MACRON AND DIAERESIS]
				case '\u1EE5': // ụ  [LATIN SMALL LETTER U WITH DOT BELOW]
				case '\u1EE7': // ủ  [LATIN SMALL LETTER U WITH HOOK ABOVE]
				case '\u1EE9': // ứ  [LATIN SMALL LETTER U WITH HORN AND ACUTE]
				case '\u1EEB': // ừ  [LATIN SMALL LETTER U WITH HORN AND GRAVE]
				case '\u1EED': // ử  [LATIN SMALL LETTER U WITH HORN AND HOOK ABOVE]
				case '\u1EEF': // ữ  [LATIN SMALL LETTER U WITH HORN AND TILDE]
				case '\u1EF1': // ự  [LATIN SMALL LETTER U WITH HORN AND DOT BELOW]
				case '\u24E4': // ⓤ  [CIRCLED LATIN SMALL LETTER U]
				case '\uFF55': // ｕ  [FULLWIDTH LATIN SMALL LETTER U]
					output[outputPos++] = 'u';
					break;
				case '\u24B0': // ⒰  [PARENTHESIZED LATIN SMALL LETTER U]
					output[outputPos++] = '(';
					output[outputPos++] = 'u';
					output[outputPos++] = ')';
					break;
				case '\u1D6B': // ᵫ  [LATIN SMALL LETTER UE]
					output[outputPos++] = 'u';
					output[outputPos++] = 'e';
					break;
				case '\u01B2': // Ʋ  [LATIN CAPITAL LETTER V WITH HOOK]
				case '\u0245': // Ʌ  [LATIN CAPITAL LETTER TURNED V]
				case '\u1D20': // ᴠ  [LATIN LETTER SMALL CAPITAL V]
				case '\u1E7C': // Ṽ  [LATIN CAPITAL LETTER V WITH TILDE]
				case '\u1E7E': // Ṿ  [LATIN CAPITAL LETTER V WITH DOT BELOW]
				case '\u1EFC': // Ỽ  [LATIN CAPITAL LETTER MIDDLE-WELSH V]
				case '\u24CB': // Ⓥ  [CIRCLED LATIN CAPITAL LETTER V]
				case '\uA75E': // Ꝟ  [LATIN CAPITAL LETTER V WITH DIAGONAL STROKE]
				case '\uA768': // Ꝩ  [LATIN CAPITAL LETTER VEND]
				case '\uFF36': // Ｖ  [FULLWIDTH LATIN CAPITAL LETTER V]
					output[outputPos++] = 'V';
					break;
				case '\u028B': // ʋ  [LATIN SMALL LETTER V WITH HOOK]
				case '\u028C': // ʌ  [LATIN SMALL LETTER TURNED V]
				case '\u1D65': // ᵥ  [LATIN SUBSCRIPT SMALL LETTER V]
				case '\u1D8C': // ᶌ  [LATIN SMALL LETTER V WITH PALATAL HOOK]
				case '\u1E7D': // ṽ  [LATIN SMALL LETTER V WITH TILDE]
				case '\u1E7F': // ṿ  [LATIN SMALL LETTER V WITH DOT BELOW]
				case '\u24E5': // ⓥ  [CIRCLED LATIN SMALL LETTER V]
				case '\u2C71': // ⱱ  [LATIN SMALL LETTER V WITH RIGHT HOOK]
				case '\u2C74': // ⱴ  [LATIN SMALL LETTER V WITH CURL]
				case '\uA75F': // ꝟ  [LATIN SMALL LETTER V WITH DIAGONAL STROKE]
				case '\uFF56': // ｖ  [FULLWIDTH LATIN SMALL LETTER V]
					output[outputPos++] = 'v';
					break;
				case '\uA760': // Ꝡ  [LATIN CAPITAL LETTER VY]
					output[outputPos++] = 'V';
					output[outputPos++] = 'Y';
					break;
				case '\u24B1': // ⒱  [PARENTHESIZED LATIN SMALL LETTER V]
					output[outputPos++] = '(';
					output[outputPos++] = 'v';
					output[outputPos++] = ')';
					break;
				case '\uA761': // ꝡ  [LATIN SMALL LETTER VY]
					output[outputPos++] = 'v';
					output[outputPos++] = 'y';
					break;
				case '\u0174': // Ŵ  [LATIN CAPITAL LETTER W WITH CIRCUMFLEX]
				case '\u01F7': // Ƿ  http://en.wikipedia.org/wiki/Wynn  [LATIN CAPITAL LETTER WYNN]
				case '\u1D21': // ᴡ  [LATIN LETTER SMALL CAPITAL W]
				case '\u1E80': // Ẁ  [LATIN CAPITAL LETTER W WITH GRAVE]
				case '\u1E82': // Ẃ  [LATIN CAPITAL LETTER W WITH ACUTE]
				case '\u1E84': // Ẅ  [LATIN CAPITAL LETTER W WITH DIAERESIS]
				case '\u1E86': // Ẇ  [LATIN CAPITAL LETTER W WITH DOT ABOVE]
				case '\u1E88': // Ẉ  [LATIN CAPITAL LETTER W WITH DOT BELOW]
				case '\u24CC': // Ⓦ  [CIRCLED LATIN CAPITAL LETTER W]
				case '\u2C72': // Ⱳ  [LATIN CAPITAL LETTER W WITH HOOK]
				case '\uFF37': // Ｗ  [FULLWIDTH LATIN CAPITAL LETTER W]
					output[outputPos++] = 'W';
					break;
				case '\u0175': // ŵ  [LATIN SMALL LETTER W WITH CIRCUMFLEX]
				case '\u01BF': // ƿ  http://en.wikipedia.org/wiki/Wynn  [LATIN LETTER WYNN]
				case '\u028D': // ʍ  [LATIN SMALL LETTER TURNED W]
				case '\u1E81': // ẁ  [LATIN SMALL LETTER W WITH GRAVE]
				case '\u1E83': // ẃ  [LATIN SMALL LETTER W WITH ACUTE]
				case '\u1E85': // ẅ  [LATIN SMALL LETTER W WITH DIAERESIS]
				case '\u1E87': // ẇ  [LATIN SMALL LETTER W WITH DOT ABOVE]
				case '\u1E89': // ẉ  [LATIN SMALL LETTER W WITH DOT BELOW]
				case '\u1E98': // ẘ  [LATIN SMALL LETTER W WITH RING ABOVE]
				case '\u24E6': // ⓦ  [CIRCLED LATIN SMALL LETTER W]
				case '\u2C73': // ⱳ  [LATIN SMALL LETTER W WITH HOOK]
				case '\uFF57': // ｗ  [FULLWIDTH LATIN SMALL LETTER W]
					output[outputPos++] = 'w';
					break;
				case '\u24B2': // ⒲  [PARENTHESIZED LATIN SMALL LETTER W]
					output[outputPos++] = '(';
					output[outputPos++] = 'w';
					output[outputPos++] = ')';
					break;
				case '\u1E8A': // Ẋ  [LATIN CAPITAL LETTER X WITH DOT ABOVE]
				case '\u1E8C': // Ẍ  [LATIN CAPITAL LETTER X WITH DIAERESIS]
				case '\u24CD': // Ⓧ  [CIRCLED LATIN CAPITAL LETTER X]
				case '\uFF38': // Ｘ  [FULLWIDTH LATIN CAPITAL LETTER X]
					output[outputPos++] = 'X';
					break;
				case '\u1D8D': // ᶍ  [LATIN SMALL LETTER X WITH PALATAL HOOK]
				case '\u1E8B': // ẋ  [LATIN SMALL LETTER X WITH DOT ABOVE]
				case '\u1E8D': // ẍ  [LATIN SMALL LETTER X WITH DIAERESIS]
				case '\u2093': // ₓ  [LATIN SUBSCRIPT SMALL LETTER X]
				case '\u24E7': // ⓧ  [CIRCLED LATIN SMALL LETTER X]
				case '\uFF58': // ｘ  [FULLWIDTH LATIN SMALL LETTER X]
					output[outputPos++] = 'x';
					break;
				case '\u24B3': // ⒳  [PARENTHESIZED LATIN SMALL LETTER X]
					output[outputPos++] = '(';
					output[outputPos++] = 'x';
					output[outputPos++] = ')';
					break;
				case '\u00DD': // Ý  [LATIN CAPITAL LETTER Y WITH ACUTE]
				case '\u0176': // Ŷ  [LATIN CAPITAL LETTER Y WITH CIRCUMFLEX]
				case '\u0178': // Ÿ  [LATIN CAPITAL LETTER Y WITH DIAERESIS]
				case '\u01B3': // Ƴ  [LATIN CAPITAL LETTER Y WITH HOOK]
				case '\u0232': // Ȳ  [LATIN CAPITAL LETTER Y WITH MACRON]
				case '\u024E': // Ɏ  [LATIN CAPITAL LETTER Y WITH STROKE]
				case '\u028F': // ʏ  [LATIN LETTER SMALL CAPITAL Y]
				case '\u1E8E': // Ẏ  [LATIN CAPITAL LETTER Y WITH DOT ABOVE]
				case '\u1EF2': // Ỳ  [LATIN CAPITAL LETTER Y WITH GRAVE]
				case '\u1EF4': // Ỵ  [LATIN CAPITAL LETTER Y WITH DOT BELOW]
				case '\u1EF6': // Ỷ  [LATIN CAPITAL LETTER Y WITH HOOK ABOVE]
				case '\u1EF8': // Ỹ  [LATIN CAPITAL LETTER Y WITH TILDE]
				case '\u1EFE': // Ỿ  [LATIN CAPITAL LETTER Y WITH LOOP]
				case '\u24CE': // Ⓨ  [CIRCLED LATIN CAPITAL LETTER Y]
				case '\uFF39': // Ｙ  [FULLWIDTH LATIN CAPITAL LETTER Y]
					output[outputPos++] = 'Y';
					break;
				case '\u00FD': // ý  [LATIN SMALL LETTER Y WITH ACUTE]
				case '\u00FF': // ÿ  [LATIN SMALL LETTER Y WITH DIAERESIS]
				case '\u0177': // ŷ  [LATIN SMALL LETTER Y WITH CIRCUMFLEX]
				case '\u01B4': // ƴ  [LATIN SMALL LETTER Y WITH HOOK]
				case '\u0233': // ȳ  [LATIN SMALL LETTER Y WITH MACRON]
				case '\u024F': // ɏ  [LATIN SMALL LETTER Y WITH STROKE]
				case '\u028E': // ʎ  [LATIN SMALL LETTER TURNED Y]
				case '\u1E8F': // ẏ  [LATIN SMALL LETTER Y WITH DOT ABOVE]
				case '\u1E99': // ẙ  [LATIN SMALL LETTER Y WITH RING ABOVE]
				case '\u1EF3': // ỳ  [LATIN SMALL LETTER Y WITH GRAVE]
				case '\u1EF5': // ỵ  [LATIN SMALL LETTER Y WITH DOT BELOW]
				case '\u1EF7': // ỷ  [LATIN SMALL LETTER Y WITH HOOK ABOVE]
				case '\u1EF9': // ỹ  [LATIN SMALL LETTER Y WITH TILDE]
				case '\u1EFF': // ỿ  [LATIN SMALL LETTER Y WITH LOOP]
				case '\u24E8': // ⓨ  [CIRCLED LATIN SMALL LETTER Y]
				case '\uFF59': // ｙ  [FULLWIDTH LATIN SMALL LETTER Y]
					output[outputPos++] = 'y';
					break;
				case '\u24B4': // ⒴  [PARENTHESIZED LATIN SMALL LETTER Y]
					output[outputPos++] = '(';
					output[outputPos++] = 'y';
					output[outputPos++] = ')';
					break;
				case '\u0179': // Ź  [LATIN CAPITAL LETTER Z WITH ACUTE]
				case '\u017B': // Ż  [LATIN CAPITAL LETTER Z WITH DOT ABOVE]
				case '\u017D': // Ž  [LATIN CAPITAL LETTER Z WITH CARON]
				case '\u01B5': // Ƶ  [LATIN CAPITAL LETTER Z WITH STROKE]
				case '\u021C': // Ȝ  http://en.wikipedia.org/wiki/Yogh  [LATIN CAPITAL LETTER YOGH]
				case '\u0224': // Ȥ  [LATIN CAPITAL LETTER Z WITH HOOK]
				case '\u1D22': // ᴢ  [LATIN LETTER SMALL CAPITAL Z]
				case '\u1E90': // Ẑ  [LATIN CAPITAL LETTER Z WITH CIRCUMFLEX]
				case '\u1E92': // Ẓ  [LATIN CAPITAL LETTER Z WITH DOT BELOW]
				case '\u1E94': // Ẕ  [LATIN CAPITAL LETTER Z WITH LINE BELOW]
				case '\u24CF': // Ⓩ  [CIRCLED LATIN CAPITAL LETTER Z]
				case '\u2C6B': // Ⱬ  [LATIN CAPITAL LETTER Z WITH DESCENDER]
				case '\uA762': // Ꝣ  [LATIN CAPITAL LETTER VISIGOTHIC Z]
				case '\uFF3A': // Ｚ  [FULLWIDTH LATIN CAPITAL LETTER Z]
					output[outputPos++] = 'Z';
					break;
				case '\u017A': // ź  [LATIN SMALL LETTER Z WITH ACUTE]
				case '\u017C': // ż  [LATIN SMALL LETTER Z WITH DOT ABOVE]
				case '\u017E': // ž  [LATIN SMALL LETTER Z WITH CARON]
				case '\u01B6': // ƶ  [LATIN SMALL LETTER Z WITH STROKE]
				case '\u021D': // ȝ  http://en.wikipedia.org/wiki/Yogh  [LATIN SMALL LETTER YOGH]
				case '\u0225': // ȥ  [LATIN SMALL LETTER Z WITH HOOK]
				case '\u0240': // ɀ  [LATIN SMALL LETTER Z WITH SWASH TAIL]
				case '\u0290': // ʐ  [LATIN SMALL LETTER Z WITH RETROFLEX HOOK]
				case '\u0291': // ʑ  [LATIN SMALL LETTER Z WITH CURL]
				case '\u1D76': // ᵶ  [LATIN SMALL LETTER Z WITH MIDDLE TILDE]
				case '\u1D8E': // ᶎ  [LATIN SMALL LETTER Z WITH PALATAL HOOK]
				case '\u1E91': // ẑ  [LATIN SMALL LETTER Z WITH CIRCUMFLEX]
				case '\u1E93': // ẓ  [LATIN SMALL LETTER Z WITH DOT BELOW]
				case '\u1E95': // ẕ  [LATIN SMALL LETTER Z WITH LINE BELOW]
				case '\u24E9': // ⓩ  [CIRCLED LATIN SMALL LETTER Z]
				case '\u2C6C': // ⱬ  [LATIN SMALL LETTER Z WITH DESCENDER]
				case '\uA763': // ꝣ  [LATIN SMALL LETTER VISIGOTHIC Z]
				case '\uFF5A': // ｚ  [FULLWIDTH LATIN SMALL LETTER Z]
					output[outputPos++] = 'z';
					break;
				case '\u24B5': // ⒵  [PARENTHESIZED LATIN SMALL LETTER Z]
					output[outputPos++] = '(';
					output[outputPos++] = 'z';
					output[outputPos++] = ')';
					break;
				case '\u2070': // ⁰  [SUPERSCRIPT ZERO]
				case '\u2080': // ₀  [SUBSCRIPT ZERO]
				case '\u24EA': // ⓪  [CIRCLED DIGIT ZERO]
				case '\u24FF': // ⓿  [NEGATIVE CIRCLED DIGIT ZERO]
				case '\uFF10': // ０  [FULLWIDTH DIGIT ZERO]
					output[outputPos++] = '0';
					break;
				case '\u00B9': // ¹  [SUPERSCRIPT ONE]
				case '\u2081': // ₁  [SUBSCRIPT ONE]
				case '\u2460': // ①  [CIRCLED DIGIT ONE]
				case '\u24F5': // ⓵  [DOUBLE CIRCLED DIGIT ONE]
				case '\u2776': // ❶  [DINGBAT NEGATIVE CIRCLED DIGIT ONE]
				case '\u2780': // ➀  [DINGBAT CIRCLED SANS-SERIF DIGIT ONE]
				case '\u278A': // ➊  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT ONE]
				case '\uFF11': // １  [FULLWIDTH DIGIT ONE]
					output[outputPos++] = '1';
					break;
				case '\u2488': // ⒈  [DIGIT ONE FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '.';
					break;
				case '\u2474': // ⑴  [PARENTHESIZED DIGIT ONE]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = ')';
					break;
				case '\u00B2': // ²  [SUPERSCRIPT TWO]
				case '\u2082': // ₂  [SUBSCRIPT TWO]
				case '\u2461': // ②  [CIRCLED DIGIT TWO]
				case '\u24F6': // ⓶  [DOUBLE CIRCLED DIGIT TWO]
				case '\u2777': // ❷  [DINGBAT NEGATIVE CIRCLED DIGIT TWO]
				case '\u2781': // ➁  [DINGBAT CIRCLED SANS-SERIF DIGIT TWO]
				case '\u278B': // ➋  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT TWO]
				case '\uFF12': // ２  [FULLWIDTH DIGIT TWO]
					output[outputPos++] = '2';
					break;
				case '\u2489': // ⒉  [DIGIT TWO FULL STOP]
					output[outputPos++] = '2';
					output[outputPos++] = '.';
					break;
				case '\u2475': // ⑵  [PARENTHESIZED DIGIT TWO]
					output[outputPos++] = '(';
					output[outputPos++] = '2';
					output[outputPos++] = ')';
					break;
				case '\u00B3': // ³  [SUPERSCRIPT THREE]
				case '\u2083': // ₃  [SUBSCRIPT THREE]
				case '\u2462': // ③  [CIRCLED DIGIT THREE]
				case '\u24F7': // ⓷  [DOUBLE CIRCLED DIGIT THREE]
				case '\u2778': // ❸  [DINGBAT NEGATIVE CIRCLED DIGIT THREE]
				case '\u2782': // ➂  [DINGBAT CIRCLED SANS-SERIF DIGIT THREE]
				case '\u278C': // ➌  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT THREE]
				case '\uFF13': // ３  [FULLWIDTH DIGIT THREE]
					output[outputPos++] = '3';
					break;
				case '\u248A': // ⒊  [DIGIT THREE FULL STOP]
					output[outputPos++] = '3';
					output[outputPos++] = '.';
					break;
				case '\u2476': // ⑶  [PARENTHESIZED DIGIT THREE]
					output[outputPos++] = '(';
					output[outputPos++] = '3';
					output[outputPos++] = ')';
					break;
				case '\u2074': // ⁴  [SUPERSCRIPT FOUR]
				case '\u2084': // ₄  [SUBSCRIPT FOUR]
				case '\u2463': // ④  [CIRCLED DIGIT FOUR]
				case '\u24F8': // ⓸  [DOUBLE CIRCLED DIGIT FOUR]
				case '\u2779': // ❹  [DINGBAT NEGATIVE CIRCLED DIGIT FOUR]
				case '\u2783': // ➃  [DINGBAT CIRCLED SANS-SERIF DIGIT FOUR]
				case '\u278D': // ➍  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT FOUR]
				case '\uFF14': // ４  [FULLWIDTH DIGIT FOUR]
					output[outputPos++] = '4';
					break;
				case '\u248B': // ⒋  [DIGIT FOUR FULL STOP]
					output[outputPos++] = '4';
					output[outputPos++] = '.';
					break;
				case '\u2477': // ⑷  [PARENTHESIZED DIGIT FOUR]
					output[outputPos++] = '(';
					output[outputPos++] = '4';
					output[outputPos++] = ')';
					break;
				case '\u2075': // ⁵  [SUPERSCRIPT FIVE]
				case '\u2085': // ₅  [SUBSCRIPT FIVE]
				case '\u2464': // ⑤  [CIRCLED DIGIT FIVE]
				case '\u24F9': // ⓹  [DOUBLE CIRCLED DIGIT FIVE]
				case '\u277A': // ❺  [DINGBAT NEGATIVE CIRCLED DIGIT FIVE]
				case '\u2784': // ➄  [DINGBAT CIRCLED SANS-SERIF DIGIT FIVE]
				case '\u278E': // ➎  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT FIVE]
				case '\uFF15': // ５  [FULLWIDTH DIGIT FIVE]
					output[outputPos++] = '5';
					break;
				case '\u248C': // ⒌  [DIGIT FIVE FULL STOP]
					output[outputPos++] = '5';
					output[outputPos++] = '.';
					break;
				case '\u2478': // ⑸  [PARENTHESIZED DIGIT FIVE]
					output[outputPos++] = '(';
					output[outputPos++] = '5';
					output[outputPos++] = ')';
					break;
				case '\u2076': // ⁶  [SUPERSCRIPT SIX]
				case '\u2086': // ₆  [SUBSCRIPT SIX]
				case '\u2465': // ⑥  [CIRCLED DIGIT SIX]
				case '\u24FA': // ⓺  [DOUBLE CIRCLED DIGIT SIX]
				case '\u277B': // ❻  [DINGBAT NEGATIVE CIRCLED DIGIT SIX]
				case '\u2785': // ➅  [DINGBAT CIRCLED SANS-SERIF DIGIT SIX]
				case '\u278F': // ➏  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT SIX]
				case '\uFF16': // ６  [FULLWIDTH DIGIT SIX]
					output[outputPos++] = '6';
					break;
				case '\u248D': // ⒍  [DIGIT SIX FULL STOP]
					output[outputPos++] = '6';
					output[outputPos++] = '.';
					break;
				case '\u2479': // ⑹  [PARENTHESIZED DIGIT SIX]
					output[outputPos++] = '(';
					output[outputPos++] = '6';
					output[outputPos++] = ')';
					break;
				case '\u2077': // ⁷  [SUPERSCRIPT SEVEN]
				case '\u2087': // ₇  [SUBSCRIPT SEVEN]
				case '\u2466': // ⑦  [CIRCLED DIGIT SEVEN]
				case '\u24FB': // ⓻  [DOUBLE CIRCLED DIGIT SEVEN]
				case '\u277C': // ❼  [DINGBAT NEGATIVE CIRCLED DIGIT SEVEN]
				case '\u2786': // ➆  [DINGBAT CIRCLED SANS-SERIF DIGIT SEVEN]
				case '\u2790': // ➐  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT SEVEN]
				case '\uFF17': // ７  [FULLWIDTH DIGIT SEVEN]
					output[outputPos++] = '7';
					break;
				case '\u248E': // ⒎  [DIGIT SEVEN FULL STOP]
					output[outputPos++] = '7';
					output[outputPos++] = '.';
					break;
				case '\u247A': // ⑺  [PARENTHESIZED DIGIT SEVEN]
					output[outputPos++] = '(';
					output[outputPos++] = '7';
					output[outputPos++] = ')';
					break;
				case '\u2078': // ⁸  [SUPERSCRIPT EIGHT]
				case '\u2088': // ₈  [SUBSCRIPT EIGHT]
				case '\u2467': // ⑧  [CIRCLED DIGIT EIGHT]
				case '\u24FC': // ⓼  [DOUBLE CIRCLED DIGIT EIGHT]
				case '\u277D': // ❽  [DINGBAT NEGATIVE CIRCLED DIGIT EIGHT]
				case '\u2787': // ➇  [DINGBAT CIRCLED SANS-SERIF DIGIT EIGHT]
				case '\u2791': // ➑  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT EIGHT]
				case '\uFF18': // ８  [FULLWIDTH DIGIT EIGHT]
					output[outputPos++] = '8';
					break;
				case '\u248F': // ⒏  [DIGIT EIGHT FULL STOP]
					output[outputPos++] = '8';
					output[outputPos++] = '.';
					break;
				case '\u247B': // ⑻  [PARENTHESIZED DIGIT EIGHT]
					output[outputPos++] = '(';
					output[outputPos++] = '8';
					output[outputPos++] = ')';
					break;
				case '\u2079': // ⁹  [SUPERSCRIPT NINE]
				case '\u2089': // ₉  [SUBSCRIPT NINE]
				case '\u2468': // ⑨  [CIRCLED DIGIT NINE]
				case '\u24FD': // ⓽  [DOUBLE CIRCLED DIGIT NINE]
				case '\u277E': // ❾  [DINGBAT NEGATIVE CIRCLED DIGIT NINE]
				case '\u2788': // ➈  [DINGBAT CIRCLED SANS-SERIF DIGIT NINE]
				case '\u2792': // ➒  [DINGBAT NEGATIVE CIRCLED SANS-SERIF DIGIT NINE]
				case '\uFF19': // ９  [FULLWIDTH DIGIT NINE]
					output[outputPos++] = '9';
					break;
				case '\u2490': // ⒐  [DIGIT NINE FULL STOP]
					output[outputPos++] = '9';
					output[outputPos++] = '.';
					break;
				case '\u247C': // ⑼  [PARENTHESIZED DIGIT NINE]
					output[outputPos++] = '(';
					output[outputPos++] = '9';
					output[outputPos++] = ')';
					break;
				case '\u2469': // ⑩  [CIRCLED NUMBER TEN]
				case '\u24FE': // ⓾  [DOUBLE CIRCLED NUMBER TEN]
				case '\u277F': // ❿  [DINGBAT NEGATIVE CIRCLED NUMBER TEN]
				case '\u2789': // ➉  [DINGBAT CIRCLED SANS-SERIF NUMBER TEN]
				case '\u2793': // ➓  [DINGBAT NEGATIVE CIRCLED SANS-SERIF NUMBER TEN]
					output[outputPos++] = '1';
					output[outputPos++] = '0';
					break;
				case '\u2491': // ⒑  [NUMBER TEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '0';
					output[outputPos++] = '.';
					break;
				case '\u247D': // ⑽  [PARENTHESIZED NUMBER TEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '0';
					output[outputPos++] = ')';
					break;
				case '\u246A': // ⑪  [CIRCLED NUMBER ELEVEN]
				case '\u24EB': // ⓫  [NEGATIVE CIRCLED NUMBER ELEVEN]
					output[outputPos++] = '1';
					output[outputPos++] = '1';
					break;
				case '\u2492': // ⒒  [NUMBER ELEVEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '1';
					output[outputPos++] = '.';
					break;
				case '\u247E': // ⑾  [PARENTHESIZED NUMBER ELEVEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '1';
					output[outputPos++] = ')';
					break;
				case '\u246B': // ⑫  [CIRCLED NUMBER TWELVE]
				case '\u24EC': // ⓬  [NEGATIVE CIRCLED NUMBER TWELVE]
					output[outputPos++] = '1';
					output[outputPos++] = '2';
					break;
				case '\u2493': // ⒓  [NUMBER TWELVE FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '2';
					output[outputPos++] = '.';
					break;
				case '\u247F': // ⑿  [PARENTHESIZED NUMBER TWELVE]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '2';
					output[outputPos++] = ')';
					break;
				case '\u246C': // ⑬  [CIRCLED NUMBER THIRTEEN]
				case '\u24ED': // ⓭  [NEGATIVE CIRCLED NUMBER THIRTEEN]
					output[outputPos++] = '1';
					output[outputPos++] = '3';
					break;
				case '\u2494': // ⒔  [NUMBER THIRTEEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '3';
					output[outputPos++] = '.';
					break;
				case '\u2480': // ⒀  [PARENTHESIZED NUMBER THIRTEEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '3';
					output[outputPos++] = ')';
					break;
				case '\u246D': // ⑭  [CIRCLED NUMBER FOURTEEN]
				case '\u24EE': // ⓮  [NEGATIVE CIRCLED NUMBER FOURTEEN]
					output[outputPos++] = '1';
					output[outputPos++] = '4';
					break;
				case '\u2495': // ⒕  [NUMBER FOURTEEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '4';
					output[outputPos++] = '.';
					break;
				case '\u2481': // ⒁  [PARENTHESIZED NUMBER FOURTEEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '4';
					output[outputPos++] = ')';
					break;
				case '\u246E': // ⑮  [CIRCLED NUMBER FIFTEEN]
				case '\u24EF': // ⓯  [NEGATIVE CIRCLED NUMBER FIFTEEN]
					output[outputPos++] = '1';
					output[outputPos++] = '5';
					break;
				case '\u2496': // ⒖  [NUMBER FIFTEEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '5';
					output[outputPos++] = '.';
					break;
				case '\u2482': // ⒂  [PARENTHESIZED NUMBER FIFTEEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '5';
					output[outputPos++] = ')';
					break;
				case '\u246F': // ⑯  [CIRCLED NUMBER SIXTEEN]
				case '\u24F0': // ⓰  [NEGATIVE CIRCLED NUMBER SIXTEEN]
					output[outputPos++] = '1';
					output[outputPos++] = '6';
					break;
				case '\u2497': // ⒗  [NUMBER SIXTEEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '6';
					output[outputPos++] = '.';
					break;
				case '\u2483': // ⒃  [PARENTHESIZED NUMBER SIXTEEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '6';
					output[outputPos++] = ')';
					break;
				case '\u2470': // ⑰  [CIRCLED NUMBER SEVENTEEN]
				case '\u24F1': // ⓱  [NEGATIVE CIRCLED NUMBER SEVENTEEN]
					output[outputPos++] = '1';
					output[outputPos++] = '7';
					break;
				case '\u2498': // ⒘  [NUMBER SEVENTEEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '7';
					output[outputPos++] = '.';
					break;
				case '\u2484': // ⒄  [PARENTHESIZED NUMBER SEVENTEEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '7';
					output[outputPos++] = ')';
					break;
				case '\u2471': // ⑱  [CIRCLED NUMBER EIGHTEEN]
				case '\u24F2': // ⓲  [NEGATIVE CIRCLED NUMBER EIGHTEEN]
					output[outputPos++] = '1';
					output[outputPos++] = '8';
					break;
				case '\u2499': // ⒙  [NUMBER EIGHTEEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '8';
					output[outputPos++] = '.';
					break;
				case '\u2485': // ⒅  [PARENTHESIZED NUMBER EIGHTEEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '8';
					output[outputPos++] = ')';
					break;
				case '\u2472': // ⑲  [CIRCLED NUMBER NINETEEN]
				case '\u24F3': // ⓳  [NEGATIVE CIRCLED NUMBER NINETEEN]
					output[outputPos++] = '1';
					output[outputPos++] = '9';
					break;
				case '\u249A': // ⒚  [NUMBER NINETEEN FULL STOP]
					output[outputPos++] = '1';
					output[outputPos++] = '9';
					output[outputPos++] = '.';
					break;
				case '\u2486': // ⒆  [PARENTHESIZED NUMBER NINETEEN]
					output[outputPos++] = '(';
					output[outputPos++] = '1';
					output[outputPos++] = '9';
					output[outputPos++] = ')';
					break;
				case '\u2473': // ⑳  [CIRCLED NUMBER TWENTY]
				case '\u24F4': // ⓴  [NEGATIVE CIRCLED NUMBER TWENTY]
					output[outputPos++] = '2';
					output[outputPos++] = '0';
					break;
				case '\u249B': // ⒛  [NUMBER TWENTY FULL STOP]
					output[outputPos++] = '2';
					output[outputPos++] = '0';
					output[outputPos++] = '.';
					break;
				case '\u2487': // ⒇  [PARENTHESIZED NUMBER TWENTY]
					output[outputPos++] = '(';
					output[outputPos++] = '2';
					output[outputPos++] = '0';
					output[outputPos++] = ')';
					break;
				case '\u00AB': // «  [LEFT-POINTING DOUBLE ANGLE QUOTATION MARK]
				case '\u00BB': // »  [RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK]
				case '\u201C': // “  [LEFT DOUBLE QUOTATION MARK]
				case '\u201D': // ”  [RIGHT DOUBLE QUOTATION MARK]
				case '\u201E': // „  [DOUBLE LOW-9 QUOTATION MARK]
				case '\u2033': // ″  [DOUBLE PRIME]
				case '\u2036': // ‶  [REVERSED DOUBLE PRIME]
				case '\u275D': // ❝  [HEAVY DOUBLE TURNED COMMA QUOTATION MARK ORNAMENT]
				case '\u275E': // ❞  [HEAVY DOUBLE COMMA QUOTATION MARK ORNAMENT]
				case '\u276E': // ❮  [HEAVY LEFT-POINTING ANGLE QUOTATION MARK ORNAMENT]
				case '\u276F': // ❯  [HEAVY RIGHT-POINTING ANGLE QUOTATION MARK ORNAMENT]
				case '\uFF02': // ＂  [FULLWIDTH QUOTATION MARK]
					output[outputPos++] = '"';
					break;
				case '\u2018': // ‘  [LEFT SINGLE QUOTATION MARK]
				case '\u2019': // ’  [RIGHT SINGLE QUOTATION MARK]
				case '\u201A': // ‚  [SINGLE LOW-9 QUOTATION MARK]
				case '\u201B': // ‛  [SINGLE HIGH-REVERSED-9 QUOTATION MARK]
				case '\u2032': // ′  [PRIME]
				case '\u2035': // ‵  [REVERSED PRIME]
				case '\u2039': // ‹  [SINGLE LEFT-POINTING ANGLE QUOTATION MARK]
				case '\u203A': // ›  [SINGLE RIGHT-POINTING ANGLE QUOTATION MARK]
				case '\u275B': // ❛  [HEAVY SINGLE TURNED COMMA QUOTATION MARK ORNAMENT]
				case '\u275C': // ❜  [HEAVY SINGLE COMMA QUOTATION MARK ORNAMENT]
				case '\uFF07': // ＇  [FULLWIDTH APOSTROPHE]
				case '\u02BC': // aprostrofo genitivo sassone e carattere ebraico (ticket 2984)
					output[outputPos++] = '\'';
					break;
				case '\u2010': // ‐  [HYPHEN]
				case '\u2011': // ‑  [NON-BREAKING HYPHEN]
				case '\u2012': // ‒  [FIGURE DASH]
				case '\u2013': // –  [EN DASH]
				case '\u2014': // —  [EM DASH]
				case '\u207B': // ⁻  [SUPERSCRIPT MINUS]
				case '\u208B': // ₋  [SUBSCRIPT MINUS]
				case '\uFF0D': // －  [FULLWIDTH HYPHEN-MINUS]
					output[outputPos++] = '-';
					break;
				case '\u2045': // ⁅  [LEFT SQUARE BRACKET WITH QUILL]
				case '\u2772': // ❲  [LIGHT LEFT TORTOISE SHELL BRACKET ORNAMENT]
				case '\uFF3B': // ［  [FULLWIDTH LEFT SQUARE BRACKET]
					output[outputPos++] = '[';
					break;
				case '\u2046': // ⁆  [RIGHT SQUARE BRACKET WITH QUILL]
				case '\u2773': // ❳  [LIGHT RIGHT TORTOISE SHELL BRACKET ORNAMENT]
				case '\uFF3D': // ］  [FULLWIDTH RIGHT SQUARE BRACKET]
					output[outputPos++] = ']';
					break;
				case '\u207D': // ⁽  [SUPERSCRIPT LEFT PARENTHESIS]
				case '\u208D': // ₍  [SUBSCRIPT LEFT PARENTHESIS]
				case '\u2768': // ❨  [MEDIUM LEFT PARENTHESIS ORNAMENT]
				case '\u276A': // ❪  [MEDIUM FLATTENED LEFT PARENTHESIS ORNAMENT]
				case '\uFF08': // （  [FULLWIDTH LEFT PARENTHESIS]
					output[outputPos++] = '(';
					break;
				case '\u2E28': // ⸨  [LEFT DOUBLE PARENTHESIS]
					output[outputPos++] = '(';
					output[outputPos++] = '(';
					break;
				case '\u207E': // ⁾  [SUPERSCRIPT RIGHT PARENTHESIS]
				case '\u208E': // ₎  [SUBSCRIPT RIGHT PARENTHESIS]
				case '\u2769': // ❩  [MEDIUM RIGHT PARENTHESIS ORNAMENT]
				case '\u276B': // ❫  [MEDIUM FLATTENED RIGHT PARENTHESIS ORNAMENT]
				case '\uFF09': // ）  [FULLWIDTH RIGHT PARENTHESIS]
					output[outputPos++] = ')';
					break;
				case '\u2E29': // ⸩  [RIGHT DOUBLE PARENTHESIS]
					output[outputPos++] = ')';
					output[outputPos++] = ')';
					break;
				case '\u276C': // ❬  [MEDIUM LEFT-POINTING ANGLE BRACKET ORNAMENT]
				case '\u2770': // ❰  [HEAVY LEFT-POINTING ANGLE BRACKET ORNAMENT]
				case '\uFF1C': // ＜  [FULLWIDTH LESS-THAN SIGN]
					output[outputPos++] = '<';
					break;
				case '\u276D': // ❭  [MEDIUM RIGHT-POINTING ANGLE BRACKET ORNAMENT]
				case '\u2771': // ❱  [HEAVY RIGHT-POINTING ANGLE BRACKET ORNAMENT]
				case '\uFF1E': // ＞  [FULLWIDTH GREATER-THAN SIGN]
					output[outputPos++] = '>';
					break;
				case '\u2774': // ❴  [MEDIUM LEFT CURLY BRACKET ORNAMENT]
				case '\uFF5B': // ｛  [FULLWIDTH LEFT CURLY BRACKET]
					output[outputPos++] = '{';
					break;
				case '\u2775': // ❵  [MEDIUM RIGHT CURLY BRACKET ORNAMENT]
				case '\uFF5D': // ｝  [FULLWIDTH RIGHT CURLY BRACKET]
					output[outputPos++] = '}';
					break;
				case '\u207A': // ⁺  [SUPERSCRIPT PLUS SIGN]
				case '\u208A': // ₊  [SUBSCRIPT PLUS SIGN]
				case '\uFF0B': // ＋  [FULLWIDTH PLUS SIGN]
					output[outputPos++] = '+';
					break;
				case '\u207C': // ⁼  [SUPERSCRIPT EQUALS SIGN]
				case '\u208C': // ₌  [SUBSCRIPT EQUALS SIGN]
				case '\uFF1D': // ＝  [FULLWIDTH EQUALS SIGN]
					output[outputPos++] = '=';
					break;
				case '\uFF01': // ！  [FULLWIDTH EXCLAMATION MARK]
					output[outputPos++] = '!';
					break;
				case '\u203C': // ‼  [DOUBLE EXCLAMATION MARK]
					output[outputPos++] = '!';
					output[outputPos++] = '!';
					break;
				case '\u2049': // ⁉  [EXCLAMATION QUESTION MARK]
					output[outputPos++] = '!';
					output[outputPos++] = '?';
					break;
				case '\uFF03': // ＃  [FULLWIDTH NUMBER SIGN]
					output[outputPos++] = '#';
					break;
				case '\uFF04': // ＄  [FULLWIDTH DOLLAR SIGN]
					output[outputPos++] = '$';
					break;
				case '\u2052': // ⁒  [COMMERCIAL MINUS SIGN]
				case '\uFF05': // ％  [FULLWIDTH PERCENT SIGN]
					output[outputPos++] = '%';
					break;
				case '\uFF06': // ＆  [FULLWIDTH AMPERSAND]
					output[outputPos++] = '&';
					break;
				case '\u204E': // ⁎  [LOW ASTERISK]
				case '\uFF0A': // ＊  [FULLWIDTH ASTERISK]
					output[outputPos++] = '*';
					break;
				case '\uFF0C': // ，  [FULLWIDTH COMMA]
					output[outputPos++] = ',';
					break;
				case '\uFF0E': // ．  [FULLWIDTH FULL STOP]
					output[outputPos++] = '.';
					break;
				case '\u2044': // ⁄  [FRACTION SLASH]
				case '\uFF0F': // ／  [FULLWIDTH SOLIDUS]
					output[outputPos++] = '/';
					break;
				case '\uFF1A': // ：  [FULLWIDTH COLON]
					output[outputPos++] = ':';
					break;
				case '\u204F': // ⁏  [REVERSED SEMICOLON]
				case '\uFF1B': // ；  [FULLWIDTH SEMICOLON]
					output[outputPos++] = ';';
					break;
				case '\uFF1F': // ？  [FULLWIDTH QUESTION MARK]
					output[outputPos++] = '?';
					break;
				case '\u2047': // ⁇  [DOUBLE QUESTION MARK]
					output[outputPos++] = '?';
					output[outputPos++] = '?';
					break;
				case '\u2048': // ⁈  [QUESTION EXCLAMATION MARK]
					output[outputPos++] = '?';
					output[outputPos++] = '!';
					break;
				case '\uFF20': // ＠  [FULLWIDTH COMMERCIAL AT]
					output[outputPos++] = '@';
					break;
				case '\uFF3C': // ＼  [FULLWIDTH REVERSE SOLIDUS]
					output[outputPos++] = '\\';
					break;
				case '\u2038': // ‸  [CARET]
				case '\uFF3E': // ＾  [FULLWIDTH CIRCUMFLEX ACCENT]
					output[outputPos++] = '^';
					break;
				case '\uFF3F': // ＿  [FULLWIDTH LOW LINE]
					output[outputPos++] = '_';
					break;
				case '\u2053': // ⁓  [SWUNG DASH]
				case '\uFF5E': // ～  [FULLWIDTH TILDE]
					output[outputPos++] = '~';
					break;
				default:
					output[outputPos++] = c;
					break;
				}
			}
		}
		return output;
	}
	public static int getNextSize(int targetSize) {
		/* This over-allocates proportional to the list size, making room
		 * for additional growth.  The over-allocation is mild, but is
		 * enough to give linear-time amortized behavior over a long
		 * sequence of appends() in the presence of a poorly-performing
		 * system realloc().
		 * The growth pattern is:  0, 4, 8, 16, 25, 35, 46, 58, 72, 88, ...
		 */
		return (targetSize >> 3) + (targetSize < 9 ? 3 : 6) + targetSize;
	}
	public static void main(String[] args) {
		String test = "xxxxxx1baaaaaab1xxxxxxx";
		test = test.substring(7,15);
		System.out.println(test);
	}
}

