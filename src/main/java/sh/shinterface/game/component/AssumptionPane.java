package sh.shinterface.game.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import sh.shinterface.datacontainer.Deck;
import sh.shinterface.datacontainer.GovModel;
import sh.shinterface.datacontainer.Policy;

import static java.lang.Math.toIntExact;

public class AssumptionPane extends GridPane implements InvalidationListener {

    private final GovModel govModel;
    private final Tab tab;

    public AssumptionPane(GovModel govModel, Tab tab) {
        this.govModel = govModel;
        this.tab = tab;
    }

    @Override
    public void invalidated(Observable observable) {
        tab.setDisable(false);
        Deck previousDeck = govModel.getPreviousDeck();
        int assumption = toIntExact(govModel.getAssumption().stream().filter(policy -> policy.equals(Policy.LIBERAL)).count());
        double assumedChance = previousDeck.predictChance(assumption);
        this.getChildren().clear();
        for (int i = 0; i <= 3; i++) {
            this.add(new AssumptionView(i, previousDeck, i == assumption, assumedChance), i % 2, i / 2);
        }
    }
}
