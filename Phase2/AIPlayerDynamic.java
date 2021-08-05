import java.awt.*;

public class AIPlayerDynamic extends GamePlayer {

    private int searchDepth;
    private OnlineComparator evaluator;

    public AIPlayerDynamic(int mark, int depth) {
        super(mark);
        searchDepth = depth;
        evaluator = new OnlineComparator();
    }

    @Override
    public boolean isUserPlayer() {
        return false;
    }

    @Override
    public String playerName() {
        return "Dynamic AI (Depth " + searchDepth + ")";
    }

    @Override
    public Point play(int[][] board) {
        return Minimax.solve(board,myMark,searchDepth,evaluator);
    }
}
