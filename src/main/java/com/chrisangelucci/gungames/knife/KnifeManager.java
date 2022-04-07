package com.chrisangelucci.gungames.knife;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KnifeManager implements Listener {

    public final static Material KNIFE_MATERIAL = Material.WOODEN_SWORD;

    public static void giveKnife(Player player) {
        ItemStack knife = new ItemStack(KNIFE_MATERIAL);
        ItemMeta im = knife.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.displayName(Component.text("Knife").style(Style.style(TextColor.color(255,0,0), TextDecoration.BOLD)));
        knife.setItemMeta(im);
        player.getInventory().addItem(knife);
        player.updateInventory();
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player){
            Player stabber = (Player)event.getDamager();
            if (stabber.getInventory().getItemInMainHand() == null) return;
            ItemStack inHand = stabber.getInventory().getItemInMainHand();
            if (inHand.getType() == KNIFE_MATERIAL){
                KnifeStabEvent knifeStabEvent = new KnifeStabEvent(stabber, event.getEntity());
                Bukkit.getPluginManager().callEvent(knifeStabEvent);
            }
        }
    }

}
