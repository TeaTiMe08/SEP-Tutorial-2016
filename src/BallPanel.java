import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents the View of the program. It repaints
 */
public class BallPanel extends JPanel implements Observer {

    private Ball state;
    private boolean isPainting;

    public BallPanel(int width, int height){
        setSize(width, height);
        setBackground(Color.black);
        setVisible(true);

        isPainting = false;
        PainterThread p = new PainterThread(this);
        p.start();

    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO: arg = Ball, also muss state = arg
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(state != null) {
            g2d.fillOval(
                    (int)state.getxPos(),
                    (int)state.getyPos(),
                    (int)state.getRadius(),
                    (int)state.getRadius()
            );
        }
    }

    public boolean isPainting() {
        return isPainting;
    }
}
