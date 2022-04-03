package sh.shinterface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PolicyConverter {
    private static final Map<Integer, String> TOSTRING = new HashMap<>() {{
        put(1, "B");
        put(2, "R");
    }};

    private static final Map<String, Integer> FROMSTRING = new HashMap<>() {{
        put("B", 1);
        put("R", 2);
    }};

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