package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class Planets {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(Planets.class);
    private static HashMap<String, Planet> mapAllPlanets = null;
	
    public static HashMap<String, Planet> getAllPlanets() throws JSONException, IOException {
        if (mapAllPlanets == null)
            mapAllPlanets = loadAllPlanets();
        return mapAllPlanets;
    }

    private static HashMap<String, Planet> loadAllPlanets() throws JSONException, IOException {
        LOGGER.trace("loadAllPlanets()");
        
        HashMap<String, Planet>  mapPlanets = new HashMap<String, Planet>();
        
        int from = 0;
        
        while (true) {
            
            String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADALLPLANETS, Integer.toString(from), Integer.toString(from+999));
            JSONObject jsonObject = Util.getJSONObjectFromApiCommand(apiCmd);
            JSONArray jsonPlanets = jsonObject.getJSONArray("planets");
            
            if (jsonPlanets == null || jsonPlanets.isEmpty())
                break;
            
            for (int i = 0; i < jsonPlanets.length(); i++) {
                Planet p = new Planet(jsonPlanets.getJSONObject(i));
                mapPlanets.put(p.getId(), p);
            }
            
            from += 1000;
        }
        
        return mapPlanets;
    }
    
    public static HashMap<String, Planet> loadUserPlanets(String userName) throws JSONException, IOException {
        LOGGER.trace("loadPlanets()");
        LOGGER.debug("    LOADING planets of user " + userName);
        
        HashMap<String, Planet>  mapPlanets = new HashMap<String, Planet>();
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADUSERPLANETS, userName);
        JSONObject jsonObject = Util.getJSONObjectFromApiCommand(apiCmd);
        JSONArray jsonPlanets = jsonObject.getJSONArray("planets");
        
        for (int i = 0; i < jsonPlanets.length(); i++) {
            Planet p = new Planet(jsonPlanets.getJSONObject(i));
            mapPlanets.put(p.getId(), p);
        }
        return mapPlanets;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Util.setProxy();
    }
}
