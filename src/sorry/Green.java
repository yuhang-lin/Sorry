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
public class Green extends PieceColor{
    private int[][] greenStart = {{10,13},{10,14},{11,13},{11,14}};
    private int[][] greenHome = {{13,9},{13,8},{12,9},{12,8}};
    private int[][] greenSafe = {{13,14},{13,13},{13,12},{13,11},{13, 10}};
    private int[][] greenFirst = {{11, 15}};
    private int[][] greenLast = {{13, 15}};
    
    public Green(){
        
    }
    
    @Override
    public Color getColor(){
        return Color.GREEN;
    }
    
    /**
     * @return the homeCoords
     */
    @Override
    public int[][] getHomeCoords() {
        return greenHome;
    }

    /**
     * @return the safeCoords
     */
    @Override
    public int[][] getSafeCoords() {
        return greenSafe;
    }

    /**
     * @return the startCoords
     */
    @Override
    public int[][] getStartCoords() {
        return greenStart;
    }

    /**
     * @return the firstSpot
     */
    @Override
    public int[][] getFirstSpot() {
        return greenFirst;
    }

    /**
     * @return the lastSpot
     */
    @Override
    public int[][] getLastSpot() {
        return greenLast;
    }
    
    @Override
    public String toString(){
        return "Green";
    }
}
