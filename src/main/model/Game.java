package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import model.configs.ScoreConfig;
import model.pieces.*;
import persistence.Writable;

/*
 * THIS CLASS IS RESPONSIBLE FOR KEEPING TRACK OF THE GAME STATE, WHICH INCLUDES:
 *  - The state of the board, all pieces on the board currently
 *  - The next 21 pieces
 *  - The random generator and its seed
 *  - The current piece
 * ADDITIONALLY, IT HANDLES THE LOGIC FOR THE FOLLOWING:
 *  - Clearing lines
 *  - Dropping pieces
 *  - Generating bags
 */
public class Game implements Writable {
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 23;
    public static final int SPAWN_X = 4;
    public static final int SPAWN_Y = 1;

    private Random rand;
    // TODO: startingSeed if adding replay feature

    private String[][] board;

    private Piece currentPiece;
    private int droppedY;

    private String heldPiece;
    private boolean canHold;
    private List<String> bag;
    private int score;

    private boolean alive;
    private List<Score> highScores;
    
    // EFFECTS: Sets up highScores and starts a new game
    public Game() {
        highScores = new ArrayList<Score>();
        newGame();
    }

    // MODIFIES: this
    // EFFECTS: Starts a new game. This includes:
    //          prepares an empty 23x10 board (extra 3 for extra top out height)
    //          prepares the Random generation that will be used for this playthrough
    //          prepares the random bag, generating the next 3 bags
    public void newGame() {
        alive = true;

        board = new String[BOARD_HEIGHT][BOARD_WIDTH];
        bag = new ArrayList<String>();
        rand = new Random();

        canHold = true;
        heldPiece = "NONE";
        
        score = 0;

        nextBag();
        nextBag();
        nextBag();

        nextPiece();

        EventLog.getInstance().logEvent(new Event("NEW GAME STARTED"));
    }

    // EFFECTS: get current board state
    public String[][] getBoard() {
        return board;
    }

