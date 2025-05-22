package persistence;

import model.Game;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
// MODELLED OFF OF https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git, thanks!
public class JsonWriter {
    private static final int TAB = 4;

    private Game game;
    private PrintWriter writer;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(Game game) {
        this.game = game;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    //                        be opened for writing
    //          writes JSON representation of game state to file
    //          closes writer
    public void write(String destination) throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));

        JSONObject json = game.toJson();
        saveToFile(json.toString(TAB));

        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
