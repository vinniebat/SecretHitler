package sh.shinterface.playable.gov;

import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import sh.shinterface.playable.Player;
import sh.shinterface.playable.Policy;

import java.util.ArrayList;
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

    public List<Policy> getClaims() {
        List<Policy> claims = new ArrayList<>(claim1);
        if (conf)
            claims.addAll(claim2);
        return claims;
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

    @Override
    public HBox getAssumptionHBox() {
        HBox result = new HBox();
        if (assumption != null) {
            for (Policy policy : assumption) {
                result.getChildren().add(new Rectangle(15, 20, policy.getColor()));
            }
        }
        return result;
    }

    @Override
    public void setAssumption(int numberOfLibs) {
        int libs = (int) claim1.stream().filter(policy -> policy.equals(Policy.LIBERAL)).count();
        if (numberOfLibs == libs) {
            assumption = null;
        } else {
            List<Policy> newAssumption = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                if (3 - newAssumption.size() > numberOfLibs) {
                    newAssumption.add(Policy.FASCIST);
                } else {
                    newAssumption.add(Policy.LIBERAL);
                }
            }
            assumption = newAssumption;
        }
    }
}