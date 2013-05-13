package map;

import buildings.*;
import core.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import org.newdawn.slick.*;
import units.*;

/**
 * Map of the game.
 *
 * Rectangular map of tiles, procedurally generated. Give it a MapGenerator.
 * Stores location of walls, strategical points, buildings, and units.
 *
 *   pierre
 */
final public class Map
{

    private MapGenerator gen;
    private int w;
    private int h;
    private static int tileL = 0;
    private BitSet blocked;
    private BitSet warfog;//Display of buildings and ground, but not of units.
    private BitSet blackout;//Parts of the map not explored by the player.
    Tile background[][];   //Background of the map : floor and walls
    Tile ornaments[][];
    Tile buildings[][];//Peut contenir null
    Building buildingMask[][];//Chaque tile sous le bâtiment contient un pointeur vers le bâtiment
    Unit units[][];// Peut contenir null = pas d'unités à cet endroit.
    private HashSet<Unit> selectedUnits;
    private HashSet<Building> selectedBuildings;

    /**
     * Stores information about the map.
     *
     * @param width (number of tiles in width)
     * @param height (number of tiles in height)
     * @param g map generator How to build the map.
     */
    public Map(int width, int height, int tileLength, MapGenerator g)
    {
        w = width;
        h = height;
        tileL = tileLength;
        gen = g;
        blocked = new BitSet(width * height);
        blocked.set(0, w * h);
        warfog = new BitSet(width * height);
        blackout = new BitSet(width * height);
        blackout.set(0, w * h);

        // Will be progressively filled by MapGenerator
        background = new Tile[width][height];

        ornaments = new Tile[width][height];


        //to store units
        units = new Unit[width][height];

        //To store buildings

        buildings = new Tile[width][height];
        buildingMask = new Building[width][height];

        selectedUnits = new HashSet<>();
        selectedBuildings = new HashSet<>();

        //We generate all the map. @todo : dynamic generation
        g.generate(this, 0, 0, w, h);
    }

    /**
     * Whether a tile is blocked or not.
     *
     * @param x
     * @param y
     * @return a boolean
     */
    public boolean isBlocked(int x, int y)
    {
        return !((x < 0 || x >= w || y < 0 || y >= h)) && blocked.get(w * y + x);
    }

    /**
     * Blocks a tile.
     *
     * @param x
     * @param y
     */
    void block(int x, int y)
    {
        blocked.set(w * y + x);
    }

    /**
     * Unblocks a tile.
     *
     * @param x
     * @param y
     */
    void unblock(int x, int y)
    {
        blocked.set(w * y + x, false);
    }

    /**
     * Detects whether the tile is in the war fog.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isWarFog(int x, int y)
    {
        return warfog.get(w * y + x);
    }

    /**
     * Set the war fog.
     *
     * @param x
     * @param y
     */
    public void setWarFog(int x, int y)
    {
        warfog.set(w * y + x);
    }

    /**
     * Clears the war fog.
     *
     * @param x
     * @param y
     */
    public void unsetWarFog(int x, int y)
    {
        warfog.set(w * y + x, false);
    }

    /**
     * Detects whether the tile is in the blackout.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isBlackout(int x, int y)
    {
        return blackout.get(w * y + x);
    }

    /**
     * Set the blackout.
     *
     * @param x
     * @param y
     */
    public void setBlackout(int x, int y)
    {
        blackout.set(w * y + x);
    }

    /**
     * Clears the blackout.
     *
     * @param x
     * @param y
     */
    public void unsetBlackout(int x, int y)
    {
        blackout.set(w * y + x, false);
    }

    /**
     * Updates the map. Updates for instance the war fog the selected units...
     *
     * @param cont
     * @param delta
     */
    public void update(GameContainer cont, int delta, Player player)
    {
        //Stocker directement les unités sélectionnées sous cette forme ?
        selectedUnits = new HashSet(player.getSelectedUnits());
        selectedBuildings = new HashSet<>(player.getSelectedBuildings());
    }

