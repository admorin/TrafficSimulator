package Primary;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Intersection intersection = new Intersection();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();

        TestTCS test = new TestTCS(intersection);
        test.run();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
