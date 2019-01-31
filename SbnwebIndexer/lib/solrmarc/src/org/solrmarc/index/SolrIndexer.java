package org.solrmarc.index;

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

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;
import javax.xml.xpath.*;

import org.apache.log4j.Logger;
import org.marc4j.*;
import org.marc4j.marc.*;
import org.solrmarc.marc.MarcImporter;
import org.solrmarc.tools.SolrMarcIndexerException;
import org.solrmarc.tools.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bsh.BshMethod;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.Primitive;
import bsh.UtilEvalError;

/**
 * 
 * @author Robert Haschart
 * @version $Id$
 * 
 */
/**
 * @author rh9ec
 *
 */
public class SolrIndexer
{
    /** map: keys are solr field names, values inform how to get solr field values */
    private Map<String, String[]> fieldMap = null;

    /** map of translation maps.  keys are names of translation maps; 
     *  values are the translation maps (hence, it's a map of maps) */
    private Map<String, Map<String, String>> transMapMap = null;

    /** map of custom methods.  keys are names of custom methods; 
     *  values are the methods to call for that custom method */
    private Map<String, Method> customMethodMap = null;

    /** map of custom mixin classes that contain additional indexing functions 
     *  values are the translation maps (hence, it's a map of maps) */
    private Map<String, SolrIndexerMixin> customMixinMap = null;

    /** map of script interpreters.  keys are names of scripts; 
     *  values are the Interpterers  */
    private Map<String, Interpreter> scriptMap = null;

    /** current datestamp for indexed solr document */
    private Date indexDate = null;

    /** list of path to look for property files in */
    protected String propertyFilePaths[];

    /** Error Handler used for reporting errors */
    protected ErrorHandler errors;

    // Initialize logging category
    protected static Logger logger = Logger.getLogger(MarcImporter.class.getName());

    /**
     * private constructor; initializes fieldMap, transMapMap and indexDate to
     * empty objects
     */
    private SolrIndexer()
    {
        fieldMap = new HashMap<String, String[]>();
        transMapMap = new HashMap<String, Map<String, String>>();
        scriptMap = new HashMap<String, Interpreter>();
        customMethodMap = new HashMap<String, Method>();
        customMixinMap = new HashMap<String, SolrIndexerMixin>();
        indexDate = new Date();
    }

    /**
     * Constructor
     * @param indexingPropsFile the x_index.properties file mapping solr
     *  field names to values in the marc records
     * @param propertyDirs - array of directories holding properties files     
     */
    public SolrIndexer(String indexingPropsFile, String propertyDirs[])
    {
        this();
        propertyFilePaths = propertyDirs;
        if (indexingPropsFile != null)
        {
            String indexingPropsFiles[] = indexingPropsFile.split("[;,]");
            for (String indexProps : indexingPropsFiles)
            {
                indexProps = indexProps.trim();
                Properties indexingProps = Utils.loadProperties(propertyFilePaths, indexProps);
                fillMapFromProperties(indexingProps);
            }
        }
    }
    
    /* A constructor that takes an INDEXER Properties object, and a search
     * path (possibly empty). This is used by SolrMarc tests, may not
     * work as you might expect right in actual program use, not sure.
     *
     *  You can initialize an 'empty' indexex for unit testing like so:
     *  SolrIndexer.indexerFromProperties( new Properties(), 
     *
     * @param indexingProperties a Properties mapping solr
     *  field names to values in the marc records
     * @param propertyDirs - array of directories holding properties files
     * UNTESTED SO COMMENTED OUT FOR THE FUTURE
     */
     public static SolrIndexer indexerFromProperties(Properties indexingProperties, String searchPath[]) 
     {
        SolrIndexer indexer = new SolrIndexer();
        indexer.propertyFilePaths = searchPath;
        indexer.fillMapFromProperties(indexingProperties);
        
        return indexer;
     }
          
     /* Attempt to reinitialize an indexer given an INDEXER Properties object,
      * and a search path (possibly empty). This is used by SolrMarc tests, may not
      * work as you might expect right in actual program use, not sure.
      *
      *  You can re-init an indexer for unit testing like so:
      *  indexer.reinitFromProperties(new Properties())
      *
      * @param indexingProperties a Properties mapping solr
      *  field names to values in the marc records
      * UNTESTED SO COMMENTED OUT FOR THE FUTURE
      */
      public void reinitFromProperties(Properties indexingProperties) 
      {
         this.fieldMap.clear();
         this.transMapMap.clear();
         this.customMethodMap.clear();
         this.customMixinMap.clear();

         this.fillMapFromProperties(indexingProperties);
      }
           
    /**
     * Parse the properties file and load parameters into fieldMap. Also
     * populate transMapMap and indexDate
     * @param props _index.properties as Properties object
     */
    protected void fillMapFromProperties(Properties props)
    {
        Enumeration<?> en = props.propertyNames();

        while (en.hasMoreElements())
        {
            String propName = (String) en.nextElement();

            // ignore map, pattern_map; they are handled separately
            if (!propName.startsWith("map") &&
                !propName.startsWith("pattern_map"))
            {
                String propValue = props.getProperty(propName);
                String fieldDef[] = new String[4];
                fieldDef[0] = propName;
                fieldDef[3] = null;
                if (propValue.startsWith("\""))
                {
                    // value is a constant if it starts with a quote
                    fieldDef[1] = "constant";
                    fieldDef[2] = propValue.trim().replaceAll("\"", "");
                }
                else
                // not a constant
                {
                    // split it into two pieces at first comma or space
                    String values[] = propValue.split("[, ]+", 2);
                    if (values[0].startsWith("custom") ||  values[0].startsWith("script"))
                    {
                        fieldDef[1] = values[0];

                        // parse sections of custom value assignment line in
                        // _index.properties file
                        String lastValues[];
                        // get rid of empty parens
                        if (values[1].indexOf("()") != -1)
                            values[1] = values[1].replace("()", "");

                        // index of first open paren after custom method name
                        int parenIx = values[1].indexOf('(');

                        // index of first unescaped comma after method name
                        int commaIx = Utils.getIxUnescapedComma(values[1]);

                        if (parenIx != -1 && commaIx != -1 && parenIx < commaIx) {
                            // remainder should be split after close paren
                            // followed by comma (optional spaces in between)
                            lastValues = values[1].trim().split("\\) *,", 2);
                            
                            // Reattach the closing parenthesis:
                            if (lastValues.length == 2)  lastValues[0] += ")";
                        }
                        else
                            // no parens - split comma preceded by optional spaces
                            lastValues = values[1].trim().split(" *,", 2);

                        fieldDef[2] = lastValues[0].trim();

                        fieldDef[3] = lastValues.length > 1 ? lastValues[1].trim() : null;
                        // is this a translation map?
                        if (fieldDef[3] != null && fieldDef[3].contains("map"))
                        {
                            try
                            {
                                fieldDef[3] = loadTranslationMap(props, fieldDef[3]);
                            }
                            catch (IllegalArgumentException e)
                            {
                                logger.error("Unable to find file containing specified translation map (" + fieldDef[3] + ")");
                                throw new IllegalArgumentException("Error: Problems reading specified translation map (" + fieldDef[3] + ")");
                            }
                        }
                    } // end custom
                    else if (values[0].equals("xml") ||
                             values[0].equals("raw") ||
                             values[0].equals("date") ||
                             values[0].equals("json") ||
                             values[0].equals("json2") ||
                             values[0].equals("index_date") ||
                             values[0].equals("era"))
                    {
                        fieldDef[1] = "std";
                        fieldDef[2] = values[0];
                        fieldDef[3] = values.length > 1 ? values[1].trim() : null;
                        // NOTE: assuming no translation map here
                        if (fieldDef[2].equals("era") && fieldDef[3] != null)
                        {
                            try
                            {
                                fieldDef[3] = loadTranslationMap(props, fieldDef[3]);
                            }
                            catch (IllegalArgumentException e)
                            {
                                logger.error("Unable to find file containing specified translation map (" + fieldDef[3] + ")");
                                throw new IllegalArgumentException("Error: Problems reading specified translation map (" + fieldDef[3] + ")");
                            }
                        }
                    }
                    else if (values[0].startsWith("xpath") )
                    {
                        fieldDef[1] = values[0];
                        String values2[] = values[1].trim().split("[ ]*,[ ]*", 2);
                        fieldDef[2] = values2[0];
                        fieldDef[3] = values2.length > 1 ? values2[1] : null;
                        if (fieldDef[3] != null)
                        {
                            try
                            {
                                fieldDef[3] = loadTranslationMap(props, fieldDef[3]);
                            }
                            catch (IllegalArgumentException e)
                            {
                                logger.error("Unable to find file containing specified translation map (" + fieldDef[3] + ")");
                                throw new IllegalArgumentException("Error: Problems reading specified translation map (" + fieldDef[3] + ")");
                            }
                        }
                    }
                    else if (values[0].equalsIgnoreCase("FullRecordAsXML") ||
                             values[0].equalsIgnoreCase("FullRecordAsMARC") ||
                             values[0].equalsIgnoreCase("FullRecordAsJson") ||
                             values[0].equalsIgnoreCase("FullRecordAsJson2") ||
                             values[0].equalsIgnoreCase("FullRecordAsText") ||
                             values[0].equalsIgnoreCase("DateOfPublication") ||
                             values[0].equalsIgnoreCase("DateRecordIndexed"))
                    {
                        fieldDef[1] = "std";
                        fieldDef[2] = values[0];
                        fieldDef[3] = values.length > 1 ? values[1].trim() : null;
                        // NOTE: assuming no translation map here
                    }
                    else if (values.length == 1)
                    {
                        fieldDef[1] = "all";
                        fieldDef[2] = values[0];
                        fieldDef[3] = null;
                    }
                    else
                    // other cases of field definitions
                    {
                        String values2[] = values[1].trim().split("[ ]*,[ ]*", 2);
                        fieldDef[1] = "all";
                        if (values2[0].equals("first") ||
                                (values2.length > 1 && values2[1].equals("first")))
                            fieldDef[1] = "first";

                        if (values2[0].startsWith("join"))
                            fieldDef[1] = values2[0];

                        if ((values2.length > 1 && values2[1].startsWith("join")))
                            fieldDef[1] = values2[1];

                        if (values2[0].equalsIgnoreCase("DeleteRecordIfFieldEmpty") ||
                            (values2.length > 1 && values2[1].equalsIgnoreCase("DeleteRecordIfFieldEmpty")))
                            fieldDef[1] = "DeleteRecordIfFieldEmpty";

                        fieldDef[2] = values[0];
                        fieldDef[3] = null;

                        // might we have a translation map?
                        if (!values2[0].equals("all") &&
                            !values2[0].equals("first") &&
                            !values2[0].startsWith("join") &&
                            !values2[0].equalsIgnoreCase("DeleteRecordIfFieldEmpty"))
                        {
                            fieldDef[3] = values2[0].trim();
                            if (fieldDef[3] != null)
                            {
                                try
                                {
                                    fieldDef[3] = loadTranslationMap(props, fieldDef[3]);
                                }
                                catch (IllegalArgumentException e)
                                {
                                    logger.error("Unable to find file containing specified translation map (" + fieldDef[3] + ")");
                                    throw new IllegalArgumentException("Error: Problems reading specified translation map (" + fieldDef[3] + ")");
                                }
                            }
                        }
                    } // other cases of field definitions

                } // not a constant

                fieldMap.put(propName, fieldDef);

            } // if not map or pattern_map

        } // while enumerating through property names

        // verify that fieldMap is valid
        verifyCustomMethodsAndTransMaps();
    }

    /**
     * Verify that custom methods are available and translation maps are in
     * transMapMap
     */
    private void verifyCustomMethodsAndTransMaps()
    {
        for (String key : fieldMap.keySet())
        {
            String fieldMapVal[] = fieldMap.get(key);
            String indexType = fieldMapVal[1];
            String indexParm = fieldMapVal[2];
            String mapName = fieldMapVal[3];

            if (indexType.startsWith("custom"))
            {
                verifyCustomMethodExists(indexType, indexParm);
            }

            // check that translation maps are present in transMapMap
            if (mapName != null && findMap(mapName) == null)
            {
//                System.err.println("Error: Specified translation map (" + mapName + ") not found in properties file");
                logger.error("Specified translation map (" + mapName + ") not found in properties file");
                throw new IllegalArgumentException("Specified translation map (" + mapName + ") not found in properties file");
            }
        }
    }

    public SolrIndexerMixin findMixin(String className) 
    {
        SolrIndexerMixin instance = null;
        if (customMixinMap.containsKey(className))
        {
            return( customMixinMap.get(className));
        }
        else 
        { 
            try
            {
                Class<?> classToLookIn = Class.forName(className);
                if (SolrIndexerMixin.class.isAssignableFrom(classToLookIn))
                {
                   Constructor<?> classConstructor = classToLookIn.getConstructor();
                   instance = (SolrIndexerMixin)classConstructor.newInstance((Object[])null);
                   instance.setMainIndexer(this);
                   customMixinMap.put(className, instance);
                }
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Unable to find and load indexer mixin class "  + className);
            }
        }
        return(instance);
    }

