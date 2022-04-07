package com.chrisangelucci.gungames.gun.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BulletHitEntityEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player shooter;
    private Entity hit;

    public BulletHitEntityEvent(Player shooter, Entity hit){
        this.shooter = shooter;
        this.hit = hit;
    }
    public Player getShooter() {
        return shooter;
    }

    public Entity getHit() {
        return hit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}