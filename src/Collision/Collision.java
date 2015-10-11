package Collision;

/**
 *
 * @author Pajcak
 */
public class Collision {

    public float collisionTime;
    private static final float EPSILON = 0.005f;

    public float newVelX;
    public float newVelY;

    public Collision() {
        reset();
    }

    public void reset() {
        collisionTime = Float.MAX_VALUE;
    }
    
    public void replace(Collision c) {
        this.collisionTime = c.collisionTime;
        this.newVelX = c.newVelX;
        this.newVelY = c.newVelY;
    }
    
    public float getNewCoord(float actualPosCoord, float actualSpeed) {
        if (collisionTime > EPSILON) { // still some time from collision == still can compute next position forward
            return (actualPosCoord + actualSpeed * (collisionTime - EPSILON));
        } else { // Time to collision is lower than EPS == collision occurs almost immediately == no need to move forward
            return actualPosCoord;
        }
    }
    
    
}
