package sh.shinterface.game.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.GovModel;
import sh.shinterface.db.CreateTables;
import sh.shinterface.game.Game;

public class BoardPane extends StackPane implements InvalidationListener {

    private final String players;
    private final ImageView liberalBoard;
    private final ImageView fascistBoard;
    private final GovModel govModel;

    public BoardPane(GovModel govModel, Game game) {
        this.govModel = govModel;
        this.players = govModel.getPlayerString();
        liberalBoard = new ImageView(new Image("sh/shinterface/images/boards/liberal/0.png"));
        fascistBoard = new ImageView(new Image("sh/shinterface/images/boards/fascist" + players + "/0.png"));

        liberalBoard.setPreserveRatio(true);
        fascistBoard.setPreserveRatio(true);
        liberalBoard.setFitHeight(150);
        fascistBoard.setFitHeight(150);

        VBox boards = new VBox(liberalBoard, fascistBoard);
        Button endGameButton = new Button("End game");
        endGameButton.setOnAction(e -> endGame(game, endGameButton));
        HBox buttonBox = new HBox(endGameButton);
        this.getChildren().addAll(boards, buttonBox);
    }

    public void endGame(Game game, Button endGameButton) {
        endGameButton.setText("Save Game");
        endGameButton.setOnAction(e -> saveGame(game, endGameButton));
        game.end();
    }

    public void invalidated(Observable observable) {
        int libPlayed = govModel.getLibPlayed();
        int fascPlayed = govModel.getFascPlayed();
        liberalBoard.setImage(new Image("sh/shinterface/images/boards/liberal/" + libPlayed + ".png"));
        fascistBoard.setImage(new Image("sh/shinterface/images/boards/fascist" + players + "/" + fascPlayed + ".png"));
    }

    public void saveGame(Game game, Button button) {
        CreateTables.createDB(game, button);
    }
}
