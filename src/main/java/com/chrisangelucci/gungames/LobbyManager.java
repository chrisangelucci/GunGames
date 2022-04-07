package com.chrisangelucci.gungames;

import com.chrisangelucci.gungames.games.Deathmatch;
import com.chrisangelucci.gungames.games.GameType;
import com.chrisangelucci.gungames.games.Gamemode;
import com.chrisangelucci.gungames.games.Infected;
import com.chrisangelucci.gungames.gun.GunManager;
import com.chrisangelucci.gungames.knife.KnifeManager;
import com.chrisangelucci.gungames.map.Map;
import com.chrisangelucci.gungames.map.MapManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.concurrent.TimeUnit;

public class LobbyManager {

    public static int gameType = 0;
    public static double reloadTime = 1.0;
    public static boolean ammo = true;
    public static int selectedMap = 0;
    public static int matchTime = 5; //minutes

    private final static int MAXPLAYERS = 16;
    private final static int MINPLAYERS = 2;
    private final static int COUNTDOWNTIME = 20; //seconds

    public Location lobbySpawn;

    private boolean inGame = false;

    public Gamemode currentGame;

    //private Player host;

    public GunGames plugin;

    private MapManager map;

    private BukkitTask countdown;

    private Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective objective;

    private String gameState = "Lobby";

    public LobbyManager(GunGames plugin, Player host){
        this.plugin = plugin;
        //this.host = host;
        this.lobbySpawn = new Location(Bukkit.createWorld(new WorldCreator("lobby")), 0.5, 4, 0.5);

        host.sendMessage("You are the lobby host.");
        GameSettings.giveSelector(host);

        plugin.getServer().getPluginManager().registerEvents(new GunManager(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new KnifeManager(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GameSettings(), plugin);

        objective = board.registerNewObjective("lobbyboard", "dummy", Component.text("Need more players...").style(Style.style(TextColor.color(255,0,0), TextDecoration.BOLD)));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public int getScore(String name){
        return objective.getScore(name).getScore();
    }

    public void setScore(String name, int score){
        Score s = objective.getScore(name);
        s.setScore(score);
    }

    public void addScore(String name, int score){
        Score s = objective.getScore(name);
        s.setScore(s.getScore() + score);
    }

    public void removeScore(String name){
        board.resetScores(name);
    }

    public void setLobbyStatus(Component lobbyStatus){
        objective.displayName(lobbyStatus);
    }

    public Scoreboard getBoard(){
        return this.board;
    }

    public MapManager getMapManager(){
        return this.map;
    }

    public void addPlayer(String name){
        setScore(name, 0);
        if(Bukkit.getOnlinePlayers().size() == MINPLAYERS){
            startCountdown(COUNTDOWNTIME);
        }
    }

    public void checkStart(){
        if(Bukkit.getOnlinePlayers().size() >= MINPLAYERS){
            startCountdown(COUNTDOWNTIME);
        }else{
            setLobbyStatus(Component.text("Need more players...").style(Style.style(TextColor.color(255,0,0), TextDecoration.BOLD)));
        }
    }

    public void removePlayer(String name){
        removeScore(name);
        if(!inGame){
            if(Bukkit.getOnlinePlayers().size()-1 < MINPLAYERS){
                stopCountdown();
                setLobbyStatus(Component.text("Need more players...").style(Style.style(TextColor.color(255,0,0), TextDecoration.BOLD)));
            }
        }else{
            if(Bukkit.getOnlinePlayers().size()-1 < MINPLAYERS){
                currentGame.countdownComplete();
            }
        }
    }

    public boolean isFull() {
        return Bukkit.getOnlinePlayers().size() >= MAXPLAYERS;
    }

    private void countdownDone(){
        if(inGame){
            //stop game
            currentGame.countdownComplete();
        }else{
            //start game
            inGame = true;
            startCountdown(matchTime * 60);
            map = new MapManager(Map.values()[selectedMap]);
            map.teleportPlayers();
            currentGame = GameType.values()[gameType].getInstance();
            gameState = GameType.values()[gameType].toString();
            plugin.getServer().getPluginManager().registerEvents(currentGame, plugin);
        }
    }

    public void forceStartGame(){
        stopCountdown();
        countdownDone();
    }

    int secondsLeft;
    //time in seconds
    private void startCountdown(int time) {
        secondsLeft = time;
        countdown = new BukkitRunnable(){
            @Override
            public void run() {
                secondsLeft--;
                String time = String.format("%d:%02d",
                        TimeUnit.SECONDS.toMinutes(secondsLeft),
                        TimeUnit.SECONDS.toSeconds(secondsLeft)%60
                );
                setLobbyStatus(Component.text(gameState + " " + time).style(Style.style(TextColor.color(0xFFAA00), TextDecoration.BOLD)));
                if(secondsLeft == 0){
                    stopCountdown();
                    countdownDone();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void stopCountdown() {
        if(countdown == null)return;
        countdown.cancel();
    }

    public boolean inGame() {
        return this.inGame;
    }
}
