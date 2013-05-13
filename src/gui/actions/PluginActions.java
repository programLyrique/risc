package gui.actions;

import buildings.Building;
import buildings.Kernel;
import buildings.Plugin;
import core.HostGame;
import core.Player;
import core.Ressources;
import gui.Action;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.ActionMessage;
import org.newdawn.slick.SlickException;

/**
 *
 *    
 */
public class PluginActions extends GroupActions {

    public PluginActions(Player player, map.Map map, Ressources res, core.Game g) {
        super("Plugin", player, map, res);
        addAction(new PluginActions.Create_Bit(g));
    }
    
    public PluginActions(Player player, map.Map map, Ressources res, HostGame g) {
        super("Plugin", player, map, res);
        addAction(new PluginActions.Create_Bit(g));
    }

    private class Create_Bit extends Action {

        public Create_Bit(core.Game g) {
            super(res.getLogo_bit(), "Créer un bit", g);
        }
        
        public Create_Bit(HostGame g) {
            super(res.getLogo_bit(), "Créer un bit", g);
        }

        @Override
        public void clicked() {
            /*ArrayList<Building> selectedBuildings = player.getSelectedBuildings();
            //Pour l'instant, il n'y a que des kernels
            if (selectedBuildings != null && selectedBuildings.get(0) instanceof Plugin) {
                Plugin plug = (Plugin) selectedBuildings.get(0);
                try {
                    plug.new_bit(map, res);
                } catch (SlickException ex) {
                    Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                            "Erreur à la création de l'unité dans le Plugin", ex);
                }

            }*/
            sendAction();
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = false;
            m.type = 6;
            return m;
        }
    }
}
