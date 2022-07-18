package sh.shinterface;

import javafx.scene.control.Alert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

    private static final Properties SETTINGS = new Properties();

    private static boolean styleSwitch = true;

    public static void load() throws IOException {
        try (InputStream stream = Settings.class.getResourceAsStream("settings.properties")) {
            SETTINGS.load(stream);
            styleSwitch = Boolean.parseBoolean(SETTINGS.getProperty("style-switch"));
        }
    }

    public static boolean styleSwitchEnabled() {
        return styleSwitch;
    }

    public static void setStyleSwitch(boolean styleSwitch) {
        Settings.styleSwitch = styleSwitch;
        SETTINGS.setProperty("style-switch", String.valueOf(styleSwitch));
    }

    public static void save() {
        try (FileOutputStream stream = new FileOutputStream("src/main/resources/sh/shinterface/settings.properties")) {
            SETTINGS.store(stream, "User Settings");
        } catch (IOException | NullPointerException ex) {
            new Alert(Alert.AlertType.WARNING, "Error saving settings.").showAndWait();
        }
    }
}
