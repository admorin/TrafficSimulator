package Primary;

public class Lane {

    private boolean isTurn;
    private String name;
    private boolean carOnLane;
    private boolean emergencyOnLane;
    private MicrowaveSensor microwaveSensor;
    private EmergencySensor emergencySensor;

    public Lane(boolean isTurn, String name){
        this.isTurn = isTurn;
        this.name = name;
        this.microwaveSensor = new MicrowaveSensor(this);
        this.emergencySensor = new EmergencySensor(this);
    }

    public String getName (){
        return this.name;
    }

    public boolean carOnLane(){
        return this.carOnLane;
    }

    public boolean emergencyOnLane(){
        return this.emergencyOnLane;
    }
}
