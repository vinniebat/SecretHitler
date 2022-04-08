package sh.shinterface.game.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.game.Game;
import sh.shinterface.util.PlayerStringConverter;

public class FivePlayerGameWindow extends SplitPane {

    private final TableView<Gov> govTable;

    public FivePlayerGameWindow(Game game, Role role) {
        PlayerStringConverter playerStringConverter = new PlayerStringConverter(game);
        SplitPane leftSide = new SplitPane();
        leftSide.setOrientation(Orientation.VERTICAL);
        govTable = new TableView<>();

        govTable.setPlaceholder(new Label("No govs yet!"));

        TableColumn<Gov, String> president = new TableColumn<>("President");
        TableColumn<Gov, String> chancellor = new TableColumn<>("Chancellor");
        TableColumn<Gov, String> claim = new TableColumn<>("Claim(s)");

        president.setCellValueFactory(data -> new SimpleStringProperty(playerStringConverter.toString(data.getValue().president())));
        chancellor.setCellValueFactory(data -> new SimpleStringProperty(playerStringConverter.toString(data.getValue().chancellor())));
        claim.setCellValueFactory(data -> data.getValue().displayClaims());

        govTable.getColumns().setAll(president, chancellor, claim);

        leftSide.getItems().addAll(govTable, new NewGovPane(game, role));

        VBox rightSide = new VBox();

        rightSide.getChildren().add(new RightUpperWindow(game));

        this.getItems().addAll(leftSide, rightSide);
        this.getStyleClass().add("inner-box");
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }
}
