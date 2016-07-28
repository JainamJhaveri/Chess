package main;

import java.awt.Dimension;
import javax.swing.JFrame;

public class AlphaBetaChess {
    public static void main(String[] args) {
        JFrame f = new JFrame("Smart Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui = new UserInterface();
        f.add(ui);

        
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//        f.setUndecorated(true);
        f.setVisible(true);
    }    
}
