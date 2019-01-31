package it.iccu.sbn.opac2.mlol.integration.client.marc;

import it.iccu.sbn.opac2.mlol.integration.client.util.Consumable;
import it.iccu.sbn.opac2.mlol.integration.client.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Subfield;

public class Tag950Parser {
	
	public static class Tag950 {
		String bib;
		List<Inv950> inv = new ArrayList<>();
		List<Col950> coll = new ArrayList<>();
	}
	
	static class Col950 {
		String bib;
		String cd_sez;
		String cd_loc;
		String spec_loc;
		String consis;
		List<Inv950> inv = new ArrayList<>();
	}
	
	static class Inv950 {
		String bib;
		String cd_serie;
		int cd_inv;
		String precis_inv;
		String seq_coll;
		String cd_riproducibilita;
		String stato_con;
		String cd_mat_inv;
		String cod_no_disp;
		String cd_sit;
		String cd_frui;
		Date dt_ingresso;
		Date dt_prima_coll;
	}
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	private Col950 pendingCol = null;	//coll corrente
	private Inv950 pendingInv = null;	//inv corrente
	private Consumable<String> consis = Consumable.empty();
	
	public Tag950 parse(DataField field) {
		pendingCol = null;
		pendingInv = null;
		Tag950 tag = new Tag950();
		
			try {
			
			for (Subfield sf : field.getSubfields()) {
		
				switch (sf.getCode()) {
				case 'a':	// biblioteca
					tag.bib = sf.getData().trim();
					break;
					
				case 'c':	//consistenza di collocazione
					consis = Consumable.of(sf.getData().trim());
					break;
					
				case 'd':	// sezione + collocazione + specificazione
					pendingCol = buildColl950(sf);	//nuova coll
					//associo la consistenza del sottocampo $c, se presente
					pendingCol.consis = consis.consume();
					tag.coll.add(pendingCol);
					break;
		
				case 'e':	// inventario
					pendingInv = buildInv950(sf);
					if (pendingCol != null) {
						// sto gìà riempiendo una coll.
						// Aggiungo l'inventario alla coll in corso
						pendingCol.inv.add(pendingInv);
					} else // aggiungo a inventari non collocati
						tag.inv.add(pendingInv);
					break;
					
				case 'f':	//cat. fruizione
					if (pendingInv != null)
						pendingInv.cd_frui = sf.getData().trim();
					break;
					
				case 'h':	//data ingresso
					if (pendingInv != null)
						pendingInv.dt_ingresso = DATE_FORMAT.parse(sf.getData());
					break;
					
				case 'i':	//data prima coll
					if (pendingInv != null)
						pendingInv.dt_prima_coll = DATE_FORMAT.parse(sf.getData());
					break;
				}
		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return tag;

	}
	
	
	private Inv950 buildInv950(Subfield sf) {
		String data = sf.getData();
		Inv950 i = new Inv950();

		String cd_biblioteca = Utils.safeSubstringAndTrim(data, 0, 3);
		i.bib = cd_biblioteca.replace(" ", "");
		
		i.precis_inv = Utils.safeSubstringAndTrim(data, 44);// OK
		i.seq_coll = Utils.safeSubstringAndTrim(data, 24, 44);// OK
		i.cd_riproducibilita = Utils.safeSubstringAndTrim(data, 22, 24);// OK
		i.stato_con = Utils.safeSubstringAndTrim(data, 20, 22);// OK
		i.cd_mat_inv = Utils.safeSubstringAndTrim(data, 18, 20); // OK
		i.cod_no_disp = Utils.safeSubstringAndTrim(data, 16, 18);// OK
		i.cd_sit = Utils.safeSubstringAndTrim(data, 15, 16);// OK
		i.cd_inv = Integer.valueOf(Utils.safeSubstringAndTrim(data, 6, 15) );// OK
		i.cd_serie = Utils.safeSubstringAndTrim(data, 3, 6);// OK

		return i;
	}


	private Col950 buildColl950(Subfield sf) {
		String data = sf.getData();
		Col950 c = new Col950();

		c.bib = Utils.safeSubstringAndTrim(data, 0, 3);
		c.cd_sez = Utils.safeSubstringAndTrim(data, 3, 13);
		c.cd_loc = Utils.safeSubstringAndTrim(data, 13, 37);
		c.spec_loc = Utils.safeSubstringAndTrim(data, 37);
		
		return c;
	}

}
