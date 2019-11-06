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
import java.util.HashMap;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.gui.PanelGalaxyMap;
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

public class NextValiumGui {

    private static final Logger LOGGER = LoggerFactory.getLogger(NextValiumGui.class);
    private static final String version = "0.1.2-SNAPSHOT";
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
    private JTextField textFieldMarkedAsTarget;
    private JButton btnClearTarget;
    private JButton btnRefresh;
    private ArrayList<String> listUsers;
    private String selectedUser = null;

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
                    nextValiumGui = new NextValiumGui();
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
        
        PanelGalaxyMap panelGalaxyMap = new PanelGalaxyMap();
        
        frmNextvaliumManagementGui = new JFrame();
        frmNextvaliumManagementGui.setTitle("NextValium GUI " + version + " - Multi user management GUI for NextColony");
        frmNextvaliumManagementGui.setBounds(10, 10, 1250, 900);
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
        
        JPanel panelTop = new JPanel();
        panelTop.setBorder(null);
        frmNextvaliumManagementGui.getContentPane().add(panelTop, BorderLayout.NORTH);
        GridBagLayout gbl_panelTop = new GridBagLayout();
        gbl_panelTop.columnWidths = new int[]{26, 91, 0, 0};
        gbl_panelTop.rowHeights = new int[]{23, 0};
        gbl_panelTop.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panelTop.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panelTop.setLayout(gbl_panelTop);
        
        JPanel panelUserPlanet = new JPanel();
        panelUserPlanet.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Select user planet or x/y", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagConstraints gbc_panelUserPlanet = new GridBagConstraints();
        gbc_panelUserPlanet.insets = new Insets(0, 0, 0, 5);
        gbc_panelUserPlanet.fill = GridBagConstraints.BOTH;
        gbc_panelUserPlanet.gridx = 0;
        gbc_panelUserPlanet.gridy = 0;
        panelTop.add(panelUserPlanet, gbc_panelUserPlanet);
        GridBagLayout gbl_panelUserPlanet = new GridBagLayout();
        gbl_panelUserPlanet.columnWidths = new int[]{26, 86, 0, 0, 0, 0};
        gbl_panelUserPlanet.rowHeights = new int[]{23, 1, 0};
        gbl_panelUserPlanet.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
        GridBagConstraints gbc_textFieldPosX = new GridBagConstraints();
        gbc_textFieldPosX.insets = new Insets(0, 2, 5, 5);
        gbc_textFieldPosX.anchor = GridBagConstraints.WEST;
        gbc_textFieldPosX.gridx = 3;
        gbc_textFieldPosX.gridy = 0;
        panelUserPlanet.add(textFieldPosX, gbc_textFieldPosX);
        textFieldPosX.setColumns(4);
        textFieldPosX.setText("0");
        
