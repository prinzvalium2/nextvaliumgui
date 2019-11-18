package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

public class TableFleetValues {
    
    private String shipType;
    private Integer numberPlanet;
    private Integer numberFleet;
    private Integer position;
    
    public TableFleetValues() {
        this.shipType = null;
        this.numberPlanet = -1;
        this.numberFleet = -1;
        this.position = -1;
    }
    
    public TableFleetValues(String shipType, Integer numberPlanet, Integer numberFleet, Integer position) {
        this.shipType = shipType;
        this.numberPlanet = numberPlanet;
        this.numberFleet = numberFleet;
        this.position = position;
    }

    public String getShipType() {
        return shipType;
    }

    public Integer getNumberPlanet() {
        return numberPlanet;
    }

    public Integer getNumberFleet() {
        return numberFleet;
    }

    public Integer getPosition() {
        return position;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public void setNumberPlanet(Integer numberPlanet) {
        this.numberPlanet = numberPlanet;
    }

    public void setNumberFleet(Integer numberFleet) {
        this.numberFleet = numberFleet;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
