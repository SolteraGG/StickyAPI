package com.dumbdogdiner.stickyapi.bukkit.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

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
         * lastSeen, lastLogin, firstLogin
         * if still a tie just return 0
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
            return 0;
        }

        /**
         * Try to return long as an int, capped at int max and int min
         * @param l the long
         * @return the long as a capped int
         */
        private int intHelper(long l){
            try {
                return Math.toIntExact(l);
            } catch (ArithmeticException ae){
                switch(Long.compare(l, 0)){
                    case 1:
                        return Integer.MAX_VALUE;
                    case 0:
                        return 0;
                    case -1:
                        return Integer.MIN_VALUE;
                    default:
                        throw new ArithmeticException(); // Somehow Long.compare is broken??
                }
            }
        }
    }
}