    /**
     * Renders the part of the map (x, y, wi, he) on the screen. Call startUse
     * and endUse between it for the main spritesheet.
     *
     * @param x
     * @param y
     * @param wi
     * @param he
     */
    public void render(GameContainer cont, Graphics g, int x, int y, int wi, int he)
    {
        for (int i = x; i < Math.min(x + wi + 1, w); i++)
        {
            for (int j = y; j < Math.min(y + he, h); j++)
            {
                if (!isBlackout(i, j))//Ajouter gestion war fog
                {
                    //Arrière plan et murs
                    background[i][j].render(cont, g, (i - x) * tileL, (j - y) * tileL);
                    //Second calque pour les décors
                    if (ornaments[i][j] != null)
                    {
                        ornaments[i][j].render(cont, g, (i - x) * tileL, (j - y) * tileL);
                    }


                    //Affichage des bâtiments
                    if (buildings[i][j] != null)
                    {
                        buildings[i][j].getImage().drawEmbedded((i - x) * tileL, (j - y) * tileL);
                    }
                }

            }
        }
    }

    /**
     * Renders the unit on the map
     *
     * @param cont
     * @param g
     * @param x
     * @param y
     * @param wi
     * @param he
     * @param lissage
     */
    public void renderUnits(GameContainer cont, Graphics g, int x, int y, int wi, int he)
    {
        for (int i = x; i < Math.min(x + wi + 1, w); i++)
        {
            for (int j = y; j < Math.min(y + he, h); j++)
            {
                // Affichage des unités.
                if (!isWarFog(i, j))
                {
                    Unit unit = units[i][j];
                    if (unit != null)
                    {

                        /* Lissage de trajectoire : 
                         * l'unité vient de quatre cases adjacentes possibles.
                         * On calcule donc le vecteur de lissage
                         */
                        float lissX = (i - x) * tileL;
                        float lissY = (j - y) * tileL;
                        
                        Point prev = unit.getPreviousLocation();

                        if (unit.getLissage())
                        {
                            Timer timerMove = unit.getTimerMove();
                            //Mise à jour du lissage 
                            float lissage = Map.getTileLenght() * (float) timerMove.time() / (float) timerMove.getDelay();


                            //System.err.println("Avant et maintenant : " + prev + " et "  + unit.getLocation());
                             /*
                             * Perte de continuité du lissage, donc on place 
                             * l'unité à la bonne position
                             */
                            if (prev.x != i || prev.y != j)
                            {
                                if (prev.x < i)//vient de la gauche
                                {
                                    lissX += -tileL + lissage;

                                } else if (prev.x > i)// vient de la droite
                                {
                                    lissX += tileL - lissage;
                                } else if (prev.y > j)//vient d'en haut (au sens d'un repère indirect
                                {
                                    lissY += tileL - lissage;
                                } else if (prev.y < j)
                                {
                                    lissY += -tileL + lissage;
                                }

                                // Si on est arrivé à destination
                                if ((Math.abs(lissX - (i - x) * tileL) < 5) && (Math.abs(lissY - (j - y) * tileL) < 5))
                                {
                                    unit.setLocation(unit.getLocation());
                                }
                            }

                        }

                        //On détermine l'orientation
                        if (prev.x < i)//vient de la gauche
                        {
                            unit.setOrientation(Unit.Orientation.DROITE);

                        } else if (prev.x > i)// vient de la droite
                        {
                            unit.setOrientation(Unit.Orientation.GAUCHE);
                        } else if (prev.y > j)//vient d'en haut (au sens d'un repère indirect
                        {
                            unit.setOrientation(Unit.Orientation.HAUT);
                        } else if (prev.y < j)
                        {
                            unit.setOrientation(Unit.Orientation.BAS);
                        }

                        unit.render(cont, g, lissX, lissY);
                    }
                }
            }
        }
    }

