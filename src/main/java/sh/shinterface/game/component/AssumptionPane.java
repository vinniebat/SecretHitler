package sh.shinterface.game.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
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
            AssumptionView assumptionView = new AssumptionView(govModel, i, previousDeck, i == assumption, assumedChance);
            this.add(assumptionView, i % 2, i / 2);
        }
    }

    public void updateRelChance(int libsAssumption) {
        for (Node node : this.getChildren()) {
            for (Node child : ((AssumptionView) node).getChildren()) {
                child.setDisable(false);
            }
            ((AssumptionView) node).setRelativeChance(libsAssumption);
        }
    }
}
