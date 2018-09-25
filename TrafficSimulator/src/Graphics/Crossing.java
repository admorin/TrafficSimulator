package Graphics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;


public class Crossing extends Ground {

    private GraphicsContext gc;
    private RoadDisplay road;
    private double roadLength;
    private Corner corner;

    private Corner dest;

    public Crossing(GraphicsContext gc, RoadDisplay road){
        this.gc = gc;
        this.road = road;
        this.side = road.side;
        this.roadLength = (gc.getCanvas().getWidth() - 100) / 2;
        this.corner = new Corner(gc, road, roadLength);
    }

    public Pedestrian spawn(Ground dst){
        return corner.spawnPed(dst);
    }

    public Corner getCorner(){
        return corner;
    }

    public void setDest(Corner dest){
        this.dest = dest;
    }

    public Corner getDest(Direction dir){

        if (side == Direction.NORTH){
            if (dir == Direction.EAST){
                return dest;
            } else {
                return getCorner();
            }
        }
        if (side == Direction.WEST){
            if (dir == Direction.SOUTH){
                return getCorner();
            } else {
                return dest;
            }
        }

        if (side == Direction.EAST){
            if (dir == Direction.NORTH){
                return getCorner();
            } else {
                return dest;
            }
        }

        if (side == Direction.SOUTH){
            if (dir == Direction.EAST){
                return getCorner();
            } else {
                return dest;
            }
        }

        return dest;
    }



    public void draw(){
        gc.setFill(Paint.valueOf("#0f1e3e"));
        // draws the base cover over the lane

        if (road.side == Direction.NORTH){
            this.x = road.x;
            this.y = road.y + roadLength - 15;
            gc.fillRect(road.x, road.y + roadLength - 15, 100, 15);
        }
        if (road.side == Direction.SOUTH){
            this.x = road.x;
            this.y = road.y + 4;
            gc.fillRect(road.x, road.y + 4, 100, 15);
        } else if (road.side == Direction.EAST){
            this.x = road.x + 4;
            this.y = road.y;
            gc.fillRect(road.x + 4, road.y, 15, 100);

        } else if (road.side == Direction.WEST){
            this.x = road.x + roadLength - 15;
            this.y = road.y;
            gc.fillRect(road.x + roadLength - 15, road.y, 15, 100);
        }
        drawDashes();
        corner.draw();

    }

    private void drawDashes(){
        gc.setFill(Paint.valueOf("#ffffff"));

        // draws the actual crossing dashes
        for (int i = 0; i < 10; i ++) {
            if (road.side == Direction.NORTH){
                gc.fillRect((road.x + (10 * i) + 4), road.y + roadLength - 15, 3, 15);
            } else if(road.side == Direction.SOUTH){
                gc.fillRect((road.x + (10 * i) + 4), road.y + 4, 3, 15);
            }else if (road.side == Direction.EAST){
                gc.fillRect(road.x + 4, (road.y + (10 * i) + 4), 15, 3);
            } else if(road.side == Direction.WEST){
                gc.fillRect(road.x + roadLength - 15, (road.y + (10 * i) + 4) , 15, 3);
            }
        }
    }
}
