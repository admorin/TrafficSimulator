package Primary;

public enum Lanes {
    N1 (1, true, false, false, SignalColor.RED),
    N2 (2, false, false, false, SignalColor.RED),
    N3 (3, false, false, false, SignalColor.RED),
    E1 (11, true, false, false, SignalColor.RED),
    E2 (12, false, false, false, SignalColor.RED),
    E3 (13, false, false, false, SignalColor.RED),
    S1 (21, true, false, false, SignalColor.RED),
    S2 (22, false, false, false, SignalColor.RED),
    S3 (23, false, false, false, SignalColor.RED),
    W1 (31, true, false, false, SignalColor.RED),
    W2 (32, false, false, false, SignalColor.RED),
    W3 (33, false, false, false, SignalColor.RED);

    public final int i;
    public final boolean isTurn;
    public boolean carOnLane;
    public boolean emergencyOnLane;
    public SignalColor color;

    Lanes(int i, boolean isTurn, boolean carOnLane, boolean emergencyOnLane, SignalColor color) {
        this.i = i;
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
        System.out.println("Changed color of " + this + " to " + color);
    }
}
