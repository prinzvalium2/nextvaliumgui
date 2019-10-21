package de.prinzvalium.nextvaliumgui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prinzvalium.nextvaliumgui.gui.PanelGalaxyMap;
import de.prinzvalium.nextvaliumgui.lib.Util;

import javax.swing.JButton;

public class NextValiumGui {

    private static final Logger LOGGER = LoggerFactory.getLogger(NextValiumGui.class);
    private JFrame frmNextvaliumManagementGui;
    private JTextField textFieldPosX;
    private JTextField textFieldPosY;
    private PanelGalaxyMap panelGalaxyMap;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NextValiumGui window = new NextValiumGui();
                    window.panelGalaxyMap.loadGalaxyMap(0, 0); 
                    window.frmNextvaliumManagementGui.setVisible(true);
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
    public NextValiumGui() throws FileNotFoundException, IOException {
        LOGGER.trace("");
        LOGGER.trace("---------------");
        LOGGER.trace("NextValiumGui()");
        LOGGER.trace("---------------");
        
        // Read user and keys from ini-file. User skills are loaded
        HashMap<String, String> mapUserAndKey = new HashMap<String, String>();
        Util.loadProperties(mapUserAndKey);
        
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        LOGGER.trace("initialize()");
        
        frmNextvaliumManagementGui = new JFrame();
        frmNextvaliumManagementGui.setTitle("NextValium GUI - Management GUI for NextColony");
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
        frmNextvaliumManagementGui.getContentPane().add(panel_1, BorderLayout.NORTH);
        
        JLabel lblMapPosition = new JLabel("Map position - X:");
        panel_1.add(lblMapPosition);
        
        textFieldPosX = new JTextField();
        textFieldPosX.setText("0");
        panel_1.add(textFieldPosX);
        textFieldPosX.setColumns(10);
        
        JLabel lblMapPositionY = new JLabel("Y:");
        panel_1.add(lblMapPositionY);
        
        textFieldPosY = new JTextField();
        textFieldPosY.setText("0");
        panel_1.add(textFieldPosY);
        textFieldPosY.setColumns(10);
        
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
        
        panelGalaxyMap = new PanelGalaxyMap();
        frmNextvaliumManagementGui.getContentPane().add(panelGalaxyMap, BorderLayout.CENTER);
    }
}
