package sh.shinterface.config;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import sh.shinterface.datacontainer.Player;
import sh.shinterface.datacontainer.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Model that represents the party. Keeps track of all players, all fascists and hitler.
 */
public class PartyModel implements Observable {

    /**
     * The party that this model represents
     */
    private final List<Player> party = new ArrayList<>();

    /**
     * List of selected fascists, some elements are null for empty places.
     * The first element (index 0) always represents hitler
     */
    private final List<Player> fascists = new ArrayList<>();
    /**
     * Objects listening to this model
     */
    private final List<InvalidationListener> listeners = new ArrayList<>();
    /**
     * The active player that is using the interface
     */
    private Player activePlayer = null;

    /**
     * Returns the size of the party
     *
     * @return Size of the party
     */
    public int getPartySize() {
        return party.size();
    }

    /**
     * Sets party size. Automatically adjust fascists count.
     *
     * @param size new party size
     */
    public void setPartySize(int size) {
        if (size == party.size()) // Nothing changes if the size stays the same, model does not become invalid
            return;

        if (size < party.size()) { // If the party needs to shrink
            int dF = maxFascistCount() - (size - 1) / 2; // Difference in fascist party size
            party.retainAll(party.subList(0, size));
            // Replace all removed players with null in fascist party
            fascists.replaceAll(p -> (p != null && p.getId() > size) ? null : p);
            if (dF > 0) { // Only change the fascist party if the size needs to change
                // Sort the fascists in order of active player, fascists and last null elements.
                List<Player> subList = fascists.subList(1, fascists.size()).stream().sorted((p1, p2) -> {
                    if (p1 == p2) return 0; // Needs to return 0 by definition
                    if (p1 == null || p2 == activePlayer) return 1;
                    if (p2 == null || p1 == activePlayer) return -1;
                    return fascists.indexOf(p1) - fascists.indexOf(p2);
                }).toList().subList(maxFascistCount() - 1, fascists.size() - 1);
                // The last few elements of the sorted list are the ones that will be removed from the fascist party.
                // So first null elements are removed, then players starting from last in the fascist party
                for (Player fascist : subList) {
                    if (fascist != null)
                        fascist.setRole(Role.UNKNOWN);
                    // Reset the roles of players that are removed
                    fascists.remove(fascists.lastIndexOf(fascist));
                    // We can't use removeAll() because there are multiple null elements
                    // Use last index to avoid deleting the hitler role
                }
            }
            if (!party.contains(activePlayer)) {
                activePlayer = null;
                // If the active player was removed from the party, set it to null
            }
        } else {
            while (party.size() < size) {
                party.add(new Player(party.size() + 1, "Player " + (char) ('A' + party.size()), Role.UNKNOWN));
                // Fill the party with new players
            }
            while (fascists.size() < maxFascistCount()) {
                fascists.add(null);
                // Pad the fascist party with null elements
            }
        }
        invalidate();
    }

    /**
     * Returns the party which this model represents
     *
     * @return Party of players
     */
    public List<Player> getParty() {
        return party;
    }

    /**
     * Returns the final party, which means that if no active player is selected, this just returns the players.
     * If the active player is liberal, only his role is selected.
     * If the player is fascist, all fascist roles are selected and the remaining roles are set to liberal
     *
     * @return The final party with all roles set accordingly
     */
    public List<Player> getFinalParty() {
        if (knowsFascist()) {
            party.forEach(p -> {
                if (p != null && !p.getRole().isFascist())
                    p.setRole(Role.LIBERAL);
            });
        }
        return party;
    }

    /**
     * Returs all player that don't have a role
     *
     * @return List of all players without a role
     */
    public List<Player> getUnknownPlayers() {
        return party.stream().filter(Player::isUnknown).toList();
    }

    /**
     * Return the active player that is selected
     *
     * @return Optional of the active player
     */
    public Optional<Player> getActivePlayer() {
        return Optional.ofNullable(activePlayer);
    }

    /**
     * Set the selected active player
     *
     * @param activePlayer New active player
     */
    public void setActivePlayer(Player activePlayer) {
        if (activePlayer == this.activePlayer)
            return;

        Player oldActive = this.activePlayer;
        this.activePlayer = activePlayer;
        swapRoles(oldActive, activePlayer); // Swap roles with the last selected active player
        invalidate();
    }

