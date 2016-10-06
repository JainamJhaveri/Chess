package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import static pkg_bitboards.MethodUtils.*;

public class TempMoves {

    public static String getSafeMovesFrom(String movelist, char piece)
    {
        BBStruct bbstruct;
        String safelist = "";

        for (int i = 0; i < movelist.length() / 5; i++)
        {
            bbstruct = new BBStruct(); // setting global bitboards to local bitboards for each move

            String temp = movelist.substring((i * 5), (i * 5) + 5);
            int oldRow = 0, oldCol = 0, newRow = 0, newCol = 0;
            
            if( isPromotionMove(temp) )
            {
                System.out.println("promotion move temp: " +temp);
                oldCol = Character.getNumericValue(temp.charAt(1));
                newCol = Character.getNumericValue(temp.charAt(2));
                if(moveW)
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
            else if( isCastleMove(temp) )
            {
                System.out.println("castle move temp: " +temp);
                
                
            }
            else if( isEnpassMove(temp)  )
            {
                
            }
            else    // is general move
            {
                System.out.println("general move temp: " +temp);
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

    /**
     * 1. if an opponents piece exists in new location where our piece is going from old location,
     * then remove that piece's bit from its bb
     * 2. remove our piece from its old location and add it to new location in its bitboard
     * 3. update capturable bitboards
     * 4. return true if my king is safe after operating this
     * @param bbstruct
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     */
    private static boolean checkIfSafe(BBStruct bbstruct, int oldRow, int oldCol, int newRow, int newCol)
    {
        long oldPos = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = getBitBoardCorrespondingTo((newRow * 8) + newCol);

        if( moveW )
        {
            if( (bbstruct.mBB & newPos) != 0)      bbstruct.mBB &= ~newPos;
            else if( (bbstruct.mBN & newPos) != 0) bbstruct.mBN &= ~newPos;
            else if( (bbstruct.mBP & newPos) != 0) bbstruct.mBP &= ~newPos;
            else if( (bbstruct.mBQ & newPos) != 0) bbstruct.mBQ &= ~newPos;
            else if( (bbstruct.mBR & newPos) != 0) bbstruct.mBR &= ~newPos;
            else if( (bbstruct.mBK & newPos) != 0) bbstruct.mBK &= ~newPos;
            else System.out.println("Blank square where your piece want to move");
            
            if( (bbstruct.mWB & oldPos) != 0)      { bbstruct.mWB &= ~oldPos; bbstruct.mWB |= newPos; }
            else if( (bbstruct.mWN & oldPos) != 0) { bbstruct.mWN &= ~oldPos; bbstruct.mWN |= newPos; } 
            else if( (bbstruct.mWP & oldPos) != 0) { bbstruct.mWP &= ~oldPos; bbstruct.mWP |= newPos; }
            else if( (bbstruct.mWQ & oldPos) != 0) { bbstruct.mWQ &= ~oldPos; bbstruct.mWQ |= newPos; }
            else if( (bbstruct.mWR & oldPos) != 0) { bbstruct.mWR &= ~oldPos; bbstruct.mWR |= newPos; }
            else if( (bbstruct.mWK & oldPos) != 0) { bbstruct.mWK &= ~oldPos; bbstruct.mWK |= newPos; }
            else System.out.println("some error");
        }
        else
        {
            if( (bbstruct.mWB & newPos) != 0)      bbstruct.mWB &= ~newPos;
            else if( (bbstruct.mWN & newPos) != 0) bbstruct.mWN &= ~newPos;
            else if( (bbstruct.mWP & newPos) != 0) bbstruct.mWP &= ~newPos;
            else if( (bbstruct.mWQ & newPos) != 0) bbstruct.mWQ &= ~newPos;
            else if( (bbstruct.mWR & newPos) != 0) bbstruct.mWR &= ~newPos;
            else if( (bbstruct.mWK & newPos) != 0) bbstruct.mWK &= ~newPos;
            else System.out.println("Blank piece where your piece want to move");

            if( (bbstruct.mBB & oldPos) != 0)      { bbstruct.mBB &= ~oldPos; bbstruct.mBB |= newPos; }
            else if( (bbstruct.mBN & oldPos) != 0) { bbstruct.mBN &= ~oldPos; bbstruct.mBN |= newPos; }
            else if( (bbstruct.mBP & oldPos) != 0) { bbstruct.mBP &= ~oldPos; bbstruct.mBP |= newPos; }
            else if( (bbstruct.mBQ & oldPos) != 0) { bbstruct.mBQ &= ~oldPos; bbstruct.mBQ |= newPos; }
            else if( (bbstruct.mBR & oldPos) != 0) { bbstruct.mBR &= ~oldPos; bbstruct.mBR |= newPos; }
            else if( (bbstruct.mBK & oldPos) != 0) { bbstruct.mBK &= ~oldPos; bbstruct.mBK |= newPos; }
            else System.out.println("some error");
        }
        
//        bbstruct.updateTempCap();

        if(moveW)
        {
            if((bbstruct.unsafeForWhite() & bbstruct.mWK) == 0)
                return true;
            return false;
        }
        else
        {
            if((bbstruct.unsafeForBlack() & bbstruct.mBK) == 0)
                return true;
            return false;
        }
    }
}