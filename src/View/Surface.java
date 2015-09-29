package View;

import Controller.BallSimulator;
import Model.Ball;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author faistaver
 */
public class Surface extends JPanel {

    private final Ball ball = new Ball();

    
    public Surface() {
//        paintChildren(super.getGraphics());
        setPreferredSize(new Dimension(BallSimulator.WIDTH, BallSimulator.HEIGHT));
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    public void updateUI() {
        super.updateUI(); //To change body of generated methods, choose Tools | Templates.
//        repaint();
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ball.draw(g);
    }

}
