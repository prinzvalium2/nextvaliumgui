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
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class PanelFleet extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableShips;
    private JTextField textFieldTargetUser;
    private JTextField textFieldTargetPositionX;
    private JTextField textFieldTargetPositionY;
    
    public PanelFleet(Planet planet) {
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[]{0.0, 1.0};
        gridBagLayout.columnWidths = new int[]{189, 0};
        setLayout(gridBagLayout);
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridheight = 4;
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
        
        JPanel panelMissions = new JPanel();
        panelMissions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Missions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagConstraints gbc_panelMissions = new GridBagConstraints();
        gbc_panelMissions.insets = new Insets(0, 0, 5, 0);
        gbc_panelMissions.fill = GridBagConstraints.BOTH;
        gbc_panelMissions.gridx = 1;
        gbc_panelMissions.gridy = 0;
        add(panelMissions, gbc_panelMissions);
        GridBagLayout gbl_panelMissions = new GridBagLayout();
        gbl_panelMissions.columnWidths = new int[]{70, 0};
        gbl_panelMissions.columnWeights = new double[]{0.0, 1.0};
        panelMissions.setLayout(gbl_panelMissions);
        
        JLabel lblMissionsPredefined = new JLabel("Predefined:");
        GridBagConstraints gbc_lblMissionsPredefined = new GridBagConstraints();
        gbc_lblMissionsPredefined.anchor = GridBagConstraints.EAST;
        gbc_lblMissionsPredefined.insets = new Insets(0, 0, 5, 5);
        gbc_lblMissionsPredefined.gridx = 0;
        gbc_lblMissionsPredefined.gridy = 0;
        panelMissions.add(lblMissionsPredefined, gbc_lblMissionsPredefined);
        
        JComboBox comboBoxMissionsPredefined = new JComboBox();
        GridBagConstraints gbc_comboBoxMissionsPredefined = new GridBagConstraints();
        gbc_comboBoxMissionsPredefined.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxMissionsPredefined.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxMissionsPredefined.gridx = 1;
        gbc_comboBoxMissionsPredefined.gridy = 0;
        panelMissions.add(comboBoxMissionsPredefined, gbc_comboBoxMissionsPredefined);
        
        JLabel lblMissionsStandard = new JLabel("Standard:");
        GridBagConstraints gbc_lblMissionsStandard = new GridBagConstraints();
        gbc_lblMissionsStandard.anchor = GridBagConstraints.EAST;
        gbc_lblMissionsStandard.insets = new Insets(0, 0, 0, 5);
        gbc_lblMissionsStandard.gridx = 0;
        gbc_lblMissionsStandard.gridy = 1;
        panelMissions.add(lblMissionsStandard, gbc_lblMissionsStandard);
        
        JComboBox comboBoxMissionsStandard = new JComboBox();
        GridBagConstraints gbc_comboBoxMissionsStandard = new GridBagConstraints();
        gbc_comboBoxMissionsStandard.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxMissionsStandard.gridx = 1;
        gbc_comboBoxMissionsStandard.gridy = 1;
        panelMissions.add(comboBoxMissionsStandard, gbc_comboBoxMissionsStandard);
        
        JPanel panelTarget = new JPanel();
        panelTarget.setBorder(new TitledBorder(null, "Target", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_panelTarget = new GridBagConstraints();
        gbc_panelTarget.insets = new Insets(0, 0, 5, 0);
        gbc_panelTarget.fill = GridBagConstraints.BOTH;
        gbc_panelTarget.gridx = 1;
        gbc_panelTarget.gridy = 1;
        add(panelTarget, gbc_panelTarget);
        GridBagLayout gbl_panelTarget = new GridBagLayout();
        gbl_panelTarget.columnWidths = new int[]{70, 0, 0};
        gbl_panelTarget.rowHeights = new int[]{0, 0, 0, 0};
        gbl_panelTarget.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelTarget.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        panelTarget.setLayout(gbl_panelTarget);
        
        JLabel lblTargetUser = new JLabel("User:");
        GridBagConstraints gbc_lblTargetUser = new GridBagConstraints();
        gbc_lblTargetUser.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetUser.anchor = GridBagConstraints.EAST;
        gbc_lblTargetUser.gridx = 0;
        gbc_lblTargetUser.gridy = 0;
        panelTarget.add(lblTargetUser, gbc_lblTargetUser);
        
        textFieldTargetUser = new JTextField();
        GridBagConstraints gbc_textFieldTargetUser = new GridBagConstraints();
        gbc_textFieldTargetUser.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldTargetUser.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldTargetUser.gridx = 1;
        gbc_textFieldTargetUser.gridy = 0;
        panelTarget.add(textFieldTargetUser, gbc_textFieldTargetUser);
        textFieldTargetUser.setColumns(10);
        
        JLabel lblTargetPlanet = new JLabel("Planet:");
        GridBagConstraints gbc_lblTargetPlanet = new GridBagConstraints();
        gbc_lblTargetPlanet.anchor = GridBagConstraints.EAST;
        gbc_lblTargetPlanet.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetPlanet.gridx = 0;
        gbc_lblTargetPlanet.gridy = 1;
        panelTarget.add(lblTargetPlanet, gbc_lblTargetPlanet);
        
        JComboBox comboBoxTargetPlanet = new JComboBox();
        GridBagConstraints gbc_comboBoxTargetPlanet = new GridBagConstraints();
        gbc_comboBoxTargetPlanet.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxTargetPlanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxTargetPlanet.gridx = 1;
        gbc_comboBoxTargetPlanet.gridy = 1;
        panelTarget.add(comboBoxTargetPlanet, gbc_comboBoxTargetPlanet);
        
        JLabel lblTargetPosition = new JLabel("Position:");
        GridBagConstraints gbc_lblTargetPosition = new GridBagConstraints();
        gbc_lblTargetPosition.anchor = GridBagConstraints.EAST;
        gbc_lblTargetPosition.insets = new Insets(0, 0, 0, 5);
        gbc_lblTargetPosition.gridx = 0;
        gbc_lblTargetPosition.gridy = 2;
        panelTarget.add(lblTargetPosition, gbc_lblTargetPosition);
        
        JPanel panelTargetPosition = new JPanel();
        GridBagConstraints gbc_panelTargetPosition = new GridBagConstraints();
        gbc_panelTargetPosition.fill = GridBagConstraints.BOTH;
        gbc_panelTargetPosition.gridx = 1;
        gbc_panelTargetPosition.gridy = 2;
        panelTarget.add(panelTargetPosition, gbc_panelTargetPosition);
        
        JLabel lblTargetPositionX = new JLabel("X:");
        panelTargetPosition.add(lblTargetPositionX);
        
        textFieldTargetPositionX = new JTextField();
        panelTargetPosition.add(textFieldTargetPositionX);
        textFieldTargetPositionX.setColumns(10);
        
        JLabel lblTargetPositionY = new JLabel("Y:");
        panelTargetPosition.add(lblTargetPositionY);
        
        textFieldTargetPositionY = new JTextField();
        panelTargetPosition.add(textFieldTargetPositionY);
        textFieldTargetPositionY.setColumns(10);
        
        JButton btnSendTransaction = new JButton("Send Transaction to Steem");
        GridBagConstraints gbc_btnSendTransaction = new GridBagConstraints();
        gbc_btnSendTransaction.gridx = 1;
        gbc_btnSendTransaction.gridy = 2;
        add(btnSendTransaction, gbc_btnSendTransaction);
    }
}