    /**
     * verify that custom methods defined in the _index properties file are
     * present and accounted for
     * @param indexParm - name of custom function plus args
     */
    private void verifyCustomMethodExists(String indexType, String indexParm)
    {
        String className = null;
        Class<?> classToLookIn = this.getClass();
        try
        {
//            if (!indexType.equals("custom"))
//            {
//                className = null;
//            }
            if (indexType.matches("custom(DeleteRecordIfFieldEmpty)?[(][a-zA-Z0-9.]+[)]"))
            {
                className = indexType.replaceFirst("custom(DeleteRecordIfFieldEmpty)?[(]([a-zA-Z0-9.]+)[)]", "$2");
                SolrIndexerMixin instance = findMixin(className);
                classToLookIn = instance.getClass();
            }
        }
        catch (IllegalArgumentException e)
        {
            logger.error("Unable to find indexer mixin class "  + className + " which should defing the custom indexing function " + indexParm);
            logger.debug(e.getCause());
            throw new IllegalArgumentException("Unable to find and load indexer mixin class "  + className + " which should define the custom indexing function " + indexParm);
        }

        try
        {

            Method method = null;
            int parenIx = indexParm.indexOf("(");
            if (parenIx != -1)
            {
                String functionName = indexParm.substring(0, parenIx);
                String parmStr = indexParm.substring(parenIx + 1, indexParm.lastIndexOf(')'));
                // parameters are separated by unescaped commas
                String parms[] = parmStr.trim().split("[^\\\\],");
                int numparms = parms.length;
                Class<?> parmClasses[] = new Class<?>[numparms + 1];
                parmClasses[0] = Record.class;
                for (int i = 0; i < numparms; i++)
                {
                    parmClasses[i + 1] = String.class;
                }
                method = classToLookIn.getMethod(functionName, parmClasses);
                if (customMethodMap.containsKey(functionName))
                {
                    customMethodMap.put(functionName, null);
                }
                else
                {
                    customMethodMap.put(functionName, method);
                }
            }
            else
            {    
                method = classToLookIn.getMethod(indexParm, new Class[] { Record.class });
                if (customMethodMap.containsKey(indexParm))
                {
                    customMethodMap.put(indexParm, null);
                }
                else
                {
                    customMethodMap.put(indexParm, method);
                }
            }
            Class<?> retval = method.getReturnType();
            // if (!method.isAccessible())
            // {
            //   System.err.println("Error: Unable to invoke custom indexing function "+indexParm);
            // valid = false;
            // }
            if (!(Set.class.isAssignableFrom(retval) || String.class.isAssignableFrom(retval) ||
                    Map.class.isAssignableFrom(retval) ||  List.class.isAssignableFrom(retval)) )
            {
//                System.err.println("Error: Return type of custom indexing function " + indexParm + " must be either String or Set<String>");
                logger.error("Error: Return type of custom indexing function " + indexParm + " must be String or Set<String> or List<String> or Map<String, String>");
                throw new IllegalArgumentException("Error: Return type of custom indexing function " + indexParm + " must be String or Set<String> or List<String> or Map<String, String>");
            }
        }
        catch (SecurityException e)
        {
//            System.err.println("Error: Unable to invoke custom indexing function " + indexParm);
            logger.error("Unable to invoke custom indexing function " + indexParm);
            logger.debug(e.getCause(), e);
            throw new IllegalArgumentException("Unable to invoke custom indexing function " + indexParm);
        }
        catch (NoSuchMethodException e)
        {
//            System.err.println("Error: Unable to find custom indexing function " + indexParm);
            logger.error("Unable to find custom indexing function " + indexParm);
            logger.debug(e.getCause());
            throw new IllegalArgumentException("Unable to find custom indexing function " + indexParm);
        }
        catch (IllegalArgumentException e)
        {
//            System.err.println("Error: Unable to find custom indexing function " + indexParm);
            logger.error("Unable to find custom indexing function " + indexParm);
            logger.debug(e.getCause());
            throw new IllegalArgumentException("Unable to find custom indexing function " + indexParm);
        }
    }

    /**
     * public interface callable from custom indexing scripts to 
     * load the translation map into transMapMap
     * @param translationMapSpec the specification of a translation map - 
     *   could be name of a _map.properties file, or some subset of entries in a 
     *   _map.properties file
     * @return the name of the translation map to be used in a subsequent call to FindMap
     */
    public String loadTranslationMap(String translationMapSpec) 
    {
        return(this.loadTranslationMap(null, translationMapSpec));
    }
    
    /**
     * load the translation map into transMapMap
     * @param indexProps _index.properties as Properties object
     * @param translationMapSpec the specification of a translation map - 
     *   could be name of a _map.properties file, or something in _index 
     *   properties ...
     * @return the name of the translation map
     */
    protected String loadTranslationMap(Properties indexProps, String translationMapSpec) 
    {
        if (translationMapSpec.length() == 0)
            return null;

        String mapName = null;
        String mapKeyPrefix = null;
        if (translationMapSpec.startsWith("(")
                && translationMapSpec.endsWith(")"))
        {
            // translation map entries are in passed Properties object
            mapName = translationMapSpec.replaceAll("[\\(\\)]", "");
            mapKeyPrefix = mapName;
            loadTranslationMapValues(indexProps, mapName, mapKeyPrefix);
        }
        else
        {
            // translation map is a separate file
            String transMapFname = null;
            if (translationMapSpec.contains("(")
                    && translationMapSpec.endsWith(")"))
            {
                String mapSpec[] = translationMapSpec.split("(//s|[()])+");
                transMapFname = mapSpec[0];
                mapName = mapSpec[1];
                mapKeyPrefix = mapName;
            }
            else
            {
                transMapFname = translationMapSpec;
                mapName = translationMapSpec.replaceAll(".properties", "");
                mapKeyPrefix = "";
            }

            if (findMap(mapName) == null)
                loadTranslationMapValues(transMapFname, mapName, mapKeyPrefix);
        }

        return mapName;
    }

    /**
     * Load translation map into transMapMap.  Look for translation map in 
     * site specific directory first; if not found, look in solrmarc top 
     * directory
     * @param transMapName name of translation map file to load
     * @param mapName - the name of the Map to go in transMapMap (the key in transMapMap)
     * @param mapKeyPrefix - any prefix on individual Map keys (entries in the
     *   value in transMapMap) 
     */
    private void loadTranslationMapValues(String transMapName, String mapName, String mapKeyPrefix)
    {
        Properties props = null;
        props = Utils.loadProperties(propertyFilePaths, transMapName);
        logger.debug("Loading Custom Map: " + transMapName);
        loadTranslationMapValues(props, mapName, mapKeyPrefix);
    }

    /**
     * populate transMapMap
     * @param transProps - the translation map as a Properties object
     * @param mapName - the name of the Map to go in transMapMap (the key in transMapMap)
     * @param mapKeyPrefix - any prefix on individual Map keys (entries in the
     *   value in transMapMap) 
     */
    private void loadTranslationMapValues(Properties transProps, String mapName, String mapKeyPrefix)
    {
        Enumeration<?> en = transProps.propertyNames();
        while (en.hasMoreElements())
        {
            String property = (String) en.nextElement();
            if (mapKeyPrefix.length() == 0 || property.startsWith(mapKeyPrefix))
            {
                String mapKey = property.substring(mapKeyPrefix.length());
                if (mapKey.startsWith("."))
                    mapKey = mapKey.substring(1);
                String value = transProps.getProperty(property);
                value = value.trim();
                if (value.equals("null"))
                    value = null;

                Map<String, String> valueMap;
                if (transMapMap.containsKey(mapName))
                    valueMap = transMapMap.get(mapName);
                else
                {
                    valueMap = new LinkedHashMap<String, String>();
                    transMapMap.put(mapName, valueMap);
                }

                valueMap.put(mapKey, value);
            }
        }
    }

    /**
     * Given a record, return a Map of solr fields (keys are field names, values
     * are an Object containing the values (a Set or a String)
     */
    public Map<String, Object> map(Node doc)
    {
        return (map(doc, null));
    }

    /**
     * Given a record, return a Map of solr fields (keys are field names, values
     * are an Object containing the values (a Set or a String)
     */
    public Map<String, Object> map(Node doc, ErrorHandler errors)
    {
        this.errors = errors;
        Map<String, Object> indexMap = new HashMap<String, Object>();
        for (String key : fieldMap.keySet())
        {
            String fieldVal[] = fieldMap.get(key);
            String indexField = fieldVal[0];
            String indexType = fieldVal[1];
            String indexParm = fieldVal[2];
            String mapName = fieldVal[3];

            if (indexType.equals("constant"))
            {
                if (indexParm.contains("|"))
                {
                    String parts[] = indexParm.split("[|]");
                    Set<String> result = new LinkedHashSet<String>();
                    result.addAll(Arrays.asList(parts));
                    // if a zero length string appears, remove it
                    result.remove("");
                    addFields(indexMap, indexField, null, result);
                }
                else
                    addField(indexMap, indexField, indexParm);
            }
            else if (indexType.startsWith("xpath"))
            {
                Set<String> fields = getXPathFieldList(doc, indexParm);
                if (indexType.startsWith("xpath-join"))
                {
                    String sep = ", ";
                    String field = "";
                    for (String sf : fields)
                    {
                        if (mapName != null && findMap(mapName) != null)
                            sf = Utils.remap(sf, findMap(mapName), true);
                        if (field.length() > 0) field = field + ", ";
                        field = field + sf;
                    }
                    addField(indexMap, indexField, null, field);
                }
                else 
                {
                    if (mapName != null && findMap(mapName) != null)
                        fields = Utils.remap(fields, findMap(mapName), true);

                    if (fields.size() != 0)
                        addFields(indexMap, indexField, null, fields);
                }
//                else  // no entries produced for field => generate no record in Solr
//                    throw new SolrMarcIndexerException(SolrMarcIndexerException.DELETE, 
//                                                    "Index specification: "+ indexField +" says this record should be deleted.");
                
            }
/*            else if (indexType.startsWith("custom"))
            {
                try {
                    handleCustom(indexMap, indexType, indexField, mapName, record, indexParm);
                }
                catch(SolrMarcIndexerException e)
                {
                    String recCntlNum = null;
                    try {
                        recCntlNum = record.getControlNumber();
                    }
                    catch (NullPointerException npe) { }// ignore 

                    if (e.getLevel() == SolrMarcIndexerException.DELETE)
                    {
                        throw new SolrMarcIndexerException(SolrMarcIndexerException.DELETE, 
                                "Record " + (recCntlNum != null ? recCntlNum : "") + " purposely not indexed because " + key + " field is empty");
//                      logger.error("Record " + (recCntlNum != null ? recCntlNum : "") + " not indexed because " + key + " field is empty -- " + e.getMessage(), e);
                    }
                    else
                    {
                        logger.error("Unable to index record " + (recCntlNum != null ? recCntlNum : "") + " due to field " + key + " -- " + e.getMessage(), e);
                        throw(e);
                    }
                }
            }
            else if (indexType.startsWith("script"))
            {
                try {
                    handleScript(indexMap, indexType, indexField, mapName, record, indexParm);
                }
                catch(SolrMarcIndexerException e)
                {
                    String recCntlNum = null;
                    try {
                        recCntlNum = record.getControlNumber();
                    }
                    catch (NullPointerException npe) {} // ignore  

                    if (e.getLevel() == SolrMarcIndexerException.DELETE)
                    {
                        logger.error("Record " + (recCntlNum != null ? recCntlNum : "") + " purposely not indexed because " + key + " field is empty -- " + e.getMessage(), e);
                    }
                    else
                    {
                        logger.error("Unable to index record " + (recCntlNum != null ? recCntlNum : "") + " due to field " + key + " -- " + e.getMessage(), e);
                        throw(e);
                    }
                }
            }*/
        }
        this.errors = null;
        return indexMap;

    }
    
    private Set<String> getXPathFieldList(Node doc, String indexParm) 
    {
        String pathParts[] = indexParm.split("[|][|]");
        Set<String> result = new LinkedHashSet<String>();
        for (String pathPart : pathParts)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList resultNodeList;
            try {
                resultNodeList = (NodeList) xpath.evaluate(pathPart, doc, XPathConstants.NODESET);
                for (int i = 0; i < resultNodeList.getLength(); i++)
                {
                    Node node = resultNodeList.item(i);
                    String str = node.getTextContent();
                    str = Utils.cleanData(str);
                    if (str != null && str.length() != 0) 
                        result.add(str);
                }
            } 
            catch (XPathExpressionException e) 
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        return(result);
    }

    /**
     * Given a record, return a Map of solr fields (keys are field names, values
     * are an Object containing the values (a Set or a String)
     */
    public Map<String, Object> map(Record record)
    {
        return (map(record, null));
    }

    /**
     * Given a record, return a Map of solr fields (keys are field names, values
     * are an Object containing the values (a Set or a String)
     */
    public Map<String, Object> map(Record record, ErrorHandler errors)
    {
        this.errors = errors;
        perRecordInitMaster(record);
        Map<String, Object> indexMap = new HashMap<String, Object>();
        getSolrId(record, indexMap);

        for (String key : fieldMap.keySet())
        {
            if (key.equals("id")) 
            {
                // do nothing, already handled above
            }
            else 
            {
                addFieldValueToMap(record, key, indexMap);
            }
        }
        this.errors = null;
        // Check the fieldsMap for a special case field name indicating that some kind of indexing error occurred.
        if (indexMap.containsKey(SolrMarcIndexerException.getLevelFieldName(SolrMarcIndexerException.EXIT)))
        {
            String message = indexMap.get(SolrMarcIndexerException.getLevelFieldName(SolrMarcIndexerException.EXIT)).toString();
            throw(new SolrMarcIndexerException(SolrMarcIndexerException.EXIT, indexMap, message));
        }
        else if (indexMap.containsKey(SolrMarcIndexerException.getLevelFieldName(SolrMarcIndexerException.DELETE)))
        {
            String message = indexMap.get(SolrMarcIndexerException.getLevelFieldName(SolrMarcIndexerException.DELETE)).toString();
            throw(new SolrMarcIndexerException(SolrMarcIndexerException.DELETE, indexMap, message));
        }
        else if (indexMap.containsKey(SolrMarcIndexerException.getLevelFieldName(SolrMarcIndexerException.IGNORE)))
        {
            String message = indexMap.get(SolrMarcIndexerException.getLevelFieldName(SolrMarcIndexerException.IGNORE)).toString();
            throw(new SolrMarcIndexerException(SolrMarcIndexerException.IGNORE, indexMap, message));
        }

        return indexMap;
    }

