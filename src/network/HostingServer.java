/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import core.Main;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 *    
 */
public class HostingServer {
    
    Server s;
    
    public HostingServer() {
        s = new Server();
        /*
         * On register tous les types de message qu'on va envoyer ici
         */
        s.getKryo().register(ConnectionWantedMessage.class);
        s.getKryo().register(ConnectionClientMessage.class);
        s.start();
        try {
            s.bind(54500, 54700);
        } catch (IOException ex) {
            System.err.println("Impossible to create server");
            Logger.getLogger(HostingServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        s.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {

            }       
        });
    }
    
    public void close() {
        s.close();
    }
}
