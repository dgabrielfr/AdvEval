package fr.m1m2.advancedEval;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.Canvas;

public class ExpShape {
	protected Ellipse2D circle;
	protected Color c;
	protected CExtensionalTag ellipseTag = new CExtensionalTag() { };
	
	public ExpShape(Color c, int posX, int posY){
		circle = new Ellipse2D.Double(posX-Modele.SIZE_SHAPE/2,posY-Modele.SIZE_SHAPE/2,Modele.SIZE_SHAPE,Modele.SIZE_SHAPE);
		this.c = c;
	}
	
	public void draw(Canvas canvas){
		canvas.removeShapes(ellipseTag);
		CEllipse ce = canvas.newEllipse(circle.getX(), circle.getY(), Modele.SIZE_SHAPE, Modele.SIZE_SHAPE);
		ce.setFillPaint(c);
		ce.addTag(ellipseTag);
	}
	
	public void remove(Canvas canvas){
		canvas.removeShapes(ellipseTag);
	}
	
	public void animate(){
		
	}
}
