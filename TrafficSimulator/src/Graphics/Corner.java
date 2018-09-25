package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Corner extends Ground{

    private RoadDisplay road;
    private GraphicsContext gc;
    private double roadLength;
    private int size = 40;

    public Corner(GraphicsContext gc, RoadDisplay road, double roadLength){
        this.gc = gc;
        this.road = road;
        this.side = road.side; // if north that's top left box, east is top right, south is bottom right, etc.
        this.roadLength = roadLength;
    }

    public Pedestrian spawnPed(Ground dst){
        return new Pedestrian(gc, this, dst);
    }

    public void draw(){
        gc.setFill(Paint.valueOf("#d9d9d9"));
        x = road.x;
        y = road.y;

        if (road.side == Direction.NORTH){
            y += roadLength - size;
            x -= size;
        } else if (road.side == Direction.EAST){
            y -= size;
        } else if (road.side == Direction.SOUTH) {
            x += 100; // size of intersection square
        } else if (road.side == Direction.WEST){
            x += roadLength - size;
            y += 100;
        }

        gc.fillRect(x, y, size, size);
    }
}
