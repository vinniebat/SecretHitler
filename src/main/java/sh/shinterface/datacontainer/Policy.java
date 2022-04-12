package sh.shinterface.datacontainer;

import javafx.scene.paint.Color;

import java.util.Map;

/**
 * A liberal or fascist policy
 */
public enum Policy {
    LIBERAL('B', Color.web("#004E6E")), FASCIST('R', Color.web("#990200"));

    /**
     * Maps the corresponding policy letter to the given policy (B -> liberal, R -> fascist)
     */
    private static final Map<Character, Policy> LETTER_TO_POLICY = Map.of(
            'B', LIBERAL,
            'R', FASCIST
    );

    /**
     * Letter representing the policy
     */
    private final char letter;

    private final Color color;

    Policy(char letter, Color color) {
        this.letter = letter;
        this.color = color;
    }

    /**
     * Returns the policy corresponding to the given letter, or null if no Policy matches
     *
     * @param letter "B" for liberal or "R" for fascist
     * @return Corresponding Policy
     */
    public static Policy fromChar(char letter) {
        return LETTER_TO_POLICY.get(letter);
    }

    /**
     * Returns the single letter representation of this Policy
     *
     * @return "B" for liberal, "R" for fascist
     */
    public String toString() {
        return "" + letter;
    }

    public Color getColor() {
        return color;
    }
}
