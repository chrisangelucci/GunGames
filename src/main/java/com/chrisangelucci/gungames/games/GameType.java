package com.chrisangelucci.gungames.games;

public enum GameType {

    DEATHMATCH{
        public String toString(){
            return "Deathmatch";
        }
    },
    INFECTED{
        public String toString(){
            return "Infected";
        }
    };

    public abstract String toString();

}