    // EFFECTS: gets the block at a specific coordinate on the board
    //          returns "OUT" if requested spot is out of array index of the board
    public String getBoard(int x, int y) {
        if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
            return "OUT";
        }
        return board[y][x];
    }

    // REQUIRES: template has the same width and height as game.getBoard()
    // MODIFIES: this
    // EFFECTS: for testing purposes, sets the board, NOT BUT NOT AS THE ORIGINAL REFERENCE
    // Throws InvalidBoardSizeException if inputted board does not match
    // the current board size
    public void setBoard(String[][] template) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = template[y][x];
            }
        }
    }

    // REQUIRES: template has the same width as game.getBoard()'s rows
    // MODIFIES: this
    // EFFECTS: sets an entire row of the board
    public void setBoard(String[] template, int row) {
        for (int x = 0; x < board[row].length; x++) {
            board[row][x] = template[x];
        }
    }

    // TODO: add this if theres free time
    // // MODIFIES: this
    // // EFFECTS: adds an entire row of garbage at the bottom of the board
    // //          with a hole at a random location "height" blocks tall
    // public void addGarbage(int height) {

    // }

    // REQUIRES: garbage is within the array index of the board
    // MODIFIES: this
    // EFFECTS: adds one block of garbage at (x, y)
    public void addGarbage(int x, int y) {
        addToBoard(x, y, "G");
    }

    // MODIFIES: this
    // EFFECTS: adds one block of "type" to board at (x, y)
    private void addToBoard(int x, int y, String type) {
        board[y][x] = type;
    }

    // MODIFIES: this
    // EFFECTS: places the current piece, and clears lines that can be cleared
    //          additionally, checks if more pieces need to be generated (bag size < starting size - 7)
    public void updateBoard(int dropHeight) {
        placePiece(dropHeight);
        clearLines();
        checkTopOut();

        nextPiece();

        if (bag.size() <= 14) {
            nextBag();
        }
    }

    // MODIFIES: this
    // EFFECTS: checks if the current piece spawned inside of an existing block
    //          if yes, game over!
    //          if no, then keep going
    private void checkTopOut() {
        if (currentPiece.checkIsObstructed(SPAWN_X, SPAWN_Y)) {
            EventLog.getInstance().logEvent(new Event("THE PLAYER HAS TOPPED OUT"));
            alive = false;
        }
    }

    // EFFECTS: gets alive
    public boolean getStatus() {
        return alive;
    }

    // MODIFIES: this
    // EFFECTS: places the current piece on the board, 
    //          and cycle to the next piece
    //          additionally, sets canHold to true
    private void placePiece(int dropHeight) {
        String piece = currentPiece.toString();
        Seg[] configuration = currentPiece.getConfiguration();
        int centerX = currentPiece.getCenterX();
        int centerY = currentPiece.getCenterY();

        for (Seg seg : configuration) {
            int posX = centerX + seg.getX();
            int posY = centerY + seg.getY();

            addToBoard(posX, posY, piece);
        }

        canHold = true;

        String eventDescription = piece + " piece placed at x: " + centerX + " y: " + centerY
                                + " orientation: " + currentPiece.getOrientation();
        EventLog.getInstance().logEvent(new Event(eventDescription));
    }

    // MODIFIES: this
    // EFFECTS: clears any filled lines
    private void clearLines() {
        int linesCleared = 0;
        
        StringBuilder eventDescription = new StringBuilder();
        for (int y = 0; y < board.length; y++) {
            if (isRowFilled(y)) {
                moveDown(y);
                linesCleared++;

                eventDescription.append("LINE CLEAR: Row " + y + " cleared\n");
            }
        }

        if (linesCleared > 0) {
            addScoreLineClear(linesCleared);

            eventDescription.append("LINE CLEAR: Cleared " + linesCleared + " rows");
            EventLog.getInstance().logEvent(new Event(eventDescription.toString()));
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the droppedY to the centerY of the current
    //          piece if it were to be hard dropped
    public void updatePiecePreview() {
        int testY = currentPiece.getCenterY();

        while (!currentPiece.checkIsObstructed(currentPiece.getCenterX(), testY)) {
            testY++;
        }

        droppedY = testY - 1;
    }

    // EFFECTS: getter for droppedY
    public int getDroppedY() {
        return droppedY;
    }

    // REQUIRES: row is [0, Game.BOARD_HEIGHT)
    // EFFECTS: returns true if the entire row is filled, false otherwise
    private boolean isRowFilled(int row) {
        for (int x = 0; x < board[row].length; x++) {
            if (board[row][x] == null) {
                return false;
            }
        }
        return true;
    }

    // REQUIRES: height is [0, Game.BOARD_HEIGHT)
    // MODIFIES: this
    // EFFECTS: moves every block above height down by one
    //          height is the y, from 0 (highest) to Game.BOARD_HEIGHT - 1 (lowest)
    private void moveDown(int height) {
        for (int y = height - 1; y >= 0; y--) {
            setBoard(board[y], y + 1);
        }
    }

    // REQUIRES: lines > 0
    // MODIFIES: this
    // EFFECTS: adds score for line clears according to the Guideline scoring system
    //          see: https://harddrop.com/wiki/Scoring#Guideline_scoring_system
    private void addScoreLineClear(int lines) {
        if (lines < 5) {
            score += ScoreConfig.SCORE_LINES[lines - 1];
        } else {
            score += lines * ScoreConfig.SCORE_LINES_OVER;
        }
    }

    // REQUIRES: dropHeight >= 0
    // MODIFIES: this
    // EFFECTS: adds score for "per cell" drops according to the Guideline scoring system
    //          hard drops grant SCORE_HD, while soft drops grand SCORE_SD
    //          see: https://harddrop.com/wiki/Scoring#Guideline_scoring_system
    public void addScoreDrop(boolean wasHardDrop) {
        if (wasHardDrop) {
            addScore(ScoreConfig.SCORE_HD);
        } else {
            addScore(ScoreConfig.SCORE_SD);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds score
    public void addScore(int score) {
        this.score += score;
    }

    // MODIFIES: this
    // EFFECTS: sets score
    public void setScore(int score) {
        this.score = score;
    }

    // EFFECTS: get score
    public int getScore() {
        return score;
    }

    // MODIFIES: this
    // EFFECTS: cycle to next piece, generating the next bag every 7 pieces
    public void nextPiece() {
        String type = bag.get(0);

        setCurrentPieceByType(type);

        bag.remove(0);
    }

    // MODIFIES: this
    // EFFECTS: sets the current piece to type
    @SuppressWarnings("methodlength")
    public void setCurrentPieceByType(String type) {
        currentPiece = getPieceByString(type);
    }

    // EFFECTS: gets the corresponding piece from string
    //          returns null if can't find a corresponding piece
    public Piece getPieceByString(String type) {
        switch (type) {
            case "I":
                return new PieceI(this);
            case "O":
                return new PieceO(this);
            case "T":
                return new PieceT(this);
            case "J":
                return new PieceJ(this);
            case "L":
                return new PieceL(this);
            case "S":
                return new PieceS(this);
            case "Z":
                return new PieceZ(this);
            default:
                return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: generates the next 7 pieces
    private void nextBag() {
        ArrayList<String> sevenBag = new ArrayList<String>();
        sevenBag.add("I");
        sevenBag.add("O");
        sevenBag.add("T");
        sevenBag.add("J");
        sevenBag.add("L");
        sevenBag.add("S");
        sevenBag.add("Z");

        StringBuilder eventDescription = new StringBuilder();
        eventDescription.append("GENERATING NEXT 7 BAG: Generated in order ");
        for (int i = 0; i < 7; i++) {
            int randIndex = rand.nextInt(sevenBag.size());
            String selected = sevenBag.get(randIndex);
            bag.add(selected);
            sevenBag.remove(randIndex);

            eventDescription.append(selected + (i < 6 ? ", " : "."));
        }
        EventLog.getInstance().logEvent(new Event(eventDescription.toString()));
    }
    
    // EFFECTS: get next pieces
    public List<String> getBag() {
        return bag;
    }

    // MODIFIES: this
    // EFFECTS: clears the current bag
    public void clearBag() {
        bag.clear();
    }

    // EFFECTS: gets the current piece
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    // EFFECTS: gets the currently held piece
    public String getHeldPiece() {
        return heldPiece;
    }

    // EFFECTS: sets the currently held piece
    public void setHeldPiece(String piece) {
        heldPiece = piece;
    }

    // MODIFIES: this
    // EFFECTS: cycles the hold piece with the current piece on the board.
    //          for this to be able to happen, canHold must be true
    //          if canHold, canHold = false
    //          then, the current piece is stored, and the held piece is set as the current piece
    //          if there is no hold piece, the next piece is played instead after holding
    public void cycleHoldPiece() {
        if (!canHold) {
            return;
        }

        canHold = false;
        
        String prevHeldPiece = heldPiece;
        setHeldPiece(currentPiece.toString());

        if (prevHeldPiece.equals("NONE")) {
            nextPiece();
        } else {
            setCurrentPieceByType(prevHeldPiece);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new high score
    public void addHighScore(Score score) {
        highScores.add(score);

        String eventDescription = "Added new highscore: name = " + score.getName() + ", score = " + score.getScore();
        EventLog.getInstance().logEvent(new Event(eventDescription));
    }

    // EFFECTS: gets all scores that have been added
    public List<Score> getHighScores() {
        String eventDescription = "High scores accessed: " + highScores.size() + " high score(s).";
        EventLog.getInstance().logEvent(new Event(eventDescription));

        return highScores;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        
        JSONArray boardArray = new JSONArray();
        setupJsonBoard(boardArray);
        
        JSONObject piece = new JSONObject();
        setupJsonPiece(piece);

        JSONObject otherPieces = new JSONObject();
        otherPieces.put("bag", bag.toArray());
        otherPieces.put("heldPiece", heldPiece);

        JSONArray highScoresArray = new JSONArray();
        for (Score score : highScores) {
            JSONObject scoreJson = new JSONObject();
            scoreJson.put("name", score.getName());
            scoreJson.put("score", score.getScore());

            highScoresArray.put(scoreJson);
        }

        json.put("board", boardArray);

        json.put("currentPiece", piece);
        json.put("otherPieces", otherPieces);

        json.put("score", score);

        json.put("highScores", highScoresArray);

        return json;
    }

    // MODIFIES: piece
    // EFFECTS: adds the appropriate elements representing the current piece to a JSONObject
    private void setupJsonPiece(JSONObject piece) {
        piece.put("type", currentPiece.toString());
        piece.put("posX", currentPiece.getCenterX());
        piece.put("posY", currentPiece.getCenterY());
        piece.put("orientation", currentPiece.getOrientation());
    }

    // MODIFIES: boardArray
    // EFFECTS: converts board to an array; sets the inputted JSONArray to this array
    private void setupJsonBoard(JSONArray boardArray) {
        for (int y = 0; y < board.length; y++) {
            JSONArray row = new JSONArray();

            for (int x = 0; x < board[y].length; x++) {
                row.put(board[y][x]);
            }

            boardArray.put(row);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets bag, this method is used specifically for loading
    public void setBag(ArrayList<String> bag) {
        this.bag = bag;
    }

    // MODIFIES: this
    // EFFECTS: adds to the current bag, FOR TESTING ONLY!
    public void addToBag(String piece) {
        bag.add(piece);
    }

    // MODIFIES: this
    // EFFECTS: changes the first element in bag, FOR TESTING ONLY!
    public void changeFirstInBag(String piece) {
        bag.set(0, piece);
    }
}