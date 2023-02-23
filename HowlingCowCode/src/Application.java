import java.awt.*;

public final class Application {
    public static void main(final String args[]) {
        new GameFrame();


        /*
        System.out.println("Starting...(thread: " + Thread.currentThread() + ")");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Creating GUI...(thread: " + Thread.currentThread() + ")");
                new GameFrame();
            }
        });
        */

    }
}
