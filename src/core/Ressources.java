package core;

import map.Tile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

/**
 * Cette classe définie tous les champs constants de toutes les unités et de
 * tous les batiments. TODO : créer une classe Ressources_cont par type
 * d'éléments pour faciliter l'ajout de nouvelles ressources.
 *
 *    
 */
public class Ressources
{

    final private SpriteSheet sheet;
    final private int att_speed_bit;
    final private int dep_speed_bit;
    final private int damage_bit;
    final private int range_bit;
    final private int heigth_bit;
    final private int width_bit;
    final private int armor_bit;
    final private int cons_time_bit;
    final private int energy_cost_bit;
    final private int warm_cost_bit;
    final private Image logo_bit;
    final private Image[] sprites_bit;
    final private Sound cons_bit;
    final private Sound sel_bit;
    final private Sound att_bit;
    final private Sound des_bit;
    /**
     *
     */
    final private int att_speed_octet;
    final private int dep_speed_octet;
    final private int damage_octet;
    final private int range_octet;
    final private int heigth_octet;
    final private int width_octet;
    final private int armor_octet;
    final private int cons_time_octet;
    final private int energy_cost_octet;
    final private int warm_cost_octet;
    final private Image logo_octet;
    final private Image[] sprites_octet;
    final private Sound cons_octet;
    final private Sound sel_octet;
    final private Sound att_octet;
    final private Sound des_octet;
    /**
     *
     */
    final private int att_speed_bombe;
    final private int dep_speed_bombe;
    final private int damage_bombe;
    final private int range_bombe;
    final private int heigth_bombe;
    final private int width_bombe;
    final private int armor_bombe;
    final private int cons_time_bombe;
    final private int energy_cost_bombe;
    final private int warm_cost_bombe;
    final private Image logo_bombe;
    final private Image[] sprites_bombe;
    final private Sound cons_bombe;
    final private Sound sel_bombe;
    final private Sound att_bombe;
    final private Sound des_bombe;
    /**
     *
     */
    final private int att_speed_mult;
    final private int dep_speed_mult;
    final private int damage_mult;
    final private int range_mult;
    final private int heigth_mult;
    final private int width_mult;
    final private int armor_mult;
    final private int cons_time_mult;
    final private int energy_cost_mult;
    final private int warm_cost_mult;
    final private Image logo_mult;
    final private Image[] sprites_mult;
    final private Sound cons_mult;
    final private Sound sel_mult;
    final private Sound att_mult;
    final private Sound des_mult;
    /**
     *
     * Batiments
     */
    final private int damage_antivirus;
    final private int att_speed_antivirus;
    final private int range_antivirus;
    final private int heigth_antivirus;
    final private int width_antivirus;
    final private int armor_antivirus;
    final private int cons_time_antivirus;
    final private int energy_cost_antivirus;
    final private int warm_cost_antivirus;
    final private Image logo_antivirus;
    final private Tile[][] tiles_antivirus;
    final private Sound cons_antivirus;
    final private Sound sel_antivirus;
    final private Sound des_antivirus;
    /**
     *
     */
    final private int range_scan;
    final private int heigth_scan;
    final private int width_scan;
    final private int armor_scan;
    final private int cons_time_scan;
    final private int energy_cost_scan;
    final private int warm_cost_scan;
    final private Image logo_scan;
    final private Tile[][] tiles_scan;
    final private Sound cons_scan;
    final private Sound sel_scan;
    final private Sound des_scan;
    final private int range_kernel;
    final private int heigth_kernel;
    final private int width_kernel;
    final private int armor_kernel;
    final private int cons_time_kernel;
    final private int energy_cost_kernel;
    final private int warm_cost_kernel;
    final private Image logo_kernel;
    final private Tile[][] tiles_kernel;
    final private Sound cons_kernel;
    final private Sound sel_kernel;
    final private Sound des_kernel;
    /**
     *
     */
    final private int range_cond;
    final private int heigth_cond;
    final private int width_cond;
    final private int armor_cond;
    final private int cons_time_cond;
    final private int energy_cost_cond;
    final private int warm_cost_cond;
    final private Image logo_cond;
    final private Tile[][] tiles_cond;
    final private Sound cons_cond;
    final private Sound sel_cond;
    final private Sound des_cond;
    /**
     *
     */
    final private int range_plugin;
    final private int heigth_plugin;
    final private int width_plugin;
    final private int armor_plugin;
    final private int cons_time_plugin;
    final private int energy_cost_plugin;
    final private int warm_cost_plugin;
    final private Tile[][] tiles_plugin;
    final private Image logo_plugin;
    final private Sound cons_plugin;
    final private Sound sel_plugin;
    final private Sound des_plugin;
    /**
     *
     */
    final private int range_event;
    final private int heigth_event;
    final private int width_event;
    final private int armor_event;
    final private int cons_time_event;
    final private int energy_cost_event;
    final private int warm_cost_event;
    final private Image logo_event;
    final private Tile[][] tiles_event;
    final private Sound cons_event;
    final private Sound sel_event;
    final private Sound des_event;
    // Décors
    final private Image wall;

