package fr.m1m2.advancedEval;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Random;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.Canvas;

/*	COUPLE VV1VV2 = 0->HORIZONTAL BLACK
 * 					1->HORIZONTAL RED
 * 					2->VERTICAL BLACK
 * 					3->VERTICAL RED
 * 
 */

public class Modele {
	
	public static int LENGHT_SIZE = 50;
	public static int SIZE_SHAPE = 50;
	public static int GAP_CIRCLE = 150;
	ExpShape[][] listShape;
	CEllipse[][] listClick;
	Point[][] listCenter;
	int random = -1;
	int number = -1;
	Random randomGenerator = new Random();
	
	protected CExtensionalTag ellipseTag = new CExtensionalTag() { };
	
	public Modele(String str, int number, Canvas canvas, boolean mirror){
		this.number = number;
		int offset = 0;
		if(number == 9){
			random = randomGenerator.nextInt(9);
			listShape = new ExpShape[3][3];
			listClick = new CEllipse[3][3];
			listCenter = new Point[3][3];
		}else if(number == 16){
			random = randomGenerator.nextInt(16);
			listShape = new ExpShape[4][4];
			listClick = new CEllipse[4][4];
			listCenter = new Point[4][4];
			offset = 50;
		}else if(number == 36){
			random = randomGenerator.nextInt(36);
			listShape = new ExpShape[6][6];	
			listClick = new CEllipse[6][6];	
			listCenter = new Point[6][6];
		}
		
		int centerX = canvas.getWidth()/2;
		int centerY = canvas.getHeight()/2;
		centerX = centerX- (GAP_CIRCLE*((int)Math.sqrt(number)/2)) - offset;
		centerY = centerY- (GAP_CIRCLE*((int)Math.sqrt(number)/2)) - offset;
		
		for(int i=0; i < (int)Math.sqrt(number); i++){
			for(int j=0; j < (int)Math.sqrt(number); j++){
				listCenter[i][j] = new Point( (centerX + j*GAP_CIRCLE)-Modele.SIZE_SHAPE/2 , centerY-Modele.SIZE_SHAPE/2);
			}
			centerY += GAP_CIRCLE;
		}
		
		instanciateShape(str, number, mirror);
	}

