package Model;

import Controller.BallSimulator;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author faistaver
 */
public class Ball {

    private double x, y; // position
    private final double radius;
    private final double angle;
    private double speed;
    private final double acceleration;
    private boolean moving;
    private double velocityX;
    private double velocityY;

    private double time = 0.0;

    private List<Point2D.Double> path;

    public Ball() {
        radius = 20;
        x = 0;
        y = BallSimulator.HEIGHT - 2*radius;
        angle = 85;
        speed = 5;
        acceleration = 0;
        moving = false;

        path = new ArrayList();
        path.add(new Point2D.Double(x + radius, y + radius));
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        /* Enable anti-aliasing and pure stroke */
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        updatePosition();

        for (Point2D.Double d : path) {
            g2d.setColor(Color.red);
            Ellipse2D.Double shape = new Ellipse2D.Double(d.x, d.y, 2, 2);
            g2d.fill(shape);
        }

        /* Construct a shape and draw it */
        g2d.setColor(Color.black);
        Ellipse2D.Double shape = new Ellipse2D.Double(x, y, 2 * radius, 2 * radius);
        g2d.fill(shape);
    }

    private void updatePosition() {
//        velocityX = speed * Math.cos(Math.toRadians(angle));
//        velocityY = speed * Math.sin(Math.toRadians(angle));

        x += speed * Math.cos(Math.toRadians(angle)) * time;
        y -= speed * Math.sin(Math.toRadians(angle)) * time - 0.5 * BallSimulator.GRAVITY * time * time;
        path.add(new Point2D.Double(x + radius, y + radius));
        time += 1;
//        ----------------shit cake---------------------------------------------
//        if (!moving) {
//            moving = true;
//            double distance = 200;
//            double time = 15;
//            velocityY = time * BallSimulator.GRAVITY * 0.5;
//            velocityX = distance / time;
//
//        }
//        if (moving) {
//            x += velocityX;
//            y += velocityY;
//            path.add(new Point2D.Double(x + radius, y + radius));
//
//            velocityY += BallSimulator.GRAVITY;
//        }
//        ----------------Forward acceleration----------------------------------
//        speed += acceleration;
//        velocityX = speed * Math.cos(Math.toRadians(angle));
//        velocityY = speed * Math.sin(Math.toRadians(angle));
//        x += velocityX;
//        y -= velocityY;
//        path.add(new Point2D.Double(x + radius, y + radius));

//        --------------------------FALL----------------------------------------
//        velocityY -= BallSimulator.GRAVITY;
//        if (y + (2*radius) - velocityY < BallSimulator.HEIGHT ) {
//            y -= velocityY;
//        } else y = BallSimulator.HEIGHT - 2*radius;
    }
}
