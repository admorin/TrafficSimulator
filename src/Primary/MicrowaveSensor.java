package Primary;

public class MicrowaveSensor {

    private Lane lane;

    public MicrowaveSensor(Lane lane){
        this.lane = lane;
    }

    public Lane getLane(){
        return this.lane;
    }
}