        btnRefresh = new JButton("Refresh");
        GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
        gbc_btnRefresh.insets = new Insets(0, 5, 5, 0);
        gbc_btnRefresh.anchor = GridBagConstraints.WEST;
        gbc_btnRefresh.gridx = 4;
        gbc_btnRefresh.gridy = 0;
        panelUserPlanet.add(btnRefresh, gbc_btnRefresh);
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                        frmNextvaliumManagementGui.repaint();
                    }
                }.execute();
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
        GridBagConstraints gbc_textFieldPosY = new GridBagConstraints();
        gbc_textFieldPosY.insets = new Insets(0, 2, 0, 5);
        gbc_textFieldPosY.anchor = GridBagConstraints.WEST;
        gbc_textFieldPosY.gridx = 3;
        gbc_textFieldPosY.gridy = 1;
        panelUserPlanet.add(textFieldPosY, gbc_textFieldPosY);
        textFieldPosY.setText("0");
        textFieldPosY.setColumns(4);
        
        JPanel panelTarget = new JPanel();
        panelTarget.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Right click on planet to mark as target", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagConstraints gbc_panelTarget = new GridBagConstraints();
        gbc_panelTarget.insets = new Insets(0, 0, 0, 5);
        gbc_panelTarget.fill = GridBagConstraints.BOTH;
        gbc_panelTarget.gridx = 1;
        gbc_panelTarget.gridy = 0;
        panelTop.add(panelTarget, gbc_panelTarget);
        GridBagLayout gbl_panelTarget = new GridBagLayout();
        gbl_panelTarget.columnWidths = new int[]{200, 0};
        gbl_panelTarget.rowHeights = new int[]{23, 1, 0};
        gbl_panelTarget.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_panelTarget.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panelTarget.setLayout(gbl_panelTarget);
        
        btnClearTarget = new JButton("Clear target");
        GridBagConstraints gbc_btnClearTarget = new GridBagConstraints();
        gbc_btnClearTarget.anchor = GridBagConstraints.EAST;
        gbc_btnClearTarget.gridx = 0;
        gbc_btnClearTarget.gridy = 1;
        panelTarget.add(btnClearTarget, gbc_btnClearTarget);
        
        textFieldMarkedAsTarget = new JTextField();
        GridBagConstraints gbc_textFieldMarkedAsTarget = new GridBagConstraints();
        gbc_textFieldMarkedAsTarget.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldMarkedAsTarget.gridx = 0;
        gbc_textFieldMarkedAsTarget.gridy = 0;
        panelTarget.add(textFieldMarkedAsTarget, gbc_textFieldMarkedAsTarget);
        textFieldMarkedAsTarget.setEditable(false);
        textFieldMarkedAsTarget.setColumns(10);
        
        JPanel panelRadar = new JPanel();
        panelRadar.setBorder(new TitledBorder(null, "Flight radar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_panelRadar = new GridBagConstraints();
        gbc_panelRadar.anchor = GridBagConstraints.WEST;
        gbc_panelRadar.fill = GridBagConstraints.VERTICAL;
        gbc_panelRadar.gridx = 2;
        gbc_panelRadar.gridy = 0;
        panelTop.add(panelRadar, gbc_panelRadar);
        GridBagLayout gbl_panelRadar = new GridBagLayout();
        gbl_panelRadar.columnWidths = new int[]{0, 0};
        gbl_panelRadar.rowHeights = new int[]{0, 0, 0};
        gbl_panelRadar.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panelRadar.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        panelRadar.setLayout(gbl_panelRadar);
        
        chckbxRadarExplorations = new JCheckBox("Explorations");
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
        chckbxRadarOthers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmNextvaliumManagementGui.repaint();
            }
        });
        chckbxRadarOthers.setSelected(true);
        GridBagConstraints gbc_chckbxRadarOthers = new GridBagConstraints();
        gbc_chckbxRadarOthers.anchor = GridBagConstraints.NORTHWEST;
        gbc_chckbxRadarOthers.gridx = 0;
        gbc_chckbxRadarOthers.gridy = 1;
        panelRadar.add(chckbxRadarOthers, gbc_chckbxRadarOthers);
        btnClearTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                planetMarkedAsTarget = null;
                textFieldMarkedAsTarget.setText(null);
                frmNextvaliumManagementGui.repaint();
            }
        });
        
        frmNextvaliumManagementGui.getContentPane().add(panelGalaxyMap, BorderLayout.CENTER); // $hide$
        frmNextvaliumManagementGui.setVisible(true);
        
        listUsers.forEach(user -> comboBoxUsers.addItem(user));
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        btnRefresh.doClick();
                    }});
                
            }}).start();
     }

    public Planet getPlanetMarkedAsTarget() {
        return planetMarkedAsTarget;
    }

    public void setPlanetMarkedAsTarget(Planet planetMarkedAsTarget) {
        this.planetMarkedAsTarget = planetMarkedAsTarget;
        textFieldMarkedAsTarget.setText(planetMarkedAsTarget.getUserName() + " / " + planetMarkedAsTarget.getName());
        frmNextvaliumManagementGui.repaint();
    }
    
    public void clearTarget() {
        planetMarkedAsTarget = null;
        textFieldMarkedAsTarget.setText(null);
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
    
    public void  setCenterPosition(Planet planet) {
        comboBoxUsers.setSelectedItem(planet.getUserName());
        comboBoxPlanets.setSelectedItem(planet.getName());
        btnRefresh.doClick();
    }
}
