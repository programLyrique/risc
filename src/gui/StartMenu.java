package gui;

import core.HostGame;
import core.Main;
import core.Options;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Map;
import network.HostingServer;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.*;

/**
 * The start menu of the game
 *
 * - Rejoindre partie - CrÃ©er partie - Options - Quitter
 *
 *   pierre
 */
public class StartMenu extends BasicGameState implements ComponentListener {

    static final int id = Main.GameState.START_MENU.ordinal();
    private StateBasedGame game;
    private GameContainer container;
    private MouseOverArea joinGame;
    private MouseOverArea createGame;
    private MouseOverArea options;
    private MouseOverArea quit;
    int w = 0;
    int h = 0;
    int menuStartPos = 0;
    int menuYStep = 0;
    int menuDWidth = 0;
    int menuTHeight = 0;
    private Image background;
    private Music music;
    private Options opts;

    public StartMenu(Options o) {
        opts = o;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        h = gc.getHeight();
        w = gc.getWidth();
        
        try
        {
            gc.setMouseCursor("res/img/cursor.png", 16, 16);
        }
        catch(java.lang.Exception e)
        {
            System.err.println("Curseur non géré : " + e.getMessage());
        }
        
        
        // Informations utiles pour le débogage
        System.err.println("Max size single image : " + Image.getMaxSingleImageSize());
        
        
        Image imgMenu = new Image("res/img/startMenu.png");
        imgMenu = imgMenu.getScaledCopy(gc.getWidth() / 3, gc.getHeight() / 7);
        
        music = new Music("res/music/ambiance.ogg", true);
        if(opts.isMusic())
        {
            music.loop();
            music.setVolume(opts.getMusicVolume()); 
        }
        
        
        menuDWidth = imgMenu.getWidth() / 2;
        menuTHeight = imgMenu.getHeight() / 2;
        menuStartPos = w/ 2 - menuDWidth ;
        menuYStep = h / 6;


        background = new Image("res/img/risc.png");
        background = background.getScaledCopy(gc.getWidth(), gc.getHeight());
        //background.setImageColor(0.5f, 0.5f, 0.5f);

        joinGame = new MouseOverArea(gc, imgMenu, menuStartPos, menuYStep, this);
        joinGame.setNormalColor(Color.darkGray);
        //joinGame.setMouseOverSound(sndMenu);
        joinGame.setMouseOverColor(Color.gray);

        createGame = new MouseOverArea(gc, imgMenu, menuStartPos, 2 * menuYStep, this);
        createGame.setNormalColor(Color.darkGray);
        //createGame.setMouseOverSound(sndMenu);
        createGame.setMouseOverColor(Color.gray);

        options = new MouseOverArea(gc, imgMenu, menuStartPos, 3 * menuYStep, this);
        options.setNormalColor(Color.darkGray);
        //options.setMouseOverSound(sndMenu);
        options.setMouseOverColor(Color.gray);

        quit = new MouseOverArea(gc, imgMenu, menuStartPos, 4 * menuYStep, this);
        quit.setNormalColor(Color.darkGray);
        //quit.setMouseOverSound(sndMenu);
        quit.setMouseOverColor(Color.gray);

        this.container = gc;
        this.game = sbg;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gr)
            throws SlickException {

        background.drawCentered(w / 2, h / 2);

        joinGame.render(gc, gr);

        int textSPos = menuStartPos + (int) ((double) menuDWidth * 0.7);

        gr.setColor(new Color(230, 230, 230));
        gr.drawString("Rejoindre partie", textSPos, menuYStep + menuTHeight);
        createGame.render(gc, gr);
        gr.drawString("Créer partie", textSPos, 2 * menuYStep + menuTHeight);
        options.render(gc, gr);
        gr.drawString("Options", textSPos, 3 * menuYStep + menuTHeight);
        quit.render(gc, gr);
        gr.drawString("Quitter", textSPos, 4 * menuYStep + menuTHeight);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
    }

    @Override
    public void componentActivated(AbstractComponent source) {
        if (source == quit) {
            container.exit();// Ca quitte, mais en lanÃ§ant une exception...
        } else if (source == joinGame) {
            game.enterState(Main.GameState.CHOICE_GAME.ordinal());
        } else if (source == createGame) {
            try {
                Main.server.bind(54500, 54700);
            } catch (IOException ex) {
                System.err.println("Impossible to create server");
                Logger.getLogger(HostingServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            game.enterState(Main.GameState.LOBBY_HOST.ordinal());
        }
    }
    
    @Override
   public void leave(GameContainer arg0, StateBasedGame arg1)
         throws SlickException {
       
   }
    
    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_F1:
                ((HostGame) game.getState(Main.GameState.HOST_GAME.ordinal())).setMap(null);
                ((HostGame) game.getState(Main.GameState.HOST_GAME.ordinal())).activateOfflineMode();
                game.enterState(Main.GameState.HOST_GAME.ordinal());
            break;
        }
    }
}
