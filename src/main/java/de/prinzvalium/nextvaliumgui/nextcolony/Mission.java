package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Mission {
    
    private String user;
    private String type;
    private Date arrival;
    private Date returning;
    private String toPlanetName;
    private String toPlanetId;
    private String fromPlanetName;
    private String cancel_trx;
    
    Mission(JSONObject jsonMission) {
        
        user = jsonMission.getString("user"); 
        
        try {
            cancel_trx = jsonMission.getString("cancel_trx");
        }
        catch (JSONException e) {
            cancel_trx = null;
        }
        
        type = jsonMission.getString("type"); 
        arrival = new Date(jsonMission.getLong("arrival") * 1000);
        try {
            returning = new Date(jsonMission.getLong("return") * 1000);
        }
        catch (JSONException e) {
            returning = null;
        }
        
        try {
            JSONObject jsonToPlanet = jsonMission.getJSONObject("to_planet");
            toPlanetName = jsonToPlanet.getString("name");
            toPlanetId = jsonToPlanet.getString("id");
        }
        catch(JSONException e) {
            toPlanetName = "<not set>";
            toPlanetId = "<not set>";
        }
        
        try {
            JSONObject jsonFromPlanet = jsonMission.getJSONObject("from_planet");
            fromPlanetName = jsonFromPlanet.getString("name");
        }
        catch(JSONException e) {
            fromPlanetName = "<not set>";
        }
    }
    
    Mission(String userName, String type) {
        this.user = userName;
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public Date getArrival() {
        return arrival;
    }
    
    public Date getReturning() {
        return returning;
    }

    public String getToPlanetName() {
        return toPlanetName;
    }

    public String getToPlanetId() {
        return toPlanetId;
    }

    public String getFromPlanetName() {
        return fromPlanetName;
    }

    public String getCancel_trx() {
        return cancel_trx;
    }
}
