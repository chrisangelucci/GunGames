package com.chrisangelucci.gungames;

import com.chrisangelucci.gungames.gun.events.BulletHitBlockEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GunGamesListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player)
            event.setCancelled(!GunGames.getLobby().inGame());
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event){
        if (event.getEntity() instanceof TNTPrimed)
            event.setYield(0);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockHit(BulletHitBlockEvent event){
        if(event.getHit().getType() == Material.TNT){
            event.getHit().setType(Material.AIR);
            TNTPrimed tnt = (TNTPrimed)event.getHit().getWorld().spawn(event.getHit().getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(0);
        }else if(event.getHit().getType() == Material.ORANGE_WOOL){
            Block n = event.getHit().getRelative(BlockFace.NORTH);
            Block s = event.getHit().getRelative(BlockFace.SOUTH);
            Block ea = event.getHit().getRelative(BlockFace.EAST);
            Block w = event.getHit().getRelative(BlockFace.WEST);
            n.setType(Material.RED_WOOL);
            s.setType(Material.RED_WOOL);
            ea.setType(Material.RED_WOOL);
            w.setType(Material.RED_WOOL);
            event.getHit().setType(Material.AIR);
        }else if(event.getHit().getType() == Material.RED_WOOL){
            event.getHit().setType(Material.AIR);
        }else if(event.getHit().getType() == Material.GLASS || event.getHit().getType() == Material.GLASS_PANE){
            event.getHit().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onJumpPad(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.PHYSICAL)){
            if(event.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE){
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 10));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.joinMessage(Component.empty());
        resetPlayer(event.getPlayer());
        if(!GunGames.lobbyExists()){
            GunGames.createLobby(event.getPlayer());
        }
        if(GunGames.getLobby().inGame()){
            event.getPlayer().kickPlayer("The game has already started!");
            return;
        }
        if(GunGames.getLobby().isFull()){
            event.getPlayer().kickPlayer("This lobby is full!");
            return;
        }
        GunGames.getLobby().addPlayer(event.getPlayer().getName());
        event.getPlayer().setScoreboard(GunGames.getLobby().getBoard());
        event.getPlayer().teleport(GunGames.getLobby().lobbySpawn);

    }

    private void resetPlayer(Player player){
        player.getInventory().clear();
        player.updateInventory();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFireTicks(0);
        player.setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        event.quitMessage(Component.empty());
        GunGames.getLobby().removePlayer(event.getPlayer().getName());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        event.setCancelled(!event.getPlayer().isOp() || GunGames.getLobby().inGame());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        event.setCancelled(!event.getPlayer().isOp() || GunGames.getLobby().inGame());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getHand() == EquipmentSlot.OFF_HAND)return;
        if(event.getItem() == null)return;
        if(event.getItem().getType() == Material.WATER_BUCKET || event.getItem().getType() == Material.LAVA_BUCKET){
            event.setCancelled(!event.getPlayer().isOp() || GunGames.getLobby().inGame());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        event.setCancelled(!event.getPlayer().isOp() || GunGames.getLobby().inGame());
    }

}
