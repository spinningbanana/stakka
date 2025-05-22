package model.pieces;

import model.Game;
import model.configs.PieceConfigs;

// class responsible for the rotation of the J piece
public class PieceL extends Piece {

    // REQUIRES: game is not null
    // EFFECTS: does code in super class
    //          then, sets the piece type and configuration to "L"
    public PieceL(Game game) {
        super(game);

        piece = "L";
        pieceConfigs = PieceConfigs.PIECE_L;
        configuration = pieceConfigs[0];
    }
}
