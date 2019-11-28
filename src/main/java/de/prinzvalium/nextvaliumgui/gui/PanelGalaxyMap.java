package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JPanel;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiValuedMap;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.Galaxy;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapKey;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;
import javax.swing.JLabel;

public class PanelGalaxyMap extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelGalaxyMap.class);
    private MultiValuedMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = null;
    private int locationX;
    private int locationY;
    private Color color = null;
    private static HashMap<String, Color> mapUserColor = new HashMap<String, Color>();
    private PanelFlightRadar panelFlighRadar = null;
    private JLabel lblNewLabel;
    
    public PanelGalaxyMap()  {
        LOGGER.trace("PanelGalaxyMap()");
        
        setLayout(null);
        
        lblNewLabel = new JLabel("Loading data...");
        lblNewLabel.setBounds(1, 1, 80, 14);
        add(lblNewLabel);
    }
    
    public void loadGalaxyMap(int x, int y) {
        LOGGER.trace("loadGalaxyMap()");
        
        try {
            locationX = x;
            locationY = y;
            galaxyMap = Galaxy.loadGalaxyMap(locationX-50, locationY, 100, 125);
            
            // loadGalaxy returns width+1 (left and right from the location plus the location)
            //galaxyMap.putAll(Galaxy.loadGalaxyMap(locationX+50, locationY, 100, 125));
            galaxyMap.putAll(Galaxy.loadGalaxyMap(locationX+51, locationY, 100, 125));
           
            panelFlighRadar = new PanelFlightRadar(galaxyMap, locationX, locationY);
            panelFlighRadar.setBounds(0, 0, getWidth(), getHeight());
            
        } catch (JSONException | IOException e) {
            lblNewLabel.setText(e.getMessage());
        }
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (galaxyMap == null)
            return;
        
        MapIterator<GalaxyMapKey, GalaxyMapValue> mapIterator = galaxyMap.mapIterator();
        
        while (mapIterator.hasNext()) {
        
            GalaxyMapKey galaxyMapKey = mapIterator.next();
            
            int x = (galaxyMapKey.getX() - locationX) * 6 + (getWidth() / 2);
            int y = (galaxyMapKey.getY() - locationY) * -6 + (getHeight() / 2);
            
            Collection<GalaxyMapValue> galaxyMapValues = galaxyMap.get(galaxyMapKey);
            
            galaxyMapValues.forEach(galaxyMapValue -> {
        
                String status = galaxyMapValue.getStatus();
                String userName = galaxyMapValue.getUserName();
                
                color = Color.BLACK;
                
                if (userName != null) {
                    color = mapUserColor.get(userName);
                    if (color == null) {
                        color = Util.getUserColor(userName);
                        mapUserColor.put(userName, color);
                    }
                }
                
                switch (status) {
                case "explore":
                    g.setColor(color);
                    g.fillOval(x-3, y-3, 6, 6);
                   break;
                case "explored":
                    g.setColor(color);
                    g.drawOval(x-1, y-1, 2, 2);
                    g.drawOval(x-2, y-2, 4, 4);
                    break;
                }
            });
        }
        
        removeAll();
        
        if (panelFlighRadar != null) {
            panelFlighRadar.setBounds(0, 0, getWidth(), getHeight());
            add(panelFlighRadar);
        }
        
        paintPlanets();
        
        NextValiumGui.getNextValiumGui().getFrmNextvaliumManagementGui().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void paintPlanets() {
        LOGGER.trace("paintPlanets() - start");
        
        MapIterator<GalaxyMapKey, GalaxyMapValue> mapIterator = galaxyMap.mapIterator();
        
        while (mapIterator.hasNext()) {
        
            GalaxyMapKey galaxyMapKey = mapIterator.next();
            Collection<GalaxyMapValue> galaxyMapValues = galaxyMap.get(galaxyMapKey);
            
            galaxyMapValues.forEach(galaxyMapValue -> {
                
                if (!galaxyMapValue.getStatus().equalsIgnoreCase("planet"))
                    return;
                
                int x = (galaxyMapKey.getX() - locationX) * 6 + (getWidth() / 2);
                int y = (galaxyMapKey.getY() - locationY) * -6 + (getHeight() / 2);
                
                PanelPlanet panelPlanet = new PanelPlanet(galaxyMapValue, mapUserColor);
                panelPlanet.setLocation(x-panelPlanet.getWidth()/2, y-panelPlanet.getHeight()/2);
                add(panelPlanet);
            });
        }
        
        LOGGER.trace("paintPlanets() - end");
    }
}