    /**
     * Given a record, a field name, and a Map of solr fields (where keys are field names, values
     * are an Object containing the values (a Set or a String) 
     * Compute the value for a field and 
     */
    public void addFieldValueToMap(Record record, String key, Map<String, Object> indexMap)
    {
        String fieldVal[] = fieldMap.get(key);
        String indexField = fieldVal[0];
        String indexType = fieldVal[1];
        String indexParm = fieldVal[2];
        String mapName = fieldVal[3];

        if (indexType.equals("constant"))
        {
            if (indexParm.contains("|"))
            {
                String parts[] = indexParm.split("[|]");
                Set<String> result = new LinkedHashSet<String>();
                result.addAll(Arrays.asList(parts));
                // if a zero length string appears, remove it
                result.remove("");
                addFields(indexMap, indexField, null, result);
            }
            else
                addField(indexMap, indexField, indexParm);
        }
        else if (indexType.equals("first"))
            addField(indexMap, indexField, getFirstFieldVal(record, mapName, indexParm));
        else if (indexType.equals("all"))
            addFields(indexMap, indexField, mapName, getFieldList(record, indexParm));
        else if (indexType.equals("DeleteRecordIfFieldEmpty"))
        {
            Set<String> fields = getFieldList(record, indexParm);
            if (mapName != null && findMap(mapName) != null)
                fields = Utils.remap(fields, findMap(mapName), true);

            if (fields.size() != 0)
                addFields(indexMap, indexField, null, fields);
            else  // no entries produced for field => generate no record in Solr
                addIndexerExceptionAsField(record, indexMap, indexField, SolrMarcIndexerException.DELETE);
        }
        else if (indexType.startsWith("join"))
        {
            String joinChar = " ";
            if (indexType.contains("(") && indexType.endsWith(")"))
                joinChar = indexType.replace("join(", "").replace(")", "");
            addField(indexMap, indexField, getFieldVals(record, indexParm, joinChar));
        }
        else if (indexType.equals("std"))
        {
            if (indexParm.equals("era"))
                addFields(indexMap, indexField, mapName, getEra(record));
            else
                addField(indexMap, indexField, getStd(record, indexParm));
        }
        else if (indexType.startsWith("custom"))
        {
            try {
                handleCustom(indexMap, indexType, indexField, mapName, record, indexParm);
            }
            catch(SolrMarcIndexerException e)
            {
                addIndexerExceptionAsField(record, indexMap, indexField, e.getLevel());
            }
        }
        else if (indexType.startsWith("script"))
        {
            try {
                handleScript(indexMap, indexType, indexField, mapName, record, indexParm);
            }
            catch(SolrMarcIndexerException e)
            {
                addIndexerExceptionAsField(record, indexMap, indexField, e.getLevel());
            }
        }
    }
    
    /**
     * When a custom indexing script throws an indexing exception, this routine converts it to a special-named index map key
     * and includes a descriptive message as the index value for that key
     */
    private void addIndexerExceptionAsField(Record record, Map<String, Object> indexMap, String indexField, int exceptionLevel)
    {
        String fieldName = SolrMarcIndexerException.getLevelFieldName(exceptionLevel);
        if (exceptionLevel == SolrMarcIndexerException.DELETE)
        {
            addField(indexMap, fieldName, null, 
                     "Index specification: "+ indexField +" says this record should be deleted.");
        }
        else if (exceptionLevel == SolrMarcIndexerException.IGNORE)
        {
            addField(indexMap, fieldName, null, 
                     "Index specification: "+ indexField +" says this record should be ignored.");
        }
        else if (exceptionLevel == SolrMarcIndexerException.EXIT)
        {
            addField(indexMap, fieldName, null, 
                     "Index specification: "+ indexField +" says because of this record indexing should be terminated.");
        }
    }
    
    /**
     * This routine CANNOT be overridden in a sub-class.  Its is called to perform some processing that needs 
     * to be done once for each record, and which may be needed by several indexing specifications.  Basically all 
     * this method does is call the override-able method perRecordInit for the SolrIndexer class, and the perRecordInit 
     * methods of any SolrIndexerMixin that are in use.
     * 
     * @param record -  The MARC record that is being indexed.
     */
    private final void perRecordInitMaster(Record record)
    {
        perRecordInit(record);
        for (String key : customMixinMap.keySet())
        {
            SolrIndexerMixin mixin = customMixinMap.get(key);
            mixin.perRecordInit(record);
        }
    }
    
    /**
     * This routine can be overridden in a sub-class to perform some processing that needs to be done once 
     * for each record, and which may be needed by several indexing specifications, especially custom methods.
     * The default version does nothing.
     * 
     * @param record -  The MARC record that is being indexed.
     */
    protected void perRecordInit(Record record)
    {
    }

    /**
     * Calling a custom method defined in a user-supplied custom subclass of SolrIndexer, 
     * do the processing indicated by a custom function, putting the solr field
     * name and value into the indexMap parameter
     * 
     * @param indexMap - The map contain the solr index record that is being constructed for this MARC record.
     * @param indexType - Indicates whether the the solr record should be deleted if no value 
     *                    is generated by this custom indexing method.
     * @param indexField - The name of the field to be added to the solr index record.  Note that 
     *                     in that case of a custom index method that returns a Map, the keys of the map 
     *                     define the names of the fields to be added, and this value is then simply a dummy.
     * @param mapName - The name (or file and name) of a translation map to use to convert 
     *                  the data in the specified fields of the MARC record to the desired values 
     *                  to be included in the Solr index record.  (If mapName is null, the values 
     *                  in the record will be returned as-is.) 
     * @param record -  The MARC record that is being indexed.
     * @param indexParm - contains the name of the custom method to invoke, as well as the 
     *                    additional parameters to pass to that method.
     */
    private void handleCustom(Map<String, Object> indexMap,
            String indexType, String indexField, String mapName, Record record,
            String indexParm)  throws SolrMarcIndexerException
    {
        Object retval = null;
        Class<?> returnType = null;
        String recCntlNum = null;
        try {
        	recCntlNum = record.getControlNumber();
        }
        catch (NullPointerException npe) { /* ignore as this is for error msgs only*/ }
        String className = null;
        Class<?> classThatContainsMethod = this.getClass();
        Object objectThatContainsMethod = this;
        try
        {
            if (indexType.matches("custom(DeleteRecordIfFieldEmpty)?[(][a-zA-Z0-9.]+[)]"))
            {
                className = indexType.replaceFirst("custom(DeleteRecordIfFieldEmpty)?[(]([a-zA-Z0-9.]+)[)]", "$2");
                if (customMixinMap.containsKey(className))
                {
                    objectThatContainsMethod = customMixinMap.get(className);
                    classThatContainsMethod = objectThatContainsMethod.getClass();
                }
            }

            Method method;
            if (indexParm.indexOf("(") != -1)
            {
                String functionName = indexParm.substring(0, indexParm.indexOf('('));
                String parmStr = indexParm.substring(indexParm.indexOf('(')+1, indexParm.lastIndexOf(')'));
                // parameters are separated by unescaped commas
                String parms[] = parmStr.trim().split("(?<=[^\\\\]),");
                int numparms = parms.length;
                Class parmClasses[] = new Class[numparms + 1];
                parmClasses[0] = Record.class;
                Object objParms[] = new Object[numparms + 1];
                objParms[0] = record;
                for (int i = 0; i < numparms; i++)
                {
                    parmClasses[i + 1] = String.class;
                    objParms[i + 1] = dequote(parms[i].trim());
                }
                method = customMethodMap.get(functionName);
                if (method == null)  
                    method = classThatContainsMethod.getMethod(functionName, parmClasses);
                returnType = method.getReturnType();
                retval = method.invoke(objectThatContainsMethod, objParms);
            }
            else
            {
                method = customMethodMap.get(indexParm);
                if (method == null)  
                    method = classThatContainsMethod.getMethod(indexParm, new Class[]{Record.class});
                returnType = method.getReturnType();
                retval = method.invoke(objectThatContainsMethod, new Object[] { record });
            }
        }
        catch (SecurityException e)
        {
            // e.printStackTrace();
//            logger.error(record.getControlNumber() + " " + indexField + " " + e.getCause());
            logger.error("Error while indexing " + indexField + " for record " + (recCntlNum != null ? recCntlNum : "") + " -- " + e.getCause());
        }
        catch (NoSuchMethodException e)
        {
            // e.printStackTrace();
//            logger.error(record.getControlNumber() + " " + indexField + " " + e.getCause());
            logger.error("Error while indexing " + indexField + " for record " + (recCntlNum != null ? recCntlNum : "") + " -- " + e.getCause());
        }
        catch (IllegalArgumentException e)
        {
            // e.printStackTrace();
//            logger.error(record.getControlNumber() + " " + indexField + " " + e.getCause());
            logger.error("Error while indexing " + indexField + " for record " + (recCntlNum != null ? recCntlNum : "") + " -- " + e.getCause());
        }
        catch (IllegalAccessException e)
        {
            // e.printStackTrace();
//            logger.error(record.getControlNumber() + " " + indexField + " " + e.getCause());
            logger.error("Error while indexing " + indexField + " for record " + (recCntlNum != null ? recCntlNum : "") + " -- " + e.getCause());
        }
        catch (InvocationTargetException e)
        {
            if (e.getTargetException() instanceof SolrMarcIndexerException)
            {
                addIndexerExceptionAsField(record, indexMap, indexField, ((SolrMarcIndexerException)e.getTargetException()).getLevel());
            }
            //e.printStackTrace();   // DEBUG
//            logger.error(record.getControlNumber() + " " + indexField + " " + e.getCause());
            logger.error("Error while indexing " + indexField + " for record " + (recCntlNum != null ? recCntlNum : "") + " -- " + e.getCause());
        }
        boolean deleteIfEmpty = false;
        if (indexType.startsWith("customDeleteRecordIfFieldEmpty")) 
            deleteIfEmpty = true;
        boolean result = finishCustomOrScript(indexMap, indexField, mapName, returnType, retval, deleteIfEmpty);
        if (result == true) addIndexerExceptionAsField(record, indexMap, indexField, SolrMarcIndexerException.DELETE);
       // if (result == true) throw new SolrMarcIndexerException(SolrMarcIndexerException.DELETE);
    }

    /**
     * Analogous to handleCustom, however instead of calling a custom method defined 
     * in a user-supplied custom subclass SolrIndexer, this will invoke a custom BeanShell 
     * script method found in a script file that is referenced in the index specification
     * in parentheses following the keyword "script".  
     * 
     * @param indexMap - The map contain the solr index record that is being constructed for this MARC record.
     * @param indexType - Indicates whether the the solr record should be deleted if no value 
     *                    is generated by this custom indexing script
     * @param indexField - The name of the field to be added to the solr index record.  Note that 
     *                     in that case of a custom index method that returns a Map, the keys of the map 
     *                     define the names of the fields to be added, and this value is then simply a dummy.
     * @param mapName - The name (or file and name) of a translation map to use to convert 
     *                  the data in the specified fields of the MARC record to the desired values 
     *                  to be included in the Solr index record.  (If mapName is null, the values 
     *                  in the record will be returned as-is.) 
     * @param record -  The MARC record that is being indexed.
     * @param indexParm - contains the name of the custom BeanShell script method to invoke, as well as the 
     *                    additional parameters to pass to that method.
     */
    private void handleScript(Map<String, Object> indexMap, String indexType, String indexField, String mapName, Record record, String indexParm)
    {
        String scriptFileName = indexType.replaceFirst("script[A-Za-z]*[(]", "").replaceFirst("[)]$", "");
        Interpreter bsh = getInterpreterForScript(scriptFileName);
        Object retval;
        Class<?> returnType;
        String functionName = null;
        try {
            bsh.set("indexer", this);
            BshMethod bshmethod;
            if (indexParm.indexOf("(") != -1)
            {
                functionName = indexParm.substring(0, indexParm.indexOf('('));
                String parmStr = indexParm.substring(indexParm.indexOf('(')+1, indexParm.lastIndexOf(')'));
                // parameters are separated by unescaped commas
                String parms[] = parmStr.trim().split("(?<=[^\\\\]),");
                int numparms = parms.length;
                Class parmClasses[] = new Class[numparms + 1];
                parmClasses[0] = Record.class;
                Object objParms[] = new Object[numparms + 1];
                objParms[0] = record;
                for (int i = 0; i < numparms; i++)
                {
                    parmClasses[i + 1] = String.class;
                    objParms[i + 1] = dequote(parms[i].trim());
                }
                bshmethod = bsh.getNameSpace().getMethod(functionName, parmClasses);
                if (bshmethod == null)
                {
                    throw new IllegalArgumentException("Unable to find Specified method " + functionName + " in  script: " + scriptFileName);
                }
                else
                {    
                    returnType = bshmethod.getReturnType();
                    retval = bshmethod.invoke(objParms, bsh);
                }
            }
            else
            {
                bshmethod = bsh.getNameSpace().getMethod(indexParm, new Class[]{Record.class});
                if (bshmethod == null)
                {
                    throw new IllegalArgumentException("Unable to find Specified method " + indexParm + " in  script: " + scriptFileName);
                }
                else
                {    
                    returnType = bshmethod.getReturnType();
                    retval = bshmethod.invoke(new Object[] { record }, bsh);
                }
            }
            if (returnType == null && retval != null) 
                returnType = retval.getClass();
        }
        catch (EvalError e)
        {
            throw new IllegalArgumentException("Error while trying to evaluate script: " + scriptFileName, e);
        }
        catch (UtilEvalError e)
        {
            throw new IllegalArgumentException("Unable to find Specified method " + functionName + " in  script: " + scriptFileName, e);
        } 
        boolean deleteIfEmpty = false;
        if (indexType.startsWith("scriptDeleteRecordIfFieldEmpty")) 
            deleteIfEmpty = true;
        if (retval == Primitive.NULL)  
            retval = null;
        boolean result = finishCustomOrScript(indexMap, indexField, mapName, returnType, retval, deleteIfEmpty);
        if (result == true) addIndexerExceptionAsField(record, indexMap, indexField, SolrMarcIndexerException.DELETE);
        // throw new SolrMarcIndexerException(SolrMarcIndexerException.DELETE);
    }

    /**
     * Finish up the processing for a custom indexing function or a custom BeanShell script method 
     * @param indexMap - The map contain the solr index record that is being constructed for this MARC record.
     * @param indexField - The name of the field to be added to the solr index record.  Note that 
     *                     in that case of a custom index method that returns a Map, the keys of the map 
     *                     define the names of the fields to be added, and this value is then simply a dummy.
     * @param mapName - The name (or file and name) of a translation map to use to convert 
     *                  the data in the specified fields of the MARC record to the desired values 
     *                  to be included in the Solr index record.  (If mapName is null, the values 
     *                  in the record will be returned as-is.) 
     * @param returnType - The Class of the return type of the custom indexing function or the 
     *                     custom BeanShell script method, the valid expected types are String, Set<String>, or Map<String, Object>
     * @param retval - The value that was returned from the custom indexing function or the 
     *                 custom BeanShell script method
     * @param deleteIfEmpty - Indicates whether the the solr record should be deleted if no value 
     *                        was generated.
     * @return returns true if the indexing process should stop and the solr record should be deleted.
     */
    private boolean finishCustomOrScript(Map<String, Object> indexMap, String indexField, String mapName,
                                         Class<?> returnType, Object retval, boolean deleteIfEmpty)
    {
        if (returnType == null || retval == null)
            return(deleteIfEmpty);
        else if (returnType.isAssignableFrom(Map.class))
        {
            if (deleteIfEmpty && ((Map<String, String>) retval).size() == 0) return (true);
            if (retval != null)  
            {
                Map<String, String> retmap =  (Map<String, String>) retval;
                for (String retkey : retmap.keySet())
                {
                    indexMap.putAll((Map<String, String>) retval);
                }
            }
        }
        else if (returnType.isAssignableFrom(List.class))
        {
            List<String> fields = (List<String>) retval;
            if (mapName != null && findMap(mapName) != null)
            {
                List<String> newFields = new ArrayList<String>();
                for (String field: fields) 
                {
                    String newfield = Utils.remap(field, findMap(mapName), true);
                    newFields.add(newfield);
                }
                fields = newFields;
            }
            if (deleteIfEmpty && fields.size()== 0)  return (true);
            addFields(indexMap, indexField, fields);
        }
        else if (returnType.isAssignableFrom(Set.class))
        {
            Set<String> fields = (Set<String>) retval;
            if (mapName != null && findMap(mapName) != null)
                fields = Utils.remap(fields, findMap(mapName), true);
            if (deleteIfEmpty && fields.size()== 0)  return (true);
            addFields(indexMap, indexField, null, fields);
        }
        else if (returnType.isAssignableFrom(String.class))
        {
            String field = (String) retval;
            if (mapName != null && findMap(mapName) != null)
                field = Utils.remap(field, findMap(mapName), true);
            addField(indexMap, indexField, null, field);
        }
        return false;
    }
    
