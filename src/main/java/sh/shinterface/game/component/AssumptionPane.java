package sh.shinterface.game.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.layout.GridPane;
import sh.shinterface.datacontainer.GovModel;

public class AssumptionPane extends GridPane implements InvalidationListener {

    private final GovModel govModel;

    public AssumptionPane(GovModel govModel) {
        this.govModel = govModel;
    }

    @Override
    public void invalidated(Observable observable) {
        //TODO
    }
}
