package sh.shinterface.datacontainer;

import javafx.beans.property.SimpleStringProperty;
import sh.shinterface.util.PolicyConverter;

import java.util.List;

public record PlayerGov(Player president, Player chancellor, int played, Policy[] claim1, Policy[] claim2, boolean conf,
                        List<Vote> votes) implements Gov {

    public SimpleStringProperty displayClaims() {
        String claimText = PolicyConverter.toString(claim1);
        if (conf) {
            claimText += " \uD83D\uDDF2 " + PolicyConverter.toString(claim2);
        }
        return new SimpleStringProperty(claimText);
    }
}