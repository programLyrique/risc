package core;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import gui.EndGameWindow;
import gui.LobbyClient;
import gui.LobbyHost;
import gui.StartMenu;
import gui.choiceGame;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.ActionMessage;
import network.BeginningOfTurnMessage;
import network.ChatMessage;
import network.ConnectionClientMessage;
import network.ConnectionWantedMessage;
import network.EndOfGameMessage;
import network.EndOfTurnMessage;
import network.GameStartedMessage;
import network.InformationGameMessage;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.state.*;

/**
 * @brief Classe principale POur l'instant, hérite de BasicGame pour tester.
 *   pierre
 */
final public class Main extends StateBasedGame {
    Options options;
    
    public static Server server = new Server();
    public static Client client = new Client();
    
    public enum GameState {

        START_MENU,
        OPTION,
        CHOICE_GAME,
        LOBBY_CLIENT,
        LOBBY_HOST,
        GAME,
        HOST_GAME,
        GAME_COMPLETED,
        NEW_GAME,
        JOIN_GAME,
        END_GAME
    };
    
    public Main(Options options) {
        super("Au coeur du RISC");
        this.options = options;
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        addState(new StartMenu(options));
        addState(new choiceGame(client, options));
        addState(new LobbyClient(client, options));
        addState(new LobbyHost(server, options));
        addState(new Game(client, options));
        addState(new HostGame(server, options));
        addState(new EndGameWindow(client));
    }

    /**
     * Méthode main.
     *
     * @throws SlickException
     */
    public static void main(String[] args) throws SlickException {
        
        server.getKryo().register(ConnectionClientMessage.class);
        client.getKryo().register(ConnectionClientMessage.class);
        server.getKryo().register(ConnectionWantedMessage.class);
        client.getKryo().register(ConnectionWantedMessage.class);
        server.getKryo().register(InformationGameMessage.class);
        client.getKryo().register(InformationGameMessage.class);
        server.getKryo().register(GameStartedMessage.class);
        client.getKryo().register(GameStartedMessage.class);
        server.getKryo().register(BeginningOfTurnMessage.class);
        client.getKryo().register(BeginningOfTurnMessage.class);
        server.getKryo().register(EndOfTurnMessage.class);
        client.getKryo().register(EndOfTurnMessage.class);
        server.getKryo().register(ChatMessage.class);
        client.getKryo().register(ChatMessage.class);
        server.getKryo().register(ActionMessage.class);
        client.getKryo().register(ActionMessage.class);
        server.getKryo().register(Point.class);
        client.getKryo().register(Point.class);
        server.getKryo().register(EndOfGameMessage.class);
        client.getKryo().register(EndOfGameMessage.class);
        
        server.start();
        client.start();
        
        System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "lib/lwjgl-2.8.5/native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
        
        Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
        
        //Chargement des options du jeu
        Options options = new Options("res/options.properties");
        
        AppGameContainer app = new AppGameContainer(new Main(options));
        
        //app.setDisplayMode(1024, 600, false);
        
        boolean fullscreen = options.isFullscreen();
        if(options.isDimScreen())
        {
                  app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), fullscreen);  
        }
        else
        {
            app.setDisplayMode(options.getDimX(), options.getDimY(), fullscreen);
        }
        // Pour avoir une gestion temporelle fine, malgré la différence de Fps d'une 
        // machine à une autre
        app.setMinimumLogicUpdateInterval(20); // 20 millisecondes entre deux updates au minimum
        app.setMaximumLogicUpdateInterval(30);//30 millisecondes au plus
        
        app.setUpdateOnlyWhenVisible(false);
        
        app.setSmoothDeltas(true);//Pour que l'interpolation ne soit pas tremblotante
        
        //app.setTargetFrameRate(100);//Ca permet d'économiser du temps processeur qui serait gâché sinon
        app.setVSync(true);//Idem, mais même en synchro avec l'écran !
        app.setVerbose(true);
        app.start();
        app.setDefaultMouseCursor();
        
        try
        {
            //Fin, donc on sauvegarde les options
            options.save();
        } catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Impossible de sauvegarder les options", ex);
        }
    }
}
