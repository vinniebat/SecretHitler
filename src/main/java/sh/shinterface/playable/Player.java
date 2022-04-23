package sh.shinterface.playable;

public class Player {

    private final int id;
    private String name;
    private Role role;

    public Player(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        if (id < 0) {
            return name;
        }
        return id + ". " + name;
    }

    public boolean isUnknown() {
        return role == Role.UNKNOWN || role == Role.NONE;
    }
}
