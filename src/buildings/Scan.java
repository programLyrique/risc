package buildings;

import core.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 *     Spécifie le batiment Scan
 *
 */
public class Scan extends Building {

    private Morph morph;

    /**
     *
     * @param p
     * @param _owner
     * @throws SlickException
     */
    public Scan(Point p, Player _owner, Ressources res, Map map) throws SlickException {
        super(res.getCons_scan(),
                res.getSel_scan(),
                res.getDes_scan(),
                res.getLogo_scan(),
                res.getTiles_scan(),
                res.getHeigth_scan(),
                res.getWidth_scan(),
                p,
                410,//hp
                res.getArmor_scan(),
                res.getCons_time_scan(),
                res.getEnergy_cost_scan(),
                res.getWarm_cost_scan(),
                _owner,
                res.getRange_scan(),
                map);
        Antivirus a = new Antivirus(this.getLocation(), this.getOwner(), res, map);
        morph = new Morph(this, map, true, a);
    }

    @Override
    public void update(int delta) {
        if (morph.isFinished()) {
            newU.update(delta);
        } else {
            try {
                morph.update(delta);
            } catch (SlickException ex) {
                Logger.getLogger(Scan.class.getName()).log(Level.SEVERE, "erreur sur l'update de morph", ex);
            }
        }
    }

    /**
     *
     * @return true si l'on a pu amélioré en antivirus
     * @throws SlickException
     */
    public void morph(Ressources res, Map map) throws SlickException {
        if (morph.isFinished()) {
            Antivirus a = new Antivirus(this.getLocation(), this.getOwner(), res, map);
            morph = new Morph(this, map, false, a);
        }
    }

    public Morph getMorph() {
        return morph;
    }
}
