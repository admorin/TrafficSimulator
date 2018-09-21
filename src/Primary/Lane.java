package Primary;

public class Lane extends Thread {

    private boolean isTurn;
    private String name;
    private boolean carOnLane;
    private boolean emergencyOnLane;
    private MicrowaveSensor microwaveSensor;
    private EmergencySensor emergencySensor;

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Lane Thread" +
                    " is running");

        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught LANE " + this.name);
        }
    }

    public Lane(boolean isTurn, String name){
        this.isTurn = isTurn;
        this.name = name;
        this.microwaveSensor = new MicrowaveSensor(this);
        this.emergencySensor = new EmergencySensor(this);
    }

    public String getLaneName (){
        return this.name;
    }

    public boolean carOnLane(){
        return this.carOnLane;
    }

    public boolean emergencyOnLane(){
        return this.emergencyOnLane;
    }

    public void setCarOnLane(){
        this.carOnLane = true;
    }
}
