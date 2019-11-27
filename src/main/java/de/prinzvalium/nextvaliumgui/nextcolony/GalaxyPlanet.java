package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONObject;

public class GalaxyPlanet {

    private boolean abandoned;
    private boolean bonus;
    private String id;
    private String name;
    private boolean starter;
    private int type;
    private Date update;
    private String user;
    private int x;
    private int y;
    
    public GalaxyPlanet(JSONObject jsonPlanet) {
        abandoned = jsonPlanet.getInt("abandoned") == 1; 
        bonus = jsonPlanet.getInt("bonus") == 1; 
        id = jsonPlanet.getString("id"); 
        name = jsonPlanet.getString("name"); 
        starter = jsonPlanet.getInt("starter") == 1; 
        type = jsonPlanet.getInt("type"); 
        update = new Date(jsonPlanet.getLong("update") * 1000);
        user = jsonPlanet.getString("user"); 
        x = jsonPlanet.getInt("x"); 
        y = jsonPlanet.getInt("y"); 
    }

    public boolean isAbandoned() {
        return abandoned;
    }

    public boolean isBonus() {
        return bonus;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isStarter() {
        return starter;
    }

    public int getType() {
        return type;
    }

    public Date getUpdate() {
        return update;
    }

    public String getUser() {
        return user;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
