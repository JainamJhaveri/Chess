package main_package;

import java.util.Scanner;

import static main_package.Moves.*;
import static utils.Constants.*;
import static utils.MethodUtils.getBitBoardCorrespondingTo;

class UpdateBitBoards {

    /***
     * 1. finds and removes opposite piece (if it is present) at oldposition(oldRow, oldCol) on it's bitboard
     * 2. removes my piece from oldposition(oldRow, oldCol) and adding to newPositon(newRow, newCol) on it's bitboard
     * 3. calls updateCap() method
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     */
    static void updateBitBoard(int oldRow, int oldCol, int newRow, int newCol)
    {
        long oldPos = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = getBitBoardCorrespondingTo((newRow * 8) + newCol);

        if( moveW )
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (BB & newPos) != 0)      BB &= ~newPos;
            else if( (BN & newPos) != 0) BN &= ~newPos;
            else if( (BP & newPos) != 0) BP &= ~newPos;
            else if( (BQ & newPos) != 0) BQ &= ~newPos;
            else if( (BR & newPos) != 0) BR &= ~newPos;
            else if( (BK & newPos) != 0) BK &= ~newPos;

            // removing mypiece from oldposition and moving to newposition
            if( (WB & oldPos) != 0)      { WB &= ~oldPos; WB |= newPos; }
            else if( (WN & oldPos) != 0) { WN &= ~oldPos; WN |= newPos; }
            else if( (WP & oldPos) != 0) { WP &= ~oldPos; WP |= newPos; }
            else if( (WQ & oldPos) != 0) { WQ &= ~oldPos; WQ |= newPos; }
            else if( (WR & oldPos) != 0) { WR &= ~oldPos; WR |= newPos;
                if( CASTLEW_QSIDE && ((WR & CASTLE_ROOKS[0]) == 0) )
                    CASTLEW_QSIDE = false;
                else if ( CASTLEW_KSIDE && ( (WR & CASTLE_ROOKS[1]) == 0) )
                    CASTLEW_KSIDE = false;  }
            else if( (WK & oldPos) != 0) { WK &= ~oldPos; WK |= newPos;
                CASTLEW_KSIDE = false;
                CASTLEW_QSIDE = false;  }
            else System.err.println("updateBitBoard: shouldn't reach here!");
        }
        else
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (WB & newPos) != 0)      WB &= ~newPos;
            else if( (WN & newPos) != 0) WN &= ~newPos;
            else if( (WP & newPos) != 0) WP &= ~newPos;
            else if( (WQ & newPos) != 0) WQ &= ~newPos;
            else if( (WR & newPos) != 0) WR &= ~newPos;
            else if( (WK & newPos) != 0) WK &= ~newPos;

            if( (BB & oldPos) != 0)      { BB &= ~oldPos; BB |= newPos; }
            else if( (BN & oldPos) != 0) { BN &= ~oldPos; BN |= newPos; }
            else if( (BP & oldPos) != 0) { BP &= ~oldPos; BP |= newPos; }
            else if( (BQ & oldPos) != 0) { BQ &= ~oldPos; BQ |= newPos; }
            else if( (BR & oldPos) != 0) { BR &= ~oldPos; BR |= newPos;
                if( CASTLEB_QSIDE && ((BR & CASTLE_ROOKS[2]) == 0) )
                    CASTLEB_QSIDE = false;
                else if ( CASTLEB_KSIDE && ((BR & CASTLE_ROOKS[3]) == 0) )
                    CASTLEB_KSIDE = false;  }
            else if( (BK & oldPos) != 0) { BK &= ~oldPos; BK |= newPos;
                CASTLEB_KSIDE = false;
                CASTLEB_QSIDE = false;  }
            else System.err.println("shouldn't reach here!");
        }

        history = ""+ oldRow + oldCol + newRow + newCol;
        updateCap();
    }

    /**
     * This method is used during enpass move to update bitboard of opponents pawn at capRow, newCol.
     * @param capRow
     * @param newCol
     * @param pieceCap char piece whose bitboard needs to be updated
     */
    static void updatePawnCapBitBoard(int capRow, int newCol, char pieceCap)
    {
        long pos = getBitBoardCorrespondingTo((capRow * 8) + newCol);
        switch(pieceCap)
        {
            case B_PAWN:    BP &= ~pos; break;
            case W_PAWN:    WP &= ~pos; break;
            default: System.err.println("Shouldn't reach here !");
        }
        updateCap();
    }

    /**
     * This method asks for promotion choice in terminal and
     * updates bitboards of corresponding player's pawn and the piece to which it is promoted
     * @param newRow
     * @param newCol
     * @return char piece to which it is promoted
     */
    static char updatePromotePawnBitBoard(int newRow, int newCol)
    {
        long pos = getBitBoardCorrespondingTo((newRow * 8) + newCol);
        int choice ;
        char piece = ' ';
        do
        {
            System.out.println("What do you wish to promote the pawn to?");
            System.out.println("1.Queen, 2.Rook, 3.Bishop, 4.Knight");
            System.out.println("Enter Choice (1/2/3/4) - ");
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
        } while( choice < 1 || choice > 4 );

        if( moveW )
        {
            WP &= ~pos;
            switch( choice )
            {
                case 1: WQ = WQ | pos;  piece = W_QUEEN;
                    break;
                case 2: WR = WR | pos;  piece = W_ROOK;
                    break;
                case 3: WB = WB | pos;  piece = W_BISHOP;
                    break;
                case 4: WN = WN | pos;  piece = W_KNIGHT;
                    break;
                default:    System.err.println("Warning! Invalid Pawn Promotion Chosen! Program Should Never Reach Here");
                    break;
            }
        }
        else
        {
            BP &= ~pos;
            switch(choice)
            {
                case 1: BQ = BQ | pos;  piece = B_QUEEN;
                    break;
                case 2: BR = BR | pos;  piece = B_ROOK;
                    break;
                case 3: BB = BB | pos;  piece = B_BISHOP;
                    break;
                case 4: BN = BN | pos;  piece = B_KNIGHT;
                    break;
                default:    System.err.println("Warning! Invalid Pawn Promotion Chosen! Program Should Never Reach Here");
                    break;
            }
        }
        updateCap();
        return piece;
    }

}
