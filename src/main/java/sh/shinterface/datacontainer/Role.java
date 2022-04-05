package sh.shinterface.datacontainer;

/**
 * Available roles in the game
 */
public enum Role {
    NONE("", "liberal"), UNKNOWN("Unknown", "liberal"), LIBERAL("Liberal", "liberal"), FASCIST("Fascist", "fascist"), HITLER("Hitler", "fascist");

    /**
     * String representation
     */
    private final String str;

    /**
     * css style class name
     */
    private final String style;

    /**
     * @param str   String representation of the role
     * @param style css style class name
     */
    Role(String str, String style) {
        this.str = str;
        this.style = style;
    }

    @Override
    public String toString() {
        return str;
    }

    /**
     * Get corresponding style class
     *
     * @return a css style class name
     */
    public String getStyle() {
        return style;
    }
}
