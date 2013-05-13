package core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to store the options
 *
 *   pierre
 */
public class Options
{
    //Options vidéos

    private boolean fullscreen;
    private boolean dimScreen;// Aux dimensions de l'écran ?
    //Si non, quelles dimensions ?
    private int dimX;
    private int dimY;
    //Options du son
    private boolean music;
    //Entre 0 et 1
    private float musicVolume;
    private Properties properties;
    //Pour la carte par défaut
    private int defaultMapSeed;
    private int defaultWallSize;
    private int defaultCorridorSize;
    private int defaultSizeXMap;
    private int defaultSizeYMap;
    
    private String fileProperties;
    private String name = "Player";

    public Options(String fileProperties)
    {
        this.fileProperties = fileProperties;
        properties = new Properties();
        FileInputStream fis;
        try
        {
            fis = new FileInputStream(fileProperties);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            properties.load(isr);

            fullscreen = Boolean.parseBoolean(properties.getProperty("fullscreen"));
            dimScreen = Boolean.parseBoolean(properties.getProperty("dimScreen"));
            dimX = Integer.parseInt(properties.getProperty("dimX", "800"));
            dimY = Integer.parseInt(properties.getProperty("dimY", "480"));
            music = Boolean.parseBoolean(properties.getProperty("music"));
            musicVolume = Float.parseFloat(properties.getProperty("musicVolume"));
            if (musicVolume > 1 || musicVolume < 0)
            {
                musicVolume = 0.4f;
            }
            defaultMapSeed = Integer.parseInt(properties.getProperty("defaultMapSeed"));
            defaultWallSize = Integer.parseInt(properties.getProperty("defaultWallSize"));
            defaultCorridorSize = Integer.parseInt(properties.getProperty("defaultCorridorSize"));
            defaultSizeXMap = Integer.parseInt(properties.getProperty("defaultSizeXMap"));
            defaultSizeYMap = Integer.parseInt(properties.getProperty("defaultSizeYMap"));
            
            name = properties.getProperty("name");
        } catch (IOException | NumberFormatException ex)
        {
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE,
                    "Impossible d'initialiser les propriétés avec le fichier", ex);
            fullscreen = true;
            dimScreen = true;
            dimX = 800;
            dimY = 480;
            music = true;
            musicVolume = 0.4f;
            defaultMapSeed = 100;
            defaultWallSize = 2;
            defaultCorridorSize = 8;
            defaultSizeXMap = 32;
            defaultSizeYMap = 32;
            name = "Player";
        }

    }

    public boolean isFullscreen()
    {
        return fullscreen;
    }

    public boolean isDimScreen()
    {
        return dimScreen;
    }

    public int getDimX()
    {
        return dimX;
    }

    public int getDimY()
    {
        return dimY;
    }

    public int getDefaultMapSeed()
    {
        return defaultMapSeed;
    }

    public int getDefaultWallSize()
    {
        return defaultWallSize;
    }

    public int getDefaultCorridorSize()
    {
        return defaultCorridorSize;
    }

    public int getDefaultSizeXMap()
    {
        return defaultSizeXMap;
    }

    public int getDefaultSizeYMap()
    {
        return defaultSizeYMap;
    }
    

    public boolean isMusic()
    {
        return music;
    }

    public float getMusicVolume()
    {
        return musicVolume;
    }
    
    public String getName() {
        return name;
    }

    public void setFullscreen(boolean fullscreen)
    {
        this.fullscreen = fullscreen;
    }

    public void setDimScreen(boolean dimScreen)
    {
        this.dimScreen = dimScreen;
    }

    public void setDimX(int dimX)
    {
        this.dimX = dimX;
    }

    public void setDimY(int dimY)
    {
        this.dimY = dimY;
    }

    public void setMusic(boolean music)
    {
        this.music = music;
    }

    public void setMusicVolume(float musicVolume)
    {
        if (musicVolume > 1 || musicVolume < 0)
        {
            this.musicVolume = 0.4f;
        } else
        {
            this.musicVolume = musicVolume;
        }
    }

    public void setDefaultMapSeed(int defaultMapSeed)
    {
        this.defaultMapSeed = defaultMapSeed;
    }

    public void setDefaultWallSize(int defaultWallSize)
    {
        this.defaultWallSize = defaultWallSize;
    }

    public void setDefaultCorridorSize(int defaultCorridorSize)
    {
        this.defaultCorridorSize = defaultCorridorSize;
    }

    public void setDefaultSizeXMap(int defaultSizeXMap)
    {
        this.defaultSizeXMap = defaultSizeXMap;
    }

    public void setDefaultSizeYMap(int defaultSizeYMap)
    {
        this.defaultSizeYMap = defaultSizeYMap;
    }
    
    
    
    private void registerProperties()
    {
        properties.setProperty("fullscreen", Boolean.toString(fullscreen));
        properties.setProperty("dimScreen", Boolean.toString(dimScreen));
        properties.setProperty("dimX", Integer.toString(dimX));
        properties.setProperty("dimY", Integer.toString(dimY));
        properties.setProperty("music", Boolean.toString(music));
        properties.setProperty("musicVolume", Float.toString(musicVolume));
        properties.setProperty("name", name);
        properties.setProperty("defaultMapSeed", Integer.toString(defaultMapSeed));
        properties.setProperty("defaultWallSize", Integer.toString(defaultWallSize));
        properties.setProperty("defaultCorridorSize", Integer.toString(defaultCorridorSize));
        properties.setProperty("defaultSizeXMap", Integer.toString(defaultSizeXMap));
        properties.setProperty("defaultSizeYMap", Integer.toString(defaultSizeYMap));
    }
    
    
    public void save() throws IOException
    {
        FileOutputStream os = new FileOutputStream(fileProperties);
        OutputStreamWriter osr =  new OutputStreamWriter(os, "UTF-8");
        registerProperties();
        properties.store(osr, "Options pour Au coeur du RISC.");
    }

    /**
     * Pour créer le premier fichier de propriétés
     *
     * @param args
     */
    public static void main(String[] args)
    {
        Options options = new Options("res/options.properties");
        try
        {
            options.save();
        } catch (IOException ex)
        {
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, "Impossible de sauvegarder le fichier d'options", ex);
        }
    }
}
