package Primary;

import javafx.animation.AnimationTimer;

public class Controller extends Thread{

    private AnimationTimer animationTimer;

    public Controller(){
    }

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Controller Thread" +
                    " is running");

            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    //updateGUI();
                    //TODO Beau!!!! This is where we are going to update the GUI, can you make this one function call do all the updating?
                }
            };
            animationTimer.start();
            TestTCS test = new TestTCS();
            test.begin();
        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught");
        }
    }
}
