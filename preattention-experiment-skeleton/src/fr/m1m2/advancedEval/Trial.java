package fr.m1m2.advancedEval;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;

public class Trial {

	protected Timer timer;
	
	protected boolean practice = false;
	protected int block;
	protected int trial;
	protected String VV;
	protected boolean mirror;
	protected int numberObject;
	
	protected Experiment experiment;
	protected KeyListener displayTrialListener;
	protected KeyListener displayInputListener;
	protected MouseListener clickListener;
	protected Modele modele;
	
	protected CExtensionalTag instructions = new CExtensionalTag() { };
	
	protected long processTime = -1;
	
	public Trial(Experiment experiment, boolean practice, int block, int trial, String VV, int numberObject) {
		this.practice = practice;
		this.block = block;
		this.trial = trial;
		this.VV = VV;
		this.mirror = false;
		this.numberObject = numberObject;
		this.experiment = experiment;
	}
	
	public int getBlock(){ return block;}
	public int getTrial(){ return trial;}
	public String getVV(){ return VV;}
	public void setMirror(boolean b){mirror = b;}
	
	public void displayInstructions() {
		for(KeyListener k: experiment.getCanvas().getListeners(KeyListener.class)){
			experiment.getCanvas().removeKeyListener(k);
		}
		
		this.modele = new Modele(VV,numberObject,experiment.getCanvas(),mirror);
		System.out.println("displayInstr");
		Canvas canvas = experiment.getCanvas();
		CText text1 = canvas.newText(0, 0, "A scene with multiple shapes will get displayed", Experiment.FONTTrial);
		CText text2 = canvas.newText(0, 50, "Identify the shape that is different from all other shapes", Experiment.FONTTrial);
		CText text3 = canvas.newText(0, 100, "    1. Press Space bar", Experiment.FONTTrial);
		CText text4 = canvas.newText(0, 150, "    2. Click on the identified shape", Experiment.FONTTrial);
		CText text5 = canvas.newText(0, 200, "Do it AS FAST AND AS ACCURATELY AS POSSIBLE", Experiment.FONTTrial);
		CText text6 = canvas.newText(0, 350, "--> Press Enter key when ready", Experiment.FONTTrial.deriveFont(Font.PLAIN, 15));
		text1.addTag(instructions);
		text2.addTag(instructions);
		text3.addTag(instructions);
		text4.addTag(instructions);
		text5.addTag(instructions);
		text6.addTag(instructions);
		double textCenterX = instructions.getCenterX();
		double textCenterY = instructions.getCenterY();
		double canvasCenterX = canvas.getWidth()/2;
		double canvasCenterY = canvas.getHeight()/2;
		double dx = canvasCenterX - textCenterX;
		double dy = canvasCenterY - textCenterY;
		instructions.translateBy(dx, dy);
		canvas.setAntialiased(true);

		// TODO install keyboard listener to handle user input
		canvas.requestFocus();
		
		displayTrialListener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					displayTrial();
					processTime = System.currentTimeMillis();
		        }
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
		canvas.addKeyListener(displayTrialListener);
	}
	
	public void displayTrial(){
		Canvas canvas = experiment.getCanvas();
		canvas.removeShapes(instructions);
		
		canvas.removeKeyListener(displayTrialListener);
		
		System.out.println("displayTrial");
		modele.draw1(canvas);
		
		if ( VV.contains("VV2") ){
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
			
			  @Override
			  public void run() {				
				  modele.animate();
				  modele.draw1(canvas);
				  Toolkit.getDefaultToolkit().sync();
			  }
			}, 30, 30);
		}
		
		displayInputListener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SPACE){
					displayInputWaiting();
					if (timer != null){
						timer.cancel();
						timer = null;
					}
					processTime = System.currentTimeMillis() - processTime;
		        }
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
		canvas.addKeyListener(displayInputListener);
	}
	
	public void displayInputWaiting(){
		Canvas canvas = experiment.getCanvas();
		modele.removedraw1(canvas);
		canvas.removeKeyListener(displayInputListener);
		System.out.println("displayInputWait");
		
		modele.draw2(canvas);
		
		clickListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int res = modele.tryIntersect(e);
				if (res == 1){
					next_Trial();
				}else if ( res == -1){
					redoTrial();
				}
			}
		};
		canvas.addMouseListener(clickListener);
	}
	
	public void redoTrial(){
		Canvas canvas = experiment.getCanvas();
		canvas.removeMouseListener(clickListener);
		modele.removedraw2(canvas);
		displayInstructions();
	}
	
	public void next_Trial(){
		Canvas canvas = experiment.getCanvas();
		canvas.removeMouseListener(clickListener);
		modele.removedraw2(canvas);
		
		System.out.println("nextTrial trial");
		String str = String.valueOf(practice)+ "," + block+ "," + trial+ "," + VV+ "," + numberObject+ "," + processTime ;
		System.out.println(str);
		experiment.saveLog(str);
		experiment.nextTrial();
	}
	
	
}
