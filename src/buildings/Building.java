package buildings;

import core.*;
import map.Map;
import map.Move;
import map.Tile;
import org.newdawn.slick.*;
import units.Unit;

/**
 *     classe rassemblant l'ensemble des batiments
 */
public class Building extends UandB {
    
    private Point exit;
    private Point ralliement;
    protected NewUnit newU;
    
    //Les tiles formant l'image du bâtiment
    //Potentiellement différent du logo.
    private Tile[][] tiles;

    /**
     * 
     * @param s1
     * @param s2
     * @param s3
     * @param h
     * @param w
     * @param p
     * @param _hp
     * @param _armor
     * @param _cons_time
     * @param _energy_cost
     * @param _warm_cost
     * @param _owner
     * @param _range
     * @throws SlickException 
     */
    public Building(Sound s1, Sound s2, Sound s3, Image im, Tile[][] tiles, int h, int w, Point p, int _hp, 
            int _armor, int _cons_time, int _energy_cost, int _warm_cost, Player _owner, int _range, Map map)
            throws SlickException {
        super(s1, s2, s3, _range, im, h, w, _owner.getColor(), p, _hp, _armor, _cons_time, _energy_cost, _warm_cost, _owner);
        Point p1 = (Point) p.clone();//Très important, ce clone !!
        p1.translate(new Point(w/2,-1));
        exit = p1;
        ralliement = (Point)p1.clone(); 
        this.tiles = tiles;
        newU = new NewUnit(this, map, false);
    }

 
    
    public void update(int delta) {
       newU.update(delta);
    }

    public Point getExit() {
        return exit;
    }

    public Point getRalliement() {
        return ralliement;
    }

    public void setRalliement(Point ralliement) {
        this.ralliement = ralliement;
    }
    
    /**
     * Renders the building on the screen.
     * Do not use this method. It does not behave well if you move the viewport, for instance.
     * @param gc
     * @param gr
     * @param x
     * @param y 
     */
    @Override
    public void render(GameContainer gc, Graphics gr, int x, int y) 
    {
        //System.err.println("Rendu du bâtiment.");
        int tileL = Map.getTileLenght();
        for(int i =0; i < getWidth(); i++)
        {
            for(int j = 0; j < getHeight() ; j++)
            {
                tiles[i][j].getImage().drawEmbedded(x + i*tileL, y + j * tileL);
            }
        }
    }
    
    /**
     * 
     * @param i
     * @param j
     * @return sub image Error if not in the bound
     */
    public Tile getSubTile(int i, int j)
    {
        return tiles[i][j];
    }
    
}
