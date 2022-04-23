package sh.shinterface.playable.gov;

import javafx.scene.layout.HBox;
import sh.shinterface.playable.Player;
import sh.shinterface.playable.Policy;

import java.util.List;

public interface Gov {

    Player getPresident();

    Player getChancellor();

    List<Policy> getClaims();

    List<Policy> getCards();

    Policy getPlayed();

    List<Vote> getVotes();

    List<Policy> getClaim1();

    List<Policy> getClaim2();

    boolean isConf();

    List<Policy> getAssumption();

    HBox getAssumptionHBox();

    void setAssumption(int numberOfLibs);
}
