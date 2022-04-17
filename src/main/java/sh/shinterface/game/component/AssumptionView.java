package sh.shinterface.game.component;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sh.shinterface.datacontainer.Deck;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AssumptionView extends VBox {

    public AssumptionView(int numberOfLibs, Deck deck, boolean assumption, double assumedChance) {
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

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        double chance = deck.predictChance(numberOfLibs);
        String labelText = "Chance: " + df.format(chance * 100) + "%";
        if (chance != 0 && chance != 1) {
            labelText += " \u2248 1/" + Math.round(1 / chance);
        }
        Label absoluteChance = new Label(labelText);

        this.getChildren().addAll(policies, absoluteChance);
    }
}
