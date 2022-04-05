package sh.shinterface.datacontainer;

import java.util.Map;

/**
 * A liberal or fascist policy
 */
public enum Policy {
    LIBERAL('B'), FASCIST('R');

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

    Policy(char letter) {
        this.letter = letter;
    }

    /**
     * Returns the policy corresponding to the given letter, or null if no Policy matches
     * @param letter "B" for liberal or "R" for fascist
     * @return Corresponding Policy
     */
    public static Policy fromChar(char letter) {
        return LETTER_TO_POLICY.get(letter);
    }

    /**
     * Returns the single letter representation of this Policy
     * @return "B" for liberal, "R" for fascist
     */
    public String toString() {
        return "" + letter;
    }
}
