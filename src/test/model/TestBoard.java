package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

// common methods used for testing a game board
public abstract class TestBoard {
    // REQUIRES: both boards are exactly the same size
    // EFFECTS: true if all elements in board are the same, false otherwise
    protected void assertBoardsEqual(String[][] expected, String[][] actual) {
        for (int y = 0; y < expected.length; y++) {
            for (int x = 0; x < expected[y].length; x++) {
                assertEquals(expected[y][x], actual[y][x]);
            }
        }
    }

    // REQUIRES: a valid game and piece
    // EFFECTS: sets the current piece to piece, and the next piece to "T"
    protected void forceAPiece(Game game, String piece) {
        game.getBag().set(0, piece);
        game.getBag().set(1, "T");
        game.nextPiece();
    }

    // REQUIRES: game is not null, size >= 7
    // EFFECTS: checks if the inputted bag is the correct size and has all the pieces
    protected void assertBagValid(int size, Game game) {
        List<String> bag = game.getBag();
        assertEquals(size, bag.size());
        assertTrue(bag.contains("I"));
        assertTrue(bag.contains("O"));
        assertTrue(bag.contains("T"));
        assertTrue(bag.contains("J"));
        //assertTrue(bag.contains("L"));
        //assertTrue(bag.contains("S"));
        //assertTrue(bag.contains("Z"));
    }
}
