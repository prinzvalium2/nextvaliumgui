package de.prinzvalium.nextvaliumgui.nextcolony;

import org.json.JSONObject;

public class FleetShip {
    
    //long busy;
    private String type;
    private int capacity;
    //int hor;
    //int ver;

    FleetShip(JSONObject jsonShip) {
        //busy = jsonShip.getLong("busy");
        type = jsonShip.getString("type");
        capacity = jsonShip.getInt("capacity");
        //hor = jsonShip.getInt("hor");
        //ver = jsonShip.getInt("ver");
    }

    public int getCapacity() {
        return capacity;
    }

    public String getType() {
        return type;
    }
}
