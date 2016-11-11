package main_package;

import static utils.Constants.*;
import static utils.Constants.VAL_ROOK;

class Rating {

    /**
     * evaluation function that returns a heuristic-value for a given position
     * @return score for that position
     */
    static int evaluate(BBStruct bb)
    {
        return evaluateAttack(bb) + evaluateMaterial(bb) + evaluateMoveability(bb) + evaluatePosition(bb);
    }

    private static int evaluatePosition(BBStruct bb) {
        return 0;
    }

    private static int evaluateMoveability(BBStruct bb) {
        return 0;
    }

    private static int evaluateMaterial(BBStruct bb) {
        int numBB = Long.bitCount(bb.mBB);
        int numBN = Long.bitCount(bb.mBN);
        int numBQ = Long.bitCount(bb.mBQ);
        int numBP = Long.bitCount(bb.mBP);
        int numBR = Long.bitCount(bb.mBR);
        int numWB = Long.bitCount(bb.mWB);
        int numWN = Long.bitCount(bb.mWN);
        int numWQ = Long.bitCount(bb.mWQ);
        int numWP = Long.bitCount(bb.mWP);
        int numWR = Long.bitCount(bb.mWR);

        int blackscore = numBB * VAL_BISHOP + numBN * VAL_KNIGHT + numBQ * VAL_QUEEN + numBP * VAL_PAWN + numBR * VAL_ROOK;
        int whitescore = numWB * VAL_BISHOP + numWN * VAL_KNIGHT + numWQ * VAL_QUEEN + numWP * VAL_PAWN + numWR * VAL_ROOK;
        return whitescore - blackscore;
    }

    private static int evaluateAttack(BBStruct bb) {
        return 0;
    }

}
