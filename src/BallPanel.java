import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents the View of the program. It uses the PainterThread to repaint
 * the ball on the panel.
 */
public class BallPanel extends JPanel implements Observer {

    private Ball state;
    private JLabel rollLabel;
    private PainterThread p;

    public BallPanel(int width, int height){
        setSize(width, height);
        setBackground(Color.black);
        setVisible(true);

        // Create the label and add it to the panel
        rollLabel = new JLabel();
        this.add(rollLabel);

        p = new PainterThread(this);
        p.start();

    }

    public void stop(){
        p.setDoPaint(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO: für arg = ball muss doPaint = false;
        //TODO: arg = Ball, also muss state = arg
        state = (Ball)arg;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(state != null) {
            // Paints the label if ball is rolling or not
            if(state.isMoving) {
                rollLabel.setText("GO");
                rollLabel.setForeground(Color.green);
            } else {
                rollLabel.setText("STOP");
                rollLabel.setForeground(Color.red);
            }
            // Paints the ball
            g2d.setBackground(Color.pink);
            g2d.fillOval(
                    (int)state.getxPos(),
                    (int)state.getyPos(),
                    (int)state.getRadius(),
                    (int)state.getRadius()
            );
        }
    }
}
