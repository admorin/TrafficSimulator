package Primary;

public enum SignalColor {
    RED (0),
    YELLOW (1),
    GREEN (2);

    int color;

    SignalColor(int i) {
        this.color = i;
    }
}
