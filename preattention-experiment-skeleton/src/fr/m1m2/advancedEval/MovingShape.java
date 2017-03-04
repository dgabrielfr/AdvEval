package fr.m1m2.advancedEval;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.Canvas;

public class MovingShape extends ExpShape{
	
	protected int posX = -1;
	protected int posY = -1;
	protected int borneGH = -1;
	protected int borneDB = -1;
	
	protected int offset = 0;
	protected int preOffset = -1;
	
	protected int lenght = -1;
	protected boolean vertical = false; 
	protected boolean gaucheDroite = true;
	protected CEllipse cellipse = null;
	
	
	public MovingShape(int posX, int posY, int lenght, Color c, boolean b){
		super(c, posX, posY);
		
		this.vertical = b;
		this.posX = posX;
		this.posY = posY;
		if ( vertical ){
			borneGH = posY - lenght;
			borneDB = posY + lenght;
		}else{
			borneGH = posX - lenght;
			borneDB = posX + lenght;
		}
		
	//	int random = new Random().nextInt(2);
	//	if ( random == 0){gaucheDroite = false; }
	}
	
	@Override
	public void animate(){
		if( gaucheDroite ){
			preOffset = offset;
			offset += 2;
	    }else{
	    	preOffset = offset;
	    	offset -= 2;
	    }
	    if ( vertical){
	    	if ( posY + offset > borneDB || posY + offset < borneGH ){
	    		
	    		gaucheDroite = !gaucheDroite;
	    	}
	    }else{
	    	if ( posX + offset > borneDB || posX + offset < borneGH ){
	    		gaucheDroite = !gaucheDroite;
	    	}
	    }
	}
	
	@Override
	public void draw(Canvas canvas){
		if ( cellipse == null){
			circle = new Ellipse2D.Double(posX, posY+offset, Modele.SIZE_SHAPE, Modele.SIZE_SHAPE);
			cellipse = canvas.newEllipse(circle.getX(), circle.getY(), Modele.SIZE_SHAPE, Modele.SIZE_SHAPE);
		}
		if ( vertical){
			
			cellipse.translateTo(circle.getX(), circle.getY()+offset);
			cellipse.setFillPaint(c);
			cellipse.addTag(ellipseTag);
		}else{
			cellipse.translateTo(circle.getX()+offset, circle.getY());
			cellipse.setFillPaint(c);
			cellipse.addTag(ellipseTag);
		}
	}
	
}
