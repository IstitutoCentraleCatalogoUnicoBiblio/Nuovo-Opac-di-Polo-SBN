package it.almaviva.sbnweb.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;
import it.almaviva.sbnweb.Indexer.SbnwebIndexer;
import it.almaviva.sbnweb.util.Constants;

public class SbnwebIndexerTest {
	public static void main(String[] args) throws FileNotFoundException {
		SbnwebIndexer indexer = new SbnwebIndexer("", args);
		InputStream in = new FileInputStream("" +"SBW_00011517"
				+ ".mrc");
		

		MarcReader reader = new MarcStreamReader(in);
		while (reader.hasNext()) {
			// lettura dei record
			Record record = reader.next();
			record.getLeader().setCharCodingScheme(' ');
			try {
				

				System.out.println("id: " + indexer.getSbnId(record) + " " + indexer.getNome_tot_noPoss(record));
			} catch (Exception e) {
				System.out.println("id: " + indexer.getSbnId(record));
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
