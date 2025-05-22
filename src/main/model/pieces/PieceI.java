package model.pieces;

import model.Game;
import model.configs.PieceConfigs;

// class responsible for the rotation of the I piece
public class PieceI extends Piece {

    // REQUIRES: game is not null
    // EFFECTS: does code in super class
    //          then, sets the piece type and configuration to "I"
    public PieceI(Game game) {
        super(game);

        piece = "I";
        pieceConfigs = PieceConfigs.PIECE_I;
        configuration = pieceConfigs[0];
    }

    /*
     * CLOCKWISE ROTATION
     */

    // EFFECTS: clockwise rotation: "0" -> "R"
    //          adheres to SRS rotation rules
    protected void rotateCW0() {
        orientation = 'R';
        configuration = pieceConfigs[1];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX - 2, centerY)) {
            centerX -= 2;
        } else if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX - 2, centerY + 1)) {
            centerX -= 2;
            centerY += 1;
        } else if (!checkIsObstructed(centerX + 1, centerY - 2)) {
            centerX += 1;
            centerY -= 2;
        } else {
            orientation = '0';
            configuration = pieceConfigs[0];
        }
    }

    // EFFECTS: clockwise rotation: "R" -> "2"
    //          adheres to SRS rotation rules
    protected void rotateCWR() {
        orientation = '2';
        configuration = pieceConfigs[2];

        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }
        
        if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX + 2, centerY)) {
            centerX += 2;
        } else if (!checkIsObstructed(centerX - 1, centerY - 2)) {
            centerX -= 1;
            centerY -= 2;
        } else if (!checkIsObstructed(centerX + 2, centerY + 1)) {
            centerX += 2;
            centerY += 1;
        } else {
            orientation = 'R';
            configuration = pieceConfigs[1];
        }
    }

    // EFFECTS: clockwise rotation: "2" -> "L"
    //          adheres to SRS rotation rules
    protected void rotateCW2() {
        orientation = 'L';
        configuration = pieceConfigs[3];
        
        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 2, centerY)) {
            centerX += 2;
        } else if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX + 2, centerY - 1)) {
            centerX += 2;
            centerY -= 1;
        } else if (!checkIsObstructed(centerX - 1, centerY + 2)) {
            centerX -= 1;
            centerY += 2;
        } else {
            orientation = '2';
            configuration = pieceConfigs[2];
        }
    }

    // EFFECTS: clockwise rotation: "L" -> "0"
    //          adheres to SRS rotation rules
    protected void rotateCWL() {
        orientation = '0';
        configuration = pieceConfigs[0];
        
        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX - 2, centerY)) {
            centerX -= 2;
        } else if (!checkIsObstructed(centerX + 1, centerY + 2)) {
            centerX += 1;
            centerY += 2;
        } else if (!checkIsObstructed(centerX - 2, centerY - 1)) {
            centerX -= 2;
            centerY -= 1;
        } else {
            orientation = 'L';
            configuration = pieceConfigs[3];
        }
    }

    /*
     * COUNTER-CLOCKWISE ROTATION
     */

    // EFFECTS: counter-clockwise rotation: "0" -> "L"
    //          adheres to SRS rotation rules
    protected void rotateCounterCW0() {
        orientation = 'L';
        configuration = pieceConfigs[3];
        
        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX + 2, centerY)) {
            centerX += 2;
        } else if (!checkIsObstructed(centerX - 1, centerY - 2)) {
            centerX -= 1;
            centerY -= 2;
        } else if (!checkIsObstructed(centerX + 2, centerY + 1)) {
            centerX += 2;
            centerY += 1;
        } else {
            orientation = '0';
            configuration = pieceConfigs[0];
        }
    }

    // EFFECTS: counter-clockwise rotation: "L" -> "2"
    //          adheres to SRS rotation rules
    protected void rotateCounterCWL() {
        orientation = '2';
        configuration = pieceConfigs[2];
        
        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX - 2, centerY)) {
            centerX -= 2;
        } else if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX - 2, centerY + 1)) {
            centerX -= 2;
            centerY += 1;
        } else if (!checkIsObstructed(centerX + 1, centerY - 2)) {
            centerX += 1;
            centerY -= 2;
        } else {
            orientation = 'L';
            configuration = pieceConfigs[3];
        }
    }

    // EFFECTS: counter-clockwise rotation: "2" -> "R"
    //          adheres to SRS rotation rules
    protected void rotateCounterCW2() {
        orientation = 'R';
        configuration = pieceConfigs[1];
        
        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 1, centerY)) {
            centerX += 1;
        } else if (!checkIsObstructed(centerX - 2, centerY)) {
            centerX -= 2;
        } else if (!checkIsObstructed(centerX + 1, centerY + 2)) {
            centerX += 1;
            centerY += 2;
        } else if (!checkIsObstructed(centerX - 2, centerY - 1)) {
            centerX -= 2;
            centerY -= 1;
        } else {
            orientation = '2';
            configuration = pieceConfigs[2];
        }
    }

    // EFFECTS: counter-clockwise rotation: "R" -> "0"
    //          adheres to SRS rotation rules
    protected void rotateCounterCWR() {
        orientation = '0';
        configuration = pieceConfigs[0];
        
        if (!checkIsObstructed(centerX, centerY)) {
            return;
        }

        if (!checkIsObstructed(centerX + 2, centerY)) {
            centerX += 2;
        } else if (!checkIsObstructed(centerX - 1, centerY)) {
            centerX -= 1;
        } else if (!checkIsObstructed(centerX + 2, centerY - 1)) {
            centerX += 2;
            centerY -= 1;
        } else if (!checkIsObstructed(centerX - 1, centerY + 2)) {
            centerX -= 1;
            centerY += 2;
        } else {
            orientation = 'R';
            configuration = pieceConfigs[1];
        }
    }
}
