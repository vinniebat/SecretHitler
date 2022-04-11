package sh.shinterface.datacontainer;

import javafx.scene.paint.Color;

public enum Vote {
    JA("JA"), NEIN("NEIN");

    private String vote;
    private Color color;

    Vote(String str) {
        vote = str;
        if (str.equals("JA")) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
    }

    public String getVote() {
        return vote;
    }

    public Color getColor() {
        return color;
    }
}
