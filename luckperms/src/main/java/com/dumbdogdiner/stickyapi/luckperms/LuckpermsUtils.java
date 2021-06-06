package com.dumbdogdiner.stickyapi.luckperms;

import com.dumbdogdiner.stickyapi.common.ServerVersion;
import lombok.experimental.UtilityClass;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class LuckpermsUtils {
    public @NotNull LuckPerms getLuckpermsInstance(){
        if(ServerVersion.isBukkit()){
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if(provider != null)
                return provider.getProvider();
            else
                throw new IllegalStateException("Luckperms is not available", new AssertionError());
        } else if(ServerVersion.isBungee()) {
            // We need to try to use the singleton, which is potentially less safe
            try {
                return LuckPermsProvider.get();
            } catch (Throwable e) {
                e.printStackTrace();
                throw new IllegalStateException("Luckperms is not available", e);
            }
        } else {
            throw new IllegalStateException("Luckperms only runs on Bukkit and Bungeecord, what's going on??");
        }
    }

    /**
     * Get a list of all groups
     * @return
     */
    public List<String> getGroupsList(){
        return getLuckpermsInstance()
                .getGroupManager()
                .getLoadedGroups()
                .stream()
                .map(
                        group -> group
                                .getName()
                                .toLowerCase()
                )
                .collect(Collectors.toList());
    }
}
