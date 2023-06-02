import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;


public class GameFrame extends JFrame {
    public GameFrame() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int width = (int) screenSize.getWidth();
    	int height = (int) screenSize.getHeight();
    	this.setSize(width, height);
        this.add(new GameArea()); //allows game area to be used
        this.setTitle("Howling Cow"); //sets title of application window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //stops program when application is closed
        this.setResizable(false); //if false, application cannot be resized
        this.setUndecorated(true);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.BLACK);
        this.setVisible(true); //sets visibility application
    }
}