    /**
     * Adds graphical information about which unit belongs to which player,
     * their hp...
     *
     * @param cont
     * @param g
     * @param x
     * @param y
     * @param wi
     * @param he
     */
    public void renderPlayers(GameContainer cont, Graphics g, int x, int y, int wi, int he)
    {
        HashSet<Building> builds = new HashSet<>();
        for (int i = x; i < Math.min(x + wi + 1, w); i++)
        {
            for (int j = y; j < Math.min(y + he, h); j++)
            {
                //Rendu des barres de points de vie pour les unités
                // Affichage d'un petit carré vert pour les unités sélectionnées
                Unit unit = units[i][j];
                if (unit != null && !isWarFog(i, j) )
                {
                    /* Lissage de trajectoire : 
                     * l'unité vient de quatre cases adjacentes possibles.
                     * On calcule donc le vecteur de lissage
                     */
                    float lissX = (i - x) * tileL;
                    float lissY = (j - y) * tileL;
                    if (unit.getLissage())
                    {
                        Timer timerMove = unit.getTimerMove();
                        //Mise à jour du lissage 
                        float lissage = Map.getTileLenght() * (float) timerMove.time() / (float) timerMove.getDelay();


                        Point prev = unit.getPreviousLocation();
                        //System.err.println("Avant et maintenant : " + prev + " et "  + unit.getLocation());
                             /*
                         * Perte de continuité du lissage, donc on place 
                         * l'unité à la bonne position
                         */
                        if (prev.x != i || prev.y != j)
                        {
                            if (prev.x < i)//vient de la gauche
                            {
                                lissX += -tileL + lissage;

                            } else if (prev.x > i)// vient de la droite
                            {
                                lissX += tileL - lissage;
                            } else if (prev.y > j)//vient d'en haut (au sens d'un repère indirect
                            {
                                lissY += tileL - lissage;
                            } else if (prev.y < j)
                            {
                                lissY += -tileL + lissage;
                            }

                            // Si on est arrivé à destination
                            if ((Math.abs(lissX - (i - x) * tileL) < 5) && (Math.abs(lissY - (j - y) * tileL) < 5))
                            {
                                unit.setLocation(unit.getLocation());
                            }
                        }
                    }
                    //Barres de vie
                    g.setLineWidth(2);
                    g.setColor(unit.getOwner().getColor());
                    g.drawLine(lissX + 2, lissY - 5,
                            lissX + ((unit.getHp() / (float) unit.getMaxHp()) * (float) (tileL - 4)), lissY - 5);
                    //Sélection
                    if (selectedUnits.contains(unit))
                    {
                        g.setColor(Color.green);
                        g.fillRect(lissX, lissY + tileL - 4, 4, 4);
                    }

                }
                //Rendu des barres de points de vie pour les bâtiments
                // Il faut gérer les cases de pointeurs
                Building building = buildingMask[i][j];
                if (building != null && !isBlackout(i, j) &&
                    !builds.contains(building))
                {
                    builds.add(building);
                    //tracer barre de vie
                    g.setLineWidth(4);
                    g.setColor(building.getOwner().getColor());
                    Point l = building.getLocation();
                    g.drawLine((l.x - x) * tileL + 3, (l.y - y) * tileL - 10,
                            (l.x - x) * tileL + 3 + (tileL * building.getWidth() - 6) * building.getHp() / (float) building.getMaxHp(), (l.y - y) * tileL - 10);
                    if (selectedBuildings.contains(building))
                    {
                        g.setColor(Color.green);
                        g.fillRect((l.x - x) * tileL, (l.y - y + building.getHeight()) * tileL - 8,
                                8, 8);

                    }
                }
            }
        }
        g.setLineWidth(1);
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    /**
     *
     * @return tile length. If not set, 0
     */
    static public int getTileLenght() {
        return tileL;
    }

    /**
     * Adds an unit to the map if the case is not blocked. For now, erases the
     * previous unit on the case !
     *
     * @todo If the case is already occupied by a unit, we try to move this
     * unit.
     * @todo -> Plutôt faire ça dans la classe du bâtiment qui crée ces unités ?
     * @param unit
     * @return true if the unit has be addez
     */
    public boolean addUnit(Unit unit) {
        Point location = unit.getLocation();
        //System.err.println("L'unité est en (" + location.x + "," + location.y +")");
        //System.err.println("La couleur de la tile est " + map[location.x][location.y].getColor());
        if (!isBlocked(location.x, location.y)) {
            units[location.x][location.y] = unit;
            block(location.x, location.y);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a unit from the map.
     *
     * @param unit
     */
    public void removeUnit(Unit unit) {
        Point location = unit.getLocation();
        units[location.x][location.y] = null;
        unblock(location.x, location.y);
    }

    public void removeUnitAt(int x, int y) {
        if (isUnitAt(x, y)) {
            units[x][y] = null;
            unblock(x, y);
        }
    }

    /**
     * Moves a group of units. Chooses a default formation to move the group,
     * with group movement : all the units follow in a certain way the leader.
     *
     * @param x the leader will be one that location
     * @param y
     * @param units
     */
    public Move moveUnits(int x, int y, ArrayList<Unit> units) {
        if (!isBlocked(x, y)) {
            return new Move(this, units, new Point(x, y));
        } else {
            return new Move();
        }
    }

    public Move moveUnit(int x, int y, Unit unit) {
        if (!isBlocked(x, y)) {
            return new Move(this, unit, new Point(x, y));
        } else {
            return new Move(this, unit, unit.getLocation());
        }
    }

    /**
     * @param x
     * @param y
     * @return the unit ont the tile clicked or null if no unit
     */
    public Unit getUnitAt(int x, int y) {
        return units[x][y];
    }

    /**
     *
     * @param location
     * @param w
     * @param y
     * @return units in the square
     */
    public ArrayList<Unit> getUnitsAt(int x, int y, int width, int height) {
        ArrayList<Unit> u = new ArrayList<>();
        x = Math.max(x, 0);
        y = Math.max(y, 0);
        for (int i = x; i < x + width && i < w; i++) {
            for (int j = y; j < y + height && j < h; j++) {
                Unit unit = getUnitAt(i, j);
                if (unit != null) {
                    u.add(unit);
                }
            }
        }
        return u;
    }

    public boolean isUnitAt(int x, int y) {
        return units[x][y] != null;
    }

    /**
     * Adds a building if possible
     *
     * @param building
     * @return false if the building can not be placed at the location
     */
    public boolean addBuilding(Building building) {
        Point location = building.getLocation();
        int widthB = building.getWidth();
        int heightB = building.getHeight();

        //Vérifier si l'emplacement n'est pas bloqué
        for (int i = location.x; i < location.x + widthB; i++) {
            for (int j = location.y; j < location.y + heightB; j++) {
                if (isBlocked(i, j)) {
                    return false;
                }
            }
        }

        //Ajouter le bâtiment
        // et bloquer l'emplacement
        for (int i = 0; i < widthB; i++) {
            for (int j = 0; j < heightB; j++) {
                int lX = location.x + i;
                int lY = location.y + j;
                buildings[lX][lY] = building.getSubTile(i, j);
                buildingMask[lX][lY] = building;
                block(lX, lY);
            }
        }

        return true;
    }

    /**
     * Removes a building from the map
     *
     * @param building
     */
    public void removeBuilding(Building building) {
        Point location = building.getLocation();
        for (int i = location.x; i < location.x + building.getWidth(); i++) {
            for (int j = location.y; j < location.y + building.getHeight(); j++) {
                buildings[i][j] = null;
                buildingMask[i][j] = null;
                unblock(i, j);
            }
        }

    }

    /**
     * @param x
     * @param y
     * @return true if there is a building on that tile
     */
    public boolean isBuildingAt(int x, int y) {
        return buildingMask[x][y] != null;
    }

    /**
     *
     * @param x
     * @param y
     * @return building or null if no building there
     */
    public Building getBuildingAt(int x, int y) {
        return buildingMask[x][y];
    }

    /**
     * All the buildings in the square. Useful to select some buildings
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public ArrayList<Building> getBuildingsAt(int x, int y, int width, int height) {
        HashSet<Point> locations = new HashSet(width * height);//Au pire un bâtiment par tiles...

        ArrayList<Building> b = new ArrayList();
        x = Math.max(x, 0);
        y = Math.max(y, 0);
        for (int i = x; i < x + width && i < w; i++) {
            for (int j = y; j < y + height && j < h; j++) {
                Building building = getBuildingAt(i, j);
                if (building != null && !locations.contains(building.getLocation())) {
                    b.add(building);
                    locations.add(building.getLocation());
                }
            }
        }
        return b;
    }
}
