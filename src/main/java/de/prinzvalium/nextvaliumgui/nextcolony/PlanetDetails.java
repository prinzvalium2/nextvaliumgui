package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class PlanetDetails {
    
    private String planetId;
    private String userName;
    
    private String img;
    private int level_base;
    private int level_coal;
    private int level_coaldepot;
    private int level_copper;
    private int level_copperdepot;
    private int level_ore;
    private int level_oredepot;
    private int level_research;
    private int level_ship;
    private int level_uranium;
    private int level_uraniumdepot;
    private int planet_bonus;
    private int planet_corx;
    private int planet_cory;
    private int planet_crts;
    private String planet_name;
    private String planet_rarity;
    private String planet_type;
    private long shieldcharge_busy;
    private int shieldprotection_busy;
    private int startplanet;
    private int total_type;
    
    public PlanetDetails(String planetId) throws JSONException, IOException {
        this.planetId = planetId;
        loadPlanetDetails();
    }

    public void loadPlanetDetails() throws JSONException, IOException {
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADPLANET, planetId);
        JSONObject jsonPlanet = Util.getJSONObjectFromApiCommand(apiCmd);
        
        userName = jsonPlanet.getString("user");
        planetId = jsonPlanet.getString("planet_id");
        planet_corx = jsonPlanet.getInt("planet_corx");
        planet_cory = jsonPlanet.getInt("planet_cory");
        
        level_coal = jsonPlanet.getInt("level_coal");
        level_ore = jsonPlanet.getInt("level_ore");
        level_copper = jsonPlanet.getInt("level_copper");
        level_uranium = jsonPlanet.getInt("level_uranium");
        
        level_coaldepot = jsonPlanet.getInt("level_coaldepot");
        level_oredepot = jsonPlanet.getInt("level_oredepot");
        level_copperdepot = jsonPlanet.getInt("level_copperdepot");
        level_uraniumdepot = jsonPlanet.getInt("level_uraniumdepot");
    }

    public String getPlanetId() {
        return planetId;
    }

    public String getUserName() {
        return userName;
    }

    public String getImg() {
        return img;
    }

    public int getLevel_base() {
        return level_base;
    }

    public int getLevel_coal() {
        return level_coal;
    }

    public int getLevel_coaldepot() {
        return level_coaldepot;
    }

    public int getLevel_copper() {
        return level_copper;
    }

    public int getLevel_copperdepot() {
        return level_copperdepot;
    }

    public int getLevel_ore() {
        return level_ore;
    }

    public int getLevel_oredepot() {
        return level_oredepot;
    }

    public int getLevel_research() {
        return level_research;
    }

    public int getLevel_ship() {
        return level_ship;
    }

    public int getLevel_uranium() {
        return level_uranium;
    }

    public int getLevel_uraniumdepot() {
        return level_uraniumdepot;
    }

    public int getPlanet_bonus() {
        return planet_bonus;
    }

    public int getPlanet_corx() {
        return planet_corx;
    }

    public int getPlanet_cory() {
        return planet_cory;
    }

    public int getPlanet_crts() {
        return planet_crts;
    }

    public String getPlanet_name() {
        return planet_name;
    }

    public String getPlanet_rarity() {
        return planet_rarity;
    }

    public String getPlanet_type() {
        return planet_type;
    }

    public long getShieldcharge_busy() {
        return shieldcharge_busy;
    }

    public int getShieldprotection_busy() {
        return shieldprotection_busy;
    }

    public int getStartplanet() {
        return startplanet;
    }

    public int getTotal_type() {
        return total_type;
    }
    
    public static void main(String[] args) throws JSONException, IOException {
        Util.setProxy();
        new PlanetDetails("P-ZGD7FULO3I8").loadPlanetDetails(); // patti.pizza
    }
    
}
