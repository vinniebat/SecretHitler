package sh.shinterface.db.datatypes;

import java.util.ArrayList;

public record GameDB(ArrayList<PlayerDB> players, ArrayList<GovDB> govs, ArrayList<VoteDB> votes) {
}
