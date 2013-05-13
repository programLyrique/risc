/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import core.Game;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *    
 */
public class ClientTimerFin implements Runnable {

    Game game;
    boolean  stop = false;
    
    public ClientTimerFin(Game g) {
        
        game = g;
    }
    
    public void arret() {
        stop = true;
    }
    
    @Override
    public void run() {
        
            
        int timeWaited = 0;
        while(!stop && timeWaited < 1000) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lag.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeWaited += 50;
        }
        if(stop)
            return;
        int compteur = 60;
        if(game == null)
            System.out.println("WTF fin?");
        game.setTimeLeft(compteur);
        game.waitForHost();
        while(compteur > 0&& !stop) {
            compteur--;
            for(int i = 0; (i < 10) && !stop; ++i) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Lag.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            game.setTimeLeft(compteur);
        }
    }
    
}
