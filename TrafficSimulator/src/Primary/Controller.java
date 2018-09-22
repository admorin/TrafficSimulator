package Primary;

import GUI.Simulation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

public class Controller extends Thread{

    private Simulation simulation;
    public volatile static int activeCount = 0; // used to see how many threads need to move before draw update
    public static final Object countLock = new Object(); // Used to lock the activeCount when changed
    public Timeline spawnInterval = new Timeline();
    public int spawnDuration = 0;

    public Controller(GraphicsContext gc)
    {
        this.simulation = new Simulation(gc);

        spawnInterval.setCycleCount(2);
        spawnInterval.setAutoReverse(true);
        spawnInterval.getKeyFrames().add(new KeyFrame(Duration.millis(spawnDuration*1000)));
    }

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Controller Thread" +
                    " is running");

            // Create a new Display animation that will just keep
            // moving the traffic threads and drawing their new spots

            Animation a = new Animation();
            a.start();

            TestTCS test = new TestTCS();
            test.begin();
        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught: " + e.toString());
        }
    }

    // Button press action to spawn a car
    //
    public void spawnCar(){
        simulation.spawnCar();
    }

    // Button press action to remove all the traffic threads
    //
    public void reset(){
        simulation.clear();
    }

    public void highTrafficMode()
    {
        spawnInterval.stop();
        spawnDuration = 3;
        spawnInterval.getKeyFrames().add(new KeyFrame(Duration.millis(spawnDuration*1000)));
        spawnInterval.playFromStart();
    }

    public void moderateTrafficMode()
    {
        spawnInterval.stop();
        spawnDuration = 5;
        spawnInterval.getKeyFrames().add(new KeyFrame(Duration.millis(spawnDuration*1000)));
        spawnInterval.playFromStart();
    }

    public void lowTrafficMode()
    {
        spawnInterval.stop();
        spawnDuration = 7;
        spawnInterval.getKeyFrames().add(new KeyFrame(Duration.millis(spawnDuration*1000)));
        spawnInterval.playFromStart();
    }

    public void combinationMode()
    {

    }

    // Inner gui updating class that moves and redraws traffic on a timer
    //
    class Animation extends AnimationTimer
    {

        public Animation(){}

        @Override
        public void handle(long now) // called by JavaFX at 60Hz
        {
            System.out.println(spawnInterval.getCurrentTime().toSeconds());
            
            // activeCount will be 0 when every single car thread has moved
            // then the simulation can redraw all traffic at the new positions and notify
            // each thread it can move again
            if (activeCount == 0) {
                //simulation.updateSpots();
                simulation.drawTraffic(); // loop over all traffic and draw new positions
                simulation.freeTraffic(); // notify all
            } else {
                // this shouldn't happen
                System.out.println("threads count at " + activeCount);
            }
        }

    }

}
