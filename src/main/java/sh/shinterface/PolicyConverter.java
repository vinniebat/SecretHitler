package sh.shinterface;

import java.util.Arrays;
import java.util.Map;

public class PolicyConverter {
    private static final Map<Integer, String> TOSTRING = Map.of(1, "B", 2, "R");

    private static final Map<String, Integer> FROMSTRING = Map.of("B", 1, "R", 2);

    public static String toString(int[] policies) {
        StringBuilder result = new StringBuilder();
        for (int policy :
                policies) {
            result.append(TOSTRING.get(policy));
        }
        return result.toString();
    }

    public static int[] fromString(String policies) {
        if (policies.equals("")) {
            return new int[0];
        }
        return Arrays.stream(policies.toUpperCase().split("")).mapToInt(FROMSTRING::get).toArray();
    }
}
