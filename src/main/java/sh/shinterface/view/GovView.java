package sh.shinterface.view;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import sh.shinterface.playable.gov.Gov;
import sh.shinterface.model.GovModel;
import sh.shinterface.view.tab.AssumptionPane;
import sh.shinterface.view.tab.BoardPane;
import sh.shinterface.screen.Game;

import java.io.IOException;

public class GovView extends TabPane implements InvalidationListener {

    @FXML
    private GovModel govModel;

    @FXML
    private Tab board;
    @FXML
    private Tab claimsAndAssumptions;
    private Game game;

    public GovView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("govView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Could not load govView.fxml");
            Platform.exit();
        }
    }

    public void setGame(Game game) {
        this.game = game;
        govModel.setGame(game);
        ((BoardPane) board.getContent()).setGame(game);
        game.getGovTable().getSelectionModel().selectedItemProperty().addListener(govModel);
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void invalidated(Observable observable) {
        if (govModel.getTable().getSelectionModel().getSelectedItem().getClaim1().size() < 3){
            claimsAndAssumptions.setDisable(true);
            this.getSelectionModel().select(board);
        }
    }
}
