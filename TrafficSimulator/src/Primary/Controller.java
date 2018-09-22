package Primary;

import GUI.Simulation;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class Controller extends Thread{

    private Simulation sim;
    public volatile static int threadCount = 0; // used to see how many threads need to move before draw update
    public static final Object countLock = new Object(); // Used to lock the threadCount when changed


    public Controller(GraphicsContext gc){
        this.sim = new Simulation(gc);
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
        sim.spawnCar();
    }

    // Button press action to remove all the traffic threads
    //
    public void clearTraffic(){
        sim.clear();
    }


    // Inner gui updating class that moves and redraws traffic on a timer
    //
    class Animation extends AnimationTimer
    {

        public Animation(){}

        @Override
        public void handle(long now) // called by JavaFX at 60Hz
        {
            // threadCount will be 0 when every single car thread has moved
            // then the sim can redraw all traffic at the new positions and notify
            // each thread it can move again

            if (threadCount == 0) {
                //sim.updateSpots();
                sim.drawTraffic(); // loop over all traffic and draw new positions
                sim.freeTraffic(); // notify all
            } else {
                // this shouldn't happen
                System.out.println("threads count at " + threadCount);
            }
        }

    }

}
