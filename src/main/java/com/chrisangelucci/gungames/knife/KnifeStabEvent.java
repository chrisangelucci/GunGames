package com.chrisangelucci.gungames.knife;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KnifeStabEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player whoStabbed;
    private Entity stabbed;

    public KnifeStabEvent(Player whoStabbed, Entity stabbed){
        this.whoStabbed = whoStabbed;
        this.stabbed = stabbed;
    }

    public Player getWhoStabbed(){
        return this.whoStabbed;
    }

    public Entity getStabbed(){
        return this.stabbed;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