    /**
     * First checks whether a given BeanShell script has been already loaded, and if so returns the
     * BeanShell Interpreter created from that script.  Is it hasn't been loaded this function will 
     * read in the named script file, create a new BeanShell Interpreter, and have that Interpreter 
     * process the named script. 
     * @param scriptFileName
     * @return
     */
    private Interpreter getInterpreterForScript(String scriptFileName)
    {
        if (scriptMap.containsKey(scriptFileName))
        {
            return(scriptMap.get(scriptFileName));
        }
        Interpreter bsh = new Interpreter();
        bsh.setClassLoader(this.getClass().getClassLoader());
        String paths[] = new String[propertyFilePaths.length+1];
        System.arraycopy(propertyFilePaths, 0, paths, 1, propertyFilePaths.length);
        paths[0] = "scripts";
        InputStream script = Utils.getPropertyFileInputStream(paths, scriptFileName);
        String scriptContents;
        try
        {
            scriptContents = Utils.readStreamIntoString(script);
            bsh.eval(scriptContents);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Unable to read script: " + scriptFileName, e);
        }
        catch (EvalError e)
        {
            throw new IllegalArgumentException("Unable to evaluate script: " + scriptFileName, e);
        }
        scriptMap.put(scriptFileName, bsh);
        return(bsh);
    }

    /**
     * if the first and last characters of the string are quote marks ("), then
     * delete them.
     */
    private String dequote(String str)
    {
        if (str.length() >= 2 && str.charAt(0) == '"' && str.charAt(str.length()-1) == '"')
            return str.substring(1, str.length() - 1);

        return str;
    }

    /**
     * get values that don't require parsing specified record fields:
     *   raw, xml, date, index_date ...
     * @param indexParm - what type of value to return
     */
    private String getStd(Record record, String indexParm)
    {
        if (indexParm.equals("raw")
                || indexParm.equalsIgnoreCase("FullRecordAsMARC"))
            return writeRaw(record);
        else if (indexParm.equals("xml")
                || indexParm.equalsIgnoreCase("FullRecordAsXML"))
            return writeXml(record);
        else if (indexParm.equals("json")
                || indexParm.equalsIgnoreCase("FullRecordAsJSON"))
            return writeJson(record, true);
        else if (indexParm.equals("json2")
                || indexParm.equalsIgnoreCase("FullRecordAsJSON2"))
            return writeJson(record, false);
        else if (indexParm.equals("xml")
                || indexParm.equalsIgnoreCase("FullRecordAsText"))
            return (record.toString().replaceAll("\n", "<br/>"));
        else if (indexParm.equals("date")
                || indexParm.equalsIgnoreCase("DateOfPublication"))
            return getDate(record);
        else if (indexParm.equals("index_date")
                || indexParm.equalsIgnoreCase("DateRecordIndexed"))
            return getCurrentDate();
        return null;
    }

    /**
     * get the era field values from 045a as a Set of Strings
     */
    public Set<String> getEra(Record record)
    {
        Set<String> result = new LinkedHashSet<String>();
        String eraField = getFirstFieldVal(record, "045a");
        if (eraField == null)
            return result;

        if (eraField.length() == 4)
        {
            eraField = eraField.toLowerCase();
            char eraStart1 = eraField.charAt(0);
            char eraStart2 = eraField.charAt(1);
            char eraEnd1 = eraField.charAt(2);
            char eraEnd2 = eraField.charAt(3);
            if (eraStart2 == 'l')
                eraEnd2 = '1';
            if (eraEnd2 == 'l')
                eraEnd2 = '1';
            if (eraStart2 == 'o')
                eraEnd2 = '0';
            if (eraEnd2 == 'o')
                eraEnd2 = '0';
            return getEra(result, eraStart1, eraStart2, eraEnd1, eraEnd2);
        }
        else if (eraField.length() == 5)
        {
            char eraStart1 = eraField.charAt(0);
            char eraStart2 = eraField.charAt(1);

            char eraEnd1 = eraField.charAt(3);
            char eraEnd2 = eraField.charAt(4);
            char gap = eraField.charAt(2);
            if (gap == ' ' || gap == '-')
                return getEra(result, eraStart1, eraStart2, eraEnd1, eraEnd2);
        }
        else if (eraField.length() == 2)
        {
            char eraStart1 = eraField.charAt(0);
            char eraStart2 = eraField.charAt(1);
            if (eraStart1 >= 'a' && eraStart1 <= 'y' && 
                    eraStart2 >= '0' && eraStart2 <= '9')
                return getEra(result, eraStart1, eraStart2, eraStart1, eraStart2);
        }
        return result;
    }

    /**
     * get the two eras indicated by the four passed characters, and add them
     *  to the result parameter (which is a set).  The characters passed in are
     *  from the 045a.
     */
    public static Set<String> getEra(Set<String> result, char eraStart1, char eraStart2, char eraEnd1, char eraEnd2)
    {
        if (eraStart1 >= 'a' && eraStart1 <= 'y' && eraEnd1 >= 'a' && eraEnd1 <= 'y')
        {
            for (char eraVal = eraStart1; eraVal <= eraEnd1; eraVal++)
            {
                if (eraStart2 != '-' || eraEnd2 != '-')
                {
                    char loopStart = (eraVal != eraStart1) ? '0' : Character.isDigit(eraStart2) ? eraStart2 : '0';
                    char loopEnd = (eraVal != eraEnd1) ? '9' : Character.isDigit(eraEnd2) ? eraEnd2 : '9';
                    for (char eraVal2 = loopStart; eraVal2 <= loopEnd; eraVal2++)
                    {
                        result.add("" + eraVal + eraVal2);
                    }
                }
                result.add("" + eraVal);
            }
        }
        return result;
    }

    /**
     * Add a field-value pair to the indexMap representation of a solr doc.
     *  The value will be "translated" per the translation map indicated.
     * @param indexMap  the mapping of solr doc field names to values
     * @param ixFldName the name of the field to add to the solr doc
     * @param mapName   the name of a translation map for the field value, or null
     * @param fieldVal  the (untranslated) field value to add to the solr doc field
     */
    protected void addField(Map<String, Object> indexMap, String ixFldName, String mapName, String fieldVal)
    {
        if (mapName != null && findMap(mapName) != null)
            fieldVal = Utils.remap(fieldVal, findMap(mapName), true);

        if (fieldVal != null && fieldVal.length() > 0)
            indexMap.put(ixFldName, fieldVal);
    }

    /**
     * Add a field-value pair to the indexMap representation of a solr doc.
     * 
     * @param indexMap  the mapping of solr doc field names to values
     * @param ixFldName the name of the field to add to the solr doc
     * @param fieldVal  the (untranslated) field value to add to the solr doc field
     */
    protected void addField(Map<String, Object> indexMap, String ixFldName, String fieldVal)
    {
        addField(indexMap, ixFldName, null, fieldVal);
    }

    /**
     * Add a field-value pair to the indexMap representation of a solr doc for
     *  each value present in the "fieldVals" parameter.
     *  The values will be "translated" per the translation map indicated.
     * @param indexMap  the mapping of solr doc field names to values
     * @param ixFldName the name of the field to add to the solr doc
     * @param mapName   the name of a translation map for the field value, or null
     * @param fieldVals a set of (untranslated) field values to be assigned to the solr doc field
     */
    // TODO: refactor so fieldVals is Collection<String>, must change Utils.remap first
    protected void addFields(Map<String, Object> indexMap, String ixFldName, String mapName, Set<String> fieldVals)
    {
        if (mapName != null && findMap(mapName) != null)
            fieldVals = Utils.remap(fieldVals, findMap(mapName), true);

        if (!fieldVals.isEmpty())
        {
            if (fieldVals.size() == 1)
            {
                String value = fieldVals.iterator().next();
                indexMap.put(ixFldName, value);
            }
            else
                indexMap.put(ixFldName, fieldVals);
        }
    }
    
    /**
     * Add a field-value pair to the indexMap representation of a solr doc for
     *  each value present in the "fieldVals" parameter.
     *  The values will be "translated" per the translation map indicated.
     *  
     * @param indexMap  the mapping of solr doc field names to values
     * @param ixFldName the name of the field to add to the solr doc
     * @param fieldVals a <code>Collection</code> of (untranslated) field values
     *                  to be assigned to the solr doc field, usuall a <code>Set</code>
     *                  or <code>List</code>. 
     */
    protected void addFields(Map<String, Object> indexMap, String ixFldName, Collection<String> fieldVals)
    {
        if (!fieldVals.isEmpty())
        {
            if (fieldVals.size() == 1)
            {
                String value = fieldVals.iterator().next();
                indexMap.put(ixFldName, value);
            }
            else
                indexMap.put(ixFldName, fieldVals);
        }
    }

    /**
     * Get <code>Collection</code> of Strings as indicated by tagStr. For each field 
     * spec in the tagStr that is NOT about bytes (i.e. not a 008[7-12] type fieldspec),  
     * the result string is the concatenation of all the specific subfields.
     * 
     * @param record -
     *            the marc record object
     * @param tagStr
     *            string containing which field(s)/subfield(s) to use. This is a
     *            series of: marc "tag" string (3 chars identifying a marc
     *            field, e.g. 245) optionally followed by characters identifying
     *            which subfields to use. Separator of colon indicates a
     *            separate value, rather than concatenation. 008[5-7] denotes
     *            bytes 5-7 of the 008 field (0 based counting) 100[a-cf-z]
     *            denotes the bracket pattern is a regular expression indicating
     *            which subfields to include. Note: if the characters in the
     *            brackets are digits, it will be interpreted as particular
     *            bytes, NOT a pattern. 100abcd denotes subfields a, b, c, d are
     *            desired.
     * @param collector
     *            object in which to collect the data from the fields described by
     *            <code>tagStr</code>. A <code>Set</code> will automatically de-dupe
     *            values, a <code>List</code> will allow values to repeat. 
     */
    public static void getFieldListCollector(Record record, 
                                             String tagStr, 
                                             Collection<String> collector)
    {
        String[] tags = tagStr.split(":");
        //Set<String> collector = new LinkedHashSet<String>();
        for (int i = 0; i < tags.length; i++)
        {
            // Check to ensure tag length is at least 3 characters
            if (tags[i].length() < 3)
            {
                System.err.println("Invalid tag specified: " + tags[i]);
                continue;
            }

            // Get Field Tag
            String tag = tags[i].substring(0, 3);
            boolean linkedField = false;
            if (tag.equals("LNK"))
            {
                tag = tags[i].substring(3, 6);
                linkedField = true;
            }
            // Process Subfields
            String subfield = tags[i].substring(3);
            boolean havePattern = false;
            int subend = 0;
            // brackets indicate parsing for individual characters or as pattern
            int bracket = tags[i].indexOf('[');
            if (bracket != -1)
            {
                String sub[] = tags[i].substring(bracket + 1).split("[\\]\\[\\-, ]+");
                try
                {
                    // if bracket expression is digits, expression is treated as character positions
                    int substart = Integer.parseInt(sub[0]);
                    subend = (sub.length > 1) ? Integer.parseInt(sub[1]) + 1 : substart + 1;
                    String subfieldWObracket = subfield.substring(0, bracket-3);
                    getSubfieldDataCollector(record, tag, subfieldWObracket, substart, subend, collector);
                }
                catch (NumberFormatException e)
                {
                    // assume brackets expression is a pattern such as [a-z]
                    havePattern = true;
                }
            }
            if (subend == 0) // don't want specific characters.
            {
                String separator = null;
                if (subfield.indexOf('\'') != -1)
                {
                    separator = subfield.substring(subfield.indexOf('\'') + 1, subfield.length() - 1);
                    subfield = subfield.substring(0, subfield.indexOf('\''));
                }

                if (havePattern)
                    if (linkedField)
                    	getLinkedFieldValueCollector(record, tag, subfield, separator, collector);
                    else
                        getAllSubfieldsCollector(record, tag + subfield, separator, collector);
                else if (linkedField)
                	getLinkedFieldValueCollector(record, tag, subfield, separator, collector);
                else
                	getSubfieldDataCollector(record, tag, subfield, separator, collector);
            }
        }
        return;
    }

    /**
     * Get Set of Strings as indicated by tagStr. For each field spec in the
     * tagStr that is NOT about bytes (i.e. not a 008[7-12] type fieldspec), the
     * result string is the concatenation of all the specific subfields.
     * 
     * @param record -
     *            the marc record object
     * @param tagStr
     *            string containing which field(s)/subfield(s) to use. This is a
     *            series of: marc "tag" string (3 chars identifying a marc
     *            field, e.g. 245) optionally followed by characters identifying
     *            which subfields to use. Separator of colon indicates a
     *            separate value, rather than concatenation. 008[5-7] denotes
     *            bytes 5-7 of the 008 field (0 based counting) 100[a-cf-z]
     *            denotes the bracket pattern is a regular expression indicating
     *            which subfields to include. Note: if the characters in the
     *            brackets are digits, it will be interpreted as particular
     *            bytes, NOT a pattern. 100abcd denotes subfields a, b, c, d are
     *            desired.
     * @return the contents of the indicated marc field(s)/subfield(s), as a set
     *         of Strings.
     */
    public static Set<String> getFieldList(Record record, String tagStr)
    {
        Set<String> result = new LinkedHashSet<String>();
        getFieldListCollector(record, tagStr, result);
        return result;
    }

