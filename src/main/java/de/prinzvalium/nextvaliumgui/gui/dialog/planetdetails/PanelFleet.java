package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.nextcolony.Fleet;
import de.prinzvalium.nextvaliumgui.nextcolony.FleetShip;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import java.awt.Dimension;

public class PanelFleet extends JPanel {

    private Planet planet;
    
    public PanelFleet(Planet planet) {
        
        this.planet = planet;
        setLayout(new BorderLayout(0, 0));
        
        DefaultListModel<String> l1 = new DefaultListModel<>();
        
        try {
            Fleet fleet = new Fleet(planet.getUserName(), planet.getName(), planet.getId());
            HashMap<String, Integer> mapShips = fleet.getNumberOfShipTypesInShipyard();
            mapShips.forEach((shipType, num) -> l1.addElement(shipType + " " + num));
            
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        JList<String> listShips = new JList<String>(l1);
        listShips.setPreferredSize(new Dimension(100, 0));
        add(listShips, BorderLayout.WEST);
    }

}
