package sh.shinterface.datacontainer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.TableView;
import sh.shinterface.game.Game;
import sh.shinterface.game.component.GovSpecifics;

public class GovModel implements InvalidationListener {

    private final GovSpecifics govSpecifics;
    private final Game game;

    private int lib;
    private int fasc;
    private int libPlayed;
    private int fascPlayed;


    public GovModel(Game game, GovSpecifics govSpecifics) {
        this.govSpecifics = govSpecifics;
        this.game = game;
        lib = 0;
        fasc = 0;
    }

    @Override
    public void invalidated(Observable observable) {
        TableView<Gov> tableView = game.getGovTable();

        Gov gov = tableView.getSelectionModel().getSelectedItem();
        int govIndex = tableView.getItems().indexOf(gov);

        lib = 0;
        fasc = 0;
        libPlayed = 0;
        fascPlayed = 0;

        for (int i = 0; i <= govIndex; i++) {
            Gov govi = tableView.getItems().get(i);
            for (Policy card : govi.getCards()) {
                lib += card.equals(Policy.LIBERAL) ? 1 : 0;
                fasc += card.equals(Policy.FASCIST) ? 1 : 0;
//                if (card.equals(Policy.LIBERAL)) {
//                    lib++;
//                } else {
//                    fasc++;
//                }
            }
            libPlayed += govi.getPlayed().equals(Policy.LIBERAL) ? 1 : 0;
            fascPlayed += govi.getPlayed().equals(Policy.FASCIST) ? 1 : 0;
        }

        updateBoards();
    }

    public Game getGame() {
        return game;
    }

    private void updateBoards() {
        govSpecifics.getBoardTab().updateBoards(libPlayed, fascPlayed);
    }
}
