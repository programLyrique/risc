/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import com.esotericsoftware.kryonet.Connection;
import org.newdawn.slick.gui.MouseOverArea;

/**
 *
 *    
 */
public class InfoGame {
    
    String name;
    Connection connec;
    MouseOverArea bouton;
    
    public InfoGame(String n, Connection c) {
        name = n;
        connec = c;
    }
    
    public Connection getConnec() {
        return connec;
    }
    
    public String getName() {
        return name;
    }
    
    public void setBouton(MouseOverArea b) {
        bouton = b;
    }
    
    public MouseOverArea getBouton() {
        return bouton;
    }
    
    
}
