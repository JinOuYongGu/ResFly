package me.jinouyonggu.resfly;

import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Resfly extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private static List<String> flyPermissions = new ArrayList<>();
    private static List<String> bypassPermissions = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        flyPermissions = config.getStringList("Auto-fly-permissions");
        bypassPermissions = config.getStringList("Bypass-permissions");

        getServer().getPluginManager().registerEvents(this,this);

        getLogger().info("[Resfly] enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("[Resfly] disabled!");
    }

    @EventHandler
    public void onResChanged(ResidenceChangedEvent e) {
        ClaimedResidence res = e.getTo();
        Player player = e.getPlayer();
        if(res == null || res.getOwner() != player.getName()){
            for (String bypass : bypassPermissions) {
                if(!player.hasPermission(bypass)) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(config.getString("message.leave"));
                }
            }
        }else {
            for (String permission : flyPermissions) {
                if (player.hasPermission(permission)) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(config.getString("message.enter"));
                }
            }
        }
    }
}
