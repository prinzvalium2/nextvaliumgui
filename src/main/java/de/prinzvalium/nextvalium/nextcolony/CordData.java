package de.prinzvalium.nextvalium.nextcolony;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import de.prinzvalium.nextvalium.lib.Util;

public class CordData {
    
    private String userName;
   
    public CordData(int x, int y) throws JSONException, IOException {
        loadCordData(x, y);
    }

    public void loadCordData(int x, int y) throws JSONException, IOException {
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADCORDDATA, x, y);
        JSONObject jsonPlanet = Util.getJSONObjectFromApiCommand(apiCmd);
        
        userName = jsonPlanet.getString("user");
    }

    public String getUserName() {
        return userName;
    }
}
