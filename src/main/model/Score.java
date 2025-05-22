package model;

// class representing a score, and the player who holds the score
public class Score {

    String name;
    int score;

    // EFFECTS: sets a score and assigns a name to it
    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
