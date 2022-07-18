package sh.shinterface.view.tab;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sh.shinterface.model.GovModel;
import sh.shinterface.playable.Deck;
import sh.shinterface.playable.gov.Gov;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AssumptionView extends VBox {

    private final double chance;
    private final Label relChanceLabel;
    private final GovModel govModel;
    private final DecimalFormat df = new DecimalFormat("#.##");
    private final Gov currentGov;

    public AssumptionView(GovModel govModel, int numberOfLibs, Deck deck, boolean assumption, double assumedChance) {
        this.govModel = govModel;
        currentGov = govModel.getTable().getSelectionModel().getSelectedItem();
        HBox policies = new HBox();
        for (int i = 0; i < 3 - numberOfLibs; i++) {
            ImageView imageView = new ImageView(new Image("sh/shinterface/images/draw_pile/policies/fascist.png"));
            policies.getChildren().add(imageView);
            imageView.setFitWidth(70);
            imageView.setPreserveRatio(true);
        }
        for (int i = 0; i < numberOfLibs; i++) {
            ImageView imageView = new ImageView(new Image("sh/shinterface/images/draw_pile/policies/liberal.png"));
            policies.getChildren().add(imageView);
            imageView.setFitWidth(70);
            imageView.setPreserveRatio(true);
        }

        df.setRoundingMode(RoundingMode.HALF_UP);
        chance = deck.predictChance(numberOfLibs);
        String absLabelText = "Chance: " + df.format(chance * 100) + "%";
        if (chance != 0 && chance != 1) {
            absLabelText += " \u2248 1/" + Math.round(1 / chance);
        }
        Label absChanceLabel = new Label(absLabelText);

        relChanceLabel = new Label();
        setRelativeChance(assumedChance);

        Button assumptionButton = new Button("Set assumption");
        assumptionButton.setOnAction(e -> {
            ((AssumptionPane) this.getParent()).updateRelChance(numberOfLibs);
            currentGov.setAssumption(numberOfLibs);
            govModel.getTable().refresh();
            assumptionButton.setDisable(true);
        });

        if (assumption) {
            assumptionButton.setDisable(true);
        }

        this.getChildren().addAll(policies, absChanceLabel, relChanceLabel, assumptionButton);
    }

    public void setRelativeChance(double assumedChance) {
        String relChance = df.format(chance / assumedChance);
        if (assumedChance == 0 && chance == 0) {
            relChance = "1";
        }

        relChanceLabel.setText("Relative Chance: " + relChance);
    }

    public void setRelativeChance(int libsAssumption) {
        Deck deck = govModel.getPreviousDeck();
        double assumedChance = deck.predictChance(libsAssumption);
        setRelativeChance(assumedChance);
    }
}
