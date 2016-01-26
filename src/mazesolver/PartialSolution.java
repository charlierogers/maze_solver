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
public class PartialSolution {
    
    private Cell[] path;
    
    public static final int ACCEPT = 1;
    public static final int CONTINUE = 2;
    public static final int ABANDON = 3;
    
    /**
     * Constructs a partial solution of the given size
     * @param size the size
    */
    public PartialSolution(int size) {
        path = new Cell[size];
    }
    
    /**
     * Constructs a partial solution with a given first cell
     * @param cell the cell from which to start solving the maze
     */
    public PartialSolution(Cell cell) {
        path = new Cell[1];
        path[0] = cell;
    }
    
    /**
     * Examines a partial solution
     * @param grid the ArrayList of ArrayLists of Tiles with which to compare the partial solution
     * @param cellsVisited an ArrayList of cells that have already been visited
     * @return either ABANDON, CONTINUE, or ACCEPT
     */
    public int examine(ArrayList<ArrayList<Tile>> grid, ArrayList<Cell> cellsVisited) {
        //the current cell to examine is the last cell in the path for the current partial solution
        Cell currentCell = path[path.length-1];
        //immediately abandon if cell has already been visited
        for (Cell visitedCell : cellsVisited) {
            if (currentCell.getX() == visitedCell.getX() && currentCell.getY() == visitedCell.getY()) {
                return ABANDON;
            }
        }
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (currentCell.getX() == j && currentCell.getY() == i) {   
                    //has found a tile on the grid that the last cell in the path intersects
                    
                    if (grid.get(i).get(j).isWall()) {    //if cell intersects a wall
                        return ABANDON;
                    }
                    if (!grid.get(i).get(j).isWall() && i == grid.size()-1) {    //if cell intersects finish tile
                        return ACCEPT;
                    }
                }
            }
        }
        return CONTINUE;
    }
    
    public int examine2(ArrayList<ArrayList<Tile>> grid, ArrayList<Cell> cellsVisited) {
        //the current cell to examine is the last cell in the path for the current partial solution
        Cell currentCell = path[path.length-1];
        //if end of maze has been reached
        if (currentCell.isExit(grid)) {
            return ACCEPT;
        }
        //create array of all bordering cells to check
        Cell[] borderingCell;
        if (currentCell.getY() == 0) {     //we are extending partial solutions from start cell
            borderingCell = new Cell[3];
        } else {
            borderingCell = new Cell[4];
        }
        //fill array of bordering cells
        for (int i = 0; i < borderingCell.length; i++) {
            switch (i) {
                //add the cell to left
                case 0: borderingCell[i] = new Cell(currentCell.getX()-1, currentCell.getY()); break;
                //add the cell to the right
                case 1: borderingCell[i] = new Cell(currentCell.getX()+1, currentCell.getY()); break;
                //add the cell to the bottom
                case 2: borderingCell[i] = new Cell(currentCell.getX(), currentCell.getY()+1); break;
                //add the cell to the top (won't get reached if current cell is start cell
                case 3: borderingCell[i] = new Cell(currentCell.getX(), currentCell.getY()-1); break;
            }
        }
        //check to see if any of the bordering cells are clear (no walls or already visited cells)
        int cellsClear = 0;
        for (Cell cell : borderingCell) {
            if (cell.isClear(grid, cellsVisited, path)) {
                cellsClear++;
            }
        }
        if (cellsClear == 0) {
            return ABANDON;
        }
        return CONTINUE;
    }
    
    /**
     * Yields all extensions of this partial solutions (the cells to the right, left, up and down)
     * @return an array of partial solutions that extend this solution
     */
    public ArrayList<PartialSolution> extend(ArrayList<ArrayList<Tile>> grid, ArrayList<Cell> cellsVisited) {
        int size = path.length;
        int directions = 0;
        Cell lastCell = path[size-1];
        ArrayList<Cell> extensions = new ArrayList<>();
        ArrayList<PartialSolution> result = new ArrayList<>();
        if (lastCell.getY() == 0) {     //we are extending partial solutions from start cell
            directions = 3;  //only 3 diretions to check
        } else {
            directions = 4;   //all 4 directions to check
        }
        
        for (int i = 0; i < directions; i++) {
            Cell nextCell;
            //add the new cell to the end of the new path
            switch (i) {
                //add the cell to left
                case 0: 
                    nextCell = new Cell(lastCell.getX()-1, lastCell.getY());
                    if (nextCell.isClear(grid, cellsVisited, path)) {
                        extensions.add(nextCell);
                    }
                    break;
                //add the cell to the right
                case 1: 
                    nextCell = new Cell(lastCell.getX()+1, lastCell.getY());
                    if (nextCell.isClear(grid, cellsVisited, path)) {
                        extensions.add(nextCell);
                    }
                    break;
                //add the cell to the bottom
                case 2: 
                    nextCell = new Cell(lastCell.getX(), lastCell.getY()+1);
                    if (nextCell.isExit(grid)) {
                            extensions.add(nextCell);
                    } else if (nextCell.isClear(grid, cellsVisited, path)) {
                        extensions.add(nextCell);
                    }
                    break;
                //add the cell to the top (won't get reached if extending start cell
                case 3: 
                    nextCell = new Cell(lastCell.getX(), lastCell.getY()-1);
                    if (nextCell.isClear(grid, cellsVisited, path)) {
                        extensions.add(nextCell);
                    }
                    break;
            }
        }
        for (int i = 0; i < extensions.size(); i++) {
            //create a new partial solution for each extension
            result.add(new PartialSolution(size + 1));
            //copy cells from old path into new path
            for (int j = 0; j < path.length; j++) {
                result.get(i).path[j] = path[j];
            }
            //add extended cell to end of new path
            result.get(i).path[size] = extensions.get(i);
        }
        return result;
    }
    
    public ArrayList<PartialSolution> extend2(ArrayList<ArrayList<Tile>> grid, ArrayList<Cell> cellsVisited) {
        //extend a path in all possible diretions
        
        int size = path.length;
        int directions = 0;
        int extensionsAdded = 0;
        Cell lastCell = path[size-1];
        ArrayList<PartialSolution> result = new ArrayList<>();
        if (lastCell.getY() == 0) {     //we are extending partial solutions from start cell
            directions = 3;  //only 3 diretions to check
        } else {
            directions = 4;   //all 4 directions to check
        }
        
        for (int i = 0; i < directions; i++) {
            Cell currentCell = lastCell;
            Cell nextCell;
            ArrayList<Cell> extension = new ArrayList<>();
            //move to the next cell
            switch (i) {
                //extend down
                case 0: 
                    nextCell = new Cell(currentCell.getX(), currentCell.getY()+1);
                    while (nextCell.isClear(grid, path)) {
                        if (nextCell.isExit(grid)) {
                            extension.add(nextCell);
                            break;
                        }
                        //add the next cell to extension list
                        extension.add(nextCell);
                        currentCell = nextCell;
                        nextCell = new Cell(currentCell.getX(), currentCell.getY()+1);
                    }
                    break;
                //extend left
                case 1:
                    nextCell = new Cell(currentCell.getX()-1, currentCell.getY());
                    while (nextCell.isClear(grid, path)) {
                        //if in last row that isn't a wall
                        if (currentCell.getY() == grid.size()-2) {
                            //check to see if finish cell is passed
                            Cell possExitCell = new Cell(currentCell.getX(), currentCell.getY()+1);
                            //if cell directly below is finish cell
                            if (possExitCell.isExit(grid)) {
                                extension.add(possExitCell);
                                break;
                            }
                        }
                        //add the next cell to extension list
                        extension.add(nextCell);
                        currentCell = nextCell;
                        nextCell = new Cell(currentCell.getX()-1, currentCell.getY());
                    }
                    break;
                //extend right
                case 2:
                    nextCell = new Cell(currentCell.getX()+1, currentCell.getY());
                    while (nextCell.isClear(grid, path)) {
                        if (currentCell.getY() == grid.size()-2) {
                            //check to see if finish cell is passed
                            Cell possExitCell = new Cell(currentCell.getX(), currentCell.getY()+1);
                            //if cell directly below is finish cell
                            if (possExitCell.isExit(grid)) {
                                extension.add(possExitCell);
                                break;
                            }
                        }
                        //add the next cell to extension list
                        extension.add(nextCell);
                        currentCell = nextCell;
                        nextCell = new Cell(currentCell.getX()+1, currentCell.getY());
                    }
                    break;
                //extend up
                case 3:
                    nextCell = new Cell(currentCell.getX(), currentCell.getY()-1);
                    while (nextCell.isClear(grid, path)) {
                        //add the next cell to extension list
                        extension.add(nextCell);
                        currentCell = nextCell;
                        nextCell = new Cell(currentCell.getX(), currentCell.getY()-1);
                    }
                    break;
                default:
                    nextCell = currentCell;
                    extension.add(nextCell);
                    break;
            }
            //only create a new partial solution if there is a extension
            if (extension.size() > 0) {
                result.add(new PartialSolution(size + extension.size()));
                //add the new extension to the path 
                //copy current path into new path
                for (int j = 0; j < size; j++) {
                    result.get(extensionsAdded).path[j] = path[j];
                }
                //add new extension to the end of the new path
                for (int j = size; j < size + extension.size(); j++) {
                    result.get(extensionsAdded).path[j] = extension.get(j-size);
                }
                extensionsAdded++;
            }
        }
            
        return result;
    }
    
    public Cell[] getPath() {
        return path;
    }
    
    public ArrayList<Cell> getRefinedPath() {
        ArrayList<Cell> refinedPath = new ArrayList<>();
        for (int i = 0; i < path.length; i++) { //iterate through original path
            for (int j = 0; j < refinedPath.size(); j++) {  
                if (path[i].isAdjacentTo(refinedPath.get(j))) {  //check for adjacent cell matches in refined path
                    while (!refinedPath.get(refinedPath.size()-1).equals(refinedPath.get(j))) {
                        Cell cellToRemove = refinedPath.get(refinedPath.size()-1);
                        refinedPath.remove(cellToRemove); //remove unnecessary steps in between two adjacent cells
                    }
                }
            }
            refinedPath.add(path[i]);
        }
        return refinedPath;
    }
}   
