import javax.swing.*;
import java.awt.*;


public class GameFrame extends JFrame {
    public GameFrame() {
        this.add(new GameArea()); //allows game area to be used
        this.setTitle("Howling Cow"); //sets title of application window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null); //appears in center of computer screen
    }
}
