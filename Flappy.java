package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/*
 * Created by Aaron on 8/23/2016.
 * Modified by:
 * Jerrod Pope and Jack Jeffers 11/20/2018
 * 		This is a modified flappy bird-style game involving Brett Kavanaugh
 * 	Any resemblance to persons, living or dead, is entirely coincidental or is intended purely as a satire,
 *  parody or spoof of such persons and is not intended to communicate any true or factual information about that person.
 *  This game is intended for a mature and discerning audience.
 */

public class Flappy implements ActionListener, KeyListener{
	public static Flappy instance;	//game instance
    
    private final int WIDTH = 1000;		//Can't these two be moved outside the class?
    private final int HEIGHT = 700;	
    private int score, gameSpeed, objectCount, beerCollected;
    
    private int beerHeight = 70, beerWidth = 38;
    
    private Renderer renderer;		//also confused by this
    private Rectangle brett;

    private ArrayList<Rectangle> cloud;
    private Random rand;

    private boolean start = false, 
    				gameover = false,
    				isDrunk = false;

    private int gameTick, 
    			drunkCooldown = 0,
    			highscore = 0;

    public Flappy() {	//constructor

        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();
        cloud = new ArrayList<Rectangle>();
        brett = new Rectangle(200, 220, 20, 20);
        
        jFrame.setTitle("Flappy Brett");
        jFrame.add(renderer);
        
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.addKeyListener(this);
        
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(int i = 0; i < 5; i++) {
        	addObject();
        }

        timer.start();
    }

    void repaint(Graphics g) {//this 'repaints' the graphics to start a new game after a gameover, i'm considering renamming this to restarter
        g.setColor(Color.black);			//resets the background to black
        g.fillRect(0,0, WIDTH, HEIGHT);		
        
        g.setColor(Color.blue);
        g.fillRect(0, HEIGHT - 150, WIDTH, 150);	//resets the 'ocean' at the bottom
        
        g.setColor(Color.green);	//creates brett
        g.fillRect(brett.x, brett.y, brett.width, brett.height);
        
        g.setColor(Color.WHITE);	
        g.setFont(new Font("Arial", 1 ,32));	
        g.drawString("Score: " + score , 15, 35);	//draw score counter
        g.drawString("Beers enjoyed: " + beerCollected , 15, 67);	//draws beerCollected

        if (brett.y >=  HEIGHT - 100) {
            gameover = true;
        }
        
        if((brett.y ) < 0)
        	brett.y = 0;
        
        for (Rectangle rect : cloud) {	//draws and fills clouds	
        	if(rect.width == beerWidth) {
        		g.setColor(Color.yellow);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
        	} else {
        		g.setColor(Color.white);
        		g.fillRect(rect.x, rect.y, rect.width, rect.height);
        	} 
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1, 24));
        g.drawString("Drunk Cooldown: " + drunkCooldown, 15, 95);

        g.drawString("Speed: " + gameSpeed , 15, 122);

