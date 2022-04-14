package sh.shinterface.game.component;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.Game;

import java.util.Map;

public class GameWindow extends SplitPane {

    private static final Map<String, String> TOGGLETOPDECKTEXT = Map.of("Top deck", "Cancel", "Cancel", "Top deck");

    //TODO GovTable css for selection and scrollbar
    private final TableView<Gov> govTable;
    private final TopDeckWindow topDeckWindow;
    private final Button topDeckButton;
    private final NewGovPane newGovPane;

    public GameWindow(Game game, Role role) {
        govTable = new TableView<>();

        govTable.setPlaceholder(new Label("No govs yet!"));
        govTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Gov, String> president = new TableColumn<>("President");
        TableColumn<Gov, String> chancellor = new TableColumn<>("Chancellor");
        TableColumn<Gov, HBox> claim = new TableColumn<>("Claim(s)");
        claim.getStyleClass().add("claims");

        president.setCellValueFactory(new PropertyValueFactory<>("president"));
        chancellor.setCellValueFactory(new PropertyValueFactory<>("chancellor"));
        claim.setCellValueFactory(new PropertyValueFactory<>("claims"));

        govTable.getColumns().setAll(president, chancellor, claim);

        topDeckWindow = new TopDeckWindow(govTable, role, this);
        topDeckWindow.setVisible(false);
        StackPane stackPane = new StackPane(govTable, topDeckWindow);
        stackPane.getStyleClass().add("gov-stack");

        newGovPane = new NewGovPane(game, role, this);
        topDeckButton = newGovPane.getTopDeckButton();
        VBox leftSide = new VBox(stackPane, newGovPane);
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        leftSide.getStyleClass().add("left");

        RightUpperWindow rightUpperWindow = new RightUpperWindow(game, govTable);
        GovSpecifics specifics = new GovSpecifics(game, govTable);
        SplitPane.setResizableWithParent(specifics, false);
        SplitPane rightSide = new SplitPane(rightUpperWindow, specifics);
        rightSide.setOrientation(Orientation.VERTICAL);
        govTable.getSelectionModel().selectedItemProperty().addListener(rightUpperWindow);

        this.getItems().addAll(leftSide, rightSide);
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }

    public void toggleTopDeck() {
        topDeckButton.setText(TOGGLETOPDECKTEXT.get(topDeckButton.getText()));
        govTable.setVisible(!govTable.isVisible());
        topDeckWindow.setVisible(!topDeckWindow.isVisible());
    }

    public NewGovPane getNewGovPane() {
        return newGovPane;
    }
}
