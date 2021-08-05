import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PlayGround extends JPanel implements GameHandler {
    int[][] board;
    int turn = 1;
    PlayGroundTile[][] cells;
    JLabel score1;
    JLabel score2;

    int sum1 = 0;
    int sum2 = 0;

    GamePlayer player1 = new AI(1,6,true);
    GamePlayer player2 = new UserPlayer(2);


    Timer player1HandlerTimer;
    Timer player2HandlerTimer;

    @Override
    public int getBoardValue(int i,int j){
        return board[i][j];
    }

    @Override
    public void setBoardValue(int i,int j,int value){
        board[i][j] = value;
    }

    public PlayGround(){
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        JPanel reversiBoard = new JPanel();
        reversiBoard.setLayout(new GridLayout(8,8));
        reversiBoard.setPreferredSize(new Dimension(500,500));
        reversiBoard.setBackground(new Color(41,100, 59));

        resetBoard();

        cells = new PlayGroundTile[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new PlayGroundTile(this,reversiBoard,i,j);
                reversiBoard.add(cells[i][j]);
            }
        }

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar,BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(0,40));

        score1 = new JLabel("Score 1");
        score2 = new JLabel("Score 2");

        sidebar.add(score1);
        sidebar.add(score2);


        this.add(sidebar,BorderLayout.NORTH);
        this.add(reversiBoard);

        updateBoardInfo();

        player1HandlerTimer = new Timer(1000,(ActionEvent e) -> {
            handleAI(player1);
            player1HandlerTimer.stop();
            manageTurn();
        });

        player2HandlerTimer = new Timer(1000,(ActionEvent e) -> {
            handleAI(player2);
            player2HandlerTimer.stop();
            manageTurn();
        });

        manageTurn();
    }

    private boolean awaitForClick = false;

    public void manageTurn(){
        if(BoardHelper.hasAnyMoves(board,1) || BoardHelper.hasAnyMoves(board,2)) {
            updateBoardInfo();
            if (turn == 1) {
                if(BoardHelper.hasAnyMoves(board,1)) {
                    if (player1.isUserPlayer()) {
                        awaitForClick = true;
                    } else {
                        player1HandlerTimer.start();
                    }
                }else{
                    System.out.println("AI has no legal moves!");
                    turn = 2;
                    manageTurn();
                }
            } else {
                if(BoardHelper.hasAnyMoves(board,2)) {
                    if (player2.isUserPlayer()) {
                        awaitForClick = true;
                    } else {
                        player2HandlerTimer.start();
                    }
                }else{
                    System.out.println("User has no legal moves!");
                    turn = 1;
                    manageTurn();
                }
            }
        }else{
            System.out.println("Game Finished!");
            updateBoardInfo();
            int winner = BoardHelper.getWinner(board);
            if(winner==1) {
                System.out.println("AI won the game!");
                sum1++;
            }
            else if(winner==2) {
                System.out.println("You won the game!");
                sum2++;
            }
        }
    }

    public void resetBoard(){
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j]=0;
            }
        }
        setBoardValue(3,3,2);
        setBoardValue(3,4,1);
        setBoardValue(4,3,1);
        setBoardValue(4,4,2);
    }

    public void updateBoardInfo(){

        int p1score = 0;
        int p2score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == 1) p1score++;
                if(board[i][j] == 2) p2score++;

                if(BoardHelper.canPlay(board,turn,i,j)){
                    cells[i][j].highlight = 1;
                }else{
                    cells[i][j].highlight = 0;
                }
            }
        }

        score1.setText(player1.playerName() + " : " + p1score);
        score2.setText(player2.playerName() + " : " + p2score);
    }


    @Override
    public void handleClick(int i,int j){
        if(awaitForClick && BoardHelper.canPlay(board,turn,i,j)){
            System.out.println("User Played in : "+ i + " , " + j);

            //update board
            board = BoardHelper.getNewBoardAfterMove(board,new Point(i,j),turn);
            turn = (turn == 1) ? 2 : 1;

            repaint();

            awaitForClick = false;
            manageTurn();
        }
    }

    public void handleAI(GamePlayer ai){
        Point aiPlayPoint = ai.play(board);
        int i = aiPlayPoint.x;
        int j = aiPlayPoint.y;
        if(!BoardHelper.canPlay(board,ai.myMark,i,j)) System.err.println("AI Invalid Move !");
        System.out.println(ai.playerName() + " Played in : "+ i + " , " + j);

        board = BoardHelper.getNewBoardAfterMove(board,aiPlayPoint,turn);

        turn = (turn == 1) ? 2 : 1;

        repaint();
    }

}
