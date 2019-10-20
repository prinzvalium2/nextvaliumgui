package de.prinzvalium.nextvaliumgui;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvalium.nextcolony.CordData;
import de.prinzvalium.nextvalium.nextcolony.GalaxyMapValue;
import de.prinzvalium.nextvalium.nextcolony.PlanetDetails;

public class PanelPlanet extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private GalaxyMapValue galaxyMapValue;
    private PlanetDetails planetDetails;
    private Color color;

    public PanelPlanet(GalaxyMapValue value, HashMap<String, Color> mapUserColor) {
        
        galaxyMapValue = value;
        color = null;
        
        try {
            //String userName = new PlanetDetails(value.getPlanetId()).getUserName();
            String userName = new CordData(value.getPlanetX(), value.getPlanetY()).getUserName();
            color = mapUserColor.get(userName);
            
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (color == null)
            color = Color.BLACK;
                
        setSize(12, 12);
    }

    protected void paintComponent(Graphics g) {
        
        g.setColor(color.darker());
//        g.fillOval(0, 0, getSize().width, getSize().height);
//        g.drawOval(0, 0, getSize().width, getSize().height);
        
        g.fillOval(0, 0, getWidth(), getHeight());
        g.drawOval(0, 0, getWidth(), getHeight());
    }
}
