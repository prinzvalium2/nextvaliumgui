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
import java.awt.Insets;

public class NextValiumGui {

    private static final Logger LOGGER = LoggerFactory.getLogger(NextValiumGui.class);
    private static final String version = "0.0.1-SNAPSHOT";
    private static NextValiumGui nextValiumGui = null;
    private JFrame frmNextvaliumManagementGui;
    private JTextField textFieldPosX;
    private JTextField textFieldPosY;
    private JComboBox<String> comboBoxPlanets;
    private HashMap<String, Planet> mapPlanets;
    private Planet planetMarkedAsTarget = null;
    private JTextField textFieldMarkedAsTarget;
    private JButton btnClearTarget;
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
        
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(null);
        frmNextvaliumManagementGui.getContentPane().add(panel_1, BorderLayout.NORTH);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[]{26, 91, 0};
        gbl_panel_1.rowHeights = new int[]{23, 0};
        gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel_1.setLayout(gbl_panel_1);
        
        JPanel panelUserPlanet = new JPanel();
        panelUserPlanet.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Select user planet or x/y", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagConstraints gbc_panelUserPlanet = new GridBagConstraints();
        gbc_panelUserPlanet.fill = GridBagConstraints.BOTH;
        gbc_panelUserPlanet.gridx = 0;
        gbc_panelUserPlanet.gridy = 0;
        panel_1.add(panelUserPlanet, gbc_panelUserPlanet);
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
        
        JComboBox comboBoxUsers = new JComboBox();
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
        
        JButton btnRefresh = new JButton("Refresh");
        GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
        gbc_btnRefresh.insets = new Insets(0, 5, 5, 0);
        gbc_btnRefresh.anchor = GridBagConstraints.WEST;
        gbc_btnRefresh.gridx = 4;
        gbc_btnRefresh.gridy = 0;
        panelUserPlanet.add(btnRefresh, gbc_btnRefresh);
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Planets.clearAllPlanets();
                int x = Integer.parseInt(textFieldPosX.getText());
                int y = Integer.parseInt(textFieldPosY.getText());
                panelGalaxyMap.loadGalaxyMap(x, y);
                frmNextvaliumManagementGui.repaint();
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
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Right click on planet to mark as target", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 0;
        panel_1.add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{200, 0};
        gbl_panel.rowHeights = new int[]{23, 1, 0};
        gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        btnClearTarget = new JButton("Clear target");
        GridBagConstraints gbc_btnClearTarget = new GridBagConstraints();
        gbc_btnClearTarget.anchor = GridBagConstraints.EAST;
        gbc_btnClearTarget.gridx = 0;
        gbc_btnClearTarget.gridy = 1;
        panel.add(btnClearTarget, gbc_btnClearTarget);
        
        textFieldMarkedAsTarget = new JTextField();
        GridBagConstraints gbc_textFieldMarkedAsTarget = new GridBagConstraints();
        gbc_textFieldMarkedAsTarget.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldMarkedAsTarget.gridx = 0;
        gbc_textFieldMarkedAsTarget.gridy = 0;
        panel.add(textFieldMarkedAsTarget, gbc_textFieldMarkedAsTarget);
        textFieldMarkedAsTarget.setEditable(false);
        textFieldMarkedAsTarget.setColumns(10);
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
}
