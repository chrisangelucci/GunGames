# GunGames
A Minecraft Paper plugin for version 1.18.2 that manages a single lobby of minigames involving the use of weapons. Currently has two gamemodes, Deathmatch and Infected,
but adding new games is quite easy. A lot of hardcoded values in here, as it was originally made to just play with friends. Inspired by a server I used to play on, I think it might have just been called "gun games", but I can't find it now. The maps used for the plugin were downloaded using a client side mod, but are of course, not included here.

# Features
* Lobby with an in-game settings GUI. Settings can be changed by the host of the lobby, which is the first player to join the server. Settings include map selection, gamemode selection, length of game, reload speed, and whether or not ammo should be consumed.
* A gun, ammo, and knife items, which a set to be a wooden hoe, iron ingots, and a wooden sword respectively.
* When TNT is shot, it will explode. When orange wool is shot, it will turn to air, with the surrounding blocks turning to red wool. Red wool and glass can be destroyed by shooting it.
* A stone pressure plate will give the player 10 seconds of jump boost.
* Auto resetting maps by copying the world files to a new world to be played in.

# Deathmatch
* The objective is to have the highest kill count by the end of the match. Everyone uses guns here.

# Infected
* A random player starts as infected and must kill using a knife. When killed by the infected, you respawn as infected.
* The infected's goal is to infect everyone.
* The survivor's goal is to survive until the time runs out.

# Using this plugin
You need a Paper server with a world named "lobby", and one or more map worlds. For each map world you want to use, you need to specify the world name and spawn locations in [Map.java](src/main/java/com/chrisangelucci/gungames/map/Map.java).

# Creating Gamemodes
See [Deathmatch.java](src/main/java/com/chrisangelucci/gungames/games/Deathmatch.java) and [Infected.java](src/main/java/com/chrisangelucci/gungames/games/Infected.java) as examples.
1. You must make a class that extends [Gamemode.java](src/main/java/com/chrisangelucci/gungames/games/Gamemode.java). This is a listener class, use it to code any game specific logic.
2. The countdownComplete method is run when the time runs out, at the end of this method you should call GunGames.resetServer to end the game. You can, of course, call this method early for a different win condition.
3. Add your gamemode to the [GameType](src/main/java/com/chrisangelucci/gungames/games/GameType.java) enum.

There are four events you can use to create your gamemode:
* [GunShootEvent](src/main/java/com/chrisangelucci/gungames/gun/events/GunShootEvent.java)
* [BulletHitBlockEvent](src/main/java/com/chrisangelucci/gungames/gun/events/BulletHitBlockEvent.java)
* [BulletHitEntityEvent](src/main/java/com/chrisangelucci/gungames/gun/events/BulletHitEntityEvent.java)
* [KnifeStabEvent](src/main/java/com/chrisangelucci/gungames/knife/KnifeStabEvent.java)