    /**
     * Attention, att_speed > 0 obligatoirement.
     *
     * @param sheet
     * @throws SlickException
     */
    public Ressources(SpriteSheet sheet) throws SlickException
    {
        this.sheet = sheet;

        att_speed_bit = 5;
        dep_speed_bit = 4;
        damage_bit = 5;
        range_bit = 50;
        heigth_bit = 1;
        width_bit = 1;
        armor_bit = 0;
        cons_time_bit = 15;
        energy_cost_bit = 70;
        warm_cost_bit = 10;
        sprites_bit = loadUnitSprites(0);
        logo_bit = sprites_bit[0];
        cons_bit = new Sound("res/snd/bip1.ogg");
        des_bit = new Sound("res/snd/menuUFO.ogg");
        att_bit = new Sound("res/snd/tirlaser.ogg");
        sel_bit = new Sound("res/snd/bip2.ogg");


        att_speed_octet = 6;
        dep_speed_octet = 3;
        damage_octet = 20;
        range_octet = 50;
        heigth_octet = 1;
        width_octet = 1;
        armor_octet = 1;
        cons_time_octet = 30;
        energy_cost_octet = 200;
        warm_cost_octet = 60;
        sprites_octet = new Image[4];
        sprites_octet[0] = sheet.getSubImage(4, 0);
        sprites_octet[1] = sheet.getSubImage(4, 1);
        sprites_octet[2] = sprites_octet[0];
        sprites_octet[3] = sprites_octet[1];
        logo_octet = sprites_octet[0];
        cons_octet = new Sound("res/snd/repetedbip.ogg");
        des_octet = new Sound("res/snd/SFB-robot_12.ogg");
        att_octet = new Sound("res/snd/sf_laser_18.ogg");
        sel_octet = new Sound("res/snd/comp3.ogg");


        att_speed_bombe = 4;
        dep_speed_bombe = 2;
        damage_bombe = 0;
        range_bombe = 100;
        heigth_bombe = 1;
        width_bombe = 1;
        armor_bombe = 0;
        cons_time_bombe = 10;
        energy_cost_bombe = 100;
        warm_cost_bombe = 50;
        sprites_bombe = new Image[4];
        sprites_bombe[0] = sheet.getSubImage(1, 0);
        for (int i = 1; i < 4; i++)
        {
            sprites_bombe[i] = sprites_bombe[0];
        }
        logo_bombe = sprites_bombe[0];
        cons_bombe = new Sound("res/snd/menuUFO.ogg");
        des_bombe = new Sound("res/snd/boomzarbi.ogg");
        att_bombe = new Sound("res/snd/SFB-explosion2.ogg");
        sel_bombe = new Sound("res/snd/interference2.ogg");


        att_speed_mult = 1;
        dep_speed_mult = 1;
        damage_mult = 100;
        range_mult = 100;
        heigth_mult = 1;
        width_mult = 1;
        armor_mult = 1;
        cons_time_mult = 15;
        energy_cost_mult = 200;
        warm_cost_mult = 60;
        sprites_mult = loadUnitSprites(3);
        logo_mult = sprites_mult[0];
        cons_mult = new Sound("res/snd/moteur.ogg");
        des_mult = new Sound("res/snd/boomzarbi.ogg");
        att_mult = new Sound("res/snd/sf_explosion_01.ogg");
        sel_mult = new Sound("res/snd/contact.ogg");

        damage_antivirus = 100;
        att_speed_antivirus = 1;
        range_antivirus = 200;
        heigth_antivirus = 2;
        width_antivirus = 2;
        armor_antivirus = 2;
        cons_time_antivirus = 8;
        energy_cost_antivirus = 200;
        warm_cost_antivirus = 100;
        tiles_antivirus = loadTiles(26, 0, width_antivirus, heigth_antivirus, Color.blue, 0);
        logo_antivirus = tiles_antivirus[1][1].getImage();
        cons_antivirus = new Sound("res/snd/moteur.ogg");
        des_antivirus = new Sound("res/snd/boomzarbi.ogg");
        sel_antivirus = new Sound("res/snd/contact.ogg");

        range_scan = 200;
        heigth_scan = 2;
        width_scan = 2;
        armor_scan = 0;
        cons_time_scan = 8;
        energy_cost_scan = 100;
        warm_cost_scan = 50;
        tiles_scan = loadTiles(9, 0, width_scan, heigth_scan, Color.blue, 0);
        logo_scan = tiles_scan[1][1].getImage();
        cons_scan = new Sound("res/snd/moteur.ogg");
        des_scan = new Sound("res/snd/boomzarbi.ogg");
        sel_scan = new Sound("res/snd/contact.ogg");

        range_kernel = 80;
        heigth_kernel = 4;
        width_kernel = 4;
        armor_kernel = 0;
        cons_time_kernel = 8;
        energy_cost_kernel = 600;
        warm_cost_kernel = 300;
        tiles_kernel = loadTiles(5, 0, width_kernel, heigth_kernel, Color.blue, 0);
        logo_kernel = tiles_kernel[0][0].getImage();
        cons_kernel = new Sound("res/snd/moteur.ogg");
        des_kernel = new Sound("res/snd/boomzarbi.ogg");
        sel_kernel = new Sound("res/snd/contact.ogg");

        range_cond = 40;
        heigth_cond = 2;
        width_cond = 2;
        armor_cond = 0;
        cons_time_cond = 8;
        energy_cost_cond = 70;
        warm_cost_cond = 100;
        tiles_cond = loadTiles(24, 0, width_cond, heigth_cond, Color.blue, 0);
        logo_cond = tiles_cond[0][1].getImage();
        cons_cond = new Sound("res/snd/moteur.ogg");
        des_cond = new Sound("res/snd/boomzarbi.ogg");
        sel_cond = new Sound("res/snd/contact.ogg");

        range_plugin = 50;
        heigth_plugin = 2;
        width_plugin = 2;
        armor_plugin = 0;
        cons_time_plugin = 8;
        energy_cost_plugin = 70;
        warm_cost_plugin = 50;
        tiles_plugin = loadTiles(11, 0, width_plugin, heigth_plugin, Color.blue, 0);
        logo_plugin = tiles_plugin[0][0].getImage();
        cons_plugin = new Sound("res/snd/moteur.ogg");
        des_plugin = new Sound("res/snd/boomzarbi.ogg");
        sel_plugin = new Sound("res/snd/contact.ogg");

        range_event = 20;
        heigth_event = 2;
        width_event = 2;
        armor_event = 0;
        cons_time_event = 8;
        energy_cost_event = 70;
        warm_cost_event = 10;
        tiles_event = loadTiles(22, 0, width_event, heigth_event, Color.blue, 0);
        logo_event = tiles_event[0][0].getImage();
        cons_event = new Sound("res/snd/moteur.ogg");
        des_event = new Sound("res/snd/boomzarbi.ogg");
        sel_event = new Sound("res/snd/contact.ogg");

        wall = sheet.getSubImage(2, 3);
    }

