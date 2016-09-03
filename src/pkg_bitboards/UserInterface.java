package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class UserInterface extends JPanel implements MouseListener, MouseMotionListener{
    Image img_board, img_green;
    Image img_B, img_K, img_N, img_P, img_Q, img_R;
    Image img_b, img_k, img_n, img_p, img_q, img_r;
    static int base_x = 20, base_y = 40, disp = 53;
    static char dispCB[][] = new char[8][8];
    static int mouseX, mouseY, newMouseX, newMouseY;
    static boolean click2 = false, listenerset = false;
    static String movelist = "";
    
    public UserInterface(char[][] initialCB) {
        dispCB = initialCB;
        // image of board
        img_board = new ImageIcon(path_board).getImage();
        img_green = new ImageIcon(path_greenSq).getImage();
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
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(listenerset==false){
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            listenerset = true;
        }
        
        
        g.drawImage(img_board, base_x, base_y, this);        
        
        /*--------------------------------------------------------------------------------------------------------
        Square Highlighting
        ---------------------------------------------------------------------------------------------------------*/
        if(click2 == true){
            for(int i=0; i<movelist.length()/5; i++){
                String temp = movelist.substring((i*5), (i*5)+5);
                //if not pawn promotion
                if(!Character.isLetter(temp.charAt(3))){
                    int a = Character.getNumericValue(temp.charAt(3));
                    int b = Character.getNumericValue(temp.charAt(4));
                    g.drawImage(img_green, base_x+(b*disp), base_y+((7-a)*disp), this);
                    System.out.println("Green Square Debugging - "+temp);
                    System.out.println("Green Square Debugging - "+a+", "+b);
                }
                else{
                    int b = Character.getNumericValue(temp.charAt(2));
                    g.drawImage(img_green, base_x+(b*disp), base_y, this);
                    System.out.println("Green Square Debugging - "+temp);
                    System.out.println("Green Square Debugging - "+b);
                }
            }
        }
        
        
        
        /*--------------------------------------------------------------------------------------------------------
        img_piece is used as a reference pointer to the images iteratively. Following 'for' loop will populate 
                the chessboard with images of pieces according to the contents of dispCB array.
        ---------------------------------------------------------------------------------------------------------*/
        Image img_piece;        
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                switch(dispCB[i][j]){
                    case B_PAWN:    img_piece = img_p;
                                    break;
                    case B_KING:    img_piece = img_k;
                                    break;
                    case B_QUEEN:   img_piece = img_q;
                                    break;
                    case B_ROOK:    img_piece = img_r;
                                    break;
                    case B_BISHOP:  img_piece = img_b;
                                    break;
                    case B_KNIGHT:  img_piece = img_n;
                                    break;
                    case W_PAWN:    img_piece = img_P;
                                    break;
                    case W_KING:    img_piece = img_K;
                                    break;
                    case W_QUEEN:   img_piece = img_Q;
                                    break;
                    case W_ROOK:    img_piece = img_R;
                                    break;
                    case W_BISHOP:  img_piece = img_B;
                                    break;
                    case W_KNIGHT:  img_piece = img_N;
                                    break;   
                    default:        img_piece = null;
                                    break;
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
        
        if(click2 == false){                                  //Checks if first click
            if((e.getX()-base_x)<(8*disp) && e.getX()>base_x 
            && (e.getY()-base_y)<(8*disp) && e.getY()>base_y
            && e.getButton()==MouseEvent.BUTTON1){      //Checks if click is inside the board
                mouseX = (e.getX()-base_x)/disp;
                mouseY = 7 - ((e.getY()-base_y)/disp);
                
                //Y moves vertically and thus covers all rows - Similarly, X covers all columns
                String dispSq = ""+mouseY+", "+mouseX;
                System.out.println(dispSq);
            
                long position = Moves.getBitBoardCorrespondingTo((mouseY * 8) + mouseX);
            
                switch(dispCB[7 - mouseY][mouseX]){
                        case B_PAWN:    movelist += Moves.possibleBP(position);
                                        System.out.println(movelist);
                                        break;
                        case B_KING:    movelist += Moves.possibleK(position,IAMBLACK);
                                        System.out.println(movelist);
                                        break;
                        case B_QUEEN:   movelist += Moves.possibleQ(position,IAMBLACK);
                                        System.out.println(movelist);
                                        break;
                        case B_ROOK:    movelist += Moves.possibleR(position,IAMBLACK);
                                        System.out.println(movelist);
                                        break;
                        case B_BISHOP:  movelist += Moves.possibleB(position,IAMBLACK);
                                        System.out.println(movelist);
                                        break;
                        case B_KNIGHT:  movelist += Moves.possibleN(position,IAMBLACK);
                                        System.out.println(movelist);
                                        break;
                        case W_PAWN:    movelist += Moves.possibleWP(position);
                                        System.out.println(movelist);
                                        break;
                        case W_KING:    movelist += Moves.possibleK(position,IAMWHITE);
                                        System.out.println(movelist);
                                        break;
                        case W_QUEEN:   movelist += Moves.possibleQ(position,IAMWHITE);
                                        System.out.println(movelist);
                                        break;
                        case W_ROOK:    movelist += Moves.possibleR(position,IAMWHITE);
                                        System.out.println(movelist);
                                        break;
                        case W_BISHOP:  movelist += Moves.possibleB(position,IAMWHITE);
                                        System.out.println(movelist);
                                        break;
                        case W_KNIGHT:  movelist += Moves.possibleN(position,IAMWHITE);
                                        System.out.println(movelist);
                                        break;   
                        default:        
                                        break;
                    }
                click2 = true;
                repaint();
            
            }
        }
        else if(click2 == true){                                  //Checks if first click
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

}
