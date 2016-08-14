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
    static long FILE_A, FILE_B, FILE_C, FILE_D, FILE_E, FILE_F, FILE_G, FILE_H ; // ranks a..h
    static long RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8;  // files 1..8    
    static long PIECES_W_CANT_CAPTURE, CAPTURABLE_W, PIECES_B_CANT_CAPTURE, CAPTURABLE_B, ALLPIECES;
    
    static String moveHistory = "";
                
    
    /*************************************************************************************************************************        
        possibleWMoves() finds all possible LEGAL moves for white 
        constants like PIECES_W_CANT_CAPTURE. They need to be recalculated here because bitboards may change after every move.
    *************************************************************************************************************************/
    public static void possibleWMoves()
    {    
        PIECES_W_CANT_CAPTURE = (WP|WR|WN|WB|WQ|WK|BK);
        CAPTURABLE_B = (BP|BR|BN|BB|BQ);
        PIECES_B_CANT_CAPTURE = (BP|BR|BN|BB|BQ|BK|WK);
        CAPTURABLE_W = (WP|WR|WN|WB|WQ);
        ALLPIECES = (WP|WR|WN|WB|WQ|WK|BP|BR|BN|BB|BQ|BK);
        
        
        String list = "";
        list += possibleWP() ;
        System.out.println("list: \n" +  list);
//        printString("WR", WR);
//        printString("WN",WN);
//        printString("WB",WB);
//        printString("WQ",WQ);
//        printString("WK",WK);

    }
       
    /******************************************************************************************
     * (oldRow, oldCol, newRow, newCol) is the move format that we will follow for our bitboards
     * This method returns legal moves for all White Pawns
     * @return String list (which contains space separated moves as of now)
     ******************************************************************************************/
    private static String possibleWP()
    {
        String list = "";
        long moves;
        
        /*  << 7 :: white pawn can capture right if there is a capturable black piece and the piece is not on FILE_A */
        
        moves = (WP << 7) & CAPTURABLE_B & ~RANK_1 & ~FILE_A;        
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, -1);
        
//        /*  << 9 :: white pawn can capture left if there is a capturable black piece and the piece is not on FILE_H */
        moves = (WP << 9) & CAPTURABLE_B & ~RANK_1 & ~FILE_H;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 1);

//        /*  << 8 :: pawn can be pushed 1 position if it is not on rank 1 and there are no piece above its current rank */
        moves = (WP << 8) & ~ALLPIECES;
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -1, 0);

