package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import java.io.IOException;

import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.PlanetDetails;

public class PanelPlanetDetails extends JPanel {

    private Planet planet;
    
    public PanelPlanetDetails(Planet planet) {
        
        this.planet = planet;

        try {
            PlanetDetails planetDetails = new PlanetDetails(planet.getId());
            
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
