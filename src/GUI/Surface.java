package GUI;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author faistaver
 */
public class Surface extends JPanel {

    public Surface() {
        paintChildren(super.getGraphics());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
