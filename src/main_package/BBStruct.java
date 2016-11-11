package main_package;

import static utils.Constants.*;
import static main_package.Moves.*;
import static utils.MethodUtils.*;

class BBStruct {

    long    mWP = 0L, mWR = 0L, mWN = 0L, mWB = 0L, mWQ = 0L, mWK = 0L,
            mBP = 0L, mBR = 0L, mBN = 0L, mBB = 0L, mBQ = 0L, mBK = 0L;

    private boolean mCASTLEW_KSIDE, mCASTLEW_QSIDE, mCASTLEB_KSIDE, mCASTLEB_QSIDE;
    private boolean mmoveW;

    private long mPIECES_W_CANT_CAPTURE, mCAPTURABLE_W, mPIECES_B_CANT_CAPTURE, mCAPTURABLE_B, mOCCUPIEDSQ;
    private String mHistory = "";

    /***
     * assigning global bitboards in the temp object's bitboards
     */
    BBStruct() {
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
        this.mCASTLEB_KSIDE = CASTLEB_KSIDE;
        this.mCASTLEB_QSIDE = CASTLEB_QSIDE;
        this.mCASTLEW_KSIDE = CASTLEW_KSIDE;
        this.mCASTLEW_QSIDE = CASTLEW_QSIDE;
        this.mmoveW = moveW;
        updateTempCap();
    }

    BBStruct(BBStruct mybb) {
        this.mWP = mybb.mWP;
        this.mWR = mybb.mWR;
        this.mWN = mybb.mWN;
        this.mWB = mybb.mWB;
        this.mWQ = mybb.mWQ;
        this.mWK = mybb.mWK;
        this.mBP = mybb.mBP;
        this.mBR = mybb.mBR;
        this.mBN = mybb.mBN;
        this.mBB = mybb.mBB;
        this.mBQ = mybb.mBQ;
        this.mBK = mybb.mBK;
        this.mCASTLEB_KSIDE = mybb.mCASTLEB_KSIDE;
        this.mCASTLEB_QSIDE = mybb.mCASTLEB_QSIDE;
        this.mCASTLEW_KSIDE = mybb.mCASTLEW_KSIDE;
        this.mCASTLEW_QSIDE = mybb.mCASTLEW_QSIDE;
        this.mmoveW = mybb.mmoveW;
        updateTempCap();
    }

    void updateTempCap()
    {
        mPIECES_W_CANT_CAPTURE = (mWP|mWR|mWN|mWB|mWQ|mWK|mBK);
        mCAPTURABLE_B = (mBP|mBR|mBN|mBB|mBQ);
        mPIECES_B_CANT_CAPTURE = (mBP|mBR|mBN|mBB|mBQ|mBK|mWK);
        mCAPTURABLE_W = (mWP|mWR|mWN|mWB|mWQ);
        mOCCUPIEDSQ = (mWP|mWR|mWN|mWB|mWQ|mWK|mBP|mBR|mBN|mBB|mBQ|mBK);
    }

