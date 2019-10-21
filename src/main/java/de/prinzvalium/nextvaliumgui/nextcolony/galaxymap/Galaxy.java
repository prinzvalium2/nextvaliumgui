package de.prinzvalium.nextvaliumgui.nextcolony.galaxymap;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class Galaxy {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Galaxy.class);
    
    public static void explore(String userName, String planetId, int x, int y) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        explore(userName, planetId, x, y, "explorership");
    }
        
    public static void explore(String userName, String planetId, int x, int y, String shipType) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        
        JSONObject jsonCommand = new JSONObject();
        jsonCommand.put("tr_var1", planetId); 
        jsonCommand.put("tr_var2", x);
        jsonCommand.put("tr_var3", y);
        jsonCommand.put("tr_var4", shipType);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userName);
        jsonObject.put("type", "explorespace");
        jsonObject.put("command",  jsonCommand);
        
        Util.broadcastJSONObjectToSteem(jsonObject);
    }
        
    public static JSONObject loadGalaxy(int x, int y, int width, int height) throws JSONException, IOException {
        LOGGER.trace("loadGalaxy()");
        LOGGER.debug("    LOADING the Galaxy Map. Please wait...");
        
       final String NEXTCOLONY_API_CMD_LOADGALAXY = "loadgalaxy?x=%d&y=%d&width=%d&height=%d"; 
       String apiCmd = String.format(NEXTCOLONY_API_CMD_LOADGALAXY, x, y, width, height);  
     
       return Util.getJSONObjectFromApiCommand(apiCmd);
    }
    
    public static void waitForGalaxyStatusNotEmpty(GalaxyMapKey key, long maxWaitTime) throws JSONException, IOException {
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, (int)maxWaitTime/1000);
        
        LOGGER.info("Waiting 10 seconds for nextcolony getting the explore command from steem (max. " + (maxWaitTime / 60000) + " minutes)");
        Util.sleep(10000);
        LOGGER.info("Now checking the galaxy status for <not empty>");
        
        while (true) {
            
            JSONObject jsonObject = loadGalaxy(key.getX(), key.getY(), 0, 0);
            
            HashMap<GalaxyMapKey, String> galaxyMap = new HashMap<GalaxyMapKey, String>();
            
            galaxyMap = putGalaxyIntoHashMap(galaxyMap, jsonObject);
            String statusGalaxy = galaxyMap.get(key);
            
            if (statusGalaxy != null) {
                LOGGER.info("Galaxy status: " + statusGalaxy);
                return;
            }
            
            LOGGER.info("Waiting additional 5 seconds for galaxy status <not empty>");
            Util.sleep(5000);
            
            if (cal.before(Calendar.getInstance())) {
                LOGGER.info("Waiting for galaxy status not empty -> TIMEOUT!");
                break;
            }
        }
    }
    
    public static HashMap<GalaxyMapKey, String> putGalaxyIntoHashMap(HashMap<GalaxyMapKey, String> mapGalaxy, JSONObject jsonGalaxy) {
        
        JSONArray jsonExplore = jsonGalaxy.getJSONArray("explore");
        JSONArray jsonExplored = jsonGalaxy.getJSONArray("explored");
        JSONArray jsonPlanets = jsonGalaxy.getJSONArray("planets");
        
        for (int i = 0; i < jsonExplore.length(); i++) {
            JSONObject obj = jsonExplore.getJSONObject(i);
            mapGalaxy.put(new GalaxyMapKey(obj.getInt("x"), obj.getInt("y")), "explore");
        }
        for (int i = 0; i < jsonExplored.length(); i++) {
            JSONObject obj = jsonExplored.getJSONObject(i);
            mapGalaxy.put(new GalaxyMapKey(obj.getInt("x"), obj.getInt("y")), "explored");
        }
        for (int i = 0; i < jsonPlanets.length(); i++) {
            JSONObject obj = jsonPlanets.getJSONObject(i);
            mapGalaxy.put(new GalaxyMapKey(obj.getInt("x"), obj.getInt("y")), "planet");
        }
        
        return mapGalaxy;
    }
    
    public static void main(String[] args) throws JSONException, IOException {
        LOGGER.info("Start");
        Util.setProxy();
        Galaxy.waitForGalaxyStatusNotEmpty(new GalaxyMapKey(0, 1), 30000);
        LOGGER.info("Stop");
    }

    public static HashMap<GalaxyMapKey, GalaxyMapValue> loadGalaxyMap(int x, int y, int distanceMax) throws JSONException, IOException {
        return loadGalaxyMap(x, y, distanceMax * 2, distanceMax * 2);
    }
    
    public static HashMap<GalaxyMapKey, GalaxyMapValue> loadGalaxyMap(int x, int y, int width, int height) throws JSONException, IOException {
            //LOGGER.trace("loadTheGalaxyMap()");
            
            JSONObject jsonObject = loadGalaxy(x, y, width, height);
            
    //        JSONObject jsonArea = jsonObject.getJSONObject("area");
            JSONArray jsonExplore = jsonObject.getJSONArray("explore");
            JSONArray jsonExplored = jsonObject.getJSONArray("explored");
            JSONArray jsonPlanets = jsonObject.getJSONArray("planets");
            
    //        int yMin = jsonArea.getInt("ymin");
    //        int yMax = jsonArea.getInt("ymax");
    //        int xMin = jsonArea.getInt("xmin");
    //        int xMax = jsonArea.getInt("xmax");
            
            HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = new HashMap<GalaxyMapKey, GalaxyMapValue>();
            
            for (int i = 0; i < jsonExplore.length(); i++) {
                JSONObject obj = jsonExplore.getJSONObject(i);
                GalaxyMapKey key = new GalaxyMapKey(obj.getInt("x"), obj.getInt("y"));
                GalaxyMapValue value = new GalaxyMapValue("explore", obj.getString("user"));
                galaxyMap.put(key, value);
            }
            for (int i = 0; i < jsonExplored.length(); i++) {
                JSONObject obj = jsonExplored.getJSONObject(i);
                GalaxyMapKey key = new GalaxyMapKey(obj.getInt("x"), obj.getInt("y"));
                GalaxyMapValue value = new GalaxyMapValue("explored", obj.getString("user"));
                galaxyMap.put(key, value);
            }
            for (int i = 0; i < jsonPlanets.length(); i++) {
                JSONObject obj = jsonPlanets.getJSONObject(i);
                GalaxyMapKey key = new GalaxyMapKey(obj.getInt("x"), obj.getInt("y"));
                GalaxyMapValue value = new GalaxyMapValue("planet", null, obj.getString("id"), obj.getString("img"), key);
                galaxyMap.put(key, value);
            }
            
            return galaxyMap;
        }
}
