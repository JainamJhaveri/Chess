package main;
import static main.Constants.*;
import javax.swing.JFrame;

public class AlphaBetaChess {
/*  
    // initial chess board
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
    */
    // test chess board
    static char chessBoard[][] = 
    {
        {B_ROOK,    B_KNIGHT,   B_BISHOP,   B_QUEEN,    B_KING,     B_BISHOP,   B_KNIGHT,   B_ROOK},
        {B_PAWN,    B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     B_PAWN},
        {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
        {BLANK,     BLANK,      W_KNIGHT,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
        {BLANK,     BLANK,      BLANK,      BLANK,      B_BISHOP,      BLANK,      BLANK,      BLANK},
        {BLANK,     B_QUEEN,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},        
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
                        list += possibleWhiteQ(i, j);
                        break;
                    case W_ROOK:
                        list += possibleWhiteR(i, j);
                        break;
                    case W_BISHOP:
                        list += possibleWhiteB(i, j);
                        break;
                    case W_KNIGHT:
                        list += possibleWhiteN(i, j);
                        break;   
                    default:
                        break;
                }   
            }
        }   
        System.out.println("all possible moves: " +list);
        return list;
    }
    
/* -----------------------------------------------------------------------------------------
  Sample move generated will be oldrow, oldcol, newrow, newcol, currentpiece on that position  
                    Example: 3344q
    For piece at (3,3), valid move is (4,4) where black Queen is currently present
------------------------------------------------------------------------------------------ */
    
    private static String possibleWhiteK(int oldRow, int oldCol){        
        String list = "";
        int newCol, newRow;        
        for(int i= -1; i<2; i++){
            for(int j= -1; j<2; j++){                
                if(0 == i && 0 == j)
                    continue;
                newRow = oldRow + i;
                newCol = oldCol + j;                
                try{    // if the piece is at corner or at borders, newRow and newCol index can go out of bounds
                    /*------------------------------------------------------------------------ 
                        a move can be valid only if chesspiece on one square of king's 
                                surrounding is either blank or a black piece
                    ------------------------------------------------------------------------*/
                    if(Character.isLowerCase(chessBoard[newRow][newCol]) || chessBoard[newRow][newCol] == BLANK){
                        char currentPiece = chessBoard[newRow][newCol];
                        /*---------------------------------------------------------------------------------
                        place white king in new position and check if it is safe for the king to move there
                        ---------------------------------------------------------------------------------*/
                        chessBoard[newRow][newCol] = W_KING;
                        chessBoard[oldRow][oldCol] = BLANK;                        
                        if(isW_KingSafe()){
                            list += oldRow + "" + oldCol + "" + newRow + "" + newCol + "" + currentPiece;
                        }
                        chessBoard[newRow][newCol] = currentPiece;
                        chessBoard[oldRow][oldCol] = W_KING;
                        
                    }
                }catch(ArrayIndexOutOfBoundsException e){}
            }
        }
        System.out.println("WKing's possible moves: " +list);
        return list;
    }
        
    private static String possibleWhiteQ(int oldRow, int oldCol){
        String list = "";
        int newRow, newCol, dist;        
        /*--------------------------------------------------------------------------------------- 
            i and j are used to obtain all 8 directions for the queen.
            Example: i=-1, j=-1 will consider square at -45deg from the current position 
            and increasing dist will diagonally cover all the squares in -45deg direction
        -------------------------------------------------------------------------------------- */
        for(int i=-1; i<2; i++){            
            for(int j=-1; j<2; j++){
                if(0 == i && 0 == j)    // the position where piece itself is present
                    continue;
                dist = 1;
                newRow = oldRow + i * dist;
                newCol = oldCol + j * dist;
                try{            // if the piece is at corner or at borders, newRow and newCol index can go out of bounds
                    /*------------------------------------------------------------------------ 
                        By this while loop, all the moves on diagonal, horizontal or 
                        vertical(depending on i and j) blank spaces are considered valid 
                        moves for queen till the king remains safe after queen's movement
                    ------------------------------------------------------------------------*/
                    while(chessBoard[newRow][newCol] == BLANK){
                        list += addBlankSquareOnPath(oldRow, oldCol, newRow, newCol, W_QUEEN);
                        dist++ ;
                        newRow = oldRow + i * dist;
                        newCol = oldCol + j * dist;
                    }
                }catch(ArrayIndexOutOfBoundsException e){continue;}
                                
                list += addBlackPieceSquare(oldRow, oldCol, newRow, newCol, W_QUEEN);
            }
        }
        System.out.println("WQueen's possible moves: " +list);
        return list;
    }
    
    private static String possibleWhiteB(int oldRow, int oldCol){
        String list = "";
        int newRow, newCol, dist;
        /*--------------------------------------------------------------------------------------- 
                    i and j are used to obtain 4 directions for the bishop
            Example: i=-1, j=-1 will consider square at -45deg from the current position 
            and increasing dist will diagonally cover all the squares in -45deg direction
            After that i and j are increased by 2 which increases the angle by 90deg clockwise
        -------------------------------------------------------------------------------------- */
        for(int i=-1; i<2; i=i+2){            
            for(int j=-1; j<2; j=j+2){                
                dist = 1;
                newRow = oldRow + i * dist;
                newCol = oldCol + j * dist;
                try{            // if the piece is at corner or at borders, newRow and newCol index can go out of bounds
                    /*------------------------------------------------------------------------ 
                        By this while loop, all the moves on diagonal (depending on i and j) 
                        blank spaces are considered valid moves for bishop till the king 
                        remains safe after bishop's movement
                    ------------------------------------------------------------------------*/
                    
                    while(chessBoard[newRow][newCol] == BLANK){                    
                        list += addBlankSquareOnPath(oldRow, oldCol, newRow, newCol, W_BISHOP);
                        dist++ ;
                        newRow = oldRow + i * dist;
                        newCol = oldCol + j * dist;                        
                    }
                }catch(ArrayIndexOutOfBoundsException e){continue;}
                
                list += addBlackPieceSquare(oldRow, oldCol, newRow, newCol, W_BISHOP);                
            }
        }
        System.out.println("WBishop's possible moves: " +list);
        return list;
    }

    private static String possibleWhiteR(int oldRow, int oldCol){
        String list = "";
        int newRow, newCol, dist;
        /*--------------------------------------------------------------------------------------- 
                i is used to cover left and right directions of white rook
            and increasing dist will cover all the squares in both left and right direction
        -------------------------------------------------------------------------------------- */
        newRow = oldRow;
        for(int i=-1; i<2; i=i+2){                        
            dist = 1;                
            newCol = oldCol + i * dist;

            try{            // if the piece is at corner or at borders, newRow and newCol index can go out of bounds
                /*------------------------------------------------------------------------ 
                By this while loop, all the moves on horizontal blank spaces are considered
                    valid moves for rook till the king remains safe after it's movement
                ------------------------------------------------------------------------*/
                while(chessBoard[newRow][newCol] == BLANK){                    
                    list += addBlankSquareOnPath(oldRow, oldCol, newRow, newCol, W_ROOK);
                    dist++ ;
                    newCol = oldCol + i * dist;
                }
            }catch(ArrayIndexOutOfBoundsException e){continue;}

            list += addBlackPieceSquare(oldRow, oldCol, newRow, newCol, W_ROOK);            
        }
        
        /*--------------------------------------------------------------------------------------- 
                i is used for up and down directions of white rook
            and increasing dist will cover all the squares in both left and right direction
        -------------------------------------------------------------------------------------- */
        newCol = oldCol;
        for(int i=-1; i<2; i=i+2){                        
            dist = 1;                
            newRow = oldRow + i * dist;

            try{            // if the piece is at corner or at borders, newRow and newCol index can go out of bounds
                /*------------------------------------------------------------------------ 
                By this while loop, all the moves on vertical blank spaces are considered
                    valid moves for rook till the king remains safe after it's movement
                ------------------------------------------------------------------------*/                
                while(chessBoard[newRow][newCol] == BLANK){
                    list += addBlankSquareOnPath(oldRow, oldCol, newRow, newCol, W_ROOK);
                    dist++ ;
                    newRow = oldRow + i * dist;
                }
            }catch(ArrayIndexOutOfBoundsException e){continue;}
                                    
            list += addBlackPieceSquare(oldRow, oldCol, newRow, newCol, W_ROOK);
        }
        
        System.out.println("WRook's possible moves: " +list);
        return list;
    }
    
    private static String possibleWhiteN(int oldRow, int oldCol){
        String list = "";
        int newRow, newCol;
        /*--------------------------------------------------------------------------------------- 
          i and j are used to get possible position of knights. In following nested for loop, 
          we reduce/increase i by 1 and j by 2 to cover 4 possibilities out of 8 for the knight.
        -------------------------------------------------------------------------------------- */
        for(int i=-1; i<2; i=i+2){
            for(int j=-1; j<2; j=j+2){
                newRow = oldRow + i;
                newCol = oldCol + j * 2;
                try{            // if the piece is at corner or at borders, newRow and newCol index can go out of bounds                    
                    if(chessBoard[newRow][newCol] == BLANK){                    
                        list += addBlankSquareOnPath(oldRow, oldCol, newRow, newCol, W_KNIGHT);                                                
                    }
                }catch(ArrayIndexOutOfBoundsException e){continue;}
                
                list += addBlackPieceSquare(oldRow, oldCol, newRow, newCol, W_KNIGHT);                
            }
        }
        /*--------------------------------------------------------------------------------------- 
          i and j are used to get possible position of knights. In following nested for loop, 
          we reduce/increase i by 1 and j by 2 to cover 4 possibilities out of 8 for the knight.
        -------------------------------------------------------------------------------------- */
        for(int i=-1; i<2; i=i+2){
            for(int j=-1; j<2; j=j+2){
                newRow = oldRow + i * 2;
                newCol = oldCol + j;
                try{            // if the piece is at corner or at borders, newRow and newCol index can go out of bounds                    
                    if(chessBoard[newRow][newCol] == BLANK){                    
                        list += addBlankSquareOnPath(oldRow, oldCol, newRow, newCol, W_KNIGHT);                        
                    }
                }catch(ArrayIndexOutOfBoundsException e){continue;}
                list += addBlackPieceSquare(oldRow, oldCol, newRow, newCol, W_KNIGHT);                
            }
        }
        System.out.println("WKnight's possible moves: " +list);
        return list;
    }
    /*----------------------------------------------------------------------------------------------
    addBlankSquareOnPath(..) method adds single blank square on the path of piece as a possible move
    -----------------------------------------------------------------------------------------------*/
    private static String addBlankSquareOnPath(int oldRow, int oldCol, int newRow, int newCol, char MY_WPIECE){
        String list = "";
        chessBoard[newRow][newCol] = MY_WPIECE;
        chessBoard[oldRow][oldCol] = BLANK;                        
        if(isW_KingSafe()){
            list = oldRow + "" + oldCol + "" + newRow + "" + newCol + "" + BLANK;
        }
        chessBoard[newRow][newCol] = BLANK;
        chessBoard[oldRow][oldCol] = MY_WPIECE;        
        return list;
    }
    
    /*--------------------------------------------------------------------------------------
    If a black piece is present on oldRow, oldCol then capturing that black piece will also 
    be a valid move for white piece.
    addBlackPieceSquare(..) method adds that black piece to the possible path
    ---------------------------------------------------------------------------------------*/
    private static String addBlackPieceSquare(int oldRow, int oldCol, int newRow, int newCol, char MY_WPIECE){
        char currentPiece;
        String list = "";
        if(Character.isLowerCase(chessBoard[newRow][newCol])){
            currentPiece = chessBoard[newRow][newCol];
            chessBoard[newRow][newCol] = MY_WPIECE;
            chessBoard[oldRow][oldCol] = BLANK;                        
            if(isW_KingSafe()){
                list += oldRow + "" + oldCol + "" + newRow + "" + newCol + "" + currentPiece;
            }
            chessBoard[newRow][newCol] = currentPiece;
            chessBoard[oldRow][oldCol] = MY_WPIECE;
        }
        return list;
    }
            
    private static void printCB() {
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
