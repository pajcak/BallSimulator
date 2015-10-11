package Controller;

import Collision.Collision;
import Collision.CollisionPhysics;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Formatter;

/**
 *
 * @author Pajcak
 */
public class Ball {

    float x, y;
    float velX, velY;
    float radius;
    Color fillCol;

    // Maintains the earliest collision detected 
    Collision earliestCollision = new Collision();

    // Working copy for computing response in intersect(ContainerBox box), to avoid repeatedly allocating objects.
    private Collision tempCollision = new Collision();

    public Ball(float x, float y, float radius, float speed, float angleInDegrees, Color fillCol) {
        this.x = x;
        this.y = y;
        this.velX = (float) (speed * Math.cos(Math.toRadians(angleInDegrees)));
        this.velY = (float) (-speed * Math.sin(Math.toRadians(angleInDegrees)));
        this.radius = radius;
        this.fillCol = fillCol;
    }

    public void draw(Graphics g) {
        g.setColor(fillCol);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public void intersect(Box box, float timeLimit) {
        CollisionPhysics.pointIntersectsRectangle(x, y, velX, velY, radius,
                box.minX, box.minY, box.maxX, box.maxY, timeLimit, tempCollision);
        if (tempCollision.collisionTime < earliestCollision.collisionTime) {
            earliestCollision.replace(tempCollision);
        }
    }

    public void update(float timeLimit) {
        if (earliestCollision.collisionTime <= timeLimit) { // collision detected
            //this ball collided, get new pos and speed
            this.x = earliestCollision.getNewCoord(x, velX);
            this.y = earliestCollision.getNewCoord(y, velY);
            this.velX = earliestCollision.newVelX;
            this.velY = earliestCollision.newVelY;
        } else {
            this.x += velX * timeLimit;
            this.y += velY * timeLimit;
        }
        earliestCollision.reset();
    }

    /**
     * Return the magnitude of speed.
     */
    public float getSpeed() {
        return (float) Math.sqrt(velX * velX + velY * velY);
    }

    /**
     * Return the direction of movement in degrees (counter-clockwise).
     */
    public float getMoveAngle() {
        return (float) Math.toDegrees(Math.atan2(-velY, velX));
    }

    /**
     * Return mass
     */
    public float getMass() {
        return radius * radius * radius / 1000f;  // Normalize by a factor
    }

    /**
     * Return the kinetic energy (0.5mv^2)
     */
    public float getKineticEnergy() {
        return 0.5f * getMass() * (velX * velX + velY * velY);
    }

    /**
     * Describe itself.
     */
    public String toString() {
        sb.delete(0, sb.length());
        formatter.format("@(%3.0f,%3.0f) r=%3.0f V=(%2.0f,%2.0f) "
                + "S=%4.1f angle=%4.0f KE=%3.0f",
                x, y, radius, velX, velY, getSpeed(), getMoveAngle(),
                getKineticEnergy());
        return sb.toString();
    }
    // Re-use to build the formatted string for toString()
    private StringBuilder sb = new StringBuilder();
    private Formatter formatter = new Formatter(sb);
}
