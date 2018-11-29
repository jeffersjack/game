package game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by acous on 8/23/2016.
 */
//public class Renderer {
public class Renderer extends JPanel{
	
    private static final long serialVersionUID = 1L;

    protected void paintComponent(Graphics g) {
      //  Flappy.instance.repaint(g);	since instance is public, do you need the Flappy.?
    	Flappy.instance.repaint(g);
    }

}
