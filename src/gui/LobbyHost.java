/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import core.HostGame;
import core.Main;
import core.Options;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.BasicMap;
import network.ActionMessage;
import network.ConnectionClientMessage;
import network.ConnectionWantedMessage;
import network.EndOfTurnMessage;
import network.GameStartedMessage;
import network.HostingServer;
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
public class LobbyHost extends BasicGameState implements ComponentListener {

    static final int id = Main.GameState.LOBBY_HOST.ordinal();
    Server server;
    
    int menuXStep;
    int menuXStep2;
    int menuYStep;
    int menuTHeight;
    int menuDWidth;
    private MouseOverArea start;
    private MouseOverArea cancel;
    
    boolean readyToGo = false;
    String clientName = "";
    Connection connec;
    
    Image icon;
    StateBasedGame states;
    private Class mapClass = BasicMap.class;
    private Options options;
    
    private Listener localServerListener = new Listener() {
            @Override
            public void connected(Connection connection) {
                
            }
            
            @Override
            public void disconnected(Connection connection) {
                readyToGo = false;
                clientName = "";
            }
            
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ConnectionWantedMessage) {
                    System.out.println("Demande");
                    if(!readyToGo) {
                        InformationGameMessage i = new InformationGameMessage();
                        i.name = options.getName();
                        i.mapType = 0;
                        connection.sendUDP(i);
                    }
                }
                else if(object instanceof ConnectionClientMessage) {
                    clientName = ((ConnectionClientMessage) object).name;
                    connec = connection;
                    readyToGo = true;
                }
                //System.out.println("Recoit d'un message");
            }
        };
    
    public LobbyHost(Server s, Options o) {
        server = s;
        options = o;
    }
    
    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        states = sbg;
        
        server.addListener(localServerListener);
        
        menuYStep = 5 * gc.getHeight() / 6;
        Image imgMenu = new Image("res/img/startMenu.png");
        imgMenu = imgMenu.getScaledCopy(gc.getWidth() / 6, gc.getHeight() / 14);
        menuXStep = 6 * gc.getHeight() / 10;
        start = new MouseOverArea(gc, imgMenu, menuXStep, menuYStep, this);
        start.setNormalColor(Color.darkGray);
        start.setMouseOverColor(Color.gray);
        
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
        int textSPos = menuXStep + menuDWidth;
        if(readyToGo) {
            start.render(gc, grphcs);
            grphcs.drawString("Start", textSPos , menuYStep + menuTHeight );
        }
        textSPos = menuXStep2 + menuDWidth;
        cancel.render(gc, grphcs);
        grphcs.drawString("Cancel", textSPos , menuYStep + menuTHeight );
        
        //icon.drawCentered(gc.getWidth() / 3, gc.getHeight() / 4);
        grphcs.drawString(options.getName(), gc.getWidth() / 3, gc.getHeight() / 4);
        //icon.drawCentered(gc.getWidth() / 3, gc.getHeight() / 2);
        grphcs.drawString(clientName, gc.getWidth() / 3, gc.getHeight() / 2);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        
    }

    @Override
    public void componentActivated(AbstractComponent ac) {
        if(ac == cancel) {
            server.close();
            states.enterState(Main.GameState.START_MENU.ordinal());
        }
        else if(ac == start && readyToGo) {
            ((HostGame) states.getState(Main.GameState.HOST_GAME.ordinal())).setMap(null);
            connec.sendUDP(new GameStartedMessage());
            states.enterState(Main.GameState.HOST_GAME.ordinal());
        }
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame s) {
        server.addListener(localServerListener);
    }
    
    @Override
    public void leave(GameContainer gc, StateBasedGame s) {
        server.removeListener(localServerListener);
    }
}