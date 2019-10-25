package de.prinzvalium.nextvaliumgui.lib;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.steem.SteemUtil;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class CustomJson {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJson.class);
    
    public static void deployShipsOfPlanet(HashMap<String, Integer> mapNumberOfShipTypes, String userName, String planetId, Planet planetTo) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        
        JsonObject jsonCommand = new JsonObject();
        
        // Ships
        JsonObject jsonShips = new JsonObject();
        mapNumberOfShipTypes.forEach((shipType, num) -> jsonShips.addProperty(shipType, num.intValue()));
        jsonCommand.add("tr_var1", jsonShips); 
        
        // Destination
        jsonCommand.addProperty("tr_var2", planetTo.getPosX());
        jsonCommand.addProperty("tr_var3", planetTo.getPosY());

        // Ressources
        jsonCommand.addProperty("tr_var4", 0); 
        jsonCommand.addProperty("tr_var5", 0);
        jsonCommand.addProperty("tr_var6", 0);
        jsonCommand.addProperty("tr_var7", 0);
        
        // Planet-ID from
        jsonCommand.addProperty("tr_var8", planetId);
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", userName);
        jsonObject.addProperty("type", "deploy");
        jsonObject.add("command",  jsonCommand);
        
        broadcastJSONObjectToSteem(jsonObject);
    }
    
    public boolean fullAttack(HashMap<String, Integer> map, String userName, String planetId, int x, int y, boolean fast) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        
        String[] shipTypeAndPosition;
        
        if (fast) {
            shipTypeAndPosition = new String[] {
                    "corvette1",
                    "corvette",
                    };
        }
        else {
            shipTypeAndPosition = new String[] {
                    "transportship",
                    "carrier",
                    "carrier1",
                    "destroyer",
                    "destroyer1",
                    "frigate",
                    "frigate1",
                    "corvette1",
                    "corvette",
                    };
        }
        
        int pos = 1;
        
        // Ships
        JsonObject jsonShips = new JsonObject();
        
        for (String shipType : shipTypeAndPosition) {
            
            Integer num = map.get(shipType);
            if (num == null  || num <= 0)
                continue;
            
            JsonObject posAndNum = new JsonObject();
            posAndNum.addProperty("pos", pos++);
            posAndNum.addProperty("n",  num);
            jsonShips.add(shipType, posAndNum);
        }
        
        if (jsonShips.size() < 1) {
            LOGGER.info("    No attack -> no ships in shipyard");
            return false;
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
        jsonObject.addProperty("type", "attack");
        jsonObject.add("command",  jsonCommand);
        
        broadcastJSONObjectToSteem(jsonObject);
        
        LOGGER.info("    fullAttack: " + (fast ? "fast" : "slow"));
        return true;
    }
    
    public static void broadcastJSONObjectToSteem(JsonObject jsonObject) throws SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
        SteemUtil.broadcastJSONStringWithRetry(jsonObject.toString(), "nextcolony");
    }
}
