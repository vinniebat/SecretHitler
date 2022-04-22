package sh.shinterface.game.component;

import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Policy;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.Game;
import sh.shinterface.game.component.gamewindow.CreateGovPane;
import sh.shinterface.game.component.gamewindow.GovView;
import sh.shinterface.game.component.gamewindow.PartyView;
import sh.shinterface.game.component.gamewindow.TopDeckWindow;

import java.util.List;
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
        TableColumn<Gov, List<Policy>> claim = new TableColumn<>("Claim(s)");
        TableColumn<Gov, List<Policy>> assumption = new TableColumn<>("Assumptions");

        president.setCellValueFactory(new PropertyValueFactory<>("president"));
        chancellor.setCellValueFactory(new PropertyValueFactory<>("chancellor"));
        claim.setCellValueFactory(new PropertyValueFactory<>("claims"));
        claim.setCellFactory(column -> new PolicyCell());
        assumption.setCellValueFactory(new PropertyValueFactory<>("assumption"));
        assumption.setCellFactory(column -> new PolicyCell());

        List<TableColumn<Gov, ?>> columns = List.of(president, chancellor, claim, assumption);
        for (TableColumn<Gov, ?> column : columns) {
            column.setSortable(false);
            column.setReorderable(false);
        }

        govTable.getColumns().setAll(columns);

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
        govTable.getSelectionModel().selectedItemProperty().addListener(specifics);
        SplitPane.setResizableWithParent(specifics, false);
        SplitPane rightSide = new SplitPane(partyView, specifics);
        rightSide.setOrientation(Orientation.VERTICAL);
        govTable.getSelectionModel().selectedItemProperty().addListener(partyView);

        this.getItems().addAll(leftSide, rightSide);

        govTable.getItems().addListener((ListChangeListener<Gov>) change -> {
            while (change.next()){
                if (change.wasAdded()) {
                    govTable.getSelectionModel().select(govTable.getItems().size()-1);
                }
            }
        });
        setStyle("-fx-font-size: " + Screen.getPrimary().getBounds().getWidth() * 0.01);
        this.getStyleClass().addAll(role.getStyle(), "interface");
        this.getStylesheets().add("sh/shinterface/stylesheets/interface.css");
    }

    private static class PolicyCell extends TextFieldTableCell<Gov, List<Policy>> {

        @Override
        public void updateItem(List<Policy> policies, boolean empty) {
            super.updateItem(policies, empty);
            setText(null);
            if (empty) {
                setGraphic(null);
            } else {
                HBox hBox = new HBox();
                List<Node> nodes = hBox.getChildren();
                int i = 0;
                while (i < policies.size() && i < 3) {
                    nodes.add(new Rectangle(15, 20, policies.get(i).getColor()));
                    i++;
                }
                while (i < 3) {
                    nodes.add(new Rectangle(15, 20, Color.TRANSPARENT));
                    i++;
                }
                if (policies.size() > 3) {
                    nodes.add(new Label("\uD83D\uDDF2"));
                }
                while (i < policies.size()) {
                    nodes.add(new Rectangle(15, 20, policies.get(i).getColor()));
                    i++;
                }
                setGraphic(hBox);
            }
        }
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }

    public void toggleTopDeck() {
        topDeckButton.setText(TOGGLETOPDECKTEXT.get(topDeckButton.getText()));
        govTable.setVisible(!govTable.isVisible());
        topDeckWindow.setVisible(!topDeckWindow.isVisible());
    }

    public CreateGovPane getCreateGovPane() {
        return createGovPane;
    }
}
