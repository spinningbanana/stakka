package model;

// FOR THE PIECE, represents the coordinates of a segment of a piece with
// respect to its center
public class Seg {
    private int posX;
    private int posY;

    // EFFECTS: sets the posX and posY
    public Seg(int x, int y) {
        posX = x;
        posY = y;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