//        /*  << 16 :: pawn can be pushed 2 positions if it is on rank 2 and there are no pieces on rank 4 or rank 3 for the corresponding pawn */
        moves = ( (WP & RANK_2) << 16 ) &  ~( (ALLPIECES & RANK_4) | ((ALLPIECES & RANK_3) << 8) );
        list += getMovesWhereRelDifferenceFromNewCoordsIs(moves, -2, 0);
        
        return list;
    }

    /************************************************************************************
     * Prints binary string value of a bitboard
     * @param pc String description to be printed prior to binary String of bitboard.
     * @param piece bitboard whose binary String value needs to be printed.
     ************************************************************************************/
    private static void printString(String pc, long piece) 
    {
        System.out.print(pc +": ");
        for(int i =0; i < Long.numberOfLeadingZeros(piece); i++)
        {
            System.out.print(0);    
        }        
        System.out.println(Long.toBinaryString(piece));
    }

    
    /*************************************************************************************************
    * RANKS are horizontal rows while FILES are vertical columns of a chess board.
    * initFilesAndRanks() initializes the long values of each file (a to h) and rank (1 to 8).
    * These values of RANKS and FILES will be used for move calculation for various pieces.
    
    * Example: For FILE E, Binary string is:
    * FILE_E: 0000100000001000000010000000100000001000000010000000100000001000
    * Here, 5th column is marked as 1s
    * Example: For RANK 3, Binary string is:
    * RANK_3: 0000000000000000000000000000000000000000111111110000000000000000
    * Here, 3rd row is marked as 1s    
    ************************************************************************************************/
    
    private static void initFilesAndRanks() 
    {
        String baseString = "0000000000000000000000000000000000000000000000000000000000000000";
        String binString;
        int position;
        for(byte i=0; i<8; i++)
        {
            position =  i * 8;            
            binString = baseString.substring( position + 8 ) + "11111111" + baseString.substring(0,position);
            switch(i)
            {
                case 0: RANK_1 += Long.parseUnsignedLong(binString, 2); break;
                case 1: RANK_2 += Long.parseUnsignedLong(binString, 2); break;
                case 2: RANK_3 += Long.parseUnsignedLong(binString, 2); break;
                case 3: RANK_4 += Long.parseUnsignedLong(binString, 2); break;
                case 4: RANK_5 += Long.parseUnsignedLong(binString, 2); break;
                case 5: RANK_6 += Long.parseUnsignedLong(binString, 2); break;
                case 6: RANK_7 += Long.parseUnsignedLong(binString, 2); break;
                case 7: RANK_8 += Long.parseUnsignedLong(binString, 2); break;    
            }
        }
                
        String basePattern = "00000000";
        String binPattern;
        for(byte i=0; i<8; i++)
        {            
            binPattern = basePattern.substring( i+1 ) + '1' + basePattern.substring( 0, i );            
            binString = binPattern;
            for(int j=0; j<3; j++)
            {
                binString += binString;
            }            
                                
            switch(i)
            {
                case 0: FILE_H += Long.parseUnsignedLong(binString, 2); break;
                case 1: FILE_G += Long.parseUnsignedLong(binString, 2); break;
                case 2: FILE_F += Long.parseUnsignedLong(binString, 2); break;
                case 3: FILE_E += Long.parseUnsignedLong(binString, 2); break;
                case 4: FILE_D += Long.parseUnsignedLong(binString, 2); break;
                case 5: FILE_C += Long.parseUnsignedLong(binString, 2); break;
                case 6: FILE_B += Long.parseUnsignedLong(binString, 2); break;
                case 7: FILE_A += Long.parseUnsignedLong(binString, 2); break;    
            }
        }
//        
//        printString("FILE_A: ", FILE_A);
//        printString("FILE_B: ", FILE_B);
//        printString("FILE_C: ", FILE_C);
//        printString("FILE_D: ", FILE_D);
//        printString("FILE_E: ", FILE_E);
//        printString("FILE_F: ", FILE_F);
//        printString("FILE_G: ", FILE_G);
//        printString("FILE_H: ", FILE_H);
//        
//        printString("RANK_1: ", RANK_1);
//        printString("RANK_2: ", RANK_2);
//        printString("RANK_3: ", RANK_3);
//        printString("RANK_4: ", RANK_4);
//        printString("RANK_5: ", RANK_5);
//        printString("RANK_6: ", RANK_6);
//        printString("RANK_7: ", RANK_7);
//        printString("RANK_8: ", RANK_8);
    }
    
    /********************************************************************************************************************
     * In initialChessboard[][] which is passed from AlphaBetaChess class, topleft is 0,0 and bottomright is 7,7
     * In our bitboards, leftmost bit is 7,7 and rightmost bit 0,0                    
     * @param initialChessBoard        
    ********************************************************************************************************************/
    public static void setBitBoardsFrom(char initialChessBoard[][])
    {
        int position;
        for(byte i=0; i<8; i++)
        {
            position =  i * 8 ;
            for(byte j=0; j<8; j++)
            {
                String binString = "0000000000000000000000000000000000000000000000000000000000000000";                
                binString = binString.substring(0,position) + "1" + binString.substring(position+1);
//                System.out.println(binString + " i: " +i + " j: "+j);                
                position++ ;  
                switch(initialChessBoard[i][j])
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
        
        initFilesAndRanks();
    }

    /***********************************************************************************************************************
     * From the trailing zeros of the rightmost significant bit (rightmost 1), we calculate the position of (newRow, newCol)
     * And from that we calculate (oldRow, oldCol), append it to list String.
     * In our bitboards, leftmost bit is 7,7 and rightmost bit 0,0
     * @param moves bitboard of new moves whose movelist needs to be generated as (oldRow, newCol, newRow, newCol).
     * @param relativeRowDiff row difference of oldRow from newRow.
     * @param relativeColDiff col difference of oldCol from newCol.
     * @return String movelist as oldRow, newCol, newRow, newCol.
     **********************************************************************************************************************/
    private static String getMovesWhereRelDifferenceFromNewCoordsIs(long moves, int relativeRowDiff, int relativeColDiff) {
        String list = "";
        int trailingzeros, newRow, newCol, oldRow, oldCol;
        while(moves != 0)
        {         
//            printString("moves: ", moves);
            trailingzeros = Long.numberOfTrailingZeros(moves);
            newRow = trailingzeros / 8;
            newCol = (63 - trailingzeros) % 8 ;
            oldRow = newRow + relativeRowDiff;
            oldCol = newCol + relativeColDiff;
            
            list += " " + oldRow + oldCol + newRow + newCol;
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
        int shifts;
        char myBitBoard[][] = new char[8][8];
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {     
                shifts = (7 - i) * 8 + (7 - j) ;    // from 63 to 0 shifts
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
    
}
