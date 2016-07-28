package main;

import static main.Constants.*;
import java.awt.*;
import javax.swing.*;

class UserInterface extends JPanel{
    Image img_board;
    Image img_B, img_K, img_N, img_P, img_Q, img_R;
    Image img_b, img_k, img_n, img_p, img_q, img_r;
    static int base_x = 20, base_y = 40, disp = 53;
    
//    static int x = 0, y = 0;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
                
        g.drawImage(img_board, 20, 40, this);        

        // initial stage for black pieces
        g.drawImage(img_r, base_x, base_y, this);
        g.drawImage(img_n, base_x + disp, base_y, this);
        g.drawImage(img_b, base_x + 2 * disp, base_y, this);
        g.drawImage(img_q, base_x + 3 * disp, base_y, this);
        g.drawImage(img_k, base_x + 4 * disp, base_y, this);
        g.drawImage(img_b, base_x + 5 * disp, base_y, this);
        g.drawImage(img_n, base_x + 6 * disp, base_y, this);
        g.drawImage(img_r, base_x + 7 * disp, base_y, this);
        
        // initial stage for black pawns
        int dy1 = base_y + disp;        
        for( int i=0; i<8; i++ ){
            int dx1 = base_x + i * disp;            
            g.drawImage(img_p, dx1, dy1, this);
        }
        
        // initial stage for white pawns
        dy1 = base_y + 6 * disp;        
        for( int i=0; i<8; i++ ){
            int dx1 = base_x + i * disp;                        
            g.drawImage(img_P, dx1, dy1, this);
        }
        
        // initial stage for black pieces
        dy1 = base_y + 7 * disp;
        g.drawImage(img_R, base_x, dy1, this);
        g.drawImage(img_N, base_x + disp, dy1, this);
        g.drawImage(img_B, base_x + 2 * disp, dy1, this);
        g.drawImage(img_Q, base_x + 3 * disp, dy1, this);
        g.drawImage(img_K, base_x + 4 * disp, dy1, this);
        g.drawImage(img_B, base_x + 5 * disp, dy1, this);
        g.drawImage(img_N, base_x + 6 * disp, dy1, this);
        g.drawImage(img_R, base_x + 7 * disp, dy1, this);        
        
    }

}
