package sh.shinterface;

public class Player {

    private final int id;
    private final String name;
    //0 = ?, 1 = lib, 2 = fasc
    private Role suspectedFaction;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        suspectedFaction = Role.UNKNOWN;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getSuspectedFaction() {
        return suspectedFaction;
    }

    public void setSuspectedFaction(Role suspectedFaction) {
        this.suspectedFaction = suspectedFaction;
    }
}
