import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * The main class creating all the components needed to start the simulation.
 * It creates the JFrame with a JPanel to paint on, a random Ball and the
 * math-simulation of the ball.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("SEP Einführungsaufgabe 2016");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        int[] val = validateSize(args);
        BallPanel panel = new BallPanel(val[0], val[1]);

        frame.setSize(val[0], val[1]);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        Ball ball = randomBall(20, val[0], val[1]);

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
            } catch (NumberFormatException e){}
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

    /**
     * Returns a Ball with random position and Speed in a given borderrange.
     * @param radius the fix radius the ball should have
     * @param xBorder the maximum width the ball can have
     * @param yBorder the maximum height the ball can have
     * @return a random generated Ball
     */
    public static Ball randomBall(double radius, int xBorder, int yBorder) {
        double xPos, yPos, xVec, yVec;
        Random ran = new Random();
        xPos = (ran.nextDouble() * (double)xBorder) - 0.6 * radius;
        yPos = (ran.nextDouble() * (double)yBorder) - 0.6 * radius;
        // TODO: Vector einbringen

        return new Ball(xPos, yPos, radius);
    }
}


