package persistence;

import model.Game;
import model.Score;
import model.TestBoard;
import model.TestGame;
import model.configs.PieceConfigs;
import model.pieces.Piece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// MODELLED OFF OF https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git, thanks!
class TestJsonWriter extends TestBoard {
    Game game;

    @BeforeEach
    void runBefore() {
        game = new Game();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            new JsonWriter(game).write("./data/my\0illegal:fileName.json");
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGameState() {
        try {
            String file = "./data/test/testWriterEmptyGameState.json";

            JsonWriter writer = new JsonWriter(game);
            writer.write(file);

            JsonLoader loader = new JsonLoader(file, game);
            loader.loadGameState();

            assertBoardsEqual(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], game.getBoard());
            assertTrue(game.getStatus());

            assertBagValid(20, game);
            
            assertEquals(0, game.getScore());

            assertNotNull(game.getCurrentPiece());
            assertEquals('0', game.getCurrentPiece().getOrientation());

            assertEquals("NONE", game.getHeldPiece());

            assertEquals(0, game.getHighScores().size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGameState() {
        try {
            game.addHighScore(new Score("Andrew", 6565));
            game.setBoard(TestGame.SAMPLE_BOARD_1);
            game.addScoreDrop(false);

            forceAPiece(game, "I");
            game.cycleHoldPiece();

            game.getCurrentPiece().teleport(5, 4);
            game.getCurrentPiece().rotateCW();

            String file = "./data/test/testWriterGeneralGameState.json";

            JsonWriter writer = new JsonWriter(game);
            writer.write(file);

            helperSoCheckstyleDoesntGetMad(file);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private void helperSoCheckstyleDoesntGetMad(String file) throws IOException {
        Game newGame = new Game();

        JsonLoader loader = new JsonLoader(file, newGame);
        loader.loadGameState();

        assertBoardsEqual(TestGame.SAMPLE_BOARD_1, newGame.getBoard());

        assertEquals(1, newGame.getScore());

        Piece currentPiece = newGame.getCurrentPiece();
        assertEquals(5, currentPiece.getCenterX());
        assertEquals(4, currentPiece.getCenterY());
        assertEquals('R', currentPiece.getOrientation());
        assertEquals(PieceConfigs.PIECE_T[1], currentPiece.getConfiguration());

        assertEquals("I", newGame.getHeldPiece());
        assertBagValid(18, newGame);
        
        List<Score> highScores = newGame.getHighScores();
        assertEquals(1, highScores.size());
        Score score = highScores.get(0);
        assertEquals("Andrew", score.getName());
        assertEquals(6565, score.getScore());
    }
}