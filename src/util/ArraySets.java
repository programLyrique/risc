package util;

import java.util.*;

/**
 * This class enables to manipulate a set of sets on a narray.
 * You can refer to a set in the array by the case of the array, and merge the sets.
 * The class only manipulates indexes, so store the real elements in another array
 * 
 * This class is mainly used to generate a maze for the Eller maze algorithm.
 *   pierre
 */
public final class ArraySets 
{
    private long[] sets;
    private long uni;
    /**
     * 
     * @param length the length of the array
     */
    public ArraySets(int size)
    {
        sets = new long[size];
        resetUni();
    }
    
    /**
     * The set of sets is populated with singletons : each element of the array. 
     * It is the same to do clear() then singletons(0)
     */
    public void singletons()
    {
        for(int i = 0 ; i < sets.length ; i++)
        {
            sets[i] = nextUni();
        }
    }
    
    /**
     * Cut a set refered by the index i into singletons
     * @param i 
     */
    public void singletons(int i)
    {
        //Pas optimisé puisqu'on parcourt tout le tableau
        // Il y a moyen de faire mieux...
        for(int j =0; j < sets.length ; j++)
        {
            if(sets[j] == sets[i])
            {
                sets[j] = nextUni();
            }
        }
    }
    
    /**
     * Clears the set of sets : all the elements of the array are in the same set
     */
    public void clear()
    {
        resetUni();
        Arrays.fill(sets, 0L);
    }
    
    /**
     * Adds case j of the array to the set of case j
     * @param i set
     * @param j index of the new element
     */
    public void add(int i, int j)
    {
        sets[j] = sets[i];
    }
    
    /**
     * Adds index i as a singleton ensemble.
     * Equivalent to do add(i,i)
     * @param i 
     */
    public void addSingleton(int i)
    {
        add(i, i);
    }
    
    /**
     * Adds indexes from j to k (included in the range) to the set which contains index i
     * @param i
     * @param j
     * @param k 
     */
    public void addRange(int i, int j, int k)
    {
        for(int p = j ; p <= k ; p++)
        {
            sets[p] = sets[i];
        }
    }
    
    /**
     * Merges the sets refered by the indexes i and j
     * @param i
     * @param j 
     */
    public void merge(int i, int j)
    {
        //Peut être amélioré
        
        for(int k = 0 ; k < sets.length ; k++)
        {
            if(sets[k] == sets[j])
            {
                sets[k] = sets[i];
            }
        }
    }
    
    /**
     * Returns the index of the first element of the array which is in the next set.
     * Returns -1 if there is not next set.
     * @param i
     * @return 
     */
    public int nextSet(int i)
    {
        for( int j = i+1; j < sets.length ; j++ )
        {
            if(sets[j] != sets[i])
            {
                return j;
            }
        }
        return -1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.sets);
        hash = 47 * hash + (int) (this.uni ^ (this.uni >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArraySets other = (ArraySets) obj;
        if (!Arrays.equals(this.sets, other.sets)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() 
    {
        String s = "";
        for(int i = 0 ; i < sets.length ; i++)
        {
            s += sets[i] + " ";
        }
        return s;
    }
    
    /**
     * Whether the element indexed  by i is in the same set of the one indexed by j
     * @param i
     * @param j
     * @return 
     */
    public boolean sameSet(int i, int j)
    {
        return sets[i] == sets[j];
    }
    
    /**
     * Gives the size of the set a representative of which is the case of index i
     * @param i
     * @return 
     */
    public int size(int i)
    {
        int si =0;
        //Il y a plus efficace
        for(int j = 0; j < sets.length ; j++)
        {
            if(sets[j] == sets[i])
            {
                si++;
            }
        }
        return si;
    }
    
    /**
     * Returns the index of the next element of the array in the set
     * @param i
     * @return -1 if there is no more element
     */
    public int nextIndexSet(int i)
    {
        for(int j=i+1 ; j < sets.length ; j++)
        {
            if(sets[j] == sets[i])
            {
                return j;
            }
        }
        return -1;
    }
    
    /**
     * Gives the n-th element after the element of index i in the same set
     * Not specified if n < 0
     * @param i
     * @param n
     * @return -1 if not enough elements
     */
    public int nextIndexSet(int i, int n)
    {
        int index = i;
        for(int j = i ; j < n && index != -1; j++)
        {
            index = nextIndexSet(j);
        }
        return index;
    }
    /**
     * Reset the unique number generator
     */
    private void resetUni()
    {
        uni = 0L;
    }
    
    /**
     * Gives a unique number
     * In fact, period of sizeof(long)
     * @return 
     */
    private long nextUni()
    {
        return uni++;
    }
    
    public static void main(String[] args)
    {
           System.out.println("Test d'ArraySets");
           int taille = 30;
           System.out.println("Création ; taille = " + taille);
           ArraySets sets = new ArraySets(taille);
           
           System.out.println(sets);
           
           sets.singletons();
           
           System.out.println("Chaque éléments a son singleton :\n " + sets);
           
           sets.merge(0, sets.nextSet(0));
           
           System.out.println("Fusion des deux premiers ensembles : \n" + sets);
           
           sets.add(4, 9);
           System.out.println("Ajout de la case d'index 9 à l'ensemble "
                   + "dont fait partie la case d'index 4 :\n" + sets);
           
           System.out.println("Les cases d'index 0 et 1 sont-elles dans le même ensemble ?" );
           if(sets.sameSet(0, 1))
           {
               System.out.println("oui");
           }
           else
           {
               System.out.println("non");
           }
           
           System.out.println("Les cases d'index 10 et 20 sont-elles dans le même ensemble ?" );
           if(sets.sameSet(10, 20))
           {
               System.out.println("oui");
           }
           else
           {
               System.out.println("non");
           }
           
           sets.clear();
           System.out.println("Effacer le contenu :\n" + sets);
    }
}
