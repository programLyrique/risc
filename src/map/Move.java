package map;

import core.*;
import java.util.*;
import org.newdawn.slick.*;
import units.*;

/**
 * A move for a unit : the various steps to reach its goal. The movement ought
 * to be put in a list, and removed when a move is finished.
 *
 *   pierre
 */
public class Move {

    ArrayList<Unit> units;
    Unit leader;
    List<Point> movement;
    ListIterator<Point> path;
    Point goal;
    Map map;
    
    Point current;
    Integer xx;

    /**
     * Moves a single unit to a goal
     *
     * @param unit
     * @param goal
     */
    public Move(Map map, Unit unit, Point goal) {
        xx = 0;
        units = new ArrayList<>(1);
        units.add(unit);

        leader = unit;

        this.goal = goal;
        this.map = map;

        movement = Pathfinding.pathfinding(map, unit.getLocation(), goal);
        path = movement.listIterator();
        current = leader.getLocation();
    }

    /**
     * Moves a group of units to a goal. A leader is chosen and goes towards
     * thegoal.
     *
     * @param units
     * @param goal
     */
    public Move(Map map, ArrayList<Unit> units, Point goal) {
        xx = 0;
        this.units = units;
        this.goal = goal;
        this.map = map;
        int d = Pathfinding.manhattan_distance(units.get(0).getLocation(), goal);
        leader = units.get(0);
        for (Unit u : units)
        {
            if (Pathfinding.manhattan_distance(u.getLocation(), goal) < d && !Pathfinding.validNeighbours(map, u.getLocation()).isEmpty())
            {
                leader = u;
                d = Pathfinding.manhattan_distance(u.getLocation(), goal);
            }
        }
        movement = Pathfinding.pathfinding(map, leader.getLocation(), goal);
        path = movement.listIterator();
        current = leader.getLocation();
    }

    /**
     * No movement
     * @todo mettre du code fonctionnel ici
     */
    public Move() {
    }

    public boolean isFinished() {
        return !path.hasNext();
    }

    /**
     *
     * @return true if the movement is finished
     */
    public boolean update() {
        if (path.hasNext()) {
            Point pt = path.next();
            int xxx = 0;
            int yyy = 0;
            if (pt.x > current.x)
            {
                xxx = 1;
            }
            if (pt.x < current.x)
            {
                xxx = -1;
            }
            if (pt.y < current.y)
            {
                yyy = -1;
            }
            if (pt.y > current.y)
            {
                yyy = 1;
            }
            if (!map.isBlocked(pt.x, pt.y))
            {
                map.units[pt.x][pt.y] = leader;
                // On modifie les coordonnées de l'unité 
                leader.setLocation(pt);
                // On bloque la nouvelle case
                map.block(pt.x, pt.y);
                //On efface le contenu de la tile précédente
                map.units[current.x][current.y] = null;
                //On débloque la case précédente
                map.unblock(current.x, current.y);
            
                current = pt;
                for (Unit u : units)
                {
                    if (Pathfinding.manhattan_distance(u.getLocation(),current)> 10)
                    {
                        List<Point> pa = Pathfinding.pathfinding(map, u.getLocation(), current);
                        if (pa.size() > 1)
                        {
                            Point p = pa.get(1);
                            if (!map.isBlocked(p.x, p.y))
                            {
                                Point q = u.getLocation();
                                map.units[p.x][p.y] = u;
                                u.setLocation(p);
                                map.block(p.x, p.y);
                                map.units[q.x][q.y] = null;
                                map.unblock(q.x, q.y);
                            }
                        }
                    }
                    else
                    {
                        Point q = u.getLocation();
                        Point p = new Point(q.x+xxx, q.y+yyy);
                        if (!map.isBlocked(p.x,p.y) && q != current)
                        {
                            map.units[p.x][p.y] = u;
                            u.setLocation(p);
                            map.block(p.x, p.y);
                            map.units[q.x][q.y] = null;
                            map.unblock(q.x, q.y);
                        }
                    }
                }
                return true;
            }
            else
            {   
                if (xx == 1)
                {
                movement = Pathfinding.pathfinding(map, leader.getLocation(), goal);
                path = movement.listIterator();
                xx = 0;
                return true;
                }
                xx = xx + 1;
                return true;
//                movement = Pathfinding.pathfinding(map, leader.getLocation(), goal);
//                path = movement.listIterator();
//                if (path.hasNext()) {
//                    Point pt2 = path.next();
//                    map.units[pt2.x][pt2.y] = leader;
//                    // On modifie les coordonnées de l'unité 
//                    leader.setLocation(pt2);
//                    // On bloque la nouvelle case
//                    map.block(pt2.x, pt2.y);
//                    //On efface le contenu de la tile précédente
//                    map.units[current.x][current.y] = null;
//                    //On débloque la case précédente
//                    map.unblock(current.x, current.y);
//            
//                    current = pt2;
//                    return true;
//                }
//                else {
//                return false;
//                }
            }
        } 
        else {
//            if (current != goal)
//            {
//             movement = Pathfinding.pathfinding(map, leader.getLocation(), goal);
//             path = movement.listIterator();
//             return true;
//            }
            for (Unit u : units)
                {
                    if (Pathfinding.manhattan_distance(u.getLocation(),current)> 1)
                    {
                        List<Point> pa = Pathfinding.pathfinding(map, u.getLocation(), current);
                        if (pa.size() > 1)
                        {
                            Point p = pa.get(1);
                            if (!map.isBlocked(p.x, p.y))
                            {
                                Point q = u.getLocation();
                                map.units[p.x][p.y] = u;
                                u.setLocation(p);
                                map.block(p.x, p.y);
                                map.units[q.x][q.y] = null;
                                map.unblock(q.x, q.y);
                            }
                        }
                    }
                }
            return false;
        }
    }

    /**
     * Stops the movement. USeful if some units are rescheduled.
     */
    public void stop() {
        path = (new ArrayList()).listIterator();
    }

    /**
     * Draw the path
     * Call it in a render method.
     */
    public void drawMove(Graphics gr, int x, int y) 
    {
        ListIterator<Point> pathDraw= movement.listIterator();
        Point pt = movement.get(0);
        int tileL = Map.getTileLenght();
        int center = Map.getTileLenght() /2;
        while(pathDraw.hasNext())
        {
            Point next = pathDraw.next();
            //System.err.println("Debut : "  + pt + " Fin : " + next );
            gr.setColor(Color.blue);
            gr.setLineWidth(3);
            gr.drawLine((pt.x  -x)* tileL + center  ,  (pt.y - y)* tileL + center , 
                    (next.x - x)* tileL+ center  , (next.y - y) * tileL + center);
            pt = next;
        }
    }
}
