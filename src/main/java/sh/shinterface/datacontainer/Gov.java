package sh.shinterface.datacontainer;

import java.util.List;

public interface Gov {

    Player getPresident();

    Player getChancellor();

    ObservableHBox displayClaims();

    Policy[] getCards();

    Policy getPlayed();

    List<Vote> getVotes();
}
