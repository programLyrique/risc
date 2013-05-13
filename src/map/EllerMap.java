/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.util.*;
import org.newdawn.slick.*;
import util.ArraySets;
import core.*;

/**
 * Uses Eller's algorithm to generate a maze in linear time.
 *
 *   pierre
 */
final public class EllerMap implements MapGenerator
{

    private Tile wall;
    private Tile[][] background;
    private Tile stratPoint;
    private int d;
    private Random random;
    private int wW;
    private int hW;
    private int wC;
    private int hC;
    private int x;
    private int y;
    private int w;
    private int h;
    private Point lP1;
    private Point lP2;
    private final int beginningSquare;
    private Map map;

    /**
     *
     * @param wallImg
     * @param wallWidth
     * @param heightWall
     * @param corridorWidth
     * @param corridorHeight
     * @param seed
     */
    public EllerMap(Ressources res, int wallWidth, int wallHeight, int corridorWidth, int corridorHeight, long seed)
    {
        wall = res.getWall();
        background = res.getBackground();

        stratPoint = res.loadTile(21, 0, Color.green, 100);

        wW = wallWidth;
        hW = wallHeight;
        wC = corridorWidth;
        hC = corridorHeight;

        beginningSquare = 16;

        random = new Random(seed);
    }

    /**
     * Until now, doesn't handle a right generation of a part of the map.
     *
     * @param map
     * @param x
     * @param y
     * @param w
     * @param h
     */
    @Override
    public void generate(Map map, int x, int y, int w, int h)
    {
        this.map = map;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        /* Calcul du nombre de cases en largeur du labyrinthes en considÃ©rant 
         *  qu'une case = un largeur de couloir de bases
         * et qu'il y a une possible alternance d'un mur et d'un couloir :
         * | | |
         * idem pour la hauteur
         */
        int width = (w - x) / (wC + wW);
        int height = (h - y) / (hC + hW);

        eller(width, height);

        finalizeUnblocking(map, width, height);

        //Traitement pour ajouter tout ce qui concerne les joueurs.
        //On ne rajoute pas les joueurs eux-mÃªme ou les bÃ¢timents, mais on
        // prÃ©pare les emplacements
        addPlayers(map);

        addBackground(map);
    }

    /**
     *
     * @param width
     * @param height
     */
    public void eller(int width, int height)
    {
        ArraySets sets = new ArraySets(width);

        //Se remÃ©morer des cases dÃ©jÃ  utilisÃ©es
        boolean[] usedCases = new boolean[width];

        //Initialisation
        // Chaque case correspond Ã  un unique ensemble
        sets.singletons();

        // La derniÃ¨re rangÃ©e a un traitement particulier
        // dans la version normale
        // Ici, non
        for (int row = 0; row < height; row++)
        {
            //Fusion des ensembles
            int index = 0;
            while (index != -1)
            {
                int next = sets.nextSet(index);
                if (next == -1)// A amÃ©liorer
                {
                    break;
                }
                if (random.nextBoolean())
                {
                    sets.merge(index, next);
                }
                index = next;
            }

            //Choix des liaisons verticales
            Arrays.fill(usedCases, false);//Aucune case reliÃ©es au dÃ©part
            //Pas reliÃ©e = mur

            ArraySets newsets = new ArraySets(width);

            index = 0;
            while (index != -1)
            {
                int size = 0;
                boolean chosen = false;
                int newindex = -1;
                for (int i = index; i != -1; i = sets.nextIndexSet(i))
                {
                    if (random.nextBoolean())
                    {
                        usedCases[i] = true;
                        if (!chosen)
                        {
                            newindex = i;
                            newsets.addSingleton(i);
                        } else
                        {
                            newsets.add(newindex, i);
                        }
                        chosen = true;
                    }
                    size++;//on en profite pour calculer la taille
                }
                //On veut qu'au moins une case de l'ensemble soit reliÃ©e Ã  celle du bas.
                if (!chosen)
                {
                    int nb = random.nextInt(size);
                    newindex = sets.nextIndexSet(index, nb);
                    usedCases[newindex] = true;
                    newsets.addSingleton(newindex);
                }
                index = sets.nextSet(index);
            }

            generateRow(row, width, height - 1, sets, usedCases);

            //Les cases non assignÃ©es Ã  un ensemble se voient former un ensemble Ã  elles-seules
            //Elles sont automatiquement intercalÃ©es entre les ensembles dÃ©jÃ  formÃ©s.
            newsets.singletons(0);

            sets = newsets;

        }

        //dans la version normale, traitement particulier de la derniÃ¨re rangÃ©e. Ici, non.
        //A la derniÃ¨re rangÃ©e, on relit tout :
        //sets.clear();

        //Arrays.fill(usedCases, false);

        //generateRow(height, width, height, sets, usedCases);
    }

