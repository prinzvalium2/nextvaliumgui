package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails.DialogPlanet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;
import de.prinzvalium.nextvaliumgui.nextcolony.galaxymap.GalaxyMapValue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelPlanet extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private Planet planet;
    private Color color;
    private PanelPlanet panelPlanet;

    public PanelPlanet(GalaxyMapValue value, HashMap<String, Color> mapUserColor) {
        
        panelPlanet = this;
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    DialogPlanet dialog = new DialogPlanet(planet);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                 }
                else {
                    PopupMenuPlanet popupMenuPlanet = new PopupMenuPlanet(panelPlanet);
                    popupMenuPlanet.show(arg0.getComponent(), arg0.getX(), arg0.getY());
                }
            }
        });
        
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
            
            String userName = planet.getUserName();
            
            Planet planetTarget = NextValiumGui.getNextValiumGui().getPlanetMarkedAsTarget();
            if (planetTarget != null && planetTarget.getId().equalsIgnoreCase(planetId))
                return Color.WHITE;
            
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
                        colorValues[i] = (c - 0x30 + 10) * 10;
                    else if (c >= 0x41 && c <= 0x5A)
                        colorValues[i] = ((c - 0x41) / 2 + 8) * 10;
                    if (++i > 2)
                        break;;
                }
                    
                color = new Color(colorValues[0], colorValues[1], colorValues[2]);
                mapUserColor.put(userName, color);
            }
            
            return color;
            
        } catch (JSONException | IOException e) {
            return null;
        }
    }

    protected void paintComponent(Graphics g) {
        
        if (color.equals(Color.WHITE)) {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth()-1, getHeight()-1);
            g.setColor(Color.RED);
            g.drawOval(0+0, 0+0, getWidth()-1, getHeight()-1);
            g.drawOval(0+2, 0+2, getWidth()-5, getHeight()-5);
            g.drawOval(0+4, 0+4, getWidth()-9, getHeight()-9);
            return;
        }
        g.setColor(color);
        
        String selectedUser = NextValiumGui.getNextValiumGui().getSelectedUser();
        if (selectedUser.equalsIgnoreCase(planet.getUserName())) {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth()-1, getHeight()-1);
            g.setColor(Color.DARK_GRAY);
            g.drawOval(0, 0, getWidth()-1, getHeight()-1);
        }
        else {
            g.setColor(color);
            g.fillOval(1, 1, getWidth()-3, getHeight()-3);
            g.drawOval(1, 1, getWidth()-3, getHeight()-3);
        }
    }
    
    public void setMarked(boolean marked) {
        if (marked)
            NextValiumGui.getNextValiumGui().setPlanetMarkedAsTarget(planet);
        else
            NextValiumGui.getNextValiumGui().clearTarget();
    }

    public Planet getPlanet() {
        return planet;
    }
}
