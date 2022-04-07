package com.chrisangelucci.gungames.map;

public enum Map {

    PRISON{
        public String getName() {
            return "Prison";
        }
        public Loc[] getSpawns() {
            return new Loc[]{
                    new Loc(-9, 208, -20),
                    new Loc(6, 218, -3),
                    new Loc(19, 215, -21),
                    new Loc(10, 207, -16),
                    new Loc(-3, 207, -20),
                    new Loc(-14, 201, -21),
                    new Loc(-17, 195, -14),
                    new Loc(-46, 190, -20),
                    new Loc(-32, 190, -32),
                    new Loc(-26, 191, -41),
                    new Loc(-29, 191, -53),
                    new Loc(-15, 191, -58),
                    new Loc(-9, 199, -54),
                    new Loc(6, 199, -49),
                    new Loc(21, 203, -44),
                    new Loc(30, 203, -17)
            };
        }

        public String getFileName() {
            return "mapPrison";
        }
    },
    JEDAMA{
        public String getName() {
            return "Jedama's Farm";
        }
        public Loc[] getSpawns() {
            return new Loc[]{
                    new Loc(6, 71, -33),
                    new Loc(-22, 56, -45),
                    new Loc(29, 56, -27),
                    new Loc(25, 56, 14),
                    new Loc(10, 56, 22),
                    new Loc(0, 56, 29),
                    new Loc(-2, 61, 17),
                    new Loc(-22, 56, 7),
                    new Loc(-32, 56, -26),
                    new Loc(-6, 56, -11),
                    new Loc(9, 58, -26),
                    new Loc(17, 58, -31),
                    new Loc(-2, 58, -37),
                    new Loc(18, 50, 23),
                    new Loc(-1, 50, 27),
                    new Loc(7, 53, -28),
            };
        }
        public String getFileName() {
            return "mapJedama";
        }
    };

    public abstract String getName();
    public abstract Loc[] getSpawns();
    public abstract String getFileName();

}
