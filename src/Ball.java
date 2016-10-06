import java.util.Vector;

/**
 * Created by dabian on 04.10.16.
 */
public class Ball {
    double xPos, yPos; // The balls position in the field
    double xVec, yVec;
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
