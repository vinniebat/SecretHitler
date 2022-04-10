package sh.shinterface.datacontainer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.TableView;
import sh.shinterface.game.component.GovSpecifics;

public class GovModel implements InvalidationListener {

    private final GovSpecifics govSpecifics;
    private final TableView<Gov> tableView;

    private int lib;
    private int fasc;


    public GovModel(TableView<Gov> tableView, GovSpecifics govSpecifics) {
        this.govSpecifics = govSpecifics;
        lib = 0;
        fasc = 0;
        tableView.selectionModelProperty().addListener(this);
        this.tableView = tableView;
    }

    @Override
    public void invalidated(Observable observable) {
        Gov gov = tableView.getSelectionModel().getSelectedItem();
        int govIndex = tableView.getItems().indexOf(gov);

        lib = 0;
        fasc = 0;

        for (int i = 0; i < govIndex; i++) {
            for (Policy card : tableView.getItems().get(i).getCards()) {
                if (card.equals(Policy.LIBERAL)) {
                    lib++;
                } else {
                    fasc++;
                }
            }
        }
    }
}
