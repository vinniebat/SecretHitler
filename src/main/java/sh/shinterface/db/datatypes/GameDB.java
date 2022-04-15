package sh.shinterface.db.datatypes;

import java.util.List;

public record GameDB(List<PlayerDB> players, List<GovDB> govs, List<VoteDB> votes) {
}
