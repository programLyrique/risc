package map;


import core.Point;
import java.util.*;
import map.Map;
import org.newdawn.slick.*;
/**
 * A basic random map for testing purposes.
 *   pierre
 */
final public class BasicMap implements MapGenerator {
    
    private Tile iron;
    private Tile blackout;
    
    private Random random;
    
    /**
     * 
     * @param i
     * @param j coordinates of the image in the spritesheet
     * @throws SlickException 
     */
    public BasicMap(Image img1, Image img2, long seed) throws SlickException
    {
        iron = new Tile(img1, Color.red, 0);
        blackout = new Blackout(img2);
        random = new Random(seed);
    }
    
    @Override
    synchronized public void generate(Map map, int x, int y, int w, int h) 
    {
        for(int i = 0 ; i < w ; i++)
        {
            for(int j = 0; j < h; j++)
            {
                if(random.nextInt(10) == 0)
                {
                    map.background[x + i][y + j] = iron;
                    map.unsetBlackout(x + i, y + j);
                    map.block(x+i, y+j);
                }
                else
                {
                    //map.map[x + i][y + j] = blackout;
                    map.unblock(x+i, y+j);
                }
            }
        }
        
    }
    
    /**
     * Test du générateur aléatoire et de la carte.
     * @param args 
     */
    public static void main(String[] args)
    {
        Random gen = new Random(42L);
        for(int i = 0; i < 100 ; i++)
        {
            System.out.println("Random : " + gen.nextInt(10));
        }
    }

    @Override
    public Point getLocationPlayer1() {
        return new Point(0,0);
    }

    @Override
    public Point getLocationPlayer2() {
        return new Point(0,0);
    }
    
}
