package sh.shinterface.util;

import sh.shinterface.datacontainer.Policy;

import java.util.List;

public class PolicyConverter {
    public static String toString(List<Policy> policies) {
        StringBuilder result = new StringBuilder();
        for (Policy policy : policies) {
            result.append(policy.toString());
        }
        return result.toString();
    }

    public static List<Policy> fromString(String policies) {
        return policies.chars().filter(c -> (c == 'B') || (c == 'R')).mapToObj(c -> Policy.fromChar((char) c)).toList();
    }
}
