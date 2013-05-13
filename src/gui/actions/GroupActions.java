package gui.actions;

import core.*;
import gui.*;
import map.*;
import java.util.*;

/**
 * Base class for group of actions
 *   pierre
 */
public class GroupActions
{
    protected Ressources res;
    protected Player player;
    protected map.Map map;
    private ArrayList<Action> actions;
    
    private String name;

    public GroupActions(String name, Player player, map.Map map, Ressources res)
    {
        this.player = player;
        this.res = res;
        this.map = map;
        this.name = name;
        actions = new ArrayList<>(10);
    }
    
    /**
     * Name of the class you want to bind to the group of action (= the view).
     * @return 
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Adds an action to the group of actions
     * @param action 
     */
    public void addAction(Action action)
    {
        actions.add(action);
    }
    
    /**
     * 
     * @return actions
     */
    public ArrayList<Action> getActions()
    {
        return actions;
    }

}
