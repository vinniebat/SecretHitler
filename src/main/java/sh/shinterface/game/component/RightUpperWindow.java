package sh.shinterface.game.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import sh.shinterface.game.Game;
import sh.shinterface.datacontainer.Player;

public class RightUpperWindow extends HBox {

    private int fascists;
    private Game game;
    private int hitler;
    boolean rollback = false;

    public RightUpperWindow(Game game) {
        fascists = 0;
        hitler = 0;

        this.game = game;
        //contains playerOverview and deck info
        //TODO deck info
        HBox hBox = new HBox();

        HBox playerOverview = new HBox();
        for (Player player :
                game.getPlayers()) {
            playerOverview.getChildren().add(new PlayerOverviewComponent(player, game, this));
        }

        this.getChildren().add(playerOverview);
    }


    public boolean updateFasc(int fasc, int hitler, ImageView imageView, Image image) {
        if (rollback) {
            rollback = false;
            return true;
        }
        if ((game.getPlayers().size() + 1) / 2 > fascists + fasc && this.hitler + hitler < 2 && !rollback) {
            fascists += fasc;
            this.hitler += hitler;
            imageView.setImage(image);
            return true;
        }
        rollback = true;
        return false;
    }
}
