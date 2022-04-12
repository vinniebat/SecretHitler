package sh.shinterface.datacontainer;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sh.shinterface.util.SpecialGovPlayers;

import java.util.List;

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
    public ObservableHBox displayClaims() {
        Label label = new Label("\uD83D\uDDF2");
        label.setVisible(false);
        HBox hBox = new HBox(new Rectangle(15, 20, policy.getColor()),
                new Rectangle(15, 20, Color.TRANSPARENT),
                new Rectangle(15, 20, Color.TRANSPARENT));

        return new ObservableHBox(hBox);
    }

    public Policy[] getCards() {
        return new Policy[]{policy};
    }

    @Override
    public Policy getPlayed() {
        return policy;
    }

    @Override
    public List<Vote> getVotes() {
        return null;
    }


}