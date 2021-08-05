public interface GameHandler {

    int getBoardValue(int i, int j);

    void setBoardValue(int i, int j, int value);

    void handleClick(int i, int j);

}
