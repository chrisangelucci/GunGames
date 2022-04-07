package com.chrisangelucci.gungames.gun;

import org.bukkit.entity.Player;

public class BulletData {

    private Player shooter;
    private double damage;

    public BulletData(Player shooter, double damage) {
        this.shooter = shooter;
        this.damage = damage;
    }

    public Player getShooter(){
        return shooter;
    }

    public Double getDamage() {
        return damage;
    }

}
