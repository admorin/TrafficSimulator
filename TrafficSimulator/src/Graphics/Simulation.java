package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.LinkedList;
import java.util.Random;

public class Simulation {

    private final GraphicsContext gc;
    private Boolean needsRefresh = true;
    private final Intersection intersection;
    private final int size = 80;

    private LinkedList<Car> cars = new LinkedList<>();
    private Boolean endSim = false;

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

    public Boolean updateSpots(){
        for (Car c : cars) {
            if (c.collision) endSim = true;
            if (c.running && c.needsGroundUpdate) c.updateGround();
        }
        return  endSim;
    }


    // Spawns a new car on a random start lane with a
    // random destination lane
    //
    public void spawnCar(){
        Random rn = new Random();
        LinkedList<RoadDisplay> roads = intersection.getRoads();

        int range = (roads.size() - 1) + 1;
        int randomStart =  rn.nextInt(range);
        //randomStart = 3; // uncomment this line to spawn on specific road
        RoadDisplay r = roads.get(randomStart);
        LaneDisplay start = r.getRandomStart();

        LaneDisplay dst = intersection.getRandomDest(start);

        System.out.println("spawning car at lane " + start.count + " with dst " + dst.side + " with lane count " + dst.count);
        Car c = new Car(r.getSide(), start, dst, gc);
        cars.add(c);
    }

    public void showEnd(){
        gc.setFill(Paint.valueOf("#4f4f4f"));
        gc.strokeText("12 dead. Sim failed.", gc.getCanvas().getWidth() * .70 , 50, 250);
    }



    // Draws the initial setup with no traffic
    //
    private void drawInterSection() {
        gc.setFill(Paint.valueOf("#4f4f4f"));
        intersection.draw();
    }


    // Creates new Intersection with four connecting roads,
    // and draws the start of the simulation
    //
    private void setUp(){

        // All this initialization trash could be put into the Intersection constructor
        // but this could be used to let users customize the type of roads they want from a Graphics
        LinkedList<RoadDisplay> roads = new LinkedList<>();

        RoadDisplay up = new RoadDisplay(gc, Direction.NORTH, size, intersection);
        RoadDisplay down = new RoadDisplay(gc, Direction.SOUTH, size, intersection);
        RoadDisplay right = new RoadDisplay(gc, Direction.EAST, size, intersection);
        RoadDisplay left = new RoadDisplay(gc, Direction.WEST, size, intersection);

        // roads(0) = north,  roads(1) = south, roads(2) = east, roads(3) = west
        roads.add(up);
        roads.add(down);
        roads.add(right);
        roads.add(left);

        intersection.connectRoads(roads); // give intersection object reference to all the roads
        drawInterSection();

    }


}
