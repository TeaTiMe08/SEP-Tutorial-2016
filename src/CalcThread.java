import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Random;

/**
 * Created by dabian on 06.10.16.
 */
public class CalcThread extends Observable implements Runnable  {

    private boolean doCalc;
    private Ball ball;
    private Dimension fieldSize;
    private int waitTime;

    public CalcThread(Ball ball, Dimension fieldSize){
        waitTime = 10;
        this.ball = ball;
        this.fieldSize = fieldSize;
    }

    @Override
    public void run() {
        Random random = new Random();
        Timer time = new Timer(waitTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check, if the ball is out if range
                if(     ball.getxPos() - ball.getRadius() < 0
                        || ball.getyPos() - ball.getRadius() < 0
                        || ball.getxPos() + ball.getRadius() > fieldSize.getWidth()
                        || ball.getyPos() + ball.getRadius() > fieldSize.getHeight() ) {
                    System.err.println("Ball on illegal Position");
                    System.out.println(ball.toString());
                    System.out.println(fieldSize.toString());
                } else if(Math.hypot(ball.getxVec(), ball.getyVec()) < 0.0001) {
                    // if the balls speed is so low, that it stops
                    ball.setMoving(false);
                    // wait 3-10 seconds to reset the balls speed vectors
                    waitTime = random.nextInt(7000) + 3000;
                    ball.setVec(random.nextDouble(), random.nextDouble());
                    ball.setMoving(true);
                    setChanged();
                    notifyObservers(ball);
                } else {
                // simulating the next 10 steps to check, if the model has to be updated in every step
                double newXPos = ball.getxPos();
                double tempXVec = ball.getxVec();
                double newYPos = ball.getyPos();
                double tempYVec = ball.getyVec();
                double resVec = Math.hypot(tempXVec, tempYVec);
                double tempSum = tempXVec + tempYVec;
                for(int i=0;i<10;i++) {
                    //TODO: err resVec - resVec + 0.0001 = 0.0001
                    resVec = resVec - (Math.hypot(tempXVec, tempYVec) - 0.0001);
                    newXPos = newXPos + tempXVec;
                    tempXVec = tempXVec - resVec * (tempXVec / tempSum);
                    newYPos = newYPos + tempYVec;
                    tempYVec = tempYVec - resVec * (tempYVec / tempSum);
                    tempSum = tempXVec + tempYVec;
                }
                if(     newXPos - ball.getRadius() < 0
                        || newYPos - ball.getRadius() < 0
                        || newXPos + ball.getRadius() > fieldSize.getWidth()
                        || newYPos + ball.getRadius() > fieldSize.getHeight() ) {
                    // when ball has no collision in the next 10 steps the program skips all these
                    // and st
                    ball.setPos(newXPos, newYPos);
                    ball.setVec(tempXVec, tempYVec);
                    setChanged();
                    notifyObservers(ball);
                    waitTime = 10;
                } else {
                    // when the ball would collide in the next ten steps, the program calculates every
                    // step to the collision
                    tempSum = ball.getxVec() + ball.getyVec();
                    tempXVec = ball.getxVec() - 0.0001 * (tempXVec / tempSum);
                    tempYVec = ball.getyVec() - 0.0001 * (tempYVec / tempSum);
                    newXPos = ball.getxPos() + tempXVec;
                    newYPos = ball.getyPos() + tempYVec;
                    if(     newXPos - ball.getRadius() < 0
                            || newYPos - ball.getRadius() < 0
                            || newXPos + ball.getRadius() > fieldSize.getWidth()
                            || newYPos + ball.getRadius() > fieldSize.getHeight() ) {
                        // when there is a collision with a wall in this step
                        // collisiontreatment:
                        double intersection;
                        // when the ball hits the left wall in this step
                        if(newXPos - ball.getRadius() < 0) {
                            intersection = ball.getyPos()
                                    + ball.getxPos()
                                    * (ball.getyVec() / ball.getxVec());
                            ball.setVec(Math.abs(ball.getxVec()), ball.getyVec());
                            intersection = intersection
                                    + (ball.getxVec() -  ball.getxPos())
                                    * (ball.getyVec() / ball.getxVec());
                            ball.setPos(ball.getxVec() - ball.getxPos(), intersection);
                            ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                            setChanged();
                            notifyObservers(ball);
                            waitTime = 1;
                        }
                        // when the ball hits the bottom wall in this step
                        if(newYPos - ball.getRadius() < 0) {
                            intersection = ball.getxPos()
                                    + ball.getyPos()
                                    * (ball.getxVec() / ball.getyVec());
                            ball.setVec(ball.getxVec(), Math.abs(ball.getyVec()));
                            //TODO: vll 3. zeile austauschen
                            intersection = intersection
                                    + (ball.getyVec() - ball.getyPos())
                                    * (ball.getyVec() / ball.getxVec());
                            ball.setPos(intersection, ball.getyVec() - ball.getyPos());
                            ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                            setChanged();
                            notifyObservers(ball);
                            waitTime = 1;
                        }
                        // when the ball hits the right wall in this step
                        if(newXPos + ball.getRadius() > fieldSize.getWidth()) {
                            intersection = ball.getyPos()
                                    + (fieldSize.getWidth() - ball.getxPos())
                                    * (ball.getyVec() / ball.getxVec());
                            ball.setVec(-Math.abs(ball.getxVec()), ball.getyVec());
                            intersection = intersection
                                    + (ball.getxVec() - (fieldSize.getWidth() - ball.getxPos()))
                                    * (ball.getyVec() / ball.getyVec());
                            ball.setPos(ball.getxVec() - (fieldSize.getWidth() - ball.getxPos()),
                                    intersection);
                            ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                            setChanged();
                            notifyObservers(ball);
                            waitTime = 1;
                        }
                        // when the ball hits the upper wall in this step
                        if(newYPos + ball.getRadius() > fieldSize.getHeight()) {
                            intersection = ball.getxPos()
                                    + (fieldSize.getHeight() - ball.getyPos())
                                    * (ball.getxVec() / ball.getyVec());
                            ball.setVec(ball.getxVec(), -Math.abs(ball.getyVec()));
                            intersection = intersection
                                    + (ball.getyVec() - (fieldSize.getHeight() - ball.getyPos()))
                                    * (ball.getyVec() /ball.getxVec());
                            ball.setPos(intersection,
                                    ball.getyVec() - (fieldSize.getHeight() - ball.getyPos()));
                            ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                            setChanged();
                            notifyObservers(ball);
                            waitTime = 1;
                        }
                    } else {
                        // when there is no collision with a wall in this step
                        ball.setPos(newXPos, newYPos);
                        ball.setVec(tempXVec, tempYVec);
                        setChanged();
                        notifyObservers(ball);
                        waitTime = 1;
                    }
                }}
            }
        });
        /*
        while(doCalc) {
            // check, if the ball is out if range
            if(     ball.getxPos() - ball.getRadius() < 0
                    || ball.getyPos() - ball.getRadius() < 0
                    || ball.getxPos() + ball.getRadius() > fieldSize.getWidth()
                    || ball.getyPos() + ball.getRadius() > fieldSize.getHeight() ) {
                System.err.println("Ball on illegal Position");
                System.out.println(ball.toString());
                System.out.println(fieldSize.toString());
                doCalc = false;
                break;
            }
            //TODO: wenn Kugel sich nicht bewegt, warten(3-10s) und Kugel dann wieder "anstoßen"
            if(Math.hypot(ball.getxVec(), ball.getyVec()) < 0.0001) {
                ball.setMoving(false);
                // Wait 3-10 seconds to reset the balls speed vectors
                try {
                    //TODO: java timer
                    this.sleep(random.nextInt(7000) + 3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ball.setVec(random.nextDouble(), random.nextDouble());
                ball.setMoving(true);
                setChanged();
                notifyObservers(ball);
            }
            // simulating the next 10 steps to check, if the model has to be updated in every step
            double newXPos = ball.getxPos();
            double tempXVec = ball.getxVec();
            double newYPos = ball.getyPos();
            double tempYVec = ball.getyVec();
            double resVec = Math.hypot(tempXVec, tempYVec);
            double tempSum = tempXVec + tempYVec;
            for(int i=0;i<10;i++) {
                //TODO: err resVec - resVec + 0.0001 = 0.0001
                resVec = resVec - (Math.hypot(tempXVec, tempYVec) - 0.0001);
                newXPos = newXPos + tempXVec;
                tempXVec = tempXVec - resVec * (tempXVec / tempSum);
                newYPos = newYPos + tempYVec;
                tempYVec = tempYVec - resVec * (tempYVec / tempSum);
                tempSum = tempXVec + tempYVec;
            }
            if(     newXPos - ball.getRadius() < 0
                    || newYPos - ball.getRadius() < 0
                    || newXPos + ball.getRadius() > fieldSize.getWidth()
                    || newYPos + ball.getRadius() > fieldSize.getHeight() ) {
                //TODO: wenn nicht, dann neue Pos + neue Beschl berechnen und 10ZE warten
                ball.setPos(newXPos, newYPos);
                ball.setVec(tempXVec, tempYVec);
                setChanged();
                notifyObservers(ball);
                try {
                    //TODO: java timer
                    this.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                // when the ball would collide in the next ten steps, the program calculates every
                // step to the collision
                tempSum = ball.getxVec() + ball.getyVec();
                tempXVec = ball.getxVec() - 0.0001 * (tempXVec / tempSum);
                tempYVec = ball.getyVec() - 0.0001 * (tempYVec / tempSum);
                newXPos = ball.getxPos() + tempXVec;
                newYPos = ball.getyPos() + tempYVec;
                if(     newXPos - ball.getRadius() < 0
                        || newYPos - ball.getRadius() < 0
                        || newXPos + ball.getRadius() > fieldSize.getWidth()
                        || newYPos + ball.getRadius() > fieldSize.getHeight() ) {
                    // when there is a collision with a wall in this step
                    // collisiontreatment:
                    double intersection;
                    // when the ball hits the left wall in this step
                    if(newXPos - ball.getRadius() < 0) {
                        intersection = ball.getyPos()
                                + ball.getxPos()
                                * (ball.getyVec() / ball.getxVec());
                        ball.setVec(Math.abs(ball.getxVec()), ball.getyVec());
                        intersection = intersection
                                + (ball.getxVec() -  ball.getxPos())
                                * (ball.getyVec() / ball.getxVec());
                        ball.setPos(ball.getxVec() - ball.getxPos(), intersection);
                        ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                        setChanged();
                        notifyObservers(ball);
                    }
                    // when the ball hits the bottom wall in this step
                    if(newYPos - ball.getRadius() < 0) {
                        intersection = ball.getxPos()
                                + ball.getyPos()
                                * (ball.getxVec() / ball.getyVec());
                        ball.setVec(ball.getxVec(), Math.abs(ball.getyVec()));
                        //TODO: vll 3. zeile austauschen
                        intersection = intersection
                                + (ball.getyVec() - ball.getyPos())
                                * (ball.getyVec() / ball.getxVec());
                        ball.setPos(intersection, ball.getyVec() - ball.getyPos());
                        ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                        setChanged();
                        notifyObservers(ball);
                    }
                    // when the ball hits the right wall in this step
                    if(newXPos + ball.getRadius() > fieldSize.getWidth()) {
                        intersection = ball.getyPos()
                                + (fieldSize.getWidth() - ball.getxPos())
                                * (ball.getyVec() / ball.getxVec());
                        ball.setVec(-Math.abs(ball.getxVec()), ball.getyVec());
                        intersection = intersection
                                + (ball.getxVec() - (fieldSize.getWidth() - ball.getxPos()))
                                * (ball.getyVec() / ball.getyVec());
                        ball.setPos(ball.getxVec() - (fieldSize.getWidth() - ball.getxPos()),
                                intersection);
                        ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                        setChanged();
                        notifyObservers(ball);
                    }
                    // when the ball hits the upper wall in this step
                    if(newYPos + ball.getRadius() > fieldSize.getHeight()) {
                        intersection = ball.getxPos()
                                + (fieldSize.getHeight() - ball.getyPos())
                                * (ball.getxVec() / ball.getyVec());
                        ball.setVec(ball.getxVec(), -Math.abs(ball.getyVec()));
                        intersection = intersection
                                + (ball.getyVec() - (fieldSize.getHeight() - ball.getyPos()))
                                * (ball.getyVec() /ball.getxVec());
                        ball.setPos(intersection,
                                ball.getyVec() - (fieldSize.getHeight() - ball.getyPos()));
                        ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                        setChanged();
                        notifyObservers(ball);
                    }
                } else {
                    // when there is no collision with a wall in this step
                    ball.setPos(newXPos, newYPos);
                    ball.setVec(tempXVec, tempYVec);
                    setChanged();
                    notifyObservers(ball);
                    try {
                        //TODO: java timer
                        this.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        */
    }

    public boolean isDoCalc() {
        return doCalc;
    }

    public void setDoCalc(boolean doCalc) {
        this.doCalc = doCalc;
    }
}
