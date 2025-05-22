package model.pieces;

import model.Game;
import model.configs.PieceConfigs;

// class responsible for the rotation of the J piece
public class PieceZ extends Piece {

    // REQUIRES: game is not null
    // EFFECTS: does code in super class
    //          then, sets the piece type and configuration to "Z"
    public PieceZ(Game game) {
        super(game);

        piece = "Z";
        pieceConfigs = PieceConfigs.PIECE_Z;
        configuration = pieceConfigs[0];
    }
}
