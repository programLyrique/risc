package map;

import buildings.Building;
import core.*;
import units.*;

/**
 *
 *    
 */
public class ExplosionBombe {

    private BombeLogique bombe;
    private Point cible;
    private Map map;
    private boolean finished;

    public ExplosionBombe(BombeLogique bombe, Point cible, Map map, boolean finished) {
        this.bombe = bombe;
        this.cible = cible;
        this.map = map;
        this.finished = finished;
        if (bombe.getLocation().equals(cible)) {
            bombe.getMove().stop();
        } else {
            bombe.setMove(new Move(map, bombe, cible));
        }
    }

    public void update() {
        if (!finished) {
            if (bombe.getLocation().equals(cible)) {
                for (Unit[] utab : map.units) {
                    for (Unit u : utab) {
                        if (!u.getOwner().getLocation().equals(bombe.getOwner().getLocation())
                                && u.distance_square(bombe) < bombe.getSplash()) {
                            u.setHp(u.getHp() - (bombe.getDamage() - (u.getArmor() * bombe.getDamage()) / 100));
                            if (!u.isAlive()) {
                                u.getOwner().setWarm(u.getOwner().getWarm() - u.getWarm_cost());
                                u.getOwner().removeUnit(u);
                            }
                        }
                    }
                }
                for (Building[] utab : map.buildingMask) {
                    for (Building u : utab) {
                        if (!u.getOwner().getLocation().equals(bombe.getOwner().getLocation())
                                && u.distance_square(bombe) < bombe.getSplash()) {
                            u.setHp(u.getHp() - (bombe.getDamage() - (u.getArmor() * bombe.getDamage()) / 100));
                            if (!u.isAlive()) {
                                u.getOwner().setWarm(u.getOwner().getWarm() - u.getWarm_cost());
                                u.getOwner().removeBuilding(u);
                            }
                        }
                    }
                }
                bombe.getOwner().setWarm(bombe.getOwner().getWarm() - bombe.getWarm_cost());
                bombe.getOwner().removeUnit(bombe);
                finished = true;
            } else {
                bombe.getMove().update();
            }
        }
    }

    public void stop() {
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }
}
