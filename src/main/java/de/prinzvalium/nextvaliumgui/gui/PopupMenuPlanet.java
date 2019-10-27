package de.prinzvalium.nextvaliumgui.gui;

import javax.swing.JPopupMenu;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.nextcolony.Fleet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

public class PopupMenuPlanet extends JPopupMenu {
    
    private static final long serialVersionUID = 1L;
    
    public PopupMenuPlanet(PanelPlanet panelPlanet) {
        setPopupSize(new Dimension(200, 200));
        
        Planet planet = panelPlanet.getPlanet();
        String titleBorder = planet.getUserName() + " / " + planet.getName();
        
        JPanel panelPlanetDetails = new JPanel();
        panelPlanetDetails.setBorder(new TitledBorder(null, titleBorder, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(panelPlanetDetails);
        
        DefaultListModel<String> model = new DefaultListModel<>();
        GridBagLayout gbl_panelPlanetDetails = new GridBagLayout();
        gbl_panelPlanetDetails.columnWidths = new int[]{119, 0};
        gbl_panelPlanetDetails.rowHeights = new int[]{22, 0, 0};
        gbl_panelPlanetDetails.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panelPlanetDetails.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        panelPlanetDetails.setLayout(gbl_panelPlanetDetails);
        
        JCheckBox chckbxMarkAsTarget = new JCheckBox("Mark as target");
        chckbxMarkAsTarget.setSelected(panelPlanet.isMarked());
        chckbxMarkAsTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                panelPlanet.setMarked(chckbxMarkAsTarget.isSelected());
            }
        });
        GridBagConstraints gbc_chckbxMarkAsTarget = new GridBagConstraints();
        gbc_chckbxMarkAsTarget.anchor = GridBagConstraints.WEST;
        gbc_chckbxMarkAsTarget.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxMarkAsTarget.gridx = 0;
        gbc_chckbxMarkAsTarget.gridy = 0;
        panelPlanetDetails.add(chckbxMarkAsTarget, gbc_chckbxMarkAsTarget);
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        panelPlanetDetails.add(scrollPane, gbc_scrollPane);
        
//        JMenuItem mntmMarkAsTarget = new JMenuItem("Mark as target");
//        GridBagConstraints gbc_mntmMarkAsTarget = new GridBagConstraints();
//        gbc_mntmMarkAsTarget.insets = new Insets(0, 0, 5, 0);
//        gbc_mntmMarkAsTarget.anchor = GridBagConstraints.NORTHWEST;
//        gbc_mntmMarkAsTarget.gridx = 0;
//        gbc_mntmMarkAsTarget.gridy = 0;
//        panelPlanetDetails.add(mntmMarkAsTarget, gbc_mntmMarkAsTarget);
//        mntmMarkAsTarget.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                panelPlanet.setMarked(true);
//            }
//        });
        JList listShips = new JList(model);
        scrollPane.setViewportView(listShips);
        listShips.setVisibleRowCount(8);
        listShips.setEnabled(false);
        listShips.setBackground(SystemColor.menu);
        
        model.addElement("Loading ships...");
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Fleet fleet = new Fleet(planet.getUserName(), planet.getName(), planet.getId());
                    HashMap<String, Integer> mapShips = fleet.getNumberOfShipTypesInShipyard();
                    model.removeAllElements();
                    for (Map.Entry<?,?> entry : mapShips.entrySet())
                        model.addElement(entry.getKey() + ": " + entry.getValue());

                } catch (JSONException | IOException e) {
                    model.addElement(e.getMessage());
                }
            }}).start();
    }
}
