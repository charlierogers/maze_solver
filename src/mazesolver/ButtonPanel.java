/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author charlie
 */
public class ButtonPanel extends JPanel {
    
    public JButton defaultButton;
    public JButton solveButton;
    public JButton resetButton;
    
    public ButtonPanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setSize(200, 600);
        setLocation(800, 0);
        setBackground(Color.white);
        setLayout(null);
        
        defaultButton = new JButton("Default Maze");
        defaultButton.setSize(125, 30);
        defaultButton.setLocation(835, 150);
        defaultButton.setVisible(true);
        add(defaultButton);
        
        solveButton = new JButton("Solve Maze");
        solveButton.setSize(125, 30);
        solveButton.setLocation(835, 250);
        solveButton.setVisible(true);
        add(solveButton);
        
        resetButton = new JButton("Reset");
        resetButton.setSize(125, 30);
        resetButton.setLocation(835, 350);
        resetButton.setVisible(true);
        add(resetButton);
        
        setVisible(true);
    }
}
