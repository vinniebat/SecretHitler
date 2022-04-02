package sh.shinterface;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NewGovPane extends VBox {

    public NewGovPane(Game game) {
        HBox presBox = new HBox();
        HBox chancBox = new HBox();
        Label presLabel = new Label("President: ");
        Label chancLabel = new Label("Chancellor: ");
        PlayerStringConverter playerStringConverter = new PlayerStringConverter(game);
        ChoiceBox<Player> presidentChoiceBox = new ChoiceBox<>();
        ChoiceBox<Player> chancellorChoiceBox = new ChoiceBox<>();
        presidentChoiceBox.setConverter(playerStringConverter);
        chancellorChoiceBox.setConverter(playerStringConverter);
        presidentChoiceBox.getItems().setAll(game.getPlayers());
        chancellorChoiceBox.getItems().setAll(game.getPlayers());

        presBox.getChildren().addAll(presLabel, presidentChoiceBox);
        chancBox.getChildren().addAll(chancLabel, chancellorChoiceBox);


        this.getChildren().addAll(new Label("Add new gov:"), presBox, chancBox, new Label("Votes"));

    }
}
