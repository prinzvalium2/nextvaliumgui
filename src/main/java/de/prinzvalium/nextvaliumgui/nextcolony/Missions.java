package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class Missions {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Missions.class);

    public static Vector<Mission> loadAllActiveUserMissions(String userName) throws JSONException, IOException {
        return loadMissionsOfUserWithFilter(userName, "&active=1&onlyuser=1");
    }
    
    public static Vector<Mission> loadAllActiveMissions(String userName) throws JSONException, IOException {
        return loadMissionsOfUserWithFilter(userName, "&active=1&onlyuser=0");
    }
    
    public static Vector<Mission> loadAllActiveMissionsOfPlanet(String userName, String planetId) throws JSONException, IOException {
        return loadMissionsOfUserWithFilter(userName, "&active=1&onlyuser=0&planetid=" + planetId);
    }
    
    public static Vector<Mission> loadAllActiveUserMissionsOfPlanet(String userName, String planetId) throws JSONException, IOException {
        return loadMissionsOfUserWithFilter(userName, "&active=1&onlyuser=1&planetid=" + planetId);
    }

    public static Vector<Mission> loadAllActiveUserMissionsStartedFromPlanet(String userName, String planetId) throws JSONException, IOException {
        Vector<Mission> missions = loadAllActiveUserMissionsOfPlanet(userName, planetId);
        Vector<Mission> activeUserMissionsStartedFromPlanet = new Vector<Mission>();
        for (Mission mission : missions) {
            if (mission.getFromPlanetId().equalsIgnoreCase(planetId))
                activeUserMissionsStartedFromPlanet.add(mission);
        }
        return activeUserMissionsStartedFromPlanet;
    }

    public static Vector<Mission> loadAllUserMissionsOfPlanet(String userName, String planetId) throws JSONException, IOException {
        return loadMissionsOfUserWithFilter(userName, "&onlyuser=1&planetid=" + planetId);
    }

    public static Vector<Mission> loadMissionsOfUserWithFilter(String userName, String filter) throws JSONException, IOException {
        LOGGER.trace("loadMissionsOfUserWithFilter()");
        LOGGER.debug("    LOADING missions of " + userName + " (" + filter + "). Please wait...");
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADMISSIONS + filter, userName);
        JSONArray jsonMissions = Util.getJSONArrayFromApiCommand(apiCmd);
        
        Vector<Mission> missions = new Vector<Mission>();
        
        for (int i = 0; i < jsonMissions.length(); i++) {
            missions.add(new Mission(jsonMissions.getJSONObject(i)));
        }
        
        return missions;
    }
    
    public static void main(String[] argc ) throws JSONException, IOException {
        Util.setProxy();
    }
}
