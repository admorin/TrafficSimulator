package Primary;

import javafx.scene.paint.Color;

public enum SignalColor {
    RED (Color.RED),
    YELLOW (Color.YELLOW),
    GREEN (Color.GREEN);

    private Color color;

    SignalColor(Color color) {
        this.color = color;
    }
}
