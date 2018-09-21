package Primary;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * This is the primary controller for the program. This will create the intersection, create the GUI, and give the
     * intersection to the TestTCS program for them to run. This will also call run() on the TestTCS code.
     */


    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Pane mainPane = new Pane();

        Controller controller = new Controller();
        controller.start();

        root.getChildren().add(mainPane);

        Scene scene = new Scene(root, 600, 450);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
