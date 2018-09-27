package Graphics;

import Primary.Controller;
import Primary.Lights;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Pedestrian extends Thread{

    // Very similar to Car traffic thread, starts on Corner with a destination crosswalk to take
    // and only moves of the Corner if the signal is green for peds

    private GraphicsContext gc;
    private Ground ground;
    private Lights signal;
    private Ground dst;
    private Pedestrian lead;

    public Boolean running = true;
    public  Boolean collision = false;

    private Boolean isMoving = true;
    private int type = 0;

    private Direction dir;
    private Paint color = Paint.valueOf("#80ff80");

    private double pedX = 0;
    private double pedY = 0;
    private int width = 10;
    private int height = 10;
    private int groundSize = 40;
    private Boolean crossing = false;


    public Pedestrian(GraphicsContext gc, Ground ground, Ground dst){
        this.gc = gc;
        this.ground = ground;
        this.dst = dst;

        this.lead = ground.getLastPed(); // get Ped to check for collision
        this.ground.setLast(this); // set this Ped as last on the current Ground piece

        pedX = ground.x;
        pedY = ground.y;

        setDir();
        setSignal();
        this.start();
    }

    // Used to check for car collisions
    //
    public double getX(){
        return pedX;
    }

    public double getY(){
        return pedY;
    }

    private void setSignal(){
        Crossing c = (Crossing) dst;
        signal = c.getSignal();
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

    // Moves if the ped is moving else checks if it has arrived or
    // has collided with a car to stop  or if the signal has changed
    // and it can switch to the crosswalk else it must be ready to switch to dest
    //
    private void updatePosition(){
        if (isMoving) {
            move();
        } else {
            // else check signal
            if (type == 2){ // ped has arrived
                isMoving = false;
                running = false;
            } else if (collision){ // ped got nailed by a car
                isMoving = false;
            } else if (!crossing){
                if (signal.getIsGreen()){
                    switchToCross();
                } else {
                    isMoving = false;
                }
            } else {
                switchToDest();
            }
        }
    }


    // Switches current ground piece from start Corner
    // to crosswalk destination
    //
    private synchronized void switchToCross(){
        ground = dst;
        pedX = dst.x;
        pedY = dst.y;
        type = 1;

        groundSize = 100;
        if (dst.side == Direction.SOUTH && dir == Direction.WEST) pedX += groundSize;
        if (dst.side == Direction.EAST && dir == Direction.NORTH) pedY += groundSize;
        if (dst.side == Direction.NORTH && dir == Direction.WEST) pedX += groundSize;
        if (dst.side == Direction.WEST && dir == Direction.NORTH) pedY += groundSize;

        isMoving = true;
        crossing = true;


    }

    // Switches current ground component to the destination Corner
    // and keeps moving
    private synchronized void switchToDest(){
        ground = ((Crossing) ground).getDest(dir);
        type = 2;
        groundSize = 40;

        if (dir == Direction.EAST || dir == Direction.WEST) pedX = ground.x;
        if (dir == Direction.NORTH || dir == Direction.SOUTH) pedY = ground.y;

        if (dst.side == Direction.SOUTH && dir == Direction.WEST) pedX += groundSize - height/2;
        if (dst.side == Direction.NORTH && dir == Direction.WEST) pedX += groundSize - height/2;
        if (dst.side == Direction.EAST && dir == Direction.NORTH) pedY += groundSize - height/2;
        if (dst.side == Direction.WEST && dir == Direction.NORTH) pedY += groundSize;

        isMoving = true;
        crossing = false;
    }

    // Checks for collisions with cars within the crosswalk
    //
    private void checkCollision(){
        synchronized (Controller.simLock) {
            if (type == 1){
                Boolean result = ground.checkCollision(this); // then check all other cars on it
                if (result){ // result = true when car has crashed
                    color = Paint.valueOf("#ff0000");
                    isMoving = false;
                    collision = true;
                    //running = false;
                }
            }
        }

    }

    // moves the ped depending on the dir and
    // checks for bounds on the component its walking on
    //
    private void move(){

        checkBounds();
        if (type == 1) checkCollision();

        if (dir == Direction.NORTH){
            pedY -= 1;
        } else if(dir == Direction.SOUTH){
            pedY += 1;
        } else if (dir == Direction.EAST){
            pedX += 1;
        } else if (dir == Direction.WEST){
            pedX -= 1;
        }
    }

    // stops the ped if it needs to switch components
    //
    private void checkBounds(){

        if (dir == Direction.WEST){
            if (pedX < ground.x){
                isMoving = false;
            }
        } else if(dir == Direction.EAST){
            if (pedX > ground.x + groundSize - width){
                isMoving = false;
            }
        } else if (dir == Direction.SOUTH){
            if (pedY > ground.y + groundSize - width){
                isMoving = false;
            }
        } else if (dir == Direction.NORTH){
            if (pedY < ground.y){
                isMoving = false;
            }
        }

    }

    // Sets the direction the ped is heading based on its start ground and dest
    //
    private void setDir(){
        if (ground.side == Direction.NORTH){
            if (dst.side == ground.side){
                dir = Direction.EAST;
                pedY += 25;
            } else {
                dir = Direction.SOUTH;
                pedX += 25;
            }
        } else if (ground.side == Direction.SOUTH){

            if (dst.side == ground.side){
                dir = Direction.WEST;
                pedX += 20;
                pedY += 5;
            } else {
                dir = Direction.NORTH;
                pedY += 20;
                pedX += 5;
            }
        } else if (ground.side == Direction.EAST){
            if (dst.side == ground.side){
                dir = Direction.SOUTH;
                pedX += 5;
            } else {
                dir = Direction.WEST;
                pedX += 25;
                pedY += 25;
            }
        } else if (ground.side == Direction.WEST){
            if (dst.side == ground.side){
                dir = Direction.NORTH;
                pedY += 30;
                pedX += 25;
            } else {
                dir = Direction.EAST;
                pedY += 5;
            }
        }
    }

    public void draw(){
        gc.setFill(color);
        gc.fillOval(pedX, pedY , width, height);
    }
}
