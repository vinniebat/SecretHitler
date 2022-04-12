package sh.shinterface.game.component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Policy;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.datacontainer.TopDeck;

public class TopDeckWindow extends VBox {

    public TopDeckWindow(TableView<Gov> tableView, Role role, GameWindow gameWindow) {
        VBox innerContainer = new VBox();
        HBox buttons = new HBox();

        Button lib = new Button("Liberal");
        Button fasc = new Button("Fascist");
        lib.setOnAction(e -> buttonAction(tableView, Policy.LIBERAL, gameWindow));
        fasc.setOnAction(e -> buttonAction(tableView, Policy.FASCIST, gameWindow));

        buttons.getChildren().addAll(lib, fasc);
        innerContainer.getChildren().addAll(new Label("Topdeck policy:"), buttons);
        innerContainer.getStyleClass().addAll(role.getStyle(), "inner-box");
        VBox.setVgrow(innerContainer, Priority.ALWAYS);
        this.getChildren().add(innerContainer);
    }

    private void buttonAction(TableView<Gov> tableView, Policy policy, GameWindow gameWindow) {
        tableView.getItems().add(new TopDeck(policy));
        gameWindow.toggleTopDeck();
    }
}
