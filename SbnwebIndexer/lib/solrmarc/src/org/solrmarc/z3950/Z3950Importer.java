package org.solrmarc.z3950;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;

import org.marc4j.marc.Record;
import org.solrmarc.marc.MarcImporter;
import org.solrmarc.tools.Utils;

/**
 * Test Z39.50 importer
 * @author Robert Haschart
 * @version $Id$
 *
 */

public class Z3950Importer
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String properties = "import.properties";
        String tempdir = System.getProperty("java.io.tmpdir");
//        if(args.length > 0){
//            properties = args[0];
//        }
        File dir1 = new File (".");
        try {
           System.out.println ("Current dir : " + dir1.getCanonicalPath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Loading properties from " + properties);
        
//        try {
//            MarcImporter.loadProperties(properties);
//        } catch (IOException ioe) {
//            System.err.println("Error reading properties file. Make sure it exits.");
//            ioe.printStackTrace();
//            System.exit(1);
//        }
//
//        //try{
//        InputStream input = null;
//        
//        System.out.println("Making sure the marc file exists...");
//        
//        try {
//            input = new FileInputStream(MarcImporter.MARC_FILE);
//        } catch (FileNotFoundException e) {
//            System.err.println("Marc file does not exist. Please provide the filename");
//            e.printStackTrace();
//            System.exit(1);
//        }
//        
//        System.out.println("Found " + MARC_FILE);
//        
//        MarcImporter importer = null;
//        try {
//            importer = new MarcImporter(properties);
//        }
//        catch (IOException ioe)
//        {
//            System.err.println("Marc file does not exist. Please provide the filename");
//            ioe.printStackTrace();
//            System.exit(1);
//        }
        
//        MarcReader reader = new MarcStreamReader(input);
        
        // for the time being, calculate the number of records in the file
        // by looping over
        int totalRecords = 0;
        
        //System.out.println("Calculating the total number of records...");
        
        //while(reader.hasNext()){
        //  totalRecords++;
        //}     
        
        //System.out.println("Found " + totalRecords + " records in your marc file.\nPreparing to index...");
        
        DecimalFormat df = new DecimalFormat("0.00");
        
        // keep track of record
        int recordCounter = 0;
        
        System.out.println("Here we go...");
        
        Date start = new Date();
        
        ZClient newclient = new ZClient();
        Package ir_package = Package.getPackage("com.k_int.IR");
        Package a2j_runtime_package = Package.getPackage("com.k_int.codec.runtime");         
       
//        System.out.println("JZKit command line z39.50 client $Revision: 1.53 $");    
       
        newclient.openConnection("virgo.lib.virginia.edu", "2200");
        newclient.cmdBase("Unicorn");
        newclient.cmdElements("F");
        newclient.cmdFormat("usmarc");        
        
        //while(reader.hasNext() && recordCounter < totalRecords){
 //       newclient.cmdFind("@attrset bib-1 @attr 1=1016 house");
        for ( int recordNum = 0; recordNum < 1000; recordNum += 1)
 //       while(reader.hasNext()){
        {
            recordCounter++;
            
            try{
                Iterator<Record> recordIter = newclient.getRecordByIDStr(""+recordNum);
           //     newclient.cmdFind("@attrset bib-1 @attr 1=1016 house");
           //     Record record = newclient.getRecord(recordNum);
                Record record = recordIter.next();
                if (record != null)
                {
                    //System.out.println("Adding record " + recordCounter + ": " + record.getControlNumber() + " (" + df.format( ( recordCounter/totalRecords) * 100 )  + "% complete)");
                    System.out.println("Adding record " + recordCounter + ": " + record.getControlNumber());
   //                 importer.addToIndex(record);
                }
            }catch(Exception e){
                // keep going?
                System.err.println("Error indexing");
                e.printStackTrace();
            }
            
        }
        
//        try {
 //           MarcImporter.commit(true);
//        } catch (IOException e) {
//            System.err.println("Final commit and optmization failed");
//            e.printStackTrace();
//        }
        
        Date end = new Date();
        
        long totalTime = end.getTime() - start.getTime();
        
        System.out.println("Finished in " + Utils.calcTime(totalTime) );
        
        // calculate the time taken
        float indexingRate = recordCounter*1000 / totalTime;
        
        System.out.println("Indexed " + recordCounter + " at a rate of about " + indexingRate + "per sec");
        
//        MarcImporter.close();
        System.exit(1);
        
    }
    
}
