package core;

import java.util.ArrayList;
import map.*;
import org.newdawn.slick.*;

/**
 *
 *    
 */
public abstract class UandB {

    protected Sound cons_snd;
    protected Sound selected_snd;
    protected Sound destruction_snd;
    protected int range;
    protected Image logo;
    protected int height;
    protected int width;
    protected Player owner;
    protected Point location;
    protected int hp;
    protected int max_hp;
    protected int armor;
    protected int cons_time;
    protected int energy_cost;
    protected int warm_cost;
    

    /**
     * @param s1 son de construction
     * @param s2 son de selection
     * @param s3 son de destruction
     * @param range porté de l'unité
     * @param im logo de l'unité
     * @param p position de l'unité
     * @param _hp points de vie
     * @param _armor armure
     * @param _cons_time temps de construction
     * @param _energy_cost cout en énergie
     * @param _warm_cost cout en chaleur
     * @param _owner joueur propriétaire de l'unité
     * @throws SlickException
     */
    protected UandB(Sound s1, Sound s2, Sound s3, int _range, Image im, int h, int w, Color color, Point p, int _hp, int _armor, int _cons_time,
            int _energy_cost, int _warm_cost, Player _owner)
            throws SlickException {
        // Toute les unités et les bâtiments sont infranchissables
        cons_snd = s1;
        selected_snd = s2;
        destruction_snd = s3;
        range = _range;
        height = h;
        width = w;
        logo = im;
        owner = _owner;
        location = p;
        hp = _hp;
        max_hp = _hp;
        armor = _armor;
        cons_time = _cons_time;
        energy_cost = _energy_cost;
        warm_cost = _warm_cost;
    }

    /**
     *
     * @return true si l'unité ou le batiment est encore en vie
     */
    public final Boolean isAlive() {
        return (hp > 0);
    }

    public final int distance_square(UandB u) {
        Point p = new Point(u.getLocation().x + width / 2, (u.getLocation().y + height / 2));
        int i = p.distance_square(new Point (getLocation().x + width / 2, (getLocation().y + height / 2)));
        return i;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Sound getCons_snd() {
        return cons_snd;
    }

    public Sound getSelected_snd() {
        return selected_snd;
    }

    public Sound getDestruction_snd() {
        return destruction_snd;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getCons_time() {
        return cons_time;
    }

    public void setCons_time(int cons_time) {
        this.cons_time = cons_time;
    }

    public int getEnergy_cost() {
        return energy_cost;
    }

    public void setEnergy_cost(int energy_cost) {
        this.energy_cost = energy_cost;
    }

    public int getWarm_cost() {
        return warm_cost;
    }

    public void setWarm_cost(int warm_cost) {
        this.warm_cost = warm_cost;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMaxHp() {
        return max_hp;
    }

    /**
     * Basic version that draws only one tile. Reimplement it for units or
     * building which have more than one tile.
     *
     * @param gc
     * @param gr
     * @param x
     * @param y
     */
    public void render(GameContainer gc, Graphics gr, int x, int y) {
        render(gc, gr, (int) x, (int) y);
    }
    
    /**
     * A version of render to smooth the trajectories of a unit
     * @param gc
     * @param gr
     * @param x
     * @param y 
     */
    public void render(GameContainer gc, Graphics gr, float x, float y)
    {
        getLogo().drawEmbedded(x, y);
    }
}
