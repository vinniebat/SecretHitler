package sh.shinterface.datacontainer;

import javafx.scene.paint.Color;

public enum Vote {
    JA("JA", Color.GREEN), NEIN("NEIN", Color.RED);

    private String vote;
    private Color color;

    Vote(String str, Color color) {
        vote = str;
        this.color = color;
    }

    public String getVote() {
        return vote;
    }

    public Color getColor() {
        return color;
    }
}
