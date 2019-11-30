package de.prinzvalium.nextvaliumgui.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails.DialogPlanet;
import de.prinzvalium.nextvaliumgui.gui.popupmenu.PopupMenuPlanet;
import de.prinzvalium.nextvaliumgui.lib.Util;
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
                color = Util.getUserColor(userName);
                mapUserColor.put(userName, color);
            }
            
            return color;
            
        } catch (JSONException | IOException e) {
            return null;
        }
    }

    protected void paintComponent(Graphics g) {
        
        // Target white with red circles
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
        String planetUser = planet.getUserName();
        
        if (selectedUser != null && planetUser != null && selectedUser.equalsIgnoreCase(planetUser)) {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth()-1, getHeight()-1);
            g.setColor(Color.DARK_GRAY);
            g.drawOval(0, 0, getWidth()-1, getHeight()-1);
        }
        else if (planetUser.equalsIgnoreCase("null")) {
            g.setColor(new Color(160, 160, 160));
            g.drawOval(1, 1, getWidth()-3, getHeight()-3);
        }
        else {
            g.setColor(color);
            g.fillOval(1, 1, getWidth()-3, getHeight()-3);
            g.setColor(color.darker());
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
