/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import java.util.ArrayList;

/**
 *
 * @author charlie
 */
public class Cell {
    
    private int xPos, yPos;
    
    /**
     * Creates a cell in a grid 
     * @param xPos the x coordinate of the cell
     * @param yPos the y coordinate of the cell
     */
    public Cell(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    public int getX() {
        return xPos;
    }
    
    public int getY() {
        return yPos;
    }
    
    public void setX(int x) {
        xPos = x;
    }
    
    public void setY(int y) {
        yPos = y;
    }
    
    /**
     * Checks whether a cell has already been visited or lies on a wall of the maze
     * @param grid the grid of walls and pathways against which to check
     * @param cellsVisited an ArrayList of cells that have already been visited
     * @return true if the cell hasn't been visited already and doesn't lie on a wall and 
     * false if the cell has already been visited or lies on a wall
     */
    public boolean isClear(ArrayList<ArrayList<Tile>> grid, ArrayList<Cell> cellsVisited, Cell[] path) {
        //check against all cells in path
        for (Cell visitedCell : cellsVisited) {
            if (getX() == visitedCell.getX() && getY() == visitedCell.getY()) {
                return false;
            }
        }
        if (path.length > 1) {
            //examine all cells in path except last cell
            Cell[] path2 = new Cell[path.length-1];
            for (int i = 0; i < path2.length; i++) {
                path2[i] = path[i];
            }
            //check against all elements in path
            for (Cell cell : path2) {
                if (getX() == cell.getX() && getY() == cell.getY()) {
                    return false;
                }
            }
        }
        //check against all tiles in grid
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (getX() == j && getY() == i) {   
                    //has found a tile on the grid that the last cell in the path intersects
                    
                    if (grid.get(i).get(j).isWall()) {    //if cell intersects a wall
                        return false;
                    }
                }
            }
        }
        //otherwise return true since cell doesn't intersect wall or previously visited cell
        return true;
    }
    
    public boolean isClear(ArrayList<ArrayList<Tile>> grid, Cell[] path) {
        //check against all tiles in grid
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (getX() == j && getY() == i) {   
                    //has found a tile on the grid that the last cell in the path intersects
                    
                    if (grid.get(i).get(j).isWall()) {    //if cell intersects a wall
                        return false;
                    }
                }
            }
        }
        //check against all cells in path
        if (path.length > 1) {
            //examine all cells in path except last cell
            Cell[] path2 = new Cell[path.length-1];
            for (int i = 0; i < path2.length; i++) {
                path2[i] = path[i];
            }
            //check against all elements in path
            for (Cell cell : path2) {
                if (getX() == cell.getX() && getY() == cell.getY()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isExit(ArrayList<ArrayList<Tile>> grid) {
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (getX() == j && getY() == i) {   //if cell intersects the current tile being looped through
                    if (!grid.get(i).get(j).isWall() && i == grid.size()-1) {    //if cell intersects finish tile
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isAdjacentTo(Cell cell) {
        if (cell.getX() == getX()) {
            if (Math.abs(cell.getY() - getY()) == 1) 
                return true;
        }
        if (cell.getY() == getY()) {
            if (Math.abs(cell.getX() - getX()) == 1)
                return true;
        }
        return false;
    }
}