    long unsafeForWhite()
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
            long newmoves = mDiagonalMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where queen/rook can move horizontally are unsafe for white
        piecepositions = mBQ|mBR;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = mHVMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where king can move, are unsafe for white
        piecepositions = mBK;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = mKingMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where knight can move, are unsafe for white
        piecepositions = mBN;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = mKnightMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

//        printString2("unsafe white moves from bbstruct", unsafemoves);
        return unsafemoves;

    }


    long unsafeForBlack()
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
            long newmoves = mDiagonalMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white queen/rook can move horizontally are unsafe for black
        piecepositions = mWQ|mWR;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = mHVMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white king can move, are unsafe for black
        piecepositions = mWK;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = mKingMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white knight can move, are unsafe for black
        piecepositions = mWN;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = mKnightMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        return unsafemoves;
    }

    private long mHVMoves(int i)
    {
        long slider = 1L << i;
        long horizontalPossibilities =(
                ( ( mOCCUPIEDSQ & RankMask[i/8] ) - 2*slider )
                        ^ rev(rev( mOCCUPIEDSQ & RankMask[i/8] ) - 2*rev(slider))
        ) & RankMask[i/8];

        long verticalPossibilities = (
                ( ( mOCCUPIEDSQ & FileMask[i%8] ) - 2*slider )
                        ^ rev(rev( mOCCUPIEDSQ & FileMask[i%8] ) - 2*rev(slider))
        ) & FileMask[i%8];
        return (horizontalPossibilities|verticalPossibilities);
    }

    private long mDiagonalMoves(int i)
    {
        long slider = 1L << i;

        long fwdDiaPossibilities =(
                ( ( mOCCUPIEDSQ & ForwardDiagonalMask[ i/8 + 7-(i%8) ] ) - 2*slider )
                        ^ rev(rev( mOCCUPIEDSQ & ForwardDiagonalMask[ i/8 + 7-(i%8) ] ) - 2*rev(slider))
        ) & ForwardDiagonalMask[ i/8 + 7-(i%8) ];

        long backDiaPossibilities =(
                ( ( mOCCUPIEDSQ & BackDiagonalMask[i/8 + i%8] ) - 2*slider )
                        ^ rev(rev( mOCCUPIEDSQ & BackDiagonalMask[i/8 + i%8] ) - 2*rev(slider))
        ) & BackDiagonalMask[i/8 + i%8];

        return (fwdDiaPossibilities | backDiaPossibilities);
    }

    private static long mKnightMoves(int oldposition)
    {
        long newmoves;
        if(oldposition > 18)
            newmoves = (KnightMask << (oldposition-18));
        else
            newmoves = (KnightMask >> (18-oldposition));

        if(oldposition%8 < 4)
            newmoves = newmoves & ~(FILE_G | FILE_H);
        else
            newmoves = newmoves & ~(FILE_A | FILE_B);

        return newmoves;
    }

    private static long mKingMoves(int oldposition)
    {
        long newmoves;
        if(oldposition > 9)
            newmoves = (KingMask << (oldposition-9));
        else
            newmoves = (KingMask >> (9-oldposition));

        if(oldposition%8 == 0)
            newmoves = newmoves & ~FILE_H;
        else if(oldposition%8 == 7)
            newmoves = newmoves & ~FILE_A;

        return newmoves;
    }

    private boolean isBBCastleMove(String move)
    {

        if( mmoveW )
        {
            if( ( mCASTLEW_KSIDE && move.equals(" 0406") )
                    ||( mCASTLEW_QSIDE && move.equals(" 0402") )   )
                return true;
        }
        else
        {
            if( ( mCASTLEB_KSIDE && move.equals(" 7476") )
                    ||( mCASTLEB_QSIDE && move.equals(" 7472") )   )
                return true;
        }

        return false;
    }


    private void updatePromotePawnBitBoard(long pos, char promotionPiece) {
        if(mmoveW)
        {
            mWP &= ~pos;
            switch (promotionPiece)
            {
                case W_BISHOP:  mWB |= pos; break;
                case W_KNIGHT:  mWN |= pos; break;
                case W_QUEEN:   mWQ |= pos; break;
                case W_ROOK:    mWR |= pos; break;
                default: System.out.println("cant reach here !");
            }
        }
        else
        {
            mBP &= ~pos;
            switch (promotionPiece)
            {
                case B_BISHOP:  mBB |= pos; break;
                case B_KNIGHT:  mBN |= pos; break;
                case B_QUEEN:   mBQ |= pos; break;
                case B_ROOK:    mBR |= pos; break;
                default: System.out.println("cant reach here !");
            }
        }
        updateTempCap();
    }

    public String getMoves()
    {
        if(mmoveW)
            return possiblemB(mWB|mWQ, IAMWHITE) + possiblemN(mWN, IAMWHITE) + possiblemR(mWR|mWQ, IAMWHITE) + possiblemP(mWP, IAMWHITE) + possiblemK(mWK, IAMWHITE);
        else
            return possiblemB(mBB|mBQ, IAMBLACK) + possiblemN(mBN, IAMBLACK) + possiblemR(mBR|mBQ, IAMBLACK) + possiblemP(mBP, IAMBLACK) + possiblemK(mBK, IAMBLACK);
    }


    public void makeMove(String move)
    {
        int oldRow, oldCol, newRow, newCol;

        if( isPromotionMove(move) )
        {
            oldCol = Character.getNumericValue(move.charAt(1));
            newCol = Character.getNumericValue(move.charAt(2));
            char promotionPiece = move.charAt(3);
            if(mmoveW)
            {
                oldRow = 6;
                newRow = 7;
            }
            else
            {
                oldRow = 1;
                newRow = 0;
            }
            updateBitBoard(oldRow, oldCol, newRow, newCol);
            updatePromotePawnBitBoard(getBitBoardCorrespondingTo((newRow * 8) + newCol), promotionPiece);
        }
        else if( isEnpassMove(move)  )
        {

            oldCol = Character.getNumericValue(move.charAt(1));
            newCol = Character.getNumericValue(move.charAt(2));
            if(mmoveW)
            {
                oldRow = 4;
                newRow = 5;
            }
            else
            {
                oldRow = 3;
                newRow = 2;
            }

            updateBitBoard(oldRow, oldCol, newRow, newCol);
            updatePawnCapBitBoard( getBitBoardCorrespondingTo((oldRow * 8) + newCol) );

        }
        else if( isBBCastleMove(move))
        {
            oldRow = Character.getNumericValue(move.charAt(1));
            oldCol = Character.getNumericValue(move.charAt(2));
            newRow = Character.getNumericValue(move.charAt(3));
            newCol = Character.getNumericValue(move.charAt(4));

            updateBitBoard(oldRow, oldCol, newRow, newCol);

            int rook_oldCol;
            int rook_newCol;
            if( newCol == 6 )
            {
                rook_oldCol = 7;
                rook_newCol = 5;
            }
            else
            {
                rook_oldCol = 0;
                rook_newCol = 3;
            }

            updateBitBoard(oldRow, rook_oldCol, newRow, rook_newCol);
        }
        else    // is general move
        {
            oldRow = Character.getNumericValue(move.charAt(1));
            oldCol = Character.getNumericValue(move.charAt(2));
            newRow = Character.getNumericValue(move.charAt(3));
            newCol = Character.getNumericValue(move.charAt(4));

            updateBitBoard(oldRow, oldCol, newRow, newCol);
        }
        mmoveW = !mmoveW;
    }


    private void updatePawnCapBitBoard(long pos)
    {
        if( mmoveW )
            mBP &= ~pos;
        else
            mWP &= ~pos;

        updateTempCap();
    }

    private void updateBitBoard(int oldRow, int oldCol, int newRow, int newCol)
    {
//        System.out.println("BBS.updateBitBoard: " + oldRow + oldCol+newRow+newCol);
        long oldPos = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = getBitBoardCorrespondingTo((newRow * 8) + newCol);

        if( mmoveW )
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (mBB & newPos) != 0)      mBB &= ~newPos;
            else if( (mBN & newPos) != 0) mBN &= ~newPos;
            else if( (mBP & newPos) != 0) mBP &= ~newPos;
            else if( (mBQ & newPos) != 0) mBQ &= ~newPos;
            else if( (mBR & newPos) != 0) mBR &= ~newPos;
            else if( (mBK & newPos) != 0) mBK &= ~newPos;

            // removing mypiece from oldposition and moving to newposition
            if( (mWB & oldPos) != 0)      { mWB &= ~oldPos; mWB |= newPos; }
            else if( (mWN & oldPos) != 0) { mWN &= ~oldPos; mWN |= newPos; }
            else if( (mWP & oldPos) != 0) { mWP &= ~oldPos; mWP |= newPos; }
            else if( (mWQ & oldPos) != 0) { mWQ &= ~oldPos; mWQ |= newPos; }
            else if( (mWR & oldPos) != 0) { mWR &= ~oldPos; mWR |= newPos;
                                            if( mCASTLEW_QSIDE && ((mWR & CASTLE_ROOKS[0]) == 0) )
                                                mCASTLEW_QSIDE = false;
                                            else if ( mCASTLEW_KSIDE && ( (mWR & CASTLE_ROOKS[1]) == 0) )
                                                mCASTLEW_KSIDE = false;  }
            else if( (mWK & oldPos) != 0) { mWK &= ~oldPos; mWK |= newPos;
                                            mCASTLEW_KSIDE = false;
                                            mCASTLEW_QSIDE = false;  }
            else System.out.println("BBStruct.updateBitBoard: shouldn't reach here!");
        }
        else
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (mWB & newPos) != 0)      mWB &= ~newPos;
            else if( (mWN & newPos) != 0) mWN &= ~newPos;
            else if( (mWP & newPos) != 0) mWP &= ~newPos;
            else if( (mWQ & newPos) != 0) mWQ &= ~newPos;
            else if( (mWR & newPos) != 0) mWR &= ~newPos;
            else if( (mWK & newPos) != 0) mWK &= ~newPos;

            // removing mypiece from oldposition and moving to newposition
            if( (mBB & oldPos) != 0)      { mBB &= ~oldPos; mBB |= newPos; }
            else if( (mBN & oldPos) != 0) { mBN &= ~oldPos; mBN |= newPos; }
            else if( (mBP & oldPos) != 0) { mBP &= ~oldPos; mBP |= newPos; }
            else if( (mBQ & oldPos) != 0) { mBQ &= ~oldPos; mBQ |= newPos; }
            else if( (mBR & oldPos) != 0) { mBR &= ~oldPos; mBR |= newPos;
                                            if( mCASTLEB_QSIDE && ((mBR & CASTLE_ROOKS[2]) == 0) )
                                                mCASTLEB_QSIDE = false;
                                            else if ( mCASTLEB_KSIDE && ((mBR & CASTLE_ROOKS[3]) == 0) )
                                                mCASTLEB_KSIDE = false;  }
            else if( (mBK & oldPos) != 0) { mBK &= ~oldPos; mBK |= newPos;
                                            mCASTLEB_KSIDE = false;
                                            mCASTLEB_QSIDE = false;  }
            else System.out.println("BBStruct.updateBitBoard: shouldn't reach here!");
        }

        mHistory = ""+ oldRow + oldCol + newRow + newCol;
        updateTempCap();
    }

