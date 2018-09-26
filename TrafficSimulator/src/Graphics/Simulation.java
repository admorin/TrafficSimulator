package Graphics;

import Primary.Controller;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;

public class Simulation {

    private final GraphicsContext gc;
    private final Intersection intersection;
    private final int size = 100;

    private LinkedList<Car> cars = new LinkedList<>();
    private LinkedList<Pedestrian> peds = new LinkedList<>();
    private Boolean endSim = false;

    public Simulation(GraphicsContext gc){
        this.gc = gc;
        this.intersection = new Intersection(gc, size);
        setUp(); // set up roads and connect to intersection
    }


    // Called in animation loop to notify cars they
    // can move again before being drawn
    public void freeTraffic(){
        synchronized (Controller.simLock)
        {
            for (Car c : cars) {
                if (c.running) c.free(); // running = false if the car has arrived
            }
            for (Pedestrian p : peds) {
                if (p.running) p.free();
            }
        }
    }

    // Button action that will clear out all the
    // Car threads from the simulation
    //
    public void clear(){
        synchronized (Controller.simLock) {
            while (!cars.isEmpty()){
                cars.remove();
            }
            while (!peds.isEmpty()){
                peds.remove();
            }
            intersection.clearOut();
            endSim = false;
        }
    }

    // Redraws the intersection on each animation loop a
    // and all the traffic in it's new positions
    //
    public void drawTraffic(){
        synchronized (Controller.simLock) {
            drawInterSection();
            for (Car c : cars) {
                if (c.running) c.drawCar();
            }

            for (Pedestrian p : peds) {
                if (p.running) p.draw();
            }
        }
    }

    public Boolean updateSpots(){
        synchronized (Controller.simLock) {
            for (Car c : cars) {
                if (c.collision) endSim = true;
                if (c.running && c.needsGroundUpdate != 0) c.updateGround();
            }
        }
        return  endSim;
    }


    // Spawns a new car on a random start lane with a
    // random destination lane
    //
    public synchronized void spawnCar(Boolean emergency){
        synchronized (Controller.simLock) {
            Random rn = new Random();
            LinkedList<RoadDisplay> roads = intersection.getRoads();

            int range = (roads.size() - 1) + 1;
            int randomStart =  rn.nextInt(range);
            //randomStart = 3; // uncomment this line to spawn on specific road (0 = north, 1 = south, 2 = east, 3 = west)
            RoadDisplay r = roads.get(randomStart);
            LaneDisplay start = r.getRandomStart();

            LaneDisplay dst = intersection.getRandomDest(start);

            Car c = new Car(r.getSide(), start, dst, emergency, gc);
            cars.add(c);
        }

    }

    public void spawnPed(){
        synchronized (Controller.simLock) {
            peds.add(intersection.createPed());
        }
    }

    private void showEnd(){
        gc.setFill(Paint.valueOf("#4f4f4f"));
        gc.strokeText("12 dead. Sim failed.", gc.getCanvas().getWidth() * .70 , 50, 250);
    }



    // Draws the initial setup with no traffic
    //
    private void drawInterSection() {
        gc.setFill(Paint.valueOf("#33334d"));
        intersection.draw();
        if (endSim) showEnd();
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
