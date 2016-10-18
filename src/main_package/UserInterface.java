package main_package;

import temp.BBStruct;

import static utils.Constants.*;
import static utils.MethodUtils.*;
import static main_package.Moves.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener
{
    private final Image img_board, img_green;
    private final Image img_B, img_K, img_N, img_P, img_Q, img_R;
    private final Image img_b, img_k, img_n, img_p, img_q, img_r;
    private final int base_x = 20, base_y = 40, disp = 53;
    private static char dispCB[][] = new char[8][8];
    private int oldCol = 0, oldRow = 0;     // oldRow, oldCol are row, col of bitboard
    private boolean click2 = false;
    private String movelist = "";


    public UserInterface(char[][] initialCB)
    {

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
    protected void paintComponent(Graphics g)
    {
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
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // Y moves vertically and thus covers all rows - Similarly, X covers all columns
        int x = e.getX();
        int y = e.getY();

        if( !click2 )
        {
            handleFirstClick(x, y, e);
        }
        else
        {
            handleSecondClick(x, y, e);
            // if it is black's move then print minimax move for black
            if( !moveW )
            {

                long starttime = System.currentTimeMillis();
                Minimax mm = new Minimax();
                System.out.println( mm.getMinimaxMoveForBlack() );
                long endtime = System.currentTimeMillis();

                System.out.println("eval time: "+(endtime-starttime) + " ms");
            }
        }
    }

    /**
     * This method updates dispCB array by removing the piece at oldRow, oldCol and moving it to newRow, newCol.
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     */
    private void updateDisplayArray(int oldRow, int oldCol, int newRow, int newCol)
    {
        System.out.println("Updating Display Array: "+(7-newRow)+", "+newCol
                +" is now ->"+dispCB[7 - oldRow][oldCol]+", While: "
                +(7-oldRow)+", "+oldCol+" is now BLANK");
        char temp = dispCB[7 - newRow][newCol];
        dispCB[7 - newRow][newCol] = dispCB[7 - oldRow][oldCol];
        dispCB[7 - oldRow][oldCol] = BLANK;
    }


    private char promotePawn(long pos)
    {
        int choice ;
        char piece = ' ';
        do
        {
            System.out.println("What do you wish to promote the pawn to?");
            System.out.println("1.Queen, 2.Rook, 3.Bishop, 4.Knight");
            System.out.println("Enter Choice (1/2/3/4) - ");
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
        } while( choice < 1 || choice > 4 );

        if( moveW )
        {
            WP = WP & ~pos;
            switch( choice )
            {
                case 1: WQ = WQ | pos;  piece = W_QUEEN;
                        break;
                case 2: WR = WR | pos;  piece = W_ROOK;
                        break;
                case 3: WB = WB | pos;  piece = W_BISHOP;
                        break;
                case 4: WN = WN | pos;  piece = W_KNIGHT;
                        break;
                default:    System.out.println("Warning! Invalid Pawn Promotion Chosen! Program Should Never Reach Here");
                            break;
            }
        }
        else
        {
            BP = BP & ~pos;
            switch(choice)
            {
                case 1: BQ = BQ | pos;  piece = B_QUEEN;
                        break;
                case 2: BR = BR | pos;  piece = B_ROOK;
                        break;
                case 3: BB = BB | pos;  piece = B_BISHOP;
                        break;
                case 4: BN = BN | pos;  piece = B_KNIGHT;
                        break;
                default:    System.out.println("Warning! Invalid Pawn Promotion Chosen! Program Should Never Reach Here");
                            break;
            }
        }
        updateCap();
        return piece;
    }

    private void highlightSquares(Graphics g)
    {
        int newRow, newCol;
        if( click2 )
        {
            for(int i=0; i<movelist.length()/5; i++)
            {
                String temp = movelist.substring((i*5), (i*5)+5);
                System.out.println(temp+ ": " + temp.charAt(4));

                //if not pawn promotion                
                if( Character.isDigit(temp.charAt(3)) )
                {
                    newRow = Character.getNumericValue(temp.charAt(3));
                    newCol = Character.getNumericValue(temp.charAt(4));
                    g.drawImage(img_green, base_x + (newCol * disp), base_y + ( (7-newRow) * disp ), this);
//                    System.out.println("Green Square Debugging - " + temp + ": " + newRow+", "+newCol);
                }
                // if pawn promotion
                else if( temp.charAt(4) == 'P' )
                {
                    newCol = Character.getNumericValue(temp.charAt(2));
//                    System.out.println("Green Square Debugging: " + temp );
                    if( moveW )
                        g.drawImage(img_green, base_x + (newCol * disp), base_y, this);
                    else
                        g.drawImage(img_green, base_x + (newCol * disp), base_y + (7 * disp), this);
                }
//                // if enpass move
                else if( temp.charAt(4) == 'E')
                {
                    newCol = Character.getNumericValue(temp.charAt(2));
                    if( moveW )
                        g.drawImage(img_green, base_x + (newCol * disp), base_y + (2 * disp), this);
                    else
                        g.drawImage(img_green, base_x + (newCol * disp), base_y + (5 * disp), this);
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

        assert bufferedImage != null;
        ImageProducer ip = new FilteredImageSource(bufferedImage.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    /**
     * Checks if clicked inside the board and the click is a left-click
     * @param x: x=e.getX();
     * @param y: y=e.getY();
     * @param e
     * @return true if clicked inside the board else false
     */
    private boolean isClickedInsideBoard(int x, int y, MouseEvent e)
    {
       return      ((x-base_x) < (8*disp))   &&   (x > base_x)
               &&  ((y-base_y) < (8*disp))   &&   (y > base_y)
               &&  (e.getButton()  ==  MouseEvent.BUTTON1);
    }


    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}

    private void handleFirstClick(int x, int y, MouseEvent e)
    {
        movelist = "";
        if( isClickedInsideBoard(x, y, e) )
        {
            oldRow = 7 - ( (y - base_y)/disp );
            oldCol = (x - base_x)/disp;
            long position = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);

            System.out.println(oldRow+", "+oldCol);

            if( moveW )
            {
                switch(dispCB[7 - oldRow][oldCol])
                {
                    case W_PAWN:    movelist = possibleP(position,IAMWHITE);  break;
                    case W_KING:    movelist = possibleK(position,IAMWHITE) + possibleCastle(IAMWHITE);  break;
                    case W_QUEEN:   movelist = possibleQ(position,IAMWHITE);  break;
                    case W_ROOK:    movelist = possibleR(position,IAMWHITE);  break;
                    case W_BISHOP:  movelist = possibleB(position,IAMWHITE);  break;
                    case W_KNIGHT:  movelist = possibleN(position,IAMWHITE);  break;

                    default:        break;
                }
            }
            else
            {
                switch(dispCB[7 - oldRow][oldCol])
                {
                    case B_PAWN:    movelist = possibleP(position,IAMBLACK);  break;
                    case B_KING:    movelist = possibleK(position,IAMBLACK) + possibleCastle(IAMBLACK);  break;
                    case B_QUEEN:   movelist = possibleQ(position,IAMBLACK);  break;
                    case B_ROOK:    movelist = possibleR(position,IAMBLACK);  break;
                    case B_BISHOP:  movelist = possibleB(position,IAMBLACK);  break;
                    case B_KNIGHT:  movelist = possibleN(position,IAMBLACK);  break;

                    default:        break;
                }
            }

            if(movelist.length() > 0)
            {
                System.out.println("handleFirstClick: " +movelist);
                click2 = true;
                repaint();
            }
            else
            {
                System.out.println("Either Blank square or a piece whose moves are not possible or "
                        + "opposite player's piece is clicked is clicked!");
            }

        }
    }

    private void handleSecondClick(int x, int y, MouseEvent e)
    {
        if( isClickedInsideBoard(x, y, e)   )
        {
            int newRow = 7 - ( (y - base_y)/disp );
            int newCol = (x - base_x)/disp;
            boolean canMove = false;    // debugging variable to check if any of highlighted square is clicked

            System.out.println("Movelist Length = "+movelist.length()/5);

            for(int i=0; i < movelist.length()/5; i++)
            {
                String temp = movelist.substring((i * 5), (i * 5) + 5);

                if( isPromotionMove(temp, newRow, newCol) )
                {
                    System.out.println("promotion move temp: " +temp);
                    canMove = true;

                    updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    updateBitBoard(oldRow, oldCol, newRow, newCol);
                    char piecePromotedTo = promotePawn(getBitBoardCorrespondingTo((newRow * 8) + newCol));

                    dispCB[7 - newRow][newCol] = piecePromotedTo;

                    moveW = !moveW;
                    break;
                }
                else if( isCastleMove(temp, newCol, newRow, oldRow, oldCol) )
                {
                    System.out.println("castle move temp: " +temp);
                    canMove = true;

                    // update kings bitboard and disp array
                    updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    updateBitBoard(oldRow, oldCol, newRow, newCol);

                    int rook_oldCol;
                    int rook_newCol;
                    if( newCol == 6 )
                    {
                        rook_oldCol = 7;
                        rook_newCol = 5;
                    }

                    else
                    {
                        rook_oldCol = 0;
                        rook_newCol = 3;
                    }

                    // update rooks bitboard and disp array
                    updateDisplayArray(oldRow, rook_oldCol, newRow, rook_newCol);
                    updateBitBoard(oldRow, rook_oldCol, newRow, rook_newCol);

                    moveW = !moveW;
                    break;
                }
                else if( isEnpassMove(temp, newCol, newRow, oldCol)  )
                {
                    System.out.println("enpass move temp: " +temp);
                    canMove = true;

                    updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    updateBitBoard(oldRow, oldCol, newRow, newCol);

                    char pawn;
                    int capRow;
                    if( moveW )
                    {
                        pawn = B_PAWN;
                        capRow = newRow - 1;
                    }
                    else
                    {
                        pawn = W_PAWN;
                        capRow = newRow + 1;
                    }

                    updateDisplayArray(oldRow, oldCol, capRow, newCol);
                    updatePawnCapBitBoard(getBitBoardCorrespondingTo((capRow * 8) + newCol), pawn);

                    moveW = !moveW;
                    break;
                }
                else if( isGeneralMove(temp, newCol, newRow) )
                {
                    System.out.println("general move temp: " +temp);
                    canMove = true;

                    updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    updateBitBoard(oldRow, oldCol, newRow, newCol);

                    moveW = !moveW;
                    break;
                }
            }

            System.out.println("New Square Clicked Is - "+newRow+", "+newCol);
            System.out.println("Selected Move is " +canMove);

        }
        click2 = false;


        repaint();

        isCheckmateStalemate();

    }

    /***
     * called after each move to check if checkmate or stalemate condition has reached.
     * Game over and program quits in both cases else the game continues normally
     */
    private void isCheckmateStalemate()
    {
        System.out.println("movelist from isCheckmateStalemate: " +movelist);

        if(moveW)
        {
            if(possibleWMoves().length() != 0) return;
            if( (WK & unsafeForWhite()) != 0 )
            {
                System.out.println("checkmate > > black wins");
            }
            else
            {
                System.out.println("stalemate");
            }
        }

        else
        {
            if(possibleBMoves().length() != 0) return;
            if( (BK & unsafeForBlack()) != 0 )
            {
                System.out.println("checkmate > > white wins");
            }
            else
            {
                System.out.println("stalemate");
            }
        }
        System.out.println("---- game over ----");
        System.exit(0);
        // resetBoard();
    }


    /***
     * 1. finds and removes opposite piece (if it is present) at oldposition(oldRow, oldCol) on it's bitboard
     * 2. removes my piece from oldposition(oldRow, oldCol) and adding to newPositon(newRow, newCol) on it's bitboard
     * 3. calls updateCap() method
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     */
    private static void updateBitBoard(int oldRow, int oldCol, int newRow, int newCol)
    {
        long oldPos = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = getBitBoardCorrespondingTo((newRow * 8) + newCol);

        if( moveW )
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (BB & newPos) != 0)      BB &= ~newPos;
            else if( (BN & newPos) != 0) BN &= ~newPos;
            else if( (BP & newPos) != 0) BP &= ~newPos;
            else if( (BQ & newPos) != 0) BQ &= ~newPos;
            else if( (BR & newPos) != 0) BR &= ~newPos;
            else if( (BK & newPos) != 0) BK &= ~newPos;
            else System.out.println("UI.updateBitBoard: Blank square where your piece want to move");

            // removing mypiece from oldposition and moving to newposition
            if( (WB & oldPos) != 0)      { WB &= ~oldPos; WB |= newPos; }
            else if( (WN & oldPos) != 0) { WN &= ~oldPos; WN |= newPos; }
            else if( (WP & oldPos) != 0) { WP &= ~oldPos; WP |= newPos; }
            else if( (WQ & oldPos) != 0) { WQ &= ~oldPos; WQ |= newPos; }
            else if( (WR & oldPos) != 0) { WR &= ~oldPos; WR |= newPos;
                                            if( CASTLEW_QSIDE && ((WR & CASTLE_ROOKS[0]) == 0) )
                                                CASTLEW_QSIDE = false;
                                            else if ( CASTLEW_KSIDE && ( (WR & CASTLE_ROOKS[1]) == 0) )
                                                CASTLEW_KSIDE = false;  }
            else if( (WK & oldPos) != 0) { WK &= ~oldPos; WK |= newPos;
                                            CASTLEW_KSIDE = false;
                                            CASTLEW_QSIDE = false;  }
            else System.out.println("UI.updateBitBoard: shouldn't reach here!");
        }
        else
        {
            // removing opponent's piece if it existed on newposition where mypiece is going to be moved
            if( (WB & newPos) != 0)      WB &= ~newPos;
            else if( (WN & newPos) != 0) WN &= ~newPos;
            else if( (WP & newPos) != 0) WP &= ~newPos;
            else if( (WQ & newPos) != 0) WQ &= ~newPos;
            else if( (WR & newPos) != 0) WR &= ~newPos;
            else if( (WK & newPos) != 0) WK &= ~newPos;
            else System.out.println("updateBitBoard: Blank piece where your piece want to move");

            // removing mypiece from oldposition and moving to newposition
            if( (BB & oldPos) != 0)      { BB &= ~oldPos; BB |= newPos; }
            else if( (BN & oldPos) != 0) { BN &= ~oldPos; BN |= newPos; }
            else if( (BP & oldPos) != 0) { BP &= ~oldPos; BP |= newPos; }
            else if( (BQ & oldPos) != 0) { BQ &= ~oldPos; BQ |= newPos; }
            else if( (BR & oldPos) != 0) { BR &= ~oldPos; BR |= newPos;
                                            if( CASTLEB_QSIDE && ((BR & CASTLE_ROOKS[2]) == 0) )
                                                CASTLEB_QSIDE = false;
                                            else if ( CASTLEB_KSIDE && ((BR & CASTLE_ROOKS[3]) == 0) )
                                                CASTLEB_KSIDE = false;  }
            else if( (BK & oldPos) != 0) { BK &= ~oldPos; BK |= newPos;
                                            CASTLEB_KSIDE = false;
                                            CASTLEB_QSIDE = false;  }
            else System.out.println("shouldn't reach here!");
        }

        history = ""+ oldRow + oldCol + newRow + newCol;
        updateCap();
    }


    /**
     * This method updates bitboard of captured piece using pos bitboard.
     * pos bitboard is returned by the updateBitBoard(..) method.
     * pos is the bit board of position to which the selected piece had moved.
     * @param pos position which should be removed from bitboard of captured piece
     * @param pieceCap char piece whose bitboard needs to be updated
     */
    private static void updatePawnCapBitBoard(long pos, char pieceCap)
    {
        switch(pieceCap)
        {
            case B_PAWN:    BP &= ~pos; break;
            case W_PAWN:    WP &= ~pos; break;
            default: System.out.println("Shouldn't reach here !");
        }
        updateCap();
    }


}