package com.chrisangelucci.gungames.gun.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GunShootEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    private Player shooter;

    public GunShootEvent(Player shooter){
        this.shooter = shooter;
    }

    public Player getShooter() {
        return shooter;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}