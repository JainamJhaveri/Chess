package pkg_bitboards;

import static pkg_bitboards.Constants.*;


    
/**************************************************************************************************************************        
    for each of 64 squares, we check which piece is present on that particular square and 
    then we are setting up bitboards for corresponding piece from 12 possible pieces
    (WP, WR, WN, WB, WQ, WK, B..)

    Bitboards represent current position(s) of any piece in a long number
    In Binary, they are represented as series of 0s and 1s. Ex: WP = 000100000100010000... 64 digits
    Whereas in string, they can be represented as 1, 2, 4, 8, .. 
    Example: If there are 2 pawns on board, on a2 and b2, then long value of WP will be
    2^9  + 2^10 (0000000000000000000000000000000000000000000000000000001100000000)
    where 1 indicates the position where white pawns are currently present        
**************************************************************************************************************************/                
public class Moves {
    static long WP = 0L, WR = 0L, WN = 0L, WB = 0L, WQ = 0L, WK = 0L, BP = 0L, BR = 0L, BN = 0L, BB = 0L, BQ = 0L, BK = 0L; // 12 bitboards    
    static long PIECES_W_CANT_CAPTURE, CAPTURABLE_W, PIECES_B_CANT_CAPTURE, CAPTURABLE_B, OCCUPIEDSQ;                    
        
    /*************************************************************************************************************************        
        * possibleWMoves() finds all possible LEGAL moves for white constants like PIECES_W_CANT_CAPTURE. 
        * They need to be recalculated here because bitboards may change after every move.
    *********************************************************************************/
    public static void possibleWMoves()
    {    
        PIECES_W_CANT_CAPTURE = (WP|WR|WN|WB|WQ|WK|BK);
        CAPTURABLE_B = (BP|BR|BN|BB|BQ);
        PIECES_B_CANT_CAPTURE = (BP|BR|BN|BB|BQ|BK|WK);
        CAPTURABLE_W = (WP|WR|WN|WB|WQ);
        OCCUPIEDSQ = (WP|WR|WN|WB|WQ|WK|BP|BR|BN|BB|BQ|BK);
        
//        printMasks();
        String list = "";
        list += possibleWP();
//        list += possibleWR();
        System.out.println("list: " +  list);
    }
  
