package buildings;

import core.*;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 *     Spécifie le batiment Condensateur
 *
 */
public class Condensateur extends Building {

    private Timer timerEnergy;

    /**
     *
     * @param p
     * @param _owner
     * @throws SlickException
     */
    public Condensateur(Point p, Player _owner, Ressources res, Map map) throws SlickException {
        super(res.getCons_cond(),
                res.getSel_cond(),
                res.getDes_cond(),
                res.getLogo_cond(),
                res.getTiles_cond(),
                res.getHeigth_cond(),
                res.getWidth_cond(),
                p,
                250,//hp
                res.getArmor_cond(),
                res.getCons_time_cond(),
                res.getEnergy_cost_cond(),
                res.getWarm_cost_cond(),
                _owner,
                res.getRange_cond(),
                map);
                timerEnergy = new Timer(4000);

    }

    /**
     * fonction qui doit être appelée toute les secondes pour apporter de
     * l'énergie au joueur
     */
    @Override
    public void update(int delta) {
        timerEnergy.update(delta);
        if (timerEnergy.tick()) {
            apport();
        }
    }

    public void apport() {
        owner.setEnergy(owner.getEnergy() + 10);
    }
}