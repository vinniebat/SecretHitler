package sh.shinterface.screen;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TitledScreen extends StackPane {

    protected final Label titleLabel;

    public TitledScreen(String title) {
        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");
    }

    public void initialize() {
        getChildren().add(titleLabel);
    }

}
