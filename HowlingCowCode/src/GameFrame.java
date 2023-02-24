import javax.swing.*;
import java.io.IOException;


public class GameFrame extends JFrame {
    public GameFrame() throws IOException {
        this.add(new GameArea()); //allows game area to be used
        this.setTitle("Howling Cow"); //sets title of application window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //stops program when application is closed
        this.setResizable(false); //if false, application cannot be resized
        this.pack(); //packs all elements into the smallest space
        this.setVisible(true); //sets visibility application
        this.setLocationRelativeTo(null); //appears in center of computer screen
    }
}
