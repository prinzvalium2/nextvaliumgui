package de.prinzvalium.nextvalium.nextcolony;


public class GalaxyMapValue {
    
    private String status;
    private String userName;
    private String planetId;
    private String planetImg;
    
    public GalaxyMapValue(String status, String userName) {
        this.status = status;
        this.userName = userName;
        this.planetId = null;
        this.planetImg = null;
    }

    public GalaxyMapValue(String status, String userName, String planetId, String planetImg) {
        this.status = status;
        this.userName = userName;
        this.planetId = planetId;
        this.planetImg = planetImg;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public String getPlanetId() {
        return planetId;
    }

    public String getPlanetImg() {
        return planetImg;
    }
}