    /**
     * Get <code>List</code> of Strings as indicated by tagStr. For each field spec in the
     * tagStr that is NOT about bytes (i.e. not a 008[7-12] type fieldspec), the
     * result string is the concatenation of all the specific subfields.
     * 
     * @param record -
     *            the marc record object
     * @param tagStr
     *            string containing which field(s)/subfield(s) to use. This is a
     *            series of: marc "tag" string (3 chars identifying a marc
     *            field, e.g. 245) optionally followed by characters identifying
     *            which subfields to use. Separator of colon indicates a
     *            separate value, rather than concatenation. 008[5-7] denotes
     *            bytes 5-7 of the 008 field (0 based counting) 100[a-cf-z]
     *            denotes the bracket pattern is a regular expression indicating
     *            which subfields to include. Note: if the characters in the
     *            brackets are digits, it will be interpreted as particular
     *            bytes, NOT a pattern. 100abcd denotes subfields a, b, c, d are
     *            desired.
     * @return the contents of the indicated marc field(s)/subfield(s).
     */
    public static List<String> getFieldListAsList(Record record, String tagStr)
    {
        List<String> result = new ArrayList<String>();
        getFieldListCollector(record, tagStr, result);
        return result;
    }

    /**
     * Get the value that Solr should use as the <code>id</code> for this record.
     * If <code>indexMap</code> has an entry for <code>id</code>, use that definition;
     * if not, use the contents of the first <code>001</code>.
     * 
     * @param  record
     * @param  indexMap
     * @return Solr <code>id</code> for the record
     */
    public String getSolrId(Record record, Map<String, Object> indexMap)
    {      
        if (!indexMap.containsKey("id"))
        {
            if (fieldMap.containsKey("id"))
                addFieldValueToMap(record, "id", indexMap);
            else
            {
                addField(indexMap, "id", getFirstFieldVal(record, null, "001"));
            }
        }
        Object keyObj = indexMap.get("id");
        if (!(keyObj instanceof String))
        {
            throw new IllegalArgumentException("Value computed for Solr value \"id\" value must be a single String");
        }
        return ((String)keyObj);
    }
    
    /**
     * Intended to be called as a custom method from an indexing properties
     * file: some_field = custom, getWithOptions( marcFieldSpec, options)
     *
     *  marcFieldSpec is the ordinary field spec for SolrIndexer, eg
     *  "245a:500az" etc. 
     *
     *  Options is a colon-seperated list of one or more of the following:
     *
     *  first  => take first value found only
     *  includeLinkedFields => include all 880 linked fields for spec, with getLinkedField
     *  removeTrailingPunct => Remove trailing punctuation from all values, using Util.cleanData. 
     * map=mapName  =>   A map to send all values through, using the same semantics as the other SolrIndexer mapping functions. Eg, "map=somefile.properties" or "map=map_defined_in_index_properties". Utils.remap is used to do the mapping. 
     * "default=Default Value" => A default value to use whenever there are otherwise no values found for this index entry. Default value can contain spaces, but not colons (since we parse colon-seperated). If a map is used, and there are marc fields matching the spec,  this default value will be used ONLY if the map returns _no_ values (map is not set to pass through, map does not have default value). If there are no marc fields matching the spec, the default value will always be used. 
     * combineSubfields=joinStr => combine all subfields in the same marc field in one Solr field, joined by the joinStr. Will use getAllSubfields to fetch, instead of getFieldList. joinStr can include spaces but not colons. If you want it to end in a space, and it's the last option in an options list, just add a terminal colon.   "combineSubfields= :"
     *
     * eg:  some_field = custom, getWithOptions(245a:500a, includeLinkedFields:removeTrailingPunct:default=Unknown)
     * 
     */
    public Set<String> getWithOptions(Record record, String tagStr, String optionStr) {
      //default options
      boolean first = false;
      boolean includeLinked = false;
      boolean removeTrailingPunct = false;
      String strDefault = null;
      String mapName = null;
      String combineSubfieldsJoin = null;
      String condition = null;
      //specified options
      String[] options = optionStr.split(":");
      for (int i = 0; i < options.length; i++)
      {
          String option = options[i];
          if (option.equals("first"))
          {
              first = true;
          }
          else if (option.equals("includedLinkedFields"))
          {
              includeLinked = true;
          }
          else if (option.equals("removeTrailingPunct"))
          {
              removeTrailingPunct = true;
          }
          else if (option.length() > 4 && option.substring(0, 4).equals("map="))
          {
              mapName = option.substring(4, option.length());
          }
          else if (option.length() > 8 && option.substring(0, 8).equals("default="))
          {
              strDefault = option.substring(8, option.length());
          }
          else if (option.length() > 17 && option.substring(0, 17).equals("combineSubfields="))
          {
              combineSubfieldsJoin = option.substring(17, option.length());
              if (combineSubfieldsJoin.length() > 2 && combineSubfieldsJoin.startsWith("\"") && combineSubfieldsJoin.endsWith("\""))
              {
                  combineSubfieldsJoin = combineSubfieldsJoin.substring(1, combineSubfieldsJoin.length() - 1);
              }
          }
//          else if (option.startsWith("condition="))
//          {
//              condition = option.substring("condition=".length());
//              if (combineSubfieldsJoin.length() > 2 && combineSubfieldsJoin.startsWith("\"") && combineSubfieldsJoin.endsWith("\""))
//              {
//                  combineSubfieldsJoin = combineSubfieldsJoin.substring(1, combineSubfieldsJoin.length() - 1);
//              }
//          }
      }
      
      
       // While getFieldList and similar methods are
       // declared as returning only a Set, for
       // 'first' functionality to work, it better be a LinkedHashSet
       // with predictable order! That we can't really guarantee this
       // seems to be a flaw in the jdk library designs, there's no
       // interface for predictable-order-set. oh well.
       
       // We need to use two different methods depending on if we're doing
      // a combineSubfieldsJoin or not. TODO, would be nice to refactor
      // SolrIndexer to make this more straightforward. 
      
      Set<String> results = null;
      if ( combineSubfieldsJoin == null ) {      
        results = getFieldList(record, tagStr);
      }
      else {
        //combine subfields!
        results = getAllSubfields(record, tagStr, combineSubfieldsJoin);
      }
       

       
       //linked fields?
       if (includeLinked) {
          results.addAll(getLinkedField(record, tagStr));    
       }       
       
       //first only?       
       if ( results.size() > 0 && first) {
         Iterator<String> iter = results.iterator();
         Set<String> newResults = new LinkedHashSet<String>();
         newResults.add(iter.next());
         results = newResults;
       }
       
       //Map?
       if (mapName != null ) {
         //TODO: It's somewhat inefficient to call loadTranslationMap
         // when it may already have beenloaded. But it's the only
         // way to get the internal map name we need for later call
         // to findMap, to pass to remap. Code in rest of this class
         // should be refactored to make this a lot more reasonable.  
         String internalMapName = loadTranslationMap(mapName);         
         results = Utils.remap(results, findMap(internalMapName), true);         
       }
       
       //removeTrailingPunct?
       if ( removeTrailingPunct ) {
         Set<String> newResults = new LinkedHashSet<String>();
         for (String s : results)
         {
            newResults.add(Utils.cleanData(s));
         }
         results = newResults;
       }
       
       //default value?
       if ( (results.size() == 0) && (strDefault != null) ) {
         results.add(strDefault); 
       }
       
       return results;
    }

    
    /**
     * Get all field values specified by tagStr, joined as a single string.
     * @param record - the marc record object
     * @param tagStr string containing which field(s)/subfield(s) to use. This 
     *  is a series of: marc "tag" string (3 chars identifying a marc field, 
     *  e.g. 245) optionally followed by characters identifying which subfields 
     *  to use.
     * @param separator string separating values in the result string
     * @return single string containing all values of the indicated marc
     *         field(s)/subfield(s) concatenated with separator string
     */
    public String getFieldVals(Record record, String tagStr, String separator)
    {
        Set<String> result = getFieldList(record, tagStr);
        return org.solrmarc.tools.Utils.join(result, separator);
    }

    /**
     * Get the first value specified by the tagStr
     * @param record - the marc record object
     * @param tagStr string containing which field(s)/subfield(s) to use. This 
     *  is a series of: marc "tag" string (3 chars identifying a marc field, 
     *  e.g. 245) optionally followed by characters identifying which subfields 
     *  to use.
     * @return first value of the indicated marc field(s)/subfield(s) as a string
     */
    public static String getFirstFieldVal(Record record, String tagStr)
    {
        Set<String> result = getFieldList(record, tagStr);
        Iterator<String> iter = result.iterator();
        if (iter.hasNext())
            return iter.next();
        else
            return null;
    }

    /**
     * Get the first field value, which is mapped to another value. If there is
     * no mapping for the value, use the mapping for the empty key, if it
     * exists, o.w., use the mapping for the __DEFAULT key, if it exists.
     * @param record - the marc record object
     * @param mapName - name of translation map to use to xform values
     * @param tagStr - which field(s)/subfield(s) to use
     * @return first value as a string
     */
    public String getFirstFieldVal(Record record, String mapName, String tagStr)
    {
        Set<String> result = getFieldList(record, tagStr);
        if (mapName != null && findMap(mapName) != null)
        {
            result = Utils.remap(result, findMap(mapName), false);
            if (findMap(mapName).containsKey(""))
            {
                result.add(findMap(mapName).get(""));
            }
            if (findMap(mapName).containsKey("__DEFAULT"))
            {
                result.add(findMap(mapName).get("__DEFAULT"));
            }
        }
        Iterator<String> iter = result.iterator();
        if (iter.hasNext())
            return iter.next();
        else
            return null;
    }

    /**
     * Default version of format retrieval, invoke the getContentTypesAndMediaTypesMapped method
     * of the GetFormatMixin and then map the returned result through the map passed in, or the 
     * map defined in the file getformat_mixin_map.properties 
     * @param record - the marc record object
     * @param formatMapName - the the properties file (and optional map name) of the translation map 
     *                        to use to remap the return value.
     * @return set of format strings after being mapped via the passed in map. 
     */

    public Set<String> getFormatMapped(final Record record, String formatMapName)
    {    
        SolrIndexerMixin formatmixin = findMixin("org.solrmarc.index.GetFormatMixin");
        Set<String>formats = formatmixin.invokeByName("getContentTypesAndMediaTypesMapped", record, formatMapName);                
 //       formats.add("Online"); // Online
 //       formats.remove("Computer Resource"); // Online
        return(formats);
    }
    
    /**
     * Default version of format retrieval, invoke the getContentTypesAndMediaTypesMapped method
     * of the GetFormatMixin and then map the returned result through the map passed in, or the 
     * map defined in the file getformat_mixin_map.properties 
     * @param record - the marc record object
     * @return set of format strings after being mapped via the passed in map. 
     */

    public Set<String> getFormat(final Record record)
    {    
        return(getFormatMapped(record, "getformat_mixin_map.properties"));
    }
    
    
    /**
     * Get the 245a (and 245b, if it exists, concatenated with a space between
     *  the two subfield values), with trailing punctuation removed.
     *    See org.solrmarc.tools.Utils.cleanData() for details on the 
     *     punctuation removal
     * @param record - the marc record object
     * @return 245a, b, and k values concatenated in order found, with trailing punct removed. Returns empty string if no suitable title found. 
     */
    public String getTitle(Record record)
    {
        DataField titleField = (DataField) record.getVariableField("245");
        if ( titleField == null) {
          return ""; 
        }
        
        StringBuilder titleBuilder = new StringBuilder();

        Iterator<Subfield> iter = titleField.getSubfields().iterator();
        while ( iter.hasNext() ) {
          Subfield f = iter.next(); 
          char code = f.getCode();
          if ( code == 'a' || code == 'b' || code == 'k' ) {
             titleBuilder.append(f.getData()); 
          }
        }        

        return Utils.cleanData(titleBuilder.toString());
    }

    /**
     * Get the title (245ab) from a record, without non-filing chars as
     * specified in 245 2nd indicator, and lowercased. 
     * @param record - the marc record object
     * @return 245a and 245b values concatenated, with trailing punct removed,
     *         and with non-filing characters omitted. Null returned if no
     *         title can be found. 
     * 
     * @see SolrIndexer#getTitle
     */
    public String getSortableTitle(Record record)
    {
        DataField titleField = (DataField) record.getVariableField("245");
        if (titleField == null)
            return "";
          
        int nonFilingInt = getInd2AsInt(titleField);
        
        String title = getTitle(record);
        title = title.toLowerCase();
        
        //Skip non-filing chars, if possible. 
        if ( title.length() > nonFilingInt )  {
          title = title.substring(nonFilingInt);          
        }
        
        if ( title.length() == 0) {
          return null;
        }                
        
        return title;
    }

    /**
     * returns string for author sort:  a string containing
     *  1. the main entry author, if there is one 
     *  2. the main entry uniform title (240), if there is one - not including 
     *    non-filing chars as noted in 2nd indicator
     * followed by
     *  3.  the 245 title, not including non-filing chars as noted in ind 2
     */
    @SuppressWarnings("unchecked")
    public String getSortableAuthor(final Record record)
    {
        StringBuffer resultBuf = new StringBuffer();

        DataField df = (DataField) record.getVariableField("100");
        // main entry personal name
        if (df != null)
            resultBuf.append(getAlphaSubfldsAsSortStr(df, false));

        df = (DataField) record.getVariableField("110");
        // main entry corporate name
        if (df != null)
            resultBuf.append(getAlphaSubfldsAsSortStr(df, false));

        df = (DataField) record.getVariableField("111");
        // main entry meeting name
        if (df != null)
            resultBuf.append(getAlphaSubfldsAsSortStr(df, false));

        // need to sort fields missing 100/110/111 last
        if (resultBuf.length() == 0)
        {
            resultBuf.append(Character.toChars(Character.MAX_CODE_POINT));
            resultBuf.append(' '); // for legibility in luke
        }

        // uniform title, main entry
        df = (DataField) record.getVariableField("240");
        if (df != null)
            resultBuf.append(getAlphaSubfldsAsSortStr(df, false));

        // 245 (required) title statement
        df = (DataField) record.getVariableField("245");
        if (df != null)
            resultBuf.append(getAlphaSubfldsAsSortStr(df, true));

        // Solr field properties should convert to lowercase
        return resultBuf.toString().trim();
    }

