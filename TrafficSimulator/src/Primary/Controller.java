package Primary;

import Graphics.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Controller extends Thread{

    private Simulation sim;
    private GraphicsContext gc;
    private TestTCS test;
    public volatile static int threadCount = 0; // used to see how many threads need to move before draw update
    public static final Object countLock = new Object(); // Used to lock the threadCount when changed
    public static final Object simLock = new Object(); // Used to lock the threadCount when changed
    private ScheduledExecutorService spawner;
    private ScheduledFuture<?> spawnInterval;

    public Controller(GraphicsContext gc){
        this.gc = gc;
        this.sim = new Simulation(gc);
    }

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Controller Thread" +
                    " is running");

            spawner = Executors.newScheduledThreadPool(1);
            spawnInterval = spawner.scheduleAtFixedRate(() -> spawnCar(), 1000000, 1000000, TimeUnit.SECONDS);

            Animation a = new Animation();
            a.start();

            this.test = new TestTCS();
            test.begin();
        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught: " + e.toString());
        }
    }

    public void extremeMode(Label label){
        label.setText("Modes:\nHeavy traffic\nPeriod = 0.2s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnBoth(), 0, 200, TimeUnit.MILLISECONDS);
    }


    public void heavyMode(Label label)
    {
        label.setText("Modes:\nHeavy traffic\nPeriod = 1s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnBoth(), 0, 1, TimeUnit.SECONDS);
    }

    public void moderateMode(Label label)
    {
        label.setText("Modes:\nModerate traffic\nPeriod = 2s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnBoth(), 0, 2, TimeUnit.SECONDS);
    }

    public void lightMode(Label label)
    {
        label.setText("Modes:\nLight traffic\nPeriod = 4s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnBoth(), 0, 4, TimeUnit.SECONDS);
    }

    public void spawnBoth()
    {
        spawnCar();
        spawnPed();
    }

    // Button press action to spawn a car
    //
    public void spawnCar(){
        sim.spawnCar();
    }

    // Button press action to spawn a pedestrian
    //
    public void spawnPed(){
        sim.spawnPed();
    }

    // Button press action to remove all the traffic threads
    //
    public void clearTraffic(Label label){
        label.setText("Modes:\n\n");
        spawnInterval.cancel(false);
        this.sim = new Simulation(gc);
        //sim.clear();
    }


    // Inner gui updating class that moves and redraws traffic on a timer
    //
    class Animation extends AnimationTimer
    {
        private Boolean ending = false; // collision is true if any cars have collided in intersection

        Animation(){}

        @Override
        public void handle(long now) // called by JavaFX at 60Hz
        {
            // threadCount will be 0 when every single car thread has moved
            // then the sim can redraw all traffic at the new positions and notify
            // each thread it can move again

            if (threadCount == 0) {
                // comment out the next two collision lines and you can test while cars
                // don't care about hitting each other

                Boolean collision = sim.updateSpots(); // checks cars for collision
                if (collision && !ending) end();

                sim.drawTraffic(); // loop over all traffic and draw new positions
                sim.freeTraffic(); // notify all
            } else {
                // this shouldn't happen
                System.out.println("threads count at " + threadCount);
            }
        }

        // Called when any cars have collided in intersection,
        // uses a timer so cars can escape the crashes
        // if they weren't involved and then stops 3s later
        //
        private void end(){
            ending = true; // so this isn't called multiple times while updating Gui
            //test.end(); // end their test controller
            sim.showEnd(); // show super ugly end popup
            spawnInterval.cancel(true);

            Timer timer = new Timer();
            timer.schedule(t, 3000);
        }


        // Timed task to clear the traffic, draw it again
        // and stop the animation updates
        //
        final TimerTask t = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {

                    sim.clear();
                    //ending = false;
                    sim.drawTraffic();
                    Animation.super.stop();
                });

            }
        };
    }

}
