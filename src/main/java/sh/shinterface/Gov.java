package sh.shinterface;

import javafx.beans.property.SimpleStringProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record Gov(Player president, Player chancellor, int played, int[] claim1, int[] claim2) {

    private static final Map<Integer, String> CONVERT= new HashMap<>() {{
        put(1, "Lib");
        put(2, "Fasc");
    }};

    public SimpleStringProperty displayClaims() {
        String claimText = claimText(claim1);
        if (claim2 != null) {
            claimText += " - " + claimText(claim2);
        }
        return new SimpleStringProperty(claimText);
    }

    private String claimText(int[] claim) {
        return Arrays.stream(claim).mapToObj(CONVERT::get).collect(Collectors.joining(", "));
    }
}