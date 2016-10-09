import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * The main class creating all the components needed to start the simulation.
 * It creates the JFrame with a JPanel to paint on, a random Ball and the
 * math-simulation of the ball.
 */
public class Main {

    /**
     * The method is executed to start the ball-simulation It crates the ball and the field
     * and stats all processes to simulate the ball movement.
     * @param args first arg is the width of the field, second one the height of the field
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("SEP Einführungsaufgabe 2016");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        int[] val = validateSize(args);
        BallPanel panel = new BallPanel(val[0], val[1]);

        frame.setSize(val[0], val[1]);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        Ball ball = Ball.randomBall(20, val[0], val[1]);

        CalcThread calc = new CalcThread(ball, panel.getSize());
        calc.addObserver(panel);

        Thread thread = new Thread();
        thread.start();

    }

    /**
     * Validates the users arguments
     * @param args An two-dimensional array holding the panel width on [0] and the height on [1]
     * @return the users input size if valid, default values width:640 height:480 if not valid
     */
    public static int[] validateSize(String[] args) {
        int[] result = {640, 480};
        if (args.length != 0 && args[0] != null && args[1] != null) {
            int x = 640,y = 480;
            try{
                x = Integer.parseInt(args[0]);
                y = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){ }
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int)screenSize.getWidth();
            int height = (int)screenSize.getHeight();
            if(x > 100 && x <= width && y > 100 && y <= height){
                result[0] = x;
                result[1] = y;
            }
        }
        return result;
    }
}


