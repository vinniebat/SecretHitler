package sh.shinterface.util;

import sh.shinterface.datacontainer.Vote;

public class BooleanVoteConverter {

    public static boolean toBool(Vote vote) {
        return vote.equals(Vote.JA);
    }

    public static Vote fromBool(boolean vote) {
        if (vote) {
            return Vote.JA;
        }
        return Vote.NEIN;
    }
}
