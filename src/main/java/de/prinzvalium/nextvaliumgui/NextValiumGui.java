package de.prinzvalium.nextvaliumgui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.gui.PanelGalaxyMap;
import de.prinzvalium.nextvaliumgui.gui.PanelUniverse;
import de.prinzvalium.nextvaliumgui.gui.dialog.fulldepots.DialogFullDepots;
import de.prinzvalium.nextvaliumgui.gui.dialog.hostilemissions.HostileMissions;
import de.prinzvalium.nextvaliumgui.gui.dialog.lastplanets.DialogLastPlanets;
import de.prinzvalium.nextvaliumgui.gui.dialog.seasonranking.SeasonRanking;
import de.prinzvalium.nextvaliumgui.lib.Util;
import de.prinzvalium.nextvaliumgui.nextcolony.Planet;
import de.prinzvalium.nextvaliumgui.nextcolony.Planets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class NextValiumGui {

    private static final Logger LOGGER = LoggerFactory.getLogger(NextValiumGui.class);
    private static final String version = "0.7.1-SNAPSHOT";
    private static NextValiumGui nextValiumGui = null;
    private JFrame frmNextvaliumManagementGui;
    private JTextField textFieldPosX;
    private JTextField textFieldPosY;
    private JComboBox<String> comboBoxUsers;
    private JComboBox<String> comboBoxPlanets;
    private JCheckBox chckbxRadarExplorations;
    private JCheckBox chckbxRadarOthers;
    private HashMap<String, Planet> mapPlanets;
    private Planet planetMarkedAsTarget = null;
    private JTextField textFieldTargetUser;
    private JButton btnClearTarget;
    private JButton btnRefresh;
    private ArrayList<String> listUsers;
    private String selectedUser = null;
    private PanelGalaxyMap panelGalaxyMap;
    private JPanel panelStatusBar;
    private JLabel lblGamedelay;
    private JTextField textFieldGameDelay;
    private JCheckBox chckbxYamatos;
    private JMenu mnInfo;
    private JMenuItem mntmSeasonRanking;
    private JButton btnLastPlanet;
    private Planet lastPlanet = null;
    private JLabel lblTargetuser;
    private JLabel lblTargetplanet;
    private JTextField txtTargetplanet;
    private JMenuItem mntmLastPlanets;
    private JLabel lblStatus;
    private JLabel txtStatus;
    private PanelUniverse panelUniverse;
    private static final Integer LAYER_UNIVERSE = new Integer(1);
    private static final Integer LAYER_GALAXYMAP = new Integer(0);
    private JLayeredPane lp;
    private Boolean galaxyLoaded = false;
    private JPanel panel;
    private JPanel panelTabUniverse;
    private JTabbedPane tabbedPane;
    private JPanel panelUniverseUser;
    private JLabel lblUser;
    private JComboBox<String> comboBoxUniverseUser;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new NextValiumGui();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public NextValiumGui() {
        nextValiumGui = this;
        
        LOGGER.trace("");
        LOGGER.trace("---------------");
        LOGGER.trace("NextValiumGui()");
        LOGGER.trace("---------------");
        
        try {
            // Read user and keys from ini-file. User skills are loaded
            listUsers = Util.loadProperties();
            
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(-1);
        }
        
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        LOGGER.trace("initialize()");
        
        panelGalaxyMap = new PanelGalaxyMap();
        panelUniverse = new PanelUniverse();
        
        frmNextvaliumManagementGui = new JFrame();
        frmNextvaliumManagementGui.setTitle("NextValium GUI " + version + " - Multi user management GUI for NextColony");
        frmNextvaliumManagementGui.setBounds(10, 10, 1250, 970);
        frmNextvaliumManagementGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        frmNextvaliumManagementGui.setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmNextvaliumManagementGui.dispose();
            }
        });
        mnFile.add(mntmExit);
        
        mnInfo = new JMenu("Info");
        menuBar.add(mnInfo);
        
        mntmSeasonRanking = new JMenuItem("Season ranking");
        mntmSeasonRanking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new SeasonRanking().setVisible(true);
            }
        });
        mnInfo.add(mntmSeasonRanking);
        
        mntmLastPlanets = new JMenuItem("Last planets");
        mntmLastPlanets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new DialogLastPlanets().setVisible(true);
            }
        });
        mnInfo.add(mntmLastPlanets);
        
        JMenuItem mntmHostileMissions = new JMenuItem("Hostile missions");
        mntmHostileMissions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new HostileMissions().setVisible(true);
            }
        });
        mnInfo.add(mntmHostileMissions);
        
        JMenuItem mntmFulldepots = new JMenuItem("Full depots");
        mntmFulldepots.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new DialogFullDepots().setVisible(true);
            }
        });
        mnInfo.add(mntmFulldepots);
        
        JPanel panelTop = new JPanel();
        panelTop.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        frmNextvaliumManagementGui.getContentPane().add(panelTop, BorderLayout.NORTH);
        GridBagLayout gbl_panelTop = new GridBagLayout();
        gbl_panelTop.columnWidths = new int[]{26, 0};
        gbl_panelTop.rowHeights = new int[]{23, 0};
        gbl_panelTop.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panelTop.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panelTop.setLayout(gbl_panelTop);
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                if (lp == null || panelUniverse == null)
                    return;
                switch (tabbedPane.getSelectedIndex()) {
                case 0:
                    lp.setLayer(panelGalaxyMap, 0);
                    break;
                case 1:
                    lp.setLayer(panelUniverse, 0);
                    if (!galaxyLoaded)
                        btnRefresh.doClick();
                    break;
                }
            }
        });
        GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
        gbc_tabbedPane.fill = GridBagConstraints.BOTH;
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 0;
        panelTop.add(tabbedPane, gbc_tabbedPane);
        
        panelTabUniverse = new JPanel();
        tabbedPane.addTab("Universe", null, panelTabUniverse, null);               
        GridBagLayout gbl_panelTabUniverse = new GridBagLayout();
        gbl_panelTabUniverse.columnWidths = new int[]{607, 0};
        gbl_panelTabUniverse.rowHeights = new int[]{10, 0};
        gbl_panelTabUniverse.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_panelTabUniverse.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panelTabUniverse.setLayout(gbl_panelTabUniverse);
        
        panelUniverseUser = new JPanel();
        panelUniverseUser.setBorder(new TitledBorder(null, "Show", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_panelUniverseUser = new GridBagConstraints();
        gbc_panelUniverseUser.anchor = GridBagConstraints.WEST;
        gbc_panelUniverseUser.fill = GridBagConstraints.VERTICAL;
        gbc_panelUniverseUser.gridx = 0;
        gbc_panelUniverseUser.gridy = 0;
        panelTabUniverse.add(panelUniverseUser, gbc_panelUniverseUser);
        GridBagLayout gbl_panelUniverseUser = new GridBagLayout();
        gbl_panelUniverseUser.columnWidths = new int[]{26, 28, 0};
        gbl_panelUniverseUser.rowHeights = new int[]{20, 0};
        gbl_panelUniverseUser.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_panelUniverseUser.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panelUniverseUser.setLayout(gbl_panelUniverseUser);
        
        lblUser = new JLabel("User:");
        GridBagConstraints gbc_lblUser = new GridBagConstraints();
        gbc_lblUser.anchor = GridBagConstraints.WEST;
        gbc_lblUser.insets = new Insets(0, 0, 0, 5);
        gbc_lblUser.gridx = 0;
        gbc_lblUser.gridy = 0;
        panelUniverseUser.add(lblUser, gbc_lblUser);
        
        comboBoxUniverseUser = new JComboBox<String>();
        comboBoxUniverseUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                panelUniverse.repaint();
            }
        });
        comboBoxUniverseUser.setEditable(true);
        comboBoxUniverseUser.setPreferredSize(new Dimension(150, 20));
        GridBagConstraints gbc_comboBoxUniverseUser = new GridBagConstraints();
        gbc_comboBoxUniverseUser.anchor = GridBagConstraints.NORTHWEST;
        gbc_comboBoxUniverseUser.gridx = 1;
        gbc_comboBoxUniverseUser.gridy = 0;
        panelUniverseUser.add(comboBoxUniverseUser, gbc_comboBoxUniverseUser);
        
        panel = new JPanel();
        tabbedPane.addTab("Galaxy map", null, panel, null);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{26, 91, 0, 0, 0};
        gbl_panel.rowHeights = new int[]{23, 0};
        gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        JPanel panelUserPlanet = new JPanel();
        GridBagConstraints gbc_panelUserPlanet = new GridBagConstraints();
        gbc_panelUserPlanet.fill = GridBagConstraints.BOTH;
        gbc_panelUserPlanet.insets = new Insets(0, 0, 0, 5);
        gbc_panelUserPlanet.gridx = 0;
        gbc_panelUserPlanet.gridy = 0;
        panel.add(panelUserPlanet, gbc_panelUserPlanet);
        panelUserPlanet.setToolTipText("Select user planet or x/y");
        panelUserPlanet.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Center map", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagLayout gbl_panelUserPlanet = new GridBagLayout();
        gbl_panelUserPlanet.columnWidths = new int[]{26, 86, 0, 0, 0, 0};
        gbl_panelUserPlanet.rowHeights = new int[]{23, 1, 0};
        gbl_panelUserPlanet.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panelUserPlanet.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panelUserPlanet.setLayout(gbl_panelUserPlanet);
        
        JLabel lblUserName = new JLabel("User:");
        GridBagConstraints gbc_lblUserName = new GridBagConstraints();
        gbc_lblUserName.insets = new Insets(0, 2, 5, 5);
        gbc_lblUserName.anchor = GridBagConstraints.EAST;
        gbc_lblUserName.gridx = 0;
        gbc_lblUserName.gridy = 0;
        panelUserPlanet.add(lblUserName, gbc_lblUserName);
        
        comboBoxUsers = new JComboBox<String>();
        comboBoxUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> list = new ArrayList<String>();
                    selectedUser = (String)comboBoxUsers.getSelectedItem();
                    mapPlanets = Planets.loadUserPlanets(selectedUser);
                    mapPlanets.forEach((planetId, planet) -> list.add(planet.getName()));
                    Collections.sort(list);
                    comboBoxPlanets.removeAllItems();
                    list.forEach(planetName -> comboBoxPlanets.addItem(planetName));
                    
                } catch (JSONException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        comboBoxUsers.setEditable(true);
        GridBagConstraints gbc_comboBoxUser = new GridBagConstraints();
        gbc_comboBoxUser.insets = new Insets(0, 0, 5, 5);
        gbc_comboBoxUser.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxUser.gridx = 1;
        gbc_comboBoxUser.gridy = 0;
        panelUserPlanet.add(comboBoxUsers, gbc_comboBoxUser);
        
        JLabel lblMapPosition = new JLabel("X:");
        GridBagConstraints gbc_lblMapPosition = new GridBagConstraints();
        gbc_lblMapPosition.insets = new Insets(0, 5, 5, 5);
        gbc_lblMapPosition.anchor = GridBagConstraints.EAST;
        gbc_lblMapPosition.gridx = 2;
        gbc_lblMapPosition.gridy = 0;
        panelUserPlanet.add(lblMapPosition, gbc_lblMapPosition);
        
        textFieldPosX = new JTextField();
        textFieldPosX.setText("0");
        GridBagConstraints gbc_textFieldPosX = new GridBagConstraints();
        gbc_textFieldPosX.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldPosX.insets = new Insets(0, 2, 5, 5);
        gbc_textFieldPosX.gridx = 3;
        gbc_textFieldPosX.gridy = 0;
        panelUserPlanet.add(textFieldPosX, gbc_textFieldPosX);
        textFieldPosX.setColumns(4);
        
        btnRefresh = new JButton("<html><center>Reload<br>\r\nmap</center></html>");
        btnRefresh.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRefresh.setBackground(Color.ORANGE);
        GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
        gbc_btnRefresh.anchor = GridBagConstraints.WEST;
        gbc_btnRefresh.fill = GridBagConstraints.VERTICAL;
        gbc_btnRefresh.gridheight = 2;
        gbc_btnRefresh.gridx = 4;
        gbc_btnRefresh.gridy = 0;
        panelUserPlanet.add(btnRefresh, gbc_btnRefresh);
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnRefresh(e);
            }
        });
        
        JLabel lblPlanet = new JLabel("Planet:");
        GridBagConstraints gbc_lblPlanet = new GridBagConstraints();
        gbc_lblPlanet.insets = new Insets(0, 2, 0, 5);
        gbc_lblPlanet.anchor = GridBagConstraints.WEST;
        gbc_lblPlanet.gridx = 0;
        gbc_lblPlanet.gridy = 1;
        panelUserPlanet.add(lblPlanet, gbc_lblPlanet);
        
        comboBoxPlanets = new JComboBox<String>();
        GridBagConstraints gbc_comboBoxPlanets = new GridBagConstraints();
        gbc_comboBoxPlanets.insets = new Insets(0, 2, 0, 5);
        gbc_comboBoxPlanets.anchor = GridBagConstraints.WEST;
        gbc_comboBoxPlanets.gridx = 1;
        gbc_comboBoxPlanets.gridy = 1;
        panelUserPlanet.add(comboBoxPlanets, gbc_comboBoxPlanets);
        comboBoxPlanets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comboBoxPlanets.getSelectedItem() == null)
                    return;
                
                String planetName = comboBoxPlanets.getSelectedItem().toString();
                mapPlanets.forEach((planetId, planet) -> {
                    if (planet.getName().equalsIgnoreCase(planetName)) {
                        textFieldPosX.setText(Integer.toString(planet.getPosX()));
                        textFieldPosY.setText(Integer.toString(planet.getPosY()));
                    }
                });
            }
        });
        comboBoxPlanets.setPreferredSize(new Dimension(150, 20));
        
        JLabel lblMapPositionY = new JLabel("Y:");
        GridBagConstraints gbc_lblMapPositionY = new GridBagConstraints();
        gbc_lblMapPositionY.insets = new Insets(0, 0, 0, 5);
        gbc_lblMapPositionY.anchor = GridBagConstraints.EAST;
        gbc_lblMapPositionY.gridx = 2;
        gbc_lblMapPositionY.gridy = 1;
        panelUserPlanet.add(lblMapPositionY, gbc_lblMapPositionY);
        
        textFieldPosY = new JTextField();
        textFieldPosY.setText("0");
        GridBagConstraints gbc_textFieldPosY = new GridBagConstraints();
        gbc_textFieldPosY.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldPosY.insets = new Insets(0, 2, 0, 5);
        gbc_textFieldPosY.gridx = 3;
        gbc_textFieldPosY.gridy = 1;
        panelUserPlanet.add(textFieldPosY, gbc_textFieldPosY);
        textFieldPosY.setColumns(4);
        
        JPanel panelTarget = new JPanel();
        GridBagConstraints gbc_panelTarget = new GridBagConstraints();
        gbc_panelTarget.fill = GridBagConstraints.BOTH;
        gbc_panelTarget.insets = new Insets(0, 0, 0, 5);
        gbc_panelTarget.gridx = 1;
        gbc_panelTarget.gridy = 0;
        panel.add(panelTarget, gbc_panelTarget);
        panelTarget.setToolTipText("Right click on planet to mark as target");
        panelTarget.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Target", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagLayout gbl_panelTarget = new GridBagLayout();
        gbl_panelTarget.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panelTarget.rowHeights = new int[]{23, 1, 0};
        gbl_panelTarget.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panelTarget.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panelTarget.setLayout(gbl_panelTarget);
        
        lblTargetuser = new JLabel("User:");
        GridBagConstraints gbc_lblTargetuser = new GridBagConstraints();
        gbc_lblTargetuser.insets = new Insets(0, 0, 5, 5);
        gbc_lblTargetuser.anchor = GridBagConstraints.EAST;
        gbc_lblTargetuser.gridx = 0;
        gbc_lblTargetuser.gridy = 0;
        panelTarget.add(lblTargetuser, gbc_lblTargetuser);
        
        lblTargetplanet = new JLabel("Planet:");
        GridBagConstraints gbc_lblTargetplanet = new GridBagConstraints();
        gbc_lblTargetplanet.anchor = GridBagConstraints.EAST;
        gbc_lblTargetplanet.insets = new Insets(0, 0, 0, 5);
        gbc_lblTargetplanet.gridx = 0;
        gbc_lblTargetplanet.gridy = 1;
        panelTarget.add(lblTargetplanet, gbc_lblTargetplanet);
        
        btnClearTarget = new JButton("<html><center>Clear<br>target</center></html>");
        GridBagConstraints gbc_btnClearTarget = new GridBagConstraints();
        gbc_btnClearTarget.fill = GridBagConstraints.VERTICAL;
        gbc_btnClearTarget.gridheight = 2;
        gbc_btnClearTarget.anchor = GridBagConstraints.EAST;
        gbc_btnClearTarget.gridx = 2;
        gbc_btnClearTarget.gridy = 0;
        panelTarget.add(btnClearTarget, gbc_btnClearTarget);
        
        textFieldTargetUser = new JTextField();
        GridBagConstraints gbc_textFieldMarkedAsTarget = new GridBagConstraints();
        gbc_textFieldMarkedAsTarget.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldMarkedAsTarget.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldMarkedAsTarget.gridx = 1;
        gbc_textFieldMarkedAsTarget.gridy = 0;
        panelTarget.add(textFieldTargetUser, gbc_textFieldMarkedAsTarget);
        textFieldTargetUser.setEditable(false);
        textFieldTargetUser.setColumns(15);
        
        txtTargetplanet = new JTextField();
        txtTargetplanet.setEditable(false);
        GridBagConstraints gbc_txtTargetplanet = new GridBagConstraints();
        gbc_txtTargetplanet.insets = new Insets(0, 0, 0, 5);
        gbc_txtTargetplanet.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtTargetplanet.gridx = 1;
        gbc_txtTargetplanet.gridy = 1;
        panelTarget.add(txtTargetplanet, gbc_txtTargetplanet);
        txtTargetplanet.setColumns(15);
        
        JPanel panelRadar = new JPanel();
        GridBagConstraints gbc_panelRadar = new GridBagConstraints();
        gbc_panelRadar.anchor = GridBagConstraints.WEST;
        gbc_panelRadar.fill = GridBagConstraints.VERTICAL;
        gbc_panelRadar.insets = new Insets(0, 0, 0, 5);
        gbc_panelRadar.gridx = 2;
        gbc_panelRadar.gridy = 0;
        panel.add(panelRadar, gbc_panelRadar);
        panelRadar.setBorder(new TitledBorder(null, "Flight radar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagLayout gbl_panelRadar = new GridBagLayout();
        gbl_panelRadar.columnWidths = new int[]{0, 0};
        gbl_panelRadar.rowHeights = new int[]{0, 0, 0, 0};
        gbl_panelRadar.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panelRadar.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
        panelRadar.setLayout(gbl_panelRadar);
        
        chckbxRadarExplorations = new JCheckBox("Explorations");
        chckbxRadarExplorations.setMargin(new Insets(0, 0, 0, 0));
        chckbxRadarExplorations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frmNextvaliumManagementGui.repaint();
            }
        });
        chckbxRadarExplorations.setSelected(true);
        GridBagConstraints gbc_chckbxRadarExplorations = new GridBagConstraints();
        gbc_chckbxRadarExplorations.anchor = GridBagConstraints.NORTHWEST;
        gbc_chckbxRadarExplorations.gridx = 0;
        gbc_chckbxRadarExplorations.gridy = 0;
        panelRadar.add(chckbxRadarExplorations, gbc_chckbxRadarExplorations);
        
        chckbxRadarOthers = new JCheckBox("Others");
        chckbxRadarOthers.setMargin(new Insets(0, 0, 0, 0));
        chckbxRadarOthers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmNextvaliumManagementGui.repaint();
            }
        });
        
        chckbxYamatos = new JCheckBox("Yamatos");
        chckbxYamatos.setSelected(true);
        chckbxYamatos.setMargin(new Insets(0, 0, 0, 0));
        chckbxYamatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmNextvaliumManagementGui.repaint();
            }
        });
        GridBagConstraints gbc_chckbxYamatos = new GridBagConstraints();
        gbc_chckbxYamatos.anchor = GridBagConstraints.WEST;
        gbc_chckbxYamatos.gridx = 0;
        gbc_chckbxYamatos.gridy = 1;
        panelRadar.add(chckbxYamatos, gbc_chckbxYamatos);
        chckbxRadarOthers.setSelected(true);
        GridBagConstraints gbc_chckbxRadarOthers = new GridBagConstraints();
        gbc_chckbxRadarOthers.anchor = GridBagConstraints.NORTHWEST;
        gbc_chckbxRadarOthers.gridx = 0;
        gbc_chckbxRadarOthers.gridy = 2;
        panelRadar.add(chckbxRadarOthers, gbc_chckbxRadarOthers);
        
        JPanel panelLastPlanets = new JPanel();
        GridBagConstraints gbc_panelLastPlanets = new GridBagConstraints();
        gbc_panelLastPlanets.fill = GridBagConstraints.BOTH;
        gbc_panelLastPlanets.gridx = 3;
        gbc_panelLastPlanets.gridy = 0;
        panel.add(panelLastPlanets, gbc_panelLastPlanets);
        panelLastPlanets.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Quick jump last planet", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagLayout gbl_panelLastPlanets = new GridBagLayout();
        gbl_panelLastPlanets.columnWidths = new int[] {120};
        gbl_panelLastPlanets.rowHeights = new int[] {1};
        gbl_panelLastPlanets.columnWeights = new double[]{0.0};
        gbl_panelLastPlanets.rowWeights = new double[]{1.0};
        panelLastPlanets.setLayout(gbl_panelLastPlanets);
        
        btnLastPlanet = new JButton("");
        btnLastPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (lastPlanet == null)
                    return;
                NextValiumGui.getNextValiumGui().setCenterPosition(lastPlanet);
            }
        });
        GridBagConstraints gbc_btnLastPlanet = new GridBagConstraints();
        gbc_btnLastPlanet.fill = GridBagConstraints.BOTH;
        gbc_btnLastPlanet.gridx = 0;
        gbc_btnLastPlanet.gridy = 0;
        panelLastPlanets.add(btnLastPlanet, gbc_btnLastPlanet);
        
        btnClearTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                planetMarkedAsTarget = null;
                textFieldTargetUser.setText(null);
                txtTargetplanet.setText(null);
                frmNextvaliumManagementGui.repaint();
            }
        });
        
        panelStatusBar = new JPanel();
        panelStatusBar.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        frmNextvaliumManagementGui.getContentPane().add(panelStatusBar, BorderLayout.SOUTH);
        GridBagLayout gbl_panelStatusBar = new GridBagLayout();
        gbl_panelStatusBar.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_panelStatusBar.rowHeights = new int[] {0};
        gbl_panelStatusBar.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panelStatusBar.rowWeights = new double[]{0.0};
        panelStatusBar.setLayout(gbl_panelStatusBar);
        
        lblStatus = new JLabel("Alarmstatus:");
        lblStatus.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new HostileMissions().setVisible(true);
            }
        });
        GridBagConstraints gbc_lblStatus = new GridBagConstraints();
        gbc_lblStatus.anchor = GridBagConstraints.WEST;
        gbc_lblStatus.insets = new Insets(0, 5, 0, 5);
        gbc_lblStatus.gridx = 0;
        gbc_lblStatus.gridy = 0;
        panelStatusBar.add(lblStatus, gbc_lblStatus);
        
        txtStatus = new JLabel();
        txtStatus.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                new HostileMissions().setVisible(true);
            }
        });
        //txtStatus.setEditable(false);
        GridBagConstraints gbc_txtStatus = new GridBagConstraints();
        gbc_txtStatus.insets = new Insets(0, 0, 0, 5);
        gbc_txtStatus.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtStatus.gridx = 1;
        gbc_txtStatus.gridy = 0;
        panelStatusBar.add(txtStatus, gbc_txtStatus);
        //txtStatus.setColumns(10);
        
        lblGamedelay = new JLabel("Game delay:");
        GridBagConstraints gbc_lblGamedelay = new GridBagConstraints();
        gbc_lblGamedelay.insets = new Insets(1, 0, 1, 5);
        gbc_lblGamedelay.anchor = GridBagConstraints.WEST;
        gbc_lblGamedelay.gridx = 2;
        gbc_lblGamedelay.gridy = 0;
        panelStatusBar.add(lblGamedelay, gbc_lblGamedelay);
        
        textFieldGameDelay = new JTextField();
        textFieldGameDelay.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldGameDelay.setEditable(false);
        textFieldGameDelay.setEnabled(false);
        GridBagConstraints gbc_textFieldGameDelay = new GridBagConstraints();
        gbc_textFieldGameDelay.anchor = GridBagConstraints.EAST;
        gbc_textFieldGameDelay.insets = new Insets(0, 0, 1, 1);
        gbc_textFieldGameDelay.gridx = 3;
        gbc_textFieldGameDelay.gridy = 0;
        panelStatusBar.add(textFieldGameDelay, gbc_textFieldGameDelay);
        textFieldGameDelay.setColumns(8);
        
        lp = new JLayeredPane();
        lp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                panelUniverse.setBounds(((JLayeredPane)arg0.getComponent()).getVisibleRect());
                panelGalaxyMap.setBounds(((JLayeredPane)arg0.getComponent()).getVisibleRect());
            }
        });
        frmNextvaliumManagementGui.getContentPane().add(lp, BorderLayout.CENTER);
        lp.add(panelGalaxyMap, LAYER_GALAXYMAP);
        lp.add(panelUniverse, LAYER_UNIVERSE);
        
        frmNextvaliumManagementGui.setVisible(true);
        
        listUsers.forEach(user -> comboBoxUsers.addItem(user));
        comboBoxUniverseUser.addItem("");
        listUsers.forEach(user -> comboBoxUniverseUser.addItem(user));
        
        frmNextvaliumManagementGui.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        doInBackgroundGameDelay();
        doInBackgroundAlarm();
        showLastPlanet();
        
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        btnRefresh.doClick();
//                    }});
//                
//            }}).start();
     }

    public Planet getPlanetMarkedAsTarget() {
        return planetMarkedAsTarget;
    }

    public void setPlanetMarkedAsTarget(Planet planetMarkedAsTarget) {
        this.planetMarkedAsTarget = planetMarkedAsTarget;
        textFieldTargetUser.setText(planetMarkedAsTarget.getUserName());
        txtTargetplanet.setText(planetMarkedAsTarget.getName());
        frmNextvaliumManagementGui.repaint();
    }
    
    public void clearTarget() {
        planetMarkedAsTarget = null;
        textFieldTargetUser.setText(null);
        frmNextvaliumManagementGui.repaint();
    }

    public static NextValiumGui getNextValiumGui() {
        return nextValiumGui;
    }

    public JFrame getFrmNextvaliumManagementGui() {
        return frmNextvaliumManagementGui;
    }

    public String getSelectedUser() {
        return selectedUser;
    }
    
    public boolean isRadarOthersEnabled() {
        return chckbxRadarOthers.isSelected();
    }
    
    public boolean isRadarExplorationsEnabled() {
        return chckbxRadarExplorations.isSelected();
    }
    
    public boolean isRadarYamatosEnabled() {
        return chckbxYamatos.isSelected();
    }
    
    public void  setCenterPosition(Planet planet) {
        comboBoxUsers.setSelectedItem(planet.getUserName());
        comboBoxPlanets.setSelectedItem(planet.getName());
        btnRefresh.doClick();
    }
    
    public void  setCenterPosition(String userName, String planetName) {
        comboBoxUsers.setSelectedItem(userName);
        comboBoxPlanets.setSelectedItem(planetName);
        btnRefresh.doClick();
    }
    
    public void  setCenterPosition(int x, int y) {
        comboBoxUsers.setSelectedItem(null);
        comboBoxPlanets.setSelectedItem(null);
        textFieldPosX.setText(String.valueOf(x));
        textFieldPosY.setText(String.valueOf(y));
        btnRefresh.doClick();
    }
    
    private void actionPerformed_btnRefresh(ActionEvent e) {
        
        galaxyLoaded = true;
        
        frmNextvaliumManagementGui.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                Planets.clearAllPlanets();
                int x = Integer.parseInt(textFieldPosX.getText());
                int y = Integer.parseInt(textFieldPosY.getText());
                panelGalaxyMap.loadGalaxyMap(x, y);
                return null;
            }

            @Override
            protected void done() {
                tabbedPane.setSelectedIndex(1);
                frmNextvaliumManagementGui.repaint();
                showLastPlanet();
            }
        }.execute();
    }
    
    private void showLastPlanet(){
        
        new SwingWorker<Vector<Planet>, Object>() {

            @Override
            protected Vector<Planet> doInBackground() throws Exception {
                
                Vector<Planet> planets = new Vector<Planet>();
                for (String user : listUsers) {
                    Planet p = Planets.loadLastUserPlanet(user);
                    planets.add(p);
                }
                Collections.sort(planets, new Comparator<Planet>() {
                   @Override
                    public int compare(Planet arg0, Planet arg1) {
                        return arg0.getDate().before(arg1.getDate()) ? 1 : -1;
                    }});
                
                return planets;
            }

            @Override
            protected void done() {
                
                try {
                    Vector<Planet> planets = get();
                    lastPlanet = planets.get(0);
                    String date = Util.getDateAsString(lastPlanet.getDate());
                    String name = lastPlanet.getName();
                    String user = lastPlanet.getUserName();
                    String text = "<html><center>"+date+"<br>"+name+"<br>"+user+"</center></html>";
                    btnLastPlanet.setText(text);
                    
                } catch (InterruptedException | ExecutionException e) {
                }
                super.done();
            }
        }.execute();
    }
    
    private void doInBackgroundGameDelay() {
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                
                while (true) {
                    try {
                        JSONObject jsonState = Util.getJSONObjectFromApiCommand(Util.NEXTCOLONY_API_CMD_STATE);
                        int delay = jsonState.getInt("processing_delay_seconds");
                        int sec = delay % 60;
                        int min = (delay / 60)%60;
                        int hours = (delay/60)/60;
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                textFieldGameDelay.setText(String.format("%02d:%02d:%02d", hours, min, sec));
                            }});
                    } catch (JSONException | IOException e) {
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    private void doInBackgroundAlarm() {
        
        new Thread(new Runnable() {
            boolean alarm = false;
            @Override
            public void run() {
                
                while (true) {
                    try {
                        
                        alarm = false;
                        
                        for (String user : listUsers) {
                            
                            String apiCall = String.format(Util.NEXTCOLONY_API_CMD_MISSIONOVERVIEW, user);
                            JSONObject jsonMissions = Util.getJSONObjectFromApiCommand(apiCall);
                            if (jsonMissions.getInt("hostile_missions") > 0) {
                                alarm = true;
                                break;
                            }
                        }
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (alarm) {
                                    txtStatus.setForeground(Color.RED.darker());
                                    txtStatus.setText("Hostile mission");
                                }
                                else {
                                    txtStatus.setForeground(Color.GREEN.darker());
                                    txtStatus.setText("OK");
                                 }
                           }});
                        
                    } catch (JSONException | IOException e) {
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    public ArrayList<String> getListUsers() {
        return listUsers;
    }
    
    public String getSelectedUniverseUser() {
        return (String) comboBoxUniverseUser.getSelectedItem();
    }
}
