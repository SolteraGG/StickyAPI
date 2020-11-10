package com.dumbdogdiner.stickyapi.bukkit.item.generator;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.google.gson.Gson;

import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class PlayerHeadGenerator {
    private SkullMeta meta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD, 1)).getItemMeta();

    PlayerProfile ownerProfile;

    public PlayerHeadGenerator(UUID playerId){
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerId));
        ownerProfile = Bukkit.getServer().createProfile(playerId);
    }

    public PlayerHeadGenerator(Player player){
        this(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }

    public PlayerHeadGenerator(OfflinePlayer player){
        meta.setOwningPlayer(player);
        ownerProfile = Bukkit.createProfile(player.getUniqueId());
    }

    public PlayerHeadGenerator(ItemStack head){
        if(head.getType() != Material.PLAYER_HEAD && head.getType() != Material.PLAYER_WALL_HEAD)
            throw new IllegalArgumentException("head must be a player head or player wall head");
        meta = (SkullMeta) head.getItemMeta();
        meta.getPlayerProfile();
        if(!ownerProfile.hasTextures()){
            if(!ownerProfile.complete()){
                throw new IllegalArgumentException("Invalid player profile attached to the head, with no UUID or textures!");
            }
        }
    }

    public void setTexture(URL textureURL){
        // {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/63d621100fea5883922e78bb448056448c983e3f97841948a2da747d6b08b8ab"}}}
        class textures {
            SKIN skn;
            public textures(String url){
                skn = new SKIN(url);
            }
            class SKIN{
                String url;
                public SKIN(String s){
                    url = s;
                }
            }
        }

        setTexture(new String(Base64.getEncoder().encode(new Gson().toJson(new textures(textureURL.toString())).getBytes())));
    }

    /**
     * Set the texture with a pre-encoded string
     * @param texture Base64 string of the json of texture location
     */
    public void setTexture(String texture){
        ownerProfile.setProperty(new ProfileProperty("texture", texture));
        meta.setPlayerProfile(ownerProfile);
    }

    public ItemStack getHead(){
        return getHead(1);
    }

    public ItemStack getHead(int amount){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, amount);
        head.setItemMeta(meta);
        return null;
    }

}
