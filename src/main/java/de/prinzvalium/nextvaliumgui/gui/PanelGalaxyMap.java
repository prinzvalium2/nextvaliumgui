package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.Galaxy;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapKey;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;

public class PanelGalaxyMap extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMap = null;
    private int locationX;
    private int locationY;
    private Color color = null;
    private HashMap<String, Color> mapUserColor = new HashMap<String, Color>();
    
    public PanelGalaxyMap()  {
        setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(116, 98, 10, 10);
        add(panel);
    }
    
    public void loadGalaxyMap(int x, int y) {
        
        try {
            locationX = x;
            locationY = y;
            HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMapLeft;
            HashMap<GalaxyMapKey, GalaxyMapValue> galaxyMapRight;
            galaxyMapLeft = Galaxy.loadGalaxyMap(locationX-50, locationY, 100, 125);
            galaxyMapRight = Galaxy.loadGalaxyMap(locationX+50, locationY, 100, 125);
            galaxyMap = new HashMap<GalaxyMapKey, GalaxyMapValue>(galaxyMapLeft);
            galaxyMap.putAll(galaxyMapRight);
            
           
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
                    int red = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                    int green = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                    int blue = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                    color = new Color(red, green, blue);
                    mapUserColor.put(userName, color);
                }
            }
            
            switch (status) {
            case "explore":
                g.setColor(color.brighter());
                g.fillOval(x-3, y-3, 6, 6);
               break;
            case "explored":
                g.setColor(color);
                g.fillOval(x-3, y-3, 6, 6);
                break;
            }
        });
        
        galaxyMap.forEach((galaxyMapKey, galaxyMapValue) -> {
            int x = (galaxyMapKey.getX() - locationX) * 6 + (getWidth() / 2);
            int y = (galaxyMapKey.getY() - locationY) * -6 + (getHeight() / 2);
            
            if (galaxyMapValue.getStatus().equalsIgnoreCase("planet")) {
                PanelPlanet panelPlanet = new PanelPlanet(galaxyMapValue, mapUserColor);
                panelPlanet.setLocation(x-panelPlanet.getWidth()/2, y-panelPlanet.getHeight()/2);
                //panelPlanet.setVisible(true);
                add(panelPlanet);
            }
        });
        
    }
}
