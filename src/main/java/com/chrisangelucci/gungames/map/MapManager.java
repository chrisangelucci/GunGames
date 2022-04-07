package com.chrisangelucci.gungames.map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Random;

public class MapManager {

    public static final String CURRENT_MAP_NAME = "currentMap";

    private Map map;

    public MapManager(Map map) {
        this.map = map;
        MapUtils.copyWorld(new File(map.getFileName()), new File(CURRENT_MAP_NAME));
    }

    public void teleportPlayers(){
        int i = 0;
        for(Player p : Bukkit.getOnlinePlayers()){
            map.getSpawns()[i].teleportPlayer(p);
            i++;
        }
    }

    public Loc bestSpawn(){
        int bestDistanceFactor = -1;
        Loc best = null;
        for(Loc l : map.getSpawns()){
            if(l.getDistanceFactor() > bestDistanceFactor){
                bestDistanceFactor = l.getDistanceFactor();
                best = l;
            }
        }
        return best;
    }

    public Loc randomLocation(){
        return map.getSpawns()[new Random().nextInt(map.getSpawns().length)];
    }

}
