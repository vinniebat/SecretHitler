package sh.shinterface;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NewGovPane extends VBox {

    private ChoiceBox<Player> presidentChoiceBox;
    private ChoiceBox<Player> chancellorChoiceBox;
    private TextField claim1;
    private TextField claim2;
    private CheckBox conf;
    private List<ToggleButton> voteList = new ArrayList<>();

    private static final Map<String, String> SWITCH = new HashMap<>() {{
        put("JA", "NEIN");
        put("NEIN", "JA");
    }};

    public NewGovPane(Game game) {

        GridPane govPlayers = new GridPane();

        Label presLabel = new Label("President: ");
        Label chancLabel = new Label("Chancellor: ");

        PlayerStringConverter playerStringConverter = new PlayerStringConverter(game);

        presidentChoiceBox = new ChoiceBox<>();
        chancellorChoiceBox = new ChoiceBox<>();
        presidentChoiceBox.setOnAction(e -> choiceBoxAction(presidentChoiceBox));
        chancellorChoiceBox.setOnAction(e -> choiceBoxAction(chancellorChoiceBox));
        presidentChoiceBox.setConverter(playerStringConverter);
        chancellorChoiceBox.setConverter(playerStringConverter);
        List<Player> players = game.getPlayers();
        presidentChoiceBox.getItems().setAll(players);
        chancellorChoiceBox.getItems().setAll(players);

        claim1 = new TextField();
        claim2 = new TextField();
        claim1.setPromptText("Claim of president");
        claim2.setPromptText("Claim of chancellor");

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
        boolean valid = true;
        Player president = presidentChoiceBox.getValue();
        Player chancellor = chancellorChoiceBox.getValue();
        int[] claim1 = null;
        int[] claim2 = null;
        int played = 0;
        boolean conf = this.conf.isSelected();
        //TODO fix error
        Boolean[] voteList = (Boolean[]) this.voteList.stream().map(toggle -> !toggle.isSelected()).toArray();
        //TODO check for empty claim2 and autofill
        try {
            claim1 = PolicyConverter.fromString(this.claim1.getText());
            claim2 = PolicyConverter.fromString(this.claim2.getText());
            if (claim1.length!=3 || claim2.length!=2 || checkConf(claim1, claim2)||president==null||chancellor==null){
                valid=false;
            } else {
                if (Arrays.stream(claim2).anyMatch(i->i==1)) {
                    played = 1;
                } else {
                    played = 2;
                }
            }
        } catch (NullPointerException e) {
            valid = false;
        }

        if (valid) {
            game.getGovTable().getItems().add(new Gov(president, chancellor, played, claim1, claim2, conf, voteList));
        }

    }

    private void choiceBoxAction(ChoiceBox<Player> box) {
        TextField textField = (TextField) box.getParent().getChildrenUnmodifiable().get(2);
        String startText = Stream.of(textField.getPromptText().split(" ")).limit(2).collect(Collectors.joining(" "));
        textField.setPromptText(startText + " " + box.getValue().getName());
    }

    private void checkBoxAction(CheckBox box) {
        if (box.isSelected()) {
            box.setText("Conflict!");
        } else {
            box.setText("No conflict");
        }
    }

    public boolean checkConf(int[] claim1, int[] claim2) {
        boolean lib1 = IntStream.of(claim1).anyMatch(x -> x == 1);
        boolean lib2 = IntStream.of(claim2).anyMatch(x->x==1);
        return lib1 ^ lib2;
    }
}
