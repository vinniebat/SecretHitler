package sh.shinterface.datacontainer;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sh.shinterface.util.SpecialGovPlayers;

import java.util.ArrayList;
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
    public HBox getClaims() {
        Label label = new Label("\uD83D\uDDF2");
        label.setVisible(false);
        HBox hBox = new HBox(new Rectangle(15, 20, policy.getColor()),
                new Rectangle(15, 20, Color.TRANSPARENT),
                new Rectangle(15, 20, Color.TRANSPARENT));

        return hBox;
    }

    public List<Policy> getCards() {
        return List.of(policy);
    }

    @Override
    public Policy getPlayed() {
        return policy;
    }

    @Override
    public List<Vote> getVotes() {
        return null;
    }

    @Override
    public List<Policy> getClaim1() {
        return List.of(policy);
    }

    @Override
    public List<Policy> getClaim2() {
        return new ArrayList<>();
    }

    @Override
    public boolean isConf() {
        return false;
    }
}