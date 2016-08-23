package pkg_bitboards;

public class Constants {
    public static final String basepath = "C:\\Users\\jaina_000\\Documents\\Chess\\";
//    public static final String black_basepath = basepath + "CP\\B\\";
//    public static final String white_basepath = basepath + "CP\\W\\";
    public static final String black_basepath = basepath + "CP\\blackpcs\\";
    public static final String white_basepath = basepath + "CP\\whitepcs\\";
    
    public static final String path_board = basepath + "CP\\chessboard.png";    
    
    public static final String path_B = white_basepath + "B.png";
    public static final String path_K = white_basepath + "K.png";
    public static final String path_N = white_basepath + "N.png";
    public static final String path_P = white_basepath + "P.png";
    public static final String path_Q = white_basepath + "Q.png";
    public static final String path_R = white_basepath + "R.png";
        
    public static final String path_b = black_basepath + "b.png";
    public static final String path_k = black_basepath + "k.png";
    public static final String path_n = black_basepath + "n.png";
    public static final String path_p = black_basepath + "p.png";
    public static final String path_q = black_basepath + "q.png";
    public static final String path_r = black_basepath + "r.png";
    
    public static final char W_ROOK = 'R';
    public static final char W_KNIGHT = 'N';
    public static final char W_BISHOP = 'B';
    public static final char W_QUEEN = 'Q';
    public static final char W_KING = 'K';   
    public static final char W_PAWN = 'P';
    public static final char BLANK = ' ';
    public static final char B_ROOK = 'r';
    public static final char B_KNIGHT = 'n';
    public static final char B_BISHOP = 'b';
    public static final char B_QUEEN = 'q';
    public static final char B_KING = 'k';   
    public static final char B_PAWN = 'p';
 
    public static char promotedTo[] = {W_QUEEN, W_ROOK, W_KNIGHT, W_BISHOP};
    
    public static long BackDiagonalMask[] = 
    {
        0x1L,
        0x102L,
        0x10204L,
        0x1020408L,
        0x102040810L,
        0x10204081020L,
        0x1020408102040L,
        0x102040810204080L,
        0x204081020408000L,        
        0x408102040800000L,
        0x810204080000000L,
        0x1020408000000000L,
        0x2040800000000000L,
        0x4080000000000000L,
        0x8000000000000000L
    };
    public static long ForwardDiagonalMask[] = 
    {
        0x80L,
        0x8040L,
        0x804020L,
        0x80402010L,
        0x8040201008L,
        0x804020100804L,
        0x80402010080402L,
        0x8040201008040201L,
        0x4020100804020100L,
        0x2010080402010000L,
        0x1008040201000000L,
        0x804020100000000L,
        0x402010000000000L,
        0x201000000000000L,
        0x100000000000000L
    };
    public static long RankMask[] = 
    {
        0xFFL,
        0xFF00L,
        0xFF0000L,
        0xFF000000L,
        0xFF00000000L,
        0xFF0000000000L,
        0xFF000000000000L,
        0xFF00000000000000L
    };
    public static long FileMask[] = 
    {
        0x0101010101010101L,
        0x0202020202020202L,
        0x0404040404040404L,
        0x0808080808080808L,
        0x1010101010101010L,   
        0x2020202020202020L,
        0x4040404040404040L,   
        0x8080808080808080L
    };
    
    public static long RANK_1 = 0xFFL;
    public static long RANK_2 = 0xFF00L;
    public static long RANK_3 = 0xFF0000L;
    public static long RANK_4 = 0xFF000000L;
    public static long RANK_5 = 0xFF00000000L;
    public static long RANK_6 = 0xFF0000000000L;
    public static long RANK_7 = 0xFF000000000000L;
    public static long RANK_8 = 0xFF00000000000000L;   
                        
    public static long FILE_A = 0x0101010101010101L;
    public static long FILE_B = 0x0202020202020202L;
    public static long FILE_C = 0x0404040404040404L;
    public static long FILE_D = 0x0808080808080808L;
    public static long FILE_E = 0x1010101010101010L;    
    public static long FILE_F = 0x2020202020202020L;
    public static long FILE_G = 0x4040404040404040L;    
    public static long FILE_H = 0x8080808080808080L;
}