    /**
     * Return the date in 260c as a string
     * @param record - the marc record object
     * @return 260c, "cleaned" per org.solrmarc.tools.Utils.cleanDate()
     */
    public String getDate(Record record)
    {
        String date = getFieldVals(record, "260c", ", ");
        if (date == null || date.length() == 0)
            return (null);
        return Utils.cleanDate(date);
    }
    
    /**
     * Stub more advanced version of getDate that looks in the 008 field as well as the 260c field
     * this routine does some simple sanity checking to ensure that the date to return makes sense. 
     * @param record - the marc record object
     * @return 260c or 008[7-10] or 008[11-14], "cleaned" per org.solrmarc.tools.Utils.cleanDate()
     */
    public String getPublicationDate(final Record record)
    {
        String field008 = getFirstFieldVal(record, "008");
        String pubDateFull = getFieldVals(record, "260c", ", ");
        String pubDateJustDigits = pubDateFull.replaceAll("[^0-9]", "");       
        String pubDate260c = getDate(record);
        if (field008 == null || field008.length() < 16) 
        {
            return(pubDate260c);
        }
        String field008_d1 = field008.substring(7, 11);
        String field008_d2 = field008.substring(11, 15);
        String retVal = null;
        char dateType = field008.charAt(6);
        if (dateType == 'r' && field008_d2.equals(pubDate260c)) retVal = field008_d2;
        else if (field008_d1.equals(pubDate260c))               retVal = field008_d1;
        else if (field008_d2.equals(pubDate260c))               retVal = field008_d2;
        else if (pubDateJustDigits.length() == 4 && pubDate260c != null &&
                 pubDate260c.matches("(20|19|18|17|16|15)[0-9][0-9]"))
                                                                retVal = pubDate260c;
        else if (field008_d1.matches("(20|1[98765432])[0-9][0-9]"))        
                                                                retVal = field008_d1;
        else if (field008_d2.matches("(20|1[98765432])[0-9][0-9]"))        
                                                                retVal = field008_d2;
        else                                                    retVal = pubDate260c;
        return(retVal);
    }
    
