package GUI;

import Primary.Lanes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.LinkedList;
import java.util.Random;

public class Simulation {

    private GraphicsContext gc;
    private Boolean needsRefresh = true;
    private Intersection intersection;
    private int size = 80;

    private LinkedList<Car> cars = new LinkedList<Car>();

    public Simulation(GraphicsContext gc){
        this.gc = gc;
        this.intersection = new Intersection(gc, size);
        setUp(); // set up roads and connect to intersection
    }


    // Called in animation loop to notify cars they
    // can move again before being drawn
    public void freeTraffic(){
        for (Car c : cars) {
            if (c.running) c.free(); // running = false if the car has arrived
        }
    }

    // Button action that will clear out all the
    // Car threads from the simulation
    //
    public void clear(){
        needsRefresh = true;
        while (!cars.isEmpty()){
            cars.removeLast();
        }
    }

    // Redraws the intersection on each animation loop a
    // and all the traffic in it's new positions
    //
    public void drawTraffic(){
        drawInterSection();
        for (Car c : cars) {
            if (c.running){
                c.drawCar();
            }
        }
    }

    public void updateSpots(){
        for (Car c : cars) {
            if (c.running && c.needsGroundUpdate) c.updateGround();
        }
    }


    // Spawns a new car on a random start lane with a
    // random destination lane
    //
    public void spawnCar(){
        Random rn = new Random();
        LinkedList<Road> roads = intersection.getRoads();

        int range = (roads.size() - 1) + 1;
        int randomStart =  rn.nextInt(range);
        Road r1 = roads.get(randomStart);
        Lane start = r1.getRandomStart();

        int randomDest = rn.nextInt(range);
        Road r2 = roads.get(randomDest);
        Lane dest = r2.getRandomDest();

        Car c = new Car(r1.getSide(), start, dest, gc);
        cars.add(c);
    }

    // Draws the initial setup with no traffic
    //
    private void drawInterSection() {
        gc.setFill(Paint.valueOf("#383838"));
        intersection.draw();
    }


    // Creates new Intersection with four connecting roads,
    // and draws the start of the simulation
    //
    private void setUp(){

        // All this initialization trash could be put into the Intersection constructor
        // but this could be used to let users customize the type of roads they want from a GUI
        LinkedList<Road> roads = new LinkedList<Road>();

        Road up = new Road(gc, Direction.NORTH, size, intersection);
        Road down = new Road(gc, Direction.SOUTH, size, intersection);
        Road right = new Road(gc, Direction.EAST, size, intersection);
        Road left = new Road(gc, Direction.WEST, size, intersection); /// left was 3, and right was 2

        roads.add(up);
        roads.add(down);
        roads.add(right);
        roads.add(left);

        intersection.connectRoads(roads); // give intersection object reference to the roads
        gc.setFill(Paint.valueOf("#e8e8e8"));
        gc.fillRect(0, 0, 500, 500);
        drawInterSection();

    }


}
