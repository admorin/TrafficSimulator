package Graphics;

import Primary.Controller;

import java.util.Iterator;
import java.util.LinkedList;

public class Ground {

    private Ground neighbor;
    public double x;
    public double y;
    public int type;
    public Direction side;
    public int count = 0;

    // keeps track of the last Car in line on the component so if a
    // new one is spawned it knows the lead Car in front of it
    private Car last;
    private Crossing crosswalk;

    private LinkedList<Car> cars = new LinkedList<>();



    // Allows a RoadDisplay or LaneDisplay object to have reference to the Intersection
    // object that is a neighbor of it for Cars to transfer onto
    //
    void setIntersection(Ground neighbor) {
        this.neighbor = neighbor;
    }

    public Ground getIntersection() {
        return this.neighbor;
    }

    void setPosition(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.type = type; // type = 1 if Intersection, type = 0 if LaneDisplay
    }

    public void setLast(Car last){
        this.last = last;
    }

    // Used on Car creation to ask the object the last car in the lane
    // so it knows which car to check for collision
    //
    public Car getLast(){
        return last;
    }

    public void setCrosswalk(Crossing crosswalk){
        this.crosswalk = crosswalk;
    }

    public void addCar(Car c){
        cars.add(c);
    }

    public void removeCar(Car c ){
        cars.remove(c);
    }

    public void clearOut(){
        while (!cars.isEmpty()){
            cars.remove();
        }

        last = null;
    }

    public Crossing getCrossing(){
        return crosswalk;
    }


    // Check the passed in car to see if it has collided
    // with any of the other cars in the intersection and
    // returns true if so
    //
    public Boolean checkCollision(Car car){
        Boolean result = false;
        Boolean collision;
        for (Car c : cars){
            if (c != car){
                collision = checkBounds(c, car);
                if (collision) result = true;
            }
        }
        return result;
    }

    public synchronized Boolean checkCollision(Pedestrian p){
        Boolean result = false;
        Boolean collision;
        synchronized (Controller.simLock){
            for (Iterator<Car> i = cars.iterator(); i.hasNext();) {
                Car c = i.next();
                if (c.running && !c.collision) {
                    collision = checkBounds(c, p);
                    if (collision) result = true;
                }
            }
        }

        return result;

    }

    // Takes in two cars and returns true if they are withing
    // 5 x and y of each other
    //
    private Boolean checkBounds(Car c1, Car c2){
        double yDif = Math.abs(c1.getCarY() - c2.getCarY());
        double xDif = Math.abs(c1.getCarX() - c2.getCarX());
        return yDif < 5 && xDif < 5;
    }

    // Takes in two cars and returns true if they are withing
    // 5 x and y of each other
    //
    private Boolean checkBounds(Car c, Pedestrian p){
        if (c != null && p != null){
            double yDif = Math.abs(c.getCarY() -  p.getY());
            double xDif = Math.abs(c.getCarX() - p.getX());
            return yDif < 5 && xDif < 5;
        }

        return true;
    }


}
