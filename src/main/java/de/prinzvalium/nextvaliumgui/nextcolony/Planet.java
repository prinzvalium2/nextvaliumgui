package de.prinzvalium.nextvaliumgui.nextcolony;

import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Planet implements Comparable<Planet> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Planet.class);
    public static final int GALAXY_MAP_MAX_DISTANCE = 50;
    private int posX;
    private int posY;
    private boolean starter;
    private String planetName;
    private String planetId;
    private Date date;
    private String userName;

    Planet(JSONObject jsonPlanet) {
        LOGGER.trace("Planet()");
        
        posX = jsonPlanet.getInt("posx");
        posY = jsonPlanet.getInt("posy");
        starter = jsonPlanet.getInt("starter") == 1;
        planetName = jsonPlanet.getString("name");
        planetId = jsonPlanet.getString("id");
        userName = jsonPlanet.getString("user");;
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
}
