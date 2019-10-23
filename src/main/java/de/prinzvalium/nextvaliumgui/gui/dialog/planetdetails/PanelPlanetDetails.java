package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.PlanetDetails;

public class PanelPlanetDetails extends JPanel {

    private static final long serialVersionUID = 1L;
    
    public PanelPlanetDetails(Planet planet) {
        
        try {
            new PlanetDetails(planet.getId());
            
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
