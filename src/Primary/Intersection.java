package Primary;

import java.util.LinkedList;

public class Intersection {

    public LinkedList<Light> lights = new LinkedList<>();

    public Intersection(){
        lights.add(new Light("North Light",3, 1));
        lights.add(new Light("East Light",3, 1));
        lights.add(new Light("South Light",3, 1));
        lights.add(new Light("West Light",3, 1));
    }

    public LinkedList<Light> getLights(){
        return this.lights;
    }

    public LinkedList<Lane> getLane(Light light){
        for(Light i : lights){
            if(light.equals(i)){
                return i.getLanes();
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
            names += i.getName();
            names += "   ";
        }
        return names;
    }

    public String printLanes(){
        String names = "";
        for(Light i : lights){
            for(Lane j : i.lanes){
                names += j.getName();
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
