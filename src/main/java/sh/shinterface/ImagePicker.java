package sh.shinterface;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImagePicker {

    private static final Image UNKNOWN = new Image("sh/shinterface/secretrole.png");
    private static final Image HITLER = new Image("sh/shinterface/hitler0.png");

    private static final List<Image> FASC = Stream.of(
            new Image("sh/shinterface/fascist0.png"),
            new Image("sh/shinterface/fascist1.png"),
            new Image("sh/shinterface/fascist2.png")
    ).collect(Collectors.toList());

    private static final List<Image> LIB = Stream.of(
            new Image("sh/shinterface/liberal0.png"),
            new Image("sh/shinterface/liberal1.png"),
            new Image("sh/shinterface/liberal2.png"),
            new Image("sh/shinterface/liberal3.png"),
            new Image("sh/shinterface/liberal4.png"),
            new Image("sh/shinterface/liberal5.png")
    ).collect(Collectors.toList());

    public static Image pick(Role role) {
        if (role.equals(Role.UNKNOWN)) {
            return UNKNOWN;
        }
        if (role.equals(Role.HITLER)){
            return HITLER;
        }
        Random RG = new Random();
        if (role.equals(Role.LIBERAL)) {
            return LIB.get(RG.nextInt(LIB.size()));
        }
        return FASC.get(RG.nextInt(FASC.size()));
    }
}
