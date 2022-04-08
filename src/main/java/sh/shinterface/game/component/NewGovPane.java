package sh.shinterface.game.component;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sh.shinterface.datacontainer.*;
import sh.shinterface.game.Game;
import sh.shinterface.util.BooleanVoteConverter;
import sh.shinterface.util.PlayerStringConverter;
import sh.shinterface.util.PolicyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewGovPane extends VBox {

    private static final Map<String, String> SWITCH = Map.of("JA", "NEIN", "NEIN", "JA");
    private static final List<Character> STRINGPOLICIES = List.of('R', 'B');
    private static final List<Character> LOWERSTRINGPOLICIES = List.of('r', 'b');

    private final ChoiceBox<Player> presidentChoiceBox;
    private final ChoiceBox<Player> chancellorChoiceBox;
    private final TextField claim1;
    private final TextField claim2;
    private final CheckBox conf;
    private final List<ToggleButton> voteList = new ArrayList<>();
    private final GridPane govPlayers;

    public NewGovPane(Game game, Role role) {

        govPlayers = new GridPane();

        Label title1 = new Label("Add new gov:");
        title1.getStyleClass().add("titleLabel");

        Label presLabel = new Label("President: ");
        Label chancLabel = new Label("Chancellor: ");

        PlayerStringConverter playerStringConverter = new PlayerStringConverter(game);

        presidentChoiceBox = new ChoiceBox<>();
        chancellorChoiceBox = new ChoiceBox<>();
        presidentChoiceBox.valueProperty().addListener((observableValue, oldPlayer, newPlayer) -> choiceBoxAction(newPlayer, 0));
        chancellorChoiceBox.valueProperty().addListener((observableValue, oldPlayer, newPlayer) -> choiceBoxAction(newPlayer, 1));
        presidentChoiceBox.setConverter(playerStringConverter);
        chancellorChoiceBox.setConverter(playerStringConverter);
        List<Player> players = game.getPlayers();
        presidentChoiceBox.getItems().setAll(players);
        chancellorChoiceBox.getItems().setAll(players);

        presidentChoiceBox.valueProperty().addListener((observableValue, oldPlayer, newPlayer) -> {
            ObservableList<Player> items = chancellorChoiceBox.getItems();
            items.removeIf(player -> player.equals(newPlayer));
            if (oldPlayer != null) {
                items.add(insertIndex(oldPlayer, items), oldPlayer);
            }
        });

        chancellorChoiceBox.valueProperty().addListener((observableValue, oldPlayer, newPlayer) -> {
            ObservableList<Player> items = presidentChoiceBox.getItems();
            items.removeIf(player -> player.equals(newPlayer));
            if (oldPlayer != null) {
                items.add(insertIndex(oldPlayer, items), oldPlayer);
            }
        });


        claim1 = new TextField();
        claim2 = new TextField();
        claim1.setPromptText("Claim of president");
        claim2.setPromptText("Claim of chancellor");
        textFieldRestrict(claim1, 3);
        textFieldRestrict(claim2, 2);

        conf = new CheckBox("No conflict!");
        conf.selectedProperty().addListener(obs -> checkBoxAction(conf));

        govPlayers.addRow(0, presLabel, presidentChoiceBox, claim1);
        govPlayers.addRow(1, chancLabel, chancellorChoiceBox, claim2);
        govPlayers.add(new Label("Conflict?"), 0, 2);
        govPlayers.add(conf, 1, 2, 2, 1);

        Label title2 = new Label("Votes");
        title2.getStyleClass().add("titleLabel");

        GridPane votes = new GridPane();
        for (int i = 0; i < players.size(); i++) {
            Label voteName = new Label(playerStringConverter.toString(players.get(i)));
            ToggleButton jaNein = new ToggleButton("JA");
            jaNein.setOnAction(e -> switchVote(jaNein));
            votes.addRow(i % 5, voteName, jaNein);
            voteList.add(jaNein);
        }

        HBox buttons = new HBox();

        Button createGov = new Button("Create gov");
        createGov.setOnAction(e -> createGov(game));

        Button topDeck = new Button("Top deck");
        topDeck.setOnAction(e -> createTopDeckWindow(game, role));

        buttons.getChildren().addAll(createGov, topDeck);
        this.getChildren().addAll(title1, govPlayers, title2, votes, buttons);
    }

    private void switchVote(ToggleButton button) {
        button.setText(SWITCH.get(button.getText()));
    }

    private void createGov(Game game) {
        Player president = presidentChoiceBox.getValue();
        Player chancellor = chancellorChoiceBox.getValue();
        Policy[] claim1 = PolicyConverter.fromString(this.claim1.getText());
        Policy[] claim2 = PolicyConverter.fromString(this.claim2.getText());
        int played = 0;
        List<Vote> voteList = this.voteList.stream().map(toggle -> BooleanVoteConverter.fromBool(!toggle.isSelected())).toList();

        boolean presBool = choiceBoxCheck(presidentChoiceBox);
        boolean chancBool = choiceBoxCheck(chancellorChoiceBox);
        boolean valid = !(presBool || chancBool);

        if (claim1.length < 3) {
            valid = false;
            this.claim1.getStyleClass().add("newGovPaneTextFieldError");
        } else {
            this.claim1.getStyleClass().removeAll("newGovPaneTextFieldError");
            if (claim2.length < 2) {
                claim2 = autoGenerate(claim1);
            }
            if (Arrays.stream(claim2).anyMatch(i -> i == Policy.LIBERAL)) {
                played = 1;
            } else {
                played = 2;
            }
        }
        boolean conf = checkConf(claim1, claim2, this.conf.isSelected());

        if (voteList.stream().filter(BooleanVoteConverter::toBool).count() <= voteList.size() / 2) {
            valid = false;
            for (ToggleButton voteButton : this.voteList) {
                if (voteButton.getText().equals("NEIN")) {
                    voteButton.getStyleClass().add("newGovPaneBoxError");
                } else {
                    voteButton.getStyleClass().removeAll("newGovPaneBoxError");
                }
            }
        } else {
            for (ToggleButton voteButton : this.voteList) {
                voteButton.getStyleClass().removeAll("newGovPaneBoxError");
            }
        }

        if (valid) {
            resetPane();
            game.getGovTable().getItems().add(new PlayerGov(president, chancellor, played, claim1, claim2, conf, voteList));
        }
    }

    private void resetPane() {
        presidentChoiceBox.setValue(null);
        chancellorChoiceBox.setValue(null);
        claim1.setPromptText("Claim of president");
        claim1.setText("");
        claim2.setPromptText("Claim of chancellor");
        claim2.setText("");
        conf.setSelected(false);
        for (ToggleButton button :
                voteList) {
            button.setSelected(false);
            button.setText("JA");
        }
    }

    private void choiceBoxAction(Player player, int rij) {
        if (player != null) {
            TextField textField = (TextField) govPlayers.getChildren().get(3 * rij + 2);
            String startText = Stream.of(textField.getPromptText().split(" ")).limit(2).collect(Collectors.joining(" "));
            textField.setPromptText(startText + " " + player.getName());
        }
    }

    private void checkBoxAction(CheckBox box) {
        if (box.isSelected()) {
            box.setText("Conflict!");
        } else {
            box.setText("No conflict");
        }
    }

    public boolean checkConf(Policy[] claim1, Policy[] claim2, boolean conf) {
        boolean lib1 = Arrays.stream(claim1).anyMatch(p -> p == Policy.LIBERAL);
        boolean lib2 = Arrays.stream(claim2).anyMatch(p -> p == Policy.LIBERAL);
        return (lib1 ^ lib2) || conf;
    }

    private boolean choiceBoxCheck(ChoiceBox<Player> choiceBox) {
        if (choiceBox.getSelectionModel().getSelectedItem() == null) {
            choiceBox.getStyleClass().add("newGovPaneBoxError");
            return true;
        } else {
            choiceBox.getStyleClass().removeAll("newGovPaneBoxError");
            return false;
        }
    }

    private void textFieldRestrict(TextField textField, int numberOfClaims) {
        textField.textProperty().addListener((observableValue, oldString, newString) -> {
            if (newString.length() > numberOfClaims) {
                textField.setText(oldString);
            } else if (!newString.equals("") && newString.length() > oldString.length()) {
                Character newChar = newString.charAt(newString.length() - 1);
                if (LOWERSTRINGPOLICIES.contains(newChar)) {
                    textField.setText(oldString + newChar.toString().toUpperCase());
                } else if (!STRINGPOLICIES.contains(newChar)) {
                    textField.setText(oldString);
                }
            }
        });
    }

    private Policy[] autoGenerate(Policy[] claim1) {
        int blauw = (int) Arrays.stream(claim1).filter(policy -> policy == Policy.LIBERAL).count();
        Policy[] result = new Policy[2];
        for (int i = 0; i < 2; i++) {
            if (blauw == 0) {
                result[i] = Policy.FASCIST;
            } else {
                result[i] = Policy.LIBERAL;
                blauw--;
            }
        }
        return result;
    }

    private int insertIndex(Player player, List<Player> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() > player.getId()) {
                return i;
            }
        }
        return list.size();
    }

    private void createTopDeckWindow(Game game, Role role) {
        Stage stage = new Stage();
        TopDeckWindow topDeckWindow = new TopDeckWindow(game.getGovTable(), stage, role);
        Scene scene = new Scene(topDeckWindow);
        scene.getStylesheets().add("sh/shinterface/configscreen.css");
        topDeckWindow.getStyleClass().addAll(role.getStyle(), "top-deck-window");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}