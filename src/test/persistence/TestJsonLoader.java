package persistence;

import model.Game;
import model.Score;
import model.TestBoard;
import model.TestGame;
import model.configs.PieceConfigs;
import model.exceptions.InvalidOrientationException;
import model.pieces.Piece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// MODELLED OFF OF https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git, thanks!
class TestJsonLoader extends TestBoard {
    Game game;

    @BeforeEach
    void runBefore() {
        game = new Game();
    }

    @Test
    void testReaderNoFile() {
        try {
            JsonLoader jsonLoader = new JsonLoader("./data/hello/not-real.json", game);
            jsonLoader.loadGameState();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGameState() {
        try {
            String file = "./data/test/testLoaderEmptyGameState.json";

            JsonLoader loader = new JsonLoader(file, game);
            loader.loadGameState();

            assertBoardsEqual(new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH], game.getBoard());
            assertTrue(game.getStatus());

            assertBagValid(20, game);
            
            assertEquals(0, game.getScore());

            assertNotNull(game.getCurrentPiece());

            assertEquals("NONE", game.getHeldPiece());

            assertEquals(0, game.getHighScores().size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGameState() {
        try {
            String file = "./data/test/testLoaderGeneralGameState.json";

            JsonLoader loader = new JsonLoader(file, game);
            loader.loadGameState();


            assertBoardsEqual(TestGame.SAMPLE_BOARD_1, game.getBoard());

            assertEquals(1, game.getScore());

            Piece currentPiece = game.getCurrentPiece();
            assertEquals(5, currentPiece.getCenterX());
            assertEquals(4, currentPiece.getCenterY());
            assertEquals('R', currentPiece.getOrientation());
            assertEquals(PieceConfigs.PIECE_T[1], currentPiece.getConfiguration());

            assertEquals("I", game.getHeldPiece());
            assertBagValid(18, game);
            
            List<Score> highScores = game.getHighScores();
            assertEquals(1, highScores.size());
            Score score = highScores.get(0);
            assertEquals("Andrew", score.getName());
            assertEquals(6565, score.getScore());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testLoaderOrientation2() {
        try {
            game.getCurrentPiece().rotateCW();
            game.getCurrentPiece().rotateCW();

            String file = "./data/test/testLoaderOrientation2.json";

            JsonWriter writer = new JsonWriter(game);
            writer.write(file);

            Game newGame = new Game();

            JsonLoader loader = new JsonLoader(file, newGame);
            loader.loadGameState();

            Piece currentPiece = newGame.getCurrentPiece();
            assertEquals('2', currentPiece.getOrientation());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testLoaderOrientationL() {
        try {
            game.getCurrentPiece().rotateCounterCW();

            String file = "./data/test/testLoaderOrientationL.json";

            JsonWriter writer = new JsonWriter(game);
            writer.write(file);

            Game newGame = new Game();

            JsonLoader loader = new JsonLoader(file, newGame);
            loader.loadGameState();

            Piece currentPiece = newGame.getCurrentPiece();
            assertEquals('L', currentPiece.getOrientation());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testLoaderInvalidOrientation() {
        try {
            game.getCurrentPiece().rotateCounterCW();

            String file = "./data/test/testLoaderOrientationInvalid.json";

            JsonLoader loader = new JsonLoader(file, game);
            loader.loadGameState();

            fail("InvalidOrientationException should have been thrown");
        } catch (InvalidOrientationException e) {
            // pass
        } catch (IOException e) {
            fail("This exception should not have been thrown");
        }
    }
}