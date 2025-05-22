package model.pieces;

import model.Game;
import model.Seg;
import model.exceptions.InvalidOrientationException;

/* 
 * class which represents the current piece
 * 
 * responsible for rotations as well, this game will use the SRS system
 * see: https://harddrop.com/wiki/SRS
*/
public abstract class Piece {
    protected Game game;
    protected char orientation;
    protected String piece;

    protected Seg[][] pieceConfigs;
    protected Seg[] configuration;

    protected int centerX;
    protected int centerY;

    // REQUIRES: game is not null
    // EFFECTS: creates a new piece, setup reference to a Game
    //          and orientation starts at '0'
    public Piece(Game game) {
        this.game = game;
        this.orientation = '0';

        centerX = Game.SPAWN_X;
        centerY = Game.SPAWN_Y;
    }

    // REQUIRES: the new position will keep the piece within the board
    // MODIFIES: this
    // EFFECTS: teleports the center of a piece to a coordinate, for testing purposes
    public void teleport(int x, int y) {
        centerX = x;
        centerY = y;
    }

    // EFFECTS: checks if the inputted coordinates will cause the piece
    //          to be obstructed by something on the board
    public boolean checkIsObstructed(int x, int y) {
        for (Seg segment : configuration) {
            int posX = x + segment.getX();
            int posY = y + segment.getY();

            if (game.getBoard(posX, posY) != null) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: x is [-1, 1], y is [0, 1]
    // MODIFIES: this
    // EFFECTS: moves the piece in the specified direction, 
    //          if path is not obstructed
    public void move(int x, int y) {
        int newX = centerX + x;
        int newY = centerY + y;

        if (!checkIsObstructed(newX, newY)) {
            centerX = newX;
            centerY = newY;
        }
    }

    // MODIFIES: this
    // EFFECTS: moves the piece down by 1, adding score to Game depending on what type of
    //          drop was performed
    public void move(boolean isHardDrop) {
        int oldY = centerY;
        move(0, 1);

        if (centerY != oldY) {
            game.addScoreDrop(isHardDrop);
        }
    }

    // MODIFIES: this
    // EFFECTS: drops the piece to the lowest it can go instantly, then place it on the board!
    public void hardDrop() {
        int dropHeight = getCenterY();

        while (!checkIsObstructed(centerX, centerY + 1)) {
            move(true);
        }

        game.updateBoard(dropHeight);
    }

    /*
     * CLOCKWISE ROTATION
     */

    // MODIFIES: this
    // EFFECTS: rotates the piece clockwise
    // Throws InvalidOrientationException if orientation is not valid
    public void rotateCW() {
        switch (orientation) {
            case '0':
                rotateCW0();
                break;
            case 'R':
                rotateCWR();
                break;
            case '2':
                rotateCW2();
                break;
            case 'L':
                rotateCWL();
                break;
            default:
                throw new InvalidOrientationException();
        }
    }

    // EFFECTS: clockwise rotation: '0' -> 'R'
    //          adheres to SRS rotation rules
    protected void rotateCW0() {
        orientation = 'R';
        configuration = pieceConfigs[1];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX - 1, centerY - 1)) {
            centerX -= 1;
            centerY -= 1;
        } else if (!checkIsObstructed(centerX, centerY + 2)) {
            centerY += 2;
        } else if (!checkIsObstructed(centerX - 1, centerY + 2)) {
            centerX -= 1;
            centerY += 2;
        } else {
            orientation = '0';
            configuration = pieceConfigs[0];
        }
    }

