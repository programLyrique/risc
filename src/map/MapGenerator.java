package map;

import core.Point;

/**
 * Generates a map.
 * 
 * Can generate parts of the map on the fly.
 * Useful because you can only calculate some parts of the map when they're not 
 * longer hidden by the war fog.
 * 
 * Méthode d'Eller pour la génération de labyrinthe ?
 * 
 *   pierre
 */
public interface MapGenerator {
    
    /**
     *  Generates the rectangle of corner (x, y) and size(w, h) if 
     * not generated before.
     * @param x
     * @param y
     * @param w
     * @param h 
     */
    public void generate(Map map, int x, int y, int w, int h);
    
    /**
     * 
     * @return location of the first kernel of player 1
     */
    public Point getLocationPlayer1();
    /**
     * 
     * @return location of the first kernel of player 2 
     */
    public Point getLocationPlayer2();

    
}
