package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Game;
import model.Score;
import model.exceptions.InvalidOrientationException;
import model.pieces.Piece;

// reads from a JSON file that reads and sets various game states
public class JsonLoader {
    private JSONObject json;
    private Game game;

    // EFFECTS: new JsonToGame, reading JSONObject from file in a directory.
    //          will be modifying game
    public JsonLoader(String file, Game game) throws IOException {
        this.game = game;

        String data = readFile(file);
        json = new JSONObject(data);
    }

    // EFFECTS: loads a game state to game
    //          this includes:
    //           - board
    //           - current piece
    //           - hold piece
    //           - bag (next pieces)
    //           - score
    //           - high scores
    public void loadGameState() {
        loadBoard();

        game.setScore(json.getInt("score"));

        loadOtherPieces();
        loadCurrentPiece();

        loadHighScores();
    }

    // EFFECTS: loads board from json
    private void loadBoard() {
        JSONArray boardJson = json.getJSONArray("board");
        String[][] board = new String[Game.BOARD_HEIGHT][Game.BOARD_WIDTH];
        
        for (int y = 0; y < board.length; y++) {
            JSONArray row = (JSONArray) boardJson.get(y);

            for (int x = 0; x < board[y].length; x++) {
                if (row.get(x).equals(null)) {
                    board[y][x] = null;
                } else {
                    board[y][x] = row.get(x).toString();
                }
            }
        }

        game.setBoard(board);
    }

    // EFFECTS: loads otherPieces from json
    private void loadOtherPieces() {
        JSONObject otherPieces = json.getJSONObject("otherPieces");
        List<Object> bagObjects = otherPieces.getJSONArray("bag").toList();
        ArrayList<String> bag = new ArrayList<>();

        bagObjects.stream().forEach(p -> bag.add(p.toString()));

        game.setBag(bag);
        game.setHeldPiece(otherPieces.getString("heldPiece"));
    }

    // EFFECTS: loads currentPiece from json
    // Throws InvalidPieceException if piece is not valid
    private void loadCurrentPiece() {
        JSONObject currentPiece = json.getJSONObject("currentPiece");

        game.setCurrentPieceByType(currentPiece.getString("type"));
        Piece piece = game.getCurrentPiece();

        switch (currentPiece.getInt("orientation")) {
            case '0':
                // do nothing!
                break;
            case 'R':
                piece.rotateCW();
                break;
            case '2':
                piece.rotateCW();
                piece.rotateCW();
                break;
            case 'L':
                piece.rotateCounterCW();
                break;
            default:
                throw new InvalidOrientationException();
        }

        int posX = currentPiece.getInt("posX");
        int posY = currentPiece.getInt("posY");
        piece.teleport(posX, posY);
    }

    // EFFECTS: loads only high scores to game
    public void loadHighScores() {
        JSONArray highScores = json.getJSONArray("highScores");

        for (Object o : highScores) {
            JSONObject scoreJson = (JSONObject) o;

            String name = scoreJson.getString("name");
            int val = scoreJson.getInt("score");
            Score score = new Score(name, val);

            game.addHighScore(score);
        }
    }

    // EFFECTS: reads source file as string and returns it
    // taken from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git, thanks!
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
}
