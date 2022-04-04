package sh.shinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class FivePlayerGameWindow extends SplitPane {

    private final TableView<Gov> govTable;

    public FivePlayerGameWindow(Game game) {
        VBox leftSide = new VBox();
        govTable = new TableView<>();

        TableColumn<Gov, String> president = new TableColumn<>("President");
        TableColumn<Gov, String> chancellor = new TableColumn<>("Chancellor");
        TableColumn<Gov, String> claim = new TableColumn<>("Claim(s)");

        president.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().president().getId() + ". " + data.getValue().president().getName()));
        chancellor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().chancellor().getId() + ". " + data.getValue().chancellor().getName()));
        claim.setCellValueFactory(data -> data.getValue().displayClaims());

        govTable.getColumns().setAll(president, chancellor, claim);

        leftSide.getChildren().add(govTable);
        leftSide.getChildren().add(new NewGovPane(game));

        VBox rightSide = new VBox();

        //do rightside


        this.getItems().addAll(leftSide, rightSide);
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }
}
