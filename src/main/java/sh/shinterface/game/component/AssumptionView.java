package sh.shinterface.game.component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Deck;
import sh.shinterface.datacontainer.GovModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AssumptionView extends VBox {

    private final double chance;
    private final Label relChanceLabel;
    private final GovModel govModel;
    private final DecimalFormat df = new DecimalFormat("#.##");

    public AssumptionView(GovModel govModel, int numberOfLibs, Deck deck, boolean assumption, double assumedChance) {
        this.govModel = govModel;
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
            assumptionButton.setDisable(true);
        });

        if (assumption) {
            assumptionButton.setDisable(true);
        }

        this.getChildren().addAll(policies, absChanceLabel, relChanceLabel, assumptionButton);
    }

    public void setRelativeChance(double assumedChance) {
        relChanceLabel.setText("Relative Chance: " + df.format(chance / assumedChance));
    }

    public void setRelativeChance(int libsAssumption) {
        Deck deck = govModel.getPreviousDeck();
        double assumedChance = deck.predictChance(libsAssumption);
        setRelativeChance(assumedChance);
    }
}
