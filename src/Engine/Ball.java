
package Engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author faistaver
 */
public class Ball {
    private double x, y; // position
    private final double radius;

    public Ball() {
        radius = 40;
        x = 0;
        y = 0;
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.black);
//        g2d.fillOval(x, y, 2*radius, 2*radius);
    }
}
