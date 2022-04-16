package sh.shinterface.datacontainer;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class PlayerGov implements Gov {

    private final Player president;
    private final Player chancellor;
    private final Policy played;
    private final List<Policy> claim1;
    private final List<Policy> claim2;
    private final boolean conf;
    private final List<Vote> votes;
    private List<Policy> assumption = null;

    public PlayerGov(Player president, Player chancellor, Policy played, List<Policy> claim1, List<Policy> claim2, boolean conf, List<Vote> votes) {
        this.president = president;
        this.chancellor = chancellor;
        this.played = played;
        this.claim1 = claim1;
        this.claim2 = claim2;
        this.conf = conf;
        this.votes = votes;
    }

    public HBox getClaims() {
        HBox result = new HBox();
        for (Policy policy : claim1) {
            result.getChildren().add(new Rectangle(15, 20, policy.getColor()));
        }
        if (conf) {
            result.getChildren().add(new Label("\uD83D\uDDF2"));
            for (Policy policy : claim2) {
                result.getChildren().add(new Rectangle(15, 20, policy.getColor()));
            }
        }
//        String claimText = PolicyConverter.toString(claim1);
//        if (conf) {
//            claimText += " \uD83D\uDDF2 " + PolicyConverter.toString(claim2);
//        }
        return result;
    }

    @Override
    public Player getPresident() {
        return president;
    }

    @Override
    public Player getChancellor() {
        return chancellor;
    }

    public List<Policy> getCards() {
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

    public List<Vote> getVotes() {
        return votes;
    }

    @Override
    public List<Policy> getClaim1() {
        return claim1;
    }

    @Override
    public List<Policy> getClaim2() {
        return claim2;
    }

    @Override
    public boolean isConf() {
        return conf;
    }

    @Override
    public List<Policy> getAssumption() {
        if (assumption == null) {
            return claim1;
        } else {
            return assumption;
        }
    }
}