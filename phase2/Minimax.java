import java.awt.*;

public class Minimax {

    static int nodesExplored = 0;

    //returns max score move
    public static Point solve(int[][] board, int player, int depth, Comparator e){
        nodesExplored = 0;
        int bestScore = Integer.MIN_VALUE;
        Point bestMove = null;
        for(Point move : BoardHelper.getAllPossibleMoves(board,player)){
            //create new node
            int[][] newNode = BoardHelper.getNewBoardAfterMove(board,move,player);
            //recursive call
            int childScore = MMAB(newNode,player,depth-1,false,Integer.MIN_VALUE,Integer.MAX_VALUE,e);
                if(childScore > bestScore) {
                bestScore = childScore;
                bestMove = move;
            }
        }
        System.out.println("Nodes Explored : " + nodesExplored);
        return bestMove;
    }

    //minimax value for a node with A/B pruning
    private static int MMAB(int[][] node, int player, int depth, boolean max, int alpha, int beta, Comparator e){
        nodesExplored++;
        //terminal reached or depth limit
        if(depth == 0 || BoardHelper.isGameFinished(node)){
            return e.eval(node,player);
        }
        int oplayer = (player==1) ? 2 : 1;
        //no moves available
        if((max && !BoardHelper.hasAnyMoves(node,player)) || (!max && !BoardHelper.hasAnyMoves(node,oplayer))){
            return MMAB(node,player,depth-1,!max,alpha,beta,e);
        }
        int score;
        if(max){
            //maximizing
            score = Integer.MIN_VALUE;
            for(Point move : BoardHelper.getAllPossibleMoves(node,player)){ //my turn
                //new node
                int[][] newNode = BoardHelper.getNewBoardAfterMove(node,move,player);
                //recursive
                int childScore = MMAB(newNode,player,depth-1,false,alpha,beta,e);
                if(childScore > score) score = childScore;
                //update alpha
                if(score > alpha) alpha = score;
                if(beta <= alpha) break; //Beta Cutoff
            }
        }else{
            //minimizing
            score = Integer.MAX_VALUE;
            for(Point move : BoardHelper.getAllPossibleMoves(node,oplayer)){ //opponent turn
                //new node
                int[][] newNode = BoardHelper.getNewBoardAfterMove(node,move,oplayer);
                //recursive
                int childScore = MMAB(newNode,player,depth-1,true,alpha,beta,e);
                if(childScore < score) score = childScore;
                //update beta
                if(score < beta) beta = score;
                if(beta <= alpha) break; //Alpha Cutoff
            }
        }
        return score;
    }

}