    /**
     * Loads several tiles to create a big image. Used for buildings
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public Tile[][] loadTiles(int x, int y, int width, int height, Color col, int weight)
    {
        Tile[][] imgs = new Tile[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                imgs[i][j] = new Tile(sheet.getSubImage(x + i, y + j), col, weight);
            }
        }
        return imgs;
    }

    public Tile loadTile(int x, int y, Color col, int weight)
    {
        return new Tile(sheet.getSubImage(x, y), col, weight);
    }

    /**
     * Charger les 4 tiles haut, droite bas gauche pour les unités qui ont les
     * quatre
     */
    public Image[] loadUnitSprites(int i)
    {
        Image[] spri = new Image[4];
        for (int j = 0; j < 4; j++)
        {
            spri[j] = sheet.getSubImage(i, j);
        }
        return spri;
    }

    public Tile getWall()
    {
        Tile tile = new Tile(wall, Color.red, 0);
        //tile.addLayer(sheet.getSubImage(21, 0));
        return tile;
    }

    public Tile[][] getBackground()
    {
        return loadTiles(13, 0, 8, 8, Color.black, 100);
    }

    public Image getLogo_antivirus()
    {
        return logo_antivirus;
    }

    public Image getLogo_scan()
    {
        return logo_scan;
    }

