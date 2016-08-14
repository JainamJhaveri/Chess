package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import java.awt.*;
import javax.swing.*;

class UserInterface extends JPanel{
    Image img_board;
    Image img_B, img_K, img_N, img_P, img_Q, img_R;
    Image img_b, img_k, img_n, img_p, img_q, img_r;
    static int base_x = 20, base_y = 40, disp = 53;
    static char dispCB[][] = new char[8][8];
    
    public UserInterface(char[][] initialCB) {
        dispCB = initialCB;
        // image of board
        img_board = new ImageIcon(path_board).getImage();
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
                        
        g.drawImage(img_board, 20, 40, this);        

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

}
