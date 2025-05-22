package model.configs;

import model.Seg;

/*
 * CLASS RESPONSIBLE FOR THE "CONFIGURATION" OF EACH PIECE
 * What this is is an array of segments, each with positions relative to the
 * centerX and centerY of the actual piece. 
 * 
 * For example, Seg(-1, 5) would be a segment 1 to the left of the centerX, and 5 below the centerY
 * (yes, positive y is "down")
 * 
 * INDEX 0 = ORIENTATION '0'
 * INDEX 1 = ORIENTATION 'R'
 * INDEX 2 = ORIENTATION '2'
 * INDEX 3 = ORIENTATION 'L'
 */
public final class PieceConfigs {

    /*
     * I PIECE
     */

    public static final Seg[][] PIECE_I = {
        {
            new Seg(-1, 0),
            new Seg(0, 0),
            new Seg(1, 0),
            new Seg(2, 0)
        },
        {
            new Seg(1, -1),
            new Seg(1, 0),
            new Seg(1, 1),
            new Seg(1, 2)
        },
        {
            new Seg(-1, 1),
            new Seg(0, 1),
            new Seg(1, 1),
            new Seg(2, 1)
        },
        {
            new Seg(0, -1),
            new Seg(0, 0),
            new Seg(0, 1),
            new Seg(0, 2)
        }
    };

    /*
     * T PIECE
     */
    
    public static final Seg[][] PIECE_T = {
        {
            new Seg(-1, 0),
            new Seg(0, 0),
            new Seg(0, -1),
            new Seg(1, 0)
        },
        {
            new Seg(0, -1),
            new Seg(0, 0),
            new Seg(0, 1),
            new Seg(1, 0)
        },
        {
            new Seg(-1, 0),
            new Seg(0, 0),
            new Seg(0, 1),
            new Seg(1, 0)
        },
        {
            new Seg(0, -1),
            new Seg(0, 0),
            new Seg(0, 1),
            new Seg(-1, 0)
        }
    };

    /*
     * O PIECE
     */

    public static final Seg[] PIECE_O = {
        new Seg(0, 0),
        new Seg(1, 0),
        new Seg(0, -1),
        new Seg(1, -1)
    };

    /*
     * J PIECE
     */

    public static final Seg[][] PIECE_J = {
        {
            new Seg(-1, 0),
            new Seg(-1, -1),
            new Seg(0, 0),
            new Seg(1, 0)
        },
        {
            new Seg(0, -1),
            new Seg(1, -1),
            new Seg(0, 0),
            new Seg(0, 1)
        },
        {
            new Seg(-1, 0),
            new Seg(0, 0),
            new Seg(1, 0),
            new Seg(1, 1)
        },
        {
            new Seg(0, -1),
            new Seg(0, 0),
            new Seg(0, 1),
            new Seg(-1, 1)
        }
    };

    /*
     * L PIECE
     */

    public static final Seg[][] PIECE_L = {
        {
            new Seg(-1, 0),
            new Seg(1, -1),
            new Seg(0, 0),
            new Seg(1, 0)
        },
        {
            new Seg(0, -1),
            new Seg(1, 1),
            new Seg(0, 0),
            new Seg(0, 1)
        },
        {
            new Seg(-1, 0),
            new Seg(0, 0),
            new Seg(1, 0),
            new Seg(-1, 1)
        },
        {
            new Seg(0, -1),
            new Seg(0, 0),
            new Seg(0, 1),
            new Seg(-1, -1)
        }
    };

    /*
     * Z PIECE
     */

    public static final Seg[][] PIECE_Z = {
        {
            new Seg(0, 0),
            new Seg(0, -1),
            new Seg(1, 0),
            new Seg(-1, -1)
        },
        {
            new Seg(0, 0),
            new Seg(0, 1),
            new Seg(1, 0),
            new Seg(1, -1)
        },
        {
            new Seg(0, 0),
            new Seg(-1, 0),
            new Seg(0, 1),
            new Seg(1, 1)
        },
        {
            new Seg(0, 0),
            new Seg(0, -1),
            new Seg(-1, 0),
            new Seg(-1, 1)
        }
    };

    /*
     * S PIECE
     */

    public static final Seg[][] PIECE_S = {
        {
            new Seg(0, 0),
            new Seg(0, -1),
            new Seg(1, -1),
            new Seg(-1, 0)
        },
        {
            new Seg(0, 0),
            new Seg(1, 1),
            new Seg(0, -1),
            new Seg(1, 0)
        },
        {
            new Seg(0, 0),
            new Seg(1, 0),
            new Seg(0, 1),
            new Seg(-1, 1)
        },
        {
            new Seg(0, 0),
            new Seg(-1, -1),
            new Seg(-1, 0),
            new Seg(0, 1)
        }
    };

}
