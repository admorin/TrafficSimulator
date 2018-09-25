package Graphics;

import Primary.Controller;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Pedestrian extends Thread{

    private GraphicsContext gc;
    private Ground ground;
    private Ground dst;
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

        pedX = ground.x;
        pedY = ground.y;

        setDir();
        this.start();
    }

    public double getX(){
        return pedX;
    }

    public double getY(){
        return pedY;
    }

    private void setDir(){
        if (ground.side == Direction.NORTH){
            if (dst.side == ground.side){
                dir = Direction.EAST;
            } else {
                dir = Direction.SOUTH;
            }
        } else if (ground.side == Direction.SOUTH){

            if (dst.side == ground.side){
                dir = Direction.WEST;
                pedX += 20;
            } else {
                dir = Direction.NORTH;
                pedY += 20;
            }
        } else if (ground.side == Direction.EAST){
            if (dst.side == ground.side){
                dir = Direction.SOUTH;
            } else {
                dir = Direction.WEST;
                pedX += 20;
            }
        } else if (ground.side == Direction.WEST){
            if (dst.side == ground.side){
                dir = Direction.NORTH;
                pedY += 20;
            } else {
                dir = Direction.EAST;
            }
        }
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

    private void updatePosition(){
        if (isMoving) {
            move();
        } else {
            // else check signal
            if (type == 2){
                isMoving = false;
                running = false;
            } else if (collision){
                isMoving = false;
            } else if (!crossing){
                switchToCross();
            } else {
                switchToDest();
            }
        }
    }

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

    private synchronized void switchToDest(){
        ground = ((Crossing) ground).getDest(dir);
        pedX = ground.x;
        pedY = ground.y;
        type = 2;
        groundSize = 40;

        if (dst.side == Direction.SOUTH && dir == Direction.WEST) pedX += groundSize;
        if (dst.side == Direction.NORTH && dir == Direction.WEST) pedX += groundSize;
        if (dst.side == Direction.EAST && dir == Direction.NORTH) pedY += groundSize;
        if (dst.side == Direction.WEST && dir == Direction.NORTH) pedY += groundSize;

        isMoving = true;
        crossing = false;
    }

    private void checkCollision(){
        Boolean result = ground.checkCollision(this); // then check all other cars on it
        if (result){ // result = true when car has crashed
            System.out.println("oohhh ped collision");
            color = Paint.valueOf("#ff0000");
            isMoving = false;
            collision = true;
            //running = false;
        }
    }

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

    public void draw(){
        gc.setFill(color);
        gc.fillOval(pedX, pedY , width, height);
    }
}