// TODO: make common class of Moves.java and BBStruct.java containing following methods
/* ---------------- possible moves for each piece  ---------------- starts  ..........customised from Moves.java*/
    private String possiblemP(long pawnpos, char whoAmI)
    {
        String list;

        if(whoAmI == IAMWHITE)
        {
            list = possiblemWP(pawnpos) + possibleEnPass(pawnpos, whoAmI);
            list = getSafeMovesFrom(list);
        }
        else
        {
            list = possiblemBP(pawnpos) + possibleEnPass(pawnpos, whoAmI);
            list = getSafeMovesFrom(list);
        }

        return list;
    }

    private String possibleEnPass(long pawnpos, char whoAmI)
    {
        if( mHistory.length() == 0 ) return "";
        String list = "";
        byte hist_oldRow = Byte.parseByte(mHistory.substring(0,1));
        byte hist_oldCol = Byte.parseByte(mHistory.substring(1,2));
        byte hist_newRow = Byte.parseByte(mHistory.substring(2,3));
        byte hist_newCol = Byte.parseByte(mHistory.substring(3,4));
        byte histPos = (byte) (hist_newRow * 8 + hist_newCol);

        byte oldCol, newCol;
        if( (Math.abs(hist_oldRow - hist_newRow) == 2) &&
                (hist_oldCol == hist_newCol) )      // if last move was (y+2)x(y)x
        {

            if ( whoAmI == IAMWHITE )  // if black pawn was moved in the last move
            {
                // if black pawn was on left of white pawn
                if ( ((pawnpos << 1) & mBP & ~FILE_A & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol - 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
                else if( ((pawnpos >> 1) & mBP & ~FILE_H & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol + 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
            }
            else
            {
                if ( ((pawnpos << 1) & mWP & ~FILE_A & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol - 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
                else if( ((pawnpos >> 1) & mWP & ~FILE_H & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol + 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
            }

        }
        //System.out.println("movelist from enpass method : " +list);
        return list;
    }

    private String possiblemWP(long pawnpos)
    {
        String list = "";
        long moves;

        /*  << 9 :: white pawn can capture right if there is a capturable black piece and that piece is not on FILE_A after shift */
        moves = (pawnpos << 9) & mCAPTURABLE_B & ~RANK_8 & ~FILE_A;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, -1);

        /*  << 7 :: white pawn can capture left if there is a capturable black piece and that piece is not on FILE_H after shift */
        moves = (pawnpos << 7) & mCAPTURABLE_B & ~RANK_8 & ~FILE_H;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 1);

        /*  >> 8 :: pawn can be pushed 1 position if it doesn't go on rank 8 after push and there are no piece below its current rank */
        moves = (pawnpos << 8) & ~RANK_8 & ~mOCCUPIEDSQ;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 0);

        /*  << 16 :: pawn can be pushed 2 positions if it is on rank 2 and there are no pieces on rank 4 or rank 3 for the corresponding pawn */
        moves = ( (pawnpos & RANK_2) << 16 ) &  ~( (mOCCUPIEDSQ & RANK_4) | ((mOCCUPIEDSQ & RANK_3) << 8) );
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -2, 0);

        /*  << 9 :: right capture + promotion */
        moves = (pawnpos << 9) & mCAPTURABLE_B & RANK_8 & ~FILE_A;
        list += getPromotionPaths(moves, -1);

        /*  << 7 :: left capture + promotion */
        moves = (pawnpos << 7) & mCAPTURABLE_B & RANK_8 & ~FILE_H;
        list += getPromotionPaths(moves, 1);
        /*  << 8 :: simple promotion */
        moves = (pawnpos << 8) & RANK_8 & ~mOCCUPIEDSQ;
        list += getPromotionPaths(moves, 0);

        return list;
    }

    private String possiblemBP(long pawnpos)
    {
        String list = "";
        long moves;

        /*  >> 9 :: black pawn can capture right if there is a capturable black piece and that piece is not on FILE_H after shift */
        moves = (pawnpos >> 9) & mCAPTURABLE_W & ~RANK_1 & ~FILE_H;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 1, 1);

        /*  >> 7 :: black pawn can capture left if there is a capturable black piece and that piece is not on FILE_A after shift */
        moves = (pawnpos >> 7) & mCAPTURABLE_W & ~RANK_1 & ~FILE_A;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 1, -1);

        /*  >> 8 :: pawn can be pushed 1 position if it doesn't go on rank 1 after push and there are no piece below its current rank */
        moves = (pawnpos >> 8) & ~RANK_1 & ~mOCCUPIEDSQ;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 1, 0);

        /*  >> 16 :: pawn can be pushed 2 positions if it is on rank 7 and there are no pieces on rank 5 or rank 6 for the corresponding pawn */
        moves = ( (pawnpos & RANK_7) >> 16 ) &  ~( (mOCCUPIEDSQ & RANK_5) | ((mOCCUPIEDSQ & RANK_6) >> 8) );
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 2, 0);

        /*  >> 9 :: right capture + promotion */
        moves = (pawnpos >> 9) & mCAPTURABLE_W & RANK_1 & ~FILE_H;
        list += getPromotionPaths(moves, 1);

        /*  >> 7 :: left capture + promotion */
        moves = (pawnpos >> 7) & mCAPTURABLE_W & RANK_1 & ~FILE_A;
        list += getPromotionPaths(moves, -1);

        /*  >> 8 :: simple promotion */
        moves = (pawnpos >> 8) & RANK_1 & ~mOCCUPIEDSQ;
        list += getPromotionPaths(moves, 0);

        return list;
    }

    private String possiblemR(long ROOK, char whoAmI)
    {
        String list = "";

        list += getMoveListFromBitBoards(ROOK, 'H', whoAmI);
        list = getSafeMovesFrom(list);

        return list;
    }

    private String possiblemB(long BISHOP, char whoAmI)
    {
        String list = "";

        list += getMoveListFromBitBoards(BISHOP, 'D', whoAmI);
        list = getSafeMovesFrom(list);

        return list;
    }

    private String possiblemN(long KNIGHT, char whoAmI)
    {
        String list = "";

        list += getMoveListFromBitBoards(KNIGHT, 'N', whoAmI);
        list = getSafeMovesFrom(list);

        return list;
    }

    private String possiblemK(long KING, char whoAmI)
    {
        String list = "";

        list += getMoveListFromBitBoards(KING, 'K', whoAmI);
        list = getSafeMovesFrom(list);

        return list;
    }

    /**
     * This method returns horizontal or vertical moves according to the choice ('H' or 'D')
     * (oldRow, oldCol, newRow, newCol) is the move format that we will follow for our movelist
     * @param PIECE_BB
     * @param choice
     * @return String of movelist
     */
    private String getMoveListFromBitBoards(long PIECE_BB, char choice, char whoAmI)
    {
        String list = "";
        long moves = PIECE_BB;
        long newmoves;
        int newRow, newCol, oldRow, oldCol, oldposition;
        long PIECES_I_CANT_CAPTURE =
                ( whoAmI == IAMWHITE )  ?   mPIECES_W_CANT_CAPTURE :  mPIECES_B_CANT_CAPTURE;

        switch (choice) {
            case 'H':
                while(moves != 0)
                {
                    oldposition = Long.numberOfTrailingZeros(moves);
                    oldRow = oldposition / 8;
                    oldCol = oldposition % 8 ;
                    newmoves = mHVMoves(oldposition) & ~PIECES_I_CANT_CAPTURE;

                    while(newmoves != 0)
                    {
                        oldposition = Long.numberOfTrailingZeros(newmoves);
                        newRow = oldposition / 8;
                        newCol = oldposition % 8 ;
                        list += " " + oldRow + oldCol + newRow + newCol;
                        newmoves = newmoves & (newmoves-1);
                    }

                    moves = moves & (moves-1);
                }   break;
            case 'D':
                while(moves != 0)
                {
                    oldposition = Long.numberOfTrailingZeros(moves);
                    oldRow = oldposition / 8;
                    oldCol = oldposition % 8 ;
                    newmoves = mDiagonalMoves(oldposition) & ~PIECES_I_CANT_CAPTURE;

                    while(newmoves != 0)
                    {
                        oldposition = Long.numberOfTrailingZeros(newmoves);
                        newRow = oldposition / 8;
                        newCol = oldposition % 8 ;
                        list += " " + oldRow + oldCol + newRow + newCol;
                        newmoves = newmoves & (newmoves-1);
                    }

                    moves = moves & (moves-1);
                }   break;
            case 'N':
                while(moves != 0)
                {
                    oldposition = Long.numberOfTrailingZeros(moves);
                    oldRow = oldposition / 8;
                    oldCol = oldposition % 8 ;
                    newmoves = mKnightMoves(oldposition) & ~PIECES_I_CANT_CAPTURE;

                    while(newmoves != 0)
                    {
                        oldposition = Long.numberOfTrailingZeros(newmoves);
                        newRow = oldposition / 8;
                        newCol = oldposition % 8 ;
                        list += " " + oldRow + oldCol + newRow + newCol;
                        newmoves = newmoves & (newmoves-1);
                    }

                    moves = moves & (moves-1);
                }   break;
            case 'K':
                while(moves != 0)
                {
                    oldposition = Long.numberOfTrailingZeros(moves);
                    oldRow = oldposition / 8;
                    oldCol = oldposition % 8 ;
                    long unsafeMoves = ( whoAmI == IAMWHITE )  ?   unsafeForWhite() :  unsafeForBlack();
                    newmoves = mKingMoves(oldposition) & ~PIECES_I_CANT_CAPTURE & ~unsafeMoves;

                    while(newmoves != 0)
                    {
                        oldposition = Long.numberOfTrailingZeros(newmoves);
                        newRow = oldposition / 8;
                        newCol = oldposition % 8 ;
                        list += " " + oldRow + oldCol + newRow + newCol;
                        newmoves = newmoves & (newmoves-1);
                    }

                    moves = moves & (moves-1);
                }   break;
            default:
                break;
        }

        return list;
    }
