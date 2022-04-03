package sh.shinterface;

public enum Role {
    NONE("", "liberal"), LIBERAL("Liberal", "liberal"), FASCIST("Fascist", "fascist"), HITLER("Hitler", "fascist");

    /**
     * String representation
     */
    private final String str;

    /**
     * style class
     */
    private final String style;

    Role(String str, String style) {
        this.str = str;
        this.style = style;
    }

    @Override
    public String toString() {
        return str;
    }

    public String getStyle() {
        return style;
    }
}
