package fr.m1m2.advancedEval;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;

public class Experiment {

	public static Font FONTExp = new Font("Helvetica", Font.BOLD, 32);
	public static Font FONTTrial = new Font("Helvetica", Font.PLAIN, 26);
	
	protected String participant = "";
	protected int block = 1;
	protected int trial = -1;

	protected File designFile = null;
	protected PrintWriter printWriterLog;

	protected ArrayList<Trial> allTrials = new ArrayList<Trial>();
	protected int currentTrial = 0;
	
	protected Canvas canvas;
	

	public Experiment() { }

	public void start() {
		// 1. parse the experiment design file for feeding allTrials with the list of trials that should be run for that participant
		loadTrials(); //ok
		
		// 2. init the log file
		initLog(); // ok
		
		// 3. init the graphical scene
		initScene(); //ok
		
		// 4. start the first trial
		nextTrial();
	}
	
	public void initScene() {
		JFrame frame = new JFrame("Experiment -- preattention");
		canvas = new Canvas(1400, 1200);
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	public void loadTrials() {
		try {
			int count = 0;
			int training = 1;
			
			BufferedReader br = new BufferedReader(new FileReader(new File("experiment.csv")));
			String line = br.readLine();
			String[] parts = line.split(",");
			line = br.readLine();
			while(line != null) {
				parts = line.split(",");
				// check that these are the right data for the current participant
				if(parts[0].equals(participant)) {
					boolean practice = parts[1].equals("true");
					int b = Integer.parseInt(parts[2]); // block
					
					int t = Integer.parseInt(parts[3]); // trial
					String vv = parts[4];
					int objectsCount = Integer.parseInt(parts[5]);
					
					
					if ( (training > block || training == block ) && trial == -1 && practice == true) {
						System.out.println("training" + training + " " + b + " " + block + " et "+ t + " " + trial);
						Trial tl = new Trial(this, practice, b, t, vv, objectsCount);
						allTrials.add(tl);	
					}
					else if ( (b==block && t>=trial) || b>block){
						System.out.println(b + " " + block + " et "+ t + " " + trial);
						Trial tl = new Trial(this, practice, b, t, vv, objectsCount);
						allTrials.add(tl);	
					}
					if(b == 0){ count++;}
					if(count == 3){ count = 0; training++;}
				}
				line = br.readLine();
			}
			System.out.println(allTrials.size());
			setMirror();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void setMirror(){
		int[][] count = new int[3][2];
		for (Trial t: allTrials){
			int random = new Random().nextInt(2);
			if (random == 0){ t.setMirror(true);}
			else { t.setMirror(false); }
		}
	}

	public void initLog() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
			Date date = new Date();

			FileWriter file = new FileWriter("log/participant=" + participant + " " + date + ".csv", false);
			printWriterLog = new PrintWriter(file);
			
			String header = "Participant,Practice,Block,Trial,VV,ObjectsCount,MeasuredTime";
					
			printWriterLog.print(header);
			printWriterLog.print("\n");
			printWriterLog.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void nextTrial() {
		if ( currentTrial >= allTrials.size()){
			end();
			return;
		}else if ( allTrials.get(currentTrial).block == 0 && currentTrial == 0 ){
			instructionPractice();
			return;
		}else if ( allTrials.get(currentTrial).block !=0 && currentTrial-1 >= 0 && allTrials.get(currentTrial-1).block ==0 ){
			instructionTrial();
			return;
		}else if ( allTrials.get(currentTrial).block ==0 && currentTrial-1 >= 0 && allTrials.get(currentTrial-1).block !=0 ){
			instructionPractice();
			return;
		}
	
		
		Trial trial = allTrials.get(currentTrial);
		trial.displayInstructions();
		currentTrial++;	
	}
	
	public void instructionPractice(){		
		CExtensionalTag practiceInstruction = new CExtensionalTag() { };
		CText text1 = canvas.newText(0, 0, "The next 3 trial will be part of a practice session", Experiment.FONTExp);
		CText text2 = canvas.newText(0, 50, "Identify the shape that is different from all other shapes based on:", Experiment.FONTExp);
		CText text3 = canvas.newText(0, 100, "COLOR and/or MOUVEMENT", Experiment.FONTExp);
		CText text6 = canvas.newText(0, 350, "--> Press Enter key when ready", Experiment.FONTExp.deriveFont(Font.PLAIN, 15));
		text1.addTag(practiceInstruction);
		text2.addTag(practiceInstruction);
		text3.addTag(practiceInstruction);
		text6.addTag(practiceInstruction);
		double textCenterX = practiceInstruction.getCenterX();
		double textCenterY = practiceInstruction.getCenterY();
		double canvasCenterX = canvas.getWidth()/2;
		double canvasCenterY = canvas.getHeight()/2;
		double dx = canvasCenterX - textCenterX +40;
		double dy = canvasCenterY - textCenterY ;
		practiceInstruction.translateBy(dx, dy);
		canvas.setAntialiased(true);

		KeyListener key = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					canvas.removeShapes(practiceInstruction);
					Trial trial = allTrials.get(currentTrial);
					trial.displayInstructions();
					currentTrial++;
		        }
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
		canvas.addKeyListener(key);
		canvas.requestFocus();
	}
	
	public void instructionTrial(){ 
		CExtensionalTag practiceInstruction = new CExtensionalTag() { };
		CText text1 = canvas.newText(0, 0, "The next 15 trial will be part of a measured session", Experiment.FONTExp);
		CText text2 = canvas.newText(0, 50, "Identify the shape that is different from all other shapes based on:", Experiment.FONTExp);
		CText text3 = canvas.newText(0, 100, "COLOR and/or MOUVEMENT", Experiment.FONTExp);
		CText text6 = canvas.newText(0, 350, "--> Press Enter key when ready", Experiment.FONTExp.deriveFont(Font.PLAIN, 15));
		text1.addTag(practiceInstruction);
		text2.addTag(practiceInstruction);
		text3.addTag(practiceInstruction);
		text6.addTag(practiceInstruction);
		double textCenterX = practiceInstruction.getCenterX();
		double textCenterY = practiceInstruction.getCenterY();
		double canvasCenterX = canvas.getWidth()/2;
		double canvasCenterY = canvas.getHeight()/2;
		double dx = canvasCenterX - textCenterX +40;
		double dy = canvasCenterY - textCenterY ;
		practiceInstruction.translateBy(dx, dy);
		canvas.setAntialiased(true);

		canvas.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					canvas.removeShapes(practiceInstruction);
					Trial trial = allTrials.get(currentTrial);
					trial.displayInstructions();
					currentTrial++;
		        }
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		canvas.requestFocus();
	}
	
	public void saveLog(String str){
		str = participant + "," + str;
		printWriterLog.print(str);
		printWriterLog.print("\n");
		printWriterLog.flush();
	}
	
	public void end(){
		CExtensionalTag practiceInstruction = new CExtensionalTag() { };
		CText text1 = canvas.newText(0, 0, "THANK YOU", Experiment.FONTExp);
		text1.addTag(practiceInstruction);
		double textCenterX = practiceInstruction.getCenterX();
		double textCenterY = practiceInstruction.getCenterY();
		double canvasCenterX = canvas.getWidth()/2;
		double canvasCenterY = canvas.getHeight()/2;
		double dx = canvasCenterX - textCenterX +40;
		double dy = canvasCenterY - textCenterY ;
		practiceInstruction.translateBy(dx, dy);
		canvas.setAntialiased(true);
	}

	/*******************************/
	/******GETTERS AND SETTERS******/
	/*******************************/

	public Canvas getCanvas() {
		return canvas;
	}
	
	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public int getTrial() {
		return trial;
	}

	public void setTrial(int trial) {
		this.trial = trial;
	}

	public File getDesignFile() {
		return designFile;
	}

	public void setDesignFile(File designFile) {
		this.designFile = designFile;
	}

	/*********************************************/
	/******METHODS TO START AT A GIVEN POINT******/
	/*********************************************/

	/**
	 * @param participantsHeader the name of the column where the participant ID can be found
	 * @return the list of participants found in the experiment file
	 */
	public ArrayList<String> participantsList(String participantsHeader) {
		ArrayList<String> participants = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(designFile));
			String line = br.readLine();
			String[] parts = line.split(",");
			int participantsIndex = 0;
			for (int i = 0; i < parts.length; i++) {
				if(parts[i].compareTo(participantsHeader) == 0) {
					participantsIndex = i;
				}
			}
			line = br.readLine();
			String currentParticipant = "";
			while(line != null) {
				parts = line.split(",");
				String p = parts[participantsIndex];
				if(p.compareTo(currentParticipant) != 0) {
					currentParticipant = p;
					participants.add(p);
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return participants;
	}

	/**
	 * @param blockHeader the name of the column where the block number can be found
	 * @param trialHeader the name of the column where the trial number ID can be found
	 * @return an array of size 2 containing the number of blocks in its first cell and the maximum number of trials per block in its second cell 
	 */
	public int[] trialsCounter(String blockHeader, String trialHeader) {
		int[] res = new int[2];
		res[0] = -1;
		res[1] = -1;
		try {
			BufferedReader br = new BufferedReader(new FileReader(designFile));
			String line = br.readLine();
			String[] parts = line.split(",");
			int blockIndex = 0;
			int trialIndex = 0;
			for (int i = 0; i < parts.length; i++) {
				if(parts[i].compareTo(blockHeader) == 0) {
					blockIndex = i;
				} else if(parts[i].compareTo(trialHeader) == 0) {
					trialIndex = i;
				}
			}
			line = br.readLine();
			while(line != null) {
				parts = line.split(",");
				int b = Integer.parseInt(parts[blockIndex]);
				int t = Integer.parseInt(parts[trialIndex]);
				res[0] = Math.max(res[0], b);
				res[1] = Math.max(res[1], t);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/*******************************/
	/*************MAIN**************/
	/*******************************/

	public static void main(String[] args) {
		final Experiment experiment = new Experiment();

		final JFrame starterFrame = new JFrame("Experiment starter");
		starterFrame.getContentPane().setLayout(new GridLayout(4, 2));

		File experimentFile = new File("experiment.csv");
		experiment.setDesignFile(experimentFile);

		ArrayList<String> participantsList = experiment.participantsList("Participant");
		String[] participantsArray = new String[participantsList.size()];
		int i = 0;
		for (Iterator<String> iterator = participantsList.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			participantsArray[i] = s;
			i++;
		}
		JComboBox<String> comboParticipants = new JComboBox<String>(participantsArray);
		starterFrame.getContentPane().add(new JLabel("Participant ID:"));
		starterFrame.getContentPane().add(comboParticipants);
		comboParticipants.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				String p = (String)cb.getSelectedItem();
				experiment.setParticipant(p);
			}
		});
		experiment.setParticipant(participantsArray[0]);

		int[] trialsCounter = experiment.trialsCounter("Block", "Trial");
		starterFrame.getContentPane().add(new JLabel("Block:"));
		JSpinner spinnerBlock = new JSpinner();
		spinnerBlock.setModel(new SpinnerNumberModel(1, 1, trialsCounter[0], 1));
		starterFrame.getContentPane().add(spinnerBlock);
		spinnerBlock.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Integer b = (Integer)(((JSpinner)e.getSource()).getModel().getValue());
				experiment.setBlock(b);
			}
		});

		starterFrame.getContentPane().add(new JLabel("Trial:"));
		JSpinner spinnerTrial = new JSpinner();
		spinnerTrial.setModel(new SpinnerNumberModel(-1, -1, trialsCounter[1], 1));
		starterFrame.getContentPane().add(spinnerTrial);
		spinnerTrial.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Integer t = (Integer)(((JSpinner)e.getSource()).getModel().getValue());
				experiment.setTrial(t);
			}
		});

		starterFrame.getContentPane().add(new JLabel(""));
		JButton goButton = new JButton("OK");
		starterFrame.getContentPane().add(goButton);
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				experiment.start();
				starterFrame.setVisible(false);
			}
		});

		starterFrame.pack();
		starterFrame.setVisible(true);
		starterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
