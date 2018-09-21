package Primary;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller extends Thread{

    private Intersection intersection;
    private Pane mainPane;

    public Controller(){
        intersection = new Intersection();
        intersection.start();
    }

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Controller Thread" +
                    " is running");

            TestTCS test = new TestTCS(intersection);
            test.begin();

            //END

            System.out.println("HERE");

        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught");
        }
    }

    public Intersection getIntersection(){
        return intersection;
    }

}
