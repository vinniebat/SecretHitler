package sh.shinterface.datacontainer;

import javafx.beans.property.SimpleStringProperty;
import sh.shinterface.util.PolicyConverter;

import java.util.List;

public class PlayerGov implements Gov {

    private final Player president;
    private final Player chancellor;
    private final Policy played;
    private final Policy[] claim1;
    private final Policy[] claim2;
    private final boolean conf;
    private final List<Vote> votes;
    private Policy[] assumption = null;

    public PlayerGov(Player president, Player chancellor, Policy played, Policy[] claim1, Policy[] claim2, boolean conf, List<Vote> votes) {
        this.president = president;
        this.chancellor = chancellor;
        this.played = played;
        this.claim1 = claim1;
        this.claim2 = claim2;
        this.conf = conf;
        this.votes = votes;
    }

    public SimpleStringProperty displayClaims() {
        String claimText = PolicyConverter.toString(claim1);
        if (conf) {
            claimText += " \uD83D\uDDF2 " + PolicyConverter.toString(claim2);
        }
        return new SimpleStringProperty(claimText);
    }

    @Override
    public Player getPresident() {
        return president;
    }

    @Override
    public Player getChancellor() {
        return chancellor;
    }

    public Policy[] getCards() {
        if (assumption != null) {
            return assumption;
        } else {
            return claim1;
        }
    }

    @Override
    public Policy getPlayed() {
        return played;
    }
}