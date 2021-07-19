package it.almaviva.sbnweb.Indexer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.marc4j.marc.impl.DataFieldImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.almaviva.sbnweb.auth.cleanDatazione.DatazioneCleaner;
import it.almaviva.sbnweb.util.CleanNoSort;
import it.almaviva.sbnweb.util.Constants;
import it.almaviva.sbnweb.util.IncipitBean;
import it.almaviva.sbnweb.util.Util;
import it.iccu.sbn.opac2.mlol.integration.client.marc.Tag950Parser;
import it.iccu.sbn.opac2.mlol.integration.client.marc.Tag950Parser.Tag950;
import it.iccu.sbn.opac2.mlol.integration.client.util.Utils;
import it.inera.opacsbn.solrmarc.OpacSbnIndexer;

public class SbnwebIndexer extends OpacSbnIndexer {

	public SbnwebIndexer(String indexingPropsFile, String[] propertyDirs) {
		super(indexingPropsFile, propertyDirs);
	}

	public Set<String> getFieldCleaned(final Record record, String tagStr) {
		Set<String> values = getFieldList(record, tagStr);
		Set<String> result = new LinkedHashSet<String>();

		for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
			String value = ((String) iterator.next()).trim();
			String appo = value.replaceAll("\\|", "");
			if (appo.length() > 0)
				result.add(appo);
		}
		return result;
	}

	public String getSbnId(Record record) {

		ControlField data = (ControlField) record.getVariableField("001");
		return data.getData();
	}

	public String getSbnUnimarc(Record record) {
		// logger.info("custom SBN Unimarc");
		// OCCHIO AL CARATTERE NOSORT
		// logger.info("Estracted Record");
		return record.toString();
	}

	public Set<String> getI950(Record record) {
		// logger.info("tag950");
		// lettura dei record
		Set<String> dollI = new LinkedHashSet<String>();
		DataFieldImpl field = (DataFieldImpl) record.getVariableField("950");

		Map<String, String> dollE = new HashMap<String, String>();
		Map<String, Map<String, String>> listaMapE = new HashMap<String, Map<String, String>>();
		List<Map<String, Map<String, String>>> listaDiE = new ArrayList<Map<String, Map<String, String>>>();
		// logger.info();
		try {
			// logger.info("Trovata 950:");
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> n = subfields.iterator();
			// TODO: contatori da eliminare e creare la'array da ritornare
			// Map<String, String> map = new HashMap<String, String>();

			while (n.hasNext()) {
				Subfield subfield = (Subfield) n.next();
				char code = subfield.getCode();
				String data = subfield.getData();
				// data = "SGFabc0000120905";
				// data =
				// "SGFabc0000120905DDVMBBRRllllllllllllllllllllYYYYYYYYYY";

				switch (code) {
					case 'a':

						// logger.info("\t"+ code + "\t" + a +") '" + data+"'
						// lunghezza: " + data.length());
						break;
					case 'b':

						// logger.info("\t"+ code + "\t" + b +") '" + data+"'
						// lunghezza: " + data.length());
						break;
					case 'c':

						// logger.info("\t"+ code + "\t" + c +") '" + data+"'
						// lunghezza: " + data.length());
						break;
					case 'd':

						// logger.info("\t"+ code + "\t" + d +") '" + data+"'
						// lunghezza: " + data.length());
						// campi posizionali
						String cd_bib = null;
						cd_bib = data.substring(0, 3);
						String cd_sez = null;
						String cd_loc = null;
						String spec_loc = null;
						if (data.length() > 37) {
							spec_loc = data.substring(38);
							cd_loc = data.substring(14, 37);
							cd_sez = data.substring(4, 13);

						} else if (data.length() > 13) {
							cd_loc = data.substring(14);
							cd_sez = data.substring(4, 13);

						} else {
							cd_sez = data.substring(4);
						}
						// pulizia da spazi inutili
						spec_loc = Util.cleanStr(spec_loc);
						cd_loc = Util.cleanStr(cd_loc);
						cd_sez = Util.cleanStr(cd_sez);
						cd_bib = Util.cleanStr(cd_bib);
						/*
						 * logger.info("\n cd_bib:" + cd_bib+ "\n cd_sez: " + cd_sez + "\n cd_loc:" +
						 * cd_loc+ "\n cd_sez:"+cd_sez+ "\n spec_loc:"+spec_loc);
						 */
						break;
					case 'e':

						String cd_biblioteca = null;
						cd_biblioteca = data.substring(0, 3);
						cd_biblioteca = cd_biblioteca.replace(" ", "");
						String cd_serie = null;
						String cd_inv = null;
						String cd_sit = null;
						String cod_no_disp = null;
						String cd_mat_inv = null;
						String stato_con = null;
						String cd_riproducibilita = null;
						String seq_coll = null;
						String precis_inv = null;
						// logger.info("\t"+ code + "\t" + d +") '" + data+"'
						// lunghezza: " + data.length());

						if (data.length() > 44) {
							precis_inv = data.substring(44);// OK
							seq_coll = data.substring(24, 44);// OK
							cd_riproducibilita = data.substring(22, 24);// OK
							stato_con = data.substring(20, 22);// OK
							cd_mat_inv = data.substring(18, 20); // OK
							cod_no_disp = data.substring(16, 18);// OK
							cd_sit = data.substring(15, 16);// OK
							cd_inv = data.substring(6, 15);// OK
							cd_serie = data.substring(3, 6);// OK

						} else if (data.length() > 25) {
							seq_coll = data.substring(24);// OK
							cd_riproducibilita = data.substring(22, 24);// OK
							stato_con = data.substring(20, 22);// OK
							cd_mat_inv = data.substring(18, 20); // OK
							cod_no_disp = data.substring(16, 18);// OK
							cd_sit = data.substring(15, 16);// OK
							cd_inv = data.substring(6, 15);// OK
							cd_serie = data.substring(3, 6);// OK

						} else if (data.length() > 23) {
							cd_riproducibilita = data.substring(22);// OK
							stato_con = data.substring(20, 22);// OK
							cd_mat_inv = data.substring(18, 20); // OK
							cod_no_disp = data.substring(16, 18);// OK
							cd_sit = data.substring(15, 16);// OK
							cd_inv = data.substring(6, 15);// OK
							cd_serie = data.substring(3, 6);// OK

						} else if (data.length() > 21) {
							stato_con = data.substring(20);// OK
							cd_mat_inv = data.substring(18, 20); // OK
							cod_no_disp = data.substring(16, 18);// OK
							cd_sit = data.substring(15, 16);// OK
							cd_inv = data.substring(6, 15);// OK
							cd_serie = data.substring(3, 6);// OK

						} else if (data.length() > 19) {
							cd_mat_inv = data.substring(18); // OK
							cod_no_disp = data.substring(16, 18);// OK
							cd_sit = data.substring(15, 16);// OK
							cd_inv = data.substring(6, 15);// OK
							cd_serie = data.substring(3, 6);// OK

						} else if (data.length() > 16) {
							cod_no_disp = data.substring(16);// OK
							cd_sit = data.substring(15, 16);// OK
							cd_inv = data.substring(6, 15);// OK
							cd_serie = data.substring(3, 6);// OK

						} else if (data.length() > 15) {
							cd_sit = data.substring(15);// OK
							cd_inv = data.substring(6, 15);// OK
							cd_serie = data.substring(3, 6);// OK ;

						} else if (data.length() > 14) {
							cd_inv = data.substring(14);// OK
							cd_serie = data.substring(3, 6);// OK ;
						} else {
							cd_serie = data.substring(3);// OK ;
						}
						precis_inv = Util.cleanStr(precis_inv);
						seq_coll = Util.cleanStr(seq_coll);
						cd_riproducibilita = Util.cleanStr(cd_riproducibilita);
						stato_con = Util.cleanStr(stato_con);
						cd_mat_inv = Util.cleanStr(cd_mat_inv);
						cod_no_disp = Util.cleanStr(cod_no_disp);
						cd_sit = Util.cleanStr(cd_sit);
						cd_inv = Util.cleanStr(cd_inv);
						cd_serie = Util.cleanStr(cd_serie);

						dollE.put("cd_biblioteca", new String(cd_biblioteca));
						dollE.put("cd_serie", cd_serie);
						dollE.put("cd_inv", cd_inv);
						dollE.put("cd_sit", cd_sit);
						dollE.put("cod_no_disp", cod_no_disp);
						dollE.put("cd_mat_inv", cd_mat_inv);
						dollE.put("stato_con", stato_con);
						dollE.put("cd_riproducibilita", cd_riproducibilita);
						dollE.put("seq_coll", seq_coll);
						dollE.put("precis_inv", precis_inv);
						listaMapE.put("dollE", dollE);
						listaDiE.add(listaMapE);
						/*
						 * //logger.info("'" + stato_con + "' lunghezza: " + stato_con.length()); String
						 * allData = "cd_biblioteca:" + cd_biblioteca+ " | cd_serie: " + cd_serie +
						 * " | cd_inv:" + cd_inv+ " | cd_sit:"+cd_sit+ " | cod_no_disp:"+cod_no_disp+
						 * " | cd_mat_inv:"+cd_mat_inv+ " | stato_con:"+stato_con+
						 * " | cd_riproducibilita:"+cd_riproducibilita+ " | seq_coll:"+seq_coll+
						 * " | precis_inv:" +precis_inv; logger.info(allData);
						 * 
						 * logger.info("\n cd_bibl:'" + cd_biblioteca+ "'\n Serie: " + cd_serie +
						 * "'\n Inv:'" + cd_inv+ "'\n Sit:'"+cd_sit+ "'\n Nodisp:'"+cod_no_disp+
						 * "'\n mat_inv:'"+cd_mat_inv+ "'\n statcon:'"+stato_con+
						 * "'\n riprod:'"+cd_riproducibilita+ "'\n seq_coll:'"+seq_coll+
						 * "'\n precis_inv:'" +precis_inv);
						 */

						break;
					case 'f':

						// logger.info("\t"+ code + "\t" + f +") '" + data+"'
						// lunghezza: " + data.length());
						break;
					case 'h':

						// logger.info("\t"+ code + "\t" + h +") '" + data+"'
						// lunghezza: " + data.length());
						break;
					case 'i':
						// AL MOMENTO CI SERVE SOLO QUESTA!!
						// String d = data;
						SimpleDateFormat prePars = new SimpleDateFormat("yyyyMMdd");
						// SimpleDateFormat postPars = new
						// SimpleDateFormat("dd-MM-YYYY");
						Date convertedCurrentDate = prePars.parse(data);
						try {
							String d = prePars.format(convertedCurrentDate);
							// logger.info(dollI);
							dollI.add(d);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.info(e);
							e.printStackTrace();
						}
						// logger.info("\t"+ code + "\t" + i +") '" + data+"'
						// lunghezza: " + data.length());
						break;
					case 'l':

						// logger.info("\t"+ code + "\t" + l +") '" + data+"'
						// lunghezza: " + data.length());
						break;
					case 'm':
						break;
					default:
						logger.info("Record " + record.getVariableField("001").toString()
								+ ": Ho trovato qualcosa che non conosco: " + code);
				}
				// logger.info("Subfield code: " + code + " Data element: " +
				// data);

			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			dollI = null;
		}

		// logger.info("Trovate " + dollI.size() + " dollaro I" );

		return dollI;

	}

	public List<String> get950(Record rec) {
		Tag950Parser parser = new Tag950Parser();
		String json = new String();
		List<String> all950 = new ArrayList<String>();

		for (VariableField vf : rec.getVariableFields("950")) {
			// logger.info(String.format("950 mrc %d:%s", ++cnt950,
			// vf.toString()));
			Tag950 tag = parser.parse((DataField) vf);

			Gson gson = new GsonBuilder().create();
			json = gson.toJson(tag);
			// logger.info("950 json: " + json);
			all950.add(json);
		}
		return all950;
	}

	public List<String> getNoveSette(Record record) {
		// lettura dei record
		// logger.info("tag977");
		DataFieldImpl field = (DataFieldImpl) record.getVariableField("977");
		try {
			// logger.info(" Trovata 977:" + field.getSubfields());
			List<Subfield> subfields = field.getSubfields();
			Iterator<Subfield> i = subfields.iterator();
			List<String> codBib = new ArrayList<String>();
			while (i.hasNext()) {
				Subfield subfield = (Subfield) i.next();
				// char code = subfield.getCode();
				String data = subfield.getData();
				data = data.replace(" ", "");
				codBib.add(data);
				// logger.info("Subfield code: " + code + " Data element: " +
				// data);
			}
			return codBib;
			/*
			 * System.out.print("{"); for(String t: codBib) { System.out.print(t+","); }
			 * logger.info("}");
			 */
		} catch (Exception e) {
			// logger.info("Non presente");
			return null;
		}

		// logger.info(i+") ID:" + record.getVariableField(campo));
		// logger.info(record.toString());
	}

	// Developed by almaviva6 per il titolo

	/**
	 * 200 $acefg sintassi: " ; a . c : e / f ; g"
	 */
	public String getTitoloSint(final Record record) {
		StringBuilder buffer = new StringBuilder("");

		List<VariableField> marcFieldList = record.getVariableFields("200");
		for (VariableField vf : marcFieldList) {

			DataField marcField = (DataField) vf;

			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();

				switch (code) {
					case 'a':
						if (buffer.length() > 0)
							buffer.append(" ; ");
						buffer.append(cleanPretitolo(subfield.getData()));
						break;
					case 'c':
						if (buffer.length() > 0)
							buffer.append(" . ");
						buffer.append(subfield.getData());
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
				}

			}

		}
		// logger.info("TITOLO SINTETICO");
		return buffer.toString();
	}

	public List<String> getSbnIncipit(final Record record) {
		Gson gson = new GsonBuilder().create();

		List<String> result = new ArrayList<String>();
		List<VariableField> marcFieldList = record.getVariableFields("926");
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;
			IncipitBean incipit926 = new IncipitBean();
			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				switch (subfield.getCode()) {
					case 'n':
						incipit926.setAlterazioni(subfield.getData());
						break;
					case 'm':
						incipit926.setChiave(subfield.getData());
						break;
					case 'o':
						incipit926.setMisura(subfield.getData());
						break;
					case 'c':
						incipit926.setIncipit(subfield.getData());
						break;
					case 'p':
						incipit926.setIndMovimento(subfield.getData());
						break;
					case 'i':
						incipit926.setForma(subfield.getData());
						break;
					case 'l':
						incipit926.setTonalita(subfield.getData());
						break;
					case 'h':
						incipit926.setStrumento(subfield.getData());
						break;
					case 'f':
						incipit926.setF(subfield.getData());
						break;
					case 'g':
						incipit926.setG(subfield.getData());
						break;
					case 's':
						incipit926.setTestuale(subfield.getData());
						break;
				}
			}
			incipit926.setMovimento(null);
			if (incipit926.isValid())
				result.add(gson.toJson(incipit926));
		}
		return result;
	}

	/**
	 * Nuova versione indicizzazione Classificazione Dewey per dewey_tot, dewey_totf
	 * sintassi: 676a:676v:6769, "codice|ed.$v - $9"
	 */
	public static Set<String> getDewey_tot(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		StringBuilder foundDatas = new StringBuilder(10);

		// prendo tutti i $c, $a e $2 della 686
		List<VariableField> field = record.getVariableFields("676");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();
			List<Subfield> subfields = null;
			subfields = dataField.getSubfields('a');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				// codice.add(subfield.getData());
				foundDatas.append(subfield.getData().trim());
			}

			subfields = dataField.getSubfields('v');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				foundDatas.append("|ed.").append(subfield.getData().trim());

				subfields = dataField.getSubfields('9');
				for (Iterator<Subfield> iterator21 = subfields.iterator(); iterator21.hasNext();) {
					Subfield subfield1 = (Subfield) iterator21.next();
					// classe_app.add(subfield.getData());
					if (!subfield1.getData().equals(""))
						foundDatas.append(" - ").append(subfield1.getData().trim());

				}
			}

			result.add(foundDatas.toString().trim());
			// pulisco il buffer
			foundDatas.setLength(0);
			foundDatas.setLength(10);
		}

		return result;

	}

	/**
	 * Nuova versione indicizzazione Classificazione PEGI, 11/01/2017
	 */
	public Set<String> getClassi686Tot(final Record record, String classe) {

		Set<String> result = new LinkedHashSet<String>();

		StringBuilder foundDatas = new StringBuilder(10);

		// prendo tutti i $c, $a e $2 della 686
		List<VariableField> field = record.getVariableFields("686");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();
			List<Subfield> subfields = null;
			/*
			 * subfields = dataField.getSubfields('a'); for (Iterator<Subfield> iterator2 =
			 * subfields.iterator(); iterator2.hasNext();) { Subfield subfield = (Subfield)
			 * iterator2.next(); // codice.add(subfield.getData());
			 * foundDatas.append(subfield.getData().trim()); }
			 */
			subfields = dataField.getSubfields('c');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				foundDatas.append(subfield.getData().trim());
				// descrizioni.add(subfield.getData());
				// resultA.add(cleanPretitolo(subfield.getData()));
			}

			String classReadedFromRecord = new String();
			subfields = dataField.getSubfields('2');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				// classe_app.add(subfield.getData());
				// foundDatas.append("|").append(subfield.getData())
				classReadedFromRecord = subfield.getData();
			}
			if (classe != null && !classe.equals("")) {
				classe = classe.trim().toUpperCase();
				if (classReadedFromRecord.toUpperCase().trim().equals(classe))
					result.add(foundDatas.toString());
			}

			// pulisco il buffer
			foundDatas.setLength(0);
			foundDatas.setLength(10);
		}

		return result;
	}
	public Set<String> getClassi686Tot(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		// prendo tutti i $c, $a e $2 della 686
		List<VariableField> u686s = record.getVariableFields("686");
		for (VariableField v686 : u686s) {
			DataField u686 = (DataField) v686;
			for (Subfield sub : u686.getSubfields()) {
				switch (sub.getCode()) {
					case 'c':
						result.add(sub.getData());
						break;
	
				}
			}
		}
		return result;
	}
	/**
	 * Ritorna tutte le altre classificazioni dewey 
	 * @param record
	 * @param toExclude Sistema di classificazione da escludere, se non impostato default PGI
	 * @return Set<String>
	 */
	public Set<String> getAltreClassi686Tot(final Record record, String toExclude) {
		//Default esclude le PGI
		if(!Utils.isFilled(toExclude))
			toExclude = "";
		Set<String> result = new LinkedHashSet<String>();

		// prendo tutti i $c, $a e $2 della 686
		List<VariableField> u686s = record.getVariableFields("686");
		for (VariableField v686 : u686s) {
			DataField u686 = (DataField) v686;
			List<Subfield> subfields = null;

			StringBuilder sb = new StringBuilder();
			// $2 | $a | $c // sistema | valore | descrizione valore
			// Trovo la $2, una sola -> non ripetibile
			subfields = u686.getSubfields('2');
			// Controllo che non sia presente la classe da escludere
			Boolean isToExclude = false;
			for (Subfield sub : subfields) {
				isToExclude = toExclude.equals(sub.getData());
			}
			if (isToExclude)
				continue;
			// Trovo la $2, una sola -> non ripetibile
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				sb.append(subfield.getData().trim());
			}
			sb.append("|");
			//Trovo la $a, una sola -> non ripetibile
			subfields = u686.getSubfields('a');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				sb.append(subfield.getData().trim());
			}
			sb.append("|");
			//Trovo la $c, una sola -> non ripetibile
			subfields = u686.getSubfields('c');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				sb.append(subfield.getData().trim());
			}
			
			result.add(sb.toString());
		}
		return result;
	}

	/**
	 * estrae nome | vid | relator code | $5 etichette 700-750
	 * 
	 */
	public Set<String> getNome_tot(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		Set<String> resultNopretit = new LinkedHashSet<String>();

		String separator = "";

		/**
		 * prima parte: 700-750 a,b,c,d,e,f,g (separati da spazio)
		 */
		int startTag = 700;
		int endTag = 750;

		String subfldTags = "[a|b|c|d|e|f|g|3|4|5]";

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
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
							if (buffer.length() > 0 && !subfield.getData().startsWith(","))
								buffer.append(separator != null ? separator : "");

							if (subfield.getCode() == '3' || subfield.getCode() == '4' || subfield.getCode() == '5')
								buffer.append("|");
							if (subfield.getCode() == '4' && subfield.getData().length() > 3)
								buffer.append(subfield.getData().substring(subfield.getData().indexOf("=") + 1));
							else
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

		result = getFold(result);

		return result;
	}

	/*
	 * Estrazione del bid insieme al titolo (per creare link navigazione) titolo |
	 * bid colltit_tip_tag da 421 a 464
	 */
	public Set<String> getCollTit(final Record record, String tag) {

		return getCollTit(record, tag, null, true);
	}

	private Set<String> getCollTit(final Record record, String tag, String ind1, Boolean clean_pretit) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> marcFieldList = record.getVariableFields(tag);
		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;

			boolean firstA = true;

			boolean parentesiB = false;
			int idxLastB = 0;
			boolean firstB = true;
			boolean good = false;

			List<Subfield> subfields = marcField.getSubfields();
			StringBuilder buffer = new StringBuilder("");
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();
				String subfieldString = subfield.toString();
				if (ind1 == null && subfieldString.startsWith("$1001"))
					good = true;
				if (ind1 == null && subfieldString.startsWith("$1200"))
					good = true;
				if (subfieldString.startsWith("$17"))
					good = false;

				if (good) {
					switch (code) {

						case 'a':
							if (firstA) {
								// if (clean_pretit)
								// buffer.append(cleanPretitolo(subfield.getData()));
								// else
								buffer.append(CleanNoSort.clean(subfield.getData()));
								// buffer.append(subfield.getData().replaceAll(START_PRETITOLO,
								// "<<").replaceAll(END_PRETITOLO,">>"));
								firstA = false;
							} else {
								// if (clean_pretit)
								// buffer.append(" ;
								// ").append(cleanPretitolo(subfield.getData()));
								// else
								buffer.append(" ; ").append(CleanNoSort.clean(subfield.getData()));
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

						case '1':
							if (subfieldString.startsWith("$1001"))
								buffer.append(subfield.getData().substring(3)).append("|");
							break;
						// solo per le 410
						case 'i':
							buffer.append(subfield.getData());
							break;
						// solo per le 464 e le 410
						case 'v':
							if (tag.equals("464"))
								buffer.append("|").append(subfield.getData());
							else if (tag.equals("410"))
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
			if (buffer.length() > 0)
				result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
		}
		return result;
	}

	/**
	 * 560 $a $5 $9 sintassi: "a|5|9"
	 */
	public Set<String> getTitoloRaccolta(final Record record) {

		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> marcFieldList = record.getVariableFields("560");
		for (VariableField vf : marcFieldList) {

			DataField marcField = (DataField) vf;

			List<Subfield> subfields = marcField.getSubfields();
			StringBuilder buffer = new StringBuilder("");
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();

				switch (code) {
					case 'a':
						buffer.append(subfield.getData());
						break;
					case '5':
						if (buffer.length() > 0)
							buffer.append("|").append(subfield.getData());
						break;
					case '9':
						if (buffer.length() > 0)
							buffer.append("|").append(subfield.getData());
						break;
				}
			}
			if (buffer.length() > 0)
				result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());

		}
		return result;
	}

	/**
	 * 560 $a
	 */
	public Set<String> getTitoloRaccoltaf(final Record record) {

		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> marcFieldList = record.getVariableFields("560");
		for (VariableField vf : marcFieldList) {

			DataField marcField = (DataField) vf;

			List<Subfield> subfields = marcField.getSubfields();
			StringBuilder buffer = new StringBuilder("");
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();

				switch (code) {
					case 'a':
						buffer.append(subfield.getData());
						break;
				}
			}
			if (buffer.length() > 0)
				result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());

		}
		return result;
	}

	/**
	 * 510 $a $e $n sintassi: "a__e__n", " ; __ : __ "
	 */
	public Set<String> getTitoloParallelo(final Record record) {

		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> marcFieldList = record.getVariableFields("510");
		for (VariableField vf : marcFieldList) {

			DataField marcField = (DataField) vf;

			List<Subfield> subfields = marcField.getSubfields();
			StringBuilder buffer = new StringBuilder("");
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();

				switch (code) {
					case 'a':
						buffer.append(subfield.getData());
						break;
					case 'e':
						buffer.append(" ; ").append(subfield.getData());
						break;
					case 'n':
						buffer.append(" : ").append(subfield.getData());
						break;
				}
			}
			if (buffer.length() > 0)
				result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());

		}
		return result;
	}

	/*
	 * Estrazione da 461/462 delle stringhe "fa parte di" da inserire nella parte
	 * sintetica della scheda
	 */
	public Set<String> getFaParte(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		int startTag = 461;
		int endTag = 462;

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			for (VariableField vf : marcFieldList) {
				DataField marcField = (DataField) vf;

				boolean firstA = true;

				boolean parentesiB = false;
				int idxLastB = 0;
				boolean firstB = true;
				boolean good = false;

				List<Subfield> subfields = marcField.getSubfields();
				StringBuilder buffer = new StringBuilder("");
				for (Subfield subfield : subfields) {
					char code = subfield.getCode();
					String subfieldString = subfield.toString();
					if (subfieldString.startsWith("$1001"))
						good = true;
					if (subfieldString.startsWith("$1200"))
						good = true;
					if (subfieldString.startsWith("$17"))
						good = false;

					if (good) {
						switch (code) {

							case 'a':
								if (firstA) {
									buffer.append(CleanNoSort.clean(subfield.getData()));
									firstA = false;
								} else {
									buffer.append(" ; ").append(CleanNoSort.clean(subfield.getData()));

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
				if (buffer.length() > 0)
					result.add(String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
			}
		}
		return result;
	}

	/*
	 * Trattamento di data1 e data2 nel tag "100" scrittura di $k a partire da data1
	 * e $w a partire da data2
	 * 
	 * se data2 = spazio se data1 numero $k=data1 $w=data1 se data1 nn.. $k=nn00
	 * $w=nn99 se data1 nnn. $k=nnn0 $w=nnn9
	 * 
	 * se data2 > spazio se data1 numero e data2 numero $k=data1 $w=data2 se data1
	 * numero e data2 nn.. $k=data1 $w=nn99 se data1 numero e data2 nnn. $k=data1
	 * $w=nnn9 se data1 nn.. e data2 numero $k=nn00 $w=data2 se data1 nnn. e data2
	 * numero $k=nnn0 $w=data2 se data1 nn.. e data2 mm.. $k=nn00 $w=mm99
	 */
	public String elaboraData1Data2(final Record record, String tagStr, String quale) {
		Set<String> values = getFieldList(record, tagStr);
		Iterator<String> iterator = values.iterator();
		String data = "";
		String tpdata = "";
		String data1 = "";
		String data2 = "";
		String result_data = "";

		if (iterator.hasNext()) {
			data = (String) iterator.next();
			tpdata = data.substring(0, 1);
			data1 = data.substring(1, 5).replaceAll(" ", "");
			data2 = data.substring(5, 9).replaceAll(" ", "");
		}
		// System.out.println("------------" + tpdata + data1 + data2);
		if (data1.equals("") && data2.equals("")) // situazione che non dovrebbe
													// esserci
			return "";

		// per tipo data E (che ha data1 > data2) si tiene conto soltanto della
		// data1
		// per tipo data A (che l'export esporta con data2=9999) si tiene conto
		// soltanto della data1
		if (tpdata.equals("e") || tpdata.equals("a"))
			data2 = "";

		if (data2.trim().equals("")) { // data2 a spazio
			if (Util.isNumeric(data1)) { // data1 numerica
				result_data = data1 + "|" + data1; // primo elemento = secondo
													// elem = data1
			} else if (data1.substring(2, 4).equals("..")) {
				// data1 contiene caratteri .. (due punti) in posizione 3 e 4
				result_data = data1.substring(0, 2) + "00" + "|" + data1.substring(0, 2) + "99";
			} else if (!data1.substring(2, 3).equals(".") && data1.substring(3, 4).equals(".")) {
				// data1 contiene carattere . (punto) in posizione 4
				result_data = data1.substring(0, 3) + "0" + "|" + data1.substring(0, 3) + "9";
			}
		} // fine data2 a spazio
		else { // data2 diversa da spazio
			if (Util.isNumeric(data1) && Util.isNumeric(data2)) { // data1 e
																	// data2
																	// numeriche
				result_data = data1 + "|" + data2;
			} else if (Util.isNumeric(data1) && !Util.isNumeric(data2)) { // data1
																			// numerica
																			// e
																			// data2
																			// non
																			// numerica
				if (data2.substring(2, 4).equals("..")) {
					// data2 contiene caratteri .. (due punti) in posizione 3 e
					// 4
					result_data = data1 + "|" + data2.substring(0, 2) + "99";
				} else if (!data2.substring(2, 3).equals(".") && data2.substring(3, 4).equals(".")) {
					// data2 contiene carattere . (punto) in posizione 4
					result_data = data1 + "|" + data2.substring(0, 3) + "9";
				}
			} // fine data1 numerica e data2 non numerica
			else if (!Util.isNumeric(data1) && Util.isNumeric(data2)) { // data1
																		// non
																		// numerica
																		// e
																		// data2
																		// numerica
				if (data1.substring(2, 4).equals("..")) {
					// data1 contiene caratteri .. (due punti) in posizione 3 e
					// 4
					result_data = data1.substring(0, 2) + "00" + "|" + data2;
				} else if (!data1.substring(2, 3).equals(".") && data1.substring(3, 4).equals(".")) {
					// data1 contiene carattere . (punto) in posizione 4
					result_data = data1.substring(0, 3) + "0" + "|" + data2;
				}
			} // fine data1 non numerica e data2 numerica
			else if (!Util.isNumeric(data1) && !Util.isNumeric(data2)) { // data1
																			// e
																			// data2
																			// non
																			// numeriche
				if (data1.substring(2, 4).equals("..")) {
					// data1 contiene caratteri .. (due punti) in posizione 3 e
					// 4
					if (!data2.substring(2, 3).equals(".") && data2.substring(3, 4).equals(".")) {
						result_data = data1.substring(0, 2) + "00" + "|" + data2.substring(0, 3) + "9";
					} else if (data2.substring(2, 4).equals("..")) {
						result_data = data1.substring(0, 2) + "00" + "|" + data2.substring(0, 2) + "99";
					}
				} else if (!data1.substring(2, 3).equals(".") && data1.substring(3, 4).equals(".")) {
					// data1 contiene carattere . (punto) in posizione 4
					if (!data2.substring(2, 3).equals(".") && data2.substring(3, 4).equals(".")) {
						result_data = data1.substring(0, 3) + "0" + "|" + data2.substring(0, 3) + "9";
					} else if (!data2.substring(2, 4).equals("..")) {
						result_data = data1.substring(0, 3) + "0" + "|" + data2.substring(0, 2) + "99";
					}
				}
			} // fine data1 e data2 non numeriche

		} // fine data2 diversa da spazio

		if (quale.equals("1"))
			return result_data = result_data.substring(0, 4);
		else if (quale.equals("2"))
			return result_data = result_data.substring(5, 9);
		else
			return result_data;

	}
	/*
	 * AUTHORITY AUTORI: Elaborazione della "datazione" estratta dalla 300$a (parte
	 * prima di //) o, in subordine, dalla 200$f oppure 210$f per definire gli
	 * intervalli di date di nascita/morte di un autore/ente (certi o presunti).
	 * 
	 * La funzione prevede come parametro il codice convenzionale della data da
	 * "determinare": n1 (inizio intervallo data nascita), n2 (fine intervallo data
	 * nascita), m1 (inizio intervallo data morte), m2 (fine intervallo data morte)
	 * Caso normale : NNNN-MMMM --> n1=n2=NNNN, m1=m2=MMMM Caso solo nascita: NNNN-
	 * --> n1=n2=NNNN Caso solo morte: -MMMM --> m1=m2=MMMM Caso con secolo: sec.16
	 * --> n1=1501, n2=1600, m1=1501, m2=1600 Caso tra due secoli: sec.19.-20. -->
	 * n1=1801, n2=1900, m1=1901, m2=2000
	 * 
	 * Eliminate prima dell'estrazione delle date le stringhe: "ca." "c." "?"
	 * Interpretate le stringhe "n." (equivale a nascita) e "m." (equivale a morte)
	 * Escluse le datazioni con "fl."
	 * 
	 */

	public String getDatazioneAUTH(final Record record, String qualedata) {

		String result = getDatazioneAUTH(record);
		String result2 = "";

		// esamina result
		if (result.length() > 0) {
			result = result.toLowerCase();
			DatazioneCleaner cleaner = new DatazioneCleaner(result);
			String appo2 = cleaner.cleanedString();
			// System.out.println("---DatazioneCleaner:" + appo2 );
			if ((appo2.length() < 2) || (appo2.length() > 0 && appo2.substring(0, 2).equals("-1")))
				appo2 = "";

			if (appo2.length() > 0 && appo2.contains("-sec")) { // gli anni
																// estratti
																// rappresentano
																// il secolo
				appo2 = appo2.replace("-sec", "");
				String[] nums = appo2.split("-");
				if (nums.length > 0) {
					if (qualedata.equals("n1") || qualedata.equals("m1"))
						if (Util.isNumeric(nums[0]))
							result2 = nums[0];
						else
							result2 = "";
					if (qualedata.equals("n2") || qualedata.equals("m2"))
						if (Util.isNumeric(nums[1]))
							result2 = nums[1];
						else
							result2 = "";
				}
			} else if (appo2.length() > 0) { // gli anni estratti sono veri
				String[] nums = appo2.split("-");
				if (nums.length > 0) {
					if (qualedata.equals("n1") || qualedata.equals("n2"))
						if (Util.isNumeric(nums[0]))
							result2 = nums[0];
						else
							result2 = "";
					if ((qualedata.equals("m1") || qualedata.equals("m2")) && nums.length > 1)
						if (Util.isNumeric(nums[1]))
							result2 = nums[1];
						else
							result2 = "";
				}
			}
		}
		// if (result2.length() > 0)
		// System.out.println("---result2:" + result2 );
		return result2;

	}

	/*
	 * AUTHORITY AUTORI: Estrazione della "datazione" dalla 300$a (parte prima di
	 * //) oppure, in subordine, dalla 200$f oppure dalla 210$f
	 * 
	 */
	public String getDatazioneAUTH(final Record record) {

		String result = "";
		StringBuilder buffer = new StringBuilder("");
		List<VariableField> marcFieldList3 = record.getVariableFields("300");
		for (VariableField vf : marcFieldList3) {
			DataField marcField = (DataField) vf;

			List<Subfield> subfields = marcField.getSubfields();
			for (Subfield subfield : subfields) {
				char code = subfield.getCode();

				switch (code) {
					case 'a':
						buffer.append(subfield.getData());
						break;
					default:
						break;
				}
			}
		}
		if (buffer.length() > 0) {
			String appo = (String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
			if (appo.indexOf("//") > 0) {
				appo = appo.substring(0, appo.indexOf("//")).trim();
				result = appo;
				// System.out.println("DATAZIONE 300a " + result );
			} else {
				buffer = new StringBuilder("");
			}
		}

		if (result.length() == 0) { // non c'� 300$a cerca in 200$f
			List<VariableField> marcFieldList2 = record.getVariableFields("200");
			for (VariableField vf : marcFieldList2) {
				DataField marcField = (DataField) vf;

				List<Subfield> subfields = marcField.getSubfields();
				for (Subfield subfield : subfields) {
					char code = subfield.getCode();

					switch (code) {
						case 'f':
							buffer.append(subfield.getData());
							break;
						default:
							break;
					}
				}
			}
			if (buffer.length() > 0) {
				result = (String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
				// System.out.println("DATAZIONE 200f " + result );
			}
		}

		if (result.length() == 0) { // non c'� 300$a, nemmeno 200$f, cerca in
									// 210$f
			List<VariableField> marcFieldList2 = record.getVariableFields("210");
			for (VariableField vf : marcFieldList2) {
				DataField marcField = (DataField) vf;

				List<Subfield> subfields = marcField.getSubfields();
				for (Subfield subfield : subfields) {
					char code = subfield.getCode();

					switch (code) {
						case 'f':
							buffer.append(subfield.getData());
							break;
						default:
							break;
					}
				}
			}
			if (buffer.length() > 0) {
				result = (String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
				// System.out.println("DATAZIONE 210f " + result );
			}
		}
		return result;
	}

	/*
	 * Determina il contenuto del campo "nome" non comprendendo anche il campo 500$9
	 * (che in Polo � un vid) Parti comprese: prima parte: 700-750 a,b,c,d,e,f,g
	 * (separati da spazio) seconda parte: 790-791 b <c> : d : g f â� ’ z terza
	 * parte: 927 c quarta parte: 411-499 prefix 1700, 1710 a,b,c,d,e,f,g (separati
	 * da spazio) non compresa: quinta parte: 500 $9
	 */
	public Set<String> getNome_bis(final Record record) {
		Set<String> result = getNomeGenerico_bis(record, null, false, false);
		return result;
	}

	/**
	 * come getNome_bis ma senza 790-791
	 */
	public Set<String> getNomef_bis(final Record record) {
		Set<String> result = getNomeGenerico_bis(record, null, true, false);
		return result;
	}

	public Set<String> getNomeGenerico_bis(final Record record, String $4, boolean nomef, boolean nomeVid) {
		Set<String> result = new LinkedHashSet<String>();
		String separator = "";
		if ("cbl".equals(project))
			separator = " ";

		/**
		 * prima parte: 700-750 a,b,c,d,e,f,g (separati da spazio)
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

		// se Ã¨ nomef non passo da qui...
		if (!nomef) {
			/**
			 * seconda parte: 790-791 b <c> : d : g f â� ’ z
			 */
			startTag = 790;
			endTag = 791;
			subfldTags = "[a|b|c|d|e|g|f|z|y]";

			if (nomeVid) { // $3<---- vid
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

									/*
									 * // se il subfield Ã¨ c controllo < > if ('c' == subfield.getCode()) { if
									 * (!data.trim().startsWith("<")) data = "<".concat(data); if
									 * (!data.trim().endsWith(">")) data = data.concat(">"); }
									 * 
									 * // se il subfield Ã¨ d o g metto i due punti if ('d' == subfield.getCode() ||
									 * 'g' == subfield.getCode()) { data = " : ".concat(data); }
									 */
									// se il subfield Ã¨ z metto â� ’
									if ('z' == subfield.getCode()) {
										data = " -> ".concat(data);
									}
									if ('3' == subfield.getCode()) {
										vid = data;
									} else if ('y' == subfield.getCode()
											&& !buffer.toString().trim().equals(data.trim())) {
										buffer.delete(0, buffer.length());

										if (!nomeVid) {
											buffer.append(data);
										} else
											vid = "";
									} else
										// b e f entrano come arrivano...
										buffer.append(data);
								}
							}
							if (nomeVid && !vid.isEmpty()) {
								result.add(vid);

							} else if (buffer.length() > 0) {
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
		if (!"cbl".equals(project)) {
			/**
			 * terza parte 927c // $3<----
			 */
			char subField = 'c';

			if (nomeVid) { // $3<---- vid
				subField = '3';
			}
			if ($4 == null) { // solo se $4 == null, sono in nome e non in
								// nome_rel_$4
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
			 * quarta parte: 411-499 prefix 1 700, 1 710 a,b,c,d,e,f,g (separati da spazio)
			 */
			if ($4 == null) { // solo se $4 == null, sono in nome e non in
								// nome_rel_$4

				startTag = 410;
				endTag = 499;
				subfldTags = "[a|b|c|d|e|f|g]";
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
							boolean good = false;
							StringBuilder buffer = new StringBuilder("");
							List<Subfield> subfields = marcField.getSubfields();
							for (Subfield subfield : subfields) {
								String subfieldString = subfield.toString();
								if (good && buffer.length() > 0 && subfieldString.startsWith("$17")) {
									result.add(buffer.toString());
									buffer = new StringBuilder("");
									// buffer.delete(0, buffer.length());
								}
								if (subfieldString.startsWith("$17"))
									good = true;
								if (subfieldString.startsWith("$1200"))
									good = false;
								if (tag == 463
										&& (subfieldString.startsWith("$1702") || subfieldString.startsWith("$1712")))
									good = false;
								if (good) {
									/*
									 * if (new_field && buffer.length() > 0) { result.add(buffer.toString()); //
									 * buffer.delete(0, buffer.length()); buffer = new StringBuilder(""); }
									 */
									Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
									if (matcher.matches()) {
										if (buffer.length() > 0 && !subfield.getData().startsWith(","))
											buffer.append(separator != null ? separator : "");
										if (subfieldString.startsWith("$171"))
											buffer.append(cleanPretitolo(subfield.getData()));
										else
											buffer.append(subfield.getData());
									}
								}
							}
							if (buffer.length() > 0)
								result.add(buffer.toString());
						}
					}
				}
			}

		}
		result = getFold(result);

		return result;
	}
	// fine getNome_bis

	/**
	 * Nuova versione indicizzazione soggetti: CID | descrizione | soggettario
	 * 600-675 a, - x ,b, c, d
	 * 
	 * @param record
	 * @return
	 */
	public Set<String> getSoggetto_tot(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		String separator = " ";

		/**
		 * 600-675 a, - x ,b, c, d (separati da spazio) e non 620
		 */
		int startTag = 600;
		int endTag = 675;
		String subfldTags = "[a|x|b|c|d|2|3]"; /*
												 * "[a|x|b|c|d|2] non necessario ticket
												 */

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
							String data3 = "";

							// se il subfield Ã¨ x metto -
							if ('x' == subfield.getCode()) {
								data = "- ".concat(data);
							}

							if ('2' == subfield.getCode()) {
								data = "|".concat(data);
							}

							if ('3' == subfield.getCode()) {
								data3 = data.concat("|");
							}

							if (data3.length() > 0)
								buffer.insert(0, data3);
							else
								buffer.append(data);
						}
					}
					if (buffer.length() > 0)
						result.add(
								String.valueOf(foldToASCII(buffer.toString().toCharArray(), buffer.length())).trim());
				}

			}
		}

		return result;
	}

	public Set<String> getFormatoElettronicof(Record record) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> u956s = record.getVariableFields("956");
		List<VariableField> u899s = record.getVariableFields("899");

		if (u899s != null && u899s.size() > 0) {

			for (VariableField u899 : u899s) {
				if ((((DataField) u899)).getSubfield('u') != null) {
					result.add("DC");
				}
			}

		}
		if (u956s != null && u956s.size() > 0) {

			for (VariableField u956 : u956s) {
				if ((((DataField) u956)).getSubfield('u') != null) {
					result.add("DC");
				}
			}

		}
		// result.add("DC");
		List<VariableField> u856s = record.getVariableFields("856");

		if (u856s != null && u856s.size() > 0) {

			for (VariableField u856 : u856s) {
				if ((((DataField) u856)).getSubfield('u') != null) {
					result.add("B");
				}
			}

		}
		// result.add("B");
		return result;
	}

	/**
	 * Indicizzazione risorse digitali etichetta 899: sintassi $a|$b|$u|indicatore2
	 * decodificato
	 */
	public Set<String> getFormatoElettronico899(final Record record) {

		Set<String> result = new LinkedHashSet<String>();

		// prendo tutti i $a, $b, $u
		List<String> resultA = new ArrayList<String>();
		List<String> resultB = new ArrayList<String>();
		List<String> resultU = new ArrayList<String>();
		List<String> resultI = new ArrayList<String>();

		List<VariableField> field = record.getVariableFields("899");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();

			List<Subfield> subfields = dataField.getSubfields('a');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				resultA.add(subfield.getData());
			}

			subfields = dataField.getSubfields('b');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				resultB.add("IT-" + subfield.getData());
			}

			subfields = dataField.getSubfields('u');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				// fix per multipli url separati da pipe almaviva3 e
				// almaviva4
				String data = subfield.getData();
				if (Utils.isFilled(data))
					resultU.add(data.replace(" | ", Constants.separatore_url_899_no_pipe));
			}
			Character ind2 = dataField.getIndicator2();
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext(); iterator2.next()) {
				resultI.add(ind2.toString());
				/*
				 * if (ind2 == '0') resultI.add("dig. parziale"); else if (ind2 == '1')
				 * resultI.add("dig. totale"); else if (ind2 == '2')
				 * resultI.add("copia di 'born digital'");
				 */
			}
		}

		// rimetto insieme i risultati
		for (int i = 0; i < resultA.size(); i++) {
			// Fix per $b in 899 all'interno di tesi di laurea non presente
			// almaviva3 13/07/2020 se $u non presente non indicizzare
			if (!"".equals(Util.isFilled899(resultU, i).trim()))
				result.add(Util.isFilled899(resultA, i).concat("|").concat(Util.isFilled899(resultB, i)).concat("|")
						.concat(Util.isFilled899(resultU, i)).concat("|").concat(Util.isFilled899(resultI, i)));
		}

		return result;
	}

	/**
	 * Indicizzazione risorse digitali etichetta 956: sintassi $e|$u
	 */
	public Set<String> getFormatoElettronico956(final Record record) {

		Set<String> result = new LinkedHashSet<String>();

		// prendo tutti i $e, $u
		Vector<String> resultE = new Vector<String>();
		Vector<String> resultU = new Vector<String>();

		List<VariableField> field = record.getVariableFields("956");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();

			List<Subfield> subfields = dataField.getSubfields('e');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				resultE.add(subfield.getData());
			}
			subfields = dataField.getSubfields('u');
			for (Iterator<Subfield> iterator2 = subfields.iterator(); iterator2.hasNext();) {
				Subfield subfield = (Subfield) iterator2.next();
				resultU.add(subfield.getData());
			}
		}

		// rimetto insieme i risultati
		for (int i = 0; i < resultE.size(); i++) {
			result.add(resultE.get(i).concat("|").concat(resultU.get(i)));
		}

		return result;
	}

	/**
	 * Indicizzazione risorse digitali etichetta 856: sintassi $e|$u
	 */
	public Set<String> getFormatoElettronico856(final Record record) {

		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> field = record.getVariableFields("856");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();

			Subfield u = dataField.getSubfield('u');
			Subfield due = dataField.getSubfield('2');
			if (due != null && due.getData().indexOf("http") == 0) {
				result.add(due.getData());
			} else {
				result.add(u.getData());
			}
		}

		return result;
	}

	/**
	 * Indicizzazione risorse digitali etichetta 856 con $2http
	 * in prospettazione sarà "concatenata" alla 899
	 * TD*
	 */
	public Set<String> getFormatoElettronicoTesiDottorato(final Record record) {

		Set<String> result = new LinkedHashSet<String>();

		List<VariableField> field = record.getVariableFields("856");
		for (Iterator<VariableField> iterator = field.iterator(); iterator.hasNext();) {
			DataField dataField = (DataField) iterator.next();

			Subfield u = dataField.getSubfield('u');
			Subfield due = dataField.getSubfield('2');
			if (due != null && due.getData().indexOf("http") == 0) {
				result.add("Deposito legale| |" + u.getData() + "|2");
			}
		}

		return result;
	}

	/**
	 * Controllo se � un libretto
	 * 
	 * @param record
	 * @return String
	 */
	public String getLibretto(Record record) {
		StringBuilder builder = new StringBuilder(5);

		List<VariableField> marcFieldList = record.getVariableFields("105");
		if (marcFieldList.isEmpty())
			marcFieldList = record.getVariableFields("140");
		if (marcFieldList.isEmpty())
			return null;

		for (VariableField vf : marcFieldList) {
			DataField marcField = (DataField) vf;

			List<Subfield> subfields = marcField.getSubfields();
			for (Iterator<Subfield> it = subfields.iterator(); it.hasNext();) {

				builder.append(it.next().getData());
			}
		}
		return (builder.toString().indexOf("i") > -1 || builder.toString().indexOf("da") > -1) ? "Y" : null;

	}

	/**
	 * materiale inventariale (950$e[18-19])
	 */
	public List<String> getMatInv950(final Record record) {

		// lettura dei record
		List<String> matInv = new ArrayList<String>();
		List<VariableField> fields = record.getVariableFields("950");

		try {
			for (VariableField field : fields) {
				List<Subfield> subfields = ((DataField) field).getSubfields();
				Iterator<Subfield> n = subfields.iterator();
				while (n.hasNext()) {
					Subfield subfield = (Subfield) n.next();
					char code = subfield.getCode();
					String data = subfield.getData();

					if (code == 'e') {
						String cd_mat_inv = null;
						if (data.length() > 19) {
							cd_mat_inv = data.substring(18, 20);
						}

						if (cd_mat_inv.trim().length() > 0) {
							if (!matInv.contains(cd_mat_inv))
								matInv.add(cd_mat_inv);
						}
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ((matInv.size() > 0) ? matInv : null);
	}

	// relatorCode
	public Set<String> getRelatorCode(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		int startTag = 700;
		int endTag = 750;

		String subfldTags = "[a|b|c|d|e|f|g|3|4|5]";

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;

					List<Subfield> subfields = marcField.getSubfields();
					for (Subfield subfield : subfields) {
						Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
						if (matcher.matches()) {
							if (subfield.getCode() == '4')
								// buffer.append(subfield.getData());
								result.add((subfield.getData().length() > 3)
										? subfield.getData().substring(subfield.getData().indexOf("=") + 1)
										: subfield.getData());

						}
					}
					/*
					 * if (buffer.length() > 0) { if (tag > 709 && tag < 720)
					 * result.add(cleanPretitolo(buffer.toString())); else
					 * result.add(buffer.toString()); }
					 */

				}

			}
		}

		// result = getFold(result);

		return (result.size() > 0) ? result : null;
	}

	// Metodo per le classificazioni dewey authority
	public static List<String> getClassificazioniDeweyf(Record record, String num_of_chars) {
		int int_num_of_chars = 1;
		try {
			int_num_of_chars = Integer.parseInt(num_of_chars);
		} catch (Exception e) {
			return null;
		}
		if (int_num_of_chars > 3)
			return null;

		List<String> result = new ArrayList<>();
		Set<String> deweys = getDewey_tot(record);
		for (String dewey : deweys) {
			String dewey_code = dewey.substring(0, dewey.indexOf("|"));
			result.add(dewey_code.substring(0, int_num_of_chars));
		}
		return result;
	}
	/* 20180517 nuovi indici */

	/*
	 * Indice per faccetta su relatorCode con esclusione di possessori 390 e
	 * provenienze 320
	 */
	public Set<String> getRelatorCode_noPoss(final Record record) {
		Set<String> result = new LinkedHashSet<String>();

		int startTag = 700;
		int endTag = 750;

		String subfldTags = "[a|b|c|d|e|f|g|3|4|5]";
		Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;

					List<Subfield> subfields = marcField.getSubfields();
					for (Subfield subfield : subfields) {
						Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
						if (matcher.matches()) {
							if (subfield.getCode() == '4') {
								// buffer.append(subfield.getData());
								if (!subfield.getData().equals("390") && !subfield.getData().equals("320")) {
									result.add((subfield.getData().length() > 3)
											? subfield.getData().substring(subfield.getData().indexOf("=") + 1)
											: subfield.getData());
								}

							}
						}
					}

				}

			}
		}

		// result = getFold(result);
		return (result.size() > 0) ? result : null;
	}

	/*
	 * Indici per faccetta su nome con esclusione dei nomi che hanno relator code di
	 * possessori 390 e provenienze 320
	 * 
	 * 
	 */

	public Set<String> getNomef_noPoss(final Record record) {
		Set<String> result = getNomeGenerico_noPoss(record, null, "noPoss");
		return (result.size() == 0) ? null : result;
	}

	public Set<String> getNome_noPoss(final Record record) {
		Set<String> result = getNomeGenerico_noPoss(record, null, "noPoss");
		return result;
	}

	public Set<String> getNomeGenerico_noPoss(final Record record, String $4, String noPoss) {
		Set<String> result = new LinkedHashSet<String>();
		String separator = "";
		if ("sbnweb".equals(project))
			separator = " ";

		/**
		 * prima parte: 700-750 a,b,c,d,e,f,g (separati da spazio)
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

					if (isNome_noPoss(marcField, noPoss)) {
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

		if (!"sbnweb".equals(project)) {
			/**
			 * terza parte 927c // $3<----
			 */
			char subField = 'c';

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
			 * quarta parte: 411-499 prefix 1 700, 1 710 a,b,c,d,e,f,g (separati da spazio)
			 */
			if ($4 == null) { // solo se $4 == null, sono in nome e non in nome_rel_$4

				startTag = 410;
				endTag = 499;
				subfldTags = "[a|b|c|d|e|f|g]";

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
								if (good && buffer.length() > 0 && subfieldString.startsWith("$17")) {
									result.add(buffer.toString());
									buffer = new StringBuilder("");
									// buffer.delete(0, buffer.length());
								}
								if (subfieldString.startsWith("$17"))
									good = true;
								if (subfieldString.startsWith("$1200"))
									good = false;
								if (tag == 463
										&& (subfieldString.startsWith("$1702") || subfieldString.startsWith("$1712")))
									good = false;
								if (good) {
									/*
									 * if (new_field && buffer.length() > 0) { result.add(buffer.toString()); //
									 * buffer.delete(0, buffer.length()); buffer = new StringBuilder(""); }
									 */
									if (isNome_noPoss(marcField, noPoss)) {
										Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
										if (matcher.matches()) {
											if (buffer.length() > 0 && !subfield.getData().startsWith(","))
												buffer.append(separator != null ? separator : "");
											if (subfieldString.startsWith("$171"))
												buffer.append(cleanPretitolo(subfield.getData()));
											else
												buffer.append(subfield.getData());
										}
									}
								}
							}
							if (buffer.length() > 0)
								result.add(buffer.toString());
						}
					}
				}
			}

			/**
			 * quinta parte: 500 $9
			 */
			subField = '9';

			if ($4 == null) { // solo se $4 == null, sono in nome e non in nome_rel_$4
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
	 * Se noPoss == null return true; Se noPoss != null &&
	 * marcField.getSubfields('4') != 390 && !320 return true else return false;
	 */
	public boolean isNome_noPoss(DataField marcField, String noPoss) {
		boolean test = true;
		if (noPoss != null) {
			test = false;
			List<Subfield> checkSubfields = marcField.getSubfields('4');
			if (checkSubfields.size() > 0) {
				for (Subfield checkSubfield : checkSubfields) {
					String $4_data = checkSubfield.getData();
					// if (noPoss.equalsIgnoreCase(checkSubfield.getData()))
					if (!$4_data.equals("390") && !$4_data.equals("320"))
						test = true;
				}
			} else {
				test = true;
			}
		}
		return test;
	}

	/* 20180517 - fine */
	/* 20180910 - dall’indice nome_tot sono esclusi possessori/provenienze */
	public Set<String> getNome_tot_noPoss(final Record record) {
		Set<String> result = new LinkedHashSet<String>();
		Set<String> resultNopretit = new LinkedHashSet<String>();

		String separator = "";

		/**
		 * prima parte: 700-750 a,b,c,d,e,f,g (separati da spazio)
		 */
		int startTag = 700;
		int endTag = 791;// 750;

		String subfldTags = "[a|b|c|d|e|f|g|3|4|5]";

		// itero sui tag
		for (int tag = startTag; tag <= endTag; tag++) {
			List<VariableField> marcFieldList = record.getVariableFields(String.valueOf(tag));
			if (!marcFieldList.isEmpty()) {
				Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
				for (VariableField vf : marcFieldList) {
					DataField marcField = (DataField) vf;
					if (isNome_noPoss(marcField, "noPoss")) {
						StringBuilder buffer = new StringBuilder("");
						List<Subfield> subfields = marcField.getSubfields();
						for (Subfield subfield : subfields) {
							Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
							if (matcher.matches()) {
								if (buffer.length() > 0 && !subfield.getData().startsWith(","))
									buffer.append(separator != null ? separator : "");

								if (subfield.getCode() == '3' || subfield.getCode() == '4' || subfield.getCode() == '5')
									buffer.append("|");
								if (subfield.getCode() == '4' && subfield.getData().length() > 3)
									buffer.append(subfield.getData().substring(subfield.getData().indexOf("=") + 1));
								else
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

		result = getFold(result);

		return result;
	}

}