    public Image getLogo_kernel()
    {
        return logo_kernel;
    }

    public Image getLogo_cond()
    {
        return logo_cond;
    }

    public Image getLogo_plugin()
    {
        return logo_plugin;
    }

    public Image getLogo_event()
    {
        return logo_event;
    }

    public int getAtt_speed_bit()
    {
        return att_speed_bit;
    }

    public int getDep_speed_bit()
    {
        return dep_speed_bit;
    }

    public int getDamage_bit()
    {
        return damage_bit;
    }

    public int getAtt_speed_octet()
    {
        return att_speed_octet;
    }

    public int getDep_speed_octet()
    {
        return dep_speed_octet;
    }

    public int getDamage_octet()
    {
        return damage_octet;
    }

    public int getAtt_speed_bombe()
    {
        return att_speed_bombe;
    }

    public int getDep_speed_bombe()
    {
        return dep_speed_bombe;
    }

    public int getDamage_bombe()
    {
        return damage_bombe;
    }

    public int getAtt_speed_mult()
    {
        return att_speed_mult;
    }

    public int getDep_speed_mult()
    {
        return dep_speed_mult;
    }

    public int getDamage_mult()
    {
        return damage_mult;
    }

    public int getRange_bit()
    {
        return range_bit;
    }

    public int getHeigth_bit()
    {
        return heigth_bit;
    }

    public int getWidth_bit()
    {
        return width_bit;
    }

    public int getArmor_bit()
    {
        return armor_bit;
    }

    public int getCons_time_bit()
    {
        return cons_time_bit;
    }

    public int getEnergy_cost_bit()
    {
        return energy_cost_bit;
    }

    public int getWarm_cost_bit()
    {
        return warm_cost_bit;
    }

    public int getRange_octet()
    {
        return range_octet;
    }

    public int getHeigth_octet()
    {
        return heigth_octet;
    }

    public int getWidth_octet()
    {
        return width_octet;
    }

    public int getArmor_octet()
    {
        return armor_octet;
    }

    public int getCons_time_octet()
    {
        return cons_time_octet;
    }

    public int getEnergy_cost_octet()
    {
        return energy_cost_octet;
    }

    public int getWarm_cost_octet()
    {
        return warm_cost_octet;
    }

    public int getRange_bombe()
    {
        return range_bombe;
    }

