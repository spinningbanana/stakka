package ui.application;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import model.EventLog;
import model.Game;
import model.Score;
import model.pieces.Piece;

/*
 * The main window of the game,
 * also responsible for key inputs and ticks
 */
public class Stacker extends JFrame implements KeyListener, WindowListener {

    private Game game;
    private Board boardUI;

    private Timer gravityTimer;
    private static final int INITIAL_GRAVITY_DELAY_MS = 1000;
    private int gravityDelay;

    private Timer lockTimer;
    private static final int LOCK_DELAY = 50;
    private static final int LOCK_RESETS = 15;
    private int lockResetsRemaining;
    private int timeBeforeLock;

    private int das;
    private int arr;
    private int sdf;

    private Timer arrLeftTimer;
    private int arrLeftOrder;
    private Timer arrRightTimer;
    private int arrRightOrder;

    private Timer dasLeftTimer;
    private Timer dasRightTimer;

    private Timer boardFallingTimer;
    private Timer mouseTrackerTimer;

    private Map<String, Integer> keyBindMap;
    private Map<String, Boolean> heldKeysMap;

    private boolean shouldBeFocused;
    private int popups;

    // EFFECTS: sets up window in which stacker game will be played
    public Stacker() {
        super("legally distinct stacker game");

        shouldBeFocused = false;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game = new Game();

        initializeVariables();
        initializeKeyBinds();

        boardUI = new Board(game, this);
        
        add(boardUI);

        initializeArrTimers();
        initializeDasTimers();
        initializeGravityTimer();
        initializeLockTimer();
        initializeMouseTrackingTimer();
        gravityTimer.start();
        lockTimer.start();

        addKeyListener(this);
        addWindowListener(this);
        
        setFocusable(true);

        setSize(1280, 720);
        setMinimumSize(new Dimension(854, 480));

        centreOnScreen(this);
        setVisible(true);

        shouldBeFocused = true;
    }

    // MODIFIES: this
    // EFFECTS: setter for arr
    public void setArr(int value) {
        arr = value;
        arrLeftTimer.setDelay(arr);
        arrRightTimer.setDelay(arr);
    }

    // MODIFIES: this
    // EFFECTS: setter for das
    public void setDas(int value) {
        das = value;
        dasLeftTimer.setInitialDelay(das);
        dasRightTimer.setInitialDelay(das);
    }

    // MODIFIES: this
    // EFFECTS: setter for sdf
    public void setSdf(int value) {
        sdf = value;
    }

    public int getArr() {
        return arr;
    }

    public int getDas() {
        return das;
    }

    public int getSdf() {
        return sdf;
    }

    // MODIFIES: this
    // EFFECTS: initializees all keybinds and variables
    private void initializeVariables() {
        das = 167;
        arr = 33;
        sdf = 6;
        gravityDelay = INITIAL_GRAVITY_DELAY_MS;
        lockResetsRemaining = LOCK_RESETS;
        timeBeforeLock = LOCK_DELAY;

        arrLeftOrder = 0;
        arrRightOrder = 0;

        popups = 0;
    }

    // MODIFIES: this
    // EFFECTS: DEFAULT KEY BINDINGS HERE: maps default key bindings
    private void initializeKeyBinds() {
        keyBindMap = new HashMap<>();

        keyBindMap.put("L", KeyEvent.VK_LEFT);
        keyBindMap.put("R", KeyEvent.VK_RIGHT);
        keyBindMap.put("CW", KeyEvent.VK_UP);
        keyBindMap.put("CCW", KeyEvent.VK_Z);
        keyBindMap.put("SD", KeyEvent.VK_DOWN);
        keyBindMap.put("HD", KeyEvent.VK_SPACE);
        keyBindMap.put("H", KeyEvent.VK_C);
        keyBindMap.put("RESET", KeyEvent.VK_R);

        // // my preferred control scheme :D
        // keyBindMap.put("L", KeyEvent.VK_K);
        // keyBindMap.put("R", KeyEvent.VK_SEMICOLON);
        // keyBindMap.put("CW", KeyEvent.VK_O);
        // keyBindMap.put("CCW", KeyEvent.VK_A);
        // keyBindMap.put("SD", KeyEvent.VK_S);
        // keyBindMap.put("HD", KeyEvent.VK_L);
        // keyBindMap.put("H", KeyEvent.VK_C);
        // keyBindMap.put("RESET", KeyEvent.VK_R);
        
        heldKeysMap = new HashMap<>();
        heldKeysMap.put("L", false);
        heldKeysMap.put("R", false);
        heldKeysMap.put("CW", false);
        heldKeysMap.put("CCW", false);
        heldKeysMap.put("SD", false);
        heldKeysMap.put("HD", false);
        heldKeysMap.put("H", false);
        heldKeysMap.put("RESET", false);
    }

