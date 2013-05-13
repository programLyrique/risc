/**
 *
 */
package units;

import buildings.*;
import core.*;
import map.*;
import org.newdawn.slick.*;

/**
 *     Cette classe spécifie les Bit
 */
public final class Bit extends Unit {

    public Bit(Point p, Player _owner, Ressources res, Map m) throws SlickException {
        super(res.getCons_bit(),
                res.getSel_bit(),
                res.getDes_bit(),
                res.getLogo_bit(),
                res.getSprites_bit(),
                res.getHeigth_bit(),
                res.getWidth_bit(),
                p,
                200,//hp
                res.getDamage_bit(),
                res.getArmor_bit(),
                res.getDep_speed_bit(),
                res.getAtt_speed_bit(),
                res.getCons_time_bit(),
                res.getEnergy_cost_bit(),
                res.getWarm_cost_bit(),
                _owner,
                res.getRange_bit(),
                m,
                res);
        setLissage(true);
        shoot_snd = res.getAtt_bit();
    }

    /**
     *
     * @param p
     * @param m
     * @return true si on a pu construire un Kernel
     * @throws SlickException
     */
    public final void new_kernel(Point p, Map m, Ressources res) throws SlickException {
        Kernel k = new Kernel(p, owner, res, m);
        build = new Build(this, m, false, k);
    }

    /**
     *
     * @param p
     * @param m
     * @return ...Scan
     * @throws SlickException
     */
    public final void new_scan(Point p, Map m, Ressources res) throws SlickException {
        Scan k = new Scan(p, owner, res, m);
        build = new Build(this, m, false, k);
    }

    /**
     *
     * @param p
     * @param m
     * @return ...Condensateur
     * @throws SlickException
     */
    public final void new_condensateur(Point p, Map m, Ressources res) throws SlickException {
        Condensateur k = new Condensateur(p, owner, res, m);
        build = new Build(this, m, false, k);
    }

    /**
     *
     * @param p
     * @param m
     * @return ... event d'aeration
     * @throws SlickException
     */
    public final void new_event_aeration(Point p, Map m, Ressources res) throws SlickException {
        EventAeration k = new EventAeration(p, owner, res, m);
        build = new Build(this, m, false, k);
    }

    /**
     *
     * @param p
     * @param m
     * @throws SlickException
     */
    public final void new_plugin(Point p, Map m, Ressources res) throws SlickException {
        Plugin k = new Plugin(p, owner, res, m);
        build = new Build(this, m, false, k);
    }
}
