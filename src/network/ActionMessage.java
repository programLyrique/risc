/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import core.Point;

/**
 *
 *    
 */
public class ActionMessage extends Message {
    
    public Point goal;
    
    public boolean unitOrBuilding;
    public int type;
    public int player;
    public int selX;
    public int selY;
    public int selWidth;
    public int selHeight;
    
    public ActionMessage()
    {
        super(3);
    }
    
}
