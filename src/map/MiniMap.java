package map;

import buildings.Building;
import org.newdawn.slick.*;
import units.Unit;
import util.PixelData;

/**
 * The minimap.
 *
 * @todo Case when there is several tiles for only one pixel.
 *   pierre
 */
final public class MiniMap
{

    private int w;
    private int h;
    private float stepX;
    private float stepY;
    private int miniTileLengthX;
    private int miniTileLengthY;
    private Map map;
    private PixelData pixels;
    private Image minimap;
    private Image background;
    //To display the paths of the units
    private boolean displayPath;

    /**
     * @param _map
     * @param width
     * @param height
     * @throws SlickException
     */
    public MiniMap(Map _map, int width, int height, Image background, int w_tile, int h_tile) throws SlickException
    {
        w = width;
        h = height;
        map = _map;

        displayPath = false;

        //Première création de la minimap.

        pixels = new PixelData(width, height);

        minimap = new Image(width, height);

        this.background = background;

        //Taille 
        stepX = (float) map.getWidth() / (float) width;
        stepY = (float) map.getHeight() / (float) height;

        //La taille voulue d'une tile sur la minimap.
        miniTileLengthX = Math.max(w_tile, width / map.getWidth() + 1);
        miniTileLengthY = Math.max(h_tile, height / map.getHeight());

    }

    public MiniMap(Map _map, int width, int height, Image background, int s_tile) throws SlickException
    {
        this(_map, width, height, background, s_tile, s_tile);
    }

    public MiniMap(Map _map, int width, int height, Image background) throws SlickException
    {
        this(_map, width, height, background, 2);
    }

    public void setDisplayPath(boolean mode)
    {
        displayPath = mode;
    }

    public boolean isDisplayedPath()
    {
        return displayPath;
    }

    /**
     * Updates the minimap.
     *
     * It's better to update only 1/10 or 1/100 updates of the game that
     * minimap.
     *
     * @param cont
     * @param delta
     */
    public void update(GameContainer cont, int delta)
    {
    }

    /**
     * Renders the minimap
     *
     * @param cont
     * @param g
     */
    public void render(GameContainer cont, Graphics g, int posMapX, int posMapY, int x, int y)
    {
        if (!displayPath)
        {
            pixels.clear();
        }
        //@todo afficher les bâtiments et unités.
        for (int i = 0; i < map.getWidth(); i++)
        {
            for (int j = 0; j < map.getHeight(); j++)
            {

                if (!map.isBlackout(i, j))
                {
                    //Dessin des unités
                    Unit unit = map.units[i][j];
                    if (unit != null && !map.isWarFog(i, j))
                    {
                        Color pix = unit.getOwner().getColor();

                        //Dessiner un carré 
                        for (int x1 = 0; x1 < miniTileLengthX && (int) (i / stepX) + x1 < w; x1++)
                        {
                            for (int y1 = 0; y1 < miniTileLengthY && (int) (j / stepY) + y1 < h; y1++)
                            {
                                pixels.setPixel((int) (i / stepX) + x1, (int) (j / stepY) + y1, pix);
                            }
                        }
                    }
                    //Dessin des bâtiments
                    Building building = map.getBuildingAt(i, j);
                    if (building != null)
                    {
                        Color pix = building.getOwner().getColor();
                        //Dessiner un carré  ; pour bâtiments, deux fois plus grand que les unités
                        for (int x1 = 0; x1 < miniTileLengthX && (int) (i / stepX) + x1 < w; x1++)
                        {
                            for (int y1 = 0; y1 < miniTileLengthY && (int) (j / stepY) + y1 < h; y1++)
                            {
                                pixels.setPixel((int) (i / stepX) + x1, (int) (j / stepY) + y1, pix);
                            }
                        }

                    }

                }

            }
        }

        pixels.apply(minimap.getTexture());


        background.draw(x, y, w, h);
        minimap.draw(x, y);
        g.setColor(Color.blue);
        //rendu du rectangle représentant l'écran 
        g.setLineWidth(1);
        g.drawRect(x + posMapX / stepX, y + posMapY / stepY,
                cont.getWidth() / (float) Map.getTileLenght() / stepX,
                (cont.getHeight() - h) / (float) Map.getTileLenght() / stepY);
        //rectangle pour délimiter la map
        g.setColor(Color.yellow);
        g.drawRect(x, y, getWidth() - 1, getHeight() - 1);
    }

    public int getWidth()
    {
        return w;
    }

    public int getHeight()
    {
        return h;
    }

    public float getMiniX()
    {
        return stepX;
    }

    public float getMiniY()
    {
        return stepY;
    }
}
