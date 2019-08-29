package org.computer.aman.metrics.size.loc;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.computer.aman.io.FileFinder;
import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;

/**
 * A CUI application for measuring LOC values of source files.<br>
 * Supported languages include Java, C and C++. 
 * <p></p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class LOCCounterCUI
{
    public static void main(String[] args) 
    throws SecurityException, IOException, NotSupportedSourceFileExeption
    {
    	System.err.println("LOCCounter version 0.6");
        System.err.println("Copyright (C) 2008-2019 Hirohisa AMAN <aman@computer.org>");
    	System.err.println("----------------------------------------------------------------");
    	
    	// Parses option to decide the display mode.
    	// (mode)
    	//    0 : (default) display only LOC value
    	//    1 : (-t option) display only the total line count
    	//    2 : (-a option) display both LOC and the total line count
    	//    3 : (-d option) display the details including the LOC, the total line count, and the total blank line count together with the source code
    	int mode = 0;    	    	
    	String target = null;    	
    	for ( int i = 0; i < args.length; i++ ){
    		if ( args[i].startsWith("-") ){
    			if ( args[i].equals("-t") ){
    				mode = 1;
    			}
    			else if ( args[i].equals("-a") ){
    				mode = 2;
    			}
    			else if ( args[i].equals("-d") ){
    				mode = 3;
    			}
    			else{
    				printUsage();
    				return;
    			}
    		}
    		else{
    			target = args[i];
    			break;
    		}
    	}
    	
    	if ( target == null ){
    		printUsage();
    		return;
    	}    	
    	List<String> list = (new FileFinder(target)).getList(".+\\.(java|JAVA|c|C|h|H|cpp|CPP|cc|CC|cxx|CXX|m|M|i|I|ii|II)$");
    	
        // For each source file, executes the measurement and prints the results 
        BigInteger totalLOC = new BigInteger("0");
        BigInteger totalLineCount = new BigInteger("0");
    	for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
            LOC results = LOCCounter.measure(new SourceFile(iterator.next()));

            totalLOC = totalLOC.add(BigInteger.valueOf(results.getLOC()));
            totalLineCount = totalLineCount.add(BigInteger.valueOf(results.getTotalLineCount()));
            System.out.print(results.getSourceFile().getPath() + ",");
            if ( mode == 0 ){
                System.out.println(results.getLOC());
            }
            else if ( mode == 1 ){
                System.out.println(results.getTotalLineCount());
            }
            else{
            	System.out.println(results.getLOC() + "," + results.getTotalLineCount() + "," + results.getBlankCount());
                if ( mode == 3 ){
                    System.err.println(results);        	
                }
            }
    	}    	
        System.err.println("----------------------------------------------------------------");
    	System.err.println("# of source files = " + list.size() + ", " +
    	                   "Total LOC = " + totalLOC + ", " +
    	                   "Total line counts = " + totalLineCount);
    }

    /**
     * Prints the usage of this application.
     */
    private static void printUsage()
    {
		System.err.println("java -jar LOCCounter.jar [option] (source_file | directory)");
		System.err.println("option : ");
		System.err.println(" (none) : prints LOC");    				
		System.err.println("     -t : prints the total line count");
		System.err.println("     -a : prints LOC, the total line count, and the total blank line count");
		System.err.println("     -d : prints all results together with the code");
		System.err.println();
		System.err.println("The printing format is as below:");
        System.err.println("  (default) source_file,LOC");
        System.err.println("  (-t)      source_file,total_line_count");
		System.err.println("  (-a)      source_file,LOC,total_line_count,total_blank_line_count");
        System.err.println("  (-d)      (same as -a, but the source code is printed through the standard error output");
    }
}
