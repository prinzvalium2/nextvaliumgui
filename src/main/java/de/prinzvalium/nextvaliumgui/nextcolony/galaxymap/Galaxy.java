package de.prinzvalium.nextvaliumgui.nextcolony.galaxymap;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
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
    
    public static MultiValuedMap<GalaxyMapKey, GalaxyMapValue> loadGalaxyMap(int x, int y, int distanceMax) throws JSONException, IOException {
        return loadGalaxyMap(x, y, distanceMax * 2, distanceMax * 2);
    }
    
    public static MultiValuedMap<GalaxyMapKey, GalaxyMapValue> loadGalaxyMap(int x, int y, int width, int height) throws JSONException, IOException {
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
            
            MultiValuedMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = new ArrayListValuedHashMap<GalaxyMapKey, GalaxyMapValue>();
            
            for (int i = 0; i < jsonExplore.length(); i++) {
                JSONObject obj = jsonExplore.getJSONObject(i);
                GalaxyMapKey key = new GalaxyMapKey(obj.getInt("x"), obj.getInt("y"));
                
                GalaxyMapValueExplore galaxyMapValueExplore = new GalaxyMapValueExplore();
                galaxyMapValueExplore.x = obj.getInt("x");
                galaxyMapValueExplore.y = obj.getInt("y");
                galaxyMapValueExplore.start_x = obj.getInt("start_x");
                galaxyMapValueExplore.start_y = obj.getInt("start_y");
                galaxyMapValueExplore.user = obj.getString("user");
                galaxyMapValueExplore.type = obj.getString("type");
                try {
                    galaxyMapValueExplore.date = new Date(obj.getLong("date") * 1000);
                }
                catch (JSONException e) {
                    galaxyMapValueExplore.date = null;
                }
                try {
                    galaxyMapValueExplore.date_return = new Date(obj.getLong("date_return") * 1000);
                }
                catch (JSONException e) {
                    galaxyMapValueExplore.date_return = null;
                }
                galaxyMapValueExplore.mapShips = new HashMap<String, Integer>();
                JSONObject jsonShips = new JSONObject(obj.getString("ships"));
                for (Iterator<String> iterator = jsonShips.keys(); iterator.hasNext();) {
                    String ship = iterator.next();
                    int num = jsonShips.getInt(ship);
                    galaxyMapValueExplore.mapShips.put(ship, num);
                }
                
                GalaxyMapValue galaxyMapValue = new GalaxyMapValue("explore", obj.getString("user"), galaxyMapValueExplore);
                galaxyMap.put(key, galaxyMapValue);
                
                // TODO remove!!!!!!!!!! just testing
//                if (!galaxyMapValueExplore.type.contentEquals("explore")) {
//                    int size = galaxyMap.get(key).size();
//                    LOGGER.trace("put galaxyMap(" + key.getX()+ "/"+key.getY()+"-"+size+"): "+galaxyMapValue.getStatus()+"/"+galaxyMapValue.getUserName()+": GalaxyMapValueExplore="+galaxyMapValueExplore.user+"/"+galaxyMapValueExplore.type+": "+galaxyMapValueExplore.start_x+"/"+galaxyMapValueExplore.start_y+"->"+galaxyMapValueExplore.x+"/"+galaxyMapValueExplore.y);
//                }
            }
            for (int i = 0; i < jsonExplored.length(); i++) {
                JSONObject obj = jsonExplored.getJSONObject(i);
                GalaxyMapKey key = new GalaxyMapKey(obj.getInt("x"), obj.getInt("y"));
                GalaxyMapValue galaxyMapValue = new GalaxyMapValue("explored", obj.getString("user"));
                galaxyMap.put(key, galaxyMapValue);
            }
            for (int i = 0; i < jsonPlanets.length(); i++) {
                JSONObject obj = jsonPlanets.getJSONObject(i);
                GalaxyMapKey key = new GalaxyMapKey(obj.getInt("x"), obj.getInt("y"));
                GalaxyMapValue galaxyMapValue = new GalaxyMapValue("planet", null, obj.getString("id"), obj.getString("img"), key);
                galaxyMap.put(key, galaxyMapValue);
            }
            
            return galaxyMap;
        }
    
    public static void main(String[] args) throws JSONException, IOException {
        LOGGER.info("Start");
        Util.setProxy();
        LOGGER.info("Stop");
    }
}
