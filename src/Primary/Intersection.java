package Primary;

import java.util.LinkedList;

public class Intersection extends Thread {

    public LinkedList<Light> lights = new LinkedList<>();

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Intersection Thread" +
                    " is running");

        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught");
        }
    }

    public Intersection(){
        lights.add(new Light("North Light",3, 1));
        lights.add(new Light("East Light",3, 1));
        lights.add(new Light("South Light",3, 1));
        lights.add(new Light("West Light",3, 1));

        for(Light i : lights){
            i.start();
        }
    }

    public LinkedList<Light> getLights(){
        return this.lights;
    }

    public Lane getLane(Light light, int index){
        for(Light i : lights){
            if(light.equals(i)){
                return i.getLanes().get(index);
            }
        }
        return null;
    }

    public LinkedList<Lane> getLanes(){
        LinkedList<Lane> allLanes = new LinkedList<>();

        for(Light i : lights){
            allLanes.addAll(i.lanes);
        }
        return allLanes;
    }

    public String printLights(){
        String names = "";
        for(Light i : lights){
            names += i.getLightName();
            names += "   ";
        }
        return names;
    }

    public String printLanes(){
        String names = "";
        for(Light i : lights){
            for(Lane j : i.lanes){
                names += j.getLaneName();
                names += "   ";
            }
            names += "\n";
        }
        return names;
    }

    public boolean getCarOnLane(Lane lane){
        return lane.carOnLane();
    }

    public boolean getEmergencyOnLane(Lane lane){
        return lane.emergencyOnLane();
    }
}
