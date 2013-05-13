package gui.actions;

import buildings.Building;
import buildings.Kernel;
import core.Game;
import core.HostGame;
import core.Player;
import core.Point;
import core.Ressources;
import gui.Action;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.ActionMessage;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import units.Bit;
import units.Unit;

/**
 *
 *    
 */
public class BitActions extends GroupActions
{

    public BitActions(Player player, map.Map map, Ressources res, Game g)
    {
        super("Bit", player, map, res);
        addAction(new Create_Kernel(g));
        addAction(new Create_Scan(g));
        addAction(new Create_Plugin(g));
        addAction(new Create_Event(g));
        addAction(new Create_Cond(g));
    }
    
    public BitActions(Player player, map.Map map, Ressources res, HostGame g)
    {
        super("Bit", player, map, res);
        addAction(new Create_Kernel(g));
        addAction(new Create_Scan(g));
        addAction(new Create_Plugin(g));
        addAction(new Create_Event(g));
        addAction(new Create_Cond(g));
    }
    
    abstract private class Create_Factory extends Action 
    {

        public Create_Factory(Image img, String des, Game g)
        {
            super(img, des, g);
        }
        
        public Create_Factory(Image img, String des, HostGame g)
        {
            super(img, des, g);
        }
        
        public abstract void new_building(Bit bit, Point location, map.Map map, Ressources res) 
                throws SlickException;
        
        @Override
        public void clicked()
        {
            /*ArrayList<Unit> selectedUnits = player.getSelectedUnits();
            if (selectedUnits != null && selectedUnits.get(0) instanceof Bit)
            {
                Bit bit = (Bit) selectedUnits.get(0);
                try
                {
                    Point p = new Point(bit.getLocation().x, bit.getLocation().y);
                    new_building(bit, p, map, res);
                } catch (SlickException ex)
                {
                    Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                            "Erreur à la création du kernel par le bit", ex);
                }
            }*/
            sendAction();
        }
        
    }

    private class Create_Kernel extends Create_Factory
    {

        public Create_Kernel(Game g)
        {
            super(res.getLogo_kernel(), "Créer un Kernel", g);
        }
        
        public Create_Kernel(HostGame g)
        {
            super(res.getLogo_kernel(), "Créer un Kernel", g);
        }

        @Override
        public void new_building(Bit bit, Point location, map.Map map, Ressources res) 
                throws SlickException
        {
            bit.new_kernel(location, map, res);
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = true;
            m.type = 1;
            return m;
        }
      
    }
    
    private class Create_Scan extends Create_Factory
    {

        public Create_Scan(Game g)
        {
            super(res.getLogo_scan(), "Créer un Scan", g);
        }
        
        public Create_Scan(HostGame g)
        {
            super(res.getLogo_scan(), "Créer un Scan", g);
        }

        @Override
         public void new_building(Bit bit, Point location, map.Map map, Ressources res) 
                throws SlickException
        {
            bit.new_scan(location, map, res);
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = true;
            m.type = 2;
            return m;
        }
    }
    
     private class Create_Plugin extends Create_Factory
    {
        public Create_Plugin(Game g)
        {
            super(res.getLogo_plugin(), "Créer un Plugin", g);
        }
        
        public Create_Plugin(HostGame g)
        {
            super(res.getLogo_plugin(), "Créer un Plugin", g);
        }

        @Override
         public void new_building(Bit bit, Point location, map.Map map, Ressources res) 
                throws SlickException
        {
            bit.new_plugin(location, map, res);
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = true;
            m.type = 3;
            return m;
        }
    }
     
     private class Create_Event extends Create_Factory
     {
         public Create_Event(core.Game g)
         {
             super(res.getLogo_event(), "Créer un évent d'aération", g);
         }
         
         public Create_Event(HostGame g)
         {
             super(res.getLogo_event(), "Créer un évent d'aération", g);
         }
         
         @Override
         public void new_building(Bit bit, Point location, map.Map map, Ressources res) 
                throws SlickException
        {
            bit.new_event_aeration(location, map, res);
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = true;
            m.type = 4;
            return m;
        }
     }
     
     private class Create_Cond extends Create_Factory
     {
         public Create_Cond(core.Game g)
         {
             super(res.getLogo_cond(), "Créer un condensateur", g);
         }
         
         public Create_Cond(HostGame g)
         {
             super(res.getLogo_cond(), "Créer un condensateur", g);
         }
         
         @Override
         public void new_building(Bit bit, Point location, map.Map map, Ressources res) 
                throws SlickException
        {
            bit.new_condensateur(location, map, res);
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = true;
            m.type = 5;
            return m;
        }
     }
}
