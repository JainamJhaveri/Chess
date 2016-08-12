package pkg_bitboards;

import static pkg_bitboards.Constants.*;


    
/*------------------------------------------------------------------------------------------------
    for each of 64 squares, we check which piece is present on that particular square and 
    then we are setting up bitboards for corresponding piece from 12 possible pieces
    (WP, WR, WN, WB, WQ, WK, B..)

    Bitboards represent current position(s) of any piece in a long number
    In Binary, they are represented as series of 0s and 1s. Ex: WP = 000100000100010000... 64 digits
    Whereas in string, they can be represented as 1, 2, 4, 8, .. 
    Eg: If there are 2 pawns on board, on a2 and b2, then long value of WP will be
    2^9  + 2^10 (0000000000000000000000000000000000000000000000000000001100000000)
    where 1 indicates the position where white pawns are currently present        
------------------------------------------------------------------------------------------------*/                
public class Moves {
    static long WP = 0L, WR = 0L, WN = 0L, WB = 0L, WQ = 0L, WK = 0L, BP = 0L, BR = 0L, BN = 0L, BB = 0L, BQ = 0L, BK = 0L; // 12 bitboards
    public static long RANK_A, RANK_B, RANK_C, RANK_D, RANK_E, RANK_F, RANK_G, RANK_H ; // ranks a..h
    public static long FILE_1, FILE_2, FILE_3, FILE_4, FILE_5, FILE_6, FILE_7, FILE_8;  // files 1..8
    
    
    static String moveHistory = "";
    
    static long PIECES_W_CANT_CAPTURE = 0L;
    static long PIECES_B = 0L;
    static long PIECES_B_CANT_CAPTURE = 0L;
    static long PIECES_W = 0L;
    
    
    public static char[][] getDisplayBoard() 
    {
        int shifts;
        char myBitBoard[][] = new char[8][8];
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {     
                shifts = i * 8 + j ;    // from 0 to 63 shifts
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
    
    
    public static void setBitBoardsFrom(char initialChessBoard[][])
    {
        int position;
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {
                String binString = "0000000000000000000000000000000000000000000000000000000000000000";
                position =  i * 8 + j;  // [0][0] till [7][7] ---> 0 to 63   Eg: [1][2] to 10 
                binString = binString.substring(position+1) + "1" + binString.substring(0,position);
                switch(initialChessBoard[i][j])
                {
                    case W_PAWN:    WP += Long.parseUnsignedLong(binString, 2);
                                    break;                    
                    case W_ROOK:    WR += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case W_BISHOP:  WB += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case W_KNIGHT:  WN += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case W_KING:    WK += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case W_QUEEN:   WQ += Long.parseUnsignedLong(binString, 2);
                                    break;                        
                    case B_PAWN:    BP += Long.parseUnsignedLong(binString, 2);
                                    break;                    
                    case B_ROOK:    BR += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case B_BISHOP:  BB += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case B_KNIGHT:  BN += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case B_KING:    BK += Long.parseUnsignedLong(binString, 2);
                                    break;
                    case B_QUEEN:   BQ += Long.parseUnsignedLong(binString, 2);
                                    break;
                    default:        break;
                }
            }
        }
    }
    
    public static void possibleWMoves()
    {    
        PIECES_W_CANT_CAPTURE = (WP|WR|WN|WB|WQ|WK|BK);
        PIECES_B = (BP|BR|BN|BB|BQ|BK);
        PIECES_B_CANT_CAPTURE = (BP|BR|BN|BB|BQ|BK|WK);
        PIECES_W = (WP|WR|WN|WB|WQ|WK);
        
        String list = null;
        list += possibleWP();
        System.out.println(list);
    }
            
    
    private static String possibleWP()
    {
        String list = null;
        long wp_bitboard;
        /*  >> 7 :: white pawn capture right */
//        wp_bitboard = (WP >> 7) & PIECES_B & ~RANK_8 & ~FILE_A;
        return list;
    }
               
}
