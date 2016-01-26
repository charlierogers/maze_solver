/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import java.awt.*;

/**
 *
 * @author charlie
 */
public class Tile extends Rectangle {
    
    private boolean isWall, isEdge, isSolution, isLocked;
    public final static int SIZE = 15; //side length in pixels
    
    public Tile(int x, int y) {
        isWall = false;
        isEdge = false;
        isSolution = false;
        isLocked = false;
        setSize(SIZE, SIZE);
        setLocation(x, y);
    }
    
    public void setWall (boolean state) {
        isWall = state;
    }
    
    public boolean isWall() {
        return isWall;
    }
    
    public void setEdge(boolean state) {
        isEdge = state;
    }
    
    public boolean isEdge() {
        return isEdge;
    }
    
    public void setSolution(boolean state) {
        isSolution = state;
    }
    
    public boolean isSolution() {
        return isSolution;
    }
    
    public void lock() {
        isLocked = true;
    }
    
    public void unlock() {
        isLocked = false;
    }
    
    public boolean isLocked() {
        return isLocked;
    }
}