    /**
     * Mets Ã  jour les cases de la rangÃ©es indiquÃ©es dans la map.
     *
     * @param sets
     */
    private void generateRow(int row, int width, int height, ArraySets sets, boolean[] usedCases)
    {
        int previndex = 0;
        //GÃ©nÃ©ration en console pour tester
        if (map == null)
        {
            for (int index = 0; index < width; index++)
            {
                if (!sets.sameSet(index, previndex) && !usedCases[index])
                {
                    System.out.print("|_");
                } else if (!sets.sameSet(index, previndex))
                {
                    System.out.print("| ");
                } else if (!usedCases[index])
                {
                    System.out.print("__");
                } else
                {
                    System.out.print("  ");
                }
                previndex = index;
            }
            //System.out.print("|");
            System.out.println();
        } else //GÃ©nÃ©ration dans la map
        {
            /*
             *  le monde est divisÃ© en un damier de quadrants de mÃªmes dimensions
             * La division du quadrant n'est par contre pas centrÃ© :
             * |_
             * | 
             * __
             * 
             * sont les quatres configurations possibles, avec des tailles variables pour 
             * les quatres Ã©lÃ©ments.
             */
            int pasW = wC + wW;
            int pasH = hC + hW;
            previndex = 0;

            //Mur verticaux
            for (int index = 0; index < width; index++)
            {
                if (!sets.sameSet(index, previndex))
                {
                    for (int i = 0; i < wW; i++)
                    {
                        int abs = x + index * pasW + i;
                        for (int j = 0; j < pasH; j++)
                        {
                            int ord = y + row * pasH + j;
                            map.background[abs][ord] = wall;
                            map.block(abs, ord);
                            map.unsetBlackout(abs, ord);
                        }
                    }
                    // on dÃ©bloque la partie supÃ©rieure droit
                    for (int i = wW; i < pasW; i++)
                    {
                        int abs = x + index * pasW + i;
                        for (int j = 0; j < hW; j++)
                        {
                            int ord = y + row * pasH + j;
                            map.unblock(abs, ord);
                        }
                    }
                    previndex = index;//on a changÃ© d'ensemble, donc on change l'index prÃ©cÃ©dent
                } else //pas de murs, donc on dÃ©bloque tout 
                // des parties pourront Ãªtre rebloquÃ©es pour la passe des murs verticaux
                {

                    for (int i = 0; i < pasW; i++)
                    {
                        int abs = x + index * pasW + i;
                        for (int j = 0; j < pasH; j++)
                        {
                            int ord = y + row * pasH + j;
                            map.unblock(abs, ord);
                        }
                    }
                }
            }

            //Mur horizontaux
            for (int index = 0; index < width; index++)
            {
                if (!usedCases[index])//Il y a un mur
                {
                    for (int i = 0; i < pasW; i++)
                    {
                        int abs = x + index * pasW + i;
                        for (int j = 0; j < hC; j++)
                        {
                            int ord = y + row * pasH + hW + j;
                            // traiter simplement les dÃ©passements en cas de 
                            // de reste dans la division euclidienne de prÃ©traitement
                            if (ord >= h)
                            {
                                break;
                            }
                            //System.err.println("RangÃ©e " + row + " ordonnÃ©e " + ord + " hauteur max " + h + " y = ");
                            map.background[abs][ord] = wall;
                            map.block(abs, ord);
                            map.unsetBlackout(abs, ord);
                        }

                    }
                } else // on dÃ©bloque la partie corridor
                {
                    for (int i = wW; i < pasW; i++)
                    {
                        int abs = x + index * pasW + i;
                        for (int j = 0; j < hC; j++)
                        {
                            int ord = y + row * pasH + hW + j;
                            // traiter simplement les dÃ©passements en cas de 
                            // de reste dans la division euclidienne de prÃ©traitement
                            if (ord >= h)
                            {
                                break;
                            }
                            map.unblock(abs, ord);
                        }
                    }

                }
            }
        }

    }

    /**
     * DÃ©bloque les bordures de la carte non dÃ©bloquÃ©es. Normal qu'elles aient
     * Ã©tÃ© bloquÃ©es : par ex wC + wW peut ne pas diviser la largeur de la carte
     * ! Prolonge aussi les murs
     *
     * @param map
     * @param width width pour Eller
     * @param height height pour Eller
     */
    private void finalizeUnblocking(Map map, int width, int height)
    {
        //CoordonnÃ©es des demi-plans Ã  dÃ©bloquer
        int dX = width * (wC + wW);
        int dY = height * (hC + hW);

        //Nombre de tiles en abscisse Ã  dÃ©bloquer
        int nbX = w - dX;
        //Nombre de tiles en ordonnÃ©es Ã  dÃ©bloquer
        int nbY = h - dY;

        //On dÃ©bloque tout
        for (int i = dX; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                map.unblock(x + i, y + j);
            }
        }

