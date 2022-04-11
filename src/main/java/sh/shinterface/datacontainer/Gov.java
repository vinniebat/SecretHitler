package sh.shinterface.datacontainer;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public interface Gov {

    Player getPresident();

    Player getChancellor();

    SimpleStringProperty displayClaims();

    Policy[] getCards();

    Policy getPlayed();

    List<Vote> getVotes();
}
