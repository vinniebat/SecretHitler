package sh.shinterface.game.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.Game;
import sh.shinterface.util.PlayerStringConverter;

import java.util.Map;

public class GameWindow extends SplitPane {

    private static final Map<String, String> TOGGLETOPDECKTEXT = Map.of("Top deck", "Cancel", "Cancel", "Top deck");

    private final TableView<Gov> govTable;
    private final TopDeckWindow topDeckWindow;
    private final Button topDeckButton;

    public GameWindow(Game game, Role role) {
        PlayerStringConverter playerStringConverter = new PlayerStringConverter(game);
        SplitPane leftSide = new SplitPane();
        leftSide.setOrientation(Orientation.VERTICAL);
        govTable = new TableView<>();

        govTable.setPlaceholder(new Label("No govs yet!"));

        TableColumn<Gov, String> president = new TableColumn<>("President");
        TableColumn<Gov, String> chancellor = new TableColumn<>("Chancellor");
        TableColumn<Gov, String> claim = new TableColumn<>("Claim(s)");

        president.setCellValueFactory(data -> new SimpleStringProperty(playerStringConverter.toString(data.getValue().getPresident())));
        chancellor.setCellValueFactory(data -> new SimpleStringProperty(playerStringConverter.toString(data.getValue().getChancellor())));
        claim.setCellValueFactory(data -> data.getValue().displayClaims());

        govTable.getColumns().setAll(president, chancellor, claim);

        StackPane stackPane = new StackPane();
        topDeckWindow = new TopDeckWindow(govTable, role, this);
        topDeckWindow.setVisible(false);
        stackPane.getChildren().addAll(govTable, topDeckWindow);

        NewGovPane newGovPane = new NewGovPane(game, role, this);
        topDeckButton = newGovPane.getTopDeckButton();
        leftSide.getItems().addAll(stackPane, newGovPane);

        SplitPane rightSide = new SplitPane();
        rightSide.setOrientation(Orientation.VERTICAL);

        rightSide.getItems().addAll(new RightUpperWindow(game));

        this.getItems().addAll(leftSide, rightSide);
        this.getStyleClass().add("inner-box");
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }

    public void toggleTopDeck() {
        topDeckButton.setText(TOGGLETOPDECKTEXT.get(topDeckButton.getText()));
        govTable.setVisible(!govTable.isVisible());
        topDeckWindow.setVisible(!topDeckWindow.isVisible());
    }
}
