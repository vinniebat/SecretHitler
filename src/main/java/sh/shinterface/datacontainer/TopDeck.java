package sh.shinterface.datacontainer;

import javafx.beans.property.SimpleStringProperty;
import sh.shinterface.util.PolicyConverter;
import sh.shinterface.util.SpecialGovPlayers;

public class TopDeck implements Gov {

    private static final Player president = SpecialGovPlayers.TOPDECK;
    private static final Player chancellor = SpecialGovPlayers.EMPTY;

    private final Policy policy;

    public TopDeck(Policy policy) {
        this.policy = policy;
    }

    @Override
    public Player getPresident() {
        return president;
    }

    @Override
    public Player getChancellor() {
        return chancellor;
    }

    @Override
    public SimpleStringProperty displayClaims() {
        return new SimpleStringProperty(PolicyConverter.toString(new Policy[]{policy}));
    }

    public Policy[] getCards() {
        return new Policy[]{policy};
    }


}