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
public class Yellow extends PieceColor{
    private int[][] yellowStart = {{13,4},{14,4},{13,5},{14,5}};
    private int[][] yellowHome = {{8,2},{9,2},{8,3},{9,3}};
    private int[][] yellowSafe = {{14,2},{13,2},{12,2},{11,2},{10,2}};
    private int[][] yellowFirst = {{15, 4}};
    private int[][] yellowLast = {{15, 2}};
    
    public Yellow(){
        
    }
    
    @Override
    public Color getColor(){
        return Color.rgb(153, 134, 0);
    }
    
    /**
     * @return the homeCoords
     */
    @Override
    public int[][] getHomeCoords() {
        return yellowHome;
    }

    /**
     * @return the safeCoords
     */
    @Override
    public int[][] getSafeCoords() {
        return yellowSafe;
    }

    /**
     * @return the startCoords
     */
    @Override
    public int[][] getStartCoords() {
        return yellowStart;
    }

    /**
     * @return the firstSpot
     */
    @Override
    public int[][] getFirstSpot() {
        return yellowFirst;
    }

    /**
     * @return the lastSpot
     */
    @Override
    public int[][] getLastSpot() {
        return yellowLast;
    }
    
    @Override
    public String toString(){
        return "Yellow";
    }
}
