package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BallSimulator extends JPanel {

    private static final int UPDATE_RATE = 30;
    private static final float EPSILON_TIME = 1e-2f; // Threshold for zero time

    private boolean paused;

    private Ball ball;
    private Box box;

    private ControlPanel controlPanel;
    private Canvas canvas;
    private int canvasWidth;
    private int canvasHeight;

    public BallSimulator(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;

        paused = false;
        //init the ball location at random
        Random rand = new Random();
        int radius = 50;
        int x = rand.nextInt(canvasWidth - radius * 2 - 20) + radius + 10;
        int y = rand.nextInt(canvasHeight - radius * 2 - 20) + radius + 10;
        int speed = 5;
        int angleInDegree = rand.nextInt(360);
        ball = new Ball(x, y, radius, speed, angleInDegree, Color.BLUE);

        box = new Box(0, 0, canvasWidth, canvasHeight, Color.BLACK, Color.WHITE);

        canvas = new Canvas();
        controlPanel = new ControlPanel();

        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

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
                    long beginTimeMilis, timeTakenMilis, timeLeftMilis;

                    beginTimeMilis = System.currentTimeMillis();
                    if (!paused) {
                        simUpdate();
                        repaint();
                    }
                    timeTakenMilis = System.currentTimeMillis() - beginTimeMilis;

                    timeLeftMilis = 1000L / UPDATE_RATE - timeTakenMilis;

                    if (timeLeftMilis < 5) {
                        timeLeftMilis = 5; //set a minimum
                    }//                    
                    try {
//                        Thread.sleep(1000 / UPDATE_RATE);
                        Thread.sleep(timeLeftMilis);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BallSimulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        simulationThr.start();
    }

    public void simUpdate() {
//        ball.intersect(box, 1.0f);
//        ball.update(1.0f);
        float timeLeft = 1.0f; //one time-step to begin with

        // Repeat until the one time-step is up
        do {
            // Need to find the earliest collision time among all objects
            float earliestCollisionTime = timeLeft;

            // Special case here as there is only one moving ball.
            ball.intersect(box, timeLeft);
            if (ball.earliestCollision.collisionTime < earliestCollisionTime) {
                earliestCollisionTime = ball.earliestCollision.collisionTime;
            }

            // Update all the objects for earliestCollisionTime
            ball.update(earliestCollisionTime);

            if (earliestCollisionTime > 0.05) { //do not display small changes
                repaint();
                try {
                    Thread.sleep((long) (1000L / UPDATE_RATE * earliestCollisionTime));
                } catch (InterruptedException ex) {
                }
            }
            timeLeft -= earliestCollisionTime;
        } while (timeLeft > EPSILON_TIME);
    }

    class ControlPanel extends JPanel {

        public ControlPanel() {
            JCheckBox checkBox = new JCheckBox("Pause");
            this.add(checkBox);

            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    paused = !paused;
                }
            });

            int minSpeed = 2;
            int maxSpeed = 20;
            JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, minSpeed, maxSpeed, (int) ball.getSpeed());
            this.add(new JLabel("Speed"));
            this.add(speedSlider);
            speedSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider) e.getSource();
                    if (!source.getValueIsAdjusting()) {
                        int newSpeed = (int) source.getValue();
                        int currentSpeed = (int) ball.getSpeed();
                        ball.velX *= (float) newSpeed / currentSpeed;
                        ball.velY *= (float) newSpeed / currentSpeed;
                    }
                }
            });
            int minRadius = 10;
            int maxRadius = ((canvasWidth > canvasHeight) ? canvasHeight : canvasWidth) / 2 - 8;
            JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, minRadius, maxRadius, (int) ball.radius);
            this.add(new JLabel("Ball radius"));
            this.add(radiusSlider);
            radiusSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider) e.getSource();
                    if (!source.getValueIsAdjusting()) {
                        ball.radius = source.getValue();
                        //reposition the ball in order to still be inside the box
                        if (ball.x - ball.radius < box.minX) {
                            ball.x = ball.radius + 1;
                        } else if (ball.x + ball.radius > box.maxX) {
                            ball.x = box.maxX - ball.radius - 1;
                        }
                        if (ball.y - ball.radius < box.minY) {
                            ball.y = ball.radius + 1;
                        } else if (ball.y + ball.radius > box.maxY) {
                            ball.y = box.maxY - ball.radius - 1;
                        }
                    }
                }
            });
        }

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

    public static void frameMode() {
        JFrame frame = new JFrame("Bal simulator 1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BallSimulator bs = new BallSimulator(800, 500);
        bs.simulate();

        frame.setContentPane(bs);
        frame.pack();
        frame.setVisible(true);
    }

    public static void fullScreenMode() {
        JFrame fullScreen = new JFrame("Ball simulator 1.0");

        // Get the default graphic device and try full screen mode
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        if (device.isFullScreenSupported()) { // Go for full-screen mode
            fullScreen.setUndecorated(true);         // Don't show title and border
            fullScreen.setResizable(false);
            //this.setIgnoreRepaint(true);     // Ignore OS re-paint request
            device.setFullScreenWindow(fullScreen);
        } else {    // Run in windowed mode if full screen is not supported
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            fullScreen.setSize(dim.width, dim.height - 40); // minus task bar
            fullScreen.setResizable(true);
        }

        // Allocate the game panel to fill the current screen 
        BallSimulator bs = new BallSimulator(fullScreen.getWidth(), fullScreen.getHeight());
        bs.simulate();
        fullScreen.setContentPane(bs); // Set as content pane for this JFrame

        // To handle key events
        fullScreen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_ESCAPE:   // ESC to quit
                        System.exit(0);
                        break;
                }
            }
        });
        fullScreen.setFocusable(true);  // To receive key event

        fullScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fullScreen.setTitle("Ball simulator 1.0");
        fullScreen.pack();            // Pack to preferred size
        fullScreen.setVisible(true);  // Show it
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frameMode();
//                fullScreenMode();
            }
        });
    }

}
