package buildings;

import core.Ressources;
import core.Timer;
import map.Map;
import org.newdawn.slick.SlickException;

/**
 *
 *    
 */
public class Morph {

    private Scan scan;
    private Map map;
    private boolean finished;
    private boolean morphed;
    private Timer morphTimer;
    private Antivirus a;

    public Morph(Scan scan, Map map, boolean finished, Antivirus a) {
        this.scan = scan;
        this.map = map;
        this.finished = finished;
        this.a = a;
        this.morphed = false;
        if (scan.getOwner().getEnergy() >= a.getEnergy_cost() && scan.getOwner().getMaxWarm() - scan.getOwner().getWarm() > a.getWarm_cost()) {
            scan.getOwner().setEnergy(scan.getOwner().getEnergy() - a.getEnergy_cost());
            scan.getOwner().setWarm(scan.getOwner().getWarm() + a.getWarm_cost());
            morphTimer = new Timer(10000);
        } else {
            finished = true;
        }     
    }

    public void stop() {
        finished = true;
    }

    /**
     *
     * @return true si l'on a pu amélioré en antivirus
     * @throws SlickException
     */
    public void morph(Map map) throws SlickException {
        scan.setHp(0);
    }

    public void update(int delta) throws SlickException {
        if (!finished) {
            morphTimer.update(delta);
            if (morphTimer.tick()) {
                morph(map);
                finished = true;
                morphed = true;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public Antivirus getA() {
        return a;
    }

    public boolean isMorphed() {
        return morphed;
    }
}
