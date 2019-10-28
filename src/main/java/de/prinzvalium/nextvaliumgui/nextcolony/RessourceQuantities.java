package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;

public class RessourceQuantities {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RessourceQuantities.class);
    private String planetId;
    private String planetName;
    private RessourceQuantitiesRessources ressourceQuantitiesRessources;
    
    public RessourceQuantities(Planet planet) {
        planetId = planet.getId();
        planetName = planet.getName();
    }

    public RessourceQuantities(String planetName, String planetId) {
        this.planetId = planetId;
        this.planetName = planetName;
        clean();
    }
    
    public void clean() {
        ressourceQuantitiesRessources = null;
    }
    
    public RessourceQuantitiesRessources getRessourceQuantitiesRessources() throws JSONException, IOException {
        if (ressourceQuantitiesRessources == null)
            ressourceQuantitiesRessources = loadRessourceQuantites(planetName, planetId);

        return ressourceQuantitiesRessources;
    }

    public static RessourceQuantitiesRessources loadRessourceQuantites(String planetName, String planetId) throws JSONException, IOException {
        LOGGER.trace("loadRessourceQuantites()");
        LOGGER.debug("    LOADING ressource quantities of " + planetName + "(" + planetId + "). Please wait...");
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADRESSOURCEQUANTITIES, planetId);
        JSONObject jsonRessourceQuantites = Util.getJSONObjectFromApiCommand(apiCmd);
        
        return new RessourceQuantitiesRessources(jsonRessourceQuantites);
    }

    public static void main(String[] args) throws JSONException, IOException {
        Util.setProxy();
        RessourceQuantities.loadRessourceQuantites("hugo", "P-ZGD7FULO3I8"); // patti.pizza
    }
}
