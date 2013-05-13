package units;

import core.*;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *     Cette classe spécifie les bombes logiques
 */
public final class BombeLogique extends Unit {

    private ExplosionBombe exp;
    private int dmgExplosion;

    public BombeLogique(Point p, Player _owner, Ressources res, Map m) throws SlickException {
        super(res.getCons_bombe(),
                res.getSel_bombe(),
                res.getDes_bombe(),
                res.getLogo_bombe(),
                res.getSprites_bombe(),
                res.getHeigth_bombe(),
                res.getWidth_bombe(),
                p,
                350,//hp
                res.getDamage_bombe(),
                res.getArmor_bombe(),
                res.getDep_speed_bombe(),
                res.getAtt_speed_bombe(),
                res.getCons_time_bombe(),
                res.getEnergy_cost_bombe(),
                res.getWarm_cost_bombe(),
                _owner,
                res.getRange_bombe(),
                m,
                res);
        exp = new ExplosionBombe(this, p, m, true);
        splash = 40;
        dmgExplosion = 100;
        setLissage(true);
        shoot_snd = res.getAtt_bombe();
    }

    @Override
    public void update(int delta) {
        attack.update(delta);
        build.update(delta);
        exp.update();
        moveTimer.update(delta);
        if (moveTimer.tick()) {
            move.update();
        }
    }

    public void explose(Point p, Map m) throws SlickException {
        exp = new ExplosionBombe(this, p, m, false);
    }

    @Override
    public void tir_cible(UandB u, Map m) {
        exp = new ExplosionBombe(this, u.getLocation(), m, false);
    }

    public int getDmgExplosion() {
        return dmgExplosion;
    }
}
