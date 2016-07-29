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
        possibleWMoves();

        
        JFrame f = new JFrame("Smart Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface(chessBoard);
        f.add(ui);
        
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        f.setVisible(true);

    }    

    public static String possibleWMoves(){
        String list = "";
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                switch(chessBoard[i][j]){                    
                    case W_PAWN:
//                        list += possibleWhiteP(i, j);
                        break;
                    case W_KING:
                        list += possibleWhiteK(i, j);
                        break;
                    case W_QUEEN:
//                        list += possibleWhiteQ(i, j);
                        break;
                    case W_ROOK:
//                        list += possibleWhiteR(i, j);
                        break;
                    case W_BISHOP:
//                        list += possibleWhiteB(i, j);
                        break;
                    case W_KNIGHT:
//                        list += possibleWhiteN(i, j);
                        break;   
                    default:
                        break;
                }
            }
        }   
        System.out.println(list);
        return list;
    }
    
        /* -----------------------------------------------------------------------------------------
          Sample move generated will be oldrow, oldcol, newrow, newcol, currentpiece on that position  
                            Example: 3344q
          Current piece is at (3,3) and valid move is (4,4) where black Queen is currently present
        ------------------------------------------------------------------------------------------ */
    private static String possibleWhiteK(int oldRow, int oldCol){        
        String list = "";
        int newCol, newRow, count = 0;        
        for(int i= -1; i<2; i++){
            for(int j= -1; j<2; j++){
                // if it is lower case, it means black piece is present 
                if(1 == i && 1 == j)
                    continue;
                newRow = oldRow + i;
                newCol = oldCol + j;                
                try{    // if the piece is at corner or at borders, newRow and newCol index can go out of bounds
                    /*------------------------------------------------------------------------ 
                        a move can be valid only if chesspiece on one square of king's 
                                surrounding is either blank or a black piece
                    ------------------------------------------------------------------------*/
                    if(Character.isLowerCase(chessBoard[newRow][newCol]) || chessBoard[newRow][newCol] == BLANK){
                        count++;                        
                        char currentPiece = chessBoard[newRow][newCol];
                        
                        
                        /*---------------------------------------------------------------------------------
                        place white king in new position and check if it is safe for the king to move there
                        ---------------------------------------------------------------------------------*/
                        chessBoard[newRow][newCol] = W_KING;
                        chessBoard[oldRow][oldCol] = BLANK;
                        
                        if(isW_KingSafe())
                            list += oldRow + "" + oldCol + "" + newRow + "" + newCol + "" + currentPiece;
                        
                        chessBoard[newRow][newCol] = currentPiece;
                        chessBoard[oldRow][oldCol] = W_KING;
                        
                    }
                }catch(ArrayIndexOutOfBoundsException e){}
            }
        }
        System.out.println(count);
        return list;
    }
    
    private static void displayCB() {
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                System.out.print(chessBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean isW_KingSafe() {
        return true;
    }
}
