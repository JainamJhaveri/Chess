package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

class UserInterface extends JPanel implements MouseListener, MouseMotionListener{
    private final Image img_board, img_green;
    private final Image img_B, img_K, img_N, img_P, img_Q, img_R;
    private final Image img_b, img_k, img_n, img_p, img_q, img_r;
    private final int base_x = 20, base_y = 40, disp = 53;
    private char dispCB[][] = new char[8][8];
    private int oldCol = 0, oldRow = 0;
    private boolean click2 = false, moveW = true;
    private String movelist = "";
    
    
    public UserInterface(char[][] initialCB)  {
                
        dispCB = initialCB;
        // image of board
        img_board = new ImageIcon(path_board).getImage();
        img_green = makeTransparent(path_greenSq, 0x6F00FF00 );
        // images of white pieces 
        img_B = new ImageIcon(path_B).getImage();
        img_K = new ImageIcon(path_K).getImage();
        img_N = new ImageIcon(path_N).getImage();
        img_P = new ImageIcon(path_P).getImage();
        img_Q = new ImageIcon(path_Q).getImage();
        img_R = new ImageIcon(path_R).getImage(); 
        // images of black pieces 
        img_b = new ImageIcon(path_b).getImage();
        img_k = new ImageIcon(path_k).getImage();
        img_n = new ImageIcon(path_n).getImage();
        img_p = new ImageIcon(path_p).getImage();
        img_q = new ImageIcon(path_q).getImage();
        img_r = new ImageIcon(path_r).getImage();
        // adding mouselistener and mousemotionlisteners to JPanel
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);      

        g.drawImage(img_board, base_x, base_y, this);                
        highlightSquares(g);
        
