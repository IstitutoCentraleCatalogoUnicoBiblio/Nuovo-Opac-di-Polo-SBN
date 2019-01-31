package it.inera.opacsbn.solrmarc.numeri;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;

public class Numeri_tip {
	
	public static Set<String> getResult(final Record record, String tag, String checkIndicator, String checkSubfield, String checkDataSubfield, String dataSubfield) {
		Set<String> result = new LinkedHashSet<String>();
		
		List<VariableField> marcFieldList = record.getVariableFields(tag);
    	if (!marcFieldList.isEmpty()) {
    		for (VariableField vf : marcFieldList) {
    			DataField marcField = (DataField) vf;
    			String ind = String.valueOf(marcField.getIndicator1()) + String.valueOf(marcField.getIndicator2());
    			if (checkIndicator.length() == 1)
    			  ind = String.valueOf(marcField.getIndicator1());
    			
    			if (checkIndicator.equalsIgnoreCase(ind)) {
    				
    				if (checkSubfield != null) {
	    				Subfield subfield_check = marcField.getSubfield(checkSubfield.charAt(0));
	    				if (checkDataSubfield.equalsIgnoreCase(subfield_check.getData().trim())) {
	    					for (int i=0; i< dataSubfield.length(); i++) {
		    				Subfield subfield_data = marcField.getSubfield(dataSubfield.charAt(i));
		    				if (subfield_data != null && subfield_data.getData() != null)
		    				  result.add(subfield_data.getData().trim());
	    					}
	    				}
    				} else {
    					for (int i=0; i< dataSubfield.length(); i++) {
    					Subfield subfield_data = marcField.getSubfield(dataSubfield.charAt(i));
    					if (subfield_data != null && subfield_data.getData() != null)
	    				  result.add(subfield_data.getData().trim());
    					}
    				}
    			}
    		}
    	}
		return result;
	}

}
