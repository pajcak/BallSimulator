package View;

import Controller.BallSimulator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author faistaver
 */
public class MainWindow extends JFrame {
    
    private final JLabel title = new JLabel("Ball simulator");
    private final Surface surface = new Surface();
    
    
    public MainWindow() {
        super("Ball 1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        getContentPane().add(surface, BorderLayout.NORTH);
        JPanel p = new JPanel();
        p.setBackground(Color.GREEN);
        getContentPane().add(p, BorderLayout.CENTER);
        
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void updateUI() {
        surface.updateUI();
    }

    
}