	public void instanciateShape(String str, int number, boolean mirror){
		System.out.println("MIRORRRRRRRRRRRRRRRRRRRRRRRRRRRR "+ mirror);
		if( str.contains("VV1VV2")){
			int randMerge = randomGenerator.nextInt(4);
			int count1 = 2 + randomGenerator.nextInt(number-6); // 2 > 4
			int count2 = 2 + randomGenerator.nextInt(number - count1 -4);// 2 > 2
			int count3 = number- 1- count1 - count2; // 4 > 2
			count1 = (number-1)/3;
			count2 = count1;
			count3 = number - 1 - count1 - count2;
			for(int i=0; i < (int)Math.sqrt(number); i++){
				for(int j=0; j < (int)Math.sqrt(number); j++){
					if( ((int)Math.sqrt(number)*i)+j == random){
						if ( randMerge == 0){
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, false);
						}else if(randMerge == 1){
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, false);
						}else if(randMerge == 2){
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, true);
						}else{
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, true);
						}
					}else{
						int randOther = randomGenerator.nextInt(3);
						if ( randMerge == 0){
							while (true){
								if ( randOther == 0){
									if(count1 == 0){ randOther = 1;}
									else{
										count1--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, true);
										break;
									}
								}if ( randOther == 1){
									if(count2 == 0){ randOther = 2;}
									else{
										count2--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, false);
										break;
									}
								}if ( randOther == 2){
									if(count3 == 0){ randOther = 0;}
									else{
										count3--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, true);
										break;
									}
								}
							}
						}else if(randMerge == 1){
							while (true){
								if ( randOther == 0){
									if(count1 == 0){ randOther = 1;}
									else{
										count1--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, true);
										break;
									}
								}if ( randOther == 1){
									if(count2 == 0){ randOther = 2;}
									else{
										count2--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, true);
										break;
									}
								}if ( randOther == 2){
									if(count3 == 0){ randOther = 0;}
									else{
										count3--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, false);
										break;
									}
								}
							}
						}else if(randMerge == 2){
							while (true){
								if ( randOther == 0){
									if(count1 == 0){ randOther = 1;}
									else{
										count1--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, false);
										break;
									}
								}if ( randOther == 1){
									if(count2 == 0){ randOther = 2;}
									else{
										count2--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, true);
										break;
									}
								}if ( randOther == 2){
									if(count3 == 0){ randOther = 0;}
									else{
										count3--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, false);
										break;
									}
								}
							}
						}else{
							while(true){
								if ( randOther == 0){
									if(count1 == 0){ randOther = 1;}
									else{
										count1--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, false);
										break;
									}
								}
								if ( randOther == 1){
									if(count2 == 0){ randOther = 2;}
									else{
										count2--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, true);
										break;
									}
								}if ( randOther == 2){
									if(count3 == 0){ randOther = 0;}
									else{
										count3--;
										listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.BLACK, false);
										break;
									}
								}
							}
						}
					}				
				}
			}
			return;
		}else if( str.contains("VV1")){
			for(int i=0; i < (int)Math.sqrt(number); i++){
				for(int j=0; j < (int)Math.sqrt(number); j++){
					if (!mirror){
						if( ((int)Math.sqrt(number)*i)+j == random){
							listShape[i][j] = new ExpShape(Color.RED,(int)listCenter[i][j].getX(),(int)listCenter[i][j].getY());
						}else{
							listShape[i][j] = new ExpShape(Color.BLACK,(int)listCenter[i][j].getX(),(int)listCenter[i][j].getY());
						}
					}else{
						if( ((int)Math.sqrt(number)*i)+j == random){
							listShape[i][j] = new ExpShape(Color.BLACK,(int)listCenter[i][j].getX(),(int)listCenter[i][j].getY());
						}else{
							listShape[i][j] = new ExpShape(Color.RED,(int)listCenter[i][j].getX(),(int)listCenter[i][j].getY());
						}
					}
				}
			}
		}else if( str.contains("VV2")){
			for(int i=0; i < (int)Math.sqrt(number); i++){
				for(int j=0; j < (int)Math.sqrt(number); j++){
					if (!mirror){
						if( ((int)Math.sqrt(number)*i)+j == random){
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, true );
						}else{
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, false);
						}
					}else{
						if( ((int)Math.sqrt(number)*i)+j == random){
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, false );
						}else{
							listShape[i][j] = new MovingShape((int)listCenter[i][j].getX(),(int)listCenter[i][j].getY(), LENGHT_SIZE, Color.RED, true);
						}
					}
				}
			}
		}
	}
	
	public void draw1(Canvas c){
		for(int i=0; i < (int)Math.sqrt(number); i++){
			for(int j=0; j < (int)Math.sqrt(number); j++){
				listShape[i][j].draw(c);		
			}
		}
	}
	
	public void removedraw1(Canvas c){
		for(int i=0; i < (int)Math.sqrt(number); i++){
			for(int j=0; j < (int)Math.sqrt(number); j++){
				listShape[i][j].remove(c);		
			}
		}
	}
	
	public void draw2(Canvas c){
		for(int i=0; i < (int)Math.sqrt(number); i++){
			for(int j=0; j < (int)Math.sqrt(number); j++){
				CEllipse ce = c.newEllipse((int)listCenter[i][j].getX()-Modele.SIZE_SHAPE/2,(int)listCenter[i][j].getY()-Modele.SIZE_SHAPE/2,Modele.SIZE_SHAPE,Modele.SIZE_SHAPE);
				ce.addTag(ellipseTag);
				ce.setFillPaint(Color.BLACK);
				listClick[i][j] = ce;
			}
		}
	
	}
	
	public void removedraw2(Canvas c){
		for(int i=0; i < (int)Math.sqrt(number); i++){
			for(int j=0; j < (int)Math.sqrt(number); j++){
				c.removeShapes(ellipseTag);
			}
		}
	}
	
	public int tryIntersect(MouseEvent e){
		for(int i=0; i < (int)Math.sqrt(number); i++){
			for(int j=0; j < (int)Math.sqrt(number); j++){
				if ( listClick[i][j].pick(new Point2D.Double(e.getX(),e.getY()), 5) != null ){
					if ( (i*(int)Math.sqrt(number))+j == random ){ 
						return 1;
					}
					return -1;
				}
			}
		}
		return 0;
	}

	public void animate() {
		for(int i=0; i < (int)Math.sqrt(number); i++){
			for(int j=0; j < (int)Math.sqrt(number); j++){
				listShape[i][j].animate();		
			}
		}
	}
}
