package de.prinzvalium.nextvaliumgui.nextcolony;

import org.json.JSONObject;

public class ProductionRessources {
    
    private  ProductionRessource coal;
    private  ProductionRessource ore;
    private  ProductionRessource copper;
    private  ProductionRessource uranium;
    
    ProductionRessources(JSONObject jsonRessources) {
        
        JSONObject jsonCoal = jsonRessources.getJSONObject("coal");
        JSONObject jsonOre = jsonRessources.getJSONObject("ore");
        JSONObject jsonCopper = jsonRessources.getJSONObject("copper");
        JSONObject jsonUranium = jsonRessources.getJSONObject("uranium");
        
        coal = new ProductionRessource(jsonCoal);
        ore = new ProductionRessource(jsonOre);
        copper = new ProductionRessource(jsonCopper);
        uranium = new ProductionRessource(jsonUranium);
    }

    public ProductionRessource getCoal() {
        return coal;
    }

    public ProductionRessource getOre() {
        return ore;
    }

    public ProductionRessource getCopper() {
        return copper;
    }

    public ProductionRessource getUranium() {
        return uranium;
    }
}
