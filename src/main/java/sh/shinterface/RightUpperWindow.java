package sh.shinterface;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RightUpperWindow extends HBox {

    public RightUpperWindow(Game game) {
        //contains playerOverview and deck info
        HBox hBox = new HBox();

        HBox playerOverview = new HBox();
        for (Player player :
                game.getPlayers()) {
            playerOverview.getChildren().add(new PlayerOverviewComponent(player, game));
        }

        this.getChildren().add(playerOverview);

    }
}
