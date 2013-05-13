package units;

import buildings.Building;
import buildings.EventAeration;
import core.*;
import java.util.ArrayList;
import java.util.Iterator;
import map.Map;
import map.Map;
import map.Move;
import map.Move;
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
        this.bombe.getAttack().stop();
        if (bombe.getLocation().equals(cible)) {
            bombe.getMove().stop();
        } else {
            bombe.setMove(new Move(map, bombe, cible));
        }
    }

    private void explose(Map m) {
        //Estimer la taille du carré
        int x = bombe.getLocation().x - bombe.getSplash() - 2;
        int y = bombe.getLocation().y - bombe.getSplash() - 2;
        int width = 2 * (bombe.getSplash() + 2);
        int height = 2 * (bombe.getSplash() + 2);

        ArrayList<Unit> allu = m.getUnitsAt(x, y, width, height);
        for (Unit unit : allu) {
            //Dommages collatéraux, non ?
            // En plus, comme la bombe est une unité de player, elle n'était pas supprimée
            if (/*unit.getOwner() != bombe.getOwner()
                     &&*/unit.distance_square(bombe) < bombe.getSplash()) {
                //Max car sinon, il y avait des points de vie négatifs, et donc les unités n'étaient pas supprimées.
                unit.setHp(Math.max(0, unit.getHp() - (bombe.getDmgExplosion() - (unit.getArmor() * bombe.getDmgExplosion()) / 100)));
                if (!unit.isAlive()) {
                    unit.getOwner().setWarm(unit.getOwner().getWarm() - unit.getWarm_cost());
                    //unit.getOwner().removeUnit(unit);
                }
            }
        }
        ArrayList<Building> allbat = m.getBuildingsAt(x, y, width, height);
        for (Building building : allbat) {
            if (/*building.getOwner() != bombe.getOwner()
                     &&*/building.distance_square(bombe) < bombe.getSplash()) {
                building.setHp(Math.max(0, building.getHp() - (bombe.getDmgExplosion() - (building.getArmor() * bombe.getDmgExplosion()) / 100)));
                if (!building.isAlive()) {
                    building.getOwner().setWarm(building.getOwner().getWarm() - building.getWarm_cost());
                    if (building instanceof EventAeration) {
                        building.getOwner().setMaxWarm(building.getOwner().getMaxWarm() - 100);
                    }
                    //building.getOwner().removeBuilding(building);
                }
            }
        }
        bombe.getOwner().setWarm(bombe.getOwner().getWarm() - bombe.getWarm_cost());
        bombe.setHp(0); // La bombe explose, donc elle a 0 points de vie.
        //bombe.getOwner().removeUnit(bombe);
    }

    public void update() {
        if (!finished) {
            if (!map.isBlocked(cible.x, cible.y)) {
                if (bombe.getLocation().equals(cible)) {
                    bombe.getShoot_snd().play();
                    explose(map);
                    finished = true;
                }
            } else {
                UandB u = map.getUnitAt(cible.x, cible.y);
                if (u != null) {
                    if (bombe.distance_square(u) < u.getHeight() * u.getHeight() + u.getWidth() * u.getWidth()) {
                        bombe.getShoot_snd().play();
                        explose(map);
                        finished = true;
                    }
                } else {
                    u = map.getBuildingAt(cible.x, cible.y);
                    if (u != null) {
                        if (bombe.distance_square(u) < u.getHeight() * u.getHeight() + u.getWidth() * u.getWidth()) {
                            bombe.getShoot_snd().play();
                            explose(map);
                            finished = true;
                        }
                    } else {
                        stop();
                    }
                }
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
