import java.awt.*;
import java.io.IOException;

public final class Application {
    public static void main(final String args[]) throws IOException {
        new GameFrame();


        /*
        System.out.println("Starting...(thread: " + Thread.currentThread() + ")");
        Even
        tQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Creating GUI...(thread: " + Thread.currentThread() + ")");
                new GameFrame();
            }
        });
        */

    }
}
