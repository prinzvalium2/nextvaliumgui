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
    private boolean marked = false;

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
            
            String userName = planet.getUserName();
            
            // Tooltips are soooo slow.... -> remove it
            //setToolTipText("<html>" + userName + "<br>" + planet.getName() + "</html>");
            
            Planet planetTarget = NextValiumGui.getNextValiumGui().getPlanetMarkedAsTarget();
            if (planetTarget != null && planetTarget.getId().equalsIgnoreCase(planetId))
                return Color.WHITE;
            
            color = mapUserColor.get(userName);
            
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
        
        if (color.equals(Color.WHITE)) {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.drawOval(0+1, 0+1, getWidth()-2, getHeight()-2);
            g.drawOval(0+3, 0+3, getWidth()-6, getHeight()-6);
            g.drawOval(0+5, 0+5, getWidth()-10, getHeight()-10);
            return;
        }
        g.setColor(color.darker());
        g.fillOval(0, 0, getWidth(), getHeight());
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

    public boolean isMarked() {
        return marked;
    }
}
