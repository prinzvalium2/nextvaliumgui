package de.prinzvalium.nextvaliumgui.nextcolony;

import org.json.JSONObject;

public class FleetShip {
    
    //long busy;
    String type;
    //int hor;
    //int ver;

    FleetShip(JSONObject jsonShip) {
        //busy = jsonShip.getLong("busy");
        type = jsonShip.getString("type");
        //hor = jsonShip.getInt("hor");
        //ver = jsonShip.getInt("ver");
    }
}
