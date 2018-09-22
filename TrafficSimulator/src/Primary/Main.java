package Primary;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        Canvas canvas = new Canvas(550, 550);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        HBox buttonPane = new HBox(30);
        Button carButton = new Button("New Car");
        Button clearButton = new Button("Clear Traffic");

        Controller controller = new Controller(gc);
        controller.start();

        // Handle button press actions

        carButton.setOnMousePressed(e -> {
            controller.spawnCar();
        });

        clearButton.setOnMousePressed(e -> {
            controller.clearTraffic();
        });


        // Setup the scene

        buttonPane.getChildren().addAll(carButton,clearButton);
        root.setTop(buttonPane);
        root.setCenter(canvas);

        Scene scene = new Scene(root, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
