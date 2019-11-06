package de.prinzvalium.nextvaliumgui.nextcolony.galaxymap;


public class GalaxyMapValue {
    
    private String status = null;
    private String userName = null;
    private String planetId = null;
    private String planetImg = null;
    private int planetX = 0;
    private int planetY = 0;
    private GalaxyMapValueExplore galaxyMapValueExplore = null;
    
    public GalaxyMapValue(String status, String userName) {
        this.status = status;
        this.userName = userName;
    }

    public GalaxyMapValue(String status, String userName, GalaxyMapValueExplore galaxyMapValueExplore) {
        this.status = status;
        this.userName = userName;
        this.galaxyMapValueExplore = galaxyMapValueExplore;
    }

    public GalaxyMapValue(String status, String userName, String planetId, String planetImg, GalaxyMapKey key) {
        this.status = status;
        this.userName = userName;
        this.planetId = planetId;
        this.planetImg = planetImg;
        planetX = key.getX();
        planetY = key.getY();
    }
    
    public GalaxyMapValue enrich(GalaxyMapValue galaxyMapValue) {
        if (galaxyMapValue == null)
            return this;
        
        if (this.status == null)
            this.status = galaxyMapValue.status;
        if (this.userName == null)
            this.userName = galaxyMapValue.userName;
        if (this.planetId == null)
            this.planetId = galaxyMapValue.planetId;
        if (this.planetImg == null)
            this.planetImg = galaxyMapValue.planetImg;
        if (this.galaxyMapValueExplore == null)
            this.galaxyMapValueExplore = galaxyMapValue.galaxyMapValueExplore;
        
        return this;
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

    public GalaxyMapValueExplore getGalaxyMapValueExplore() {
        return galaxyMapValueExplore;
    }
}
