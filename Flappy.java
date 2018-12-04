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
 * 
 * 		This is a modified flappy bird-style game involving Brett Kavanaugh
 * 	Any resemblance to persons, living or dead, is entirely coincidental or is intended purely as a satire,
 *  parody or spoof of such persons and is not intended to communicate any true or factual information about that person.
 *  This game is intended for a mature and discerning audience.
 */

public class Flappy {
	public static Flappy instance;	//game instance
	static Listener listens;
    
    public final int WIDTH = 1000, HEIGHT = 700;	
    static int objectCount, beerCollected;
    int gameSpeed;
    
    int beerHeight = 70, beerWidth = 38;
    
    static Renderer renderer;		//also confused by this
    Rectangle brett;

    ArrayList<Rectangle> cloud;
    private Random rand;

    boolean start = false;

	boolean gameover = false;

	boolean isDrunk = false;



    public Flappy() {	//constructor
    	
    	renderer = new Renderer();
        rand = new Random();
        cloud = new ArrayList<Rectangle>();
        brett = new Rectangle(200, 220, 20, 20);        

        for(int i = 0; i < 5; i++) {
        	addObject();
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
  
     void addObject() {
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
    
    void checkDrunk() { 
    	
    	if(isDrunk == true) {
    		Listener.drunkCooldown += 75;//set this to a smaller amount for binge drinking
    	} else if(beerCollected >= 6) {
    		Listener.drunkCooldown += rand.nextInt(400) +200;  //goes into drunk mode if6+ beers collected
    		isDrunk = true;
    	}

    	
    	//feel free to add graphical function change here	
    }
    
    public void alterSpeed(int d) 
    {
    	gameSpeed = gameSpeed + d;
    	
    	if(gameSpeed < 1)
    		gameSpeed = 1;
    	
    }
    
    public void flap() {

        if (gameover) {
        	gameSpeed = 10;
            Listener.score = 0;
            beerCollected = 0;
            Listener.drunkCooldown = 0;
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
            Listener.gameTick = 0;
        }

    }

    public static void main(String args[]) {	//ok, this has me confused
        instance = new Flappy();					//this creates a game instance to run the constructor?
        listens = new Listener();
    }
} 