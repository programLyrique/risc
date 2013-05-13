package map;
import org.newdawn.slick.*;
import java.util.*;

/**
 * A tile is just a square with an associated image,
 * and a weight.
 * 
 *   pierre
 */
public class Tile {
    private ArrayList<Image> layers;
    private int w;
    
    Color mColor;

    /**
     * 
     * @param img image of the tile
     * @param mainColor the color to draw on the minimap for that tile
     * @param weight easiness to walk on that tile. O means impossible, and
     * 100, possible.
     */
    public Tile(Image img, Color mainColor, int weight)
    {
        layers = new ArrayList<>(1);
        layers.add(img);
        w = weight;
        mColor = mainColor;
    }
    
    /**
     * To add an image layer
     * @param im 
     */
    public void addLayer(Image im)
    {
        layers.add(im);
    }
    
    /**
     * To remove a layer. Only works if there is a least two layers.
     * @param i 
     */
    public void removeLayer(int i)
    {
        if(layers.size() > 1)
        {
            layers.remove(i);
        }
    }
    
    public int nbLayers()
    {
        return layers.size();
    }
    
    public Image getImage() 
    {
        return layers.get(0);
    }
    
    public Image getImage(int i)
    {
        return layers.get(i);
    }

    public int getWeight() 
    {
        return w;
    }
    
    public Color getColor()
    {
        return mColor;
    }
    
    public void render(GameContainer cont, Graphics g, int x, int y)
    {
        for(Image im : layers)
        {
            im.drawEmbedded(x, y);
        }
    }
    
}
