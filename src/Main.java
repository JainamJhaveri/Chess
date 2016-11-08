
import main_package.UserInterface;

import static main_package.Moves.*;
import static utils.Constants.*;
import javax.swing.JFrame;

public class Main
{
    //    private static char myBitBoard[][];
    private static char initialChessBoard[][];
    
    public static void main(String[] args) 
    {
        setInitialBoard();
        
        setBitBoardsFrom(initialChessBoard);
//        myBitBoard = getDisplayBoard();
        
        displayFrame();        
    }    


    private static void setInitialBoard()
    {
        
        // initial chess board        
        
        initialChessBoard = new char[][]
        {
                {B_ROOK,    B_KNIGHT,   B_BISHOP,   B_QUEEN,    B_KING,     B_BISHOP,   B_KNIGHT,   B_ROOK},
                {B_PAWN,    B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN},
                {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
                {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
                {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
                {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
                {W_PAWN,    W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN},
                {W_ROOK,    W_KNIGHT,   W_BISHOP,   W_QUEEN,    W_KING,     W_BISHOP,   W_KNIGHT,   W_ROOK}
        };       
        /*
        // test chess board            
        initialChessBoard = new char[][]
        {
            {B_ROOK,    B_KNIGHT,   B_BISHOP,   B_QUEEN,    B_KING,     B_BISHOP,   B_KNIGHT,   B_ROOK},
            {B_PAWN,    B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN},
            {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
            {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
            {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
            {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
            {W_PAWN,    W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN},
            {W_ROOK,    W_KNIGHT,   W_BISHOP,   W_QUEEN,    W_KING,     W_BISHOP,   W_KNIGHT,   W_ROOK}
        };       
        */
    }

    private static void displayFrame() 
    {
        JFrame f = new JFrame("Smart Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface(initialChessBoard);
        f.add(ui);
        short frameheight = 550, framewidth = 500;
        short initial_x = 870, initial_y = 0; // topleft, topright corners of the JFrame
        f.setBounds( initial_x, initial_y, framewidth, frameheight);       
        f.setVisible(true);
    }

    /**
     * This method returns char array generated from all 12 bitboards.
     * In our bitboards,                                       leftmost bit is 7,7 and rightmost bit 0,0
     * But for the board to be displayed in Main class,  topleft is 0,0 and bottomright is 7,7.
     * Hence, this method shifts bits, checks which bitboard contains a piece on that position and
     * populates myBitBoard array with corresponding piece.
     * @return char array
     **/
    private static char[][] getDisplayBoard()
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

}