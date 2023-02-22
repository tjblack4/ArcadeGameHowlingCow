import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JComponent {

    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,900,500);
        g.setColor(Color.BLUE);
        g.drawRect(0,0,100,100);

    }
}
