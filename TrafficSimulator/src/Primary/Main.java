package Primary;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * This is the primary controller for the program. This will create the intersection, create the GUI, and give the
     * intersection to the TestTCS program for them to run. This will also call run() on the TestTCS code.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{


        // Setup border pane with HBox of two buttons along the top and
        // canvas to display the simulation in the center, also initialize
        // the controller with something to draw on

        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Label controlLabel = new Label("Mode:");

        VBox controls = new VBox(15);

        Button highTraffic = new Button("High Traffic");
        Button moderateTraffic = new Button("Moderate Traffic");
        Button lowTraffic = new Button("Low Traffic");
        Button combinationTraffic = new Button("Combination");
        Button emergency = new Button("Emergency");
        Button reset = new Button("Reset");

        Controller controller = new Controller(gc);
        controller.start();

        // Handle button press actions

        highTraffic.setOnMousePressed(e -> {
            controller.spawnCar();
        });

        moderateTraffic.setOnMousePressed(e -> {
            controller.spawnCar();
        });

        lowTraffic.setOnMousePressed(e -> {
            controller.spawnCar();
        });

        combinationTraffic.setOnMousePressed(e -> {
            controller.spawnCar();
        });

        emergency.setOnMousePressed(e -> {
            controller.spawnCar();
        });

        reset.setOnMousePressed(e -> {
            controller.clearTraffic();
        });


        // Setup the scene

        controls.getChildren().addAll(controlLabel, highTraffic, moderateTraffic, lowTraffic, combinationTraffic,
                emergency, reset);
        root.setRight(controls);
        root.setLeft(canvas);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
