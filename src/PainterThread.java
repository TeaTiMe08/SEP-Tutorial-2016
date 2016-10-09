import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Repaints the panel every 25 milliseconds
 */
public class PainterThread extends Thread {

    private BallPanel panel;
    private boolean doPaint;

    public PainterThread(BallPanel panel){
        doPaint = true;
        this.panel = panel;
    }

    @Override
    public void run() {
        Timer timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
            }
        });
        timer.start();
        /*
        while(doPaint) {
            panel.repaint();
            try{
                this.sleep(25);
            } catch (InterruptedException e){}
        }
        */
    }

    public boolean isDoPaint() {
        return doPaint;
    }

    public void setDoPaint(boolean doPaint) {
        this.doPaint = doPaint;
    }
}
