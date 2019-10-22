package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelPlanet extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelPlanet.class);
    private Planet planet;
    private Color color;
    private PanelPlanet panelPlanet;
    private boolean marked = false;

    public PanelPlanet(GalaxyMapValue value, HashMap<String, Color> mapUserColor) {
        
        panelPlanet = this;
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getButton() == arg0.BUTTON1) {
                    DialogPlanetDetails dialog = new DialogPlanetDetails(planet);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                }
                else {
                    PopupMenuPlanet popupMenuPlanet = new PopupMenuPlanet(panelPlanet);
                    popupMenuPlanet.show(arg0.getComponent(), arg0.getX(), arg0.getY());
                }
            }
        });
        
        if (marked)
            color = Color.WHITE;
        else
            color = getUserColor(value, mapUserColor);
        
        if (color == null)
            color = Color.BLACK;
                
        setSize(12, 12);
    }
    
    private Color getUserColor(GalaxyMapValue value, HashMap<String, Color> mapUserColor) {
        
        try {
            
            HashMap<String, Planet> planets = Planets.getAllPlanets();
            String planetId = value.getPlanetId();
            planet = planets.get(planetId);
            
            if (PanelGalaxyMap.getMarkedPlanetId() != null && PanelGalaxyMap.getMarkedPlanetId().equalsIgnoreCase(planetId))
                return Color.WHITE;
            
            String userName = planet.getUserName();
            color = mapUserColor.get(userName);
            setToolTipText("<html>" + userName + "<br>" + planet.getName() + "</html>");
            
            if (color == null) {
                int red = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                int green = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                int blue = ThreadLocalRandom.current().nextInt(1, 13) * 20;
                color = new Color(red, green, blue);
                mapUserColor.put(userName, color);
            }
            
            return color;
            
        } catch (JSONException | IOException e) {
            
            return null;
        }
    }

    protected void paintComponent(Graphics g) {
        g.setColor(color.equals(Color.WHITE) ? Color.WHITE : color.darker());
        g.fillOval(0, 0, getWidth(), getHeight());
    }
    
    public void setMarked(boolean marked) {
        PanelGalaxyMap.setMarkedPlanetId(planet.getId());
    }
}
