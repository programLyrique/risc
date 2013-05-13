/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import core.Game;
import core.Main;
import core.Options;
import static gui.LobbyHost.id;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.BasicMap;
import map.Map;
import map.MiniMap;
import network.ConnectionClientMessage;
import network.ConnectionWantedMessage;
import network.GameStartedMessage;
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
public class LobbyClient extends BasicGameState implements ComponentListener {

    static final int id = Main.GameState.LOBBY_CLIENT.ordinal();
    Client client;
    
    int menuXStep;
    int menuXStep2;
    int menuYStep;
    int menuTHeight;
    int menuDWidth;
    private MouseOverArea cancel;
    
    String hostName = "";
    
    private Options options;
    
    private Listener localClientListener = new Listener() {
            @Override
            public void connected(Connection connection) {
                
            }
            
            @Override
            public void disconnected(Connection connection) {
                states.enterState(Main.GameState.CHOICE_GAME.ordinal());
            }
            
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof InformationGameMessage) {
                    System.out.println("ConnectionClient ou pas :( ");
                    hostName = ((InformationGameMessage) object).name;
                    ((Game) states.getState(Main.GameState.GAME.ordinal())).setMap(BasicMap.class);
                }
                else if(object instanceof GameStartedMessage) {
                    states.enterState(Main.GameState.GAME.ordinal());
                    connection.sendUDP(new GameStartedMessage());
                }
            }
        };
    
    Image icon;
    StateBasedGame states;
    
    public LobbyClient(Client c, Options o) {
        client = c;
        options = o;
    }
    
    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        states = sbg;
        
        menuYStep = 5 * gc.getHeight() / 6;
        Image imgMenu = new Image("res/img/startMenu.png");
        imgMenu = imgMenu.getScaledCopy(gc.getWidth() / 6, gc.getHeight() / 14);
        menuXStep = 6 * gc.getHeight() / 10;
        menuXStep2 = 9 * gc.getHeight() / 10;
        cancel = new MouseOverArea(gc, imgMenu, menuXStep2, menuYStep, this);
        cancel.setNormalColor(Color.darkGray);
        cancel.setMouseOverColor(Color.gray);
        
        menuTHeight = (int)(0.5 * imgMenu.getHeight());
        menuDWidth = (int)(0.3 * imgMenu.getWidth());
        
        icon = new Image("res/img/startMenu.png");       
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        int textSPos = menuXStep2 + menuDWidth;
        cancel.render(gc, grphcs);
        grphcs.drawString("Cancel", textSPos , menuYStep + menuTHeight );
        
        grphcs.drawString(hostName, gc.getWidth() / 3, gc.getHeight() / 4);
        
        grphcs.drawString(options.getName(), gc.getWidth() / 3, gc.getHeight() / 2);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        
    }

    @Override
    public void componentActivated(AbstractComponent ac) {
        if(ac == cancel) {
            client.close();
            states.enterState(Main.GameState.CHOICE_GAME.ordinal());
        }
    }
    
    public void setHost(String h) {
        hostName = h;
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