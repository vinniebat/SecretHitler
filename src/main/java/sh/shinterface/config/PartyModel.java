package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PartyModel implements Observable {

    private final List<Player> party = new ArrayList<>();

    private Player activePlayer = null;

    private final List<InvalidationListener> listeners = new ArrayList<>();

    private boolean invalid = false;

    public void setPartySize(int size) {
        if (size < party.size()) {
            party.retainAll(party.subList(0, size));
        } else {
            while (party.size() < size) {
                party.add(new Player(party.size() + 1,"Player " + (char) ('A' + party.size()), Role.UNKNOWN));
            }
        }
        invalidate();
    }

    public List<Player> getParty() {
        return party;
    }

    public int getPartySize() {
        return party.size();
    }

    public List<Player> getUnknownPlayers() {
        return party.stream().filter(Player::isUnknown).toList();
    }

    public List<Player> getFascists() {
        List<Player> fascists = new ArrayList<>();
        Optional<Player> hitler = party.stream().filter(p -> p.getRole() == Role.HITLER).findFirst();
        fascists.add(hitler.orElse(null));
        fascists.addAll(party.stream().filter(p -> p.getRole() == Role.FASCIST).toList());
        while (fascists.size() < (party.size()-1)/2) {
            fascists.add(null);
        }
        return fascists;
    }

    public void setPlayerRole(Player player, Role role) {
        if (player != null) {
            if (role == Role.HITLER) {
                Optional<Player> hitler = party.stream().filter(p -> p.getRole() == Role.HITLER).findFirst();
                hitler.ifPresent(value -> value.setRole(Role.FASCIST));
            }
            player.setRole(role);
            invalidate();
        }
    }

    public Optional<Player> getActivePlayer() {
        return Optional.ofNullable(activePlayer);
    }

    public void setActivePlayer(Player activePlayer) {
        Role role = Role.UNKNOWN;
        if (this.activePlayer != null) {
            role = this.activePlayer.getRole();
            this.activePlayer.setRole(Role.UNKNOWN);
        }
        if (activePlayer != null) {
            activePlayer.setRole(role);
        }
        this.activePlayer = activePlayer;
        invalidate();
    }

    public int getFascistCount() {
        return (int) party.stream().filter(p -> p.getRole().isFascist()).count();
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        listeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        listeners.remove(invalidationListener);
    }

    public void invalidate() {
        if (!invalid) {
            invalid = true;
            int i = 0;
            while (i < listeners.size()) {
                listeners.get(i).invalidated(this);
                i++;
            }
            invalid = false;
        }
    }

    public List<Player> getFinalParty() {
        if (party.stream().filter(p -> p.getRole().isFascist()).count() == (party.size()-1)/2) {
            party.stream().filter(Player::isUnknown).forEach(p -> p.setRole(Role.LIBERAL));
        }
        return party;
    }

    public void swapRoles(Player player1, Player player2) {
        Role role1 = (player1 != null) ? player1.getRole() : Role.UNKNOWN;
        Role role2 = (player2 != null) ? player2.getRole() : Role.UNKNOWN;
        if (player1 != null) {
            player1.setRole(role2);
        }
        if (player2 != null) {
            player2.setRole(role1);
        }
        invalidate();
    }
}
