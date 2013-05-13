package buildings;

import core.Timer;
import java.util.ArrayDeque;
import map.*;
import units.*;

/**
 *
 *    
 */
public class NewUnit
{

    private Building b;
    private Map map;
    private boolean finished;
    private Timer consTimer;
    private ArrayDeque<Unit> file;

    public NewUnit(Building b, Map map, boolean finished)
    {
        this.b = b;
        this.map = map;
        this.finished = finished;
        file = new ArrayDeque<>();
    }

    public void stop()
    {
        finished = true;
    }

    /**
     *
     * @param u unité a construire
     * @param p point de d'apparition.
     * @param m map
     * @return true si l'unité u a pu être construire
     */
    private Boolean new_unit(Unit u, Map m)
    {

        if (b.getOwner().addUnit(u))
        {

            u.setMove(new Move(m, u, b.getRalliement()));
            //System.out.println("Nouvelle unité : " + u.getClass().getName() + " en " + u.getLocation());
            u.getCons_snd().play();
            return true;

        } else
        {
            return false;
        }

    }

    public void update(int delta)
    {
        if (file.isEmpty())
        {
            finished = true;
        } else
        {
            finished = false;
            Unit u = file.getFirst();
            if (!finished && !(map.isBlocked(b.getExit().x, b.getExit().y)))
            {
                consTimer.update(delta);
                if (consTimer.tick())
                {
                    new_unit(u, map);
                    file.removeLast();
                    finished = true;
                }
                if (!file.isEmpty() && finished)
                {
                    consTimer = new Timer(150 * file.getFirst().getCons_time());
                    finished = false;
                }
            }
        }
    }

    public int lengthFile()
    {
        return file.size();
    }

    public void add(Unit u)
    {
        if (!(map.isBlocked(b.getExit().x, b.getExit().y)) && b.getOwner().getEnergy() >= u.getEnergy_cost()
                && (b.getOwner().getWarm() + u.getWarm_cost()) <= b.getOwner().getMaxWarm())
        {
            b.getOwner().setEnergy(b.getOwner().getEnergy() - u.getEnergy_cost());
            b.getOwner().setWarm(b.getOwner().getWarm() + u.getWarm_cost());
            b.getOwner().setPeople(b.getOwner().getPeople() + 1);

            if (file.size() < 16)
            {
                if (file.isEmpty())
                {
                    consTimer = new Timer(150 * u.getCons_time());
                }
                finished = false;
                file.addLast(u);
            }
        }
    }
}
