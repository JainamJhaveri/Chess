package main_package;

import static main_package.UpdateBitBoards.*;
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
    private boolean performFlag = false;    // flag to indicate whether alpha-beta move prediction should be made or not


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
    public void paint(Graphics g)
    {
        super.paint(g);

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
        perform();
    }

    private void perform() {
        if( moveW || !performFlag) return;

        // if it is black's move then evaluate and print minimax move for black
        long starttime = System.currentTimeMillis();
        AlphaBeta mm = new AlphaBeta();
        System.out.println( "\nCPU move: " +mm.getAlphabetamove() );
        long endtime = System.currentTimeMillis();

        System.out.println("evaluation time: "+(endtime-starttime) + " ms");
        performFlag = false;
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
            perform();
        }
    }

    /**
     * This method updates dispCB array by removing the piece at oldRow, oldCol and moving it to newRow, newCol.
     * @param oldRow where piece was placed
     * @param oldCol where piece was placed
     * @param newRow where piece is to be placed
     * @param newCol where piece is to be placed
     */
    private void updateDisplayArray(int oldRow, int oldCol, int newRow, int newCol)
    {
        dispCB[7 - newRow][newCol] = dispCB[7 - oldRow][oldCol];
        dispCB[7 - oldRow][oldCol] = BLANK;
    }


    private void highlightSquares(Graphics g)
    {
        int newRow, newCol;
        if( !click2 ) return;

        for(int i=0; i<movelist.length()/5; i++)
        {
            String temp = movelist.substring((i*5), (i*5)+5);

            //if not pawn promotion
            if( Character.isDigit(temp.charAt(3)) )
            {
                newRow = Character.getNumericValue(temp.charAt(3));
                newCol = Character.getNumericValue(temp.charAt(4));
                g.drawImage(img_green, base_x + (newCol * disp), base_y + ( (7-newRow) * disp ), this);
            }
            // if pawn promotion
            else if( temp.charAt(4) == 'P' )
            {
                newCol = Character.getNumericValue(temp.charAt(2));
                if( moveW )
                    g.drawImage(img_green, base_x + (newCol * disp), base_y, this);
                else
                    g.drawImage(img_green, base_x + (newCol * disp), base_y + (7 * disp), this);
            }
            // if enpass move
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
     * @param e: mouseEvent received from mouseReleased, mouseEntered. ... methods
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
                System.err.println("Either Blank square or a piece whose moves are not possible or "
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
                    performFlag = true;

                    updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    updateBitBoard(oldRow, oldCol, newRow, newCol);
                    char piecePromotedTo = updatePromotePawnBitBoard(newRow, newCol);

                    dispCB[7 - newRow][newCol] = piecePromotedTo;

                    moveW = !moveW;
                    break;
                }
                else if( isCastleMove(temp, newCol, newRow, oldRow, oldCol) )
                {
                    System.out.println("castle move temp: " +temp);
                    performFlag = true;

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
                    performFlag = true;

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
                    updatePawnCapBitBoard(capRow, newCol, pawn);

                    moveW = !moveW;
                    break;
                }
                else if( isGeneralMove(temp, newCol, newRow) )
                {
                    System.out.println("general move temp: " +temp);
                    performFlag = true;

                    updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    updateBitBoard(oldRow, oldCol, newRow, newCol);

                    moveW = !moveW;
                    break;
                }
                
            }

            System.out.println("New Square Clicked Is - "+newRow+", "+newCol);
            System.out.println("Selected Move is " +performFlag);

        }
        click2 = false;

        paint(this.getGraphics());

        if( isCheckmateStalemate() )
        {
            System.exit(0);
            // resetBoard();
        }

    }

    /***
     * called after each move to check if checkmate or stalemate condition has reached.
     * Game over and program quits in both cases else the game continues normally
     */
    private boolean isCheckmateStalemate()
    {
        System.out.println("movelist from isCheckmateStalemate: " +movelist);

        if(moveW)
        {
            if(possibleWMoves().length() != 0) return false;
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
            if(possibleBMoves().length() != 0) return false;
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
        return true;
    }


}