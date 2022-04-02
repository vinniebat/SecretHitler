package sh.shinterface;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ConfigScreen {

    private static final ArrayList<HBox> playerFields = new ArrayList<>();
    private static int aantalSpelers = 5;
    private static Stage stage;

    public static void display() {
        Stage stage = new Stage();
        StackPane stackPane = new StackPane();
        createContent(stackPane, stage);
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("secret-hitler-icon.png")));
        stage.setTitle("Secret Hitler");
        stage.show();
    }

    private static void createContent(StackPane stackPane, Stage stage) {
        ConfigScreen.stage = stage;
        VBox vBox = new VBox();
        MenuButton menuButton = new MenuButton("5");
        VBox inhoudContainer = new VBox();
        for (int i = 5; i <= 10; i++) {
            MenuItem menuItem = new MenuItem(i + "");
            menuButton.getItems().add(menuItem);
            menuItem.setOnAction(e -> setInhoud(menuButton, menuItem, inhoudContainer));
        }
        setInhoud(menuButton, new MenuItem(aantalSpelers + ""), inhoudContainer);

        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> create(stage));

        vBox.getChildren().addAll(menuButton, inhoudContainer, createGameButton);
        stackPane.getStyleClass().add("selection-screen");
        Label title = new Label("SECRET HITLER");
        title.getStyleClass().add("title");
        stackPane.getChildren().addAll(vBox, title);
    }

    private static void setInhoud(MenuButton menuButton, MenuItem menuItem, VBox vBox) {
        menuButton.setText(menuItem.getText());

        aantalSpelers = Integer.parseInt(menuItem.getText());
        System.out.println(aantalSpelers);
        vBox.getChildren().clear();
        playerFields.clear();
        for (int i = 1; i <= aantalSpelers; i++) {
            HBox spelerRegistratie = new HBox();
            Label spelerlabel = new Label("Speler " + i + ":");
            TextField spelerTextField = new TextField();
            playerFields.add(spelerRegistratie);
            spelerRegistratie.getChildren().addAll(spelerlabel, spelerTextField);
            vBox.getChildren().add(spelerRegistratie);

            System.out.println("Biep wouter biep gay");
        }
        stage.sizeToScene();
    }

    private static void create(Stage stage) {
        boolean valid = true;
        ArrayList<Player> players = new ArrayList<>();

        int i;
        for (i = 0; i < playerFields.size(); i++) {
            HBox PlayerField = playerFields.get(i);
            TextField currentTextField = (TextField) PlayerField.getChildren().get(1);
            String name = currentTextField.getText().trim();
            if (name.isBlank()) {
                valid = false;
                PlayerField.getStyleClass().add("emptyField");
            } else {
                players.add(new Player(i + 1, name));
                PlayerField.getStyleClass().removeAll("emptyField");
            }
        }
        if (valid) {
            //later factory
            stage.close();
            Game game = new fivePlayerGame(players);
            game.start();
        }
    }
}
