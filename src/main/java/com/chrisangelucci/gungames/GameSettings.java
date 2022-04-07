package com.chrisangelucci.gungames;

import com.chrisangelucci.gungames.games.GameType;
import com.chrisangelucci.gungames.gun.GunManager;
import com.chrisangelucci.gungames.map.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GameSettings implements Listener {

    private final static Component TITLE = Component.text("Game Settings").style(Style.style(TextColor.color(0,70,0)));
    private final static int MAP_SLOT = 3;
    private final static int GAMEMODE_SLOT = 5;
    private final static int GAMETIME_SLOT = 20;
    private final static int RELOADSPEED_SLOT = 22;
    private final static int AMMO_SLOT = 24;


    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory selector = event.getInventory();
        if(selector.equals(getSettingsGUI())){
            event.setCancelled(true);
            int slot = event.getSlot();
            if(slot == MAP_SLOT){
                LobbyManager.selectedMap = next(LobbyManager.selectedMap, Map.values().length-1, 0);
            }else if(slot == GAMETIME_SLOT){
                LobbyManager.matchTime = next(LobbyManager.matchTime, 20, 1);
            }else if(slot == AMMO_SLOT){
                LobbyManager.ammo = next(LobbyManager.ammo);
            }else if(slot == RELOADSPEED_SLOT){
                LobbyManager.reloadTime = next(LobbyManager.reloadTime, 2, 0.1);
            }else if(slot == GAMEMODE_SLOT){
                LobbyManager.gameType = next(LobbyManager.gameType, GameType.values().length-1,0);
            }
            updateSettings(selector);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(event.getInventory().equals(getSettingsGUI())){
            GunGames.getLobby().checkStart();
        }
    }

    private static boolean next(boolean current){
        return !current;
    }

    private static double round(double value) {
        return (double) Math.round(value * 10) / 10;
    }

    private static double next(double current, double top, double bottom){
        if(current+0.1>top)
            return bottom;
        return round(current+0.1);
    }

    private static int next(int current, int top, int bottom){
        if(current+1>top)
            return bottom;
        return current+1;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND)return;
        if(event.getItem() == null)return;
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getItem().getType() == Material.LEAD){
                if(GunGames.getLobby().inGame())return;
                openSettings(p);
                event.setCancelled(true);
            }
        }
    }

    public static void giveSelector(Player host) {
        ItemStack selector = new ItemStack(Material.LEAD);
        ItemMeta im = selector.getItemMeta();
        im.displayName(Component.text("Game Settings").style(Style.style(TextColor.color(0,255,0))));
        selector.setItemMeta(im);
        host.getInventory().setItem(4, selector);
    }

    private static void updateSettings(Inventory selector){
        selector.setItem(MAP_SLOT, getMapItem(LobbyManager.selectedMap));
        selector.setItem(GAMETIME_SLOT, getClockItem(LobbyManager.matchTime));
        selector.setItem(AMMO_SLOT, getAmmoItem(LobbyManager.ammo));
        selector.setItem(RELOADSPEED_SLOT, getGunItem(LobbyManager.reloadTime));
        selector.setItem(GAMEMODE_SLOT, getGamemodeItem(LobbyManager.gameType));
    }

    private static Inventory getSettingsGUI() {
        Inventory selector = Bukkit.createInventory(null, 27, TITLE);
        updateSettings(selector);
        return selector;
    }
    private static void openSettings(Player p){
        GunGames.getLobby().setLobbyStatus(Component.text("Changing game settings...").style(Style.style(TextColor.color(255,0,0), TextDecoration.BOLD)));
        GunGames.getLobby().stopCountdown();
        p.openInventory(getSettingsGUI());
    }

    private static ItemStack getGamemodeItem(int gameType) {
        ItemStack game = new ItemStack(Material.NETHER_STAR);
        ItemMeta im = game.getItemMeta();
        im.displayName(Component.text("Game Mode").style(Style.style(TextColor.color(0,255,0))));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text(GameType.values()[gameType].toString()).style(Style.style(TextColor.color(255,255,255))));
        im.lore(lore);
        game.setItemMeta(im);
        return game;
    }

    private static ItemStack getGunItem(double reloadTime) {
        ItemStack gun = new ItemStack(GunManager.GUN_MATERIAL);
        ItemMeta im = gun.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.displayName(Component.text("Reload Time").style(Style.style(TextColor.color(70,70,70))));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text(reloadTime+" seconds").style(Style.style(TextColor.color(255,255,255))));
        im.lore(lore);
        gun.setItemMeta(im);
        return gun;
    }

    private static ItemStack getAmmoItem(boolean useAmmo) {
        ItemStack ammo = new ItemStack(GunManager.AMMO_MATERIAL);
        ItemMeta im = ammo.getItemMeta();
        im.displayName(Component.text("Use Ammo").style(Style.style(TextColor.color(70,70,70))));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text(useAmmo?"Yes":"No").style(Style.style(TextColor.color(255,255,255))));
        im.lore(lore);
        ammo.setItemMeta(im);
        return ammo;
    }

    private static ItemStack getClockItem(int gameTime){
        ItemStack clock = new ItemStack(Material.CLOCK);
        ItemMeta im = clock.getItemMeta();
        im.displayName(Component.text("Game Time").style(Style.style(TextColor.color(255,255,0))));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text(gameTime + " minutes").style(Style.style(TextColor.color(255,255,255))));
        im.lore(lore);
        clock.setItemMeta(im);
        return clock;
    }

    private static ItemStack getMapItem(int mapNumber){
        ItemStack map = new ItemStack(Material.MAP);
        ItemMeta im = map.getItemMeta();
        im.displayName(Component.text("Map").style(Style.style(TextColor.color(0,255,0))));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text(Map.values()[mapNumber].getName()).style(Style.style(TextColor.color(255,255,255))));
        im.lore(lore);
        map.setItemMeta(im);
        return map;
    }

}
