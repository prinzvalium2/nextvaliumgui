package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import javax.swing.JPanel;

import org.json.JSONException;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.nextcolony.Fleet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JTextField textFieldResourcesCoal;
    private JTextField textFieldResourcesOre;
    private JTextField textFieldResourcesCopper;
    private JTextField textFieldResourcesUranium;
    private JTextField textFieldResourcesShipCoal;
    private JTextField textFieldResourcesShipOre;
    private JTextField textFieldResourcesShipCopper;
    private JTextField textFieldResourcesShipUranium;
    private JTextField textFieldResourcesShipTotal;
    private JTextField textFieldResourcesFleetMax;
    private JComboBox<String> comboBoxTargetPlanet;
    JComboBox comboBoxMissionsPredefined;
    private HashMap<String, Planet> mapPlanets;
    
    private String[] predefinedMissions = {
            "",
            "Explore with explorer",
            "Deploy all ships", 
            "Deploy all explorers",
            "Deploy all corvettes",
            "Deploy all battleships",
            "Deploy all ships except explorers",
            "Attack with all corvettes",
            "Attack with all battleships",
            "Attack with all ships except explorers"
    };
    
    private String[] missions = {
            "",
            "Explore",
            "Deploy",
            "Attack",
            "Support",
            "Siege"
    };
    
    public PanelFleet(Planet planet) {
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
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
        
        comboBoxMissionsPredefined = new JComboBox(predefinedMissions);
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
        
        JComboBox comboBoxMissionsStandard = new JComboBox(missions);
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
        
        textFieldTargetUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> list = new ArrayList<String>();
                    mapPlanets = Planets.loadUserPlanets(textFieldTargetUser.getText());
                    mapPlanets.forEach((planetId, planet) -> list.add(planet.getName()));
                    Collections.sort(list);
                    comboBoxTargetPlanet.removeAllItems();
                    list.forEach(planetName -> comboBoxTargetPlanet.addItem(planetName));
                    
                } catch (JSONException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JLabel lblTargetPlanet = new JLabel("Planet:");
        GridBagConstraints gbc_lblTargetPlanet = new GridBagConstraints();
        gbc_lblTargetPlanet.anchor = GridBagConstraints.EAST;
        gbc_lblTargetPlanet.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetPlanet.gridx = 0;
        gbc_lblTargetPlanet.gridy = 1;
        panelTarget.add(lblTargetPlanet, gbc_lblTargetPlanet);
        
        comboBoxTargetPlanet = new JComboBox<String>();
        GridBagConstraints gbc_comboBoxTargetPlanet = new GridBagConstraints();
        gbc_comboBoxTargetPlanet.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxTargetPlanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxTargetPlanet.gridx = 1;
        gbc_comboBoxTargetPlanet.gridy = 1;
        panelTarget.add(comboBoxTargetPlanet, gbc_comboBoxTargetPlanet);

        comboBoxTargetPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comboBoxTargetPlanet.getSelectedItem() == null)
                    return;
                
                String planetName = comboBoxTargetPlanet.getSelectedItem().toString();
                mapPlanets.forEach((planetId, planet) -> {
                    if (planet.getName().equalsIgnoreCase(planetName)) {
                        textFieldTargetPositionX.setText(Integer.toString(planet.getPosX()));
                        textFieldTargetPositionY.setText(Integer.toString(planet.getPosY()));
                    }
                });
            }
        });
        
        
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
        
        JPanel panelResources = new JPanel();
        panelResources.setBorder(new TitledBorder(null, "Resources", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_panelResources = new GridBagConstraints();
        gbc_panelResources.insets = new Insets(0, 0, 5, 0);
        gbc_panelResources.fill = GridBagConstraints.BOTH;
        gbc_panelResources.gridx = 1;
        gbc_panelResources.gridy = 2;
        add(panelResources, gbc_panelResources);
        GridBagLayout gbl_panelResources = new GridBagLayout();
        gbl_panelResources.columnWidths = new int[]{70, 0, 0, 0};
        gbl_panelResources.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelResources.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
        gbl_panelResources.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        panelResources.setLayout(gbl_panelResources);
        
        JLabel lblPlanet = new JLabel("Planet");
        GridBagConstraints gbc_lblPlanet = new GridBagConstraints();
        gbc_lblPlanet.insets = new Insets(0, 0, 5, 5);
        gbc_lblPlanet.gridx = 1;
        gbc_lblPlanet.gridy = 0;
        panelResources.add(lblPlanet, gbc_lblPlanet);
        
        JLabel lblShip = new JLabel("Fleet");
        GridBagConstraints gbc_lblShip = new GridBagConstraints();
        gbc_lblShip.insets = new Insets(0, 0, 5, 0);
        gbc_lblShip.gridx = 2;
        gbc_lblShip.gridy = 0;
        panelResources.add(lblShip, gbc_lblShip);
        
        JLabel lblRessourcesCoal = new JLabel("Coal:");
        GridBagConstraints gbc_lblRessourcesCoal = new GridBagConstraints();
        gbc_lblRessourcesCoal.insets = new Insets(0, 0, 5, 5);
        gbc_lblRessourcesCoal.anchor = GridBagConstraints.EAST;
        gbc_lblRessourcesCoal.gridx = 0;
        gbc_lblRessourcesCoal.gridy = 1;
        panelResources.add(lblRessourcesCoal, gbc_lblRessourcesCoal);
        
        textFieldResourcesCoal = new JTextField();
        textFieldResourcesCoal.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesCoal = new GridBagConstraints();
        gbc_textFieldResourcesCoal.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesCoal.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesCoal.gridx = 1;
        gbc_textFieldResourcesCoal.gridy = 1;
        panelResources.add(textFieldResourcesCoal, gbc_textFieldResourcesCoal);
        textFieldResourcesCoal.setColumns(10);
        
        textFieldResourcesShipCoal = new JTextField();
        GridBagConstraints gbc_textFieldResourcesShipCoal = new GridBagConstraints();
        gbc_textFieldResourcesShipCoal.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesShipCoal.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesShipCoal.gridx = 2;
        gbc_textFieldResourcesShipCoal.gridy = 1;
        panelResources.add(textFieldResourcesShipCoal, gbc_textFieldResourcesShipCoal);
        textFieldResourcesShipCoal.setColumns(10);
        
        JLabel lblRessourcesOre = new JLabel("Ore:");
        GridBagConstraints gbc_lblRessourcesOre = new GridBagConstraints();
        gbc_lblRessourcesOre.anchor = GridBagConstraints.EAST;
        gbc_lblRessourcesOre.insets = new Insets(0, 0, 5, 5);
        gbc_lblRessourcesOre.gridx = 0;
        gbc_lblRessourcesOre.gridy = 2;
        panelResources.add(lblRessourcesOre, gbc_lblRessourcesOre);
        
        textFieldResourcesOre = new JTextField();
        textFieldResourcesOre.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesOre = new GridBagConstraints();
        gbc_textFieldResourcesOre.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesOre.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesOre.gridx = 1;
        gbc_textFieldResourcesOre.gridy = 2;
        panelResources.add(textFieldResourcesOre, gbc_textFieldResourcesOre);
        textFieldResourcesOre.setColumns(10);
        
        textFieldResourcesShipOre = new JTextField();
        GridBagConstraints gbc_textFieldResourcesShipOre = new GridBagConstraints();
        gbc_textFieldResourcesShipOre.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesShipOre.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesShipOre.gridx = 2;
        gbc_textFieldResourcesShipOre.gridy = 2;
        panelResources.add(textFieldResourcesShipOre, gbc_textFieldResourcesShipOre);
        textFieldResourcesShipOre.setColumns(10);
        
        JLabel lblResourcesCopper = new JLabel("Copper:");
        GridBagConstraints gbc_lblResourcesCopper = new GridBagConstraints();
        gbc_lblResourcesCopper.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesCopper.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesCopper.gridx = 0;
        gbc_lblResourcesCopper.gridy = 3;
        panelResources.add(lblResourcesCopper, gbc_lblResourcesCopper);
        
        textFieldResourcesCopper = new JTextField();
        textFieldResourcesCopper.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesCopper = new GridBagConstraints();
        gbc_textFieldResourcesCopper.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesCopper.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesCopper.gridx = 1;
        gbc_textFieldResourcesCopper.gridy = 3;
        panelResources.add(textFieldResourcesCopper, gbc_textFieldResourcesCopper);
        textFieldResourcesCopper.setColumns(10);
        
        textFieldResourcesShipCopper = new JTextField();
        GridBagConstraints gbc_textFieldResourcesShipCopper = new GridBagConstraints();
        gbc_textFieldResourcesShipCopper.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesShipCopper.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesShipCopper.gridx = 2;
        gbc_textFieldResourcesShipCopper.gridy = 3;
        panelResources.add(textFieldResourcesShipCopper, gbc_textFieldResourcesShipCopper);
        textFieldResourcesShipCopper.setColumns(10);
        
        JLabel lblResourcesUranium = new JLabel("Uranium:");
        GridBagConstraints gbc_lblResourcesUranium = new GridBagConstraints();
        gbc_lblResourcesUranium.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesUranium.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesUranium.gridx = 0;
        gbc_lblResourcesUranium.gridy = 4;
        panelResources.add(lblResourcesUranium, gbc_lblResourcesUranium);
        
        textFieldResourcesUranium = new JTextField();
        textFieldResourcesUranium.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesUranium = new GridBagConstraints();
        gbc_textFieldResourcesUranium.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesUranium.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesUranium.gridx = 1;
        gbc_textFieldResourcesUranium.gridy = 4;
        panelResources.add(textFieldResourcesUranium, gbc_textFieldResourcesUranium);
        textFieldResourcesUranium.setColumns(10);
        
        textFieldResourcesShipUranium = new JTextField();
        GridBagConstraints gbc_textFieldResourcesShipUranium = new GridBagConstraints();
        gbc_textFieldResourcesShipUranium.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesShipUranium.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesShipUranium.gridx = 2;
        gbc_textFieldResourcesShipUranium.gridy = 4;
        panelResources.add(textFieldResourcesShipUranium, gbc_textFieldResourcesShipUranium);
        textFieldResourcesShipUranium.setColumns(10);
        
        JLabel lblResourcesShipTotal = new JLabel("Total:");
        GridBagConstraints gbc_lblResourcesShipTotal = new GridBagConstraints();
        gbc_lblResourcesShipTotal.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesShipTotal.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesShipTotal.gridx = 1;
        gbc_lblResourcesShipTotal.gridy = 5;
        panelResources.add(lblResourcesShipTotal, gbc_lblResourcesShipTotal);
        
        textFieldResourcesShipTotal = new JTextField();
        textFieldResourcesShipTotal.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesShipTotal = new GridBagConstraints();
        gbc_textFieldResourcesShipTotal.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesShipTotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesShipTotal.gridx = 2;
        gbc_textFieldResourcesShipTotal.gridy = 5;
        panelResources.add(textFieldResourcesShipTotal, gbc_textFieldResourcesShipTotal);
        textFieldResourcesShipTotal.setColumns(10);
        
        JLabel lblResourcesFleetMax = new JLabel("Max:");
        GridBagConstraints gbc_lblResourcesFleetMax = new GridBagConstraints();
        gbc_lblResourcesFleetMax.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesFleetMax.insets = new Insets(0, 0, 0, 5);
        gbc_lblResourcesFleetMax.gridx = 1;
        gbc_lblResourcesFleetMax.gridy = 6;
        panelResources.add(lblResourcesFleetMax, gbc_lblResourcesFleetMax);
        
        textFieldResourcesFleetMax = new JTextField();
        textFieldResourcesFleetMax.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesFleetMax = new GridBagConstraints();
        gbc_textFieldResourcesFleetMax.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesFleetMax.gridx = 2;
        gbc_textFieldResourcesFleetMax.gridy = 6;
        panelResources.add(textFieldResourcesFleetMax, gbc_textFieldResourcesFleetMax);
        textFieldResourcesFleetMax.setColumns(10);
        
        JButton btnSendTransaction = new JButton("Send Transaction to Steem");
        GridBagConstraints gbc_btnSendTransaction = new GridBagConstraints();
        gbc_btnSendTransaction.insets = new Insets(0, 0, 5, 0);
        gbc_btnSendTransaction.gridx = 1;
        gbc_btnSendTransaction.gridy = 3;
        add(btnSendTransaction, gbc_btnSendTransaction);
        
        setTarget();
    }
    
    private void setTarget() {
        
        Planet targetPlanet = NextValiumGui.getNextValiumGui().getPlanetMarkedAsTarget();      
        if (targetPlanet == null)
            return;
        
        textFieldTargetUser.setText(targetPlanet.getUserName());
        textFieldTargetUser.postActionEvent();
        comboBoxTargetPlanet.setSelectedItem(targetPlanet.getName());
    }
}