    /**
     * Return the index datestamp as a string
     */
    public String getCurrentDate()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        return df.format(indexDate);
    }

    // URL Methods -------------------- Begin -------------------------- URL
    // Methods

    /**
     * returns the URLs for the full text of a resource described by the record
     * 
     * @param record
     * @return Set of Strings containing full text urls, or empty set if none
     */
    @SuppressWarnings("unchecked")
    public Set<String> getFullTextUrls(final Record record)
    {
        Set<String> resultSet = new LinkedHashSet<String>();

        List<VariableField> list856 = record.getVariableFields("856");
        for (VariableField vf : list856)
        {
            DataField df = (DataField) vf;
            List<String> possUrls = Utils.getSubfieldStrings(df, 'u');
            if (possUrls.size() > 0)
            {
                char ind2 = df.getIndicator2();
                switch (ind2)
                {
                    case '0':
                        resultSet.addAll(possUrls);
                        break;
                    case '2':
                        break;
                    default:
                        if (!isSupplementalUrl(df))
                            resultSet.addAll(possUrls);
                        break;
                }
            }
        }

        return resultSet;
    }

    /**
     * returns the URLs for supplementary information (rather than fulltext)
     * 
     * @param record
     * @return Set of Strings containing supplementary urls, or empty string if
     *         none
     */
    @SuppressWarnings("unchecked")
    public Set<String> getSupplUrls(final Record record)
    {
        Set<String> resultSet = new LinkedHashSet<String>();

        List<VariableField> list856 = record.getVariableFields("856");
        for (VariableField vf : list856)
        {
            DataField df = (DataField) vf;
            List<String> possUrls = Utils.getSubfieldStrings(df, 'u');
            char ind2 = df.getIndicator2();
            switch (ind2)
            {
                case '2':
                    resultSet.addAll(possUrls);
                    break;
                case '0':
                    break;
                default:
                    if (isSupplementalUrl(df))
                        resultSet.addAll(possUrls);
                    break;
            }
        }
        return resultSet;
    }

    /**
     * return true if passed 856 field contains a supplementary url (rather than
     * a fulltext URL. Determine by presence of "table of contents" or "sample
     * text" string (ignoring case) in subfield 3 or z. Note: Called only when
     * second indicator is not 0 or 2.
     */
    protected boolean isSupplementalUrl(DataField f856)
    {
        boolean supplmntl = false;
        List<String> list3z = Utils.getSubfieldStrings(f856, '3');
        list3z.addAll(Utils.getSubfieldStrings(f856, 'z'));
        for (String s : list3z)
        {
            String lc = s.toLowerCase();
            if (lc.contains("table") || lc.contains("some") || lc.contains("suppl") || lc.contains("errata")
               || lc.matches("^no[t]? ") || lc.contains("front") || lc.contains("search") || lc.contains("summary")
               || lc.contains("cover") || lc.contains("additional") || lc.contains("glosser") || lc.contains("appendix")
               || lc.contains("guide") || lc.contains("inhalts") || lc.contains("version") || lc.contains("addendum")
               || lc.contains("abstract") || lc.contains("index") || lc.contains("digest") || lc.contains("contents")
               || lc.contains("klappentext") || lc.contains("verlagsinformation") || lc.contains("lizenzfrei") || lc.contains("rezension") 
               || lc.contains("sample") || lc.contains("related") || lc.contains("record"))
                
                supplmntl = true;
        }
        return supplmntl;
    }

    // URL Methods -------------------- End -------------------------- URL
    // Methods

    /**
     * Get the appropriate Map object from populated transMapMap
     * @param mapName the name of the translation map to find
     * @return populated Map object
     */
    public Map<String, String> findMap(String mapName)
    {
        if (mapName.startsWith("pattern_map:"))
            mapName = mapName.substring("pattern_map:".length());

        if (transMapMap.containsKey(mapName))
            return (transMapMap.get(mapName));

        return null;
    }

    public static boolean isControlField(String fieldTag)
    {
        if (fieldTag.matches("00[0-9]"))
        {
            return (true);
        }
        return (false);
    }

    /**
     * Get the specified subfields from the specified MARC field, returned as a
     * set of strings to become lucene document field values
     * 
     * @param record     the MARC record object
     * @param fldTag     the field name, e.g. 245
     * @param subfldsStr the string containing the desired subfields
     * @param separator  the separator string to insert between subfield items (if null, a " " will be 
     *                   used)
     * @param collector  an object to accumulate the data indicated by <code>fldTag</code> and 
     *                   <code>subfldsStr</code>.
     */
    public static void getSubfieldDataCollector(Record record, String fldTag, String subfldsStr, 
    											String separator, Collection<String> collector)
    {
        // Process Leader
        if (fldTag.equals("000"))
        {
            collector.add(record.getLeader().toString());
            return;
        }

        // Loop through Data and Control Fields
        // int iTag = new Integer(fldTag).intValue();
        List<VariableField> varFlds = record.getVariableFields(fldTag);
        for (VariableField vf : varFlds)
        {
            if (!isControlField(fldTag) && subfldsStr != null)
            {
                // DataField
                DataField dfield = (DataField) vf;

                if (subfldsStr.length() > 1 || separator != null)
                {
                    // concatenate subfields using specified separator or space
                    StringBuffer buffer = new StringBuffer("");
                    List<Subfield> subFlds = dfield.getSubfields();
                    for (Subfield sf : subFlds)
                    {
                        if (subfldsStr.indexOf(sf.getCode()) != -1)
                        {
                            if (buffer.length() > 0)
                                buffer.append(separator != null ? separator : " ");
                            buffer.append(sf.getData().trim());
                        }
                    }
                    if (buffer.length() > 0)
                        collector.add(buffer.toString());
                }
                else
                {
                    // get all instances of the single subfield
                    List<Subfield> subFlds = dfield.getSubfields(subfldsStr.charAt(0));
                    for (Subfield sf : subFlds)
                    {
                        collector.add(sf.getData().trim());
                    }
                }
            }
            else
            {
                // Control Field
                collector.add(((ControlField) vf).getData().trim());
            }
        }
        return;
    }

    /**
     * Get the specified substring of subfield values from the specified MARC 
     * field, returned as  a set of strings to become lucene document field values
     * @param record - the marc record object
     * @param fldTag - the field name, e.g. 008
     * @param subfldStr - the string containing the desired subfields
     * @param beginIx - the beginning index of the substring of the subfield value
     * @param endIx - the ending index of the substring of the subfield value
     * @param collector  an object to accumulate the data indicated by <code>fldTag</code> and 
     *                   <code>subfldsStr</code>.
     */
    public static void getSubfieldDataCollector(Record record, String fldTag, String subfldStr, 
    		           int beginIx, int endIx, Collection<String> collector)
    {
        // Process Leader
        if (fldTag.equals("000"))
        {
            collector.add(record.getLeader().toString().substring(beginIx, endIx));
            return;
        }

        // Loop through Data and Control Fields
        List<VariableField> varFlds = record.getVariableFields(fldTag);
        for (VariableField vf : varFlds)
        {
            if (!isControlField(fldTag) && subfldStr != null)
            {
                // Data Field
                DataField dfield = (DataField) vf;
                if (subfldStr.length() > 1)
                {
                    // automatic concatenation of grouped subfields
                    StringBuffer buffer = new StringBuffer("");
                    List<Subfield> subFlds = dfield.getSubfields();
                    for (Subfield sf : subFlds)
                    {
                        if (subfldStr.indexOf(sf.getCode()) != -1 && 
                                sf.getData().length() >= endIx)
                        {
                            if (buffer.length() > 0)
                                buffer.append(" ");
                            buffer.append(sf.getData().substring(beginIx, endIx));
                        }
                    }
                    collector.add(buffer.toString());
                }
                else
                {
                    // get all instances of the single subfield
                    List<Subfield> subFlds = dfield.getSubfields(subfldStr.charAt(0));
                    for (Subfield sf : subFlds)
                    {
                        if (sf.getData().length() >= endIx)
                            collector.add(sf.getData().substring(beginIx, endIx));
                    }
                }
            }
            else  // Control Field
            {
                String cfldData = ((ControlField) vf).getData();
                if (cfldData.length() >= endIx)
                    collector.add(cfldData.substring(beginIx, endIx));
            }
        }
        return;
    }
    
    /**
     * Get the specified subfields from the specified MARC field, returned as a
     * set of strings to become lucene document field values
     * 
     * @param record     the marc record object
     * @param fldTag     the field name, e.g. 245
     * @param subfldsStr the string containing the desired subfields
     * @param separator  the separator string to insert between subfield items 
     *                   (if <code>null</code>, a " " will be used)
     * @return           a Set of String, where each string is the concatenated contents of all the
     *                   desired subfield values from a single instance of the <code>fldTag</code>
     */
    public static Set<String> getSubfieldDataAsSet(Record record, String fldTag, String subfldsStr, String separator)
    {
        Set<String> result = new LinkedHashSet<String>();
        SolrIndexer.getSubfieldDataCollector(record, fldTag, subfldsStr, separator, result);
        return result;
    }
    
    /**
     * Get the specified substring of subfield values from the specified MARC 
     * field, returned as  a set of strings to become lucene document field values
     * @param record    the marc record object
     * @param fldTag    the field name, e.g. 008
     * @param subfldStr the string containing the desired subfields
     * @param beginIx   the beginning index of the substring of the subfield value
     * @param endIx     the ending index of the substring of the subfield value
     * @return          the result set of strings
     */
    public static Set<String> getSubfieldDataAsSet(Record record, String fldTag, String subfldStr, int beginIx, int endIx)
    {
        Set<String> result = new LinkedHashSet<String>();
        SolrIndexer.getSubfieldDataCollector(record, fldTag, subfldStr, beginIx, endIx, result);
        return result;
    }
    
    /**
     * Get the specified subfields from the specified MARC field, returned as a
     * set of strings to become lucene document field values
     * 
     * @param record    the marc record object
     * @param fldTag    the field name, e.g. 245
     * @param subfldStr the string containing the desired subfields
     * @param separator  the separator string to insert between subfield items 
     *                   (if <code>null</code>, a " " will be used)
     * @return           a List of String, where each string is the concatenated contents of all the
     *                   desired subfield values from a single instance of the <code>fldTag</code>
     */
    public static List<String> getSubfieldDataAsList(Record record, String fldTag, String subfldStr, String separator)
    {
    	List<String> result = new ArrayList<String>();
        SolrIndexer.getSubfieldDataCollector(record, fldTag, subfldStr, separator, result);
        return result;
    }
    
    /**
     * Get the specified substring of subfield values from the specified MARC 
     * field, returned as  a set of strings to become lucene document field values
     * @param record    the marc record object
     * @param fldTag    the field name, e.g. 008
     * @param subfldStr the string containing the desired subfields
     * @param beginIx   the beginning index of the substring of the subfield value
     * @param endIx     the ending index of the substring of the subfield value
     * @return          the result list of strings
     */
    public static List<String> getSubfieldDataAsList(Record record, String fldTag, String subfldStr, int beginIx, int endIx)
    {
    	List<String> result = new ArrayList<String>();
        SolrIndexer.getSubfieldDataCollector(record, fldTag, subfldStr, beginIx, endIx, result);
        return result;
    }
    
    /**
     * Write a marc record as a binary string to the
     * @param record marc record object to be written
     * @return string containing binary (UTF-8 encoded) representation of marc
     *         record object.
     */
    protected String writeRaw(Record record)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MarcWriter writer = new MarcStreamWriter(out, "UTF-8", true);
        writer.write(record);
        writer.close();

        String result = null;
        try
        {
            result = out.toString("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // e.printStackTrace();
            logger.error(e.getCause());
        }
        return result;
    }

    /**
     * Write a marc record as a json string to the
     * @param record marc record object to be written
     * @return string containing binary (UTF-8 encoded) representation of marc
     *         record object.
     */
    protected String writeJson(Record record, boolean MARCinJSON)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MarcWriter writer = new MarcJsonWriter(out, (MARCinJSON) ? MarcJsonWriter.MARC_IN_JSON : MarcJsonWriter.MARC_JSON);
        writer.write(record);
        writer.close();

        String result = null;
        try
        {
            result = out.toString("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // e.printStackTrace();
            logger.error(e.getCause());
        }
        return result;
    }

    /**
     * Write a marc record as a string containing MarcXML
     * @param record marc record object to be written
     * @return String containing MarcXML representation of marc record object
     */
    protected String writeXml(Record record)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // TODO: see if this works better
        // MarcWriter writer = new MarcXmlWriter(out, false);
        MarcWriter writer = new MarcXmlWriter(out, "UTF-8");
        writer.write(record);
        writer.close();

        String tmp = null;
        try
        {
            tmp = out.toString("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // e.printStackTrace();
            logger.error(e.getCause());
        }
        return tmp;
    }

    /**
     * remove trailing punctuation (default trailing characters to be removed)
     *    See org.solrmarc.tools.Utils.cleanData() for details on the 
     *     punctuation removal
     * @param record marc record object
     * @param fieldSpec - the field to have trailing punctuation removed
     * @return Set of strings containing the field values with trailing
     *         punctuation removed
     */
    public Set<String> removeTrailingPunct(Record record, String fieldSpec)
    {
        Set<String> result = getFieldList(record, fieldSpec);
        Set<String> newResult = new LinkedHashSet<String>();
        for (String s : result)
        {
            newResult.add(Utils.cleanData(s));
        }
        return newResult;
    }

    /**
     * extract all the subfields requested in requested marc fields. Each
     * instance of each marc field will be put in a separate result (but the
     * subfields will be concatenated into a single value for each marc field)
     * 
     * @param record
     *            marc record object
     * @param fieldSpec -
     *            the desired marc fields and subfields as given in the
     *            xxx_index.properties file
     * @param separator -
     *            the character to use between subfield values in the solr field
     *            contents
     * @param collector
     *            object in which to collect the data from the fields described by
     *            <code>tagStr</code>. A <code>Set</code> will automatically de-dupe
     *            values, a <code>List</code> will allow values to repeat. 
     */
    public static void getAllSubfieldsCollector(final Record record, 
                                                String fieldSpec, String separator, 
                                                Collection<String> collector)
    {
        String[] fldTags = fieldSpec.split(":");
        for (int i = 0; i < fldTags.length; i++)
        {
            // Check to ensure tag length is at least 3 characters
            if (fldTags[i].length() < 3)
            {
                System.err.println("Invalid tag specified: " + fldTags[i]);
                continue;
            }

            String fldTag = fldTags[i].substring(0, 3);

            String subfldTags = fldTags[i].substring(3);

            List<VariableField> marcFieldList = record.getVariableFields(fldTag);
            if (!marcFieldList.isEmpty())
            {
                Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
                for (VariableField vf : marcFieldList)
                {
                    DataField marcField = (DataField) vf;
                    StringBuffer buffer = new StringBuffer("");
                    List<Subfield> subfields = marcField.getSubfields();
                    for (Subfield subfield : subfields)
                    {
                        Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
                        if (matcher.matches())
                        {
                            if (buffer.length() > 0)
                                buffer.append(separator != null ? separator : " ");
                            buffer.append(subfield.getData().trim());
                        }
                    }
                    if (buffer.length() > 0)
                        collector.add(Utils.cleanData(buffer.toString()));
                }
            }
        }

        return;
    }

    /**
     * extract all the subfields requested in requested marc fields. Each
     * instance of each marc field will be put in a separate result (but the
     * subfields will be concatenated into a single value for each marc field)
     * 
     * @param record
     *            marc record object
     * @param fieldSpec -
     *            the desired marc fields and subfields as given in the
     *            xxx_index.properties file
     * @param separator -
     *            the character to use between subfield values in the solr field
     *            contents
     * @return Set of values (as strings) for solr field
     */
    public static Set<String> getAllSubfields(final Record record, String fieldSpec, String separator)
    {
        Set<String> result = new LinkedHashSet<String>();
        getAllSubfieldsCollector(record, fieldSpec, separator, result);
        return result;
    }

    /**
     * extract all the subfields requested in requested marc fields. Each
     * instance of each marc field will be put in a separate result (but the
     * subfields will be concatenated into a single value for each marc field)
     * 
     * @param record
     *            marc record object
     * @param fieldSpec -
     *            the desired marc fields and subfields as given in the
     *            xxx_index.properties file
     * @param separator -
     *            the character to use between subfield values in the solr field
     *            contents
     * @return Set of values (as strings) for solr field
     */
    public static List<String> getAllSubfieldsAsList(final Record record, String fieldSpec, String separator)
    {
        List<String> result = new ArrayList<String>();
        getAllSubfieldsCollector(record, fieldSpec, separator, result);
        return result;
    }
    
    /**
     * For each occurrence of a marc field in the fieldSpec list, extract the
     * contents of all alphabetical subfields, concatenate them with a space
     * separator and add the string to the result set. Each instance of each
     * marc field will be put in a separate result.
     * 
     * @param record    the marc record
     * @param fieldSpec the marc fields (e.g. 600:655) in which we will grab the
     *                  alphabetic subfield contents for the result set. The field may
     *                  not be a control field (must be 010 or greater)
     * @return          a set of strings, where each string is the concatenated values of
     *                  all the alphabetic subfields.
     */
    public Set<String> getAllAlphaSubfields(final Record record, String fieldSpec) 
    {
        Set<String> resultSet = new LinkedHashSet<String>();

        String[] fldTags = fieldSpec.split(":");
        for (int i = 0; i < fldTags.length; i++)
        {
            String fldTag = fldTags[i];
            if (fldTag.length() < 3 || Integer.parseInt(fldTag) < 10)
            {
                System.err.println("Invalid marc field specified for getAllAlphaSubfields: " + fldTag);
                continue;
            }

            List<VariableField> varFlds = record.getVariableFields(fldTag);
            for (VariableField vf : varFlds)
            {

                StringBuffer buffer = new StringBuffer(500);

                DataField df = (DataField) vf;
                if (df != null)
                {
                    List<Subfield> subfields = df.getSubfields();
                    for (Subfield sf : subfields)
                    {
                        if (Character.isLetter(sf.getCode()))
                        {
                            if (buffer.length() > 0) {
                                buffer.append(" " + sf.getData().trim());
                            } else {
                                buffer.append(sf.getData().trim());
                            }
                        }
                    }
                }
                if (buffer.length() > 0)
                    resultSet.add(buffer.toString());
            }
        }

        return resultSet;
    }

    /**
     * For each occurrence of a marc field in the fieldSpec list, extract the
     * contents of all alphabetical subfields, concatenate them with a space
     * separator and add the string to the result set, handling multiple
     * occurrences as indicated
     * 
     * @param record - the marc record
     * @param fieldSpec -
     *            the marc fields (e.g. 600:655) in which we will grab the
     *            alphabetic subfield contents for the result set. The field may
     *            not be a control field (must be 010 or greater)
     * @param multOccurs -
     *            "first", "join" or "all" indicating how to handle multiple
     *            occurrences of field values
     * @return a set of strings, where each string is the concatenated values of
     *         all the alphabetic subfields.
     */
    public final Set<String> getAllAlphaSubfields(final Record record, String fieldSpec, String multOccurs) 
    {
        Set<String> result = getAllAlphaSubfields(record, fieldSpec);

        if (multOccurs.equals("first"))
        {
            Set<String> first = new HashSet<String>();
            for (String r : result)
            {
                first.add(r);
                return first;
            }
        }
        else if (multOccurs.equals("join"))
        {
            StringBuffer resultBuf = new StringBuffer();
            for (String r : result)
            {
                if (resultBuf.length() > 0)
                    resultBuf.append(' ');
                resultBuf.append(r);
            }
            Set<String> resultAsSet = new HashSet<String>();
            resultAsSet.add(resultBuf.toString());
            return resultAsSet;
        }
        // "all" is default

        return result;
    }

    /**
     * For each occurrence of a marc field in the fieldSpec list, extract the
     * contents of all subfields except the ones specified, concatenate the
     * subfield contents with a space separator and add the string to the result
     * set.
     * 
     * @param record -
     *            the marc record
     * @param fieldSpec -
     *            the marc fields (e.g. 600:655) in which we will grab the
     *            alphabetic subfield contents for the result set. The field may
     *            not be a control field (must be 010 or greater)
     * @return a set of strings, where each string is the concatenated values of
     *         all the alphabetic subfields.
     */
    public Set<String> getAllAlphaExcept(final Record record, String fieldSpec)
    {
        Set<String> resultSet = new LinkedHashSet<String>();
        String[] fldTags = fieldSpec.split(":");
        for (int i = 0; i < fldTags.length; i++)
        {
            String fldTag = fldTags[i].substring(0, 3);
            if (fldTag.length() < 3 || Integer.parseInt(fldTag) < 10)
            {
                System.err.println("Invalid marc field specified for getAllAlphaExcept: " + fldTag);
                continue;
            }

            String tabooSubfldTags = fldTags[i].substring(3);

            List<VariableField> varFlds = record.getVariableFields(fldTag);
            for (VariableField vf : varFlds)
            {

                StringBuffer buffer = new StringBuffer(500);
                DataField df = (DataField) vf;
                if (df != null)
                {

                    List<Subfield> subfields = df.getSubfields();

                    for (Subfield sf : subfields)
                    {
                        if (Character.isLetter(sf.getCode())
                                && tabooSubfldTags.indexOf(sf.getCode()) == -1)
                        {
                            if (buffer.length() > 0)
                                buffer.append(' ' + sf.getData().trim());
                            else
                                buffer.append(sf.getData().trim());
                        }
                    }
                    if (buffer.length() > 0)
                        resultSet.add(buffer.toString());
                }
            }
        }

        return resultSet;
    }

    /**
     * Given a fieldSpec, get any linked 880 fields and include the appropriate
     * subfields as a String value in the result set.
     * 
     * @param record
     *            marc record object
     * @param fieldSpec -
     *            the marc field(s)/subfield(s) for which 880s are sought.
     *            Separator of colon indicates a separate value, rather than
     *            concatenation. 008[5-7] denotes bytes 5-7 of the linked 008
     *            field (0 based counting) 100[a-cf-z] denotes the bracket
     *            pattern is a regular expression indicating which subfields to
     *            include from the linked 880. Note: if the characters in the
     *            brackets are digits, it will be interpreted as particular
     *            bytes, NOT a pattern 100abcd denotes subfields a, b, c, d are
     *            desired from the linked 880.
     * 
     * @return set of Strings containing the values of the designated 880
     *         field(s)/subfield(s)
     */
    public Set<String> getLinkedField(final Record record, String fieldSpec)
    {
        Set<String> set = getFieldList(record, "8806");

        if (set.isEmpty())
            return set;

        String[] tags = fieldSpec.split(":");
        Set<String> result = new LinkedHashSet<String>();
        for (int i = 0; i < tags.length; i++)
        {
            // Check to ensure tag length is at least 3 characters
            if (tags[i].length() < 3)
            {
                System.err.println("Invalid tag specified: " + tags[i]);
                continue;
            }

            // Get Field Tag
            String tag = tags[i].substring(0, 3);

            // Process Subfields
            String subfield = tags[i].substring(3);

            String separator = null;
            if (subfield.indexOf('\'') != -1)
            {
                separator = subfield.substring(subfield.indexOf('\'') + 1, subfield.length() - 1);
                subfield = subfield.substring(0, subfield.indexOf('\''));
            }

            result.addAll(getLinkedFieldValue(record, tag, subfield, separator));
        }
        return result;
    }

    
    /**
     * Given a tag for a field, and a list (or regex) of one or more subfields
     * get any linked 880 fields and include the appropriate subfields as a String value 
     * in the result set.
     * 
     * @param record - marc record object
     * @param tag -  the marc field for which 880s are sought.
     * @param subfield -
     *           The subfield(s) within the 880 linked field that should be returned
     *            [a-cf-z] denotes the bracket pattern is a regular expression indicating 
     *            which subfields to include from the linked 880. Note: if the characters 
     *            in the brackets are digits, it will be interpreted as particular
     *            bytes, NOT a pattern 100abcd denotes subfields a, b, c, d are
     *            desired from the linked 880.
     * @param separator - the separator string to insert between subfield items (if null, a " " will be used)
     * @param collector - object to accumulate the values of the designated 880 field(s)/subfield(s)
     */
    public static void getLinkedFieldValueCollector(final Record record, 
    												String tag, String subfield, String separator,
    												Collection<String> collector)
    {
        // assume brackets expression is a pattern such as [a-z]
        boolean havePattern = false;
        Pattern subfieldPattern = null;
        if (subfield.indexOf('[') != -1)
        {
            havePattern = true;
            subfieldPattern = Pattern.compile(subfield);
        }
        List<VariableField> fields = record.getVariableFields("880");
        for (VariableField vf : fields)
        {
            DataField dfield = (DataField) vf;
            Subfield link = dfield.getSubfield('6');
            if (link != null && link.getData().startsWith(tag))
            {
                List<Subfield> subList = dfield.getSubfields();
                StringBuffer buf = new StringBuffer("");
                for (Subfield subF : subList)
                {
                    boolean addIt = false;
                    if (havePattern)
                    {
                        Matcher matcher = subfieldPattern.matcher("" + subF.getCode());
                        // matcher needs a string, hence concat with empty
                        // string
                        if (matcher.matches())
                            addIt = true;
                    }
                    else
                    // a list a subfields
                    {
                        if (subfield.indexOf(subF.getCode()) != -1)
                            addIt = true;
                    }
                    if (addIt)
                    {
                        if (buf.length() > 0)
                            buf.append(separator != null ? separator : " ");
                        buf.append(subF.getData().trim());
                    }
                }
                if (buf.length() > 0) 
                    collector.add(Utils.cleanData(buf.toString()));
            }
        }
        return;
    }

    /**
     * Given a tag for a field, and a list (or regex) of one or more subfields
     * get any linked 880 fields and include the appropriate subfields as a String value 
     * in the result set.
     * 
     * @param record - marc record object
     * @param tag -  the marc field for which 880s are sought.
     * @param subfield -
     *           The subfield(s) within the 880 linked field that should be returned
     *            [a-cf-z] denotes the bracket pattern is a regular expression indicating 
     *            which subfields to include from the linked 880. Note: if the characters 
     *            in the brackets are digits, it will be interpreted as particular
     *            bytes, NOT a pattern 100abcd denotes subfields a, b, c, d are
     *            desired from the linked 880.
     * @param separator - the separator string to insert between subfield items (if null, a " " will be used)
     * 
     * @return set of Strings containing the values of the designated 880 field(s)/subfield(s)
     */
    public static Set<String> getLinkedFieldValue(final Record record, String tag, String subfield, String separator)
    {
        // assume brackets expression is a pattern such as [a-z]
        Set<String> result = new LinkedHashSet<String>();
        SolrIndexer.getLinkedFieldValueCollector(record, tag, subfield, separator, result);
		return result;
    }

    /**
     * Given a tag for a field, and a list (or regex) of one or more subfields
     * get any linked 880 fields and include the appropriate subfields as a String value 
     * in the result set.
     * 
     * @param record - marc record object
     * @param tag -  the marc field for which 880s are sought.
     * @param subfield -
     *           The subfield(s) within the 880 linked field that should be returned
     *            [a-cf-z] denotes the bracket pattern is a regular expression indicating 
     *            which subfields to include from the linked 880. Note: if the characters 
     *            in the brackets are digits, it will be interpreted as particular
     *            bytes, NOT a pattern 100abcd denotes subfields a, b, c, d are
     *            desired from the linked 880.
     * @param separator - the separator string to insert between subfield items (if null, a " " will be used)
     * 
     * @return list of Strings containing the values of the designated 880 field(s)/subfield(s)
     */
    public static List<String> getLinkedFieldValueAsList(final Record record, String tag, String subfield, String separator)
    {
        // assume brackets expression is a pattern such as [a-z]
    	List<String> result = new ArrayList<String>();
        SolrIndexer.getLinkedFieldValueCollector(record, tag, subfield, separator, result);
		return result;
    }
    
    /**
     * Given a fieldSpec, get the field(s)/subfield(s) values, PLUS any linked
     * 880 fields and return these values as a set.
     * @param record marc record object
     * @param fieldSpec - the marc field(s)/subfield(s) 
     * @return set of Strings containing the values of the indicated field(s)/
     *         subfields(s) plus linked 880 field(s)/subfield(s)
     */
    public Set<String> getLinkedFieldCombined(final Record record, String fieldSpec)
    {
        Set<String> result1 = getLinkedField(record, fieldSpec);
        Set<String> result2 = getFieldList(record, fieldSpec);

        if (result1 != null)
            result2.addAll(result1);
        return result2;
    }

    /**
     * return an int for the passed string
     * @param str
     * @param defValue - default value, if string doesn't parse into int
     */
    private int localParseInt(String str, int defValue)
    {
        int value = defValue;
        try
        {
            value = Integer.parseInt(str);
        }
        catch (NumberFormatException nfe)
        {
            // provided value is not valid numeric string
            // Ignoring it and moving happily on.
        }
        return (value);
    }
    
    /**
     * Loops through all datafields and creates a field for "all fields"
     * searching. Shameless stolen from Vufind Indexer Custom Code
     * 
     * @param record
     *            marc record object
     * @param lowerBoundStr -
     *            the "lowest" marc field to include (e.g. 100). defaults to 100
     *            if value passed doesn't parse as an integer
     * @param upperBoundStr -
     *            one more than the "highest" marc field to include (e.g. 900
     *            will include up to 899). Defaults to 900 if value passed
     *            doesn't parse as an integer
     * @return a Set of strings containing ALL subfields of ALL marc fields within the
     *         range indicated by the bound string arguments, with one string for each field encountered.
     */
    public Set<String> getAllSearchableFieldsAsSet(final Record record, String lowerBoundStr, String upperBoundStr)
    {
        Set<String> result = new LinkedHashSet<String>();
        int lowerBound = localParseInt(lowerBoundStr, 100);
        int upperBound = localParseInt(upperBoundStr, 900);

        List<DataField> fields = record.getDataFields();
        for (DataField field : fields)
        {
            // Get all fields starting with the 100 and ending with the 839
            // This will ignore any "code" fields and only use textual fields
            int tag = localParseInt(field.getTag(), -1);
            if ((tag >= lowerBound) && (tag < upperBound))
            {
                // Loop through subfields
                StringBuffer buffer = new StringBuffer("");
                List<Subfield> subfields = field.getSubfields();
                for (Subfield subfield : subfields)
                {
                    if (buffer.length() > 0)
                        buffer.append(" ");
                    buffer.append(subfield.getData());
                }
                result.add(buffer.toString());
            }
        }
        return result;
    }

    /**
     * Loops through all datafields and creates a field for "all fields"
     * searching. Shameless stolen from Vufind Indexer Custom Code
     * 
     * @param record
     *            marc record object
     * @param lowerBoundStr -
     *            the "lowest" marc field to include (e.g. 100). defaults to 100
     *            if value passed doesn't parse as an integer
     * @param upperBoundStr -
     *            one more than the "highest" marc field to include (e.g. 900
     *            will include up to 899). Defaults to 900 if value passed
     *            doesn't parse as an integer
     * @return a string containing ALL subfields of ALL marc fields within the
     *         range indicated by the bound string arguments.
     */
    public String getAllSearchableFields(final Record record, String lowerBoundStr, String upperBoundStr)
    {
        StringBuffer buffer = new StringBuffer("");
        int lowerBound = localParseInt(lowerBoundStr, 100);
        int upperBound = localParseInt(upperBoundStr, 900);

        List<DataField> fields = record.getDataFields();
        for (DataField field : fields)
        {
            // Get all fields starting with the 100 and ending with the 839
            // This will ignore any "code" fields and only use textual fields
            int tag = localParseInt(field.getTag(), -1);
            if ((tag >= lowerBound) && (tag < upperBound))
            {
                // Loop through subfields
                List<Subfield> subfields = field.getSubfields();
                for (Subfield subfield : subfields)
                {
                    if (buffer.length() > 0)
                        buffer.append(" ");
                    buffer.append(subfield.getData());
                }
            }
        }
        return buffer.toString();
    }

    /**
     * Custom routine to process a field specification very similar to the way 
     *  that specifying "first" works  ie. create a list of responses based on 
     *  the field spec, but only return one on them. (In this case, the longest of them)
     *  Additionally this function demonstrates how custom indexing functions can 
     *  encounter errors, and continue indexing, but record the error in the generated index map.
     *
     * @param record -  marc record object
     * @param fieldSpec - the marc field(s)/subfield(s) 
     * @param flagExtraEntries - boolean string - if "true" and more than one index entry
     *            would be returned, flag the condition as an error, and continue
     * @return a string containing longest entry matching the provided field spec.
     */
    public String getSingleIndexEntry(final Record record, String fieldSpec, String flagExtraEntries)
    {
        Set<String> set = getFieldList(record, fieldSpec);
        if (set.size() == 0)
        {
            return (null);
        }
        else if (set.size() == 1)
        {
            return (set.toArray(new String[0])[0]);
        }
        else
        {
            String longest = "";
            for (String item : set)
            {
                if (item.length() > longest.length())
                {
                    longest = item;
                }
            }
            if (flagExtraEntries.equalsIgnoreCase("true") && errors != null)
            {
                for (String item : set)
                {
                    if (!item.equals(longest))
                    {
                        errors.addError(record.getControlNumber(), fieldSpec.substring(0,3), fieldSpec.substring(3), 
                                        ErrorHandler.MINOR_ERROR, "Multiple fields found for Field that expects only one occurance");
                    }
                }
            }
            return (longest);
        }
    }

    /**
     * given a field spec that is assured to be for a single marc tag, return
     * the marc tag (the three character designation, such as 245, 650, etc.)
     * and the designated subfields string
     * 
     * @param singleFieldSpec -
     *            a field spec for a single tag only
     * @return a 2 element array of Strings, where the first element is the
     *         field tag and the second element is the list of subfields. Either
     *         element may be null: in the first element, null means the field
     *         tag was not 3 chars; in the second element, null means there were
     *         no subfields indicated.
     */
    private String[] parseSinglefieldSpec(String singleFieldSpec)
    {
        String[] result = new String[2];
        // Check to ensure tag length is at least 3 characters
        if (singleFieldSpec.length() < 3)
        {
            System.err.println("Invalid tag specified: " + singleFieldSpec);
            result[0] = null;
        }
        else
            result[0] = singleFieldSpec.substring(0, 3);

        // subfields
        if (singleFieldSpec.length() >= 3)
            result[1] = singleFieldSpec.substring(3);
        else
            result[1] = null;
        return result;
    }

    /**
     * treats indicator 2 as the number of non-filing indicators to exclude,
     * removes ascii punctuation
     * @param df          <code>DataField</code> with <code>ind2</code> containing # non-filing chars, or has value ' '
     * @param skipSubFldc <code>true</code> if subfield c contents should be skipped
     * @return            <code>StringBuffer</code> of the contents of the subfields - with a trailing
     *                    space
     */
    @SuppressWarnings("unchecked")
    protected StringBuffer getAlphaSubfldsAsSortStr(DataField df, boolean skipSubFldc)
    {
        StringBuffer result = new StringBuffer();
        int nonFilingInt = getInd2AsInt(df);
        boolean firstSubfld = true;

        List<Subfield> subList = df.getSubfields();
        for (Subfield sub : subList)
        {
            char subcode = sub.getCode();
            if (Character.isLetter(subcode) && (!skipSubFldc || subcode != 'c'))
            {
                String data = sub.getData();
                if (firstSubfld)
                {
                    if (nonFilingInt < data.length() - 1)
                        data = data.substring(nonFilingInt);
                    firstSubfld = false;
                }
                // eliminate ascii punctuation marks from sorting as well
                result.append(data.replaceAll("\\p{Punct}*", "").trim() + ' ');
            }
        }
        return result;
    }

    public List<VariableField> getFieldSetMatchingTagList(final Record record, String fieldspecs[])
    {
        String tags[] = new String[fieldspecs.length];
        for (int i = 0; i < fieldspecs.length; i++)
        {
            String tag = fieldspecs[i].substring(0, 3);
            if (tag == "LNK") tag = fieldspecs[i].substring(0, 6);
            tags[i] = tag;
        }
        List<VariableField> fieldSet = record.getVariableFields(tags);
        return(fieldSet);
    }
    
    public List<VariableField> getFieldSetMatchingTagList(final Record record, String fieldSpec)
    {
        String fieldspecs[] = fieldSpec.split(":");
        return(getFieldSetMatchingTagList(record, fieldspecs));
    }

    public String getDataFromVariableField(VariableField vf, String subfldTags, String separator, boolean cleanIt)
    {
        if (subfldTags.length() > 1 && !subfldTags.startsWith("["))
            subfldTags = '[' + subfldTags + ']';
        Pattern subfieldPattern = Pattern.compile(subfldTags.length() == 0 ? "." : subfldTags);
        DataField marcField = (DataField) vf;
        StringBuffer buffer = new StringBuffer("");
        List<Subfield> subfields = marcField.getSubfields();
        for (Subfield subfield : subfields)
        {
            Matcher matcher = subfieldPattern.matcher("" + subfield.getCode());
            if (matcher.matches())
            {
                if (buffer.length() > 0)
                    buffer.append(separator != null ? separator : " ");
                buffer.append(subfield.getData().trim());
            }
        }
        if (buffer.length() > 0)
            return(cleanIt ? Utils.cleanData(buffer.toString()) : buffer.toString());
        else
            return(null);
    }
