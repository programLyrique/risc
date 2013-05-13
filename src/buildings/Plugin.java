package buildings;

import core.*;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import units.Bit;


/**
 *
 *    
 * Spécifie le batiment Plugin
 * 
 */
public class Plugin extends Building{
    
    private Point pointRalliement;
    
    /**
     * 
     * @param p
     * @param _owner
     * @throws SlickException 
     */
    public Plugin(Point p, Player _owner, Ressources res, Map map) throws SlickException {
        super(res.getCons_plugin(),
                res.getSel_plugin(),
                res.getDes_plugin(),
                res.getLogo_plugin(),
                res.getTiles_plugin(),
                res.getHeigth_plugin(), 
                res.getWidth_plugin(),
                p,
                600,//hp
                res.getArmor_plugin(),
                res.getCons_time_plugin(),
                res.getEnergy_cost_plugin(),
                res.getWarm_cost_plugin(),
                _owner,
                res.getRange_plugin(),
                map);
    }
    
    /**
     * 
     * @return
     * @throws SlickException 
     */
    public final void new_bit(Map m, Ressources res) throws SlickException{
        Bit b = new Bit(getExit(), owner,res, m);
        newU.add(b);
    }
}

