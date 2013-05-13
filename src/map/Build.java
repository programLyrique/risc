package map;

import core.*;
import units.*;
import buildings.*;

/**
 *
 *    
 */
public class Build {

    private Unit bit;
    private Map map;
    private boolean finished;
    private Building b;
    private Timer buildTimer;

    public Build(Unit bit, Map map, boolean finished, Building b) {
        this.bit = bit;
        this.map = map;
        this.finished = finished;
        this.b = b;
        this.bit.getAttack().stop();
        if (bit.getOwner().getEnergy() >= b.getEnergy_cost()
                && (bit.getOwner().getWarm() + b.getWarm_cost()) <= bit.getOwner().getMaxWarm()) {
            bit.getOwner().setEnergy(bit.getOwner().getEnergy() - b.getEnergy_cost());
            bit.getOwner().setWarm(bit.getOwner().getWarm() + b.getWarm_cost());
            if (this.bit.getLocation().equals(b.getExit())) {
                this.bit.getMove().stop();
            } else {
                this.bit.setMove(new Move(map, bit, b.getExit()));
            }
            buildTimer = new Timer(1000 * b.getCons_time());
        } else {
            finished = true;
        }
    }

    public void stop() {
        finished = true;
    }

    /**
     *
     * @param p le "centre" du batiment
     * @param b le batiment à construire
     * @param m la map sur laquelle il faut construire
     * @return true si on a pu construire!
     */
    private final boolean new_building(Map m) {
        boolean bool = true;
        int h = b.getHeight();
        int w = b.getWidth();
        int x = b.getLocation().x;
        int y = b.getLocation().y;
        while (x < b.getLocation().x + w && bool) {
            while (y < b.getLocation().y + h && bool) {
                bool = !map.isBlocked(x, y);
                y++;
            }
            x++;
            y = b.getLocation().y;
        }
        if (bool && bit.getOwner().addBuilding(b)) {
            b.getCons_snd().play();
            return true;
        } else {
            return false;
        }
    }

    public void update(int delta) {
        if (!(bit instanceof Bit)) {
            finished = true;
        } else {
            if (!finished) {
                if (this.bit.getLocation().equals(b.getExit())) {
                    buildTimer.update(delta);
                    if (buildTimer.tick()) {
                        new_building(map);
                        finished = true;
                    }
                }
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
