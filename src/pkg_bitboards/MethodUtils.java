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


    /**
     * This method checks if the move is a promotion move from
     * newRow and newCol coordinates. Example: ( _23QP, 7, 3) return true
     * @param move
     * @param newRow
     * @param newCol
     * @return
     */
    public static boolean isPromotionMove(String move, int newRow, int newCol)
    {
        return            move.charAt(4) == 'P'
                &&    newCol == Character.getNumericValue(move.charAt(2))
                &&  (   (newRow == 7 && moveW)
                || (newRow == 0 && !moveW));

    }

    public static boolean isGeneralMove(String move, int newCol, int newRow)
    {
        return     newRow == Character.getNumericValue(move.charAt(3))
                && newCol == Character.getNumericValue(move.charAt(4));
    }

    public static boolean isCastleMove(String move, int newCol, int newRow, int oldRow, int oldCol)
    {

        if( moveW )
        {
            if( ( CASTLEW_KSIDE && move.equals(" 0406") && (oldRow == 0 && oldCol == 4) && (newRow == 0 && newCol == 6) )
                    ||( CASTLEW_QSIDE && move.equals(" 0402") && (oldRow == 0 && oldCol == 4) && (newRow == 0 && newCol == 2) )   )
                return true;
        }
        else
        {
            if( ( CASTLEB_KSIDE && move.equals(" 7476") && (oldRow == 7 && oldCol == 4) && (newRow == 7 && newCol == 6) )
                    ||( CASTLEB_QSIDE && move.equals(" 7472") && (oldRow == 7 && oldCol == 4) && (newRow == 7 && newCol == 2) )   )
                return true;
        }

        return false;
    }

    public static boolean isEnpassMove(String move, int newCol, int newRow, int oldCol)
    {
        System.out.println(move + " " +oldCol + newCol);
        return          move.charAt(4) == 'E'
                && oldCol == Character.getNumericValue(move.charAt(1))
                && newCol == Character.getNumericValue(move.charAt(2))
                && ( ( moveW && (newRow == 5) ) ||
                ( !moveW && (newRow == 2) ) );
    }
    

}
