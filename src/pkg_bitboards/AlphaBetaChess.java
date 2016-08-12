package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import javax.swing.JFrame;

public class AlphaBetaChess {
    static char myBitBoard[][]  = new char[8][8]; 
    static char chessBoard[][];   
    static long WP = 0L, WR = 0L, WN = 0L, WB = 0L, WQ = 0L, WK = 0L, BP = 0L, BR = 0L, BN = 0L, BB = 0L, BQ = 0L, BK = 0L;

    
    /*------------------------------------------------------------------------------------------------
        for each of 64 squares, we check which piece is present on that particular square and 
        then we are setting up bitboards for corresponding piece from 12 possible pieces
        (WP, WR, WN, WB, WQ, WK, B..)
    
        Bitboards represent current position(s) of any piece in a long number
        In Binary, they are represented as series of 0s and 1s. Ex: WP = 000100000100010000... 64 digits
        Whereas in string, they can be represented as 1, 2, 4, 8, .. 
        Eg: If there are 2 pawns on board, on a2 and b2, then long value of WP will be
        2^9  + 2^10 (0000000000000000000000000000000000000000000000000000001100000000)
        where 1 indicates the position where white pawns are currently present        
    ------------------------------------------------------------------------------------------------*/            
    public static void arrayToBitBoards(){
        int position;
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {
                String binString = "0000000000000000000000000000000000000000000000000000000000000000";
                position =  i * 8 + j;  // [0][0] till [7][7] ---> 0 to 63   Eg: [1][2] to 10 
                binString = binString.substring(position+1) + "1" + binString.substring(0,position);
                System.out.println("pos: " + binString + " position: " +position);
                switch(chessBoard[i][j])
                {
                    case W_PAWN:
                        WP += getLongFrom(binString);
                        break;                    
                    case W_ROOK:
                        WR += getLongFrom(binString);
                        break;
                    case W_BISHOP:
                        WB += getLongFrom(binString);
                        break;
                    case W_KNIGHT:
                        WN += getLongFrom(binString);
                        break;
                    case W_KING:
                        WK += getLongFrom(binString);
                        break;
                    case W_QUEEN:
                        WQ += getLongFrom(binString);
                        break;
                        
                    case B_PAWN:
                        BP += getLongFrom(binString);
                        break;                    
                    case B_ROOK:
                        BR += getLongFrom(binString);
                        break;
                    case B_BISHOP:
                        BB += getLongFrom(binString);
                        break;
                    case B_KNIGHT:
                        BN += getLongFrom(binString);
                        break;
                    case B_KING:
                        BK += getLongFrom(binString);
                        break;
                    case B_QUEEN:
                        BQ += getLongFrom(binString);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    private static void setFrom12BitBoards(char[][] myBitBoard) {
        int shifts;
        for(byte i=0; i<8; i++)
        {
            for(byte j=0; j<8; j++)
            {     
                shifts = i * 8 + j ;    // from 0 to 63 shifts
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
    }
    
    public static void main(String[] args) {
        setInitialBoard();
        possibleWMoves();
        arrayToBitBoards();                
        setFrom12BitBoards(myBitBoard);
        
        JFrame f = new JFrame("Smart Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface(myBitBoard);
        f.add(ui);
        short frameheight = 550, framewidth = 500;
        short initial_x = 870, initial_y = 0; // topleft, topright corners of the JFrame
        f.setBounds( initial_x, initial_y, framewidth, frameheight);       
        f.setVisible(true);

    }    

    public static String possibleWMoves(){
        String list = "";
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                switch(chessBoard[i][j]){                    
                    case W_PAWN:
                        list += possibleWhiteP(i, j);
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
    
    private static String possibleWhiteP(int oldRow, int oldCol){
        String list = "";
        int newCol, newRow;
        /*-----------------------------------------------------------------------------
        i is used along with (newRow, newCol) for capturing either of diagonal squares 
        (newRow, newCol) is the square where white pawn can possibly capture
        -----------------------------------------------------------------------------*/
        for(int i=-1; i<2; i=i+2){
            newRow = oldRow - 1;
            newCol = oldCol + i;
            try{
                if(oldRow != 1) 
                {   // when white pawn is anywhere except 7th row
                    // capture the piece
                    list += addBlackPieceSquare(oldRow, oldCol, newRow, newCol, W_PAWN);                                        
                }
                else
                {   // when white pawn reaches the 7th row                    
                    // promote + capture                                        
                    list += addWhitePawnPromotionPath(oldCol, newCol);
                }
            }catch(ArrayIndexOutOfBoundsException e){}                        
        }
        
        /*-----------------------------------------------------------------------------
        remaining cases for pawn are single advance, double advance and simple promotion        
        -----------------------------------------------------------------------------*/
        newRow = oldRow - 1;
        newCol = oldCol;                
        if(chessBoard[newRow][newCol] == BLANK)
        {
            // pawn single advance        
            list += addBlankSquareOnPath(oldRow, oldCol, newRow, newCol, W_PAWN);
        
            // pawn double advance
            if( oldRow == 6 && chessBoard[newRow - 1][newCol] == BLANK)
            {
                list += addBlankSquareOnPath(oldRow, oldCol, newRow - 1, newCol, W_PAWN);
            }
            else if(oldRow == 1)
            {
                list += addWhitePawnPromotionPath(oldCol, newCol);
            }
        }
            

        System.out.println("WPawn's possible moves: " +list);
        return list;
    }
            
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
     
    /*----------------------------------------------------------------------------------------------------------
        Only for pawn promotion, notation is changed to oldCol, newCol, captured-piece, promotedPiece, W_PAWN 
    ----------------------------------------------------------------------------------------------------------*/
    private static String addWhitePawnPromotionPath(int oldCol, int newCol)
    {        
        String list = "";
        char currentPiece;        
        
        int newRow = 0, oldRow = 1; 
        
        if(Character.isLowerCase(chessBoard[newRow][newCol]))
        {
            for(int k=0; k<4; k++){
                currentPiece = chessBoard[newRow][newCol];
                chessBoard[newRow][newCol] = promotedTo[k];
                chessBoard[oldRow][oldCol] = BLANK;                        
                if(isW_KingSafe())
                {                                
                    list += oldCol + "" + newCol + "" + currentPiece + "" + promotedTo[k] + "" + W_PAWN;
                }
                chessBoard[newRow][newCol] = currentPiece;
                chessBoard[oldRow][oldCol] = W_PAWN;
            }
        }
       return list;
    }        

    private static long getLongFrom(String bin){
        System.out.print("bin: " +bin + ", long: " +Long.parseUnsignedLong(bin, 2)) ;
        return Long.parseUnsignedLong(bin, 2);
    }
    
    private static boolean isW_KingSafe() {
        return true;
    }

    private static void setInitialBoard(){
        /*
        // initial chess board
        chessBoard = new char[][]
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
        chessBoard = new char[][]
        {
            {B_ROOK,    B_KNIGHT,   B_BISHOP,   B_QUEEN,    B_KING,     B_BISHOP,   B_KNIGHT,   B_ROOK},
            {W_PAWN,    B_PAWN,     B_PAWN,     W_PAWN,     B_PAWN,     B_PAWN,     B_PAWN,     W_PAWN},
            {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},
            {BLANK,     BLANK,      B_PAWN,      BLANK,      B_PAWN,      BLANK,      BLANK,      BLANK},
            {BLANK,     BLANK,      BLANK,      W_PAWN,      BLANK,      BLANK,      BLANK,      BLANK},
            {BLANK,     BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK,      BLANK},        
            {W_PAWN,    W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN,     W_PAWN},
            {W_ROOK,    W_KNIGHT,   W_BISHOP,   W_QUEEN,    W_KING,     W_BISHOP,   W_KNIGHT,   W_ROOK}
        };
       
    }
}
