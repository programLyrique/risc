/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.Main;
import core.Options;
import static gui.LobbyClient.id;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.ConnectionClientMessage;
import network.ConnectionWantedMessage;
import network.InfoGame;
import network.InformationGameMessage;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 *    
 */
public class choiceGame extends BasicGameState implements ComponentListener {

    static final int id = Main.GameState.CHOICE_GAME.ordinal();
    StateBasedGame states;
    
    List<InfoGame> choices = new ArrayList<InfoGame>();
    MouseOverArea[] liste = new MouseOverArea[20];
    Client client;
    
    Image imgMenu;
    private MouseOverArea reload;
    private MouseOverArea cancel;
    
    private Options options;
    
    private Listener localClientListener = new Listener() {
            @Override
            public void connected(Connection connection) {
                
            }
            
            @Override
            public void disconnected(Connection connection) {
                //fin de partie, hostvictory
            }
            
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof InformationGameMessage) {
                    InformationGameMessage i = (InformationGameMessage) object;
                    choices.add(new InfoGame(i.name, connection));
                }
            }
        };
    
    @Override
    public int getID() {
        return id;
    }
    
    public choiceGame(Client c, Options o) {
        client = c;
        options = o;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        states = sbg;
        
        imgMenu = new Image("res/img/startMenu.png");
        imgMenu = imgMenu.getScaledCopy(gc.getWidth() / 6, gc.getHeight() / 14);
        
        Image rel = new Image("res/img/reload.png");
        rel = rel.getScaledCopy(4);
        reload = new MouseOverArea(gc, rel, (int) (gc.getWidth() / 3), 10, this);
        reload.setNormalColor(Color.darkGray);
        reload.setMouseOverColor(Color.gray);
        
        for(int i = 0; i < 20; ++i) {
            liste[i] = new MouseOverArea(gc, imgMenu, 0, 0, this);
            liste[i].setNormalColor(Color.darkGray);
            liste[i].setMouseOverColor(Color.gray);
        }
        
        cancel = new MouseOverArea(gc, imgMenu, (int)(9 * gc.getHeight() / 10), (int)(5 * gc.getHeight() / 6), this);
        cancel.setNormalColor(Color.darkGray);
        cancel.setMouseOverColor(Color.gray);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        reload.render(gc, grphcs);
        cancel.render(gc, grphcs);
        grphcs.drawString("Cancel", (int)(9 * gc.getHeight() / 10) + 20, (int)((5 * gc.getHeight() / 6) + (gc.getHeight() / 28)));
        for(int i = 0; i < choices.size(); ++i) {
            liste[i].setLocation(gc.getWidth() / 3, 2 * gc.getHeight() / 14 + i * gc.getHeight() / choices.size());
            liste[i].render(gc, grphcs);
            choices.get(i).setBouton(liste[i]);
            grphcs.drawString(choices.get(i).getName(), gc.getWidth() / 3, 2 * gc.getHeight() / 14 + i * gc.getHeight() / choices.size());
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        
    }
    
    public void reload() {
        choices.clear();
        List<java.net.InetAddress> serverList = client.discoverHosts(54700, 1000);
        for(int i = 0; i < serverList.size();i++) {
            try {
                System.out.println(serverList.get(i));
                client.connect(500, serverList.get(i), 54500, 54700);
                ConnectionWantedMessage test = new ConnectionWantedMessage();
                client.sendUDP(test); 
            } catch (IOException ex) {
                Logger.getLogger(LobbyClient.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }

    @Override
    public void componentActivated(AbstractComponent ac) {
        if(ac == reload) {
            reload();
        }
        else if(ac == cancel) {
            states.enterState(Main.GameState.START_MENU.ordinal());
        }
        else {
            for(int i = 0; i < choices.size(); ++i) {
                if(ac == choices.get(i).getBouton()) {
                    ((LobbyClient) states.getState(Main.GameState.LOBBY_CLIENT.ordinal())).setHost(choices.get(i).getName());
                    ConnectionClientMessage m = new ConnectionClientMessage();
                    m.name = options.getName();
                    choices.get(i).getConnec().sendUDP(m);
                    states.enterState(Main.GameState.LOBBY_CLIENT.ordinal());
                }
            }
        }
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame s) {
        client.addListener(localClientListener);
    }
    
    @Override
    public void leave(GameContainer gc, StateBasedGame s) {
        client.removeListener(localClientListener);
    }
}
