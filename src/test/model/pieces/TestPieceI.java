package model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestGame;
import model.configs.PieceConfigs;
import model.configs.TestConfig;
import model.pieces.boards.PieceITestBoards;

public class TestPieceI {
    private Piece piece;

    private Game game;

    @BeforeEach
    public void runBefore() {
        game = new Game();

        game.changeFirstInBag("I");
        game.nextPiece();
        piece = game.getCurrentPiece();
    }

    @Test
    public void testPieceConstructor() {
        assertEquals("I", piece.toString());
        assertEquals(game, piece.getGame());
        assertEquals(4, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
        assertEquals('0', piece.getOrientation());
        TestConfig.assertConfigEquals(PieceConfigs.PIECE_I[0], piece.getConfiguration());
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
        game.addGarbage(6, 1);
        assertTrue(piece.checkIsObstructed(4, 1));
    }

    @Test
    public void testObstructedBecauseLeftBoardR() {
        assertTrue(piece.checkIsObstructed(8, 1));
    }

    @Test
    public void testObstructedBecauseLeftBoardL() {
        assertTrue(piece.checkIsObstructed(0, 1));
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
        piece.teleport(1, 1);
        piece.move(-1, 0);
        assertEquals(1, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveLeftAtObstruction() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(3, 21);
        piece.move(-1, 0);
        assertEquals(3, piece.getCenterX());
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
        piece.teleport(7, 1);
        piece.move(1, 0);
        assertEquals(7, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
    }

    @Test
    public void testMoveRightAtObstruction() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        piece.teleport(3, 21);
        piece.move(1, 0);
        assertEquals(3, piece.getCenterX());
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
        assertEquals(18, piece.getCenterY());
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
        assertEquals(18, piece.getCenterY());
    }

    @Test
    public void testHardDrop() {
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[22][2]);
        assertNull(board[21][3]);
        assertNull(board[21][4]);
        assertNull(board[21][5]);
        assertNull(board[21][6]);
        assertEquals("I", board[22][3]);
        assertEquals("I", board[22][4]);
        assertEquals("I", board[22][5]);
        assertEquals("I", board[22][6]);
        assertNull(board[22][7]);
    }

    @Test
    public void testHardDropOnObstacle0() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        
        piece.move(-1, 0);
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertEquals("T", board[21][1]);
        assertEquals("I", board[21][2]);
        assertEquals("I", board[21][3]);
        assertEquals("I", board[21][4]);
        assertEquals("I", board[21][5]);
        assertEquals("I", board[21][6]);
        assertNull(board[21][7]);
    }

