package units;

import core.*;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *     Cette classe spécifie les Octets
 */
public class Octet extends Unit {

    public Octet(Point p, Player _owner, Ressources res, Map m) throws SlickException {
        super(res.getCons_octet(),
                res.getSel_octet(),
                res.getDes_octet(),
                res.getLogo_octet(),
                res.getSprites_octet(),
                res.getHeigth_octet(),
                res.getWidth_octet(),
                p,
                450,//hp
                res.getDamage_octet(),
                res.getArmor_octet(),
                res.getDep_speed_octet(),
                res.getAtt_speed_octet(),
                res.getCons_time_octet(),
                res.getEnergy_cost_octet(),
                res.getWarm_cost_octet(),
                _owner,
                res.getRange_octet(),
                m,
                res
                );
        setLissage(true);
        shoot_snd = res.getAtt_octet();
    }
}
