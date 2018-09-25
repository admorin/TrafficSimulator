package Graphics;

import Primary.Controller;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import java.util.Random;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Car extends Thread{

    private Ground ground; // keeps track of which Ground piece it's on, could be LaneDisplay or Intersection
    private final Ground dest; // Always the ground piece it's heading to
    private final GraphicsContext gc;
    private Boolean isMoving = true;
    private double laneLength;
    private double destLength;
    private Paint color;

    private CarSignalDisplay carSignalDisplay; // CarSignalDisplay it's checking for when it can go
    private final Car lead; // Reference to the car in front of it in each lane (could be null if first)

    private double carX;
    private double carY;
    private int width;
    private int height;
    private Direction side;

    private int pathType; // 0 = straight, 1 = right, 2 = left
    private int rotation = 0; // keeps track of rotation angle when turning
    private double rotationRate = 0;

    public Boolean running = true; // running is true if car hasn't arrived at destination

    private Boolean isLeaving = false; // is true when car has passed through intersection
    private Boolean willSwitch = false; // is true when car is about to switch Ground components
    private Boolean atCross = false;

    public Boolean collision = false; // is true when collided within intersection and stopped
    public Boolean needsGroundUpdate = false; // is true when it has entered or exited the intersection

    private final Paint[] col = {
            Paint.valueOf("#ff8888"), // Array of colors
            Paint.valueOf("#88ff88"), // for the rectangle car to be
            Paint.valueOf("#8888ff"),
            Paint.valueOf("#8f8f8f"),
            Paint.valueOf("#ffffff")
    };


    public Car(Direction side, Ground ground, Ground dest, GraphicsContext gc){
        this.gc = gc;
        this.ground = ground;
        this.dest = dest;
        this.side = side;
        this.color = randomColor();

        this.lead = ground.getLast(); // get Car to check for collision
        this.ground.setLast(this); // set this Car as last on the current Ground piece

        this.carX = ground.x;
        this.carY = ground.y;
        this.laneLength = (gc.getCanvas().getWidth() - 100) / 2;
        this.destLength = laneLength;

        setUpCar(this.side); // set up x, y, width, height for car
        setPathType(); // set if going straight = 0, right = 1, left = 2
        this.start();
    }

    // Used when intersection is checking for collisions
    //
    public double getCarX(){
        return carX;
    }

    public double getCarY(){
        return carY;
    }


    @Override
    public void run() {
        increment();
        while (running){
            updatePosition(); // make a move or stay stopped
            waiter(); // wait to be drawn
        }
        decrement();
    }


    // Notify car it has been drawn so it can move again
    //
    public synchronized void free() {
        this.notify();
    }

    // Decrement the total threadCount back towards 0 after move update
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
            Controller.threadCount++;
        }
    }

    private void decrement(){
        synchronized (Controller.countLock)
        {
            Controller.threadCount--;
        }
    }

    // Called in animation loop for each car that
    // needsGroundUpdate and will do one of two things
    // 1 ) Add car to intersection's list of cars to check for collision
    // 2 ) Remove car from the intersection's list of cars once it reaches destination lane
    //
    public void updateGround(){
        if (ground.type == 1){ // 1 type = intersection
            ground.addCar(this);
            needsGroundUpdate = false;
        } else if (ground.type == 0){ // 0 type = lane
            ground.getIntersection().removeCar(this);
            needsGroundUpdate = false;
        }
    }


    /*
    Keep going unless stopped then check five different scenarios --

    1 ) if ground is a LaneDisplay type ( 0 ) and car is incoming then if the carSignalDisplay isn't red switch into intersection
    2 ) if car is leaving and it's out of bounds of current component then it must've arrived so stop running
    3 ) if car has crashed then stop it and wait for end
    4 ) if ground type is 1 then it's on the intersection so switch to destination lane
    5 ) else just check if the lead car it stopped for has moved yet
    */
    private void updatePosition() {
        if (isMoving) {
            move();
        } else {
            if (ground.type == 0 && !isLeaving && willSwitch) {
                atCross = false;
                ground.getCrossing().removeCar(this);
                switchToIntersection();
            } else if (atCross){
                if (!carSignalDisplay.isRed()) {
                    isMoving = true;
                }
            } else if (isLeaving){
                running = false;
            } else if(collision){
                isMoving = false;
            } else if (ground.type == 1){
                System.out.println("switching to dest");
                switchToDest();
                ground.getCrossing().addCar(this);
                atCross = true;
            } else {
                checkCollision();
            }
        }
    }

    private void renderCar(Direction side)
    {
        if (side == Direction.NORTH || side == Direction.SOUTH) {
            width = 8;
            height = 16;
            carX += 2;
        } else {
            width = 16;
            height = 8;
            carY += 2;
        }
    }

    // Called by animation timer to draw the car -- this Boolean used t
    // to return if the car neededRefresh then would only redraw the intersection
    // if one of the cars needed it but that's not being used now
    //
    public void drawCar(){

        gc.setFill(color);

        if ((pathType == 1 || pathType == 2) && ground.type == 1) {
            if (!collision)rotation += rotationRate;
            gc.save();
            gc.transform(new Affine(new Rotate(rotation, carX + width/2, carY + height/2)));
            gc.fillRect(carX, carY, width, height);
            gc.restore();
        } else {
            gc.fillRect(carX, carY, width, height);
        }

    }

    // Changes the current Ground object from the start lane to
    // the Intersection object that is the neighbor of it
    //
    private synchronized void switchToIntersection() {
        Ground old = ground;
        ground = ground.getIntersection(); // sets current Ground object to be intersection
        laneLength = 100; // very important must match intersection size
        isMoving = true;

        if (side == Direction.NORTH || side == Direction.SOUTH) {
            carY = ground.y;
            carX = old.x ;
            carX += 2;
        } else {
            carX = ground.x;
            carY = old.y;
            carY += 2;
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
        needsGroundUpdate = true; // need to add the car to intersection's list
    }


    // Switches from Intersection object to the destination LaneDisplay
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
            carX +=  laneLength - width;
        }

        renderCar(side);

        // Tell the current ground object that this car is the last
        // one to be on th piece so if a new car is created then it knows
        // it's lead
        ground.setLast(this);
        needsGroundUpdate = true; // need to remove the car from intersection list
    }

    // Sets up the width, height, x, y, and carSignalDisplay for
    // a new car
    //
    private void setUpCar(Direction side){
        renderCar(side);
        if (ground.type == 0) {
            LaneDisplay l = (LaneDisplay) ground;
            carSignalDisplay = l.getCarSignalDisplay();
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

    private void setPathType(){
        if (side == Direction.NORTH){
            if (dest.side == Direction.EAST){
                pathType = 2;
            } else if (dest.side == Direction.SOUTH){
                pathType = 0;
            } else if (dest.side == Direction.WEST){
                pathType = 1;
            }
        } else if (side == Direction.SOUTH){
            if (dest.side == Direction.EAST){
                pathType = 1;
            } else if (dest.side == Direction.NORTH){
                pathType = 0;
            } else if (dest.side == Direction.WEST){
                pathType = 2;
            }
        } else if (side == Direction.EAST){
            if (dest.side == Direction.NORTH){
                pathType = 1;
            } else if (dest.side == Direction.SOUTH){
                pathType = 2;
            } else if (dest.side == Direction.WEST){
                pathType = 0;
            }
        } else if (side == Direction.WEST){
            if (dest.side == Direction.EAST){
                pathType = 0;
            } else if (dest.side == Direction.SOUTH){
                pathType = 1;
            } else if (dest.side == Direction.NORTH){
                pathType = 2;
            }
        }

        if (pathType == 1) rotationRate = 3; else if(pathType == 2) rotationRate = -1.5;
    }


    // Checks if the car is about to exit the current Ground object it's on
    //
    private void checkBounds() {

        if (ground.type == 1 && (pathType == 1 || pathType == 2)){ // checks when to switch to dest while turning
            if (side == Direction.SOUTH && carY < dest.y){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.NORTH && carY > dest.y){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.EAST && carX < dest.x){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.WEST && carX > dest.x){
                isMoving = false;
                willSwitch = true;
            }
        } else if (ground.type == 1 && pathType == 0){ // on the intersection and headed straight through
            if (side == Direction.SOUTH && carY < dest.y + destLength ){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.NORTH && carY > dest.y){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.EAST && carX < dest.x + destLength){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.WEST && carX > dest.x){
                isMoving = false;
                willSwitch = true;
            }
        } else if (isLeaving) { // checks when to stop moving on arrival
            if (side == Direction.NORTH) {
                if (atCross && carY < ground.y + laneLength - 20){
                    System.out.println("off da cross north");
                    ground.getCrossing().removeCar(this);
                    atCross = false;
                } else if(carY < ground.y){
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.SOUTH){
                if (atCross && carY > ground.y + 20){
                    System.out.println("off da crosss south");
                    ground.getCrossing().removeCar(this);
                    atCross = false;
                } else if (carY > ground.y - height + laneLength){
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.EAST){
                if (atCross && carX > ground.x + 20){
                    ground.getCrossing().removeCar(this);
                    System.out.println("off da cross east");
                    atCross = false;
                }else if (carX > ground.x - width + laneLength) {
                    isMoving = false;
                    willSwitch = true;
                }
            }
            if (side == Direction.WEST){
                if (atCross && carX < ground.x + laneLength -20){
                    ground.getCrossing().removeCar(this);
                    System.out.println("off da cross west");
                    atCross = false;
                }else if (carX < ground.x) {
                    isMoving = false;
                    willSwitch = true;
                }
            }

        } else if (ground.type == 0 && !isLeaving){ // checks when to stop moving on lane or intersection
            if (side == Direction.NORTH) {
                if (carY > ground.y - height + laneLength ) {
                    isMoving = false;
                    willSwitch = true;
                } else if (carY > ground.y - height + laneLength - 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    ground.getCrossing().addCar(this);
                }
            }

            if (side == Direction.SOUTH) {
                if (carY < ground.y){
                    isMoving = false;
                    willSwitch = true;
                } else if (carY < ground.y + 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    ground.getCrossing().addCar(this);
                }
            }

            if (side == Direction.EAST){
                if (carX < ground.x) {
                    isMoving = false;
                    willSwitch = true;
                } else if (carX < ground.x + 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    ground.getCrossing().addCar(this);
                }
            }
            if (side == Direction.WEST) {
                if (carX > ground.x - width + laneLength) {
                    isMoving = false;
                    willSwitch = true;
                } else if (carX > ground.x - width + laneLength - 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    ground.getCrossing().addCar(this);
                }
            }
        }
    }



    // Checks if the car needs to switch Ground component then
    // moves a direction depending on the road it's on and if outgoing
    private void move(){

        if (!isLeaving) checkCollision();
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
                if (ground.type == 1 && pathType == 1)carX -= 0.3;
                if (ground.type == 1 && pathType == 2) carX += 0.5;
                carY += 1;
            } else if (side == Direction.SOUTH) {
                if (ground.type == 1 && pathType == 1)carX += 0.3;
                if (ground.type == 1 && pathType == 2) carX -= 0.5;
                carY -= 1;
            }else if (side == Direction.EAST) {
                if (ground.type == 1 && pathType == 1)carY -= 0.3;
                if (ground.type == 1 && pathType == 2) carY += 0.5;
                carX -= 1;
            } else if (side == Direction.WEST) {
                if (ground.type == 1 && pathType == 1)carY += 0.3;
                if (ground.type == 1 && pathType == 2) carY -= 0.5;
                carX += 1;
            }
        }
    }

    private Paint randomColor(){
        Random r = new Random();
        int range = (5 - 1) + 1;
        int rand =  r.nextInt(range);
        return col[rand];

    }



    // Check car thread leading this one to see if it's within bounds
    // for collision then stops if so
    //
    private void checkCollision() {
        if (ground.type == 1 && isMoving){ // if on the intersection
            Boolean result = ground.checkCollision(this); // then check all other cars on it
            if (result){ // result = true when car has crashed
                color = Paint.valueOf("#ff0000");
                isMoving = false;
                collision = true;
            }

        } else if (lead != null) { // Check the car in front for collision
            if (side == Direction.NORTH || side == Direction.SOUTH){
                double yDif = Math.abs(lead.carY - carY);
                isMoving = !(yDif < height + 5);
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
        } else { // the car in front must've moved onto different component so start moving
            isMoving = true;
        }
    }
}
