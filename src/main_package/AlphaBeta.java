package main_package;

import static utils.Constants.DEPTH;

class AlphaBeta
{

    private String minimaxmove = "";
    private String alphabetamove = "";

    /*  ----------  testing alphabeta ----------- */
    String getAlphabetamove()
    {
        BBStruct currentBB = new BBStruct(); // initialize current bitboards in a BBStruct object
        int ans = alphaBetaMax(DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, currentBB);
        System.out.println(" Over here: " +ans);
        return alphabetamove;
    }
    /*  ----------  testing alphabeta ----------- */

    /*  ----------  code for alphabeta ----------- */
    private int alphaBetaMin(int depth, int alpha, int beta, BBStruct bb)
    {
        if( depth == 0 ) return -bb.evaluate();
        String movelist = bb.getMoves();

        for( int i=0; i<movelist.length()/5; i++ )
        {
            String move = movelist.substring((i*5), (i*5)+5);

            BBStruct mybb = new BBStruct(bb);
            mybb.makeMove(move);
            int score = alphaBetaMax(depth-1, alpha, beta, mybb);

            if( score <= alpha )
            {
                return alpha;
            }
            if( score < beta )
            {
                beta = score;
            }
        }
        return beta;
    }

    private int alphaBetaMax(int depth, int alpha, int beta, BBStruct bb)
    {
        if( depth == 0 ) return bb.evaluate();
        String movelist = bb.getMoves();

        for( int i=0; i<movelist.length()/5; i++ )
        {
            String move = movelist.substring((i*5), (i*5)+5);

            BBStruct mybb = new BBStruct(bb);
            mybb.makeMove(move);
            int score = alphaBetaMin(depth-1, alpha, beta, mybb);

            if( score >= beta )
            {
                return beta;
            }
            if( score > alpha )
            {
                alpha = score;
                if( depth == DEPTH )
                    alphabetamove = move;         // TODO: return proper move
            }
        }
        return alpha;
    }
    /*  ----------  code for alphabeta ----------- */







    /*  ----------  testing minimax ----------- */
    String getMinimaxMoveForBlack()
    {
        BBStruct currentBB = new BBStruct(); // initialize current bitboards in a BBStruct object
        int ans = maxi(DEPTH, currentBB);    // perform minimax algorithm and get the best move till DEPTH
        System.out.println( "Here: " + ans );
        return minimaxmove;
    }
    /*  ----------  testing minimax ----------- */

    /*  ----------  code for minimax ----------- */
    private int maxi(int depth, BBStruct bb)
    {
        if( depth == 0 ) return bb.evaluate();

        int max = Integer.MIN_VALUE;
        String movelist = bb.getMoves();

        for( int i=0; i<movelist.length()/5; i++ )
        {
            String move = movelist.substring((i*5), (i*5)+5);

            BBStruct mybb = new BBStruct(bb);
            mybb.makeMove(move);
            int score = mini(depth-1, mybb);

            if( score > max)
            {
                max = score;
                if(depth == DEPTH)
                    minimaxmove = move;         // TODO: return proper move
            }

        }

        return max;
    }

    private int mini(int depth, BBStruct bb)
    {
        if( depth == 0 ) return bb.evaluate();

        int min = Integer.MAX_VALUE;
        String movelist = bb.getMoves();

        for( int i=0; i<movelist.length()/5; i++ )
        {
            String move = movelist.substring((i*5), (i*5)+5);

            BBStruct mybb = new BBStruct(bb);
            mybb.makeMove(move);
            int score = maxi(depth-1, mybb);

            if( score < min)
            {
                min = score;
            }

        }

        return min;
    }
    /*  ----------  code for minimax ----------- */

}