    // EFFECTS: clockwise rotation: 'R' -> '2'
    //          adheres to SRS rotation rules
    protected void rotateCWR() {
        orientation = '2';
        configuration = pieceConfigs[2];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX + 1, centerY + 1)) {
            centerX += 1;
            centerY += 1;
        } else if (!checkIsObstructed(centerX, centerY - 2)) {
            centerY -= 2;
        } else if (!checkIsObstructed(centerX + 1, centerY - 2)) {
            centerX += 1;
            centerY -= 2;
        } else {
            orientation = 'R';
            configuration = pieceConfigs[1];
        }
    }

    // EFFECTS: clockwise rotation: '2' -> 'L'
    //          adheres to SRS rotation rules
    protected void rotateCW2() {
        orientation = 'L';
        configuration = pieceConfigs[3];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX + 1, centerY - 1)) {
            centerX += 1;
            centerY -= 1;
        } else if (!checkIsObstructed(centerX, centerY + 2)) {
            centerY += 2;
        } else if (!checkIsObstructed(centerX + 1, centerY + 2)) {
            centerX += 1;
            centerY += 2;
        } else {
            orientation = '2';
            configuration = pieceConfigs[2];
        }
    }

    // EFFECTS: clockwise rotation: 'L' -> '0'
    //          adheres to SRS rotation rules
    protected void rotateCWL() {
        orientation = '0';
        configuration = pieceConfigs[0];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX - 1, centerY + 1)) {
            centerX -= 1;
            centerY += 1;
        } else if (!checkIsObstructed(centerX, centerY - 2)) {
            centerY -= 2;
        } else if (!checkIsObstructed(centerX - 1, centerY - 2)) {
            centerX -= 1;
            centerY -= 2;
        } else {
            orientation = 'L';
            configuration = pieceConfigs[3];
        }
    }

    /*
     * COUNTER-CLOCKWISE ROTATION
     */

    // MODIFIES: this
    // EFFECTS: rotates the piece counter-clockwise
    // Throws InvalidOrientationException if orientation is not valid
    public void rotateCounterCW() {
        switch (orientation) {
            case '0':
                rotateCounterCW0();
                break;
            case 'L':
                rotateCounterCWL();
                break;
            case '2':
                rotateCounterCW2();
                break;
            case 'R':
                rotateCounterCWR();
                break;
            default:
                throw new InvalidOrientationException();
        }
    }

    // EFFECTS: counter-clockwise rotation: '0' -> 'L'
    //          adheres to SRS rotation rules
    protected void rotateCounterCW0() {
        orientation = 'L';
        configuration = pieceConfigs[3];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX + 1, centerY - 1)) {
            centerX += 1;
            centerY -= 1;
        } else if (!checkIsObstructed(centerX, centerY + 2)) {
            centerY += 2;
        } else if (!checkIsObstructed(centerX + 1, centerY + 2)) {
            centerX += 1;
            centerY += 2;
        } else {
            orientation = '0';
            configuration = pieceConfigs[0];
        }
    }

    // EFFECTS: counter-clockwise rotation: 'L' -> '2'
    //          adheres to SRS rotation rules
    protected void rotateCounterCWL() {
        orientation = '2';
        configuration = pieceConfigs[2];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX - 1, centerY + 1)) {
            centerX -= 1;
            centerY += 1;
        } else if (!checkIsObstructed(centerX, centerY - 2)) {
            centerY -= 2;
        } else if (!checkIsObstructed(centerX - 1, centerY - 2)) {
            centerX -= 1;
            centerY -= 2;
        } else {
            orientation = 'L';
            configuration = pieceConfigs[3];
        }
    }

    // EFFECTS: counter-clockwise rotation: '2' -> 'R'
    //          adheres to SRS rotation rules
    protected void rotateCounterCW2() {
        orientation = 'R';
        configuration = pieceConfigs[1];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX - 1, centerY - 1)) {
            centerX -= 1;
            centerY -= 1;
        } else if (!checkIsObstructed(centerX, centerY + 2)) {
            centerY += 2;
        } else if (!checkIsObstructed(centerX - 1, centerY + 2)) {
            centerX -= 1;
            centerY += 2;
        } else {
            orientation = '2';
            configuration = pieceConfigs[2];
        }
    }

    // EFFECTS: counter-clockwise rotation: 'R' -> '0'
    //          adheres to SRS rotation rules
    protected void rotateCounterCWR() {
        orientation = '0';
        configuration = pieceConfigs[0];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX + 1, centerY + 1)) {
            centerX += 1;
            centerY += 1;
        } else if (!checkIsObstructed(centerX, centerY - 2)) {
            centerY -= 2;
        } else if (!checkIsObstructed(centerX + 1, centerY - 2)) {
            centerX += 1;
            centerY -= 2;
        } else {
            orientation = 'R';
            configuration = pieceConfigs[1];
        }
    }

    /*
     * OTHER THINGS, GETTERS AND SETTERS
     */

    public String toString() {
        return piece;
    }

    // MODIFIES: this
    // EFFECTS: sets the orientation FOR TESTING PURPOSES ONLY!
    public void setOrientation(char orientation) {
        this.orientation = orientation;
    }

    public char getOrientation() {
        return orientation;
    }

    public Seg[] getConfiguration() {
        return configuration;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Game getGame() {
        return game;
    }
}
