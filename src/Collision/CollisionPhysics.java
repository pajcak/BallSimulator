package Collision;

/**
 *
 * @author Pajcak
 */
public class CollisionPhysics {

    private static Collision detectedCollision = new Collision();

    public static void pointIntersectsRectangle(
            float pointX, float pointY, float speedX, float speedY, float radius,
            float rectX1, float rectY1, float rectX2, float rectY2,
            float timeLimit, Collision response) {

        response.reset();

        //Left
        pointIntersectsVerticalLine(pointX, pointY, speedX, speedY, radius, rectX1, timeLimit, detectedCollision);
        if (detectedCollision.collisionTime < response.collisionTime) {
            response.replace(detectedCollision);
        }
        //Right
        pointIntersectsVerticalLine(pointX, pointY, speedX, speedY, radius, rectX2, timeLimit, detectedCollision);
        if (detectedCollision.collisionTime < response.collisionTime) {
            response.replace(detectedCollision);
        }
        //Top
        pointIntersectsHorizontalLine(pointX, pointY, speedX, speedY, radius, rectY1, timeLimit, detectedCollision);
        if (detectedCollision.collisionTime < response.collisionTime) {
            response.replace(detectedCollision);
        }
        //Bottom
        pointIntersectsHorizontalLine(pointX, pointY, speedX, speedY, radius, rectY2, timeLimit, detectedCollision);
        if (detectedCollision.collisionTime < response.collisionTime) {
            response.replace(detectedCollision);
        }
    }

    public static void pointIntersectsVerticalLine(
            float pointX, float pointY, float speedX, float speedY, float radius,
            float lineX, float timeLimit, Collision response) {

        response.reset();

        if (speedX == 0) {
            return;
        }
        float distance;
        if (lineX > pointX) {
            distance = lineX - pointX - radius;
        } else {
            distance = lineX - pointX + radius;
        }
        
        float time = distance / speedX;

        if (time > 0 && time < timeLimit) {
            response.collisionTime = time;
            response.newVelX = -speedX;
            response.newVelY = speedY;
        }
    }

    public static void pointIntersectsHorizontalLine(
            float pointX, float pointY, float speedX, float speedY, float radius,
            float lineY, float timeLimit, Collision response) {

        response.reset();

        if (speedY == 0) {
            return;
        }
        float distance;
        if (lineY > pointY) {
            distance = lineY - pointY - radius;
        } else {
            distance = lineY - pointY + radius;
        }
        
        float time = distance / speedY;

        if (time > 0 && time < timeLimit) {
            response.collisionTime = time;
            response.newVelX = speedX;
            response.newVelY = -speedY;
        }
    }
}
