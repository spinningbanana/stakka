package model.pieces;

import model.Game;
import model.configs.PieceConfigs;

// class responsible for the rotation of the J piece
public class PieceS extends Piece {

    // REQUIRES: game is not null
    // EFFECTS: does code in super class
    //          then, sets the piece type and configuration to "S"
    public PieceS(Game game) {
        super(game);

        piece = "S";
        pieceConfigs = PieceConfigs.PIECE_S;
        configuration = pieceConfigs[0];
    }
}
