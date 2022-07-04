package sh.shinterface.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sh.shinterface.playable.gov.Gov;
import sh.shinterface.playable.Policy;
import sh.shinterface.playable.Role;
import sh.shinterface.playable.gov.TopDeck;
import sh.shinterface.screen.GameWindow;

import java.io.IOException;

public class TopDeckWindow extends VBox {

    private TableView<Gov> tableView;

    private GameWindow gameWindow;

    public TopDeckWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("topDeckWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            System.err.println("Could not load topDeckWindow.fxml");
            Platform.exit();
        }
    }

    public void topDeckLiberal(ActionEvent event) {
        tableView.getItems().add(new TopDeck(Policy.LIBERAL));
        gameWindow.toggleTopDeck();
    }

    public void topDeckFascist(ActionEvent event) {
        tableView.getItems().add(new TopDeck(Policy.FASCIST));
        gameWindow.toggleTopDeck();
    }

    public TableView<Gov> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Gov> tableView) {
        this.tableView = tableView;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }
}
