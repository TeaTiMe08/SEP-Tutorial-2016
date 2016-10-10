import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The PainterThread is part of the View.
 * It is created in the BallPanel and receives the BallPanel itself
 * Every 25 milliseconds the BallPanel should be repainted witch is handled in the run()-method.
 */
public class PainterThread extends Thread {

    private BallPanel panel;

    /**
     * Created a new PainterThread
     * @param panel the BallPanel passes itself to the PainterThread so it can be repainted.
     */
    public PainterThread(BallPanel panel){
        this.panel = panel;
    }

    /**
     * In this method a Timer is created witch repaints the BallPanel every 25 milliseconds.
     */
    @Override
    public void run() {
        Timer timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
            }
        });
        timer.start();
    }
}
