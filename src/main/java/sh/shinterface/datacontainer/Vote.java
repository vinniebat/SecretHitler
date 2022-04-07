package sh.shinterface.datacontainer;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public enum Vote {
    JA("JA"), NEIN("NEIN");

    private String vote;
    private Rectangle graphic;

    Vote(String str) {
        vote = str;
        if (str.equals("JA")) {
            graphic = new Rectangle(10, 20, Color.GREEN);
        } else {
            graphic = new Rectangle(10, 20, Color.RED);
        }
    }

    public String getVote() {
        return vote;
    }

    public Rectangle getGraphic() {
        return graphic;
    }
}
