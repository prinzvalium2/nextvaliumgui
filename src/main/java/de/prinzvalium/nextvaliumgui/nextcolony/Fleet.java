package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class Fleet {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Fleet.class);
    private String userName;
    private String planetName;
    private String planetId;
    private Vector<FleetShip> fleetShips;
    
    public Fleet(String userName, String planetName, String planetId) {
        this.userName = userName;
        this.planetName = planetName;
        this.planetId = planetId;
        clean();
    }
    
    public void clean() {
        fleetShips = null;
    }
    
    public Vector<FleetShip> getFleetShips() throws JSONException, IOException {
        if (fleetShips == null)
            fleetShips = loadFleet(userName, planetName, planetId);
        return fleetShips;
    }

    public int getNumberOfExplorersInShipyard() throws JSONException, IOException {
        LOGGER.trace("getNumberOfExplorersInShipyard()");
        return getNumberOfShipsInShipyard("explorership");
    }
    
    public int getNumberOfExp2InShipyard() throws JSONException, IOException {
        LOGGER.trace("getNumberOfExp2InShipyard()");
        return getNumberOfShipsInShipyard("explorership1");
    }
    
    public void reduceNumberOfExplorersInShipyard() throws JSONException, IOException {
        LOGGER.trace("reduceNumberOfExplorersInShipyard()");
        
        for (FleetShip s : getFleetShips()) {
            if (s.getType().equalsIgnoreCase("explorership")) {
                getFleetShips().remove(s);
                break;
            }
        }
    }
    
    public void reduceNumberOfExp2InShipyard() throws JSONException, IOException {
        LOGGER.trace("reduceNumberOfExp2InShipyard()");
        
        for (FleetShip s : getFleetShips()) {
            if (s.getType().equalsIgnoreCase("explorership1")) {
                getFleetShips().remove(s);
                break;
            }
        }
    }
    
    public int getNumberOfShipsInShipyard(String shipType) throws JSONException, IOException {
        
        int num = 0;
        for (FleetShip s : getFleetShips()) {
            if (s.getType().equalsIgnoreCase(shipType)) {
                num++;
            }
        }
        return num;
    }
    
    public int getCapacityOfShip(String shipType) throws JSONException, IOException {
        
        for (FleetShip ship : getFleetShips()) {
            if (ship.getType().equalsIgnoreCase(shipType))
                return ship.getCapacity();
        }
        return -1;
    }
    
    public HashMap<String, Integer> getNumberOfShipTypesInShipyard() throws JSONException, IOException {
        
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        
        for (FleetShip ship : getFleetShips()) {
            Integer numShips = map.get(ship.getType());
            if (numShips == null)
                map.put(ship.getType(), 1);
            else
                map.put(ship.getType(), ++numShips);
        }
        return map;
    }
    
   public static Vector<FleetShip> loadFleet(String userName, String planetName, String planetId) throws JSONException, IOException {
       LOGGER.trace("loadFleet()");
       LOGGER.debug("    LOADING the fleet of " + planetName + "(" + planetId + "). Please wait...");
        
        Vector<FleetShip> ships = new Vector<FleetShip>();
        
        final String NEXTCOLONY_API_CMD_LOADFLEET = "loadfleet?user=%s&planetid=%s";
        String apiCmd = String.format(NEXTCOLONY_API_CMD_LOADFLEET, userName, planetId);
        JSONArray jsonFleet = Util.getJSONArrayFromApiCommand(apiCmd);
        
        if (jsonFleet != null) {
            for (int i = 0; i < jsonFleet.length(); i++) {
                ships.add(new FleetShip(jsonFleet.getJSONObject(i)));
            }
        }
        
        return ships;
    }
}
