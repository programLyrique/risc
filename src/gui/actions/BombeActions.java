package gui.actions;

import core.Game;
import core.HostGame;
import core.Player;
import core.Point;
import core.Ressources;
import gui.Action;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Map;
import network.ActionMessage;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import units.Bit;
import units.BombeLogique;
import units.Unit;

/**
 *
 *    
 */
public class BombeActions extends GroupActions{
    
    public BombeActions(Player player, map.Map map, Ressources res, Game g) {
        super("bombe", player, map, res);
        addAction(new Explose_Cible(g));
    }
    
    public BombeActions(Player player, map.Map map, Ressources res, HostGame g) {
        super("BombeLogique", player, map, res);
        addAction(new Explose_Cible(g));
    }
    
    
    private class Explose_Cible extends Action {
        
        public Explose_Cible(Game g) {
          super(res.getLogo_bombe(), "Exploser", g);
        }
        
        public Explose_Cible(HostGame g) {
          super(res.getLogo_bombe(), "Exploser", g);
        }
        
        @Override
        public void clicked()
        {
            /*ArrayList<Unit> selectedUnits = player.getSelectedUnits();
            if (selectedUnits != null && selectedUnits.get(0) instanceof BombeLogique)
            {
                BombeLogique bombe = (BombeLogique) selectedUnits.get(0);
                try
                {
                    Point p = new Point(bombe.getLocation().x, bombe.getLocation().y);
                    bombe.explose(p, map);
                } catch (SlickException ex)
                {
                    Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                            "Erreur à la création du kernel par le bit", ex);
                }
            }*/
            sendAction();
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = true;
            m.type = 6;
            return m;
        }
    }
    
}
