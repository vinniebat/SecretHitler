package sh.shinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class FivePlayerGame extends Bord1Game {

    private final List<Player> players;
    private TableView<Gov> govTable;

    public FivePlayerGame(List<Player> players) {
        this.players = players;
    }

    @Override
    public void start() {
        Stage stage = new Stage();
        SplitPane gameVenster = new SplitPane();
        Scene scene = new Scene(gameVenster);

        VBox leftSide = new VBox();
        govTable = new TableView<>();

        TableColumn<Gov, String> president = new TableColumn<>("President");
        TableColumn<Gov, String> chancellor = new TableColumn<>("Chancellor");
        TableColumn<Gov, String> claim = new TableColumn<>("Claim(s)");

        president.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().president().getId() + ". " + data.getValue().president().getName()));
        chancellor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().chancellor().getId() + ". " + data.getValue().chancellor().getName()));
        claim.setCellValueFactory(data -> data.getValue().displayClaims());

        govTable.getColumns().setAll(president, chancellor, claim);

        //test
        govTable.getItems().add(new Gov(new Player(1, "Vincent"), new Player(2, "Wouter"), 1, new int[]{1, 2, 2}, null, false, null));
        govTable.getItems().add(new Gov(new Player(2, "Wouter"), new Player(1, "Vincent"), 1, new int[]{1, 2, 2}, new int[]{2, 2}, true, null));

        leftSide.getChildren().add(govTable);
        leftSide.getChildren().add(new NewGovPane(this));
        gameVenster.getItems().add(leftSide);

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    public TableView<Gov> getGovTable() {
        return govTable;
    }
}
