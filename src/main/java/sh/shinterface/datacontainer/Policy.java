package sh.shinterface.datacontainer;

import java.util.Map;

public enum Policy {
    LIBERAL('B'), FASCIST('R');

    private static final Map<Character, Policy> LETTER_TO_POLICY = Map.of(
            'B', LIBERAL,
            'R', FASCIST
    );

    private final char letter;

    Policy(char letter) {
        this.letter = letter;
    }

    public String toString() {
        return "" + letter;
    }

    public static Policy fromChar(char letter) {
        return LETTER_TO_POLICY.get(letter);
    }
}
