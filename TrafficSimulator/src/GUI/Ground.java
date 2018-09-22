package GUI;

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

    // Allows a Road or Lane object to have reference to the Intersection
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
        this.type = type;
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

}
