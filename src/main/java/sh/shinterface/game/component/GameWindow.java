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
    private final CreateGovPane createGovPane;

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

        createGovPane = new CreateGovPane(game, role, this);
        topDeckButton = createGovPane.getTopDeckButton();
        VBox leftSide = new VBox(stackPane, createGovPane);
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        leftSide.getStyleClass().add("left");

        PartyView partyView = new PartyView(game, govTable);
        GovView specifics = new GovView(game, govTable);
        SplitPane.setResizableWithParent(specifics, false);
        SplitPane rightSide = new SplitPane(partyView, specifics);
        rightSide.setOrientation(Orientation.VERTICAL);
        govTable.getSelectionModel().selectedItemProperty().addListener(partyView);

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

    public CreateGovPane getNewGovPane() {
        return createGovPane;
    }
}
