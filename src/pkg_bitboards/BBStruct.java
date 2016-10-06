package pkg_bitboards;


import static pkg_bitboards.Constants.*;
import static pkg_bitboards.MethodUtils.printString2;
import static pkg_bitboards.Moves.*;

/**
 * Created by jainu on 6/10/16.
 */
public class BBStruct {
    public long
        mWP = 0L, mWR = 0L, mWN = 0L, mWB = 0L, mWQ = 0L, mWK = 0L,
        mBP = 0L, mBR = 0L, mBN = 0L, mBB = 0L, mBQ = 0L, mBK = 0L;

    private long mPIECES_W_CANT_CAPTURE, mCAPTURABLE_W, mPIECES_B_CANT_CAPTURE, mCAPTURABLE_B, mOCCUPIEDSQ;
    private String mHistory = "";

    public BBStruct(long mWP, long mWR, long mWN, long mWB, long mWQ, long mWK, long mBP, long mBR, long mBN, long mBB, long mBQ, long mBK) {
        this.mWP = mWP;
        this.mWR = mWR;
        this.mWN = mWN;
        this.mWB = mWB;
        this.mWQ = mWQ;
        this.mWK = mWK;
        this.mBP = mBP;
        this.mBR = mBR;
        this.mBN = mBN;
        this.mBB = mBB;
        this.mBQ = mBQ;
        this.mBK = mBK;
        updateTempCap();
    }

    /***
     * assigning global bitboards in the temp object's bitboards
     */
    public BBStruct() {
        this.mWP = WP;
        this.mWR = WR;
        this.mWN = WN;
        this.mWB = WB;
        this.mWQ = WQ;
        this.mWK = WK;
        this.mBP = BP;
        this.mBR = BR;
        this.mBN = BN;
        this.mBB = BB;
        this.mBQ = BQ;
        this.mBK = BK;
        updateTempCap();
    }

    public void updateTempCap()
    {
        mPIECES_W_CANT_CAPTURE = (mWP|mWR|mWN|mWB|mWQ|mWK|mBK);
        mCAPTURABLE_B = (mBP|mBR|mBN|mBB|mBQ);
        mPIECES_B_CANT_CAPTURE = (mBP|mBR|mBN|mBB|mBQ|mBK|mWK);
        mCAPTURABLE_W = (mWP|mWR|mWN|mWB|mWQ);
        mOCCUPIEDSQ = (mWP|mWR|mWN|mWB|mWQ|mWK|mBP|mBR|mBN|mBB|mBQ|mBK);
    }

    public long unsafeForWhite()
    {
        {
            long unsafemoves, piecepositions;

            // positions where black pawns can capture are unsafe for white
            unsafemoves = (mBP >> 9) & ~FILE_H;
            unsafemoves |= (mBP >> 7) & ~FILE_A;

            // positions where queen/bishop can move diagonally are unsafe for white
            piecepositions = mBQ|mBB;
            while(piecepositions != 0)
            {
                int oldposition = Long.numberOfTrailingZeros(piecepositions);
                long newmoves = DiagonalMoves(oldposition);
                unsafemoves |= newmoves;
                piecepositions = piecepositions & (piecepositions-1);
            }

            // positions where queen/rook can move horizontally are unsafe for white
            piecepositions = mBQ|mBR;
            while(piecepositions != 0)
            {
                int oldposition = Long.numberOfTrailingZeros(piecepositions);
                long newmoves = HVMoves(oldposition);
                unsafemoves |= newmoves;
                piecepositions = piecepositions & (piecepositions-1);
            }

            // positions where king can move, are unsafe for white
            piecepositions = mBK;
            while(piecepositions != 0)
            {
                int oldposition = Long.numberOfTrailingZeros(piecepositions);
                long newmoves = KingMoves(oldposition);
                unsafemoves |= newmoves;
                piecepositions = piecepositions & (piecepositions-1);
            }

            // positions where knight can move, are unsafe for white
            piecepositions = mBN;
            while(piecepositions != 0)
            {
                int oldposition = Long.numberOfTrailingZeros(piecepositions);
                long newmoves = KnightMoves(oldposition);
                unsafemoves |= newmoves;
                piecepositions = piecepositions & (piecepositions-1);
            }
            printString2("unsafe moves from bbstruct", unsafemoves);
            return unsafemoves;
        }
    }

    public long unsafeForBlack()
    {
        long unsafemoves, piecepositions;

        // positions where white pawns can capture are unsafe for black
        unsafemoves = (mWP << 9) & ~FILE_A;
        unsafemoves |= (mWP << 7) & ~FILE_H;

        // positions where white queen/bishop can move diagonally are unsafe for black
        piecepositions = mWQ|mWB;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = DiagonalMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white queen/rook can move horizontally are unsafe for black
        piecepositions = mWQ|mWR;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = HVMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white king can move, are unsafe for black
        piecepositions = mWK;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = KingMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white knight can move, are unsafe for black
        piecepositions = mWN;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = KnightMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        return unsafemoves;
    }
}
