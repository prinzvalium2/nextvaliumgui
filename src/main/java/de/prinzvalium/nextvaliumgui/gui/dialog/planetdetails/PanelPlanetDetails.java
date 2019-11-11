package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.PlanetDetails;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.awt.GridBagConstraints;

public class PanelPlanetDetails extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel panelImage;
    
    public PanelPlanetDetails(Planet planet) {
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        panelImage = new JLabel();
      
        GridBagConstraints gbc_panelImage = new GridBagConstraints();
        gbc_panelImage.fill = GridBagConstraints.BOTH;
        gbc_panelImage.gridx = 0;
        gbc_panelImage.gridy = 0;
        
        add(panelImage, gbc_panelImage);
       
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                PlanetDetails planetDetails = new PlanetDetails(planet.getId());
                String path = "https://nextcolony.io/img/planets/" + planetDetails.getImg();
                URL url = new URL(path);
                BufferedImage image = ImageIO.read(url);
                panelImage = new JLabel(new ImageIcon(image));
                return null;
            }

            @Override
            protected void done() {
                add(panelImage, gbc_panelImage);
                super.done();
            }
            
        }.execute();
    }
}