    // MODIFES: this
    // EFFECTS: initializes both ARR timers
    //          Auto Repeat Rate (ARR)
    //          
    //          these are responsible for the "auto move" of pieces in both directions, every arr milliseconds
    //
    //          if a timer's order is less than the other direction's, the other one
    //          will take priority. This ensures satisfying gameplay when two directional
    //          keys are pressed at once and both directions' ARR timers are active
    private void initializeArrTimers() {
        arrLeftTimer = new Timer(arr, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (arr == 0) {
                    teleportPieceToSide(-1);
                } else if (arrLeftOrder > arrRightOrder) {
                    movePieceHorizontal(true);
                }
            }
        });

        arrRightTimer = new Timer(arr, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (arr == 0) {
                    teleportPieceToSide(+1);
                } else if (arrRightOrder > arrLeftOrder) {
                    movePieceHorizontal(false);
                }
            }
        });
    }

    // MODIFES: this
    // EFFECTS: initializes both DAS timers
    //          Delayed Auto Shift (DAS): how long in milliseconds before automoving a piece
    //          when a timer is fired, begins ARR timer after das milliseconds
    private void initializeDasTimers() {
        dasLeftTimer = new Timer(das, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                arrLeftTimer.restart();
            }
		});
        dasLeftTimer.setInitialDelay(das);
        dasLeftTimer.setRepeats(false);

        dasRightTimer = new Timer(das, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                arrRightTimer.restart();
            }
		});
        dasRightTimer.setInitialDelay(das);
        dasRightTimer.setRepeats(false);
    }

    // REQUIRES: direction is either -1 or 1
    // EFFECTS: instantly snaps the piece to a side. happens when arr = 0
    private void teleportPieceToSide(int direction) {
        for (int i = 0; i < 10; i++) {
            game.getCurrentPiece().move(direction,0);
        }
        boardUI.repaint();
    }

    // MODIIFIES: this
    // EFFECTS: initializes timer for the gravity of the current piece
    //          if sdf is -1 (what represents infinity), will teleport the piece as far as it can go down
    private void initializeGravityTimer() {
        gravityTimer = new Timer(gravityDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Piece piece = game.getCurrentPiece();

                if (!heldKeysMap.get("SD")) {
                    piece.move(0, 1);
                    boardUI.repaint();
                    return;
                }

                if (sdf == -1) {
                    for (int y = piece.getCenterY(); y <= game.getDroppedY(); y++) {
                        piece.move(false);
                    }
                } else {
                    piece.move(false);
                }
                boardUI.repaint();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes the timer for automatic placement for pieces if they remain on a surface for too long
    private void initializeLockTimer() {
        lockTimer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Piece piece = game.getCurrentPiece();
                if (!piece.checkIsObstructed(piece.getCenterX(), piece.getCenterY() + 1)) {
                    return;
                }

                timeBeforeLock--;
                if (timeBeforeLock <= 0) {
                    placePiece();
                    timeBeforeLock = LOCK_DELAY;
                    lockResetsRemaining = LOCK_RESETS;
                    boardUI.repaint();
                } 
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes timer which tracks where the mouse is relative to the frame.
    //          if it is on the right side, then it will call the boardUI to open the configuration tab
    //          IMPORTANT:
    //              - WILL CONTINUALLY FOCUS THE GAME IF THE MOUSE IS NOT IN THE LAST 1/5 OF THE SCREEN!
    //                to stop this, set the boolean shouldBeFocused to false
    private void initializeMouseTrackingTimer() {
        mouseTrackerTimer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    float mouseX = MouseInfo.getPointerInfo().getLocation().x;
                    float componentX = boardUI.getLocationOnScreen().x;

                    float mouseRelX = mouseX - componentX;

                    float triggerX = getWidth() * 0.8f;

                    Configuration settingsPanel = boardUI.getSettingsPanel();
                    if (mouseRelX >= triggerX) {
                        settingsPanel.extendPanel();
                    } else {
                        settingsPanel.retractPanel();
                        if (shouldBeFocused) {
                            requestFocus();
                        }
                    }
                } catch (IllegalComponentStateException e) {
                    // this is fine
                }
            }
        });
        mouseTrackerTimer.start();
    }

    // REQUIRES: message is not null
    // EFFECTS: an alert popup
    public void showAlert(String message) {
        popups++;
        shouldBeFocused = false;

        JFrame popup = new JFrame();
        popup.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        popup.setResizable(false);
        popup.setSize(250, 200);
        popup.setLayout(new GridBagLayout());
        popup.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                popups--;
                shouldBeFocused = popups <= 0;
            }
        });

        addAlertElements(message, popup);

        centreOnScreen(popup);
        popup.setVisible(true);
    }

    // REQUIRES: message, popup are not null
    // MODIFIES: popup
    // EFFECTS: helper for showAlert() that adds all the alert elements
    private void addAlertElements(String message, JFrame popup) {
        JTextArea alert = new JTextArea(message);
        alert.setEditable(false);
        alert.setWrapStyleWord(true);
        alert.setLineWrap(true);
        alert.setPreferredSize(new Dimension(200, 100));
        
        JButton acknowledge = new JButton("OK");
        acknowledge.setFocusPainted(false);
        acknowledge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                popup.dispatchEvent(new WindowEvent(popup, WindowEvent.WINDOW_CLOSING));
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weighty = 0.1;

        popup.add(alert, gbc);
        popup.add(acknowledge, gbc);
    }

    // Centres frame on desktop
	// MODIFIES: this
	// EFFECTS:  location of frame is set so frame is centred on desktop
    // taken from https://edge.edx.org/courses/course-v1:UBC+CPSC210+all/jump_to_id/efd3918213644a3ba93ceea82a76d28a, thanks!
    private void centreOnScreen(JFrame frame) {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((scrn.width - frame.getWidth()) / 2, (scrn.height - frame.getHeight()) / 2);
    }

    public static void main(String[] args) {
        new Stacker();
    }

    /*********************************************************
     * KEYBOARD INPUT, GAME AND HANDLING LOGIC
     *********************************************************/

    // EFFECTS: hard drops the current piece, and checks if the game is over
    private void placePiece() {
        game.getCurrentPiece().hardDrop();
        
        if (!game.getStatus()) {
            gameOver();
        }
    }

    // EFFECTS: does nothing, but needs to be here as it is part of the interface
    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    /*****************************
     * KEY DOWN
     */

    // MODIFIES: this
    // EFFECTS: responsible for most key inputs: movement, rotation, drops, hold
    //          
    //          ARR - how long in milliseconds between automoves
    //          ALSO, REPAINTS THE boardUI EVERY INPUT
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        Piece piece = game.getCurrentPiece();

        handleMovementPressed(key, piece); 
        handleDroppingPressed(key, piece);

        if (!heldKeysMap.get("H") && key == keyBindMap.get("H")) {
            heldKeysMap.put("H", true);
            game.cycleHoldPiece();
        }

        boardUI.repaint();
    }

    // MODIFIES: this
    // EFFECTS: handles the key down inputs for soft/hard drops
    //          if sdf = -1 (what represents infinity sdf),
    //          gravityTimer delay is set to 1
    private void handleDroppingPressed(int key, Piece piece) {
        if (!heldKeysMap.get("SD") && key == keyBindMap.get("SD")) {
            heldKeysMap.put("SD", true);

            gravityTimer.setDelay(sdf == -1 ? 1 : gravityDelay / sdf);
            gravityTimer.setInitialDelay(0);
            gravityTimer.restart();
        } else if (!heldKeysMap.get("HD") && key == keyBindMap.get("HD")) {
            heldKeysMap.put("HD", true);
            placePiece();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles the key down inputs for movement (including rotations)
    //          resets lock delay value if lockResetsRemaining > 0
    private void handleMovementPressed(int key, Piece piece) {
        if (!heldKeysMap.get("L") && key == keyBindMap.get("L")) {
            heldKeysMap.put("L", true);

            arrLeftOrder = arrRightOrder + 1;
            movePieceHorizontal(true);
            dasLeftTimer.restart();
        } else if (!heldKeysMap.get("R") && key == keyBindMap.get("R")) {
            heldKeysMap.put("R", true);

            arrRightOrder = arrLeftOrder + 1;
            movePieceHorizontal(false);
            dasRightTimer.restart();
        } else if ((!heldKeysMap.get("CCW") && key == keyBindMap.get("CCW")) 
                    || (!heldKeysMap.get("CW") && key == keyBindMap.get("CW"))) {

            heldKeysMap.put("CCW", key == keyBindMap.get("CCW") || heldKeysMap.get("CCW"));
            heldKeysMap.put("CW", key == keyBindMap.get("CW") || heldKeysMap.get("CW"));

            rotatePiece(piece);
        } else {
            return;
        }

        if (lockResetsRemaining > 0) {
            lockResetsRemaining--;
            timeBeforeLock = LOCK_DELAY;
        }
    }

    // MODIFIES: this
    // EFFECTS: moves the current piece left or right, depending on which movement key is pressed
    private void movePieceHorizontal(boolean goingLeft) {
        Piece piece = game.getCurrentPiece();

        if (goingLeft) {
            piece.move(-1, 0);
        } else if (!goingLeft) {
            piece.move(+1, 0);
        }

        boardUI.repaint();
    }

    // MODIFIES: this, piece
    // EFFECTS: rotates the piece depending on which rotation key is pressed
    private void rotatePiece(Piece piece) {
        boolean rotatingCW = heldKeysMap.get("CW");
        boolean rotatingCCW = heldKeysMap.get("CCW");

        if (rotatingCW) {
            piece.rotateCW();
        } else if (rotatingCCW) {
            piece.rotateCounterCW();
        }
    }

    /*****************************
     * KEY UP
     */

    // MODIFIES: this
    // EFFECTS: method responsible for key up inputs
    //          this includes:
    //              - stopping arrTimer and dasTimer
    //              - moving the piece in the direction if arrTimer never fired
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        handleMovementReleased(key); 
        handleDroppingReleased(key); 

        if (key == keyBindMap.get("H")) {
            heldKeysMap.put("H", false);
        }
    }

    // MODIFIES: this
    // EFFECTS: handles the key up inputs for soft/hard drops
    private void handleDroppingReleased(int key) {
        if (key == keyBindMap.get("SD")) {
            heldKeysMap.put("SD", false);
            
            gravityTimer.setDelay(gravityDelay);
            gravityTimer.setInitialDelay(gravityDelay);
            gravityTimer.restart();
        } else if (key == keyBindMap.get("HD")) {
            heldKeysMap.put("HD", false);
        }
    }

    // MODIFIES: this
    // EFFECTS: handles the key up inputs for movement (including rotations)
    private void handleMovementReleased(int key) {
        if (key == keyBindMap.get("L")) {
            heldKeysMap.put("L", false);

            arrLeftTimer.stop();
            arrLeftOrder = 0;
            dasLeftTimer.stop();
        } else if (key == keyBindMap.get("R")) {
            heldKeysMap.put("R", false);

            arrRightTimer.stop();
            arrRightOrder = 0;
            dasRightTimer.stop();    
        } else if (key == keyBindMap.get("CCW")) {
            heldKeysMap.put("CCW", false);
        } else if (key == keyBindMap.get("CW")) {
            heldKeysMap.put("CW", false);
        }
    }

    /*********************************************************
     * GAME OVER & POPUP
     *********************************************************/

    // REQUIRES: boardFallingTimer is not null. It should have already been created
    //           in gameOver(), which should have been run before this method
    // MODIFIES: this
    // EFFECTS: restarts the game
    //          IMPORTANT:
    //              - this method re-enables the main JFrame!
    //              - this method restarts lockTimer!
    //              - this method adds back the keylistener!
    private void restartGame() {
        boardFallingTimer.stop();

        boardUI.resetBoardFall();

        game.newGame();
        lockTimer.start();
        
        boardUI.repaint();

        setEnabled(true);
        shouldBeFocused = true;
        requestFocus();
        addKeyListener(this);
    }

    // EFFECTS: top out! ends the game, causes the board to fall, and opens a popup asking
    //          if the user would like to play again (and shows high scores!)
    //          IMPORTANT:
    //              - this method DISABLES the main JFrame!
    //              - this method STOPS lockTimer!
    //              - this method REMOVES THE KEYLISTENER!
    private void gameOver() {
        setEnabled(false);
        shouldBeFocused = false;

        lockTimer.stop();
        removeKeyListener(this);

        boardFallingTimer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boardUI.boardFall();
            }
        });
        boardFallingTimer.start();

        showGameOverPopup();
    }

    // EFFECTS: disables the game window and shows the game over screen
    private void showGameOverPopup() {
        JFrame popup = new JFrame("GAME OVER!");
        popup.setDefaultCloseOperation(EXIT_ON_CLOSE);
        popup.setResizable(false);
        popup.setLayout(null);
        popup.setSize(400, 400);

        popup.addWindowListener(this);

        int scrollPaneWidth = popup.getWidth() / 2 + 40;
        int scrollPaneHeight = popup.getHeight() / 3;

        addHighScoreElements(popup, scrollPaneWidth, scrollPaneHeight);

        addRestartButton(popup, scrollPaneWidth);
        addQuitButton(popup, scrollPaneWidth);
        
        centreOnScreen(popup);
        popup.setVisible(true);
    }

    // REQUIRES: popup is not null
    // MODIFIES: popup
    // EFFECTS: helper method for showGameOverPopup() so that it adheres to checkstyle
    //          adds quitButton, style, position, and action listener
    private void addQuitButton(JFrame popup, int scrollPaneWidth) {
        JButton quitButton = new JButton("QUIT");
        quitButton.setFont(new Font(Board.FONT, 1, 15));
        quitButton.setContentAreaFilled(false);
        quitButton.setFocusPainted(false);
        quitButton.setBounds(
                popup.getWidth() / 2 - 5, popup.getHeight() - Board.MARGIN * 5,
                scrollPaneWidth / 2, 20
        );

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                popup.dispatchEvent(new WindowEvent(popup, WindowEvent.WINDOW_CLOSING));
            }
        });

        popup.add(quitButton);
    }

    // REQUIRES: popup is not null
    // MODIFIES: popup
    // EFFECTS: helper method for showGameOverPopup() so that it adheres to checkstyle
    //          adds restartButton, style, position, and action listener
    private void addRestartButton(JFrame popup, int scrollPaneWidth) {
        JButton restartButton = new JButton("RESTART");
        restartButton.setFont(new Font(Board.FONT, 1, 15));
        restartButton.setContentAreaFilled(false);
        restartButton.setFocusPainted(false);
        restartButton.setBounds(
                popup.getWidth() / 2 - scrollPaneWidth / 2 - 5, popup.getHeight() - Board.MARGIN * 5,
                scrollPaneWidth / 2, 20
        );

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                popup.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                popup.dispatchEvent(new WindowEvent(popup, WindowEvent.WINDOW_CLOSING));
                restartGame();
            }
        });

        popup.add(restartButton);
    }

    // REQUIRES: popup is not null
    // MODIFIES: popup
    // EFFECTS: helper method for showGameOverPopup() so it adheres to checkstyle,
    //          adds all high score saving related elements to popup
    private void addHighScoreElements(JFrame popup, int scrollPaneWidth, int scrollPaneHeight) {
        JPanel highScorePanel = new JPanel();
        highScorePanel.setLayout(new GridBagLayout());
        refreshHighScores(highScorePanel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(highScorePanel);

        scrollPane.setBounds(
                popup.getWidth() / 2 - scrollPaneWidth / 2 - 5, Board.MARGIN * 2,
                scrollPaneWidth, scrollPaneHeight
        );

        JLabel highScoreTitle = new JLabel("RECORDED SCORES");
        JLabel playerScoreLabel = new JLabel("YOUR SCORE: " + game.getScore());
        setupGameOverLabels(popup, highScoreTitle, playerScoreLabel, scrollPaneWidth, scrollPaneHeight);

        JTextField nameField = new JTextField("Player");
        setupNameField(popup, nameField, scrollPaneWidth, scrollPaneHeight);

        JButton addScoreButton = new JButton("ADD SCORE");
        setupAddScoreButton(popup, nameField, addScoreButton, highScorePanel, scrollPaneWidth, scrollPaneHeight);
        
        popup.add(highScoreTitle);
        popup.add(playerScoreLabel);
        popup.add(scrollPane);
        popup.add(nameField);
        popup.add(addScoreButton);
    }

    // REQUIRES: popup, highScoreTitle, playerScoreLabel is not null
    // MODIFIES: highScoreTitle, playerScoreLabel
    // EFFECTS: sets up the style, position, text, etc. of highScoreTitle and playerScoreLabel
    private void setupGameOverLabels(JFrame popup, JLabel highScoreTitle, JLabel playerScoreLabel, 
                        int scrollPaneWidth, int scrollPaneHeight) {
        
        highScoreTitle.setFont(new Font(Board.FONT, 2, 15));
        highScoreTitle.setBounds(
                popup.getWidth() / 2 - scrollPaneWidth / 2 - 5, Board.MARGIN,
                scrollPaneWidth, 20
        );

        
        playerScoreLabel.setFont(new Font(Board.FONT, 2, 15));
        playerScoreLabel.setBounds(
                popup.getWidth() / 2 - scrollPaneWidth / 2 - 5, 
                Board.MARGIN + scrollPaneHeight + Board.PADDING * 3,
                scrollPaneWidth, 20
        );
    }

    // REQUIRES: popup, nameField, highScorePanel is not null
    // MODIFIES: nameField
    // EFFECTS: sets up the style, position, etc. of nameField
    private void setupNameField(JFrame popup, JTextField nameField, int scrollPaneWidth, int scrollPaneHeight) {
        
        nameField.setFont(new Font(Board.FONT, 0, 15));
        nameField.setBounds(
                popup.getWidth() / 2 - scrollPaneWidth / 2 - 5, 
                Board.MARGIN + scrollPaneHeight + 20 + Board.PADDING * 3,
                scrollPaneWidth, 20
        );
    }

    // REQUIRES: popup, nameField, addScoreButton, highScorePanel is not null
    // MODIFIES: addScoreButton, highScorePanel
    // EFFECTS: sets up the style, position, action listener, etc. of addScoreButton
    //          also refreshes highScorePanel if score save is successful
    private void setupAddScoreButton(JFrame popup, JTextField nameField, JButton addScoreButton, JPanel highScorePanel, 
                            int scrollPaneWidth, int scrollPaneHeight) {
        addScoreButton.setFont(new Font(Board.FONT, 1, 15));
        addScoreButton.setContentAreaFilled(false);
        addScoreButton.setFocusPainted(false);
        addScoreButton.setBounds(
                popup.getWidth() / 2 - scrollPaneWidth / 2 - 5, 
                Board.MARGIN + scrollPaneHeight + 40 + Board.PADDING * 3,
                scrollPaneWidth, 20
        );

        addScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean nameIsBlank = nameField.getText().isBlank();
                boolean nameIsTooLong = nameField.getText().length() >= 15;
                if (nameIsBlank || nameIsTooLong) {
                    showAlert(nameIsBlank ? "Please type a name!" : "Name is too long! (Max 14 characters)");
                    return;
                }
                    
                Score newScore = new Score(nameField.getText(), game.getScore());
                game.addHighScore(newScore);
                refreshHighScores(highScorePanel);
                addScoreButton.removeActionListener(this);
            }
        });
    }

    // MODIFIES: panel
    // EFFECTS: adds all high scores from game to this panel
    private void refreshHighScores(JPanel panel) {
        panel.removeAll();
        List<Score> highScores = game.getHighScores();

        for (int i = 1; i <= highScores.size(); i++) {
            addHighScoreElement(panel, highScores, i);
        }

        panel.revalidate();
        panel.repaint();
    }

    // REQUIRES: panel, highScores is not null, i is [1, highScores.size()]
    // MODIFIES: panel
    // EFFECTS: helper method for refreshHighScores() that adds a row of high scores
    private void addHighScoreElement(JPanel panel, List<Score> highScores, int i) {
        Score highScore = highScores.get(i - 1);
        String name = highScore.getName();
        int score = highScore.getScore();
        
        JPanel row = new JPanel();
        row.setLayout(new GridLayout(1, 2));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(Board.FONT, 0, 13));

        JLabel scoreLabel = new JLabel("" + score);
        scoreLabel.setFont(new Font(Board.FONT, 0, 13));
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(nameLabel);
        row.add(scoreLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        if (i == highScores.size()) {
            gbc.weighty = 1;
        }
        panel.add(row, gbc);
    }

    /*********************************************************
     * WINDOW LISTENER (PHASE 4 WINDOW CLOSING DETECTION)
     *********************************************************/

    @Override
    public void windowOpened(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowClosing(WindowEvent e) {
        EventLog.getInstance().forEach(event -> System.out.println(event.toString()));
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }
}
