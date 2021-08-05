import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow(){
        PlayGround gp = new PlayGround();
        this.add(gp);
        this.setTitle("Othello Reversi");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GameWindow();
    }

}
