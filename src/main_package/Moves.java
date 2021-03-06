package main_package;

import static utils.Constants.*;
import static utils.MethodUtils.*;


/**
    for each of 64 squares, we check which piece is present on that particular square and 
    then we are setting up bitboards for corresponding piece from 12 possible pieces
    (WP, WR, WN, WB, WQ, WK, B..)

    Bitboards represent current position(s) of any piece in a long number
    In Binary, they are represented as series of 0s and 1s. Ex: WP = 000100000100010000... 64 digits
    Whereas in string, they can be represented as 1, 2, 4, 8, .. 
    Example: If there are 2 pawns on board, on a2 and b2, then long value of WP will be
    2^9  + 2^10 (0000000000000000000000000000000000000000000000000000001100000000)
    where 1 indicates the position where white pawns are currently present

    (oldRow, oldCol, newRow, newCol) is the move format that we will follow for our movelist
    (oldCol, newCol, PromotionTo, Pawn) will be followed for promotion
**/
public class Moves {
    public static long WP = 0L, WR = 0L, WN = 0L, WB = 0L, WQ = 0L, WK = 0L, BP = 0L, BR = 0L, BN = 0L, BB = 0L, BQ = 0L, BK = 0L; // 12 bitboards
    private static long PIECES_W_CANT_CAPTURE, CAPTURABLE_W, PIECES_B_CANT_CAPTURE, CAPTURABLE_B, OCCUPIEDSQ;
    public static boolean CASTLEW_KSIDE = true, // true indicates that castle is still possible
            CASTLEW_QSIDE = true,
            CASTLEB_KSIDE = true,
            CASTLEB_QSIDE = true;

    static String history = "";

    static String possibleWMoves()
    {
        return possibleB(WB|WQ, IAMWHITE) + possibleN(WN, IAMWHITE) + possibleR(WR|WQ, IAMWHITE) + possibleP(WP, IAMWHITE) + possibleK(WK, IAMWHITE)
                +   possibleCastle(IAMWHITE);
    }

    static String possibleBMoves()
    {
        return possibleB(BB|BQ, IAMBLACK) + possibleN(BN, IAMBLACK) + possibleR(BR|BQ, IAMBLACK) + possibleP(BP, IAMBLACK) + possibleK(BK, IAMBLACK)
                +   possibleCastle(IAMBLACK);
    }

