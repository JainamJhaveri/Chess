package main;
import static main.Constants.*;
import javax.swing.JFrame;

public class AlphaBetaChess {

    static char chessBoard[][] = 
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

    public static void main(String[] args) {
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                System.out.print(chessBoard[i][j] + " ");
            }
            System.out.println();
        }
        
        JFrame f = new JFrame("Smart Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface(chessBoard);
        f.add(ui);

        
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//        f.setUndecorated(true);
        f.setVisible(true);
    }    
}