    @Test
    public void testHardDropOnObstacle1() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[18][2]);
        assertEquals("I", board[18][3]);
        assertEquals("I", board[18][4]);
        assertEquals("I", board[18][5]);
        assertEquals("I", board[18][6]);
        assertNull(board[18][7]);
    }

    @Test
    public void testHardDropOnObstacle2() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[18][3]);
        assertEquals("I", board[18][4]);
        assertEquals("I", board[18][5]);
        assertEquals("I", board[18][6]);
        assertEquals("I", board[18][7]);
        assertNull(board[18][8]);
    }

    @Test
    public void testHardDropOnObstacle3() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.move(1, 0);
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[18][4]);
        assertEquals("I", board[18][5]);
        assertEquals("I", board[18][6]);
        assertEquals("I", board[18][7]);
        assertEquals("I", board[18][8]);
        assertNull(board[18][9]);
    }

    @Test
    public void testHardDropOnObstacle4() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.move(1, 0);
        piece.move(1, 0);
        piece.hardDrop();

        String[][] board = game.getBoard();
        assertNull(board[18][5]);
        assertEquals("I", board[18][6]);
        assertEquals("I", board[18][7]);
        assertEquals("I", board[18][8]);
        assertEquals("I", board[18][9]);
    }

    /*
     * CLOCKWISE ROTATION
     */

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCW0toR(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 8);
        }

        game.setBoard(board);

        piece.rotateCW();

        if (successful) {
            assertEquals('R', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[1], piece.getConfiguration());
        } else {
            assertEquals('0', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[0], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCW0toR0() {
        testRotateCW0toR(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR1() {
        testRotateCW0toR(PieceITestBoards.CW_0_TO_R_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR2() {
        testRotateCW0toR(PieceITestBoards.CW_0_TO_R_2, true, true);
        assertEquals(2, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR3() {
        testRotateCW0toR(PieceITestBoards.CW_0_TO_R_3, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR4() {
        testRotateCW0toR(PieceITestBoards.COMMON_CW_0_AND_2_1, true, true);
        assertEquals(2, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR5() {
        testRotateCW0toR(PieceITestBoards.COMMON_CW_0_AND_2_2, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR6() {
        testRotateCW0toR(PieceITestBoards.COMMON_FULL_0_AND_2, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCWRto2(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 8);
        }

        testRotateCW0toR(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCW();
        if (successful) {
            assertEquals('2', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[2], piece.getConfiguration());
        } else {
            assertEquals('R', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[1], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCWRto20() {
        testRotateCWRto2(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto21() {
        testRotateCWRto2(PieceITestBoards.CW_R_TO_2_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto22() {
        testRotateCWRto2(PieceITestBoards.CW_R_TO_2_2, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto23() {
        testRotateCWRto2(PieceITestBoards.CW_R_TO_2_3, true, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto24() {
        testRotateCWRto2(PieceITestBoards.COMMON_CW_R_AND_L_1, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto25() {
        testRotateCWRto2(PieceITestBoards.COMMON_CW_R_AND_L_2, true, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto26() {
        testRotateCWRto2(PieceITestBoards.COMMON_FULL_R_AND_L, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCW2toL(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 8);
        }

        testRotateCWRto2(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCW();
        if (successful) {
            assertEquals('L', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[3], piece.getConfiguration());
        } else {
            assertEquals('2', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[2], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCW2toL0() {
        testRotateCW2toL(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL1() {
        testRotateCW2toL(PieceITestBoards.CW_2_TO_L_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL2() {
        testRotateCW2toL(PieceITestBoards.CW_2_TO_L_2, true, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL3() {
        testRotateCW2toL(PieceITestBoards.CW_2_TO_L_3, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL4() {
        piece.teleport(4, 7);
        testRotateCW2toL(PieceITestBoards.COMMON_CW_0_AND_2_1, false, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL5() {
        piece.teleport(4, 7);
        testRotateCW2toL(PieceITestBoards.COMMON_CW_0_AND_2_2, false, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL6() {
        piece.teleport(4, 7);
        testRotateCW2toL(PieceITestBoards.COMMON_FULL_0_AND_2, false, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCWLto0(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 8);
        }

        testRotateCW2toL(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCW();
        if (successful) {
            assertEquals('0', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[0], piece.getConfiguration());
        } else {
            assertEquals('L', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[3], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCWLto00() {
        testRotateCWLto0(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto01() {
        testRotateCWLto0(PieceITestBoards.CW_L_TO_0_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto02() {
        testRotateCWLto0(PieceITestBoards.CW_L_TO_0_2, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto03() {
        testRotateCWLto0(PieceITestBoards.CW_L_TO_0_3, true, true);
        assertEquals(2, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto04() {
        piece.teleport(5, 8);
        testRotateCWLto0(PieceITestBoards.COMMON_CW_R_AND_L_2, false, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(10, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto05() {
        piece.teleport(5, 8);
        testRotateCWLto0(PieceITestBoards.COMMON_CW_R_AND_L_1, false, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto06() {
        piece.teleport(5, 8);
        testRotateCWLto0(PieceITestBoards.COMMON_FULL_R_AND_L, false, false);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }
    
    /*
     * COUNTER-CLOCKWISE ROTATION
     */

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCCW0toL(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 9);
        }

        game.setBoard(board);

        piece.rotateCounterCW();

        if (successful) {
            assertEquals('L', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[3], piece.getConfiguration());
        } else {
            assertEquals('0', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[0], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCCW0toL0() {
        testRotateCCW0toL(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL1() {
        testRotateCCW0toL(PieceITestBoards.CCW_0_TO_L_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL2() {
        testRotateCCW0toL(PieceITestBoards.CCW_0_TO_L_2, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL3() {
        testRotateCCW0toL(PieceITestBoards.CCW_0_TO_L_3, true, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL4() {
        piece.teleport(4, 8);
        testRotateCCW0toL(PieceITestBoards.COMMON_CCW_0_AND_2_1, false, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL5() {
        piece.teleport(4, 8);
        testRotateCCW0toL(PieceITestBoards.COMMON_CCW_0_AND_2_2, false, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL6() {
        piece.teleport(4, 8);
        testRotateCCW0toL(PieceITestBoards.COMMON_FULL_0_AND_2, false, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCounterCWLto2(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(5, 8);
        }

        testRotateCCW0toL(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCounterCW();
        if (successful) {
            assertEquals('2', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[2], piece.getConfiguration());
        } else {
            assertEquals('L', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[3], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCounterCWLto20() {
        testRotateCounterCWLto2(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto21() {
        testRotateCounterCWLto2(PieceITestBoards.CCW_L_TO_2_1, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto22() {
        testRotateCounterCWLto2(PieceITestBoards.CCW_L_TO_2_2, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto23() {
        testRotateCounterCWLto2(PieceITestBoards.CCW_L_TO_2_3, true, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto24() {
        testRotateCounterCWLto2(PieceITestBoards.COMMON_CCW_R_AND_L_1, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto25() {
        testRotateCounterCWLto2(PieceITestBoards.COMMON_CCW_R_AND_L_2, true, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto26() {
        testRotateCounterCWLto2(PieceITestBoards.COMMON_FULL_R_AND_L, true, false);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCCW2toR(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 7);
        }

        testRotateCounterCWLto2(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCounterCW();
        if (successful) {
            assertEquals('R', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[1], piece.getConfiguration());
        } else {
            assertEquals('2', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[2], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCCW2toR0() {
        testRotateCCW2toR(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR1() {
        testRotateCCW2toR(PieceITestBoards.CCW_2_TO_R_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR2() {
        testRotateCCW2toR(PieceITestBoards.CCW_2_TO_R_2, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR3() {
        testRotateCCW2toR(PieceITestBoards.CCW_2_TO_R_3, true, true);
        assertEquals(2, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR4() {
        testRotateCCW2toR(PieceITestBoards.COMMON_CCW_0_AND_2_1, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR5() {
        testRotateCCW2toR(PieceITestBoards.COMMON_CCW_0_AND_2_2, true, true);
        assertEquals(2, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR6() {
        testRotateCCW2toR(PieceITestBoards.COMMON_FULL_0_AND_2, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCounterCWRto0(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(3, 8);
        }

        testRotateCCW2toR(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCounterCW();
        if (successful) {
            assertEquals('0', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[0], piece.getConfiguration());
        } else {
            assertEquals('R', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_I[1], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCounterCWRto00() {
        testRotateCounterCWRto0(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto01() {
        testRotateCounterCWRto0(PieceITestBoards.CCW_R_TO_0_1, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto02() {
        testRotateCounterCWRto0(PieceITestBoards.CCW_R_TO_0_2, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto03() {
        testRotateCounterCWRto0(PieceITestBoards.CCW_R_TO_0_3, true, true);
        assertEquals(2, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto04() {
        piece.teleport(4, 8);
        testRotateCounterCWRto0(PieceITestBoards.COMMON_CCW_R_AND_L_2, false, true);
        assertEquals(6, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto05() {
        piece.teleport(4, 8);
        testRotateCounterCWRto0(PieceITestBoards.COMMON_CCW_R_AND_L_1, false, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(10, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto06() {
        piece.teleport(4, 8);
        testRotateCounterCWRto0(PieceITestBoards.COMMON_FULL_R_AND_L, false, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }
}