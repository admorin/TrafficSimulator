package Graphics;

import javafx.scene.canvas.GraphicsContext;

public class CarSignalDisplay {

    // LaneDisplay the light's on I'm guessing will be needed for each light to know
    // how many cars are waiting for it in a laneDisplay from a sensor
    private final LaneDisplay laneDisplay;

    private final GraphicsContext gc;
    private Boolean isRed = true;
    public double x;
    public double y;

    public CarSignalDisplay(LaneDisplay laneDisplay, GraphicsContext gc) {
        this.laneDisplay = laneDisplay;
        this.gc = gc;
    }

    // Used by cars to check if red
    //
    public Boolean isRed(){
        return isRed;
    }

    // Each light is given a position based off the laneDisplay it's for
    //
    public void setPosition(double x, double y, double offset, Boolean isVert){
        if (isVert) {
            this.x = x + offset;
            this.y = y;
        } else {
            this.x = x;
            this.y = y + offset;
        }
    }

    // Needs to change to handle yellow later too, not just green n red
    //
    public void changeColor(Boolean isRed) {
        this.isRed = isRed;
    }
}