    /******************************************************************************************
     * (oldRow, oldCol, newRow, newCol) is the move format that we will follow for our movelist
     * (oldCol, newCol, PromotionTo, Pawn) will be followed for promotion
     * This method returns legal moves for all White Pawns
     * @return String list (which contains space separated moves as of now)
     ******************************************************************************************/
    private static String possibleWP()
    {
        String list = "";
        long moves;
        
        /*  << 9 :: white pawn can capture right if there is a capturable black piece and that piece is not on FILE_A */        
        moves = (WP << 9) & CAPTURABLE_B & ~RANK_8 & ~FILE_A;                
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, -1);
        System.out.println("After right cap: "+list);
        /*  << 7 :: white pawn can capture left if there is a capturable black piece and that piece is not on FILE_H */
        moves = (WP << 7) & CAPTURABLE_B & ~RANK_8 & ~FILE_H;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 1);
        System.out.println("After left cap: "+list);
        /*  << 8 :: pawn can be pushed 1 position if it is not on rank 1 and there are no piece above its current rank */
        moves = (WP << 8) & ~RANK_8 & ~OCCUPIEDSQ;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 0);
        System.out.println("After pawn push 1: "+list);
        /*  << 16 :: pawn can be pushed 2 positions if it is on rank 2 and there are no pieces on rank 4 or rank 3 for the corresponding pawn */
        moves = ( (WP & RANK_2) << 16 ) &  ~( (OCCUPIEDSQ & RANK_4) | ((OCCUPIEDSQ & RANK_3) << 8) );
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -2, 0);
        System.out.println("After pawn push 1: "+list);
        
        /*  << 9 :: right capture + promotion */        
        moves = (WP << 9) & CAPTURABLE_B & RANK_8 & ~FILE_A;        
        list += getPromotionPaths(moves, -1);
        System.out.println("After right cap + promotion: "+list);
        /*  << 7 :: left capture + promotion */        
        moves = (WP << 7) & CAPTURABLE_B & RANK_8 & ~FILE_H;
        list += getPromotionPaths(moves, 1);
        System.out.println("After left cap + promotion: "+list);
        /*  << 8 :: simple promotion */        
        moves = (WP << 8) & RANK_8 & ~OCCUPIEDSQ;
        list += getPromotionPaths(moves, 0);
        System.out.println("After simple promotion: "+list);
        return list;
    }

    private static String possibleWR()
    {
        String list = "";
        printString2("rook: ", WR);
        printString2("rookMOVES: ", HandVMoves(34));
        return list;
    }       
    
               
    /********************************************************************************************************************
     * In initialChessboard[][] which is passed from AlphaBetaChess class, topleft is 0,0 and bottomright is 7,7
     * In our bitboards, leftmost bit is 7,7(63: h8) and rightmost bit 0,0 (0: a1)
     * @param initialChessBoard        
    ********************************************************************************************************************/
    public static void setBitBoardsFrom(char initialChessBoard[][])
    {
        int position = 0;
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {                
                position = i*8 + j;  // positions from 0 to 63
                String binString = getBinString(position);      
                // initialCB ==> bitBoardPosition
                // (7,7) => (0,7): 7
                // (0,0) => (7,0): 56
                // (7,0) => (0,0): 0
                // (0,7) => (7,7): 63
                switch(initialChessBoard[7-i][j])
                {                    
                    case W_PAWN:    WP += Long.parseUnsignedLong(binString, 2); break;                    
                    case W_ROOK:    WR += Long.parseUnsignedLong(binString, 2); break;
                    case W_BISHOP:  WB += Long.parseUnsignedLong(binString, 2); break;
                    case W_KNIGHT:  WN += Long.parseUnsignedLong(binString, 2); break;
                    case W_KING:    WK += Long.parseUnsignedLong(binString, 2); break;
                    case W_QUEEN:   WQ += Long.parseUnsignedLong(binString, 2); break;                        
                    case B_PAWN:    BP += Long.parseUnsignedLong(binString, 2); break;                    
                    case B_ROOK:    BR += Long.parseUnsignedLong(binString, 2); break;
                    case B_BISHOP:  BB += Long.parseUnsignedLong(binString, 2); break;
                    case B_KNIGHT:  BN += Long.parseUnsignedLong(binString, 2); break;
                    case B_KING:    BK += Long.parseUnsignedLong(binString, 2); break;
                    case B_QUEEN:   BQ += Long.parseUnsignedLong(binString, 2); break;
                    default:        break;
                }
            }
        }
    }

    /***********************************************************************************************************************
     * From the trailing zeros of the rightmost significant bit (rightmost 1), we calculate the position of (newRow, newCol)
     * And from that we calculate (oldRow, oldCol).
     * In our bitboards, leftmost bit is 7,7 and rightmost bit 0,0
     * @param moves bitboard of new moves whose movelist needs to be generated as (oldRow, newCol, newRow, newCol).
     * @param relativeRowDiff row difference of oldRow from newRow.
     * @param relativeColDiff col difference of oldCol from newCol.
     * @return String movelist as (oldRow, newCol, newRow, newCol). Each move in the list is space separated as of now.
     **********************************************************************************************************************/
    private static String getMovesWhereRelDifferenceFromNewCoordsIs(long moves, int relativeRowDiff, int relativeColDiff) 
    {        

        String list = "";
        int trailingzeros, newRow, newCol, oldRow, oldCol;
        while(moves != 0)
        {                 
            trailingzeros = Long.numberOfTrailingZeros(moves);
            newRow = trailingzeros / 8;
            newCol = trailingzeros % 8 ;
            oldRow = newRow + relativeRowDiff;
            oldCol = newCol + relativeColDiff;
            
            list += " " + oldRow + oldCol + newRow + newCol;
            moves = moves & (moves - 1);
        }
        return list;
    }
    
    /***********************************************************************************************************************
     * From the trailing zeros of the rightmost significant bit (rightmost 1), we calculate the position of newCol
     * And to newCol we add relativeColDiff and get oldCol.
     * In our bitboards, leftmost bit is 7,7 and rightmost bit 0,0.
     * @param moves bitboard of new moves whose movelist needs to be generated as (oldCol, newCol, PromotionTo, Pawn).
     * @param relativeColDiff col difference of oldCol from newCol.
     * @return String movelist as oldRow, newCol, newRow, newCol. Each move in the list is space separated as of now.
     **********************************************************************************************************************/
    private static String getPromotionPaths(long moves, int relativeColDiff)
    {
        String list = "";
        int trailingzeros, newCol, oldCol;
        while(moves != 0)
        {         
            trailingzeros = Long.numberOfTrailingZeros(moves);
            newCol = trailingzeros % 8 ;
            oldCol = newCol + relativeColDiff;                        
            for(int k=0; k<4; k++)
            {                
                list += " " + oldCol + newCol + promotedTo[k] + W_PAWN;
            }
            moves = moves & (moves - 1);
        }
        return list;
    }
    
    /****************************************************************************************************************************
     * This method returns char array generated from all 12 bitboards.
        * In our bitboards,                                       leftmost bit is 7,7 and rightmost bit 0,0
        * But for the board to be displayed in AlphaBetaChess class,  topleft is 0,0 and bottomright is 7,7.
        * Hence, this method shifts bits, checks which bitboard contains a piece on that position and 
        * populates myBitBoard array with corresponding piece.
     * @return char array
    *****************************************************************************************************************************/
    public static char[][] getDisplayBoard() 
    {
//        printString2("WP", WP);
        byte shifts;
        char myBitBoard[][] = new char[8][8];
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {     
                shifts = (byte) ( (7 - i) * 8 + j ) ;    // from 0 to 63 shifts
                if(      ((WP >> shifts) & 1) == 1  ) myBitBoard[i][j] = W_PAWN;
                else if( ((WB >> shifts) & 1) == 1  ) myBitBoard[i][j] = W_BISHOP;
                else if( ((WN >> shifts) & 1) == 1  ) myBitBoard[i][j] = W_KNIGHT;
                else if( ((WR >> shifts) & 1) == 1  ) myBitBoard[i][j] = W_ROOK;
                else if( ((WQ >> shifts) & 1) == 1  ) myBitBoard[i][j] = W_QUEEN;
                else if( ((WK >> shifts) & 1) == 1  ) myBitBoard[i][j] = W_KING;
                
                else if( ((BP >> shifts) & 1) == 1  ) myBitBoard[i][j] = B_PAWN;
                else if( ((BB >> shifts) & 1) == 1  ) myBitBoard[i][j] = B_BISHOP;
                else if( ((BN >> shifts) & 1) == 1  ) myBitBoard[i][j] = B_KNIGHT;
                else if( ((BR >> shifts) & 1) == 1  ) myBitBoard[i][j] = B_ROOK;
                else if( ((BQ >> shifts) & 1) == 1  ) myBitBoard[i][j] = B_QUEEN;
                else if( ((BK >> shifts) & 1) == 1  ) myBitBoard[i][j] = B_KING;
            }
        }
        return myBitBoard;
    }

    private static long rev(long bitboard) {
        return Long.reverse(bitboard);
    }    
 
    private static void printString2(String pc, long piece) 
    {
        System.out.print(pc +": ");
        String a = Long.toBinaryString(piece);        
        for(byte i=0; i < Long.numberOfLeadingZeros(piece); i++)
            a = '0' + a;
        
        System.out.println(a + " " + piece+"\n");
        for(byte i = 0; i < 8; i++)
        {
            for(byte j = 7; j >= 0; j--)
                System.out.print(" " + a.charAt( i * 8 +  j));            
            System.out.println();
        }        
        
    }
    
       
    private static long HandVMoves(int i)
    {
        long slider = 1L << 31;
        printString2("1L << 0", 1L << 0);
        printString2("1L << 1", 1L << 1);
        printString2("1L << 2", 1L << 2);
        
//        printString2("Occupied", OCCUPIEDSQ);
//        printString2("RankMask", RankMask[i/8]);
        long horizontalPossibilities =(
                                       ( ( OCCUPIEDSQ & RankMask[i/8] ) - 2*slider ) 
                                ^ rev(rev( OCCUPIEDSQ & RankMask[i/8] ) - 2*rev(slider))
                                      ) & RankMask[i/8];
        printString2("horposs" ,horizontalPossibilities);
        long verticalPossibilities = (
                                       ( ( OCCUPIEDSQ & FileMask[i%8] ) - 2*slider ) 
                                ^ rev(rev( OCCUPIEDSQ & FileMask[i%8] ) - 2*rev(slider))
                                     ) & FileMask[i%8];        
        printString2("verposs" ,verticalPossibilities);
        return (horizontalPossibilities | verticalPossibilities);
    }
    
    private static long DiagonalMoves(int i)
    {
        long slider = 1L << i;
        long fwdDiaPossibilities =(
                                       ( ( OCCUPIEDSQ & ForwardDiagonalMask[i/8 + i%8 - 7] ) - 2*slider ) 
                                ^ rev(rev( OCCUPIEDSQ & ForwardDiagonalMask[i/8 + i%8 - 7] ) - 2*rev(slider))
                                  ) & ForwardDiagonalMask[i/8 + i%8 - 7];
        long backDiaPossibilities =(
                                       ( ( OCCUPIEDSQ & BackDiagonalMask[i/8 + i%8] ) - 2*slider ) 
                                ^ rev(rev( OCCUPIEDSQ & BackDiagonalMask[i/8 + i%8] ) - 2*rev(slider))
                                   ) & BackDiagonalMask[i/8 + i%8];
        return (fwdDiaPossibilities | backDiaPossibilities);
    }

    /*******
     * 
     * This method takes position (from 0 to 63) as input and returns corresponding binary string
     * by placing 1 at position th bit from left and 0s at remaining bits
     * 
     * @param position
     * @return String for populating bitboards
     */
    private static String getBinString(int position)
    {
        String binString = "0000000000000000000000000000000000000000000000000000000000000000";                
        binString = binString.substring(0,63-position) + "1" + binString.substring(64-position);
        return binString;
    }
}
