package assignment5;

import java.util.ArrayList;
import java.util.HashMap;

/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Timberlon Gray
 * tg22698
 * 16235
 * Raiyan Chowdhury
 * rac4444
 * 16235
 * Slip days used: <0>
 * Spring 2017
 */

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.control.*;

import java.io.File;
import java.lang.reflect.Method;

public class Main extends Application{
	private int maxSimWidth = 900;
	private int maxSimHeight = 700;
	private double simSpeed = 50;
	private boolean isRunning = false;
	private static String myPackage;
	static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

	public static void main(String[] args) {
		launch(args);
	}

	
	@Override
	public void start(Stage primaryStage) {
		//code for getting class names
		String path = System.getProperty("user.dir");
		String files[] = null;
		try {
			String bin = path + File.separator + "bin" + File.separator + myPackage;
			File f = new File(bin);
			files = f.list();
			for (int i = 0; i < files.length; i++) {
				files[i] = files[i].substring(0, files[i].length()-6);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//get list of Critter subclasses
		List<String> classes = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			Class<?> my_critter = null;
			try {
				my_critter = Class.forName(myPackage + "." + files[i]);
				if (Critter.class.isAssignableFrom(my_critter)
						&& !files[i].equals("Critter$TestCritter") 
						&& !files[i].equals("Critter")) {
					classes.add(files[i]);
				}
			}
			catch (ClassNotFoundException | NoClassDefFoundError e) {
				my_critter = null;
			}
		}
		
		// GridPane for all the buttons and text boxes
		GridPane gridPane = new GridPane();
		
		//Group for world
		int wWidth=Params.world_width;
		int wHeight=Params.world_height;
		int size1 = maxSimWidth/wWidth;
		if (size1 > maxSimHeight/wHeight) size1 = maxSimHeight/wHeight;
		int size = size1;
		Group pane = new Group();
    	Stage stage = new Stage();
		stage.setTitle("World");
		Scene scene = new Scene(pane,wWidth*size, wHeight*size);
		stage.setScene(scene);
		stage.show();
		
		// box for displaying error messages
		Label errorMsg = new Label();
		errorMsg.setPrefWidth(350);
		errorMsg.setTextFill(Color.RED);
		gridPane.add(errorMsg, 0, 10);
		
		// input for critter type, button to display stats, box for displaying the stats
		/*TextField critterInput = new TextField();
		critterInput.setPromptText("Critter type");
		critterInput.setPrefWidth(350);
		gridPane.add(critterInput, 0, 4);*/
		//text for displaying stats
		Text stats = new Text();
		stats.setWrappingWidth(360);
		stats.setFont(Font.font("Verdana", 12));
		gridPane.add(stats, 0, 5);
	
		MenuButton critterInput = new MenuButton();
		critterInput.setPrefWidth(350);
		List<CheckMenuItem> class_items = new ArrayList<CheckMenuItem>();
		HashMap<String, Boolean> display_or_not = new HashMap<String, Boolean>();
		for (String s : classes) {
			class_items.add(new CheckMenuItem(s));
			display_or_not.put(s, false);
		}
		for (CheckMenuItem m : class_items) {
			m.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (m.isSelected()) {
						display_or_not.put(m.getText(), true);
					} else {
						display_or_not.put(m.getText(), false);
					}
				}
			});
		}
		critterInput.getItems().addAll(class_items);
		gridPane.add(critterInput, 0, 4);
				
		Button runStats = new Button();
		runStats.setText("Get Statistics");
		gridPane.add(runStats, 2, 4);
		runStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				try {
					String critter_stats = "";
					for (String critter_class_name : display_or_not.keySet()) {
						if (display_or_not.get(critter_class_name) == true) {
							List<Critter> list_of_instances = Critter.getInstances(critter_class_name);
							String critter_class = myPackage + "." + critter_class_name;
							Class<?> type = Class.forName(critter_class);
							Method method = type.getMethod("runStats", List.class);
							critter_stats = critter_stats + critter_class_name + " stats:\n";
							critter_stats = critter_stats + (String) method.invoke(null, list_of_instances) + "\n\n";
						}
					}
					if (critter_stats.equals("")) {
						stats.setText("");
						errorMsg.setText("Please choose at least one critter type.");
					}
					else {
						stats.setText(critter_stats);
						errorMsg.setText("");
					}
			    } catch (Exception f){
			    	errorMsg.setText("You did not enter a known critter type for stats!");
			    	stats.setText("");
			    }
			}
		});
		
		// Time Step button and text box functionality
		TextField stepInput = new TextField();
		stepInput.setPromptText("Enter the desired amount of time steps");
		stepInput.setPrefWidth(350);
		gridPane.add(stepInput, 0, 0);
		
		Button stepButton = new Button();
		stepButton.setText("Perform Time Steps");
		gridPane.add(stepButton, 2, 0);
		stepButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	int steps = 1;
		    	try {
		    		steps = Integer.parseInt(stepInput.getText());
		    		errorMsg.setText("");
	    		} catch (Exception f){
	    			errorMsg.setText("You did not enter an integer for time steps! Default = 1");
	    		}
		    	while(steps > 0) {
		    		Critter.worldTimeStep();
		    		try {
						String critter_stats = "";
						for (String critter_class_name : display_or_not.keySet()) {
							if (display_or_not.get(critter_class_name) == true) {
								List<Critter> list_of_instances = Critter.getInstances(critter_class_name);
								String critter_class = myPackage + "." + critter_class_name;
								Class<?> type = Class.forName(critter_class);
								Method method = type.getMethod("runStats", List.class);
								critter_stats = critter_stats + critter_class_name + " stats:\n";
								critter_stats = critter_stats + (String) method.invoke(null, list_of_instances) + "\n\n";
							}
						}
						stats.setText(critter_stats);
					 	//errorMsg.setText("");
				    } catch (Exception f){
				    	//errorMsg.setText("You did not enter a known critter type for stats!");
				    	stats.setText("");
				    }
		    		steps--;
		    	}
		    	Critter.displayWorld(pane);
		    }
		});
		
		// Seed input and button to set
		TextField seedInput = new TextField();
		seedInput.setPromptText("Enter a seed if desired");
		seedInput.setPrefWidth(350);
		gridPane.add(seedInput, 0, 1);
		
		Button seedSet = new Button();
		seedSet.setText("Set Seed");
		gridPane.add(seedSet, 2, 1);
		seedSet.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	try {
		    		Critter.setSeed(Integer.parseInt(seedInput.getText()));
		    		errorMsg.setText("");
	    		} catch (Exception f){
	    			errorMsg.setText("You did not enter an integer for the seed!");
	    		}
		    }
		});
		
		// Code to create critters. Includes text box for critter type, amount desired, and button to activate.
				/*TextField critterInput2 = new TextField();
				critterInput2.setPromptText("Enter desired critter type");
				critterInput2.setPrefWidth(350);
				gridPane.add(critterInput2, 0, 2);*/
				final ComboBox<String> critterInput2 = new ComboBox<String>();
				List<String> classes2 = new ArrayList<String>();
				classes2.addAll(classes);
				classes2.remove("Algae");
 				critterInput2.getItems().addAll(classes2);
 				critterInput2.setPrefWidth(350);
 				gridPane.add(critterInput2, 0, 2);
				
				TextField amtToSpawn = new TextField();
				amtToSpawn.setPromptText("How many?");
				amtToSpawn.setPrefWidth(80);
				gridPane.add(amtToSpawn, 1, 2);
				
				Button createCritters = new Button();
				createCritters.setText("Create Critters");
				gridPane.add(createCritters, 2, 2);
				createCritters.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	int intToSpawn = 0;
				    	try {
				    		intToSpawn = Integer.parseInt(amtToSpawn.getText());
				    		errorMsg.setText("");
			    		} catch (Exception f){
			    			if (critterInput2.getValue() == null) 
			    				errorMsg.setText("Please specify a critter type and a valid number of critters.");
			    			else if (amtToSpawn.getText().equals(""))
			    				errorMsg.setText("Please specify a valid number of critters.");
			    			else
			    				errorMsg.setText("Number of critters specified is not an integer!");
			    		}
				    	try {
				    		while(intToSpawn > 0) {
			    				Critter.makeCritter(critterInput2.getValue());
			    				intToSpawn--;
			    			}
				    	} catch (Exception g) {
				    		errorMsg.setText("Please specify a critter type.");
				    	}
				    	Critter.displayWorld(pane);
				    }
				});
				
				// Button to quit the program
				Button quit = new Button();
				quit.setText("End Simulation");
				gridPane.add(quit, 0, 11);
				quit.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						System.exit(0);
					}
				});
				
		// Button and function for the animation
		AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	for(int i = 0; i <= (1000000000-(simSpeed)*10000000); i++) {}
            	Critter.worldTimeStep();
            	try {
					String critter_stats = "";
					for (String critter_class_name : display_or_not.keySet()) {
						if (display_or_not.get(critter_class_name) == true) {
							List<Critter> list_of_instances = Critter.getInstances(critter_class_name);
							String critter_class = myPackage + "." + critter_class_name;
							Class<?> type = Class.forName(critter_class);
							Method method = type.getMethod("runStats", List.class);
							critter_stats = critter_stats + critter_class_name + " stats:\n";
							critter_stats = critter_stats + (String) method.invoke(null, list_of_instances) + "\n\n";
						}
					}
					stats.setText(critter_stats);
				 	errorMsg.setText("");
			    } catch (Exception f){
			    	//errorMsg.setText("You did not enter a known critter type for stats!");
			    	stats.setText("");
			    }
            	Critter.displayWorld(pane);
            }
        };
        /*TextField animationSpeed = new TextField();
        animationSpeed.setPromptText("Enter the desired speed of animation");
        animationSpeed.setPrefWidth(350);*/
        Slider animationSpeed = new Slider();
        animationSpeed.setMin(0);
        animationSpeed.setMax(100);
        animationSpeed.setValue(50);
        animationSpeed.setShowTickMarks(true);
        animationSpeed.setBlockIncrement(5);
		gridPane.add(animationSpeed, 0, 3);
		
		Button animationToggle = new Button();
		animationToggle.setText("Toggle Animation");
		gridPane.add(animationToggle, 2, 3);
		animationToggle.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if(isRunning) {
		    		isRunning = false;
		    		animationSpeed.setDisable(false);
		    		createCritters.setDisable(false);
		    		amtToSpawn.setDisable(false);
		    		critterInput2.setDisable(false);
		    		runStats.setDisable(false);
		    		critterInput.setDisable(false);
		    		stats.setDisable(false);
		    		seedSet.setDisable(false);
		    		seedInput.setDisable(false);
		    		stepInput.setDisable(false);
		    		stepButton.setDisable(false);
		    		quit.setDisable(false);
		    		timer.stop();
		    	} else {
		    		simSpeed = animationSpeed.getValue();
		    		animationSpeed.setDisable(true);
		    		createCritters.setDisable(true);
		    		amtToSpawn.setDisable(true);
		    		critterInput2.setDisable(true);
		    		runStats.setDisable(true);
		    		critterInput.setDisable(true);
		    		stats.setDisable(true);
		    		seedSet.setDisable(true);
		    		seedInput.setDisable(true);
		    		stepInput.setDisable(true);
		    		stepButton.setDisable(true);
		    		quit.setDisable(true);
		    		isRunning = true;
		    		timer.start();
		    	}
		    }
		});
        
		primaryStage.setScene(new Scene(gridPane, 560, 700));
		primaryStage.show();
		Critter.displayWorld(pane);
	}
}


/*OLD MAIN


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