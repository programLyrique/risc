package buildings;

import core.*;
import java.util.ArrayList;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import units.Unit;

/**
 *
 *     Spécifie le batiment Antivirus
 *
 */
public class Antivirus extends Building {

    private int damage = 100;
    private int att_speed = 1;
    private AttackAnti att;
    private Sound shoot_snd = new Sound("res/snd/anti.ogg");

    /**
     *
     * @param p
     * @param _owner
     * @throws SlickException
     */
    public Antivirus(Point p, Player _owner, Ressources res, Map map) throws SlickException {
        super(res.getCons_antivirus(),
                res.getSel_antivirus(),
                res.getDes_antivirus(),
                res.getLogo_antivirus(),
                res.getTiles_antivirus(),
                res.getHeigth_antivirus(),
                res.getWidth_antivirus(),
                p,
                700,//hp
                res.getArmor_antivirus(),
                res.getCons_time_antivirus(),
                res.getEnergy_cost_antivirus(),
                res.getWarm_cost_antivirus(),
                _owner,
                res.getRange_antivirus(),
                map);
        att = new AttackAnti(null, this, map);
    }

    /**
     *
     * @param a une unité ou un batiment ciblé
     */
    public final void tir_cible(UandB a, Map map) {
        att = new AttackAnti(a, this, map);
    }

    @Override
    public void update(int delta) {
        att.update(delta);
    }

    public int getDamage() {
        return damage;
    }

    public void setAtt(AttackAnti att) {
        this.att = att;
    }

    public Sound getShoot_snd() {
        return shoot_snd;
    }
}