/*   
 
   
  *     public Map<String, List<VariableField>> getFieldSetMatchingTagList(final Record record, String fieldspecs[])
    {
//        String tags[] = new String[fieldspecs.length];
        Map<String, List<VariableField>> result = new LinkedHashMap<String, List<VariableField>>();
        for (int i = 0; i < fieldspecs.length; i++)
        {
            String tag = fieldspecs[i].substring(0, 3);
            if (tag == "LNK") tag = fieldspecs[i].substring(0, 6);
            List<VariableField> fieldSet = record.getVariableFields(tag); // !!!
            result.put(fieldspecs[i], fieldSet);
        }
        return(result);
    }
    
    public Map<String, List<VariableField>> getFieldSetMatchingTagList(final Record record, String fieldSpec)
    {
        String fieldspecs[] = fieldSpec.split(":");
        return(getFieldSetMatchingTagList(record, fieldspecs));
    }
      
    List<VariableField> getFieldSetMatchingTagList(final Record record, String fieldSpec, String conditional)
    {
        String fieldspecs[] = fieldSpec.split(":");
        String tags[] = new String[fieldspecs.length];
        for (int i = 0; i < fieldspecs.length; i++)
        {
            String tag = fieldspecs[i].substring(0, 3);
            if (tag == "LNK") tag = fieldspecs[i].substring(0, 6);
            tags[i] = tag;
        }
        List<VariableField> fieldSet = record.getVariableFields(tags);
        if (conditional != null)
        {
            pruneFieldSet(fieldSet, conditional);
            return(fieldSet);
        }
        return(fieldSet);
    }
    
    private void pruneFieldSet(List<VariableField> fieldSet, String conditional)
    {
        for (VariableField vf : fieldSet)
        {
            if (testConditional(vf, conditional) == false)
            {
                fieldSet.remove(vf);
            }
        }
    }

    private boolean testConditional(VariableField vf, String conditional)
    {
        conditional = conditional.trim();
        if (conditional == null) return(true);
        String operand1 = getOperand(conditional);
        
        for (String clause : conditionalClauses)
        {
            if (testConditionalAnd(vf, clause))
                return(true);
        }
        return(false);
    }

    private String getOperand(String conditional)
    {
        if (conditional.charAt(0) == '(')
        {
            for (int paren = 1, offset = 1; paren == 1 && conditional.charAt(offset) == ')'; offset ++)
            {
                char curChar = conditional.charAt(offset);
                if (curChar == '(') paren++;
                if (curChar == ')') paren--;
            }
        }
        return
    }
    
    private boolean testConditionalAnd(VariableField vf, String conditional)
    {
        if (conditional == null) return(true);
        String conditionalClauses[] = conditional.split("&");
        for (String clause : conditionalClauses)
        {
            if (!testConditionalBase(vf, clause))
                return(false);
        }
        return(true);
    }

    private boolean testConditionalBase(VariableField vf, String clause)
    {
        // TODO Auto-generated method stub
        return false;
    }
*/
    /**
     * @param df
     *            a DataField
     * @return the integer (0-9, 0 if blank or other) in the 2nd indicator
     */
    protected int getInd2AsInt(DataField df)
    {
        char ind2char = df.getIndicator2();
        int result = 0;
        if (Character.isDigit(ind2char))
            result = Integer.valueOf(String.valueOf(ind2char));
        return result;
    }

    public ErrorHandler getErrorHandler()
    {
        return errors;
    }

}