    public int getHeigth_bombe()
    {
        return heigth_bombe;
    }

    public int getWidth_bombe()
    {
        return width_bombe;
    }

    public int getArmor_bombe()
    {
        return armor_bombe;
    }

    public int getCons_time_bombe()
    {
        return cons_time_bombe;
    }

    public int getEnergy_cost_bombe()
    {
        return energy_cost_bombe;
    }

    public int getWarm_cost_bombe()
    {
        return warm_cost_bombe;
    }

    public int getRange_mult()
    {
        return range_mult;
    }

    public int getHeigth_mult()
    {
        return heigth_mult;
    }

    public int getWidth_mult()
    {
        return width_mult;
    }

    public int getArmor_mult()
    {
        return armor_mult;
    }

    public int getCons_time_mult()
    {
        return cons_time_mult;
    }

    public int getEnergy_cost_mult()
    {
        return energy_cost_mult;
    }

    public int getWarm_cost_mult()
    {
        return warm_cost_mult;
    }

    public Sound getCons_bit()
    {
        return cons_bit;
    }

    public Sound getDes_bit()
    {
        return des_bit;
    }

    public Sound getSel_bit()
    {
        return sel_bit;
    }

    public Sound getCons_octet()
    {
        return cons_octet;
    }

    public Sound getDes_octet()
    {
        return des_octet;
    }

    public Sound getSel_octet()
    {
        return sel_octet;
    }

    public Sound getCons_bombe()
    {
        return cons_bombe;
    }

    public Sound getDes_bombe()
    {
        return des_bombe;
    }

    public Sound getSel_bombe()
    {
        return sel_bombe;
    }

    public Sound getCons_mult()
    {
        return cons_mult;
    }

    public Sound getDes_mult()
    {
        return des_mult;
    }

    public Sound getSel_mult()
    {
        return sel_mult;
    }

    public int getRange_antivirus()
    {
        return range_antivirus;
    }

    public int getHeigth_antivirus()
    {
        return heigth_antivirus;
    }

    public int getWidth_antivirus()
    {
        return width_antivirus;
    }

    public int getArmor_antivirus()
    {
        return armor_antivirus;
    }

    public int getCons_time_antivirus()
    {
        return cons_time_antivirus;
    }

    public int getEnergy_cost_antivirus()
    {
        return energy_cost_antivirus;
    }

    public int getWarm_cost_antivirus()
    {
        return warm_cost_antivirus;
    }

    public Sound getCons_antivirus()
    {
        return cons_antivirus;
    }

    public Sound getSel_antivirus()
    {
        return sel_antivirus;
    }

    public Sound getDes_antivirus()
    {
        return des_antivirus;
    }

    public int getRange_scan()
    {
        return range_scan;
    }

    public int getHeigth_scan()
    {
        return heigth_scan;
    }

    public int getWidth_scan()
    {
        return width_scan;
    }

    public int getArmor_scan()
    {
        return armor_scan;
    }

    public int getCons_time_scan()
    {
        return cons_time_scan;
    }

    public int getEnergy_cost_scan()
    {
        return energy_cost_scan;
    }

    public int getWarm_cost_scan()
    {
        return warm_cost_scan;
    }

    public Sound getCons_scan()
    {
        return cons_scan;
    }

    public Sound getSel_scan()
    {
        return sel_scan;
    }

    public Sound getDes_scan()
    {
        return des_scan;
    }

    public int getRange_kernel()
    {
        return range_kernel;
    }

    public int getHeigth_kernel()
    {
        return heigth_kernel;
    }

    public int getWidth_kernel()
    {
        return width_kernel;
    }

    public int getArmor_kernel()
    {
        return armor_kernel;
    }

    public int getCons_time_kernel()
    {
        return cons_time_kernel;
    }

    public int getEnergy_cost_kernel()
    {
        return energy_cost_kernel;
    }

    public int getWarm_cost_kernel()
    {
        return warm_cost_kernel;
    }

