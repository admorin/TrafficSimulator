package Primary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import javax.swing.text.Position;

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
        controlBox.setPadding(new Insets(0,0,0,5));
        controlBox.setPrefSize(150, 550);
        controlBox.setStyle("-fx-background-color: #e0e0e0;");

        Label resultLabel = new Label("");
        resultLabel.setPrefSize(150, 50);

        Label controlLabel = new Label("Modes:\n\n");
        controlLabel.setPrefSize(150, 50);

        Button rushButton = new Button("Rush Hour Traffic");
        Button heavyButton = new Button("Heavy Traffic");
        Button moderateButton = new Button("Moderate Traffic");
        Button lightButton = new Button("Light Traffic");
        Button spawnCarButton = new Button("Spawn Car");
        Button spawnEmergencyButton = new Button("Spawn Emergency");
        Button spawnPedButton = new Button("Spawn Pedestrian");
        Button resetButton = new Button("Reset");

        rushButton.setStyle("-fx-background-color: #0077b3;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");
        heavyButton.setStyle("-fx-background-color: #0077b3;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");
        moderateButton.setStyle("-fx-background-color: #0077b3;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");
        lightButton.setStyle("-fx-background-color: #0077b3;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");

        spawnCarButton.setStyle("-fx-background-color: #00994d;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");
        spawnEmergencyButton.setStyle("-fx-background-color: #00994d;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");
        spawnPedButton.setStyle("-fx-background-color: #00994d;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");

        resetButton.setStyle("-fx-background-color: #ff6666;-fx-text-fill: white;-fx-effect: dropshadow(two-pass-box , darkgray, 5, 0.0 , 4, 5);");

        rushButton.setPrefSize(140, 30);
        heavyButton.setPrefSize(140, 30);
        moderateButton.setPrefSize(140, 30);
        lightButton.setPrefSize(140, 30);
        spawnCarButton.setPrefSize(140, 30);
        spawnEmergencyButton.setPrefSize(140, 30);
        spawnPedButton.setPrefSize(140, 30);
        resetButton.setPrefSize(140, 30);

        Controller controller = new Controller(gc);
        controller.start();

        // Handle button press actions
        rushButton.setOnMousePressed(e -> controller.rushMode(controlLabel));
        heavyButton.setOnMousePressed(e -> controller.heavyMode(controlLabel));
        moderateButton.setOnMousePressed(e -> controller.moderateMode(controlLabel));
        lightButton.setOnMousePressed(e -> controller.lightMode(controlLabel));
        spawnCarButton.setOnMousePressed(e -> controller.spawnCar());
        spawnEmergencyButton.setOnMousePressed(e -> controller.spawnEmergency());
        spawnPedButton.setOnMousePressed(e -> controller.spawnPed());
        resetButton.setOnMousePressed(e -> controller.reset());


        // Setup the scene
        controlBox.getChildren().addAll(controlLabel, rushButton, heavyButton, moderateButton, lightButton, spawnCarButton, spawnEmergencyButton, spawnPedButton, resetButton, resultLabel);
        root.setRight(controlBox);
        root.setLeft(canvas);

        Scene scene = new Scene(root, 700, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Control System: Testbed");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
