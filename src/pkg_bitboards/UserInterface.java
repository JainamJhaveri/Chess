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
    private int boardCol, boardRow;
    private boolean click2 = false;
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
//        highlightSquares(g, Moves.unsafeForWhite());
        
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
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        movelist = "";
        int x = e.getX();
        int y = e.getY();
        if(click2 == false){                                  //Checks if first click
            if( ((x-base_x) < (8*disp))   &&   (x > base_x)//Checks if click is inside the board
            &&  ((y-base_y) < (8*disp))   &&   (y > base_y)
            &&  (e.getButton()  ==  MouseEvent.BUTTON1)   )        
            {      
                boardRow = 7 - ( (y-base_y)/disp );
                boardCol = (x-base_x)/disp;                
                
                //Y moves vertically and thus covers all rows - Similarly, X covers all columns
                System.out.println(boardRow+", "+boardCol);
            
                long position = Moves.getBitBoardCorrespondingTo((boardRow * 8) + boardCol);
            
                switch(dispCB[7 - boardRow][boardCol]){
                        case B_PAWN:    movelist = Moves.possibleBP(position);  break;
                        case B_KING:    movelist = Moves.possibleK(position,IAMBLACK);  break;
                        case B_QUEEN:   movelist = Moves.possibleQ(position,IAMBLACK);  break;
                        case B_ROOK:    movelist = Moves.possibleR(position,IAMBLACK);  break;
                        case B_BISHOP:  movelist = Moves.possibleB(position,IAMBLACK);  break;
                        case B_KNIGHT:  movelist = Moves.possibleN(position,IAMBLACK);  break;
                        
                        case W_PAWN:    movelist = Moves.possibleWP(position);  break;
                        case W_KING:    movelist = Moves.possibleK(position,IAMWHITE);  break;
                        case W_QUEEN:   movelist = Moves.possibleQ(position,IAMWHITE);  break;
                        case W_ROOK:    movelist = Moves.possibleR(position,IAMWHITE);  break;
                        case W_BISHOP:  movelist = Moves.possibleB(position,IAMWHITE);  break;
                        case W_KNIGHT:  movelist = Moves.possibleN(position,IAMWHITE);  break;   
                        
                        default:        break;
                    }
                System.out.println(movelist);
                click2 = true;

                repaint();            
            }
        }
        else if(click2 == true)         //Checks if first click
        {                                  
           click2 = false;
           repaint();
        }
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
    
    private void highlightSquares(Graphics g, String mymovelist) {
        int newRow, newCol;
        if(click2 == true)
        {
            for(int i=0; i<mymovelist.length()/5; i++)
            {
                String temp = mymovelist.substring((i*5), (i*5)+5);
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
