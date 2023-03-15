import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GameArea extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 560; //up for changes (was 700 OG)
    static final int SCREEN_HEIGHT = 720; //up for changes (was 900 OG)
    static final int UNIT_SIZE = 20; //size of square, in pixels (was 25 OG)
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // unknown
    static final int DELAY = 175; // > #, slower game
    final int x[] = new int[GAME_UNITS]; //holds x locations of all cartons
    final int y[] = new int[GAME_UNITS]; //holds y locations of all cartons
    int boardMap[][] = new int[36][28]; //fill in with values
    final int FARMER_POSX[] = new int[4]; //holds x locations of all farmers
    final int FARMER_POSY[] = new int[4]; //holds y locations of all farmers
    int myPosx = 13*UNIT_SIZE; //holds player x position
    int myPosY = 20*UNIT_SIZE; //holds player y position
    int dotsEaten; //holds # of dots eaten, for score calculation
    char farmerDirection[] = new char[4]; // holds directions of each of the farmers
    char direction = 'R'; // holds direction of the cow
    boolean running = false; // holds value for if game is running
    Timer timer;
    long pauseBeginning;
    long pauseEnding;
    public long start;
    boolean isPaused = false;
    boolean isTeleporting = false;
    public boolean[] farmersInCage = {false, false, false, false};

    GameArea() {
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {

                boardMap[i][j] = 0;
            }
        }
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
            if (j ==0 ) {
                FARMER_POSX[j] = UNIT_SIZE * 11;
                FARMER_POSY[j] = UNIT_SIZE * 16;
            } else if (j==1) {
                FARMER_POSX[j] = UNIT_SIZE * 16;
                FARMER_POSY[j] = UNIT_SIZE * 16;
            } else if (j==2) {
                FARMER_POSX[j] = UNIT_SIZE * 11;
                FARMER_POSY[j] = UNIT_SIZE * 18;
            } else {
                FARMER_POSX[j] = UNIT_SIZE * 16;
                FARMER_POSY[j] = UNIT_SIZE * 18;
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /*
    Method: draw
    @param g Holds graphical information
     */
    public void draw(Graphics g) {

        if (running) {
            //creates grid pattern of game screen
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            //sets color of each farmer and cow
            for (int i = 0; i < 5; i++) {
                switch (i) {
                    case 0:
                        g.setColor(Color.GREEN);
                        g.fillRect(myPosx, myPosY, UNIT_SIZE, UNIT_SIZE);
                        break;
                    case 1:
                        g.setColor(Color.RED);
                        g.fillRect(FARMER_POSX[i - 1], FARMER_POSY[i - 1], UNIT_SIZE, UNIT_SIZE);
                        break;
                    case 2:
                        g.setColor(Color.CYAN);
                        g.fillRect(FARMER_POSX[i - 1], FARMER_POSY[i - 1], UNIT_SIZE, UNIT_SIZE);
                        break;
                    case 3:
                        g.setColor(Color.ORANGE);
                        g.fillRect(FARMER_POSX[i - 1], FARMER_POSY[i - 1], UNIT_SIZE, UNIT_SIZE);
                        break;
                    case 4:
                        g.setColor(Color.PINK);
                        g.fillRect(FARMER_POSX[i - 1], FARMER_POSY[i - 1], UNIT_SIZE, UNIT_SIZE);
                        break;
                }
            }

            //Wall is drawn on board
            for (int row = 0; row < 28; row++) {
                for (int col = 0; col < 36; col++) {
                    if ((col == 3 || col == 33) ||
                            (row == 0 || row == 27) && ((col > 3 && col < 13) || (col == 15) || (col == 17) || (col > 19 && col < 33)) ||
                            (row < 6 || row > 21) && (col == 12 || col == 15 || col == 17 || col == 20) ||
                            (row == 5 || row == 22) && (col == 13 || col == 14 || col == 18 || col == 19) ||
                            ((row > 1 && row < 6) || (row > 21 && row < 26)) && (col > 4 && col < 8) ||
                            ((row > 6 && row < 12) || (row > 15 && row < 21)) && (col > 4 && col < 8) ||
                            ((row > 1 && row < 6) || (row > 21 && row < 26)) && (col > 8 && col < 11) ||
                            (row > 12 && row < 15) && (col > 3 && col < 8) ||
                            ((row > 6 && row < 9) || (row > 18 && row < 21)) && (col > 8 && col < 16) ||
                            ((row > 9 && row < 18) && (col > 8 && col < 11)) ||
                            ((row > 8 && row < 12) || (row > 15 && row < 19)) && (col > 11 && col < 14) ||
                            (row > 12 && row < 15) && (col > 8 && col < 14) ||
                            (row > 9 && row < 18) && (col== 15 || col == 19) ||
                            (row == 10 || row == 17) && (col > 15 && col < 19) ||
                            ((row > 6 && row < 9) || (row > 18 && row < 21)) && (col > 16 && col < 23) ||
                            (row > 9 && row < 18)  && (col > 20 && col < 23) ||
                            (row > 12 && row < 15) && (col > 22 && col < 26) ||
                            ((row > 1 && row < 6) || (row > 21 && row < 26)) && (col > 21 && col < 25) ||
                            ((row < 4 || (row > 4 && row < 7) || ( row > 20 && row < 23) || row > 23 ) && col == 26) ||
                            (col == 29  && (row == 13 || row == 14)) ||
                            (((row > 6 && row < 12) || (row > 15 && row < 21)) && col == 24) ||
                            (((row > 7 && row < 12) || (row > 15 && row < 20)) && col == 25) ||
                            (row != 4 && row != 8 && row != 19 && row != 23) && (col == 27 || col == 28) ||
                            (row != 1 && row != 12 && row != 15 && row != 26) && (col == 30 || col == 31)){
                        setWall(row, col, g);
                    } else {
                        boardMap[col][row] = 1; //if not wall, 2d array is set to be 1
                    }
                }
            }
        }
        else if (isPaused) {
            //make pause screen code
        } else {
            gameOver(g); //if not paused or running, game is over
        }
    }

    /*
    Method: checkIntersection
    @param row Holds value of "big pixel width" currently being checked
    @param col Holds value of "big pixel height" currently being checked
    Purpose: If at intersection, returns true
     */
    public boolean checkIntersection(int row, int col) {
        if ((col == 4) && (row == 6 || row == 21) ||
                (col == 8 && (row == 1 || row == 6 || row == 9 || row == 12 || row == 15 || row == 18 || row == 21 || row == 26)) ||
                (col == 11 && (row == 6 || row == 21)) ||
                (col == 14 && (row == 12 || row == 15)) ||
                (col == 16 && (row == 6 || row == 9 || row == 18 || row == 21)) ||
                (col == 20 && (row == 9 || row == 18)) ||
                (col == 21 && (row == 6 || row == 21)) ||
                (col == 23 && (row == 6 || row == 9 || row == 18 || row == 21)) ||
                (col == 25 && (row == 4 || row == 6 || row == 21 || row == 23)) ||
                (col == 26 && (row == 8 || row == 12 || row == 15 || row == 19)) ||
                (col == 29 && (row == 4 || row == 8 || row == 19 || row == 23)) ||
                (col == 32 && (row == 12 || row == 15))) {
            return true;
        }
        return false;
    }

    /*
    Method: setWall
    @param row Holds value of "big pixel width" currently being checked
    @param col Holds value of "big pixel height" currently being checked
    @param g Holds graphics information
    Purpose: Sets boardMap 2D array to be value 0 if wall
     */
    public void setWall(int row, int col, Graphics g) {
        if((row == 13 || row == 14) && col == 15)
            g.setColor(Color.GRAY);
        else
            g.setColor(Color.BLUE);
        g.fillRect(row * UNIT_SIZE, col * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        boardMap[col][row] = 0;
    }

    /*
    Method: leaveCage
    @param farmerNum holds which farmer is leaving the cage
    Purpose: Allows each farmer to leave the cage
     */
    public void leaveCage(int farmerNum) {
        if (farmerNum == 1) {
            FARMER_POSX[0] += UNIT_SIZE*2;
            FARMER_POSY[0] -= UNIT_SIZE*2;
            farmerDirection[0] = 'R';
            farmersInCage[0] = true;
        } else if (farmerNum == 2) {
            FARMER_POSX[1] -= UNIT_SIZE*3;
            FARMER_POSY[1] -= UNIT_SIZE*2;
            farmerDirection[1] = 'R';
            farmersInCage[1] = true;
        } else if (farmerNum == 3) {
            FARMER_POSX[2] += UNIT_SIZE*2;
            FARMER_POSY[2] -= UNIT_SIZE*4;
            farmerDirection[2] = 'R';
            farmersInCage[2] = true;
        } else {
            FARMER_POSX[3] -= UNIT_SIZE*3;
            FARMER_POSY[3] -= UNIT_SIZE*4;
            farmerDirection[3] = 'R';
            farmersInCage[3] = true;
        }
    }

    /*
    Method: checkNextDirection
    @param direction Holds the direction of the specific farmer that which the method was called for
    @param index Holds the index of the value that the farmer's information is stored in
    @return char
    Purpose: Will check the next direction that the farmer should be facing
     */
    public char checkNextDirection(char direction, int index) {
        if ((boardMap[FARMER_POSY[index]/UNIT_SIZE][FARMER_POSX[index]/UNIT_SIZE - 1] != 0) && (direction != 'R')) {
            return 'L';
        }
        if (boardMap[FARMER_POSY[index]/UNIT_SIZE][FARMER_POSX[index]/UNIT_SIZE + 1] != 0 && direction != 'L') {
            return 'R';
        }
        if (boardMap[FARMER_POSY[index]/UNIT_SIZE - 1][FARMER_POSX[index]/UNIT_SIZE] != 0 && direction != 'D') {
            return 'U';
        }
        if (boardMap[FARMER_POSY[index]/UNIT_SIZE + 1][FARMER_POSX[index]/UNIT_SIZE] != 0 && direction != 'U') {
            return 'D';
        }
        return 0;
    }

    /*
    Method: moveFarmer1
    Description of Farmer: Highly Agressive. Takes the shortest possible route to the Cow at all times.
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer1() {

        if (!farmersInCage[0] && System.currentTimeMillis() - start > 2000) {
            leaveCage(1);
        }

        if (!checkIntersection(FARMER_POSX[0]/UNIT_SIZE, FARMER_POSY[0]/UNIT_SIZE)) {
            switch (farmerDirection[0]) {
                case 'U':
                    if((boardMap[(FARMER_POSY[0] - UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[0]/UNIT_SIZE] != 0))
                        FARMER_POSY[0] = FARMER_POSY[0] - UNIT_SIZE;
                    else
                        farmerDirection[0] = checkNextDirection(farmerDirection[0], 0);
                    break;
                case 'D':
                    if((boardMap[(FARMER_POSY[0] + UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[0]/UNIT_SIZE] != 0))
                        FARMER_POSY[0] = FARMER_POSY[0] + UNIT_SIZE;
                    else
                        farmerDirection[0] = checkNextDirection(farmerDirection[0], 0);
                    break;
                case 'L':
                    if((boardMap[FARMER_POSY[0]/UNIT_SIZE][(FARMER_POSX[0] - UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[0] = FARMER_POSX[0] - UNIT_SIZE;
                    else
                        farmerDirection[0] = checkNextDirection(farmerDirection[0], 0);
                    break;
                case 'R':
                    if((boardMap[FARMER_POSY[0]/UNIT_SIZE][(FARMER_POSX[0] + UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[0] = FARMER_POSX[0] + UNIT_SIZE;
                    else
                        farmerDirection[0] = checkNextDirection(farmerDirection[0], 0);
                    break;
            }
        } else {
            double dist[] = new double[4];
            double smallestNum = 5000;
            int smallestNumIndex = 0;

            if (boardMap[FARMER_POSY[0]/UNIT_SIZE][FARMER_POSX[0]/UNIT_SIZE - 1] != 0 && farmerDirection[0] != 'R' && (FARMER_POSX[0]/UNIT_SIZE != 6 && FARMER_POSY[0]/UNIT_SIZE != 16)) {
                dist[0] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[0] - 1),2))+Math.pow(myPosY-FARMER_POSY[0],2));
            } else {
                dist[0] = -1;
            }
            if (boardMap[FARMER_POSY[0]/UNIT_SIZE][FARMER_POSX[0]/UNIT_SIZE + 1] != 0 && farmerDirection[0] != 'L' && (FARMER_POSX[0]/UNIT_SIZE != 21 && FARMER_POSY[0]/UNIT_SIZE != 16)) {
                dist[1] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[0] + 1),2))+Math.pow(myPosY-FARMER_POSY[0],2));
            } else {
                dist[1] = -1;
            }
            if (boardMap[FARMER_POSY[0]/UNIT_SIZE - 1][FARMER_POSX[0]/UNIT_SIZE] != 0 && farmerDirection[0] != 'D') {
                dist[2] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[0],2))+Math.pow(myPosY-(FARMER_POSY[0] - 1),2));
            } else {
                dist[2] = -1;
            }
            if (boardMap[FARMER_POSY[0]/UNIT_SIZE + 1][FARMER_POSX[0]/UNIT_SIZE] != 0&& farmerDirection[0] != 'U') {
                dist[3] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[0],2))+Math.pow(myPosY-(FARMER_POSY[0] + 1),2));
            } else {
                dist[3] = -1;
            }

            for (int i = 0; i < 4; i++) {
                if ((dist[i] < smallestNum) && (dist[i] != -1)) {
                    smallestNum = dist[i];
                    smallestNumIndex = i;
                }
            }

            if (smallestNumIndex == 0) {
                farmerDirection[0] = 'L';
                FARMER_POSX[0] -= UNIT_SIZE;
            } else if (smallestNumIndex == 1) {
                farmerDirection[0] = 'R';
                FARMER_POSX[0] += UNIT_SIZE;
            } else if (smallestNumIndex == 2) {
                farmerDirection[0] = 'U';
                FARMER_POSY[0] -= UNIT_SIZE;
            }
            if (smallestNumIndex == 3) {
                farmerDirection[0] = 'D';
                FARMER_POSY[0] += UNIT_SIZE;
            }
        }
    }

    /*
    Method: moveFarmer2
    Description of Farmer: The farmer will avoid the cow at all costs!
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer2() {

        if (!farmersInCage[1] && System.currentTimeMillis() - start > 1500) {
            leaveCage(2);
        }

        if (!checkIntersection(FARMER_POSX[1]/UNIT_SIZE, FARMER_POSY[1]/UNIT_SIZE)) {
            switch (farmerDirection[1]) {
                case 'U':
                    if((boardMap[(FARMER_POSY[1] - UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[1]/UNIT_SIZE] != 0))
                        FARMER_POSY[1] = FARMER_POSY[1] - UNIT_SIZE;
                    else
                        farmerDirection[1] = checkNextDirection(farmerDirection[1], 1);
                    break;
                case 'D':
                    if((boardMap[(FARMER_POSY[1] + UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[1]/UNIT_SIZE] != 0))
                        FARMER_POSY[1] = FARMER_POSY[1] + UNIT_SIZE;
                    else
                        farmerDirection[1] = checkNextDirection(farmerDirection[1], 1);
                    break;
                case 'L':
                    if((boardMap[FARMER_POSY[1]/UNIT_SIZE][(FARMER_POSX[1] - UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[1] = FARMER_POSX[1] - UNIT_SIZE;
                    else
                        farmerDirection[1] = checkNextDirection(farmerDirection[1], 1);
                    break;
                case 'R':
                    if((boardMap[FARMER_POSY[1]/UNIT_SIZE][(FARMER_POSX[1] + UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[1] = FARMER_POSX[1] + UNIT_SIZE;
                    else
                        farmerDirection[1] = checkNextDirection(farmerDirection[1], 1);
                    break;
            }
        } else {
            double dist[] = new double[4];
            double largestNum = 0;
            int largestNumIndex = -1;


            if (boardMap[FARMER_POSY[1]/UNIT_SIZE][FARMER_POSX[1]/UNIT_SIZE - 1] != 0 && farmerDirection[1] != 'R' && (FARMER_POSX[1]/UNIT_SIZE != 6 && FARMER_POSY[1]/UNIT_SIZE != 16)) {
                dist[0] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[1] - 1),2))+Math.pow(myPosY-FARMER_POSY[1],2));
            } else {
                dist[0] = -1;
            }
            if (boardMap[FARMER_POSY[1]/UNIT_SIZE][FARMER_POSX[1]/UNIT_SIZE + 1] != 0 && farmerDirection[1] != 'L' && (FARMER_POSX[1]/UNIT_SIZE != 21 && FARMER_POSY[1]/UNIT_SIZE != 16)) {
                dist[1] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[1] + 1),2))+Math.pow(myPosY-FARMER_POSY[1],2));
            } else {
                dist[1] = -1;
            }
            if (boardMap[FARMER_POSY[1]/UNIT_SIZE - 1][FARMER_POSX[1]/UNIT_SIZE] != 0 && farmerDirection[1] != 'D') {
                dist[2] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[1],2))+Math.pow(myPosY-(FARMER_POSY[1] - 1),2));
            } else {
                dist[2] = -1;
            }
            if (boardMap[FARMER_POSY[1]/UNIT_SIZE + 1][FARMER_POSX[1]/UNIT_SIZE] != 0&& farmerDirection[1] != 'U') {
                dist[3] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[1],2))+Math.pow(myPosY-(FARMER_POSY[1] + 1),2));
            } else {
                dist[3] = -1;
            }

            for (int i = 0; i < 4; i++) {
                if ((dist[i] > largestNum) && (dist[i] != -1)) {
                    largestNum = dist[i];
                    largestNumIndex = i;
                }
            }

            if (largestNumIndex == 0) {
                farmerDirection[1] = 'L';
                FARMER_POSX[1] -= UNIT_SIZE;
            } else if (largestNumIndex == 1) {
                farmerDirection[1] = 'R';
                FARMER_POSX[1] += UNIT_SIZE;
            } else if (largestNumIndex == 2) {
                farmerDirection[1] = 'U';
                FARMER_POSY[1] -= UNIT_SIZE;
            }
            if (largestNumIndex == 3) {
                farmerDirection[1] = 'D';
                FARMER_POSY[1] += UNIT_SIZE;
            }


        }
    }
    /*
    Method: moveFarmer3
    Description of Farmer: If cow within 8 blocks (160 px) then pursue, otherwise make way to bottom right
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer3() {

        if (!farmersInCage[2] && System.currentTimeMillis() - start > 2500) {
            leaveCage(3);
        }

        if (!checkIntersection(FARMER_POSX[2]/UNIT_SIZE, FARMER_POSY[2]/UNIT_SIZE)) {
            switch (farmerDirection[2]) {
                case 'U':
                    if((boardMap[(FARMER_POSY[2] - UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[2]/UNIT_SIZE] != 0))
                        FARMER_POSY[2] = FARMER_POSY[2] - UNIT_SIZE;
                    else
                        farmerDirection[2] = checkNextDirection(farmerDirection[2], 2);
                    break;
                case 'D':
                    if((boardMap[(FARMER_POSY[2] + UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[2]/UNIT_SIZE] != 0))
                        FARMER_POSY[2] = FARMER_POSY[2] + UNIT_SIZE;
                    else
                        farmerDirection[2] = checkNextDirection(farmerDirection[2], 2);
                    break;
                case 'L':
                    if((boardMap[FARMER_POSY[2]/UNIT_SIZE][(FARMER_POSX[2] - UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[2] = FARMER_POSX[2] - UNIT_SIZE;
                    else
                        farmerDirection[2] = checkNextDirection(farmerDirection[2], 2);
                    break;
                case 'R':
                    if((boardMap[FARMER_POSY[2]/UNIT_SIZE][(FARMER_POSX[2] + UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[2] = FARMER_POSX[2] + UNIT_SIZE;
                    else
                        farmerDirection[2] = checkNextDirection(farmerDirection[2], 2);
                    break;
            }
        } else {
            if (Math.sqrt((Math.pow(myPosx-FARMER_POSX[2],2))+Math.pow(myPosY-FARMER_POSY[2],2)) < 160) {
                double dist[] = new double[4];
                double smallestNum = 5000;
                int smallestNumIndex = -1;


                if (boardMap[FARMER_POSY[2]/UNIT_SIZE][FARMER_POSX[2]/UNIT_SIZE - 1] != 0 && farmerDirection[2] != 'R' && (FARMER_POSX[2]/UNIT_SIZE != 6 && FARMER_POSY[2]/UNIT_SIZE != 16)) {
                    dist[0] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[2] - 1),2))+Math.pow(myPosY-FARMER_POSY[2],2));
                } else {
                    dist[0] = -1;
                }
                if (boardMap[FARMER_POSY[2]/UNIT_SIZE][FARMER_POSX[2]/UNIT_SIZE + 1] != 0 && farmerDirection[2] != 'L' && (FARMER_POSX[2]/UNIT_SIZE != 21 && FARMER_POSY[2]/UNIT_SIZE != 16)) {
                    dist[1] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[2] + 1),2))+Math.pow(myPosY-FARMER_POSY[2],2));
                } else {
                    dist[1] = -1;
                }
                if (boardMap[FARMER_POSY[2]/UNIT_SIZE - 1][FARMER_POSX[2]/UNIT_SIZE] != 0 && farmerDirection[2] != 'D') {
                    dist[2] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[2],2))+Math.pow(myPosY-(FARMER_POSY[2] - 1),2));
                } else {
                    dist[2] = -1;
                }
                if (boardMap[FARMER_POSY[2]/UNIT_SIZE + 1][FARMER_POSX[2]/UNIT_SIZE] != 0&& farmerDirection[2] != 'U') {
                    dist[3] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[2],2))+Math.pow(myPosY-(FARMER_POSY[2] + 1),2));
                } else {
                    dist[3] = -1;
                }

                for (int i = 0; i < 4; i++) {
                    if ((dist[i] < smallestNum) && (dist[i] != -1)) {
                        smallestNum = dist[i];
                        smallestNumIndex = i;
                    }
                }

                if (smallestNumIndex == 0) {
                    farmerDirection[2] = 'L';
                    FARMER_POSX[2] -= UNIT_SIZE;
                } else if (smallestNumIndex == 1) {
                    farmerDirection[2] = 'R';
                    FARMER_POSX[2] += UNIT_SIZE;
                } else if (smallestNumIndex == 2) {
                    farmerDirection[2] = 'U';
                    FARMER_POSY[2] -= UNIT_SIZE;
                }
                if (smallestNumIndex == 3) {
                    farmerDirection[2] = 'D';
                    FARMER_POSY[2] += UNIT_SIZE;
                }
            } else {
                double dist[] = new double[4];
                double smallestNum = 5000;
                int smallestNumIndex = -1;


                if (boardMap[FARMER_POSY[2]/UNIT_SIZE][FARMER_POSX[2]/UNIT_SIZE - 1] != 0 && farmerDirection[2] != 'R' && (FARMER_POSX[2]/UNIT_SIZE != 6 && FARMER_POSY[2]/UNIT_SIZE != 16)) {
                    dist[0] = Math.sqrt((Math.pow(SCREEN_WIDTH-(FARMER_POSX[2] - 1),2))+Math.pow(SCREEN_HEIGHT-FARMER_POSY[2],2));
                } else {
                    dist[0] = -1;
                }
                if (boardMap[FARMER_POSY[2]/UNIT_SIZE][FARMER_POSX[2]/UNIT_SIZE + 1] != 0 && farmerDirection[2] != 'L' && (FARMER_POSX[2]/UNIT_SIZE != 21 && FARMER_POSY[2]/UNIT_SIZE != 16)) {
                    dist[1] = Math.sqrt((Math.pow(SCREEN_WIDTH-(FARMER_POSX[2] + 1),2))+Math.pow(SCREEN_HEIGHT-FARMER_POSY[2],2));
                } else {
                    dist[1] = -1;
                }
                if (boardMap[FARMER_POSY[2]/UNIT_SIZE - 1][FARMER_POSX[2]/UNIT_SIZE] != 0 && farmerDirection[2] != 'D') {
                    dist[2] = Math.sqrt((Math.pow(SCREEN_WIDTH-FARMER_POSX[2],2))+Math.pow(SCREEN_HEIGHT-(FARMER_POSY[2] - 1),2));
                } else {
                    dist[2] = -1;
                }
                if (boardMap[FARMER_POSY[2]/UNIT_SIZE + 1][FARMER_POSX[2]/UNIT_SIZE] != 0&& farmerDirection[2] != 'U') {
                    dist[3] = Math.sqrt((Math.pow(SCREEN_WIDTH-FARMER_POSX[2],2))+Math.pow(SCREEN_HEIGHT-(FARMER_POSY[2] + 1),2));
                } else {
                    dist[3] = -1;
                }

                for (int i = 0; i < 4; i++) {
                    if ((dist[i] < smallestNum) && (dist[i] != -1)) {
                        smallestNum = dist[i];
                        smallestNumIndex = i;
                    }
                }

                if (smallestNumIndex == 0) {
                    farmerDirection[2] = 'L';
                    FARMER_POSX[2] -= UNIT_SIZE;
                } else if (smallestNumIndex == 1) {
                    farmerDirection[2] = 'R';
                    FARMER_POSX[2] += UNIT_SIZE;
                } else if (smallestNumIndex == 2) {
                    farmerDirection[2] = 'U';
                    FARMER_POSY[2] -= UNIT_SIZE;
                }
                if (smallestNumIndex == 3) {
                    farmerDirection[2] = 'D';
                    FARMER_POSY[2] += UNIT_SIZE;
                }
            }
        }
    }

    /*
    Method: moveFarmer4
    Description of Farmer: If cow within 8 blocks (160 px) then pursue, otherwise make way to top left
    When Make Decision on movement: Intersection of maze points
     */
    public void moveFarmer4() {

        if (!farmersInCage[3] && System.currentTimeMillis() - start > 3000) {
            leaveCage(4);
        }

        if (!checkIntersection(FARMER_POSX[3]/UNIT_SIZE, FARMER_POSY[3]/UNIT_SIZE)) {
            switch (farmerDirection[3]) {
                case 'U':
                    if((boardMap[(FARMER_POSY[3] - UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[3]/UNIT_SIZE] != 0))
                        FARMER_POSY[3] = FARMER_POSY[3] - UNIT_SIZE;
                    else
                        farmerDirection[3] = checkNextDirection(farmerDirection[3], 3);
                    break;
                case 'D':
                    if((boardMap[(FARMER_POSY[3] + UNIT_SIZE)/UNIT_SIZE][FARMER_POSX[3]/UNIT_SIZE] != 0))
                        FARMER_POSY[3] = FARMER_POSY[3] + UNIT_SIZE;
                    else
                        farmerDirection[3] = checkNextDirection(farmerDirection[3], 3);
                    break;
                case 'L':
                    if((boardMap[FARMER_POSY[3]/UNIT_SIZE][(FARMER_POSX[3] - UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[3] = FARMER_POSX[3] - UNIT_SIZE;
                    else
                        farmerDirection[3] = checkNextDirection(farmerDirection[3], 3);
                    break;
                case 'R':
                    if((boardMap[FARMER_POSY[3]/UNIT_SIZE][(FARMER_POSX[3] + UNIT_SIZE)/UNIT_SIZE] != 0))
                        FARMER_POSX[3] = FARMER_POSX[3] + UNIT_SIZE;
                    else
                        farmerDirection[3] = checkNextDirection(farmerDirection[3], 3);
                    break;
            }
        } else {
            if (Math.sqrt((Math.pow(myPosx-FARMER_POSX[3],2))+Math.pow(myPosY-FARMER_POSY[3],2)) < 160) {
                double dist[] = new double[4];
                double smallestNum = 5000;
                int smallestNumIndex = -1;


                if (boardMap[FARMER_POSY[3]/UNIT_SIZE][FARMER_POSX[3]/UNIT_SIZE - 1] != 0 && farmerDirection[3] != 'R' && (FARMER_POSX[3]/UNIT_SIZE != 6 && FARMER_POSY[3]/UNIT_SIZE != 16)) {
                    dist[0] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[3] - 1),2))+Math.pow(myPosY-FARMER_POSY[3],2));
                } else {
                    dist[0] = -1;
                }
                if (boardMap[FARMER_POSY[3]/UNIT_SIZE][FARMER_POSX[3]/UNIT_SIZE + 1] != 0 && farmerDirection[3] != 'L' && (FARMER_POSX[3]/UNIT_SIZE != 21 && FARMER_POSY[3]/UNIT_SIZE != 16)) {
                    dist[1] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[3] + 1),2))+Math.pow(myPosY-FARMER_POSY[3],2));
                } else {
                    dist[1] = -1;
                }
                if (boardMap[FARMER_POSY[3]/UNIT_SIZE - 1][FARMER_POSX[3]/UNIT_SIZE] != 0 && farmerDirection[3] != 'D') {
                    dist[2] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[3],2))+Math.pow(myPosY-(FARMER_POSY[3] - 1),2));
                } else {
                    dist[2] = -1;
                }
                if (boardMap[FARMER_POSY[3]/UNIT_SIZE + 1][FARMER_POSX[3]/UNIT_SIZE] != 0&& farmerDirection[3] != 'U') {
                    dist[3] = Math.sqrt((Math.pow(myPosx-FARMER_POSX[3],2))+Math.pow(myPosY-(FARMER_POSY[3] + 1),2));
                } else {
                    dist[3] = -1;
                }

                for (int i = 0; i < 4; i++) {
                    if ((dist[i] < smallestNum) && (dist[i] != -1)) {
                        smallestNum = dist[i];
                        smallestNumIndex = i;
                    }
                }

                if (smallestNumIndex == 0) {
                    farmerDirection[3] = 'L';
                    FARMER_POSX[3] -= UNIT_SIZE;
                } else if (smallestNumIndex == 1) {
                    farmerDirection[3] = 'R';
                    FARMER_POSX[3] += UNIT_SIZE;
                } else if (smallestNumIndex == 2) {
                    farmerDirection[3] = 'U';
                    FARMER_POSY[3] -= UNIT_SIZE;
                }
                if (smallestNumIndex == 3) {
                    farmerDirection[3] = 'D';
                    FARMER_POSY[3] += UNIT_SIZE;
                }
            } else {
                double dist[] = new double[4];
                double smallestNum = 5000;
                int smallestNumIndex = -1;

                if (boardMap[FARMER_POSY[3]/UNIT_SIZE][FARMER_POSX[3]/UNIT_SIZE - 1] != 0 && farmerDirection[3] != 'R' && (FARMER_POSX[3]/UNIT_SIZE != 6 && FARMER_POSY[3]/UNIT_SIZE != 16)) {
                    dist[0] = Math.sqrt((Math.pow(-(FARMER_POSX[3] - 1),2))+Math.pow(-FARMER_POSY[3],2));
                } else {
                    dist[0] = -1;
                }
                if (boardMap[FARMER_POSY[3]/UNIT_SIZE][FARMER_POSX[3]/UNIT_SIZE + 1] != 0 && farmerDirection[3] != 'L' && (FARMER_POSX[3]/UNIT_SIZE != 21 && FARMER_POSY[3]/UNIT_SIZE != 16)) {
                    dist[1] = Math.sqrt((Math.pow(-(FARMER_POSX[3] + 1),2))+Math.pow(-FARMER_POSY[3],2));
                } else {
                    dist[1] = -1;
                }
                if (boardMap[FARMER_POSY[3]/UNIT_SIZE - 1][FARMER_POSX[3]/UNIT_SIZE] != 0 && farmerDirection[3] != 'D') {
                    dist[2] = Math.sqrt((Math.pow(-FARMER_POSX[3],2))+Math.pow(-(FARMER_POSY[3] - 1),2));
                } else {
                    dist[2] = -1;
                }
                if (boardMap[FARMER_POSY[3]/UNIT_SIZE + 1][FARMER_POSX[3]/UNIT_SIZE] != 0&& farmerDirection[3] != 'U') {
                    dist[3] = Math.sqrt((Math.pow(-FARMER_POSX[3],2))+Math.pow(-(FARMER_POSY[3] + 1),2));
                } else {
                    dist[3] = -1;
                }

                for (int i = 0; i < 4; i++) {
                    if ((dist[i] < smallestNum) && (dist[i] != -1)) {
                        smallestNum = dist[i];
                        smallestNumIndex = i;
                    }
                }

                if (smallestNumIndex == 0) {
                    farmerDirection[3] = 'L';
                    FARMER_POSX[3] -= UNIT_SIZE;
                } else if (smallestNumIndex == 1) {
                    farmerDirection[3] = 'R';
                    FARMER_POSX[3] += UNIT_SIZE;
                } else if (smallestNumIndex == 2) {
                    farmerDirection[3] = 'U';
                    FARMER_POSY[3] -= UNIT_SIZE;
                }
                if (smallestNumIndex == 3) {
                    farmerDirection[3] = 'D';
                    FARMER_POSY[3] += UNIT_SIZE;
                }
            }
        }
    }

    /*
    Method: move
    Purpose: Moves player a direction based on called inputs
     */
    public void move() {
        switch (direction) {
            case 'U':
                if((myPosY != 0) && (boardMap[(myPosY-UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] != 0))
                    myPosY = myPosY - UNIT_SIZE;
                break;
            case 'D':
                if((myPosY != SCREEN_HEIGHT - UNIT_SIZE)  && (boardMap[(myPosY+UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] != 0))
                    myPosY = myPosY + UNIT_SIZE;
                break;
            case 'L':
                if(((myPosx - UNIT_SIZE) / UNIT_SIZE == 3) && (myPosY/UNIT_SIZE == 16) || isTeleporting)
                    teleport();
                if((myPosx != 0)  && (boardMap[myPosY/UNIT_SIZE][(myPosx-UNIT_SIZE)/UNIT_SIZE] != 0))
                    myPosx = myPosx - UNIT_SIZE;
                break;
            case 'R':
                if (((myPosx + UNIT_SIZE) / UNIT_SIZE == 25) && (myPosY/UNIT_SIZE == 16) || isTeleporting)
                    teleport();
                if((myPosx != SCREEN_WIDTH - UNIT_SIZE)   && (boardMap[myPosY/UNIT_SIZE][(myPosx+UNIT_SIZE)/UNIT_SIZE] != 0))
                    myPosx = myPosx + UNIT_SIZE;
                break;

        }
    }

    /*
    Method: teleport
    Purpose: Teleports the player across the board
     */
    public void teleport() {
        if (direction == 'L') {
            if (myPosx/UNIT_SIZE != 25) {
                isTeleporting = true;
            } else {
                isTeleporting = false;
            }
            myPosx = myPosx - UNIT_SIZE;
            if (myPosx-UNIT_SIZE == 0) {
                myPosx = SCREEN_WIDTH;
            }
        } else {
            if (myPosx/UNIT_SIZE != 3) {
                isTeleporting = true;
            } else {
                isTeleporting = false;
            }
            myPosx = myPosx + UNIT_SIZE;
            if (myPosx+UNIT_SIZE == SCREEN_WIDTH) {
                myPosx = 0;
            }

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
        //long end = System.currentTimeMillis();
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
            moveFarmer1();
            moveFarmer2();
            moveFarmer3();
            moveFarmer4();
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
                    if((boardMap[myPosY/UNIT_SIZE][(myPosx-UNIT_SIZE)/UNIT_SIZE] == 1)) {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if((boardMap[myPosY/UNIT_SIZE][(myPosx+UNIT_SIZE)/UNIT_SIZE] == 1)) {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if((boardMap[(myPosY-UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] == 1)) {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if((boardMap[(myPosY+UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] == 1)) {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_1:
                    if(!isPaused)
                        pauseGame();
                    else
                        unpauseGame();
                    break;
            }
        }
    }
}
