package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONObject;

public class Planet implements Comparable<Planet> {

    private int posX;
    private int posY;
    private boolean starter;
    private String planetName;
    private String planetId;
    private Date date;
    private String userName;

    Planet(JSONObject jsonPlanet) {
        posX = jsonPlanet.getInt("posx");
        posY = jsonPlanet.getInt("posy");
        starter = jsonPlanet.getInt("starter") == 1;
        planetName = jsonPlanet.getString("name");
        planetId = jsonPlanet.getString("id");
        userName = jsonPlanet.getString("username");
        date = new Date(jsonPlanet.getLong("date") * 1000);
    }
    
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isStarter() {
        return starter;
    }

    public String getId() {
        return planetId;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return planetName;
    }

    public String getUserName() {
        return userName;
    }

     @Override
    public int compareTo(Planet p) {
        return planetName.compareTo(p.getName());
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    @Override
    public String toString() {
        return this.planetName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return ((Planet)obj).getId().equalsIgnoreCase(planetId);
    }
}
