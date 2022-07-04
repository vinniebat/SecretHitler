package sh.shinterface.screen;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import sh.shinterface.playable.Policy;
import sh.shinterface.playable.Role;
import sh.shinterface.playable.gov.Gov;
import sh.shinterface.view.CreateGovPane;
import sh.shinterface.view.GovView;
import sh.shinterface.view.PartyView;
import sh.shinterface.view.TopDeckWindow;

import java.io.IOException;
import java.util.List;

public class GameWindow extends TitledScreen {

    @FXML
    private TableView<Gov> govTable;
    @FXML
    private TableColumn<Gov, List<Policy>> claimsColumn;
    @FXML
    private TableColumn<Gov, List<Policy>> assumptionsColumn;
    @FXML
    private TopDeckWindow topDeckWindow;
    @FXML
    private CreateGovPane createGovPane;
    @FXML
    private PartyView partyView;
    @FXML
    private GovView govView;

    @FXML
    private Game game;

    public GameWindow(Role role) {
        super("INTERFACE");
        this.getStyleClass().addAll(role.getStyle());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Could not load gameWindow.fxml");
            Platform.exit();
        }
    }

    public void setGame(Game game) {
        this.game = game;
        createGovPane.setGame(game);
        govView.setGame(game);
        partyView.setGame(game);
    }

    public GameWindow() {
        super("INTERFACE");
        this.game = null;
    }

    public void initialize() {
        govTable.getItems().addListener((ListChangeListener<Gov>) change -> {
            while (change.next()){
                if (change.wasAdded()) {
                    govTable.getSelectionModel().select(govTable.getItems().size()-1);
                }
            }
        });
        setStyle("-fx-font-size: " + Screen.getPrimary().getBounds().getWidth() * 0.01);
        claimsColumn.setCellFactory(tc -> new PolicyCell());
        assumptionsColumn.setCellFactory(tc -> new PolicyCell());
    }

    public void navigateTable(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            govTable.getSelectionModel().selectPrevious();
        } else if (event.getCode() == KeyCode.DOWN) {
            govTable.getSelectionModel().selectNext();
        }
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

    public int getLastId() {
        ObservableList<Gov> govs = govTable.getItems();
        if (govs.isEmpty()) {
            return 0;
        }
        return govs.get(govs.size()-1).getPresident().getId();
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }

    public TopDeckWindow getTopDeckWindow() {
        return topDeckWindow;
    }

    public void toggleTopDeck() {
        createGovPane.enableTopDeckButton();
    }

    public CreateGovPane getCreateGovPane() {
        return createGovPane;
    }
}
