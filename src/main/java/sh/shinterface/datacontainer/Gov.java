package sh.shinterface.datacontainer;

import javafx.beans.property.SimpleStringProperty;

public interface Gov {

    Player president();

    Player chancellor();

    SimpleStringProperty displayClaims();
}
