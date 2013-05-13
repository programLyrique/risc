package units;

import buildings.*;
import core.*;
import map.*;
import map.Move;
import org.newdawn.slick.*;

/**
 *     Cette classe gère l'ensemble des unités
 */
abstract public class Unit extends UandB {

    protected Attack attack;
    protected Move move;
    protected Sound shoot_snd;
    protected int damage;
    protected int dep_speed;
    protected int att_speed;
    protected Build build;
    protected int splash;
    protected Timer moveTimer;
    
    //Sert surtout au lissage de trajectoire des unités
    protected Point previousLocation;
    private boolean lissage;
    
    //Haut, droite, bas, gauche
    private Image[] sprites;
    
    public enum Orientation
    {
        HAUT,
        DROITE,
        BAS,
        GAUCHE
    }
    private Orientation  orientation;
    /**
     *
     * @param s1 cons_snd
     * @param s2 sel_snd
     * @param s3 des_snd
     * @param im logo
     * @param h height
     * @param w width
     * @param p location
     * @param _hp
     * @param _damage
     * @param _armor
     * @param _dep_speed
     * @param _att_speed
     * @param _cons_time
     * @param _energy_cost
     * @param _warm_cost
     * @param _owner
     * @param _range
     * @throws SlickException
     */
    public Unit(Sound s1, Sound s2, Sound s3, Image im, Image[] sprites, int h, int w, Point p, int _hp, int _damage, int _armor,
            int _dep_speed, int _att_speed, int _cons_time,
            int _energy_cost, int _warm_cost, Player _owner, int _range, Map m, Ressources res) throws SlickException {
        super(s1, s2, s3, _range, im, h, w, _owner.getColor(), p, _hp, _armor, _cons_time, _energy_cost, _warm_cost, _owner);
        //attack.stop();
        damage = _damage;
        dep_speed = _dep_speed;
        att_speed = _att_speed;
        splash = 0;
        move = new Move(m, this, p);
        move.stop();
        attack = new Attack(this, this, m, true, false);
        attack.stop();
        build = new Build(this, m, true, new Kernel(p, _owner, res, m));
        build.stop();
        moveTimer = new Timer(500/dep_speed);//Timer(100 / dep_speed);
        previousLocation = getLocation();
        lissage = false;
        this.sprites = sprites;
        orientation = Orientation.HAUT;
    }

    /**
     * tir sur une batiment si elle est à portée, se déplace vers la cible
     * sinon.
     *
     * @param b batiment ciblé
     */
    public void tir_cible(UandB b, Map m) {
        build.stop();
        attack = new Attack(b, this, m, false, false);
    }

    public void update(int delta) {
        attack.update(delta);
        build.update(delta);
        moveTimer.update(delta);
        if (moveTimer.tick()) {
            move.update();
        }
    }

    /**
     * effectue un déplacement unitaire vers le point p
     *
     * @param p
     */
    public void dep(Point p) {
        move.update();
    }

    public Sound getShoot_snd() {
        return shoot_snd;
    }

    public int getDamage() {
        return damage;
    }

    public int getDep_speed() {
        return dep_speed;
    }

    public int getAtt_speed() {
        return att_speed;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Attack getAttack() {
        return attack;
    }

    @Override
    public void setLocation(Point location) {
        previousLocation = getLocation();
        super.setLocation(location);
    }

    public Point getPreviousLocation() {
        return previousLocation;
    }

    public Build getBuild() {
        return build;
    }

    public int getSplash() {
        return splash;
    }
    
    public Timer getTimerMove()
    {
        return moveTimer;
    }
    
    public void setLissage(boolean lissage)
    {
        this.lissage = lissage;
    }
    
    public boolean getLissage()
    {
        return lissage;
    }
    
    public void setOrientation(Orientation orienta)
    {
        orientation = orienta;
    }
    
    public Orientation getOrientation()
    {
        return orientation;
    }

    @Override
    public void render(GameContainer gc, Graphics gr, float x, float y)
    {
        sprites[orientation.ordinal()].drawEmbedded(x, y);
    }
    
}
