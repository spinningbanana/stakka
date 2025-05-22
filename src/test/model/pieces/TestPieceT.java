package model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestGame;
import model.configs.PieceConfigs;
import model.configs.TestConfig;
import model.pieces.boards.PieceTTestBoards;

public class TestPieceT extends TestThreeWidePiece {

    @BeforeEach
    public void runBefore() {
        game = new Game();

        game.changeFirstInBag("T");
        game.nextPiece();
        piece = game.getCurrentPiece();
    }

    @Test
    public void testPieceConstructor() {
        assertEquals("T", piece.toString());
        assertEquals(game, piece.getGame());
        assertEquals(4, piece.getCenterX());
        assertEquals(1, piece.getCenterY());
        assertEquals('0', piece.getOrientation());
        TestConfig.assertConfigEquals(PieceConfigs.PIECE_T[0], piece.getConfiguration());
    }

    @Test
    public void testObstructedByBlockAwayFromPivot() {
        game.addGarbage(5, 1);
        assertTrue(piece.checkIsObstructed(4, 1));
    }

    @Test
    public void testHardDrop() {
        piece.hardDrop();
        assertBoardEqualsConfig("T", 4, 22, PieceConfigs.PIECE_T[0]);
    }

    @Test
    public void testHardDropOnObstacle0() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);
        
        piece.move(-1, 0);
        piece.hardDrop();

        assertBoardEqualsConfig("T", 3, 21, PieceConfigs.PIECE_T[0]);
    }

    @Test
    public void testHardDropOnObstacle1() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.hardDrop();

        assertBoardEqualsConfig("T", 5, 18, PieceConfigs.PIECE_T[0]);
    }

    @Test
    public void testHardDropOnObstacle2() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.move(1, 0);
        piece.hardDrop();

        assertBoardEqualsConfig("T", 6, 18, PieceConfigs.PIECE_T[0]);
    }

    @Test
    public void testHardDropOnObstacle3() {
        game.setBoard(TestGame.SAMPLE_BOARD_1);

        piece.move(1, 0);
        piece.move(1, 0);
        piece.move(1, 0);
        piece.hardDrop();

        assertBoardEqualsConfig("T", 7, 18, PieceConfigs.PIECE_T[0]);
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
            assertEquals(PieceConfigs.PIECE_T[1], piece.getConfiguration());
        } else {
            assertEquals('0', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[0], piece.getConfiguration());
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
        testRotateCW0toR(PieceTTestBoards.CW_0_TO_R_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR2() {
        testRotateCW0toR(PieceTTestBoards.CW_0_TO_R_2, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR3() {
        testRotateCW0toR(PieceTTestBoards.CW_0_TO_R_3, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(7, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR4() {
        testRotateCW0toR(PieceTTestBoards.CW_0_TO_R_4, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(10, piece.getCenterY());
    }

    @Test
    public void testRotateCW0toR5() {
        testRotateCW0toR(PieceTTestBoards.FULL_0, true, false);
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
            assertEquals(PieceConfigs.PIECE_T[2], piece.getConfiguration());
        } else {
            assertEquals('R', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[1], piece.getConfiguration());
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
        testRotateCWRto2(PieceTTestBoards.CW_R_TO_2_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto22() {
        testRotateCWRto2(PieceTTestBoards.CW_R_TO_2_2, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto23() {
        testRotateCWRto2(PieceTTestBoards.CW_R_TO_2_3, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto24() {
        testRotateCWRto2(PieceTTestBoards.CW_R_TO_2_4, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto25() {
        testRotateCWRto2(PieceTTestBoards.CW_R_TO_2_5, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCWRto26() {
        testRotateCWRto2(PieceTTestBoards.FULL_R, true, false);
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
            assertEquals(PieceConfigs.PIECE_T[3], piece.getConfiguration());
        } else {
            assertEquals('2', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[2], piece.getConfiguration());
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
        testRotateCW2toL(PieceTTestBoards.CW_2_TO_L_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL2() {
        testRotateCW2toL(PieceTTestBoards.CW_2_TO_L_2, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL3() {
        testRotateCW2toL(PieceTTestBoards.CW_2_TO_L_3, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(10, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL4() {
        testRotateCW2toL(PieceTTestBoards.CW_2_TO_L_4, true, true);
        assertEquals(5, piece.getCenterX());
        assertEquals(10, piece.getCenterY());
    }

    @Test
    public void testRotateCW2toL5() {
        testRotateCW2toL(PieceTTestBoards.FULL_2, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
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
            assertEquals(PieceConfigs.PIECE_T[0], piece.getConfiguration());
        } else {
            assertEquals('L', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[3], piece.getConfiguration());
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
        testRotateCWLto0(PieceTTestBoards.CW_L_TO_0_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto02() {
        testRotateCWLto0(PieceTTestBoards.CW_L_TO_0_2, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto03() {
        testRotateCWLto0(PieceTTestBoards.CW_L_TO_0_3, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto04() {
        testRotateCWLto0(PieceTTestBoards.CW_L_TO_0_4, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto05() {
        testRotateCWLto0(PieceTTestBoards.CW_L_TO_0_5, true, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(6, piece.getCenterY());
    }

    @Test
    public void testRotateCWLto06() {
        testRotateCWLto0(PieceTTestBoards.FULL_L, true, false);
        assertEquals(4, piece.getCenterX());
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
            piece.teleport(4, 8);
        }

        game.setBoard(board);

        piece.rotateCounterCW();

        if (successful) {
            assertEquals('L', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[3], piece.getConfiguration());
        } else {
            assertEquals('0', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[0], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCCW0toL0() {
        testRotateCCW0toL(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL1() {
        testRotateCCW0toL(PieceTTestBoards.CCW_0_TO_L_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL2() {
        piece.teleport(3, 8);

        testRotateCCW0toL(PieceTTestBoards.CCW_0_TO_L_2, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL3() {
        piece.teleport(3, 9);

        testRotateCCW0toL(PieceTTestBoards.CCW_0_TO_L_3, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL4() {
        piece.teleport(3, 6);

        testRotateCCW0toL(PieceTTestBoards.CCW_0_TO_L_4, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW0toL5() {
        testRotateCCW0toL(PieceTTestBoards.FULL_0, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCounterCWLto2(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 8);
        }

        testRotateCCW0toL(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCounterCW();
        if (successful) {
            assertEquals('2', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[2], piece.getConfiguration());
        } else {
            assertEquals('L', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[3], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCounterCWLto20() {
        testRotateCounterCWLto2(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto21() {
        testRotateCounterCWLto2(PieceTTestBoards.CCW_L_TO_2_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto22() {
        piece.teleport(5, 8);

        testRotateCounterCWLto2(PieceTTestBoards.CCW_L_TO_2_2, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto23() {
        piece.teleport(5, 8);

        testRotateCounterCWLto2(PieceTTestBoards.CCW_L_TO_2_3, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(9, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto24() {
        piece.teleport(4, 10);

        testRotateCounterCWLto2(PieceTTestBoards.CCW_L_TO_2_4, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto25() {
        piece.teleport(5, 10);

        testRotateCounterCWLto2(PieceTTestBoards.CCW_L_TO_2_5, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWLto26() {
        testRotateCounterCWLto2(PieceTTestBoards.FULL_L, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCCW2toR(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 8);
        }

        testRotateCounterCWLto2(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCounterCW();
        if (successful) {
            assertEquals('R', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[1], piece.getConfiguration());
        } else {
            assertEquals('2', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[2], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCCW2toR0() {
        testRotateCCW2toR(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR1() {
        testRotateCCW2toR(PieceTTestBoards.CCW_2_TO_R_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR2() {
        piece.teleport(5, 8);

        testRotateCCW2toR(PieceTTestBoards.CCW_2_TO_R_2, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR3() {
        piece.teleport(4, 6);

        testRotateCCW2toR(PieceTTestBoards.CCW_2_TO_R_3, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR4() {
        piece.teleport(5, 6);
        
        testRotateCCW2toR(PieceTTestBoards.CCW_2_TO_R_4, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCCW2toR5() {
        testRotateCCW2toR(PieceTTestBoards.FULL_2, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    // REQUIRES: board is not null
    // EFFECTS: sets up pieces, teleports the piece according to the parameters,
    //          then rotates to the required orientation
    //          and checks if the rotation was successful or not depending on parameters
    private void testRotateCounterCWRto0(String[][] board, boolean teleport, boolean successful) {
        if (teleport) {
            piece.teleport(4, 8);
        }

        testRotateCCW2toR(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], false, true);

        game.setBoard(board);

        piece.rotateCounterCW();
        if (successful) {
            assertEquals('0', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[0], piece.getConfiguration());
        } else {
            assertEquals('R', piece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[1], piece.getConfiguration());
        }
    }

    @Test
    public void testRotateCounterCWRto00() {
        testRotateCounterCWRto0(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto01() {
        testRotateCounterCWRto0(PieceTTestBoards.CCW_R_TO_0_1, true, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto02() {
        piece.teleport(3, 8);

        testRotateCounterCWRto0(PieceTTestBoards.CCW_R_TO_0_2, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto03() {
        piece.teleport(3, 7);

        testRotateCounterCWRto0(PieceTTestBoards.CCW_R_TO_0_3, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto04() {
        piece.teleport(3, 10);

        testRotateCounterCWRto0(PieceTTestBoards.CCW_R_TO_0_4, false, true);
        assertEquals(3, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto05() {
        piece.teleport(3, 10);

        testRotateCounterCWRto0(PieceTTestBoards.CCW_R_TO_0_5, false, true);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }

    @Test
    public void testRotateCounterCWRto06() {
        testRotateCounterCWRto0(PieceTTestBoards.FULL_R, true, false);
        assertEquals(4, piece.getCenterX());
        assertEquals(8, piece.getCenterY());
    }
}