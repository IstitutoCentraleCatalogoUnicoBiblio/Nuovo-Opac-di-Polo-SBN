package org.solrmarc.tools;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Appender;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.Logger;
import org.marc4j.marc.*;
import org.solrmarc.marc.MarcImporter;

/**
 * General utility functions for solrmarc
 * 
 * @author Wayne Graham
 * @version $Id$
 */

public final class Utils {
    
    private final static Pattern FOUR_DIGIT_PATTERN_BRACES = Pattern.compile("\\[[12]\\d{3,3}\\]");
    private final static Pattern FOUR_DIGIT_PATTERN_ONE_BRACE = Pattern.compile("\\[[12]\\d{3,3}");
    private final static Pattern FOUR_DIGIT_PATTERN_STARTING_WITH_1_2 = Pattern.compile("(20|19|18|17|16|15)[0-9][0-9]");
    private final static Pattern FOUR_DIGIT_PATTERN_OTHER_1 = Pattern.compile("l\\d{3,3}");
    private final static Pattern FOUR_DIGIT_PATTERN_OTHER_2 = Pattern.compile("\\[19\\]\\d{2,2}");
    private final static Pattern FOUR_DIGIT_PATTERN_OTHER_3 = Pattern.compile("(20|19|18|17|16|15)[0-9][-?0-9]");
    private final static Pattern FOUR_DIGIT_PATTERN_OTHER_4 = Pattern.compile("i.e. (20|19|18|17|16|15)[0-9][0-9]");
    private final static Pattern BC_DATE_PATTERN = Pattern.compile("[0-9]+ [Bb][.]?[Cc][.]?");
    private final static Pattern FOUR_DIGIT_PATTERN = Pattern.compile("\\d{4,4}");
    private static Matcher matcher;
    private static Matcher matcher_braces;
    private static Matcher matcher_one_brace;
    private static Matcher matcher_start_with_1_2;
    private static Matcher matcher_l_plus_three_digits;
    private static Matcher matcher_bracket_19_plus_two_digits;
    private static Matcher matcher_ie_date;
    private static Matcher matcher_bc_date;
    private static Matcher matcher_three_digits_plus_unk;
    private final static DecimalFormat timeFormat = new DecimalFormat("00.00");
    protected static Logger logger = Logger.getLogger(Utils.class.getName());    
    /**
     * Default Constructor
     * It's private, so it can't be instantiated by other objects
     *
     */    
    private Utils(){ }
    
    /**
     * Check first for a particular property in the System Properties, so that the -Dprop="value" command line arg 
     * mechanism can be used to override values defined in the passed in property file.  This is especially useful
     * for defining the marc.source property to define which file to operate on, in a shell script loop.
     * @param props    property set in which to look.
     * @param propname name of the property to lookup.
     * @return         value stored for that property (or null if it doesn't exist) 
     */
    public static String getProperty(Properties props, String propname)
    {
        return getProperty(props, propname, null);
    }
    
    /**
     * Check first for a particular property in the System Properties, so that the -Dprop="value" command line arg 
     * mechanism can be used to override values defined in the passed in property file.  This is especially useful
     * for defining the marc.source property to define which file to operate on, in a shell script loop.
     * @param props    property set in which to look.
     * @param propname name of the property to lookup.
     * @param defVal   the default value to use if property is not defined
     * @return         value stored for that property (or the  if it doesn't exist) 
     */
    public static String getProperty(Properties props, String propname, String defVal)
    {
        String prop;
        if ((prop = System.getProperty(propname)) != null)
        {
            return(prop);
        }
        if (props != null && (prop = props.getProperty(propname)) != null)
        {
            return(prop);
        }
        return defVal;
    }
    
    /**
     * load a properties file into a Properties object
     * @param propertyPaths the directories to search for the properties file
     * @param propertyFileName name of the sought properties file
     * @return Properties object 
     */
    public static Properties loadProperties(String propertyPaths[], String propertyFileName)
    {
        return(loadProperties(propertyPaths, propertyFileName, false, null));
    }
    /**
     * load a properties file into a Properties object
     * @param propertyPaths the directories to search for the properties file
     * @param propertyFileName name of the sought properties file
     * @return Properties object 
     */
    public static Properties loadProperties(String propertyPaths[], String propertyFileName, boolean showName)
    {
        return(loadProperties(propertyPaths, propertyFileName, showName, null));
    }
    /**
     * load a properties file into a Properties object
     * @param fullFilenameURLStr String representation of url to properties file whether it is in a local file or a resource
     * @return Properties object 
     */
    public static Properties loadProperties(String fullFilenameURLStr)
    {
        InputStream in = getPropertyFileInputStream(fullFilenameURLStr); 
        String errmsg = "Fatal error: Unable to find specified properties file: " + fullFilenameURLStr;

        // load the properties
        Properties props = new Properties();
        try
        {
            if (fullFilenameURLStr.endsWith(".xml") || fullFilenameURLStr.endsWith(".XML"))
            {
                props.loadFromXML(in);
            }
            else
            {
                props.load(in);
            }
            in.close();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(errmsg);
        }
        return props;
    }

