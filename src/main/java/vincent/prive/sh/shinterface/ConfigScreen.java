package vincent.prive.sh.shinterface;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ConfigScreen {

    private static ArrayList<TextField> playerNames = new ArrayList<>();
    private static int aantalSpelers = 5;

    public static void display() {
        Stage stage = new Stage();
        VBox vBox = new VBox();
        createContent(vBox, stage);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("vincent/prive/sh/shinterface/configscreen.css");
        stage.setScene(scene);
        stage.show();
    }

    private static void createContent(VBox vBox, Stage stage) {
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
    }

    private static void setInhoud(MenuButton menuButton, MenuItem menuItem, VBox vBox) {
        menuButton.setText(menuItem.getText());

        aantalSpelers = Integer.parseInt(menuItem.getText());
        System.out.println(aantalSpelers);
        vBox.getChildren().clear();
        playerNames.clear();
        for (int i = 1; i <= aantalSpelers; i++) {
            HBox spelerRegistratie = new HBox();
            Label spelerlabel = new Label("Speler " + i + ":");
            TextField spelerTextField = new TextField();
            playerNames.add(spelerTextField);
            spelerRegistratie.getChildren().addAll(spelerlabel, spelerTextField);
            vBox.getChildren().add(spelerRegistratie);

            System.out.println("Biep wouter biep gay");

        }
    }

    private static void create(Stage stage) {
        boolean valid = true;
        ArrayList<Player> players = new ArrayList<>();


        for (int i = 0; i < playerNames.size(); i++) {
            TextField currentTextField = playerNames.get(i);
            String name = currentTextField.getText().trim();
            if (name.equals("")) {
                valid = false;
                currentTextField.getStyleClass().add("leegLabel");
            } else {
                players.add(new Player(i + 1, name));
                currentTextField.getStyleClass().remove("leegLabel");
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