/* ---------------- possible moves for each piece  ---------------- ends*/

    /**
     * returns a safemovelist from a given movelist
     * @param movelist
     * @return String of safe movelist from a given movelist
     */
    private String getSafeMovesFrom(String movelist)
    {
        BBStruct bbstruct;
        String safelist = "";

        for (int i = 0; i < movelist.length() / 5; i++)
        {
            bbstruct = new BBStruct(this);

            String temp = movelist.substring((i * 5), (i * 5) + 5);
            int oldRow, oldCol, newRow, newCol;

            if( isPromotionMove(temp) )
            {
                oldCol = Character.getNumericValue(temp.charAt(1));
                newCol = Character.getNumericValue(temp.charAt(2));
                if(mmoveW)
                {
                    oldRow = 6;
                    newRow = 7;
                }
                else
                {
                    oldRow = 1;
                    newRow = 0;
                }
            }
            else if( isEnpassMove(temp)  )
            {
                oldCol = Character.getNumericValue(temp.charAt(1));
                newCol = Character.getNumericValue(temp.charAt(2));
                if(mmoveW)
                {
                    oldRow = 4;
                    newRow = 5;
                }
                else
                {
                    oldRow = 3;
                    newRow = 2;
                }
            }
            else    // is general move
            {
                oldRow = Character.getNumericValue(temp.charAt(1));
                oldCol = Character.getNumericValue(temp.charAt(2));
                newRow = Character.getNumericValue(temp.charAt(3));
                newCol = Character.getNumericValue(temp.charAt(4));
            }

            if( checkIfSafe(bbstruct, oldRow, oldCol, newRow, newCol) )
                safelist += temp;
        }
        return safelist;
    }


    private boolean checkIfSafe(BBStruct bbstruct, int oldRow, int oldCol, int newRow, int newCol)
    {
        long oldPos = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = getBitBoardCorrespondingTo((newRow * 8) + newCol);

        if( mmoveW )
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (bbstruct.mBB & newPos) != 0)      bbstruct.mBB &= ~newPos;
            else if( (bbstruct.mBN & newPos) != 0) bbstruct.mBN &= ~newPos;
            else if( (bbstruct.mBP & newPos) != 0) bbstruct.mBP &= ~newPos;
            else if( (bbstruct.mBQ & newPos) != 0) bbstruct.mBQ &= ~newPos;
            else if( (bbstruct.mBR & newPos) != 0) bbstruct.mBR &= ~newPos;
            else if( (bbstruct.mBK & newPos) != 0) bbstruct.mBK &= ~newPos;

            // removing mypiece from oldposition and moving to newposition
            if( (bbstruct.mWB & oldPos) != 0)      { bbstruct.mWB &= ~oldPos; bbstruct.mWB |= newPos; }
            else if( (bbstruct.mWN & oldPos) != 0) { bbstruct.mWN &= ~oldPos; bbstruct.mWN |= newPos; }
            else if( (bbstruct.mWP & oldPos) != 0) { bbstruct.mWP &= ~oldPos; bbstruct.mWP |= newPos; }
            else if( (bbstruct.mWQ & oldPos) != 0) { bbstruct.mWQ &= ~oldPos; bbstruct.mWQ |= newPos; }
            else if( (bbstruct.mWR & oldPos) != 0) { bbstruct.mWR &= ~oldPos; bbstruct.mWR |= newPos; }
            else if( (bbstruct.mWK & oldPos) != 0) { bbstruct.mWK &= ~oldPos; bbstruct.mWK |= newPos; }
            else {
                System.out.println("turn of white: BBstruct.checkIfSafe: some error");
                printString2("wb", mWB);
                printString2("wp", mWP);
                printString2("wn", mWN);
                printString2("wr", mWR);
                printString2("wq", mWQ);
                printString2("wk", mWK);

                printString2("mwb", bbstruct.mWB);
                printString2("mwp", bbstruct.mWP);
                printString2("mwn", bbstruct.mWN);
                printString2("mwr", bbstruct.mWR);
                printString2("mwq", bbstruct.mWQ);
                printString2("mwk", bbstruct.mWK);
            }
        }
        else
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (bbstruct.mWB & newPos) != 0)      bbstruct.mWB &= ~newPos;
            else if( (bbstruct.mWN & newPos) != 0) bbstruct.mWN &= ~newPos;
            else if( (bbstruct.mWP & newPos) != 0) bbstruct.mWP &= ~newPos;
            else if( (bbstruct.mWQ & newPos) != 0) bbstruct.mWQ &= ~newPos;
            else if( (bbstruct.mWR & newPos) != 0) bbstruct.mWR &= ~newPos;
            else if( (bbstruct.mWK & newPos) != 0) bbstruct.mWK &= ~newPos;

            // removing mypiece from oldposition and moving to newposition
            if( (bbstruct.mBB & oldPos) != 0)      { bbstruct.mBB &= ~oldPos; bbstruct.mBB |= newPos; }
            else if( (bbstruct.mBN & oldPos) != 0) { bbstruct.mBN &= ~oldPos; bbstruct.mBN |= newPos; }
            else if( (bbstruct.mBP & oldPos) != 0) { bbstruct.mBP &= ~oldPos; bbstruct.mBP |= newPos; }
            else if( (bbstruct.mBQ & oldPos) != 0) { bbstruct.mBQ &= ~oldPos; bbstruct.mBQ |= newPos; }
            else if( (bbstruct.mBR & oldPos) != 0) { bbstruct.mBR &= ~oldPos; bbstruct.mBR |= newPos; }
            else if( (bbstruct.mBK & oldPos) != 0) { bbstruct.mBK &= ~oldPos; bbstruct.mBK |= newPos; }
            else {
                System.out.println("turn of: black BBstruct.checkIfSafe: some error");
                printString2("bb", mBB);
                printString2("bp", mBP);
                printString2("bn", mBN);
                printString2("br", mBR);
                printString2("bq", mBQ);
                printString2("bk", mBK);

                printString2("mbb", bbstruct.mBB);
                printString2("mbp", bbstruct.mBP);
                printString2("mbn", bbstruct.mBN);
                printString2("mbr", bbstruct.mBR);
                printString2("mbq", bbstruct.mBQ);
                printString2("mbk", bbstruct.mBK);
            }
        }

        bbstruct.updateTempCap();

        if(mmoveW)
        {
            return (bbstruct.unsafeForWhite() & bbstruct.mWK) == 0;
        }
        else
        {
            return (bbstruct.unsafeForBlack() & bbstruct.mBK) == 0;
        }
    }

}