package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.HashMap;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapKey;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValueExplore;

import java.awt.BasicStroke;
import java.awt.Color;

public class PanelFlightRadar extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelGalaxyMap.class);
    private HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = null;
    private int locationX;
    private int locationY;
    private Color colorAttack = new Color(245, 66, 66, 255);
    private Color colorSupport = new Color(66, 245, 132, 255);
    private Color colorTransport = new Color(219, 211, 59, 255);
    private Color colorExplore = new Color(0, 0, 0, 80);
    private Color colorSiege = new Color(219, 48, 211, 160); 
    private Color colorBreakSiege = new Color(219, 48, 211, 255); 
    private Color colorDeploy = new Color(78, 66, 245, 255);
    
    
    public PanelFlightRadar(HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap, int locationX, int locationY) {
        LOGGER.trace("PanelFlightRadar()");
        
        this.galaxyMap = galaxyMap;
        this.locationX = locationX;
        this.locationY = locationY;
        
        setLayout(null);
        setOpaque(false);
    }

    @Override 
    protected void paintComponent(Graphics g) {
        
        galaxyMap.forEach((galaxyMapKey, galaxyMapValue) -> {
            
            if (!galaxyMapValue.getStatus().equalsIgnoreCase("explore"))
                return;
            
            GalaxyMapValueExplore val = galaxyMapValue.getGalaxyMapValueExplore();
            
            Color color;
            
            switch (val.type) {
            case "attack":
                color = colorAttack;
                break;
            case "support":
                color = colorSupport;
                break;
            case "transport":
                color = colorTransport;
                break;
            case "explore":
                color = colorExplore;
                break;
            case "siege":
                color = colorSiege;
                break;
            case "breaksiege":
                color = colorBreakSiege;
                break;
            case "deploy":
                color = colorDeploy;
                break;
            default:
                color = Color.CYAN;
            }
            
            int start_x = val.start_x;
            int start_y = val.start_y;
            int end_x = val.x;
            int end_y = val.y;
            
            int xs = (start_x - locationX) * 6 + (getWidth() / 2);
            int ys = (start_y - locationY) * -6 + (getHeight() / 2);
            int xe = (end_x - locationX) * 6 + (getWidth() / 2);
            int ye = (end_y - locationY) * -6 + (getHeight() / 2);
           
            g.setColor(color);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.draw(new Line2D.Float(xs, ys, xe, ye));
        });
    }
}
