package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.*;

public class Listener implements ActionListener, KeyListener {
	    static int gameTick, 
		drunkCooldown = 0,
		highscore = 0,
		score;
	    Random rand = new Random();
	
	Listener()
	{

        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        
        jFrame.setTitle("Flappy Brett");
        jFrame.add(Flappy.instance.renderer);
        
        jFrame.setSize(Flappy.instance.WIDTH, Flappy.instance.HEIGHT);
        jFrame.addKeyListener((KeyListener) this);
        
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        timer.start();
        
	}
	
	static void repaint(Graphics g) {//this 'repaints' the graphics to start a new game after a gameover, i'm considering renamming this to restarter
        g.setColor(Color.black);			//resets the background to black
        g.fillRect(0,0, Flappy.instance.WIDTH, Flappy.instance.HEIGHT);		
        
        g.setColor(Color.blue);
        g.fillRect(0, Flappy.instance.HEIGHT - 150, Flappy.instance.WIDTH, 150);	//resets the 'ocean' at the bottom
        
        g.setColor(Color.green);	//creates brett
        g.fillRect(Flappy.instance.brett.x, Flappy.instance.brett.y, Flappy.instance.brett.width, Flappy.instance.brett.height);
        
        g.setColor(Color.WHITE);	
        g.setFont(new Font("Arial", 1 ,32));	
        g.drawString("Score: " + score , 15, 35);	//draw score counter
        g.drawString("Beers enjoyed: " + Flappy.instance.beerCollected , 15, 67);	//draws beerCollected

        if (Flappy.instance.brett.y >=  Flappy.instance.HEIGHT - 100) {
            Flappy.instance.gameover = true;
        }
        
        if((Flappy.instance.brett.y ) < 0)
        	Flappy.instance.brett.y = 0;
        
        for (Rectangle rect : Flappy.instance.cloud) {	//draws and fills clouds	
        	if(rect.width == Flappy.instance.beerWidth) {
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

        g.drawString("Speed: " + Flappy.instance.gameSpeed , 15, 122);

        g.setColor(Color.WHITE);
        if (Flappy.instance.start != true) {
        	g.setFont(new Font("Arial", 1, 72));
        	g.drawString("Flappy Brett", Flappy.instance.WIDTH/4, 200);
        	
        	g.setFont(new Font("Arial", 1, 42));
        	g.drawString("Press space to jump", Flappy.instance.WIDTH/4, 300);
        	g.drawString("Press left arrow to slow down", Flappy.instance.WIDTH/6, 370);
        	g.drawString("Press right arrow to speed up", Flappy.instance.WIDTH/6, 420);
        	
        	g.setFont(new Font("Arial", 1, 24));  
        	g.drawString("10 points for passing a cloud", 100, 470);
        	g.drawString("20 points for collecting a beer", 470, 470);
        	
        	Flappy.instance.gameSpeed = 10;
        }
        else if (Flappy.instance.gameover) {
        	g.setFont(new Font("Arial", 1, 90));
            g.drawString("Game Over!", Flappy.instance.WIDTH/4, 230);
            
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("Score: " + score, 300, 300);
            
            g.setFont(new Font("Arial", 1, 50));
            if (score > highscore) {
            	highscore = score;
            }
            g.drawString("Highscore: " + highscore, 300, 370);
            
            Flappy.instance.objectCount = 0;
        }
    }
	
	@Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) 	//increase game gameSpeed by 1 pixel per tick
    		//gameSpeed = (gameSpeed > 1) ?  gameSpeed -= 1 : 1;
    		Flappy.instance.alterSpeed(-1);

    	if (e.getKeyCode() == KeyEvent.VK_RIGHT)	//decrease game speed by 1 pixel per tick
    		Flappy.instance.alterSpeed(1);
    	
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {  //jumps when space bar is pressed
            Flappy.instance.flap();
        }
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (Flappy.instance.start) {
            for (int i = 0; i < Flappy.instance.cloud.size(); i++) {	//cloud movement
                Rectangle rect = Flappy.instance.cloud.get(i);
                rect.x -= Flappy.instance.gameSpeed;
                
                if(rect.x <= Flappy.instance.brett.x  && rect.x >= Flappy.instance.brett.x - Flappy.instance.gameSpeed + 1 && !Flappy.instance.gameover && rect.width != Flappy.instance.beerWidth) {	//score updater for cloud passage
                	score+= 10;	
                } 
            }
    
            for (int i = 0; i < Flappy.instance.cloud.size(); i++) {	//removes a cloud after it has exited the frame and creates a new one
                Rectangle rect = Flappy.instance.cloud.get(i);

                if (rect.x + rect.width < 0) {
                	Flappy.instance.cloud.remove(rect);
                	Flappy.instance.addObject();
                }
            }
            
            for (int i = 0; i < Flappy.instance.cloud.size(); i++) {	//cloud/beer hit detection
                Rectangle rect = Flappy.instance.cloud.get(i);
                
                if (rect.intersects(Flappy.instance.brett) && rect.width == Flappy.instance.beerWidth) {//adds score for hitting beer and removes beer
            		score+= 20;
            		Flappy.instance.cloud.remove(rect);
            		Flappy.instance.addObject();      		
            		Flappy.instance.beerCollected++;
            		Flappy.instance.checkDrunk();
            		
            	} else if (rect.intersects(Flappy.instance.brett)) {    //lose if cloud was hit
            		Flappy.instance.gameover = true;
            		Flappy.instance.brett.x -= Flappy.instance.gameSpeed;
                }  
            }

            
            if (Flappy.instance.isDrunk == true) {
            	if(drunkCooldown % 12 == 0)
            		Flappy.instance.gameSpeed = rand.nextInt(12) + 7;	//if drunk, game speed randomly speeds up and slows down
            	
            	drunkCooldown--;
            	
            	if(drunkCooldown == 0) {                //resets drunkenness when cooldown ends
            		Flappy.instance.isDrunk = false;
            		Flappy.instance.gameSpeed = 10;
            		Flappy.instance.beerCollected = 0;
            	}
            }
 
            gameTick ++;
          
           if (gameTick %2 == 0 && Flappy.instance.brett.y < Flappy.instance.HEIGHT - 100)  //this is for letting the brett fall and setting floor height
        	   Flappy.instance.brett.y += gameTick;

        }

        Flappy.renderer.repaint();
    }
		
}