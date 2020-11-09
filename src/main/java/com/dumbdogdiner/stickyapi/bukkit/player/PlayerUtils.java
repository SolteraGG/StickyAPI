package com.dumbdogdiner.stickyapi.bukkit.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

import static com.dumbdogdiner.stickyapi.common.util.NumberUtil.intHelper;

@SuppressWarnings("unused")
public class PlayerUtils {
    public static class Names {
        public static List<String> getOnlinePlayers() {
            ArrayList<String> playerNames = new ArrayList<>();

            Bukkit.getOnlinePlayers().forEach(player -> {
                playerNames.remove(player.getName());
            });

            return playerNames;
        }

        public static List<String> getOfflinePlayers() {
            List<String> playerNames = getAllPlayers();

            Bukkit.getOnlinePlayers().forEach(player -> {
                playerNames.remove(player.getName());
            });
            return playerNames;
        }

        public static List<String> getAllPlayers() {
            ArrayList<String> allPlayerNames = new ArrayList<>();
            OfflinePlayer [] offlinePlayers = Bukkit.getServer().getOfflinePlayers();
            Arrays.sort(offlinePlayers, new OfflinePlayerSeenComparator());

            for (OfflinePlayer player : offlinePlayers) {
                allPlayerNames.add(player.getName());
            }
            return allPlayerNames;
        }
    }

    public static class OfflinePlayerSeenComparator implements Comparator<OfflinePlayer> {

        /**
         * Bigger number means seen more recently, which means we want it earlier in the list, so return -1
         * if o1 has a bigger number, we should return a negative number
         * if o2 has a bigger number, we should return a positive number
         * Check the following, in order, break on non-tie:
         * lastSeen, lastLogin, firstLogin, compare names as strings
         * if still a tie just return 0
         * @param o1 The first {@link org.bukkit.OfflinePlayer}
         * @param o2 The second {@link org.bukkit.OfflinePlayer}
         * @return An integer representing if o1 is less than, equal to, or greater than o2
         */
        @Override
        public int compare(OfflinePlayer o1, OfflinePlayer o2) {


            long [] diffs = new long[] {
                    o2.getLastSeen() - o1.getLastSeen(),
                    o2.getLastLogin() - o1.getLastLogin(),
                    o2.getFirstPlayed() - o1.getFirstPlayed()
            };

            for(long diff : diffs) {
                if (diff != 0) {
                    return intHelper(diff);
                }
            }
            return Objects.requireNonNull(o1.getName()).compareTo(Objects.requireNonNull(o2.getName()));
        }


    }
}
