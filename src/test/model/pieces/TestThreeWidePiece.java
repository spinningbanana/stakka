package model.pieces;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.Game;
import model.Seg;
import model.TestGame;
import model.exceptions.InvalidOrientationException;

// class with shared tests in all pieces that are three wide
public abstract class TestThreeWidePiece {
    protected Piece piece;

    protected Game game;

    public abstract void testPieceConstructor();

    @Test
    void testTeleport() {
        piece.teleport(5, 8);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    void testNotObstructed() {
        assertFalse(piece.checkIsObstructed(4, 1));
    }

    @Test
    void testObstructedByBlockAtPivot() {
        game.addGarbage(4, 1);
        assertTrue(piece.checkIsObstructed(4, 1));
    }
    
    @Test
    void testNotObstructedBeforeLeftBoardR() {
        assertFalse(piece.checkIsObstructed(8, 1));
    }

    @Test
    void testObstructedBecauseLeftBoardR() {
        assertTrue(piece.checkIsObstructed(9, 1));
    }

    @Test
    void testNotObstructedBeforeLeftBoardL() {
        assertFalse(piece.checkIsObstructed(1, 1));
    }

    @Test
    void testObstructedBecauseLeftBoardL() {
        assertTrue(piece.checkIsObstructed(0, 1));
    }

    @Test
    void testMoveLeft() {
        piece.move(-1, 0);
        assertEquals(3, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    void testMoveLeftTwice() {
        piece.move(-1, 0);
        piece.move(-1, 0);
        assertEquals(2, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    void testMoveLeftAtBoundary() {
        piece.teleport(1, 1);
        piece.move(-1, 0);
        assertEquals(1, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    void testMoveLeftAtObstruction() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(3, 21);
        piece.move(-1, 0);
        assertEquals(3, piece.getCenterX());
        assertEquals(21, piece.getCenterY());
    }

    @Test
    void testMoveRight() {
        piece.move(1, 0);
        assertEquals(5, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    void testMoveRightTwice() {
        piece.move(1, 0);
        piece.move(1, 0);
        assertEquals(6, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    void testMoveRightAtBoundary() {
        piece.teleport(8, 1);
        piece.move(1, 0);
        assertEquals(8, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    void testMoveRightAtObstruction() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(4, 21);
        piece.move(1, 0);
        assertEquals(4, piece.getCenterX());
        assertEquals(21, piece.getCenterY());
    }

    @Test
    void testMoveDown() {
        piece.move(false);
        assertEquals(4, piece.getCenterX());
        assertEquals(2, piece.getCenterY());
    }

    @Test
    void testMoveDownTwice() {
        piece.move(false);
        piece.move(false);
        assertEquals(4, piece.getCenterX());
        assertEquals(3, piece.getCenterY());
    }

    @Test
    void testMoveDownOnFloor() {
        piece.teleport(4, 22);
        piece.move(false);
        assertEquals(4, piece.getCenterX());
        assertEquals(22, piece.getCenterY());
    }

    @Test
    void testMoveDownOnObstruction() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(6, 18);
        piece.move(false);
        assertEquals(6, piece.getCenterX());
        assertEquals(18, piece.getCenterY());
    }

    @Test
    void testInvalidOrientation() {
        piece.setOrientation('m');
        try {
            piece.rotateCW();
            fail("This was an invalid orientation!");
        } catch (InvalidOrientationException err) {
            assertTrue(true);
        }

        try {
            piece.rotateCounterCW();
            fail("This was an invalid orientation!");
        } catch (InvalidOrientationException err) {
            assertTrue(true);
        }
    }

    public abstract void testObstructedByBlockAwayFromPivot();

    protected void assertBoardEqualsConfig(String expected, int x, int y, Seg[] config) {
        for (Seg seg : config) {
            int posX = x + seg.getX();
            int posY = y + seg.getY();
            assertEquals(expected, game.getBoard()[posY][posX]);
        }
    }

    public abstract void testHardDrop();

    public abstract void testHardDropOnObstacle0();

    public abstract void testHardDropOnObstacle1();

    public abstract void testHardDropOnObstacle2();

    public abstract void testHardDropOnObstacle3();
}
