package Primary;

import java.util.LinkedList;

public class Light {

    private int numLanes;
    private int turnLanes;
    private String name;
    public LinkedList<Lane> lanes = new LinkedList<>();

    public Light(String name, int numLanes, int turnLanes){
        //Initialize instance variables.
        this.name = name;
        this.turnLanes = turnLanes;
        this.numLanes = numLanes;

        //Create lanes for each light.
        for(int i = 0; i < numLanes; i++){
            boolean isTurnLane = false;
            if(i < turnLanes){
                isTurnLane = true;
            }
            lanes.add(new Lane(isTurnLane, this.name + " - Lane " + i));
        }
    }

    public LinkedList<Lane> getLanes(){
        return lanes;
    }

    public String getName(){
        return this.name;
    }
}
