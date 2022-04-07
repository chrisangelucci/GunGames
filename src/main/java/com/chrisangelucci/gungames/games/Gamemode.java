package com.chrisangelucci.gungames.games;

import org.bukkit.event.Listener;

public abstract class Gamemode implements Listener {

    //method to force a win
    public abstract void countdownComplete();
}
