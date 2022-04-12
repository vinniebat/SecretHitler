package sh.shinterface.game.component;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.GovModel;
import sh.shinterface.game.Game;

public class GovSpecifics extends TabPane {

    private final GovModel govModel;
    private final BoardTab boardTab;

    public GovSpecifics(Game game, TableView<Gov> tableView) {
        Tab board = new Tab("Board");
        Tab claimsAndAssumptions = new Tab("Claims and assumptions");
        //claimsAndAssumptions.setDisable(true);

        govModel = new GovModel(game, this);
        tableView.getSelectionModel().selectedItemProperty().addListener(govModel);

        boardTab = new BoardTab(govModel, game);
        board.setContent(boardTab);

        this.getTabs().addAll(board, claimsAndAssumptions);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    }

    public BoardTab getBoardTab() {
        return boardTab;
    }
}
