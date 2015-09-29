package Controller;

import View.MainWindow;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pajcak
 */
public class BallSimulator {

    private static final MainWindow window = new MainWindow();
    public static final int WIDTH  = 1000;
    public static final int HEIGHT = 600;
    public static final double GRAVITY = 0.5;

    public void simulate() {
        while (true) {
            window.updateUI();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
