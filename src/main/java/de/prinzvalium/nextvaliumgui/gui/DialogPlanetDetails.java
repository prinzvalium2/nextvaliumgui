package de.prinzvalium.nextvaliumgui.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.PlanetDetails;
import javax.swing.JTabbedPane;

public class DialogPlanetDetails extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private Planet planet;

    /**
     * Create the dialog.
     */
    public DialogPlanetDetails(Planet planet) {
        
        this.planet = planet;
        setTitle(planet.getUserName() + " / " + planet.getName());
        
        setBounds(100, 150, 800, 600);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            contentPanel.add(tabbedPane);
            {
                PanelPlanetDetails panelPlanetDetails = new PanelPlanetDetails(planet);
                tabbedPane.addTab("Details", null, panelPlanetDetails, null);
            }
            {
                JPanel panelFleet = new JPanel();
                tabbedPane.addTab("Fleet", null, panelFleet, null);
            }
            {
                JPanel panelMissions = new JPanel();
                tabbedPane.addTab("Missions", null, panelMissions, null);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            DialogPlanetDetails dialog = new DialogPlanetDetails(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
