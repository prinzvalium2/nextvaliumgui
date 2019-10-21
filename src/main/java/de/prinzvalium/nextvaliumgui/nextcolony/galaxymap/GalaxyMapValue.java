package de.prinzvalium.nextvaliumgui.nextcolony.galaxymap;


public class GalaxyMapValue {
    
    private String status;
    private String userName;
    private String planetId;
    private String planetImg;
    private int planetX;
    private int planetY;
    
    public GalaxyMapValue(String status, String userName) {
        this.status = status;
        this.userName = userName;
        this.planetId = null;
        this.planetImg = null;
    }

    public GalaxyMapValue(String status, String userName, String planetId, String planetImg, GalaxyMapKey key) {
        this.status = status;
        this.userName = userName;
        this.planetId = planetId;
        this.planetImg = planetImg;
        planetX = key.getX();
        planetY = key.getY();
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

    public int getPlanetX() {
        return planetX;
    }

    public int getPlanetY() {
        return planetY;
    }
}
