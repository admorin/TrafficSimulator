package GUI;

import Primary.Controller;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Car extends Thread{
    private Ground ground; // keeps track of which Ground piece it's on, could be Lane or Intersection
    private Ground dest; // Always the ground piece it's heading to
    private GraphicsContext gc;
    private Boolean isMoving = true;
    private double laneLength;

    private Light light; // Light it's checking for when it can go
    private Car lead; // Reference to the car in front of it in each lane (could be null if first)

    private double carX;
    private double carY;
    private Direction side;

    public Boolean running = true; // running is true if car hasn't arrived at destination
    private int width;
    private int height;
    private Boolean isLeaving = false; // is true when car has passed through intersection
    private Boolean willSwitch = false; // is true when car is about to switch Ground components

    public Boolean needsGroundUpdate = false;

    public Car(Direction side, Ground ground, Ground dest, GraphicsContext gc) {
        this.gc = gc;
        this.ground = ground;
        this.dest = dest;
        this.side = side;

        this.lead = ground.getLast(); // get Car to check for collision
        this.ground.setLast(this); // set this Car as last on the current Ground piece

        this.carX = ground.x;
        this.carY = ground.y;
        this.laneLength = (gc.getCanvas().getWidth() - 100) / 2;

        setUpCar(this.side);
        this.start();
    }


    @Override
    public void run() {
        increment();
        while (running){
            updatePosition(); // make a move or stay waiting
            waiter(); // wait to be drawn
        }
        decrement();
    }


    // Notify car it has been drawn so it can move again
    //
    public synchronized void free() {
        this.notify();
    }

    // Decrement the total activeCount back towards 0 after move update
    // then wait until drawn and notified to increment back up for another move
    //
    private synchronized void waiter(){
        decrement();
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        increment();
    }

    private void increment(){
        synchronized (Controller.countLock)
        {
            Controller.activeCount++;
        }
    }

    private void decrement(){
        synchronized (Controller.countLock)
        {
            Controller.activeCount--;
        }
    }

    public void updateGround(){
        if (ground.type == 1){
            ground.addCar(this);
            needsGroundUpdate = false;
        } else if (ground.type == 0){
            ground.getIntersection().removeCar(this);
        }
    }


    // Draw the car and cover up it's trail while moving
    //
    public Boolean drawCar() {
        gc.setFill(Paint.valueOf("#ffff80"));

        gc.fillRect(carX, carY, width, height);
        return  true;
    }

    /*
    Keep going unless stopped then check four different scenarios

    1 ) if ground is a Lane type ( 0 ) and car is incoming then if the light isn't red switch into intersection
    2 ) if car is leaving and it's out of bounds of current component then it must've arrived so remove it and stop running
    3 ) else if ground type is 1 then it's on the intersection so switch to destination lane
    4 ) else just check if the lead car it stopped for has moved yet
    */
    public void updatePosition() {
        if (isMoving) {
            move();
        } else {
            if (ground.type == 0 && !isLeaving && willSwitch) {
                if (!light.isRed()) {
                    switchToIntersection();
                }
            } else if (isLeaving){
                running = false;
            } else if (ground.type == 1){
                switchToDest();
            } else {
                checkCollision();
            }
        }
    }


    // Sets up the width, height, x, y, and light for
    // a new car
    //
    private void setUpCar(Direction side){
        if (side == Direction.NORTH || side == Direction.SOUTH) {
            width = 8;
            height = 16;
            carX += 8;
        } else {
            width = 16;
            height = 8;
            carY += 8;
        }

        if (ground.type == 0) {
            Lane l = (Lane) ground;
            light = l.getLight();
        }

        if (isLeaving) {
            if (side == Direction.NORTH) {
                carY += laneLength;
            }

            if (side == Direction.WEST)
            {
                carX +=  laneLength;
            }

        } else {
            if (side == Direction.SOUTH) {
                carY +=  laneLength;
            }

            if (side == Direction.EAST)
            {
                carX +=  laneLength;
            }
        }
    }

    // Checks if the car is about to exit the current Ground object it's on
    //
    private void checkBounds() {

        if (isLeaving) {
            if (side == Direction.NORTH) {
                if (carY < ground.y) {
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.SOUTH){
                if (carY > ground.y - height + laneLength){
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.EAST){
                if (carX > ground.x - width + laneLength) {
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.WEST){
                if (carX < ground.x) {
                    isMoving = false;
                    willSwitch = true;
                }
            }

        } else {
            if (side == Direction.NORTH) {
                if (carY > ground.y - height + laneLength) {
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.SOUTH) {
                if (carY < ground.y){
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.EAST){
                if (carX < ground.x) {
                    isMoving = false;
                    willSwitch = true;
                }
            }
            if (side == Direction.WEST) {
                if (carX > ground.x - width + laneLength) {
                    isMoving = false;
                    willSwitch = true;
                }
            }
        }
    }



    // Changes the current Ground object from the start lane to
    // the Intersection object that is the neighbor of it
    //
    private synchronized void switchToIntersection() {
        Ground old = ground;
        ground = ground.getIntersection(); // sets current Ground object to be intersection
        laneLength = 80;
        isMoving = true;

        if (side == Direction.NORTH || side == Direction.SOUTH) {
            carY = ground.y;
            carX = old.x ;
            carX += 8;
        } else {
            carX = ground.x;
            carY = old.y;
        }


        if (side == Direction.SOUTH) {
            carY +=  laneLength;
        }

        if (side == Direction.EAST)
        {
            carX +=  laneLength;
        }

        // Tell the current ground object that this car is the last
        // one to be on th piece so if a new car is created then it knows
        // it's lead
        ground.setLast(this);
        needsGroundUpdate = true;


    }


    // Switches from Intersection object to the destination Lane
    //
    private synchronized void switchToDest(){
        ground = dest;
        side = ground.side;
        laneLength = (gc.getCanvas().getWidth() - 100) / 2;
        isLeaving = true;
        carX = ground.x;
        carY = ground.y;
        isMoving = true;

        if (side == Direction.NORTH) {
            carY +=  laneLength;
        }

        if (side == Direction.WEST)
        {
            carX +=  laneLength;
        }

        if (side == Direction.NORTH || side == Direction.SOUTH) {
            width = 8;
            height = 16;
            carX += 8;
        } else {
            width = 16;
            height = 8;
            carY += 8;
        }

        // Tell the current ground object that this car is the last
        // one to be on th piece so if a new car is created then it knows
        // it's lead
        ground.setLast(this);
        needsGroundUpdate = true;
    }


    // Checks if the car needs to switch Ground component then
    // moves a direction depending on the road it's on and if outgoing
    private void move(){

        checkCollision();
        checkBounds();

        if (isLeaving) {
            if (side == Direction.NORTH) {
                carY -= 1;
            } else if (side == Direction.SOUTH) {
                carY += 1;
            }else if (side == Direction.EAST) {
                carX += 1;
            } else if (side == Direction.WEST) {
                carX -= 1;
            }
        } else {
            if (side == Direction.NORTH) {
                carY += 1;
            } else if (side == Direction.SOUTH) {
                carY -= 1;
            }else if (side == Direction.EAST) {
                carX -= 1;
            } else if (side == Direction.WEST) {
                carX += 1;
            }
        }
    }


    // Check car thread leading this one to see if it's within bounds
    // for collision then stops if so
    //
    private void checkCollision() {
        if (lead != null) {
            if (side == Direction.NORTH || side == Direction.SOUTH){
                double yDif = Math.abs(lead.carY - carY);
                if (yDif < height + 5){
                    isMoving = false;
                } else{
                    isMoving = true;
                }
                if (lead.ground != this.ground) isMoving = true;
            } else {
                double xDif = Math.abs(lead.carX - carX);
                if (xDif < width + 5) {
                    isMoving = false;
                } else {
                    isMoving = true;
                }
                if (lead.ground != this.ground) isMoving = true;
            }
        } else {
            isMoving = true;
        }
    }
}
