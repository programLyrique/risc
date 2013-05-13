package core;

import java.util.*;

/**
 * @todo mettre toutes les méthodes statiques ?
 *    
 */
public class Pathfinding {
    
    static private int abs(int n)
    {
        if (n > 0)
        {
            return n;
        }
        else
        {
            return -n;
        }
    }
    
    static private int squared_distance(Point a, Point b)
    {
        return (b.get_x()-a.get_x())*(b.get_x()-a.get_x()) + (b.get_y() - a.get_y())*(b.get_y() - a.get_y());
    }
    
    static public int manhattan_distance(Point a, Point b)
    {
        return (abs(b.get_x()-a.get_x()) + abs(b.get_y()-a.get_y()));
    }
    
    static public List<Point> validNeighbours(map.Map map, Point p)
    {
        List<Point> l = new ArrayList();
        int x = p.get_x();
        int y = p.get_y();
        int h = map.getHeight();
        int w = map.getWidth();
        if (x+1 < w && !map.isBlocked(x+1, y)) //droite
        {
            l.add(new Point(x+1, y));
        }
        if (y+1 < h && !map.isBlocked(x, y+1)) //haut
        {
            l.add(new Point(x, y+1));
        }
        if (x-1 > -1 && !map.isBlocked(x-1, y)) //gauche
        {
            l.add(new Point(x-1, y));
        }
        if (y-1 > -1 && !map.isBlocked(x, y-1)) //bas
        {
            l.add(new Point(x, y-1));
        }
        //if ((x+1 < w) && (y+1 < h) && (!map.isBlocked(x+1, y+1))) //haut droite
        //{
        //    l.add(new Point(x+1, y+1));
        //}
        //if ((x+1 < w) && (y-1 > -1) && (!map.isBlocked(x+1, y-1))) //bas droite
        //{
        //    l.add(new Point(x+1, y-1));
        //}
        //if ((x-1 > -1) && (y-1 > -1) && (!map.isBlocked(x-1, y-1))) //bas gauche
        //{
        //    l.add(new Point(x-1, y-1));
        //}
        //if ((x-1 > -1) && (y+1 < h) && (!map.isBlocked(x-1, y+1))) //haut gauche
        //{
        //    l.add(new Point(x-1, y+1));
        //}
        return l;
    }
    
    // renvoie le voisin de b le plus proche de a
    static public Point near(Point a, Point b)
    {
        int x = 0;
        int y = 0;
        if (a.get_x() < b.get_x())
        {
            x = -1;
        }
        if (a.get_x() > b.get_x())
        {
            x = 1;
        }
        if (a.get_y() < b.get_y())
        {
            y = -1;
        }
        if (a.get_y() > b.get_y())
        {
            x = 1;
        }
        return new Point(b.get_x() + x, b.get_y() + y);
    }
    
    //renvoie un chemin de a vers b s'il existe
    //renvoie un chemin de a vers le dernier point examiné sinon
    
    static public List<Point> pathfinding(map.Map map, Point a, Point b)
    {
        if (a.get_x() == b.get_x() && a.get_y() == b.get_y())
        {
            List<Point> l = new ArrayList();
            l.add(a);
            return l;
        }
        if (map.isBlocked(b.get_x(), b.get_y())) //si la destination est bloquée, on cherche un chemin vers un de ses voisins
        {
            return pathfinding(map, a, near(a,b));
        }
        else
        {
            Map<Point,Point> came_from = new HashMap();  //origine
            Map<Point,Integer> score = new HashMap(); //distance parcourue
            Map<Point,Integer> from_b = new HashMap(); //estimation de la distance à parcourir encore
            List<Point> l = new ArrayList();
            List<Point> open = new ArrayList(); //points à explorer
            Set<Point> closed = new HashSet(); //points déja explorés
            open.add(a);
            score.put(a, 0);
            from_b.put(a, manhattan_distance(a,b));
            Point current = a;
            
            //System.out.println("a = ("+ current.get_x() + "," + current.get_y()+ ")");
            //System.out.println("b = ("+ b.get_x() + "," + b.get_y()+ ")");
        
            int p = 0;
            while(!open.isEmpty() && p < 250000)
            {
                p = p+1;
                current = open.get(0);                
                //System.out.println("current = ("+ current.get_x() + "," + current.get_y()+ ")");
                //System.out.println("score current = " + score.get(current));
                
                if (current.get_x() == b.get_x() && current.get_y() == b.get_y())
                {
                    open.clear();
                }
                else
                {
                    open.remove(0);
                    closed.add(current);
                    List<Point> neighbours = validNeighbours(map, current);
                    
//                    for (p = 0; p < open.size(); p++)
//                    {
//                        System.out.println("open["+ p +"] = (" + open.get(p).get_x() + "," + open.get(p).get_y() + ")");
//                    }
//                    for (Point u : closed)
//                    {
//                        System.out.println("(" + u.get_x() + "," + u.get_y() + ")");
//                    }
                    
                    if (!neighbours.isEmpty())
                    {
                        for(int i = 0; i < neighbours.size(); i++)
                        {
                            Point neighbour = neighbours.get(i);
                            int d = manhattan_distance(current, neighbour);
                            int h = manhattan_distance(neighbour, b);
                            if (closed.contains(neighbour))
                            {
                                 if (score.get(current) + d < score.get(neighbour))
                                {
                                    score.put(neighbour, score.get(current) + d);
                                    from_b.put(neighbour, h);
                                    came_from.put(neighbour, current);
                                    closed.remove(neighbour);
                                    if (!open.isEmpty())
                                    {
                                        int k = 0;
                                        while((k < open.size()) && (h+score.get(neighbour) > from_b.get(open.get(k))))
                                        {
                                            k = k + 1;
                                        }
                                        open.add(k,neighbour);
                                    }
                                    else
                                    {
                                        open.add(neighbour);
                                    }
                                }
                            }
                            else
                            {
                                if (!open.contains(neighbour) || (score.get(current) + d < score.get(neighbour)))
                                {
                                    came_from.put(neighbour, current);
                                    from_b.put(neighbour, h);
                                    score.put(neighbour, score.get(current) + d);
                                    if (!open.isEmpty())
                                    {
                                        int k = 0;
                                        while((k < open.size()) && (h+score.get(neighbour) > from_b.get(open.get(k))))
                                        {
                                            k = k + 1;
                                        }
                                        open.add(k,neighbour);
                                    }
                                    else
                                    {
                                        open.add(neighbour);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            l.add(current);
            while (current != a)
            {
                l.add(0,came_from.get(current));
                current = came_from.get(current);
            }
//            l.add(0,a);
            return l;
        }
    }
}
