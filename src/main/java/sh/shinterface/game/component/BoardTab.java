package sh.shinterface.game.component;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.GovModel;

public class BoardTab extends StackPane {

    private final String players;
    private GovModel govModel;
    private final ImageView liberalBoard;
    private final ImageView fascistBoard;

    public BoardTab(GovModel govModel) {
        this.players = govModel.getPlayerString();
        this.govModel = govModel;
        liberalBoard = new ImageView(new Image("sh/shinterface/images/boards/liberal/0.png"));
        fascistBoard = new ImageView(new Image("sh/shinterface/images/boards/fascist" + players + "/0.png"));

        liberalBoard.setPreserveRatio(true);
        fascistBoard.setPreserveRatio(true);
        liberalBoard.setFitHeight(150);
        fascistBoard.setFitHeight(150);

        VBox boards = new VBox(liberalBoard, fascistBoard);
        Button endGameButton = new Button("End game");
        endGameButton.setOnAction(e -> endGame());
        HBox buttonBox = new HBox(endGameButton);
        this.getChildren().addAll(boards, buttonBox);
    }

    private void endGame() {

    }

    public void updateBoards(int lib, int fasc) {
        liberalBoard.setImage(new Image("sh/shinterface/images/boards/liberal/" + lib + ".png"));
        fascistBoard.setImage(new Image("sh/shinterface/images/boards/fascist" + players + "/" + fasc + ".png"));
    }


}
