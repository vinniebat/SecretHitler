package sh.shinterface.game.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.GovModel;
import sh.shinterface.game.Game;

public class GovView extends TabPane implements InvalidationListener {

    private final GovModel govModel;
    private final Tab claimsAndAssumptions;

    public GovView(Game game, TableView<Gov> tableView) {
        Tab board = new Tab("Board");
        claimsAndAssumptions = new Tab("Claims and assumptions");
        claimsAndAssumptions.setDisable(true);

        govModel = new GovModel(game, this);
        tableView.getSelectionModel().selectedItemProperty().addListener(govModel);

        BoardPane boardPane = new BoardPane(govModel, game);
        board.setContent(boardPane);
        AssumptionPane assumptionPane = new AssumptionPane(govModel, claimsAndAssumptions);
        claimsAndAssumptions.setContent(assumptionPane);

        this.getTabs().addAll(board, claimsAndAssumptions);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        govModel.addListener(boardPane);
        govModel.addListener(assumptionPane);
    }

    @Override
    public void invalidated(Observable observable) {
        claimsAndAssumptions.setDisable(govModel.getTable().getSelectionModel().getSelectedItem().getClaim1().size() < 3);
    }
}
