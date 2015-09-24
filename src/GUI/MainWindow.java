package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author faistaver
 */
public class MainWindow extends JFrame {

    private final JLabel title = new JLabel("Ball simulator");
    private final Surface pane = new Surface();
    
    
    public MainWindow() {
        super("Ball 1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));
        setResizable(false);
        
        add(pane);
        
        pack();
        setVisible(true);
    }

}
