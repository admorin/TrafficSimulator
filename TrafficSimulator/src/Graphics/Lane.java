package Graphics;

import Primary.Lanes;
import Primary.SignalColor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Lane extends Ground{

    private GraphicsContext gc;
    public Boolean isVert; // vertical or horizontal lane
    public Boolean in; // ingoing or outgoing lane
    private Boolean isMid = false;

    private Light light;

    double laneX = 0;
    double laneY = 0;
    double laneLength;
    double laneWidth;

    private Lanes lane; // reference to the actual enum lane that the Test TCS will control


    public Lane(GraphicsContext gc, Boolean isVert, int count, Direction side) {
        this.gc = gc;
        this.isVert = isVert;
        this.side = side;
        this.count = count;
        this.laneLength = (this.gc.getCanvas().getWidth() - 100) / 2;
        this.light = new Light(this, gc); // each lane get's a light but not drawn unless it's incoming

        setIncoming(); // determine whether incoming or leaving lane
        setLanes(); // assign each Lane object a reference to the proper Lanes enum object
    }

    public Light getLight() {
        return this.light;
    }

    // Draw the lane and it's light if ingoing traffic
    //
    public void drawLane(double x, double y, double laneWidth) {

        gc.setFill(Paint.valueOf("#4f4f4f"));
        this.laneWidth = laneWidth;
        double offset = count * laneWidth;

        if (isVert) {
            gc.fillRect(x  + offset, y, laneWidth, laneLength);
            laneX = x + offset;
            laneY = y;
            drawDashes(x + offset ,  y , laneWidth, laneLength);
        } else {
            laneX = x;
            laneY = y + offset;
            gc.fillRect(x, y + offset, laneLength, laneWidth);
            drawDashes(x ,  y + offset, laneWidth, laneLength);
        }

        if (in) { // draw light on incoming lanes only
            light.setPosition(x, y, offset, isVert);
            drawLight();
        }

        this.x = laneX; // setting the Ground class x,y
        this.y = laneY; // positions for each lane

    }


    // Draw the light at the end of each lane, handles
    // the light's color
    //
    public void drawLight() {

        Paint color = Paint.valueOf("#990000");;
        SignalColor c = lane.getSignal(); // Checks what the TC set this lane's signal as
        Boolean isRed = true;

        if (c == SignalColor.GREEN){ // would need to react if the light is yellow still
           color = Paint.valueOf("#009900");
           isRed = false;
        }

        gc.setFill(color);
        light.changeColor(isRed);

        if (side == Direction.NORTH ) {
            gc.fillRect(light.x, light.y + laneLength, laneWidth, 4);
        } else if (side == Direction.SOUTH){
            gc.fillRect(light.x, y  , laneWidth, 4);
        } else if (side == Direction.EAST){
            gc.fillRect(light.x, light.y, 4, laneWidth);
        } else if (side == Direction.WEST){
            gc.fillRect(light.x + laneLength, light.y , 4, laneWidth);
        }
    }

    // This checks what type of lane it is and gives a reference to the
    // actual enum Lanes type it should be looking for changes at
    //
    private void setLanes(){
        if (in){
            if (side == Direction.NORTH){
                if (count == 0){
                    lane = Lanes.N1;
                } else if (count == 1) {
                    lane = Lanes.N2;
                } else if(count == 2){
                    lane = Lanes.N3;
                    isMid = true;
                }
            } else if (side == Direction.SOUTH){
                if (count == 2){
                    lane = Lanes.S1;
                    isMid = true;
                } else if (count == 3) {
                    lane = Lanes.S2;
                } else if(count == 4){
                    lane = Lanes.S3;
                }
            } else if (side == Direction.EAST){
                if (count == 0){
                    lane = Lanes.E1;
                } else if (count == 1) {
                    lane = Lanes.E2;
                } else if (count == 2){
                    lane = Lanes.E3;
                    isMid = true;
                }

            } else if (side == Direction.WEST){
                if (count == 2){
                    lane = Lanes.W1;
                    isMid = true;
                } else if (count == 3) {
                    lane = Lanes.W2;
                } else if(count == 4){
                    lane = Lanes.W3;
                }
            }
        }
    }

    // Checks which lane it is and determines if incoming our outgoing
    // traffic depending on the side the roads on
    //
    private void setIncoming() {
        if (side == Direction.NORTH || side == Direction.EAST) {
            if (count > 2){
                in = false;
            } else {
                in = true;
            }
        }

        if (side == Direction.SOUTH || side == Direction.WEST) {
            if (count > 1) {
                in = true;
            } else {
                in = false;
            }
        }
    }

    // Draw the dashed white lanes along the side of each lane
    //
    private void drawDashes(double x , double y, double s1, double s2)  {

        if (isMid) { // if mid lane draw the double yellows
            drawMidLine(x, y);
        } else{
            gc.setFill(Paint.valueOf("#fffff0"));
            if (side == Direction.NORTH) x += laneWidth - 2;
            if (side == Direction.EAST) y += laneWidth - 2;

            for (int i = 0; i < 11; i ++) {
                if (side == Direction.NORTH && count < 4){
                    gc.fillRect(x , (y + (s2) - 20) - (20 * i), 2, 10);
                } else if(side == Direction.SOUTH && count > 0){
                    gc.fillRect(x , (y + (s2) - 20) - (20 * i), 2, 10);
                }else if (side == Direction.EAST && count < 4){
                    gc.fillRect((x  + (20 * i) + 8), y , 10, 2);
                } else if(side == Direction.WEST && count > 0){
                    gc.fillRect((x  + (20 * i) + 8), y , 10, 2);
                }
            }
        }

    }

    // Draw the two mid yellow lanes
    //
    private void drawMidLine(double x, double y){
        gc.setFill(Paint.valueOf("#ffff00"));

        if (side == Direction.NORTH){
            gc.fillRect(x + 15, y, 1, laneLength);
            gc.fillRect(x + 13, y, 1, laneLength);
        } else if(side == Direction.SOUTH){
            gc.fillRect(x , y, 1, laneLength);
            gc.fillRect(x + 2, y, 1, laneLength);
        }else if (side == Direction.EAST){
            gc.fillRect(x, y + 15, laneLength, 1);
            gc.fillRect(x, y + 13, laneLength, 1);
        } else if(side == Direction.WEST){
            gc.fillRect(x, y , laneLength, 1);
            gc.fillRect(x, y + 2, laneLength, 1);
        }
    }
}
