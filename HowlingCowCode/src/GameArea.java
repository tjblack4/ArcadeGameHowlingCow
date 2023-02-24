import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GameArea extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 700; //up for changes
    static final int SCREEN_HEIGHT = 900; //up for changes
    static final int UNIT_SIZE = 25; //size of square, in pixels
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // unknown
    static final int DELAY = 125; // > #, slower game
    final int x[] = new int[GAME_UNITS]; //holds x locations of all cartons
    final int y[] = new int[GAME_UNITS]; //holds y locations of all cartons
    int boardMap[][] = new int[36][28]; //fill in with values
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
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {

                boardMap[i][j] = 0;
            }
        }
        System.out.println(boardMap[35][27]);
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
        for (int j = 0; j<4; j++) {
            FARMER_POSX[j] = UNIT_SIZE * random.nextInt(SCREEN_WIDTH / UNIT_SIZE);
            FARMER_POSY[j] = UNIT_SIZE * random.nextInt(SCREEN_HEIGHT / UNIT_SIZE);
        }
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

            for (int i = 0; i < 5; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(myPosx, myPosY, UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.red);
                    g.fillRect(FARMER_POSX[i-1], FARMER_POSY[i-1], UNIT_SIZE, UNIT_SIZE);

                    //draw farmer 1,2,3,or 4
                }
            }

            for (int row = 0; row < 28; row++) {
                for (int col = 0; col < 36; col++) {
                    if (col == 3 || col == 33) {
                        g.setColor(Color.BLUE);
                        g.fillRect(row * UNIT_SIZE, col * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                    if ((row == 0 || row == 27) && ((col > 3 && col < 13) || (col == 15) || (col == 17) || (col > 19 && col < 33))) {
                        g.setColor(Color.BLUE);
                        g.fillRect(row * UNIT_SIZE, col * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                    if ((row < 6 || row > 21) && (col == 12 || col == 15 || col == 17 || col == 20)) {
                        g.setColor(Color.BLUE);
                        g.fillRect(row * UNIT_SIZE, col * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                    if ((row == 5 || row == 22) && (col == 13 || col == 14 || col == 18 || col == 19)) {
                        g.setColor(Color.BLUE);
                        g.fillRect(row * UNIT_SIZE, col * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                    if (((row > 1 && row < 6) || (row > 21 && row < 26)) && (col > 4 && col < 8)) {
                        g.setColor(Color.BLUE);
                        g.fillRect(row * UNIT_SIZE, col * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
            //write score code
        }
        else if (isPaused) {

        } else {
            gameOver(g);
        }
    }

    /*
    Method: moveFarmer1
    Description of Farmer: Highly Agressive. Takes the shortest possible route to the Cow at all times.
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer1() {
        //if farmer 1 is @ intersection
        //do distance formula for all choices
        //move toward shortest choice

        //otherwise, follow open pathing
    }

    /*
    Method: moveFarmer2
    Description of Farmer: Low Agression. Will only take on the cow when it is far away from powerups
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer2() {
        //if farmer 2 is @ intersection
        //do distance formula between cow and powerups
        //if lower than tolerance, do distance formula for choices
        //move toward shortest choice

        //otherwise, follow open pathing
    }

    /*
    Method: moveFarmer3
    Description of Farmer: Random. Will make movements randomly based on a couple of factors
    1. Is the cow close?
    2. Is the powerup close?
    3. Are any other ghosts pursuing?
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer3() {
        //if farmer 3 is @ intersection
        //check for cow proximity
        //check for cow's proximity to powerup
        //check other farmers for pursuit
        //if all true, do distance formula for all choices


        //otherwise, follow open pathing
    }

    /*
    Method: moveFarmer4
    Description of Farmer: Genius Trickster. Will act like is running but will actually be trying to juke the cow
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer4() {
        //come up with smart farmer method
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
        for (int i = 0; i < 4; i++) {
            if ((FARMER_POSX[i] == myPosx) && (FARMER_POSY[i] == myPosY)) {
                running = false;
                break;
            }
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
        //g.drawString("Time: " + (double) (end-|start|) /1000, (SCREEN_WIDTH - scoreMetrics.stringWidth("Time:" + (end+start)))/2, SCREEN_HEIGHT/2 - 100);

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
