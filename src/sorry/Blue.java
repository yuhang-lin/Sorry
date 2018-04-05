/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorry;

import javafx.scene.paint.Color;

/**
 *
 * @author austinbatistoni
 */
public class Blue extends PieceColor{
    
    private int[][] blueStart = {{4,1},{4,2},{5,1},{5,2}};
    private int[][] blueSafe = {{2,1},{2,2},{2,3},{2,4},{2,5}};
    private int[][] blueHome = {{2,6},{2,7},{3,6},{3,7}};
    private int[][] blueFirst = {{4,0}};
    private int[][] blueLast = {{2,0}};
    
    Blue(){
        
    }
    
    @Override
    public Color getColor(){
        return Color.DARKBLUE;
    }
    
    /**
     * @return the homeCoords
     */
    @Override
    public int[][] getHomeCoords() {
        return blueHome;
    }

    /**
     * @return the safeCoords
     */
    @Override
    public int[][] getSafeCoords() {
        return blueSafe;
    }

    /**
     * @return the startCoords
     */
    @Override
    public int[][] getStartCoords() {
        return blueStart;
    }

    /**
     * @return the firstSpot
     */
    @Override
    public int[][] getFirstSpot() {
        return blueFirst;
    }

    /**
     * @return the lastSpot
     */
    @Override
    public int[][] getLastSpot() {
        return blueLast;
    }
    
    @Override
    public String toString(){
        return "Blue";
    }
    
}
