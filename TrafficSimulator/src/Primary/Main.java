package Primary;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * This is the primary controller for the program. This will create the intersection, create the Graphics, and give the
     * intersection to the TestTCS program for them to run. This will also call run() on the TestTCS code.
     */

    @Override
    public void start(Stage primaryStage) {


        // Setup border pane with HBox of two buttons along the top and
        // canvas to display the simulation in the center, also initialize
        // the controller with something to draw on
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(550, 550);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        VBox controlBox = new VBox(15);
        controlBox.setPrefSize(150, 550);
        controlBox.setStyle("-fx-background-color: #e0e0e0;");

        Label controlLabel = new Label("Modes:");

        Button heavyButton = new Button("Heavy Traffic");
        Button moderateButton = new Button("Moderate Traffic");
        Button lightButton = new Button("Light Traffic");
        Button combinationButton = new Button("Combination Traffic");
        Button spawnButton = new Button("Spawn");
        Button resetButton = new Button("Reset");

        Controller controller = new Controller(gc);
        controller.start();

        // Handle button press actions
        spawnButton.setOnMousePressed(e -> controller.spawnCar());
        resetButton.setOnMousePressed(e -> controller.clearTraffic());


        // Setup the scene
        controlBox.getChildren().addAll(controlLabel, heavyButton, moderateButton, lightButton,
                                        combinationButton, spawnButton, resetButton);
        root.setRight(controlBox);
        root.setLeft(canvas);

        Scene scene = new Scene(root, 700, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Control System: Testbed");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
