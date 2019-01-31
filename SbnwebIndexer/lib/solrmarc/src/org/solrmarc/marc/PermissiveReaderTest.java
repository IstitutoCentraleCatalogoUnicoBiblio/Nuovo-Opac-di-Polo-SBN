package org.solrmarc.marc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.marc4j.ErrorHandler;
import org.marc4j.MarcException;
import org.marc4j.MarcPermissiveStreamReader;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.Record;

public class PermissiveReaderTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        System.setProperty("org.marc4j.marc.MarcFactory", "marcoverride.UVAMarcFactoryImpl");
        PrintStream out = System.out;
        try
        {
            out = new PrintStream(System.out, true, "UTF-8");
        }
        catch (UnsupportedEncodingException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        boolean verbose = Boolean.parseBoolean(System.getProperty("marc.verbose"));
        boolean veryverbose = Boolean.parseBoolean(System.getProperty("marc.verbose"));
        boolean usemap = false;
        LinkedHashMap<Character,String> map = null;
        if (args[0].equals("-m")) 
        {
            usemap = true;
            String newArgs[] = new String[args.length-1];
            System.arraycopy(args, 1, newArgs, 0, args.length-1);
            args = newArgs;
            map = new LinkedHashMap<Character,String>();
        }
        if (args[0].equals("-v")) 
        {
            verbose = true;
            String newArgs[] = new String[args.length-1];
            System.arraycopy(args, 1, newArgs, 0, args.length-1);
            args = newArgs;
        }
        if (args[0].equals("-vv")) 
        {
            verbose = true;
            veryverbose = true;
            String newArgs[] = new String[args.length-1];
            System.arraycopy(args, 1, newArgs, 0, args.length-1);
            args = newArgs;
        }
        String fileStr = args[0];
        File file = new File(fileStr);
        MarcReader readerNormal = null;
        MarcReader readerPermissive = null;
        boolean to_utf_8 = true;
       
        InputStream inNorm;
        InputStream inPerm;
        OutputStream patchedRecStream = null;
        MarcWriter patchedRecs = null;
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorHandler errorHandler1 = new ErrorHandler();
        try
        {
            inNorm = new FileInputStream(file);
//          readerNormal = new MarcPermissiveStreamReader(inNorm, false, to_utf_8, "MARC8");
            readerNormal = new MarcPermissiveStreamReader(inNorm, errorHandler1, to_utf_8);
            inPerm = new FileInputStream(file);
            readerPermissive = new MarcPermissiveStreamReader(inPerm, errorHandler, to_utf_8);
            readerPermissive = new MarcTranslatedReader(readerPermissive, "C");
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean done = false;
        if (args.length > 1)
        {
            try
            {
                patchedRecStream = new FileOutputStream(new File(args[1]));
                patchedRecs = new MarcStreamWriter(patchedRecStream, "UTF8");
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        while (readerNormal.hasNext() && readerPermissive.hasNext())
        {
            Record recNorm;
            Record recPerm;
            recPerm = readerPermissive.next();
            String strPerm = recPerm.toString();
            try {
                recNorm = readerNormal.next();
            }
            catch (MarcException me)
            {
                if (verbose)
                {
                    out.println("Fatal Exception: "+ me.getMessage());
                    dumpErrors(out, errorHandler);
                    showDiffs(out, null, strPerm, verbose, map);
                    out.println("-------------------------------------------------------------------------------------");
                }
                if (patchedRecs != null)
                {
                    patchedRecs.write(recPerm);
                }
                continue;
            }
            String strNorm = recNorm.toString();
            if (!strNorm.equals(strPerm))
            {
                if (verbose)
                {
                    dumpErrors(out, errorHandler);
                    showDiffs(out, strNorm, strPerm, verbose, map);
                    out.println("-------------------------------------------------------------------------------------");
                    
                }
                else
                {
                    showDiffs(out, strNorm, strPerm, false, map);
                }
                if (patchedRecs != null)
                {
                    patchedRecs.write(recPerm);
                }
            }
            else if (errorHandler.hasErrors())
            {
                if (verbose)
                {
                    out.println("Results identical, but errors reported");
                    dumpErrors(out, errorHandler);
                    showDiffs(out, strNorm, strPerm, verbose, map);
                    out.println("-------------------------------------------------------------------------------------");
                }
                if (patchedRecs != null)
                {
                    patchedRecs.write(recPerm);
                }
            }
            else if (veryverbose)
            {
                showDiffs(out, strNorm, strPerm, veryverbose, map);                
            }
                
        }
    }

    public static void showDiffs(PrintStream out, String strNorm, String strPerm, boolean verbose, Map<Character,String> map)
    {
        if (strNorm != null)
        {
            String normLines[] = strNorm.split("\n");
            String permLines[] = strPerm.split("\n");
            if (normLines.length == permLines.length)
            {
                for (int i = 0; i < normLines.length; i++)
                {
                    if (normLines[i].equals(permLines[i]))
                    {
                        if (verbose) out.println("   " + normLines[i]);
                    }
                    else if (map != null)
                    {
                        int index1 = 0; 
                        int index2 = 0;
                        boolean showLines = false;
                        while (index1 < normLines[i].length() && index2 < permLines[i].length())
                        {
                            while (index1 < normLines[i].length() && index2 < permLines[i].length() &&
                                   normLines[i].charAt(index1) == permLines[i].charAt(index2))
                            {
                                index1++; index2++;
                            }
                            if (index1 < normLines[i].length() && index2 < permLines[i].length())
                            {
                                if (!map.containsKey(permLines[i].charAt(index2)))
                                {
                                    Character key = permLines[i].charAt(index2);
                                    map.put(key, normLines[i] + "@@" +  permLines[i]);
                                    showLines = true; 
                                    out.println(" "+key+" : " + normLines[i]);
                                    out.println(" "+key+" : " + permLines[i]);                    

                                }
                                index2++;
                                index1++;
                                if (index1 < normLines[i].length() && index2 < permLines[i].length())
                                {
                                    while (permLines[i].substring(index2,index2+1).matches("\\p{M}") )
                                    {
                                        index2++;
                                    }                           
                                    while (normLines[i].substring(index1,index1+1).matches("\\p{M}") )
                                    {
                                        index1++;
                                    }    
                                }
                            }
                        }
//                        if (showLines)
//                        {
//                            out.println(" < " + normLines[i]);
//                            out.println(" > " + permLines[i]);                    
//                        }
                    }
                    else
                    {
                        out.println(" < " + normLines[i]);
                        out.println(" > " + permLines[i]);                    
                    }
                }
            }
        }
        else
        {
            String permLines[] = strPerm.split("\n");
            for (int i = 0; i < permLines.length; i++)
            {
                if (verbose) out.println("   " + permLines[i]);
            }
        }

    }
    
    public static void dumpErrors(PrintStream out, ErrorHandler errorHandler)
    {
        List<Object> errors = errorHandler.getErrors();
        if (errors != null) 
        {
            Iterator<Object> iter = errors.iterator();
            while (iter.hasNext())
            {
                Object error = iter.next();
                out.println(error.toString());
            }
        }
    }
}
