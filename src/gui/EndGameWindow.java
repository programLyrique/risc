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
import org.newdawn.slick.Input;
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
public class EndGameWindow extends BasicGameState implements ComponentListener {

    static final int id = Main.GameState.END_GAME.ordinal();
    Client client;
    private MouseOverArea quit;
    
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
                
            }
        };
    
    StateBasedGame states;
    private int victory;
    
    public EndGameWindow(Client c) {
        client = c;
    }
    
    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        int menuYStep = 5 * gc.getHeight() / 6;
        int menuXStep2 = 9 * gc.getHeight() / 10;
        
        Image imgMenu = new Image("res/img/startMenu.png");
        imgMenu = imgMenu.getScaledCopy(gc.getWidth() / 6, gc.getHeight() / 14);
        
        quit = new MouseOverArea(gc, imgMenu, menuXStep2, menuYStep, this);
        quit.setNormalColor(Color.darkGray);
        quit.setMouseOverColor(Color.gray);
        
        states = sbg;
        
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        if(victory == 0) {
            grphcs.drawString("Defeat", 0, 0);
        }
        else if(victory == 1){
            grphcs.drawString("Victory", 0, 0);
        }
        else {
            grphcs.drawString("Connection problem", 0, 0);
        }    
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        
    }

    @Override
    public void componentActivated(AbstractComponent ac) {
        if(ac == quit) {
            states.enterState(Main.GameState.CHOICE_GAME.ordinal());
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
    
    public void setVictoryType(int i) {
        victory = i;
    }
    
    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_ESCAPE:
                states.enterState(Main.GameState.START_MENU.ordinal());
                break;
        }
    }
}