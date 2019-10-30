package de.prinzvalium.nextvaliumgui.nextcolony;

import org.json.JSONObject;

public class FleetShip {
    
    //long busy;
    private String type;
    private int capacity;
    private double consumption;
    //int hor;
    //int ver;

    FleetShip(JSONObject jsonShip) {
        //busy = jsonShip.getLong("busy");
        type = jsonShip.getString("type");
        capacity = jsonShip.getInt("capacity");
        consumption = jsonShip.getDouble("cons");
        
        //hor = jsonShip.getInt("hor");
        //ver = jsonShip.getInt("ver");
    }

    public int getCapacity() {
        return capacity;
    }

    public String getType() {
        return type;
    }

    public double getConsumption() {
        return consumption;
    }
}
