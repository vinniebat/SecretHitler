package sh.shinterface.game.component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Policy;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.datacontainer.TopDeck;

public class TopDeckWindow extends VBox {

    public TopDeckWindow(TableView<Gov> tableView, Role role, GameWindow gameWindow) {

        Button lib = new Button("Liberal");
        Button fasc = new Button("Fascist");
        lib.setOnAction(e -> buttonAction(tableView, Policy.LIBERAL, gameWindow));
        fasc.setOnAction(e -> buttonAction(tableView, Policy.FASCIST, gameWindow));

        HBox buttons = new HBox(lib, fasc);
        this.getChildren().addAll(new Label("Topdeck policy:"), buttons);
    }

    private void buttonAction(TableView<Gov> tableView, Policy policy, GameWindow gameWindow) {
        tableView.getItems().add(new TopDeck(policy));
        gameWindow.toggleTopDeck();
    }
}
