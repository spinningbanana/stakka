package model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestGame;
import model.configs.PieceConfigs;
import model.configs.TestConfig;

public class TestPieceO {
    private Piece piece;

    private Game game;

    @BeforeEach
    public void runBefore() {
        game = new Game();

        game.changeFirstInBag("O");
        game.nextPiece();
        piece = game.getCurrentPiece();
    }

    @Test
    public void testPieceConstructor() {
        assertEquals("O", piece.toString());
        assertEquals(game, piece.getGame());
        assertEquals(4, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
        assertEquals('0', piece.getOrientation());
        TestConfig.assertConfigEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }

    @Test
    public void testTeleport() {
        piece.teleport(5, 8);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testNotObstructed() {
        assertFalse(piece.checkIsObstructed(4, 1));
    }

    @Test
    public void testObstructedByBlockAtPivot() {
        game.addGarbage(4, 1);
        assertTrue(piece.checkIsObstructed(4, 1));
    }

    @Test
    public void testObstructedByBlockAwayFromPivot() {
        game.addGarbage(5, 1);
        assertTrue(piece.checkIsObstructed(4, 1));
    }

    @Test
    public void testObstructedBecauseLeftBoardR() {
        assertTrue(piece.checkIsObstructed(9, 1));
    }

    @Test
    public void testNotObstructedBeforeLeftBoardR() {
        assertFalse(piece.checkIsObstructed(8, 1));
    }

    @Test
    public void testObstructedBecauseLeftBoardL() {
        assertTrue(piece.checkIsObstructed(-1, 1));
    }

    @Test
    public void testNotObstructedBeforeLeftBoardL() {
        assertFalse(piece.checkIsObstructed(0, 1));
    }

    @Test
    public void testMoveLeft() {
        piece.move(-1, 0);
        assertEquals(3, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveLeftTwice() {
        piece.move(-1, 0);
        piece.move(-1, 0);
        assertEquals(2, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveLeftAtBoundary() {
        piece.teleport(0, 1);
        piece.move(-1, 0);
        assertEquals(0, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveLeftAtObstruction() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(2, 21);
        piece.move(-1, 0);
        assertEquals(2, piece.getCenterX());
        assertEquals(21, piece.getCenterY());
    }

    @Test
    public void testMoveRight() {
        piece.move(1, 0);
        assertEquals(5, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveRightTwice() {
        piece.move(1, 0);
        piece.move(1, 0);
        assertEquals(6, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveRightAtBoundary() {
        piece.teleport(9, 1);
        piece.move(1, 0);
        assertEquals(9, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveRightAtObstruction() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(4, 21);
        piece.move(1, 0);
        assertEquals(4, piece.getCenterX());
        assertEquals(21, piece.getCenterY());
    }

    @Test
    public void testMoveDown() {
        piece.move(false);
        assertEquals(4, piece.getCenterX());
        assertEquals(2, piece.getCenterY());
    }

    @Test
    public void testMoveDownTwice() {
        piece.move(false);
        piece.move(false);
        assertEquals(4, piece.getCenterX());
        assertEquals(3, piece.getCenterY());
    }


    @Test
    public void testMoveDownOnFloor() {
        piece.teleport(4, 22);
        piece.move(false);
        assertEquals(4, piece.getCenterX());
        assertEquals(22, piece.getCenterY());
    }

    @Test
    public void testMoveDownOnObstruction1() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(4, 18);
        piece.move(false);
        assertEquals(4, piece.getCenterX());
        assertEquals(19, piece.getCenterY());
    }

    @Test
    public void testMoveDownOnObstruction2() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(5, 18);
        piece.move(false);
        assertEquals(5, piece.getCenterX());
        assertEquals(18, piece.getCenterY());
    }

    @Test
    public void testMoveDownOnObstruction3() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(6, 18);
        piece.move(false);
        assertEquals(6, piece.getCenterX());
        assertEquals(18, piece.getCenterY());
    }

    @Test
    public void testMoveDownOnObstruction4() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(7, 18);
        piece.move(false);
        assertEquals(7, piece.getCenterX());
        assertEquals(19, piece.getCenterY());
    }

    @Test
    public void testHardDrop() {
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[22][3]);
        assertEquals("O", board[22][4]);
        assertEquals("O", board[22][5]);
        assertNull(board[22][6]);

        assertNull(board[21][3]);
        assertEquals("O", board[21][4]);
        assertEquals("O", board[21][5]);
        assertNull(board[21][6]);
    }

    @Test
    public void testHardDropOnObstacle1() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[18][4]);
        assertEquals("O", board[18][5]);
        assertEquals("O", board[18][6]);
        assertNull(board[18][7]);

        assertNull(board[17][4]);
        assertEquals("O", board[17][5]);
        assertEquals("O", board[17][6]);
        assertNull(board[17][7]);
    }

    @Test
    public void testHardDropOnObstacle2() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.move(1, 0);
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[18][5]);
        assertEquals("O", board[18][6]);
        assertEquals("O", board[18][7]);
        assertNull(board[18][8]);

        assertNull(board[17][5]);
        assertEquals("O", board[17][6]);
        assertEquals("O", board[17][7]);
        assertNull(board[17][8]);
    }

    /*
     * CLOCKWISE ROTATION
     */

    @Test
    public void testRotateCW0toR() {
        piece.rotateCW();
        assertEquals('R', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }

    @Test
    public void testRotateCWRto2() {
        piece.rotateCW();
        piece.rotateCW();
        assertEquals('2', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }

    @Test
    public void testRotateCW2toL() {
        piece.rotateCW();
        piece.rotateCW();
        piece.rotateCW();
        assertEquals('L', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }

    @Test
    public void testRotateCWLto0() {
        piece.rotateCW();
        piece.rotateCW();
        piece.rotateCW();
        piece.rotateCW();
        assertEquals('0', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }
    
    /*
     * COUNTER-CLOCKWISE ROTATION
     */

    @Test
    public void testRotateCCW0toL() {
        piece.rotateCounterCW();
        assertEquals('L', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }

    @Test
    public void testRotateCounterCWLto2() {
        piece.rotateCounterCW();
        piece.rotateCounterCW();
        assertEquals('2', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }

    @Test
    public void testRotateCCW2toR() {
        piece.rotateCounterCW();
        piece.rotateCounterCW();
        piece.rotateCounterCW();
        assertEquals('R', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }

    @Test
    public void testRotateCounterCWRto0() {
        piece.rotateCounterCW();
        piece.rotateCounterCW();
        piece.rotateCounterCW();
        piece.rotateCounterCW();
        assertEquals('0', piece.getOrientation());
        assertEquals(PieceConfigs.PIECE_O, piece.getConfiguration());
    }
}