package com.chrisangelucci.gungames.games;

public enum GameType {

    DEATHMATCH{
        public String toString(){
            return "Deathmatch";
        }

        public Deathmatch getInstance() {
            return new Deathmatch();
        }
    },
    INFECTED{
        public String toString(){
            return "Infected";
        }

        public Infected getInstance() {
            return new Infected();
        }
    };

    public abstract String toString();
    public abstract Gamemode getInstance();

}
