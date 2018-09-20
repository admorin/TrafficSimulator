package Primary;

public class Lane {

    private boolean isTurn;
    private String name;
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
}
