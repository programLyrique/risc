package units;

import buildings.Building;
import core.*;
import map.Attack;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *     Cette classe spécifie les Multiplexeurs
 */
public final class Multiplexeur extends Unit {

    //rayon de l'aoe au carré

    public Multiplexeur(Point p, Player _owner, Ressources res, Map m) throws SlickException {
        super(res.getCons_mult(),
                res.getSel_mult(),
                res.getDes_mult(),
                res.getLogo_mult(),
                res.getSprites_mult(),
                res.getHeigth_mult(),
                res.getWidth_mult(),
                p,
                900,//hp
                res.getDamage_mult(),
                res.getArmor_mult(),
                res.getDep_speed_mult(),
                res.getAtt_speed_mult(),
                res.getCons_time_mult(),
                res.getEnergy_cost_mult(),
                res.getWarm_cost_mult(),
                _owner,
                res.getRange_mult(),
                m,
                res
                );
        splash = 30;
        setLissage(true);
        shoot_snd = res.getAtt_mult();
    }

    /**
    * 
    * @param r
    * @param m 
    */
    public final void tir_cible(UandB r, Map m) {
        attack = new Attack(r, this, m, false, true);
    }
}
