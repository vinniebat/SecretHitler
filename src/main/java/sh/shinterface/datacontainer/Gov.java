package sh.shinterface.datacontainer;

import javafx.scene.layout.HBox;

import java.util.List;

public interface Gov {

    Player getPresident();

    Player getChancellor();

    HBox getClaims();

    List<Policy> getCards();

    Policy getPlayed();

    List<Vote> getVotes();
}
