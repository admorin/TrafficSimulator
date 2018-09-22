package GUI;

import GUI.Ground;
import GUI.Road;
import Primary.Lanes;
import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;

public class Intersection extends Ground {

    private  GraphicsContext gc;
    private double size;
    private LinkedList<Road> roads = new LinkedList<>();

    public Intersection(GraphicsContext gc, double size){
        this.gc = gc;
        this.size = size;
        this.setPosition(gc.getCanvas().getWidth() /2 - (size/2), gc.getCanvas().getHeight() /2 - (size/2), 1);
    }

    // Draws the main square and all it's connecting roads
    //
    public void draw(){
        gc.fillRect(x , y, size, size);

        for (Road r : roads) {
            r.drawRoad();
       }
    }

    public void connectRoads(LinkedList<Road> roads){
        this.roads = roads;
    }

    public LinkedList<Road> getRoads(){
        return roads;
    }

}