        /*--------------------------------------------------------------------------------------------------------
        img_piece is used as a reference pointer to the images iteratively. Following 'for' loop will populate 
                the chessboard with images of pieces according to the contents of dispCB array.
        ---------------------------------------------------------------------------------------------------------*/
        Image img_piece;        
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                switch(dispCB[i][j]){
                    case B_PAWN:    img_piece = img_p;  break;
                    case B_KING:    img_piece = img_k;  break;
                    case B_QUEEN:   img_piece = img_q;  break;
                    case B_ROOK:    img_piece = img_r;  break;
                    case B_BISHOP:  img_piece = img_b;  break;
                    case B_KNIGHT:  img_piece = img_n;  break;
                    case W_PAWN:    img_piece = img_P;  break;
                    case W_KING:    img_piece = img_K;  break;
                    case W_QUEEN:   img_piece = img_Q;  break;
                    case W_ROOK:    img_piece = img_R;  break;
                    case W_BISHOP:  img_piece = img_B;  break;
                    case W_KNIGHT:  img_piece = img_N;  break;   
                    default:        img_piece = null;   break;
                }
                /*--------------------------------------------------------------------------------------------------------
                                drawing images by displacing 'j' (with base_x as horizontal base) 
                                            for each 'i' with base_y as vertical base
                ---------------------------------------------------------------------------------------------------------*/
                g.drawImage(img_piece, base_x + j * disp, base_y + i * disp, this);
            }
        }
        System.out.println("Movelist is - "+movelist);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(click2 == false){                                    //Checks if first click
            movelist = "";
            if( ((x-base_x) < (8*disp))   &&   (x > base_x)     
            &&  ((y-base_y) < (8*disp))   &&   (y > base_y)
            &&  (e.getButton()  ==  MouseEvent.BUTTON1)   )     
            {                                                   //Checks if click is leftclick and inside the board        
                oldRow = 7 - ( (y-base_y)/disp );
                oldCol = (x-base_x)/disp;                
                
                //Y moves vertically and thus covers all rows - Similarly, X covers all columns
                System.out.println(oldRow+", "+oldCol);
            
                long position = Moves.getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
                if(moveW == true){
                    switch(dispCB[7 - oldRow][oldCol]){
                        case W_PAWN:    movelist = Moves.possibleP(position,IAMWHITE);  break;
                        case W_KING:    movelist = Moves.possibleK(position,IAMWHITE);  break;
                        case W_QUEEN:   movelist = Moves.possibleQ(position,IAMWHITE);  break;
                        case W_ROOK:    movelist = Moves.possibleR(position,IAMWHITE);  break;
                        case W_BISHOP:  movelist = Moves.possibleB(position,IAMWHITE);  break;
                        case W_KNIGHT:  movelist = Moves.possibleN(position,IAMWHITE);  break;   
                        
                        default:        break;
                    }
                }
                else{
                    switch(dispCB[7 - oldRow][oldCol]){
                        case B_PAWN:    movelist = Moves.possibleP(position,IAMBLACK);  break;
                        case B_KING:    movelist = Moves.possibleK(position,IAMBLACK);  break;
                        case B_QUEEN:   movelist = Moves.possibleQ(position,IAMBLACK);  break;
                        case B_ROOK:    movelist = Moves.possibleR(position,IAMBLACK);  break;
                        case B_BISHOP:  movelist = Moves.possibleB(position,IAMBLACK);  break;
                        case B_KNIGHT:  movelist = Moves.possibleN(position,IAMBLACK);  break;
                        
                        default:        break;
                    }
                }
                
                if(movelist.length()>0){
                    System.out.println(movelist);
                    click2 = true;
                }
                else{
                    System.out.println("Click On a Valid Piece!");
                }

                repaint();            
            }
        }
        else if(click2 == true)                                     //Checks if second click
        {                                  
            if( ((x-base_x) < (8*disp))   &&   (x > base_x)     
            &&  ((y-base_y) < (8*disp))   &&   (y > base_y)
            &&  (e.getButton()  ==  MouseEvent.BUTTON1)   )
            {                                                       //Checks if click is leftclick and inside the board        
                int newRow, newCol;
                boolean moveTrue = false;
                newRow = 7 - ( (y-base_y)/disp );
                newCol = (x-base_x)/disp;
                System.out.println("Movelist Length = "+movelist.length()/5);
                for(int i=0; i<movelist.length()/5; i++){
                    
                    String temp = movelist.substring((i*5), (i*5)+5);
                    //if pawn promotion
                    if((oldRow==6 && dispCB[7 - oldRow][oldCol]==W_PAWN)
                    || (oldRow==1 && dispCB[7 - oldRow][oldCol]==B_PAWN))
                    {
                        if(newCol==Character.getNumericValue(temp.charAt(2))){
                            //Found Move
                            moveTrue = true;
                            break;
                        }
                        //
                    }
                    else
                    {
                        if(newRow == Character.getNumericValue(temp.charAt(3)) &&
                        newCol == Character.getNumericValue(temp.charAt(4))){
                            //Found Move
                            moveTrue = true;
                            break;
                        }
                    }
                }
                System.out.println("Square Clicked Is - "+newRow+", "+newCol);
                System.out.println("Moves Selected is "+moveTrue);
                if(moveTrue == true){
                                        
                    //Update Display Array
                    char pieceCap = updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    
                    //Update Bitboard
                    long capPos = updateBitBoard(oldRow, oldCol, newRow, newCol);
                    
                    //Update Bitboard for Captured Piece
                    updateCapBitBoard(capPos, pieceCap);
                    
                    //Update Current Player
                    moveW = moveW != true;
                }
            }
            click2 = false;
            repaint();
        }
        Moves.UpdateCap();
        Moves.printString2("Pieces Black Cannot Capture", Moves.PIECES_B_CANT_CAPTURE);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    private char updateDisplayArray(int oldRow, int oldCol, int newRow, int newCol) {
        System.out.println("Updating Display Array: "+(7-newRow)+", "+newCol
                                        +" is now"+dispCB[7 - oldRow][oldCol]+" While: "
                                        +(7-oldRow)+", "+oldCol+" is now BLANK");
        char temp = dispCB[7 - newRow][newCol];
        dispCB[7 - newRow][newCol] = dispCB[7 - oldRow][oldCol];
        dispCB[7 - oldRow][oldCol] = BLANK;
        return temp;
    }

    private long updateBitBoard(int oldRow, int oldCol, int newRow, int newCol) {
        long oldPos = Moves.getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = Moves.getBitBoardCorrespondingTo((newRow * 8) + newCol);
                    
        if(moveW == true){
            switch(dispCB[7 - newRow][newCol]){
                case W_PAWN:    Moves.WP = Moves.WP & (~oldPos);
                                Moves.WP = Moves.WP | newPos;
                                break;
                case W_KING:    Moves.WK = Moves.WK & (~oldPos);
                                Moves.WK = Moves.WK | newPos;
                                break;
                case W_QUEEN:   Moves.WQ = Moves.WQ & (~oldPos);
                                Moves.WQ = Moves.WQ | newPos;
                                break;
                case W_ROOK:    Moves.WR = Moves.WR & (~oldPos);
                                Moves.WR = Moves.WR | newPos;
                                break;
                case W_BISHOP:  Moves.WB = Moves.WB & (~oldPos);
                                Moves.WB = Moves.WB | newPos;
                                break;
                case W_KNIGHT:  Moves.WN = Moves.WN & (~oldPos);
                                Moves.WN = Moves.WN | newPos;
                                break;

                default:        break;
            }
        }
        else{
            System.out.println("Black Move Bitboard Update!!!!!!!!!!");
            switch(dispCB[7 - newRow][newCol]){
                case B_PAWN:    Moves.BP = Moves.BP & (~oldPos);
                                Moves.BP = Moves.BP | newPos;
                                break;
                case B_KING:    Moves.BK = Moves.BK & (~oldPos);
                                Moves.BK = Moves.BK | newPos;
                                break;
                case B_QUEEN:   Moves.BQ = Moves.BQ & (~oldPos);
                                Moves.BQ = Moves.BQ | newPos;
                                break;
                case B_ROOK:    Moves.BR = Moves.BR & (~oldPos);
                                Moves.BR = Moves.BR | newPos;
                                break;
                case B_BISHOP:  Moves.BB = Moves.BB & (~oldPos);
                                Moves.BB = Moves.BB | newPos;
                                break;
                case B_KNIGHT:  Moves.BN = Moves.BN & (~oldPos);
                                Moves.BN = Moves.BN | newPos;
                                break;

                default:        break;
            }
        }
        return newPos;
    }

    private void updateCapBitBoard(long pos, char pieceCap) {
        if(moveW == false){
            switch(pieceCap){
                case W_PAWN:    Moves.WP = Moves.WP & (~pos);
                                break;
                case W_KING:    Moves.WK = Moves.WK & (~pos);
                                break;
                case W_QUEEN:   Moves.WQ = Moves.WQ & (~pos);
                                break;
                case W_ROOK:    Moves.WR = Moves.WR & (~pos);
                                break;
                case W_BISHOP:  Moves.WB = Moves.WB & (~pos);
                                break;
                case W_KNIGHT:  Moves.WN = Moves.WN & (~pos);
                                break;

                default:        break;
            }
        }
        else{
            switch(pieceCap){
                case B_PAWN:    Moves.BP = Moves.BP & (~pos);
                                break;
                case B_KING:    Moves.BK = Moves.BK & (~pos);
                                break;
                case B_QUEEN:   Moves.BQ = Moves.BQ & (~pos);
                                break;
                case B_ROOK:    Moves.BR = Moves.BR & (~pos);
                                break;
                case B_BISHOP:  Moves.BB = Moves.BB & (~pos);
                                break;
                case B_KNIGHT:  Moves.BN = Moves.BN & (~pos);
                                break;

                default:        break;
            }
        }
    }
    
    private void highlightSquares(Graphics g) {
        int newRow, newCol;
        if(click2 == true)
        {
            for(int i=0; i<movelist.length()/5; i++)
            {
                String temp = movelist.substring((i*5), (i*5)+5);
                //if not pawn promotion
                if(!Character.isLetter(temp.charAt(3)))
                {
                    newRow = Character.getNumericValue(temp.charAt(3));
                    newCol = Character.getNumericValue(temp.charAt(4));
                    g.drawImage(img_green, base_x + (newCol*disp), base_y + ( (7-newRow) * disp ), this);
                    System.out.println("Green Square Debugging - " + temp + ": " + newRow+", "+newCol);
                }
                else
                {
                    newCol = Character.getNumericValue(temp.charAt(2));                    
                    g.drawImage(img_green, base_x + (newCol*disp), base_y, this);                                         
                }
            }
        }
    }        

    private Image makeTransparent(String imagepath, int hexTransparency )
    {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(imagepath));
        } catch (IOException e) {
            System.out.println("Image path " +imagepath+ " not found ");
        }
        
        ImageFilter filter = new RGBImageFilter()
        {
          @Override
          public final int filterRGB(int x, int y, int rgb)
          {
            return rgb & hexTransparency;
          }
        };

        ImageProducer ip = new FilteredImageSource(bufferedImage.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
}

