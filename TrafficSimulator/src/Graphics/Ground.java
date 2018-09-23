package Graphics;

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

    private LinkedList<Car> cars = new LinkedList<>();



    // Allows a RoadDisplay or LaneDisplay object to have reference to the Intersection
    // object that is a neighbor of it for Cars to transfer onto
    //
    public void setIntersection(Ground neighbor) {
        this.neighbor = neighbor;
    }

    public Ground getIntersection() {
        return this.neighbor;
    }

    public void setPosition(double x, double y, int type) {
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



    public void addCar(Car c){
        cars.add(c);
    }

    public void removeCar(Car c ){
        cars.remove(c);
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

    // Takes in two cars and returns true if they are withing
    // 5 x and y of each other
    //
    private Boolean checkBounds(Car c1, Car c2){
        double yDif = Math.abs(c1.getCarY() - c2.getCarY());
        double xDif = Math.abs(c1.getCarX() - c2.getCarX());
        if (yDif < 5 && xDif < 5){
            return  true;
        }
        return false;
    }

}
