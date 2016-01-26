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
public class MainPanel extends JPanel {
    
    public MainPanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setSize(1000, 600);
        add(new GridPanel());
        add(new ButtonPanel());
        setVisible(true);
    }
}
