package map;

import buildings.Building;
import buildings.EventAeration;
import core.Timer;
import core.UandB;
import java.util.ArrayList;
import units.Unit;

/**
 *
 *    
 */
public class Attack {

    private UandB cible;
    private Unit unit;
    private Map map;
    private boolean finished;
    private boolean aoe;
    private Timer attTimer;

    public Attack(UandB cible, Unit unit, Map map, boolean finished, boolean aoe) {
        this.cible = cible;
        this.unit = unit;
        this.map = map;
        this.finished = finished;
        this.aoe = aoe;
        unit.setMove(new Move(map, unit, cible.getLocation()));
        attTimer = new Timer(4000 / unit.getAtt_speed());
    }

    /**
     * effectue un tir ou se déplace
     */
    public void update(int delta) {
        if (!finished) {
            attTimer.update(delta);
            if (unit.distance_square(cible) <= unit.getRange()) {
                unit.getMove().stop();
                if (attTimer.tick() && cible.isAlive()) {
                    unit.getShoot_snd().play();
                    if (!aoe) {
                        cible.setHp(cible.getHp() - (unit.getDamage() - (cible.getArmor() * unit.getDamage()) / 100));
                        System.out.println(cible.getHp());
                    } else {
                        int x = cible.getLocation().x - unit.getSplash() - 2;
                        int y = cible.getLocation().y - unit.getSplash() - 2;
                        int width = 2 * (unit.getSplash() + 2);
                        int height = 2 * (unit.getSplash() + 2);
                        ArrayList<Unit> allu = map.getUnitsAt(x, y, width, height);
                        for (Unit u : allu) {
                            if ( u.distance_square(cible) < unit.getSplash()) {
                                u.setHp(Math.max(0, u.getHp() - (unit.getDamage() - (u.getArmor() * unit.getDamage()) / 100)));
                                if (!u.isAlive()) {
                                    u.getOwner().setWarm(u.getOwner().getWarm() - u.getWarm_cost());
                                }
                            }
                        }
                        ArrayList<Building> allbat = map.getBuildingsAt(x, y, width, height);
                        for (Building building : allbat) {
                            if (building.distance_square(cible) < unit.getSplash()) {
                                building.setHp(Math.max(0, building.getHp() - (unit.getDamage() - (building.getArmor() * unit.getDamage()) / 100)));
                                if (!building.isAlive()) {
                                    building.getOwner().setWarm(building.getOwner().getWarm() - building.getWarm_cost());
                                    if (building instanceof EventAeration) {
                                        building.getOwner().setMaxWarm(building.getOwner().getMaxWarm() - 100);
                                    }
                                    //building.getOwner().removeBuilding(building);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void stop() {
        finished = true;
    }

    public UandB getCible() {
        return cible;
    }

    public Move getMove() {
        return unit.getMove();
    }

    public Unit getUnit() {
        return unit;
    }

    public Map getMap() {
        return map;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
