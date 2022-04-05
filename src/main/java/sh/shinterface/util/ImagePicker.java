package sh.shinterface.util;

import javafx.scene.image.Image;
import sh.shinterface.datacontainer.Role;

import java.util.List;
import java.util.Random;

public class ImagePicker {

    private static final Image UNKNOWN = new Image("sh/shinterface/images/secretrole.png");
    private static final Image HITLER = new Image("sh/shinterface/images/hitler0.png");

    private static final List<Image> FASC = List.of(
            new Image("sh/shinterface/images/fascist0.png"),
            new Image("sh/shinterface/images/fascist1.png"),
            new Image("sh/shinterface/images/fascist2.png")
    );

    private static final List<Image> LIB = List.of(
            new Image("sh/shinterface/images/liberal0.png"),
            new Image("sh/shinterface/images/liberal1.png"),
            new Image("sh/shinterface/images/liberal2.png"),
            new Image("sh/shinterface/images/liberal3.png"),
            new Image("sh/shinterface/images/liberal4.png"),
            new Image("sh/shinterface/images/liberal5.png")
    );

    public static Image pick(Role role) {
        if (role.equals(Role.UNKNOWN)) {
            return UNKNOWN;
        }
        if (role.equals(Role.HITLER)) {
            return HITLER;
        }
        Random RG = new Random();
        if (role.equals(Role.LIBERAL)) {
            return LIB.get(RG.nextInt(LIB.size()));
        }
        return FASC.get(RG.nextInt(FASC.size()));
    }
}
