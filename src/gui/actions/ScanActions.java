package gui.actions;

import buildings.*;
import core.*;
import gui.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.ActionMessage;
import org.newdawn.slick.SlickException;

/**
 *
 *   pierre
 */
public class ScanActions extends GroupActions
{

    public ScanActions(Player player, map.Map map, Ressources res, core.Game g)
    {
        super("Scan", player, map, res);
        addAction(new Evolution(g));
    }
    
    public ScanActions(Player player, map.Map map, Ressources res, HostGame g)
    {
        super("Scan", player, map, res);
        addAction(new Evolution(g));
    }

    private class Evolution extends Action
    {

        public Evolution(core.Game g)
        {
            super(res.getLogo_antivirus(), "Améliorer en antivirus", g);
        }
        
        public Evolution(HostGame g)
        {
            super(res.getLogo_antivirus(), "Améliorer en antivirus", g);
        }

        @Override
        public void clicked()
        {
            /*ArrayList<Building> selectedBuildings = player.getSelectedBuildings();
            for (Building building : selectedBuildings)
            {
                try
                {
                    if (building instanceof Scan)
                    {
                        Scan scan = (Scan) building;
                        scan.morph(res, map);
                    }
                } catch (SlickException ex)
                {
                    Logger.getLogger(ScanActions.class.getName()).log(Level.SEVERE, 
                            "Impossible d'améliorer le scan", ex);
                }
            }*/
            sendAction();
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = false;
            m.type = 5;
            return m;
        }
    }
}
