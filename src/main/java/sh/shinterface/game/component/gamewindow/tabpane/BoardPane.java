package sh.shinterface.game.component.gamewindow.tabpane;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.GovModel;
import sh.shinterface.db.CreateTables;
import sh.shinterface.game.Game;

public class BoardPane extends StackPane implements InvalidationListener {

    private final int maxFasc;
    private final ImageView liberalBoard;
    private final ImageView fascistBoard;
    private final GovModel govModel;
    private final Button gameButton = new Button("End game");
    private final Game game;

    public BoardPane(GovModel govModel, Game game) {
        this.govModel = govModel;
        this.game = game;
        this.maxFasc = (govModel.getGame().getPlayers().size()-1)/2;
        liberalBoard = new ImageView(new Image("sh/shinterface/images/boards/liberal/0.png"));
        fascistBoard = new ImageView(new Image("sh/shinterface/images/boards/fascist" + maxFasc + "/0.png"));

        liberalBoard.setPreserveRatio(true);
        fascistBoard.setPreserveRatio(true);
        liberalBoard.setFitHeight(150);
        fascistBoard.setFitHeight(150);

        VBox boards = new VBox(liberalBoard, fascistBoard);
        gameButton.setOnAction(e -> endGame());
        HBox buttonBox = new HBox(gameButton);
        this.getChildren().addAll(boards, buttonBox);
    }

    public void endGame() {
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
            endGame();
        }
    }

    public void saveGame() {
        CreateTables.createDB(game, gameButton);
    }
}
