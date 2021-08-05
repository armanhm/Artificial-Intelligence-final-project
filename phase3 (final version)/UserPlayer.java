import java.awt.*;

public class UserPlayer extends GamePlayer {

    public UserPlayer(int mark) {
        super(mark);
    }

    @Override
    public boolean isUserPlayer() {
        return true;
    }

    @Override
    public String playerName() {
        return "User (white)" ;
    }

    @Override
    public Point play(int[][] board) {
        return null;
    }

}
