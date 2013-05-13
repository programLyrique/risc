/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import map.MapGenerator;

/**
 *
 *    
 */
public class InformationGameMessage extends Message{
    
    public String name;
    public int mapType;
    
    public InformationGameMessage() {
        super(8);
    }
}
