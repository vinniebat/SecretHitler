package sh.shinterface.game.component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sh.shinterface.datacontainer.Gov;
import sh.shinterface.datacontainer.Policy;
import sh.shinterface.datacontainer.Role;
import sh.shinterface.datacontainer.TopDeck;

public class TopDeckWindow extends VBox {

    public TopDeckWindow(TableView<Gov> tableView, Stage stage, Role role) {
        VBox innerContainer = new VBox();
        HBox buttons = new HBox();

        Button lib = new Button("Lib");
        Button fasc = new Button("Fasc");
        lib.setOnAction(e -> buttonAction(tableView, Policy.LIBERAL, stage));
        fasc.setOnAction(e -> buttonAction(tableView, Policy.FASCIST, stage));

        buttons.getChildren().addAll(lib, fasc);
        innerContainer.getChildren().addAll(new Label("Topdeck policy:"), buttons);
        innerContainer.getStyleClass().addAll(role.getStyle(), "inner-box");
        this.getChildren().add(innerContainer);
    }

    private void buttonAction(TableView<Gov> tableView, Policy policy, Stage stage) {
        tableView.getItems().add(new TopDeck(policy));
        stage.close();
    }
}