        for (int j = dY; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                map.unblock(x + i, y + j);
            }
        }

        //On repasse et on remet les murs oÃ¹ il le faut
        //Bordure droite
        for (int j = 0; j < h; j++)
        {
            //Si c'est bloquÃ©, Ã  ce stade de la gÃ©nÃ©ration, c'est que c'est un mur
            if (map.isBlocked(x + dX - 1, y + j))
            {
                for (int i = dX; i < w; i++)
                {
                    map.unsetBlackout(x + i, y + j);
                    map.background[x + i][y + j] = wall;
                    map.block(x + i, y + j);
                }
            }
        }
        //Bordure en bas
        for (int i = 0; i < w; i++)
        {
            if (map.isBlocked(x + i, y + dY - 1))
            {
                for (int j = dY - 1; j < h; j++)
                {
                    map.unsetBlackout(x + i, y + j);
                    map.background[x + i][y + j] = wall;
                    map.block(x + i, y + j);
                }
            }
        }
    }

    /**
     * Adds the tiles for the background and the ornaments To do : use a Simplex
     * noise to organize the ornaments ?
     *
     * @param map
     * @param width
     * @param height
     */
    public void addBackground(Map map)
    {
        //Tout ce qui n'est pas bloquÃ© Ã  ce stade est un dÃ©cor
        int w = background.length;
        int h = background[0].length;


        for (int i = 0; i < map.getWidth(); i++)
        {
            for (int j = 0; j < map.getHeight(); j++)
            {
                if (!map.isBlocked(i, j))
                {
                    map.background[i][j] = background[i % w][j % h];
                    map.unsetBlackout(i, j);
                    // Mais plus tard, il faudra laisser le brouillard de guerre


                    // On essaye de mettre un dÃ©cor de chaque type Ã  un endroit toutes les 64 tiles
                    final int intervalle = 20;
                    if (i % intervalle == 0 && j % intervalle == 0)
                    {
                        int x = i + random.nextInt(intervalle);
                        int y = j + random.nextInt(intervalle);
                        if( x < map.getWidth() && y < map.getHeight())
                        {
                             map.ornaments[x][y] = stratPoint;
                        }
                    }
                }
            }
        }
    }

    /**
     * Ajoute un kernel pour les deux joueurs, avec deux bits chacun . Supprime
     * les murs Ã©ventuels qui gÃ©nÃ©raient. Emplacements de dÃ©part : en haut Ã 
     * gauche et en bas Ã  droite.
     *
     * @param map
     */
    private void addPlayers(Map map)
    {
        carveBeginningPlace(map, 0, 0);
        carveBeginningPlace(map, w - beginningSquare, h - beginningSquare);

        //Choix de l'emplacement de dÃ©part
        if (random.nextBoolean())
        {
            lP1 = new Point(4, 4);
            lP2 = new Point(w - 9, h - 9);
        } else
        {
            lP1 = new Point(w - 9, h - 9);
            lP2 = new Point(6, 6);
        }
    }

    /**
     * EnlÃ¨ve les murs qui seraient prÃ©sents aux emplacements de dÃ©part
     *
     * @param map
     */
    private void carveBeginningPlace(Map map, int cX, int cY)
    {
        //On enlÃ¨ve les murs sur un carrÃ© de 16 x 16 dans les deux coins
        //On suppose donc que la carte est de taille supÃ©rieure Ã  16x16!
        for (int i = cX; i < cX + beginningSquare; i++)
        {
            for (int j = cY; j < cY + beginningSquare; j++)
            {
                map.setBlackout(x + i, y + j);
                map.background[x + i][y + j] = null;
                map.unblock(x + i, y + j);
            }
        }

    }

    public Point getLocationPlayer1()
    {
        return lP1;
    }

    public Point getLocationPlayer2()
    {
        return lP2;
    }

    public static void main(String[] args)
    {
        System.out.println("Test de l'algorithme d'Eller de gÃ©nÃ©ration de labyrinthe.");

        EllerMap eller = new EllerMap(null, 1, 1, 1, 1, 30);

        final long startTime = System.currentTimeMillis();
        eller.generate(null, 0, 0, 100, 100);
        final long endTime = System.currentTimeMillis();

        System.out.println("Fin de gÃ©nÃ©ration de la carte en " + (endTime - startTime) + " millisecondes");
    }
}