    private static String possibleEnPass(long pawnpos, char whoAmI)
    {
        if( history.length() == 0 ) return "";
        String list = "";
        byte hist_oldRow = Byte.parseByte(history.substring(0,1));
        byte hist_oldCol = Byte.parseByte(history.substring(1,2));
        byte hist_newRow = Byte.parseByte(history.substring(2,3));
        byte hist_newCol = Byte.parseByte(history.substring(3,4));
        byte histPos = (byte) (hist_newRow * 8 + hist_newCol);

        byte oldCol, newCol;
        if( (Math.abs(hist_oldRow - hist_newRow) == 2) &&
            (hist_oldCol == hist_newCol) )      // if last move was (y+2)x(y)x
        {

            if ( whoAmI == IAMWHITE )  // if black pawn was moved in the last move
            {
                // if black pawn was on left of white pawn
                if ( ((pawnpos << 1) & BP & ~FILE_A & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol - 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
                else if( ((pawnpos >> 1) & BP & ~FILE_H & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol + 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
            }
            else
            {
                if ( ((pawnpos << 1) & WP & ~FILE_A & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol - 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
                else if( ((pawnpos >> 1) & WP & ~FILE_H & getBitBoardCorrespondingTo(histPos)) != 0 )
                {
                    oldCol = (byte) (hist_oldCol + 1);
                    newCol = hist_oldCol;
                    list = " " + oldCol + newCol+" E";
                }
            }

        }
        System.out.println("movelist from enpass method : " +list);
        return list;
    }

    static String possibleCastle(char whoAmI)
    {
        String list = "";
        long unsafe;
        if( whoAmI == IAMWHITE)
        {
            unsafe = unsafeForWhite();
            if ( ( CASTLEW_QSIDE && ((WR & CASTLE_ROOKS[0])!= 0) )
               && ( (unsafe & CASTLE_CHECK[0])  == 0 )
               && ( (OCCUPIEDSQ & CASTLE_CHECK[0] & ~WK) == 0  )      )
                    list += " 0402";


            if (  ( CASTLEW_KSIDE && ((WR & CASTLE_ROOKS[1])!= 0) )
               && ( (unsafe & CASTLE_CHECK[1]) == 0 )
               && ( (OCCUPIEDSQ & CASTLE_CHECK[1] & ~WK) == 0  )      )
                    list += " 0406";
        }
        else
        {
            unsafe = unsafeForBlack();
            if (  ( CASTLEB_QSIDE && ((BR & CASTLE_ROOKS[2])!= 0) )
               && ( (unsafe & CASTLE_CHECK[2]) == 0 )
               && ( (OCCUPIEDSQ & CASTLE_CHECK[2] & ~BK) == 0  )      )
                    list += " 7472";
            if (   ( CASTLEB_KSIDE && ((BR & CASTLE_ROOKS[3])!= 0) )
                && ( (unsafe & CASTLE_CHECK[3]) == 0 )
                && ( (OCCUPIEDSQ & CASTLE_CHECK[3] & ~BK) == 0  )      )
                list += " 7476";
        }

        System.out.println(whoAmI+" castle movelist: " +  list);
        return list;
    }

    static long unsafeForWhite()
    {
        long unsafemoves, piecepositions;

        // positions where black pawns can capture are unsafe for white
        unsafemoves = (BP >> 9) & ~FILE_H;
        unsafemoves |= (BP >> 7) & ~FILE_A;

        // positions where queen/bishop can move diagonally are unsafe for white
        piecepositions = BQ|BB;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = DiagonalMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where queen/rook can move horizontally are unsafe for white
        piecepositions = BQ|BR;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = HVMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where king can move, are unsafe for white
        piecepositions = BK;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = KingMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where knight can move, are unsafe for white
        piecepositions = BN;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = KnightMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        //printString2("UNSAFE MOVES", unsafemoves);
        return unsafemoves;
    }

    static long unsafeForBlack()
    {
        long unsafemoves, piecepositions;

        // positions where white pawns can capture are unsafe for black
        unsafemoves = (WP << 9) & ~FILE_A;
        unsafemoves |= (WP << 7) & ~FILE_H;

        // positions where white queen/bishop can move diagonally are unsafe for black
        piecepositions = WQ|WB;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = DiagonalMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white queen/rook can move horizontally are unsafe for black
        piecepositions = WQ|WR;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = HVMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white king can move, are unsafe for black
        piecepositions = WK;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = KingMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        // positions where white knight can move, are unsafe for black
        piecepositions = WN;
        while(piecepositions != 0)
        {
            int oldposition = Long.numberOfTrailingZeros(piecepositions);
            long newmoves = KnightMoves(oldposition);
            unsafemoves |= newmoves;
            piecepositions = piecepositions & (piecepositions-1);
        }

        //printString2("UNSAFE MOVES", unsafemoves);
        return unsafemoves;
    }

    static String possibleP(long pawnpos, char whoAmI)
    {
        String list;

        if(whoAmI == IAMWHITE)
        {
            list = possibleWP(pawnpos) + possibleEnPass(pawnpos, whoAmI);
            list = SafeMoves.getSafeMovesFrom(list);
            System.out.println( "WP possible moves: " +list );

        }
        else
        {
            list = possibleBP(pawnpos) + possibleEnPass(pawnpos, whoAmI);
            list = SafeMoves.getSafeMovesFrom(list);
            System.out.println( "BP possible moves: " +list );
        }


        return list;
    }

    private static String possibleWP(long pawnpos)
    {
        String list = "";
        long moves;

        /*  << 9 :: white pawn can capture right if there is a capturable black piece and that piece is not on FILE_A after shift */
        moves = (pawnpos << 9) & CAPTURABLE_B & ~RANK_8 & ~FILE_A;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, -1);

        /*  << 7 :: white pawn can capture left if there is a capturable black piece and that piece is not on FILE_H after shift */
        moves = (pawnpos << 7) & CAPTURABLE_B & ~RANK_8 & ~FILE_H;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 1);

        /*  >> 8 :: pawn can be pushed 1 position if it doesn't go on rank 8 after push and there are no piece below its current rank */
        moves = (pawnpos << 8) & ~RANK_8 & ~OCCUPIEDSQ;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 0);

        /*  << 16 :: pawn can be pushed 2 positions if it is on rank 2 and there are no pieces on rank 4 or rank 3 for the corresponding pawn */
        moves = ( (pawnpos & RANK_2) << 16 ) &  ~( (OCCUPIEDSQ & RANK_4) | ((OCCUPIEDSQ & RANK_3) << 8) );
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -2, 0);

        /*  << 9 :: right capture + promotion */
        moves = (pawnpos << 9) & CAPTURABLE_B & RANK_8 & ~FILE_A;
        list += getPromotionPaths(moves, -1);

        /*  << 7 :: left capture + promotion */
        moves = (pawnpos << 7) & CAPTURABLE_B & RANK_8 & ~FILE_H;
        list += getPromotionPaths(moves, 1);

        /*  << 8 :: simple promotion */
        moves = (pawnpos << 8) & RANK_8 & ~OCCUPIEDSQ;
        list += getPromotionPaths(moves, 0);

        return list;
    }

    private static String possibleBP(long pawnpos)
    {
        String list = "";
        long moves;

        /*  >> 9 :: black pawn can capture right if there is a capturable black piece and that piece is not on FILE_H after shift */
        moves = (pawnpos >> 9) & CAPTURABLE_W & ~RANK_1 & ~FILE_H;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 1, 1);

        /*  >> 7 :: black pawn can capture left if there is a capturable black piece and that piece is not on FILE_A after shift */
        moves = (pawnpos >> 7) & CAPTURABLE_W & ~RANK_1 & ~FILE_A;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 1, -1);

        /*  >> 8 :: pawn can be pushed 1 position if it doesn't go on rank 1 after push and there are no piece below its current rank */
        moves = (pawnpos >> 8) & ~RANK_1 & ~OCCUPIEDSQ;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 1, 0);

