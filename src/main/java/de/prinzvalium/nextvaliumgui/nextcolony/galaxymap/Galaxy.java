package de.prinzvalium.nextvaliumgui.nextcolony.galaxymap;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class Galaxy {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Galaxy.class);
    
    public static JSONObject loadGalaxy(int x, int y, int width, int height) throws JSONException, IOException {
        LOGGER.trace("loadGalaxy()");
        LOGGER.debug("    LOADING the Galaxy Map. Please wait...");
        
       final String NEXTCOLONY_API_CMD_LOADGALAXY = "loadgalaxy?x=%d&y=%d&width=%d&height=%d"; 
       String apiCmd = String.format(NEXTCOLONY_API_CMD_LOADGALAXY, x, y, width, height);  
     
       return Util.getJSONObjectFromApiCommand(apiCmd);
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
    
    public static HashMap<GalaxyMapKey, GalaxyMapValue> loadGalaxyMap(int x, int y, int distanceMax) throws JSONException, IOException {
        return loadGalaxyMap(x, y, distanceMax * 2, distanceMax * 2);
    }
    
    public static HashMap<GalaxyMapKey, GalaxyMapValue> loadGalaxyMap(int x, int y, int width, int height) throws JSONException, IOException {
            LOGGER.trace("loadTheGalaxyMap()");
            
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
    
    public static void main(String[] args) throws JSONException, IOException {
        LOGGER.info("Start");
        Util.setProxy();
        LOGGER.info("Stop");
    }

}
