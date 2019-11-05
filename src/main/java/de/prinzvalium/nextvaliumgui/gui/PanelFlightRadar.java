package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.nextcolony.Mission;
import de.prinzvalium.nextvaliumgui.nextcolony.Missions;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapKey;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;

import java.awt.BasicStroke;
import java.awt.Color;

public class PanelFlightRadar extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelGalaxyMap.class);
    private HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = null;
    private int locationX;
    private int locationY;
    private HashMap<String, Planet> planets;
    private HashMap<String, Vector<Mission>> missionMap;
    
    public PanelFlightRadar(HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap, int locationX, int locationY) {
        LOGGER.trace("PanelFlightRadar()");
        
        this.galaxyMap = galaxyMap;
        this.locationX = locationX;
        this.locationY = locationY;
        
        setLayout(null);
        setOpaque(false);
        setVisible(true);
        
        try {
            planets = Planets.getAllPlanets();
        } catch (JSONException | IOException e1) {
            planets = null;
        }
        
        missionMap = new HashMap<String, Vector<Mission>>();
        
        galaxyMap.forEach((galaxyMapKey, galaxyMapValue) -> {
            
            if (!galaxyMapValue.getStatus().equalsIgnoreCase("planet"))
                return;
            
            String planetId = galaxyMapValue.getPlanetId();
            Planet planet = planets.get(planetId);
            String userName = planet.getUserName();
            
//            if (!planetId.equalsIgnoreCase("P-ZZPJTYEYI8G"))
//                return;
            
//            if (!userName.equalsIgnoreCase("prinzvalium"))
//                return;
            
            if (missionMap.get(userName) != null)
                return;
            
            Vector<Mission> missions = null;
            
            try {
                missions = Missions.loadAllActiveMissions(userName);
            } catch (JSONException | IOException e) {
                return;
            }
            
            missionMap.put(userName, missions);
        });
    }

    @Override 
    protected void paintComponent(Graphics g) {
        
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        
        galaxyMap.forEach((galaxyMapKey, galaxyMapValue) -> {
            
            if (!galaxyMapValue.getStatus().equalsIgnoreCase("planet"))
                return;
            
            String planetId = galaxyMapValue.getPlanetId();
            Planet planet = planets.get(planetId);
            String userName = planet.getUserName();
            
            Vector<Mission> missions = missionMap.get(userName);
            
            if (missions == null || missions.isEmpty())
                return;
            
            for (Mission mission : missions) {
                
                if (!mission.getFromPlanetId().equalsIgnoreCase(planetId) &&
                        !mission.getToPlanetId().equalsIgnoreCase(planetId))
                    continue;
                
                int start_x = mission.getStart_x();
                int start_y = mission.getStart_y();
                int end_x = mission.getEnd_x();
                int end_y = mission.getEnd_y();
                
                int xs = (start_x - locationX) * 6 + (getWidth() / 2);
                int ys = (start_y - locationY) * -6 + (getHeight() / 2);
                int xe = (end_x - locationX) * 6 + (getWidth() / 2);
                int ye = (end_y - locationY) * -6 + (getHeight() / 2);
                 
                g2.draw(new Line2D.Float(xs, ys, xe, ye));
            }
        });
    }
}
