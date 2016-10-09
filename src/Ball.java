import java.util.Random;
import java.util.Vector;

/**
 * The ball class holds the position on the ball and the speed represented by
 * two directional vectors. It also holds the information over the radius of the ball
 * and its state if the ball is moving.
 */
public class Ball {
    double xPos, yPos; // The position in the field
    double xVec, yVec; // The speed the ball has. In Pixel per Calculation Step
    double radius;
    boolean isMoving;

    public Ball(double xPos, double yPos, double radius) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.isMoving = false;
    }

    public Ball(double xPos, double yPos, double xVec, double yVec, double radius) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVec = xVec;
        this.yVec = yVec;
        this.radius = radius;
        this.isMoving = true;
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
        xVec = (ran.nextDouble() * 2) - 1;
        yVec = (ran.nextDouble() * 2) - 1;
        return new Ball(xPos, yPos, xVec, yVec, radius);
    }

    public void setPos(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setVec(double xVec, double yVec) {
        this.xVec = xVec;
        this.yVec = yVec;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getxVec() {
        return xVec;
    }

    public double getyVec() {
        return yVec;
    }

    public double getRadius() {
        return radius;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
