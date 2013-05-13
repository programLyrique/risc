/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import buildings.Antivirus;
import buildings.AttackAnti;
import buildings.Building;
import buildings.Condensateur;
import buildings.Kernel;
import buildings.Scan;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import gui.CommandPanel;
import gui.EndGameWindow;
import gui.actions.BitActions;
import gui.actions.BombeActions;
import gui.actions.KernelActions;
import gui.actions.PluginActions;
import gui.actions.ScanActions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.BasicMap;
import map.EllerMap;
import map.Map;
import map.MapGenerator;
import map.MiniMap;
import map.Move;
import network.ActionMessage;
import network.BeginningOfTurnMessage;
import network.ChatMessage;
import network.ConnectionClientMessage;
import network.ConnectionWantedMessage;
import network.EndOfGameMessage;
import network.EndOfTurnMessage;
import network.GameStartedMessage;
import network.HostingServer;
import network.Lag;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.*;
import units.Bit;
import units.BombeLogique;
import units.Unit;

/**
 * Game state.
 *
 *   pierre
 */
public class HostGame extends BasicGameState {
    // Uniquement pour prendre en main la bibliothèque Slick.

    private boolean offlineMode = false;
    
    Server s;
    Connection connec;
    boolean connected = false;
    boolean gameStarted = false;
    // Buffer d'action, composé chaque action son tour.
    int compteurTour = 0;
    private boolean waitingPlayer = false;
    private int timeLeft;
    private Lag lagTimer;
    Object lock = new Object();
    private static final int id = Main.GameState.HOST_GAME.ordinal();
    private Map map;
    private MiniMap minimap;
    private StateBasedGame states;
    private GameContainer container;
    private SpriteSheet sheet;
    //Position de l'écran par rapport à la carte (en tiles).
    private int mapX;
    private int mapY;
    //Limite basse de la map sur l'écran en pixel
    private int mapLimit;
    // Les mouvements prévus
    private ArrayList<Move> moves;
    //Bit pour tester
    //private Bit bit;
    //Position de la minimap sur l'écran.
    private int miniX;
    private int miniY;
    //Modes de jeu
    private boolean debugPathfinding = false;
    //Constante représentant les constantes des unites
    private Ressources res;
    //Les deux joueurs
    private Player player1;
    private Player player2;//Le garder vraiment sur cet instance du jeu ? Pas sûr que ce soit utile
    //Position de la souris en pixels
    // Juste pour l'affichage de cette position en mode debug
    private int mX;
    private int mY;
    //Sélection
    private boolean enterSelection;
    private int selX;
    private int selY;
    private int selX2;
    private int selY2;
    // Le panneau de commandes
    private CommandPanel pannel;
    //Différents timers
    private Timer timerEnergy;//Pour l'énergie : 1 secondes
    private Timer timerMove;//Pour les mouvements : 1/60 de secondes
    private boolean enterSelectionMap;
    private float lissage;
    private Image panneauCentral;
    
    public Ressources getRes() {
        return res;
    }
    private TextField chat;
    private String previousMessage = "";
    private Listener localServerListener = new Listener() {
        @Override
        public void connected(Connection connection) {
            connected = true;
        }

        @Override
        public void disconnected(Connection connection) {
            /*((EndGameWindow) states.getState(Main.GameState.END_GAME.ordinal())).setVictoryType(2);
            if (gameStarted) {
                lagTimer.arret();
                states.enterState(Main.GameState.END_GAME.ordinal());
            }*/
        }

        @Override
        public void received(Connection connection, Object object) {
            connec = connection;
            //System.out.println("Message recu");
            if (object instanceof GameStartedMessage) {
                System.out.println("Debut de la partie");
                gameStartedFunction();
            } else if (object instanceof ActionMessage) {
                System.out.println("ActionMessage");
                actions.add((ActionMessage) object);
            } else if (object instanceof ChatMessage) {
                previousMessage = ((ChatMessage) object).name + ": " + ((ChatMessage) object).message;
            } else if (object instanceof EndOfTurnMessage) {
                System.out.println("Fin/Debut de tour");
                endOfTurnFunction();
            } else if (object instanceof EndOfGameMessage) {
                ((EndGameWindow) states.getState(Main.GameState.END_GAME.ordinal())).setVictoryType(1);
                states.enterState(Main.GameState.END_GAME.ordinal());
            }
        }
    };
    //Liste des actions à effectuer au tour suivant
    private ArrayList<ActionMessage> actions;
    private Options options;

