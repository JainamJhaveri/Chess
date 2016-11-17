package main_package;

import static utils.Constants.DEPTH;

class AlphaBeta
{

    private String moveSeq[] = new String[DEPTH+1];
    private String moveSeqMain[] = new String[DEPTH+1];
    private String alphabetamove = "";

    String getAlphabetamove()
    {
        BBStruct currentBB = new BBStruct(); // initialize current bitboards in a BBStruct object
        int ans = alphaBetaMax(DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, currentBB);
        System.out.println(" Over here: " +ans);
        printArr(moveSeqMain);
        return alphabetamove;
    }

    private void printArr(String[] list) {
        System.out.print("\nfinal sequence: ");
        for( String move: list){
            System.out.print(move+" ");
        }
    }

    private void printArr(String[] moveSeq, int score, int alpha, int beta) {
        System.out.print("\nmovesequence: ");
        for( String move: moveSeq){
            System.out.print(move+" ");
        }
        System.out.print("\tscore: " +score +" a: "+alpha+" b: "+beta);
    }

    private int alphaBetaMin(int depth, int alpha, int beta, BBStruct bb)
    {
        if( depth == 0 ) return -Rating.evaluate(bb);
        String movelist = bb.getMoves();
        String move;

        for( int i=0; i<movelist.length()/5; i++ )
        {
            move = movelist.substring((i*5), (i*5)+5);
            moveSeq[depth] = move;
            BBStruct mybb = new BBStruct(bb);
            mybb.makeMove(move);

            int score = alphaBetaMax(depth-1, alpha, beta, mybb);
            if( score <= alpha ) {
                return alpha;
            }
            if( score < beta )
            {
                moveSeq[depth] = move;
                moveSeqMain = moveSeq;
                printArr(moveSeq, score, alpha, beta);
                beta = score;
            }
        }
        return beta;
    }

    private int alphaBetaMax(int depth, int alpha, int beta, BBStruct bb)
    {
        if( depth == 0 ) return Rating.evaluate(bb);
        String movelist = bb.getMoves();
        String move;

        for( int i=0; i<movelist.length()/5; i++ )
        {
            move = movelist.substring((i*5), (i*5)+5);
            moveSeq[depth] = move;
            BBStruct mybb = new BBStruct(bb);
            mybb.makeMove(move);

            int score = alphaBetaMin(depth-1, alpha, beta, mybb);
            if( score >= beta ) {
                return beta;
            }
            if( score > alpha )
            {
                moveSeq[depth] = move;
                moveSeqMain = moveSeq;
                printArr(moveSeq, score, alpha, beta);
                alpha = score;
                alphabetamove = moveSeq[DEPTH];
            }
        }
        return alpha;
    }

}



//
//    /*  ----------  testing minimax ----------- */
//    String getMinimaxMoveForBlack()
//    {
//        BBStruct currentBB = new BBStruct(); // initialize current bitboards in a BBStruct object
//        int ans = maxi(DEPTH, currentBB);    // perform minimax algorithm and get the best move till DEPTH
//        System.out.println( "Here: " + ans );
//        return minimaxmove;
//    }
//    /*  ----------  testing minimax ----------- */
//
//    /*  ----------  code for minimax ----------- */
//    private int maxi(int depth, BBStruct bb)
//    {
//        if( depth == 0 ) return bb.evaluate();
//
//        int max = Integer.MIN_VALUE;
//        String movelist = bb.getMoves();
//
//        for( int i=0; i<movelist.length()/5; i++ )
//        {
//            String move = movelist.substring((i*5), (i*5)+5);
//
//            BBStruct mybb = new BBStruct(bb);
//            mybb.makeMove(move);
//            int score = mini(depth-1, mybb);
//
//            if( score > max)
//            {
//                max = score;
//                if(depth == DEPTH)
//                    minimaxmove = move;
//            }
//
//        }
//
//        return max;
//    }
//
//    private int mini(int depth, BBStruct bb)
//    {
//        if( depth == 0 ) return bb.evaluate();
//
//        int min = Integer.MAX_VALUE;
//        String movelist = bb.getMoves();
//
//        for( int i=0; i<movelist.length()/5; i++ )
//        {
//            String move = movelist.substring((i*5), (i*5)+5);
//
//            BBStruct mybb = new BBStruct(bb);
//            mybb.makeMove(move);
//            int score = maxi(depth-1, mybb);
//
//            if( score < min)
//            {
//                min = score;
//            }
//
//        }
//
//        return min;
//    }
//    /*  ----------  code for minimax ----------- */