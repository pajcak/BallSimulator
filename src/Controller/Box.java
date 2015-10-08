package Controller;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Pajcak
 */
public class Box {
    int minX, minY, maxX, maxY;

    private Color borderCol;
    private Color backgroundCol;
    
    public Box(int x, int y, int width, int height, Color bgColor, Color borderColor) {
        this.minX = x;
        this.minY = y;
        this.maxX = x + width - 1;
        this.maxY = y + height - 1;
        this.backgroundCol = bgColor;
        this.borderCol = borderColor;
    }
    
    public void set (int x, int y, int width, int height) {
        minX = x;
        minY = y;
        maxX = x + width - 1;
        maxY = y + height - 1;
    }
    
    public void draw(Graphics g) {
        g.setColor(backgroundCol);
        g.fillRect(minX, minY, maxX - minX, maxY - minY);
        g.setColor(borderCol);
        g.drawRect(minX, minY, maxX - minX, maxY - minY);
    }
}
