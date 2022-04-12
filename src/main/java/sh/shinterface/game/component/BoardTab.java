package sh.shinterface.game.component;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.GovModel;
import sh.shinterface.game.Game;

public class BoardTab extends StackPane {

    private final int maxFasc;
    private final ImageView liberalBoard;
    private final ImageView fascistBoard;

    public BoardTab(GovModel govModel, Game game) {
        this.maxFasc = (govModel.getGame().getPlayers().size()-1)/2;
        liberalBoard = new ImageView(new Image("sh/shinterface/images/boards/liberal/0.png"));
        fascistBoard = new ImageView(new Image("sh/shinterface/images/boards/fascist" + maxFasc + "/0.png"));

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
        endGameButton.setOnAction(e -> saveGame(game));
        game.end();
    }

    public void updateBoards(int lib, int fasc) {
        liberalBoard.setImage(new Image("sh/shinterface/images/boards/liberal/" + lib + ".png"));
        fascistBoard.setImage(new Image("sh/shinterface/images/boards/fascist" + maxFasc + "/" + fasc + ".png"));
    }

    public void saveGame(Game game) {

    }
}
