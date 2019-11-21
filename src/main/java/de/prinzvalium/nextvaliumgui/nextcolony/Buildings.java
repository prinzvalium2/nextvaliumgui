package de.prinzvalium.nextvaliumgui.nextcolony;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.Building;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class Buildings {

    private static final Logger LOGGER = LoggerFactory.getLogger(Buildings.class);
    private String planetId;
    private String userName;
    private String planetName;
    HashMap<String, Building> buildings;
    
   public Buildings(Planet planet) {
        this.planetId = planet.getId();
        this.userName = planet.getUserName();
        this.planetName = planet.getName();
        clean();
    }
   
    public Buildings(String planetId, String planetName, String userName) {
        this.planetId = planetId;
        this.userName = userName;
        this.planetName = planetName;
    }   
    
    public void clean() {
        buildings = null;
    }
     
    public HashMap<String, Building> getBuildings() throws JSONException, IOException {
        if (buildings == null)
            buildings = loadBuildings(this);
        return buildings;
    }
    
    public Building getBuilding(String building) throws JSONException, IOException {
        return getBuildings().get(building);
    }
    
    private boolean checkBuildingMine(Building mineToBuild, Building mineToCompareWith) {
        
        String fromTo = mineToBuild.getName() + ": " + mineToBuild.getCurrent() + "->" + (mineToBuild.getCurrent() + 1) + ". ";
        String tryingToUpgrade = planetName + ": Trying to upgrade " + fromTo;
        String notUpgrading = planetName + ": Not upgrading " + fromTo;
        String levelOfTheMineToCompare = "Level of " + mineToCompareWith.getName() + ": " + mineToCompareWith.getCurrent();
        
        if (mineToBuild.getCurrent() < mineToCompareWith.getCurrent()) {
            LOGGER.info(tryingToUpgrade + levelOfTheMineToCompare);
            return true;
        }
        
        if (mineToBuild.getCurrent() == mineToCompareWith.getCurrent() && mineToCompareWith.getBusy().after(new Date())) {
            LOGGER.info(tryingToUpgrade + levelOfTheMineToCompare + "->" + (mineToCompareWith.getCurrent() + 1));
            return true;
        }
        
        LOGGER.info(notUpgrading + levelOfTheMineToCompare);
        return false;
    }
    
    private boolean checkBuildingShipyard(Building shipyard, Building uraniumMine) {

        String fromTo = shipyard.getName() + ": " + shipyard.getCurrent() + "->" + (shipyard.getCurrent() + 1) + ". ";
        String tryingToUpgrade = planetName + ": Trying to upgrade " + fromTo;
        String notUpgrading = planetName + ": Not upgrading " + fromTo;
        String levelOfTheMineToCompare = "Level of " + uraniumMine.getName() + ": " + uraniumMine.getCurrent();
//        String uraniumMineReachedMax = "Uranium mine has reached max level " + uraniumMine.getCurrent() + "/" + uraniumMine.getSkill();
        
        if (shipyard.getCurrent() >= 18) {
            LOGGER.info(notUpgrading + "Max level 18 reached.");
            return false;
        }
        
        // Try to build mines first. Uranmine 20 -> Shipyard 18
        if (shipyard.getCurrent() < (uraniumMine.getCurrent() - 2)) {
            LOGGER.info(tryingToUpgrade + levelOfTheMineToCompare);
            return true;
        }
        
//        if (uraniumMine.getCurrent() < uraniumMine.getSkill() ) {
//            LOGGER.info(notUpgrading + levelOfTheMineToCompare);
//            return false;
//        }
//        
//        LOGGER.info(tryingToUpgrade + uraniumMineReachedMax);
//        return true;
        
        LOGGER.info(notUpgrading + levelOfTheMineToCompare);
        return false;
    }
    
    private boolean checkBuildingBase(Building base, Building uraniumMine) {

        String fromTo = base.getName() + ": " + base.getCurrent() + "->" + (base.getCurrent() + 1) + ". ";
        String tryingToUpgrade = planetName + ": Trying to upgrade " + fromTo;
        String notUpgrading = planetName + ": Not upgrading " + fromTo;
        String levelOfTheMineToCompare = "Level of " + uraniumMine.getName() + ": " + uraniumMine.getCurrent();
//        String uraniumMineReachedMax = "Uranium mine has reached max level " + uraniumMine.getCurrent() + "/" + uraniumMine.getSkill();
        
        if (base.getCurrent() >= 10) {
            LOGGER.info(notUpgrading + "Max level 10 reached.");
            return false;
        }
        
        // Try to build mines first. Uranmine 20 -> Shipyard 18
        if (base.getCurrent() < (uraniumMine.getCurrent() - 2)) {
            LOGGER.info(tryingToUpgrade + levelOfTheMineToCompare);
            return true;
        }
        
//        if (uraniumMine.getCurrent() < uraniumMine.getSkill() ) {
//            LOGGER.info(notUpgrading + levelOfTheMineToCompare);
//            return false;
//        }
//        
//        LOGGER.info(tryingToUpgrade + uraniumMineReachedMax);
//        return true;
        
        LOGGER.info(notUpgrading + levelOfTheMineToCompare);
        return false;
    }
    
    private boolean checkBuildingDepots(Building depotToBuild, Building mine, int neededRessource, HashMap<String, Building> bs) {

        String fromTo = depotToBuild.getName() + ": " + depotToBuild.getCurrent() + "->" + (depotToBuild.getCurrent() + 1) + ". ";
        String tryingToUpgrade = planetName + ": Trying to upgrade " + fromTo;
        String notUpgrading = planetName + ": Not upgrading " + fromTo;
       
        if (neededRessource > depotToBuild.getCurRate()) {
            LOGGER.info(tryingToUpgrade + "Larger depot needed to build " + mine.getName() + ": " + depotToBuild.getCurRate() + "/" + neededRessource);
            return true;
        }
        
        LOGGER.info(notUpgrading + "No larger depots needed for " + mine.getName());
        return false;
        
//        if (!areAllMinesBuildToMax(bs)) {
//            LOGGER.info(notUpgrading + "First building mines to max skill.");
//            return false;
//        }
//        
//        if (!isShipyardBuildToMax(bs)) {
//            LOGGER.info(notUpgrading + "First builing shipyard to max skill.");
//            return false;
//        }
//        
//        LOGGER.info(tryingToUpgrade + "All mines and shipyard have reached max level.");
//        return true;
    }
    
    private boolean checkBuildingBunker(Building bunker, HashMap<String, Building> bs) {

        String fromTo = bunker.getName() + ": " + bunker.getCurrent() + "->" + (bunker.getCurrent() + 1) + ". ";
        String tryingToUpgrade = planetName + ": Trying to upgrade " + fromTo;
        String notUpgrading = planetName + ": Not upgrading " + fromTo;

        if (bunker.getCurrent() < bs.get("copperdepot").getCurrent() - 5) {
            LOGGER.info(tryingToUpgrade + "Copperdepot reached level: " + bs.get("copperdepot").getCurrent());
            return true;
        }
        
        if (!areAllMinesBuildToMax(bs)) {
           LOGGER.info(notUpgrading + "First building mines to max skill.");
           return false;
        }
        
        if (!isShipyardBuildToMax(bs)) {
            LOGGER.info(notUpgrading + "First builing shipyard to max skill.");
            return false;
        }
        
        if (!areAllDepotsBuildToMax(bs)) {
            LOGGER.info(notUpgrading + "First building all depots to max skill.");
            return false;     
        }
        
        LOGGER.info(tryingToUpgrade + "All mines, shipyard and depots have reached max level.");
        return true;
    }
    
    private boolean checkBuildingShield(Building shield, HashMap<String, Building> bs) {

        String fromTo = shield.getName() + ": " + shield.getCurrent() + "->" + (shield.getCurrent() + 1) + ". ";
        String tryingToUpgrade = planetName + ": Trying to upgrade " + fromTo;
        String notUpgrading = planetName + ": Not upgrading " + fromTo;

        if (shield.getCurrent() < bs.get("bunker").getCurrent() - 5) {
            LOGGER.info(tryingToUpgrade + "Bunker reached level: " + bs.get("bunker").getCurrent());
            return true;
        }
        
        if (!areAllMinesBuildToMax(bs)) {
            LOGGER.info(notUpgrading + "First building mines to max skill.");
            return false;
        }
        
        if (!isShipyardBuildToMax(bs)) {
            LOGGER.info(notUpgrading + "First building shipyard to max skill.");
            return false;
        }
        
        if (!areAllDepotsBuildToMax(bs)) {
            LOGGER.info(notUpgrading + "First building all depots to max skill.");
            return false;        
        }
        
        if (!isBunkerBuildToMax(bs)) {
            LOGGER.info(notUpgrading + "First building bunker to max skill.");
            return false;
        }
        
        LOGGER.info(tryingToUpgrade + "All mines, shipyard, depots and bunker have reached max level.");
        return true;
    }
    
    
    private boolean areAllMinesBuildToMax(HashMap<String, Building> bs) {        
        if (bs.get("coppermine").getSkill() > bs.get("coppermine").getCurrent())
            return false;
        if (bs.get("coalmine").getSkill() > bs.get("coalmine").getCurrent())
            return false;
        if (bs.get("oremine").getSkill() > bs.get("oremine").getCurrent())
            return false;
        if (bs.get("uraniummine").getSkill() > bs.get("uraniummine").getCurrent())
            return false;
        
        return true;
    }
    
    private boolean isShipyardBuildToMax(HashMap<String, Building> bs) {        
        return (bs.get("shipyard").getCurrent() >= bs.get("shipyard").getSkill());
    }
    
    private boolean isBunkerBuildToMax(HashMap<String, Building> bs) {        
        return (bs.get("bunker").getCurrent() >= bs.get("bunker").getSkill());
    }
    
    private boolean areAllDepotsBuildToMax(HashMap<String, Building> bs) {    
        if (bs.get("copperdepot").getSkill() > bs.get("copperdepot").getCurrent())
            return false;
        if (bs.get("coaldepot").getSkill() > bs.get("coaldepot").getCurrent())
            return false;
        if (bs.get("oredepot").getSkill() > bs.get("oredepot").getCurrent())
            return false;
        if (bs.get("uraniumdepot").getSkill() > bs.get("uraniumdepot").getCurrent())
            return false;
        
        return true;
    }
    
    public String getUserName() {
        return userName;
    }

    public String getPlanetId() {
        return planetId;
    }

    public String getPlanetName() {
        return planetName;
    }

    public static HashMap<String, Building> loadBuildings(Buildings buildings) throws JSONException, IOException {
        LOGGER.trace("loadBuildings()");
        LOGGER.debug("    LOADING buildings of " + buildings.getPlanetName() + "("+ buildings.getPlanetId() + "). Please wait...");
        
        String apiCmd = String.format(Util.NEXTCOLONY_API_CMD_LOADBUILDINGS, buildings.getPlanetId());
        JSONArray jsonBuildings = Util.getJSONArrayFromApiCommand(apiCmd);
        
        HashMap<String, Building> mapBuildings = new HashMap<String, Building>();
        
        for (int i = 0; i < jsonBuildings.length(); i++) {
            Building b = new Building(jsonBuildings.getJSONObject(i), buildings);
            mapBuildings.put(b.getName(), b);
        }
        return mapBuildings;
    }
    
//    public static void main(String[] args) throws FileNotFoundException, IOException, JSONException, SteemInvalidTransactionException, SteemCommunicationException, SteemResponseException {
//
//        // Read user and keys from ini-file
//        HashMap<String, String> mapUserAndKey = new HashMap<String, String>();
//        Util.loadProperties(mapUserAndKey);
//        
//        SteemUtil.setDefaultAccount("captain.kirk",  mapUserAndKey.get("captain.kirk"));
//        
//        RessourceQuantities ressourceQuantities = new RessourceQuantities("captain.kirk", "P-ZMO0RCR18U8");//.loadRessourceQuantites();
//
//        new Buildings("P-ZMO0RCR18U8", "18-Sigma", "captain.kirk").upgradeAll(ressourceQuantities);
//    }
}
