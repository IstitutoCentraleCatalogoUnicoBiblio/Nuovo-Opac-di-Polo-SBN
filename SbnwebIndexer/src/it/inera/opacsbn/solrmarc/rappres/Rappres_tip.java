package it.inera.opacsbn.solrmarc.rappres;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.solrmarc.tools.Utils;

/**
 * 922 p,q,r,s,t,u con $a = 2
 * @author reschini
 *
 */
public class Rappres_tip {
	
	public static Set<String> getResult(final Record record, String tag, String subfieldName, String checkSubfieldName, String checkSubfieldValue) {
		Set<String> result = new LinkedHashSet<String>();
		List<VariableField> marcFieldList = record.getVariableFields(tag);
		
		Pattern subfieldPattern = Pattern.compile(subfieldName); 
		if (!marcFieldList.isEmpty()) {
			for (VariableField vf : marcFieldList) {
				DataField marcField = (DataField) vf;
				Subfield checkSubfield = marcField.getSubfield(checkSubfieldName.charAt(0));
				if (checkSubfield != null && checkSubfieldValue.equalsIgnoreCase(checkSubfield.getData().trim())) {
					
					StringBuffer buffer = new StringBuffer();
					List<Subfield> subfields = marcField.getSubfields();
	    			for (Subfield subfield : subfields) {
	    				Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
	    				if (matcher.matches()) {
	    					
	    					if (buffer.length() > 1)
	    						buffer.append("" != null ? "" : " ");
	    					String data = subfield.getData().trim();
	    					buffer.append(data);
	    				}
	    			}
	    			if (buffer.length() > 0)
	    				result.add(buffer.toString());
					
				}
			}
		}
		
		
		
		return result;
	}

}
