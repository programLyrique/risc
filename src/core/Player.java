package core;

import buildings.*;
import java.util.ArrayList;
import java.util.Iterator;
import map.Map;
import org.newdawn.slick.*;
import units.*;

/**
 * A player
 *
 * Information about a player.
 *
 *   pierre
 */
public final class Player {

    private int warm;
    private int energy;
    private int people;
    private int maxWarm;
    private Color color;
    private Point locPlayer;
    private int nbCondensateurs;
    /**
     * Les unités et les batiments du joueur
     */
    private ArrayList<Unit> units;
    private ArrayList<Building> buildings;
    /**
     * Les unités sélectionnés par le joueur
     */
    private ArrayList<Unit> selectedUnits;
    // Les bâtiments sélectionnés par le joueur
    private ArrayList<Building> selectedBuildings;
    /**
     * La matrice dont les cases retournent true si le joueur peut "voir" la
     * case.
     */
    public boolean[][] seen;
    private Map m;

    /**
     * Create a new player
     *
     * @param color
     * @param location of the first kernel of the player
     * @param m
     * @throws SlickException
     */
    public Player(Color color, Point location, Map m, Ressources res) throws SlickException {
        locPlayer = location;
        this.maxWarm = 500;
        this.warm = 0;
        this.energy = 900;
        this.people = 3;
        this.color = color;
        this.m = m;


        //Initialisation des ArrayList
        units = new ArrayList<>(30);
        buildings = new ArrayList<>(20);
        selectedUnits = new ArrayList<>(20);
        selectedBuildings = new ArrayList<>(10);
        Kernel k = new Kernel(location, this, res, m);
        addBuilding(k);
        Bit b1 = new Bit(new Point(location.x + k.getWidth() + 1, location.y), this, res, m);
        Bit b2 = new Bit(new Point(location.x + k.getWidth() + 1, location.y + k.getHeight() / 2), this, res, m);
        Bit b3 = new Bit(new Point(location.x + k.getWidth() + 1, location.y + k.getHeight()), this, res, m);
        addUnit(b3);
        addUnit(b2);
        addUnit(b1);
        setWarm(3 * b1.getWarm_cost() + k.getWarm_cost());
        seen = new boolean[m.getWidth()][m.getHeight()];
        seen_maj(m);// mise à jour de la carte de vision.
    }

    /**
     * Fait la mise à jour de seen.
     *
     * @param m
     */
    public void seen_maj(Map m) {
        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                seen[x][y] = false;
            }
        }
        Point p;
        for (Building b : buildings) {
            for (int x = 0; x < m.getWidth(); x++) {
                for (int y = 0; y < m.getHeight(); y++) {
                    p = new Point(x, y);
                    seen[x][y] = seen[x][y] || (b.getLocation().distance_square(p) < b.getRange());
                }
            }
        }
        for (Unit u : units) {
            for (int x = 0; x < m.getWidth(); x++) {
                for (int y = 0; y < m.getHeight(); y++) {
                    p = new Point(x, y);
                    seen[x][y] = seen[x][y] || (u.getLocation().distance_square(p) < u.getRange());
                }
            }
        }
    }

    public void update(int delta) {
        for (Unit u : units) {
            u.update(delta);
        }
        for (Building u : buildings) {
            u.update(delta);
        }
    }

    public boolean addUnit(Unit unit) {
        if (m.addUnit(unit)) {
            units.add(unit);
            return true;
        }
        return false;
    }

    public void removeUnit(Unit unit) {
        m.removeUnit(unit);
        units.remove(unit);
    }

    public boolean addBuilding(Building building) {
        if (m.addBuilding(building)) {
            buildings.add(building);
            return true;
        }
        return false;
    }

    public void removeBuilding(Building building) {
        m.removeBuilding(building);
        buildings.remove(building);
    }

    public void removeUandB(UandB u) {
        if (u instanceof Unit) {
            Unit v = (Unit) u;
            removeUnit(v);
        } else {
            Building b = (Building) u;
            removeBuilding(b);
        }
    }

    /**
     *
     * @param uab
     * @return true si le joueur/client voit l'unite ou le batiment uab
     */
    public boolean see_cible(UandB uab) {
        boolean aux = false;
        int h = uab.getHeight();
        int w = uab.getWidth();
        int x = -0;
        int y = -0;
        while (!aux && x < w) {
            while (!aux && y < h) {
                aux = seen[uab.getLocation().x + x][uab.getLocation().y + y];
                y++;
            }
            y = 0;
            x++;
        }
        return aux;
    }

    /**
     *
     * @return true si le joueur peut encore jouer utilement
     */
    public boolean isAlive() {
        boolean isUseless = false;
        for (Iterator<Building> it = buildings.iterator(); it.hasNext() && !isUseless;) {
            Building b = it.next();
            isUseless = b instanceof Kernel;
        }
        return (!units.isEmpty() || isUseless);
    }

    public int getMaxWarm() {
        return maxWarm;
    }

    public void setMaxWarm(int maxWarm) {
        this.maxWarm = maxWarm;
    }

    public int getWarm() {
        return warm;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPeople() {
        return people;
    }

    public Color getColor() {
        return color;
    }

    public void setWarm(int warm) {
        this.warm = warm;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point getLocation() {
        return locPlayer;
    }

    /**
     * Select units of the player which are in the desired square. All arguments
     * in tiles, and positive
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void select(int x, int y, int width, int height) {
        selectedBuildings = m.getBuildingsAt(x, y, width, height);
        //On enlève les bâtiments qui ne sont pas du joueur
        Iterator<Building> itB = selectedBuildings.iterator();
        while (itB.hasNext()) {
            if (itB.next().getOwner() != this) {
                itB.remove();
            }
        }
        selectedUnits = m.getUnitsAt(x, y, width, height);
        //On enlève les unités qui ne sont pas du joueur
        Iterator<Unit> itU = selectedUnits.iterator();
        while (itU.hasNext()) {
            if (itU.next().getOwner() != this) {
                itU.remove();
            }
        }
    }

    /**
     *
     * @return selected units
     */
    public ArrayList<Unit> getSelectedUnits() {
        return selectedUnits;
    }

    /**
     *
     * @return selected buildings
     */
    public ArrayList<Building> getSelectedBuildings() {
        return selectedBuildings;
    }

    public void clearSelection() {
        selectedUnits.clear();
        selectedBuildings.clear();
    }

    /**
     * To filter selected elements (units or buildings) for a player
     *
     * @param element
     * @param player
     * @return
     */
    public static void filter(ArrayList<UandB> element, Player player) {
        Iterator<UandB> it = element.iterator();
        while (it.hasNext()) {
            if (!it.next().getOwner().equals(player)) {
                it.remove();
            }
        }
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }
}
