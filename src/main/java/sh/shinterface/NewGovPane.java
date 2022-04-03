package sh.shinterface;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NewGovPane extends VBox {

    private final ChoiceBox<Player> presidentChoiceBox;
    private final ChoiceBox<Player> chancellorChoiceBox;
    private final TextField claim1;
    private final TextField claim2;
    private final CheckBox conf;
    private final List<ToggleButton> voteList = new ArrayList<>();
    private final GridPane govPlayers;

    private static final Map<String, String> SWITCH = new HashMap<>() {{
        put("JA", "NEIN");
        put("NEIN", "JA");
    }};

    private static final List<String> STRINGPOLICIES = Arrays.asList("R", "B");

    public NewGovPane(Game game) {

        govPlayers = new GridPane();

        Label presLabel = new Label("President: ");
        Label chancLabel = new Label("Chancellor: ");

        PlayerStringConverter playerStringConverter = new PlayerStringConverter(game);

        presidentChoiceBox = new ChoiceBox<>();
        chancellorChoiceBox = new ChoiceBox<>();
        presidentChoiceBox.setOnAction(e -> choiceBoxAction(presidentChoiceBox, 0));
        chancellorChoiceBox.setOnAction(e -> choiceBoxAction(chancellorChoiceBox, 1));
        presidentChoiceBox.setConverter(playerStringConverter);
        chancellorChoiceBox.setConverter(playerStringConverter);
        List<Player> players = game.getPlayers();
        presidentChoiceBox.getItems().setAll(players);
        chancellorChoiceBox.getItems().setAll(players);

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
        govPlayers.addRow(2, new Label("Conflict?"), conf);

        GridPane votes = new GridPane();
        for (int i = 0; i < players.size(); i++) {
            Label voteName = new Label(playerStringConverter.toString(players.get(i)));
            ToggleButton jaNein = new ToggleButton("JA");
            jaNein.setOnAction(e -> switchVote(jaNein));
            votes.addRow(i, voteName, jaNein);
            voteList.add(jaNein);
        }

        Button createGov = new Button("Create gov");
        createGov.setOnAction(e -> createGov(game));

        this.getChildren().addAll(new Label("Add new gov:"), govPlayers, new Label("Votes"), votes, createGov);
    }

    private void switchVote(ToggleButton button) {
        button.setText(SWITCH.get(button.getText()));
    }

    private void createGov(Game game) {
        Player president = presidentChoiceBox.getSelectionModel().getSelectedItem();
        Player chancellor = chancellorChoiceBox.getSelectionModel().getSelectedItem();
        int[] claim1 = PolicyConverter.fromString(this.claim1.getText());
        int[] claim2 = PolicyConverter.fromString(this.claim2.getText());
        int played = 0;
        List<Boolean> voteList = this.voteList.stream().map(toggle -> !toggle.isSelected()).toList();

        boolean valid = !(choiceBoxCheck(presidentChoiceBox) || choiceBoxCheck(chancellorChoiceBox));

        if (claim1.length<3) {
            valid=false;
            if (!this.claim1.getStyleClass().contains("textFieldError")) {
                this.claim1.getStyleClass().add("textFieldError");
            }
        } else {
            this.claim1.getStyleClass().remove("textFieldError");
            if (claim2.length<2) {
                claim2 = autoGenerate(claim1);
            }
            if (Arrays.stream(claim2).anyMatch(i -> i == 1)) {
                played = 1;
            } else {
                played = 2;
            }
        }
        boolean conf = checkConf(claim1, claim2, this.conf.isSelected());
        if (valid) {
            resetPane();
            game.getGovTable().getItems().add(new Gov(president, chancellor, played, claim1, claim2, conf, voteList));
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

    private void choiceBoxAction(ChoiceBox<Player> box, int rij) {
        TextField textField = (TextField) govPlayers.getChildren().get(3 * rij + 2);
        String startText = Stream.of(textField.getPromptText().split(" ")).limit(2).collect(Collectors.joining(" "));
        textField.setPromptText(startText + " " + box.getSelectionModel().getSelectedItem().getName());
    }

    private void checkBoxAction(CheckBox box) {
        if (box.isSelected()) {
            box.setText("Conflict!");
        } else {
            box.setText("No conflict");
        }
    }

    public boolean checkConf(int[] claim1, int[] claim2, boolean conf) {
        boolean lib1 = IntStream.of(claim1).anyMatch(x -> x == 1);
        boolean lib2 = IntStream.of(claim2).anyMatch(x -> x == 1);
        return (lib1 ^ lib2) || conf;
    }

    private boolean choiceBoxCheck(ChoiceBox<Player> choiceBox) {
        if (choiceBox.getSelectionModel().getSelectedItem() == null) {
            if (!choiceBox.getStyleClass().contains("choiceBoxError")) {
                choiceBox.getStyleClass().add("choiceBoxError");
            }
            return true;
        } else {
            choiceBox.getStyleClass().remove("choiceBoxError");
            return false;
        }
    }

    private void textFieldRestrict(TextField textField, int numberOfClaims) {
        textField.textProperty().addListener((observableValue, oldString, newString) -> {
            if (newString.length() > numberOfClaims) {
                textField.setText(oldString);
            } else if (!newString.equals("") && Arrays.stream(newString.split("")).filter(string -> !STRINGPOLICIES.contains(string.toUpperCase())).toList().size() != 0) {
                textField.setText(oldString);
            }
        });
    }

    private int[] autoGenerate(int[] claim1) {
        int blauw = (int) Arrays.stream(claim1).filter(policy -> policy==1).count();
        int[] result= new int[2];
        for (int i = 0; i < 2; i++) {
            if (blauw==0) {
                result[i] = 2;
            } else {
                result[i] = 1;
                blauw--;
            }
        }
        return result;
    }
}