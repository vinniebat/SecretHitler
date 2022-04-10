package sh.shinterface.datacontainer;

import javafx.beans.property.SimpleStringProperty;

public interface Gov {

    Player getPresident();

    Player getChancellor();

    SimpleStringProperty displayClaims();

    Policy[] getCards();
}
