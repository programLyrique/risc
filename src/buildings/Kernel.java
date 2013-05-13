package buildings;

import core.*;
import java.util.ArrayDeque;
import map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import units.*;

/**
 *
 *     Spécifie le batiment Kernel
 *
 */
public class Kernel extends Building {

    /**
     *
     * @param p
     * @param _owner
     * @throws SlickException
     */
    public Kernel(Point p, Player _owner, Ressources res, Map map) throws SlickException {
        super(res.getCons_kernel(),
                res.getSel_kernel(),
                res.getDes_kernel(),
                res.getLogo_kernel(),
                res.getTiles_kernel(),
                res.getHeigth_kernel(),
                res.getWidth_kernel(),
                p,
                1500,//hp
                res.getArmor_kernel(),
                res.getCons_time_kernel(),
                res.getEnergy_cost_kernel(),
                res.getWarm_cost_kernel(),
                _owner,
                res.getRange_kernel(),
                map);
    }

    /**
     *
     * @return true si le bit a bien été créé!
     * @throws SlickException
     */
    public void new_bit(Map m, Ressources res) throws SlickException {
        Bit b = new Bit(getExit(), owner, res, m);
        newU.add(b);
    }

    /**
     *
     * @return ...octet
     * @throws SlickException
     */
    public void new_octet(Map m, Ressources res) throws SlickException {
        Octet b = new Octet(getExit(), owner, res, m);
        newU.add(b);
    }

    /**
     *
     * @return ... multiplexeur
     * @throws SlickException
     */
    public void new_mult(Map m, Ressources res) throws SlickException {
        Multiplexeur b = new Multiplexeur(getExit(), owner, res, m);
        newU.add(b);
    }

    /**
     *
     * @return ... bombe logique
     * @throws SlickException
     */
    public void new_bombe(Map m, Ressources res) throws SlickException {
        BombeLogique b = new BombeLogique(getExit(), owner, res, m);
        newU.add(b);
    }
}
