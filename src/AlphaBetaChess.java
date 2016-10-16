import main_package.Moves;
import main_package.UserInterface;
import sun.misc.DoubleConsts;
import temp.BBStruct;
import utils.Constants;

import static utils.Constants.*;
import javax.swing.JFrame;

public class AlphaBetaChess 
{
    //    private static char myBitBoard[][];
    private static char initialChessBoard[][];
    
    public static void main(String[] args) 
    {
        setInitialBoard();
        
        Moves.setBitBoardsFrom(initialChessBoard);
//        myBitBoard = Moves.getDisplayBoard();
        
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

}