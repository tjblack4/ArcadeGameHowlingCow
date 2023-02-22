import javax.swing.*;
import java.awt.*;


public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Howling Cow");
        setLayout(new BorderLayout());

        GameCanvas canvas = new GameCanvas();
        add(canvas, BorderLayout.CENTER);

        pack();
    }
}
