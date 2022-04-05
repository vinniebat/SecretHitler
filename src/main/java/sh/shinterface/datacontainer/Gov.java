package sh.shinterface.datacontainer;

import javafx.beans.property.SimpleStringProperty;
import sh.shinterface.util.PolicyConverter;

import java.util.List;

public record Gov(Player president, Player chancellor, int played, List<Policy> claim1, List<Policy> claim2, boolean conf,
                  List<Boolean> votes) {

    public SimpleStringProperty displayClaims() {
        String claimText = PolicyConverter.toString(claim1);
        if (conf) {
            claimText += " \uD83D\uDDF2 " + PolicyConverter.toString(claim2);
        }
        return new SimpleStringProperty(claimText);
    }
}