package com.chrisangelucci.gungames.games;

import com.chrisangelucci.gungames.GunGames;
import com.chrisangelucci.gungames.LobbyManager;
import com.chrisangelucci.gungames.gun.GunManager;
import com.chrisangelucci.gungames.gun.events.BulletHitEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Deathmatch extends Gamemode implements Listener {

    public Deathmatch(){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            GunManager.giveGun(p);
        }

        if(LobbyManager.ammo){
            new BukkitRunnable(){
                public void run() {
                    Location l = GunGames.getLobby().getMapManager().randomLocation().getLocation();
                    l.getWorld().dropItem(l.add(0,1,0), new ItemStack(GunManager.AMMO_MATERIAL));
                }
            }.runTaskTimer(GunGames.getLobby().plugin, 0, 20);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onLava(PlayerMoveEvent event){
        if(event.getTo().getBlock().getType() == Material.LAVA){
            Component deathMessage = Component.text(event.getPlayer().getName()).style(Style.style(TextColor.color(120,0,0))).append(Component.text(" has died!").style(Style.style(TextColor.color(255,255,255))));
            Bukkit.broadcast(deathMessage);
            event.getPlayer().getInventory().clear();
            GunManager.giveGun(event.getPlayer());
            GunGames.getLobby().getMapManager().bestSpawn().teleportPlayer(event.getPlayer());
            event.getPlayer().setFireTicks(0);
        }
    }

    @EventHandler
    public void onPlayerHit(BulletHitEntityEvent event){
        if(event.getHit() instanceof Player hit){
            Component shotMessage = Component.text(event.getShooter().getName()).style(Style.style(TextColor.color(120,0,0)))
                    .append(Component.text(" has shot ").style(Style.style(TextColor.color(255,255,255))))
                    .append(Component.text(hit.getName()).style(Style.style(TextColor.color(120,0,0))))
                    .append(Component.text("!").style(Style.style(TextColor.color(255,255,255))));
            Bukkit.broadcast(shotMessage);
            GunGames.getLobby().addScore(event.getShooter().getName(), 1);
            int amount = 0;
            for (ItemStack item : hit.getInventory().getContents()) {
                if(item == null)continue;
                if (item.getType() == GunManager.AMMO_MATERIAL) {
                    amount += item.getAmount();
                }
            }
            if(amount != 0)
                hit.getWorld().dropItem(hit.getEyeLocation(), new ItemStack(GunManager.AMMO_MATERIAL, amount));
            hit.getInventory().clear();
            GunManager.giveGun(hit);
            GunGames.getLobby().getMapManager().bestSpawn().teleportPlayer(hit);
        }
    }

    @Override
    public void countdownComplete() {
        String top = "";
        int topScore = -1;
        for(Player p : Bukkit.getOnlinePlayers()){
            int score = GunGames.getLobby().getScore(p.getName());
            if(score > topScore){
                top = p.getName();
                topScore = score;
            }
        }
        Component resetMessage = Component.text(top).style(Style.style(TextColor.color(150,150,0), TextDecoration.BOLD))
                        .append(Component.text(" has won the game!").style(Style.style(TextColor.color(255,255,255))));
        GunGames.resetServer(resetMessage);
    }

}