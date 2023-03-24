import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GameArea extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 560; //up for changes (was 700 OG)
    static final int SCREEN_HEIGHT = 720; //up for changes (was 900 OG)
    static final int UNIT_SIZE = 20; //size of square, in pixels (was 25 OG)
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // unknown
    static final int DELAY = 175; // > #, slower game
    int boardMap[][] = new int[36][28]; //fill in with values
    int FARMER_POSX[] = new int[4]; //holds x locations of all farmers
    int FARMER_POSY[] = new int[4]; //holds y locations of all farmers
    int myPosx = 13*UNIT_SIZE; //holds player x position
    int myPosY = 20*UNIT_SIZE; //holds player y position
    int dotsEaten; //holds # of dots eaten, for score calculation
    char farmerDirection[] = new char[4]; // holds directions of each of the farmers
    char direction = 'R'; // holds direction of the cow
    boolean running = false; // holds value for if game is running
    boolean inMenu = false;
    char select;
    Timer timer;
    long pauseBeginning;
    boolean inDifficultyMenu = false;
    long pauseEnding;
    public long start;
    boolean isPaused = false;
    boolean isTeleporting = false;
    boolean gameEnded = false;
    public boolean[] farmersInCage = {false, false, false, false};
<<<<<<< HEAD
    long totalPauseTime;
    Font customFont;
    String difficulty = "Medium";

    GameArea() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/Quinquefive-K7qep.ttf")).deriveFont(10f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
