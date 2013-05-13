package gui;

import core.UandB;
import gui.actions.GroupActions;
import java.util.*;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;

/**
 * A command panel to control units and buildings.
 *   pierre
 */
public class CommandPanel implements MouseListener {
    private int px;
    private int py;
    private int w;
    private int h;
    
    private int length;
    
    //Image de fond
    private Image background;
    
    String view;
    ArrayList<Action> actionsForView;
    
    // Nombre de boutons en largeur
    private int nbButtonsW;
    
    //Les actions qu'on peut lancer dans le CommandPanel.
    HashMap<String, ArrayList<Action>> actions;
    
    
    /**
     * 
     * @param gc
     * @param x
     * @param y
     * @param width
     * @param height
     * @param background can be null
     */
    public CommandPanel(GameContainer gc, int x, int y, int width, int height, Image background)
    {
        this(gc, x, y, width, height, background, 64);
    }  
    
    /**
     * 
     * @param gc
     * @param x
     * @param y
     * @param width
     * @param height
     * @param background
     * @param defaultButtonLength 
     */
    public CommandPanel(GameContainer gc, int x, int y, int width, int height, Image background, int defaultButtonLength)
    {
        px = x;
        py = y;
        w= width;
        h = height;
        length = Math.min(w / 4, defaultButtonLength);
        nbButtonsW = w / length;
        actions = new HashMap<>();
        
        this.background = background;
        
        Input input = gc.getInput();
        input.addMouseListener(this);
    }   
    
    /**
     * 
     * @param view String , the name of the class
     * @param action 
     */
    public void addAction(String view, Action action)
    {
        ArrayList<Action> actForView = actions.get(view);
        if(actForView == null)
        {
            actForView = new ArrayList();
        }
        actForView.add(action);
        actions.put(view, actForView);
    }
    
    /**
     * Adds a group of actions for a view.
     * Erases all previous actions for this group.
     * @param groupActions 
     */
    public void addGroupActions(GroupActions groupActions)
    {
        ArrayList<Action> actForView = groupActions.getActions();
        //System.err.println("Ajout d'action :" + actForView + " pour " + groupActions.getName());
        actions.put(groupActions.getName(), actForView);
    }
    
    /**
     * Change the view of the pannel
     * @param ub 
     */
    public void setView(UandB ub)
    {
        System.out.print("Vue sur " + removePackage(ub.getClass().getName()) + " : ");
        view = removePackage(ub.getClass().getName());
        actionsForView = actions.get(view);
        System.out.println(actionsForView);
    }
    
    /**
     * 
     * @return class name without package names
     */
    private String removePackage(String name)
    {
        String[] s = name.split("\\.");
        //On renvoie le dernier élément qui est le nom de la classe
        return s[s.length - 1];     
    }
    

    /**
     * Reinitialize the view.
     * Useful when the selection is empty.
     */
    public void clearView()
    {
        view = "";
        actionsForView = null;
    }
    
    public void update(GameContainer gc,  int delta) throws SlickException 
    {
           
    }
    
    public void render(GameContainer gc,  Graphics gr)
    {
      //Rendu du fond
      if(background != null)//certains aiment les fonds noirs
      {
          background.draw(px, py, w, h);
      }
      //On affiche les logos des actions 
      int pX = px;
      int pY = py;
      
      if(actionsForView != null)
      {
          for (Action action : actionsForView) 
          {
              action.getLogo().draw(pX, pY, length, length);
              //Cadre jaune autour du logo
              gr.setColor(Color.yellow);
              gr.drawRect(pX, pY, length, length);
              if (pX < w) 
              {
                  pX += length;
              } else 
              {
                  pY += length;
                  pX = 0;
              }
          }
      }
  
      
      //Cadre jaune 
      gr.setColor(Color.yellow);
      gr.drawRect(px, py, w, h-1);
    }
    
    @Override
    public void mouseWheelMoved(int change) {
        
    }

    @Override
    public void mouseClicked(int button, int x, int y, int countClicked) {
        
    }

    @Override
    public void mousePressed(int button, int x, int y) 
    {
        if(button == 0)
        {
            // Clic dans le panneau ?
            if (x >= px && x < px + w && y >= py && y < h + py)
            {
                if (actionsForView != null)
                {
                    //Emplacement sur la grille de boutons
                    int gX = (x - px) / length;
                    int gY = (y - py) / length;

                    int but = gX + gY * nbButtonsW;

                    // Clic sur un bouton
                    if (but < actionsForView.size())
                    {
                        actionsForView.get(but).clicked();
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
       
    }

    @Override
    public void setInput(Input input) {
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {
        
    }

    @Override
    public void inputStarted() {
       
    }

    public static void main(String[] args)
    {
        String test = "buildings.Kernel";
        String s[] = test.split("\\.");
        System.out.println(s[0]);
    }
    
}