    public Sound getCons_kernel()
    {
        return cons_kernel;
    }

    public Sound getSel_kernel()
    {
        return sel_kernel;
    }

    public Sound getDes_kernel()
    {
        return des_kernel;
    }

    public int getRange_cond()
    {
        return range_cond;
    }

    public int getHeigth_cond()
    {
        return heigth_cond;
    }

    public int getWidth_cond()
    {
        return width_cond;
    }

    public int getArmor_cond()
    {
        return armor_cond;
    }

    public int getCons_time_cond()
    {
        return cons_time_cond;
    }

    public int getEnergy_cost_cond()
    {
        return energy_cost_cond;
    }

    public int getWarm_cost_cond()
    {
        return warm_cost_cond;
    }

    public Sound getCons_cond()
    {
        return cons_cond;
    }

    public Sound getSel_cond()
    {
        return sel_cond;
    }

    public Sound getDes_cond()
    {
        return des_cond;
    }

    public int getRange_plugin()
    {
        return range_plugin;
    }

    public int getHeigth_plugin()
    {
        return heigth_plugin;
    }

    public int getWidth_plugin()
    {
        return width_plugin;
    }

    public int getArmor_plugin()
    {
        return armor_plugin;
    }

    public int getCons_time_plugin()
    {
        return cons_time_plugin;
    }

    public int getEnergy_cost_plugin()
    {
        return energy_cost_plugin;
    }

    public int getWarm_cost_plugin()
    {
        return warm_cost_plugin;
    }

    public Sound getCons_plugin()
    {
        return cons_plugin;
    }

    public Sound getSel_plugin()
    {
        return sel_plugin;
    }

    public Sound getDes_plugin()
    {
        return des_plugin;
    }

    public int getRange_event()
    {
        return range_event;
    }

    public int getHeigth_event()
    {
        return heigth_event;
    }

    public int getWidth_event()
    {
        return width_event;
    }

    public int getArmor_event()
    {
        return armor_event;
    }

    public int getCons_time_event()
    {
        return cons_time_event;
    }

    public int getEnergy_cost_event()
    {
        return energy_cost_event;
    }

    public int getWarm_cost_event()
    {
        return warm_cost_event;
    }

    public Sound getCons_event()
    {
        return cons_event;
    }

    public Sound getSel_event()
    {
        return sel_event;
    }

    public Sound getDes_event()
    {
        return des_event;
    }

    public int getDamage_antivirus()
    {
        return damage_antivirus;
    }

    public int getAtt_speed_antivirus()
    {
        return att_speed_antivirus;
    }

    public Image getLogo_bit()
    {
        return logo_bit;
    }

    public Image getLogo_octet()
    {
        return logo_octet;
    }

    public Image getLogo_bombe()
    {
        return logo_bombe;
    }

    public Image getLogo_mult()
    {
        return logo_mult;
    }

    public Tile[][] getTiles_kernel()
    {
        return tiles_kernel;
    }

    public Tile[][] getTiles_scan()
    {
        return tiles_scan;
    }

    public Tile[][] getTiles_plugin()
    {
        return tiles_plugin;
    }

    public Tile[][] getTiles_event()
    {
        return tiles_event;
    }

    public Tile[][] getTiles_cond()
    {
        return tiles_cond;
    }

    public Tile[][] getTiles_antivirus()
    {
        return tiles_antivirus;
    }

    public Sound getAtt_bit()
    {
        return att_bit;
    }

    public Sound getAtt_bombe()
    {
        return att_bombe;
    }

    public Sound getAtt_octet()
    {
        return att_octet;
    }

    public Sound getAtt_mult()
    {
        return att_mult;
    }

    public Image[] getSprites_bit()
    {
        return sprites_bit;
    }

    public Image[] getSprites_bombe()
    {
        return sprites_bombe;
    }

    public Image[] getSprites_octet()
    {
        return sprites_octet;
    }

    public Image[] getSprites_mult()
    {
        return sprites_mult;
    }
}