        g.setColor(Color.WHITE);
        if (!start) {
        	g.setFont(new Font("Arial", 1, 72));
        	g.drawString("Flappy Brett", WIDTH/4, 200);
        	
        	g.setFont(new Font("Arial", 1, 42));
        	g.drawString("Press space to jump", WIDTH/4, 300);
        	g.drawString("Press left arrow to slow down", WIDTH/6, 370);
        	g.drawString("Press right arrow to speed up", WIDTH/6, 420);
        	
        	g.setFont(new Font("Arial", 1, 24));  
        	g.drawString("10 points for passing a cloud", 100, 470);
        	g.drawString("20 points for collecting a beer", 470, 470);
        	
        	gameSpeed = 10;
        }
        else if (gameover) {
        	g.setFont(new Font("Arial", 1, 90));
            g.drawString("Game Over!", WIDTH/4, 230);
            
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("Score: " + score, 300, 300);
            
            g.setFont(new Font("Arial", 1, 50));
            if (score > highscore) {
            	highscore = score;
            }
            g.drawString("Highscore: " + highscore, 300, 370);
            
            objectCount = 0;
        }
    }

    private void addCloud() {	//adds clouds
        if (cloud.isEmpty() ) {	//if true, the function adds a new cloud at a random height
            cloud.add(new Rectangle(WIDTH + cloud.size() * 300, rand.nextInt(HEIGHT-200), 80, 100));
        } else {	            //spawning a cloud with proper spacing outside of frame
            cloud.add(new Rectangle(cloud.get(cloud.size() - 1).x + 300, rand.nextInt(HEIGHT-200), 80, 100));
        }
    }
    
    private void addBeer() {    //adds a consumable beer instead of a cloud
    	cloud.add(new Rectangle(cloud.get(cloud.size() - 1).x + 300, rand.nextInt(HEIGHT-170), beerWidth, beerHeight));
    }
  
    private void addObject() {
    	int beerRandom = rand.nextInt(7) + 1;
    	if(objectCount == 0) {          //initial cloud
    		addCloud();
    	}else if(objectCount % beerRandom != 0) {//recurring clouds
    		addCloud();
    	}else {
    		addBeer();                  //beer instead of cloud
    	}
    	objectCount++;
    }
    
    private void checkDrunk() { 
    	
    	if(isDrunk == true) {
    		drunkCooldown += 75;//set this to a smaller amount for binge drinking
    	} else if(beerCollected >= 6) {
    		drunkCooldown += rand.nextInt(400) +200;  //goes into drunk mode if6+ beers collected
    		isDrunk = true;
    	}

    	
    	//feel free to add graphical function change here	
    }
    
    private void flap() {

        if (gameover) {
        	gameSpeed = 10;
            score = 0;
            beerCollected = 0;
            drunkCooldown = 0;
            isDrunk = false;
            brett = new Rectangle(200, 300, 20, 20);
            cloud.clear();
            addCloud();
            
            for(int i = 0; i < 5 ; i++)
            	addObject();
       
            gameover = false;
        }
        
        if (!start) {
            start = true;
        }
        else if (!gameover) {
            brett.y -= 70;
            gameTick = 0;
        }

    }

    public static void main(String args[]) {	//ok, this has me confused
        instance = new Flappy();					//this creates a game instance to run the constructor?
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Space");
        if (start) {
            for (int i = 0; i < cloud.size(); i++) {	//cloud movement
                Rectangle rect = cloud.get(i);
                rect.x -= gameSpeed;
                
                if(rect.x <= brett.x  && rect.x >= brett.x - gameSpeed + 1 && !gameover && rect.width != beerWidth){	//score updater for cloud passage
                	score+= 10;	
                } 
            }
    
            for (int i = 0; i < cloud.size(); i++) {	//removes a cloud after it has exited the frame and creates a new one
                Rectangle rect = cloud.get(i);

                if (rect.x + rect.width < 0) {
                    cloud.remove(rect);
                    addObject();
                }
            }
            
            for (int i = 0; i < cloud.size(); i++) {	//cloud/beer hit detection
                Rectangle rect = cloud.get(i);
                
                if (rect.intersects(brett) && rect.width == beerWidth) {//adds score for hitting beer and removes beer
            		score+= 20;
            		cloud.remove(rect);
            		addObject();      		
            		beerCollected++;
            		checkDrunk();
            		
            	} else if (rect.intersects(brett)) {    //lose if cloud was hit
                    gameover = true;
                    brett.x -= gameSpeed;
                }  
            }

            
            if (isDrunk == true) {
            	if(drunkCooldown % 12 == 0)
            		gameSpeed = rand.nextInt(12) + 7;	//if drunk, game speed randomly speeds up and slows down
            	
            	drunkCooldown--;
            	
            	if(drunkCooldown == 0) {                //resets drunkenness when cooldown ends
            		isDrunk = false;
            		gameSpeed = 10;
            		beerCollected = 0;
            	}
            }
 
            gameTick ++;
          
           if (gameTick %2 == 0 && brett.y < HEIGHT - 100)  //this is for letting the brett fall and setting floor height
                brett.y += gameTick;

        }

        renderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) 	//increase game gameSpeed by 1 pixel per tick
    		gameSpeed = (gameSpeed > 1) ?  gameSpeed -= 1 : 1;

    	if (e.getKeyCode() == KeyEvent.VK_RIGHT)	//decrease game speed by 1 pixel per tick
    		gameSpeed++;
    	
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {  //jumps when space bar is pressed
            flap();
        }
        
    }
}
