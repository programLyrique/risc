/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

/**
 *
 *    
 */
public class BeginningOfTurnMessage extends Message {
    
    public int turn;
    public BeginningOfTurnMessage() {
        super(6);
    }
}
