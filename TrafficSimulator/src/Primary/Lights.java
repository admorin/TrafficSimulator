package Primary;

public enum Lights {
    NORTH (0, false),
    EAST (1, false),
    SOUTH (2, false),
    WEST (3, false);

    private final int number;
    private boolean pedestrianAt;

    Lights(int i, boolean pedestrianAt) {
        this.number = i;
        this.pedestrianAt = pedestrianAt;
    }

    public void setPedestrianAt(boolean pedestrianAt){
        this.pedestrianAt = pedestrianAt;
    }

    public boolean isPedestrianAt() {
        return pedestrianAt;
    }
}
