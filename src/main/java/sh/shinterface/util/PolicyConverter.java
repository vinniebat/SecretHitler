package sh.shinterface.util;

import sh.shinterface.datacontainer.Policy;

import java.util.ArrayList;
import java.util.List;

public class PolicyConverter {
    public static String toString(Policy[] policies) {
        StringBuilder result = new StringBuilder();
        for (Policy policy : policies) {
            result.append(policy.toString());
        }
        return result.toString();
    }

    public static Policy[] fromString(String policies) {
        List<Policy> policyList = new ArrayList<>();
        for (int c : policies.chars().filter(c -> !Character.isSpaceChar(c)).toArray()) {
            policyList.add(Policy.fromChar((char) c));
        }
        return policyList.toArray(new Policy[0]);
    }
}
