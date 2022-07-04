package sh.shinterface.view.tab;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sh.shinterface.model.GovModel;
import sh.shinterface.db.CreateTables;
import sh.shinterface.screen.Game;

import java.io.IOException;

public class BoardPane extends StackPane implements InvalidationListener {

    private int maxFasc;
    @FXML
    private ImageView liberalBoard;
    @FXML
    private ImageView fascistBoard;
    private GovModel govModel;
    @FXML
    private Button gameButton;
    private Game game;

    public BoardPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("boardPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            System.err.println("Could not load boardPane.fxml");
            Platform.exit();
        }
    }

    public GovModel getGovModel() {
        return govModel;
    }

    public void setGovModel(GovModel govModel) {
        this.govModel = govModel;
        govModel.addListener(this);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.maxFasc = (game.getPlayers().size()-1)/2;
        fascistBoard.setImage(new Image("sh/shinterface/images/boards/fascist" + maxFasc + "/0.png"));
    }

    public void endGame(ActionEvent event) {
        gameButton.setText("Save Game");
        gameButton.setOnAction(e -> saveGame());
        game.end();
    }

    public void invalidated(Observable observable) {
        int libPlayed = govModel.getLibPlayed();
        int fascPlayed = govModel.getFascPlayed();
        liberalBoard.setImage(new Image("sh/shinterface/images/boards/liberal/" + libPlayed + ".png"));
        fascistBoard.setImage(new Image("sh/shinterface/images/boards/fascist" + maxFasc + "/" + fascPlayed + ".png"));
        if (libPlayed >= 5 || fascPlayed >= 6) {
            endGame(null);
        }
    }

    public void saveGame() {
        CreateTables.createDB(game, gameButton);
    }
}
