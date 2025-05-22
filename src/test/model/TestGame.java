package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.configs.ScoreConfig;
import model.pieces.Piece;
import model.pieces.PieceI;
import model.pieces.PieceO;
import model.pieces.PieceT;

import java.util.*;

public class TestGame extends TestBoard {
    private Game game1;
    private Game game2;
    public static final String[][] SAMPLE_BOARD_1 = {
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null,  "I", null, null, null},
        {null, null, null, null, null, null,  "I", null, null,  "J"},
        {null,  "T", null, null, null, null,  "I", null, null,  "J"},
        { "T",  "T",  "T", null, null, null,  "I", null,  "J",  "J"}
    };

    static final String[][] SAMPLE_BOARD_2 = {
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        { "G",  "G",  "G",  "G",  "G",  "G", null,  "G",  "G",  "G"},
        { "G",  "G",  "G",  "G",  "G",  "G", null,  "G",  "G",  "G"},
        { "G",  "G",  "G",  "G",  "G",  "G", null,  "G",  "G",  "G"},
        { "G",  "G",  "G",  "G",  "G",  "G", null,  "G",  "G",  "G"}
    };

    static final String[][] SAMPLE_BOARD_3 = {
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "T", "G", null, "T", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "I", "G", "G", null, "G", "G", "I", "G"},
        {"G", "G", "G", "I", "G", null, "G", "I", "G", "G"},
        {"G", "G", "G", "G", "I", null, "I", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
        {"G", "G", "G", "G", "G", null, "G", "G", "G", "G"},
    };

    static final String[][] IMPOSSIBLE_BOARD = {
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null},
        { "G",  "G",  "O",  "G",  "O",  "G",  "O",  "G",  "G",  "G"},
        { "G",  "G",  "O",  "G",  "O",  "G",  "O",  "G",  "G",  "G"},
        { "G",  "G",  "O",  "O",  "O",  "G",  "O",  "G",  "G",  "G"},
        { "G",  "G",  "O",  "G",  "O",  "G",  "O",  "G",  "G",  "G"},
        { "G",  "G",  "O",  "G",  "O",  "G",  "O",  "G",  "G",  "G"}
    };

    @BeforeEach
    public void runBefore() {
        game1 = new Game();

        game2 = new Game();
    }

    private void testNewGameSingular(Game game) {
        assertTrue(game.getStatus());

        String[][] board = game.getBoard();
        assertEquals(Game.BOARD_HEIGHT, board.length);
        for (int i = 0; i < board.length; i++) {
            assertEquals(Game.BOARD_WIDTH, board[i].length);
        }
        assertBoardsEqual(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], board);

        List<String> bag = game.getBag();
        assertEquals(20, bag.size());
        assertTrue(bag.contains("I"));
        assertTrue(bag.contains("O"));
        assertTrue(bag.contains("T"));
        assertTrue(bag.contains("J"));
        //assertTrue(bag.contains("L"));
        //assertTrue(bag.contains("S"));
        //assertTrue(bag.contains("Z"));
        
        assertEquals(0, game.getScore());

        assertNotNull(game.getCurrentPiece());

        assertEquals("NONE", game.getHeldPiece());
    }

    @Test
    public void testGameConstructor() {
        testNewGameSingular(game1);
        assertEquals(0, game1.getHighScores().size());

        testNewGameSingular(game2);
        assertEquals(0, game2.getHighScores().size());
    }

    @Test
    public void testSetBoard() {
        game1.setBoard(SAMPLE_BOARD_3);
        assertNotEquals(SAMPLE_BOARD_3, game1.getBoard());
        assertBoardsEqual(SAMPLE_BOARD_3, game1.getBoard());

        game1.setBoard(SAMPLE_BOARD_1);
        assertNotEquals(SAMPLE_BOARD_1, game1.getBoard());
        assertBoardsEqual(SAMPLE_BOARD_1, game1.getBoard());

        game1.setBoard(SAMPLE_BOARD_1);
        assertNotEquals(SAMPLE_BOARD_1, game1.getBoard());
        assertBoardsEqual(SAMPLE_BOARD_1, game1.getBoard());

        game1.setBoard(SAMPLE_BOARD_2);
        assertNotEquals(SAMPLE_BOARD_2, game1.getBoard());
        assertBoardsEqual(SAMPLE_BOARD_2, game1.getBoard());

        String[][] anEmptyBoard = new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH];
        game1.setBoard(anEmptyBoard);
        assertNotEquals(anEmptyBoard, game1.getBoard());
        assertBoardsEqual(anEmptyBoard, game1.getBoard());
    }

    @Test
    public void testNewGame() {
        game1.setBoard(SAMPLE_BOARD_1);

        Score score = new Score("Seraphina", 605842);
        game1.addHighScore(score);

        game1.newGame();

        testNewGameSingular(game1);
        assertEquals(1, game1.getHighScores().size());
        assertEquals(score, game1.getHighScores().get(0));
    }

    @Test
    public void testGetBoardOutYLow() {
        assertEquals("OUT", game1.getBoard(0, -1));
    }

    @Test
    public void testGetBoardOutYHigh() {
        assertEquals("OUT", game1.getBoard(0, Game.BOARD_HEIGHT));
        assertEquals("OUT", game1.getBoard(0, Game.BOARD_HEIGHT + 1));
    }

    @Test
    public void testGetBoardOutXLow() {
        assertEquals("OUT", game1.getBoard(-1, 0));
    }

    @Test
    public void testGetBoardOutXHigh() {
        assertEquals("OUT", game1.getBoard(Game.BOARD_WIDTH, 0));
        assertEquals("OUT", game1.getBoard(Game.BOARD_WIDTH + 1, 0));
    }

    @Test
    public void testAddGarbageAt() {
        game1.addGarbage(5, 10);
        assertEquals("G", game1.getBoard(5, 10));
    }

    @Test
    public void testAddGarbageAtHighest() {
        game1.addGarbage(0, 0);
        assertEquals("G", game1.getBoard(0, 0));
    }

    @Test
    public void testAddGarbageAtLowest() {
        game1.addGarbage(9, 19);
        assertEquals("G", game1.getBoard(9, 19));
    }

    @Test
    public void testUpdateBoardBagOK() {
        game1.clearBag();
        for (int i = 0; i < 21; i++) {
            game1.addToBag("I");
        }
        assertEquals(game1.getBag().size(), 21);
        game1.getCurrentPiece().teleport(4, 8); // prevents top out
        game1.updateBoard(8);
        assertEquals(game1.getBag().size(), 20);
    }

    @Test
    public void testUpdateBoardBagAlmostRegenerate() {
        game1.clearBag();
        for (int i = 0; i < 16; i++) {
            game1.addToBag("I");
        }
        assertEquals(game1.getBag().size(), 16);
        game1.getCurrentPiece().teleport(4, 8); // prevents top out
        game1.updateBoard(8);
        assertEquals(game1.getBag().size(), 15);
    }

    @Test
    public void testUpdateBoardBagRegenerate() {
        game1.clearBag();
        for (int i = 0; i < 15; i++) {
            game1.addToBag("I");
        }
        assertEquals(game1.getBag().size(), 15);
        game1.getCurrentPiece().teleport(4, 8); // prevents top out
        game1.updateBoard(8);
        assertEquals(game1.getBag().size(), 21);
    }

    @Test
    public void testUpdateBoardBagUnderRegenerate() {
        game1.clearBag();
        for (int i = 0; i < 14; i++) {
            game1.addToBag("I");
        }
        assertEquals(game1.getBag().size(), 14);
        game1.getCurrentPiece().teleport(4, 8); // prevents top out
        game1.updateBoard(8);
        assertEquals(game1.getBag().size(), 20);
    }

    @Test
    public void testUpdateBoardNextPieceIsIThenT() {
        forceAPiece(game1, "I");
        assertEquals(PieceI.class, game1.getCurrentPiece().getClass());
        game1.nextPiece();
        assertEquals(PieceT.class, game1.getCurrentPiece().getClass());
    }

    @Test
    public void testUpdateBoardNextPieceIsT() {
        forceAPiece(game1, "T");
        assertEquals(PieceT.class, game1.getCurrentPiece().getClass());
    }

    @Test
    public void testUpdateBoardNextPieceIsO() {
        forceAPiece(game1, "O");
        assertEquals(PieceO.class, game1.getCurrentPiece().getClass());
    }

    @Test
    public void testUpdateBoardNextPieceIsNonsense() {
        forceAPiece(game1, "Hello, I would like to get an I piece");
        assertNull(game1.getCurrentPiece());
    }

    @Test
    public void testCheckTopOutIOK() {
        forceAPiece(game1, "I");
        game1.getCurrentPiece().hardDrop();
        assertTrue(game1.getStatus());
    }

    @Test
    public void testCheckTopOutILoss1() {
        forceAPiece(game1, "I");

        game1.addGarbage(3, 2);

        game1.getCurrentPiece().hardDrop();
        assertFalse(game1.getStatus());
    }

    @Test
    public void testCheckTopOutILoss2() {
        forceAPiece(game1, "I");

        game1.addGarbage(4, 2);

        game1.getCurrentPiece().hardDrop();
        assertFalse(game1.getStatus());
    }

    @Test
    public void testCheckTopOutILoss3() {
        forceAPiece(game1, "I");

        game1.addGarbage(5, 2);

        game1.getCurrentPiece().hardDrop();
        assertFalse(game1.getStatus());
    }

    @Test
    public void testCheckTopOutILoss4() {
        forceAPiece(game1, "I");

        game1.addGarbage(6, 2);

        game1.getCurrentPiece().hardDrop();
        assertFalse(game1.getStatus());
    }

    @Test
    public void testUpdateBoardPlacePieceI0() {
        forceAPiece(game1, "I");
        game1.getCurrentPiece().teleport(4, 8);
        game1.updateBoard(8);

        String[][] board = game1.getBoard();
        assertNull(board[8][1]);
        assertNull(board[7][3]);
        assertNull(board[7][4]);
        assertNull(board[7][5]);
        assertNull(board[7][6]);
        assertEquals("I", board[8][3]);
        assertEquals("I", board[8][4]);
        assertEquals("I", board[8][5]);
        assertEquals("I", board[8][6]);
        assertNull(board[9][3]);
        assertNull(board[9][4]);
        assertNull(board[9][5]);
        assertNull(board[9][6]);
        assertNull(board[8][7]);
    }

    @Test
    public void testUpdateBoardPlacePieceIR() {
        forceAPiece(game1, "I");
        game1.getCurrentPiece().teleport(4, 8);
        game1.getCurrentPiece().rotateCW();
        game1.updateBoard(8);

        String[][] board = game1.getBoard();
        assertEquals("I", board[7][5]);
        assertEquals("I", board[8][5]);
        assertEquals("I", board[9][5]);
        assertEquals("I", board[10][5]);
    }

    @Test
    public void testUpdateBoardPlacePieceIL() {
        forceAPiece(game1, "I");
        game1.getCurrentPiece().teleport(4, 8);
        game1.getCurrentPiece().rotateCounterCW();
        game1.updateBoard(8);

        String[][] board = game1.getBoard();
        assertEquals("I", board[7][4]);
        assertEquals("I", board[8][4]);
        assertEquals("I", board[9][4]);
        assertEquals("I", board[10][4]);
    }

    @Test
    public void testUpdateBoardPlacePieceI2() {
        forceAPiece(game1, "I");
        game1.getCurrentPiece().teleport(4, 8);
        game1.getCurrentPiece().rotateCW();
        game1.getCurrentPiece().rotateCW();
        game1.updateBoard(8);

        String[][] board = game1.getBoard();
        assertNull(board[9][1]);
        assertNull(board[8][3]);
        assertNull(board[8][4]);
        assertNull(board[8][5]);
        assertNull(board[8][6]);
        assertEquals("I", board[9][3]);
        assertEquals("I", board[9][4]);
        assertEquals("I", board[9][5]);
        assertEquals("I", board[9][6]);
        assertNull(board[10][3]);
        assertNull(board[10][4]);
        assertNull(board[10][5]);
        assertNull(board[10][6]);
        assertNull(board[10][7]);
    }

    private void testUpdateBoardClearLines(int lines, String[][] before, String[][] after) {
        forceAPiece(game1, "I");

        game1.setBoard(before);

        Piece piece = game1.getCurrentPiece();
        piece.rotateCW();
        int oldY = piece.getCenterY();
        piece.hardDrop();

        int cellScore = ScoreConfig.SCORE_HD * (piece.getCenterY() - oldY);

        assertBoardsEqual(game1.getBoard(), after);
        
        assertEquals(ScoreConfig.SCORE_LINES[lines - 1] + cellScore, game1.getScore());
    }

    @Test
    public void testUpdateBoardClear1Line() {
        testUpdateBoardClearLines(1, GameGarbageBoards.GARBAGE_1, GameGarbageBoards.GARBAGE_1_AFTER);
    }

    @Test
    public void testUpdateBoardClear2Lines() {
        testUpdateBoardClearLines(2, GameGarbageBoards.GARBAGE_2, GameGarbageBoards.GARBAGE_2_AFTER);
    }

    @Test
    public void testUpdateBoardClear3Lines() {
        testUpdateBoardClearLines(3, GameGarbageBoards.GARBAGE_3, GameGarbageBoards.GARBAGE_3_AFTER);
    }

    @Test
    public void testUpdateBoardClear4Lines() {
        testUpdateBoardClearLines(4, GameGarbageBoards.GARBAGE_4, GameGarbageBoards.GARBAGE_4_AFTER);
    }

    @Test
    public void testClearImpossibleLines() {
        game1.setBoard(IMPOSSIBLE_BOARD);
        game1.updateBoard(0);
        assertEquals(ScoreConfig.SCORE_LINES_OVER * 5, game1.getScore());
    }

    private void testAddScoreHardDropDroppedFromHeight(int height) {
        Piece piece = game1.getCurrentPiece();
        piece.teleport(4, height);
        piece.hardDrop();

        int expectedScore = ScoreConfig.SCORE_HD * (Game.BOARD_HEIGHT - 1 - height);
        assertEquals(expectedScore, game1.getScore());
    }

    @Test
    public void testAddScoreHardDropDroppedFromHeightMax() {
        testAddScoreHardDropDroppedFromHeight(0);
    }

    @Test
    public void testAddScoreHardDropDroppedFromHeightMid() {
        testAddScoreHardDropDroppedFromHeight(15);
    }

    @Test
    public void testAddScoreHardDropDroppedFromHeightMin() {
        testAddScoreHardDropDroppedFromHeight(22);
    }

    @Test
    public void testAddScoreSoftDrop() {
        Piece piece = game1.getCurrentPiece();
        piece.move(false);
        assertEquals(ScoreConfig.SCORE_SD, game1.getScore());
    }

    @Test
    public void testAddScoreSoftDropNoMovement() {
        Piece piece = game1.getCurrentPiece();
        piece.teleport(4, 22);
        piece.move(false);
        assertEquals(0, game1.getScore());
    }

    @Test
    void testSetScoreOnce() {
        game1.addScore(5);
        assertEquals(5, game1.getScore());
    }

    @Test
    void testSetScoreTwice() {
        game1.setScore(2);
        assertEquals(2, game1.getScore());

        game1.setScore(100);
        assertEquals(100, game1.getScore());
    }

    @Test
    public void testAddHighScore() {
        Score score = new Score("Cindy", 71635);

        game1.addHighScore(score);

        List<Score> scores = game1.getHighScores();
        assertEquals(1, scores.size());
        assertEquals(score, scores.get(0));
    }

    @Test
    public void testAddHighScoreTwice() {
        Score score1 = new Score("Cindy", 71635);
        Score score2 = new Score("Connor", 2234);

        game1.addHighScore(score1);
        
        List<Score> scores = game1.getHighScores();
        assertEquals(1, scores.size());
        assertEquals(score1, scores.get(0));

        game1.addHighScore(score2);
        
        scores = game1.getHighScores();
        assertEquals(2, scores.size());
        assertEquals(score1, scores.get(0));
        assertEquals(score2, scores.get(1));
    }

    @Test
    public void testAddHighScoreSame() {
        Score score1 = new Score("Cindy", 71635);

        game1.addHighScore(score1);
        game1.addHighScore(score1);

        List<Score> scores = game1.getHighScores();
        assertEquals(2, scores.size());
        assertEquals(score1, scores.get(0));
        assertEquals(score1, scores.get(1));
    }

    @Test
    void testCycleHoldPieceCanHoldImmediately() {
        forceAPiece(game1, "O");
        game1.cycleHoldPiece();
        assertEquals("O", game1.getHeldPiece());
        assertEquals(4, game1.getCurrentPiece().getCenterX());
        assertEquals(1, game1.getCurrentPiece().getCenterY());
        assertEquals(PieceT.class, game1.getCurrentPiece().getClass());
    }

    @Test
    void testCycleHoldPieceCantHold() {
        forceAPiece(game1, "O");
        game1.cycleHoldPiece();

        forceAPiece(game1, "I");

        game1.cycleHoldPiece();
        assertEquals("O", game1.getHeldPiece());
        assertEquals(PieceI.class, game1.getCurrentPiece().getClass());
    }

    @Test
    void testCycleHoldPieceCanHoldAgain() {
        forceAPiece(game1, "T");
        game1.cycleHoldPiece();
        
        forceAPiece(game1, "I");

        game1.cycleHoldPiece();
        
        game1.getCurrentPiece().hardDrop();

        forceAPiece(game1, "I");

        game1.cycleHoldPiece();
        assertEquals("I", game1.getHeldPiece());
        assertEquals(PieceT.class, game1.getCurrentPiece().getClass());
    }

    @Test
    void testSetBag() {
        game1.setBag(null);
        assertNull(game1.getBag());

        ArrayList<String> bag = new ArrayList<>();
        bag.add("HELLO!");
        game1.setBag(bag);
        assertEquals(1, game1.getBag().size());
        assertEquals("HELLO!", game1.getBag().get(0));
    }

    @Test
    void testSetHeldPiece() {
        game1.setHeldPiece("O");
        assertEquals("O", game1.getHeldPiece());
        game1.setHeldPiece("I");
        assertEquals("I", game1.getHeldPiece());
    }

    @Test
    void testUpdatePiecePreview() {
        forceAPiece(game1, "I");

        for (int y = 22; y >= 1; y--) {
            game1.addGarbage(4, y);
            game1.updatePiecePreview();
            assertEquals(y - 1, game1.getDroppedY());
        }
    }
}