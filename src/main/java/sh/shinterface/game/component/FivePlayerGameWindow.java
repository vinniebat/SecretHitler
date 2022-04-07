package sh.shinterface.game.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.game.Game;

public class FivePlayerGameWindow extends SplitPane {

    private final TableView<Gov> govTable;

    public FivePlayerGameWindow(Game game) {
        SplitPane leftSide = new SplitPane();
        leftSide.setOrientation(Orientation.VERTICAL);
        govTable = new TableView<>();

        govTable.setPlaceholder(new Label("No govs yet!"));

        TableColumn<Gov, String> president = new TableColumn<>("President");
        TableColumn<Gov, String> chancellor = new TableColumn<>("Chancellor");
        TableColumn<Gov, String> claim = new TableColumn<>("Claim(s)");

        president.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().president().getId() + ". " + data.getValue().president().getName()));
        chancellor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().chancellor().getId() + ". " + data.getValue().chancellor().getName()));
        claim.setCellValueFactory(data -> data.getValue().displayClaims());

        govTable.getColumns().setAll(president, chancellor, claim);

        leftSide.getItems().addAll(govTable, new NewGovPane(game));

        VBox rightSide = new VBox();

        rightSide.getChildren().add(new RightUpperWindow(game));

        this.getItems().addAll(leftSide, rightSide);
        this.getStyleClass().add("inner-box");
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }
}
