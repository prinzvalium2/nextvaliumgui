package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class Shipyard {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Shipyard.class);
    private Planet planet;
    private HashMap<String, ShipyardShip> shipyardShips;
    
    public Shipyard(Planet planet) {
        this.planet = planet;
        clean();
    }
    
    public void clean() {
        shipyardShips = null;
    }
    
    public HashMap<String, ShipyardShip> getShipyardShips() throws JSONException, IOException {
        if (shipyardShips == null)
            shipyardShips = loadShipyard(planet.getUserName(), planet.getId(), planet.getName());
        return shipyardShips;
    }

    public static HashMap<String, ShipyardShip> loadShipyard(String userName, String planetId, String planetName) throws JSONException, IOException {
        LOGGER.trace("loadShipyard()");
        LOGGER.debug("    LOADING shipyard of " + planetName + "("+ planetId + "). Please wait...");
        
        final String NEXTCOLONY_API_CMD_SHIPYARD = "shipyard?id=%s";
        String apiCmd = String.format(NEXTCOLONY_API_CMD_SHIPYARD, planetId);
        JSONArray jsonShipyard = Util.getJSONArrayFromApiCommand(apiCmd);

        HashMap<String, ShipyardShip> mapShipyard = new HashMap<String, ShipyardShip>();
        
        for (int i = 0; i < jsonShipyard.length(); i++) {
            ShipyardShip shipyardShip = new ShipyardShip(jsonShipyard.getJSONObject(i), userName, planetId, planetName );
            mapShipyard.put(shipyardShip.getType(), shipyardShip);
        }
        
        return mapShipyard;
    }    
    
    public static void main(String[] args) throws JSONException, IOException, SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        Util.setProxy();
        Shipyard.loadShipyard("pv5", "P-ZDFR0X681RK", "Alpha"); 
        //HashMap<String, ShipyardShip> mapShips = Shipyard.loadShipyard("patti.pizza", "P-ZGD7FULO3I8", "dummy123"); // "P-ZGD7FULO3I8" patti.pizza
        //HashMap<String, ShipyardShip> mapShips = Shipyard.loadShipyard("tonalddrump", "P-ZKQDQ16BX2O", "dummy123"); // "P-ZGD7FULO3I8" patti.pizza
//        HashMap<String, ShipyardShip> mapShips = Shipyard.loadShipyard("prinzvalium", "P-ZWZVQCFOS34", "06-Sandurz"); // "P-ZGD7FULO3I8" patti.pizza
//        ShipyardShip explorer = mapShips.get("explorership");
//        RessourceQuantities ressourceQuantities = new RessourceQuantities("P-ZKQDQ16BX2O", "tonalddrump");//.loadRessourceQuantites();
//        boolean build = explorer.buildIfPossible(ressourceQuantities);
//        System.out.println("Build: " + build);
    }
}
