package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Building {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Building.class);
    private String name;
    private Date busy;
    private int coal;
    private int ore;
    private int copper;
    private int uranium;
    private int current;
    private int skill;
    private int cur_rate;
    private String planetId;
    private String userName;
    private String planetName;
    private Buildings buildings;
    private static boolean upgraded = false;
    
    public static boolean isUpgraded() {
        return upgraded;
    }

    public static void setUpgraded(boolean upgraded) {
        Building.upgraded = upgraded;
    }

    public Building(JSONObject jsonBuilding, Buildings buildings) {
        
        this.buildings = buildings;
        this.userName = buildings.getUserName();
        this.planetId = buildings.getPlanetId();
        this.planetName = buildings.getPlanetName();
        
        name = jsonBuilding.getString("name");
        try {
            busy = new Date(jsonBuilding.getLong("busy")*1000);
        }
        catch (JSONException e) {
            busy = new Date(0);
        }
        current = jsonBuilding.getInt("current");
        skill = jsonBuilding.isNull("skill") ? 0 : jsonBuilding.getInt("skill");
        
        coal = jsonBuilding.getInt("coal");
        ore = jsonBuilding.getInt("ore");
        copper = jsonBuilding.getInt("copper");
        uranium = jsonBuilding.getInt("uranium");
        
        try {
            cur_rate = jsonBuilding.getInt("cur_rate");
        }
        catch (JSONException e) {
            cur_rate = -1;
        }
    }
    
    
    public String getName() {
        return name;
    }

    public Date getBusy() {
        return busy;
    }

    public int getCurrent() {
        return current;
    }

    public int getSkill() {
        return skill;
    }

    public int getCoal() {
        return coal;
    }

    public int getOre() {
        return ore;
    }

    public int getCopper() {
        return copper;
    }

    public int getUranium() {
        return uranium;
    }

    public int getCurRate() {
        return cur_rate;
    }
}
