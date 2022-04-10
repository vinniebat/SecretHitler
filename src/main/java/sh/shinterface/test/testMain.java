package sh.shinterface.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Used to test new Windows
 */
public class testMain extends Application {
    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Board");
        Tab tab2 = new Tab("Claims and assumptions");
        tabPane.getTabs().addAll(tab1, tab2);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Scene scene = new Scene(tabPane);
        scene.getStylesheets().add("sh/shinterface/testMain.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
