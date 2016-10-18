package main_package;

import temp.BBStruct;

import static utils.Constants.DEPTH;

class Minimax
{

    private String minimaxmove = "";


    /*  ----------  testing minimax ----------- */
    String getMinimaxMoveForBlack()
    {
        BBStruct currentBB = new BBStruct(); // initialize current bitboards in a BBStruct object
        int ans = maxi(DEPTH, currentBB);    // perform minimax algorithm and get the best move till DEPTH
        System.out.println( "Here: " + ans );
        return minimaxmove;
    }
    /*  ----------  testing minimax ----------- */

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
//            bb.unmakeMove(move);

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

//            if(depth == DEPTH-1)
//                minimaxmove = move;         // TODO: return proper move
        }

        return min;
    }




}
