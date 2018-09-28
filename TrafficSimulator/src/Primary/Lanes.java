package Primary;

public enum Lanes {
    N1 (true, false, false, SignalColor.RED),
    N2 (false, false, false, SignalColor.RED),
    N3 (false, false, false, SignalColor.RED),
    E1 (true, false, false, SignalColor.RED),
    E2 (false, false, false, SignalColor.RED),
    E3 (false, false, false, SignalColor.RED),
    S1 (true, false, false, SignalColor.RED),
    S2 (false, false, false, SignalColor.RED),
    S3 (false, false, false, SignalColor.RED),
    W1 (true, false, false, SignalColor.RED),
    W2 (false, false, false, SignalColor.RED),
    W3 (false, false, false, SignalColor.RED);

    private final boolean isTurn;
    private boolean carOnLane;
    private boolean emergencyOnLane;
    private SignalColor color;

    Lanes(boolean isTurn, boolean carOnLane, boolean emergencyOnLane, SignalColor color) {
        this.isTurn = isTurn;
        this.carOnLane = carOnLane;
        this.emergencyOnLane = emergencyOnLane;
        this.color = color;
    }

    public boolean getCarOnLane(){
        return this.carOnLane;
    }

    public boolean getEmergencyOnLane(){
        return this.emergencyOnLane;
    }

    public void setCarOnLane(boolean carOnLane){
        this.carOnLane = carOnLane;
    }

    public void setEmergencyOnLane(boolean emergencyOnLane){
        this.emergencyOnLane = emergencyOnLane;
    }

    public void setColor(SignalColor color){
        this.color = color;
        //System.out.println("Changed color of " + this + " to " + color);
    }

    // Had to add this to get the value of the signal on the lane
    // is there an easier way to pull the last value out of an enum?? like Lanes.color or something ?
    public SignalColor getSignal(){
        return this.color;
    }

}
