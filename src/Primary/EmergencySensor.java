package Primary;

public class EmergencySensor {

    Lane lane;

    public EmergencySensor(Lane lane){
        this.lane = lane;
    }

    public Lane getLane(){
        return this.lane;
    }
}
