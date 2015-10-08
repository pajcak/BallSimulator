package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BallSimulator extends JPanel {

    private static final int UPDATE_RATE = 30;

    private Ball ball;
    private Box box;

    private Canvas canvas;
    private int canvasWidth;
    private int canvasHeight;

    public BallSimulator(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;

        //init the ball location at random
        Random rand = new Random();
        int radius = 50;
        int x = rand.nextInt(canvasWidth - radius * 2 - 20) + radius + 10;
        int y = rand.nextInt(canvasHeight - radius * 2 - 20) + radius + 10;
        int speed = 10;
        int angleInDegree = rand.nextInt(360);
        ball = new Ball(x, y, radius, speed, angleInDegree, Color.BLUE);

        box = new Box(0, 0, canvasWidth, canvasHeight, Color.BLACK, Color.WHITE);

        canvas = new Canvas();

        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);

        //handling window resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                Dimension dim = c.getSize();
                canvasWidth = dim.width;
                canvasHeight = dim.height;
                //adjust the bounds of the box to fill the window
                box.set(0, 0, canvasWidth, canvasHeight);
            }
        });
    }

    public void simulate() {
        Thread simulationThr = new Thread() {
            @Override
            public void run() {
                while (true) {
                        simUpdate();
                        repaint();
                    try {
                        Thread.sleep(1000/UPDATE_RATE);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BallSimulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        simulationThr.start();
    }
    
    public void simUpdate() {
        ball.moveOneStepWithCollisionDetection(box);
    }
    

    class Canvas extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            box.draw(g);
            ball.draw(g);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.PLAIN, 12));
            g.drawString("Ball " + ball.toString(), 20, 30);
        }

        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Bal simulator 1.0");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                BallSimulator bs = new BallSimulator(800, 500);
                
                frame.setContentPane(bs);
                frame.pack();
                frame.setVisible(true);
                bs.simulate();
            }
        });
    }

}
