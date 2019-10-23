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
import javax.swing.JRadioButton;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;

public class PanelFleet extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableShips;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    
    public PanelFleet(Planet planet) {
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{300, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0};
        setLayout(gridBagLayout);
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridheight = 10;
        gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        add(scrollPane, gbc_scrollPane);
        
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {
                    {null, null, null, null},
                },
                new String[] {
                    "Ship type", "Count", "Use", "Pos"
                }
            ) {
                private static final long serialVersionUID = 1L;
                Class<?>[] columnTypes = new Class<?>[] {
                    String.class, Integer.class, Integer.class, Integer.class
                };
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
                boolean[] columnEditables = new boolean[] {
                    false, false, true, true
                };
                public boolean isCellEditable(int row, int column) {
                    return columnEditables[column];
                }
            };
        
        try {
            Fleet fleet = new Fleet(planet.getUserName(), planet.getName(), planet.getId());
            HashMap<String, Integer> mapShips = fleet.getNumberOfShipTypesInShipyard();
            model.removeRow(0);
            for (Map.Entry<?,?> entry : mapShips.entrySet())
                model.addRow(new Object[] { entry.getKey(), entry.getValue(), null, null });

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        
        tableShips = new JTable();
        tableShips.setModel(model);
        tableShips.getColumnModel().getColumn(0).setPreferredWidth(125);
        tableShips.getColumnModel().getColumn(1).setPreferredWidth(25);
        tableShips.getColumnModel().getColumn(2).setPreferredWidth(25);
        tableShips.getColumnModel().getColumn(3).setPreferredWidth(25);
        scrollPane.setViewportView(tableShips);
        
        JPanel panelMission = new JPanel();
        panelMission.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mission", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagConstraints gbc_panelMission = new GridBagConstraints();
        gbc_panelMission.gridheight = 7;
        gbc_panelMission.insets = new Insets(0, 0, 5, 0);
        gbc_panelMission.fill = GridBagConstraints.BOTH;
        gbc_panelMission.gridx = 1;
        gbc_panelMission.gridy = 0;
        add(panelMission, gbc_panelMission);
        GridBagLayout gbl_panelMission = new GridBagLayout();
        gbl_panelMission.columnWidths = new int[]{0, 0};
        gbl_panelMission.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        gbl_panelMission.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_panelMission.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelMission.setLayout(gbl_panelMission);
        
        JRadioButton rdbtnMissionExplore = new JRadioButton("Explore");
        GridBagConstraints gbc_rdbtnMissionExplore = new GridBagConstraints();
        gbc_rdbtnMissionExplore.anchor = GridBagConstraints.WEST;
        gbc_rdbtnMissionExplore.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnMissionExplore.gridx = 0;
        gbc_rdbtnMissionExplore.gridy = 0;
        panelMission.add(rdbtnMissionExplore, gbc_rdbtnMissionExplore);
        buttonGroup.add(rdbtnMissionExplore);
        
        JRadioButton rdbtnMissionTransport = new JRadioButton("Transport");
        GridBagConstraints gbc_rdbtnMissionTransport = new GridBagConstraints();
        gbc_rdbtnMissionTransport.anchor = GridBagConstraints.WEST;
        gbc_rdbtnMissionTransport.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnMissionTransport.gridx = 0;
        gbc_rdbtnMissionTransport.gridy = 1;
        panelMission.add(rdbtnMissionTransport, gbc_rdbtnMissionTransport);
        buttonGroup.add(rdbtnMissionTransport);
        
        JRadioButton rdbtnMissionDeploy = new JRadioButton("Deploy");
        GridBagConstraints gbc_rdbtnMissionDeploy = new GridBagConstraints();
        gbc_rdbtnMissionDeploy.anchor = GridBagConstraints.WEST;
        gbc_rdbtnMissionDeploy.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnMissionDeploy.gridx = 0;
        gbc_rdbtnMissionDeploy.gridy = 2;
        panelMission.add(rdbtnMissionDeploy, gbc_rdbtnMissionDeploy);
        buttonGroup.add(rdbtnMissionDeploy);
        
        JRadioButton rdbtnMissionAttack = new JRadioButton("Attack");
        buttonGroup.add(rdbtnMissionAttack);
        GridBagConstraints gbc_rdbtnMissionAttack = new GridBagConstraints();
        gbc_rdbtnMissionAttack.anchor = GridBagConstraints.WEST;
        gbc_rdbtnMissionAttack.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnMissionAttack.gridx = 0;
        gbc_rdbtnMissionAttack.gridy = 3;
        panelMission.add(rdbtnMissionAttack, gbc_rdbtnMissionAttack);
        
        JRadioButton rdbtnMissionSupport = new JRadioButton("Support");
        buttonGroup.add(rdbtnMissionSupport);
        GridBagConstraints gbc_rdbtnMissionSupport = new GridBagConstraints();
        gbc_rdbtnMissionSupport.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnMissionSupport.anchor = GridBagConstraints.WEST;
        gbc_rdbtnMissionSupport.gridx = 0;
        gbc_rdbtnMissionSupport.gridy = 4;
        panelMission.add(rdbtnMissionSupport, gbc_rdbtnMissionSupport);
        
        JRadioButton rdbtnSiege = new JRadioButton("Siege");
        buttonGroup.add(rdbtnSiege);
        GridBagConstraints gbc_rdbtnSiege = new GridBagConstraints();
        gbc_rdbtnSiege.anchor = GridBagConstraints.WEST;
        gbc_rdbtnSiege.gridx = 0;
        gbc_rdbtnSiege.gridy = 5;
        panelMission.add(rdbtnSiege, gbc_rdbtnSiege);
    }
}
