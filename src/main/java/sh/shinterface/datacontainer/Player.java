package sh.shinterface.datacontainer;

public class Player {

    private final int id;
    private final String name;
    private final Role role;

    public Player(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        if (id < 0) {
            return name;
        }
        return id + ". " + name;
    }
}
