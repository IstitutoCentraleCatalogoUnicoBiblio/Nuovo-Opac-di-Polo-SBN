package it.almaviva.sbnweb.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;

import bsh.util.Util;
import it.almaviva.sbnweb.Indexer.SbnwebIndexer;
import it.iccu.sbn.opac2.mlol.integration.client.util.Utils;

//per ulteriori info su unimarc
//http://unimarc-it.wikidot.com/
public class SbnwebIndexerTest {
	public static void main(String[] args) throws FileNotFoundException {
		SbnwebIndexer indexer = new SbnwebIndexer("", args);
		InputStream in = new FileInputStream(args[0]);

		MarcReader reader = new MarcStreamReader(in);
		while (reader.hasNext()) {
			// lettura dei record
			Record record = reader.next();
			record.getLeader().setCharCodingScheme(' ');
			try {

				Set<String> classi686Tot = indexer.getAltreClassi686Tot(record, "PGI");
				if (Utils.isFilled(classi686Tot))
					System.out.println("id: " + indexer.getSbnId(record) + " " + classi686Tot);
			} catch (Exception e) {
				System.out.println("id: " + indexer.getSbnId(record));
				e.printStackTrace();
			}

		}

	}

}