        /*  >> 16 :: pawn can be pushed 2 positions if it is on rank 7 and there are no pieces on rank 5 or rank 6 for the corresponding pawn */
        moves = ( (pawnpos & RANK_7) >> 16 ) &  ~( (OCCUPIEDSQ & RANK_5) | ((OCCUPIEDSQ & RANK_6) >> 8) );
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, 2, 0);

        /*  >> 9 :: right capture + promotion */
        moves = (pawnpos >> 9) & CAPTURABLE_W & RANK_1 & ~FILE_H;
        list += getPromotionPaths(moves, 1);

        /*  >> 7 :: left capture + promotion */
        moves = (pawnpos >> 7) & CAPTURABLE_W & RANK_1 & ~FILE_A;
        list += getPromotionPaths(moves, -1);

        /*  >> 8 :: simple promotion */
        moves = (pawnpos >> 8) & RANK_1 & ~OCCUPIEDSQ;
        list += getPromotionPaths(moves, 0);

        return list;
    }

    static String possibleR(long ROOK, char whoAmI)
    {
        String list = "";
        //printString2("rook: ", ROOK);
        list += getMoveListFromBitBoards(ROOK, 'H', whoAmI);
        list = SafeMoves.getSafeMovesFrom(list);
        System.out.println("rook movelist: " +  list);
        return list;
    }

    static String possibleB(long BISHOP, char whoAmI)
    {
        String list = "";
        //printString2("bishop: ", BISHOP);
        list += getMoveListFromBitBoards(BISHOP, 'D', whoAmI);
        list = SafeMoves.getSafeMovesFrom(list);
        System.out.println("bishop movelist: " +  list);
        return list;
    }


    static String possibleQ(long QUEEN, char whoAmI)
    {
        String list = "";
        //printString2("queen: ", QUEEN);
        list += getMoveListFromBitBoards(QUEEN, 'H', whoAmI);
        list += getMoveListFromBitBoards(QUEEN, 'D', whoAmI);
        list = SafeMoves.getSafeMovesFrom(list);
        System.out.println("queen movelist: " +  list);
        return list;
    }

    static String possibleN(long KNIGHT, char whoAmI)
    {
        String list = "";
        //printString2("knight: ", KNIGHT);
        list += getMoveListFromBitBoards(KNIGHT, 'N', whoAmI);
        list = SafeMoves.getSafeMovesFrom(list);
        System.out.println("knight movelist: " +  list);
        return list;
    }

    static String possibleK(long KING, char whoAmI)
    {
        String list = "";
        //printString2("king:", KING);
        list += getMoveListFromBitBoards(KING, 'K', whoAmI);
        list = SafeMoves.getSafeMovesFrom(list);
        System.out.println("king movelist: " + list);
        return list;
    }


    /**
     * This method returns horizontal or vertical moves according to the choice ('H' or 'D')
     * (oldRow, oldCol, newRow, newCol) is the move format that we will follow for our movelist
     * @param PIECE_BB
     * @param choice
     * @return String of movelist
     */
    private static String getMoveListFromBitBoards(long PIECE_BB, char choice, char whoAmI)
    {

        String list = "";
        long moves = PIECE_BB;
        long newmoves;
        int newRow, newCol, oldRow, oldCol, oldposition;
        long PIECES_I_CANT_CAPTURE =
                ( whoAmI == IAMWHITE )  ?   PIECES_W_CANT_CAPTURE :  PIECES_B_CANT_CAPTURE;

        switch (choice) {
            case 'H':
                while(moves != 0)
                {
                    oldposition = Long.numberOfTrailingZeros(moves);
                    oldRow = oldposition / 8;
                    oldCol = oldposition % 8 ;
                    newmoves = HVMoves(oldposition) & ~PIECES_I_CANT_CAPTURE;
                    //printString2( piece+"MOVES: ", newmoves);

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
                    newmoves = DiagonalMoves(oldposition) & ~PIECES_I_CANT_CAPTURE;
                    //printString2( piece+"MOVES: ", newmoves);

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
                    newmoves = KnightMoves(oldposition) & ~PIECES_I_CANT_CAPTURE;
                    //printString2( piece+"MOVES: ", newmoves);

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
                    newmoves = KingMoves(oldposition) & ~PIECES_I_CANT_CAPTURE & ~unsafeMoves;
                    //printString2( piece+"MOVES: ", newmoves);

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

    /**
     * In initialChessboard[][] which is passed from Main class, topleft is 0,0 and bottomright is 7,7
     * In our bitboards, leftmost bit is 7,7(63: h8) and rightmost bit 0,0 (0: a1)
     * initialCB ==> bitBoardPosition : position
      (7,7) => (0,7): 7
      (0,0) => (7,0): 56
      (7,0) => (0,0): 0
      (0,7) => (7,7): 63
     * @param initialChessBoard
    **/
    public static void setBitBoardsFrom(char initialChessBoard[][])
    {
        int position = 0;
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {
                position = i*8 + j;  // positions from 0 to 63
                switch(initialChessBoard[7-i][j])
                {
                    case W_PAWN:    WP += getBitBoardCorrespondingTo(position); break;
                    case W_ROOK:    WR += getBitBoardCorrespondingTo(position); break;
                    case W_BISHOP:  WB += getBitBoardCorrespondingTo(position); break;
                    case W_KNIGHT:  WN += getBitBoardCorrespondingTo(position); break;
                    case W_KING:    WK += getBitBoardCorrespondingTo(position); break;
                    case W_QUEEN:   WQ += getBitBoardCorrespondingTo(position); break;
                    case B_PAWN:    BP += getBitBoardCorrespondingTo(position); break;
                    case B_ROOK:    BR += getBitBoardCorrespondingTo(position); break;
                    case B_BISHOP:  BB += getBitBoardCorrespondingTo(position); break;
                    case B_KNIGHT:  BN += getBitBoardCorrespondingTo(position); break;
                    case B_KING:    BK += getBitBoardCorrespondingTo(position); break;
                    case B_QUEEN:   BQ += getBitBoardCorrespondingTo(position); break;
                    default:        break;
                }
            }
        }
        updateCap();
    }


    private static long HVMoves(int i) {
        long slider = 1L << i;
        long horizontalPossibilities =(
                               ( ( OCCUPIEDSQ & RankMask[i/8] ) - 2*slider )
                        ^ rev(rev( OCCUPIEDSQ & RankMask[i/8] ) - 2*rev(slider))
                              ) & RankMask[i/8];

        long verticalPossibilities = (
                                       ( ( OCCUPIEDSQ & FileMask[i%8] ) - 2*slider )
                                ^ rev(rev( OCCUPIEDSQ & FileMask[i%8] ) - 2*rev(slider))
                                     ) & FileMask[i%8];
        return (horizontalPossibilities|verticalPossibilities);
    }

    private static long DiagonalMoves(int i) {
        long slider = 1L << i;

        long fwdDiaPossibilities =(
                                       ( ( OCCUPIEDSQ & ForwardDiagonalMask[ i/8 + 7-(i%8) ] ) - 2*slider )
                                ^ rev(rev( OCCUPIEDSQ & ForwardDiagonalMask[ i/8 + 7-(i%8) ] ) - 2*rev(slider))
                                  ) & ForwardDiagonalMask[ i/8 + 7-(i%8) ];

        long backDiaPossibilities =(
                                       ( ( OCCUPIEDSQ & BackDiagonalMask[i/8 + i%8] ) - 2*slider )
                                ^ rev(rev( OCCUPIEDSQ & BackDiagonalMask[i/8 + i%8] ) - 2*rev(slider))
                                   ) & BackDiagonalMask[i/8 + i%8];

        return (fwdDiaPossibilities | backDiaPossibilities);
    }

    private static long KnightMoves(int oldposition) {
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

    private static long KingMoves(int oldposition) {
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

    /**
     * This method updates bitboards which are used to determine capturable and non-capturable pieces
     * It also updates the bitboard of occupied squares.
     */
    static void updateCap()
    {
        PIECES_W_CANT_CAPTURE = (WP|WR|WN|WB|WQ|WK|BK);
        CAPTURABLE_B = (BP|BR|BN|BB|BQ);
        PIECES_B_CANT_CAPTURE = (BP|BR|BN|BB|BQ|BK|WK);
        CAPTURABLE_W = (WP|WR|WN|WB|WQ);
        OCCUPIEDSQ = (WP|WR|WN|WB|WQ|WK|BP|BR|BN|BB|BQ|BK);
    }

}