/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;

/**
 *
 * @author charlie
 */
public class Maze extends JFrame {
    
    private GridPanel gridPanel;
    private ButtonPanel buttonPanel;
    private ArrayList<ArrayList<Tile>> grid;
    private boolean solved, drawing;
    private ArrayList<Cell> cellsVisited;
    
    public Maze() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Maze Solver");
        setSize(1000, 600);
        gridPanel = new GridPanel();
        buttonPanel = new ButtonPanel();
        add(gridPanel);
        add(buttonPanel);
        setVisible(true);
        solved = false;
        drawing = false;
        
        //set up ArrayList to store cells visited
        cellsVisited = new ArrayList<>();
        
        //set up button action listeners
        buttonPanel.resetButton.addActionListener(new resetButtonListener());
        buttonPanel.defaultButton.addActionListener(new defaultButtonListener());
        buttonPanel.solveButton.addActionListener(new solveButtonListener());
        
        //set up mouse listeners
        gridPanel.addMouseListener(new mouseListener());
        gridPanel.addMouseMotionListener(new mouseMotion());
        
        //set up maze grid
        grid = new ArrayList<>();
        resetGrid();
    }
    
    public static void main(String[] args) {
        Maze maze = new Maze();
    }
    
    public int getStartX() {
        for (int i = 0; i < grid.get(0).size(); i++) {
            if (!grid.get(0).get(i).isWall()) {
                return i;
            }
        }
        return 0;
    }
    
    public void drawGrid() {
        gridPanel.drawGrid(grid);
    }
    
    public void solve(PartialSolution sol) {
        //examine the current path of the partial solution
        int examineVal = sol.examine2(grid, cellsVisited);
        
        //see if the solver needs to continue or if it has found a solution
        if (examineVal == PartialSolution.ACCEPT) {
            solved = true;
            for (Cell c : sol.getRefinedPath()) {
                for (int i = 0; i < grid.size(); i++) {
                    for (int j = 0; j < grid.get(i).size(); j++) {
                        if (c.getX() == j && c.getY() == i) {   //if tile intersects the current tile being looped through
                            grid.get(i).get(j).setSolution(true);
                        }
                    }
                }
            }
            drawGrid();
        } else if (examineVal == PartialSolution.CONTINUE && !solved) {
            for (PartialSolution p : sol.extend(grid, cellsVisited)) {
                if (solved)
                    break;
                solve(p);
                for (Cell cell : p.getPath()) {
                    if (!cellsVisited.contains(cell)) {
                        cellsVisited.add(cell);
                    }
                }
            }
        }
    }
    
    private void resetGrid() {
        //maze hasn't been solved yet
        solved = false;
        //sets up a 30 x 30 grid of blank squares
        grid.clear();
        //loop for each row
        int rowYPos = 50;
        for (int i = 0; i < 30; i++) {
            //loop for each element in row
            int tileXPos = 100;
            ArrayList<Tile> tileList = new ArrayList<>();
            for (int j = 0; j < 30; j++) {
                Tile tile = new Tile(tileXPos, rowYPos);
                if (i == 0 || i == 29 || j == 0 || j == 29) {
                    tile.setEdge(true);
                }
                tileList.add(tile);
                tileXPos += Tile.SIZE;
            }
            grid.add(tileList);
            rowYPos += Tile.SIZE;
        }
        //clear out visited cells array
        cellsVisited.clear();
        gridPanel.drawGrid(grid);
    }
    
    private void setUpDefaultMaze() {
        
        //set up top wall
        for (int i = 0; i < 15; i++) {
            grid.get(0).get(i).setWall(true);
        }
        for (int i = 16; i < grid.get(0).size(); i++) {
            grid.get(0).get(i).setWall(true);
        }
        //set up bottom wall
        for (int i = 0; i < 5; i++) {
            grid.get(29).get(i).setWall(true);
        }
        for (int i = 6; i < grid.get(29).size(); i++) {
            grid.get(29).get(i).setWall(true);
        }
        //set up side walls
        for (ArrayList<Tile> row : grid) {
            row.get(0).setWall(true);
            row.get(29).setWall(true);
        }
        //set up a few inner walls
        for (int i = 0; i < 20; i++) {
            grid.get(8).get(i).setWall(true);
        }
        for (int j = 4; j < 15; j++) {
            grid.get(j).get(20).setWall(true);
        }
        for (int i = 10; i < 24; i++) {
            grid.get(15).get(i).setWall(true);
        }
        for (int j = 15; j < 20; j++) {
            grid.get(j).get(15).setWall(true);
        }
        for (int i = 29; i > 16; i--) {
            grid.get(19).get(i).setWall(true);
        }
        for (int i = 29; i > 7; i--) {
            grid.get(26).get(i).setWall(true);
        }
        for (int j = 26; j > 18; j--) {
            grid.get(j).get(12).setWall(true);
        }
        for (int i = 0; i < 10; i++) {
            grid.get(24).get(i).setWall(true);
        }
        gridPanel.drawGrid(grid);
    }
    
    private class resetButtonListener implements ActionListener {
        
        public resetButtonListener() {
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            resetGrid();
        }
    }
    
    private class defaultButtonListener implements ActionListener {
        
        public defaultButtonListener() {
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            setUpDefaultMaze();
        }
    }
    
    private class solveButtonListener implements ActionListener {
        
        public solveButtonListener() {
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            solve(new PartialSolution(new Cell(getStartX(), 0)));
        }
    }
    
    private class mouseListener extends MouseAdapter {
        
        public mouseListener() {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            drawing = true;
            for (int i = 0; i < grid.size(); i++) {
                for (int j = 0; j < grid.get(i).size(); j++) {  //iterate through all tiles in grid to find which one was clicked
                    Tile currentTile = grid.get(i).get(j);
                    if (currentTile.contains(e.getPoint())) {  //if the tile contains the cursor
                        //change the state of the tile color
                        if (currentTile.isWall()) {
                            currentTile.setWall(false);
                        } else {
                            currentTile.setWall(true);
                        }
                        //lock the tile so its color can't be changed until mouse is released
                        currentTile.lock();
                        gridPanel.repaint();
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            drawing = false;
            for (int i = 0; i < grid.size(); i++) {
                for (int j = 0; j < grid.get(i).size(); j++) {  //iterate through all tiles in grid
                    Tile currentTile = grid.get(i).get(j);
                    currentTile.unlock();
                }
            }
        }
    }
    
    private class mouseMotion extends MouseMotionAdapter {
        
        public mouseMotion() {
            
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            
            if (drawing) {
                for (int i = 0; i < grid.size(); i++) {
                    for (int j = 0; j < grid.get(i).size(); j++) {  //iterate through all tiles in grid to find which one was clicked
                        Tile currentTile = grid.get(i).get(j);
                        if (currentTile.contains(e.getPoint()) && !currentTile.isLocked()) {  //if the tile contains the cursor
                            //change the state of the tile color
                            if (currentTile.isWall()) {
                                currentTile.setWall(false);
                            } else {
                                currentTile.setWall(true);
                            }
                            //lock the tile so its color can't be changed until mouse is released
                            currentTile.lock();
                            gridPanel.repaint();
                        }
                    }
                }
            }
        }
    }
}
