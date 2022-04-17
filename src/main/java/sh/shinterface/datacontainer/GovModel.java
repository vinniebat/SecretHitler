package sh.shinterface.datacontainer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import sh.shinterface.game.Game;
import sh.shinterface.game.component.GovView;

import java.util.ArrayList;
import java.util.List;

public class GovModel implements InvalidationListener, Observable {

    private final GovView govView;
    private final Game game;
    private final List<InvalidationListener> listeners;

    private int libPlayed;
    private int fascPlayed;
    private List<Policy> assumption;


    public GovModel(Game game, GovView govView) {
        listeners = new ArrayList<>();
        this.govView = govView;
        this.game = game;
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        listeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        listeners.remove(invalidationListener);
    }

    @Override
    public void invalidated(Observable observable) {
        TableView<Gov> tableView = game.getGovTable();

        Gov gov = tableView.getSelectionModel().getSelectedItem();
        assumption = gov.getAssumption();
        int govIndex = tableView.getItems().indexOf(gov);

        libPlayed = 0;
        fascPlayed = 0;

        for (int i = 0; i <= govIndex; i++) {
            Gov govi = tableView.getItems().get(i);
//            for (Policy card : govi.getCards()) {
//                lib += card.equals(Policy.LIBERAL) ? 1 : 0;
//                fasc += card.equals(Policy.FASCIST) ? 1 : 0;
//                if (card.equals(Policy.LIBERAL)) {
//                    lib++;
//                } else {
//                    fasc++;
//                }
//            }
            libPlayed += govi.getPlayed().equals(Policy.LIBERAL) ? 1 : 0;
            fascPlayed += govi.getPlayed().equals(Policy.FASCIST) ? 1 : 0;
        }

        fireInvalidationEvent();
    }

    public String getPlayerString() {
        int numberOfPlayers = game.getPlayers().size();
        if (numberOfPlayers / 2 == (numberOfPlayers + 1) / 2) {
            return (numberOfPlayers - 1) + "" + numberOfPlayers;
        } else {
            return numberOfPlayers + "" + (numberOfPlayers + 1);
        }
    }

    private void fireInvalidationEvent() {
        for (InvalidationListener invalidationListener : listeners) {
            invalidationListener.invalidated(this);
        }
    }

    public int getLibPlayed() {
        return libPlayed;
    }

    public int getFascPlayed() {
        return fascPlayed;
    }

    public Deck getPreviousDeck() {
        int deckLibPlayed = 0;
        int deckFascPlayed = 0;
        int lib = 0;
        int fasc = 0;
        ObservableList<Gov> govs = game.getGovTable().getItems();
        int govIndex = govs.indexOf(game.getGovTable().getSelectionModel().getSelectedItem());
        for (int i = 0; i < govIndex; i++) {
            Gov gov = govs.get(i);
            List<Policy> assumption = gov.getAssumption();
            Policy played = gov.getPlayed();
            lib += assumption.stream().filter(policy -> policy.equals(Policy.LIBERAL)).count();
            fasc += assumption.stream().filter(policy -> policy.equals(Policy.FASCIST)).count();
            if (played.equals(Policy.FASCIST)) {
                deckFascPlayed++;
            } else {
                deckLibPlayed++;
            }
            if (17 - lib - fasc < 3) {
                //new deck
                lib = 0;
                fasc = 0;
                deckLibPlayed = 0;
                deckFascPlayed = 0;
            }
        }
        Gov currentGov = game.getGovTable().getSelectionModel().getSelectedItem();
        int libPlayed = this.libPlayed - (currentGov.getPlayed().equals(Policy.LIBERAL) ? 1 : 0);
        int fascPlayed = this.fascPlayed - (currentGov.getPlayed().equals(Policy.FASCIST) ? 1 : 0);
        int allLib = 6 - libPlayed + deckLibPlayed;
        int allFasc = 11 - fascPlayed + deckFascPlayed;
        int restLib = allLib - lib;
        int restFasc = allFasc - fasc;
        return new Deck(allLib, allFasc, restLib, restFasc);
    }

    public List<Policy> getAssumption() {
        return assumption;
    }

    public Game getGame() {
        return game;
    }
}
