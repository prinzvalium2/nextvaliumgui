package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Mission {
    
    private String user;
    private String type;
    private Date arrival;
    private Date returning;
    private Date start_date;
    private String toPlanetName;
    private String toPlanetId;
    private String fromPlanetName;
    private String fromPlanetId;
    private String cancel_trx;
    private String id;
    private int start_x;
    private int start_y;
    private int end_x;
    private int end_y;
    private String result;
    private int ships_total;
    
    Mission(JSONObject jsonMission) {
        
        id = jsonMission.getString("id"); 
        user = jsonMission.getString("user"); 
        
        start_x = jsonMission.getInt("start_x");
        start_y = jsonMission.getInt("start_y");
        end_x = jsonMission.getInt("end_x");
        end_y = jsonMission.getInt("end_y");
        
//        try {
//            result = jsonMission.getInt("result");
//        }
//        catch (JSONException e) {
//            result = -1;
//        }
        try {
            result = jsonMission.getString("result");
        }
        catch (JSONException e) {
            result = null;
        }
        
        
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
            start_date = new Date(jsonMission.getLong("start_date") * 1000);
        }
        catch (JSONException e) {
            start_date = null;
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
            fromPlanetId = jsonFromPlanet.getString("id");
        }
        catch(JSONException e) {
            fromPlanetName = "<not set>";
        }
        try {
            JSONObject jsonShips = jsonMission.getJSONObject("ships");
            ships_total = jsonShips.getInt("total");
        }
        catch(JSONException e) {
            ships_total = -1;
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

    public Date getStart_date() {
        return start_date;
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

    public String getFromPlanetId() {
        return fromPlanetId;
    }

    public String getCancel_trx() {
        return cancel_trx;
    }

    public int getStart_x() {
        return start_x;
    }

    public int getStart_y() {
        return start_y;
    }

    public int getEnd_x() {
        return end_x;
    }

    public int getEnd_y() {
        return end_y;
    }

    public String getResult() {
        return result;
    }

    public int getShips_total() {
        return ships_total;
    }

    @Override
    public String toString() {
        return getType();
    }

    public String getId() {
        return id;
    }
}
