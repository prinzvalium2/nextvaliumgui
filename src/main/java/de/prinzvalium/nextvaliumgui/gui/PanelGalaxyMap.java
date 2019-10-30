package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.Galaxy;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapKey;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;
import javax.swing.JLabel;

public class PanelGalaxyMap extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelGalaxyMap.class);
    private HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = null;
    private int locationX;
    private int locationY;
    private Color color = null;
    private HashMap<String, Color> mapUserColor = new HashMap<String, Color>();
    
    public PanelGalaxyMap()  {
        LOGGER.trace("PanelGalaxyMap()");
        
        setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Loading data...");
        lblNewLabel.setBounds(1, 1, 80, 14);
        add(lblNewLabel);
    }
    
    public void loadGalaxyMap(int x, int y) {
        LOGGER.trace("loadGalaxyMap()");
        
        try {
            locationX = x;
            locationY = y;
            galaxyMap = Galaxy.loadGalaxyMap(locationX-50, locationY, 100, 125);
            galaxyMap.putAll(Galaxy.loadGalaxyMap(locationX+50, locationY, 100, 125));
           
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    @Override 
    protected void paintComponent(Graphics g) {
        
        if (galaxyMap == null)
            return;
        
        galaxyMap.forEach((galaxyMapKey, galaxyMapValue) -> {
            int x = (galaxyMapKey.getX() - locationX) * 6 + (getWidth() / 2);
            int y = (galaxyMapKey.getY() - locationY) * -6 + (getHeight() / 2);
            
            String status = galaxyMapValue.getStatus();
            String userName = galaxyMapValue.getUserName();
            
            color = Color.BLACK;
            
            if (userName != null) {
                
                color = mapUserColor.get(userName);
                
                if (color == null) {
                    
                    int[] colorValues = new int[3];

                    colorValues[0] = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                    colorValues[1] = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                    colorValues[2] = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                    
                    String s = userName.toUpperCase();
                    
                    int i = 0;
                    for (char c : s.toCharArray())  {
                        if (c >= 0x30 && c <= 0x39)
                            colorValues[i++] = (c - 0x30) * 28;
                        else if (c >= 0x41 && c <= 0x5A)
                            colorValues[i++] = (c - 0x41) * 10;
                        if (i > 2)
                            break;
                    }
                       
                    color = new Color(colorValues[0], colorValues[1], colorValues[2]);
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
        
        removeAll();
        paintPlanets();
    }
    
    public void paintPlanets() {
        LOGGER.trace("paintPlanets() - start");
        
        galaxyMap.forEach((galaxyMapKey, galaxyMapValue) -> {
            
            if (!galaxyMapValue.getStatus().equalsIgnoreCase("planet"))
                return;
            
            int x = (galaxyMapKey.getX() - locationX) * 6 + (getWidth() / 2);
            int y = (galaxyMapKey.getY() - locationY) * -6 + (getHeight() / 2);
            
            PanelPlanet panelPlanet = new PanelPlanet(galaxyMapValue, mapUserColor);
            panelPlanet.setLocation(x-panelPlanet.getWidth()/2, y-panelPlanet.getHeight()/2);
            add(panelPlanet);
        });
        
        LOGGER.trace("paintPlanets() - end");
    }
}
