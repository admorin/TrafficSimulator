package Graphics;

import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;
import java.util.Random;

class Intersection extends Ground {

    private final GraphicsContext gc;
    private final double size;
    private LinkedList<RoadDisplay> roads = new LinkedList<>();

    public Intersection(GraphicsContext gc, double size){
        this.gc = gc;
        this.size = size;
        this.setPosition(gc.getCanvas().getWidth() /2 - (size/2), gc.getCanvas().getHeight() /2 - (size/2), 1);
    }

    // Draws the main square and all it's connecting roads
    //
    public void draw(){
        gc.fillRect(x , y, size, size);

        for (RoadDisplay r : roads) {
            r.drawRoad();
       }
    }

    // Cycles over each road type and determines based on the
    // start lane passed in where the car could get to
    // so if in left turn lane then it has to turn, in middle lane
    // it has to go straight, and in right line it can do straight or right
    //
    public LaneDisplay getRandomDest(LaneDisplay start){

        LinkedList<RoadDisplay> possible = new LinkedList<>();
        Random r = new Random();

        if (start.side == Direction.NORTH ){
            if (start.count == 2){
                possible.add(roads.get(2)); // left lane so has to turn east
            } else if (start.count == 0){
                possible.add(roads.get(1)); // right lane so it has two options
                possible.add(roads.get(3));
            } else {
                possible.add(roads.get(1)); // mid lane so must head south
            }
        } else if (start.side == Direction.EAST){

            if (start.count == 2){
                possible.add(roads.get(1));
            } else if (start.count == 0){
                possible.add(roads.get(0));
                possible.add(roads.get(3));
            } else {
                possible.add(roads.get(3));
            }
        } else if (start.side == Direction.SOUTH){
            if (start.count == 4 ){
                possible.add(roads.get(2));
                possible.add(roads.get(0));
            } else if (start.count == 2){
                possible.add(roads.get(3));
            } else {
                possible.add(roads.get(0));
            }
        } else if (start.side == Direction.WEST) {
            if (start.count == 2) {
                possible.add(roads.get(0));
            } else if (start.count == 3){
                possible.add(roads.get(2));
            } else {
                possible.add(roads.get(2));
                possible.add(roads.get(1));
            }
        }

        // Get random road from possible destinations
        int range = (possible.size() - 1) + 1;
        int randRoad = r.nextInt(range);
        RoadDisplay road = possible.get(randRoad);

        return road.getRandomDest(start);
    }

    public void connectRoads(LinkedList<RoadDisplay> roads){
        this.roads = roads;
    }

    public LinkedList<RoadDisplay> getRoads(){
        return roads;
    }

}
