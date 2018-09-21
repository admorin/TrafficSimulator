package Primary;

import java.util.LinkedList;

public class Light extends Thread {

    private int numLanes;
    private int turnLanes;
    private String name;
    public LinkedList<Lane> lanes = new LinkedList<>();

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Light Thread" +
                    " is running");

        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught LIGHT " + this.name);
        }
    }

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

        for(Lane j : lanes){
            j.start();
        }
    }

    public LinkedList<Lane> getLanes(){
        return lanes;
    }

    public String getLightName(){
        return this.name;
    }
}
