package de.prinzvalium.nextvaliumgui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
import java.awt.Component;
import javax.swing.Box;
import java.awt.FlowLayout;

public class NextValiumGui {

    private static final Logger LOGGER = LoggerFactory.getLogger(NextValiumGui.class);
    private JFrame frmNextvaliumManagementGui;
    private JTextField textFieldPosX;
    private JTextField textFieldPosY;
    private PanelGalaxyMap panelGalaxyMap;
    private JTextField textFieldUserName;
    private JComboBox<String> comboBoxPlanets;
    private HashMap<String, Planet> mapPlanets;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
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
        LOGGER.trace("");
        LOGGER.trace("---------------");
        LOGGER.trace("NextValiumGui()");
        LOGGER.trace("---------------");
        
         // Read user and keys from ini-file. User skills are loaded
        HashMap<String, String> mapUserAndKey = new HashMap<String, String>();
        try {
            File f = new File("nextvalium.ini");
            if (!f.exists())
                f.createNewFile();
            
            Util.loadProperties(mapUserAndKey);
            
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
        
        frmNextvaliumManagementGui = new JFrame();
        frmNextvaliumManagementGui.setTitle("NextValium GUI - Management GUI for NextColony");
        frmNextvaliumManagementGui.setBounds(10, 10, 1250, 880);
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
        frmNextvaliumManagementGui.getContentPane().add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        Component horizontalStrut_2 = Box.createHorizontalStrut(20);
        panel_1.add(horizontalStrut_2);
        
        JLabel lblUserName = new JLabel("User:");
        panel_1.add(lblUserName);
        
        textFieldUserName = new JTextField();
        textFieldUserName.setMinimumSize(new Dimension(150, 20));
        textFieldUserName.setPreferredSize(new Dimension(150, 20));
        textFieldUserName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> list = new ArrayList<String>();
                    mapPlanets = Planets.loadUserPlanets(textFieldUserName.getText());
                    mapPlanets.forEach((planetId, planet) -> list.add(planet.getName()));
                    Collections.sort(list);
                    comboBoxPlanets.removeAllItems();
                    list.forEach(planetName -> comboBoxPlanets.addItem(planetName));
                    
                } catch (JSONException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        panel_1.add(textFieldUserName);
        textFieldUserName.setColumns(10);
        
        JLabel lblPlanet = new JLabel("Planet:");
        panel_1.add(lblPlanet);
        
        comboBoxPlanets = new JComboBox<String>();
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
        panel_1.add(comboBoxPlanets);
        
        Component horizontalStrut_1 = Box.createHorizontalStrut(20);
        panel_1.add(horizontalStrut_1);
        
        JLabel lblMapPosition = new JLabel("Map position - X:");
        panel_1.add(lblMapPosition);
        
        textFieldPosX = new JTextField();
        textFieldPosX.setText("0");
        panel_1.add(textFieldPosX);
        textFieldPosX.setColumns(4);
        
        JLabel lblMapPositionY = new JLabel("Y:");
        panel_1.add(lblMapPositionY);
        
        textFieldPosY = new JTextField();
        textFieldPosY.setText("0");
        panel_1.add(textFieldPosY);
        textFieldPosY.setColumns(4);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int x = Integer.parseInt(textFieldPosX.getText());
                int y = Integer.parseInt(textFieldPosY.getText());
                panelGalaxyMap.loadGalaxyMap(x, y);
                frmNextvaliumManagementGui.repaint();
            }
        });
        panel_1.add(btnRefresh);
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        horizontalStrut.setPreferredSize(new Dimension(1000, 0));
        panel_1.add(horizontalStrut);
        
        panelGalaxyMap = new PanelGalaxyMap();
        frmNextvaliumManagementGui.getContentPane().add(panelGalaxyMap, BorderLayout.CENTER);
        
        frmNextvaliumManagementGui.setVisible(true);
    }
}
