package sh.shinterface.screen;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TitledScreen extends StackPane {

    public TitledScreen(String title) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");
        getChildren().add(titleLabel);
    }

}
