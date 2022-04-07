package com.chrisangelucci.gungames.games;

import com.chrisangelucci.gungames.GunGames;
import com.chrisangelucci.gungames.LobbyManager;
import com.chrisangelucci.gungames.gun.GunManager;
import com.chrisangelucci.gungames.gun.events.BulletHitEntityEvent;
import com.chrisangelucci.gungames.knife.KnifeManager;
import com.chrisangelucci.gungames.knife.KnifeStabEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Infected extends Gamemode implements Listener {

    private final static int INFECTED = 1;

    public Infected(){
        int random = new Random().nextInt(Bukkit.getOnlinePlayers().size());
        Player infected = (Player) Bukkit.getOnlinePlayers().toArray()[random];
        infected.getInventory().clear();
        KnifeManager.giveKnife(infected);
        infected.setGameMode(GameMode.ADVENTURE);
        setInfected(infected);

        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getName().equals(infected.getName()))continue;
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

    private void respawnPlayer(Player p){
        p.getInventory().clear();
        KnifeManager.giveKnife(p);
        GunGames.getLobby().getMapManager().randomLocation().teleportPlayer(p);
    }

    private boolean isInfected(Player p){
        return GunGames.getLobby().getScore(ChatColor.RED + p.getName()) == INFECTED;
    }

    private void setInfected(Player p){
        GunGames.getLobby().removeScore(p.getName());
        GunGames.getLobby().setScore(ChatColor.RED + p.getName(), INFECTED);
        if(allInfected()){
            noSurvivors();
        }
    }

    private boolean allInfected(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!isInfected(p)){
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onLava(PlayerMoveEvent e){
        if(e.getTo().getBlock().getType() == Material.LAVA){
            if(!isInfected(e.getPlayer())) {
                Component deathMessage = Component.text(e.getPlayer().getName()).style(Style.style(TextColor.color(120,0,0))).append(Component.text(" has died!").style(Style.style(TextColor.color(255,255,255))));
                Bukkit.broadcast(deathMessage);
            }
            respawnPlayer(e.getPlayer());
            e.getPlayer().setFireTicks(0);
        }
    }

    @EventHandler
    public void onStab(KnifeStabEvent e){
        if(e.getStabbed() instanceof Player){
            Player stabbed = (Player)e.getStabbed();
            if(isInfected(stabbed))return;
            Component shotMessage = Component.text(e.getWhoStabbed().getName()).style(Style.style(TextColor.color(120,0,0)))
                    .append(Component.text(" has infected ").style(Style.style(TextColor.color(255,255,255))))
                    .append(Component.text(e.getStabbed().getName()).style(Style.style(TextColor.color(120,0,0))))
                    .append(Component.text("!").style(Style.style(TextColor.color(255,255,255))));
            Bukkit.broadcast(shotMessage);
            respawnPlayer(stabbed);
            setInfected(stabbed);
        }
    }

    @EventHandler
    public void onHit(BulletHitEntityEvent e){
        if(e.getHit() instanceof Player){
            Player hit = (Player)e.getHit();
            if(!isInfected(hit))return;
            respawnPlayer((Player)e.getHit());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        e.setCancelled(true);
    }

    private void noSurvivors() {
        Component winMessage = Component.text("The Infected").style(Style.style(TextColor.color(0xFFAA00), TextDecoration.BOLD))
                        .append(Component.text(" have won the game!").style(Style.style(TextColor.color(255,255,255))));
        GunGames.resetServer(winMessage);
    }

    public void countdownComplete() {
        Component winMessage = Component.text("The Survivors").style(Style.style(TextColor.color(0xFFAA00), TextDecoration.BOLD))
                .append(Component.text(" have won the game!").style(Style.style(TextColor.color(255,255,255))));
        GunGames.resetServer(winMessage);
    }

}