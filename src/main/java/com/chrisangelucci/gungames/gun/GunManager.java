package com.chrisangelucci.gungames.gun;

import com.chrisangelucci.gungames.GunGames;
import com.chrisangelucci.gungames.LobbyManager;
import com.chrisangelucci.gungames.gun.events.BulletHitBlockEvent;
import com.chrisangelucci.gungames.gun.events.BulletHitEntityEvent;
import com.chrisangelucci.gungames.gun.events.GunShootEvent;
import com.chrisangelucci.gungames.util.Cooldown;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class GunManager implements Listener {

    public static final Material GUN_MATERIAL = Material.WOODEN_HOE;
    public static final Material AMMO_MATERIAL = Material.IRON_INGOT;

    public final static WeakHashMap<Entity, BulletData> bullets = new WeakHashMap<Entity, BulletData>();

    public static void giveGun(Player p){
        ItemStack gun = new ItemStack(GUN_MATERIAL);
        ItemMeta im = gun.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.displayName(Component.text("Gun").style(Style.style(TextColor.color(150,150,150), TextDecoration.BOLD)));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Right-click to shoot.").style(Style.style(TextColor.color(255,255,255))));
        im.lore(lore);
        gun.setItemMeta(im);
        p.getInventory().addItem(gun);
        p.updateInventory();
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player)event.getEntity();
            if (!(event.getItem().getItemStack().getType() == AMMO_MATERIAL)){
                event.setCancelled(!player.isOp() || GunGames.getLobby().inGame());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (event.getItem() != null){
                if (event.getItem().getType() == GUN_MATERIAL){
                    event.setCancelled(true);
                    if (LobbyManager.ammo){
                        if (!p.getInventory().contains(AMMO_MATERIAL)) return;
                        if (Cooldown.tryCooldown(p, "fire", (long)LobbyManager.reloadTime*1000)){
                            if(shootBullet(p)){
                                takeAmmo(p);
                            }
                        }
                    } else {
                        if (Cooldown.tryCooldown(p, "fire", (long) LobbyManager.reloadTime*1000)){
                            shootBullet(p);
                        }
                    }
                }
            }
        }
    }

    private void takeAmmo(Player p){
        for (ItemStack is : p.getInventory().getContents()){
            if (is == null) continue;
            if (is.getType() == AMMO_MATERIAL) {
                is.setAmount(is.getAmount()-1);
                p.updateInventory();
                return;
            }
        }
    }

    @EventHandler
    public void onProjectHit(ProjectileHitEvent event){
        if (event.getEntity() instanceof Snowball) {
            if (GunManager.bullets.containsKey(event.getEntity())) {
                event.getEntity().remove();
                BulletData bd = GunManager.bullets.get(event.getEntity());
                if (event.getHitEntity() != null){
                    BulletHitEntityEvent bulletHitEntityEvent = new BulletHitEntityEvent(bd.getShooter(), event.getHitEntity());
                    Bukkit.getPluginManager().callEvent(bulletHitEntityEvent);
                } else if (event.getHitBlock() != null){
                    BulletHitBlockEvent bulletHitBlockEvent = new BulletHitBlockEvent(bd.getShooter(), event.getHitBlock());
                    Bukkit.getPluginManager().callEvent(bulletHitBlockEvent);
                }
            }
        }
    }

    //returns whether or not the bullet was shot
    private boolean shootBullet(Player player) {
        GunShootEvent event = new GunShootEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled())return false;

        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setVelocity(projectile.getVelocity().multiply(10));
        BulletData data = new BulletData(player, 2000);
        bullets.put(projectile, data);

        Location start = player.getEyeLocation();
        Vector increase = start.getDirection();
        for (int counter = 0; counter < 100; counter++) {
            Location point = start.add(increase);
            if(point.getBlock().getType() != Material.AIR)
                break;
            player.getWorld().spawnParticle(Particle.REDSTONE, point, 25, new Particle.DustOptions(Color.GRAY, 1));
        }
        return true;
    }

}
