package assignment5;

public class Main {

	public static void main(String[] args) {
		// launch(args);
	}

}


/*OLD MAIN
package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Timberlon Gray
 * tg22698
 * 16235
 * Raiyan Chowdhury
 * rac4444
 * 16235
 * Slip days used: <0>
 * Fall 2016
 */
/*
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Method;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
 /*
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     *//*
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        /*
        boolean continueSim= true;
        while(continueSim) {
        	ArrayList<String> tokens = parse(kb);
        	if(tokens.size() > 0) {
	        	try {
	        		continueSim = execute(tokens);
	        	} catch (Exception e) {
	        		System.out.print("error processing:");
	        		for(String token : tokens) {
	        			System.out.print(" " + token);
	        		}
	        		System.out.println();
	        	}
        	}
        }
        
        /* Write your code above *//*
        System.out.flush();

    }
    
    /**
     * Converts the input into an array of String tokens. 
     * @param kb - keyboard input
     * @return - array of tokens relatig to input
     *//*
    public static ArrayList<String> parse(Scanner kb) {
    	String token = kb.nextLine();
    	Scanner sc = new Scanner(token);
    	ArrayList<String> tokens = new ArrayList<String>();
    	while(sc.hasNext()) {
    		tokens.add(sc.next());
    	}
    	sc.close();
    	return tokens;
    }
    
    /**
     * Throws an exception if the command is not the correct length of tokens.
     * @param tokens - keyboard input
     * @param length - desired length of command
     * @throws Exception - thrown if not desired length
     *//*
    public static void checkCommandLength(ArrayList<String> tokens, int length) throws Exception {
    	if(tokens.size() != length) throw new Exception();
    }
    
    /**
     * Takes a keyboard input and processes it. Checks for each command or 
     * prints an error if no correct command exists. prints a different error 
     * if the command is not structured correctly. 
     * @param tokens - keyboard input structured as a series of tokens
     * @return - whether or not to continue checking for commands, in other 
     * words if "quit" was input.
     * @throws Exception - thrown if incorrect command structure.
     *//*
    public static boolean execute(ArrayList<String> tokens) throws Exception {
    	// quit
    	if(tokens.get(0).equals("quit")) {
    		try {
    			checkCommandLength(tokens, 1);
    			return false;
    		} catch (Exception e){
    			throw e;
    		}
    	}
    	// show
    	if(tokens.get(0).equals("show")) {
    		try {
    			checkCommandLength(tokens, 1);
    			Critter.displayWorld();
    		} catch (Exception e){
    			throw e;
    		}
    		return true;
    	}
    	// step
    	if(tokens.get(0).equals("step")) {
    		int steps = 1;
    		if(tokens.size() > 1) {
    			try {
    				checkCommandLength(tokens, 2);
    				steps = Integer.parseInt(tokens.get(1));
    			} catch (Exception e){ // next token isnt an int
    				throw e;
    			}
    		}
    		while (steps > 0) {
    			Critter.worldTimeStep();
    			steps--;
    		}
    		return true;
    	}
    	// seed
    	if(tokens.get(0).equals("seed")) {
    		try {
    			checkCommandLength(tokens, 2);
    			Critter.setSeed(Integer.parseInt(tokens.get(1)));
    		} catch (Exception e){ // next token isnt an int
				throw e;
			}
    		return true;
    	}
    	// make
    	if(tokens.get(0).equals("make")) {
    		// checks if number specified, or if not then 1
    		int num = 1;
    		if(tokens.size() > 2) {
    			try {
    				checkCommandLength(tokens, 3);
    				num = Integer.parseInt(tokens.get(2));
    			} catch (Exception e){ // next token isn't an int or too many tokens
    				throw e;
    			}
    		}
    		try {
    			String critter_class_name = tokens.get(1);
    			for (int i = 0; i < num; i++) {
    				Critter.makeCritter(critter_class_name);
    			}
    		} catch (Exception e) {
    			throw e;
    		}
    		
    		return true;
    	}
    	// stats
    	if(tokens.get(0).equals("stats")) {
    		try {
    			checkCommandLength(tokens, 2);
    			String critter_class_name = tokens.get(1);
    			if (critter_class_name.toLowerCase().equals("critter"))
    				throw new Exception();
    			List<Critter> list_of_instances = Critter.getInstances(critter_class_name);
    			critter_class_name = myPackage + "." + critter_class_name;
    			Class<?> type = Class.forName(critter_class_name);
    			Method method = type.getMethod("runStats", List.class);
    			method.invoke(null, list_of_instances);
    		}
    		catch (Exception e) {
    			throw e;
    		}
    		return true;
    	}
    	//unknown command
    	System.out.print("invalid command:");
		for(String token : tokens) {
			System.out.print(" " + token);
		}
		System.out.println();
		return true;
		
    }
}
*/