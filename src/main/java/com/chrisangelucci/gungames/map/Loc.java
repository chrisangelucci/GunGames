package com.chrisangelucci.gungames.map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class Loc {

    public double x,y,z;

    public Loc(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location getLocation(){
        return new Location(Bukkit.createWorld(new WorldCreator(MapManager.CURRENT_MAP_NAME)), x, y, z);
    }

    public void teleportPlayer(Player p){
        p.teleport(new Location(Bukkit.createWorld(new WorldCreator(MapManager.CURRENT_MAP_NAME)), x, y, z));
    }

    public int getDistanceFactor() {
        int worstDistance = Integer.MAX_VALUE;
        for(Player p : Bukkit.getOnlinePlayers()){
            int distance = (int) p.getLocation().distanceSquared(getLocation());
            if(distance < worstDistance){
                worstDistance = distance;
            }
        }
        return worstDistance;
    }

}
