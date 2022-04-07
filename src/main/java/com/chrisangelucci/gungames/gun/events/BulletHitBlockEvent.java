package com.chrisangelucci.gungames.gun.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BulletHitBlockEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player shooter;
    private Block hit;

    public BulletHitBlockEvent(Player shooter, Block hit){
        this.shooter = shooter;
        this.hit = hit;
    }
    public Player getShooter() {
        return shooter;
    }

    public Block getHit() {
        return hit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}