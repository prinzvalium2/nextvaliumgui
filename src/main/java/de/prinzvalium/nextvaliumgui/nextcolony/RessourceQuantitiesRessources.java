package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONObject;

public class RessourceQuantitiesRessources {
    
    private double coal;     
    private double coaldepot;
    private double coalrate;    
    private double copper;   
    private double copperdepot; 
    private double copperrate;
    private double ore; 
    private double oredepot;    
    private double orerate;   
    private double uranium;    
    private double uraniumdepot;
    private double uraniumrate;
    
    private Date lastUpdate;
    
    RessourceQuantitiesRessources(JSONObject jsonRessourceQuantites) {
        
        lastUpdate = new Date(jsonRessourceQuantites.getLong("lastUpdate") * 1000);
        
        long secondsSinceLastUpdate = (new Date().getTime()-lastUpdate.getTime()) / 1000;
        
        coaldepot = jsonRessourceQuantites.getDouble("coaldepot");   
        coalrate = jsonRessourceQuantites.getDouble("coalrate");
        coal  = jsonRessourceQuantites.getDouble("coal");
        if (coal < coaldepot) {
            coal += (coalrate / (24*3600)) * secondsSinceLastUpdate;    
            if (coal > coaldepot)
                coal = coaldepot;
        }
        
        copperdepot = jsonRessourceQuantites.getDouble("copperdepot"); 
        copperrate = jsonRessourceQuantites.getDouble("copperrate");
        copper = jsonRessourceQuantites.getDouble("copper");
        if (copper < copperdepot) { 
            copper += (copperrate / (24*3600)) * secondsSinceLastUpdate;
            if (copper > copperdepot)
                copper = copperdepot;
        }
        
        oredepot = jsonRessourceQuantites.getDouble("oredepot");
        orerate = jsonRessourceQuantites.getDouble("orerate");
        ore = jsonRessourceQuantites.getDouble("ore");
        if (ore < oredepot) {
            ore += (orerate / (24*3600)) * secondsSinceLastUpdate;
            if (ore > oredepot)
                ore = oredepot;
        }
        
        uraniumdepot = jsonRessourceQuantites.getDouble("uraniumdepot");
        uraniumrate = jsonRessourceQuantites.getDouble("uraniumrate");
        uranium = jsonRessourceQuantites.getDouble("uranium");
        if (uranium < uraniumdepot) {
            uranium += (uraniumrate / (24*3600)) * secondsSinceLastUpdate;
            if (uranium > uraniumdepot)
                uranium = uraniumdepot;
        }
    }
    
    public double getCoal() {
        return coal;
    }

    public double getCoalDepot() {
        return coaldepot;
    }

    public double getCoalrate() {
        return coalrate;
    }

    public double getCopper() {
        return copper;
    }

    public double getCopperDepot() {
        return copperdepot;
    }

    public double getCopperrate() {
        return copperrate;
    }

    public double getOre() {
        return ore;
    }

    public double getOreDepot() {
        return oredepot;
    }

    public double getOrerate() {
        return orerate;
    }

    public double getUranium() {
        return uranium;
    }

    public double getUraniumDepot() {
        return uraniumdepot;
    }

    public double getUraniumrate() {
        return uraniumrate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

}
