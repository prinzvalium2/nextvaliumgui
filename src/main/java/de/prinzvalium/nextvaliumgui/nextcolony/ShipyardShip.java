package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipyardShip {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipyardShip.class);
    
    private String longname; // "Battlecruiser Tiger"
    private String type; // "battlecruiser"
    private int cur_level; // shipyard building?
    private int cur_level_skill; // shipyard skill?
    private int min_level; // Transporter 12, Explorer 13...
    private int skill; // Ship Skill must be 20
    private int speed;
    private Date busy_until;
    private boolean activated;
    private int variant; // if > 0 then activated must be 1 // no longer true!
    
    // cost
    private int coal;
    private int ore;
    private int copper;
    private int uranium;
    private Date time;
    
    private String planetId;
    private String userName;
    private String planetName;
    
   
    ShipyardShip(JSONObject jsonShipyardShip, String userName, String planetId, String planetName){
        LOGGER.trace("ShipyardShip()");
        
        this.userName = userName;
        this.planetId = planetId;
        this.planetName = planetName;
        
        longname = jsonShipyardShip.getString("longname");
        type = jsonShipyardShip.getString("type");
        activated = jsonShipyardShip.getBoolean("activated");
        variant = jsonShipyardShip.getInt("variant");
        speed = jsonShipyardShip.getInt("speed");
        
        try {
            busy_until = new Date(jsonShipyardShip.getLong("busy_until")*1000);
        }
        catch (JSONException e) {
            busy_until = new Date(0);
        }
        
        cur_level = jsonShipyardShip.getInt("cur_level");
        min_level = jsonShipyardShip.getInt("min_level");
        
        try {
            cur_level_skill = jsonShipyardShip.getInt("cur_level_skill");
        }
        catch (JSONException e) {
            cur_level_skill = 0;
        }
        
        try {
            skill = jsonShipyardShip.getInt("skill");
        }
        catch (JSONException e) {
            skill = 0;
        }
       
        // cost
        JSONObject jsonCost = jsonShipyardShip.getJSONObject("cost");
        coal = jsonCost.getInt("coal");
        ore = jsonCost.getInt("ore");
        copper = jsonCost.getInt("copper");
        uranium = jsonCost.getInt("uranium");
        try {
            time = new Date(jsonCost.getLong("time")*1000);
        }
        catch (JSONException e) {
            time = new Date(0);
        }
     }
    
    public static Logger getLogger() {
        return LOGGER;
    }

    public String getLongname() {
        return longname;
    }

    public String getType() {
        return type;
    }

    public int getCur_level() {
        return cur_level;
    }

    public int getCur_level_skill() {
        return cur_level_skill;
    }

    public int getMin_level() {
        return min_level;
    }

    public int getSkill() {
        return skill;
    }

    public Date getBusy_until() {
        return busy_until;
    }

    public boolean isActivated() {
        return activated;
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

    public Date getTime() {
        return time;
    }

    public String getPlanetId() {
        return planetId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPlanetName() {
        return planetName;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public int getSpeed() {
        return speed;
    }

    public int getVariant() {
        return variant;
    }

}
