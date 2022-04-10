package sh.shinterface.game.component;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.GovModel;

public class GovSpecifics extends TabPane {

    private final GovModel govModel;

    public GovSpecifics(TableView<Gov> tableView) {
        Tab board = new Tab("Board");
        Tab claimsAndAssumptions = new Tab("Claims and assumptions");

        this.getTabs().addAll(board, claimsAndAssumptions);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        govModel = new GovModel(tableView, this);
    }
}