=======
    long totalPauseTime = 0;

    GameArea() {
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {
                boardMap[i][j] = 0;
            }
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
        }
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        select = 'P';
        inMenu = true;
    }

    public void startGame() {
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {
                boardMap[i][j] = 0;
            }
        }

        totalPauseTime = 0;
        dotsEaten = 0;
        inMenu = false;
        inDifficultyMenu = false;
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        myPosx = 13 * UNIT_SIZE;
        myPosY = 20 * UNIT_SIZE;
        direction = 'R';
        for (int j = 0; j<4; j++) {
            if (j ==0 ) {
                FARMER_POSX[j] = UNIT_SIZE * 11;
                FARMER_POSY[j] = UNIT_SIZE * 16;
                farmersInCage[j] = false;
                farmerDirection[j] = 0;
            } else if (j==1) {
                FARMER_POSX[j] = UNIT_SIZE * 16;
                FARMER_POSY[j] = UNIT_SIZE * 16;
                farmersInCage[j] = false;
                farmerDirection[j] = 0;
            } else if (j==2) {
                FARMER_POSX[j] = UNIT_SIZE * 11;
                FARMER_POSY[j] = UNIT_SIZE * 18;
                farmersInCage[j] = false;
                farmerDirection[j] = 0;
            } else {
                FARMER_POSX[j] = UNIT_SIZE * 16;
                FARMER_POSY[j] = UNIT_SIZE * 18;
                farmersInCage[j] = false;
                farmerDirection[j] = 0;
            }
            for (int row = 0; row < 28; row++) {
                for (int col = 0; col < 36; col++) {
                    if ((col < 3 || col > 33) || ((row > 10 && row < 17) && col > 15 && col < 19) || (col == 16 && (row < 6 || row > 21))) {
                        boardMap[col][row] = -1; //if player cannot move there or is a tunnel, illegal area for score
                    } else {
                        boardMap[col][row] = 2; //if not wall, 2d array is set to be 1
                    }
                }
            }
            for (int row = 0; row < 28; row++) {
                for (int col = 0; col < 36; col++) {
                    if ((col < 3 || col > 33) || ((row > 10 && row < 17) && col > 15 && col < 19) || (col == 16 && (row < 6 || row > 21))) {
                        boardMap[col][row] = -1; //if player cannot move there or is a tunnel, illegal area for score
                    } else {
                        boardMap[col][row] = 2; //if not wall, 2d array is set to be 1
                    }
                }
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
                        if ((col < 3 || col > 33) || ((row > 10 && row < 17) && col > 15 && col < 19) ||
                                (((col == 16) || (col < 15 && col > 12) || (col > 16 && col < 20))  && (row < 6 || row > 21))) {
                            boardMap[col][row] = -1; //if player cannot move there or is a tunnel, illegal area for score
                        } else {
                            if (boardMap[col][row] == 2) {
                                g.setColor(Color.YELLOW);
                                g.fillRect(row * UNIT_SIZE + 7, col * UNIT_SIZE + 7, 6, 6);
                            }
                        }

                    }
                }
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
        }
        else if (isPaused) {
<<<<<<< HEAD
            Font pause = customFont.deriveFont(40f);
            g.setColor(Color.WHITE);
            g.setFont(pause);
            FontMetrics titleMetrics = getFontMetrics(g.getFont());
            g.drawString("Paused", (SCREEN_WIDTH - titleMetrics.stringWidth("Paused"))/2, SCREEN_HEIGHT/2 - 50);

            g.fillRect(SCREEN_WIDTH / 2 - 20, SCREEN_HEIGHT / 2 - 10, 10, 60);
            g.fillRect(SCREEN_WIDTH / 2 + 20, SCREEN_HEIGHT / 2 - 10, 10, 60);
        } else if (inMenu) {
                Font title = customFont.deriveFont(40f);
                g.setColor(Color.BLUE);
            g.setFont(title);
            FontMetrics titleMetrics = getFontMetrics(g.getFont());
            g.drawString("Howling Cow", (SCREEN_WIDTH - titleMetrics.stringWidth("Howling Cow"))/2, SCREEN_HEIGHT/2 - 200);

            Font menuOption1 = customFont.deriveFont(20f);
            g.setColor(Color.BLUE);
            g.setFont(menuOption1);
            FontMetrics option1Metrics = getFontMetrics(g.getFont());
            g.drawString("Play Game", (SCREEN_WIDTH - option1Metrics.stringWidth("Play Game"))/2, SCREEN_HEIGHT/2 - 80);

            Font menuOption2 = customFont.deriveFont(16f);
            g.setColor(Color.BLUE);
            g.setFont(menuOption2);
            FontMetrics option2Metrics = getFontMetrics(g.getFont());
            g.drawString("Difficulty Settings", (SCREEN_WIDTH - option2Metrics.stringWidth("Difficulty Settings"))/2, SCREEN_HEIGHT/2 + 30);

            Font menuOption3 = customFont.deriveFont(20f);
            g.setColor(Color.BLUE);
            g.setFont(menuOption3);
            FontMetrics option3Metrics = getFontMetrics(g.getFont());
            g.drawString("Quit Game", (SCREEN_WIDTH - option3Metrics.stringWidth("Quit Game"))/2, SCREEN_HEIGHT/2 + 140);
            switch (select) {
                case 'P':
                    g.setColor(Color.YELLOW);
                    g.drawOval(SCREEN_WIDTH - option1Metrics.stringWidth("Play Game")/2 - 50, SCREEN_HEIGHT/2 - 90, 10, 10);
                    g.fillArc(SCREEN_WIDTH - option1Metrics.stringWidth("Play Game")/2 - 50, SCREEN_HEIGHT/2 - 90, 10, 10, 0,360);
                    break;
                case 'D':
                    g.setColor(Color.YELLOW);
                    g.drawOval(SCREEN_WIDTH - option2Metrics.stringWidth("Play Game")/2 + 9, SCREEN_HEIGHT/2 + 20, 10, 10);
                    g.fillArc(SCREEN_WIDTH - option2Metrics.stringWidth("Play Game")/2 + 9, SCREEN_HEIGHT/2 + 20, 10, 10, 0,360);
                    break;
                case 'Q':
                    g.setColor(Color.YELLOW);
                    g.drawOval(SCREEN_WIDTH - option3Metrics.stringWidth("Quit Game")/2 - 50, SCREEN_HEIGHT/2 + 130, 10, 10);
                    g.fillArc(SCREEN_WIDTH - option3Metrics.stringWidth("Quit Game")/2 - 50, SCREEN_HEIGHT/2 + 130, 10, 10, 0,360);
                    break;
            }
        } else if (inDifficultyMenu) {
            Font difficultyMenuTitle = customFont.deriveFont(20f);
            g.setColor(Color.BLUE);
            g.setFont(difficultyMenuTitle);
            FontMetrics difficultyMenuTitleMetrics = getFontMetrics(g.getFont());
            g.drawString("Difficulty Settings", (SCREEN_WIDTH - difficultyMenuTitleMetrics.stringWidth("Difficulty Settings"))/2, SCREEN_HEIGHT/2 - 200);

            Font difficultyMenuOption1 = customFont.deriveFont(20f);
            g.setColor(Color.BLUE);
            g.setFont(difficultyMenuOption1);
            FontMetrics difficultyMenuOption1Metrics = getFontMetrics(g.getFont());
            g.drawString("Easy", (SCREEN_WIDTH - difficultyMenuOption1Metrics.stringWidth("Easy"))/2, SCREEN_HEIGHT/2 - 80);

            Font difficultyMenuOption2 = customFont.deriveFont(20f);
            g.setColor(Color.BLUE);
            g.setFont(difficultyMenuOption2);
            FontMetrics difficultyMenuOption2Metrics = getFontMetrics(g.getFont());
            g.drawString("Medium", (SCREEN_WIDTH - difficultyMenuOption2Metrics.stringWidth("Medium"))/2, SCREEN_HEIGHT/2 + 30);

            Font difficultyMenuOption3 = customFont.deriveFont(20f);
            g.setColor(Color.BLUE);
            g.setFont(difficultyMenuOption3);
            FontMetrics difficultyMenuOption3Metrics = getFontMetrics(g.getFont());
            g.drawString("Difficult", (SCREEN_WIDTH - difficultyMenuOption3Metrics.stringWidth("Difficult"))/2, SCREEN_HEIGHT/2 + 140);

            Font difficultyMenuOption4 = customFont.deriveFont(20f);
            g.setColor(Color.BLUE);
            g.setFont(difficultyMenuOption4);
            FontMetrics difficultyMenuOption4Metrics = getFontMetrics(g.getFont());
            g.drawString("Back", (SCREEN_WIDTH - difficultyMenuOption4Metrics.stringWidth("Back"))/2, SCREEN_HEIGHT/2 + 250);

            switch (difficulty) {
                case "Easy":
                    g.setColor(Color.WHITE);
                    g.drawOval(SCREEN_WIDTH - difficultyMenuOption1Metrics.stringWidth("Easy")/2 - 350, SCREEN_HEIGHT/2 - 90, 10, 10);
                    g.fillArc(SCREEN_WIDTH - difficultyMenuOption1Metrics.stringWidth("Easy")/2 - 350, SCREEN_HEIGHT/2 - 90, 10, 10, 0,360);
                    break;
                case "Medium":
                    g.setColor(Color.WHITE);
                    g.drawOval(SCREEN_WIDTH - difficultyMenuOption2Metrics.stringWidth("Medium")/2 - 350, SCREEN_HEIGHT/2 + 20, 10, 10);
                    g.fillArc(SCREEN_WIDTH - difficultyMenuOption2Metrics.stringWidth("Medium")/2 - 350, SCREEN_HEIGHT/2 + 20, 10, 10, 0,360);
                    break;
                case "Difficult":
                    g.setColor(Color.WHITE);
                    g.drawOval(SCREEN_WIDTH - difficultyMenuOption3Metrics.stringWidth("Difficult")/2 - 350, SCREEN_HEIGHT/2 + 130, 10, 10);
                    g.fillArc(SCREEN_WIDTH - difficultyMenuOption3Metrics.stringWidth("Difficult")/2 - 350, SCREEN_HEIGHT/2 + 130, 10, 10, 0,360);
                    break;
            }

            switch (select) {
                case 'E':
                    g.setColor(Color.YELLOW);
                    g.drawOval(SCREEN_WIDTH - difficultyMenuOption1Metrics.stringWidth("Easy")/2 - 150, SCREEN_HEIGHT/2 - 90, 10, 10);
                    g.fillArc(SCREEN_WIDTH - difficultyMenuOption1Metrics.stringWidth("Easy")/2 - 150, SCREEN_HEIGHT/2 - 90, 10, 10, 0,360);
                    break;
                case 'M':
                    g.setColor(Color.YELLOW);
                    g.drawOval(SCREEN_WIDTH - difficultyMenuOption2Metrics.stringWidth("Medium")/2 - 120, SCREEN_HEIGHT/2 + 20, 10, 10);
                    g.fillArc(SCREEN_WIDTH - difficultyMenuOption2Metrics.stringWidth("Medium")/2 - 120, SCREEN_HEIGHT/2 + 20, 10, 10, 0,360);
                    break;
                case 'H':
                    g.setColor(Color.YELLOW);
                    g.drawOval(SCREEN_WIDTH - difficultyMenuOption3Metrics.stringWidth("Difficult")/2 - 50, SCREEN_HEIGHT/2 + 130, 10, 10);
                    g.fillArc(SCREEN_WIDTH - difficultyMenuOption3Metrics.stringWidth("Difficult")/2 - 50, SCREEN_HEIGHT/2 + 130, 10, 10, 0,360);
                    break;
                case 'B':
                    g.setColor(Color.YELLOW);
                    g.drawOval(SCREEN_WIDTH - difficultyMenuOption4Metrics.stringWidth("Back")/2 - 150, SCREEN_HEIGHT/2 + 240, 10, 10);
                    g.fillArc(SCREEN_WIDTH - difficultyMenuOption4Metrics.stringWidth("Back")/2 - 150, SCREEN_HEIGHT/2 + 240, 10, 10, 0,360);
                    break;
            }
        } else {
            gameEnded = true;
=======
            //make pause screen code
        } else {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
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

<<<<<<< HEAD
        if (!farmersInCage[0] && System.currentTimeMillis() - start > 5000 && !difficulty.equals("Easy")) {
            leaveCage(1);
        }


=======
        if (!farmersInCage[0] && System.currentTimeMillis() - start > 5000) {
            leaveCage(1);
        }

>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
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
<<<<<<< HEAD
            }  else {
=======
            } else {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
                dist[0] = -1;
            }
            if (boardMap[FARMER_POSY[0]/UNIT_SIZE][FARMER_POSX[0]/UNIT_SIZE + 1] != 0 && farmerDirection[0] != 'L' && (FARMER_POSX[0]/UNIT_SIZE != 21 && FARMER_POSY[0]/UNIT_SIZE != 16)) {
                dist[1] = Math.sqrt((Math.pow(myPosx-(FARMER_POSX[0] + 1),2))+Math.pow(myPosY-FARMER_POSY[0],2));
<<<<<<< HEAD
            }  else {
=======
            } else {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
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

        if (!farmersInCage[1] && dotsEaten > 15) {
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
<<<<<<< HEAD
            }  else {
=======
            } else {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
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

<<<<<<< HEAD
        if (!farmersInCage[2] && dotsEaten > 30 && !difficulty.equals("Easy")) {
=======
        if (!farmersInCage[2] && dotsEaten > 30) {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
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

        if (!farmersInCage[3] && dotsEaten > 50) {
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
        if (boardMap[myPosY/UNIT_SIZE][myPosx/UNIT_SIZE] == 2) {
            boardMap[myPosY/UNIT_SIZE][myPosx/UNIT_SIZE] = 1;
            dotsEaten++;
<<<<<<< HEAD
        }
        if (dotsEaten == 296) {
            running = false;
=======
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
        }
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

    public void gameOver(Graphics g)  {
        long end = System.currentTimeMillis();
        long timeInterval = end - start;
        long totalGameTime = (timeInterval - totalPauseTime);
<<<<<<< HEAD
=======

>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85

        //Game Over Text
        Font gameOverText = customFont.deriveFont(30f);
        g.setColor(Color.red);
<<<<<<< HEAD
        g.setFont(gameOverText);
=======
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2 + 100);

        //Final Score Text
<<<<<<< HEAD
        String string;
        if (dotsEaten == 296) {
            string = "Final Score: " + Math.round(dotsEaten*10 + totalGameTime / (double) 100 + 5000);
        } else {
            string = "Final Score: " + Math.round(dotsEaten*10 + totalGameTime/ (double) 100);
        }
        Font scoreText = customFont.deriveFont(16f);
        g.setColor(Color.yellow);
        g.setFont(scoreText);
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        g.drawString(string, (SCREEN_WIDTH - scoreMetrics.stringWidth(string))/2, SCREEN_HEIGHT/2 - 100);

=======
        String string = "Final Score: " + Math.round(dotsEaten*10 + totalGameTime/ (double) 100);
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        g.drawString(string, (SCREEN_WIDTH - scoreMetrics.stringWidth(string))/2, SCREEN_HEIGHT/2 - 100);
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
    }

    public void pauseGame() {
        pauseBeginning = System.currentTimeMillis();
        running = false;
        isPaused = true;
    }

    public void unpauseGame() {
        isPaused = false;
        pauseEnding = System.currentTimeMillis();
        totalPauseTime += pauseEnding - pauseBeginning;
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
<<<<<<< HEAD
                    if((boardMap[myPosY/UNIT_SIZE][(myPosx-UNIT_SIZE)/UNIT_SIZE] != 0) && !isTeleporting && running && !difficulty.equals("Difficult")) {
                        direction = 'L';
                    } else if ((boardMap[myPosY/UNIT_SIZE][(myPosx-UNIT_SIZE)/UNIT_SIZE] != 0) && !isTeleporting && running && difficulty.equals("Difficult") && direction != 'R') {
=======
                    if((boardMap[myPosY/UNIT_SIZE][(myPosx-UNIT_SIZE)/UNIT_SIZE] != 0)) {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
<<<<<<< HEAD
                    if((boardMap[myPosY/UNIT_SIZE][(myPosx+UNIT_SIZE)/UNIT_SIZE] != 0) && !isTeleporting && running && !difficulty.equals("Difficult")) {
                        direction = 'R';
                    } else if ((boardMap[myPosY/UNIT_SIZE][(myPosx+UNIT_SIZE)/UNIT_SIZE] != 0) && !isTeleporting && running && difficulty.equals("Difficult") && direction != 'L') {
=======
                    if((boardMap[myPosY/UNIT_SIZE][(myPosx+UNIT_SIZE)/UNIT_SIZE] != 0)) {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
<<<<<<< HEAD
                    if((boardMap[(myPosY-UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] != 0) && running && !difficulty.equals("Difficult")) {
=======
                    if((boardMap[(myPosY-UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] != 0)) {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
                        direction = 'U';
                    } else if ((boardMap[(myPosY-UNIT_SIZE)/UNIT_SIZE][(myPosx)/UNIT_SIZE] != 0)  && running &&difficulty.equals("Difficult") && direction != 'D') {
                        direction = 'U';
                    } else if (!running && !isPaused && inMenu) {
                        switch (select) {
                            case 'P':
                                break;
                            case 'D':
                                select = 'P';
                                repaint();
                                break;
                            case 'Q':
                                select = 'D';
                                repaint();
                                break;
                        }
                    } else if (!running && !isPaused && inDifficultyMenu) {
                        switch (select) {
                            case 'E':
                                break;
                            case 'M':
                                select = 'E';
                                repaint();
                                break;
                            case 'H':
                                select = 'M';
                                repaint();
                                break;
                            case 'B':
                                select = 'H';
                                repaint();
                                break;
                        }
                    }
                    break;
                case KeyEvent.VK_DOWN:
<<<<<<< HEAD
                    if((boardMap[(myPosY+UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] != 0) && running && !difficulty.equals("Difficult")) {
=======
                    if((boardMap[(myPosY+UNIT_SIZE)/UNIT_SIZE][myPosx/UNIT_SIZE] != 0)) {
>>>>>>> ab0bb270057b3b3be89764bc87dbaf1b9acd4f85
                        direction = 'D';
                    } else if ((boardMap[(myPosY+UNIT_SIZE)/UNIT_SIZE][(myPosx)/UNIT_SIZE] != 0) && running && difficulty.equals("Difficult") && direction != 'U') {
                        direction = 'D';
                    } else if (!running && !isPaused && inMenu) {
                        switch (select) {
                            case 'P':
                                select = 'D';
                                repaint();
                                break;
                            case 'D':
                                select = 'Q';
                                repaint();
                                break;
                            case 'Q':
                                break;
                        }
                    } else if (!running && !isPaused && inDifficultyMenu) {
                        switch (select) {
                            case 'E':
                                select = 'M';
                                repaint();
                                break;
                            case 'M':
                                select = 'H';
                                repaint();
                                break;
                            case 'H':
                                select = 'B';
                                repaint();
                                break;
                            case 'B':
                                break;
                        }
                    }
                    break;
                case KeyEvent.VK_1:
                    if(!isPaused && start != 0)
                        pauseGame();
                    else if (isPaused)
                        unpauseGame();
                    break;
                case KeyEvent.VK_2:
                    if((running && !inMenu && !isTeleporting) || gameEnded) {
                        timer.stop();
                        running = false;
                        inMenu = true;
                        repaint();
                    }
                    break;
                case KeyEvent.VK_3:
                    if(!running && inMenu) {
                        if (select == 'P') {
                            start = System.currentTimeMillis();
                            startGame();
                        } else if (select == 'D') {
                            select = 'E';
                            inMenu = false;
                            inDifficultyMenu = true;
                            repaint();
                        } else {
                            System.exit(0);
                        }
                    } else if (!running && inDifficultyMenu) {
                        if (select == 'E') {
                            difficulty = "Easy";
                            repaint();
                        } else if (select == 'M') {
                            difficulty = "Medium";
                            repaint();
                        } else if (select == 'H') {
                            difficulty = "Difficult";
                            repaint();
                        } else if (select == 'B') {
                            select = 'P';
                            inDifficultyMenu = false;
                            inMenu = true;
                            repaint();
                        }
                    }
                    break;
            }
        }
    }
}