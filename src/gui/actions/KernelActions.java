package gui.actions;

import core.*;
import gui.*;
import buildings.*;
import map.*;
import org.newdawn.slick.*;
import java.util.*;
import java.util.logging.*;
import network.ActionMessage;

/**
 *
 *   pierre
 */
public class KernelActions extends GroupActions {

    public KernelActions(Player player, map.Map map, Ressources res, core.Game g) {
        super("Kernel", player, map, res);
        addAction(new Create_Bit(g));
        addAction(new Create_Octet(g));
        addAction(new Create_Mult(g));
        addAction(new Create_Bombe(g));
    }
    
    public KernelActions(Player player, map.Map map, Ressources res, HostGame g) {
        super("Kernel", player, map, res);
        addAction(new Create_Bit(g));
        addAction(new Create_Octet(g));
        addAction(new Create_Mult(g));
        addAction(new Create_Bombe(g));
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
            if (selectedBuildings != null && selectedBuildings.get(0) instanceof Kernel) {
                Kernel kern = (Kernel) selectedBuildings.get(0);
                try {
                    kern.new_bit(map, res);
                } catch (SlickException ex) {
                    Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                            "Erreur à la création de l'unité dans le Kernel", ex);
                }

            }*/
            sendAction();
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = false;
            m.type = 1;
            return m;
        }
    }

    private class Create_Octet extends Action {

        public Create_Octet(core.Game g) {
            super(res.getLogo_octet(), "Créer un octet", g);
        }
        
        public Create_Octet(HostGame g) {
            super(res.getLogo_octet(), "Créer un octet", g);
        }

        @Override
        public void clicked() {
            /*ArrayList<Building> selectedBuildings = player.getSelectedBuildings();
            if (selectedBuildings != null && selectedBuildings.get(0) instanceof Kernel) {
                Kernel kern = (Kernel) selectedBuildings.get(0);
                try {
                    kern.new_octet(map, res);
                } catch (SlickException ex) {
                    Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                            "Erreur à la création de l'unité dans le Kernel", ex);
                }

            }*/
            sendAction();
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = false;
            m.type = 2;
            return m;
        }
    }

    private class Create_Mult extends Action {

        public Create_Mult(core.Game g) {
            super(res.getLogo_mult(), "Créer un multiplexeur", g);
        }
        
        public Create_Mult(HostGame g) {
            super(res.getLogo_mult(), "Créer un multiplexeur", g);
        }

        @Override
        public void clicked() {
            /*ArrayList<Building> selectedBuildings = player.getSelectedBuildings();
            if (selectedBuildings != null && selectedBuildings.get(0) instanceof Kernel) {
                Kernel kern = (Kernel) selectedBuildings.get(0);
                try {
                    kern.new_mult(map, res);
                } catch (SlickException ex) {
                    Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                            "Erreur à la création de l'unité dans le Kernel", ex);
                }

            }*/
            sendAction();
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = false;
            m.type = 3;
            return m;
        }
    }
    
    private class Create_Bombe extends Action
    {

        public Create_Bombe(core.Game g)
        {
            super(res.getLogo_bombe(), "Créer un bombe", g);
        }
        
        public Create_Bombe(HostGame g)
        {
            super(res.getLogo_bombe(), "Créer un bombe", g);
        }

        @Override
        public void clicked()
        {
            /*ArrayList<Building> selectedBuildings = player.getSelectedBuildings();
            //Pour l'instant, il n'y a que des kernels
            if (selectedBuildings != null && selectedBuildings.get(0) instanceof Kernel)
            {
                Kernel kern = (Kernel) selectedBuildings.get(0);
                try
                {
                    kern.new_bombe(map, res);
                } catch (SlickException ex)
                {
                    Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                            "Erreur à la création de l'unité dans le Kernel", ex);
                }

            }*/
            sendAction();
        }

        @Override
        public ActionMessage getActionAssociated() {
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = false;
            m.type = 4;
            return m;
        }
    }
}
