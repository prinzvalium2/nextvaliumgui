package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.nextcolony.Fleet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class PanelFleet extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableShips;
    
    public PanelFleet(Planet planet) {
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        add(scrollPane, gbc_scrollPane);
        
        DefaultTableModel model = new DefaultTableModel(new Object[] { "Ship type", "Count", "Use for mission", "Position" }, 0);
        
        try {
            Fleet fleet = new Fleet(planet.getUserName(), planet.getName(), planet.getId());
            HashMap<String, Integer> mapShips = fleet.getNumberOfShipTypesInShipyard();
            for (Map.Entry<?,?> entry : mapShips.entrySet())
                model.addRow(new Object[] { entry.getKey(), entry.getValue(), null, null });

        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        tableShips = new JTable();
        tableShips.setModel(model);
        scrollPane.setViewportView(tableShips);
    }
}
