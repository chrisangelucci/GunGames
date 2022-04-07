package com.chrisangelucci.gungames;

import com.chrisangelucci.gungames.map.MapManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class GunGames extends JavaPlugin {

    private static LobbyManager lobby;
    private static GunGames instance;

    private static Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        getServer().getPluginManager().registerEvents(new GunGamesListener(), this);
    }

    public static void resetServer(Component finalMessage) {
        for (Player player : Bukkit.getOnlinePlayers())
            player.kick(finalMessage);
        Bukkit.getScheduler().cancelTasks(instance);
        Bukkit.unloadWorld(MapManager.CURRENT_MAP_NAME, false);
        lobby = null;
        HandlerList.unregisterAll(instance);
        instance.getServer().getPluginManager().registerEvents(new GunGamesListener(), instance);
    }

    public static void createLobby(Player host) {
        lobby = new LobbyManager(instance, host);
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    public static LobbyManager getLobby() {
        return lobby;
    }

    public static boolean lobbyExists() {
        return lobby != null;
    }
}
