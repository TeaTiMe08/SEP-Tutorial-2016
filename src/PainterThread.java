/**
 * Created by dabian on 05.10.16.
 */
public class PainterThread extends Thread {

    private BallPanel panel;

    public PainterThread(BallPanel panel){
        this.panel = panel;
    }

    @Override
    public void run() {
        while(panel.isPainting()) {
            panel.repaint();
            try{
                this.sleep(25);
            } catch (InterruptedException e){}
        }
    }
}