    /**
     * Sets the role of the active player
     *
     * @param role new role
     */
    public void setActiveRole(Role role) {
        if (activePlayer == null || activePlayer.getRole() == role)
            return;

        if (role.isFascist()) { // If the new role is fascist.
            if (activePlayer.getRole().isFascist()) {
                // If the player was already fascist it needs to change places
                assert fascists.contains(activePlayer);
                int i = fascists.indexOf(activePlayer);
                int j = (i == 0) ? (fascists.contains(null) ? fascists.indexOf(null) : 1) : 0;
                // Ternary operator ftw
                Player player = fascists.get(j);
                if (player != null)
                    player.setRole((i == 0) ? Role.HITLER : Role.FASCIST);
                fascists.set(i, player);
                fascists.set(j, activePlayer);
            } else {
                // else the fascists party is only null, so we can just add the player without trouble
                fascists.set((activePlayer.getRole() == Role.HITLER) ? 0 : 1, activePlayer);
            }
        } else if (role == Role.LIBERAL && activePlayer.getRole().isFascist()) {
            fascists.set(fascists.indexOf(activePlayer), null);
            // If the new role is liberal we remove the player from the fascists party
        }
        activePlayer.setRole(role);
        invalidate();
    }

    /**
     * Returns the maximum amount of fascist for the current party size
     *
     * @return Max size of the fascist party
     */
    public int maxFascistCount() {
        return (party.size() - 1) / 2;
    }

    /**
     * Clears the fascists party if the active player is not fascist, or no player is selected
     */
    private void updateFascists() {
        if (activePlayer == null || !activePlayer.getRole().isFascist()) {
            for (int i = 0; i < fascists.size(); i++) {
                Player fascist = fascists.get(i);
                if (fascist != null) {
                    fascist.setRole(Role.UNKNOWN);
                    fascists.set(i, null);
                }
            }
        } else if (activePlayer != null && activePlayer.getRole() == Role.HITLER && fascists.size() > 2) {
            for (int i = 0; i < fascists.size(); i++) {
                Player fascist = fascists.get(i);
                if (fascist != null && fascist.getRole() == Role.FASCIST) {
                    fascist.setRole(Role.UNKNOWN);
                    fascists.set(i, null);
                }
            }
        }
    }

    /**
     * Sets the spot at the given index as the given player
     *
     * @param fascist New fascist
     * @param index   index of the place where the fascist is inserted
     */
    public void setFascist(Player fascist, int index) {
        if (index < 0 || index >= fascists.size() || fascists.get(index) == fascist)
            return;
        Player player = fascists.get(index);
        fascists.set(index, fascist);
        if (fascist != null)
            fascist.setRole((index == 0) ? Role.HITLER : Role.FASCIST);
        if (player != null)
            player.setRole(Role.UNKNOWN);
        invalidate();
    }

    /**
     * Returns the fascist at the given index
     *
     * @param index index in the fascist party
     * @return The fascist in place index of the fascist party
     */
    public Player getFascist(int index) {
        return (index >= 0 && index < fascists.size()) ? fascists.get(index) : null;
    }

    /**
     * Check if the active player knows who the fascists are
     *
     * @return True if the active player should know who the fascists are
     */
    public boolean knowsFascist() {
        return activePlayer != null &&
                (activePlayer.getRole() == Role.FASCIST || (activePlayer.getRole() == Role.HITLER && fascists.size() == 2));
    }

    /**
     * Swaps the roles and places in the fascist party of two players
     *
     * @param player1 First player to be swapped
     * @param player2 Second player to be swapped
     */
    public void swapRoles(Player player1, Player player2) {
        Role role1 = (player1 != null) ? player1.getRole() : Role.UNKNOWN;
        Role role2 = (player2 != null) ? player2.getRole() : Role.UNKNOWN;

        if (role1 == role2)
            return;

        int ind1 = (player1 != null) ? fascists.indexOf(player1) : -1;
        int ind2 = (player2 != null) ? fascists.indexOf(player2) : -1;
        if (ind1 != -1) {
            fascists.set(ind1, player2);
        }
        if (ind2 != -1) {
            fascists.set(ind2, player1);
        }
        if (player1 != null) {
            player1.setRole(role2);
        }
        if (player2 != null) {
            player2.setRole(role1);
        }
        invalidate();
    }

    /**
     * Check if the party is valid
     * @return Returns true if the party is valid (active role chosen, fascist players chosen if fascists)
     */
    public boolean isValid() {
        boolean fascistsValid = fascists.stream().allMatch(Objects::nonNull); // Check if all fascists are set
        // The party is valid if
        return activePlayer == null || // there is no active player
                activePlayer.getRole() == Role.LIBERAL || // the active role is liberal
                (activePlayer.getRole().isFascist() && fascistsValid) || // the active role is fascist and all fascists are chosen
                (activePlayer.getRole() == Role.HITLER && fascists.size() > 2); // the active role is hitler and there are more than 2 fascists
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        listeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        listeners.remove(invalidationListener);
    }

    /**
     * Invalidates the model
     */
    public void invalidate() {
        updateFascists();
        int i = 0;
        while (i < listeners.size()) {
            listeners.get(i).invalidated(this);
            i++;
        }
    }
}
