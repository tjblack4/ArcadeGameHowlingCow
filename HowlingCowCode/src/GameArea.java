import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameArea extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600; //up for changes
    static final int SCREEN_HEIGHT = 600; //up for changes
    static final int UNIT_SIZE = 25; //size of square, in pixels
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // unknown
    static final int DELAY = 100; // > #, slower game
    final int x[] = new int[GAME_UNITS]; //holds x locations of all cartons
    final int y[] = new int[GAME_UNITS]; //holds y locations of all cartons
    int boardMap[] = {}; //fill in with values
    final int FARMER_POSX[] = new int[4]; //holds x locations of all farmers
    final int FARMER_POSY[] = new int[4]; //holds y locations of all farmers
    int myPosx; //holds player x position
    int myPosY; //holds player y position
    int dotsEaten; //holds # of dots eaten, for score calculation
    char farmerDirection[] = new char[4]; // holds directions of each of the farmers
    char direction = 'R'; // holds direction of the cow
    boolean running = false; // holds value for if game is running
    Timer timer;
    Random random;
    long pauseBeginning;
    long pauseEnding;
    long start;
    boolean isPaused = false;

    GameArea() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start = System.currentTimeMillis();
        startGame();
    }

    public void startGame() {
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            //creates grid pattern of game screen
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(myPosx, myPosY, UNIT_SIZE, UNIT_SIZE);
                } else {
                    //draw farmer 1,2, or 3
                }
            }
            //write score code
        }
        else if (isPaused) {
            
        } else {
            gameOver(g);
        }
    }
    public void move() {
        switch (direction) {
            case 'U':
                if(myPosY != 0)
                    myPosY = myPosY - UNIT_SIZE;
                break;
            case 'D':
                if(myPosY != SCREEN_HEIGHT - 25)
                    myPosY = myPosY + UNIT_SIZE;
                break;
            case 'L':
                if(myPosx != 0)
                    myPosx = myPosx - UNIT_SIZE;
                break;
            case 'R':
                if(myPosx != SCREEN_WIDTH - 25)
                    myPosx = myPosx + UNIT_SIZE;
                break;

        }
    }

    public void checkCarton() {
        /*
        for (int i=0; i<=x.length; i++) {
            if ((x[i] == myPosx) && (y[i] == myPosY) && (boardMap[i] != 0)) {
               //code for collecting a carton
               //should include the removal of the image
               //should include an addition to point calculation
               //should include the changing of board map
            }
        }
        */
    }

    public void checkCollisions() {
        //write method to check if farmer has hit the cow

        //check if cow has touched left border
        if (myPosx < 0+2*UNIT_SIZE) {
            //dont let cow move
        }

        //check if cow has touched right border
        if (myPosx > SCREEN_WIDTH-2*UNIT_SIZE) {
            //dont let cow move
        }

        //check if cow has touched top border
        if (myPosY < 0+2*UNIT_SIZE) {
            //dont let cow move
        }

        //check if cow has touched bottom border
        if (myPosY > SCREEN_HEIGHT-2*UNIT_SIZE) {
            //dont let cow move
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        long end = System.currentTimeMillis();
        //g.setColor(Color.green);
        //g.setFont(new Font("Ink Free", Font.BOLD, 75));
        //FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        //g.drawString("Time: " + (double) (end+start) /1000, (SCREEN_WIDTH - scoreMetrics.stringWidth("Time:" + (end+start)))/2, SCREEN_HEIGHT/2 - 100);
        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    public void pauseGame() {
        pauseBeginning = System.currentTimeMillis();
        running = false;
        isPaused = true;
    }

    public void unpauseGame() {
        isPaused = false;
        pauseEnding = System.currentTimeMillis();
        start = start - (pauseEnding - pauseBeginning);
        running = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkCarton();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed (KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_1:
                    if(isPaused == false)
                        pauseGame();
                    else
                        unpauseGame();
                    break;
            }
        }
    }
}
