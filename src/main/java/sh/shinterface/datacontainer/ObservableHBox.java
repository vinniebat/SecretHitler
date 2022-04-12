package sh.shinterface.datacontainer;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;

public class ObservableHBox implements ObservableValue<HBox> {

    private HBox hBox;

    public ObservableHBox(HBox hbox) {
        this.hBox = hbox;
    }

    @Override
    public void addListener(ChangeListener<? super HBox> changeListener) {
        //unused
    }

    @Override
    public void removeListener(ChangeListener<? super HBox> changeListener) {
        //unused
    }

    @Override
    public HBox getValue() {
        return hBox;
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        //unused
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        //unused
    }
}
