package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class PartyModel implements Observable {

    private final List<Player> party = new ArrayList<>();

    private Player activePlayer = null;

    private final List<InvalidationListener> listeners = new ArrayList<>();

    private final List<Player> fascists = new ArrayList<>();

    private boolean invalid = false;

    public void setPartySize(int size) {
        if (size == party.size())
            return;

        if (size < party.size()) {
            party.retainAll(party.subList(0, size));
            fascists.replaceAll(p -> (p != null && p.getId() > size) ? null : p);
            List<Player> subList = fascists.subList(1, fascists.size()).stream().sorted((p1, p2) -> {
                if (p1 == p2) return 0;
                if (p1 == null) return 1;
                if (p2 == null) return -1;
                return fascists.indexOf(p1) - fascists.indexOf(p2);
            }).toList().subList(maxFascistCount() - 1, fascists.size()-1);
            for (Player fascist : subList) {
                if (fascist != null)
                    fascist.setRole(Role.UNKNOWN);
            }
            subList.forEach(p -> fascists.remove(fascists.lastIndexOf(p)));
            if (fascists.get(0) != null)
                fascists.get(0).setRole(Role.HITLER);
            if (activePlayer != null && activePlayer.getId() > size) {
                activePlayer = null;
            }
        } else {
            while (party.size() < size) {
                party.add(new Player(party.size() + 1, "Player " + (char) ('A' + party.size()), Role.UNKNOWN));
            }
            while (fascists.size() < maxFascistCount()) {
                fascists.add(null);
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
        return fascists;
    }

    public void setPlayerRole(Player player, Role role) {
        if (player == null || player.getRole() == role)
            return;

        if (role == Role.HITLER) {
            if (fascists.get(0) != null) {
                swapRoles(fascists.get(0), player);
                return;
            } else {
                removeFascist(player);
                fascists.set(0, player);
            }
        } else if (role == Role.FASCIST) {
            if (player.getRole() == Role.HITLER) {
                if (getFascistCount() == maxFascistCount()) {
                    swapRoles(player, fascists.get(1));
                    return;
                }
                fascists.set(0, null);
            }
            List<Player> nonFasc = fascists.subList(1, fascists.size()).stream().filter(p -> p == null || !p.getRole().isFascist()).toList();
            if (!nonFasc.isEmpty()) {
                fascists.set(fascists.subList(1,fascists.size()).indexOf(nonFasc.get(0)) + 1, player);
            }
        } else {
            removeFascist(player);
        }
        player.setRole(role);
        invalidate();
    }

    private void removeFascist(Player player) {
        int i = fascists.indexOf(player);
        if (i != -1) {
            fascists.set(i, null);
        }
    }

    public Optional<Player> getActivePlayer() {
        return Optional.ofNullable(activePlayer);
    }

    public void setActivePlayer(Player activePlayer) {
        if (activePlayer == this.activePlayer)
            return;

        Player oldActive = this.activePlayer;
        this.activePlayer = activePlayer;
        swapRoles(oldActive, activePlayer);
        invalidate();
    }

    public int getFascistCount() {
        return (int) fascists.stream().filter(Objects::nonNull).count();
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
        updateFascists();
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

    private void updateFascists() {
        if (activePlayer == null || !activePlayer.getRole().isFascist()) {
            fascists.forEach(p -> {
                if (p != null)
                    p.setRole(Role.UNKNOWN);
            });
            fascists.replaceAll(p -> null);
        }
    }

    public List<Player> getFinalParty() {
        if (activePlayer != null) {
            if (activePlayer.getRole().isFascist()) {
                party.stream().filter(Player::isUnknown).forEach(p -> p.setRole(Role.LIBERAL));
            } else {
                activePlayer.setRole(Role.LIBERAL);
            }
        }
        return party;
    }

    public int maxFascistCount() {
        return (party.size()-1)/2;
    }

    public void swapRoles(Player oldPlayer, Player newPlayer) {
        Role role1 = (oldPlayer != null) ? oldPlayer.getRole() : Role.UNKNOWN;
        Role role2 = (newPlayer != null) ? newPlayer.getRole() : Role.UNKNOWN;

        if (role1 == role2)
            return;

        int ind1 = (oldPlayer != null) ? fascists.indexOf(oldPlayer) : -1;
        int ind2 = (newPlayer != null) ? fascists.indexOf(newPlayer) : -1;
        if (ind1 != -1) {
            fascists.set(ind1, newPlayer);
        }
        if (ind2 != -1) {
            fascists.set(ind2, oldPlayer);
        }
        if (oldPlayer != null) {
            oldPlayer.setRole(role2);
        }
        if (newPlayer != null) {
            newPlayer.setRole(role1);
        }
        invalidate();
    }
}
