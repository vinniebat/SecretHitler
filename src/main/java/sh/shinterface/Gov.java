package sh.shinterface;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public record Gov(Player president, Player chancellor, int played, int[] claim1, int[] claim2, boolean conf,
                  List<Boolean> votes) {

    public SimpleStringProperty displayClaims() {
        String claimText = PolicyConverter.toString(claim1);
        if (conf) {
            claimText += " \uD83D\uDDF2 " + PolicyConverter.toString(claim2);
        }
        return new SimpleStringProperty(claimText);
    }
}