    /**
     * load a properties file into a Properties object
     * @param propertyPaths the directories to search for the properties file
     * @param propertyFileName name of the sought properties file
     * @param showName whether the name of the file/resource being read should be shown.
     * @return Properties object 
     */
    public static Properties loadProperties(String propertyPaths[], String propertyFileName, boolean showName, String filenameProperty)
    {
        String inputStreamSource[] = new String[]{null};
        InputStream in = getPropertyFileInputStream(propertyPaths, propertyFileName, showName, inputStreamSource);
        String errmsg = "Fatal error: Unable to find specified properties file: " + propertyFileName;
        
        // load the properties
        Properties props = new Properties();
        try
        {
            if (propertyFileName.endsWith(".xml") || propertyFileName.endsWith(".XML"))
            {
                props.loadFromXML(in);
            }
            else
            {
                props.load(in);
            }
            in.close();
            if (filenameProperty != null && inputStreamSource[0] != null)
            {
                File tmpFile = new File(inputStreamSource[0]);
                
                props.setProperty(filenameProperty, tmpFile.getParent());
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(errmsg);
        }
        return props;
    }

    public static InputStream getPropertyFileInputStream(String[] propertyPaths, String propertyFileName) 
    {
        return(getPropertyFileInputStream(propertyPaths, propertyFileName, false));
    }
    
    public static InputStream getPropertyFileInputStream(String[] propertyPaths, String propertyFileName, boolean showName) 
    {
        return(getPropertyFileInputStream(propertyPaths, propertyFileName, false, null));
    }
    
    public static InputStream getPropertyFileInputStream(String propertyFileURLStr) 
    {
        InputStream in = null;
        String errmsg = "Fatal error: Unable to open specified properties file: " + propertyFileURLStr;
        try
        {
            URL url = new URL(propertyFileURLStr);
            in = url.openStream();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(errmsg);
        }

        return(in);
    }
    
    public static InputStream getPropertyFileInputStream(String[] propertyPaths, String propertyFileName, boolean showName, String inputSource[]) 
    {
        InputStream in = null;
        String fullPropertyFileURLStr = getPropertyFileAbsoluteURL(propertyPaths, propertyFileName, showName, inputSource);
        return(getPropertyFileInputStream(fullPropertyFileURLStr));
    }
    
//        String verboseStr = System.getProperty("marc.test.verbose");
//        boolean verbose = (verboseStr != null && verboseStr.equalsIgnoreCase("true"));
//        String lookedIn = "";
//        if (propertyPaths != null)
//        {
//            File propertyFile = new File(propertyFileName);
//            int pathCnt = 0;
//            do 
//            {
//                if (propertyFile.exists() && propertyFile.isFile() && propertyFile.canRead())
//                {
//                    try
//                    {
//                        in = new FileInputStream(propertyFile);
//                        if (inputSource != null && inputSource.length >= 1)
//                        {
//                            inputSource[0] = propertyFile.getAbsolutePath();
//                        }
//                        if (showName)
//                            logger.info("Opening file: "+ propertyFile.getAbsolutePath());
//                        else
//                            logger.debug("Opening file: "+ propertyFile.getAbsolutePath());
//                    }
//                    catch (FileNotFoundException e)
//                    {
//                        // simply eat this exception since we should only try to open the file if we previously
//                        // determined that the file exists and is readable. 
//                    }
//                    break;   // we found it!
//                }
//                if (verbose)  lookedIn = lookedIn + propertyFile.getAbsolutePath() + "\n";
//                if (propertyPaths != null && pathCnt < propertyPaths.length)
//                {
//                    propertyFile = new File(propertyPaths[pathCnt], propertyFileName);
//                }
//                pathCnt++;
//            } while (propertyPaths != null && pathCnt <= propertyPaths.length);
//        }
//        // if we didn't find it as a file, look for it as a URL
//        String errmsg = "Fatal error: Unable to find specified properties file: " + propertyFileName;
//        if (verbose) errmsg = errmsg + "\n Looked in: "+ lookedIn;
//        if (in == null)
//        {
//            Utils utilObj = new Utils();
//            URL url = utilObj.getClass().getClassLoader().getResource(propertyFileName);
//            if (url == null)  
//                url = utilObj.getClass().getResource("/" + propertyFileName);
//            if (url == null)
//            {
//                logger.error(errmsg);
//                throw new IllegalArgumentException(errmsg);
//            }
//            if (showName)
//                logger.info("Opening resource via URL: "+ url.toString());
//            else
//                logger.debug("Opening resource via URL: "+ url.toString());
//
///*
//            if (url == null) 
//                url = utilObj.getClass().getClassLoader().getResource(propertyPath + "/" + propertyFileName);
//            if (url == null) 
//                url = utilObj.getClass().getResource("/" + propertyPath + "/" + propertyFileName);
//*/
//            if (url != null)
//            {
//                try
//                {
//                    in = url.openStream();
//                }
//                catch (IOException e)
//                {
//                    throw new IllegalArgumentException(errmsg);
//                }
//            }
//        }
//        return(in);
//    }
    
    public static String getPropertyFileAbsoluteURL(String[] propertyPaths, String propertyFileName, boolean showName, String inputSource[]) 
    {
        InputStream in = null;
        // look for properties file in paths
        String verboseStr = System.getProperty("marc.test.verbose");
        boolean verbose = (verboseStr != null && verboseStr.equalsIgnoreCase("true"));
        if (verbose)  showName = true;
        String lookedIn = "";
        String fullPathName = null;
        if (propertyPaths != null)
        {
            File propertyFile = new File(propertyFileName);
            int pathCnt = 0;
            do 
            {
                if (propertyFile.exists() && propertyFile.isFile() && propertyFile.canRead())
                {
                    try
                    {
                        fullPathName = propertyFile.toURI().toURL().toExternalForm();
                        if (inputSource != null && inputSource.length >= 1)
                        {
                            inputSource[0] = propertyFile.getAbsolutePath();
                        }
                    }
                    catch (MalformedURLException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (showName)
                        logger.info("Opening file: "+ propertyFile.getAbsolutePath());
                    else
                        logger.debug("Opening file: "+ propertyFile.getAbsolutePath());
                    break;   // we found it!
                }
                if (verbose)  lookedIn = lookedIn + propertyFile.getAbsolutePath() + "\n";
                if (propertyPaths != null && pathCnt < propertyPaths.length)
                {
                    propertyFile = new File(propertyPaths[pathCnt], propertyFileName);
                }
                pathCnt++;
            } while (propertyPaths != null && pathCnt <= propertyPaths.length);
        }
        // if we didn't find it as a file, look for it as a URL
        String errmsg = "Fatal error: Unable to find specified properties file: " + propertyFileName;
        if (verbose) errmsg = errmsg + "\n Looked in: "+ lookedIn;
        if (fullPathName == null)
        {
            Utils utilObj = new Utils();
            URL url = utilObj.getClass().getClassLoader().getResource(propertyFileName);
            if (url == null)  
                url = utilObj.getClass().getResource("/" + propertyFileName);
            if (url == null)
            {
                logger.error(errmsg);
                throw new IllegalArgumentException(errmsg);
            }
            if (showName)
                logger.info("Opening resource via URL: "+ url.toString());
            else
                logger.debug("Opening resource via URL: "+ url.toString());

/*
            if (url == null) 
                url = utilObj.getClass().getClassLoader().getResource(propertyPath + "/" + propertyFileName);
            if (url == null) 
                url = utilObj.getClass().getResource("/" + propertyPath + "/" + propertyFileName);
*/
            fullPathName = url.toExternalForm();
        }
        return(fullPathName);
    }
    
    
    /**
     * Takes an InputStream, reads the entire contents into a String
     * @param stream - the stream to read in.
     * @return String containing entire contents of stream.
     */
    public static String readStreamIntoString(InputStream stream) throws IOException 
    {
        Reader in = new BufferedReader(new InputStreamReader(stream));

        StringBuilder sb = new StringBuilder();
        char[] chars = new char[4096];
        int length;
        
        while ((length = in.read(chars)) > 0) {
          sb.append(chars, 0, length);
        }

        return sb.toString();
     }
    
    
    /**
     * Cleans non-digits from a String
     * @param date String to parse
     * @return Numeric part of date String (or null)
     */
    public static String cleanDate(final String date)
    {
        matcher_braces = FOUR_DIGIT_PATTERN_BRACES.matcher(date);
        matcher_one_brace = FOUR_DIGIT_PATTERN_ONE_BRACE.matcher(date);
        matcher_start_with_1_2 = FOUR_DIGIT_PATTERN_STARTING_WITH_1_2.matcher(date);
        matcher_l_plus_three_digits = FOUR_DIGIT_PATTERN_OTHER_1.matcher(date);
        matcher_bracket_19_plus_two_digits = FOUR_DIGIT_PATTERN_OTHER_2.matcher(date);
        matcher_three_digits_plus_unk = FOUR_DIGIT_PATTERN_OTHER_3.matcher(date);
        matcher_ie_date = FOUR_DIGIT_PATTERN_OTHER_4.matcher(date);
        matcher = FOUR_DIGIT_PATTERN.matcher(date);
        matcher_bc_date = BC_DATE_PATTERN.matcher(date);
        
        String cleanDate = null; // raises DD-anomaly
        
        if(matcher_braces.find())
        {   
            cleanDate = matcher_braces.group();
            cleanDate = Utils.removeOuterBrackets(cleanDate);
            if (matcher.find())
            {
                String tmp = matcher.group();
                if (!tmp.equals(cleanDate))
                {
                    tmp = "" + tmp;
                }
            }
        } 
        else if (matcher_ie_date.find())
        {
            cleanDate = matcher_ie_date.group().replaceAll("i.e. ", "");
        }
        else if(matcher_one_brace.find())
        {   
            cleanDate = matcher_one_brace.group();
            cleanDate = Utils.removeOuterBrackets(cleanDate);
            if (matcher.find())
            {
                String tmp = matcher.group();
                if (!tmp.equals(cleanDate))
                {
                    tmp = "" + tmp;
                }
            }
        }
        else if(matcher_bc_date.find())
        {   
            cleanDate = null;
        } 
        else if(matcher_start_with_1_2.find())
        {   
            cleanDate = matcher_start_with_1_2.group();
        } 
        else if(matcher_l_plus_three_digits.find())
        {   
            cleanDate = matcher_l_plus_three_digits.group().replaceAll("l", "1");
        } 
        else if(matcher_bracket_19_plus_two_digits.find())
        {   
            cleanDate = matcher_bracket_19_plus_two_digits.group().replaceAll("\\[", "").replaceAll("\\]", "");
        } 
        else if(matcher_three_digits_plus_unk.find())
        {   
            cleanDate = matcher_three_digits_plus_unk.group().replaceAll("[-?]", "0");
        } 
        if (cleanDate != null)
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String thisYear = dateFormat.format(calendar.getTime());
            try {
                if (Integer.parseInt(cleanDate) > Integer.parseInt(thisYear) + 1) 
                    cleanDate = null;
            }
            catch (NumberFormatException nfe)
            {
                cleanDate = null;
            }
        }
        if (cleanDate != null)
        {
            logger.debug("Date : "+ date + " mapped to : "+ cleanDate);            
        }
        else
        {
            logger.debug("No Date match: "+ date);
        }
        return cleanDate;
    }
    
    /**
     * Removes trailing characters (space, comma, slash, semicolon, colon),
     *  trailing period if it is preceded by at least three letters, 
     *  and single square bracket characters if they are the start and/or end
     *  chars of the cleaned string
     *
     * @param origStr String to clean
     * @return cleaned string
     */
    public static String cleanData(String origStr)
    {
        String currResult = origStr; 
        String prevResult;
        do {
            prevResult = currResult;
            currResult = currResult.trim();
    
            currResult = currResult.replaceAll(" *([,/;:])$", "");

            // trailing period removed in certain circumstances
            if (currResult.endsWith("."))
            {
                if (currResult.matches(".*[JS]r\\.$"))
                {
                    // dont strip period off of Jr. or Sr.
                }
                else if (currResult.matches(".*\\w\\w\\.$"))
                {
                    currResult = currResult.substring(0, currResult.length()-1);
                }
                else if (currResult.matches(".*\\p{L}\\p{L}\\.$"))
                {
                    currResult = currResult.substring(0, currResult.length()-1);
                }
                else if (currResult.matches(".*\\w\\p{InCombiningDiacriticalMarks}?\\w\\p{InCombiningDiacriticalMarks}?\\.$"))
                {
                    currResult = currResult.substring(0, currResult.length()-1);
                }
                else if (currResult.matches(".*\\p{Punct}\\.$"))
                {
                    currResult = currResult.substring(0, currResult.length()-1);
                }
            }

            currResult = removeOuterBrackets(currResult);

            if (currResult.length() == 0)
                return currResult;

        } while (! currResult.equals(prevResult));
        
//        if (!currResult.equals(origStr))  
//            System.out.println(origStr + " -> "+ currResult); 

        return currResult;       
    }

    /**
     * Call cleanData on an entire set of Strings has a side effect
     * of deleting entries that are identical when they are cleaned.
     * @param values - the set to clean
     * @return Set<String> - the "same" set with all of its entries cleaned.
     */
    private static Set<String> cleanData(Set<String> values)
    {
        Set<String> result = new LinkedHashSet<String>();
        for (String entry : values)
        {
            String cleaned = cleanData(entry);
            result.add(cleaned);
        }
        return(result);
    }


    /**
     * Repeatedly removes trailing characters indicated in regular expression, 
     * PLUS trailing period if it is preceded by its regular expression 
     *
     * @param origStr String to clean
     * @param trailingCharsRegEx a regular expression of trailing chars to be
     *   removed (see java Pattern class).  Note that the regular expression
     *   should NOT have '$' at the end.
     *   (e.g. " *[,/;:]" replaces any commas, slashes, semicolons or colons
     *     at the end of the string, and these chars may optionally be preceded
     *     by a space)
     * @param charsB4periodRegEx a regular expression that must immediately 
     *  precede a trailing period IN ORDER FOR THE PERIOD TO BE REMOVED. 
     *  Note that the regular expression will NOT have the period or '$' at 
     *  the end. 
     *   (e.g. "[a-zA-Z]{3,}" means at least three letters must immediately 
     *   precede the period for it to be removed.)
     * @return cleaned string
     */
    public static String removeAllTrailingCharAndPeriod(String origStr, String trailingCharsRegEx, String charsB4periodRegEx)
    {
        if (origStr == null)
            return null;

        String currResult = origStr; 
        String prevResult;
        do {
            prevResult = currResult;
            currResult = removeTrailingCharAndPeriod(currResult.trim(), trailingCharsRegEx, charsB4periodRegEx);

            if (currResult.length() == 0)
                return currResult;

        } while (! currResult.equals(prevResult));

        return currResult;       
    }
    
    /**
     * Removes trailing characters indicated in regular expression, PLUS
     *  trailing period if it is preceded by its regular expression.
     *
     * @param origStr String to clean
     * @param trailingCharsRegEx a regular expression of trailing chars to be
     *   removed (see java Pattern class).  Note that the regular expression
     *   should NOT have '$' at the end.
     *   (e.g. " *[,/;:]" replaces any commas, slashes, semicolons or colons
     *     at the end of the string, and these chars may optionally be preceded
     *     by a space)
     * @param charsB4periodRegEx a regular expression that must immediately 
     *  precede a trailing period IN ORDER FOR THE PERIOD TO BE REMOVED. 
     *  Note that the regular expression will NOT have the period or '$' at 
     *  the end. 
     *   (e.g. "[a-zA-Z]{3,}" means at least three letters must immediately 
     *   precede the period for it to be removed.)
     * @return cleaned string
     */
    public static String removeTrailingCharAndPeriod(String origStr, String trailingCharsRegEx, String charsB4periodRegEx)
    {
        if (origStr == null)
            return null;

        String result = removeTrailingChar(origStr, trailingCharsRegEx);

        result = removeTrailingPeriod(result, charsB4periodRegEx);
            
        return result ;       
    }
    
    /**
     * Remove the characters per the regular expression if they are at the end
     *  of the string.
     * @param origStr string to be cleaned
     * @param charsToReplaceRegEx - a regular expression of the trailing string/chars
     *   to be removed e.g. " *([,/;:])" meaning last character is a comma, 
     *   slash, semicolon, colon, possibly preceded by one or more spaces.
     * @see Pattern class in java api
     * @return the string with the specified trailing characters removed
     */
    public static String removeTrailingChar(String origStr, String charsToReplaceRegEx) 
    {
        if (origStr == null)
            return origStr;
           // get rid of reg ex specified chars at the end of the string
        return origStr.trim().replaceAll(charsToReplaceRegEx + "$", "");
    }
    
    /**
     * If there is a period at the end of the string, remove the period if it is
     *  immediately preceded by the regular expression
     * @param origStr the string to be cleaned
     * @param precedingCharsRegEx a regular expression that must immediately 
     *  precede a trailing period IN ORDER FOR THE PERIOD TO BE REMOVED. 
     *  Note that the regular expression will NOT have the period or '$' at 
     *  the end. 
     *   (e.g. "[a-zA-Z]{3,}" means at least three letters must immediately 
     *   precede the period for it to be removed.)
     * @return the string without a trailing period iff the regular expression
     *   param was found immediately before the trailing period
     */
    public static String removeTrailingPeriod(String origStr, String precedingCharsRegEx) 
    {
        if (origStr == null)
            return origStr;
        String result = origStr.trim();
        if (result.endsWith(".") && result.matches(".*" + precedingCharsRegEx + "\\.$"))
            result = result.substring(0, result.length() - 1).trim();

           return result;
     }

    
    /**
     * Remove single square bracket characters if they are the start and/or end
     *  chars (matched or unmatched) and are the only square bracket chars in
     *  the string.
     */
    public static String removeOuterBrackets(String origStr) 
    {
        if (origStr == null || origStr.length() == 0)
            return origStr;

        String result = origStr.trim();
                
        if (result.length() > 0)
        {
            boolean openBracketFirst = result.charAt(0) == '[';
            boolean closeBracketLast = result.endsWith("]");
            if (openBracketFirst && closeBracketLast && 
                    result.indexOf('[', 1) == -1 && 
                    result.lastIndexOf(']', result.length()-2) == -1)
                // only square brackets are at beginning and end
                result = result.substring(1, result.length()-1);
            else if (openBracketFirst && result.indexOf(']') == -1)
                // starts with '[' but no ']'; remove open bracket
                result = result.substring(1);                
            else if (closeBracketLast && result.indexOf('[') == -1)
                // ends with ']' but no '['; remove close bracket
                result = result.substring(0, result.length()-1);                
        }

        return result.trim();
    }

    
    /**
     * Calculate time from milliseconds
     * @param totalTime Time in milliseconds
     * @return Time in the format mm:ss.ss
     */
    public static String calcTime(final long totalTime)
    {
        return totalTime / 60000 + ":" + timeFormat.format((totalTime % 60000 ) / 1000);
    }
    
    /**
     * Test if a String has a numeric equivalent
     * @param number String representation of a number
     * @return True if String is a number; False if it is not
     */
    public static boolean isNumber(final String number)
    {
        boolean isNumber; // fix for dd-anomaly
        
        try {
            Integer.parseInt(number);
            isNumber = true;
        }
        catch(NumberFormatException nfe)
        {
            // eat the exception
            isNumber = false;
        }
        
        return isNumber;        
    }

    /**
     * Remap a field value.  If the field value is not present in the map, then:
     *   if "displayRawIfMissing" is a key in the map, then the raw field value
     *   is used.
     *   if "displayRawIfMissing" is not a key in the map, and the allowDefault
     *   param is set to true, then if the map contains "__DEFAULT" as a key, 
     *   the value of "__DEFAULT" in the map is used;  if allowDefault is true
     *   and there is neither "displayRawIfMissing" nor "__DEFAULT", as a key
     *   in the map, then if the map contains an empty key, the map value of the
     *   empty key is used.
     *     NOTE:  If the spec for a field is supposed to contain all matching 
     *      values, then the default lookup needs to be done here.  If the spec
     *      for a field is only supposed to return the first matching mappable 
     *      value, then the default mapping should be done in the calling method 
     * @param fieldVal - the raw value to be mapped
     * @param map - the map to be used
     * @param allowDefault - if "displayRawIfMissing" is not a key in the map, 
     *   and this is to true, then if the map contains "__DEFAULT" as a key, 
     *   the value of "__DEFAULT" in the map is used.  
     * @return the new value, as determined by the mapping.
     */
    public static String remap(String fieldVal, Map<String, String> map, boolean allowDefault)
    {
        String result = null;
        
        if (map.keySet().contains("pattern_0"))
        {
            for (int i = 0; i < map.keySet().size(); i++)
            {
                String patternStr = map.get("pattern_"+i);
                String parts[] = patternStr.split("=>");
                if (containsMatch(fieldVal, parts[0]))
                {
                    String newVal = parts[1];
                    if (parts[1].contains("$"))
                    {
                        newVal = fieldVal.replaceAll(parts[0], parts[1]);
                        fieldVal = newVal;
                    }
                    result = newVal;                    
                }
            }
        }
        if (map.containsKey(fieldVal))
        {
            result = map.get(fieldVal);
        }
        else if (map.containsKey("displayRawIfMissing")) 
        {
            result = fieldVal;
        }
        else if (allowDefault && map.containsKey("__DEFAULT"))
        {
            result = map.get("__DEFAULT");
        }                      
        else if (allowDefault && map.containsKey(""))
        {
            result = map.get("");
        }                      
        if (result == null || result.length() == 0) return null;
        return result;
    }

    /**
     * Remap a set of field values.  If a field value is not present in the map, 
     * then:
     *   if "displayRawIfMissing" is a key in the map, then the raw field value
     *   is used.
     *   if "displayRawIfMissing" is not a key in the map, and the allowDefault
     *   param is set to true, then if the map contains "__DEFAULT" as a key, 
     *   the value of "__DEFAULT" in the map is used;  if allowDefault is true
     *   and there is neither "displayRawIfMissing" nor "__DEFAULT", as a key
     *   in the map, then if the map contains an empty key, the map value of the
     *   empty key is used.
     *     NOTE:  If the spec for a field is supposed to contain all matching 
     *      values, then the default lookup needs to be done here.  If the spec
     *      for a field is only supposed to return the first matching mappable 
     *      value, then the default mapping should be done in the calling method 
     * @param set          the raw values to be mapped
     * @param map          the map to be used
     * @param allowDefault if "displayRawIfMissing" is not a key in the map, 
     *                     and this is to true, then if the map contains "__DEFAULT" as a key, 
     *                     the value of "__DEFAULT" in the map is used.  
     * @return the new value, as determined by the mapping.
     */
    public static Set<String> remap(Set<String> set, Map<String, String> map, boolean allowDefault)
    {
        if (map == null)  return(set);
        Iterator<String> iter = set.iterator();
        Set<String> result = new LinkedHashSet<String>();
        
        while(iter.hasNext())
        {
            String val = iter.next();
            if (map.keySet().contains("pattern_0"))
            {
                String tmpResult = null;
                for (int i = 0; i < map.keySet().size(); i++)
                {
                    String patternStr = map.get("pattern_"+i);
                    String parts[] = patternStr.split("=>");
                    if (containsMatch(val, parts[0]))
                    {
                        String newVal = parts[1];
                        if (parts[1].contains("$"))
                        {
                            newVal = val.replaceAll(parts[0], parts[1]);
                            val = newVal;
                        }
                        else
                        {
                            result.add(newVal); 
                        }
                        tmpResult = newVal;
                    }
                }
                if (tmpResult != null) result.add(tmpResult); 
            }
            else
            {            
                String mappedVal = remap(val, map, allowDefault);
                if (mappedVal != null)
                {
                    if (mappedVal.contains("|"))
                    {
                        String vals[] = mappedVal.split("[|]");
                        for (String oneVal : vals)
                        {
                            result.add(oneVal);
                        }
                    }
                    else
                        result.add(mappedVal);
                }
            }
        }
        if (result.isEmpty() && allowDefault && map.containsKey("__DEFAULT"))
        {
            result.add(map.get("__DEFAULT"));
        }                      
        return result;
    }

    public static boolean containsMatch(String val, String pattern)
    {
        String rep = val.replaceFirst(pattern, "###match###");
        
        if (!rep.equals(val)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Test if a set contains a specified pattern
     * @param set Set of marc fields to test
     * @param pattern Regex String pattern to match
     * @return If the set contains the pattern, return true, else false
     */
    public static boolean setItemContains(Set<String> set, String pattern)
    {
        if (set.isEmpty()) {
            return(false);
        }
        
        Iterator<String> iter = set.iterator();        
       
        while (iter.hasNext())
        {
            String value = (String)iter.next();
            
            if (containsMatch(value, pattern)) {
                return true;
            }
                
        }
        return false;
    }  

    /**
     * Join two fields together with seperator
     * @param set Set of marc fields to join
     * @param separator Separation character to put between 
     * @return Joined fields
     */
    public static String join(Set<String> set, String separator)
    {
        Iterator<String> iter = set.iterator();
        //String result = "";
        StringBuffer result = new StringBuffer("");
       
        while(iter.hasNext())
        {
            //result += iter.next();
            result.append(iter.next());
            if (iter.hasNext())  {
                //result += separator;
                result.append(separator);
            }
        }
        
        return result.toString();
    }

    public static Set<String> trimNearDuplicates(Set<String> locations)
    {
        locations = cleanData(locations);
        if (locations.size() <= 1) return(locations);
        Object locArr[] = locations.toArray();
        int size = locArr.length;
        for (int i = 0; i < size; i++)
        {
            boolean copyStrI = true;
            for (int j = 0; j < size; j++)
            {
                if (i == j) continue;
                if (locArr[j].toString().contains(locArr[i].toString())) { copyStrI = false; break; }                       
            }
            if (copyStrI == false) locations.remove(locArr[i]);   
        }
        return locations;
    }

    
    /**
     * returns <code>true</code> if the 3 letter language code is for a right to left 
     * language (one written in arabic or hebrew characters)
     * @param  langcode
     * @return <code>true</code> if code is for a right-to-left language
     */
    public final static boolean isRightToLeftLanguage(String langcode) 
    {
        if (
               // arabic characters
               langcode.equals("ara") || langcode.equals("per") || langcode.equals("urd")        
            || 
            // hebrew characters
            langcode.equals("heb") || langcode.equals("yid") || langcode.equals("lad")
            || langcode.equals("jpr") || langcode.equals("jrb") 
            )
            return true;
        else
            return false;
    }
    

    /**
     * return the index within this string of the first occurrence of an open
     *  parenthesis that isn't escaped with a backslash.
     * @param str
     * @return if an unescaped open parenthesis occurs within this object, 
     * return the index of the first open paren; -1 if no unescaped open paren.
     */
    public final static int getIxUnescapedOpenParen(String str) 
    {
        if (str.startsWith("("))
            return 0;
        Pattern p = Pattern.compile(".*[^\\\\](\\().*");
        Matcher m = p.matcher(str);
        if (m.matches())
            return m.start(1); 
        else
            return -1;
    }

    
    /**
     * return the index within this string of the first occurrence of a comma
     *  that isn't escaped with a backslash.
     * @param str
     * @return if an unescaped comma occurs within this object, the index of the
     *  first comma; -1 if no unescaped comma.
     */
    public final static int getIxUnescapedComma(String str) 
    {
        if (str.startsWith(","))
            return 0;
        Pattern p = Pattern.compile(".*[^\\\\](,).*");
        Matcher m = p.matcher(str);
        if (m.matches())
            return m.start(1); 
        else
            return -1;
    }
    
    /**
     * Look for Strings in the set, that start with the given prefix.  If found,
     *  remove the prefix, trim the result and add it to the returned set of 
     *  Strings to be returned.
     * @param valueSet
     * @param prefix
     * @return set members that had the prefix, but now prefix is removed and
     *   remaining value is trimmed.
     */
    public final static Set<String> getPrefixedVals(Set<String> valueSet, String prefix) {
        Set<String> resultSet = new LinkedHashSet<String>();
        if (!valueSet.isEmpty()) {
            Iterator<String> iter = valueSet.iterator();
            while (iter.hasNext()) {
                String s = removePrefix((String) iter.next(), prefix);
                if (s != null) {
                    String value = s.trim();
                    if (value != null && value.length() != 0)
                        resultSet.add(value);
                }
            }
        }
        return resultSet;
    }
    
    /**
     * remove prefix from the beginning of the value string.
     */
    public final static String removePrefix(String value, String prefix) {
        if (value.startsWith(prefix)) {
            value = value.substring(prefix.length());
            if (value != null && value.length() != 0)
                return value;
        }
        return null;
    }
    
    /**
     * returns the valid ISBN(s) from the set of candidate Strings
     * @return Set of strings containing valid ISBN numbers
     */
    public static Set<String> returnValidISBNs(Set<String> candidates)
    {
        // NOTE 1: last digit of ISBN is a check digit and may be "X" (0,1,2,3,4,5,6.7.8.9.X)
        // NOTE 2: ISBN can be 10 or 13 digits (and may end with X).
        // NOTE 3: 13 digit ISBN must start with 978 or 979.
        // NOTE 4: there may be text after the ISBN, which should be removed 
        
        Set<String> isbnSet = new LinkedHashSet<String>();

        Pattern p10 = Pattern.compile("^\\d{9}[\\dX].*");
        Pattern p13 = Pattern.compile("^(978|979)\\d{9}[X\\d].*");
        // p13any matches a 13 digit isbn pattern without the correct prefix
        Pattern p13any = Pattern.compile("^\\d{12}[X\\d].*");

        Iterator<String> iter = candidates.iterator();
        while (iter.hasNext()) {
            String value = (String)iter.next().trim();
            // check we have the right pattern, and remove trailing text
            if (p13.matcher(value).matches()) 
                isbnSet.add(value.substring(0, 13));
            else if (p10.matcher(value).matches() && !p13any.matcher(value).matches()) 
                isbnSet.add(value.substring(0, 10));
        }
        return isbnSet;            
    }
    
    
    /**
     * For each occurrence of a marc field in the tags list, extract all 
     * subfield data from the field, place it in a single string (individual 
     * subfield data separated by spaces) and add the string to the result set.
     */
    @SuppressWarnings("unchecked")
    public static final Set<String> getAllSubfields(final Record record, String[] tags) 
    {
        Set<String> result = new LinkedHashSet<String>();

        List<VariableField> varFlds = record.getVariableFields(tags);
        for (VariableField vf : varFlds) {
            
            StringBuffer buffer = new StringBuffer(500);

            DataField df = (DataField) vf;
            if (df != null) {
                List<Subfield> subfields = df.getSubfields();
                for (Subfield sf : subfields) {
                    if (buffer.length() > 0) {
                        buffer.append(" " + sf.getData());
                    } else {
                        buffer.append(sf.getData());
                    }
                }
            }
            if (buffer.length() > 0)
                result.add(buffer.toString());
        }
        
        return result;
    }

    /**
     * get the contents of a subfield, rigorously ensuring no NPE
     * @param df - DataField of interest
     * @param code - code of subfield of interest
     * @return the contents of the subfield, if it exists; null otherwise
     */
    public static final String getSubfieldData(DataField df, char code) {
        String result = null;
        if (df != null) {
            Subfield sf = df.getSubfield(code);
            if (sf != null && sf.getData() != null) {
                result = sf.getData();
            }
        }
        return result;
    }

    /** returns all values of subfield strings of a particular code 
     *  contained in the data field
     */
    @SuppressWarnings("unchecked")
    public static final List<String> getSubfieldStrings(DataField df, char code) {
        List<Subfield> listSubcode = df.getSubfields(code);
        List<String> vals = new ArrayList(listSubcode.size());
        for (Subfield s : listSubcode) {
            vals.add(s.getData());
        }
        return vals;
    }
    
    
    /**
     * given a latin letter with a diacritic, return the latin letter without
     *  the diacritic.
     * Shamelessly stolen from UnicodeCharUtil class of UnicodeNormalizeFilter
     *  by Bob Haschart 
     */
    public static char foldDiacriticLatinChar ( char c ) 
    {
        switch (c) {
            case 0x0181:  return(0x0042);    //  LATIN CAPITAL LETTER B WITH HOOK -> LATIN CAPITAL LETTER B
            case 0x0182:  return(0x0042);    //  LATIN CAPITAL LETTER B WITH TOPBAR -> LATIN CAPITAL LETTER B
            case 0x0187:  return(0x0043);    //  LATIN CAPITAL LETTER C WITH HOOK -> LATIN CAPITAL LETTER C
            case 0x0110:  return(0x0044);    //  LATIN CAPITAL LETTER D WITH STROKE -> LATIN CAPITAL LETTER D
            case 0x018A:  return(0x0044);    //  LATIN CAPITAL LETTER D WITH HOOK -> LATIN CAPITAL LETTER D
            case 0x018B:  return(0x0044);    //  LATIN CAPITAL LETTER D WITH TOPBAR -> LATIN CAPITAL LETTER D
            case 0x0191:  return(0x0046);    //  LATIN CAPITAL LETTER F WITH HOOK -> LATIN CAPITAL LETTER F
            case 0x0193:  return(0x0047);    //  LATIN CAPITAL LETTER G WITH HOOK -> LATIN CAPITAL LETTER G
            case 0x01E4:  return(0x0047);    //  LATIN CAPITAL LETTER G WITH STROKE -> LATIN CAPITAL LETTER G
            case 0x0126:  return(0x0048);    //  LATIN CAPITAL LETTER H WITH STROKE -> LATIN CAPITAL LETTER H
            case 0x0197:  return(0x0049);    //  LATIN CAPITAL LETTER I WITH STROKE -> LATIN CAPITAL LETTER I
            case 0x0198:  return(0x004B);    //  LATIN CAPITAL LETTER K WITH HOOK -> LATIN CAPITAL LETTER K
            case 0x0141:  return(0x004C);    //  LATIN CAPITAL LETTER L WITH STROKE -> LATIN CAPITAL LETTER L
            case 0x019D:  return(0x004E);    //  LATIN CAPITAL LETTER N WITH LEFT HOOK -> LATIN CAPITAL LETTER N
            case 0x0220:  return(0x004E);    //  LATIN CAPITAL LETTER N WITH LONG RIGHT LEG -> LATIN CAPITAL LETTER N
            case 0x00D8:  return(0x004F);    //  LATIN CAPITAL LETTER O WITH STROKE -> LATIN CAPITAL LETTER O
            case 0x019F:  return(0x004F);    //  LATIN CAPITAL LETTER O WITH MIDDLE TILDE -> LATIN CAPITAL LETTER O
            case 0x01FE:  return(0x004F);    //  LATIN CAPITAL LETTER O WITH STROKE AND ACUTE -> LATIN CAPITAL LETTER O
            case 0x01A4:  return(0x0050);    //  LATIN CAPITAL LETTER P WITH HOOK -> LATIN CAPITAL LETTER P
            case 0x0166:  return(0x0054);    //  LATIN CAPITAL LETTER T WITH STROKE -> LATIN CAPITAL LETTER T
            case 0x01AC:  return(0x0054);    //  LATIN CAPITAL LETTER T WITH HOOK -> LATIN CAPITAL LETTER T
            case 0x01AE:  return(0x0054);    //  LATIN CAPITAL LETTER T WITH RETROFLEX HOOK -> LATIN CAPITAL LETTER T
            case 0x01B2:  return(0x0056);    //  LATIN CAPITAL LETTER V WITH HOOK -> LATIN CAPITAL LETTER V
            case 0x01B3:  return(0x0059);    //  LATIN CAPITAL LETTER Y WITH HOOK -> LATIN CAPITAL LETTER Y
            case 0x01B5:  return(0x005A);    //  LATIN CAPITAL LETTER Z WITH STROKE -> LATIN CAPITAL LETTER Z
            case 0x0224:  return(0x005A);    //  LATIN CAPITAL LETTER Z WITH HOOK -> LATIN CAPITAL LETTER Z
            case 0x0180:  return(0x0062);    //  LATIN SMALL LETTER B WITH STROKE -> LATIN SMALL LETTER B
            case 0x0183:  return(0x0062);    //  LATIN SMALL LETTER B WITH TOPBAR -> LATIN SMALL LETTER B
            case 0x0253:  return(0x0062);    //  LATIN SMALL LETTER B WITH HOOK -> LATIN SMALL LETTER B
            case 0x0188:  return(0x0063);    //  LATIN SMALL LETTER C WITH HOOK -> LATIN SMALL LETTER C
            case 0x0255:  return(0x0063);    //  LATIN SMALL LETTER C WITH CURL -> LATIN SMALL LETTER C
            case 0x0111:  return(0x0064);    //  LATIN SMALL LETTER D WITH STROKE -> LATIN SMALL LETTER D
            case 0x018C:  return(0x0064);    //  LATIN SMALL LETTER D WITH TOPBAR -> LATIN SMALL LETTER D
            case 0x0221:  return(0x0064);    //  LATIN SMALL LETTER D WITH CURL -> LATIN SMALL LETTER D
            case 0x0256:  return(0x0064);    //  LATIN SMALL LETTER D WITH TAIL -> LATIN SMALL LETTER D
            case 0x0257:  return(0x0064);    //  LATIN SMALL LETTER D WITH HOOK -> LATIN SMALL LETTER D
            case 0x0192:  return(0x0066);    //  LATIN SMALL LETTER F WITH HOOK -> LATIN SMALL LETTER F
            case 0x01E5:  return(0x0067);    //  LATIN SMALL LETTER G WITH STROKE -> LATIN SMALL LETTER G
            case 0x0260:  return(0x0067);    //  LATIN SMALL LETTER G WITH HOOK -> LATIN SMALL LETTER G
            case 0x0127:  return(0x0068);    //  LATIN SMALL LETTER H WITH STROKE -> LATIN SMALL LETTER H
            case 0x0266:  return(0x0068);    //  LATIN SMALL LETTER H WITH HOOK -> LATIN SMALL LETTER H
            case 0x0268:  return(0x0069);    //  LATIN SMALL LETTER I WITH STROKE -> LATIN SMALL LETTER I
            case 0x029D:  return(0x006A);    //  LATIN SMALL LETTER J WITH CROSSED-TAIL -> LATIN SMALL LETTER J
            case 0x0199:  return(0x006B);    //  LATIN SMALL LETTER K WITH HOOK -> LATIN SMALL LETTER K
            case 0x0142:  return(0x006C);    //  LATIN SMALL LETTER L WITH STROKE -> LATIN SMALL LETTER L
            case 0x019A:  return(0x006C);    //  LATIN SMALL LETTER L WITH BAR -> LATIN SMALL LETTER L
            case 0x0234:  return(0x006C);    //  LATIN SMALL LETTER L WITH CURL -> LATIN SMALL LETTER L
            case 0x026B:  return(0x006C);    //  LATIN SMALL LETTER L WITH MIDDLE TILDE -> LATIN SMALL LETTER L
            case 0x026C:  return(0x006C);    //  LATIN SMALL LETTER L WITH BELT -> LATIN SMALL LETTER L
            case 0x026D:  return(0x006C);    //  LATIN SMALL LETTER L WITH RETROFLEX HOOK -> LATIN SMALL LETTER L
            case 0x0271:  return(0x006D);    //  LATIN SMALL LETTER M WITH HOOK -> LATIN SMALL LETTER M
            case 0x019E:  return(0x006E);    //  LATIN SMALL LETTER N WITH LONG RIGHT LEG -> LATIN SMALL LETTER N
            case 0x0235:  return(0x006E);    //  LATIN SMALL LETTER N WITH CURL -> LATIN SMALL LETTER N
            case 0x0272:  return(0x006E);    //  LATIN SMALL LETTER N WITH LEFT HOOK -> LATIN SMALL LETTER N
            case 0x0273:  return(0x006E);    //  LATIN SMALL LETTER N WITH RETROFLEX HOOK -> LATIN SMALL LETTER N
            case 0x00F8:  return(0x006F);    //  LATIN SMALL LETTER O WITH STROKE -> LATIN SMALL LETTER O
            case 0x01FF:  return(0x006F);    //  LATIN SMALL LETTER O WITH STROKE AND ACUTE -> LATIN SMALL LETTER O
            case 0x01A5:  return(0x0070);    //  LATIN SMALL LETTER P WITH HOOK -> LATIN SMALL LETTER P
            case 0x02A0:  return(0x0071);    //  LATIN SMALL LETTER Q WITH HOOK -> LATIN SMALL LETTER Q
            case 0x027C:  return(0x0072);    //  LATIN SMALL LETTER R WITH LONG LEG -> LATIN SMALL LETTER R
            case 0x027D:  return(0x0072);    //  LATIN SMALL LETTER R WITH TAIL -> LATIN SMALL LETTER R
            case 0x0282:  return(0x0073);    //  LATIN SMALL LETTER S WITH HOOK -> LATIN SMALL LETTER S
            case 0x0167:  return(0x0074);    //  LATIN SMALL LETTER T WITH STROKE -> LATIN SMALL LETTER T
            case 0x01AB:  return(0x0074);    //  LATIN SMALL LETTER T WITH PALATAL HOOK -> LATIN SMALL LETTER T
            case 0x01AD:  return(0x0074);    //  LATIN SMALL LETTER T WITH HOOK -> LATIN SMALL LETTER T
            case 0x0236:  return(0x0074);    //  LATIN SMALL LETTER T WITH CURL -> LATIN SMALL LETTER T
            case 0x0288:  return(0x0074);    //  LATIN SMALL LETTER T WITH RETROFLEX HOOK -> LATIN SMALL LETTER T
            case 0x028B:  return(0x0076);    //  LATIN SMALL LETTER V WITH HOOK -> LATIN SMALL LETTER V
            case 0x01B4:  return(0x0079);    //  LATIN SMALL LETTER Y WITH HOOK -> LATIN SMALL LETTER Y
            case 0x01B6:  return(0x007A);    //  LATIN SMALL LETTER Z WITH STROKE -> LATIN SMALL LETTER Z
            case 0x0225:  return(0x007A);    //  LATIN SMALL LETTER Z WITH HOOK -> LATIN SMALL LETTER Z
            case 0x0290:  return(0x007A);    //  LATIN SMALL LETTER Z WITH RETROFLEX HOOK -> LATIN SMALL LETTER Z
            case 0x0291:  return(0x007A);    //  LATIN SMALL LETTER Z WITH CURL -> LATIN SMALL LETTER Z
            default:      return(0x00);
        }
    }
    
   @SuppressWarnings("unchecked")
    public static void setLog4jLogLevel(org.apache.log4j.Level newLevel)
    {
        Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        Enumeration<Logger> enLogger = rootLogger.getLoggerRepository().getCurrentLoggers();
        Logger tmpLogger = null;
        /* If logger is root, then need to loop through all loggers under root
        * and change their logging levels too.  Also, skip sql loggers so
        they
        * do not get effected.
        */
        while(enLogger.hasMoreElements())
        {
            tmpLogger = (Logger)(enLogger.nextElement());
            tmpLogger.setLevel(newLevel);
        }
        Enumeration<Appender> enAppenders = rootLogger.getAllAppenders();
        Appender appender;
        while(enAppenders.hasMoreElements())
        {
            appender = (Appender)enAppenders.nextElement();
            
            if(appender instanceof AsyncAppender)
            {
                AsyncAppender asyncAppender = (AsyncAppender)appender;
                asyncAppender.activateOptions();
//                    rfa = (RollingFileAppender)asyncAppender.getAppender("R");
//                    rfa.activateOptions();
//                    ca = (ConsoleAppender)asyncAppender.getAppender("STDOUT");
//                    ca.activateOptions();
            }
        }

    }


}