    public HostGame(Server server, Options o) {
        s = server;
        options = o;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        states = game;
        mapX = 0;
        mapY = 0;

        moves = new ArrayList<>(40);
//        enemyMoves = new ArrayList<>(40);

        Input input = gc.getInput();
        input.enableKeyRepeat();
        sheet = new SpriteSheet("res/img/spritesheet.png", 32, 32);
        actions = new ArrayList<>();
    }

    /**
     * Recentrer la vue sur le joueur
     */
    private void setView() {
        //Vue initialisé sur le player 1
        if (player1.getLocation().x < 20) {
            mapX = 0;
            mapY = 0;
        } else {
            mapX = map.getWidth() - container.getWidth() / Map.getTileLenght();
            mapY = map.getHeight() - mapLimit / Map.getTileLenght();
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        Input input = gc.getInput();

        //Scroll de la map
        //On touche un des côtés
        mX = input.getMouseX();
        mY = input.getMouseY();

        //Déplacements des unités
        //Pas même intervalle de temps pour toutes les actions.
        player1.update(delta);
        player2.update(delta);


        //Supprimer les unités et bâtiments à 0 points de vie
        removeDestroyed();

        if (player1.getBuildings().isEmpty()) {
            EndOfGameMessage m = new EndOfGameMessage();
            if(connec != null)
                connec.sendUDP(m);
            ((EndGameWindow) states.getState(Main.GameState.END_GAME.ordinal())).setVictoryType(0);
            states.enterState(Main.GameState.END_GAME.ordinal());
        }

        if (enterSelectionMap && mY < mapLimit) {
            selX2 = mX;
            selY2 = mY;
        } else if (enterSelectionMap) {
            selX2 = mX;
            selY2 = mapLimit;
        }


        if (mX == 0 && mapX > 0) {
            mapX--;
        } else if (mX == container.getWidth() - 1 && mapX < map.getWidth() - gc.getWidth() / Map.getTileLenght()) {
            mapX++;
        } else if (mY == 0 && mapY > 0) {
            mapY--;
        } else if (mY == container.getHeight() - 1 && mapY < map.getHeight() - (gc.getHeight() - minimap.getHeight()) / Map.getTileLenght()) {
            mapY++;
        }
        player1.seen_maj(map);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (player1.seen[x][y]) {
                    map.unsetWarFog(x, y);
                    if (map.isBlackout(x, y)) {
                        map.unsetBlackout(x, y);
                    }
                } else {
                    map.setWarFog(x, y);
                }
            }
        }
        map.update(gc, delta, player1);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        if (!waitingPlayer) {
            //Terrain de jeu
            sheet.startUse();
            map.render(gc, g, mapX, mapY, gc.getWidth() / Map.getTileLenght(), mapLimit / Map.getTileLenght());
            sheet.endUse();
            /*
            * On est obligé de séparer même si c'est sur la même spritesheet à cause de bugs graphiques.
            * Un problème d'ordonnancement de l'affichage par slick2D.
            */
           sheet.startUse();
           map.renderUnits(gc, g, mapX, mapY, gc.getWidth() / Map.getTileLenght(), mapLimit / Map.getTileLenght()/*, lissage*/);
           sheet.endUse();

           //Affichage des barres de vie etc pour identifier les unités
           map.renderPlayers(gc, g, mapX, mapY, gc.getWidth() / Map.getTileLenght(), mapLimit / Map.getTileLenght());

           //Affichage de la sélection
           if (enterSelectionMap) {
               g.setColor(Color.green);
               g.drawRect(selX, selY, selX2 - selX, selY2 - selY);
           }

           //Affichage debug
           if (debugPathfinding) {
               //System.err.println("Mode debug du pathfinding on/off");
               for (Move move : moves) {
                   move.drawMove(g, mapX, mapY);
               }
           }


           // Affichage de la gui
           panneauCentral.draw(gc.getWidth() / 4, 3 * gc.getHeight() / 4, gc.getWidth() /2, gc.getHeight() / 4);
           //Minimap

           minimap.render(gc, g, mapX, mapY, miniX, miniY);

           //Panneau de commande
           pannel.render(gc, g);
           
           g.setColor(Color.white);


            //Pour tester
            //g.drawString("Position : " + (mapX + mX / Map.getTileLenght()) + "," + (mapY + mY / Map.getTileLenght()), 10, 20);

            //Ici pour l'instant, mais plus tard, dans les panneaux du bas
            g.drawString("Energie : " + player1.getEnergy(), 10, 20);
            g.drawString("Chaleur : " + player1.getWarm() + "/" + player1.getMaxWarm(), 10, 35);
            chat.render(container, g);
            g.drawString(previousMessage, gc.getWidth() / 4, 3 * gc.getHeight() / 4 - 40);
            g.drawString(chat.getText(), gc.getWidth() / 4, 3 * gc.getHeight() / 4);
        } else {
            g.drawString("Ca lag: Il reste " + timeLeft + "restant avant defwin", gc.getWidth() / 4, 3 * gc.getHeight() / 4 - 40);
        }

    }

    public synchronized void endTurn() {
        //System.out.println("End of turn");
        EndOfTurnMessage m = new EndOfTurnMessage();
        m.turn = compteurTour;
        if(connec != null)
            connec.sendUDP(m);

        //vidage des actions
        for (ActionMessage a : actions) {
            //action de selection
            if (a.type == -1) {
                if (a.player == 1) {
                    player1.select(a.selX, a.selY, a.selWidth, a.selHeight);
                } else {
                    player2.select(a.selX, a.selY, a.selWidth, a.selHeight);
                }
            } //Correspond à un changement de rallypoint ou un déplacement
            else if (a.type == 0) {
                if (a.player == 1) {
                    moveAndAttack(a.goal, player1);
                } else {
                    moveAndAttack(a.goal, player2);
                }
            } else if (a.unitOrBuilding) {
                //Construction d'un kernel par un bit
                if (a.type == 1) {
                    ArrayList<Unit> selectedUnits;
                    if (a.player == 1) {
                        selectedUnits = player1.getSelectedUnits();
                    } else {
                        selectedUnits = player2.getSelectedUnits();
                    }
                    if(selectedUnits != null)
                        if (!selectedUnits.isEmpty() && selectedUnits.get(0) instanceof Bit) {
                            Bit bit = (Bit) selectedUnits.get(0);
                            try {
                                Point p = new Point(bit.getLocation().x, bit.getLocation().y);
                                bit.new_kernel(p, map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création du kernel par le bit", ex);
                            }
                        }
                } //Construction d'un scan par un bit
                else if (a.type == 2) {
                    ArrayList<Unit> selectedUnits;
                    if (a.player == 1) {
                        selectedUnits = player1.getSelectedUnits();
                    } else {
                        selectedUnits = player2.getSelectedUnits();
                    }
                    if(selectedUnits != null)
                        if (!selectedUnits.isEmpty() && selectedUnits.get(0) instanceof Bit) {
                            Bit bit = (Bit) selectedUnits.get(0);
                            try {
                                Point p = new Point(bit.getLocation().x, bit.getLocation().y);
                                bit.new_scan(p, map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création du scan par le bit", ex);
                            }
                        }
                } //Construction d'un plugin par un bit
                else if (a.type == 3) {
                    ArrayList<Unit> selectedUnits;
                    if (a.player == 1) {
                        selectedUnits = player1.getSelectedUnits();
                    } else {
                        selectedUnits = player2.getSelectedUnits();
                    }
                    if(selectedUnits != null)
                        if (!selectedUnits.isEmpty() && selectedUnits.get(0) instanceof Bit) {
                            Bit bit = (Bit) selectedUnits.get(0);
                            try {
                                Point p = new Point(bit.getLocation().x, bit.getLocation().y);
                                bit.new_plugin(p, map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création du plugin par le bit", ex);
                            }
                        }
                } //Construction d'un event par un bit
                else if (a.type == 4) {
                    ArrayList<Unit> selectedUnits;
                    if (a.player == 1) {
                        selectedUnits = player1.getSelectedUnits();
                    } else {
                        selectedUnits = player2.getSelectedUnits();
                    }
                    if(selectedUnits != null)
                        if (!selectedUnits.isEmpty() && selectedUnits.get(0) instanceof Bit) {
                            Bit bit = (Bit) selectedUnits.get(0);
                            try {
                                Point p = new Point(bit.getLocation().x, bit.getLocation().y);
                                bit.new_event_aeration(p, map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création de l'event par le bit", ex);
                            }
                        }
                } //Construction d'un condensateur par un bit
                else if (a.type == 5) {
                    ArrayList<Unit> selectedUnits;
                    if (a.player == 1) {
                        selectedUnits = player1.getSelectedUnits();
                    } else {
                        selectedUnits = player2.getSelectedUnits();
                    }
                    if(selectedUnits != null)
                        if (!selectedUnits.isEmpty() && selectedUnits.get(0) instanceof Bit) {
                            Bit bit = (Bit) selectedUnits.get(0);
                            try {
                                Point p = new Point(bit.getLocation().x, bit.getLocation().y);
                                bit.new_condensateur(p, map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création du condensateur par le bit", ex);
                            }
                        }
                } //Explosion de la bombe logique
                else if (a.type == 6) {
                    ArrayList<Unit> selectedUnits;
                    if (a.player == 1) {
                        selectedUnits = player1.getSelectedUnits();
                    } else {
                        selectedUnits = player2.getSelectedUnits();
                    }
                    if(selectedUnits != null)
                        if (!selectedUnits.isEmpty() && selectedUnits.get(0) instanceof BombeLogique) {
                            BombeLogique bombe = (BombeLogique) selectedUnits.get(0);
                            try {
                                Point p = new Point(bombe.getLocation().x, bombe.getLocation().y);
                                bombe.explose(p, map);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à l'explosion de la bombe", ex);
                            }
                        }
                }
            } else {
                //Crée un bit
                if (a.type == 1) {
                    ArrayList<Building> selectedBuildings;
                    if (a.player == 1) {
                        selectedBuildings = player1.getSelectedBuildings();
                    } else {
                        selectedBuildings = player2.getSelectedBuildings();
                    }
                    if(selectedBuildings != null)
                        if (!selectedBuildings.isEmpty() && selectedBuildings.get(0) instanceof Kernel) {
                            Kernel kern = (Kernel) selectedBuildings.get(0);
                            try {
                                kern.new_bit(map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création de l'unité dans le Kernel", ex);
                            }

                        }
                } else if (a.type == 2) {
                    ArrayList<Building> selectedBuildings;
                    if (a.player == 1) {
                        selectedBuildings = player1.getSelectedBuildings();
                    } else {
                        selectedBuildings = player2.getSelectedBuildings();
                    }
                    if(selectedBuildings != null)
                        if (!selectedBuildings.isEmpty() && selectedBuildings.get(0) instanceof Kernel) {
                            Kernel kern = (Kernel) selectedBuildings.get(0);
                            try {
                                kern.new_octet(map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création de l'unité dans le Kernel", ex);
                            }

                        }
                } else if (a.type == 3) {
                    ArrayList<Building> selectedBuildings;
                    if (a.player == 1) {
                        selectedBuildings = player1.getSelectedBuildings();
                    } else {
                        selectedBuildings = player2.getSelectedBuildings();
                    }
                    if(selectedBuildings != null)
                        if (!selectedBuildings.isEmpty() && selectedBuildings.get(0) instanceof Kernel) {
                            Kernel kern = (Kernel) selectedBuildings.get(0);
                            try {
                                kern.new_mult(map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création de l'unité dans le Kernel", ex);
                            }

                        }
                } else if (a.type == 4) {
                    ArrayList<Building> selectedBuildings;
                    if (a.player == 1) {
                        selectedBuildings = player1.getSelectedBuildings();
                    } else {
                        selectedBuildings = player2.getSelectedBuildings();
                    }
                    if(selectedBuildings != null)
                        if (!selectedBuildings.isEmpty() && selectedBuildings.get(0) instanceof Kernel) {
                            Kernel kern = (Kernel) selectedBuildings.get(0);
                            try {
                                kern.new_bombe(map, res);
                            } catch (SlickException ex) {
                                Logger.getLogger(KernelActions.class.getName()).log(Level.SEVERE,
                                        "Erreur à la création de l'unité dans le Kernel", ex);
                            }

                        }
                } //gestion du scan
                else if (a.type == 5) {
                    ArrayList<Building> selectedBuildings;
                    if (a.player == 1) {
                        selectedBuildings = player1.getSelectedBuildings();
                    } else {
                        selectedBuildings = player2.getSelectedBuildings();
                    }
                    for (Building building : selectedBuildings) {
                        try {
                            if (building instanceof Scan) {
                                Scan scan = (Scan) building;
                                scan.morph(res, map);
                            }
                        } catch (SlickException ex) {
                            Logger.getLogger(ScanActions.class.getName()).log(Level.SEVERE,
                                    "Impossible d'améliorer le scan", ex);
                        }
                    }
                } //gestion du plugin
                else if (a.type == 6) {
                }
            }
        }
        actions.clear();
        
        if(lagTimer != null)
            lagTimer.arret();
        lagTimer = new Lag(this);
        new Thread(lagTimer).start();
        compteurTour = (compteurTour + 1)%1000000000;
        
        if(offlineMode) {
            endOfTurnFunction();
        }
    }

    public void startTurn() {
        BeginningOfTurnMessage m = new BeginningOfTurnMessage();
        m.turn = compteurTour;
        if(connec != null)
            connec.sendUDP(m);
    }

    public synchronized void waitForPlayer() {
        synchronized (lock) {
            waitingPlayer = true;
        }
    }

    public void setTimeLeft(int t) {
        timeLeft = t;
    }

    public void setMap(Class type) {
    }

    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_ESCAPE:
                states.enterState(Main.GameState.START_MENU.ordinal());
                break;

            //Scroll clavier
            case Input.KEY_UP:
                if (mapY > 0) {
                    mapY--;
                }
                break;

            case Input.KEY_DOWN:
                if (mapY < map.getHeight() - (container.getHeight() - minimap.getHeight()) / Map.getTileLenght()) {
                    mapY++;
                }
                break;

            case Input.KEY_LEFT:
                if (mapX > 0) {
                    mapX--;
                }
                break;

            case Input.KEY_RIGHT:
                if (mapX < map.getWidth() - container.getWidth() / Map.getTileLenght()) {
                    mapX++;
                }
                break;

            case Input.KEY_F1:
                debugPathfinding = !debugPathfinding;
                break;

            case Input.KEY_F2:
                minimap.setDisplayPath(!minimap.isDisplayedPath());
                break;

            default:
                //Revenir au camp de base
                if (c == 'h') {
                    setView();
                }

                break;
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        //Clic dans la minicarte
        // @todo gérer la cas où le clic fait que la fenêtre devrait dépasser de la carte
        if (x > miniX && y > miniY) {

            int newX = x - miniX;
            int newY = y - miniY;

            int borneX = map.getWidth() - container.getWidth() / Map.getTileLenght();
            int borneY = map.getHeight() - mapLimit / Map.getTileLenght();
            if (button == 0) {
                if ((int) (newX * minimap.getMiniX()) < borneX) {
                    mapX = (int) (newX * minimap.getMiniX());
                } else {
                    mapX = borneX;
                }
                if ((int) (newY * minimap.getMiniY()) < borneY) {

                    mapY = (int) (newY * minimap.getMiniY());
                } else {
                    mapY = borneY;
                }
            } else if (button == 1) {
                Point goal = new Point((int) (newX * minimap.getMiniX()), (int) (newY * minimap.getMiniY()));
                ActionMessage m = new ActionMessage();
                m.unitOrBuilding = true;
                m.goal = goal;
                m.type = 0;
                m.player = 1;
                sendActionMessage(m);
            }
        } //Clic dans la zone de jeu
        else if (y < mapLimit) {
            // Clic droit pour déplacer une unité sélectionnée
            // Ou mettre un point de rassemblement pour un bâtiment
            // Priorité : déplacement d'unités
            if (button == 1) {
                Point goal = new Point(mapX + x / Map.getTileLenght(), mapY + y / Map.getTileLenght());
                //if (!map.isBlocked(goal.x, goal.y)) {
                //On récupère la sélection
                    /*
                 ArrayList<Unit> selectedUnits = player1.getSelectedUnits();


                 for (Unit unit : selectedUnits) {
                 //On supprime le mouvement précédent.
                 moves.remove(unit.getMove());
                 unit.getAttack().stop();
                 unit.getBuild().stop();
                 Move move = new Move(map, unit, goal);
                 moves.add(move);
                 unit.setMove(move);
                 }
                 ArrayList<Building> selectedBuildings = player1.getSelectedBuildings();

                 //Que des bâtiments de sélectionnés
                 if (selectedUnits.isEmpty()) {
                 for (Building building : selectedBuildings) {
                 building.setRalliement(goal);
                 }
                 }
                 */
                ActionMessage m = new ActionMessage();
                m.unitOrBuilding = true;
                m.goal = goal;
                m.type = 0;
                m.player = 1;
                sendActionMessage(m);
                //}

            } //Clic gauche pour sélectionner une unité, un bâtiment...
            else if (button == 0) {
                enterSelectionMap = true;
                pannel.clearView();//On a déselectionné ou on est en train de sélectionner.
                selX = x;
                selY = y;
                selX2 = x;
                selY2 = y;
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        //System.err.println("Mouse release " + x + "," + y);
        if (button == 0 && enterSelectionMap) {
            enterSelectionMap = false;
            int x1 = mapX + Math.min(selX, mX) / Map.getTileLenght();
            int y1 = mapY + Math.min(selY, mY) / Map.getTileLenght();
            int width = 1 + Math.abs(selX - mX) / Map.getTileLenght();
            int height = 1 + Math.abs(selY - mY) / Map.getTileLenght();

            player1.select(x1, y1, width, height);
            ActionMessage m = new ActionMessage();
            m.unitOrBuilding = true;
            m.type = -1;
            m.player = 1;
            m.selX = x1;
            m.selY = y1;
            m.selWidth = width;
            m.selHeight = height;
            sendActionMessage(m);

            /* Actions à faire : 
             * - On modifie le panneau de commande
             * - on joue les sons de sélection
             * Pour l'instant, assez simpliste, on prend celui du premier élément sélectionné
             * Bâtiment en priorité
             */
            ArrayList<Building> selectedBuildings = player1.getSelectedBuildings();
            if (selectedBuildings != null && !selectedBuildings.isEmpty()) {
                pannel.setView(selectedBuildings.get(0));
                selectedBuildings.get(0).getSelected_snd().play();;
            } else {
                ArrayList<Unit> selectedUnits = player1.getSelectedUnits();
                if (!selectedUnits.isEmpty() && selectedUnits != null
                        ) {
                    pannel.setView(selectedUnits.get(0));
                    selectedUnits.get(0).getSelected_snd().play();
                }
            }
        }
    }

    public void setConnection(Connection c) {
        connec = c;
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame state)
            throws SlickException {
        s.addListener(localServerListener);
        chat = new TextField(gc, new UnicodeFont(java.awt.Font.decode("Times")), gc.getWidth() / 4, 3 * gc.getHeight() / 4, gc.getWidth() / 2, 30, new ComponentListener() {
            @Override
            public void componentActivated(AbstractComponent ac) {
                ChatMessage m = new ChatMessage();
                m.message = chat.getText();
                m.name = options.getName();
                if(connec != null)
                connec.sendTCP(m);
                previousMessage = options.getName() + " (Me): " + chat.getText();
                chat.setText("");
            }
        });
        chat.setMaxLength(75);

        //Chargement de la spritesheet

        sheet = new SpriteSheet("res/img/spritesheet.png", 32, 32);

        //Chargement des constantes
        res = new Ressources(sheet);


        System.out.println("Début de la génération de la map.");

        //Choisir la même valeur de graine sur les deux ordinateurs.
        MapGenerator mapgen = new EllerMap(res, options.getDefaultWallSize(),
                options.getDefaultCorridorSize(), options.getDefaultCorridorSize(),
                options.getDefaultWallSize(), options.getDefaultMapSeed());


        //MapGenerator mapgen = new EllerMap(res, 2, 8, 8, 2, 100);

        map = new Map(options.getDefaultSizeXMap(), options.getDefaultSizeYMap(), 32, mapgen);
        //map = new Map(512, 512, 32, mapgen);
        System.out.println("Fin de la génération de la map.");

        mapLimit = 3 * gc.getHeight() / 4;

        Image panneau = new Image("res/img/panneauG.png");

        minimap = new MiniMap(map, gc.getWidth() / 4, gc.getHeight() / 4, panneau);
        states = state;
        container = gc;

        pannel = new CommandPanel(gc, 0, 3 * gc.getHeight() / 4,
                gc.getWidth() / 4, gc.getHeight() / 4, panneau);

        panneauCentral = new Image("res/img/panneauC.png");
        
        miniX = gc.getWidth() - minimap.getWidth();
        miniY = gc.getHeight() - minimap.getHeight();

        moves = new ArrayList<>(40);

        //Ajout des joueurs
        player1 = new Player(Color.magenta, mapgen.getLocationPlayer1(), map, res);
        player2 = new Player(Color.orange, mapgen.getLocationPlayer2(), map, res);
        player1.seen_maj(map);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (player1.seen[x][y]) {
                    map.setWarFog(x, y);
                    if (!map.isBlackout(x, y)) {
                        map.unsetBlackout(x, y);
                    }
                } else {
                    map.setWarFog(x, y);
                    map.setBlackout(x, y);
                }
            }
        }

        //Initialisation des timers
        timerEnergy = new Timer(1000);
        timerMove = new Timer(100);


        pannel.addGroupActions(new KernelActions(player1, map, res, this));
        pannel.addGroupActions(new PluginActions(player1, map, res, this));
        pannel.addGroupActions(new BitActions(player1, map, res, this));
        pannel.addGroupActions(new BombeActions(player1, map, res, this));
        pannel.addGroupActions(new ScanActions(player1, map, res, this));
        setView();

        //Code pour initialiser la gestion des événements
        Input input = gc.getInput();
        input.enableKeyRepeat();

        enterSelectionMap = false;
        if(offlineMode)
            gameStartedFunction();
    }

    @Override
    public void leave(GameContainer arg0, StateBasedGame arg1)
            throws SlickException {
        chat = null;
        s.removeListener(localServerListener);
    }

    public void sendActionMessage(ActionMessage m) {
        if(connec != null)
            connec.sendUDP(m);
        actions.add(m);
    }

    /**
     * Removes the destroyed units and buildings
     */
    private void removeDestroyed() {
        //Unités
        Iterator<Unit> itU = player1.getUnits().iterator();
        while (itU.hasNext()) {
            Unit unit = itU.next();
            if (!unit.isAlive()) {
                unit.getDestruction_snd().play();
                map.removeUnit(unit);
                itU.remove();
            }
        }
        itU = player2.getUnits().iterator();
        while (itU.hasNext()) {
            Unit unit = itU.next();
            if (!unit.isAlive()) {
                unit.getDestruction_snd().play();
                map.removeUnit(unit);
                itU.remove();
            }
        }

        //Bâtiments
        Iterator<Building> itB = player1.getBuildings().iterator();
        while (itB.hasNext()) {
            Building building = itB.next();
            if (!building.isAlive()) {
                if (building instanceof Scan && ((Scan) building).getMorph().isMorphed()) {
                    map.removeBuilding(building);
                    map.addBuilding(((Scan) building).getMorph().getA());
                } else {
                    building.getDestruction_snd().play();
                    map.removeBuilding(building);
                    itB.remove();
                }
            }
        }

        itB = player2.getBuildings().iterator();
        while (itB.hasNext()) {
            Building building = itB.next();
            if (!building.isAlive()) {
                if (building instanceof Scan && ((Scan) building).getMorph().isMorphed()) {
                    map.removeBuilding(building);
                    map.addBuilding(((Scan) building).getMorph().getA());
                } else {
                    building.getDestruction_snd().play();
                    map.removeBuilding(building);
                    itB.remove();
                }
            }
        }
    }

    /**
     * To move or attack. To be used either in the map or in the minimap
     *
     * @param x in tiles
     * @param y
     */
    private void moveAndAttack(Point goal, Player player) {
        if (!map.isBlocked(goal.x, goal.y)) {
            //On récupère la sélection
            ArrayList<Unit> selectedUnits = player.getSelectedUnits();
//                    Move move = new Move(map, selectedUnits, goal);
//                    moves.add(move);
            for (Unit unit : selectedUnits) {
                //On supprime le mouvement précédent.
                moves.remove(unit.getMove());
                unit.getAttack().stop();
                unit.getBuild().stop();
                Move move = new Move(map, unit, goal);
                moves.add(move);
                unit.setMove(move);
            }
            ArrayList<Building> selectedBuildings = player.getSelectedBuildings();

            //Que des bâtiments de sélectionnés
            if (selectedUnits.isEmpty()) {
                for (Building building : selectedBuildings) {
                    building.setRalliement(goal);
                }
            }
            // Si la case est ccupée par une unité adverse, on attaque!
        } else if (map.isUnitAt(goal.x, goal.y) && map.getUnitAt(goal.x, goal.y).getOwner() != player) {
            ArrayList<Unit> selectedUnits = player.getSelectedUnits();
            ArrayList<Building> selectedBuildings = player.getSelectedBuildings();
            for (Unit unit : selectedUnits) {
                //On crée une attack
                moves.remove(unit.getMove());
                unit.tir_cible(map.getUnitAt(goal.x, goal.y), map);
                unit.getBuild().stop();
            }
            for (Building building : selectedBuildings) {
                if (building instanceof Antivirus) {
                    Antivirus anti = (Antivirus) building;
                    anti.setAtt(new AttackAnti(map.getUnitAt(goal.x, goal.y), anti, map));
                }
            }
        } else if (map.isBuildingAt(goal.x, goal.y) && map.getBuildingAt(goal.x, goal.y).getOwner() != player) {
            ArrayList<Unit> selectedUnits = player.getSelectedUnits();
            ArrayList<Building> selectedBuildings = player.getSelectedBuildings();
            for (Unit unit : selectedUnits) {
                //On crée une attack
                moves.remove(unit.getMove());
                unit.tir_cible(map.getBuildingAt(goal.x, goal.y), map);
                unit.getBuild().stop();
            }
            for (Building building : selectedBuildings) {
                if (building instanceof Antivirus) {
                    Antivirus anti = (Antivirus) building;
                    anti.setAtt(new AttackAnti(map.getBuildingAt(goal.x, goal.y), anti, map));
                }
            }
        }
    }
    
    public void endOfTurnFunction() {
        synchronized (lock) {
            if (lagTimer != null) {
                lagTimer.arret();
                waitingPlayer = false;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startTurn();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                    endTurn();
                }
            }).start();
        }
    }
    
    public void gameStartedFunction() {
        gameStarted = true;
        synchronized (lock) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startTurn();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                    endTurn();
                }
            }).start();
        }
    }
    
    public void activateOfflineMode() {
        offlineMode = true;
    }
}
