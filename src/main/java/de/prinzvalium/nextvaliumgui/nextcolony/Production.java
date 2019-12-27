package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class Production {

    private String planetId;
    private String userName;
    
    public Production(Planet planet) {
        this.planetId = planet.getId();
        this.userName = planet.getUserName();
    }
    
    Production(String planetId, String userName) {
        this.planetId = planetId;
        this.userName = userName;
    }
    
   public ProductionRessources loadProduction() throws JSONException, IOException {
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADPRODUCTION, planetId, userName);
        JSONObject jsonProduction = Util.getJSONObjectFromApiCommand(apiCmd);
        
        return new ProductionRessources(jsonProduction);
    }
   
   public static void main(String[] args) throws JSONException, IOException {
       Util.setProxy();
       ProductionRessources res = new Production("P-ZGD7FULO3I8", "patti.pizza").loadProduction();
       System.out.println(res.getCoal().getDepot());
   }
}
