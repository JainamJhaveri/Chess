package pkg_bitboards;

import static pkg_bitboards.Constants.*;
import static pkg_bitboards.MethodUtils.*;
import static pkg_bitboards.Moves.*;
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

        if( click2 == false )
        {
            handleFirstClick(x, y, e);
        }
        else if( click2 == true )
        {
            handleSecondClick(x, y, e);
        }
    }

    /**
     * This method updates dispCB array by removing the piece at oldRow, oldCol and moving it to newRow, newCol.
     * It returns char value of piece of dispCB that was previously present at newRow, newCol.
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     * @return char value of the piece at newRow, newCol that was present in the dispCB before.
     */
    public char updateDisplayArray(int oldRow, int oldCol, int newRow, int newCol)
    {
        System.out.println("Updating Display Array: "+(7-newRow)+", "+newCol
                                +" is now ->"+dispCB[7 - oldRow][oldCol]+", While: "
                                                    +(7-oldRow)+", "+oldCol+" is now BLANK");
        char temp = dispCB[7 - newRow][newCol];
        dispCB[7 - newRow][newCol] = dispCB[7 - oldRow][oldCol];
        dispCB[7 - oldRow][oldCol] = BLANK;

        return temp;
    }


    private char promotePawn(long pos)
    {
        int choice = 0;
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
                case 1: WQ = WQ | pos;
                        piece = W_QUEEN;
                        printString2("WQueen", WQ);
                        break;
                case 2: WR = WR | pos;
                        piece = W_ROOK;
                        printString2("WRook", WR);
                        break;
                case 3: WB = WB | pos;
                        piece = W_BISHOP;
                        printString2("WBishop", WB);
                        break;
                case 4: WN = WN | pos;
                        piece = W_KNIGHT;
                        printString2("WKnight", WN);
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
                case 1: BQ = BQ | pos;
                        piece = B_QUEEN;
                        printString2("BQueen", BQ);
                        break;
                case 2: BR = BR | pos;
                        piece = B_ROOK;
                        printString2("BRook", BR);
                        break;
                case 3: BB = BB | pos;
                        piece = B_BISHOP;
                        printString2("BBishop", BB);
                        break;
                case 4: BN = BN | pos;
                        piece = B_KNIGHT;
                        printString2("BKnight", BN);
                        break;
                default:    System.out.println("Warning! Invalid Pawn Promotion Chosen! Program Should Never Reach Here");
                            break;
            }
        }
        return piece;
    }

    private void highlightSquares(Graphics g)
    {
        int newRow, newCol;
        if( click2 == true)
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

                    char capPiece = updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    long capPos = updateBitBoard(oldRow, oldCol, newRow, newCol);
                    updateCapBitBoard(capPos, capPiece);

                    char piecePromotedTo = promotePawn(capPos);
                    dispCB[7 - newRow][newCol] = piecePromotedTo;

                    moveW = !moveW;
                    UpdateCap();

                    break;

                }
                else if( MethodUtils.isCastleMove(temp, newCol, newRow, oldRow, oldCol) )
                {
                    System.out.println("castle move temp: " +temp);
                    canMove = true;

                    // update kings bitboard and disp array
                    updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    updateBitBoard(oldRow, oldCol, newRow, newCol);
                    UpdateCap();

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
                    UpdateCap();
                }
                else if( isEnpassMove(temp, newCol, newRow, oldCol)  )
                {
                    System.out.println("castle move temp: " +temp);
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
                    updateCapBitBoard(getBitBoardCorrespondingTo( (capRow * 8) + newCol), pawn);

                    moveW = !moveW;
                    UpdateCap();

                    break;
                }
                else if( isGeneralMove(temp, newCol, newRow) )
                {
                    System.out.println("general move temp: " +temp);
                    canMove = true;

                    char capPiece = updateDisplayArray(oldRow, oldCol, newRow, newCol);
                    long capPos = updateBitBoard(oldRow, oldCol, newRow, newCol);
                    updateCapBitBoard(capPos, capPiece);

                    moveW = !moveW;
                    UpdateCap();

                    break;
                }
            }

            System.out.println("New Square Clicked Is - "+newRow+", "+newCol);
            System.out.println("Selected Move is " +canMove);

        }
        click2 = false;
        repaint();
    }


    /**
     * This method updates bitboard of Moved Piece that was previously at oldRow, oldCol and moved to newRow, newCol.
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     * @return returns bit board of new position to which our piece moved. This bitboard corresponding to new position
     *  can be used to update bitboard of captured piece
     */
    private static long updateBitBoard(int oldRow, int oldCol, int newRow, int newCol)
    {
        long oldPos = getBitBoardCorrespondingTo((oldRow * 8) + oldCol);
        long newPos = getBitBoardCorrespondingTo((newRow * 8) + newCol);

        if(moveW)
        {
            switch(dispCB[7 - newRow][newCol])
            {
                case W_PAWN:    WP = WP & ~oldPos;
                    WP = WP | newPos;
                    break;
                case W_KING:    WK = WK & ~oldPos;
                    WK = WK | newPos;
                    CASTLEW_KSIDE = false;
                    CASTLEW_QSIDE = false;
                    break;
                case W_QUEEN:   WQ = WQ & ~oldPos;
                    WQ = WQ | newPos;
                    break;
                case W_ROOK:    WR = WR & ~oldPos;
                    WR = WR | newPos;
                    if( CASTLEW_QSIDE && ((WR & CASTLE_ROOKS[0]) == 0) )
                        CASTLEW_QSIDE = false;
                    else if ( CASTLEW_KSIDE && ( (WR & CASTLE_ROOKS[1]) == 0) )
                        CASTLEW_KSIDE = false;

                    break;
                case W_BISHOP:  WB = WB & ~oldPos;
                    WB = WB | newPos;
                    break;
                case W_KNIGHT:  WN = WN & ~oldPos;
                    WN = WN | newPos;
                    break;

                default:        break;
            }
        }
        else
        {
            switch(dispCB[7 - newRow][newCol])
            {
                case B_PAWN:    BP = BP & ~oldPos;
                    BP = BP | newPos;
                    break;
                case B_KING:    BK = BK & ~oldPos;
                    BK = BK | newPos;
                    break;
                case B_QUEEN:   BQ = BQ & ~oldPos;
                    BQ = BQ | newPos;
                    break;
                case B_ROOK:    BR = BR & ~oldPos;
                    BR = BR | newPos;
                    if( CASTLEB_QSIDE && ((BR & CASTLE_ROOKS[2]) == 0) )
                        CASTLEB_QSIDE = false;
                    else if ( CASTLEB_KSIDE && ((BR & CASTLE_ROOKS[3]) == 0) )
                        CASTLEB_KSIDE = false;
                    break;
                case B_BISHOP:  BB = BB & ~oldPos;
                    BB = BB | newPos;
                    break;
                case B_KNIGHT:  BN = BN & ~oldPos;
                    BN = BN | newPos;
                    break;

                default:        break;
            }
        }
        history = ""+ oldRow + oldCol + newRow + newCol;
        return newPos;
    }

    /**
     * This method updates bitboard of captured piece using pos bitboard.
     * pos bitboard is returned by the updateBitBoard(..) method.
     * pos is the bit board of position to which the selected piece had moved.
     * @param pos position which should be removed from bitboard of captured piece
     * @param pieceCap char piece whose bitboard needs to be updated
     */
    private static void updateCapBitBoard(long pos, char pieceCap)
    {
        // If no piece was captured, we can safely return without checking any case as no bitboard needs to be updated
        if( pieceCap == BLANK ) return;

        if ( moveW ) {
            switch(pieceCap)
            {
                case B_PAWN:    BP = BP & ~pos; break;
                case B_KING:    BK = BK & ~pos; break;
                case B_QUEEN:   BQ = BQ & ~pos; break;
                case B_ROOK:    BR = BR & ~pos; break;
                case B_BISHOP:  BB = BB & ~pos; break;
                case B_KNIGHT:  BN = BN & ~pos; break;

                default:        break;
            }
        } else {
            switch(pieceCap)
            {
                case W_PAWN:    WP = WP & ~pos; break;
                case W_KING:    WK = WK & ~pos; break;
                case W_QUEEN:   WQ = WQ & ~pos; break;
                case W_ROOK:    WR = WR & ~pos; break;
                case W_BISHOP:  WB = WB & ~pos; break;
                case W_KNIGHT:  WN = WN & ~pos; break;

                default:        break;
            }
        }
    }

}