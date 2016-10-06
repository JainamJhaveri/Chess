package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import static pkg_bitboards.MethodUtils.*;
import static pkg_bitboards.Moves.*;

public class TempMoves
{
    private static long
            tempWP = 0L, tempWR = 0L, tempWN = 0L, tempWB = 0L, tempWQ = 0L, tempWK = 0L,
            tempBP = 0L, tempBR = 0L, tempBN = 0L, tempBB = 0L, tempBQ = 0L, tempBK = 0L; // 12 bitboards
    private static long tempPIECES_W_CANT_CAPTURE, tempCAPTURABLE_W, tempPIECES_B_CANT_CAPTURE, tempCAPTURABLE_B, tempOCCUPIEDSQ;
    private static String tempHistory = "";


    private static void setTempBitBoards()
    {
        tempWP = WP; tempWR = WR; tempWN = WN; tempWB = WB; tempWQ = WQ; tempWK = WK;
        tempBP = BP; tempBR = BR; tempBN = BN; tempBB = BB; tempBQ = BQ; tempBK = BK;
        tempPIECES_W_CANT_CAPTURE = PIECES_W_CANT_CAPTURE; tempCAPTURABLE_W = CAPTURABLE_W;
        tempPIECES_B_CANT_CAPTURE = PIECES_B_CANT_CAPTURE; tempCAPTURABLE_B = CAPTURABLE_B; tempOCCUPIEDSQ = OCCUPIEDSQ;
        tempHistory = history;
    }

    private static void updateTempBB()
    {
        tempPIECES_W_CANT_CAPTURE = (tempWP|tempWR|tempWN|tempWB|tempWQ|tempWK|tempBK);
        tempCAPTURABLE_B = (tempBP|tempBR|tempBN|tempBB|tempBQ);
        tempPIECES_B_CANT_CAPTURE = (tempBP|tempBR|tempBN|tempBB|tempBQ|tempBK|tempWK);
        tempCAPTURABLE_W = (tempWP|tempWR|tempWN|tempWB|tempWQ);
        tempOCCUPIEDSQ = (tempWP|tempWR|tempWN|tempWB|tempWQ|tempWK|tempBP|tempBR|tempBN|tempBB|tempBQ|tempBK);
    }
/*
    public static String getSafeMovesFrom(String possibleMoves, char piece)
    {
        setTempBitBoards();
        String list = "";

        for(int i=0; i < possibleMoves.length()/5; i++)
        {
            String move = possibleMoves.substring((i * 5), (i * 5) + 5);

            if( isPromotionMove(move) )
            {
                list += move;
            }
            else if( isCastleMove(move) )
            {
                list += move;
            }
            else if( isEnpassMove(move))
            {
                list += move;
            }
            if( isGeneralMove(move) )
            {
//                if( isMoveSafeOnBB(move, piece) )
                list += move;
            }
        }
        return list;
    }
*/
    private static boolean isMoveSafeOnBB(String move, char piece)
    {

        int oldRow = Character.getNumericValue(move.charAt(1));
        int oldCol = Character.getNumericValue(move.charAt(2));
        int newRow = Character.getNumericValue(move.charAt(3));
        int newCol = Character.getNumericValue(move.charAt(4));
        int oldPosition = oldRow * 8 + oldCol;
        int newPosition = newRow * 8 + newCol;
        long newPositionBB = getBitBoardCorrespondingTo(newPosition);
        long oldPositionBB = getBitBoardCorrespondingTo(oldPosition);

        switch (piece)
        {
            case W_PAWN:
                printString2("WP status", WP);
                WP = WP | newPositionBB;
                WP = WP & ~oldPositionBB;

                if( (BB & newPositionBB) != 0  ) {    BB = BB & ~newPositionBB; }
                else if( (BQ & newPositionBB) != 0  ) {    BQ = BQ & ~newPositionBB; }

                printString2("before WP", WP);
                UpdateCap();
                long unsafeMoves = unsafeForWhite();        // TODO: do something.. .unsafeForWhite() should not be called from here
                WP = WP | oldPositionBB;
                WP = WP & ~newPositionBB;
                UpdateCap();
                printString2("after WP", WP);
                printString2("unsafe", unsafeMoves);

                if( (unsafeMoves & WK) != 0)
                {
                    System.out.println("move from oldposition: "+oldRow+oldCol+ " to "+newCol+newRow+ " is not possible");
                    return false;
                }
                System.out.println("possible move from oldposition: "+oldRow+oldCol+ " to "+newCol+newRow);
                return true;


        }

        return true;
    }






}
