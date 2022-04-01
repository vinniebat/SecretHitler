package sh.shinterface;

public class Player {

    private final int id;
    private final String name;
    //0 = ?, 1 = lib, 2 = fasc
    private int suspectedFaction = 0;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
