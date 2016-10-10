import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Random;

/**
 * The CalcThread class calculates the next step(s) the ball makes.
 * It is runnable and is run as a thread. It also extends Observable
 * so the View BallPanel can be refreshed.
 * Generally the CalcThread tries to avoid unnecessary view-updates
 * by skipping 10 steps when the ball is not going to hit a border
 * within these steps.
 */
public class CalcThread extends Observable implements Runnable  {

    private Ball ball;
    private Dimension fieldSize;
    private Timer time;

    /**
     * Creates a new CalcThread
     * @param ball a fix ball is given and the next steps can be calculated
     * @param fieldSize the range of the field the ball remains
     */
    public CalcThread(Ball ball, Dimension fieldSize){
        this.ball = ball;
        this.fieldSize = fieldSize;
    }

    /**
     * The run-method handles the calculation of the next steps the ball takes
     * First a Timer is created to wait the needed amount of seconds to the next step
     * if steps can be skipped.
     * It is checked, if the ball has an illegal position within the field.
     * If the balls resulting speed is lower then 0.0001, the ball is standing and
     * will be jolted again after 3-10 seconds.
     * If these two conditions are not valid, the method simulates the next ten steps
     * is there is no collision with a wall, the timer waits for the time of ten steps and
     * the ball is set to the new position and vectors. If the ball would collide with a wall
     * within the next ten steps, the method refreshed the ball step-wise every millisecond
     * for one step. Every step is checked, if this is the step in witch the ball
     * collides with a wall. When it does, a collision-treatment for every wall is calculated.
     * Every time the new balls position and vectors are refreshed, the view is notified
     * with the updated ball.
     */
    @Override
    public void run() {
        Random random = new Random();
        time = new Timer(10, new ActionListener() {
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
                    time.stop();
                } else if(Math.hypot(ball.getxVec(), ball.getyVec()) < 0.0001) {
                    // if the balls speed is so low, that it stops
                    ball.setMoving(false);
                    // wait 3-10 seconds to reset the balls speed vectors
                    time.setDelay(random.nextInt(7000) + 3000);
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
                double tempSum = tempXVec + tempYVec;
                for(int i=0;i<10;i++) {
                    newXPos = newXPos + tempXVec;
                    tempXVec = tempXVec - 0.0001 * (tempXVec / tempSum);
                    newYPos = newYPos + tempYVec;
                    tempYVec = tempYVec - 0.0001 * (tempYVec / tempSum);
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
                    time.setDelay(10);
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
                            time.setDelay(1);
                        }
                        // when the ball hits the bottom wall in this step
                        if(newYPos - ball.getRadius() < 0) {
                            intersection = ball.getxPos()
                                    + ball.getyPos()
                                    * (ball.getxVec() / ball.getyVec());
                            ball.setVec(ball.getxVec(), Math.abs(ball.getyVec()));
                            intersection = intersection
                                    + (ball.getyVec() - ball.getyPos())
                                    * (ball.getyVec() / ball.getxVec());
                            ball.setPos(intersection, ball.getyVec() - ball.getyPos());
                            ball.setVec(0.95 * ball.getxVec(), 0.95 * ball.getyVec());
                            setChanged();
                            notifyObservers(ball);
                            time.setDelay(1);
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
                            time.setDelay(1);
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
                            time.setDelay(1);
                        }
                    } else {
                        // when there is no collision with a wall in this step
                        ball.setPos(newXPos, newYPos);
                        ball.setVec(tempXVec, tempYVec);
                        setChanged();
                        notifyObservers(ball);
                        time.setDelay(1);
                    }
                }}
            }
        });
        time.start();
    }
}
