package core;

/**
 * A class to mesure delays and activate some actions.
 * Implement the interface IimedAction
 *   pierre
 */
public class Timer
{
    private int delay;
    
    private int time;
    
    private int chrono;
    
    
    private TimedAction timedAction;
    
    private boolean now;
    /**
     * @param interval time interval in milliseconds
     */
    public Timer(int interval)
    {
        this.delay = interval;
        time = 0;
        now = false;
        chrono = 0;
        
        this.timedAction = new defaultAction();
    }
    
    /**
     * To be called in the main update method of the game (or any update method that gets its delta).
     * @param delta 
     */
    public void update(int delta)
    {
        now = false;
        time += delta;
        chrono += delta;
        if(time >= delay)
        {
            now = true;
            time = 0;
            timedAction.run();
        }
    }
    
    public void reset()
    {
        time = 0;
    }
    
    /**
     * If you prefer not to implement TimedAction but rather act when there is a tick, 
     * use that method.
     * @return 
     */
    public boolean tick()
    {
        return now;
    }
    
    /**
     * Return the elapsed time after the previous tick.
     * @return 
     */
    public int time()
    {
        return time;
    }
    
    /**
     * To measure a time witth that timer
     * @return 
     */
    public int getChrono()
    {
        return chrono;
    }
    
    public void resetChrono()
    {
        chrono = 0;
    }
    
    /**
     * Delay of the timer
     * @return 
     */
    public int getDelay()
    {
        return delay;
    }
    
    /**
     * Set the time action you want
     * @param timedAction 
     */
    public void setTimedAction(TimedAction timedAction)
    {
        this.timedAction = timedAction;
    }
    
    public interface TimedAction
    {
        public void run();
    }
    
    private class defaultAction implements TimedAction
    {

        @Override
        public void run()
        {
        }
        
    }
}
