package buildings;

import core.*;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/**
 *
 *    
 * Spécifie le batiment Event_aeration
 * 
 */
public class EventAeration extends Building{
    
    /**
     * 
     * @param p lieu de construction
     * @param _owner joueur propriétaire
     * @throws SlickException 
     */
    
    public EventAeration(Point p, Player _owner, Ressources res, Map map) throws SlickException {
        super(res.getCons_event(),
                res.getSel_event(),
                res.getDes_event(),
                res.getLogo_event(),
                res.getTiles_event(),
                res.getHeigth_event(), 
                res.getWidth_event(),
                p,
                250,//hp
                res.getArmor_event(),
                res.getCons_time_event(),
                res.getEnergy_cost_event(),
                res.getWarm_cost_event(),
                _owner,
                res.getRange_event(),
                map);
        owner.setMaxWarm(owner.getMaxWarm()+100);    
    }
}