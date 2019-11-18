package de.prinzvalium.nextvaliumgui.lib;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails.TableFleetValues;
import de.prinzvalium.nextvaliumgui.nextcolony.Resources;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class CustomJson {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJson.class);
    
    public static void deployShipsOfPlanet(HashMap<String, Integer> mapNumberOfShipTypes, String userName, String planetId, int x, int y, Resources resources) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        
        SteemUtil.setDefaultAccount(userName);
        
        JsonObject jsonCommand = new JsonObject();
        
        // Ships
        JsonObject jsonShips = new JsonObject();
        mapNumberOfShipTypes.forEach((shipType, num) -> jsonShips.addProperty(shipType, num.intValue()));
        jsonCommand.add("tr_var1", jsonShips); 
        
        // Destination
        jsonCommand.addProperty("tr_var2", x);
        jsonCommand.addProperty("tr_var3", y);

        // Ressources
        jsonCommand.addProperty("tr_var4", resources.coal); 
        jsonCommand.addProperty("tr_var5", resources.ore);
        jsonCommand.addProperty("tr_var6", resources.copper);
        jsonCommand.addProperty("tr_var7", resources.uranium);
        
        // Planet-ID from
        jsonCommand.addProperty("tr_var8", planetId);
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", userName);
        jsonObject.addProperty("type", "deploy");
        jsonObject.add("command",  jsonCommand);
        
        broadcastJSONObjectToSteem(jsonObject);
    }
    
    public static void transportToPlanet(HashMap<String, Integer> mapNumberOfShipTypes, String userName, String planetId, int x, int y, Resources resources) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        
        SteemUtil.setDefaultAccount(userName);
        
        JsonObject jsonCommand = new JsonObject();
        
        // Ships
        JsonObject jsonShips = new JsonObject();
        mapNumberOfShipTypes.forEach((shipType, num) -> jsonShips.addProperty(shipType, num.intValue()));
        jsonCommand.add("tr_var1", jsonShips); 
        
        // Planet-ID from
        jsonCommand.addProperty("tr_var2", planetId);
        
        // Destination
        jsonCommand.addProperty("tr_var3", x);
        jsonCommand.addProperty("tr_var4", y);

        // Ressources
        jsonCommand.addProperty("tr_var5", resources.coal); 
        jsonCommand.addProperty("tr_var6", resources.ore);
        jsonCommand.addProperty("tr_var7", resources.copper);
        jsonCommand.addProperty("tr_var8", resources.uranium);
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", userName);
        jsonObject.addProperty("type", "transport");
        jsonObject.add("command",  jsonCommand);
        
        broadcastJSONObjectToSteem(jsonObject);
    }
    
    public static void explore(String userName, String planetId, int x, int y, String shipType) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        
        SteemUtil.setDefaultAccount(userName);
        
        JsonObject jsonCommand = new JsonObject();
        jsonCommand.addProperty("tr_var1", planetId); 
        jsonCommand.addProperty("tr_var2", x);
        jsonCommand.addProperty("tr_var3", y);
        jsonCommand.addProperty("tr_var4", shipType);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", userName);
        jsonObject.addProperty("type", "explorespace");
        jsonObject.add("command",  jsonCommand);
        
        broadcastJSONObjectToSteem(jsonObject);
    }
    
    public static void fightingAction(String type, HashMap<String, TableFleetValues> map, String userName, String planetId, int x, int y) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        
        SteemUtil.setDefaultAccount(userName);
        
        int pos = 1;
        
        // Ships
        JsonObject jsonShips = new JsonObject();
        
        while (map.size() > 0 ) {
            Entry<String, TableFleetValues> smallestEntry = null;
            for (Entry<String, TableFleetValues> entry : map.entrySet()) {
                if (smallestEntry == null) {
                    smallestEntry = entry;
                    continue;
                }
                
                if (entry.getValue().getPosition() == null)
                    continue;
                
                if (entry.getValue().getPosition() < smallestEntry.getValue().getPosition())
                    smallestEntry = entry;
            }
            
            JsonObject posAndNum = new JsonObject();
            posAndNum.addProperty("pos", pos++);
            posAndNum.addProperty("n",  smallestEntry.getValue().getNumberFleet());
            jsonShips.add(smallestEntry.getKey(), posAndNum);
            map.remove(smallestEntry.getKey());
        }
        
        // Command
        JsonObject jsonCommand = new JsonObject();
        
        jsonCommand.add("tr_var1", jsonShips); 
        
        // Destination
        jsonCommand.addProperty("tr_var2", x);
        jsonCommand.addProperty("tr_var3", y);

        // Planet-ID from
        jsonCommand.addProperty("tr_var4", planetId);
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", userName);
        jsonObject.addProperty("type", type);
        jsonObject.add("command",  jsonCommand);
        
        broadcastJSONObjectToSteem(jsonObject);
    }
    
    public static void renamePlanet(String userName, String planetId, String planetName) throws NextValiumException, SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        LOGGER.trace("renamePlanet()");
        
        SteemUtil.setDefaultAccount(userName);
        
        // Command
        JsonObject jsonCommand = new JsonObject();
        jsonCommand.addProperty("tr_var1", planetId); 
        jsonCommand.addProperty("tr_var2", planetName); 
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", userName);
        jsonObject.addProperty("type", "renameplanet");
        jsonObject.add("command",  jsonCommand);
        
        broadcastJSONObjectToSteem(jsonObject);
    }
    
    public static void broadcastJSONObjectToSteem(JsonObject jsonObject) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        SteemUtil.broadcastJSONStringWithRetry(jsonObject.toString(), "nextcolony");
    }
}
