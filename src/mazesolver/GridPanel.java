/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
 *
 * @author charlie
 */
public class GridPanel extends JPanel {
    
    private ArrayList<ArrayList<Tile>> tempArray;
    
    public GridPanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setSize(800, 600);
        setLocation(0,0);
        setBackground(Color.white);
        setLayout(null);
        setVisible(true);
        
        tempArray = new ArrayList<>();
    }
    
    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setBackground(Color.white);
        g.setColor(Color.black);
        
        super.paint(g);
        
        
        for (ArrayList<Tile> row : tempArray) {
            for (Tile tile : row) {
                g.setColor(Color.black);
                g.draw(tile);
                //fill black if tile is a wall
                if (tile.isWall()) {
                    g.setColor(Color.black);
                    g.fill(tile);
                } else if (tile.isSolution()) {
                    g.setColor(Color.red);
                    g.fill(tile);
                    g.setColor(Color.black);
                    g.draw(tile);
                }
                g.setColor(Color.black);
            }
        }
    }
    
    public void drawGrid(ArrayList<ArrayList<Tile>> grid) {
        tempArray = grid;
        grabFocus();
        repaint();
    }
}
