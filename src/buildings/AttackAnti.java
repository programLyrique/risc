package buildings;

import core.*;
import java.util.ArrayList;
import java.util.Iterator;
import map.*;
import units.Unit;

/**
 *
 *    
 */
public class AttackAnti {

    private UandB cible;
    private Antivirus anti;
    private Map map;
    private Timer attTimer;

    public AttackAnti(UandB cible, Antivirus anti, Map map) {
        this.cible = cible;
        this.anti = anti;
        this.map = map;
        if (cible != null) {
            attTimer = new Timer(2500);
        }
    }

    /**
     * teste si la cible existe, s'il n'y en a pas, elle en cherche une, si
     * finalement elle en a une, elle tire dessus, sinon elle attend le prochain
     * update. cible == null correspond finalement à une attaque finie.
     */
    public void update(int delta) {
        int x = anti.getLocation().x - anti.getRange() - 2;
        int y = anti.getLocation().y - anti.getRange() - 2;
        int width = 2 * (anti.getRange() + 2);
        int height = 2 * (anti.getRange() + 2);

        ArrayList<Unit> allu = map.getUnitsAt(x, y, width, height);
        Iterator<Unit> itU = allu.iterator();
        if (cible == null) {
            while (cible == null && itU.hasNext()) {
                Unit unit = itU.next();
                if (unit.getOwner() != anti.getOwner()) {
                    cible = unit;
                }
            }
            ArrayList<Building> allb = map.getBuildingsAt(x, y, width, height);
            Iterator<Building> itB = allb.iterator();
            while (cible == null && itB.hasNext()) {
                Building b = itB.next();
                if (b.getOwner() != anti.getOwner()) {
                    cible = b;
                }
            }
            if (cible != null) {
                attTimer = new Timer(2500);
            }
        }

        if (cible != null && cible.isAlive() && anti.distance_square(cible) <= anti.getRange()) {
            attTimer.update(delta);
            if (attTimer.tick()) {
                anti.getShoot_snd().play();
                cible.setHp(Math.max(0, cible.getHp() - (anti.getDamage() - (cible.getArmor() * anti.getDamage()) / 100)));
                if (!cible.isAlive()) {
                    cible.getOwner().setWarm(cible.getOwner().getWarm() - cible.getWarm_cost());
                    if (cible instanceof EventAeration) {
                        cible.getOwner().setMaxWarm(cible.getOwner().getMaxWarm() - 100);
                    }
                }
            }
        } else {
            cible = null;
        }
    }
}
