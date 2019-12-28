package de.prinzvalium.nextvaliumgui.gui.dialog.planetdetails;

import javax.swing.JPanel;

import de.prinzvalium.nextvaliumgui.NextValiumGui;
import de.prinzvalium.nextvaliumgui.lib.CustomJson;
import de.prinzvalium.nextvaliumgui.lib.MyIntFilter;
import de.prinzvalium.nextvaliumgui.lib.NextValiumException;
import de.prinzvalium.nextvaliumgui.lib.SteemUtil;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.Fleet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;
import de.prinzvalium.nextvaliumgui.nextcolony.Production;
import de.prinzvalium.nextvaliumgui.nextcolony.ProductionRessources;
import de.prinzvalium.nextvaliumgui.nextcolony.Resources;
import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantities;
import de.prinzvalium.nextvaliumgui.nextcolony.RessourceQuantitiesRessources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;

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
    private JTextField textFieldFreeMissions;
    private JTextField textFieldUraniumConsumption;
    private JComboBox<String> comboBoxTargetPlanet;
    private JComboBox<String> comboBoxShipsPredefined;
    private JComboBox<String> comboBoxMissionsStandard;
    private JButton btnSendTransaction;
    private HashMap<String, Planet> mapPlanets;
    private DefaultTableModel model;
    private Planet planet;
    private Planet targetPlanet = null;
    private DialogPlanet dialogPlanet;
    private Fleet fleet = null;
    private int missionsAvailable = -1;
    private int missionsPlanetAvailable = -1;
    private int missionsPlanetMax = -1;
    private int missionsMax = -1;
    private int numberOfShips = 0;
    private boolean actionPerformed_comboBoxShipsPredefined = false;
    private RessourceQuantitiesRessources res = null;
    private HashMap<String, Integer> mapShips = null;
    
    private final String PREDEFINED_SHIPSELECTION_EMPTY = "";
    private final String PREDEFINED_SHIPSELECTION_NONE = "No ships";
    private final String PREDEFINED_SHIPSELECTION_ALL_EXP = "All explorerships";
    private final String PREDEFINED_SHIPSELECTION_ALL_CORVETTES = "All corvettes";
    private final String PREDEFINED_SHIPSELECTION_ALL_EXCEPT_EXP0 = "All ships except explorerships";
    private final String PREDEFINED_SHIPSELECTION_ALL_BATTLESHIPS = "All battleships";
    private final String PREDEFINED_SHIPSELECTION_ALL_BATTLESHIPS_AND_TRANSPORTER0 = "All battleships and transportships";
    private final String PREDEFINED_SHIPSELECTION_ALL = "All ships";

    private String[] predefinedShipSelection = {
            PREDEFINED_SHIPSELECTION_EMPTY,
            PREDEFINED_SHIPSELECTION_NONE,
            PREDEFINED_SHIPSELECTION_ALL_EXP,
            PREDEFINED_SHIPSELECTION_ALL_CORVETTES,
            PREDEFINED_SHIPSELECTION_ALL_EXCEPT_EXP0,
            PREDEFINED_SHIPSELECTION_ALL_BATTLESHIPS,
            PREDEFINED_SHIPSELECTION_ALL_BATTLESHIPS_AND_TRANSPORTER0,
            PREDEFINED_SHIPSELECTION_ALL
    };
    
    private final String MISSION_NONE = "";
    private final String MISSION_DEPLOY = "Deploy";
    private final String MISSION_TRANSPORT = "Transport";
    private final String MISSION_ATTACK = "Attack";
    private final String MISSION_SUPPORT = "Support";
    private final String MISSION_SIEGE = "Siege";
    private final String MISSION_BREAKSIEGE = "Break siege";
    private final String MISSION_EXPLORE = "Explore";
    
    private String[] missions = {
            MISSION_NONE,
            MISSION_DEPLOY,
            MISSION_TRANSPORT,
            MISSION_ATTACK,
            MISSION_SUPPORT,
            MISSION_SIEGE,
            MISSION_BREAKSIEGE,
            MISSION_EXPLORE
    };
    private JTextField txtResourcesTotal;
    private JTextField txtFreeMissionsPlanet;
    private JTextField txtTargetloot;
    private JPanel panel;
    
    public PanelFleet(DialogPlanet dialogPlanet, Planet planet) {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent arg0) {
                checkPreconditionSendToSteemButton();
            }
        });
        
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
        gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
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
                if (!actionPerformed_comboBoxShipsPredefined && arg0.getColumn() == 2)
                    comboBoxShipsPredefined.setSelectedItem(PREDEFINED_SHIPSELECTION_EMPTY);
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
        panelMissions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mission", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
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
        
        JLabel lblFreeMissions = new JLabel("Avail. user:");
        lblFreeMissions.setToolTipText("Available user missions");
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
        
        JLabel lblFreeMissionsPlanet = new JLabel("Avail. planet:");
        lblFreeMissionsPlanet.setToolTipText("Available planet missions");
        GridBagConstraints gbc_lblFreeMissionsPlanet = new GridBagConstraints();
        gbc_lblFreeMissionsPlanet.anchor = GridBagConstraints.EAST;
        gbc_lblFreeMissionsPlanet.insets = new Insets(0, 0, 5, 5);
        gbc_lblFreeMissionsPlanet.gridx = 0;
        gbc_lblFreeMissionsPlanet.gridy = 1;
        panelMissions.add(lblFreeMissionsPlanet, gbc_lblFreeMissionsPlanet);
        
        txtFreeMissionsPlanet = new JTextField();
        txtFreeMissionsPlanet.setEditable(false);
        GridBagConstraints gbc_txtFreeMissionsPlanet = new GridBagConstraints();
        gbc_txtFreeMissionsPlanet.insets = new Insets(0, 0, 5, 0);
        gbc_txtFreeMissionsPlanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtFreeMissionsPlanet.gridx = 1;
        gbc_txtFreeMissionsPlanet.gridy = 1;
        panelMissions.add(txtFreeMissionsPlanet, gbc_txtFreeMissionsPlanet);
        txtFreeMissionsPlanet.setColumns(10);
        
        JLabel lblMissionsPredefined = new JLabel("Ships:");
        GridBagConstraints gbc_lblMissionsPredefined = new GridBagConstraints();
        gbc_lblMissionsPredefined.anchor = GridBagConstraints.EAST;
        gbc_lblMissionsPredefined.insets = new Insets(0, 0, 5, 5);
        gbc_lblMissionsPredefined.gridx = 0;
        gbc_lblMissionsPredefined.gridy = 2;
        panelMissions.add(lblMissionsPredefined, gbc_lblMissionsPredefined);
        
        comboBoxShipsPredefined = new JComboBox(predefinedShipSelection);
        comboBoxShipsPredefined.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!((String)comboBoxShipsPredefined.getSelectedItem()).equalsIgnoreCase(PREDEFINED_SHIPSELECTION_EMPTY))
                    actionPerformed_comboBoxShipsPredefined();
            }
        });
        
        GridBagConstraints gbc_comboBoxMissionsPredefined = new GridBagConstraints();
        gbc_comboBoxMissionsPredefined.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxMissionsPredefined.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxMissionsPredefined.gridx = 1;
        gbc_comboBoxMissionsPredefined.gridy = 2;
        panelMissions.add(comboBoxShipsPredefined, gbc_comboBoxMissionsPredefined);
        
        JLabel lblMissionsStandard = new JLabel("Type:");
        GridBagConstraints gbc_lblMissionsStandard = new GridBagConstraints();
        gbc_lblMissionsStandard.anchor = GridBagConstraints.EAST;
        gbc_lblMissionsStandard.insets = new Insets(0, 0, 0, 5);
        gbc_lblMissionsStandard.gridx = 0;
        gbc_lblMissionsStandard.gridy = 3;
        panelMissions.add(lblMissionsStandard, gbc_lblMissionsStandard);
        
        comboBoxMissionsStandard = new JComboBox(missions);
        comboBoxMissionsStandard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                actionPerformed_comboBoxMissionsStandard();
            }
        });
        GridBagConstraints gbc_comboBoxMissionsStandard = new GridBagConstraints();
        gbc_comboBoxMissionsStandard.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxMissionsStandard.gridx = 1;
        gbc_comboBoxMissionsStandard.gridy = 3;
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
        gbl_panelTarget.columnWidths = new int[] {70, 0, 0, 40};
        gbl_panelTarget.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_panelTarget.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0};
        gbl_panelTarget.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
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
        gbc_textFieldTargetUser.insets = new Insets(0, 0, 5, 5);
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
        
        JLabel lblTargetPositionX = new JLabel("X:");
        GridBagConstraints gbc_lblTargetPositionX = new GridBagConstraints();
        gbc_lblTargetPositionX.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetPositionX.gridx = 2;
        gbc_lblTargetPositionX.gridy = 0;
        panelTarget.add(lblTargetPositionX, gbc_lblTargetPositionX);
        
        textFieldTargetPositionX = new JTextField();
        GridBagConstraints gbc_textFieldTargetPositionX = new GridBagConstraints();
        gbc_textFieldTargetPositionX.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldTargetPositionX.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldTargetPositionX.gridx = 3;
        gbc_textFieldTargetPositionX.gridy = 0;
        panelTarget.add(textFieldTargetPositionX, gbc_textFieldTargetPositionX);
        textFieldTargetPositionX.setHorizontalAlignment(SwingConstants.RIGHT);
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
        textFieldTargetPositionX.setColumns(5);

        JLabel lblTargetPlanet = new JLabel("Planet:");
        GridBagConstraints gbc_lblTargetPlanet = new GridBagConstraints();
        gbc_lblTargetPlanet.anchor = GridBagConstraints.EAST;
        gbc_lblTargetPlanet.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetPlanet.gridx = 0;
        gbc_lblTargetPlanet.gridy = 1;
        panelTarget.add(lblTargetPlanet, gbc_lblTargetPlanet);
        
        comboBoxTargetPlanet = new JComboBox<String>();
        comboBoxTargetPlanet.setPreferredSize(new Dimension(150, 20));
        GridBagConstraints gbc_comboBoxTargetPlanet = new GridBagConstraints();
        gbc_comboBoxTargetPlanet.insets = new Insets(0, 0, 5, 5);
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
                        try {
                            RessourceQuantitiesRessources res;
                            res = RessourceQuantities.loadRessourceQuantites(planet.getName(), planet.getId());
                            
                            ProductionRessources pr = new Production(targetPlanet).loadProduction();
                            
                            Double lootCoal = res.getCoal() - pr.getCoal().getSafe();
                            if (lootCoal < 0)
                                lootCoal = 0.0;
                            Double lootOre = res.getOre() - pr.getOre().getSafe();
                            if (lootOre < 0)
                                lootOre = 0.0;
                            Double lootCopper = res.getCopper() - pr.getCopper().getSafe();
                            if (lootCopper < 0)
                                lootCopper = 0.0;
                            Double lootUranium = res.getUranium() - pr.getUranium().getSafe();
                            if (lootUranium < 0)
                                lootUranium = 0.0;
                            
                            Double loot = lootCoal + lootOre + lootCopper + lootUranium;
                            txtTargetloot.setText(String.format("%.3f", loot));
                            
                        } catch (JSONException | IOException e1) {
                        }
                    }
                });
                tableChanged_Fleet();
                checkPreconditionSendToSteemButton();
            }
        });
        
        JLabel lblTargetPositionY = new JLabel("Y:");
        GridBagConstraints gbc_lblTargetPositionY = new GridBagConstraints();
        gbc_lblTargetPositionY.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetPositionY.gridx = 2;
        gbc_lblTargetPositionY.gridy = 1;
        panelTarget.add(lblTargetPositionY, gbc_lblTargetPositionY);
        
        textFieldTargetPositionY = new JTextField();
        GridBagConstraints gbc_textFieldTargetPositionY = new GridBagConstraints();
        gbc_textFieldTargetPositionY.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldTargetPositionY.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldTargetPositionY.gridx = 3;
        gbc_textFieldTargetPositionY.gridy = 1;
        panelTarget.add(textFieldTargetPositionY, gbc_textFieldTargetPositionY);
        textFieldTargetPositionY.setHorizontalAlignment(SwingConstants.TRAILING);
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
        textFieldTargetPositionY.setColumns(5);
        
       
        JLabel lblLoot = new JLabel("Loot:");
        GridBagConstraints gbc_lblLoot = new GridBagConstraints();
        gbc_lblLoot.insets = new Insets(0, 0, 0, 5);
        gbc_lblLoot.anchor = GridBagConstraints.EAST;
        gbc_lblLoot.gridx = 0;
        gbc_lblLoot.gridy = 3;
        panelTarget.add(lblLoot, gbc_lblLoot);
        
        txtTargetloot = new JTextField();
        txtTargetloot.setEnabled(false);
        txtTargetloot.setEditable(false);
        txtTargetloot.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_txtTargetloot = new GridBagConstraints();
        gbc_txtTargetloot.insets = new Insets(0, 0, 0, 5);
        gbc_txtTargetloot.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtTargetloot.gridx = 1;
        gbc_txtTargetloot.gridy = 3;
        panelTarget.add(txtTargetloot, gbc_txtTargetloot);
        txtTargetloot.setColumns(8);
        
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
        textFieldResourcesCoal.setHorizontalAlignment(SwingConstants.TRAILING);
        textFieldResourcesCoal.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesCoal = new GridBagConstraints();
        gbc_textFieldResourcesCoal.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesCoal.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesCoal.gridx = 1;
        gbc_textFieldResourcesCoal.gridy = 1;
        panelResources.add(textFieldResourcesCoal, gbc_textFieldResourcesCoal);
        textFieldResourcesCoal.setColumns(10);
        
        textFieldResourcesShipCoal = new JTextField();
        textFieldResourcesShipCoal.setHorizontalAlignment(SwingConstants.TRAILING);
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
        ((PlainDocument)textFieldResourcesShipCoal.getDocument()).setDocumentFilter(new MyIntFilter());
        
        JLabel lblRessourcesOre = new JLabel("Ore:");
        GridBagConstraints gbc_lblRessourcesOre = new GridBagConstraints();
        gbc_lblRessourcesOre.anchor = GridBagConstraints.EAST;
        gbc_lblRessourcesOre.insets = new Insets(0, 0, 5, 5);
        gbc_lblRessourcesOre.gridx = 0;
        gbc_lblRessourcesOre.gridy = 2;
        panelResources.add(lblRessourcesOre, gbc_lblRessourcesOre);
        
        textFieldResourcesOre = new JTextField();
        textFieldResourcesOre.setHorizontalAlignment(SwingConstants.TRAILING);
        textFieldResourcesOre.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesOre = new GridBagConstraints();
        gbc_textFieldResourcesOre.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesOre.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesOre.gridx = 1;
        gbc_textFieldResourcesOre.gridy = 2;
        panelResources.add(textFieldResourcesOre, gbc_textFieldResourcesOre);
        textFieldResourcesOre.setColumns(10);
        
        textFieldResourcesShipOre = new JTextField();
        textFieldResourcesShipOre.setHorizontalAlignment(SwingConstants.TRAILING);
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
        ((PlainDocument)textFieldResourcesShipOre.getDocument()).setDocumentFilter(new MyIntFilter());
        
        JLabel lblResourcesCopper = new JLabel("Copper:");
        GridBagConstraints gbc_lblResourcesCopper = new GridBagConstraints();
        gbc_lblResourcesCopper.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesCopper.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesCopper.gridx = 0;
        gbc_lblResourcesCopper.gridy = 3;
        panelResources.add(lblResourcesCopper, gbc_lblResourcesCopper);
        
        textFieldResourcesCopper = new JTextField();
        textFieldResourcesCopper.setHorizontalAlignment(SwingConstants.TRAILING);
        textFieldResourcesCopper.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesCopper = new GridBagConstraints();
        gbc_textFieldResourcesCopper.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesCopper.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesCopper.gridx = 1;
        gbc_textFieldResourcesCopper.gridy = 3;
        panelResources.add(textFieldResourcesCopper, gbc_textFieldResourcesCopper);
        textFieldResourcesCopper.setColumns(10);
        
        textFieldResourcesShipCopper = new JTextField();
        textFieldResourcesShipCopper.setHorizontalAlignment(SwingConstants.TRAILING);
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
        ((PlainDocument)textFieldResourcesShipCopper.getDocument()).setDocumentFilter(new MyIntFilter());
        
        JLabel lblResourcesUranium = new JLabel("Uranium:");
        GridBagConstraints gbc_lblResourcesUranium = new GridBagConstraints();
        gbc_lblResourcesUranium.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesUranium.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesUranium.gridx = 0;
        gbc_lblResourcesUranium.gridy = 4;
        panelResources.add(lblResourcesUranium, gbc_lblResourcesUranium);
        
        textFieldResourcesUranium = new JTextField();
        textFieldResourcesUranium.setHorizontalAlignment(SwingConstants.TRAILING);
        textFieldResourcesUranium.setEditable(false);
        GridBagConstraints gbc_textFieldResourcesUranium = new GridBagConstraints();
        gbc_textFieldResourcesUranium.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldResourcesUranium.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldResourcesUranium.gridx = 1;
        gbc_textFieldResourcesUranium.gridy = 4;
        panelResources.add(textFieldResourcesUranium, gbc_textFieldResourcesUranium);
        textFieldResourcesUranium.setColumns(10);
        
        textFieldResourcesShipUranium = new JTextField();
        textFieldResourcesShipUranium.setHorizontalAlignment(SwingConstants.TRAILING);
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
        ((PlainDocument)textFieldResourcesShipUranium.getDocument()).setDocumentFilter(new MyIntFilter());
        
        JLabel lblResourcesShipTotal = new JLabel("Total:");
        GridBagConstraints gbc_lblResourcesShipTotal = new GridBagConstraints();
        gbc_lblResourcesShipTotal.insets = new Insets(0, 0, 5, 5);
        gbc_lblResourcesShipTotal.anchor = GridBagConstraints.EAST;
        gbc_lblResourcesShipTotal.gridx = 0;
        gbc_lblResourcesShipTotal.gridy = 5;
        panelResources.add(lblResourcesShipTotal, gbc_lblResourcesShipTotal);
        
        txtResourcesTotal = new JTextField();
        txtResourcesTotal.setHorizontalAlignment(SwingConstants.TRAILING);
        txtResourcesTotal.setEnabled(false);
        GridBagConstraints gbc_txtResourcestotal = new GridBagConstraints();
        gbc_txtResourcestotal.insets = new Insets(0, 0, 5, 5);
        gbc_txtResourcestotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtResourcestotal.gridx = 1;
        gbc_txtResourcestotal.gridy = 5;
        panelResources.add(txtResourcesTotal, gbc_txtResourcestotal);
        txtResourcesTotal.setColumns(10);
        
        textFieldResourcesShipTotal = new JTextField();
        textFieldResourcesShipTotal.setHorizontalAlignment(SwingConstants.TRAILING);
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
        textFieldResourcesFleetMax.setHorizontalAlignment(SwingConstants.TRAILING);
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
        textFieldUraniumConsumption.setHorizontalAlignment(SwingConstants.TRAILING);
        textFieldUraniumConsumption.setEditable(false);
        GridBagConstraints gbc_textFieldUraniumConsumption = new GridBagConstraints();
        gbc_textFieldUraniumConsumption.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldUraniumConsumption.gridx = 2;
        gbc_textFieldUraniumConsumption.gridy = 7;
        panelResources.add(textFieldUraniumConsumption, gbc_textFieldUraniumConsumption);
        textFieldUraniumConsumption.setColumns(10);
        
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 3;
        add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0};
        gbl_panel.rowHeights = new int[]{0, 0};
        gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        btnSendTransaction = new JButton("Send transaction to Steem");
        GridBagConstraints gbc_btnSendTransaction = new GridBagConstraints();
        gbc_btnSendTransaction.insets = new Insets(5, 5, 5, 5);
        gbc_btnSendTransaction.fill = GridBagConstraints.BOTH;
        gbc_btnSendTransaction.gridx = 0;
        gbc_btnSendTransaction.gridy = 0;
        panel.add(btnSendTransaction, gbc_btnSendTransaction);
        btnSendTransaction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                actionPerformed_btnSendTransaction();
            }
        });
        
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                
                String userName = planet.getUserName();
                String planetId = planet.getId();
                String planetName = planet.getName();
                
                fleet = new Fleet(userName, planetName, planetId);
                mapShips = fleet.getNumberOfShipTypesInShipyard();
                
                res = RessourceQuantities.loadRessourceQuantites(planetName, planetId);
                
                String cmd = Util.NEXTCOLONY_API_CMD_MISSIONINFO + "?user=" + userName + "&planet=" + planetId;
                JSONObject jsonObj = Util.getJSONObjectFromApiCommand(cmd);
                missionsAvailable = jsonObj.getInt("user_unused");
                missionsMax = jsonObj.getInt("user_max");
                missionsPlanetAvailable = jsonObj.getInt("planet_unused");
                missionsPlanetMax = jsonObj.getInt("planet_max");
                
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    dialogPlanet.setStatusError(e.getClass().getSimpleName() + ": " + e.getMessage());
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                
                textFieldFreeMissions.setText(missionsAvailable + " / " + missionsMax);
                txtFreeMissionsPlanet.setText(missionsPlanetAvailable + " / " + missionsPlanetMax);
                
                textFieldResourcesCoal.setText(String.format("%.3f", res.getCoal()));
                textFieldResourcesOre.setText(String.format("%.3f", res.getOre()));
                textFieldResourcesCopper.setText(String.format("%.3f", res.getCopper()));
                textFieldResourcesUranium.setText(String.format("%.3f", res.getUranium()));
                txtResourcesTotal.setText(String.format("%.3f", res.getCoal()+res.getOre()+res.getCopper()+res.getUranium()));

                setTarget();
                
                model.removeRow(0);
                for (Map.Entry<?,?> entry : mapShips.entrySet())
                    model.addRow(new Object[] { entry.getKey(), entry.getValue(), null, null });

                checkPreconditionSendToSteemButton();
                
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                super.done();
            }
        }.execute();
    }
    
    private void setTarget() {
        
        Planet targetPlanet = NextValiumGui.getNextValiumGui().getPlanetMarkedAsTarget();      
        if (targetPlanet == null)
            return;
        
        textFieldTargetUser.setText(targetPlanet.getUserName());
        textFieldTargetUser.postActionEvent();
        comboBoxTargetPlanet.setSelectedItem(targetPlanet.getName());
    }
    
    private void actionPerformed_comboBoxShipsPredefined() {
        
        actionPerformed_comboBoxShipsPredefined = true;
        
        // Reset all values in table
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(null, i, 2);
            model.setValueAt(null, i, 3);
        }
        
        switch ((String)comboBoxShipsPredefined.getSelectedItem()) {
            
        case PREDEFINED_SHIPSELECTION_NONE:
            break;
            
        case PREDEFINED_SHIPSELECTION_ALL_EXP:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (ship.equalsIgnoreCase("explorership"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            break;
            
        case PREDEFINED_SHIPSELECTION_ALL_CORVETTES:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (ship.equalsIgnoreCase("corvette") || ship.equalsIgnoreCase("corvette1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            break;
            
        case PREDEFINED_SHIPSELECTION_ALL_EXCEPT_EXP0:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (!ship.equalsIgnoreCase("explorership"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            break;
        
        case PREDEFINED_SHIPSELECTION_ALL_BATTLESHIPS:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (!ship.equalsIgnoreCase("explorership") && 
                        !ship.equalsIgnoreCase("explorership1") &&
                        !ship.equalsIgnoreCase("transportship") &&
                        !ship.equalsIgnoreCase("transportship1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            break;
            
        case PREDEFINED_SHIPSELECTION_ALL_BATTLESHIPS_AND_TRANSPORTER0:
            for (int i = 0; i < model.getRowCount(); i++) {
                String ship = (String)model.getValueAt(i, 0);
                if (!ship.equalsIgnoreCase("explorership") && 
                        !ship.equalsIgnoreCase("explorership1") &&
                        !ship.equalsIgnoreCase("transportship1"))
                    model.setValueAt(model.getValueAt(i, 1), i, 2);
            }
            break;
            
        case PREDEFINED_SHIPSELECTION_ALL:
            for (int i = 0; i < model.getRowCount(); i++)
                model.setValueAt(model.getValueAt(i, 1), i, 2);
            break;
        }
        
        actionPerformed_comboBoxShipsPredefined = false;
    }
    
    private void actionPerformed_btnSendTransaction() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        dialogPlanet.setStatusInfo("Sending transaction to Steem. Please wait...");
        
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                
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
                    CustomJson.fightingAction("attack", getMapOfShipsWithAllValues(), planet.getUserName(), planet.getId(), x, y);
                    break;
                    
                case MISSION_SUPPORT:
                    CustomJson.fightingAction("support", getMapOfShipsWithAllValues(), planet.getUserName(), planet.getId(), x, y);
                    break;
                    
                case MISSION_SIEGE:
                    CustomJson.fightingAction("siege", getMapOfShipsWithAllValues(), planet.getUserName(), planet.getId(), x, y);
                    break;
                    
                case MISSION_BREAKSIEGE:
                    CustomJson.fightingAction("breaksiege", getMapOfShipsWithAllValues(), planet.getUserName(), planet.getId(), x, y);
                    break;
                    
                default:
                    throw new NextValiumException("not implemented"); 
                }

                return null;
            }

            @Override
            protected void done() {
                
                try {
                    get();
                    dialogPlanet.setStatusOk("Transaction sent to Steem. Check later for NextColony accepting the transaction.");
                    
                } catch (InterruptedException | ExecutionException e) {
                    dialogPlanet.setStatusError(e.getClass().getSimpleName() + ": " + e.getMessage());
                }
                
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                super.done();
            }
            
        }.execute();
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
    
    private HashMap<String, TableFleetValues> getMapOfShipsWithAllValues() {
        
        HashMap<String, TableFleetValues> mapNumberOfShipTypes = new HashMap<String, TableFleetValues>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String ship = (String)model.getValueAt(i, 0);
            Integer numberPlanet = (Integer)model.getValueAt(i, 1);
            Integer numberFleet = (Integer)model.getValueAt(i, 2);
            Integer position = (Integer)model.getValueAt(i, 3);
            if (numberFleet != null && numberFleet > 0) {
                TableFleetValues ftv = new TableFleetValues(ship, numberPlanet, numberFleet, position);
                mapNumberOfShipTypes.put(ship, ftv);
            }
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
            if (!selectedMission.equalsIgnoreCase(MISSION_DEPLOY))
                distance += distance;
            
            textFieldUraniumConsumption.setText(String.format("%.3f", distance * consumptionTotal));
            
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
        
        if (missionsAvailable <= 0 || missionsPlanetAvailable <= 0) {
            dialogPlanet.setStatusError(but + "No mission slot available");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        String selectedMission = (String) comboBoxMissionsStandard.getSelectedItem();
        
        if (numberOfShips <= 0) {
            dialogPlanet.setStatusError(but + "No ships selected");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        if(selectedMission.equalsIgnoreCase(MISSION_NONE)) {
            dialogPlanet.setStatusError(but + "No mission type selected");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        if (selectedMission.equalsIgnoreCase(MISSION_ATTACK) || 
                selectedMission.equalsIgnoreCase(MISSION_SUPPORT) ||
                selectedMission.equalsIgnoreCase(MISSION_SIEGE) ||
                selectedMission.equalsIgnoreCase(MISSION_BREAKSIEGE)) {
            
            for (Entry<String, TableFleetValues> entry : getMapOfShipsWithAllValues().entrySet()) {
                if (entry.getValue().getPosition() == null) {
                    dialogPlanet.setStatusError(but + "Check ship position for battle (start with 1...)");
                    btnSendTransaction.setEnabled(false);
                    return;
                }
            }
        }
        
        try {
            Integer.parseInt(textFieldTargetPositionX.getText());
            Integer.parseInt(textFieldTargetPositionY.getText());
        }
        catch (Exception e) {
            dialogPlanet.setStatusError(but + "Check target X/Y coordinates");
            btnSendTransaction.setEnabled(false);
            return;
        }
        
        try {
            double shipCoal = Double.parseDouble(textFieldResourcesShipCoal.getText().replace(',','.'));
            double shipOre = Double.parseDouble(textFieldResourcesShipOre.getText().replace(',','.'));
            double shipCopper = Double.parseDouble(textFieldResourcesShipCopper.getText().replace(',','.'));
            double shipUranium = Double.parseDouble(textFieldResourcesShipUranium.getText().replace(',','.'));

            double coal = Double.parseDouble(textFieldResourcesCoal.getText().replace(',','.'));
            double ore = Double.parseDouble(textFieldResourcesOre.getText().replace(',','.'));
            double copper = Double.parseDouble(textFieldResourcesCopper.getText().replace(',','.'));
            double uranium = Double.parseDouble(textFieldResourcesUranium.getText().replace(',','.'));
            
            int resourcesShipTotal = Integer.parseInt(textFieldResourcesShipTotal.getText());
            int resourcesFleetMax = Integer.parseInt(textFieldResourcesFleetMax.getText());
            
            double consumption = Double.parseDouble(textFieldUraniumConsumption.getText().replace(',','.'));
            
            if (shipCoal > coal || 
                    shipOre > ore || 
                    shipCopper > copper || 
                    (consumption + shipUranium) > uranium ||
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
    
    private void actionPerformed_comboBoxMissionsStandard() {
        tableChanged_Fleet();
        checkPreconditionSendToSteemButton();
    }
}
