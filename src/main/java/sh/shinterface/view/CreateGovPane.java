package sh.shinterface.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import sh.shinterface.playable.Player;
import sh.shinterface.playable.Policy;
import sh.shinterface.playable.gov.PlayerGov;
import sh.shinterface.playable.gov.Vote;
import sh.shinterface.screen.Game;
import sh.shinterface.screen.GameWindow;
import sh.shinterface.util.PolicyConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreateGovPane extends VBox {

    private static final List<Character> STRINGPOLICIES = List.of('R', 'B');
    private static final List<Character> LOWERSTRINGPOLICIES = List.of('r', 'b');

    @FXML
    private ChoiceBox<Player> presidentChoiceBox;
    @FXML
    private ChoiceBox<Player> chancellorChoiceBox;
    @FXML
    private TextField claim1;
    @FXML
    private TextField claim2;
    @FXML
    private CheckBox conf;

    @FXML
    private GridPane govPlayers;

    private Game game;
    @FXML
    private GridPane votes;
    @FXML
    private ToggleButton topDeckButton;

    private GameWindow gameWindow;
    private final List<ToggleButton> voteList = new ArrayList<>();

    public CreateGovPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("createGovPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Could not load createGovPane.fxml");
            Platform.exit();
        }
    }

    public void setGame(Game game) {
        this.game = game;
        List<Player> players = game.getPlayers();
        presidentChoiceBox.getItems().setAll(players);
        chancellorChoiceBox.getItems().setAll(players);
        for (int i = 0; i < players.size(); i++) {
            Label voteName = new Label(players.get(i).toString());
            ToggleButton jaNein = new ToggleButton();
            jaNein.getStyleClass().add("voteButton");
            votes.addRow(i % 5, voteName, jaNein);
            voteList.add(jaNein);
        }
    }

    public Game getGame() {
        return game;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        gameWindow.getTopDeckWindow().visibleProperty().bind(topDeckButton.selectedProperty());
        gameWindow.getGovTable().visibleProperty().bind(topDeckButton.selectedProperty().not());
    }

    public void initialize() {
        claim1.promptTextProperty().bind(
                Bindings.createStringBinding(
                        () -> "Claim of " + ((presidentChoiceBox.getValue() != null) ? presidentChoiceBox.getValue().getName() : "president"),
                        presidentChoiceBox.valueProperty()
                ));
        claim2.promptTextProperty().bind(
                Bindings.createStringBinding(
                        () -> "Claim of " + ((chancellorChoiceBox.getValue() != null) ? chancellorChoiceBox.getValue().getName() : "chancellor"),
                        chancellorChoiceBox.valueProperty()
                ));
        addChoiceBoxFilter(presidentChoiceBox, chancellorChoiceBox);
        addChoiceBoxFilter(chancellorChoiceBox, presidentChoiceBox);
        textFieldRestrict(claim1, 3);
        textFieldRestrict(claim2, 2);
        conf.textProperty().bind(
                Bindings.createStringBinding(
                        () -> (conf.isSelected()) ? "Conflict!" : "No conflict",
                        conf.selectedProperty()
                )
        );
        topDeckButton.textProperty().bind(
                Bindings.createStringBinding(
                        () -> (topDeckButton.isSelected()) ? "Cancel" : "Top deck",
                        topDeckButton.selectedProperty()
                )
        );
    }

    private void addChoiceBoxFilter(ChoiceBox<Player> first, ChoiceBox<Player> other) {
        first.valueProperty().addListener((observableValue, oldPlayer, newPlayer) -> {
            ObservableList<Player> items = other.getItems();
            items.remove(newPlayer);
            if (oldPlayer != null) {
                items.add(oldPlayer);
            }
            items.sort(Comparator.comparing(Player::getId));
        });
    }

    public void enableTopDeckButton() {
        topDeckButton.setSelected(false);
    }

    @FXML
    private void createGov(ActionEvent event) {
        Player president = presidentChoiceBox.getValue();
        Player chancellor = chancellorChoiceBox.getValue();
        List<Policy> claim1 = PolicyConverter.fromString(this.claim1.getText());
        List<Policy> claim2 = PolicyConverter.fromString(this.claim2.getText());

        if (!(choiceBoxCheck(presidentChoiceBox) || choiceBoxCheck(chancellorChoiceBox))) {
            this.claim1.getStyleClass().removeAll("createGovPaneTextFieldError");
            if (claim1.size() == 3) {
                if (claim2.size() < 2) {
                    claim2 = autoGenerate(claim1);
                }
                Policy played;
                if (claim2.stream().anyMatch(i -> i == Policy.LIBERAL)) {
                    played = Policy.LIBERAL;
                } else {
                    played = Policy.FASCIST;
                }

                for (ToggleButton voteButton : voteList) {
                    voteButton.getStyleClass().removeAll("createGovPaneBoxError");
                }
                List<ToggleButton> voteButtons = voteList.stream().filter(ToggleButton::isSelected).toList();
                if (voteButtons.size() > voteList.size() / 2) {
                    voteButtons.forEach(b -> b.getStyleClass().add("createGovPaneBoxError"));
                } else {
                    List<Vote> votes = voteList.stream().map(b -> b.isSelected() ? Vote.NEIN : Vote.JA).toList();
                    boolean conf = checkConf(claim1, claim2, this.conf.isSelected());
                    game.getGovTable().getItems().add(new PlayerGov(president, chancellor, played, claim1, claim2, conf, votes));
                    if (!game.getGovTable().isVisible()) {
                        game.getGameWindow().toggleTopDeck();
                    }
                    game.getPlayers().forEach(p -> p.setTurnLocked(false));
                    if (game.getPlayers().size() > 5)
                        president.setTurnLocked(true);
                    chancellor.setTurnLocked(true);
                    resetPane();
                }
            } else {
                this.claim1.getStyleClass().add("createGovPaneTextFieldError");
            }
        }
    }

    private void resetPane() {
        presidentChoiceBox.setValue(null);
        chancellorChoiceBox.setValue(null);
        presidentChoiceBox.getItems().setAll(game.getAvailablePresidents());
        List<Player> chancellors = new ArrayList<>(game.getPlayers());
        chancellors.removeIf(Player::isTurnLocked);
        chancellorChoiceBox.getItems().setAll(chancellors);
        claim1.clear();
        claim2.clear();
        conf.setSelected(false);
        for (ToggleButton button : voteList) {
            button.setSelected(false);
        }
    }

    public boolean checkConf(List<Policy> claim1, List<Policy> claim2, boolean conf) {
        boolean lib1 = claim1.stream().anyMatch(p -> p == Policy.LIBERAL);
        boolean lib2 = claim2.stream().anyMatch(p -> p == Policy.LIBERAL);
        return (lib1 ^ lib2) || conf;
    }

    private boolean choiceBoxCheck(ChoiceBox<Player> choiceBox) {
        if (choiceBox.getSelectionModel().getSelectedItem() == null) {
            choiceBox.getStyleClass().add("createGovPaneBoxError");
            return true;
        } else {
            choiceBox.getStyleClass().removeAll("createGovPaneBoxError");
            return false;
        }
    }

    private void textFieldRestrict(TextField textField, int numberOfClaims) {
        textField.textProperty().addListener((observableValue, oldString, newString) -> {
            if (newString.length() > numberOfClaims) {
                textField.setText(oldString);
            } else if (!newString.isBlank() && newString.length() > oldString.length()) {
                Character newChar = newString.charAt(newString.length() - 1);
                if (LOWERSTRINGPOLICIES.contains(newChar)) {
                    textField.setText(oldString + newChar.toString().toUpperCase());
                } else if (!STRINGPOLICIES.contains(newChar)) {
                    textField.setText(oldString);
                }
            }
        });
    }

    private List<Policy> autoGenerate(List<Policy> claim1) {
        int blauw = (int) claim1.stream().filter(policy -> policy == Policy.LIBERAL).count();
        List<Policy> result = new ArrayList<>(2);
        for (int i = 0; i < blauw; i++) {
            result.add(Policy.LIBERAL);
        }
        for (int i = blauw; i < 2; i++) {
            result.add(Policy.FASCIST);
        }
        return result;
    }

}