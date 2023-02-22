import javax.swing.*;
import java.awt.*;

public class GameArea extends JPanel {

    public GameArea() {
        setBounds(80,0,100,100);
        setBackground(Color.BLUE);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.darkGray);
        g.fillRect(80,0,50, 50);

    }
}
