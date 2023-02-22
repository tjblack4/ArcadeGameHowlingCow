import javax.swing.*;
import java.awt.*;

public final class Application {
    public static void main(final String args[]) {
        System.out.println("Starting...(thread: " + Thread.currentThread() + ")");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Creating GUI...(thread: " + Thread.currentThread() + ")");
                final GameFrame frame = new GameFrame();
                frame.setSize(900,500);
                frame.setVisible(true);
            }
        });
    }
}
