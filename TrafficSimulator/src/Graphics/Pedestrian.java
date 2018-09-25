package Graphics;

import Primary.Controller;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Pedestrian extends Thread{

    private GraphicsContext gc;
    private Ground ground;
    private Ground dst;
    public Boolean running = true;
    private Boolean isMoving = true;
    private int type = 0;

    private Direction dir;

    private double pedX;
    private double pedY;
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
        System.out.println("my dir is: " + dir);
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
            }else if (!crossing){
                switchToCross();
            } else {
                switchToDest();
            }
        }
    }

    private void switchToCross(){
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

    private void switchToDest(){
        ground = ((Crossing) ground).getDest(dir);
        pedX = ground.x;
        pedY = ground.y;
        type = 2;
        groundSize = 40;

        System.out.println("my dir is: " + dir + " and my side be " + dst.side);
        if (dst.side == Direction.SOUTH && dir == Direction.WEST) pedX += groundSize;
        if (dst.side == Direction.NORTH && dir == Direction.WEST) pedX += groundSize;
        if (dst.side == Direction.EAST && dir == Direction.NORTH) pedY += groundSize;
        if (dst.side == Direction.WEST && dir == Direction.NORTH) pedY += groundSize;

        isMoving = true;
        crossing = false;
    }

    private void move(){
        checkBounds();

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
        gc.setFill(Paint.valueOf("#80ff80"));
        gc.fillOval(pedX, pedY , width, height);
    }
}
