package model.pieces;

import model.Game;
import model.configs.PieceConfigs;

// class responsible for the rotation of the O piece
public class PieceO extends Piece {

    // REQUIRES: game is not null
    // EFFECTS: does code in super class
    //          then, sets the piece type and configuration to "O"
    public PieceO(Game game) {
        super(game);

        piece = "O";
        configuration = PieceConfigs.PIECE_O;
    }

    // EFFECTS: clockwise rotation: '0' -> 'R'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCW0() {
        orientation = 'R';
    }

    // EFFECTS: clockwise rotation: 'R' -> '2'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCWR() {
        orientation = '2';
    }

    // EFFECTS: clockwise rotation: '2' -> 'L'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCW2() {
        orientation = 'L';
    }

    // EFFECTS: clockwise rotation: 'L' -> '0'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCWL() {
        orientation = '0';
    }

    // COUNTER-CLOCKWISE ROTATION

    // EFFECTS: counter-clockwise rotation: '0' -> 'L'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCounterCW0() {
        orientation = 'L';
    }

    // EFFECTS: counter-clockwise rotation: 'L' -> '2'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCounterCWL() {
        orientation = '2';
    }

    // EFFECTS: counter-clockwise rotation: '2' -> 'R'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCounterCW2() {
        orientation = 'R';
    }

    // EFFECTS: counter-clockwise rotation: 'R' -> '0'
    //          adheres to SRS rotation rules
    @Override
    protected void rotateCounterCWR() {
        orientation = '0';
    }
}
