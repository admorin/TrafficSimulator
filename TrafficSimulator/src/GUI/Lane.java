package GUI;

import Primary.Lanes;
import Primary.SignalColor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Lane extends Ground{

    private GraphicsContext gc;
    public Boolean isVert; // vertical or horizontal lane
    public Boolean in; // ingoing or outgoing lane

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

        setIncoming();
        setLanes();
    }

    public Light getLight() {
        return this.light;
    }

    // Draw the lane and it's light if ingoing traffic
    //
    public void drawLane(double x, double y, double laneWidth) {

        gc.setFill(Paint.valueOf("#383838"));
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

        if (in) {
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

        Paint color = Paint.valueOf("#ff0000");;
        SignalColor c = lane.getSignal(); // Checks what the TC set this lane's signal as
        Boolean isRed = true;

        if (c == SignalColor.GREEN){
           color = Paint.valueOf("#00b300");
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
                }
            } else if (side == Direction.SOUTH){
                if (count == 2){
                    lane = Lanes.S1;
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
                }

            } else if (side == Direction.WEST){
                if (count == 2){
                    lane = Lanes.W1;
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

        gc.setFill(Paint.valueOf("#fffff0"));

        for (int i = 0; i < 11; i ++) {
            if (this.isVert) {
                gc.fillRect(x + 8, (y + (s2) - 20) - (20 * i), 2, 10);
            } else {
                gc.fillRect((x  + (20 * i) + 8), y + 8, 10, 2);
            }
        }
    }
}
