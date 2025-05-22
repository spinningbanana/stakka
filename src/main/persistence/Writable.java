package persistence;

import org.json.JSONObject;

// taken from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// thanks!
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
