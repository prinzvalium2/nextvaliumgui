package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapKey;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;

import java.awt.Color;

public class PanelFlightRadar extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelGalaxyMap.class);
    private HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = null;
    private int locationX;
    private int locationY;
    
    public PanelFlightRadar(HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap, int locationX, int locationY) {
        setOpaque(false);
        LOGGER.trace("PanelFlightRadar()");
        
        this.galaxyMap = galaxyMap;
        this.locationX = locationX;
        this.locationY = locationY;
        
        setLayout(null);
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.BLACK);
        
        galaxyMap.forEach((galaxyMapKey, galaxyMapValue) -> {
            
            if (!galaxyMapValue.getStatus().equalsIgnoreCase("planet"))
                return;
            
            int x = (galaxyMapKey.getX() - locationX) * 6 + (getWidth() / 2);
            int y = (galaxyMapKey.getY() - locationY) * -6 + (getHeight() / 2);
            
            g.drawLine(x, y, x+100, y+100);
        });
    }
}
