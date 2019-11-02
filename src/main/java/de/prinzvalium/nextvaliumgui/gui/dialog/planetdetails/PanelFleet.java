package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import javax.swing.JPanel;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.CustomJson;
import de.prinzvalium.nextvaliumgui.lib.NextValiumException;
import de.prinzvalium.nextvaliumgui.lib.SteemUtil;
import de.prinzvalium.nextvaliumgui.nextcolony.Fleet;
import de.prinzvalium.nextvaliumgui.nextcolony.MissionsUser;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;
import de.prinzvalium.nextvaliumgui.nextcolony.Resources;
import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantities;
import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantitiesRessources;
import de.prinzvalium.nextvaliumgui.nextcolony.Skills;

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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
    private JComboBox comboBoxMissionsPredefined;
    private JComboBox comboBoxMissionsStandard;
    private JButton btnSendTransaction;
    private HashMap<String, Planet> mapPlanets;
    private DefaultTableModel model;
    private Planet planet;
    private Planet targetPlanet = null;
    private DialogPlanet dialogPlanet;
    private Fleet fleet = null;
    private boolean predefinedMissionSelected = false;
    private int missionsAvailable = 0;
    private double doubleConsumption = 0;
    private int numberOfShips = 0;
    
    private final String PREDEFINED_MISSION_NONE = "";
    
    private final String PREDEFINED_MISSION_TRANSPORT_FAST = "Transport fast with corvettes";
    
    private final String PREDEFINED_MISSION_DEPLOY_ALL = "Deploy all ships";
    private final String PREDEFINED_MISSION_DEPLOY_ALL_EXP = "Deploy all explorers";
    private final String PREDEFINED_MISSION_DEPLOY_ALL_CORVETTES = "Deploy all corvettes";
    private final String PREDEFINED_MISSION_DEPLOY_ALL_BATTLESHIPS = "Deploy all battleships";
    private final String PREDEFINED_MISSION_DEPLOY_ALL_BATTLESHIPS_AND_TRANSPORTER = "Deploy all battleships and transporter";
    private final String PREDEFINED_MISSION_DEPLOY_ALL_EXCEPT_EXP = "Deploy all ships except explorers";

    private final String PREDEFINED_MISSION_EXPLORE_EXP = "Explore with Explorer";

    
    private final String PREDEFINED_MISSION_ATTACK_WITH_ALL_CORVETTES = "Attack with all corvettes";
    private final String PREDEFINED_MISSION_EXPLORE_EXP2 = "Explore with Explorer II";
    

    private String[] predefinedMissions = {
            PREDEFINED_MISSION_NONE,
            PREDEFINED_MISSION_TRANSPORT_FAST,
            PREDEFINED_MISSION_DEPLOY_ALL, 
            PREDEFINED_MISSION_DEPLOY_ALL_EXP,
            PREDEFINED_MISSION_DEPLOY_ALL_CORVETTES,
            PREDEFINED_MISSION_DEPLOY_ALL_BATTLESHIPS,
            PREDEFINED_MISSION_DEPLOY_ALL_BATTLESHIPS_AND_TRANSPORTER,
            PREDEFINED_MISSION_DEPLOY_ALL_EXCEPT_EXP,
            PREDEFINED_MISSION_ATTACK_WITH_ALL_CORVETTES,
            PREDEFINED_MISSION_EXPLORE_EXP,
            PREDEFINED_MISSION_EXPLORE_EXP2
    };
    
    private final String MISSION_NONE = "";
    private final String MISSION_TRANSPORT = "Transport";
    private final String MISSION_DEPLOY = "Deploy";
    private final String MISSION_ATTACK = "Attack";
    private final String MISSION_EXPLORE = "Explore";
    
    private String[] missions = {
            MISSION_NONE,
            MISSION_DEPLOY,
            MISSION_TRANSPORT,
            MISSION_ATTACK,
//            "Support",
//            "Siege"
//          "Break Siege"
            MISSION_EXPLORE
    };
    private JTextField textFieldFreeMissions;
    private JTextField textFieldUraniumConsumption;
    
    public PanelFleet(DialogPlanet dialogPlanet, Planet planet) {
        
        this.dialogPlanet = dialogPlanet;
        this.planet = planet;
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0};
        gridBagLayout.columnWidths = new int[]{189, 0};
        setLayout(gridBagLayout);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridheight = 4;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        add(scrollPane, gbc_scrollPane);
        
        model = new DefaultTableModel(
                new Object[][] {
                    {"loading data...", null, null, null},
                },
                new String[] {
                    "Ship type", "Planet", "Use", "Pos"
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
        
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent arg0) {
                tableChanged_Fleet();
                checkPreconditionSendToSteemButton();
            }});
        
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
        
        JLabel lblFreeMissions = new JLabel("Available:");
        GridBagConstraints gbc_lblFreeMissions = new GridBagConstraints();
        gbc_lblFreeMissions.anchor = GridBagConstraints.EAST;
        gbc_lblFreeMissions.insets = new Insets(0, 0, 5, 5);
        gbc_lblFreeMissions.gridx = 0;
        gbc_lblFreeMissions.gridy = 0;
        panelMissions.add(lblFreeMissions, gbc_lblFreeMissions);
        
        textFieldFreeMissions = new JTextField();
        textFieldFreeMissions.setEditable(false);
        GridBagConstraints gbc_textFieldFreeMissions = new GridBagConstraints();
        gbc_textFieldFreeMissions.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldFreeMissions.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldFreeMissions.gridx = 1;
        gbc_textFieldFreeMissions.gridy = 0;
        panelMissions.add(textFieldFreeMissions, gbc_textFieldFreeMissions);
        textFieldFreeMissions.setColumns(10);
        
        JLabel lblMissionsPredefined = new JLabel("Predefined:");
        GridBagConstraints gbc_lblMissionsPredefined = new GridBagConstraints();
        gbc_lblMissionsPredefined.anchor = GridBagConstraints.EAST;
        gbc_lblMissionsPredefined.insets = new Insets(0, 0, 5, 5);
        gbc_lblMissionsPredefined.gridx = 0;
        gbc_lblMissionsPredefined.gridy = 1;
        panelMissions.add(lblMissionsPredefined, gbc_lblMissionsPredefined);
        
        comboBoxMissionsPredefined = new JComboBox(predefinedMissions);
        comboBoxMissionsPredefined.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!((String)comboBoxMissionsPredefined.getSelectedItem()).equalsIgnoreCase(PREDEFINED_MISSION_NONE))
                    actionPerformed_comboBoxMissionsPredefined();
            }
        });
        
        GridBagConstraints gbc_comboBoxMissionsPredefined = new GridBagConstraints();
        gbc_comboBoxMissionsPredefined.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxMissionsPredefined.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxMissionsPredefined.gridx = 1;
        gbc_comboBoxMissionsPredefined.gridy = 1;
        panelMissions.add(comboBoxMissionsPredefined, gbc_comboBoxMissionsPredefined);
        
        JLabel lblMissionsStandard = new JLabel("Type:");
        GridBagConstraints gbc_lblMissionsStandard = new GridBagConstraints();
        gbc_lblMissionsStandard.anchor = GridBagConstraints.EAST;
        gbc_lblMissionsStandard.insets = new Insets(0, 0, 0, 5);
        gbc_lblMissionsStandard.gridx = 0;
        gbc_lblMissionsStandard.gridy = 2;
        panelMissions.add(lblMissionsStandard, gbc_lblMissionsStandard);
        
        comboBoxMissionsStandard = new JComboBox(missions);
        comboBoxMissionsStandard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (predefinedMissionSelected) {
                    predefinedMissionSelected = false;
                } else {
                    if (!((String)comboBoxMissionsPredefined.getSelectedItem()).equalsIgnoreCase(PREDEFINED_MISSION_NONE))
                        comboBoxMissionsPredefined.setSelectedItem(PREDEFINED_MISSION_NONE);
                }
                tableChanged_Fleet();
                checkPreconditionSendToSteemButton();
            }
        });
        GridBagConstraints gbc_comboBoxMissionsStandard = new GridBagConstraints();
        gbc_comboBoxMissionsStandard.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxMissionsStandard.gridx = 1;
        gbc_comboBoxMissionsStandard.gridy = 2;
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
                    comboBoxTargetPlanet.removeAllItems();
                    String userTarget = textFieldTargetUser.getText();
                    if (userTarget == null || userTarget.isEmpty())
                        return;

                    mapPlanets = Planets.loadUserPlanets(userTarget);
                    ArrayList<String> list = new ArrayList<String>();
                    mapPlanets.forEach((planetId, planet) -> list.add(planet.getName()));
                    Collections.sort(list);
                    list.forEach(planetName -> comboBoxTargetPlanet.addItem(planetName));
                    
                } catch (Exception e1) {
                    dialogPlanet.setStatusError(e1.getClass().getSimpleName() + ": " + e1.getMessage());
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
                        targetPlanet = planet;
                        textFieldTargetPositionX.setText(Integer.toString(planet.getPosX()));
                        textFieldTargetPositionY.setText(Integer.toString(planet.getPosY()));
                    }
                });
                tableChanged_Fleet();
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
        textFieldTargetPositionX.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                if (targetPlanet != null) {
                    int planetX = targetPlanet.getPosX();
                    int targetX = 0;
                    try {
                        targetX = Integer.parseInt(textFieldTargetPositionX.getText());
                    }
                    catch (Exception e) {
                    }
                    if (planetX != targetX) {
                        textFieldTargetUser.setText("");
                        comboBoxTargetPlanet.removeAllItems();
                        targetPlanet = null;
                    }
                }
                tableChanged_Fleet();
                checkPreconditionSendToSteemButton();
            }
        });
        panelTargetPosition.add(textFieldTargetPositionX);
        textFieldTargetPositionX.setColumns(10);
        
        JLabel lblTargetPositionY = new JLabel("Y:");
        panelTargetPosition.add(lblTargetPositionY);
        
        textFieldTargetPositionY = new JTextField();
        textFieldTargetPositionY.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                if (targetPlanet != null) {
                    int planetY = targetPlanet.getPosY();
                    int targetY = 0;
                    try {
                        targetY = Integer.parseInt(textFieldTargetPositionY.getText());
                    }
                    catch (Exception e) {
                    }
                    if (planetY != targetY) {
                        textFieldTargetUser.setText("");
                        comboBoxTargetPlanet.removeAllItems();
                        targetPlanet = null;
                    }
                }
                tableChanged_Fleet();
                checkPreconditionSendToSteemButton();
            }
        });
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
        gbl_panelResources.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelResources.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
        gbl_panelResources.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
        textFieldResourcesShipCoal.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                actionPerformed_Ressources();
                checkPreconditionSendToSteemButton();
            }
        });
        textFieldResourcesShipCoal.setText("0");
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
        textFieldResourcesShipOre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                actionPerformed_Ressources();
                checkPreconditionSendToSteemButton();
            }
        });
        textFieldResourcesShipOre.setText("0");
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
        textFieldResourcesShipCopper.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                actionPerformed_Ressources();
                checkPreconditionSendToSteemButton();
            }
        });
        textFieldResourcesShipCopper.setText("0");
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
        textFieldResourcesShipUranium.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                actionPerformed_Ressources();
                checkPreconditionSendToSteemButton();
            }
        });
        textFieldResourcesShipUranium.setText("0");
        GridBagConstraints gbc_textFieldResourcesShipUranium = new GridBagConstraints();
        gbc_textFieldResourcesShipUranium.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesShipUranium.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesShipUranium.gridx = 2;
        gbc_textFieldResourcesShipUranium.gridy = 4;
        panelResources.add(textFieldResourcesShipUranium, gbc_textFieldResourcesShipUranium);
        textFieldResourcesShipUranium.setColumns(10);
        
        JLabel lblResourcesShipTotal = new JLabel("Resources total:");
        GridBagConstraints gbc_lblResourcesShipTotal = new GridBagConstraints();
        gbc_lblResourcesShipTotal.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesShipTotal.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesShipTotal.gridx = 1;
        gbc_lblResourcesShipTotal.gridy = 5;
        panelResources.add(lblResourcesShipTotal, gbc_lblResourcesShipTotal);
        
        textFieldResourcesShipTotal = new JTextField();
        textFieldResourcesShipTotal.setText("0");
        textFieldResourcesShipTotal.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesShipTotal = new GridBagConstraints();
        gbc_textFieldResourcesShipTotal.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesShipTotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesShipTotal.gridx = 2;
        gbc_textFieldResourcesShipTotal.gridy = 5;
        panelResources.add(textFieldResourcesShipTotal, gbc_textFieldResourcesShipTotal);
        textFieldResourcesShipTotal.setColumns(10);
        
        JLabel lblResourcesFleetMax = new JLabel("Capacity max:");
        GridBagConstraints gbc_lblResourcesFleetMax = new GridBagConstraints();
        gbc_lblResourcesFleetMax.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesFleetMax.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesFleetMax.gridx = 1;
        gbc_lblResourcesFleetMax.gridy = 6;
        panelResources.add(lblResourcesFleetMax, gbc_lblResourcesFleetMax);
        
        textFieldResourcesFleetMax = new JTextField();
        textFieldResourcesFleetMax.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesFleetMax = new GridBagConstraints();
        gbc_textFieldResourcesFleetMax.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldResourcesFleetMax.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesFleetMax.gridx = 2;
        gbc_textFieldResourcesFleetMax.gridy = 6;
        panelResources.add(textFieldResourcesFleetMax, gbc_textFieldResourcesFleetMax);
        textFieldResourcesFleetMax.setColumns(10);
        
        JLabel lblUramiumConsumption = new JLabel("Uranium consumption:");
        GridBagConstraints gbc_lblUramiumConsumption = new GridBagConstraints();
        gbc_lblUramiumConsumption.insets = new Insets(0, 0, 0, 5);
        gbc_lblUramiumConsumption.anchor = GridBagConstraints.EAST;
        gbc_lblUramiumConsumption.gridx = 1;
        gbc_lblUramiumConsumption.gridy = 7;
        panelResources.add(lblUramiumConsumption, gbc_lblUramiumConsumption);
        
        textFieldUraniumConsumption = new JTextField();
        textFieldUraniumConsumption.setEditable(false);
        GridBagConstraints gbc_textFieldUraniumConsumption = new GridBagConstraints();
        gbc_textFieldUraniumConsumption.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldUraniumConsumption.gridx = 2;
        gbc_textFieldUraniumConsumption.gridy = 7;
        panelResources.add(textFieldUraniumConsumption, gbc_textFieldUraniumConsumption);
        textFieldUraniumConsumption.setColumns(10);
        
        btnSendTransaction = new JButton("Send transaction to Steem");
        btnSendTransaction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                actionPerformed_btnSendTransaction();
            }
        });
        GridBagConstraints gbc_btnSendTransaction = new GridBagConstraints();
        gbc_btnSendTransaction.insets = new Insets(0, 0, 5, 0);
        gbc_btnSendTransaction.gridx = 1;
        gbc_btnSendTransaction.gridy = 3;
        add(btnSendTransaction, gbc_btnSendTransaction);
        
        setTarget();
        
        checkPreconditionSendToSteemButton();
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String userName = planet.getUserName();
                    String planetId = planet.getId();
                    String planetName = planet.getName();
                    
                    fleet = new Fleet(userName, planetName, planetId);
                    HashMap<String, Integer> mapShips = fleet.getNumberOfShipTypesInShipyard();
                    model.removeRow(0);
                    for (Map.Entry<?,?> entry : mapShips.entrySet())
                        model.addRow(new Object[] { entry.getKey(), entry.getValue(), null, null });
                    
                    RessourceQuantitiesRessources res = RessourceQuantities.loadRessourceQuantites(planetName, planetId);
                    textFieldResourcesCoal.setText(String.format("%.0f", res.getCoal()));
                    textFieldResourcesOre.setText(String.format("%.0f", res.getOre()));
                    textFieldResourcesCopper.setText(String.format("%.0f", res.getCopper()));
                    textFieldResourcesUranium.setText(String.format("%.0f", res.getUranium()));
                    
                    int starter = planet.isStarter() ? 1 : 0;
                    int missionsActive = MissionsUser.loadAllActiveUserMissions(userName).size();
                    int missionsMax = new Skills(userName).getMissionControlLevel() * 2 + starter;
                    missionsAvailable = missionsMax - missionsActive;
                    textFieldFreeMissions.setText(missionsAvailable + " / " + missionsMax);

                } catch (Exception e) {
                    dialogPlanet.setStatusError(e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }).start();
    }
    
    private void setTarget() {
        
        Planet targetPlanet = NextValiumGui.getNextValiumGui().getPlanetMarkedAsTarget();      
        if (targetPlanet == null)
            return;
        
        textFieldTargetUser.setText(targetPlanet.getUserName());
        textFieldTargetUser.postActionEvent();
        comboBoxTargetPlanet.setSelectedItem(targetPlanet.getName());
    }
    
    private void actionPerformed_comboBoxMissionsPredefined() {
        
        predefinedMissionSelected = true;
        
        // Reset all values in table
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(null, i, 2);
            model.setValueAt(null, i, 3);
        }
        
        switch ((String)comboBoxMissionsPredefined.getSelectedItem()) {
        
        // Explore
        
        case PREDEFINED_MISSION_EXPLORE_EXP:
            for (int i = 0; i < model.getRowCount(); i++) {
                if (((String)model.getValueAt(i, 0)).equalsIgnoreCase("explorership")) 
                    model.setValueAt(1, i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_EXPLORE);
            break;
            
        case PREDEFINED_MISSION_EXPLORE_EXP2:
            for (int i = 0; i < model.getRowCount(); i++) {
                if (((String)model.getValueAt(i, 0)).equalsIgnoreCase("explorership1"))
                    model.setValueAt(1, i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_EXPLORE);
            break;
            
            
        // DEPLOY
            
        case PREDEFINED_MISSION_DEPLOY_ALL:
            for (int i = 0; i < model.getRowCount(); i++)
                model.setValueAt(model.getValueAt(i, 1), i, 2);
            comboBoxMissionsStandard.setSelectedItem(MISSION_DEPLOY);
            break;
            
        case PREDEFINED_MISSION_DEPLOY_ALL_EXP:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (ship.equalsIgnoreCase("explorership"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_DEPLOY);
            break;
            
        case PREDEFINED_MISSION_DEPLOY_ALL_CORVETTES:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (ship.equalsIgnoreCase("corvette") || ship.equalsIgnoreCase("corvette1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_DEPLOY);
            break;
            
        case PREDEFINED_MISSION_DEPLOY_ALL_BATTLESHIPS:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (!ship.equalsIgnoreCase("explorership") && 
                        !ship.equalsIgnoreCase("explorership1") &&
                        !ship.equalsIgnoreCase("transportship") &&
                        !ship.equalsIgnoreCase("transportship1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_DEPLOY);
            break;
            
        case PREDEFINED_MISSION_DEPLOY_ALL_BATTLESHIPS_AND_TRANSPORTER:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (!ship.equalsIgnoreCase("explorership") && 
                        !ship.equalsIgnoreCase("explorership1") &&
                        !ship.equalsIgnoreCase("transportship1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_DEPLOY);
            break;
            
        case PREDEFINED_MISSION_DEPLOY_ALL_EXCEPT_EXP:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (!ship.equalsIgnoreCase("explorership") && !ship.equalsIgnoreCase("explorership1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_DEPLOY);
            break;
        
        // Transport
        
        case PREDEFINED_MISSION_TRANSPORT_FAST:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (ship.equalsIgnoreCase("corvette") || ship.equalsIgnoreCase("corvette1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            comboBoxMissionsStandard.setSelectedItem(MISSION_TRANSPORT);
            break;
            
        }
    }
    
    private void actionPerformed_btnSendTransaction() {
        
        try {
                
            int x = Integer.parseInt(textFieldTargetPositionX.getText());
            int y = Integer.parseInt(textFieldTargetPositionY.getText());
            
            Resources resources = new Resources();
            resources.coal = Integer.parseInt(textFieldResourcesShipCoal.getText());
            resources.ore = Integer.parseInt(textFieldResourcesShipOre.getText());
            resources.copper = Integer.parseInt(textFieldResourcesShipCopper.getText());
            resources.uranium = Integer.parseInt(textFieldResourcesShipUranium.getText());
            
            switch ((String)comboBoxMissionsStandard.getSelectedItem()) {
             
            case MISSION_DEPLOY:
                CustomJson.deployShipsOfPlanet(getMapOfShips(), planet.getUserName(), planet.getId(), x, y, resources);
                break;
                
            case MISSION_TRANSPORT:
                CustomJson.transportToPlanet(getMapOfShips(), planet.getUserName(), planet.getId(), x, y, resources);
                break;
                
            case MISSION_EXPLORE:
                Integer numExp = getMapOfShips().get("explorership");
                if (numExp != null && numExp > 0) {
                    CustomJson.explore(planet.getUserName(), planet.getId(), x, y, "explorership");
                    break;
                }
                Integer numExp2 = getMapOfShips().get("explorership1");
                if (numExp2 != null && numExp2 > 0)
                    CustomJson.explore(planet.getUserName(), planet.getId(), x, y, "explorership1");
                break;
                
            case MISSION_ATTACK:
                break;
                
            default:
                throw new NextValiumException("not implemented"); 
            }
            
            dialogPlanet.setStatusOk("Transaction sent to Steem. Check later for NextColony accepting the transaction.");

        } catch (Exception e) {
            dialogPlanet.setStatusError(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
    
    private void actionPerformed_Ressources() {
        int coal = Integer.parseInt(textFieldResourcesShipCoal.getText());
        int ore = Integer.parseInt(textFieldResourcesShipOre.getText());
        int copper = Integer.parseInt(textFieldResourcesShipCopper.getText());
        int uranium = Integer.parseInt(textFieldResourcesShipUranium.getText());
        int total = coal + ore + copper + uranium;
        textFieldResourcesShipTotal.setText(Integer.toString(total));
    }
    
    private HashMap<String, Integer> getMapOfShips() {
        
        HashMap<String, Integer> mapNumberOfShipTypes = new HashMap<String, Integer>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String ship = (String)model.getValueAt(i, 0);
            Integer number = (Integer)model.getValueAt(i, 2);
            if (number != null && number > 0)
                mapNumberOfShipTypes.put(ship, number);
        }
        return mapNumberOfShipTypes;
    }
    
    private void tableChanged_Fleet() {
        
        int capacityTotal = 0;
        double consumptionTotal = 0;
        numberOfShips = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String ship = (String)model.getValueAt(i, 0);
            Integer number = (Integer)model.getValueAt(i, 2);
            
            if (number == null)
                continue;
            
            if (number <= 0) {
                model.setValueAt(null, i, 2);
                continue;
            }
            
            Integer capacity = 0;
            double consumption = 0;
            
            try {
                capacity = fleet.getCapacityOfShip(ship);
                consumption = fleet.getConsumptionOfShip(ship);
                
            } catch (Exception e) {
                
                dialogPlanet.setStatusError(e.getClass().getSimpleName() + ": " + e.getMessage());
                return;
            }
            
            capacityTotal += number * capacity;
            consumptionTotal += number * consumption;
            numberOfShips += number;
        }
        
        textFieldResourcesFleetMax.setText(Integer.toString(capacityTotal));
        
        try {
            int targetX = Integer.parseInt(textFieldTargetPositionX.getText());
            int planetX = planet.getPosX();
            
            int targetY = Integer.parseInt(textFieldTargetPositionY.getText());
            int planetY = planet.getPosY();
            
            int distX = Math.abs(targetX - planetX);
            int distY = Math.abs(targetY - planetY);
            
            double distance = Math.sqrt(distX*distX + distY*distY);
            
            String selectedMission = (String)comboBoxMissionsStandard.getSelectedItem();
            if (selectedMission.equalsIgnoreCase(MISSION_TRANSPORT))
                distance += distance;
            
            doubleConsumption = distance * consumptionTotal;
            textFieldUraniumConsumption.setText(String.format("%.3f", doubleConsumption));
            
        }
        catch (Exception e) {
            textFieldUraniumConsumption.setText(String.format("%.3f", consumptionTotal) + " / per tile");
        }
    }
    
    private void checkPreconditionSendToSteemButton() {
        String but = "";
        
        if (!SteemUtil.isAccountRegistered(planet.getUserName())) {
            dialogPlanet.setStatusError(but + "Private posting key of user " + planet.getUserName() + " not in nextvaliumgui.ini -> Button <send transaction to Steem> disabled");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        if(((String)comboBoxMissionsStandard.getSelectedItem()).equalsIgnoreCase(MISSION_NONE)) {
            dialogPlanet.setStatusError(but + "No mission selected");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        if (missionsAvailable <= 0) {
            dialogPlanet.setStatusError(but + "No mission slot available");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        try {
            Integer.parseInt(textFieldTargetPositionX.getText());
            Integer.parseInt(textFieldTargetPositionY.getText());
        }
        catch (Exception e) {
            dialogPlanet.setStatusError(but + "No target X/Y coordinates");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        if (numberOfShips <= 0) {
            dialogPlanet.setStatusError(but + "No ships selected");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        try {
            int shipCoal = Integer.parseInt(textFieldResourcesShipCoal.getText());
            int shipOre = Integer.parseInt(textFieldResourcesShipOre.getText());
            int shipCopper = Integer.parseInt(textFieldResourcesShipCopper.getText());
            int shipUranium = Integer.parseInt(textFieldResourcesShipUranium.getText());

            int coal = Integer.parseInt(textFieldResourcesCoal.getText());
            int ore = Integer.parseInt(textFieldResourcesOre.getText());
            int copper = Integer.parseInt(textFieldResourcesCopper.getText());
            int uranium = Integer.parseInt(textFieldResourcesUranium.getText());
            
            int resourcesShipTotal = Integer.parseInt(textFieldResourcesShipTotal.getText());
            int resourcesFleetMax = Integer.parseInt(textFieldResourcesFleetMax.getText());
            
            int consumption = new Double(doubleConsumption).intValue() + 1;
            
            if (shipCoal > coal || 
                    shipOre > ore || 
                    shipCopper > copper || 
                    (shipUranium + consumption) > uranium ||
                    resourcesShipTotal > resourcesFleetMax)
                throw new NextValiumException("");
        }
        catch (Exception e) {
            dialogPlanet.setStatusError("Check resources");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        dialogPlanet.setStatusOk("Ok");
        btnSendTransaction.setEnabled(true);
    }
}
