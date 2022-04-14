package sh.shinterface.util;

import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.List;

public class SpecialGovPlayers {

    public static final Player EMPTY = new Player(-1, "", Role.NONE);
    public static final Player TOPDECK = new Player(-2, "Topdeck", Role.NONE);

    //All specialGovPlayers in List
    public static final List<Player> SPECIALGOVPLAYERS = List.of(EMPTY, TOPDECK);


}
