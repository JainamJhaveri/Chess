package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import static pkg_bitboards.Moves.*;

/**
 * Created by jainu on 5/10/16.
 */
public class MethodUtils
{
    /**
     * Prints bitboard ( for debugging purpose )
     * @param pc name of piece whose bitboard is to be printed
     * @param piece bitboard
     */
    public static void printString2(String pc, long piece)
    {
        System.out.println("\n"+pc +": ");
        String a = Long.toBinaryString(piece);
        for(byte i=0; i < Long.numberOfLeadingZeros(piece); i++)
            a = '0' + a;
        for(byte i = 0; i < 8; i++)
        {
            for(byte j = 7; j >= 0; j--)
                System.out.print(" " + a.charAt( i * 8 +  j));
            System.out.println();
        }

    }

    public static long rev(long bitboard) {
        return Long.reverse(bitboard);
    }



    /**
     * This method updates bitboard of Moved Piece that was previously at oldRow, oldCol and moved to newRow, newCol.
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     * @return returns bit board of new position to which our piece moved. This bitboard corresponding to new position
     *  can be used to update bitboard of captured piece
     */
    public static long updateBitBoard(int oldRow, int oldCol, int newRow, int newCol)
    {
        long oldPos = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = getBitBoardCorrespondingTo((newRow * 8) + newCol);

        if(moveW)
        {
            switch(dispCB[7 - newRow][newCol])
            {
                case W_PAWN:    WP = WP & ~oldPos;
                    WP = WP | newPos;
                    break;
                case W_KING:    WK = WK & ~oldPos;
                    WK = WK | newPos;
                    CASTLEW_KSIDE = false;
                    CASTLEW_QSIDE = false;
                    break;
                case W_QUEEN:   WQ = WQ & ~oldPos;
                    WQ = WQ | newPos;
                    break;
                case W_ROOK:    WR = WR & ~oldPos;
                    WR = WR | newPos;
                    if( CASTLEW_QSIDE && ((WR & CASTLE_ROOKS[0]) == 0) )
                        CASTLEW_QSIDE = false;
                    else if ( CASTLEW_KSIDE && ( (WR & CASTLE_ROOKS[1]) == 0) )
                        CASTLEW_KSIDE = false;

                    break;
                case W_BISHOP:  WB = WB & ~oldPos;
                    WB = WB | newPos;
                    break;
                case W_KNIGHT:  WN = WN & ~oldPos;
                    WN = WN | newPos;
                    break;

                default:        break;
            }
        }
        else
        {
            switch(dispCB[7 - newRow][newCol])
            {
                case B_PAWN:    BP = BP & ~oldPos;
                    BP = BP | newPos;
                    break;
                case B_KING:    BK = BK & ~oldPos;
                    BK = BK | newPos;
                    break;
                case B_QUEEN:   BQ = BQ & ~oldPos;
                    BQ = BQ | newPos;
                    break;
                case B_ROOK:    BR = BR & ~oldPos;
                    BR = BR | newPos;
                    if( CASTLEB_QSIDE && ((BR & CASTLE_ROOKS[2]) == 0) )
                        CASTLEB_QSIDE = false;
                    else if ( CASTLEB_KSIDE && ((BR & CASTLE_ROOKS[3]) == 0) )
                        CASTLEB_KSIDE = false;
                    break;
                case B_BISHOP:  BB = BB & ~oldPos;
                    BB = BB | newPos;
                    break;
                case B_KNIGHT:  BN = BN & ~oldPos;
                    BN = BN | newPos;
                    break;

                default:        break;
            }
        }
        history = ""+ oldRow + oldCol + newRow + newCol;
        return newPos;
    }

    /**
     * This method updates bitboard of captured piece using pos bitboard.
     * pos bitboard is returned by the updateBitBoard(..) method.
     * pos is the bit board of position to which the selected piece had moved.
     * @param pos position which should be removed from bitboard of captured piece
     * @param pieceCap char piece whose bitboard needs to be updated
     */
    public static void updateCapBitBoard(long pos, char pieceCap)
    {
        // If no piece was captured, we can safely return without checking any case as no bitboard needs to be updated
        if( pieceCap == BLANK ) return;

        if ( moveW ) {
            switch(pieceCap)
            {
                case B_PAWN:    BP = BP & ~pos; break;
                case B_KING:    BK = BK & ~pos; break;
                case B_QUEEN:   BQ = BQ & ~pos; break;
                case B_ROOK:    BR = BR & ~pos; break;
                case B_BISHOP:  BB = BB & ~pos; break;
                case B_KNIGHT:  BN = BN & ~pos; break;

                default:        break;
            }
        } else {
            switch(pieceCap)
            {
                case W_PAWN:    WP = WP & ~pos; break;
                case W_KING:    WK = WK & ~pos; break;
                case W_QUEEN:   WQ = WQ & ~pos; break;
                case W_ROOK:    WR = WR & ~pos; break;
                case W_BISHOP:  WB = WB & ~pos; break;
                case W_KNIGHT:  WN = WN & ~pos; break;

                default:        break;
            }
        }
    }


    public static boolean isPromotionMove(String move)
    {
        System.out.println("isPromotionMove: "+move);
        return move.charAt(4) == 'P';
    }

    public static boolean isCastleMove(String move)
    {
        return true;
    }

    public static boolean isGeneralMove(String move)
    {
        System.out.println("isGeneralMove: "+move);
        return true;
    }

    public static boolean isEnpassMove(String move)
    {
        System.out.println("isEnpassMove: "+move);
        return move.charAt(4) == 'E';
    }

    /**
     *
     * This method takes position (from 0 to 63) as input and returns corresponding bitboard
     * by placing 1 at position th bit in binString 0s on remaining 63 positions.
     *
     * @param position : bit position in a long that needs to be changed to 1
     * @return long: bitboard corresponding to position
     */
    public static long getBitBoardCorrespondingTo(int position)
    {
        String binString = "0000000000000000000000000000000000000000000000000000000000000000";
        binString = binString.substring(0,63-position) + "1" + binString.substring(64-position);
        return Long.parseUnsignedLong(binString, 2);
    }

}
