package gui;


import core.HostGame;
import network.ActionMessage;
import org.newdawn.slick.*;

/**
 * An action in the commande pannel.
 * Extend it and ovveride the clicked method.
 *   pierre
 */
abstract public class Action {
    private Image logo;
    private String description;
    private core.Game game = null;
    private HostGame hostGame = null;
    
    public Action(Image img, String des, core.Game g)
    {
        logo = img;
        description = des;
        game = g;
    }
    
    public Action(Image img, String des, HostGame g)
    {
        logo = img;
        description = des;
        hostGame = g;
    }
    
    public Image getLogo()
    {
        return logo;
    }

    @Override
    public String toString() 
    {
        return description;
    }
    
   
    /**
     * Called when the action button is clicked
     */
    abstract public void clicked();
    
    abstract public ActionMessage getActionAssociated();
    
    public void sendAction() {
        if(game == null) {
            ActionMessage m = getActionAssociated();
            m.player = 1;
            hostGame.sendActionMessage(m);
        }
        else {
            ActionMessage m = getActionAssociated();
            m.player = 2;
            game.sendActionMessage(m);
        }
    }
}
