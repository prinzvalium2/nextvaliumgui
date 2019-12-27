package de.prinzvalium.nextvaliumgui.nextcolony;

import org.json.JSONObject;

public class ProductionRessource {
    
    private int depot;
    private int level;
    private int production;
    private double safe;
    
    // {"ore":{"depot":1320,"level":16,"production":640,"safe":102.30000000000001,"booster":null},"coal":...
    ProductionRessource(JSONObject jsonRessource) {
        depot = jsonRessource.getInt("depot");
        level = jsonRessource.getInt("level");
        production = jsonRessource.getInt("production");
        safe = jsonRessource.getDouble("safe");
    }

    public int getDepot() {
        return depot;
    }

    public int getLevel() {
        return level;
    }

    public int getProduction() {
        return production;
    }

    public double getSafe() {
        return safe;
    }
}
