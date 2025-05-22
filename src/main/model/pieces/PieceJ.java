package model.pieces;

import model.Game;
import model.configs.PieceConfigs;

// class responsible for the rotation of the J piece
public class PieceJ extends Piece {

    // REQUIRES: game is not null
    // EFFECTS: does code in super class
    //          then, sets the piece type and configuration to "J"
    public PieceJ(Game game) {
        super(game);

        piece = "J";
        pieceConfigs = PieceConfigs.PIECE_J;
        configuration = pieceConfigs[0];
    }
}
